package com.boco.workflow.webservice.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.spring.SysProperty;
import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;
import com.boco.workflow.webservice.utils.HttpClientUtil;

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
	
    private String inspectedUrl =  SysProperty.getInstance().getValue("INSPECTED_URL");
	
	@Override
	public void doBusiness(PrjStatus prjStatus) throws Exception {
		
		logger.info("SyncPrjStatusServiceImpl.doBusiness");
		
		Map<String,String> map = dao.getIdByCode(prjStatus);
		
		if(null == map || map.get("CUID") == null){
			
			throw new Exception("工程不存在！");
		}
		
		String cuid = map.get("CUID");
		prjStatus.setCuid(cuid);
		
		String status = prjStatus.getPrjStatus();
		if("初验".equals(status)){
			//归档  pos更新
        			
			this.dao.updatePos(cuid);
			
			this.dao.updateAddress(cuid);

				
		}else if("作废".equals(status)){
			
			//释放端口
			this.dao.updatePtp(cuid);
			//删除树形地址
			List<String> list = this.dao.queryAddressIds(cuid);		
			
			if(list != null && list.size() > 0){
				for(String id : list){
					
					this.dao.deleteAddressRelInfo(id);
				}
			}
			
			
			this.dao.moveResourse(cuid);		
			this.dao.removeResourse(cuid);
		}
			
		NameValuePair[] nvps = {
				new BasicNameValuePair("prjcode",prjStatus.getPrjCode()),
				new BasicNameValuePair("parentprjcode",prjStatus.getParentPrjCode()),
				new BasicNameValuePair("prjstatus",status)
		};
		HttpClientUtil.sendPostRequest(inspectedUrl, nvps, "UTF-8");
		
		dao.updateProjectStatus(prjStatus);
		
	}
}
