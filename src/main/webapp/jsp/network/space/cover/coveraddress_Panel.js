Ext.namespace('NETWORK');

NETWORK.coveraddress_Panel = Ext.extend(Frame.grid.DataGridPanel, {
	loadData : false,
	border : true,
	height:160,
	hasPageBar : false,
	hasBbar : false,
	constructor : function(config) {
		this.neCuid = config.neCuid;
		config.gridCfg = {
			boName : 'XmlTemplateGridBO',
			cfgParams : {
				templateId : 'IRMS_GPON_COVER_FULLADDRESS'
			}
		};
		NETWORK.coveraddress_Panel.superclass.constructor.call(this,config);
	},
	initComponent : function() {
		this.doQuery(this.getWhereParams());
		NETWORK.coveraddress_Panel.superclass.initComponent.call(this);
	},
	getWhereParams	:function(){
		var scope = this;
		if(Ext.isEmpty(scope.neCuid)||scope.neCuid==null){
			scope.neCuid = "-1000";
		}
    	var whereParams = {};
	
		whereParams['RELATED_NE_CUID'] = {
				key 		: 'RELATED_NE_CUID',
				value 		: scope.neCuid,
				relation 	: '='	
		}
    	return whereParams;
    }
});