var MODULO_CONSULTA_ASIGNACIONES = (function(){
	var instance;
	function ModuloConsultaAsignaciones(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Winston';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		//this.compFiltrosBusqueda = $("#compFiltrosBusqueda");
		this.compSinResultados = $("#sinResultados");
		this.classCompCorrespondencia = 'compCorrespondencia';
		this.btnFiltrar = $("#btnFiltrar");
		this.corrEstado = $("#corrEstado");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.panelFiltros = $(".masFiltros");
		this.plantillaAsignaciones = $("#template-consulta-asignaciones");
		this.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
		this.URL_BASE_CORRESPONDENCIA = "app/correspondencias";
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		this.asigFechaAsignacionDesde = $("#asigFechaAsignacionDesde");
		this.btnFechaAsignacionDesde = $("#btnFechaAsignacionDesde");
		this.lblAsigCorrelativo = $("#lblAsigCorrelativo");//TICKET 9000004269
		this.asigFechaAsignacionHasta = $("#asigFechaAsignacionHasta");
		this.btnFechaAsignacionHasta = $("#btnFechaAsignacionHasta");
		this.asigFechaVencimientoDesde = $("#asigFechaVencimientoDesde");
		this.btnFechaVencimientoDesde = $("#btnFechaVencimientoDesde");
		this.asigFechaVencimientoHasta = $("#asigFechaVencimientoHasta");
		this.btnFechaVencimientoHasta = $("#btnFechaVencimientoHasta");
		this.vistaAsignaciones = $("#listaAsignaciones");
		this.compCerrarSession = $(".closeSession");
		this.compBusqueda = {
				btnExportExcel: $("#btnExportarExcel"),
				btnMasFiltros: $("#btnMasFiltros"),
				btnMenosFiltros: $("#btnMenosFiltros"),
			myModal: $("#modalBuscar"),
			//asustoCorrespondencia: $("#textAsuntoCorrespondencia"),
			tipoAccion: $("#asigTipoAccion"),
			//remitente: $("#textRemitente"),
			//numeroDocumento: $("#textNumeroDoc"),
			filtrosSecundarios: $(".filtro_secundario"),
			dataTable: $("#tablaAsignaciones"),
			dataTableConsulta: $("#tablaConsultaAsignaciones"),
			btnFiltros: $("#btnFiltros"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		this.filtroBusqueda = {
			correlativo : $("#asigCorrelativo"),
			codigoEstado : $("#asigEstado"),
			numeroDocumentoInterno : $("#asigNumeroDocumento"),
			fechaAsignacionDesde : $("#asigFechaAsignacionDesde"),
			fechaAsignacionHasta : $("#asigFechaAsignacionHasta"),
			tipoAccion : $("#asigTipoAccion"),
			dependenciaAsignante : $("#asigDependenciaAsignante"),
			personaAsignada : $("#asigPersonaAsignada"),
			fechaVencimientoDesde : $("#asigFechaVencimientoDesde"),
			fechaVencimientoHasta : $("#asigFechaVencimientoHasta")
		};
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.EXPORT_EXCEL = '../app/consultar-asignacion-excel';
		// TICKET 9000004494
		this.URL_BUSCAR_ASIGNACIONES_CONSULTA = '../' + this.URL_BASE_CORRESPONDENCIA + '/asignaciones/consultar';
		// FIN TICKET
	};
	

	
	ModuloConsultaAsignaciones.prototype.buscarAsignaciones = function(filtro){
		var ref = this;
		var parametros = {"correlativo": filtro.correlativo.val(), "codigoEstado": filtro.codigoEstado.val(), 
				"numeroDocumentoInterno": filtro.numeroDocumentoInterno.val(), "fechaAsignacionDesde": filtro.fechaAsignacionDesde.val(), 
				"fechaAsignacionHasta": filtro.fechaAsignacionHasta.val(), "tipoAccion": filtro.tipoAccion.val(), 
				"dependenciaAsignante": filtro.dependenciaAsignante.val(), "personaAsignada": filtro.personaAsignada.val(), 
				"fechaVencimientoDesde": filtro.fechaVencimientoDesde.val(), "fechaVencimientoHasta": filtro.fechaVencimientoHasta.val()};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignacion/buscar',
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaAsignaciones.prototype.htmlAsignaciones = function(asignaciones, rol_jefe, rol_gestor){
		var ref = this;
		Handlebars.registerHelper('empty', function(tipoIcono){
			if(tipoIcono == "" || tipoIcono == null || tipoIcono == "null"){
				return "-";
			}
            return tipoIcono;
        });
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_NoSA_PA_descarga', function(asignaciones){
			if(rol_jefe == true || (rol_gestor == true && (!(asignaciones && asignaciones.esConfidencial == "SI" && (asignaciones.esAsignado == null || asignaciones.esAsignado == "NO" || asignaciones.esAsignado == "")))) || (rol_jefe == false && rol_gestor == false && asignaciones && (asignaciones.esAsignado == "SI" || asignaciones.esConfidencial == "NO"))){
                return "display: block; text-align: center;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		var plantillaScript = ref.plantillaAsignaciones.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'asignaciones' : asignaciones};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloConsultaAsignaciones.prototype.obtenerInformacionDocumentoPrincipal = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/informacion-documento-principal/' + correlativo ,
			cache	:	false,
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
