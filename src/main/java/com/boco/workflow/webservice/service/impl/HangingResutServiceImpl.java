package com.boco.workflow.webservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.builder.ResultBuilder;
import com.boco.workflow.webservice.builder.ValidationBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.Result;
import com.boco.workflow.webservice.pojo.Validation;
import com.boco.workflow.webservice.project.bo.ProjectBO;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplService;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplServiceLocator;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 挂测结果处理
 * @author gaoyang 2017年3月16日
 *
 */
@Service
public class HangingResutServiceImpl extends AbstractService<ValidationBuilder,Validation> implements IService{

	private static final Logger logger = Logger.getLogger(HangingResutServiceImpl.class);
	@Autowired
	private ActiveService activeService;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ProjectBO projectBo;
	@Override
	protected void doBusiness(Validation e) throws Exception{
		
		logger.info("HangingResutServiceImpl.doBusiness");
		//存储结果？
		Map<String,String> map = new HashMap<String, String>();
		map.put("code", e.getPrjCode());
		map.put("pcode", e.getParentPrjCode());
		String cuid= projectDAO.queryActiveByPrjcode(map);
		
		if("1".equals(e.getReturnType()) || "失败".equals(e.getTestStatus())){
			
			activeService.doDeActive(cuid,null);
		}
		//向管线发消息
		String xml = e.getBuilder().toXml();
		logger.info("挂测结果：" + xml);
		
		ResourceCheckServiceImplService service = new ResourceCheckServiceImplServiceLocator();
		
		String result = service.getResourceCheckServiceImplPort().resourceCheck(xml);
		logger.info(result);
		
		Result res = PojoBuilderFactory.getBuilder(ResultBuilder.class).fromXml(result);
		if(!"成功".equals(e.getTestStatus())){
			
			//状态修改为施工
			PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addPrjStatus("施工").addCuid(cuid).build();
			projectDAO.updateProjectStatus(prjStatus);
			Map<String,String> dMap = new HashMap<String,String>();
			dMap.put("PRJ_CODE", e.getPrjCode());  
			dMap.put("PARENT_PRJ_CODE", e.getParentPrjCode());
			projectBo.deleteHanging(dMap);
			
		}
		if(StringUtils.isNotBlank(res.getErrorInfo())){
			
			//throw new Exception(res.getErrorInfo());
		}
		
		
	}
}
