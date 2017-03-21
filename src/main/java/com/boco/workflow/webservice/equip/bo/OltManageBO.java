package com.boco.workflow.webservice.equip.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.upload.service.ImportCommonMethod;

@Repository
public class OltManageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	
	public List getONUCuidByLabelCn(String labelCn){
		String sql = "SELECT CUID,FDN,RELATED_EMS_CUID,RELATED_VENDOR_CUID,RELATED_DISTRICT_CUID,'' AS RATION FROM t_attemp_AN_ONU WHERE LABEL_CN='"+labelCn+"' ";
		return this.IbatisDAO.querySql(sql);
	}
	
	public List getPOSCuidByLabelCn(String labelCn){
		String sql = "SELECT CUID,FDN,RELATED_EMS_CUID,RELATED_VENDOR_CUID,RELATED_DISTRICT_CUID, RATION FROM t_attemp_AN_POS WHERE LABEL_CN='"+labelCn+"'";
		return this.IbatisDAO.querySql(sql);
	}
	
	
	/*
	 * 根据机盘名称、端口编号查询端口
	 */
	public List isSameCardPtpExist(String cardName,String portNo,String neCuid ){
		String sql = "SELECT LABEL_CN FROM t_attemp_PTP WHERE  PORT_NO="+portNo+ ""
	            + " AND RELATED_CARD_CUID = (SELECT CUID FROM t_attemp_CARD WHERE LABEL_CN='"+cardName+"'"
	            		+ " AND RELATED_DEVICE_CUID = '"+neCuid+"' ) AND RELATED_NE_CUID='"+neCuid+"'";
		return this.IbatisDAO.querySql(sql);
	}
	
	/*
	 * 根据设备CUID，板块名称，判断板卡是否存在
	 */
	public List getCardByLabelCnAndNeCuid(String cardName,String neCuid){
		if(cardName!=null&&cardName.length()>0){
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_CARD WHERE LABEL_CN='"+cardName+"'  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}else{
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_CARD WHERE  1 =1  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}
	}
	
	/*
	 * 根据设备CUID，板块FDN，判断板卡是否存在
	 */
	public List getCardByFdnAndNeCuid(String cardFdn,String neCuid){
		if(cardFdn!=null&&cardFdn.length()>0){
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_CARD WHERE FDN='"+cardFdn+"'  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}else{
			return this.IbatisDAO.querySql("SELECT CUID,LABEL_CN FROM t_attemp_CARD WHERE  1 =1  AND RELATED_DEVICE_CUID='"+neCuid+"' ");
		}
	}
	
	/*
	 * 根据设备CUID，和板卡CUID，判断端口是否存在
	 */
	public List getPtpByLabelCnAndCardCuidAndNeCuid(String ptpName,String cardCuid,String neCuid){
		if(!ImportCommonMethod.isEmpty(ptpName)&&!ImportCommonMethod.isEmpty(cardCuid)&&!ImportCommonMethod.isEmpty(neCuid)){
			String sql = "SELECT CUID,LABEL_CN FROM t_attemp_PTP WHERE LABEL_CN='"+ptpName+"'"
					+ " AND RELATED_CARD_CUID='"+cardCuid+"' AND RELATED_NE_CUID='"+neCuid+"'";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}
	
	/*
	 * 判断板卡FND是否已经存在
	 */
	public List isExistCardFdn(String cardFdn){
		if(!ImportCommonMethod.isEmpty(cardFdn)){
			String sql = "SELECT CUID,LABEL_CN,RELATED_DEVICE_CUID FROM t_attemp_CARD WHERE FDN='"+cardFdn+"'";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}
	
	/*
	 * 判断端口FND是否已经存在
	 */
	public List isExistPtpFdn(String ptpFdn){
		if(!ImportCommonMethod.isEmpty(ptpFdn)){
			String sql = "SELECT CUID,LABEL_CN FROM t_attemp_PTP WHERE FDN='"+ptpFdn+"'";
			return this.IbatisDAO.querySql(sql);
		}else{
			return null;
		}
	}
		
}
