Ext.namespace("NETWORK");

$importjs(ctx + "/jsp/network/resource/project.js");
$importjs(ctx + "/jsp/network/resource/attach.js");
$importjs(ctx + "/jsp/network/resource/equip.js");
$importjs(ctx + "/dwr/interface/ProjectAction.js");

NETWORK.resource = Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.resource',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	//参数
	username : '',
	prjcode  : '',
	parentprjcode : '',
	cuid	:'',
	initComponent : function() {
		NETWORK.resource.superclass.initComponent.call(this);
		this._initItems();
		this.initData();
	},
	_initItems : function() {
		
		this.parentprjcode = this.parentprjcode == 'null' ? '' : this.parentprjcode;
		var scope = this;
		DWREngine.setAsync(false);
		ProjectAction.queryIdByCode(this.prjcode,this.parentprjcode,
				function(date) {			
				scope.cuid = date;
			});
		DWREngine.setAsync(true);
		
		this.project=new NETWORK.project( {
   			title: '工程信息',
   			username : this.username,
   			cuid  : this.cuid
		});
		this.attach=new NETWORK.attach( {
			title: '附件信息'
		});
		
		this.equip=new NETWORK.equip( {
			cuid  : this.cuid,
			title: '资源信息'
		});
		
		this.commonInfoFieldSet = new Ext.Panel({
  			 //title: '工程信息',
  			 border : false,
  			 frame : false,
  			 autoScroll:true,
  			 height : 550,
  			 region:'center',
  			 buttonAlign:'right', 
  			 items : [{layout : 'form',items : [this.project,this.attach,this.equip]}],
  			 buttons : [
				{
					xtype : 'button',
					width : 80,
					text : '挂测',
					id:'yanqij',
					iconCls : 'c_accept',
					listeners : {
						click: function(btn, e, eOpts){
	
		       					var mk = new Ext.LoadMask(document.body, {  
		    						msg: '提交中，请稍候！',  
		    						removeMask: true //完成后移除  
		    					});
		    					Ext.MessageBox.confirm('温馨提示', '确定进行挂测？',function(btn){
				            		 if(btn=="yes")
						   	 		 {
						   	 		 	mk.show(); //显示  
										Ext.Ajax.request({
										        url: ctx+'/webServiceAction/syncHangingResult.do',
										        method: 'POST',
										        params: {
										        	cuid: scope.cuid
										        },
										        success: function(response) {
										            var result = JSON.parse(response.responseText);
										            mk.hide(); //关闭
										            // 返回值
										            Ext.Msg.alert('温馨提示', result.msg);
										             
										        }
										});
						   	 		 }else
						   	 		 {
						   	 		 	return false;
						   	 		 }
								    	
							 	});
						}
					}
				}]
  		})
		this.add(this.commonInfoFieldSet);
	},
	initData : function() {

		var scope = this;
		
		DWREngine.setAsync(false);
		ProjectAction.queryProjectByCode(this.cuid, function(data) {
			var record = Ext.data.Record.create([]);
			var _data = new record(data);
			Ext.getCmp("customPanel").getForm().loadRecord(_data);
			Ext.getCmp("attachPanel").getForm().loadRecord(_data);
			if("施工" != _data.get('PRJ_STATUS')){
				Ext.getCmp('OnuTBar.add').hide();
				Ext.getCmp('OnuTBar.modify').hide();
				Ext.getCmp('OnuTBar.delete').hide();
				Ext.getCmp('pos_tbar.add').hide();
				Ext.getCmp('pos_tbar.modify').hide();
				Ext.getCmp('pos_tbar.delete').hide();
				Ext.getCmp('FullAddressGridTBar.add').hide();
				Ext.getCmp('FullAddressGridTBar.modify').hide();
				Ext.getCmp('FullAddressGridTBar.delete').hide();
				Ext.getCmp('village_tbar.add').hide();
				Ext.getCmp('village_tbar.modify').hide();
				Ext.getCmp('village_tbar.delete').hide();
				Ext.getCmp('cover_tbar.add').hide();
				Ext.getCmp('cover_tbar.modify').hide();
				Ext.getCmp('cover_tbar.delete').hide();
				Ext.getCmp('onuAnPtp.add').hide();
				Ext.getCmp('onuAnPtp.modify').hide();
				Ext.getCmp('onuAnPtp.delete').hide();
			
			}
		});
		DWREngine.setAsync(true);
	},
});