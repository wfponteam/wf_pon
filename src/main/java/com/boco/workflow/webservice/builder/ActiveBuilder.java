package com.boco.workflow.webservice.builder;

import java.util.ArrayList;
import java.util.List;

import com.boco.workflow.webservice.pojo.Active;
import com.boco.workflow.webservice.pojo.Active.Device;
import com.boco.workflow.webservice.upload.constants.ElementEnum.relationCrossSDHRate;

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
			
			pojo.setDeviceList(new ArrayList<Device>());
		}
		
		List<Device> list = pojo.getDeviceList();
		list.add(device);
		return this;
	}
	
	
}
