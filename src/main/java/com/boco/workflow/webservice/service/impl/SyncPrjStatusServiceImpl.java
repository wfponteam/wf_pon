package com.boco.workflow.webservice.service.impl;

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
	public void doBusiness(PrjStatus prjStatus) throws Exception {
		
		logger.info("SyncPrjStatusServiceImpl.doBusiness");
		
		String cuid = dao.getIdByCode(prjStatus);
		
		if(null == cuid){
			
			throw new Exception("工程不存在！");
		}
		
		prjStatus.setCuid(cuid);
		
		String status = prjStatus.getPrjStatus();
		if("初验".equals(status)){
			//归档
			
			ProjectNameSpace ns = new ProjectNameSpace();
			ns.setPrjcode(cuid);
			ns.setNs(NameSpace.NM.getName());
			
			this.dao.moveResourse(ns);
			ns.setNs(NameSpace.HIS.getName());
			this.dao.moveResourse(ns);
			
			this.dao.removeResourse(cuid);
				
		}else if("作废".equals(status)){
			
			//TODO:释放端口
			
			ProjectNameSpace ns = new ProjectNameSpace();
			ns.setPrjcode(cuid);
			ns.setNs(NameSpace.HIS.getName());
			this.dao.moveResourse(ns);		
			this.dao.removeResourse(cuid);
		}
		
		dao.updateProjectStatus(prjStatus);
		
	}
}
