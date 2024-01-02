var MODULO_HISTORIAL_COMPARTIDO = (function(){
	var instance;
	function ModuloHistorialCompartido(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Winston';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.componentes = {
			hdnIdCorrespondencia: $("#idCorrespondencia"),
			btnFiltros: $("#btnFiltros"),
			btnRetroceder: $("#btnRetroceder"),
			btnExportExcel: $("#btnExportar"),
			txtFechaNotificacionDesde: $("#txtFechaNotificacionDesde"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaNotificacionHasta: $("#txtFechaNotificacionHasta"),
			btnFechaHasta: $("#btnFechaHasta"),
			txtValorBuscar : $("#txtValorBuscar"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar"),
			dataTable: $("#tablaHistorial")
		};
		this.URL_CONSULTA = '../obtener-historial';
		this.EXPORT_EXCEL = '../obtener-historial-excel';
		
	}
	
	ModuloHistorialCompartido.prototype.consultar = function(filtro, id){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA + "/" + id,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloHistorialCompartido.prototype.exportarExcel = function(filtro, idCorrespondencia){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.EXPORT_EXCEL + "/"  +idCorrespondencia,
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
        var object = new ModuloHistorialCompartido(SISCORR_APP);
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