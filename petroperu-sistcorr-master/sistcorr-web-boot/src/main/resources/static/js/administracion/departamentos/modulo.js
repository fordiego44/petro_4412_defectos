var MODULO_CONSULTA_ASIGNACIONES = (function(){
	var instance;
	function ModuloConsultaAsignaciones(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Serwil';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		//this.compFiltrosBusqueda = $("#compFiltrosBusqueda");
		this.compSinResultados = $("#sinResultados");
		//this.classCompCorrespondencia = 'compCorrespondencia';
		this.btnFiltrar = $("#btnFiltrar");
		this.corrEstado = $("#corrEstado");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.panelFiltros = $(".masFiltros");

		//this.plantillaAsignaciones = $("#template-consulta-asignaciones");
		this.lblDepartamento = $("#lblDepartamento");
		
		this.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
		this.btnEliminar = $(".btnEliminar");
		this.btnEliminarDepartamento = $("#btnEliminarDepartamento")
		this.btnGrabar = $("#btnGrabar");
		this.URL_BASE_CORRESPONDENCIA = "app/correspondencias";
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		
		this.compModalEliminarDepartamento = $("#modalEliminarDepartamento");
		this.compModalAddUpdate = $("#modalAddUpdate");
	
		this.compCerrarSession = $(".closeSession");
		this.btnRetroceder = $("#btnRetroceder");

		this.compBusqueda = {
			btnExportExcel: $("#btnExportarExcel"),
			myModal: $("#modalBuscar"),
			dataTableConsulta: $("#tablaDepartamentosGeograficos"),
			btnFiltros: $("#btnFiltros"),
			btnMasFiltros: $("#btnMasFiltros"),
			btnMenosFiltros: $("#btnMenosFiltros"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		this.filtroBusqueda = {
			departamento : $("#nomDepartamento")
	
		};
		this.objetoDepartamento = {
				id : 0,
				codigoDepartamento: $("#textCodigoDeparamento"),
				departamento : $("#textDepartamento"),
				accion: ""
		
		};
		this.btnRegistrar = $("#btnRegistrar");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.EXPORT_EXCEL = '../configuracion/consultarDepaGeograficoExcel';

		this.URL_BUSCAR_DEPARTAMENTOS_GEOGRAFICOS = '../configuracion/buscarDepartamentosGeograficos';
		this.URL_INSERT_UPDATE_DETELE_DEPARTAMENTO = '../configuracion/crudDepartamentosGeograficos';
		
		
	};
		
	ModuloConsultaAsignaciones.prototype.crudDepartamentoGeogradifo = function(parametros){
		var ref = this;
		//var parametros = {"id": filtro.id.valueOf()};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_INSERT_UPDATE_DETELE_DEPARTAMENTO,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
		
	ModuloConsultaAsignaciones.prototype.abrirDocumento = function(documento){
		var ref = this;
		window.location.href = '../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento;
	};
	
	ModuloConsultaAsignaciones.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloConsultaAsignaciones(SISCORR_APP);
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
