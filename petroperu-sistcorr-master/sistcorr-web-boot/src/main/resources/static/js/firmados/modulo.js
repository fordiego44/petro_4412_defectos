var MODULO_DOCUMENTOS_FIRMADOS = (function(){
	var instance;
	function ModuloDocumentosFirmados(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnRetroceder = $("#btnRetroceder");
	}
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloDocumentosFirmados(SISCORR_APP);
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