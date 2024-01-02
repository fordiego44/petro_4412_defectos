/*9000004412*/
var MODULO_CONSULTA_DESPACHO = (function(){
	var instance;
	function ModuloConsultaDespacho(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");  
		this.btnBuscar= $("#btnBuscar");
		this.btnLimpiar= $("#btnResetear");
		
		 //datatable
		this.componentes = { 
				btnFechaDocumentoDesde: $("#btnFechaDocumentoDesde"),
				btnFechaDocumentoHasta: $("#btnFechaDocumentoHasta"),
				dataTableConsulta: $("#tablaDespachoGeneral"),	
				txtCorrelativo: $("#txtCorrelativo"),
				rmt_estado: $("#rmt_estado"),
				txtFechaDocumentoDesde : $("#txtFechaDocumentoDesde"),
				txtFechaDocumentoHasta : $("#txtFechaDocumentoHasta"),
				rmt_dependencia : $("#rmt_dependencia"),
				rmt_usuario : $("#rmt_usuario"),
				txtNumero : $("#txtNumero"),
				txtEntidad : $("#txtEntidad"),
				txtAsunto : $("#txtAsunto"),
				txtGuia :$("#txtGuia"),
				btnBuscar: $("#btnBuscar"),
				btnLimpiar: $("#btnResetear"),
		};
		
		this.URL_CONSULTA_DESPACHO='../app/consulta-despacho';
	}
	 
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloConsultaDespacho(SISCORR_APP);
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