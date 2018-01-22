package com.boco.workflow.webservice.utils;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.boco.core.spring.SysProperty;
import com.boco.workflow.webservice.builder.ResultBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.pojo.Result;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplService;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplServiceLocator;

public class SendMsgUtil {

	private Logger logger = Logger.getLogger(SendMsgUtil.class);
	
	private String inspectedUrl =  SysProperty.getInstance().getValue("INSPECTED_URL");
	
	private static SendMsgUtil instance = new SendMsgUtil();
	
	private SendMsgUtil() {
		
	}
	
	public static SendMsgUtil getInstance(){
		
		return instance;
	}
	
	public String send2Hang(NameValuePair[] nvps) throws Exception{
		
		String result = HttpClientUtil.sendPostRequest(inspectedUrl, nvps, "UTF-8");
		logger.info("挂测同步结果：" + result);
		return result;
	}
	
	public void send2Gx(String xml) throws Exception{
			
		logger.info("向管线发消息：" + xml);
		ResourceCheckServiceImplService service = new ResourceCheckServiceImplServiceLocator();
		String result = service.getResourceCheckServiceImplPort().resourceCheck(xml);
		logger.info("管线返回：" + result);
		Result res = PojoBuilderFactory.getBuilder(ResultBuilder.class).fromXml(result);
		if("失败".equals(res.getIsSuccess())){
			
			throw new Exception(res.getErrorInfo());
		}
	}
}
