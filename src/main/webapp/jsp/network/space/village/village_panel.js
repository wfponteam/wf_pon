$importjs(ctx + "/dwr/interface/BusinessCommunityManageAction.js");

Ext.ns('NETWORK');
NETWORK.village_panel = Ext.extend(
				Ext.Panel,
				{
					id : 'NETWORK.village_panel',
					region : 'center',
					border : false,
					frame : true,
					layout : 'form',
					autoScroll : true,
					_cuid : '',
					_adevType:'',
					_aDevCuid : '',
					_aDevName : '',
					_devEmsName : '',
					_returnFlag : '',
					_disabled : this._disabled ? this._disabled : true,
					_type : this._type ? this._type : '',
					_oltCuid : this._oltCuid?this._oltCuid:'',
					_returnJson : '',

					initComponent : function() {
						Ext.QuickTips.init();
						Ext.form.Field.prototype.msgTarget = 'side';
						// 判断是添加还是修改
						var isNeedValid = '';
						// 判断是否是第一次修改
						var modifyoltfalg = true;
						// 修改的初始化值
						var labelcn = '';

						if (this._type == 'add') {
							isNeedValid = 'add';
							this._disabled = false;
						}
						if (this._type == 'modify') {
							isNeedValid = 'modify';
							this._disabled = false;
						}
						var config = this;
						var emsValue;
						var scope = this;
						
						this.label_cn = new Ext.form.TextField( {
							fieldLabel : getNotNullFont('名称'),
							anchor : '90%',
							emptyText : '请填写业务区名称',
							allowBlank : false,
							name : 'LABEL_CN',
							invalidText : '名称已经存在',
							validator   : function(name) {
								var flag = true;
								if(isNeedValid == 'modify' && modifyoltfalg){
									labelcn = name;
									modifyoltfalg = false;
								}
								var cityCuid = config.city.getValue();
								if ((!Ext.isEmpty(name) && isNeedValid == 'add' && !Ext.isEmpty(cityCuid)) || (isNeedValid == 'modify' && labelcn != name && !Ext.isEmpty(cityCuid))) {
									DWREngine.setAsync(false);
									BusinessCommunityManageAction.isBcNameExist(name, cityCuid,function(_data) {
										flag =  !_data;
									});
									DWREngine.setAsync(true);
								}
								return flag;
							}
						});
						
						this.city = new IRMS.combo.AsynCombox({
							fieldLabel : getNotNullFont('地市'),
							name : 'CITY',
							emptyText : '请选择地市',
							anchor : '90%',
							blurMatch : 'both',
							allowBlank : false,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'DISTRICT_LABEL_CN_CUID'
								}
							}
						});
						
						this.address = new Ext.form.TextField( {
							fieldLabel : getNotNullFont('地址'),
							anchor : '90%',
							emptyText : '请填写地址',
							allowBlank : false,
							name : 'ADDRESS'
						});
						
						this.buildings_num  = new Ext.form.NumberField( {
							fieldLabel : '楼宇数',
							anchor : '90%',
							emptyText : '请填写楼宇数',
							name : 'BUILDINGS_NUM'
						});
						
						this.buildings_num.on('change',function(_this,newValue,oldValue){
							var re = /^[1-9]+[0-9]*]*$/;
						     if (!re.test(newValue))
						    {
						    	Ext.Msg.alert("提示","请填写正整数！");
						        _this.reset();
						        return false;
						     }
							
						},this);
						
						this.units_num  = new Ext.form.NumberField( {
							fieldLabel : '总单元数',
							anchor : '90%',
							emptyText : '请填写总单元数',
							allowBlank : true,
							disabled : false,
							name : 'UNITS_NUM'
						});
						
						this.units_num.on('change',function(_this,newValue,oldValue){
							var re = /^[1-9]+[0-9]*]*$/;
						     if (!re.test(newValue))
						    {
						    	Ext.Msg.alert("提示","请填写正整数！");
						        _this.reset();
						        return false;
						     }
						},this);
						
						this.households_num  = new Ext.form.NumberField( {
							fieldLabel : '总户数',
							anchor : '90%',
							emptyText : '请填写总户数',
							allowBlank : true,
							disabled : false,
							name : 'HOUSEHOLDS_NUM'
						});
						
						this.households_num.on('change',function(_this,newValue,oldValue){
							var re = /^[1-9]+[0-9]*]*$/;
						     if (!re.test(newValue))
						    {
						    	Ext.Msg.alert("提示","请填写正整数！");
						        _this.reset();
						        return false;
						     }
						},this);
						
						this.maintain_person  = new Ext.form.TextField( {
							fieldLabel : '维护责任人',
							anchor : '90%',
							emptyText : '请填写维护责任人',
							allowBlank : true,
							disabled : false,
							name : 'MAINTAIN_PERSON'
						});
						
						this.maintain_dept = new IRMS.combo.AsynCombox({
							fieldLabel : getNotNullFont('维护单位'),
							name : 'MAINTAIN_DEPT',
							anchor : '90%',
							emptyText : '请选择维护单位',
							blurMatch : 'both',
							allowBlank : false,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'CONSTRUCT_DEPT_COMBOX_1'
								}
							}
				        });
						
						this.warning_value = new Ext.form.NumberField( {
							fieldLabel : '预警阈值(%)',
							anchor : '90%',
							emptyText : '请填写预警阈值,范围0~100',
							allowBlank : true,
							name : 'WARNING_VALUE'
						});
						
						this.warning_value.on('change',function(_this,newValue,oldValue){
							var re = /^[1-9]+[0-9]*]*$/;
						     if (!re.test(newValue))
						    {
						    	Ext.Msg.alert("提示","请填写正整数！");
						        _this.reset();
						        return false;
						     }
						     
							if(newValue<0||newValue>100){
								_this.reset();
								Ext.Msg.alert("提示","预警阈值的范围为0~100！");
								return false;
							}
						},this);
						
						this.contact_number = new Ext.form.NumberField( {
							fieldLabel : getNotNullFont('联系电话'),
							anchor : '90%',
							emptyText : '请填写联系电话',
							allowBlank : false,
							name : 'CONTACT_NUMBER'
						});
						
						this.contact_number.on('change',function(_this,newValue,oldValue){
							var re = /^1\d{10}$/;
							    if (!re.test(newValue)) {
							    	_this.reset();
							    	Ext.Msg.alert("提示","请输入正确格式的联系方式！");
							    	return;
							    	
							    }
						},this);
					
						this.construct_dept = new IRMS.combo.AsynCombox({
							fieldLabel : '施工单位',
							name : 'CONSTRUCT_DEPT',
							anchor : '90%',
							emptyText : '请填写施工单位',
							blurMatch : 'both',
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'CONSTRUCT_DEPT_COMBOX'
								}
							}
						});
						
						this.is_priority = new IRMS.combo.AsynCombox({
							fieldLabel : '是否重点小区',
							name : 'IS_PRIORITY',
							anchor : '90%',
							emptyText : '请填写是否重点小区',
							blurMatch : 'both',
							allowBlank : true,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'BOOLEAN_COMBOX'
								}
							}
						});
						
						this.is_light_changed = new IRMS.combo.AsynCombox({
							fieldLabel : '是否光改小区',
							name : 'IS_LIGHT_CHANGED',
							anchor : '90%',
							emptyText : '请填写是否光改小区',
							blurMatch : 'both',
							allowBlank : true,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'BOOLEAN_COMBOX'
								}
							}
						});
						
						this.is_overlapp = new IRMS.combo.AsynCombox({
							fieldLabel : '是否重叠小区',
							name : 'IS_OVERLAPP',
							anchor : '90%',
							emptyText : '请填写是否重叠小区',
							blurMatch : 'both',
							allowBlank : true,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'BOOLEAN_COMBOX'
								}
							}
						});
						
						this.build_type = new IRMS.combo.AsynCombox({
							fieldLabel : getNotNullFont('小区类型'),
							name : 'BUILD_TYPE',
							anchor : '90%',
							emptyText : '小区类型',
							blurMatch : 'both',
							allowBlank : false,
							comboxCfg : {
								boName : 'XmlTemplateComboxBO',
								cfgParams : {
									code:'BUILD_TYPE_COMBOX'
								}
							}
						});
						
						this.east_longitude = new Ext.form.TextField( {
							fieldLabel : '东向经度',
							allowBlank : true,
							anchor : '90%',
							name : 'EAST_LONGITUDE'
						});	
						this.east_latitude = new Ext.form.TextField( {
							fieldLabel : '东向纬度',
							allowBlank : true,
							anchor : '90%',
							name : 'EAST_LATITUDE'
						});	
						this.west_longitude = new Ext.form.TextField( {
							fieldLabel : '西向经度',
							allowBlank : true,
							anchor : '90%',
							name : 'WEST_LONGITUDE'
						});	
						this.west_latitude = new Ext.form.TextField( {
							fieldLabel : '西向纬度',
							allowBlank : true,
							anchor : '90%',
							name : 'WEST_LATITUDE'
						});	
						this.south_longitude = new Ext.form.TextField( {
							fieldLabel : '南向经度',
							allowBlank : true,
							anchor : '90%',
							name : 'SOUTH_LONGITUDE'
						});	
						this.south_latitude = new Ext.form.TextField( {
							fieldLabel : '南向纬度',
							allowBlank : true,
							anchor : '90%',
							name : 'SOUTH_LATITUDE'
						});	
						this.north_longitude = new Ext.form.TextField( {
							fieldLabel : '北向经度',
							allowBlank : true,
							anchor : '90%',
							name : 'NORTH_LONGITUDE'
						});	
						this.north_latitude = new Ext.form.TextField( {
							fieldLabel : '北向纬度',
							allowBlank : true,
							anchor : '90%',
							name : 'NORTH_LATITUDE'
						});	
						
						this.cuid = new Ext.form.TextField( {
							name : 'CUID',
							hidden : true
						});
						this.showPonInfoPanel = new Ext.FormPanel( {
							region : 'center',
							border : false,
							labelAlign : 'left',
							frame : true,
							autoScroll : true,
							autoDestroy : true,
							items : [{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.label_cn ]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.city]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.address ]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.buildings_num]
								}]
							
								
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.units_num  ]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.households_num]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.maintain_person ]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.maintain_dept]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.warning_value ]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.contact_number]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [ this.construct_dept]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.is_priority]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.is_light_changed]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.is_overlapp]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.build_type]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.cuid]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.east_longitude]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.east_latitude]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.west_longitude]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.west_latitude]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.south_longitude]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.south_latitude]
								}]
							},{
								layout : 'column',
								items : [ {
									columnWidth : .5,
									layout : 'form',
									items : [this.north_longitude]
								},{
									columnWidth : .5,
									layout : 'form',
									items:[this.north_latitude]
								}]
							}
							]
						});
						this.bbar = [ '->', {
							xtype : 'button',
							iconCls : 'c_accept',
							text : '确定',
							scope : this,
							hideMode : 'offsets',
							hidden : this._disabled,
							handler : this.save
						}, {
							xtype : 'button',
							iconCls : 'c_cancel',
							text : '关闭',
							scope : this,
							handler : this.cancel
						} ];
						this.items = [  this.showPonInfoPanel ];
						this.initData();
						NETWORK.village_panel.superclass.initComponent.call(this);
					},
					initData : function() {
						var scope = this;
						if (!Ext.isEmpty(this._cuid)) {
							this.doRefresh();
						}
					},
					save : function() {
						var scope = this;
						if (this.showPonInfoPanel.getForm().isValid()) {
							if (!Ext.isEmpty(this._type)) {
								var obj = scope.getData();
								// 检查拓扑数据是否重复
										if (scope._type == 'modify') {
											BusinessCommunityManageAction.modifyBCInfo(obj,function(_data) {
												if (!Ext.isEmpty(_data)) {
													scope._returnJson = _data;
													if (scope.parent != null) {
														Ext.Msg.alert('提示','修改成功');
														scope.parent.close();
													}
												} else {
													Ext.Msg.alert('提示','修改失败 ');
													if (scope.parent != null) {
														scope.parent.close();
													}
												}
										    });
										} else if (scope._type == 'add') {
											BusinessCommunityManageAction.insertBCInfo(obj,function(_data) {
												if (!Ext.isEmpty(_data)){
													scope._returnJson = _data;
													if (scope.parent != null) {
														Ext.Msg.alert('提示','添加成功')
														scope.parent.close();
													}
												} else {
													Ext.Msg.alert('提示','添加失败');
													if (scope.parent != null) {
														scope.parent.close();
													}
												}
											});
										}
									
							}else{
								scope.cancel();
							}
						}
					},
					getData : function() {
						var LABEL_CN = this.showPonInfoPanel.getForm().findField('LABEL_CN').getValue();
						var CITY = this.showPonInfoPanel.getForm().findField('CITY').getValue();
						var ADDRESS = this.showPonInfoPanel.getForm().findField('ADDRESS').getValue();
						var BUILDINGS_NUM = this.showPonInfoPanel.getForm().findField('BUILDINGS_NUM').getValue();
						var UNITS_NUM = this.showPonInfoPanel.getForm().findField('UNITS_NUM').getValue();
						var HOUSEHOLDS_NUM = this.showPonInfoPanel.getForm().findField('HOUSEHOLDS_NUM').getValue();
						var MAINTAIN_PERSON = this.showPonInfoPanel.getForm().findField('MAINTAIN_PERSON').getValue();
						var MAINTAIN_DEPT = this.showPonInfoPanel.getForm().findField('MAINTAIN_DEPT').getValue();
						var WARNING_VALUE = this.showPonInfoPanel.getForm().findField('WARNING_VALUE').getValue();
						var CONTACT_NUMBER = this.showPonInfoPanel.getForm().findField('CONTACT_NUMBER').getValue();
						var CONSTRUCT_DEPT = this.showPonInfoPanel.getForm().findField('CONSTRUCT_DEPT').getValue();
						var IS_PRIORITY = this.showPonInfoPanel.getForm().findField('IS_PRIORITY').getValue();
						var IS_LIGHT_CHANGED = this.showPonInfoPanel.getForm().findField('IS_LIGHT_CHANGED').getValue();
						var IS_OVERLAPP = this.showPonInfoPanel.getForm().findField('IS_OVERLAPP').getValue();
						var BUILD_TYPE = this.showPonInfoPanel.getForm().findField('BUILD_TYPE').getValue();
						var EAST_LONGITUDE  = this.showPonInfoPanel.getForm().findField('EAST_LONGITUDE').getValue(); 
                        var EAST_LATITUDE   = this.showPonInfoPanel.getForm().findField('EAST_LATITUDE').getValue();                                              
                        var WEST_LONGITUDE  = this.showPonInfoPanel.getForm().findField('WEST_LONGITUDE').getValue(); 
                        var WEST_LATITUDE   = this.showPonInfoPanel.getForm().findField('WEST_LATITUDE').getValue();     
                        var SOUTH_LONGITUDE = this.showPonInfoPanel.getForm().findField('SOUTH_LONGITUDE').getValue(); 
                        var SOUTH_LATITUDE  = this.showPonInfoPanel.getForm().findField('SOUTH_LATITUDE').getValue();                                             
                        var NORTH_LONGITUDE = this.showPonInfoPanel.getForm().findField('NORTH_LONGITUDE').getValue(); 
                        var NORTH_LATITUDE  = this.showPonInfoPanel.getForm().findField('NORTH_LATITUDE').getValue();
						
						var CUID = "";
						if (!Ext.isEmpty(this.showPonInfoPanel.getForm().findField('CUID'))) {
							CUID = this.showPonInfoPanel.getForm().findField('CUID').getValue();
						}
						var bcData = {
								LABEL_CN : LABEL_CN,
								CITY : CITY,
								ADDRESS : ADDRESS,
								BUILDINGS_NUM : BUILDINGS_NUM,
								UNITS_NUM : UNITS_NUM,
								HOUSEHOLDS_NUM : HOUSEHOLDS_NUM,
								MAINTAIN_PERSON : MAINTAIN_PERSON,
								MAINTAIN_DEPT : MAINTAIN_DEPT,
								WARNING_VALUE : WARNING_VALUE,
								CONTACT_NUMBER : CONTACT_NUMBER,
								CONSTRUCT_DEPT : CONSTRUCT_DEPT,
								IS_PRIORITY : IS_PRIORITY,
								IS_LIGHT_CHANGED : IS_LIGHT_CHANGED,
								IS_OVERLAPP : IS_OVERLAPP,
								BUILD_TYPE	: BUILD_TYPE,
								CUID : CUID,
								RELATED_PROJECT_CUID   :Ext.getCmp('NETWORK.resource').cuid,
		                          EAST_LONGITUDE     :     EAST_LONGITUDE,                                                   
		                          EAST_LATITUDE      :     EAST_LATITUDE,  
		                          WEST_LONGITUDE     :     WEST_LONGITUDE, 
		                          WEST_LATITUDE      :     WEST_LATITUDE,  
		                          SOUTH_LONGITUDE    :     SOUTH_LONGITUDE,
		                          SOUTH_LATITUDE     :     SOUTH_LATITUDE, 
		                          NORTH_LONGITUDE    :     NORTH_LONGITUDE,
		                          NORTH_LATITUDE     :     NORTH_LATITUDE 
						};
						return bcData;
					},
					doRefresh : function() {
						var scope = this;
						var cuid = scope._cuid;
						DWREngine.setAsync(false);
						BusinessCommunityManageAction.queryBCInfoNoTransByCuid(cuid,
								function(data) {
									var record = Ext.data.Record.create( []);
									var _data = new record(data);
									scope.showPonInfoPanel.getForm().loadRecord(_data);
								});
						DWREngine.setAsync(true);
					},
					getResult : function() {
						return this._returnJson;
					},
					cancel : function() {
						if (this.parent != null) {
							this.parent.close();
							return null;
						}
					}
				});
Ext.reg('NETWORK.village_panel', NETWORK.village_panel);