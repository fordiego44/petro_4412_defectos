var MODULO_CONSULTA_CORRESPONDENCIA = (function(){
	var instance;
	function ModuloConsultaCorrespondencia(SISCORR_APP){
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
		this.plantillaCorrespondencias = $("#template-consulta-correspondencias");
		this.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
		this.URL_BASE_CORRESPONDENCIA = "app/correspondencias";
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		this.compModalDescargarArchivo = $("#modalDescargarArchivo");
		this.compBtnDescargarArchivo = $("#btnDescargaArchivoSi");
		this.compFechaRegistroDesde = $("#corrFechaRegistroDesde");
		this.btnFechaRegistroDesde = $("#btnFechaRegistroDesde");
		this.compFechaRegistroHasta = $("#corrFechaRegistroHasta");
		this.btnFechaRegistroHasta = $("#btnFechaRegistroHasta");
		this.compFechaDocumentoInterno = $("#corrFechaDocumentoInterno");
		this.btnFechaDocumentoInterno = $("#btnFechaDocumentoInterno");
		this.btnFechaDesde = $("#btnFechaDesde");//TICKET 9000004269
		this.btnFechaHasta = $("#btnFechaHasta");//TICKET 9000004269
		this.lblFechaDocumentoDesde = $("#lblFechaDocumentoDesde");//TICKET 9000004269
		this.lblCorrelativo = $("#lblCorrelativo");//TICKET 9000004269
		this.lblFechaDocumentoHasta = $("#lblFechaDocumentoHasta");//TICKET 9000004269
		this.vistaCorrespondencias = $("#listaCorrespondencias");
		this.compCerrarSession = $(".closeSession");
		this.compBusqueda = {
				btnExportExcel: $("#btnExportarExcel"),
				btnMasFiltros: $("#btnMasFiltros"),
				btnMenosFiltros: $("#btnMenosFiltros"),
				filtrosSecundarios: $(".filtro_secundario"),
				dataTable: $("#tablaCorrespondencias"),
				dataTableConsulta: $("#tablaConsultasCorrespondencias"),
				btnFiltros: $("#btnFiltros"),
				correlativo : $("#corrCorrelativo"),
				codigoEstado : $("#corrEstado"),
				fechaRegistroDesde : $("#corrFechaRegistroDesde"),
				fechaRegistroHasta : $("#corrFechaRegistroHasta"),
				numeroDocumentoInterno : $("#corrNumeroDocumentoInterno"),
				fechaDocumentoInterno : $("#corrFechaDocumentoInterno"),
				codigoDependenciaRemitente : $("#corrDependenciaRemitente"),
				codigoDependenciaDestino : $("#corrDependenciaDestinatario"),
				codigoTipoCorrespondencia : $("#corrTipoCorrespondencia"),
				nombreDependenciaExterna : $("#corrEntidadExterna"),
				guiaRemision : $("#corrGuiaRemision"),
				asunto : $("#corrAsunto"),
				procedencia : $("#corrProcedencia"),
			myModal: $("#modalBuscar"),
			//asustoCorrespondencia: $("#textAsuntoCorrespondencia"),
			//tipoAccion: $("#textTipoAccion"),
			//remitente: $("#textRemitente"),
			//numeroDocumento: $("#textNumeroDoc"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		this.filtroBusqueda = {
			correlativo : $("#corrCorrelativo"),
			codigoEstado : $("#corrEstado"),
			fechaRegistroDesde : $("#corrFechaRegistroDesde"),
			fechaRegistroHasta : $("#corrFechaRegistroHasta"),
			numeroDocumentoInterno : $("#corrNumeroDocumentoInterno"),
			fechaDocumentoInterno : $("#corrFechaDocumentoInterno"),
			codigoDependenciaRemitente : $("#corrDependenciaRemitente"),
			codigoDependenciaDestino : $("#corrDependenciaDestinatario"),
			codigoTipoCorrespondencia : $("#corrTipoCorrespondencia"),
			nombreDependenciaExterna : $("#corrEntidadExterna"),
			guiaRemision : $("#corrGuiaRemision"),
			asunto : $("#corrAsunto"),
			procedencia : $("#corrProcedencia"),
			valProcedencia: ''
		};
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.EXPORT_EXCEL = '../app/consultar-correspondencia-excel';
		// TICKET 9000004494
		this.URL_BASE_CONSULTAR_CORRESPONDENCIA = 'app';
		this.URL_BUSCAR_CORRESPONDENCIAS_CONSULTA = '../' + this.URL_BASE_CORRESPONDENCIA + '/correspondencia/consultar';
		// FIN TICKET
		
	};
	
	ModuloConsultaCorrespondencia.prototype.obtenerInformacionDocumentoPrincipal = function(correlativo){
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
	
	ModuloConsultaCorrespondencia.prototype.abrirDocumento = function(documento){
		var ref = this;
		window.location.href = '../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento;
	};
	
	ModuloConsultaCorrespondencia.prototype.buscarCorrespondencias = function(filtro){
		var ref = this;
		console.log("FILTRO CORRESPONDENCIA:");
		console.log(filtro)
		var procedencia = "NC";
		if(filtro.procedencia.is(':checked')){
			procedencia = "";
		}
		var parametros = {"correlativo": filtro.correlativo.val(), "codigoEstado": filtro.codigoEstado.val(), "fechaRegistroDesde": filtro.fechaRegistroDesde.val(), 
				"fechaRegistroHasta": filtro.fechaRegistroHasta.val(), "numeroDocumentoInterno": filtro.numeroDocumentoInterno.val(), 
				"fechaDocumentoInterno": filtro.fechaDocumentoInterno.val(), "codigoDependenciaRemitente": filtro.codigoDependenciaRemitente.val(), 
				"codigoDependenciaDestino": filtro.codigoDependenciaDestino.val(), "codigoTipoCorrespondencia": filtro.codigoTipoCorrespondencia.val(), 
				"nombreDependenciaExterna": filtro.nombreDependenciaExterna.val(), "guiaRemision": filtro.guiaRemision.val(), "asunto": filtro.asunto.val(), 
				"procedencia": procedencia};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/correspondencia/buscar',
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaCorrespondencia.prototype.htmlCorrespondencias = function(correspondencias, rol_jefe, rol_gestor){
		var ref = this;
		Handlebars.registerHelper('empty', function(tipoIcono){
			if(tipoIcono == "" || tipoIcono == null || tipoIcono == "null"){
				return "-";
			}
            return tipoIcono;
        });
		Handlebars.registerHelper('icono_noNCEM', function(correspondencia){
			if(correspondencia.tipoIcono != 'NC' && correspondencia.tipoIcono != 'EM'){
				return "display: block;"
			}
            return "display: none;";
        });
		Handlebars.registerHelper('iconoNC', function(correspondencia){
			if(correspondencia.tipoIcono == 'NC'){
				return "display: block;"
			}
            return "display: none;";
        });
		
		Handlebars.registerHelper('iconoEM', function(correspondencia){
			if(correspondencia.tipoIcono == "EM"){
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_SA_PA_descarga', function(correspondencia){
			if(rol_jefe == true || (rol_gestor == true && (!(correspondencia && correspondencia.esConfidencial == "SI" && (correspondencia.esAsignado == null || correspondencia.esAsignado == "NO" || correspondencia.esAsignado == "")))) || (rol_jefe == false && rol_gestor == false && correspondencia && (correspondencia.esAsignado == "SI" || correspondencia.esConfidencial == "NO"))){
                return "display: block; text-align: center;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		var plantillaScript = ref.plantillaCorrespondencias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'correspondencias' : correspondencias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloConsultaCorrespondencia.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloConsultaCorrespondencia(SISCORR_APP);
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
