/*9000004412*/
var MODULO_INCLUIR_DOCUMENTO_DESPACHO = (function(){
	var instance;
	function ModuloIncluirDocumentoDespacho(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		 
		 //datatable
		this.componentes = { 
		 
		};
	}
	 
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloIncluirDocumentoDespacho(SISCORR_APP);
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