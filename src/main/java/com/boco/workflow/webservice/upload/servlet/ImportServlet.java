
package com.boco.workflow.webservice.upload.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.utils.lang.TimeFormatHelper;
import com.boco.workflow.webservice.upload.service.ImportResService;


@WebServlet(name="ImportServlet",urlPatterns="/ImportServlet")
public class ImportServlet extends HttpServlet {
	
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		String fileType = ".xls";
		
		String cuid = request.getParameter("cuid");
		
		try {
			IbatisDAO dao = (IbatisDAO)SpringContextUtil.getBean("IbatisDAO");
			List<Map> list = dao.querySql("select prj_status from t_wf_project where cuid = '" + cuid + "'");
			if(list == null || list.size() == 0){
				response.getWriter().write("{'success':true,'showMessage':'工程信息缺失！'}");
		    	return ;
			}
		    String status = list.get(0).get("PRJ_STATUS").toString();
		    if(!"施工".equals(status)){
		    	
		    	response.getWriter().write("{'success':true,'showMessage':'工程状态为【" + status + "】，不能进行资源导入！'}");
		    	return ;
		    }
			
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem>  fileItems = upload.parseRequest(request);
			FileItem fileitem = fileItems.get(0);
			if(fileitem != null){

				//解析文件导入
				String tempFile = TimeFormatHelper.getFormatDate(new Date(), TimeFormatHelper.TIME_FORMAT_B) + fileType;
				//路径+文件名称
				String upLoadFileName =  request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator + tempFile;
				//上传文件写到服务器上
				File file = new File(upLoadFileName);
				fileitem.write(file);
				Map<String,ImportResultDO> map = ((ImportResService) SpringContextUtil.getBean("importResService")).resolveExcel(file,cuid);
				
			
				
				Iterator<Entry<String, ImportResultDO>> iterator = map.entrySet().iterator();
				StringBuilder showMessage = new StringBuilder();
				String errorFileDownLoadUrl = "";
				while(iterator.hasNext()){
					Entry<String, ImportResultDO> entry = iterator.next();
					if(entry != null){
						showMessage.append(entry.getKey()+":");
						ImportResultDO importResultDO = entry.getValue();
						if(importResultDO != null){
							if(!importResultDO.isSuccess() && importResultDO.isShowFileUrl()){
								errorFileDownLoadUrl = (request.getSession().getServletContext().getContextPath() + File.separatorChar + "upload" + File.separatorChar + tempFile).replace("\\", "/");
							}
							List<String> infoList = importResultDO.getInfo();
							for(String msg : infoList){
								showMessage.append(msg+" ");
							}
						}
					}
				}
				response.getWriter().write("{'success':true,'errorFileDownLoadUrl':'" + errorFileDownLoadUrl + "','showMessage':'" + showMessage.toString() + "'}");
			}else{
				//读取文件失败
				response.getWriter().write("{'success':true,'showMessage':'导入文件失败,读取文件异常'}");
			}
		} catch (Exception e) {
			LogHome.getLog().error("导入文件失败", e);
			response.getWriter().write("{'success':true,'showMessage':'系统错误，请联系管理员！ at " + TimeFormatHelper.getFormatDate(new Date(), TimeFormatHelper.TIME_FORMAT_A) + "'}");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

}