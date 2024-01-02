var MODULO_CORRESPONDENCIA_CONSULTA_AUDITORIA = (function(){
	var instance;
	function ModuloCorrespondenciaConsulta(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnDescargarDocumento = $(".btnDescargarDocumento");
		this.componentes = {
			btnExportExcel: $("#btnExportarExcel"),
			btnFiltros: $("#btnFiltros"),
			dataTable: $("#tablaCorrespondencias"),
			dataTableConsulta: $("#tablaCorrespondenciasGeneralAuditoria"),
			cmbDependenciaOriginadora: $("#cmbDependenciaOriginadora"),
			cmbDependenciaRemitente: $("#cmbDependenciaRemitente"),
			txtCorrelativo: $("#txtCorrelativo"),
			txtAsunto: $("#txtAsunto"),
			cbmEstado: $("#cbmEstado"),
			btnBuscar: $("#btnBuscar"),
			btnResetear: $("#btnResetear"),
			btnMasFiltros: $("#btnMasFiltros"),
			btnMenosFiltros: $("#btnMenosFiltros"),
			filtrosSecundarios: $(".filtro_secundario"),
			cmbNombreOriginador: $("#cmbNombreOriginador"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
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
			txtFechaModificaDesde: $("#txtFechaModificaDesde"),
			btnFechaModificaDesde: $("#btnFechaModificaDesde"),
			txtFechaModificaHasta: $("#txtFechaModificaHasta"),
			btnFechaModificaHasta: $("#btnFechaModificaHasta")
		};
		this.URL_CONSULTA = '../app/emision/consultar-bandeja-salida';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS = '../app/emision/buscar/dependencia-usuario';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS_UM = '../app/emision/buscar/dependencia-usuario-um';
		this.URL_LISTAR_DEPENDENCIAS_REMITENTES = '../app/emision/buscar/dependencias-superiores';
		this.EXPORT_EXCEL = '../app/emision/consultar-auditoria_bandeja-salida-excel';
		this.URL_LISTAR_ORIGINADORES = '../app/emision/buscar/todos-funcionarios';
		this.URL_LISTAR_TODAS_DEPENDENCIAS = '../app/emision/buscar/todos-dependencias';
		this.URL_LISTAR_TODAS_DEPENDENCIAS_EXT = '../app/emision/buscar/todos-dependencias-externas';
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_LISTAR_DEPENDENCIAS_TODOS = '../app/emision/buscar/dependencias-todas';
		
		this.URL_CONSULTA_PAGINADO = '../app/emision/consultar-auditoria-bandeja-salida-paginado';
		this.URL_DESCARGAR_ARCHIVO_ADJUNTO = '../app/emision/descargar-archivo-con_firma';
		this.URL_OBTENER_CORRESPONDENCIA = '../app/emision/buscar/correspondencia';
	}
	
	ModuloCorrespondenciaConsulta.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloCorrespondenciaConsulta.prototype.consultar = function(filtro){
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
	
	ModuloCorrespondenciaConsulta.prototype.irADetalle = function(id){
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
	
	ModuloCorrespondenciaConsulta.prototype.exportarExcel = function(filtro){
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
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};

	ModuloCorrespondenciaConsulta.prototype.obtenerCorrespondencia = function(idCorrespondencia){
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
	
    ModuloCorrespondenciaConsulta.prototype.descargarArchivoFirmaDigital = function(idCorrespondencia){
		var ref = this;
		window.open(ref.URL_DESCARGAR_ARCHIVO_ADJUNTO + "/" + idCorrespondencia, '_blank');
	};
    // fin
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaConsulta(SISCORR_APP);
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