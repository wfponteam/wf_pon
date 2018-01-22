package com.boco.workflow.webservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.boco.workflow.webservice.utils.SendMsgUtil;
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
			
			if("挂测(修改)".equals(status)){
				
				return this.syncPosHanging(cuid);
			}
			if(!"施工".equals(status) && !"施工(驳回)".equals(status)){
				return "{\"success\":true,\"msg\":\"工程状态为:" + status + ",资管系统接口数据已推送到挂测系统,不能进行挂测！\"}";
			}
			
			if("施工(驳回)".equals(status)){
				NameValuePair[] nvps = {
						new BasicNameValuePair("prjcode",IbatisDAOHelper.getStringValue(map, "PRJ_CODE")),
						new BasicNameValuePair("parentprjcode",IbatisDAOHelper.getStringValue(map, "PARENT_PRJ_CODE")),
						new BasicNameValuePair("prjstatus","回退"),
						new BasicNameValuePair("desc","管线回退，综资重新发起")
				};
				SendMsgUtil.getInstance().send2Hang(nvps);
			}
			
			projectBO.deleteHanging(map);
			projectBO.insertHanging(cuid);

			ActiveResp res= activeService.doActive(cuid,null);
			for(Active active : res.getActiveList()){
				Map<String, String> paremeterMap = new HashMap();
				paremeterMap.put("password", active.getPassword());
				paremeterMap.put("ponPort", active.getPonPort());
				paremeterMap.put("reason", active.getReason());
				paremeterMap.put("cuid", cuid);
				projectBO.updateHanging(paremeterMap);
			}
			projectBO.updateResultStatus(cuid);
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
	 * 修改后挂测
	 * @param cuid
	 * @return
	 */
	@RequestMapping("/syncPosHanging")
	public @ResponseBody String syncPosHanging(String cuid){
		
		logger.info( cuid + "修改后挂测");
		try {

			projectBO.insertHanging(cuid);

			//获取修改了的pos数据
			List<String> posNames = projectBO.queryHangingPos(cuid);
			if(posNames != null && posNames.size() > 0){
				
				ActiveResp res= activeService.doActive(cuid,posNames);
				for(Active active : res.getActiveList()){
					Map<String, String> paremeterMap = new HashMap();
					paremeterMap.put("password", active.getPassword());
					paremeterMap.put("ponPort", active.getPonPort());
					paremeterMap.put("reason", active.getReason());
					paremeterMap.put("cuid", cuid);
					projectBO.updateHanging(paremeterMap);
				}
			}
			
			projectBO.updateResultStatus(cuid);
			
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
	
	
	@RequestMapping("/noHangingResult")
	public @ResponseBody String noHangingResult(String cuid){
		
		logger.info("将" + cuid + "跳过挂测");
		try {
			//查询当前工程状态
			Map<String,Object> map = projectBO.queryProjectByCode(cuid);
			String status = IbatisDAOHelper.getStringValue(map, "PRJ_STATUS");

			if(!"施工(驳回)".equals(status)){
				return "{\"success\":true,\"msg\":\"工程状态为:" + status + ",不能跳过挂测！\"}";
			}
			
			String xml = PojoBuilderFactory.getBuilder(ValidationBuilder.class).addParentPrjCode(IbatisDAOHelper.getStringValue(map, "PARENT_PRJ_CODE"))
				.addPrjCode(IbatisDAOHelper.getStringValue(map, "PRJ_CODE")).addteststatus("成功").addReturnType("2").toXml();
			
			SendMsgUtil.getInstance().send2Gx(xml);
			////将状态改为挂测跳过
			PrjStatus prjStatus = PojoBuilderFactory.getBuilder(PrjStatusBuilder.class).addCuid(cuid).addPrjStatus("挂测跳过").build();
			projectDAO.updateProjectStatus(prjStatus);
			
		} catch (Exception e) {
			logger.error("跳过挂测失败！",e);
			return "{\"success\":true,\"msg\":\"" + e.getMessage() + "\"}"; 
		}
		return "{\"success\":true,\"msg\":\"跳过挂测成功！\"}";
	}
	
	
}
