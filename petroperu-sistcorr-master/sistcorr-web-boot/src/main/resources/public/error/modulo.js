var MODULO_403 =  (function(){
	var instance;
	function MODULO_403(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.compBtnRetoceder = $("#btnRetroceder");
	}
	
	function createInstance(SISCORR_APP) {
        var object = new MODULO_403(SISCORR_APP);
        return object;
    }
    return {
        getInstance: function (SISCORR_APP) {
            if (!instance) {
                instance = createInstance(SISCORR_APP);
                SISCORR_APP.fire('loaded', instance);
            }
            return instance;
        }
    };
})();