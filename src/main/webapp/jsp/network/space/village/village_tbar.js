Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/jsp/network/space/village/village_panel.js");
$importjs(ctx + "/dwr/interface/BusinessCommunityManageAction.js");
Frame.grid.plugins.tbar.village_tbar = Ext.extend(Object,{
		constructor : function(grid){
			this.grid = grid;
			var oltCuid = grid.oltCuid;
			if(!Ext.isEmpty(oltCuid)){
				var whereParams = {};
				whereParams['RELATED_DEST_LOGIC_CUID'] = {
	    				key 		: 'RELATED_DEST_LOGIC_CUID',
						value 		:  oltCuid,
						relation 	: '='	
	    		}
				this.grid.doQuery(whereParams);
			}
			Frame.grid.plugins.tbar.village_tbar.superclass.constructor.call(this);
			var returnCfg =  [{
						text : '新增',
						iconCls : 'c_add',
						scope : this,
						id	: 'village_tbar.add',
						handler : function() {
							this.showDetailView('', 'add', '新增业务区',this);
						}
					}, {
						text : '修改',
						iconCls : 'c_pencil',
						scope : this,
						id	: 'village_tbar.modify',
						handler : function() {
							var record = this.grid.getSelectionModel().getSelected();
							if (!Ext.isEmpty(record)) {
								this.showDetailView(record, 'modify', '修改业务区', this);
							} else {
								Ext.Msg.alert('提示', '请选择需要修改的数据!');
								return;
							}
						}
					}, {
						text : '删除',
						iconCls : 'c_delete',
						scope : this,
						id	: 'village_tbar.delete',
						handler : function() {
							var selections = this.grid.getSelectionModel().getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
											cuids.push(item.json.CUID);
										});
								// 关联删除的Flag
								var relDelFlag = false;
								DWREngine.setAsync(false);
								var taskName = '';
								var _scope = this;
								Ext.Msg.confirm('提示', taskName + '确定删除选中的数据吗？',
										function(btn) {
											if (btn == 'yes') {
												BusinessCommunityManageAction.deleteBCInfo(cuids, function(_data) {
															_scope.grid.getStore().remove(selections);
															 
												});
											}
										});
								DWREngine.setAsync(true);
							} else {
								Ext.Msg.alert('提示', '请选择需要删除的数据!');
								return;
							}
						}
					}];
			

			
			return [returnCfg];
		},
		showDetailView : function(record,type,winTitlw,object){
			var cuid = '';
			var adevType = '';
			if (!Ext.isEmpty(record)) {
				cuid = record.json.CUID;
			}
			var config = {
				winTitle : winTitlw,
				width : 700,
				height : 330,
				region : 'center',
				_cuid : cuid,
				_type : type
			};
			
		openWindow('NETWORK.village_panel', true, config, function(_result, _scope,_options) {
				
					}, object, object.scope);
		}
	});
	
	