package com.wession.scim;

import java.util.ArrayList;

import net.minidev.json.JSONArray;

public class LogicalEquation { 
	private ArrayList <AttrEquation> lstAttrEq = new ArrayList<AttrEquation>();
	private int priority;
	private String logic_operator;
	private JSONArray result = new JSONArray();
	
	public LogicalEquation() {
		
	}
	
	public String getOperator() {
		return logic_operator;
	}

	public void setOperator(String logic_operator) {
		this.logic_operator = logic_operator;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public LogicalEquation(int priority) {
		this.priority = priority;
	}
	
	public void addAttrEquation(AttrEquation eq){
		lstAttrEq.add(eq);
	}
	
	public void setEquations(ArrayList <AttrEquation> lstAttrEq){
		this.lstAttrEq = lstAttrEq;
	}
	
	public ArrayList <AttrEquation> getEquations(){
		return lstAttrEq;
	}
	
	public void setResult(JSONArray arry) {
		result = arry;
	}
	
	public JSONArray getResult(){
		return result;
	}
	
	public int getEquationCount() {
		return lstAttrEq.size();
	}
	
}
