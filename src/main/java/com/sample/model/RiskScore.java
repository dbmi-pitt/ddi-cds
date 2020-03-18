package com.sample.model;

public class RiskScore {
	Integer patientId;
	String score;
	
	public RiskScore() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RiskScore(Integer patientId, String score) {
		super();
		this.patientId = patientId;
		this.score = score;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
}
