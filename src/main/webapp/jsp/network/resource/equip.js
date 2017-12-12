Ext.ns('NETWORK');
$importjs(ctx + "/jsp/common/config.js");
	              
NETWORK.equip = Ext.extend(Ext.Panel, {
	id 	: 'equip',
	border : false,
	autoScroll : true,
	cuid  : '',
	status :'',
	initComponent : function() {
		this._initItems();
		NETWORK.equip.superclass.initComponent.call(this);
	},
	_initItems : function() {
		var scope = this;
		var items = []; 
		
		var pon = config.pon
		var user = config.getUser(this.status)
		
		this.equipPanel = new Ext.TabPanel({
			id 	: 'equipPanel',
			border : false,
			hideBorders : true,
			width:'100%',
			height:600,
			activeTab:0,
			items : [{ 
				title: 'pos信息', 
				layout: 'fit', 
				height: 'auto', 
				border: false, 
				deferredRender: false, 
				autoScroll : true, 
				html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/cmp_res/grid/ResGridPanel.jsp?code=irms_service_dict.AN_IRMS_POS_MANAGE&menuId=AN_IRMS_POS_MANAGE&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
				} ,
				{ 
					title: 'pos端口', 
					layout: 'fit', 
					height: 'auto', 
					border: false, 
					deferredRender: false, 
					autoScroll : true, 
					html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/jsp/anms/equip/ptp/OltPtpManage.jsp?devTable=pos&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
					} ,
				{ 
					title: '标准地址', 
					layout: 'fit', 
					height: 'auto', 
					border: false, 
					deferredRender: false, 
					autoScroll : true, 
					html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/cmp_res/grid/ResGridPanel.jsp?code=irms_service_dict.IRMS_T_ROFH_FULL_ADDRESS_MANAGE&menuId=IRMS_T_ROFH_FULL_ADDRESS_MANAGES&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
					} ,
					{ 
						title: '非标准地址', 
						layout: 'fit', 
						height: 'auto', 
						border: false, 
						deferredRender: false, 
						autoScroll : true, 
						html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/cmp_res/grid/ResGridPanel.jsp?code=irms_service_dict.NO_IRMS_T_ROFH_FULL_ADDRESS_MANAGE&menuId=NO_IRMS_T_ROFH_FULL_ADDRESS_MANAGE&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
						} ,
					{ 
						title: '业务区', 
						layout: 'fit', 
						height: 'auto', 
						border: false, 
						deferredRender: false, 
						autoScroll : true, 
						html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/cmp_res/grid/ResGridPanel.jsp?code=irms_service_dict.IRMS_BUSINESS_COMMUNITY_MANAGE&menuId=IRMS_BUSINESS_COMMUNITY_MANAGE&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
						} ,
					{ 
						title: '覆盖范围', 
						layout: 'fit', 
						height: 'auto', 
						border: false, 
						deferredRender: false, 
						autoScroll : true, 
						html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/cmp_res/grid/ResGridPanel.jsp?code=irms_service_dict.IRMS_GPON_COVER_MANAGE&menuId=IRMS_GPON_COVER_MANAGE&source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
						} ,
			]
		
		});
		
		this.import_res = { 
				layout: 'fit', 
				height: 100, 
				border: false, 
				deferredRender: false, 
				autoScroll : true, 
				html:' <iframe frameborder="0" width="100%" height="100%" src="'+pon+'/jsp/anms/importres/import.jsp?source=IRMS&userId='+user+'&prjcode='+this.cuid+'"> </iframe>' 
				
			}
		if(this.cuid){
			items.push(this.import_res,this.equipPanel );
		}
		this.items = items;
		return this.items;
	}
});
Ext.reg('NETWORK.equip', NETWORK.equip);