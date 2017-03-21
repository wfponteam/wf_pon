package com.boco.workflow.webservice.service.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.ProjectNameSpace;
import com.boco.workflow.webservice.pojo.ProjectNameSpace.NameSpace;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 
 * @author gaoyang 2017年3月7日
 *	同步工程状态
 */
@Service
public class SyncPrjStatusServiceImpl extends AbstractService<PrjStatusBuilder,PrjStatus> implements IService{
	
	private static final Logger logger = Logger.getLogger(SyncPrjStatusServiceImpl.class);

	@Autowired
	private ProjectDAO dao;
	
	@Override
	protected void doBusiness(PrjStatus prjStatus) throws Exception {
		
		logger.info("SyncPrjStatusServiceImpl.doBusiness");
		
		String status = prjStatus.getPrjStatus();
		if("归档".equals(status)){
			//归档把
			String prjcode = prjStatus.getPrjCode();
			
			ProjectNameSpace ns = new ProjectNameSpace();
			ns.setPrjcode(prjcode);
			ns.setNs(NameSpace.NM.getName());
			
			this.dao.moveResourse(ns);
			ns.setNs(NameSpace.HIS.getName());
			this.dao.moveResourse(ns);
			
			this.dao.removeResourse(prjcode);
				
		}
		
		dao.updateProjectStatus(prjStatus);
		
	}
}
