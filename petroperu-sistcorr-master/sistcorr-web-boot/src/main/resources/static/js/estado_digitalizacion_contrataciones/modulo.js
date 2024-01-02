/*9000004412*/
var MODULO_ESTADO_DIGITALIZACION_CONTRATACIONES = (function(){
	var instance;
	function ModuloEstadoDigitalizacionContrataciones(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		this.btnBuscar=$("#btnBuscar");
		this.btnLimpiar=$("#btnResetear"); 
		this.btnExportarExcel=$("#btnExportarExcel");
		this.btnAbrirTutoriales=$("#btnAbrirTutoriales"); //4412 defecto 23
		this.URL_TUTORIALES = "app/tutoriales";//4412 defecto 23
		
		 //datatable
		this.componentesEstadoDigitalizacion = { 
				txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
				txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
				btnFechaDocumentoDesde: $("#btnFechaDocumentoDesde"),
				btnFechaDocumentoHasta: $("#btnFechaDocumentoHasta"),
				txtProceso: $("#txtProceso"),
				txtRUC: $("#txtRUC"),
				txtEstado: $("#txtEstado"),
				btnBuscar: $("#btnBuscar"),
				btnLimpiar: $("#btnLimpiar"),
				btnExportarExcel: $("#btnExportarExcel"),
				dataTableConsulta: $("#tablaEstadoDigitalizacion")	
		};
		this.URL_CONSULTA_EST_DIG_CONTRATACIONES='../app/consulta-est-dig-contrataciones';
		this.EXPORT_EXCEL_EST_DIG_CONTRATACIONES = '../app/consulta-est-dig-contrataciones-excel';
	}
	
	ModuloEstadoDigitalizacionContrataciones.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};

	
	ModuloEstadoDigitalizacionContrataciones.prototype.exportarExcel = function(filtro){ 
		//debugger;
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.EXPORT_EXCEL_EST_DIG_CONTRATACIONES,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			xhrFields:{
                responseType: 'blob'
            },
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloEstadoDigitalizacionContrataciones(SISCORR_APP);
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