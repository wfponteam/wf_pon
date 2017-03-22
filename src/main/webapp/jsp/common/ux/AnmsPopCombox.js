Ext.namespace("Anms");
$importjs(ctx + '/commons/utils/UrlHelper.js');
(function() {
	Anms.AnmsPopCombox = Ext.extend(IRMS.combo.AsynCombox, {
		trigger1Class : "x-form-clear-trigger",
		trigger2Class : "x-form-search-trigger",
		constructor : function(config) {
			if (!config.comboxCfg.cfgParams) {
				config.mode = 'local';
			}
			if (!config.wincfg) {
				config.wincfg = {};
			}
			Anms.AnmsPopCombox.superclass.constructor.call(this, config);
		},
		initComponent : function() {
			Anms.AnmsPopCombox.superclass.initComponent.call(this);
			this.addEvents(
			/**
			 * @event beforeopen 选择窗口打开前
			 * @param {PopCombox}
			 */
			'beforeopen');
		},
		onTriggerClick : Ext.emptyFn,
		onTrigger2Click : function(e) {
			if (this.disabled == true) {
				return false;
			}
			if (this.fireEvent('beforeopen', this) !== false) {
				if (this.comboxCfg) {
					var w = Ext.getBody().getWidth() - 50;
					var h = Ext.getBody().getHeight() - 50;
					if (this.comboxCfg.url) {
						var url = this.comboxCfg.url;
						if (!this.wincfg.winArgs) {
							this.wincfg.winArgs = "dialogWidth=" + w + "px;dialogHeight=" + h + "px;";
						}
						if (this.comboxCfg.urlArgs) {
							url = UrlHelper.replaceUrlArguments(url,this.comboxCfg.urlArgs);
						}
						var returnValue = window.showModalDialog(url,'selectRecord', this.wincfg.winArgs);
						if (returnValue) {
							this.setValue(returnValue);
						}
					} else if (this.comboxCfg.panel) {
						var _scope = this;
						var _winTitle = '';
						var _winCfg = this.wincfg.winArgs;
						if (Ext.isObject(_winCfg)) {
							if (!Ext.isEmpty(_winCfg.width)) {
								w = _winCfg.width;
							}
							if (!Ext.isEmpty(_winCfg.height)) {
								h = _winCfg.height;
							}
							_winTitle = _winCfg.winTitle;
						}
						var config = {
							//其他属性 参考AnOltManage.js
							panelArgs:this.wincfg.panelArgs,
							winTitle : _winTitle,
							width  : w,
							height : h,
							region : 'center',
							initData : {
								text : this.getRawValue(),
								value : this.getValue()
							}
						}
						if(isNotNull(this.comboxCfg.panelType)){
							config.panelType = this.comboxCfg.panelType;
						}
						openWindow(this.comboxCfg.panel, true, config,
								function(_result) {
									if (!Ext.isEmpty(_result)) {
										_scope.setValue(_result);
									}
								}, this, _scope);
					}
				} else {
					return false;
				}
			}
		}
	});
	Ext.reg('anmspopcombox', Anms.AnmsPopCombox);
})();