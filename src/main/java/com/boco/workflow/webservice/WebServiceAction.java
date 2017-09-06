package com.boco.workflow.webservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.spring.SysProperty;
import com.boco.workflow.webservice.builder.PrjStatusBuilder;
import com.boco.workflow.webservice.builder.ValidationBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.ActiveResp;
import com.boco.workflow.webservice.pojo.ActiveResp.Active;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.project.bo.ProjectBO;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplService;
import com.boco.workflow.webservice.remote.ResourceCheckServiceImplServiceLocator;
import com.boco.workflow.webservice.service.impl.ActiveService;
import com.boco.workflow.webservice.utils.ZipUtil;

@Controller
@RequestMapping(value="/webServiceAction")
public class WebServiceAction {

	private Logger logger = Logger.getLogger(WebServiceAction.class);
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectBO projectBO;
	
	@Autowired
	private ActiveService activeService;
	
	@RequestMapping("/syncHangingResult")
	public @ResponseBody String syncHangingResult(String cuid){
		
		logger.info("将" + cuid + "状态改为挂测");
		try {
			//查询当前工程状态
			Map<String,Object> map = projectBO.queryProjectByCode(cuid);
			String status = IbatisDAOHelper.getStringValue(map, "PRJ_STATUS");
			
			
			if(!"施工".equals(status)){
				return "{\"success\":true,\"msg\":\"工程状态为:" + status + ",不能进行挂测！\"}";
			}
			projectBO.deleteHanging(map);
			projectBO.insertHanging(cuid);

			ActiveResp res= activeService.doActive(cuid);
			for(Active active : res.getActiveList()){
				Map<String, String> paremeterMap = new HashMap();
				paremeterMap.put("password", active.getPassword());
				paremeterMap.put("ponPort", active.getPonPort());
				paremeterMap.put("reason", active.getReason());
				projectBO.updateHanging(paremeterMap);
			}
			projectBO.updateResultStatus(map);
			////将状态改为挂测
			PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addCuid(cuid).addPrjStatus("挂测").build();
			projectDAO.updateProjectStatus(prjStatus);
			
		} catch (Exception e) {
			logger.error("挂测失败！",e);
			return "{\"success\":true,\"msg\":\"" + e.getMessage() + "\"}"; 
		}
		return "{\"success\":true,\"msg\":\"挂测成功！\"}";
	}
	
	/**
	 * 直接挂测成功
	 * @param cuid
	 * @return
	 */
	@RequestMapping("/syncHangingResult1")
	public @ResponseBody String syncHangingResult1(String cuid){
		logger.info("将" + cuid + "状态改为挂测");
		try {
			//查询当前工程状态
			Map<String,Object> map = projectBO.queryProjectByCode(cuid);
			String status = IbatisDAOHelper.getStringValue(map, "PRJ_STATUS");
			if(!"施工".equals(status)){
				
				return "{\"success\":true,\"msg\":\"工程状态为:" + status + ",不能进行挂测！\"}";
			}
			////将状态改为挂测
			PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addCuid(cuid).addPrjStatus("挂测")
				.build();
		
			projectDAO.updateProjectStatus(prjStatus);
			
			//发消息
			String xml = PojoBuilderFactory.getBuilder(ValidationBuilder.class).addParentPrjCode(IbatisDAOHelper.getStringValue(map, "PARENT_PRJ_CODE"))
					.addPrjCode(IbatisDAOHelper.getStringValue(map, "PRJ_CODE")).addtestinfo("成功了").addteststatus("成功").toXml();
			ResourceCheckServiceImplService service = new ResourceCheckServiceImplServiceLocator();
			
			logger.info(xml);
			String result = service.getResourceCheckServiceImplPort().resourceCheck(xml);
			logger.info(result);
			
			
		} catch (Exception e) {
			
			logger.error("挂测失败！",e);
			return "{\"success\":true,\"msg\":\"" + e.getCause().getMessage() + "\"}"; 
		}
		
		return "{\"success\":true,\"msg\":\"挂测成功！\"}";
		
	}
	 
	
	@RequestMapping("/downloadAttach")
	public void download(@RequestParam("fileName")String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {    
		
		
		String path = SysProperty.getInstance().getValue("attachPath");
        
        File orgFile = new File(path + fileName); 
        
        File zipFile = new File(path + fileName + ".zip");
        if(!zipFile.exists()){
        	
        	ZipUtil.zip(orgFile.getAbsolutePath(),zipFile.getAbsolutePath());
        	
        	//工具bug
    //    	zipUtils.compress(orgFile.getAbsolutePath().replaceAll("\\\\", "/"),zipFile.getAbsolutePath().replaceAll("\\\\", "/"));
        	
        	orgFile.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					
					pathname.delete();
					return false;
				}
			});
        	orgFile.delete();
        }
       
        
        if (zipFile.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + fileName + ".zip");// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(zipFile);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } finally {
                if (bis != null) {
                        bis.close();
                }
                if (fis != null) {
                     fis.close();
                }
            }
        }
         
    } 
	
	
	
}
