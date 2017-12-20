package com.boco.workflow.webservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.builder.PosValidationBuilder;
import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PosValidation;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.PosValidation.PosInfo;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 挂测结果处理
 * @author gaoyang 2017年11月16日
 *
 */
@Service
public class PosResutServiceImpl extends AbstractService<PosValidationBuilder,PosValidation> implements IService{

	private static final Logger logger = Logger.getLogger(PosResutServiceImpl.class);
	@Autowired
	private ActiveService activeService;
	@Autowired
	private ProjectDAO projectDAO;
	@Override
	protected void doBusiness(PosValidation e) throws Exception{
		
		logger.info("PosResutServiceImpl.doBusiness");

		Map<String,String> map = new HashMap<String, String>();
		map.put("code", e.getPrjCode());
		map.put("pcode", e.getParentPrjCode());
		String cuid= projectDAO.queryActiveByPrjcode(map);
		
		//状态修改为挂测(修改)
		PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addPrjStatus("挂测(修改)").addCuid(cuid).build();
		projectDAO.updateProjectStatus(prjStatus);
		
		//去激活
		PosInfo info = e.getPosInfo();
		if(info != null){
			
			List<String> names = info.getPosName();
			if(names != null && names.size() > 0){
				
				activeService.doDeActive(cuid,names);
				projectDAO.deleteHangingPos(names);
			}
		}
		
	}
}
