/*9000004412*/
var MODULO_SOLICITUD_DESPACHO = (function(){
	var instance;
	function ModuloSolicitudDespacho(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		this.compBtnGuardar = $("#btnGuardarCorrespondencia");
		this.compModalConfirmarRegistro = $("#modalConfirmarRegistro");
		 //datatable
		this.componentes = { 
		 
		};
	}
	 
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloSolicitudDespacho(SISCORR_APP);
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