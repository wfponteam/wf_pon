package com.boco.workflow.webservice.upload.service;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class ImportResService {
	
	@Autowired
//	private ImportBasicDataBO basicDataBO;
	
	Map<String,String> neModelCuidNameMap =null;
 	//解析excel
	public void resolveExcel(File file) throws Exception{
		
	/*	FileInputStream inputStream = new FileInputStream(file);
		Map<String,ImportResultDO> map = new HashMap<String,ImportResultDO>();
		
		Workbook writeWorkBook = WorkbookFactory.create(inputStream);
		
		Sheet writeSheet =writeWorkBook.getSheet(Constant.ONUNAME);
		if(neModelCuidNameMap.isEmpty()){
			neModelCuidNameMap = basicDataBO.getNeModelCfgCuidName();
		}
		ImportOnuExcel importOnuExcel=new ImportOnuExcel(neModelCuidNameMap);
		map.put(Constant.ONUNAME, importOnuExcel.importOnuBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.ONUNAME));*/
			
	
	}

}
