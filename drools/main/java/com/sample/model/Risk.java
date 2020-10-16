package com.sample.model;

public class Risk {
	private Integer id;
	private boolean cns;
	private boolean fallhx;
	
	
	public Risk() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Risk(Integer id, boolean cns, boolean fallhx) {
		super();
		this.id = id;
		this.cns = cns;
		this.fallhx = fallhx;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isCns() {
		return cns;
	}
	public void setCns(boolean cns) {
		this.cns = cns;
	}
	public boolean isFallHx() {
		return fallhx;
	}
	public void setFallHx(boolean fallhx) {
		this.fallhx = fallhx;
	}
	@Override
	public String toString() {
		return "Risk [id=" + id + ", cns=" + cns + ", fallhx=" + fallhx + "]";
	}
	
	
}
