Ext.ns("Frame.grid.plugins.query");

Frame.grid.plugins.query.FullAddressGridQuery = Ext.extend(Object, {
	constructor: function(grid){
		this.grid = grid;
		Frame.grid.plugins.query.FullAddressGridQuery.superclass.constructor.call(this);
		var _scope = this;
		
		var panel = new Ext.Panel({
			height : 60,
			border : false,
			hideBorders : true,
			autoScroll:true,
			layout : 'fit',
			items : [{
				layout : 'fit',
				//style : 'margin:5px 5px 0 5px',
				border : 'false',
				hideBorders : true,
				items : [{
							layout : 'form',
							columnWidth : .1,
							defaults : {
								anchor : '-10'
							},
							items : [{
								xtype : 'textfield',
								fieldLabel : '标准地址',
								name : 'LABEL_CN',
								emptyText : '~输入标准地址查询',
								value : '',
								queryCfg : {
									type : "string",
									relation : "like",
									blurMatch : 'both'
								}
							}]
						}]
			}]
        });
		return panel;
	}
});