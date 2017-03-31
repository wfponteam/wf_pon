	Ext.namespace('NETWORK');
	$importjs(ctx + "/jsp/network/space/cover/coveraddress_Panel.js");
	$importjs(ctx + "/jsp/network/space/cover/FullAddressQueryFormPanel.js");
	$importjs(ctx + "/dwr/interface/NetDomainMaintainDwrAction.js");
	NETWORK.cover_panel = Ext.extend(Ext.Panel, {
			region 		: 'center',
			border 		: false,
			frame 		: true,
			layout 		: 'form',
			autoScroll  : true,
			constructor : function(config) {
				this.relatedNeCuid = config.relatedNeCuid;
				this.coverInfoText = config.coverInfo;
				NETWORK.cover_panel.superclass.constructor.call(this,config);
			},
			initComponent : function() {
				var scope = this;
				var relatedNeCuid = null;
				if(scope.relatedNeCuid){
					relatedNeCuid = scope.relatedNeCuid;
				}
				
				this.ne = new IRMS.combo.AsynCombox({
					fieldLabel : getNotNullFont('关联箱体'),
					name : 'RELATED_NE_CUID',
					blurMatch : 'both',
					allowBlank : false,
					anchor 	   : '90%',
					value : '',
					comboxCfg  : {	
						boName : 'XmlTemplateComboxBO',
						cfgParams : {  
							code : relatedNeCuid==null?'GPONCOVERNEFROMNE':'GPONCOVERNEFROMNENO'
						},
						queryCfg : {
							type : 'string',
							relation : '='
						}
					}
				});
				this.scene1 = new  IRMS.combo.AsynCombox( {
					name 			: 'REGIONTYPE1',
					fieldLabel 		: getNotNullFont('覆盖场景'),
					triggerAll 		: true,
					hideTrigger1 	: true,
					allowBlank  	: false,
					anchor 			: '90%',
					comboxCfg 		: {
						boName 	  : 'XmlTemplateComboxBO',
						cfgParams : {
							code : 'GPON_SCENE_TYPE_1'
						}
					},
					listeners		: {
						"change" : function(_this,newValue,oldValue){
							var spaceCuid = scope.scene1.getValue();
							scope.scene2.setValue('')
							if(!Ext.isEmpty(spaceCuid)){
								if(spaceCuid !=3){
									scope.scene2.fieldLabel='聚类场景';
									scope.scene2.hidden=true;
									scope.scene2.allowBlank=true;
									scope.scene2.setEditable(false);
									scope.scene2.setDisabled(true);
								}else{
									scope.scene2.hidden=false;
									scope.scene2.setAllowBlank=false;
									scope.scene2.setEditable(false);
									scope.scene2.setDisabled(false);
								}
							}
							return true;
						} 
					}
				});
				
				this.scene2 = new IRMS.combo.AsynCombox( {
					name 			: 'REGIONTYPE2',
					fieldLabel 		: getNotNullFont('聚类场景'),
					triggerAll 		: true,
					hideTrigger1 	: true,
					allowBlank  	: false,
					disabled		: false,
					anchor 			: '90%',
					comboxCfg 		: {
						boName 	  : 'XmlTemplateComboxBO',
						cfgParams : {
							code : 'GPON_SCENE_TYPE_2'
						}
					},
					listeners		: {
						"beforequery" : function(_this,newValue,oldValue){
							var spaceCuid = scope.scene1.getValue();
							var whereParams = {};
							if(!Ext.isEmpty(spaceCuid)){
								whereParams['RELATED_SPACE_CUID'] = {
					    				key : 'RELATED_SPACE_CUID',
										value : spaceCuid,
										relation : '='	
					    		}
							}else{
								Ext.Msg.alert('温馨提示','请先选择覆盖场景');
								return false;
							}
							_this.combo.comboxCfg.queryParams=whereParams;
							return true;
						} 
					}
				});
				this.businesstype = new  IRMS.combo.AsynCombox( {
					name 			: 'BUSINESS_TYPE',
					fieldLabel 		: getNotNullFont('业务类型'),
					triggerAll 		: true,
					hideTrigger1 	: true,
					allowBlank  	: false,
					anchor 			: '90%',
					comboxCfg 		: {
						boName 	  : 'XmlTemplateComboxBO',
						cfgParams : {
							code : 'GPON_BUSINESS_TYPE'
						}
					}
					});
				this.coverInfo = new Ext.form.TextArea( {
					fieldLabel 	: getNotNullFont('覆盖范围描述'),
					width       : '89%',
					allowBlank : false,
					emptyText   : "~输入覆盖范围描述",
		    		name 		: 'COVER_INFO',
		    		value : scope.coverInfoText,
		    		id : 'coverInfoText'
				});	
				if(scope.relatedNeCuid){
					this.ne.setValue(scope.relatedNeCuid);
					this.ne.setEditable(false);
					this.ne.setDisabled(true);
				}
				this.showManagementInfoPanel = new Ext.FormPanel({
					region		: 'center',
					border 		: true,
					bodyStyle   :"padding:5px;", 
					labelAlign 	: 'left',
					frame 		: false,
					autoScroll  : true,
					autoDestroy : true,
					items : [{
						layout : 'column',
						items:[{
							columnWidth : 1,
							layout : 'form',
							items:[this.ne]
						}
						
						]
					},{
						layout : 'column',
						items:[{
							columnWidth : .5,
							layout : 'form',
							items:[this.scene1]
						},{
							columnWidth : .5,
							layout : 'form',
							items:[this.scene2]
						}]
					},{
						layout : 'column',
						items:[{
							columnWidth : .5,
							layout : 'form',
							items:[this.businesstype]
						},/*{
							columnWidth : .5,
							layout : 'form',
							items:[this.scene2]
						}*/]
					}
					]
				});
				
			
				
				this.coverInfoPanel = new Ext.FormPanel({
					region		: 'center',
					border 		: true,
					bodyStyle   :"padding:5px;", 
					labelAlign 	: 'left',
					frame 		: false,
					autoScroll  : true,
					autoDestroy : true,
					items : [{
						layout : 'column',
						items:[{
							columnWidth : 1,
							layout:'form',
							items:[this.coverInfo]
						}]
					}]
				});
				
				this.gponcoveraddressPanel = new NETWORK.coveraddress_Panel({
					inputParams:{},
					neCuid : scope.relatedNeCuid,
					columnWidth: .9
			    });
				
				var scope = this;
				var buttonAddSvlan = new Ext.Button({
					text : '添加标准地址',
					iconCls : 'c_add',
					iconAlign: 'top',
					height : 30,
					anchor: '99%',
					style : 'margin:0px 0px 5px 5px;',
					handler : function(){
					   var neValue = scope.ne.getValue();
					   if(neValue == ""){
						   Ext.Msg.alert("温馨提示","请选择关联设备");
						   return;
					   }
					   var FullAddressQueryFormPanel = new NETWORK.FullAddressQueryFormPanel({
							inputParams:{}
							//domainCuid:scope.resInfo.DOMAIN_CUID
						});
						var win = WindowHelper.openExtWin(FullAddressQueryFormPanel,{
							width:800,
							height:400,
							title:"添加标准地址",
							buttons:[{
								text:'确定',
								iconCls : 'c_disk',
								scope : this,
								handler : function(){
								  var sels=FullAddressQueryFormPanel.getResult();
							  	  var store = scope.gponcoveraddressPanel.getStore();
							  	  var resSvlans = [];
								  for(var i =0;i<store.getCount();i++){
										var record = store.getAt(i);
										var resSvlan = record.json.LABEL_CN+'';
										resSvlans.push(resSvlan);
								  }
								  var insertData = [];
								  var msg = "";
								  var errorMsg = "";
								  var isRight = true;
							  	  Ext.each(sels,function(sel){
							  		if(isRight){
							  			var selSvlanNew = sel.json.LABEL_CN+'';
						  				var isExist = false;
								  		for(var n=0;n<resSvlans.length;n++){
								  			if (resSvlans[n] == selSvlanNew) {  
								  	        	isExist =  true;
								  	        	break;
								  	        }  
								  		}
										if(!isExist){
											resSvlans.push(selSvlanNew);
											insertData.push(sel);
										}else{
											if(msg.length==0){
												msg = selSvlanNew;
											}else{
												msg+=","+selSvlanNew;
											}
										}
							  			
							  		}
							      });
							  	 
						  		  if(msg.length>0){
							  		Ext.Msg.alert("温馨提示", "已选择了标准地址："+msg+",请重新选择。");
							  	  }else{
							  		Ext.each(insertData,function(record){
							  			store.insert(0,record);
								    });
							  		win.hide();
							  	  }
								}
							},{
								text:'取消',
								iconCls : 'c_door_in',
								scope : this,
								handler : function(){
									win.hide();
								}
							}]
						});
						win.show();
					}
				});
				var buttonDelSvlan = new Ext.Button({
					text : '移除标准地址',
					iconCls : 'c_delete',
					iconAlign: 'top',
					height : 30,
					anchor: '99%',
					style : 'margin:5px 0px 5px 5px;',
					handler : function(){
							var swDomainCuid = scope.domainCuid;
							var sels = scope.gponcoveraddressPanel.getSelectionModel().getSelections();
							if(sels.length ==0){
								Ext.Msg.alert("温馨提示", "请选择一条数据！");
								return ;
							}
							var cuids = [];
							var selsExists = [];
							var selsNew = [];
							var store = scope.gponcoveraddressPanel.getStore();
							Ext.each(sels,function(record){
								    var flagInfo = record.json.FLAGINFO;
								    if(flagInfo=='新添加'){
								    	selsNew.push(record);
								    }else{
								    	selsExists.push(record);
								    	var val={};
									    val['GPONCOVER_CUID']=record.json.GPONCOVER_CUID;
									    val['LABEL_CN']=record.json.LABEL_CN;
									    cuids.push(val);
								    }
							});
							Ext.Msg.confirm("温馨提示","确认移除标准地址？", function(btn) {
								if(btn == 'yes') {
									if(selsNew&&selsNew.length>0){
										Ext.each(selsNew,function(record){
											store.remove(record);
									    });
									}
									if(cuids&&cuids.length>0){
										MaskHelper.mask(scope.gponcoveraddressPanel.getEl(), 'SVLAN移除中，请稍候...');
										GponCoverManageAction.deleteGponCover(cuids,function(data){
											MaskHelper.unmask(scope.gponcoveraddressPanel.getEl());
											if(Ext.isEmpty(data)){
												 Ext.Msg.alert("温馨提示","移除成功！",function(){
													   Ext.each(selsExists,function(record){
															store.remove(record);
													   });
													   scope.cvlanPanel.doQuery();
												  });
												 }else{
													  Ext.Msg.alert("温馨提示",data);
												 }
											});
									}
								}
							}, this);
					}
				});
				var buttonPanel = new Ext.Panel({
					columnWidth: .1,
					layout: 'anchor',
					height: 120,
					items:[buttonAddSvlan,buttonDelSvlan]
				});
				var filedSet1 = new Ext.form.FieldSet({
					title 			: '关联箱体',
					collapsible 	:true,
					frame			:false,
					region			:'center',
					autoHeight		:true,
					titleCollapse 	: true,
					shadow 			:true,
					items			:[this.showManagementInfoPanel]
				});
				var filedSet2 = new Ext.form.FieldSet({
					title : '已添加的标准地址',
					collapsible 	:true,
					frame			:false,
					autoHeight		:true,
					titleCollapse 	: true,
					shadow 			:true,
					layout: 'column',
					items:[this.gponcoveraddressPanel,buttonPanel]
				});
				
				
				
				var filedSet3 = new Ext.form.FieldSet({
					title 			: '覆盖范围描述',
					collapsible 	:true,
					frame			:false,
					region			:'center',
					autoHeight		:true,
					titleCollapse 	: true,
					shadow 			:true,
					items			:[this.coverInfoPanel]
				});
				
				this.items = [filedSet1,filedSet2,filedSet3];
				NETWORK.cover_panel.superclass.initComponent.call(this);
			},
			getResult : function() {
				var scope = this;
				var store = scope.gponcoveraddressPanel.getStore();
				var neCuid = scope.ne.getValue();
				var neLabelCn = scope.ne.lastSelectionText;
				var coverInfoText = Ext.getCmp('coverInfoText');
				var coverInfo = coverInfoText.getValue();
				var scene1=scope.scene1.getValue();
				var scene2=scope.scene2.getValue();
				var businesstype=scope.businesstype.getValue();
				var selValns = [];
				for(var i =0;i<store.getCount();i++){
					var record = store.getAt(i);
					var flagInfo = record.json.FLAGINFO;
					if(flagInfo=='新添加'){
				    	var selVal={};
				  	  	selVal['STANDARD_ADDR']=record.json.CUID;
				  	  	selVal['ADDRLABELCN']=record.json.LABEL_CN;
				  	  	selVal['RELATED_NE_CUID']=neCuid;
				  	  	selVal['DEVICE_NAME'] = neLabelCn;
				  	  	selVal['COVER_INFO'] = coverInfo;
				  	  	selVal['BUSINESS_TYPE'] = businesstype;
				  	  	selVal['REGIONTYPE1'] = scene1;
						selVal['RELATED_PROJECT_CUID']  = Ext.getCmp('NETWORK.resource').cuid;
				  	  	selVal['REGIONTYPE2'] = scene2;
				  	    selValns.push(selVal);
				    }else{
				    	var selVal={};
				    	selVal['CUID'] = record.json.GPONCOVER_CUID;
				    	selVal['COVER_INFO'] = coverInfo;
				  	  	selVal['BUSINESS_TYPE'] = businesstype;
				    	selVal['REGIONTYPE1'] = scene1;
				  	  	selVal['REGIONTYPE2'] = scene2;
				  	    selValns.push(selVal);
				    }
				} 
				return selValns;
			}
		});
Ext.reg('NETWORK.cover_panel', NETWORK.cover_panel);