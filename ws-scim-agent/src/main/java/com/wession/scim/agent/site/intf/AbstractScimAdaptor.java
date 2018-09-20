package com.wession.scim.agent.site.intf;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import com.wession.common.RESTClient;
import com.wession.common.ScimUtils;
import com.wession.common.WessionLog;
import com.wession.scim.Const;
import com.wession.scim.controller.ServiceProviderConfig;
import com.wession.scim.exception.ScimAuthException;
import com.wession.scim.exception.ScimException;
import com.wession.scim.intf.schemas_name;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.slf4j.Logger;

public abstract class AbstractScimAdaptor implements schemas_name  {
	private ServiceProviderConfig conf = ServiceProviderConfig.getInstance();
	
	protected RESTClient client;
	protected String URL;
	protected String Resource;
	protected String params;
	protected String appName; 
	
	private static Logger systemLog;
	private static Logger processLog;
	private static Logger provisionLog;
	private static Logger schedulerLog;
	
	private static WessionLog wlog = new WessionLog();
	
	protected JSONArray groups; 
	
	public AbstractScimAdaptor(String version) {
		JSONObject server = conf.getServerConfig();
		JSONObject sync = (JSONObject) conf.getDBConfig().get("sync");
		
		client = new RESTClient();
		URL = server.getAsString("baseURL") + version;
		Resource = "Users";
		params = "attributes=externalId";
		appName = sync.getAsString("app-name");
System.out.println("AbstractScimAdaptor appName=="+appName);		
		// Log Setting
		systemLog = wlog.getSystemLog();
		processLog = wlog.getProcessLog();
		provisionLog = wlog.getProvisionLog();
		schedulerLog = wlog.getScheduleLog();
		
		setGroups();
	}
	
	public static void writeLogger(String loggerName, String loggerLevel, String loggerData) {
		Logger logWrap = null;
		
		if ("system".equals(loggerName)) {
			logWrap = systemLog;
		} else if ("provision".equals(loggerName)) {
			logWrap = provisionLog;
		} else if ("process".equals(loggerName)) {
			logWrap = processLog;
		} else if ("scheduler".equals(loggerName)) {
			logWrap = schedulerLog;
		} else {
			logWrap = processLog;
		}
		
		if ("info".equals(loggerLevel)) {
			logWrap.info(loggerData);
		} else if ("debug".equals(loggerLevel)){
			logWrap.debug(loggerData);
		} else if ("warn".equals(loggerLevel)) {
			logWrap.warn(loggerData);
		} else if ("error".equals(loggerLevel)) {
			logWrap.error(loggerData);
		} else if ("trace".equals(loggerLevel)) {
			logWrap.trace(loggerData);
		} else {
			logWrap.debug(loggerLevel);
		}
		
	}
	
	public JSONObject Sync() {
		return new JSONObject();
	}
	
	protected Iterator getMembers(JSONObject jsonGroup) {
		JSONObject group_bo_json = client.get(URL + "/Groups/" + jsonGroup.getAsString("value"));
		JSONArray group_members = (JSONArray) group_bo_json.get("members");
		if (group_members == null) group_bo_json.put("members", new ArrayList());
		
		return group_members.iterator();
	}
	
	protected JSONObject getGroup(String groupName) {
		Iterator itor = groups.iterator();
		while (itor.hasNext()) {
			JSONObject group = (JSONObject) itor.next();
			String group_name = group.getAsString("display");
			if (groupName.equals(group_name)) {
				return group;
			}
		}
		return null;
	}
	
	private void setGroups() {
		setGroups(null);
	}
	
	private void setGroups(String attrs) {
		groups = new JSONArray();
		String [] attr = null;
		if (attrs != null) {
			attr = attrs.split(",");
			attrs = "," + attrs;
		}
		try {
			
			JSONObject gs = client.get(URL + "/Groups?attributes=displayName"+attrs);
			if (!ScimUtils.checkSchemas((JSONArray)gs.get("schemas"), Const.schemas_v20_list_response)) {
				throw new ScimException("No Groups");
			}
			JSONArray resources = (JSONArray) gs.get("Resources");
			Iterator itor = resources.iterator();
			while (itor.hasNext()) {
				JSONObject g = (JSONObject) itor.next();
				JSONObject group = new JSONObject();
				
				group.put("display", g.getAsString(_group_name_display));
				group.put("value", g.getAsString(_group_id));
				group.put("$ref", URL + "/Groups/" + g.getAsString(_group_id));
				
				if (attr != null) {
					for (int i=0; i<attr.length; i++) {
						if (attr[i] !=null && !"".equals(attr[i].trim())) {
							group.put(attr[i], g.getAsString(attr[i]));
						}
					}
				}
				
				groups.add(group);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// For Conciliation
	
	protected JSONObject setGroupFromSCIM(String displayName) {
		String param = "displayName eq \"" + displayName + "\"";
		JSONObject resp = client.get(URL+"/Groups?filter=" + URLEncoder.encode(param));
		JSONArray resources = (JSONArray) resp.get("Resources");
		if (resources == null) return null;
		
		Iterator itor = resources.iterator();
		String id = null;
		while (itor.hasNext()) {
			JSONObject group_info = (JSONObject) itor.next();
			id = group_info.getAsString("id");
		}

		if (id == null) return null;
		
		JSONObject ret = new JSONObject();
		ret.put("value", id);
		ret.put("display", displayName);
		ret.put("$ref", URL+"/Groups/" + id);
		
		return ret;
		
	}
	
	protected ArrayList<String> getInsectAccount(ArrayList<String> bean_user_list, ArrayList<String> scim_user_list) {
		ArrayList <String> arry = new ArrayList <String> ();
		Iterator <String> itor = bean_user_list.iterator();
		while (itor.hasNext()) {
			String bean_user_id = itor.next();
			if (scim_user_list.contains(bean_user_id))
				arry.add(bean_user_id);
		}
		return arry.size()>0?arry:null;
	}
	
	protected ArrayList<String> getNewAccount(ArrayList <String> bean_user_list, ArrayList <String> scim_user_list) {
		return notInSecond(bean_user_list, scim_user_list);
	}
	
	protected ArrayList<String> getRemoveAccount(ArrayList <String> bean_user_list, ArrayList <String> scim_user_list) {
		return notInSecond(scim_user_list, bean_user_list);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> notInSecond(ArrayList <String> first, ArrayList <String> second) {
		if (first == null) return null;
		if (second == null) {
			try {
				return (ArrayList<String>) ScimUtils.deepCopy(first);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		ArrayList<String> arry = new ArrayList<String> ();
		Iterator<String> itor = first.iterator();
		while (itor.hasNext()) {
			String first_id = itor.next();
			if (!second.contains(first_id)) arry.add(first_id);
		}
		return arry;
	}
	
	protected User getAccountToSCIM(String userId) {
		// REST Client를 이용하여 WessionIM 서버에서 User 객체를 호출함
		String param = URLEncoder.encode("externalId eq \"" + userId +"\"");
		JSONObject respObj = client.get(URL + "/" + Resource + "?filter="+param);
		JSONArray resources = (JSONArray) respObj.get("Resources");
		Iterator<Object> itor = resources.iterator();
		JSONObject json = new JSONObject();
		while (itor.hasNext()) {
			json = (JSONObject) itor.next(); // 1개만 오는 것이 정상
		}
		return new User(json);
	}
	
	protected ArrayList<String> getAccountListToSCIM() throws ScimAuthException {
		// REST Client를 이용하여 WessionIM 서버에서 전체 User 목록을 요청함
		ArrayList <String> userList = new ArrayList <String> ();
		
		int total = 100;
		int startIndex = 1;
		int itemsPerPage = 0;
		int endIndex = 0;
		String params = "attributes=externalId";
		
		while (total > endIndex) {
			//TODO 기본적으로 정상인증이 되어있는지에 대한 확인이 필요함
			JSONObject response = client.get(URL + "/" + Resource + "?" + params + "&startIndex=" + startIndex);

			if (ScimUtils.checkSchemas((JSONArray) response.get("schemas"), Const.schemas_v20_error)) {
				throw new ScimAuthException(401, response.getAsString("detail"));
			}
			
			total = (int) response.getAsNumber("totalResults");
			startIndex = (int) response.getAsNumber("startIndex");
			itemsPerPage = (int) response.getAsNumber("itemsPerPage");
			endIndex = startIndex + itemsPerPage;
			JSONArray resources = (JSONArray) response.get("Resources");

			Iterator<Object> itor_users =  resources.iterator();
			while (itor_users.hasNext()) {
				JSONObject resource = (JSONObject) itor_users.next();
				String externalId = resource.getAsString("externalId");
				userList.add(externalId);
			}
			
			startIndex = endIndex;
		}
		return userList;
	}
}
