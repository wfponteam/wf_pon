package com.boco.workflow.webservice.equip.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.boco.workflow.webservice.equip.bo.BusinessCommunityManageBO;
import com.boco.common.util.debug.LogHome;
import com.boco.component.export.pojo.ExportFile;
import com.boco.component.export.pojo.ExportInfo;
import com.boco.core.bean.SpringContextUtil;

public class BusinessCommunityManageAction {
	BusinessCommunityManageBO  businessCommunityManageBO = getBusinessCommunityManageBO();
	private BusinessCommunityManageBO getBusinessCommunityManageBO() {
		return (BusinessCommunityManageBO) SpringContextUtil.getBean("BusinessCommunityManageBO");
	}
	/**
	 * 查询OLT名称是否存在
	 * 
	 * @param request
	 * @param name
	 */
	public boolean isBcNameExist(HttpServletRequest request, String name,String cityCuid )throws Exception {
		return businessCommunityManageBO.isBcNameExist(name,cityCuid);
	}
	/**
	 * 根据CUID查询标准地址
	 */
	 public Map queryLoadBCInfoByCuid(HttpServletRequest request, String cuid)throws Exception {
			return businessCommunityManageBO.getLoadBCInfoByCuid(cuid);
	 }
	 
	 public Map queryBCInfoNoTransByCuid(HttpServletRequest request, String cuid)throws Exception {
			return businessCommunityManageBO.getBCInfoNoTransByCuid(cuid);
	 }
	/**
	 * 删除标准地址信息
	 */
	public String deleteBCInfo(HttpServletRequest request, List cuidList)throws Exception {
		String showMessage = "";
		List paramList = new ArrayList();
		
		try {
			if (cuidList !=  null && cuidList.size()>0) {
				for(int i=0;i<cuidList.size();i++){
					Map param = new HashMap();
					param.put("CUID", cuidList.get(i));
					paramList.add(param);
				}
				showMessage = businessCommunityManageBO.deleteBCInfo(paramList);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("", e);
		}
		return showMessage;
	}
	/**
	 * 新增标准地址信息
	 */
	public String insertBCInfo(HttpServletRequest request, Map<String,String> map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = businessCommunityManageBO.insertBCInfo(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("新增业务区信息报错", e);
		}
		return msg;
	}
	
	
	/**
	 * 修改标准地址信息
	 */
	public String modifyBCInfo(HttpServletRequest request, Map<String,String> map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = businessCommunityManageBO.updateBCInfo(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("修改业务区信息报错", e);
		}
		return msg;
	}
	/**
	 * 小区批量新增城市型标准地址信息
	 */
	public String batchInsertAddressInfo(HttpServletRequest request, Map<String,String> map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = "";//fullAddressManageBO.batchInsertAddressInfo(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("新增标准地址信息报错", e);
		}
		return msg;
	}
	
	
	/**
	 * 数据导出
	 */
	public ExportInfo exportGridData(HttpServletRequest request,
			HttpServletResponse reponse, List<String> cuidList) throws Exception {
		long startTime = System.currentTimeMillis();
		List<ExportFile> files = null;//fullAddressManageBO.exportDataByModel(request, cuidList);
		ExportInfo info = new ExportInfo();
		info.setFiles(files);
		info.setSeconds(System.currentTimeMillis() - startTime);
		int total = 0;
		for (ExportFile f : files) {
			total += f.getNum();
		}
		info.setTotal(total);
		return info;
	}
	/**
	 * 批量更新标准地址所属业务区
	 */
	public String updateAddressBusinessInfo(HttpServletRequest request,
			HttpServletResponse reponse, List<String> cuidList,String businessCuid) throws Exception {
		String msg = "";
		try {
			if (cuidList != null&&cuidList.size()>0) {
				msg = "";//fullAddressManageBO.updateAddressBusinessInfo(cuidList,businessCuid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("修改标准地址信息报错", e);
		}
		return msg;
	}
}
