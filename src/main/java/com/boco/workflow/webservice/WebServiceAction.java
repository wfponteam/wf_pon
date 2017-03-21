package com.boco.workflow.webservice;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.project.bo.ProjectBO;

@Controller()
@RequestMapping(value="/webServiceAction")
public class WebServiceAction {

	private Logger logger = Logger.getLogger(WebServiceAction.class);
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectBO projectBO;
	
	@RequestMapping("/syncHangingResult")
	public @ResponseBody String syncHangingResult(String parentPrjCode,String prjCode){
		
		/*ResourceCheckServiceImplService service = new ResourceCheckServiceImplServiceLocator();
		
		
		String xml = PojoBuilderFactory.getBuilder(ValidationBuilder.class).addParentPrjCode(parentPrjCode)
		.addPrjCode(prjCode).addtestinfo("test").addteststatus("成功").toXml();
		
		logger.info(xml);
		String result = service.getResourceCheckServiceImplPort().resourceCheck(xml);
		logger.info(result);
		
		Result res = PojoBuilderFactory.getBuilder(ResultBuilder.class).fromXml(result);*/
		
		logger.info("将" + prjCode + "状态改为挂测");
		try {
			//查询当前工程状态
			Map<String,Object> map = projectBO.queryProjectByCode(prjCode);
			String status = IbatisDAOHelper.getStringValue(map, "PRJ_STATUS");
			if(!"施工".equals(status)){
				
				return "{\"success\":true,\"msg\":\"工程状态为:" + status + ",不能进行挂测！\"}";
			}
			
			////将状态改为挂测
			PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addPrjCode(prjCode).addParentPrjCode(parentPrjCode).addPrjStatus("挂测")
				.build();
		
			projectDAO.updateProjectStatus(prjStatus);
			
			
		} catch (Exception e) {
			
			logger.error("挂测失败！",e);
			return "{\"success\":true,\"msg\":\"" + e.getMessage() + "\"}"; 
		}
		
		return "{\"success\":true,\"msg\":\"挂测成功！\"}";
		
	}
}
