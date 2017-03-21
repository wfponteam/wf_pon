Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/equip/onu/onu_query.js");
$importjs(ctx + "/jsp/network/equip/onu/onu_tbar.js");
$importjs(ctx + "/jsp/network/equip/equip_renderer.js");

NETWORK.onu= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.onu',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	prjcode     : '',
	
	initComponent : function() {
		this._initItems();
		NETWORK.onu.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			prjcode    : this.prjcode,
			rendererPlugin : 'equip_renderer',
			gridCfg : {
				boName : 'ponTemplateGirdBo',
				cfgParams : {
					templateId : 'ONU_MANAGE'
				}
			},
			queryPlugin : 'onu_query',
			tbarPlugin : 'onu_tbar',
			view : new Ext.grid.GridView({
				forceFit : true
			}),
			pageSize : 20,
			loadData : false
		});
		
		this.items = [this.rolePanel];
	}
});