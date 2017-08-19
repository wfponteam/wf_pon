package com.boco.workflow.webservice.service.impl;

import com.boco.workflow.webservice.builder.ActiveRespBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;

public class ActiveRespServiceImpl {
    
	public void doResult(){
		
	}
	public static void main(String[] args) {
		String a = PojoBuilderFactory.getBuilder(ActiveRespBuilder.class).addType("2sad").toXml();
		
		System.out.println(a);
	}
}
