Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/equip/pos/pos_query.js");
$importjs(ctx + "/jsp/network/equip/equip_renderer.js");
$importjs(ctx + "/jsp/network/equip/pos/pos_tbar.js");
NETWORK.pos= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.pos',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	prjcode     : '',
	
	initComponent : function() {
		this._initItems();
		NETWORK.pos.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			rendererPlugin : 'equip_renderer',
			prjcode    : this.prjcode,
			gridCfg : {
				boName : 'ponTemplateGirdBo',
				cfgParams : {
					templateId : 'POS_MANAGE'
				}
			},
			queryPlugin : 'pos_query',
			tbarPlugin : 'pos_tbar',
			view : new Ext.grid.GridView({
				forceFit : true
			}),
			pageSize : 20,
			loadData : false
		});
		
		this.items = [this.rolePanel];
	}
});