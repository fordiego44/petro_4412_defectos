/*9000004412*/
var MODULO_CONSULTA_PLANILLA= (function(){
	var instance;
	function ModuloConsultaPlanilla(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		
		 //datatable
		this.componentes = { 
				txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
				txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
				btnFechaDocumentoDesde: $("#btnFechaDocumentoDesde"),
				btnFechaDocumentoHasta: $("#btnFechaDocumentoHasta"),
				dataTableConsulta: $("#tablaCorrespondenciasGeneral"),	
		};
	}
	 
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloConsultaPlanilla(SISCORR_APP);
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