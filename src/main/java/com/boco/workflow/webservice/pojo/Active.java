package com.boco.workflow.webservice.pojo;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("msgBody")
public class Active {
	
    private String type;
	
	private String productId;
	
	private Set<Device> deviceList;
	
	@XStreamAlias("device")
	public static class Device{
	
		@XStreamAlias("label_dev")
		private String labelDev;
		
		private String oltName;
		
		private String ems;
		
		private String ponPort;
		
		private String password;
		
		private String svlan;
		
		private String cvlan;
		
		private String factory;
		
		public String getLabelDev() {
			return labelDev;
		}
		public void setLabelDev(String labelDev) {
			this.labelDev = labelDev;
		}
		public String getOltName() {
			return oltName;
		}
		public void setOltName(String oltName) {
			this.oltName = oltName;
		}
		public String getEms() {
			return ems;
		}
		public void setEms(String ems) {
			this.ems = ems;
		}
		public String getPonPort() {
			return ponPort;
		}
		public void setPonPort(String ponPort) {
			this.ponPort = ponPort;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getSvlan() {
			return svlan;
		}
		public void setSvlan(String svlan) {
			this.svlan = svlan;
		}
		public String getCvlan() {
			return cvlan;
		}
		public void setCvlan(String cvlan) {
			this.cvlan = cvlan;
		}
		public String getFactory() {
			return factory;
		}
		public void setFactory(String factory) {
			this.factory = factory;
		}
		
		@Override
		public String toString() {
			
			return ToStringBuilder.reflectionToString(this);
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if(!(obj instanceof Device)){
				
				return false;
			}
			Device device = (Device)obj;
			if(device == this){
				
				return true;
			}
			return this.ponPort.equals(device.ponPort);
		}
		
		@Override
		public int hashCode() {
			
			return 31 + ((ponPort == null) ? 0 : ponPort.hashCode());
		}
		
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	public Set<Device> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(Set<Device> deviceList) {
		this.deviceList = deviceList;
	}
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
}
