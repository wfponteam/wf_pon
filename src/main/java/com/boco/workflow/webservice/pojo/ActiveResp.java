package com.boco.workflow.webservice.pojo;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class ActiveResp {
	
    private String type; 
    
    private String productId;
    
    private List<Active> activeList;
    
    @XStreamAlias("active")
    public class Active{
    	
    	private String ponPort;
    	
    	private String password;
    	
    	private String isSucess;
    	
    	private String reason;

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

		public String getIsSucess() {
			return isSucess;
		}

		public void setIsSucess(String isSucess) {
			this.isSucess = isSucess;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
    	
		@Override
		public String toString() {
			
			return ToStringBuilder.reflectionToString(this);
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

	public List<Active> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<Active> activeList) {
		this.activeList = activeList;
	}
    
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
