package com.boco.workflow.webservice.fulladdress.bo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.boco.common.util.debug.LogHome;
import com.boco.component.export.pojo.ExportFile;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.spring.SysProperty;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.upload.constants.GetCh2Spell;
import com.boco.workflow.webservice.upload.service.ImportCommonMethod;

@Service

public class FullAddressManageBO {
	
	private static String[] dataColumns = { "LABEL_CN", "N_PROVINCE", "N_CITY", "N_COUNTY", "N_TOWN", "COMMUNITY", "ROAD", "ROAD_NUMBER", "VILLAGES", "VILLAGE_ALIAS", "BUILDING", "UNIT_NO", "FLOOR_NO", "ROOM_NO", "LONGITUDE", "LATITUDE", "ABBREVIATION", "PINYIN", "POSTCODE", "N_RELATED_COMMUNITY_CUID" };

	private static String BAR = "|";
	private static String BUILDING_NAME = "号楼";
	private static String UNIT_NAME = "单元";
	private static String FLOOR_NAME = "层";
	private static String ROOM_NAME = "房";
	@Autowired
	protected IbatisDAO IbatisDAO;
	
	private static final String FULLADDRESS_SQL_MAP = "FULLADDRESS";
	
	public IbatisDAO getIbatisDAO() {
		return IbatisDAO;
	}

	public void setIbatisDAO(IbatisDAO ibatisDAO) {
		IbatisDAO = ibatisDAO;
	}
	public Map<String,Object> getAddressInfoByCuid(String cuid) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".getAddressInfoByCuid",cuid);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public String insertAddressInfo(Map<String,String> map) throws Exception{
		String labelCn = map.get("LABEL_CN")==null?"":map.get("LABEL_CN");
		if(isExistAddressInfoByLabeCn(labelCn, null)){
			return "E该标准地址已存在！";
		}else{
			String cuid = CUIDHexGenerator.getInstance().generate("T_ROFH_FULL_ADDRESS");
			map.put("CUID", cuid);
			String pinyin=GetCh2Spell.getBeginCharacter(labelCn);
		  	map.put("PINYIN", pinyin);
			this.IbatisDAO.getSqlMapClientTemplate().insert(FULLADDRESS_SQL_MAP + ".insertAddressInfo", map);
			return "R"+cuid;
		}
	}

	public String batchInsertAddressInfo(Map<String,String> map,List<Map<String,String>> buildList) throws Exception{
		StringBuffer msg = new StringBuffer();
		List<Map> dataList = new ArrayList<Map>();
		int existNum = 0;
		int totalNum = 0;
		for(Map buildMap:buildList){
			String buildingName = buildMap.get("BUILDING")==null?"":buildMap.get("BUILDING").toString();
			String unitNoNum = buildMap.get("UNIT_NO")==null?"0":buildMap.get("UNIT_NO").toString();
			String floorNoNum = buildMap.get("FLOOR_NO")==null?"0":buildMap.get("FLOOR_NO").toString();
			String roomNoNum = buildMap.get("ROOM_NO")==null?"0":buildMap.get("ROOM_NO").toString();
			if(!ImportCommonMethod.isEmpty(buildingName)
					&&!ImportCommonMethod.isEmpty(unitNoNum)
					&&!ImportCommonMethod.isEmpty(floorNoNum)
					&&!ImportCommonMethod.isEmpty(roomNoNum)){
				int b = Integer.parseInt(unitNoNum);
				int c = Integer.parseInt(floorNoNum);
				int d = Integer.parseInt(roomNoNum);
				for(int j=1;j<=b;j++){
					for(int k=1;k<=c;k++){
						for(int r=1;r<=d;r++){
							Map<String,String> tempAdr = new HashMap<String,String>();
							tempAdr.putAll(map);
							tempAdr.put("BUILDING",buildingName);
							tempAdr.put("UNIT_NO", j+UNIT_NAME);
							tempAdr.put("FLOOR_NO",k+FLOOR_NAME);
							if(r<10){
								tempAdr.put("ROOM_NO", k+"0"+r+ROOM_NAME);
							}else{
								tempAdr.put("ROOM_NO", k+r+ROOM_NAME);
							}
							String labelCn = getAdrLabelCnByMap(tempAdr);
							if(!isExistAddressInfoByLabeCn(labelCn, null)){
								tempAdr.put("LABEL_CN",labelCn);
								tempAdr.put("CUID",CUIDHexGenerator.getInstance().generate("T_ROFH_FULL_ADDRESS"));
								
								if (map.get("PINYIN") == null || map.get("PINYIN").toString().trim().length() == 0) {
								  	String pinyin=GetCh2Spell.getBeginCharacter(labelCn);
								  	map.put("PINYIN", pinyin);
					            }
								dataList.add(tempAdr);
							}else{
								existNum++;
							}
							totalNum++;
						}
					}
				}
		   }
		}
		msg.append("R根据小区楼栋情况总计生成:"+totalNum+"条标准地址信息，");
		if(existNum>0){
			if(existNum<totalNum){
				msg.append("已存在"+existNum+"条，");
			}else{
				msg.append("已存在"+existNum+"条，不需要新增。");
			}
			
		}
		if(dataList.size()>0){
			this.IbatisDAO.getSqlMapClientTemplate().insert(FULLADDRESS_SQL_MAP + ".insertAddressInfo",dataList);
			msg.append("成功插入"+dataList.size()+"条。");
		}
		return msg.toString();
	}
	private String getAdrLabelCnByMap(Map<String,String> map){
		StringBuffer res = new StringBuffer(map.get("LABEL_CN"));
		if(map!=null&&map.size()>6){
			if(map.get("BUILDING")!=null){
				res.append(BAR);
				res.append(map.get("BUILDING"));
			}
			if(map.get("UNIT_NO")!=null){
				res.append(BAR);
				res.append(map.get("UNIT_NO"));
			}
			if(map.get("FLOOR_NO")!=null){
				res.append(BAR);
				res.append(map.get("FLOOR_NO"));
			}
			if(map.get("ROOM_NO")!=null){
				res.append(BAR);
				res.append(map.get("ROOM_NO"));
			}
		}
		return res.toString();
	}
	public String updateAddressInfo(Map<String,String> map) throws Exception{
		String labelCn = map.get("LABEL_CN")==null?"":map.get("LABEL_CN");
		String cuid = map.get("CUID")==null?"":map.get("CUID");
		
		String pinyin=GetCh2Spell.getBeginCharacter(labelCn);
	  	map.put("PINYIN", pinyin);
		
	  	this.IbatisDAO.getSqlMapClientTemplate().update(FULLADDRESS_SQL_MAP + ".updateAddressInfo", map);
	    //级联更新覆盖范围
	    updateGponCoverByAddress(map);
	    //级联更新业务区,暂时不处理
	    return "R"+cuid;
	}
	public String updateAddressBusinessInfo(List<String> cuidList,String businessCuid) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("CUIDS", cuidList);
		param.put("RELATED_COMMUNITY_CUID", businessCuid);
		this.IbatisDAO.getSqlMapClientTemplate().update(FULLADDRESS_SQL_MAP + ".updateAddressBusinessInfo", param);
	    return "";
	}
	
	public String updateRofhProductInfo(Map<String,String> map) throws Exception{
		map.put("BUSINESS_CITY_D",map.get("CITY"));
		map.put("BUSINESS_COUNTY_D",map.get("COUNTY"));
		map.put("RELATED_COVERAGE_ADDR_CUID",map.get("CUID"));
		this.IbatisDAO.getSqlMapClientTemplate().update(FULLADDRESS_SQL_MAP + ".updateRofhProductInfo", map);
	    return "";
	}
	
	private void updateGponCoverByAddress(Map map){
		this.IbatisDAO.getSqlMapClientTemplate().update(FULLADDRESS_SQL_MAP + ".updateGponCoverByAddress", map);
	}
	public String deleteAddressInfo(List<Map> cuidList) throws Exception{
		String msg = "";
		List<Map<String,String>> delCuidList = new ArrayList<Map<String,String>>();
		for (Map adrMap : cuidList) {
			String cuid = adrMap.get("CUID").toString();
			if(isRelatedAddress(cuid)){
				String labelCn = adrMap.get("LABEL_CN")==null?"":adrMap.get("LABEL_CN").toString();
				msg +="【"+labelCn+"】,";
			}else{
				delCuidList.add(adrMap);
			}
		}
		if(!ImportCommonMethod.isEmpty(msg)){
			msg = "标准地址："+msg+"，已被引用未删除";
		}
		if(delCuidList.size()>0){
			BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),FULLADDRESS_SQL_MAP + ".deleteAddressInfo", delCuidList);
			msg +="选择的【"+delCuidList.size()+"】条地址已被删除";
		}
		msg +="。";
		return msg;
	}
	private boolean isRelatedAddress(String cuid){
		boolean res = false;
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".selectRelatedAddressNum",cuid);
		if(list != null && list.size()>0){
			Map tempMap = list.get(0);
			if(tempMap.get("RELATED_NUM")!=null){
				int relNum = Integer.parseInt(tempMap.get("RELATED_NUM")+"");
				if(relNum>0){
					res  = true;
				}
			}
		}
		return res;
	}
	/**
	 * 判断标准地址是否已经存在
	 */
	public boolean isExistAddressInfoByLabeCn(String labelCn,String cuid) throws Exception{
		Map<String,Object> tempObj = selectAddressInfoByLabeCn(labelCn);
		if(tempObj!=null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 根据名称查询标准地址
	 */
	public Map<String,Object> selectAddressInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".selectAddressInfoByLabeCn",labelCn);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 判断区域是否已经存在
	 */
	public Map<String,Object> selectDistrictInfoByLabeCn(String labelCn,int dataType,String relatedSpaceCuid) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("LABEL_CN", labelCn);
		param.put("DATA_TYPE", dataType);
		if(!ImportCommonMethod.isEmpty(relatedSpaceCuid)){
			param.put("RELATED_SPACE_CUID", relatedSpaceCuid);
		}
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".selectDistrictInfoByLabeCn",param);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 判断业务区是否已经存在
	 */
	public Map<String,Object> selectBusinessCommunityInfoByLabeCn(String labelCn) throws Exception{
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".selectBusinessCommunityInfoByLabeCn",labelCn);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
   
	/**
	 * @param 将Map中值为空或为NULL转化空串
	 *            “”
	 */
	public Map<String, String> nullToString(Map<String, Object> obj) {
		Map<String, String> resMap = null;
		if (obj != null) {
			resMap = new HashMap<String, String>();
			for (String k : obj.keySet()) {
				Object v = obj.get(k);
				if (v == null || v.toString().length() == 0
						|| "null".equalsIgnoreCase(v.toString())) {
					resMap.put(k, "");
				} else {
					resMap.put(k, v.toString().trim());
				}
			}
		}
		return resMap;
	}
	public List<Map<String, Object>> selectAddressInfoByCuids(List<String> cuidList) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CUIDS", cuidList);
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP+".selectAddressInfoByCuids",map);
		if(list != null && list.size()>0){
			return list;
		}
		return null;
	}
	public List<Map<String, Object>> getAllChildren(String parentId) throws Exception{
		List<Map<String, Object>> list = null;
		HashMap<String, Object> para = new HashMap<String, Object>();
		
		if(!ImportCommonMethod.isEmpty(parentId)){
			String strs[] = parentId.split("\\|");
			para.put("parentId", strs[strs.length-1]);
			String sql = "";
			para.put("LABEL_CN", parentId);
			if(strs.length==1){
				//全国下省
				sql = ".getProvinceAllChildren";
			}
			if(strs.length==2 ){
				if(strs[0].equals(strs[1])){
					//省下市
					sql = ".getCityAllChildren";
				}else{
					sql = ".getCountyAllChildren";
				}
			}
			if(strs.length==3){
				//路
				sql = ".getTownAllChildren";
			}
			if(strs.length==4){
				//路
				sql = ".getRoadAllChildren";
			}
			if(strs.length==5){
				//路
				para.put("TOWN", strs[strs.length-2]);
				sql = ".getVillageAllChildren";
			
			}
			if(strs.length==6){
				//路
				para.put("TOWN", strs[strs.length-3]);
				para.put("ROAD", strs[strs.length-2]);
				sql = ".getBuildingAllChildren";
				
			}
			if(strs.length==7){
				//路
				para.put("TOWN", strs[strs.length-4]);
				para.put("ROAD", strs[strs.length-3]);
				para.put("VILLAGES", strs[strs.length-2]);
				sql = ".getUnitChildren";
			}
			
			if(strs.length==8){
				//路
				para.put("TOWN", strs[strs.length-5]);
				para.put("ROAD", strs[strs.length-4]);
				para.put("VILLAGES", strs[strs.length-3]);
				para.put("BUILDING", strs[strs.length-2]);
				sql = ".getFloorChildren";
			}
			if(!ImportCommonMethod.isEmpty(sql)){
				List<Map<String, Object>> tempList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(FULLADDRESS_SQL_MAP + sql, para);
				if(tempList!=null&&tempList.size()>0){
					list = new ArrayList<Map<String,Object>>();
					for(Map<String, Object> m:tempList){
						Map<String, Object> res = new HashMap<String, Object>();
						res.put("id", m.get("ID"));
						res.put("text", m.get("TEXT"));
						if("FALSE".equals(m.get("LEAF").toString())){
							res.put("leaf",false);
						}else{
							res.put("leaf",true);
						}
						list.add(res);
					}
				}
			}
		}
	    return list;
	}
}
