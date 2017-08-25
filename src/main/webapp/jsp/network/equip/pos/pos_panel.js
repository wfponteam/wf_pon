
$importjs(ctx + "/dwr/interface/AnPosManageAction.js");
Ext.ns('NETWORK');
 
NETWORK.pos_panel = Ext.extend(Ext.Panel, {
	id 			: 'NETWORK.pos_panel',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'form',
	autoScroll  : true,
	_cuid		: '',
	_disabled 	: this._disabled ? this._disabled : true,
	_type		: this._type?this._type:'',
    _returnJson	: '',
    oldPort     : '',
	initComponent : function() {
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
		//判断是添加还是修改
		var isNeedValid = '';
		//判断是否是第一次修改
		var modifyoltfalg = true;
		//修改的初始化值
		var labelcn = '';
		
		var scope = this;
		if(this._type == 'add'){
			isNeedValid = 'add';
			this._disabled = false;
		}
		if(this._type == 'modify'){
			isNeedValid = 'modify';
			this._disabled = false;
		}
		this.cuid = new Ext.form.TextField( {
			name : 'CUID',
			hidden : true
		});
		this.posTextFiled = new Ext.form.TextField( {
			fieldLabel  : getNotNullFont('分光器名称'),
			anchor 		: '90%',
			disabled	: this._disabled,
			allowBlank  : false,
			name 		: 'LABEL_CN',
			invalidText : '名称已经存在',
			validator   : function(name) {
				var flag = true;
				if(isNeedValid == 'modify' && modifyoltfalg){
					labelcn = name;
					modifyoltfalg = false;
				}
				if ((!Ext.isEmpty(name) && isNeedValid == 'add') || (isNeedValid == 'modify' && labelcn != name)) {
					DWREngine.setAsync(false);
					AnPosManageAction.isPosNameExist(name, function(_data) {
						flag =  !_data;
					});
					DWREngine.setAsync(true);
				}
				return flag;
			}
		});
		
		// 定义ext的ComboBox静态值
		var rationData = [['1:2','1:2'],['1:4','1:4'],['1:8','1:8'],['1:16','1:16'],
		                  ['1:32','1:32'],['1:64','1:64'],['1:128','1:128']];
		var rationStore = new Ext.data.SimpleStore({
			 fields:['channel_id','channel_name'],
			 disabled:this._disabled,
		     data:rationData
		});
		this.ration = new Ext.form.ComboBox({
			fieldLabel 	: getNotNullFont('分光比'),
			anchor 		: '90%',
			allowBlank  : false,
			disabled	: this._disabled,
			name 		: 'RATION',
			typeAhead: true,
			value : '1:2',
	        triggerAction: 'all',
	        editable : false,
			store : rationData,
			valueField : 'channel_id',
	        displayField : 'channel_name'
		});
		
		// TODO 新加的字段，没修改，待验证
		this.relatedCabCuid = new IRMS.combo.AsynCombox({
			fieldLabel : getNotNullFont('安装位置'),
			triggerAll 		: true,
			editable : true,
			disabled		: this._disabled,
			allowBlank  : false,
			hideTrigger1 	: true,
			 name       : 'RELATED_CAB_CUID',
			anchor 	   : '90%',
			blurMatch : 'both',
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'ACCESSPOINT'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			}
		});
		
		var config=this;
		this.relatedoltcuid = new IRMS.combo.AsynCombox( {
			fieldLabel 	: getNotNullFont('所属上联设备') ,
			name 		: 'RELATED_UPNE_CUID',
			emptyText 	: '请选所属上联设备',
			anchor 		: '90%',
			allowBlank  : false,
			editable    : true,
			disabled	: this._disabled,
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_POS_NAME'   
				}
			},
			listeners		: {
				"change"	: function(_this,newValue,oldValue){
					if(!Ext.isEmpty(newValue)){
						if(!Ext.isEmpty(_this.getStore()) && !Ext.isEmpty(_this.getStore().getAt(0).data.data[0])){
							var type = _this.getStore().getAt(0).data.data[0].data.type;
							config.relatedPortCuid.setValue('');
							if(type=='olt'){
								config.canallocatetouser.setValue('0');
							}else if(type=='pos'){
								config.canallocatetouser.setValue('1');
							}
						}
					}
				}
			},
			wincfg 		: {
				winArgs 	: {
					width 		: 600,
					height 		: 400,
					winTitle 	: '所属设备选择'
				},
				panelArgs	:{
					singleSelect: true
				}
			},
			editable : false
		});
		
		var _scope = this;
		this.relatedPortCuid = new IRMS.combo.AsynCombox({
			fieldLabel : getNotNullFont('上联设备主用端口') ,
			triggerAll 		: true,
			disabled		: this._disabled,
			editable        : true,
			hideTrigger1 	: true,
			allowBlank 		: false,
			name       : 'RELATED_UPNE_PORT_CUID', 
			anchor 	   : '90%',
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_PORT_NAME'   
				},
				
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			},
			//监听事件,在查询之前得到已选OLT的值,根据OLT的端口
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
					//config.relatedPortCuid.setValue("");
					var oltCuid = _scope.relatedoltcuid.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(oltCuid)){
						whereParams['RELATED_NE_CUID'] = {
			    				key : 'RELATED_NE_CUID',
								value : oltCuid,
								relation : '='	
			    		};
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				}  
			}
		});
		var _scope = this;
		this.secrelatedPortCuid = new IRMS.combo.AsynCombox({
			fieldLabel : '上联设备备用端口' ,
			triggerAll 		: true,
			disabled		: this._disabled,
			editable        : true,
			hideTrigger1 	: true,
			allowBlank 		: true,
			name       : 'RELATED_PORT2_CUID',
			anchor 	   : '90%',
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_PORT_NAME'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
				
			}
	
		});
		this.vendorCombo = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('生产厂商') ,
			id 				: 'VENDOR',
			name 			: 'RELATED_VENDOR_CUID',
			anchor 			: '90%',
			triggerAll 		: true,
			disabled		: this._disabled,
			hideTrigger1 	: false,
			allowBlank 		: false,
			editable 		: false,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ALLVENDOR'
				}
			},
			listeners		: {
				"select"	: function(_this,newValue,oldValue){
					scope.model.setValue("");
				}
			}
		});
		
		this.ownership = new  IRMS.combo.AsynCombox({
			fieldLabel 		: getNotNullFont('资产归属'),
			name 			: 'OWNERSHIP',
			anchor 			: '90%',
			triggerAll 		: true,
			value			: 1,
			allowBlank 		: false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			editable 		: false,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'OWNERSHIP_NM'
				}
			}
		});
		this.ownershipman = new  IRMS.combo.AsynCombox( {
			fieldLabel : getNotNullFont('资产归属人'),
			name : 'OWNERSHIP_MAN',
			anchor : '90%',
			editable : false,
			allowBlank  : false,
			triggerAll 		: true,
			hideTrigger1 	: true,
			value : 1,
			comboxCfg : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'OWNERMENSHIP'
				}
			}
		});
		this.maint_person = new Ext.form.TextField( {
			fieldLabel : getNotNullFont('一线数据维护人（代维/一线)'),
			allowBlank : false,
			anchor : '90%',
			name : 'MAINT_PERSON'
		});	
		this.data_quality_person = new Ext.form.TextField( {
			fieldLabel : getNotNullFont('数据质量责任人（移动）'),
			allowBlank : false,
			anchor : '90%',
			name : 'DATA_QUALITY_PERSON'
		});	
		this.livecycle = new  IRMS.combo.AsynCombox( {
			fieldLabel : getNotNullFont('生命周期状态'),
			name 			: 'LIVE_CYCLE',
			anchor 			: '90%',
			allowBlank  	: false,
			editable 		: false,
			triggerAll 		: true,
			hideTrigger1 	: true,
			value 			: 1,
			comboxCfg : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'LIVECYCLE'
				}
			}
		});
		this.accesstype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('接入类型'),
			name 			: 'ACCESS_TYPE',
			anchor 			: '90%',
			triggerAll 		: true,
			allowBlank  	: false,
			hideTrigger1 	: true,
			editable 		: false,
			config			: config,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ACCESSTYPE'
				}
			}
		});
		// 定义ext的ComboBox静态值
		var channelDataUser = [['0','否'],['1','是']];
		var channelStoreUser = new Ext.data.SimpleStore({
			 fields:['channel_id','channel_name'],
			 disabled:this._disabled,
		     data:channelDataUser
		});
		this.canallocatetouser = new  Ext.form.ComboBox({
			fieldLabel : getNotNullFont('是否直接带用户'),
			name : 'CAN_ALLOCATE_TO_USER',
			anchor : '90%',
			typeAhead: true,
			value : '0',
			allowBlank  : false,
	        triggerAction: 'all',
	        editable : false,	
			store : channelDataUser,
			valueField : 'channel_id',
			disabled:this._disabled,
	        displayField : 'channel_name'
		});
		
		this.setuptime = new  Ext.form.DateField ({
			fieldLabel 	: '入网时间',
			name 		: 'SETUP_TIME',
			disabled	: this._disabled,
			value    	: new Date(),
			editable 	: false,
			allowBlank  : false,
			anchor 		: '90%',
			format		: 'Y-m-d'
		});
		
		this.preserver = new Ext.form.TextField( {
			fieldLabel : getNotNullFont('维护班组'),
			allowBlank : false,
			anchor 	   : '90%',
			disabled   : this._disabled,
			name       : 'PRESERVER'
		});
		// 定义ext的ComboBox静态值
		var channelData = [['1','自维'],['2','代维']];
		var channelStore = new Ext.data.SimpleStore({
			 fields:['channel_id','channel_name'],
			 disabled:this._disabled,
		     data:channelData
		});
		this.maintMode = new  Ext.form.ComboBox( {
			fieldLabel : '维护方式',
			name : 'MAINT_MODE',
			anchor : '90%',
			typeAhead: true,
			value : '1',
			allowBlank  : false,
	        triggerAction: 'all',
	        editable : false,	
			store : channelData,
			valueField : 'channel_id',
			disabled:this._disabled,
	        displayField : 'channel_name'
		});
		
		this.creatTime = new  Ext.form.DateField ({
			fieldLabel 	: '建设日期',
			name 		: 'CREATE_TIME',
			disabled	: this._disabled,
			value    	: new Date(),
			editable 	: false,
			anchor 		: '90%',
			format		: 'Y-m-d'
		});
		
		this.seqno = new Ext.form.TextField( {
			fieldLabel 	: 'SN码',
			anchor 		: '90%',
			disabled	: this._disabled,
			name 		: 'SEQNO'
		});
		this.relatedProjectCuid = new Ext.form.TextField( {
			fieldLabel : '所属工程',
			anchor 	   : '90%',
			disabled   : this._disabled,
			name       : 'RELATED_PROJECT_CUID'
		});
		this.remark = new Ext.form.TextArea( {
			fieldLabel 	: '备注',
			anchor 		: '90%',
			disabled	: this._disabled,
			name 		: 'REMARK'
		});
		this.relatedaccesspoint = new IRMS.combo.AsynCombox({
			fieldLabel : '接入点' ,
			name       : 'RELATED_ACCESS_POINT',
			anchor 	   : '90%',
			triggerAll : true,
			hideTrigger1 : false,
			allowBlank : true,
			blurMatch : 'both',
			triggerAll : true,
			editable : true,
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_ACCESS_POINT'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			}
		});
		// 设备属性
		this.showEquipmentInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			items : [{
				layout : 'column',                                       
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.posTextFiled]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedCabCuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedoltcuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedPortCuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.secrelatedPortCuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.ration]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.preserver]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.seqno]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.vendorCombo]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedaccesspoint]
				}
				
				]
			}]				
		});
		
		// 管理属性
		this.showManagementInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			items : [{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.ownership]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.ownershipman]
				}]
			},
			{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.maint_person]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.data_quality_person]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.canallocatetouser]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.maintMode]
				}]
			},
			{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedProjectCuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.livecycle]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.setuptime]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.creatTime]
				}]
			},{
				layout : 'column',
				items:[
				      {
					columnWidth : .5,
					layout : 'form',
					items:[this.accesstype]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.cuid]
				}]
			}]
		});
		
		// 备注
		this.showRemarkInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			layout 		: 'form',
			items 		: [this.remark]
		});
		
		var filedSet1 = new Ext.form.FieldSet({
			title 			: '设备属性',
			collapsible 	: true,
			frame			: false,
			region			: 'center',
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			items			: [this.showEquipmentInfoPanel]
		});
		
		var filedSet2 = new Ext.form.FieldSet({
			title 			: '管理属性',
			collapsible 	: true,
			frame			: true,
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			collapsed 		: true,
			items:[this.showManagementInfoPanel]
		});
		
		var filedSet3 = new Ext.form.FieldSet({
			title 			: '备注',
			collapsible 	: true,
			frame			: true,
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			collapsed 		: true,
			items:[this.showRemarkInfoPanel]
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
		this.items = [filedSet1, filedSet2, filedSet3];
		this.initData();
		NETWORK.pos_panel.superclass.initComponent.call(this);
	},
	//修改赋初值
	initData : function() {
		oldPort = '';
		if (!Ext.isEmpty(this._cuid)) {
			this.doRefresh();
		}
	},
	//网元弹出页、厂家名称
	getVendorCuid : function(){
		return this.vendorCombo.getValue();
	},
    save:function(){
		var scope = this;
		if(this.showEquipmentInfoPanel.getForm().isValid() && this.showManagementInfoPanel.getForm().isValid() ){
			if(!Ext.isEmpty(this._type)){
				DWREngine.setAsync(false);
	    		if(this._type=='modify'){
					var obj = scope.getPosInfoData();
					obj.CUID = scope._cuid;
	    			AnPosManageAction.modifyPosInfo(obj,function(_data){
	    				var flag = _data.substr(0,1);
						var _dataInfo = _data.substr(1,_data.length);
						if(flag=='E'){
							Ext.Msg.alert('温馨提示',_dataInfo);
						}
						else{
							if (_data != null) {
								Ext.Msg.alert('提示','修改成功');
								if(scope.parent != null){
									scope._returnJson = _data;
									scope.parent.close();
								}
							}else{
								Ext.Msg.alert('提示','修改失败！');
								if(scope.parent != null){
									scope.parent.close();
								}
							}
						}
	    				
						
	    			});
	    		}else if(this._type=='add'){
					var posInfo = scope.getPosInfoData();
					AnPosManageAction.addPosInfo(posInfo, function(_data) {
						var flag = _data.substr(0,1);
						var _dataInfo = _data.substr(1,_data.length);
						if(flag=='E'){
							Ext.Msg.alert('温馨提示',_dataInfo);
						}else {
							if (scope.parent != null) {
								scope._returnJson = _data;
								scope.parent.close();
							}else{
								Ext.Msg.alert('提示','添加失败！');
								scope.parent.close();
							}
						}
					});
	    		}
	    		DWREngine.setAsync(true);
	    	}else{
				scope.cancel();
			}
		}
    },
    
    formatTime	:	function(dateTime){
    	if(!Ext.isEmpty(dateTime)){
    		return dateTime.format('Y-m-d H:i:s');
    	}
    	return '';
    },
    
    getPosInfoData : function() {
    	var LABEL_CN = this.showEquipmentInfoPanel.getForm().findField('LABEL_CN').getValue();
    	//var STANDARD_NAME = this.showEquipmentInfoPanel.getForm().findField('STANDARD_NAME').getValue();
    	var RELATED_VENDOR_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_VENDOR_CUID').getValue();    	
    	// var LOCATION = this.showEquipmentInfoPanel.getForm().findField('LOCATION').getValue();
    	var RELATED_UPNE_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_UPNE_CUID').getValue();
    	var RELATED_UPNE_PORT_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_UPNE_PORT_CUID').getValue();
    	var RELATED_PORT2_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_PORT2_CUID').getValue();
    	var RATION = this.showEquipmentInfoPanel.getForm().findField('RATION').getValue();
    	var PRESERVER = this.showEquipmentInfoPanel.getForm().findField('PRESERVER').getValue();
    	var SEQNO = this.showEquipmentInfoPanel.getForm().findField('SEQNO').getValue();
    	var RELATED_CAB_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_CAB_CUID').getValue();
    	var RELATED_ACCESS_POINT = this.showEquipmentInfoPanel.getForm().findField('RELATED_ACCESS_POINT').getValue();
    	// var ACCESS_TYPE = this.showEquipmentInfoPanel.getForm().findField('ACCESS_TYPE').getValue();
    	
    	var CUID = this.showManagementInfoPanel.getForm().findField('CUID').getValue();
    	var OWNERSHIP = this.showManagementInfoPanel.getForm().findField('OWNERSHIP').getValue();
    	var OWNERSHIP_MAN = this.showManagementInfoPanel.getForm().findField('OWNERSHIP_MAN').getValue();
    	var MAINT_PERSON = this.showManagementInfoPanel.getForm().findField('MAINT_PERSON').getValue();
		var DATA_QUALITY_PERSON = this.showManagementInfoPanel.getForm().findField('DATA_QUALITY_PERSON').getValue();
		
    	var CAN_ALLOCATE_TO_USER = this.showManagementInfoPanel.getForm().findField('CAN_ALLOCATE_TO_USER').getValue();
    	var MAINT_MODE = this.showManagementInfoPanel.getForm().findField('MAINT_MODE').getValue();
    	var RELATED_PROJECT_CUID = this.showManagementInfoPanel.getForm().findField('RELATED_PROJECT_CUID').getValue();
    	var LIVE_CYCLE = this.showManagementInfoPanel.getForm().findField('LIVE_CYCLE').getValue();
    	var ACCESS_TYPE = this.showManagementInfoPanel.getForm().findField('ACCESS_TYPE').getValue();
    	
    	var SETUP_TIME = this.showManagementInfoPanel.getForm().findField('SETUP_TIME').getValue();
    	SETUP_TIME = this.formatTime(SETUP_TIME);
    	var CREATE_TIME = this.showManagementInfoPanel.getForm().findField('CREATE_TIME').getValue();
    	CREATE_TIME = this.formatTime(CREATE_TIME);
    	var REMARK = null;
    	if(!Ext.isEmpty(this.showRemarkInfoPanel.getForm().findField('REMARK'))){
    		REMARK = this.showRemarkInfoPanel.getForm().findField('REMARK').getValue();
    	}
		var PosData = {
				LABEL_CN 			  : LABEL_CN,
				RELATED_VENDOR_CUID   : RELATED_VENDOR_CUID,
				RELATED_UPNE_CUID	  : RELATED_UPNE_CUID,
				RELATED_UPNE_PORT_CUID : RELATED_UPNE_PORT_CUID,
				RELATED_PORT2_CUID	  : RELATED_PORT2_CUID,
				RATION				  : RATION,
				PRESERVER			  : PRESERVER,
				SEQNO 				  : SEQNO,
				RELATED_CAB_CUID  : RELATED_CAB_CUID,
				RELATED_ACCESS_POINT  : RELATED_ACCESS_POINT,
				CUID	 			  : CUID,
				OWNERSHIP			  : OWNERSHIP,
				OWNERSHIP_MAN		  : OWNERSHIP_MAN,
				MAINT_PERSON		  : MAINT_PERSON,
				DATA_QUALITY_PERSON	  : DATA_QUALITY_PERSON,
				CAN_ALLOCATE_TO_USER  : CAN_ALLOCATE_TO_USER,
				MAINT_MODE			  : MAINT_MODE,
				RELATED_PROJECT_CUID  : RELATED_PROJECT_CUID,
				LIVE_CYCLE			  : LIVE_CYCLE,
				ACCESS_TYPE			  : ACCESS_TYPE,
				SETUP_TIME 			  : SETUP_TIME,
				CREATE_TIME 		  : CREATE_TIME,
				REMARK				  : REMARK,
				RELATED_PROJECT_CUID   :Ext.getCmp('NETWORK.resource').cuid,
				oldPort               : oldPort
		};
		return PosData;
	},
	doRefresh : function() {
			var cuid = this._cuid;
			var scope = this;
			DWREngine.setAsync(false);
			AnPosManageAction.getPosByCuid(cuid, function(data) {
				var record = Ext.data.Record.create([]);
				var _data = new record(data);
				scope.showEquipmentInfoPanel.getForm().loadRecord(_data);
				scope.showManagementInfoPanel.getForm().loadRecord(_data);
				scope.showRemarkInfoPanel.getForm().loadRecord(_data);
				oldPort = data.RELATED_UPNE_PORT_CUID.value;
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
	}
});
Ext.reg('NETWORK.pos_panel', NETWORK.pos_panel);