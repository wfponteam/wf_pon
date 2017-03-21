package com.boco.workflow.webservice.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ProjectNameSpace {

	private String prjcode;
	
	private String ns;

	public String getPrjcode() {
		return prjcode;
	}

	public void setPrjcode(String prjcode) {
		this.prjcode = prjcode;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	
	public static enum NameSpace{
		ATTEMP("T_ATTEMP_"),HIS("T_HIS_"),NM("");
		
		String name ;
		
		private NameSpace(String name){
			this.name = name;
		}
		
		public String getName(){
			
			return this.name;
		}
	}
}
