package com.boco.workflow.webservice.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.utils.exception.UserException;
import com.boco.workflow.webservice.builder.ProjectBuilder;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.Project;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 
 * @author gaoyang 2017年3月7日
 *	新增工程的service
 */
@Service
public class ProjectServiceImpl extends AbstractService<ProjectBuilder,Project> implements IService{
	
	private static final Logger logger = Logger.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectDAO dao;
	
	@Override
	protected void doBusiness(Project project) throws Exception {
		
		logger.info("ProjectServiceImpl.doBusiness");
		
		if("新增".equals(project.getActionType())){
			
			dao.insertProject(project.getBuilder().addPrjStatus("施工").build());
		}else if ("删除".equals(project.getActionType())){
			
			dao.deleteProject(project.getCuid());
			
		}else if ("修改".equals(project.getActionType())){
			
			dao.updateProject(project);
			
		}else{
			
			throw new UserException("操作类型不存在！");
		}
		
	}
}
