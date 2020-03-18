package com.sample.model;

public class ConceptSetItem {    	
    private String csName;
    private Integer conceptId;

    public ConceptSetItem(String name, Integer id){
	this.csName = name;
	this.conceptId = id;
    }
	    
    public String getCsName() {
	return csName;
    }
    public void setCsName(String name) {
	this.csName = csName;
    }
	
    public Integer getConceptId() {
	return conceptId;
    }
    public void setConceptId(Integer id) {
	this.conceptId = id;
    }
}

