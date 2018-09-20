package com.wession.scim.controller;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import com.wession.common.ScimUtils;
import com.wession.scim.AttrEquation;
import com.wession.scim.LogicalEquation;
import com.wession.scim.exception.ScimFilterException;
import com.wession.scim.simple.MemRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Filter {
	private static boolean debug = false;
	public Filter() {

	}

	public ArrayList<LogicalEquation> parse(String filter_param) throws ScimFilterException {
		ArrayList<LogicalEquation> lstEquations = new ArrayList<LogicalEquation>(); // 처리순서 및 결과
		ArrayList<AttrEquation> lstAttrEquations = new ArrayList<AttrEquation>(); // 파싱내용을 Equation으로 변경
		ArrayList<filter_parts> token_array = new ArrayList<filter_parts>(); // parameter 파싱처리용
		
		token_array = getTokenArray(filter_param);
		lstAttrEquations = getEquations(token_array);
		lstEquations = setArrange(token_array, lstAttrEquations);
		
		return lstEquations;
	}
	
	private Iterator getIterator(String type) {
		MemRepository repo = MemRepository.getInstance();
		Iterator itor = null;
		if ("Users".equals(type)) {
			itor = repo.iterator(type);
		} else if ("Groups".equals(type)) {
			itor = repo.iterator(type);
		}
		return itor;
	}
	
	public JSONArray doFilter(String params, String resources) throws ScimFilterException {
		Filter filter = new Filter();
		ArrayList<LogicalEquation> lstEquations = new ArrayList<LogicalEquation>(); // 처리순서 및 결과
		lstEquations = filter.parse(params);
		
		int maxDepth = 0;
		for (int i=0; i<lstEquations.size(); i++) {
			LogicalEquation leq = lstEquations.get(i);
			if (maxDepth < leq.getPriority()) maxDepth = leq.getPriority();
		}
		
		for (int i = 0; i < lstEquations.size(); i++) {
			LogicalEquation leq = lstEquations.get(i);
			ArrayList<AttrEquation> lstAttrEq = leq.getEquations();
			for (int idx = 0; idx < lstAttrEq.size(); idx++) {
				AttrEquation ae = lstAttrEq.get(idx);
				Iterator itor = filter.getIterator(resources);
				operate(ae, itor);
				
				if (idx == 0) {
					if (ae.getLogical_and_or() != null) leq.setOperator(ae.getLogical_and_or());
					leq.setResult(ae.getResult());
					
				} else if (ae.getLogical_and_or() != null && idx>0) {
					if ("and".equals(ae.getLogical_and_or())) {
						JSONArray pre = leq.getResult();
						JSONArray now = ae.getResult();
						JSONArray res = new JSONArray();
						
						Iterator now_itor = now.iterator();
						int now_count = 0;
						while (now_itor.hasNext()) {
							JSONObject object = (JSONObject) now_itor.next();
							String now_id = object.getAsString("id");
							Iterator pre_itor = pre.iterator();
							boolean has = false; 
							while (pre_itor.hasNext()) {
								JSONObject pre_object = (JSONObject) pre_itor.next();
								String pre_id = pre_object.getAsString("id");
								if (pre_id.equals(now_id)) has = true;
							}
							if (has) {
								res.add(object);
							} 
						}
						leq.setResult(res);
					} else if ("or".equals(ae.getLogical_and_or())) {
						JSONArray pre = leq.getResult();
						JSONArray now = ae.getResult();
						
						Iterator now_itor = now.iterator();
						int now_count = 0;
						while (now_itor.hasNext()) {
							JSONObject object = (JSONObject) now_itor.next();
							String now_id = object.getAsString("id");
							Iterator pre_itor = pre.iterator();
							boolean has = false; 
							while (pre_itor.hasNext()) {
								JSONObject pre_object = (JSONObject) pre_itor.next();
								String pre_id = pre_object.getAsString("id");
								if (pre_id.equals(now_id)) has = true;
							}
							if (!has) {
								pre.add(object);
							} 
						}
					}
				}
			}
		}

		// 다시한번 계산처리 
		LogicalEquation pre_leq = null; 
		int lastIdx = 0;
		
		for (int ord = maxDepth; ord>0; ord--) {
			for (int i = 0; i < lstEquations.size(); i++) {
				LogicalEquation leq = lstEquations.get(i);
				if (leq.getPriority() == ord) {
					if (pre_leq == null) {
						pre_leq = leq;
					} else {
						if (leq.getEquationCount() == 0) {
							leq.setResult(pre_leq.getResult());
							lastIdx = i;
						} else {
							if ("and".equals(leq.getOperator())) {
								// pre_leq와 and 조건 처리
								leq.setResult(join(pre_leq.getResult(), leq.getResult(), "and"));
								leq.setOperator(pre_leq.getOperator());
							} else if ("or".equals(leq.getOperator())) {
								// pre_leq와 or 조건 처리
								leq.setResult(join(pre_leq.getResult(), leq.getResult(), "or"));
								leq.setOperator(pre_leq.getOperator());
							}
							pre_leq = leq;
						}
					}
				}
			}
		}
		
		return lstEquations.get(lastIdx).getResult();
	}

	public static void main(String[] args) throws Exception {
		Filter filter = new Filter();
//		 String params = "(emails[type eq \"work\" and value ew \"굳\"] and (display co \"잡\" or not(display eq \"미숙\")) ) or ((title eq \"과장\" or title eq \"차장\" or title eq \"부장\") and age gt 38)";
//		 String params = "emails[type eq \"work\" and value ew \"wowsanta.com\"] and userName eq \"강미숙\" or userName co \"창신\"";
//		String params = "(emails[type eq \"work\" and value co \"wowsanta.com\"]) or (userName co \"미숙\")";
		String params = "(externalId co \"736\") and (displayName eq \"정창섭\")";
//		 String params = "not(emails pr)";
//		String params = "userName co \"미숙\"";
//		 String params = "groups[display eq \"데마시아\"]";
		
		String params2 = "displayName co \"데마시아\"";
		
		MemRepository repo = MemRepository.getInstance();
		repo.load();
		
		System.out.println(filter.doFilter(params, "Users").toJSONString());
		System.out.println(filter.doFilter(params2, "Groups").toJSONString());

		/*
		ArrayList<LogicalEquation> lstEquations = new ArrayList<LogicalEquation>(); // 처리순서 및 결과
		lstEquations = filter.parse(params);

// 		실제계산에 돌입
		int maxDepth = 0;
		for (int i=0; i<lstEquations.size(); i++) {
			LogicalEquation leq = lstEquations.get(i);
			if (maxDepth < leq.getPriority()) maxDepth = leq.getPriority();
		}
		System.out.println("maxDepth : " + maxDepth);

		// 먼저 단위별 계산처리 함
		for (int i = 0; i < lstEquations.size(); i++) {
			LogicalEquation leq = lstEquations.get(i);
			ArrayList<AttrEquation> lstAttrEq = leq.getEquations();
			for (int idx = 0; idx < lstAttrEq.size(); idx++) {
				AttrEquation ae = lstAttrEq.get(idx);
				Iterator itor = filter.getIterator("Users");
				operate(ae, itor);
				
				if (idx == 0) {
					if (ae.getLogical_and_or() != null) leq.setOperator(ae.getLogical_and_or());
					leq.setResult(ae.getResult());
					
				} else if (ae.getLogical_and_or() != null && idx>0) {
					if ("and".equals(ae.getLogical_and_or())) {
						JSONArray pre = leq.getResult();
						JSONArray now = ae.getResult();
						JSONArray res = new JSONArray();
						
						Iterator now_itor = now.iterator();
						int now_count = 0;
						while (now_itor.hasNext()) {
							JSONObject object = (JSONObject) now_itor.next();
							String now_id = object.getAsString("id");
							Iterator pre_itor = pre.iterator();
							boolean has = false; 
							while (pre_itor.hasNext()) {
								JSONObject pre_object = (JSONObject) pre_itor.next();
								String pre_id = pre_object.getAsString("id");
								if (pre_id.equals(now_id)) has = true;
							}
							if (has) {
								res.add(object);
							} 
						}
						leq.setResult(res);
					} else if ("or".equals(ae.getLogical_and_or())) {
						JSONArray pre = leq.getResult();
						JSONArray now = ae.getResult();
						
						Iterator now_itor = now.iterator();
						int now_count = 0;
						while (now_itor.hasNext()) {
							JSONObject object = (JSONObject) now_itor.next();
							String now_id = object.getAsString("id");
							Iterator pre_itor = pre.iterator();
							boolean has = false; 
							while (pre_itor.hasNext()) {
								JSONObject pre_object = (JSONObject) pre_itor.next();
								String pre_id = pre_object.getAsString("id");
								if (pre_id.equals(now_id)) has = true;
							}
							if (!has) {
								pre.add(object);
							} 
						}
					}
				}
			}
		}

		// 다시한번 계산처리 
		LogicalEquation pre_leq = null; 
		int lastIdx = 0;
		
		for (int ord = maxDepth; ord>0; ord--) {
			for (int i = 0; i < lstEquations.size(); i++) {
				LogicalEquation leq = lstEquations.get(i);
				if (leq.getPriority() == ord) {
					if (pre_leq == null) {
						pre_leq = leq;
					} else {
						if (leq.getEquationCount() == 0) {
							leq.setResult(pre_leq.getResult());
							lastIdx = i;
						} else {
							if ("and".equals(leq.getOperator())) {
								// pre_leq와 and 조건 처리
								System.out.println(pre_leq.getResult().size() + " - and - " + leq.getResult().size());
								leq.setResult(join(pre_leq.getResult(), leq.getResult(), "and"));
								leq.setOperator(pre_leq.getOperator());
							} else if ("or".equals(leq.getOperator())) {
								// pre_leq와 or 조건 처리
								System.out.println(pre_leq.getResult().size() + " - or - " + leq.getResult().size());
								leq.setResult(join(pre_leq.getResult(), leq.getResult(), "or"));
								leq.setOperator(pre_leq.getOperator());
							}
							pre_leq = leq;
						}
					}

					
					
					System.out.print("priority[" + leq.getPriority() + "], count[" + leq.getEquationCount() + "] " + leq.getOperator() + " ");
					System.out.println("index : " + i + ",  result size : " + leq.getResult().size());
					for (AttrEquation attrEq:  leq.getEquations())
						System.out.println(attrEq.getLogical_and_or() + "/" + attrEq.getLogical_not() + "/" + attrEq.getAttrName());
				}
			}
		}
		
		JSONArray result = lstEquations.get(lastIdx).getResult();
		System.out.println(result.toJSONString());

		*/
		
		System.out.println("{" + params + "}");
	}

	private ArrayList<LogicalEquation> setArrange(ArrayList<filter_parts> token_array, ArrayList<AttrEquation> lstAttrEquations) {
		ArrayList<LogicalEquation> array = new ArrayList<LogicalEquation>();
		int depth = 0;
		int startPoint = 0;
		if (debug) System.out.println("token_array.size() : " + token_array.size());
		for (int idx=0; idx<token_array.size(); idx++) {
			filter_parts part = token_array.get(idx);
			if (part.type == 40 || part.type == 91 ) {
				if (debug) System.out.println(showDepth(depth) + part.data);
				depth++;
			} else if (part.type == 41 || part.type == 93 ) {
				LogicalEquation leq = new LogicalEquation(depth);
				leq.setEquations(insertEquation(startPoint, idx, lstAttrEquations));
				array.add(leq);
				if (array.size()>0) startPoint = idx;
				String aa = insertEquationString(startPoint, idx, lstAttrEquations);
				if (debug) System.out.print(showDepth(depth));
				if (aa != null) {
					if (debug) System.out.print(aa);
				}
				if (debug) System.out.println(" " + part.data + "    " + depth + "  / " + leq.getEquationCount());
				depth--;
			}
		}
		return array;
	}

	private ArrayList<AttrEquation> getEquations(ArrayList<filter_parts> token_array) throws ScimFilterException {
		ArrayList<AttrEquation> array = new ArrayList<AttrEquation> ();
		
		if (token_array.size() > 1) {
			int start_point = 0;
			String parentAttrName = null;
			while (true) {
				AttrEquation attrEq = getEquation(start_point, parentAttrName, token_array);
				if (attrEq != null) {
					array.add(attrEq);
					start_point = attrEq.getStartPoint();
					parentAttrName = attrEq.getParentAttrName();
					if (debug) System.out.println(attrEq.getStartPoint() + " : "  + attrEq.getLogical_and_or() + " " + attrEq.getLogical_not() + " " + attrEq.toString());
				} else {
					break;
				}
			}
		} else {
			// parsing error
		}
		return array;
	}

	class equation_parts {
		public int array_start;
		public int array_end;
		public String eq;
	}

	class filter_parts {
		public int type;
		public boolean isAtOp;
		public boolean isLgOp;
		public boolean isGpOp;
		public String data;
	}

	class equation {
		public String attrname;
		public String op;
		public String value;
	}
	
	private String showDepth(int depth) {
		String ret = "";
		for (int i=0; i<depth*2; i++) ret = ret + " ";
		return ret;
	}

	private ArrayList<filter_parts> getTokenArray(String params) {
		ArrayList<filter_parts> token_array = new ArrayList<filter_parts>();
		
		StreamTokenizer st = new StreamTokenizer(new StringReader("(" + params + ")"));

		try {
			while (st.nextToken() != st.TT_EOF) {
				filter_parts parts = new filter_parts();
				parts.type = st.ttype;

				if (st.ttype == st.TT_NUMBER) {
					if (debug) System.out.println("TT_NUMBER : " + st.nval);
					parts.data = Double.toString(st.nval);
				} else if (st.ttype == st.TT_WORD) {
					if (debug) System.out.println("TT_WORD : " + st.sval);
					parts.data = st.sval;
				} else {
					if (debug) System.out.print(st.ttype);
					parts.data = st.sval;
					parts.isGpOp = true;
					switch (st.ttype) {
					case 40:
						parts.data = "(";
						if (debug) System.out.println("(");
						break;
					case 41:
						parts.data = ")";
						if (debug) System.out.println(")");
						break;
					case 91:
						parts.data = "[";
						if (debug) System.out.println("[");
						break;
					case 93:
						parts.data = "]";
						if (debug) System.out.println("]");
						break;
					default:
						break;
					}
				}
				token_array.add(parts);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return token_array;
	}
	
	private String insertEquationString(int startPoint, int grpPoint, ArrayList<AttrEquation> lst) {
		String ret = null;
		for (int idx=0; idx<lst.size(); idx++) {
			AttrEquation ae = lst.get(idx);
			if (debug) System.out.println("     >>>>>>>>>>> startPoint : " + startPoint + ", grpPoint : " + grpPoint + ", AttrEquation.startPoint : " + ae.getStartPoint() + " - " + ae.toString());
			if (ae.getStartPoint()>startPoint && ae.getStartPoint()<=grpPoint){
				if (ret == null)
					ret = ae.toString();
				else
					ret = ret + ", " +  ae.toString();
			}
		}
		return ret;
	}
	
	private ArrayList<AttrEquation> insertEquation(int startPoint, int grpPoint, ArrayList<AttrEquation> lst) {
		ArrayList<AttrEquation> ret = new ArrayList<AttrEquation>();
		for (int idx=0; idx<lst.size(); idx++) {
			AttrEquation ae = lst.get(idx);
			if (debug) System.out.println("     >>>>>>>>>>> startPoint : " + startPoint + ", grpPoint : " + grpPoint + ", AttrEquation.startPoint : " + ae.getStartPoint() + " - " + ae.toString());
			if (ae.getStartPoint()>startPoint && ae.getStartPoint()<=grpPoint){
				ret.add(ae);
			}
		}
		return ret;
	}
	
	private AttrEquation getEquation(int start_point, String parentAttrName, ArrayList<filter_parts> token_array) throws ScimFilterException {
		AttrEquation attrEq = new AttrEquation();
		int array_size = token_array.size();

		for (int idx = start_point; idx < array_size; idx++) {
			filter_parts part = token_array.get(idx);
			String attr = part.data;
			if (part.type == 40) {
				if (debug) System.out.println("clear attrEquation");
				attrEq.clear();
			} else if (part.type == 41) {
				if (debug) System.out.println("close ) -> pass");
			} else if (part.type == 91) {
				parentAttrName = attrEq.getAttrName();
				attrEq.clear();
			} else if (part.type == 93) {
				parentAttrName = null;
			} else {
				if (attrEq.getAttrName() == null) {
					if ("and".equals(attr) || "or".equals(attr)) {
						if (debug) System.out.println("\t" + attr + ":" + attrEq.toString());
						attrEq.setLogical_and_or(attr);
						attrEq.clear();
					} else if ("not".equals(attr)) {
						if (debug) System.out.println("\t" + attr + ":" + attrEq.toString());
						attrEq.setLogical_not(attr);
						attrEq.clear();
					} else {
						if (parentAttrName != null)
							attr = parentAttrName + "." + attr;
						attrEq.setAttrName(attr);
					}
				} else if (attrEq.getOperator() == null) {
					attrEq.setOperator(attr);
					if (attr.equals("pr"))
						attrEq.setCompareValue("");
				} else if (attrEq.getCompareValue() == null) {
					attrEq.setCompareValue(attr);
				}

				if (attrEq.isCorrect()) {
					attrEq.setStartPoint(idx+1);
					attrEq.setParentAttrName(parentAttrName);
					if (debug) System.out.println(" >> {" + attrEq.toString() + "}");
					if ("not".equals(attrEq.getLogical_not())) {
						// not은 부호를 변경함
						String _op = attrEq.getOperator();
						if ("eq".equals(_op)) attrEq.setOperator("ne");
						else if ("ne".equals(_op)) attrEq.setOperator("eq");
						else if ("co".equals(_op)) attrEq.setOperator("nc"); // not contains
						else if ("sw".equals(_op)) attrEq.setOperator("ns"); // not startwith
						else if ("ew".equals(_op)) attrEq.setOperator("nw"); // not startwith
						else if ("pr".equals(_op)) attrEq.setOperator("np"); // not present
						else if ("gt".equals(_op)) attrEq.setOperator("le");
						else if ("ge".equals(_op)) attrEq.setOperator("lt");
						else if ("lt".equals(_op)) attrEq.setOperator("ge");
						else if ("le".equals(_op)) attrEq.setOperator("gt");
					}
					return attrEq;
				}
			}
		}
		return null;
	}
	
	public static JSONArray join(JSONArray pre, JSONArray now, String type) {

		JSONArray res = new JSONArray();
		
		if (now.size() > 0) {
			
			Iterator now_itor = now.iterator();
			int now_count = 0;
	
			while (now_itor.hasNext()) {
				JSONObject object = (JSONObject) now_itor.next();
				String now_id = object.getAsString("id");
				Iterator pre_itor = pre.iterator();
				boolean has = false; 
				while (pre_itor.hasNext()) {
					JSONObject pre_object = (JSONObject) pre_itor.next();
					String pre_id = pre_object.getAsString("id");
					if (pre_id.equals(now_id)) {
						System.out.println(type + " / " + now_id);
						has = true;
					}
				}
				
				if ("and".equals(type)) {
					if (has) res.add(object);
				} else if ("or".equals(type)) {
					if (!has) pre.add(object);
				} 
	
			}
		} else {
			System.out.println("NOW SIZE is " + now.size());
			Iterator now_itor = pre.iterator();
			int now_count = 0;
	
			while (now_itor.hasNext()) {
				JSONObject object = (JSONObject) now_itor.next();
				String now_id = object.getAsString("id");
				Iterator pre_itor = now.iterator();
				boolean has = false; 
				while (pre_itor.hasNext()) {
					JSONObject pre_object = (JSONObject) pre_itor.next();
					String pre_id = pre_object.getAsString("id");
					if (pre_id.equals(now_id)) has = true;
				}
				
				if ("and".equals(type)) {
					if (has) res.add(object);
				} else if ("or".equals(type)) {
					if (!has) pre.add(object);
				} 
	
			}
		}
		
		if ("and".equals(type)) {
			return res;
		} else if ("or".equals(type)) {
			return pre;
		}
		return null; 
	}
	
	public static void operate(AttrEquation op_work, Iterator itor) {

		String attrname = op_work.getAttrName();
		String comparevalue = op_work.getCompareValue();
		String operater = op_work.getOperator();
		String subTarget = op_work.getParentAttrName();
		
		if (attrname.contains(".")) attrname = attrname.substring(attrname.lastIndexOf('.')+1, attrname.length());
		
		if (debug) System.out.println("Equation : " + attrname + " " + operater + " " + comparevalue + " " + subTarget);

		while (itor.hasNext()) {
			JSONObject json_target = (JSONObject) itor.next();
			if (operater.equals("pr")) {
				if (json_target.containsKey(attrname)) {
					op_work.addResource(json_target);
				}

			} else if (operater.equals("np")) { // not present
				if (!json_target.containsKey(attrname)) {
					op_work.addResource(json_target);
				}
			} else {
				Object value = json_target.get(attrname);

				if (subTarget != null && !"".equals(subTarget)) {
					value = json_target.get(subTarget);
				}

				if (value == null) {

				} else if (ScimUtils.checkClassName(value, "JSONObject")) {

				} else if (ScimUtils.checkClassName(value, "JSONArray")) {
					JSONArray arry = (JSONArray) value;
					op_work.addResource(operater, arry, attrname, comparevalue, json_target);

				} else if (ScimUtils.checkClassName(value, "Boolean")) {
					boolean j_value = (boolean) value;
					op_work.addResource(operater, j_value, new Boolean((String) comparevalue), json_target);

				} else if (ScimUtils.checkClassName(value, "String")) {
					
					String j_value = (String) value;
					op_work.addResource(operater, j_value, comparevalue, json_target);

				} else if (ScimUtils.checkClassName(value, "Integer")) {
					Integer j_value = (Integer) value;
					op_work.addResource(operater, j_value, new Integer(comparevalue), json_target);
				}
			}
		}
	}
}
