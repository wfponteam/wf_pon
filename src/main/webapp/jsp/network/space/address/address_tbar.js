Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/jsp/network/space/address/address_Panel.js");
$importjs(ctx + "/dwr/interface/FullAddressManageAction.js");


Frame.grid.plugins.tbar.address_tbar = Ext.extend(Object,{
		constructor : function(grid){
			this.grid = grid;
			Frame.grid.plugins.tbar.address_tbar.superclass.constructor.call(this);
			var returnCfg = [{
						text : '新增',
						iconCls : 'c_add',
						id	: 'FullAddressGridTBar.add',
						scope : this,
						handler : function() {
							this.showDetailView('', 'add', '新增标准地址', false, this,'');
						}
					}, {
						text : '修改',
						iconCls : 'c_pencil',
						id	: 'FullAddressGridTBar.modify',
						scope : this,
						handler : function() {
							var record = this.grid.getSelectionModel().getSelected();
							if (!Ext.isEmpty(record)) {
								if(this.grid.getSelectionModel().selections.length > 1){
									Ext.Msg.alert('提示', '只能选择一条数据修改!');
									return;
								}else{
									this.showDetailView(record, 'modify', '修改标准地址数据',
											false, this);
								}
							} else {
								Ext.Msg.alert('提示', '请选择需要修改的数据!');
								return;
							}
						}
					}, {
						text : '删除',
						iconCls : 'c_delete',
						id	: 'FullAddressGridTBar.delete',
						scope : this,
						handler : function() {
							var selections = this.grid.getSelectionModel()
									.getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
									var adrObj = {};
									adrObj['CUID'] = item.json.CUID;
									adrObj['LABEL_CN'] = item.json.LABEL_CN;
									cuids.push(adrObj);
								});
								// 关联删除的Flag
								DWREngine.setAsync(false);
								var taskName = '';
								var _scope = this;
								Ext.Msg.confirm('温馨提示', taskName + '确定删除选中的数据吗？',
										function(btn) {
											if (btn == 'yes') {
												FullAddressManageAction.deleteAddressInfo(cuids, function(_data) {
													if (!Ext.isEmpty(_data)) {
														Ext.Msg.alert('温馨提示', _data);
													}else{
														_scope.grid.getStore().remove(selections);
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
					}];
			return [returnCfg];
		},
		showDetailView : function(record,type,winTitlw,readOnly,object,oltCuid){
			var cuid = '';
			var adevType = '';
			if (!Ext.isEmpty(record)) {
				cuid = record.json.CUID;
			}
			var config = {
				winTitle : winTitlw,
				width : 800,
				height : 400,
				region : 'center',
				_cuid : cuid,
				_readOnly : readOnly,
				_type : type,
				_oltCuid:oltCuid
			};
		    openWindow('NETWORK.address_Panel', true, config, function(_result, _scope,
							_options) {
						if (!Ext.isEmpty(_result) && !Ext.isEmpty(type)) {
							DWREngine.setAsync(false);
							FullAddressManageAction.getAddressInfoByCuid(_result,function(_data){
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
		},
	});