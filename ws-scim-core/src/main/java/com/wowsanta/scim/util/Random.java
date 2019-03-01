package com.wowsanta.scim.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
	public static String number(int min, int max) {
		int value = ThreadLocalRandom.current().nextInt(min,max);
		return String.valueOf(value);
	}
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
	public static String yn(int per) {
		int value = (int) (Math.random() * 100);
		if(value < per) {
			return "Y";
		}else {
			return "N";
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
	public static String position() {
		String[] positions = {"사장","전무","상무","이사","부장","차장","과장","대리","사원"};
		
		int value = (int) (Math.random() * 100);
		if(value == 0) {
			return positions[0];
		}else if(value > 0 && value <=1) {
			return positions[1];
		}else if(value > 1 && value <=2) {
			return positions[2];
		}else if(value > 2 && value <=4) {
			return positions[3];
		}else if(value > 4 && value <=9) {
			return positions[4];
		}else if(value > 9 && value <=20) {
			return positions[5];
		}else if(value > 20 && value <=50) {
			return positions[6];
		}else if(value > 50 && value <=80) {
			return positions[7];
		}else{
			return positions[8];
		}
	}
	
	public static String job() {
		String[] jobs = {"팀장","파트장","파트원","팀원"};
		
		int value = (int) (Math.random() * 100);
		if(value < 5) {
			return jobs[0];
		}else if(value > 5 && value <=15) {
			return jobs[1];
		}else if(value > 15 && value <=90) {
			return jobs[2];
		}else{
			return jobs[3];
		}
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
}
