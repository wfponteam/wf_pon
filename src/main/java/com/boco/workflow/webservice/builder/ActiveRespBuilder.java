package com.boco.workflow.webservice.builder;

import java.util.ArrayList;
import java.util.List;

import com.boco.workflow.webservice.pojo.ActiveResp;
import com.boco.workflow.webservice.pojo.ActiveResp.Active;

public class ActiveRespBuilder extends AbstractBuilder<ActiveResp> implements IBuilder<ActiveResp>{

    public ActiveRespBuilder() {
		
		super(new ActiveResp());
	}
    
	public ActiveRespBuilder(ActiveResp activeResp){
		
		super(activeResp);
	}
	
	public ActiveRespBuilder addType(String type){
		
		pojo.setType(type);
		return this;
	}
	
	public ActiveRespBuilder addProductId(String productId){
		
		pojo.setProductId(productId);
		return this;
	}
	
    public ActiveRespBuilder addActive(Active active){
		
		if(pojo.getActiveList() == null){
			
			pojo.setActiveList(new ArrayList<Active>());
		}
		
		List<Active> list = pojo.getActiveList();
		list.add(active);
		return this;
	}}
