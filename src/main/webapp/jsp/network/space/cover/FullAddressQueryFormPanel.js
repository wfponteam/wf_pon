Ext.namespace('NETWORK');

$importjs(ctx + "/jsp/network/space/cover/FullAddressGridQuery.js");

NETWORK.FullAddressQueryFormPanel = Ext.extend(Frame.grid.QueryGridPanel, {
	border : false,
	loadData : false,
	queryPlugin : 'FullAddressGridQuery',
	header : false,
	constructor : function(config) {
		config.gridCfg = {
			boName : 'XmlTemplateGridBO',
			cfgParams : {
				templateId : 'IRMS_GPON_COVER_FULL_ADDRESS_MANAGE'
			}/*,
			urlParams : {
				'RELATED_NET_DOMAIN_CUID' : {
					key : 'RELATED_NET_DOMAIN_CUID',
					type : 'string',
					relation : '=',
					value : this.domainCuid
				}
			}*/
		};
		config.gridConfig = {
			rendererPluginKeys : ['ResGridRenderer'],
			eventPluginKeys : ['ResGridEvent'],
			enableContextMenu : true,
			hasPageBar : true,
			hasBbar:false,
			pageSize : 30,
			totalNum : 30
		};
		NETWORK.FullAddressQueryFormPanel.superclass.constructor.call(this,config);
	},
	initComponent : function() {
		NETWORK.FullAddressQueryFormPanel.superclass.initComponent.call(this);
	},
	getResult : function() {
		var sels = this.grid.getSelectionModel().getSelections();
		if(sels.length ==0 ){
			Ext.Msg.alert("温馨提示", "请先选择标准地址！");
			return true;
		}
		return sels;
	}
});