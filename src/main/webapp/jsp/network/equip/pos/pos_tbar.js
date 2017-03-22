Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/commons/utils/FrameHelper.js");
$importjs(ctx + "/dwr/interface/AnPosManageAction.js");
$importjs(ctx + "/jsp/network/equip/pos/pos_query.js");
$importjs(ctx + "/jsp/network/equip/pos/pos_tbar.js");
$importjs(ctx + "/jsp/network/equip/pos/pos_panel.js");
$importcss(ctx + "/jsp/component/batchmodify/css/customize.css");
$importjs(ctx + "/dwr/interface/BatchModifyAction.js");
$importjs(ctx + "/jsp/component/batchmodify/BatchModifyWin.js");
Frame.grid.plugins.tbar.pos_tbar = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.tbar.pos_tbar.superclass.constructor.call(this);
		var returnCfg = [
		 				{
							text : '新增',
							iconCls : 'c_add',
							id	:	'pos_tbar.add',
							scope : this,
							handler : function() {
								this.showDetailView('','add','新增POS数据',false,this);
							}
						},
						{
							text : '修改',
							iconCls : 'c_pencil',
							id	:	'pos_tbar.modify',
							scope : this,
							handler : function() {
								var record = this.grid.getSelectionModel().getSelected();
								if (!Ext.isEmpty(record)) {
									if(this.grid.getSelectionModel().selections.length > 1){
										Ext.Msg.alert('提示', '只能选择一条数据修改!');
										return;
									}else{
										this.showDetailView(record,'modify','修改POS数据',false,this);
									}
								} else {
									Ext.Msg.alert('提示', '请选择需要修改的数据!');
									return;
								}
							}
						},
						{
							text : '删除',
							iconCls : 'c_delete',
							id	:	'pos_tbar.delete',
							scope : this,
							handler : function() {
								var _scope = this;
								var selections = _scope.grid.getSelectionModel()
										.getSelections();
								if (selections.length > 0) {
									var cuids = [];
									Ext.each(selections, function(item) {
										cuids.push(item.json.CUID);
									});
									// 关联删除的Flag
									var relDelFlag = false;
									DWREngine.setAsync(false);
									var taskName = '';
									Ext.Msg.confirm('提示',taskName + '是否真的要删除选中的数据？',function(btn) {
										if (btn == 'yes') {
											AnPosManageAction.deletePosInfo(cuids,function(_data) {
												if(Ext.isEmpty(_data)){
													_scope.grid.getStore().remove(selections);
												}else{
													Ext.Msg.alert('提示',_data);
												}
											});
										}
									});
									DWREngine.setAsync(true);
								} else {
									Ext.Msg.alert('提示', '请选择需要删除的数据!');
									return;
								}
							}
						},
						];
		return [returnCfg];
	},
	showDetailView : function(record, type, winTitlw, readOnly, object) {
		var cuid = '';
		if(!Ext.isEmpty(record)){
			cuid = record.json.CUID;
		}
		DWREngine.setAsync(true);
		var config = {
			winTitle : winTitlw,
			width : 700,
			height : 400,
			region : 'center',
			_cuid : cuid,
			_readOnly : readOnly,
			_type : type,
		};
		openWindow('NETWORK.pos_panel', true, config, function(_result, _scope,_options) {
			if (!Ext.isEmpty(_result) && !Ext.isEmpty(type)) {
				DWREngine.setAsync(false);
				AnPosManageAction.queryAddLoadPosByCuid(_result,function(_data){
					if (!Ext.isEmpty(_data) && type == 'add'){
						object.grid.getStore().loadData( {
							'list' : [_data]
						}, true); 
					}else if(!Ext.isEmpty(_data) && type == 'modify'){
						object.grid.getStore().reload({
							'list' :[_data]
						}, false);
					}
				});
				DWREngine.setAsync(true);
			}
			
		}, object, object.scope);
	}

});