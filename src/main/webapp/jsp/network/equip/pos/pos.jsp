<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
  <head>
    <title>pos信息</title>
    
	<%@ include file="/commons/common.jsp"%>
	<%@ include file="/commons/dwr.jsp"%>
	<%@ include file="/commons/ext.jsp"%>
	<%@ include file="/cmp_include/grid.jsp"%>
	<%@ include file="/cmp_include/form.jsp"%>
	<script type="text/javascript" src="${ctx}/cmp_res/grid/ResGridPanel.js"></script>
	<script type="text/javascript" src="${ctx}/jsp/network/equip/pos/pos.js"></script>
	<script type="text/javascript">
		Ext.onReady(function() {
			var panel = new NETWORK.pos({});
			var view = new Ext.Viewport({
                layout : 'fit',
                items : [panel]
            });
		});
	</script>

  </head>
  
 
</html>