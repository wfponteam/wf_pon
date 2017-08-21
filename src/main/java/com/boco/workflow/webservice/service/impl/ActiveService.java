package com.boco.workflow.webservice.service.impl;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.builder.ActiveBuilder;
import com.boco.workflow.webservice.builder.ActiveRespBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.pojo.Active.Device;
import com.boco.workflow.webservice.pojo.ActiveResp;
import com.boco.workflow.webservice.project.bo.ProjectBO;
import com.ws.TripartWebServiceServiceLocator;
import com.ws.TripartWebServiceServiceSoapBindingStub;

@Service
public class ActiveService{

	private static final Logger logger = Logger.getLogger(ActiveService.class);
    @Autowired
	private ProjectBO projectBO;
    
	public  ActiveResp doActive(String cuid) throws Exception{
		return active(cuid,"1");
	}
	
    public ActiveResp  doDeActive(String cuid ) throws Exception{
	  return active(cuid,"2");
    }
   
     private ActiveResp active (String cuid ,String type) throws Exception{
	    List<Map<String, String>> list = projectBO.queryActiveByCuid(cuid);
	    Device device = null;
	    ActiveBuilder builder = PojoBuilderFactory.getBuilder(ActiveBuilder.class).addType(type).addProductId(cuid);
	    if (list != null && list.size() > 0){
	    	
	    	for(int i = 0; i < list.size(); i++){
	    		device = new Device();
	    		Map<String,String> map = list.get(i);
	    		assert(StringUtils.isNotBlank(map.get("LABEL_DEV"))):"LABEL_DEV为空null";
	    		device.setLabelDev(map.get("LABEL_DEV"));
	    		assert(StringUtils.isNotBlank(map.get("OLTCVLAN"))):"CVLAN为空null";
	    		device.setCvlan(map.get("OLTCVLAN"));
	    		assert(StringUtils.isNotBlank(map.get("OLTEMS"))):"EMS为空null";
	    		device.setEms(map.get("OLTEMS"));
	    		device.setFactory(map.get("OLTFACTORY"));
	    		assert(StringUtils.isNotBlank(map.get("DEVICENAME"))):"设备名称为空null";
	    		device.setOltName(map.get("DEVICENAME"));
	    		assert(StringUtils.isNotBlank(map.get("OLTSVLAN"))):"SVLAN为空null";
	    		device.setSvlan(map.get("OLTSVLAN"));
	    		assert(StringUtils.isNotBlank(map.get("PASSWORD"))):"密码为空null";
	    		device.setPassword(map.get("PASSWORD"));
	    		assert(StringUtils.isNotBlank(map.get("PONPORTNAME"))):"端口名称为空null";
	    		device.setPonPort(map.get("PONPORTNAME"));
	    		builder.addDevice(device);
	    	}
	    }
		String activeStr =builder.toXml();
		logger.info(activeStr);
		TripartWebServiceServiceLocator serviceLocator = new TripartWebServiceServiceLocator();
		URL url = new URL("http://10.221.139.9:8091/Halt/GreetingService");
		TripartWebServiceServiceSoapBindingStub serviceServiceSoapBindingStub = new TripartWebServiceServiceSoapBindingStub(url,serviceLocator);
		String result = serviceServiceSoapBindingStub.active(activeStr);
	//	String result = serviceLocator.getTripartWebServicePort().active(activeStr);
		ActiveResp activeResp = PojoBuilderFactory.getBuilder(ActiveRespBuilder.class).fromXml(result);
		logger.info(activeResp);
		return activeResp;
   }

}
