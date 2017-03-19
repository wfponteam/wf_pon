package com.boco.workflow.webservice.upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.constants.Constant;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;



@Service
public class ImportResService {
	
	@Autowired
	private ImportBasicDataBO importBasicDataBO;
	
	Map<String,String> neModelCuidNameMap =null;
 	//解析excel
	public Map<String,ImportResultDO> resolveExcel(File file) throws Exception{
		
		FileInputStream inputStream = new FileInputStream(file);
		Map<String,ImportResultDO> map = new HashMap<String,ImportResultDO>();
		
		Workbook writeWorkBook = WorkbookFactory.create(inputStream);
		
		if(neModelCuidNameMap == null){
			neModelCuidNameMap = importBasicDataBO.getNeModelCfgCuidName();
		}
		Map<String,String> cardModelMap = importBasicDataBO.getCardKind();
		
		Sheet writeSheet =writeWorkBook.getSheet(Constant.POSNAME);
		ImportPosExcel importPosExcel=new ImportPosExcel(neModelCuidNameMap);	
		ImportResultDO resultDO = importPosExcel.importPosBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.POSNAME);
		map.put(Constant.POSNAME, resultDO);
		
		if(resultDO.isSuccess()){
			
			writeSheet =writeWorkBook.getSheet(Constant.POSPORTNAME);
			ImportPonCardPortExcel importPonCardPortExcel = new ImportPonCardPortExcel(cardModelMap);
			resultDO =  importPonCardPortExcel.importCardPortBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.POSPORTNAME);
			map.put(Constant.POSPORTNAME, resultDO);
		}
		
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet(Constant.ONUNAME);
			ImportOnuExcel importOnuExcel=new ImportOnuExcel(neModelCuidNameMap);
			resultDO = importOnuExcel.importOnuBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.ONUNAME);
			map.put(Constant.ONUNAME, resultDO);
		}
		
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet(Constant.ONUPORTNAME);
			ImportPonCardPortExcel importPonCardPortExcel=new ImportPonCardPortExcel(cardModelMap);
			resultDO = importPonCardPortExcel.importCardPortBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.ONUPORTNAME);
			map.put(Constant.ONUPORTNAME, resultDO);
		}
		
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet("业务区");
			BusinessCommunityImportExcel importExcel = new BusinessCommunityImportExcel();
			resultDO =  importExcel.importData(writeWorkBook, writeSheet, file.getAbsolutePath(), "业务区");
			map.put("业务区", resultDO);
		}
		
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet("业务区");
			BusinessCommunityImportExcel importExcel = new BusinessCommunityImportExcel();
			resultDO =  importExcel.importData(writeWorkBook, writeSheet, file.getAbsolutePath(), "业务区");
			map.put("业务区", resultDO);
		}
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet("标准地址");
			ImportAddressExcel importAddress = new ImportAddressExcel();
			resultDO =  importAddress.importData(writeWorkBook, writeSheet, file.getAbsolutePath(), "标准地址");
			map.put("标准地址", resultDO);
		}
		
		if(resultDO.isSuccess()){
			writeSheet =writeWorkBook.getSheet("覆盖范围");
			GponCoverImportExcel coverImportExcel = new GponCoverImportExcel();
			resultDO =  coverImportExcel.importData(writeWorkBook, writeSheet, file.getAbsolutePath(), "覆盖范围");
			map.put("覆盖范围", resultDO);
		}
		
		inputStream.close();
		FileOutputStream out=new FileOutputStream(file.getAbsolutePath());
		writeWorkBook.write(out);	
		out.close();
		
		return map;
	}

}
