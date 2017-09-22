package com.boco.workflow.webservice.builder;

import java.util.HashSet;
import java.util.Set;

import com.boco.workflow.webservice.pojo.Active;
import com.boco.workflow.webservice.pojo.Active.Device;

/**
 * 构建工程的结构
 * @author gaoyang 2017年3月7日
 *
 */
public class ActiveBuilder extends AbstractBuilder<Active> implements IBuilder<Active>{

	public ActiveBuilder() {
		
		super(new Active());
	}
	
	public ActiveBuilder(Active active){
		
		super(active);
	}

	
	public ActiveBuilder addType(String type){
		
		pojo.setType(type);
		return this;
	}
	
    public ActiveBuilder addProductId(String productId){
    	
    	pojo.setProductId(productId);
    	return this;
    }
    
	public ActiveBuilder addDevice(Device device){
		
		if(pojo.getDeviceList() == null){
			
			pojo.setDeviceList(new HashSet<Device>());
		}
		
		Set<Device> list = pojo.getDeviceList();
		list.add(device);
		return this;
	}
	
	
}
