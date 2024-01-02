var MODULO_CORRESPONDENCIA_CONSULTA_JEFE_GESTOR = (function(){
	var instance;
	function ModuloCorrespondenciaConsultaJefeGestor(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnDescargarDocumento = $(".btnDescargarDocumento");
		this.componentesJG = {
			btnExportExcel: $("#btnExportarExcelJG"),
			btnFiltrosJG: $("#btnFiltrosJG"),
			dataTable: $("#tablaCorrespondenciasJG"),
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			cboxConsiderarDepenOriginadora: $("#cboxConsiderarDepenOriginadora"),
			cmbDependenciaOriginadora: $("#cmbDependenciaOriginadora"),
			cmbDependenciaRemitenteJG: $("#cmbDependenciaRemitenteJG"),
			txtCorrelativo: $("#txtCorrelativoJG"),
			txtAsunto: $("#txtAsuntoJG"),
			cbmEstado: $("#cbmEstadoJG"),
			btnBuscar: $("#btnBuscarJG"),
			btnResetear: $("#btnResetearJG"),
			filtrosSecundarios: $(".filtro_secundario"),
			cmbNombreOriginador: $("#cmbNombreOriginador"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesdeJG"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),
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
			
			txtFechaModificaDesde: $("#txtFechaModificaDesdeJG"),
			btnFechaModificaDesde: $("#btnFechaModificaDesdeJG"),
			txtFechaModificaHasta: $("#txtFechaModificaHastaJG"),
			btnFechaModificaHasta: $("#btnFechaModificaHastaJG")
		};
		this.URL_CONSULTA = '../app/emision/consultar-bandeja-salida';
		this.URL_CONSULTA_JEFE_GESTOR = '../app/emision/consultar-jefe-gestor';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS = '../app/emision/buscar/dependencia-usuario';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS_UM = '../app/emision/buscar/dependencia-usuario-um';
		this.URL_LISTAR_DEPENDENCIAS_REMITENTES = '../app/emision/buscar/dependencias-superiores';
		this.EXPORT_EXCEL = '../app/emision/consultar-jefe-gestor-excel';
		this.URL_LISTAR_ORIGINADORES = '../app/emision/buscar/todos-funcionarios';
		this.URL_LISTAR_TODAS_DEPENDENCIAS = '../app/emision/buscar/todos-dependencias';
		this.URL_LISTAR_TODAS_DEPENDENCIAS_EXT = '../app/emision/buscar/todos-dependencias-externas';
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_LISTAR_DEPENDENCIAS_TODOS = '../app/emision/buscar/dependencias-todas';
		this.URL_LISTAR_DEPENDENCIAS_TODOS_JEFE_GESTOR = '../app/emision/buscar/todos-dependencias-jefe-gestor';
		this.URL_CONSULTA_JEFE_GESTOR_PAGINADO = '../app/emision/consultar-jefe-gestor-paginado';
		this.URL_DESCARGAR_ARCHIVO_ADJUNTO = '../app/emision/descargar-archivo-con_firma';
		this.URL_OBTENER_CORRESPONDENCIA = '../app/emision/buscar/correspondencia';
	}
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.consultar = function(filtro){
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
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.listarDependencias = function(){
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
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.irADetalle = function(id){
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
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.exportarExcel = function(filtro){
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
	
    // TICKET 9000004808
	ModuloCorrespondenciaConsultaJefeGestor.prototype.obtenerCorrespondencia = function(idCorrespondencia){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_CORRESPONDENCIA + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaConsultaJefeGestor.prototype.descargarArchivoFirmaDigital = function(idCorrespondencia){
		var ref = this;
		window.open(ref.URL_DESCARGAR_ARCHIVO_ADJUNTO + "/" + idCorrespondencia, '_blank');
	};
	
    // fin
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaConsultaJefeGestor(SISCORR_APP);
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