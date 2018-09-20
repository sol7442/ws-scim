package com.wession.scim.test;

import java.util.ArrayList;
import java.util.Iterator;

import com.wession.scim.Const;
import com.wession.scim.resource.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.parser.JSONParser;

public class UserExt {
	public static void main(String [] args) {
		System.out.println("Samples");
		String schema_cust_user = "urn:ietf:params:wowsanta:schemas:companyK:2.0:extUser";
		String schema_cust_dept = "urn:ietf:params:wowsanta:schemas:companyK:2.0:extDept";
		User user = new User("EMPLID : 사번", "TYCC_EMPL_NM : 성명(한글)");
		
		user.addSchema(schema_cust_user);
		user.addSchema(schema_cust_dept);
		
		user.put("title", "KLCC_GRADE_TITLE : 호칭");
		user.put("active", "IDM_ID : 계정 잠김 - true/false");
		JSONArray phoneNumbers = new JSONArray();
		JSONObject pnum1 = new JSONObject();
		pnum1.put("type", "work");
		pnum1.put("value", "TYCC_TLNO : 사내전화");
		phoneNumbers.add(pnum1);
		JSONObject pnum2 = new JSONObject();
		pnum2.put("type", "fax");
		pnum2.put("value", "TYCC_FAX_NO : 팩스번호");
		phoneNumbers.add(pnum2);
		JSONObject pnum3 = new JSONObject();
		pnum3.put("type", "mobile");
		pnum3.put("value", "TYCC_MOBILE_NO : 이동전화");
		phoneNumbers.add(pnum3);
		
		
		JSONObject cust_user_org = new JSONObject();
		JSONObject cust_user = new JSONObject();
		JSONObject hr = new JSONObject();
		JSONObject account = new JSONObject();
		JSONObject name = new JSONObject();
		JSONObject current = new JSONObject();
		JSONObject effort = new JSONObject();
		JSONObject dept = new JSONObject();
		
		name.put("kr", "TYCC_EMPL_NM : 성명(한글)");
		name.put("en", "TYCC_EMPL_EN_NM : 성명(영문)");
		name.put("ch", "TYCC_EMPL_CC_NM : 성명(한자)");
		
		effort.put("startDate", "EFFDT : 유효일자");
		effort.put("sequence", "EFFSEQ : 유효순서");
		
		hr.put("springtide", "HR_IKEN_GB : HR 계정 구분");
		hr.put("status", "HR_STATUS : 재직상태");
		hr.put("statusName", "TYCC_HR_STATUS_NM : 재직상태명");
		hr.put("step", "STEP : 직급년차/호봉");
		hr.put("salary", "EMPL_STATUS : 급여상태");
		hr.put("salaryName", "TYCC_EMPL_STATUS_NM : 급여상태명");
		hr.put("sex", "SEX : 성별");
		hr.put("joinDate", "TYDE_ETCO_DT : 입사일자");
		hr.put("retireDate", "TYDE_RETIRE_DT : 퇴직일자");
		
		account.put("sapID", "SAPID : 회사 사번");
		account.put("employeeRecord", "EMPL_RCD : 사원레코드");
		account.put("ikenId", "KLCC_IKEN_ID : IKEN ID(계정)");
		account.put("kldeReqDate", "KLDE_IKENID_REQ_DT : IKEN만료요청일");
		account.put("oldEmpID", "KLCC_OLD_EMPLID : 구사번");
		
		current.put("jobCode", "JOBCODE : 직무코드");
		current.put("jobName", "JOBCODE_NM : 직무명");
		current.put("unionCode", "UNION_CD : 직책코드");
		current.put("unionName", "UNION_CD_NM : 직책명");
		current.put("gradeCode", "GRADE : 직급코드");
		current.put("gradeName", "GRADE_NM : 직급명");
		current.put("gradeTitle", "KLCC_GRADE_TITLE : 호칭");
		current.put("isManager", "TYCC_MGR_POS_YN : 부서장여부");
		
		cust_user.put("level", "POR_LEVEL : 보안레벨");
		cust_user.put("managerEmpID", "TYCC_PRNT_EMPLID : 상위자사번");
		cust_user.put("tydeEndDate", "TYDE_END_DT : 종료(예정)일");
		
		cust_user.put("account", account);
		cust_user.put("HR", hr);
		cust_user.put("name", name);
		cust_user.put("current", current);
		cust_user.put("effort", effort);
		
		cust_user_org.put("name", "COMPANYNAME : 회사명");
		cust_user_org.put("code", "COMPANY : 회사");
		cust_user_org.put("businessUnit", "BUSINESS_UNIT : 비즈니스단위");
		dept.put("setDeptId", "SETID_DEPT : 부서SetID");
		dept.put("name", "TYCC_DEPT_NM : 부서명");
		dept.put("code", "DEPTID : 부서코드");
		dept.put("type", "TYCC_HDFC_SN_CD : 부서구분");
		dept.put("typeName", "TYCC_HDFC_SN_NM : 부서구분명");
		dept.put("parentCode", "TYCC_PRNT_DEPTID : 상위부서코드");
		dept.put("estabID", "ESTABID : 사업장코드");
		dept.put("costCenter", "KLCC_COST_CTR : 코스트센터");
		dept.put("prnlsCenter", "KLCC_PRNLS_CTR : 손익센터");
		cust_user_org.put("dept", dept);
		
		user.put(schema_cust_user, cust_user);
		user.put(schema_cust_dept, cust_user_org);
		
		JSONObject enterprise = new JSONObject();
		enterprise.put("employeeNumber", "EMPLID : 사번");
		enterprise.put("organization", "COMPANYNAME : 회사명");
		enterprise.put("department", "TYCC_DEPT_NM : 부서명");
		enterprise.put("costCenter", "KLCC_COST_CTR : 코스트센터");
		JSONObject manager = new JSONObject();
		manager.put("value", "상위관리자의 SCIM User ID");
		manager.put("$ref", "상위관리자의 SCIM URI");
		manager.put("displayName", "상위관리자의 이름");
		enterprise.put("manager", manager);
		
		user.put(Const.schemas_v20_user_ext, enterprise);
		user.addSchema(Const.schemas_v20_user_ext);

		System.out.println(user.toJSONString());
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				
				
			}
			
		});
		t.run();
		
		ArrayList <String> igothim = new ArrayList<String>();
		for (int i=1; i<=4800; i++) {
			igothim.add("COL" + i);
		}
		
		Iterator itor = igothim.iterator();
		String targetField = "id";
		int max = 20;
		boolean newline = true;
		int cnt = 0;
		StringBuilder sb = new StringBuilder();
		while (itor.hasNext()) {
			if (newline == true) sb.append(" AND ").append(targetField).append(" NOT IN("); 
			String id = (String) itor.next();
			sb.append("'").append(id).append("',");
			if (cnt >= max-1) {
				cnt = 0;
				sb.delete(sb.length()-1, sb.length());
				sb.append(")").append("\n");
				newline = true;
			} else {
				cnt++;
				newline = false;
			}
		}
		if (",".equals(sb.substring(sb.length()-1))) {
			sb.delete(sb.length()-1, sb.length());
			sb.append(")").append("\n");
		}
		
		System.out.println(sb.toString());
	}
}

