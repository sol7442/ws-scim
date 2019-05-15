package com.wowsanta.scim.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
	public static String number(int min, int max) {
		int value = ThreadLocalRandom.current().nextInt(min,max);
		return String.valueOf(value);
	}
//	public static int number(int min, int max) {
//		return ThreadLocalRandom.current().nextInt(min,max);
//	}
	public static String name() {
		String[] f_name = {"김","이","박","최","정","강","조","윤","장"};
		String[] m_name = {"용","현","철","성","지","미","연","형","구"};
		String[] l_name = {"섭","중","일","진","흥","민","재","인","호"};
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(f_name[ThreadLocalRandom.current().nextInt(f_name.length)]);
		buffer.append(m_name[ThreadLocalRandom.current().nextInt(f_name.length)]);
		buffer.append(l_name[ThreadLocalRandom.current().nextInt(f_name.length)]);
		
		return buffer.toString();
	}
	public static String YN_string(int per) {
		int value = (int) (Math.random() * 100);
		if(value > per) {
			return "Y";
		}else {
			return "N";
		}
	}
	
	public static int yn_integer(int per) {
		int value = (int) (Math.random() * 100);
		if(value < per) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public static boolean yn_boolean(int per) {
		int value = (int) (Math.random() * 100);
		if(value < per) {
			return true;
		}else {
			return false;
		}
	}
	public static String department() {
		String[] positions = {"총무","인사","영업1","영업2","개발1","개발2"};
		int value = (int) (Math.random() * 100);
		int index = value%positions.length;
		return positions[index];
	}
	
	
	public static ORGANIZATION oranization() {
		int index = ThreadLocalRandom.current().nextInt(0,ORGANIZATION.values().length -1);
		return ORGANIZATION.values()[index];
		
	}
	public static POSITION position() {
		int index = ThreadLocalRandom.current().nextInt(0,POSITION.values().length -1);
		return POSITION.values()[index];
		
	}
	public static DUTY duty() {
		int index = ThreadLocalRandom.current().nextInt(0,DUTY.values().length -1);
		return DUTY.values()[index];
	}
	public static JOB job() {
		int index = ThreadLocalRandom.current().nextInt(0,JOB.values().length -1);
		return JOB.values()[index];
	}
	
	public static String phone() {
		String[] dial = {"02","031","032","033","041","043","044","051","052","053","054","055","061","062","063","064"};
		int value = (int) (Math.random() * 100);
		if(value < 80) {
			return dial[0] + number(10000000,99999999);
		}else {
			int other = ThreadLocalRandom.current().nextInt(1,dial.length - 1);
			return dial[other] + number(10000000,99999999);
		}
	}
	public static String mobile() {
		return "010" + number(10000000,99999999);
	}
	
	public static Date date(Date start, Date end ) {
		return new Date(ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime()));
	}
	public static Date beforeYears(int year) {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
	    cal.add(Calendar.YEAR, (year*-1));
	    Date start   = cal.getTime();
	    return date(start,now);
	}
	public static String group() {
		String[] groups = {"Group1","Group2","Group3"};
		
		int value = (int) (Math.random() * 100);
		int index  = value % groups.length;
		
		return groups[index];
	}
	
	public enum ORGANIZATION {
		HeadOffice("Head Office","본사"),
		BranchOffice("Branch Office","지사"),
		Division("Division","사업소"),
		SalesOffice("Sales Office","영업소"),
		OfficeoftheSecretary("Office of the Secretary","비서실"),
		PlanningTeam("Planning Team","기획팀"),
		LawDept("Law Dept.","법무팀"),
		GeneralAffairsDept("General Affairs Dept.","총무부"),
		PersonnelSection("Personnel Section","인사과"),
		
		;
		
		private String eng;
		private String kor;
		ORGANIZATION(String eng,String kor){
			this.eng = eng;
			this.kor = kor;
		}
		public String getEnglish() {
			return this.eng;
		}
		public String getKorean() {
			return this.kor;
		}
	}
	
	public enum JOB{//https://www.americasjobexchange.com/job-descriptions/information-technology-jobs
		BusinessAnalyst("Business Analyst"	,"사업분석"),
		ComputerOperator("Computer Operator","시스템운영"),
		ProjectManager("Project Manager"	,"프로젝트관리"),
		SoftwareEngineer("Software Engineer","소프트웨어관리"),		
		WebDeveloper("Web Developer"		,"웹개발")
		;
		
		private String eng;
		private String kor;
		JOB(String eng,String kor){
			this.eng = eng;
			this.kor = kor;
		}
		public String getEnglish() {
			return this.eng;
		}
		public String getKorean() {
			return this.kor;
		}
	}
	
	public enum DUTY{ //https://jisikzip.tistory.com/6
		RepresentativeDirector("Representative Director","대표이사"),
		GeneralExecutiveDirector("General Executive Director","총괄임원"),
		RegionalHeadquarterManager("Regional Headquarter Manager","본부장"),
		TeamLeader("Team Leader","팀장"),
		PartLeader("Part Leader","파트잦"),
		;
		
		private String eng;
		private String kor;
		DUTY(String eng,String kor){
			this.eng = eng;
			this.kor = kor;
		}
		public String getEnglish() {
			return this.eng;
		}
		public String getKorean() {
			return this.kor;
		}
	}
	public enum POSITION{		
		ChairmanOfTheBoard("Chairman of the Board","회장"),
		HonoraryChairman("Honorary Chairman","명예회장"),
		ViceChairman("Vice Chairman","부회장"),
		President("President","대표"),
		Owner("Owner","대표"),
		SeniorExecutiveVicePresident("Senior Executive Vice President","부사장"),
		ExecutiveManagingDirector("Executive Managing Director","전무"),
		ManagingDirector("Managing Director","상무"),
		Director("Director","이사"),
		ExecutiveAdvisor("Executive Advisor","상임고문"),
		AuditingDirector("Auditing Director","감사"),
		DeputyDirector("Deputy Director","부소장"),
		BranchManager("Branch Manager","지점장")
		;
		
		private String eng;
		private String kor;
		POSITION(String eng,String kor) {
			this.eng = eng;
			this.kor = kor;
		}
		
		public String getEnglish() {
			return this.eng;
		}
		public String getKorean() {
			return this.kor;
		}
	}
}
