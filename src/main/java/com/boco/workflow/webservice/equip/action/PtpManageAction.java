package com.boco.workflow.webservice.equip.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.boco.core.bean.SpringContextUtil;
import com.boco.workflow.webservice.equip.bo.CheckRelationBO;
import com.boco.workflow.webservice.equip.bo.PtpManageBO;

/**
 * 端口action
 * @author gaoyang 2017年3月14日
 *
 */
public class PtpManageAction {

	private PtpManageBO getPtpManageBO(){
		
		return (PtpManageBO)SpringContextUtil.getBean("ptpManageBO");
	}
	private CheckRelationBO getCheckRelationBO(){
		
		return (CheckRelationBO)SpringContextUtil.getBean("checkRelationBO");
	}
	
	/**
	 * 删除端口信息
	 * @param request
	 * @param cuidList
	 * @param devType
	 * @throws Exception
	 */
	public void deletePtpInfo(HttpServletRequest request, List<Map<String,String>> cuidList)throws Exception {
		
	    //检查是否在产品客户中占用
		getCheckRelationBO().isPtpProductRelated(cuidList);
		//检查是否被下联占用
		getCheckRelationBO().isPtpUpneRelated(cuidList);
		//删除
		getPtpManageBO().deletePtpInfo(cuidList);

	}
	
	/**
	 * 查询操作后端口单条的列表信息
	 * 
	 * @param request
	 * @param ptpCuid
	 */
	public Map<String,String> queryLoadPtpByCuid(HttpServletRequest request, String Cuid)throws Exception {
		
		return getPtpManageBO().queryLoadPtpByCuid(Cuid);
	}
	
	/**
	 * 查询单条端口信息
	 * 
	 * @param request
	 * @param ptpCuid
	 */
	public Map<String,Object> queryPtpByCuid(HttpServletRequest request,String cuid)throws Exception {
		
		return getPtpManageBO().queryPtpByCuid(cuid);
	}
	
	/**
	 * 新增端口信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	public void addPtpInfo(HttpServletRequest request, Map map,String devTable) throws Exception {
	
	
		getPtpManageBO().addPtpInfo(request,map,devTable);
	
	}	
	
	/**
	 * 修改端口数据信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	public void modifyPtpInfo(HttpServletRequest request, Map map,String devTable) throws Exception {
		
		getPtpManageBO().modifyPtpInfo(map,devTable);
	}
	
	/**
	 * 查询端口编号是否存在
	 * 
	 * @param request
	 * @param portNo
	 * @param relatedCardCuid
	 */
	public String isPtpNoExist(HttpServletRequest request, String portNo,String relatedNeCuid,String ptpCuid)throws Exception {
		return getPtpManageBO().isPtpNoExist(portNo,relatedNeCuid,ptpCuid);
	}
	
	/**
	 * 查询端口名称是否存在(以端口名称，所属设备CUID联合唯一)
	 * @param request
	 * @param name
	 * @param equiptCuid
	 * @param cardCuid
	 * @return
	 * @throws Exception
	 * @since 2016-05-14
	 * @author flp
	 */
	public String isPtpNameExist2(HttpServletRequest request, String name,String relatedNeCuid,String cuid)throws Exception {
		return getPtpManageBO().isPtpNameExist2(name,relatedNeCuid,cuid);
	}
			

	
}
