$importjs(ctx + "/jsp/anms/common/Util.js");
$importjs(ctx + "/dwr/interface/FullAddressManageAction.js");
Ext.ns('ANMS');
NETWORK.address_Panel = Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.address_Panel',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'form',
	autoScroll  : true,
	modifyflag	: false,
	_record 	: null,
	_returnFlag : '',
	_disabled 	: this._disabled ? this._disabled : true,
    _type		: this._type?this._type:'',
    _devTable	: '',
    _returnJson	: '',
    returnRecord: null,
	initComponent : function() {
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
		//判断是添加还是修改
		var isNeedValid = '';
		//判断是否是第一次修改
		var modifyoltfalg = true;
		//修改的初始化值
		var _devTable = this._devTable;
		var labelcn = '';
		var _scope = this;
		isNeedValid = 'modify';
		this._disabled = false;
		var config=this;
		this.labelcn = new Ext.form.TextField({
			name   		: 'LABEL_CN',
			fieldLabel	: getNotNullFont('地址名称'),
			readOnly    : true,
			emptyText   : "~名称自动生成",
			allowBlank  : false,
			anchor 	   	: '95%'
		});
		this.province = new  IRMS.combo.AsynCombox( {
			name 			: 'PROVINCE',
			fieldLabel 		: getNotNullFont('省份(第1级)'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_DISTRICT_PROVINCE'
				}
			}
		});
		this.city = new  IRMS.combo.AsynCombox( {
			name 			: 'CITY',
			fieldLabel 		: getNotNullFont('地市(第2级)'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_DISTRICT_CITY'
				}
			},
			listeners		: {
				"beforequery" : function(_this,newValue,oldValue){
					var spaceCuid = _scope.province.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(spaceCuid)){
						whereParams['RELATED_SPACE_CUID'] = {
			    				key : 'RELATED_SPACE_CUID',
								value : spaceCuid,
								relation : '='	
			    		}
					}else{
						Ext.Msg.alert('温馨提示','请先选择省份');
						return false;
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				} 
			}
		});
		this.county = new  IRMS.combo.AsynCombox( {
			name 			: 'COUNTY',
			fieldLabel 		: getNotNullFont('区/县(第3级)'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_DISTRICT_COUNTY'
				}
			},
			listeners		: {
				"beforequery" : function(_this,newValue,oldValue){
					var cityCuid = _scope.city.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(cityCuid)){
						whereParams['RELATED_SPACE_CUID'] = {
			    				key : 'RELATED_SPACE_CUID',
								value : cityCuid,
								relation : '='	
			    		}
					}else{
						Ext.Msg.alert('温馨提示','请先选择地市');
						return false;
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				} 
			}
		});
		this.town = new  IRMS.combo.AsynCombox( {
			name 			: 'TOWN',
			fieldLabel 		: getNotNullFont('镇乡/街道办(第4级)'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_DISTRICT_TOWN'
				}
			},
			listeners		: {
				"beforequery" : function(_this,newValue,oldValue){
					var countyCuid = _scope.county.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(countyCuid)){
						whereParams['RELATED_SPACE_CUID'] = {
			    				key : 'RELATED_SPACE_CUID',
								value : countyCuid,
								relation : '='	
			    		}
					}else{
						Ext.Msg.alert('温馨提示','请先选择区县');
						return false;
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				} 
			}
		});
		this.community = new Ext.form.TextField({
			name   		: 'COMMUNITY',
			fieldLabel	: '社区/行政村(第5级)',
			emptyText   : "~农村型填村名，城市型选填",
			anchor 	   	: '90%'
		});
		this.road = new Ext.form.TextField({
			name   		: 'ROAD',
			fieldLabel	: getNotNullFont('路/巷/街(第6级)'),
			emptyText   : "~城市型必填，农村型不填",
			allowBlank  	: false,
			anchor 	   	: '90%'
		});
		this.roadnumber = new Ext.form.TextField({
			name   		: 'ROAD_NUMBER',
			fieldLabel	: '门牌号码(第7级)',
			emptyText   : "~城市型必填，农村型不填",
			allowBlank  : true,
			anchor 	   	: '90%'
		});
		this.villages = new Ext.form.TextField({
			name   		: 'VILLAGES',
			fieldLabel	: getNotNullFont('小区/单位/学校/村组(第8级)'),
			allowBlank  : false,
			emptyText   : "~农村型和城市型都必填",
			anchor 	   	: '90%'
		});
		this.villagesalias = new Ext.form.TextField({
			name   		: 'VILLAGE_ALIAS',
			fieldLabel	: '小区别名(第9级)',
			emptyText   : "~城市型选填，农村型不填",
			anchor 	   	: '90%'
		});
		this.building = new Ext.form.TextField({
			name   		: 'BUILDING',
			fieldLabel	: getNotNullFont('楼栋号(第10级)'),
			emptyText   : "~城市型必填，农村型不填",
			allowBlank  : false,
			anchor 	   	: '90%'
		});
		this.unitno = new Ext.form.TextField({
			name   		: 'UNIT_NO',
			fieldLabel	: getNotNullFont('单元(第11级)'),
			emptyText   : "~城市型必填，农村型不填",
			allowBlank  : false,
			anchor 	   	: '90%'
		});
		this.floorno = new Ext.form.TextField({
			name   		: 'FLOOR_NO',
			fieldLabel	: getNotNullFont('楼层(第12级)'),
			emptyText   : "~城市型必填，农村型不填",
			allowBlank  : false,
			anchor 	   	: '90%'
		});
		this.roomno = new Ext.form.TextField({
			name   		: 'ROOM_NO',
			fieldLabel	: getNotNullFont('房号/村内编号(第13级)'),
			emptyText   : "~城市型填房间号，农村型填村内编号",
			allowBlank  : false,
			anchor 	   	: '90%'
		});
		this.abbreviation = new Ext.form.TextField({
			name   		: 'ABBREVIATION',
			fieldLabel	: '地址简称',
			anchor 	   	: '95%'
		});
		this.longitude = new Ext.form.NumberField( {
			name : 'LONGITUDE',
			fieldLabel : '经度',
			anchor : '90%'
		});
		this.latitude = new Ext.form.NumberField( {
			name : 'LATITUDE',
			fieldLabel : '纬度',
			anchor : '90%'
		});
		this.pinyin = new Ext.form.TextField({
			name   		: 'PINYIN',
			fieldLabel	: '地址拼音',
			anchor 	   	: '90%'
		});
		this.postcode = new Ext.form.TextField({
			name   		: 'POSTCODE',
			fieldLabel	: '邮政编码',
			anchor 	   	: '90%'
		});
		this.relatedcommunitycuid = new  IRMS.combo.AsynCombox( {
			name 			: 'RELATED_COMMUNITY_CUID',
			fieldLabel 		: getNotNullFont('所属业务区'),
			triggerAll 		: true,
			allowBlank  : false,
			hideTrigger1 	: true,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_BUSINESS_COMMUNITY'
				}
			}
		});
		
		this.regionType1 = new  IRMS.combo.AsynCombox( {
			name 			: 'REGIONTYPE1',
			fieldLabel 		: getNotNullFont('地域属性一级'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_REGION_TYPE_1'
				}
			},
			listeners		: {
				"beforequery" : function(_this,newValue,oldValue){
					_scope.regionType2.setValue('')
					return true;
				} 
			}
		});
		this.regionType2 = new IRMS.combo.AsynCombox( {
			name 			: 'REGIONTYPE2',
			fieldLabel 		: getNotNullFont('地域属性二级'),
			triggerAll 		: true,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'COMBO_REGION_TYPE_2'
				}
			},
			listeners		: {
				"beforequery" : function(_this,newValue,oldValue){
					var spaceCuid = _scope.regionType1.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(spaceCuid)){
						whereParams['RELATED_SPACE_CUID'] = {
			    				key : 'RELATED_SPACE_CUID',
								value : spaceCuid,
								relation : '='	
			    		}
					}else{
						Ext.Msg.alert('温馨提示','请先选择地域属性一级分类');
						return false;
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				} 
			}
		});
		
		
		//隐藏属性CUID
		this.cuid = new Ext.form.TextField( {
			name : 'CUID',
			hidden : true
		});
		this.showEquipmentInfoPanel = new Ext.FormPanel({
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			items : [{
				layout : 'column',
				items:[{
					columnWidth : 1,
					layout : 'form',
					items:[this.labelcn]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.province]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.city]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.county]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.town]
				}]
			},{
					layout : 'column',
					items:[{
						columnWidth : .5,
						layout : 'form',
						items:[this.community]
					},{
						columnWidth : .5,
						layout : 'form',
						items:[this.road]
					}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.roadnumber]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.villages]
				}]
		   },{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.villagesalias]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.building]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.unitno]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.floorno]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.roomno]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.longitude]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.latitude]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.abbreviation]
				}]
			},{
				   layout : 'column',
					items:[{
						columnWidth : 1,
						layout : 'form',
						items:[this.postcode]
					}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.regionType1]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.regionType2]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedcommunitycuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.cuid]
				}]
			}]
		});
		var filedSet1 = new Ext.form.FieldSet({
			title 			: '标准地址属性（地址区分：城市型和农村型，请参考灰色提示信息填写）',
			collapsible 	: true,
			frame			: false,
			region			: 'center',
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			items			: [this.showEquipmentInfoPanel]
		});
		this.bbar = [ 
				'->',{
					xtype 		: 'button',
					iconCls 	: 'c_accept',
					text 		: '确定',
					scope		: this,
					hideMode	: 'offsets',
					hidden		: this._disabled,
					handler 	: this.save
				}, {
					xtype 	: 'button',
					iconCls : 'c_cancel',
					text 	: '关闭',
					scope 	: this,
					handler : this.cancel
				} 
		];
		this.items = [filedSet1];
		this.initData();
		NETWORK.address_Panel.superclass.initComponent.call(this);
	},
	//修改赋初值
	initData : function() {
		if (!Ext.isEmpty(this._cuid)) {
			this.doRefresh();
		}
		this.on('afterrender', this._bindEvents, this);
	},
	_bindEvents :function() {
		this.province.on('change',function(){
			this.updateLabelCn(1);
		},this);
		this.city.on('change',function(){
			this.updateLabelCn(2);
		},this);
		this.county.on('change',function(){
			this.updateLabelCn(3);
		},this);
		this.town.on('change',function(){
			this.updateLabelCn(4);
		},this);
		this.community.on('change',function(){
			if(Ext.isEmpty(this.town.getValue())){
				Ext.Msg.alert('温馨提示','第4级镇乡/街道办为空，不允许填写第5级社区/行政村。',function(){
					this.community.setValue('');
				},this);
				return;
			}
			this.updateLabelCn(5);
		},this);
		this.road.on('change',function(){
			this.updateLabelCn(6);
		},this);
		this.roadnumber.on('change',function(){
			if(Ext.isEmpty(this.road.getValue())){
				Ext.Msg.alert('温馨提示','第6级路/巷/街为空，不允许填写第7级门牌号码。',function(){
					this.roadnumber.setValue('');
				},this);
				return;
			}
			this.updateLabelCn(7);
		},this);
		this.villages.on('change',function(){
			this.updateLabelCn(8);
		},this);
		this.villagesalias.on('change',function(){
			if(Ext.isEmpty(this.villages.getValue())){
				Ext.Msg.alert('温馨提示','第8级小区/单位/村组为空，不允许填写第9级别名。',function(){
					this.villagesalias.setValue('');
				},this);
				return;
			}
			this.updateLabelCn(9);
		},this);
		this.building.on('change',function(){
			this.updateLabelCn(10);
		},this);
		this.unitno.on('change',function(){
			if(Ext.isEmpty(this.building.getValue())){
				Ext.Msg.alert('温馨提示','第10级楼栋号为空，不允许填写第11级单元号。',function(){
					this.unitno.setValue('');
				},this);
				return;
			}
			this.updateLabelCn(11);
		},this);
		this.floorno.on('change',function(){
			if(Ext.isEmpty(this.unitno.getValue())){
				Ext.Msg.alert('温馨提示','第11级单元号为空，不允许填写第12级楼层。请逐级填写。',function(){
					this.floorno.setValue('');
				},this);
				return;
			}
			this.updateLabelCn(12);
		},this);
		this.roomno.on('change',function(){
			this.updateLabelCn(13);
		},this);
	},
    save:function(){
		var scope = this;
		if(this.showEquipmentInfoPanel.getForm().isValid()){
			if(!Ext.isEmpty(this._type)){
	    		if(this._type=='modify'){
					var addressInfo = scope.getInfoData();
					addressInfo.CUID = this._cuid;
					FullAddressManageAction.updateAddressInfo(addressInfo,function(_data){
						if (!Ext.isEmpty(_data)){
							var flag = _data.substr(0,1);
							_data = _data.substr(1,_data.length);
							if(flag=='E'){
								Ext.Msg.alert('提示',_data);
							}else if(flag=='R'){
								Ext.Msg.alert('提示','修改成功');
								if(scope.parent != null){
									scope._returnJson = _data;
									scope.parent.close();
								}
							}else{
								Ext.Msg.alert('提示','修改失败！');
							}
						}else{
							Ext.Msg.alert('提示','修改失败！');
						}
	    			});
	    		}else if(this._type=='add'){
					var addressInfo = scope.getInfoData();
					FullAddressManageAction.insertAddressInfo(addressInfo, function(_data) {
						if (!Ext.isEmpty(_data)){
							var flag = _data.substr(0,1);
							_data = _data.substr(1,_data.length);
							if(flag=='E'){
								Ext.Msg.alert('提示',_data);
							}else if(flag=='R'){
								Ext.Msg.alert('提示','添加成功');
								if(scope.parent != null){
									scope._returnJson = _data;
									scope.parent.close();
								}
							}else{
								Ext.Msg.alert('提示','添加失败！');
							}
						}else{
							Ext.Msg.alert('提示','添加失败！');
						}
					});
	    		}
	    	}else{
				scope.cancel();
			}
		}
    },
    getInfoData : function() {
    	var LABEL_CN = this.showEquipmentInfoPanel.getForm().findField(
						'LABEL_CN').getRawValue();
		var PROVINCE = this.showEquipmentInfoPanel.getForm().findField(
						'PROVINCE').getValue();
    	var CITY = this.showEquipmentInfoPanel.getForm().findField(
						'CITY').getValue();
		var COUNTY = this.showEquipmentInfoPanel.getForm().findField(
						'COUNTY').getValue();
		var TOWN = this.showEquipmentInfoPanel.getForm().findField(
						'TOWN').getValue();	
		
		var COMMUNITY = this.showEquipmentInfoPanel.getForm().findField(
						'COMMUNITY').getValue();	
		var ROAD = this.showEquipmentInfoPanel.getForm().findField(
						'ROAD').getValue();	
		var ROAD_NUMBER = this.showEquipmentInfoPanel.getForm().findField(
						'ROAD_NUMBER').getValue();	
		var VILLAGES = this.showEquipmentInfoPanel.getForm().findField(
						'VILLAGES').getValue();
		var VILLAGE_ALIAS = this.showEquipmentInfoPanel.getForm().findField(
						'VILLAGE_ALIAS').getValue();	
		
		var BUILDING = this.showEquipmentInfoPanel.getForm().findField(
						'BUILDING').getValue();
		var UNIT_NO = this.showEquipmentInfoPanel.getForm().findField(
						'UNIT_NO').getValue();
    	var FLOOR_NO = this.showEquipmentInfoPanel.getForm().findField(
						'FLOOR_NO').getValue();
		var ROOM_NO = this.showEquipmentInfoPanel.getForm().findField(
						'ROOM_NO').getValue();
		var LONGITUDE = this.showEquipmentInfoPanel.getForm().findField(
						'LONGITUDE').getValue();	
		var LATITUDE = this.showEquipmentInfoPanel.getForm().findField(
						'LATITUDE').getValue();
		var ABBREVIATION = this.showEquipmentInfoPanel.getForm().findField(
						'ABBREVIATION').getValue();
		var POSTCODE = this.showEquipmentInfoPanel.getForm().findField(
						'POSTCODE').getValue();	
		var REGIONTYPE1 = this.showEquipmentInfoPanel.getForm().findField(
							'REGIONTYPE1').getValue();	
		var REGIONTYPE2 = this.showEquipmentInfoPanel.getForm().findField(
						'REGIONTYPE2').getValue();	
		var RELATED_COMMUNITY_CUID = this.showEquipmentInfoPanel.getForm().findField(
						'RELATED_COMMUNITY_CUID').getValue();
		var CUID = this.showEquipmentInfoPanel.getForm().findField(
						'CUID').getValue();
		var DataInfo = {
				CUID	 			  : CUID,
				LABEL_CN 			  : LABEL_CN,
				PROVINCE		      : PROVINCE,
				CITY                  : CITY,
				COUNTY		          : COUNTY,
				TOWN		  		  : TOWN,
				COMMUNITY             : COMMUNITY,
				ROAD                  : ROAD,
				ROAD_NUMBER           : ROAD_NUMBER,
				VILLAGES	      	  : VILLAGES,
				VILLAGE_ALIAS         : VILLAGE_ALIAS,
				BUILDING        	  : BUILDING,
				UNIT_NO 			  : UNIT_NO,
				FLOOR_NO			  : FLOOR_NO,
				ROOM_NO          	  : ROOM_NO,
				LONGITUDE		      : LONGITUDE,
				LATITUDE		      : LATITUDE,
				ABBREVIATION	      : ABBREVIATION,
				POSTCODE              : POSTCODE,
				REGIONTYPE1			  : REGIONTYPE1,
				REGIONTYPE2			  : REGIONTYPE2,
				RELATED_COMMUNITY_CUID : RELATED_COMMUNITY_CUID,
				RELATED_PROJECT_CUID   :Ext.getCmp('NETWORK.resource').cuid
		};
		return DataInfo;
	},
	doRefresh : function() {
		var cuid = this._cuid;
		var scope = this;
		DWREngine.setAsync(false);
		FullAddressManageAction.getAddressInfoByCuid(cuid,function(data) {
			var record = Ext.data.Record.create([]);
			var _data = new record(data);
			scope.showEquipmentInfoPanel.getForm().loadRecord(_data);
		});
		DWREngine.setAsync(true);
	},
    getResult:function(){
		return this._returnJson;
    },
	cancel : function() {
    	if (this.parent != null) {
			this.parent.close();
		}
    	return null;
	},
	updateLabelCn : function(num){
		var labelCn = '';
		if(num>=1){
		  var province = this.province.getRawValue();
		  if(!Ext.isEmpty(province)){
			labelCn = province;
		  }else{
		  	labelCn = "";
		  }		
		  if(num==1){
		    this.city.setValue('');
		    this.county.setValue('');
		    this.town.setValue('');
		  }
		}
		if(num>=2){
		  var city = this.city.getRawValue();
		  if(!Ext.isEmpty(city)){
			  if(!Ext.isEmpty(labelCn)){
					labelCn =labelCn+'|'+city;
			  }else{
					labelCn = labelCn+city;
			  }
		  }
		  if(num==2){
		    this.county.setValue('');
		    this.town.setValue('');
		  }
		}
		if(num>=3){
		  var county = this.county.getRawValue();
		  if(!Ext.isEmpty(county)){
			if(!Ext.isEmpty(labelCn)){
				labelCn =labelCn+'|'+county;
			}else{
				labelCn = labelCn+county;
			}
		  }
		  if(num==3){
		    this.town.setValue('');
		  }
		}
		if(num>=4){
		  var town = this.town.getRawValue();
		  if(!Ext.isEmpty(town)){
			if(!Ext.isEmpty(labelCn)){
				labelCn =labelCn+'|'+town;
			}else{
				labelCn = labelCn+town;
			}
		  }
		}
	/*	
	  var community = this.community.getValue();
		  if(!Ext.isEmpty(community)){
			if(!Ext.isEmpty(labelCn)){
				labelCn =labelCn+'|'+community;
			}else{
				labelCn = labelCn+community;
			}
	  }
	  */
	  var road = this.road.getValue();
	  if(!Ext.isEmpty(road)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+road;
		}else{
			labelCn = labelCn+road;
		}
	  }
	  /*
	  var roadnumber = this.roadnumber.getValue();
	  if(!Ext.isEmpty(roadnumber)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+roadnumber;
		}else{
			labelCn = labelCn+roadnumber;
		}
	  }
	  */
	  var villages = this.villages.getValue();
	  if(!Ext.isEmpty(villages)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+villages;
		}else{
			labelCn = labelCn+villages;
		}
	  }
	  /*
	  var villagesalias = this.villagesalias.getValue();
	  if(!Ext.isEmpty(villagesalias)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+villagesalias;
		}else{
			labelCn = labelCn+villagesalias;
		}
	  }
	  */
	  var building = this.building.getValue();
	  if(!Ext.isEmpty(building)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+building;
		}else{
			labelCn = labelCn+building;
		}
	  }
	  var unitno = this.unitno.getValue();
	  if(!Ext.isEmpty(unitno)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+unitno;
		}else{
			labelCn = labelCn+unitno;
		}
	  }
	  var floorno = this.floorno.getValue();
	  if(!Ext.isEmpty(floorno)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+floorno;
		}else{
			labelCn = labelCn+floorno;
		}
	  }
	  var roomno = this.roomno.getValue();
	  if(!Ext.isEmpty(roomno)){
		if(!Ext.isEmpty(labelCn)){
			labelCn =labelCn+'|'+roomno;
		}else{
			labelCn = labelCn+roomno;
		}
	  }
	  this.labelcn.setValue(labelCn);
	}
});
Ext.reg('NETWORK.address_Panel', NETWORK.address_Panel);