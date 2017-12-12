package com.boco.workflow.webservice.pojo;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.boco.workflow.webservice.builder.PosValidationBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * pos观测结果（单条失败）
 * @author gaoyang 2017年3月8日
 *
 */
@XStreamAlias("info")
public class PosValidation {

	/**
	 * 工程编号（大项和子任务）
	 */
	@XStreamAlias("prjcode")
	private String prjCode;
	
	/**
	 * 所属工程编码
	 */
	@XStreamAlias("parentprjcode")
	private String parentPrjCode;	
	
	@XStreamAlias("posinfo")
	private PosInfo posInfo;
	
	public static class PosInfo{
		
		@XStreamImplicit(itemFieldName="posname")
		private List<String> posName;

		public List<String> getPosName() {
			return posName;
		}

		public void setPosName(List<String> posName) {
			this.posName = posName;
		}
		@Override
		public String toString() {
			
			return ToStringBuilder.reflectionToString(this);
		}
		
	}
	

	public String getPrjCode() {
		return prjCode;
	}

	public void setPrjCode(String prjCode) {
		this.prjCode = prjCode;
	}

	public String getParentPrjCode() {
		return parentPrjCode;
	}

	public void setParentPrjCode(String parentPrjCode) {
		this.parentPrjCode = parentPrjCode;
	}
	

	public PosInfo getPosInfo() {
		return posInfo;
	}

	public void setPosInfo(PosInfo posInfo) {
		this.posInfo = posInfo;
	}

	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	
	public PosValidationBuilder getBuilder(){
		
		return new PosValidationBuilder(this);
	}

}
