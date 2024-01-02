var MODULO_CONSULTA_REEMPLAZOS = (function(){
	var instance;
	function ModuloConsultaReemplazos(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.componentes = {
				btnBuscar: $("#btnBuscar"),
				btnResetear: $("#btnResetear"),
				btnMasFiltros: $("#btnMasFiltros"),
				btnMenosFiltros: $("#btnMenosFiltros"),
				filtrosSecundarios: $(".filtro_secundario"),
				btnExportExcel: $("#btnExportarExcel"),
				btnFiltros: $("#btnFiltros"),
				dataTable: $("#tablaCorrespondencias"),
				dataTableConsulta: $("#tablaCorrespondenciasGeneral"),			
				txtCorrelativo: $("#txtCorrelativo"),
				cbmEstado: $("#cbmEstado"),
				
				txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
				txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
				txtFecha: $("#txtFecha"),
				
				btnFechaDocumentoDesde: $("#btnFechaDocumentoDesde"),
				btnFechaDocumentoHasta: $("#btnFechaDocumentoHasta"),
				btnFecha: $("#btnFecha"),
				
				txtNroDocumento: $("#txtNroDocumento"),
				cmbDependenciaRemitente: $("#cmbDependenciaRemitente"),
				cmbDependenciaDestinatario: $("#cmbDependenciaDestinatario"),
				cbmTipoCorrespondencia: $("#cbmTipoCorrespondencia"),
				txtEntidadExterna: $("#txtEntidadExterna"),
				cmbRol: $("#cmbRol"),
				txtAsunto: $("#txtAsunto")
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.URL_CONSULTA_PAGINADO = '../app/consultar-roles-reemplazo-paginado';
		this.EXPORT_EXCEL = '../app/consultar-roles-reemplazos-excel';
	}
	
	ModuloConsultaReemplazos.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloConsultaReemplazos.prototype.consultar = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaReemplazos.prototype.irADetalle = function(id){
		var ref = this;
		var url = window.location.origin;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		url = url + "/app/ver-detalle/" + id;
		window.location.replace(url);
	};
	
	ModuloConsultaReemplazos.prototype.exportarExcel = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.EXPORT_EXCEL,
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
        var object = new ModuloConsultaReemplazos(SISCORR_APP);
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