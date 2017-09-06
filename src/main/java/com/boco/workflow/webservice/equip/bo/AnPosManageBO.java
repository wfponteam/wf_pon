package com.boco.workflow.webservice.equip.bo;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.dao.utils.BoUtil;
@Service
public class AnPosManageBO {
	@Autowired
	protected IbatisDAO IbatisDAO;
	private static final String EQUIP_SQL_MAP = "EQUIP";


	/**
	 * 根据传入的CUID的集合删除网元POS信息
	 * 
	 * @param cuidSet 
	 * @throws Exception
	 */
	public String deletePosinfo(Set<String> cuidSet) throws Exception {
		String showMessage = "";
		try {
			if (null != cuidSet && !cuidSet.isEmpty()) {
				List cuidList = new ArrayList();
				for (String cuid : cuidSet) {
					cuidList.add(cuid);
				}
				List list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP+".queryRelatedNeCuid",cuidList);
				if(!list.isEmpty()){
					showMessage = "该POS设备下存设备，不允许删除!";
				}
				if(showMessage.isEmpty()){
						deletePosinfo(cuidList);
				}
			}
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw e;
		}
		return showMessage;
	}
	/**
	 * 查询POS名称是否存在
	 * 
	 * @param name
	 */
	public boolean isPosNameExist(String name){
		if(!StringUtils.isEmpty(name)){
			List list = null;
			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".queryPosBylabelcn", name);
			if(null != list && !list.isEmpty() && list.size()>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/**
	 * 删除网元POS信息
	 * 
	 * @param cuid
	 * @throws Exception
	 */
	public void deletePosinfo(List cuidList) throws Exception {
		try {
				//删除POS关联板卡信息
				BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),EQUIP_SQL_MAP + ".deleteCardByNeCuid", cuidList);
				//删除POS关联覆盖范围信息
				BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),EQUIP_SQL_MAP + ".deleteGponCoverbyNeCuid", cuidList);
				//释放上联端口状态
				BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),EQUIP_SQL_MAP + ".updatePosPtpFree", cuidList);
				//删除端口
				BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),EQUIP_SQL_MAP + ".deleteDevPtp", cuidList);
				//删除POS网元信息
				BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),EQUIP_SQL_MAP + ".deletePosInfoByCuid", cuidList);
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw e;
		}
	}
     /**
	 * 新增POS信息
	 * 
	 * @param map
	 */
	public String addPosInfo(Map<Object,Object> map,boolean isRelSystemIrms) throws Exception{
		
		String cuid = CUIDHexGenerator.getInstance().generate("TRANS_ELEMENT");
		//插入的时候加入了一些定值
		map.put("DEV_CUID", "AN_POS" + "-" + cuid);
		map.put("CUID", cuid);
		map.put("GT_VERSION", 0);
		map.put("ISDELETE", 0);
		if(map.get("CREATE_TIME") == null){
			map.put("CREATE_TIME",new Timestamp(System.currentTimeMillis()));
		}else{
			java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
			Date date =  formatter.parse((String) map.get("CREATE_TIME") );
			map.put("CREATE_TIME", date);		}
		map.put("LAST_MODIFY_TIME",new Timestamp(System.currentTimeMillis()));
		if(map.get("SETUP_TIME") != null){
			java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
			Date date =  formatter.parse((String) map.get("SETUP_TIME") );
			map.put("SETUP_TIME", date);
		}
		String updevCuid = ObjectUtils.toString(map.get("RELATED_UPNE_CUID"));
		String updevPtpCuid = ObjectUtils.toString(map.get("RELATED_UPNE_PORT_CUID"));
		
		Map temp = getPosMapeByUpDevCuid(updevCuid);
		if(temp!=null){
			String neCuid = (String)temp.get("CUID");
			String upDevType = (String)temp.get("UP_DEV_TYPE");
			String LABEL_CN = (String)temp.get("LABEL_CN");
			if(upDevType.toUpperCase().equals("OLT")){
				map.put("RELATED_OLT_CUID",neCuid);
				map.put("RELATED_PORT_CUID",updevPtpCuid);
			}else{
				//获取上联设备
				Map OLtMap = null;
				List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosUpDevByName",LABEL_CN);
				if (list != null && list.size() > 0) {
					 OLtMap = list.get(0);
				}
				String OltCuid = (String)OLtMap.get("RELATED_OLT_CUID");
				String OltPortCuid = (String)OLtMap.get("RELATED_PORT_CUID");
				map.put("RELATED_OLT_CUID",OltCuid);
				map.put("RELATED_PORT_CUID",OltPortCuid);
			}
		}
		setPosFdnAndEms(map);

		String cabCuid = map.get("RELATED_CAB_CUID").toString();
		String relatedDistrictCuid = getRelatedDistrictCuidByCabCuid(cabCuid);
		map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
		
        String upNeCuid = map.get("RELATED_UPNE_CUID")+"";
        if(upNeCuid != null && upNeCuid.length()>1){
        	String posType = getPosTypeByUpDevCuid(upNeCuid);
        	if(posType != null && posType.length()>1){
        		map.put("POS_TYPE", posType);
        	}
        }
      //占用新端口
      String updatePtpState = "UPDATE PTP SET PORT_STATE = 2 WHERE CUID = '"+IbatisDAOHelper.getStringValue(map,"RELATED_UPNE_PORT_CUID")+"'";
      this.IbatisDAO.updateSql(updatePtpState);
		this.IbatisDAO.getSqlMapClientTemplate().insert(EQUIP_SQL_MAP + ".insertPosInfo", map);
		String neFdn = ObjectUtils.toString(map.get("FDN"));
		((OnuManageBO)SpringContextUtil.getBean("onuManageBO")).createCardInfo(cuid, 3);
		return "R"+cuid;
	}
	private String getPosTypeByUpDevCuid(String upNeCuid){
		String res= "";
		List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosTypeByUpDevCuid", upNeCuid);
		if(list!=null&&list.size()>0){
			res = list.get(0).get("POS_TYPE")+"";
		}
		return res;
	}
	private Map getPosMapeByUpDevCuid(String upNeCuid){
		String res= "";
		List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosTypeByUpDevCuid", upNeCuid);
		if (list != null && list.size() > 0) {
			Map m = list.get(0);
			return m;
		}
		return null;
	}
	//拼写FDN串
	private void setPosFdnAndEms(Map map){
		if(map.get("RELATED_OLT_CUID") != null){
			//boolean isTwo = isPosFdnTwo(map);
			String oltFdn = "";
			String relatedEmsCuid = null;
			String posLabelcn = null;
			String oltCuid = null;
			Map<Object, Object> listMap = new HashMap<Object, Object>();
			oltCuid = map.get("RELATED_OLT_CUID").toString();
			String sql = "SELECT RELATED_EMS_CUID,FDN AS OLT_FDN,1 AS DEV_TYPE FROM TRANS_ELEMENT WHERE CUID='"
					+ oltCuid
					+ "' AND SIGNAL_TYPE=9 AND CONFIG_TYPE=1 UNION ALL SELECT T.RELATED_EMS_CUID,(SELECT FDN FROM TRANS_ELEMENT WHERE CUID=T.RELATED_OLT_CUID)AS OLT_FDN,3 AS DEV_TYPE FROM T_ATTEMP_AN_POS T WHERE T.CUID='"
					+ oltCuid + "' ";
			List<Map> list = this.IbatisDAO.querySql(sql);
			if (list != null && list.size() > 0) {
				listMap = list.get(0);
				if(listMap.get("OLT_FDN")!=null){
					oltFdn = listMap.get("OLT_FDN").toString();
				}
				relatedEmsCuid = listMap.get("RELATED_EMS_CUID").toString();
				posLabelcn = map.get("LABEL_CN").toString();
				String posFdn = oltFdn + ":" + "POS=" + posLabelcn;
				map.put("FDN",posFdn);
				map.put("RELATED_EMS_CUID",relatedEmsCuid);
			}
		}
	}
	/**
	 * 查询Pos的FDN串是两层结构还是三层结构
	 * 
	 * @param name
	 */
	public boolean isPosFdnTwo(Map map){
		//先获取旧的FDN值，如果没有就到数据库查询
		Object fdnObject = map.get("FDN");
		String oldFdn = null;
		if(fdnObject!=null&&(fdnObject.toString()).length()>0){
			oldFdn = fdnObject.toString();
		}else{
			if(map.get("modify")!=null&&"true".equals(map.get("modify").toString())){
				List<String> listFdn = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP+".queryFdnByPosCuid",map.get("CUID"));
				if(listFdn!=null&&listFdn.size()>0){
					oldFdn = listFdn.get(0);
				}
			}
		}
		//根据FDN数据判断是否两层结构，如果是返回true
		if(oldFdn!=null&&oldFdn.indexOf((":"+"POS="))==-1){
				return true;
		}
		return false;
	}
	/**
	 * 查询单条POS信息
	 * 
	 * @param request
	 * @param cuid 
	 */
	 public Map<Object,Object> getPosByCuid(String cuid){
			List isList = null;
			Map<String,String> posMap = null;
			Map<Object,Object> returnMap = new HashMap<Object,Object>();
			List list = null;
			List topopList = null;
			if(!StringUtils.isEmpty(cuid)){
				list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP+".queryPosByCuid", cuid);
				if(list != null && list.size()>0){
					posMap = (Map)list.get(0);
					for(String key : posMap.keySet()) {
						if(key.startsWith("N_")) {
							String attr = StringUtils.substring(key, 2);
							String text = IbatisDAOHelper.getStringValue(posMap, key);
							String value = IbatisDAOHelper.getStringValue(posMap, attr);
							Map<String, String> relMap = new HashMap<String, String>();
							relMap.put("text", text);
							relMap.put("value", value);
							returnMap.put(attr, relMap);
						} else if(!returnMap.containsKey(key)) {
							returnMap.put(key, IbatisDAOHelper.getStringValue(posMap, key));
						}
					}
				}
				Map<String,String> roomMap = (Map<String,String>)returnMap.get("RELATED_ROOM_CUID");
				Map<String,String> siteMap = (Map<String,String>)returnMap.get("RELATED_SITE_CUID");
				if( roomMap != null && roomMap.get("text") != null){
					returnMap.put("RELATED_DISTRICT_CUID",returnMap.get("RELATED_ROOM_CUID"));
				}else if(siteMap != null && siteMap.get("text") != null){
					returnMap.put("RELATED_DISTRICT_CUID",returnMap.get("RELATED_SITE_CUID"));
				}
				if(returnMap.get("SETUP_TIME") != null){
					returnMap.put("SETUP_TIME", (String)returnMap.get("SETUP_TIME"));
				}
				if(returnMap.get("REPAIR_TIME") != null){
					returnMap.put("REPAIR_TIME", (String)returnMap.get("REPAIR_TIME"));
				}
				Map<String,String> oltMap = (Map<String,String>)returnMap.get("RELATED_UPNE_CUID");
				if(oltMap.get("value")!=null&&!"".equals(oltMap.get("value"))){
					Map param = new HashMap();
					Map topoMap = new HashMap();
					param.put("origCuid", cuid);
					param.put("destCuid", oltMap.get("value"));
				}
				return returnMap;
			}else{
				return null;
			}
		}
		/**
		 * 修改POS信息
		 * 
		 * @param map
		 */
		public String modifyPosInfo(Map map,boolean isRelSystemIrms) throws Exception{
			String posCuid = ObjectUtils.toString(map.get("CUID"));
			map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));
			if (map.get("CREATE_TIME").equals("")){
			map.put("CREATE_TIME", new Timestamp(System.currentTimeMillis()));
			}
				java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
				Date date =  formatter.parse((String) map.get("SETUP_TIME") );
				map.put("SETUP_TIME", date);
			setRelatedMapValuesNew(map, "AN_POS");
			map.put("modify", "true");
			String updevCuid = ObjectUtils.toString(map.get("RELATED_UPNE_CUID"));
			String updevPtpCuid = ObjectUtils.toString(map.get("RELATED_UPNE_PORT_CUID"));
			Map temp = getPosMapeByUpDevCuid(updevCuid);
			if(temp!=null){
				String neCuid = (String)temp.get("CUID");
				String upDevType = (String)temp.get("UP_DEV_TYPE");
				String LABEL_CN = (String)temp.get("LABEL_CN");
				if(upDevType.toUpperCase().equals("OLT")){
					map.put("RELATED_OLT_CUID",neCuid);
					map.put("RELATED_PORT_CUID",updevPtpCuid);
				}else{
					//获取上联设备
					Map OLtMap = getPosUpDevByName(LABEL_CN);
					String OltCuid = (String)map.get("RELATED_OLT_CUID");
					String OltPortCuid = (String)map.get("RELATED_PORT_CUID");
					map.put("RELATED_OLT_CUID",OltCuid);
					map.put("RELATED_PORT_CUID",OltPortCuid);
				}
			}
			String canallocatetouser = "";
			if(map.get("CAN_ALLOCATE_TO_USER") != null){
				canallocatetouser = map.get("CAN_ALLOCATE_TO_USER").toString();
			}
		    String related_ne_cuid =null;
		    related_ne_cuid = (String) map.get("RELATED_NE_CUID");
			String districtCuid = map.get("RELATED_DISTRICT_CUID")+"";
			if(districtCuid != null && districtCuid.length()>1){
					String cabCuid = map.get("RELATED_CAB_CUID").toString();
					String relatedDistrictCuid = getRelatedDistrictCuidByCabCuid(cabCuid);
					map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
			}
			String oldPort = IbatisDAOHelper.getStringValue(map, "oldPort");
			String newPort = IbatisDAOHelper.getStringValue(map,"RELATED_UPNE_PORT_CUID");
			if(oldPort == null || !oldPort.equals(newPort)){
				
				//释放旧端口
				if(StringUtils.isNotBlank(oldPort)){
					String updatePtpState = "UPDATE T_ATTEMP_PTP SET PORT_STATE = 1 WHERE CUID = '"+oldPort+"'";
					  this.IbatisDAO.updateSql(updatePtpState);
				}
				//占用新端口
				String updatePtpState = "UPDATE T_ATTEMP_PTP SET PORT_STATE = 2 WHERE CUID = '"+newPort+"'";
				  this.IbatisDAO.updateSql(updatePtpState);
			}
			this.IbatisDAO.getSqlMapClientTemplate().update(EQUIP_SQL_MAP + ".updatePosInfo", map);
			String neFdn = ObjectUtils.toString(map.get("FDN"));
			String neCuid = ObjectUtils.toString(map.get("CUID"));
			((OnuManageBO)SpringContextUtil.getBean("onuManageBO")).createCardInfo(neCuid, 3);
			
			///更新板卡和端口的名称
			this.IbatisDAO.updateSql("update card c  "+
                         " set c.label_cn = (select ap.labeL_cn|| '-' ||"+
                         "  substr(c.fdn,"+
                         " instr(c.fdn, 'slot=') + 5,"+
                         " instr(c.fdn, ':Equipment=1') - instr(c.fdn, 'slot=') - 5) || '-' ||"+
                         " nvl(trim(c.component_name), 'VBoard')"+
                         " from an_pos ap"+
                         " where ap.cuid = c.related_device_cuid) "+
                         " where c.related_device_cuid = '"+ posCuid +"'");
			this.IbatisDAO.updateSql("update ptp p " +
			       " set p.label_cn = ( select ap.label_cn ||'-'|| lpad(port_no, 2, 0)"+
			       "              from card ap"+
			       "            where ap.cuid = p.related_card_cuid) "+
			       "	where p.related_ne_cuid = '" + posCuid +"'" );
			
            return "R"+posCuid;
		}
		
		private List list(int i) {
			// TODO Auto-generated method stub
			return null;
		}
		//添加结束后查询列表新建的单条POS信息
		
		public Map queryAddLoadPosByCuid(String cuid){
			List list = null;
			Map returnMap = null;
			if(!StringUtils.isEmpty(cuid)){
				list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP+".queryAddLoadPosByCuid",cuid);
				if(list != null && !list.isEmpty() && list.size()>0){
					returnMap = (Map)list.get(0);
				}
			}
			return returnMap;
		}
		public List getPosPortCount(String neCuidStr){
			List list =  this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosPortCount", neCuidStr);
			return list;
		}
		private String getCardPashFdn(String cardFdn){
			String[] split = cardFdn.split("-");
			List<String> list = new ArrayList();
			String test = "^(0|[1-9][0-9]*)$";
			if(split.length == 3){
				for(int i=0;i<split.length;i++){
					if(!split[i].matches(test)){
						if("NA".equals(split[i])){
							list.add("-1");
						}else{
							list.add(split[i]);
						}
					}else{
						list.add(split[i]);
					}
				}
				return "/"+"rack="+list.get(0)
						+"/"+"shelf="+list.get(1)
						+"/"+"slot="+list.get(2);
			}else{
				return null;
			}
		}
		public String queryPortNameByOltCuid(String cuid){
			List list = null;
			String message="";
			if(!StringUtils.isEmpty(cuid)){
				list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP+".queryPortNameByOltCuid",cuid);
				if(list==null||list.size()==0){
					message="该所属上联设备没有空闲的下联端口";
				}
			}
			return message;
		}
		public String getCityCuidByFullAddresCuid(String addrCuid){
			String sql = "SELECT CITY FROM T_ROFH_FULL_ADDRESS  WHERE CUID = '" + addrCuid + "'";
			List list = this.IbatisDAO.querySql(sql);
			if(list != null && list.size() > 0){
				Map map = (Map)list.get(0);
				return map.get("CITY")==null?"":map.get("CITY").toString();
			}
			return null;
		}
		public String getRelatedDistrictCuidByCabCuid(String name){
			return this.IbatisDAO.getSqlMapClientTemplate().queryForList(
					EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid", name).get(0).toString();
		}
		
		public String getDevFdnByCuid(String neCuid){
			String devType = "";
			if(neCuid != null && neCuid.length()>1){
				String sql = "SELECT("
						+ "  SELECT FDN FROM AN_POS T WHERE T.CUID = '"+neCuid+"'"+
						" UNION ALL "
						+ " SELECT FDN FROM AN_ONU T WHERE T.CUID = '"+neCuid+"') AS FDN "
						+ " FROM DUAL ";
				List<Map> resList =  this.IbatisDAO.querySql(sql);
				if(resList!=null&&resList.size()>0){
					Map temp = resList.get(0);
					if(temp!=null){
						devType = temp.get("FDN")==null?"":temp.get("FDN").toString();
					}
				}
			}
			return devType;
		}
		/*
		 * 根据设备CUID，板块FDN，判断板卡是否存在
		 */
		public List getCardByFdnAndNeCuid(String cardFdn,String neCuid){
			if(cardFdn!=null&&cardFdn.length()>0){
				return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM T_ATTEMP_CARD WHERE FDN='"+cardFdn+"'  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
			}else{
				return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM T_ATTEMP_CARD WHERE  1 =1  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
			}
		}
		public static  String getCuidByClassName(String className) {
			return CUIDHexGenerator.getInstance().generate(className);
		}

		/*
		 * 根据设备CUID，判断端口是否存在
		 */
		public List getPtpByAndNeCuid(String neCuid){
			if(neCuid != null && neCuid.length()>1){
				String sql = "SELECT P.CUID,P.PORT_NO FROM T_ATTEMP_PTP P WHERE  RELATED_NE_CUID='"+neCuid+"'"
						+ " AND NOT EXISTS (SELECT 1 FROM T_ATTEMP_CARD C WHERE C.CUID = P.RELATED_CARD_CUID) ";
				return this.IbatisDAO.querySql(sql);
			}else{
				return null;
			}
		}
		/**
		 * 根据得到的RELATED_DISTRICT_CUID判断是区域、站点还是机房的值，在给map赋值(olt和onu共用一个)
		 * 
		 * @param RELATED_DISTRICT_CUID
		 * @throws Exception
		 */
		public void setRelatedMapValuesNew(Map map,String tableName) {
			if(!StringUtils.isEmpty(tableName)){
				String cuid = "";
				String posUpCuid = "";
				if(tableName.equals("TRANS_ELEMENT")){
					cuid = map.get("RELATED_ROOM_CUID")==null?"":map.get("RELATED_ROOM_CUID").toString();
				}else{
					if(tableName.equals("AN_POS")){
						cuid = map.get("RELATED_SPACE_CUID")==null?"":map.get("RELATED_SPACE_CUID").toString();
						posUpCuid = map.get("RELATED_OLT_CUID")==null?"":map.get("RELATED_OLT_CUID").toString();
					}
					if(tableName.equals("AN_ONU")){
						cuid = map.get("RELATED_SPACE_CUID")==null?"":map.get("RELATED_SPACE_CUID").toString();
					}
				}
				if (!StringUtils.isEmpty(cuid)) {
					List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getSpaceInfoByCuid",
							cuid);
					if(list!=null&&list.size()>0){
						Map mapInfo = list.get(0);
						map.put("RELATED_DISTRICT_CUID", mapInfo.get("RELATED_DISTRICT_CUID"));
						map.put("RELATED_SITE_CUID", mapInfo.get("RELATED_SITE_CUID"));
						map.put("RELATED_ROOM_CUID", mapInfo.get("RELATED_ROOM_CUID"));
						if(tableName.equals("AN_POS")||tableName.equals("AN_ONU")){
							map.put("RELATED_ACCESS_POINT", mapInfo.get("RELATED_ACCESS_POINT"));
						}
					}
				}
				if (!StringUtils.isEmpty(posUpCuid)) {
					List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getSpaceInfoByCuid",
							cuid);
					if(list!=null&&list.size()>0){
						Map mapInfo = list.get(0);
						map.put("RELATED_DISTRICT_CUID", mapInfo.get("RELATED_DISTRICT_CUID"));
						map.put("RELATED_SITE_CUID", mapInfo.get("RELATED_SITE_CUID"));
						map.put("RELATED_ROOM_CUID", mapInfo.get("RELATED_ROOM_CUID"));
						if(tableName.equals("AN_POS")||tableName.equals("AN_ONU")){
							map.put("RELATED_ACCESS_POINT", mapInfo.get("RELATED_ACCESS_POINT"));
						}
					}
				}
			}
		}
			public Map getPosUpDevByName(String nameValue) {
					List<Map> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(EQUIP_SQL_MAP + ".getPosUpDevByName",
									nameValue);
					if (list != null && list.size() > 0) {
						Map m = list.get(0);
						return m;
					}
				return null;
			}
			public void addTRofhFullAddress(String location,String related_ne_cuid){
				Map map = getTRofhFullAddressParams(location,related_ne_cuid);
				this.IbatisDAO.getSqlMapClientTemplate().insert(EQUIP_SQL_MAP + ".insertTRofhAddressInfo", map);
			}
			private Map getTRofhFullAddressParams(String location,String related_ne_cuid){
				Map map = new HashMap();
				String cuid = AnPosManageBO.getCuidByClassName("T_ROFH_FULL_ADDRESS");
				map.put("CUID",cuid);
				map.put("LABEL_CN",location);
				map.put("RELATED_BMCLASSTYPE_CUID","T_ROFH_FULL_ADDRESS");
				try{		
					String[] locationArray = location.split("\\|");
					if(locationArray != null && locationArray.length > 0){
						//生成业务区域
						map.put("RELATED_COMMUNITY_CUID", this.businessCommunity(location));
						map.put("PROVINCE",locationArray[0]);
						map.put("CITY",locationArray[1]);
						map.put("COUNTY",locationArray[2]);
						map.put("TOWN",locationArray[3]);
						map.put("VILLAGES",locationArray[4]);
						map.put("BUILDING",locationArray[5]);
						map.put("UNIT_NO",locationArray[6]);
						map.put("FLOOR_NO",locationArray[7]);
						map.put("ROOM_NO",locationArray[8]);
					}
				}catch(Exception ex){
					LogHome.getLog().warn("设备安装地址非标准地址格式,LOCATION:" + location,ex);
				}
				return map;
			}
			/*
			 * 业务区域生成
			 */
			public String businessCommunity(String location){
				String cuid = "";
				String[] locationArray = location.split("\\|");
				Map map = new HashMap();
				if(locationArray != null && locationArray.length > 0){
					cuid = AnPosManageBO.getCuidByClassName("BUSINESS_COMMUNITY");
					String labelCn = locationArray[2]+locationArray[4];
					String city = locationArray[1];
					String citySql = "SELECT CUID FROM DISTRICT WHERE LABEL_CN = '"+city+"'";
					List cityList = this.IbatisDAO.querySql(citySql);
					if(!cityList.isEmpty()){
						map.put("CITY", ((Map)cityList.get(0)).get("CUID").toString());
					}
					map.put("LABEL_CN",labelCn);
					map.put("ADDRESS", locationArray[0]+locationArray[1]+locationArray[2]+locationArray[3]+locationArray[4]);
					map.put("CUID", cuid);
					String isExistSql = "SELECT CUID FROM T_ATTEMP_BUSINESS_COMMUNITY WHERE LABEL_CN='"+labelCn+"'";
					List communityList = this.IbatisDAO.querySql(isExistSql);
					if(!communityList.isEmpty()){
						cuid = ((Map)communityList.get(0)).get("CUID").toString();
					}else{
						this.IbatisDAO.getSqlMapClientTemplate().insert(EQUIP_SQL_MAP + ".insertBusinessCommunityInfo", map);
					}
				}
				return cuid;
			}
		
}
