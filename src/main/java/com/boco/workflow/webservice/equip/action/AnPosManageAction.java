package com.boco.workflow.webservice.equip.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.spring.SysProperty;
import com.boco.core.utils.exception.UserException;
import com.boco.workflow.webservice.equip.bo.CheckRelationBO;
import com.boco.workflow.webservice.equip.bo.AnPosManageBO;

public class AnPosManageAction {
	
private static final Logger logger = Logger.getLogger(OnuManageAction.class);
	
	private CheckRelationBO getCheckRelationBO(){
		
		return (CheckRelationBO)SpringContextUtil.getBean("checkRelationBO");
	}
	
	private AnPosManageBO getAnPosManageBO() {
		return (AnPosManageBO)SpringContextUtil.getBean("posManageBO");
	}
	/**
	 * 删除POS信息
	 * 
	 * @param request
	 * @param cuidSet
	 */
	public String deletePosInfo(HttpServletRequest request, Set<String> cuidSet)throws Exception {
		String showMessage  = "";
		try {
			if (null != cuidSet && !cuidSet.isEmpty()) {
					List<String> devList = new ArrayList<String>();
					devList.addAll(cuidSet);
				 showMessage = getAnPosManageBO().deletePosinfo(cuidSet);
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
		}
		return showMessage;
	}

	/**
	 * 查询POS名称是否存在
	 * 
	 * @param request
	 * @param name
	 */
	public boolean isPosNameExist(HttpServletRequest request, String name)throws Exception {
		return getAnPosManageBO().isPosNameExist(name);
	}
	 /**
	 * 家客是否作为综资系统的功能模块
	 * 
	 * @param request
	 * @return
	 */
	public boolean isRelSystemIrms(HttpServletRequest request) {
		boolean isIrms = false;
		String relSystem = SysProperty.getInstance().getValue("REL_SYSTEM");
		if ("IRMS".equalsIgnoreCase(relSystem)) {
			isIrms = true;
		}
		return isIrms;
	}
	/**
	 * 新增POS信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	public String addPosInfo(HttpServletRequest request, Map map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = getAnPosManageBO().addPosInfo(map,this.isRelSystemIrms(request));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("新增POS设备信息报错", e);
		}
		return msg;
	}
	/**
	 * 查询单条POS信息
	 * 
	 * @param request
	 * @param cuid 
	 */
	 public Map getPosByCuid(HttpServletRequest request, String cuid)throws Exception {
			return getAnPosManageBO().getPosByCuid(cuid);
	 }
	/**
	 * 修改POS数据信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	
	public String modifyPosInfo(HttpServletRequest request, Map map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = getAnPosManageBO().modifyPosInfo(map,this.isRelSystemIrms(request));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("修改POS设备信息报错", e);
		}
		return msg;
	}
		/**
		 * 添加结束后查询列表新店家的单条POS信息
		 * 
		 * @param request
		 * @param cuid 
		 */
		 public Map queryAddLoadPosByCuid(HttpServletRequest request, String cuid)throws Exception {
				return getAnPosManageBO().queryAddLoadPosByCuid(cuid);
		 }
		 
		 
		 
	
		 
		 /**
			 * 判断所属上联设备下是否有所属上联端口
			 * 
			 * @param request
			 * @param cuid 
			 */
		 public String queryPortNameByOltCuid(HttpServletRequest request, String cuid)throws Exception {
				return  getAnPosManageBO().queryPortNameByOltCuid(cuid);
		 }
}
