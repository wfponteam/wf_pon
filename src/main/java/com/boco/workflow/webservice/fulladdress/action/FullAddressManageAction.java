package com.boco.workflow.webservice.fulladdress.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boco.workflow.webservice.fulladdress.bo.FullAddressManageBO;
import com.boco.common.util.debug.LogHome;
import com.boco.component.export.pojo.ExportFile;
import com.boco.component.export.pojo.ExportInfo;
import com.boco.core.bean.SpringContextUtil;

public class FullAddressManageAction {
	FullAddressManageBO  fullAddressManageBO = getFullAddressManageBO();
	private FullAddressManageBO getFullAddressManageBO() {
		return (FullAddressManageBO) SpringContextUtil.getBean("fullAddressManageBO");
	}
	/**
	 * 根据CUID查询标准地址
	 */
	 public Map getAddressInfoByCuid(HttpServletRequest request, String cuid)throws Exception {
			return fullAddressManageBO.getAddressInfoByCuid(cuid);
	 }
	/**
	 * 删除标准地址信息
	 */
	public String deleteAddressInfo(HttpServletRequest request, List<Map> cuidList)throws Exception {
		String showMessage = "";
		try {
			if (cuidList !=  null && cuidList.size()>0) {
				showMessage = fullAddressManageBO.deleteAddressInfo(cuidList);
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
	public String insertAddressInfo(HttpServletRequest request, Map<String,String> map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = fullAddressManageBO.insertAddressInfo(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("新增标准地址信息报错", e);
		}
		return msg;
	}
	/**
	 * 修改标准地址信息
	 */
	public String updateAddressInfo(HttpServletRequest request, Map<String,String> map) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()) {
				msg = fullAddressManageBO.updateAddressInfo(map);
				//2.修改标准地址归属的业务区归属时，修改产品客户表对应的RELATED_COMMUNITY_CUID
				fullAddressManageBO.updateRofhProductInfo(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("修改标准地址信息报错", e);
		}
		return msg;
	}
	/**
	 * 小区批量新增城市型标准地址信息
	 */
	public String batchInsertAddressInfo(HttpServletRequest request, Map<String,String> map,List<Map<String,String>> buildList) throws Exception {
		String msg = "";
		try {
			if (map != null && !map.isEmpty()&&buildList!=null&&buildList.size()>0) {
				msg = fullAddressManageBO.batchInsertAddressInfo(map,buildList);
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
		List<ExportFile> files = fullAddressManageBO.exportDataByModel(request, cuidList);
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
				msg = fullAddressManageBO.updateAddressBusinessInfo(cuidList,businessCuid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogHome.getLog().error("修改标准地址信息报错", e);
		}
		return msg;
	}
	/**
	 * 根据父获取子节点树
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getAllChildren(String parentId) throws Exception {
		return fullAddressManageBO.getAllChildren(parentId);
	}
}
