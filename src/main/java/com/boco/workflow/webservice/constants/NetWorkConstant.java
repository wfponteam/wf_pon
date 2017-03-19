package com.boco.workflow.webservice.constants;

/**
 * 设备常量
 * @author gaoyang 2017年3月13日
 *
 */
public class NetWorkConstant {

	private NetWorkConstant() throws IllegalAccessException {
		throw new IllegalAccessException("不能创建此类");
	}
	
	public static final String EQUIP_SQL_MAP = "EQUIP";
	
	public static final String GPONCOVER="GPONCOVER";
	
	public static final String PROJECT_SQL_MAP = "PROJECT";
	/**
	 * 批量操作数量
	 */
	public static final int COMMIT_COUNT = 500;

	public static final String BUSINESSCOMMUNITY_SQL_MAP = "BUSINESSCOMMUNITY";

	public static final String FULLADDRESS_SQL_MAP = "FULLADDRESS";

	public static final String GPONCOVER_SQL_MAP = "GPONCOVER";
}
