package com.boco.workflow.webservice.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.Project;


@Component
@Aspect
public class WebServiceAspect {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	private static Logger logger = Logger.getLogger(WebServiceAspect.class);
	
	
	@Before(value = "execution(* com.boco.workflow.webservice.dao.ProjectDAO.updateProjectStatus(..))&& args(prjStatus)")
	public void beforeUpdate(PrjStatus prjStatus){
		
		logger.info("enter WebServiceAspect.beforeUpdate");
		try {
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", prjStatus.getCuid());
			map.put("status", prjStatus.getPrjStatus());
			IbatisDAO.getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".insertProjectStatusRecord", map);
			
		} catch (SQLException e) {
			
			logger.error("error in WebServiceAspect.beforeUpdate",e);
		}
	}
	
	@Before(value = "execution(* com.boco.workflow.webservice.dao.ProjectDAO.insertProject(..))&& args(project)")
	public void beforeInsert(Project project){
		
		logger.info("enter WebServiceAspect.beforeInsert");
		try {
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", project.getCuid());
			map.put("status", project.getPrjStatus());
			IbatisDAO.getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".insertProjectStatusRecord", map);
			
		} catch (SQLException e) {
			
			logger.error("error in WebServiceAspect.beforeInsert",e);
		}
	}
	
	
	
}
