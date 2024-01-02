/*9000004412*/
var MODULO_CONSULTA_VALIJAS_RECIBIDAS = (function(){
	var instance;
	function ModuloConsultaValijasRecibidas(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader"); 
		this.btnBuscar= $("#btnBuscar");
		this.btnLimpiar= $("#btnResetear");
		
		 //datatable
		this.componentes = { 
				txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
				txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
				btnFechaDocumentoDesde: $("#btnFechaDocumentoDesde"),
				btnFechaDocumentoHasta: $("#btnFechaDocumentoHasta"),
				dataTableConsulta: $("#tablaValijasRecibidas"),
				txtCorrelativo: $("#txtCorrelativo"),
				rmt_estado: $("#rmt_estado"),
				rmt_remitente: $("#rmt_remitente"),
				rmt_recibe: $("#rmt_recibe"),
				rmt_courier: $("#rmt_courier"),
				btnBuscar: $("#btnBuscar"),
				btnLimpiar: $("#btnResetear"),
		};
		
		this.URL_CONSULTA_VALIJAS_RECIBIDAS='../app/consulta-valijas-recibidas';
	}
	 
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloConsultaValijasRecibidas(SISCORR_APP);
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