Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/space/cover/cover_query.js");
$importjs(ctx + "/jsp/network/space/cover/cover_tbar.js");
$importjs(ctx + "/jsp/network/space/cover/cover_renderer.js");

NETWORK.cover= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.cover',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	prjcode     : '',
	
	initComponent : function() {
		this._initItems();
		NETWORK.cover.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			rendererPlugin : 'cover_renderer',
			prjcode    : this.prjcode,
			gridCfg : {
				boName : 'ponTemplateGirdBo',
				cfgParams : {
					templateId : 'COVER_MANAGE'
				}
			},
			queryPlugin : 'cover_query',
			tbarPlugin : 'cover_tbar',
			view : new Ext.grid.GridView({
				forceFit : true
			}),
			pageSize : 20,
			loadData : false
		});
		
		this.items = [this.rolePanel];
	}
});