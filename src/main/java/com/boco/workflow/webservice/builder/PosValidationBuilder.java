package com.boco.workflow.webservice.builder;

import com.boco.workflow.webservice.pojo.PosValidation;

/**
 * pos挂测结果 构造类
 * @author gaoyang 2017年11月28日
 *
 */
public class PosValidationBuilder extends AbstractBuilder<PosValidation> implements IBuilder<PosValidation>{

	public PosValidationBuilder() {
		super(new PosValidation());
	}
	
	public PosValidationBuilder(PosValidation v) {
		super(v);
	}
	
	public PosValidationBuilder addPrjCode(String prjCode){
		pojo.setPrjCode(prjCode);
		return this;
	}
	
	public PosValidationBuilder addParentPrjCode(String parentPrjCode){
		pojo.setParentPrjCode(parentPrjCode);
		return this;
	}


}
