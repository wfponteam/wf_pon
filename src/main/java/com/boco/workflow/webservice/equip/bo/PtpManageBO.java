package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.upload.constants.AnFdnHelper;

/**
 * 端口管理类
 * @author gaoyang 2017年3月13日
 *
 */
@Service
public class PtpManageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	/**
	 * 更新端口状态
	 * @param state
	 * @param cuid
	 * @throws SQLException 
	 */
	public void updatePortState(String state,String cuid) throws SQLException{
		
		List<String> list = new ArrayList<String>();
		list.add(cuid);
		this.updatePortState(list,state);
	}
	
	public void updatePortState(List<String> ports,String state) throws SQLException{
		String sql = "1".equals(state) ? "updatePortStateFree":"updatePortStateOccu";
	
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP +"." + sql  , ports);
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP +"." + sql + "Att"  , ports);
	}

	/**
	 * 删除端口
	 * @param cuidList
	 * @throws SQLException 
	 */
	public void deletePtpInfo(List<Map<String,String>> cuidList) throws SQLException {

		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deletePtpByCuid",cuidList);
		
	}

	/**
	 * 查询端口
	 * @param cuid
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> queryLoadPtpByCuid(String cuid) throws SQLException {
		
		Map<String,String> returnMap = null;
		
		List<Map<String,String>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryLoadPtpByCuid",
				cuid);
		if (list != null && list.size() > 0) {
			
			returnMap =  list.get(0);
		}
		
		return returnMap;
		
	}

	/**
	 * 查询单条端口信息
	 * @param devTable
	 * @param cuid
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryPtpByCuid(String cuid) throws SQLException {
		
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryPtpByCuid",cuid);
		
		Map<String,Object> returnMap = BoUtil.convertResult2Panel(list);
		
		return returnMap;
			
	}
	
	
	/**
	 * 新增端口信息
	 * 
	 * @param map
	 * @throws Exception 
	 */
	public  void  addPtpInfo(HttpServletRequest request,Map map, String devTable) throws Exception {
		
		
		String _devTable = "";
		map.put("CUID", CUIDHexGenerator.getInstance().generate("PTP"));

		if (null != map && !map.isEmpty()) {
			
			String neCuid = ObjectUtils.toString(map.get("RELATED_NE_CUID"));
			
			if ("onu".equals(devTable)) {
				
				_devTable = "2";
			} else if ("pos".equals(devTable)) {
				_devTable = "3";
			} 
					
			map.put("DEV_TYPE", _devTable);
				
			//获取POS或ONU板卡信息
			
			String sql = "select * from(  SELECT FDN ,label_cn FROM t_attemp_AN_POS T WHERE T.CUID = '"+neCuid+"'"+
						" UNION ALL "
						+ " SELECT FDN,label_cn  FROM t_attemp_AN_ONU T WHERE T.CUID = '"+neCuid+"' )";
			List<Map<String,Object>> resList =  this.IbatisDAO.querySql(sql);
			String labelCn = IbatisDAOHelper.getStringValue(resList.get(0), "LABEL_CN");
			String neFdn = IbatisDAOHelper.getStringValue(resList.get(0), "FDN");
					
			Map<String,Object> cardmap=new HashMap<String,Object>();
			String cardCuid = CUIDHexGenerator.getInstance().generate("CARD");
			cardmap.put("CUID", cardCuid);
			cardmap.put("LABEL_CN", labelCn + "-1-VBoard");
			cardmap.put("RELATED_DEVICE_CUID", neCuid);
			String cardFdn = neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1:Equipment=1";
			cardmap.put("FDN", cardFdn);
			cardmap.put("RELATED_UPPER_COMPONENT_CUID", "EQUIPMENT_HOLDER-"+neFdn+":EquipmentHolder=/rack=1/shelf=1/slot=1");
			cardmap.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
			cardmap.put("GT_VERSION", 0);
			cardmap.put("ISDELETE", 0);
			cardmap.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
			cardmap.put("PROJECT_STATE", 0);
			cardmap.put("IS_PERMIT_SYS_DEL", 0);
			cardmap.put("SERVICE_STATE", 1);
			cardmap.put("USE_TYPE", 1);
			cardmap.put("OBJECT_TYPE_CODE", "5000");
			cardmap.put("LIVE_CYCLE", 1);
			cardmap.put("USE_STATE", 1);
			cardmap.put("PHOTOELECTRICITY", 1);
			cardmap.put("DEV_TYPE", _devTable);
			this.IbatisDAO.getSqlMapClient().insert(NetWorkConstant.EQUIP_SQL_MAP + ".insertCardInfo", cardmap);
				
						
					
			map.put("RELATED_CARD_CUID", cardCuid);
		//插入端口信息
    		map.put("SYS_NO", getPtpSysNo(map.get("PORT_NO")+"",cardFdn));
			String ptpFdn = AnFdnHelper.getPortFdnByCardFdnAndPortNo(cardFdn,Integer.parseInt(map.get("PORT_NO")+""));
			String ems = ptpFdn.split(":")[0];
			String labelcn = null;
			if(!StringUtils.isEmpty(ems)){
				labelcn =ems.contains("=") ?  ems.split("=")[1] : "";
			}//不处理空值
			if(!StringUtils.isEmpty(labelcn)){
				List list = (this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryEmsCuidByLabelcn",
						labelcn));
				if(list != null && list.size()>0){
					map.put("RELATED_EMS_CUID",list.get(0).toString());
				}
			}
		//	map.put("OBJECTID", objectId);
			map.put("GT_VERSION", 0);
			map.put("ISDELETE", 0);
			map.put("PROJECT_STATE", 0);
			map.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
			map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
			map.put("FDN", ptpFdn);
			map.put("IS_CHANNEL", 0);
			map.put("TERMINATION_MODE", 1);
			map.put("DIRECTIONALITY", 2);
			map.put("MSTP_NANFCMODE", 1);
			map.put("IS_PERMIT_SYS_DEL", 0);
			map.put("MSTP_PORT_TYPE", 1);
			map.put("MSTP_FLOWCTRL", 1);
			map.put("MSTP_ANFCMODE", 1);
			map.put("MSTP_LCAS_FLAG", 1);
			map.put("MSTP_PPTENABLE", 1);
			map.put("MSTP_EDETECT", 1);
			map.put("MSTP_TAG_FLAG", 1);
			map.put("MSTP_ENCAPFORMAT", 1);
			map.put("MSTP_ENCAPPROTOCOL", 1);
			map.put("MSTP_CFLEN", 1);
			map.put("MSTP_BMSGSUPPRESS", 1);
			map.put("MSTP_FCSCALSEQ", 1);
			map.put("LOOP_STATE", 1);
			map.put("MSTP_PORTENABLE", 1);
			map.put("DOMAIN", 0);
			map.put("MSTP_WORKMODE", 1);
			map.put("MSTP_PORT_SERVICETYPE", 1);
			map.put("MSTP_SCRAMBEL", 1);
			map.put("LINE_BRANCH_TYPE", 1);
			map.put("OBJECT_TYPE_CODE", 15013);
			map.put("MSTP_EXTENDEADER", 1);
			map.put("LIVE_CYCLE", 1);
			map.put("USE_STATE", 1);
			map.put("PORT_STATE", 1);
			map.put("PORT_TYPE", 1);

			this.IbatisDAO.getSqlMapClient().insert(NetWorkConstant.EQUIP_SQL_MAP + ".insertPtpInfo", map);
		
		} 
		
	}
	
	public static String getPtpSysNo(String ptpNo,String cardFdn){
		String res = "";
		String s1 = "";String s2 = "";String s3 = "";
		if(cardFdn!=null&&cardFdn.indexOf("rack=")!=-1&&cardFdn.indexOf("shelf=")!=-1&&cardFdn.indexOf("slot=")!=-1){
			s1 = cardFdn.substring(cardFdn.indexOf("rack=")+5,cardFdn.indexOf("shelf=")-1);
			s2 = cardFdn.substring(cardFdn.indexOf("shelf=")+6,cardFdn.indexOf("slot=")-1);
			s3 = cardFdn.substring(cardFdn.indexOf("slot=")+5,cardFdn.indexOf(":Equipment="));
			if(StringUtils.isEmpty(s1)||"-1".equals(s1)){
				s1 = "NA";
			}
			if(StringUtils.isEmpty(s2)){
				s2 = "NA";
			}
			if(StringUtils.isEmpty(s3)){
				s3 = "NA";
			}
		}
		res = s1+"-"+s2+"-"+s3+"-"+ptpNo;
		return res;
	}
	
	/**
	 * 修改端口信息
	 * 
	 * @param map
	 * @throws Exception 
	 */
	public void modifyPtpInfo(Map map, String devTable) throws Exception {

		if (null != map && !map.isEmpty()) {
			String cuid = map.get("CUID")==null?"":map.get("CUID").toString();
	
			String neCuid = ObjectUtils.toString(map.get("RELATED_NE_CUID"));
		
			//获取POS或ONU板卡信息
			Map  cardMap = null;
			
			String sql = "select c.cuid,c.fdn from t_attemp_card c,t_attemp_ptp p where c.cuid = p.RELATED_CARD_CUID and p.cuid = '" + cuid+ "'";
			List<Map<String,Object>> cardList = this.IbatisDAO.querySql(sql);
			cardMap = cardList.get(0);
			
			String cardFdn = ObjectUtils.toString(cardMap.get("FDN"));
			String cardCuid = ObjectUtils.toString(cardMap.get("CUID"));
		
		
			map.put("RELATED_CARD_CUID", cardCuid);

    		map.put("SYS_NO", getPtpSysNo(map.get("PORT_NO")+"",cardFdn));
    		String ems = cardFdn.split(":")[0];
    		String emsLabelcn = null;
			
					if ("onu".equals(devTable)) {
        				map.put("DEV_TYPE", "2");
        			} else if ("pos".equals(devTable)) {
        				map.put("DEV_TYPE", "3");
        			} 
					emsLabelcn = ems.contains("=") ? ems.split("=")[1] : "";
        			String ptpFdn = AnFdnHelper.getPortFdnByCardFdnAndPortNo(cardFdn,Integer.parseInt(map.get("PORT_NO")+""));
        			map.put("FDN", ptpFdn);
        			if(!StringUtils.isEmpty(emsLabelcn)){
    					List list = (this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryEmsCuidByLabelcn",
    							emsLabelcn));
    					if(list != null && list.size()>0){
    						map.put("RELATED_EMS_CUID",list.get(0).toString());
    					}//不处理空值
    		
    				}//不处理空值
        			map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
        			if(map.get("PORT_TYPE") == null || StringUtils.isEmpty(map.get("PORT_TYPE").toString())){
        				map.put("PORT_TYPE",0);
        			}

        			this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePtpInfo", map);
				
			
			
		}

	}
	
	/**
	 * 查询端口的编号是否存在
	 * 
	 * @param name
	 */
	public String isPtpNoExist(String portNo, String relatedNeCuid,String ptpCuid) {
		String res = "";
		if (!StringUtils.isEmpty(portNo) && !StringUtils.isEmpty(relatedNeCuid)) {
			List<Map> list = this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_PTP WHERE  PORT_NO="+portNo+" AND  RELATED_NE_CUID='"+relatedNeCuid+"' ");
			if (list != null &&list.size() > 0) {
				Map temp = list.get(0);
                if(ptpCuid.equals(temp.get("CUID").toString().trim())){
                	res = "NO";
                }else{
                	res = "YES";
                }
			} else {
				res = "NO";
			}
		}
		return res;
	}  
	
	/**
	 * 查询端口的编号是否存在
	 * 
	 * @param name
	 */
	public String isPtpNameExist2(String ptpName, String relatedNeCuid,String ptpCuid) {
		String res = "";
		if (!StringUtils.isEmpty(ptpName) && !StringUtils.isEmpty(relatedNeCuid)) {
			List<Map> list = this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_PTP WHERE  LABEL_CN = '"+ptpName+"' AND  RELATED_NE_CUID='"+relatedNeCuid+"' ");
			if (list != null &&list.size() > 0) {
				Map temp = list.get(0);
                if(ptpCuid.equals(temp.get("CUID").toString().trim())){
                	res = "NO";
                }else{
                	res = "YES";
                }
			} else {
				res = "NO";
			}
		}
		return res;
	}
}
