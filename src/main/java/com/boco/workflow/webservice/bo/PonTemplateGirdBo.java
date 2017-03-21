package com.boco.workflow.webservice.bo;

import com.alibaba.fastjson.JSONObject;
import com.boco.component.bo.AbstractTemplateBO;
import com.boco.component.grid.bo.IGridBO;
import com.boco.component.grid.bo.IGridDetailBO;
import com.boco.component.grid.pojo.GridCfg;
import com.boco.component.grid.pojo.GridColumn;
import com.boco.component.grid.pojo.GridField;
import com.boco.component.grid.pojo.GridMeta;
import com.boco.component.grid.pojo.PropertyGridRecord;
import com.boco.component.query.pojo.WhereQueryItem;
import com.boco.component.tpl.ResConfigurer;
import com.boco.component.tpl.grid.pojo.GridTplConfig;
import com.boco.component.tpl.sql.pojo.SqlTplConfig;
import com.boco.component.utils.ComponentHelper;
import com.boco.component.utils.pojo.QuerySql;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.vo.PageQuery;
import com.boco.core.ibatis.vo.PageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PonTemplateGirdBo extends AbstractTemplateBO
  implements IGridBO, IGridDetailBO
{
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ResConfigurer ResConfigurer;
  @Autowired
  protected IbatisDAO IbatisDAO;

  protected ResConfigurer getResConfigurer()
  {
    return this.ResConfigurer;
  }

  public void setResConfigurer(ResConfigurer resConfigurer) {
    this.ResConfigurer = resConfigurer;
  }

  public IbatisDAO getIbatisDAO()
  {
    return this.IbatisDAO;
  }

  public void setIbatisDAO(IbatisDAO ibatisDAO) {
    this.IbatisDAO = ibatisDAO;
  }

  public GridMeta getGridMeta(GridCfg param) {
    String name = getTemplateId(param);
    GridTplConfig cfg = this.ResConfigurer.getGridTpl(name);
    if (cfg == null) {
      throw new RuntimeException("获取模版失败：查询模版" + name + ",返回结果为空！");
    }
    GridMeta gridMeta = new GridMeta();
    gridMeta.setBmClassId(cfg.getBmClassId());
    gridMeta.setCuid(cfg.getTemplateId());
    gridMeta.setPlugins(cfg.getPlugins());
    gridMeta.setGroupField(cfg.getGroupField());
    buildGridMeta(cfg, gridMeta);
    return gridMeta;
  }

  public PageResult<Object> getGridData(PageQuery queryParam, GridCfg param) {
    String name = getTemplateId(param);
    this.logger.debug("表格查询模板：" + name);
    GridTplConfig gridTpl = this.ResConfigurer.getGridTpl(name);
    SqlTplConfig sqlTpl = null;
    if (StringUtils.isNotBlank(gridTpl.getSql())) {
      sqlTpl = new SqlTplConfig();
      sqlTpl.setName(gridTpl.getTemplateId() + ".sql");
      sqlTpl.setDao(gridTpl.getSqlDao());
      sqlTpl.setSql(gridTpl.getSql());
    } else {
      sqlTpl = this.ResConfigurer.getSqlTpl(gridTpl.getSqlKey());
    }
    if (sqlTpl == null) {
      throw new RuntimeException("获取模版SQL失败：未定义key=" + gridTpl.getSqlKey() + "的sql！");
    }

    QuerySql querySql = new QuerySql(sqlTpl.getSql());
    querySql.setOrderBy(gridTpl.getOrderBy());
    querySql.setAc(param.getAc());
    querySql.setQueryParams(param.getQueryParams());
    querySql.setExtParams(param.getExtParams());
    querySql.setFilterParams(param.getFilterParams());
    String sql = creatQuerySql(querySql);

    if (gridTpl.isAllowDisControl()) {
      sql = super.addDisControl(param.getAc(), sql);
    }

    if (queryParam == null) {
      queryParam = new PageQuery(Integer.valueOf(0), Integer.valueOf(15));
    }
    if (StringUtils.isNotBlank(queryParam.getSort())) {
      String dir = queryParam.getDir();
      if (StringUtils.isBlank(dir)) {
        dir = "ASC";
      }
      sql = "SELECT * FROM (" + sql + ") ORDER BY " + queryParam.getSort() + " " + dir;
    }
    IbatisDAO dao = null;
    if (StringUtils.isNotBlank(sqlTpl.getDao()))
      dao = (IbatisDAO)SpringContextUtil.getBean(sqlTpl.getDao());
    else {
      dao = this.IbatisDAO;
    }
    PageResult page = null;

    String prjcode = param.getQueryParams().get("RELATED_PROJECT_CUID").getValue();
    List<Map> list = this.IbatisDAO.querySql("select prj_status from t_wf_project where prj_code = '" +prjcode+ "'");
    String status = list.get(0).get("PRJ_STATUS").toString();
    
    if("归档".equals(status) || "作废".equals(status)){
    	sql = sql.replace("$ns$", "T_HIS_");
    }else{
    	sql = sql.replace("$ns$", "T_ATTEMP_");
    }
    
    
    
    if (queryParam.getPageSize().intValue() == -1) {
      int num = (queryParam.getTotalNum() == null) || (queryParam.getTotalNum().intValue() == 0) ? 200 : queryParam.getTotalNum().intValue();
      PageQuery q = new PageQuery(Integer.valueOf(1), Integer.valueOf(150000));
      q.setTotalNum(Integer.valueOf(150000));
      page = dao.queryDynPage(sql, q);
    } else {
      page = dao.queryDynPage(sql, queryParam);
    }
    return page;
  }

  public PageResult getGridPageInfo(PageQuery queryParam, GridCfg param) {
    String name = getTemplateId(param);
    GridTplConfig gridTpl = this.ResConfigurer.getGridTpl(name);
    SqlTplConfig sqlTpl = null;
    if (StringUtils.isNotBlank(gridTpl.getSql())) {
      sqlTpl = new SqlTplConfig();
      sqlTpl.setName(gridTpl.getTemplateId() + ".sql");
      sqlTpl.setDao(gridTpl.getSqlDao());
      sqlTpl.setSql(gridTpl.getSql());
    } else {
      sqlTpl = this.ResConfigurer.getSqlTpl(gridTpl.getSqlKey());
    }
    if (sqlTpl == null) {
      throw new RuntimeException("获取模版SQL失败：未定义key=" + gridTpl.getSqlKey() + "的sql！");
    }

    QuerySql querySql = new QuerySql(sqlTpl.getSql());
    querySql.setOrderBy(gridTpl.getOrderBy());
    querySql.setAc(param.getAc());
    querySql.setQueryParams(param.getQueryParams());
    querySql.setExtParams(param.getExtParams());
    querySql.setFilterParams(param.getFilterParams());
    String sql = creatQuerySql(querySql);

    if (gridTpl.isAllowDisControl()) {
      sql = super.addDisControl(param.getAc(), sql);
    }

    IbatisDAO dao = null;
    if (StringUtils.isNotBlank(sqlTpl.getDao()))
      dao = (IbatisDAO)SpringContextUtil.getBean(sqlTpl.getDao());
    else {
      dao = this.IbatisDAO;
    }
    sql = "SELECT COUNT(*) FROM (" + sql + ")";
    Integer totalNum = dao.calculate(sql);
    PageResult page = new PageResult(null, totalNum.intValue(), queryParam.getCurPageNum().intValue(), queryParam.getPageSize().intValue());
    return page;
  }

  private void buildGridMeta(GridTplConfig cfg, GridMeta gridMeta) {
    String labelCn = cfg.getLabelCn();
    Map<String,String> columnNames = cfg.getColumnNames();
    Map<String,String> fieldNames = cfg.getFieldNames();
    Map<String,String> dColumnNames = cfg.getdColumnNames();
    JSONObject cm = (JSONObject)cfg.getCm();

    gridMeta.setLabelCn(labelCn);
    Map map = new HashMap();
    List columnList = new ArrayList();
    List fieldList = new ArrayList();
    List dColumnList = new ArrayList();
    Set fieldSet = new HashSet();
    gridMeta.setColumns(columnList);
    gridMeta.setFields(fieldList);
    gridMeta.setdColumns(dColumnList);
    if (fieldNames != null) {
      for (String fieldName : fieldNames.keySet()) {
        if (!fieldSet.contains(fieldName)) {
          GridField f = new GridField();
          f.setName(fieldName);
          f.setMapping(fieldName);
          fieldList.add(f);
          fieldSet.add(fieldName);
        }
      }
    }
    int i = 1;
    for (String columnName : columnNames.keySet()) {
      GridColumn c = new GridColumn();
      c.setId(columnName);
      c.setDataIndex(columnName);
      c.setHeader((String)columnNames.get(columnName));
      c.setTooltip((String)columnNames.get(columnName));
      Integer width = Integer.valueOf(150);
      if (cm != null) {
        JSONObject cmJo = cm.getJSONObject(columnName);
        if ((cmJo != null) && (cmJo.getInteger("width") != null)) {
          width = cmJo.getInteger("width");
        }
        c.setCm(cmJo);
      }
      c.setColIndex(Integer.valueOf(i++));
      c.setWidth(width);
      if (columnName.startsWith("REL_"))
        c.setDefine("renderer", "rendererRel");
      else if (columnName.startsWith("COUNT_")) {
        c.setDefine("renderer", "rendererCount");
      }
      Boolean queryDisabled = (Boolean)cfg.getQueryDisableds().get(columnName);
      c.setQueryDisabled((queryDisabled != null) && (queryDisabled.booleanValue() == true));
      columnList.add(c);
      map.put(columnName, c);
      if (!fieldSet.contains(columnName)) {
        GridField f = new GridField();
        f.setName(columnName);
        f.setMapping(columnName);
        fieldList.add(f);
        fieldSet.add(columnName);
      }
    }
    for (String columnName : dColumnNames.keySet()) {
      GridColumn c = new GridColumn();
      c.setId(columnName);
      c.setDataIndex(columnName);
      c.setHeader((String)dColumnNames.get(columnName));
      c.setTooltip((String)dColumnNames.get(columnName));
      c.setColIndex(Integer.valueOf(i++));
      dColumnList.add(c);
    }
  }

  protected String getTemplateId(GridCfg param) {
    if (param.getCfgParams() == null) {
      throw new RuntimeException("列表配置参数gridCfg.cfgParam为空！");
    }
    String templateId = (String)param.getCfgParams().get("templateId");
    return templateId;
  }

  public List<PropertyGridRecord> getGridDetail(GridCfg gridCfg, String cuid, String bmClassId) {
    List result = new ArrayList();
    GridMeta meta = getGridMeta(gridCfg);
    String name = getTemplateId(gridCfg);
    GridTplConfig gridTpl = this.ResConfigurer.getGridTpl(name);
    SqlTplConfig sqlTpl = null;
    if (StringUtils.isNotBlank(gridTpl.getDsql())) {
      sqlTpl = new SqlTplConfig();
      sqlTpl.setName(gridTpl.getTemplateId() + ".sql");
      sqlTpl.setDao(gridTpl.getDsqlDao());
      sqlTpl.setSql(gridTpl.getDsql());
    } else {
      sqlTpl = this.ResConfigurer.getSqlTpl(gridTpl.getSqlKey());
    }
    if (sqlTpl == null) {
      throw new RuntimeException("获取模版SQL失败：未定义key=" + gridTpl.getSqlKey() + "的sql！");
    }
    Map queryItems = new HashMap();
    WhereQueryItem cuidQ = new WhereQueryItem();
    cuidQ.setKey("CUID");
    cuidQ.setRelation("=");
    cuidQ.setValue(cuid);
    queryItems.put("CUID", cuidQ);

    QuerySql querySql = new QuerySql(sqlTpl.getSql());
    querySql.setOrderBy(gridTpl.getOrderBy());
    querySql.setAc(gridCfg.getAc());
    querySql.setQueryParams(queryItems);
    querySql.setExtParams(gridCfg.getExtParams());
    querySql.setFilterParams(gridCfg.getFilterParams());
    String sql = ComponentHelper.mergeQuerySql(querySql);

    IbatisDAO dao = null;
    if (StringUtils.isNotBlank(sqlTpl.getDao()))
      dao = (IbatisDAO)SpringContextUtil.getBean(sqlTpl.getDao());
    else {
      dao = this.IbatisDAO;
    }
    List<GridColumn> dColumnList = meta.getdColumns();
    List list = dao.querySql(sql);
    Map map;
    if (list.size() > 0) {
      map = (Map)list.get(0);
      for (GridColumn column : dColumnList) {
        String value = (String)map.get(column.getDataIndex());
        PropertyGridRecord r = new PropertyGridRecord();
        r.setLabel(column.getHeader());
        r.setName(column.getDataIndex());
        r.setValue(value);
        result.add(r);
      }
    }
    return result;
  }
  private String creatQuerySql(QuerySql querySql){
	  String sqlTemplate = querySql.getSqlTemplate();
	    if (StringUtils.isBlank(sqlTemplate)) {
	      return null;
	    }
	    String orderBy = querySql.getOrderBy();
	    String querySqlTemplate = sqlTemplate;
	    Map extParams = querySql.getExtParams();
	    if ((extParams == null) || (extParams.size() == 0)) {
	      extParams = new HashMap();
	    }

	    querySqlTemplate = ComponentHelper.mergeExtParams(querySqlTemplate, extParams);
	    querySqlTemplate = ComponentHelper.mergeCommonParams(querySqlTemplate, querySql.getAc());

	    querySqlTemplate = querySqlTemplate.replaceAll("\\{\\w*\\}", "");
	    boolean hasWhere = false;
	    boolean hasOrder = false;
	    boolean hasGroup = false;
	    String tempQuerySql = querySqlTemplate.replaceAll("\\(\\w*\\)", "").toLowerCase();
	    int last = tempQuerySql.lastIndexOf(" from ") + 1;
	    tempQuerySql = tempQuerySql.substring(last).toLowerCase();
	    if (tempQuerySql.indexOf(" where ") > -1) {
	      hasWhere = true;
	    }
	    if (tempQuerySql.indexOf(" order by ") > -1) {
	      hasOrder = true;
	    }

	    if (tempQuerySql.indexOf(" group by ") > -1) {
	      hasGroup = true;
	    }

	    StringBuffer innerQuerySql = new StringBuffer();
	    if (!hasWhere) {
	      innerQuerySql.append(" where 1=1 ");
	    }

	    Map queryParams = querySql.getQueryParams();
	    if ((queryParams == null) || (queryParams.size() == 0)) {
	      queryParams = new HashMap();
	    }
	    
	    ComponentHelper.mergeQueryParamsByAliais(innerQuerySql, queryParams);

	    String querySqlWithQuery = querySqlTemplate;
	    if ((hasOrder) || (hasGroup)) {
	      int splitIndex1 = querySqlTemplate.toLowerCase().lastIndexOf(" order by ");
	      int splitIndex2 = querySqlTemplate.toLowerCase().lastIndexOf(" group by ");
	      int splitIndex = 0;
	      if (splitIndex1 == -1)
	        splitIndex = splitIndex2;
	      else if (splitIndex2 == -1)
	        splitIndex = splitIndex1;
	      else {
	        splitIndex = splitIndex1 > splitIndex2 ? splitIndex2 : splitIndex1;
	      }
	      querySqlWithQuery = querySqlTemplate.substring(0, splitIndex) + innerQuerySql.toString() + querySqlTemplate.substring(splitIndex);
	    } else {
	      querySqlWithQuery = querySqlTemplate + innerQuerySql.toString();
	    }

	    StringBuffer sql = new StringBuffer();
	    sql.append(" M0 WHERE 1=1 ");

	    mergeQueryParams(sql, queryParams);

	    Map filterParams = querySql.getFilterParams();
	    ComponentHelper.mergeFilterParams(sql, filterParams);

	    if (StringUtils.isNotBlank(orderBy)) {
	      sql.append(" ORDER BY " + orderBy);
	    }
	    if (sql.length() > 0) {
	      return "SELECT * FROM ( " + querySqlWithQuery + ")/*追加参数：*/" + sql.toString() + "/**/";
	    }
	    return querySqlTemplate;
	}
    public static void mergeQueryParams(StringBuffer sql, Map<String, WhereQueryItem> queryParams)
    {
     if ((queryParams != null) && (!queryParams.isEmpty()))
      for (WhereQueryItem q : queryParams.values())
        if (q != null) {
          String alais = q.getAlias();
          if (StringUtils.isBlank(alais)){
            String sqlValue = q.getSqlValue(alais);
            String tempVal = q.getValue();
            if (StringUtils.isNotBlank(sqlValue)) {
              sql.append(" AND ");
              if(tempVal!=null&&tempVal.length()>1&&!"%%".equals(tempVal)){
            	 if("like".equalsIgnoreCase(q.getRelation())){
                   	if("%".equals(tempVal.substring(0,1))){
                   		tempVal = tempVal.replaceAll("%", "");
                   		sqlValue = " instr("+q.getKey()+",'"+tempVal+"')>0 ";
                   	}
                 }
              }
              sql.append(sqlValue);
            }
          }
        }
   }
}