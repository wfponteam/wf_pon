package com.boco.workflow.webservice;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boco.workflow.webservice.builder.ResultBuilder;
import com.boco.workflow.webservice.builder.ValidationBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.pojo.Result;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplService;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplServiceLocator;

@Controller()
@RequestMapping(value="/webServiceAction")
public class WebServiceAction {

	private Logger logger = Logger.getLogger(WebServiceAction.class);
	
	@RequestMapping("/syncHangingResult")
	public void syncHangingResult(String parentPrjCode,String prjCode) throws RemoteException, ServiceException{
		
		ResourceCheckServiceImplService service = new ResourceCheckServiceImplServiceLocator();
		
		
		String xml = PojoBuilderFactory.getBuilder(ValidationBuilder.class).addParentPrjCode(parentPrjCode)
		.addPrjCode(prjCode).addtestinfo("test").addteststatus("正常").toXml();
		
		logger.info(xml);
		String result = service.getResourceCheckServiceImplPort().resourceCheck(xml);
		logger.info(result);
		
		Result res = PojoBuilderFactory.getBuilder(ResultBuilder.class).fromXml(result);
		logger.info(res);
	}
}
