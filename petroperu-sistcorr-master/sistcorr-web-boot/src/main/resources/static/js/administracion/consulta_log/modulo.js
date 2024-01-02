var MODULO_CONSULTALOG = (function(){
	var instance;
	function ModuloConsultaLog(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.compBtnRegistrarAdicion= $("#btnRegistrarAdicion");
		this.cmbDependencia= $("#cmbDependencia");
		this.btnEliminarReemplazo= $(".btnEliminarReemplazo");
		this.btnEliminarReemplazoAdicion=$("#btnEliminarReemplazoAdicion");
		this.btnModificarReemplazo = $(".btnModificarReemplazo");
		this.componentesJG = {
			btnExportExcel: $("#btnExportarExcelLogo"),
			btnFiltrosJG: $("#btnFiltrosJG"),
			dataTable: $("#tablaCorrespondenciasJG"),
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			cboxConsiderarDepenOriginadora: $("#cboxConsiderarDepenOriginadora"),
			cmbDependenciaOriginadora: $("#cmbDependenciaOriginadora"),
			cmbDependenciaRemitenteJG: $("#cmbDependenciaRemitenteJG"),
			txtCorrelativo: $("#txtCorrelativoJG"),
			txtAsunto: $("#txtAsuntoJG"),
			cbmEstado: $("#cbmEstadoJG"),
			filtrosSecundarios: $(".filtro_secundario"),
			cmbNombreOriginador: $("#cmbNombreOriginador"),
			txtFechaAdicionDesdeJG:$("#txtFechaAdicionDesdeJG"),
			cbmTipoCorrespondencia: $("#cbmTipoCorrespondencia"),
			cbmTipoEmision: $("#cbmTipoEmision"),
			rbtnTipoDestinatario: $("input:radio[name='rbtnTipoDestinatario']:checked"),
			cbmFlujoFirma: $("#cbmFlujoFirma"),
			cbmConfidencialidad: $("#cbmConfidencialidad"),
			cbmUrgente: $("#cbmUrgente"),
			cbmDespachoFisico: $("#cbmDespachoFisico"),
			cbmDependenciaDestinatariaInterno: $("#cbmDependenciaDestinatariaInterno"),
			cbmDependenciaDestinatariaExternaNacional : $("#cbmDependenciaDestinatariaExternaNacional"),
			txtDependenciasDestinatariaExternaInternacional : $("#txtDependenciasDestinatariaExterna"),
			txtDependenciasDestinatariaExternaNacional : $("#txtDependenciasDestinatariaExternaNacional"),
			cbmDependenciaCopia: $("#cbmDependenciaCopia"),
			/*FILTRO LOG*/
			cmbTablaFiltro: $("#cmbTablaFiltro"),
			cmbAccionFiltro: $("#cmbAccionFiltro"),
			txtUsuarioFiltro: $("#txtUsuarioFiltro"),
			btnBuscar: $("#btnBuscarFiltroLog"),
			btnResetear: $("#btnResetearFiltroLog"),
			txtFechaDocumentoDesde: $("#txtFechaIniRecepcion"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.URL_CONSULTA_LOG='../app/consulta-log';
		this.EXPORT_EXCEL = '../app/consultar-log-excel';
	}
	
	ModuloConsultaLog.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloConsultaLog.prototype.consultar = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA_JEFE_GESTOR,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaLog.prototype.listarDependencias = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({					
			url: ref.URL_LISTAR_DEPENDENCIAS_TODOS_JEFE_GESTOR,
			type: 'GET',
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }/*,
			processResults: function (respuesta) {				
				ref.listas.dependencias.agregarLista(respuesta.datos);
				return {results: respuesta.datos};
			}*/
		});
	};
	
	ModuloConsultaLog.prototype.irADetalle = function(id){
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
	
	ModuloConsultaLog.prototype.eliminarReemplazoAdicion = function(parametros){
		var ref=this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_ELIMINAR_REEMPLAZO,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaLog.prototype.exportarExcel = function(filtro){
		//debugger;
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
        var object = new ModuloConsultaLog(SISCORR_APP);
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