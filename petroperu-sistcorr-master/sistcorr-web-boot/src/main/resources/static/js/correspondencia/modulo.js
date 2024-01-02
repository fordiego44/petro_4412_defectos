var MODULO_CORRESPONDENCIA = (function(){
	var instance;
	function ModuloCorrespondencia(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		//this.URL_BASE_CORRESPONDENCIA = "./correspondencias"
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		this.tipoCorrespondencia = $("#tipoCorrespondencia");
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.classCompCorrespondencia = 'compCorrespondencia';
		this.classCompAsignacion = 'compAsignacion';
		this.classVerificarDocumentoPrincipalCorrespondencia = ".btnVerificarDocPrincipal_Correspondencia"
		this.classVerificarDocumentoPrincipalAsignacion = ".btnVerificarDocPrincipal_Asignacion";
		this.compTarea = $(".compTarea");
		this.classTareaFooter = 'footer_tarea';
		this.compTareaFooter = $(".footer_tarea");
		this.compCerrarSession = $(".closeSession");
		this.compMenuCantidad = $(".menucantidad");
		this.compTipoBandeja = $("#tipoBandeja");
		this.compUrl = $("#url");
		this.compNuevaCorrespondencia = $("#nuevaCorrespondencia");
		this.compPorAceptar = $("#porAceptar");
		this.compListaAsignaciones = $("#listaAsignaciones");
		this.compListaCorrespondencia = $("#listaCorrespondencias");
		this.compAbrirCompletarCorrespondencia = "btnCompletarCorrespondencia";
		this.compAbrirRechazarAsignacionCorresp = "btnRechazarAsignacionCorresp";//TICKET 9000004273
		this.compAbrirCerrarCorrespondencia = "btnCerrarCorrespondencia";
		this.compModalCerrarCorrespondencia = $("#modalCerrarCorrespondencia");
		this.compBtnCerrarCorrespondencia = $("#btnCerrarCorrespondencia");
		this.compComentarioCerrarCorrespondencia = $("#textCerrarCorrespondencia");
		// TICKET 9000004044
		this.compChkRequiereDocumentoCerrar = $("#requiereRespuestaCerrar");
		this.compTxtNumeroDocumentoCerrar = $("#textNumeroDocumentoCerrar");
		// FIN TICKET
		this.compModalCompletarCorrespondencia = $("#modalCompletarCorrespondencia");
		this.compModalRechazarAsignacionCorres = $("#modalRechazarAsignacionCorres");//TICKET 9000004273
		this.compComentarioCompletarCorrespondencia = $("#textCompletarCorrespondencia");
		this.compTxtMotivoRechazarAsignacionCorres = $("#textMotivoRechazarAsignacionCorres");//TICKET 9000004273
		this.compbtnCompletarCorrespondencia = $("#btnCompletarCorrespondencia");
		this.compbtnRechazarAsignacionCorres = $("#btnRechazarAsignacionCorres");//TICKET 9000004273
		// TICKET 900004044
		this.compChkRequiereDocumentoCompletar = $("#requiereRespuestaCompletar");
		this.compTxtNumeroDocumentoCompletar = $("#textNumeroDocumentoCompletar");
		// FIN TICKET
		this.compAbrirAceptarCorrespondencia = "btnAceptarCorrespondencia";
		this.compModalAceptarCorrespondencia = $("#modalAceptarCorrespondencia");
		this.compBtnAceptarCorrespondencia = $("#btnAceptarCorrespondencia");
		this.compAbrirRechazarCorrespondencia = "btnRechazarCorrespondencia";
		this.compModalRechazarCorrespondencia = $("#modalRechazarCorrespondencia");
		this.compBtnRechazarCorrespondencia = $("#btnRechazarCorrespondencia");
		this.compComentarioRechazarCorrespondencia = $("#textRechazarCorrespondencia");
		this.plantillaCorrespondencias = $("#template-correspondencias");
		this.plantillaAsignaciones = $("#template-asignaciones");
		this.plantillaFiltros = $("#template-filtros");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.btnActualizarBandeja = $("#btnActualizarBandeja");//ticket 9000004710
		this.btnContinuarBusqCA= $("#btnContinuarExportarCA");//ticket 9000004807
		this.btnExportarExcelCorrespondencia = $("#btnExportarExcelCorrespondencia");//ticket 9000004807
		this.compModalAdvertenciaExportar = $("#modalAdvertenciaExportar");//TICKET 9000004807
		this.dependenciaDefecto = "";
		this.compBusqueda = {
				myModal: $("#modalBuscar"),
				dependenciaDestino: $("#textDependenciaDestino"),
				dependenciaDestinoBPAC: $("#textDependenciaDestinoBPAC"),//TICKET 9000003866
				asustoCorrespondencia: $("#textAsuntoCorrespondencia"),
				tipoAccion: $("#textTipoAccion"),
				remitente: $("#textRemitente"),
				correlativo: $("#textCorrelativo"),
				numeroDocumento: $("#textNumeroDoc"),
				btnFechaDesde: $("#btnFechaDesde"),
				fechaDesde: $("#textFechaDesde"),
				btnFechaHasta: $("#btnFechaHasta"),
				fechaHasta: $("#textFechaHasta"),
				btnBuscar: $("#btnBuscar")
				
		},
		this.compModalDescargarArchivo = $("#modalDescargarArchivo");
		this.compBtnDescargarArchivo = $("#btnDescargaArchivoSi");
		this.compFiltrosBusqueda = $("#filtrosBusqueda");
		this.compSinResultados = $("#sinResultados");
		this.compVerMas = $("#verMas");
		this.cantidadTotales = "totales";
		this.URL_BASE_CORRESPONDENCIA = "app/correspondencias";
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.compCorrelativo = $("#correlativoDef");
		// TICKET 9000004044
		this.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA = "app/validarExisteDocumentoRespuestaCorrespondencia";
		this.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION = "app/validarExisteDocumentoRespuestaAsignacion";
		this.URL_NUEVA_CORRESPONDENCIA = "app/registro";
		this.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO = "app/verificarEstadoNumeroDocumento";
		this.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO = "app/enlazarCorrespondenciaNumeroDocumento";
		// FIN TICKET
		
		// TICKET 9000004497
		this.classCheckAsignacionGrupal = "check_asignacion_grupal";
		this.btnAsignacionGrupal = $("#btnAsignacionGrupal");
		// FIN TICKET
	}
	
	
	ModuloCorrespondencia.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloCorrespondencia.prototype.listarCorrespondenciasAsignaciones = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.compTipoBandeja.val();
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type	:	'GET',
				url		: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja,
				cache	:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type	:	'GET',
				url		: 	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja,
				cache	:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.buscarCorrespondenciasAsignaciones = function(filtros){
		var ref = this;
		var data = {'searchCriteria'	: 	filtros};
		console.log(JSON.stringify(data));
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.compTipoBandeja.val();
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type		:	'POST',
				url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja + '/buscar',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'POST',
				url			: 	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja + '/buscar',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
		
	};
	
	//INI TICKET 9000004807
	ModuloCorrespondencia.prototype.exportarExcelCorrespondenciasAsignaciones = function(filtros){
		var ref = this;
		var data = {'searchCriteria'	: 	filtros};
		console.log(JSON.stringify(data));
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.compTipoBandeja.val();
		
		return $.ajax({
			type		:	'POST',
			url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja + '/exportarExcel',
			cache		:	false,
			data		:	JSON.stringify(data),
			xhrFields	:	{
                responseType: 'blob'
            },
			beforeSend: function(xhr) {
		        
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	//FIN TICKET 9000004807
	
	// TICKET 9000004497
	ModuloCorrespondencia.prototype.validarAsignacionGrupal = function(filtros){
		var ref = this;
		var data = {'listAsignacionGrupal'	: 	filtros};
		console.log(JSON.stringify(data));
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.compTipoBandeja.val();
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type		:	'POST',
				url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja + '/validarAsignacionGrupal',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'POST',
				url			: 	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/' + bandeja + '/validarAsignacionGrupal',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
		
	};
	// FIN TICKET 
	
	// TICKET 9000004494
	ModuloCorrespondencia.prototype.filtrarCorrespondenciasSiguiente = function(respuesta){
		var ref = this;
		var data = {'continuationData': respuesta.continuationData, 'totalPagina': respuesta.totalPagina}
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.compTipoBandeja.val();
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type		:	'POST',
				url			: 	'../' + ref.URL_BASE_CORRESPONDENCIA + '/filtrarSiguientePagina',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'POST',
				url			: 	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/filtrarSiguientePagina',
				cache		:	false,
				data		:	JSON.stringify(data),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
		
	};
	// FIN TICKET
	
	ModuloCorrespondencia.prototype.obtenerInformacionDocumentoPrincipal = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		console.log("Correlativo:" + correlativo);
		if(correlativoDef==""){
			console.log('../' + ref.URL_BASE_CORRESPONDENCIA + '/informacion-documento-principal/' + correlativoDef);
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
		}else{
			return $.ajax({
				type	:	'GET',
				url		: 	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/informacion-documento-principal/' + correlativo ,
				cache	:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.abrirDocumento = function(documento){
		var ref = this;
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			window.location.href = '../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento;
		}else{
			window.location.href = '../../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento;
		}
	};
	
	ModuloCorrespondencia.prototype.completarCorrespondencia = function(idAsignacion, respuesta, documento){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"respuesta": respuesta, "documentoRespuesta": documento };
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/completar/' + idAsignacion,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/completar/' + idAsignacion,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	//INICIO TICKET 9000004273
	ModuloCorrespondencia.prototype.rechazarAsignacionCorrespondencia = function(idAsignacion, motivo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": motivo};
		var correlativo = $("#correlativoDef").val();
		if(correlativo==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazarasignacion/' + idAsignacion,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazarasignacion/' + idAsignacion,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	//FIN TICKET 9000004273
	
	ModuloCorrespondencia.prototype.cerrarCorrespondencia = function(correlativo, observacion, documentoRespuesta){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion, "documentoRespuesta": documentoRespuesta };
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/cerrar/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/cerrar/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.aceptarCorrespondencia = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/aceptar/' + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/aceptar/' + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.procesarAceptarCorrespondencia = function(nroDocumento, correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/procesarAceptar/' + nroDocumento + '/' + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/procesarAceptar/' + nroDocumento + '/' + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.rechazarCorrespondencia = function(correlativo, observacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion };
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazar/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazar/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.procesarRechazarCorrespondencia = function(nroDocumento, correlativo, comentario){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": comentario };
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/procesarRechazar/' + nroDocumento + '/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'PUT',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/procesarRechazar/' + nroDocumento + '/' + correlativo,
				data		:	JSON.stringify(parametro),
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.obtenerCorrespondencia = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'GET',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + "/recuperar/" + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'GET',
				url			:	'../../' + ref.URL_BASE_CORRESPONDENCIA + "/recuperar/" + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	// TICKET 9000004044
	
	ModuloCorrespondencia.prototype.validarExisteDocumentoRespuestaCorrespondencia = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'GET',
				url			:	'../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA + "/" + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'GET',
				url			:	'../../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA + "/" + correlativo,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.validarExisteDocumentoRespuestaAsignacion = function(idAsignacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'GET',
				url			:	'../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION + "/" + idAsignacion,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'GET',
				url			:	'../../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION + "/" + idAsignacion,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.irANuevaCorrespondencia = function(){
		var ref = this;
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			window.location.replace("../" + ref.URL_NUEVA_CORRESPONDENCIA);
		}else{
			window.location.replace("../../" + ref.URL_NUEVA_CORRESPONDENCIA);
		}
		
	};
	
	ModuloCorrespondencia.prototype.verificarEstadoNumeroDocumento = function(numeroDocumento){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'GET',
				url			:	'../' + ref.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO + "/" + numeroDocumento,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'GET',
				url			:	'../../' + ref.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO + "/" + numeroDocumento,
				cache		:	false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	};
	
	ModuloCorrespondencia.prototype.enlazarCorrespondenciaNumeroDocumento = function(correspondencia){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'POST',
				url			:	'../' + ref.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO,
				cache		:	false,
				data		:	JSON.stringify(correspondencia),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}else{
			return $.ajax({
				type		:	'POST',
				url			:	'../../' + ref.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO,
				cache		:	false,
				data		:	JSON.stringify(correspondencia),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
	}
	
	// FIN TICKET
	
	
	ModuloCorrespondencia.prototype.htmlCorrespondencias = function(correspondencias, rol_jefe, rol_gestor){
		var ref = this;
		var nuevaCorrespondencia = ref.compNuevaCorrespondencia.val();
		var porAceptar = ref.compPorAceptar.val();
		var correspondenciaMPV = $("#correspondenciaMPV").val();//TICKET 9000005111
		Handlebars.registerHelper('estiloNO_SA', function(tipoIcono){
			if(!tipoIcono){
				return "display: none; cursor: default;"
			}
            if(nuevaCorrespondencia != tipoIcono){
                return "display: none; cursor: default;";
            }
            return "display: block;";
        });
		// TICKET 9000003862
		Handlebars.registerHelper('estiloNO_SA_PA', function(tipoIcono){
			if(!tipoIcono){
				return "display: none; cursor: default;"
			}
            if(nuevaCorrespondencia != tipoIcono && porAceptar != tipoIcono){
                return "display: none; cursor: default;";
            }
            return "display: block;";
        });
		// FIN TICKET
		Handlebars.registerHelper('estiloSA', function(tipoIcono){
			if(!tipoIcono){
				return "display: none; cursor: default;"
			}
			// TICKET 9000003862
            if(nuevaCorrespondencia == tipoIcono){
                return "display: none; cursor: default;";
            }
            return "display: block:";
        });
		// TICKET 9000003862
		Handlebars.registerHelper('estiloSA_PA', function(tipoIcono){
			if(!tipoIcono){
				return "display: none; cursor: default;"
			}
            if(nuevaCorrespondencia == tipoIcono || porAceptar == tipoIcono){
                return "display: none; cursor: default;";
            }
            return "display: block:";
        });
		// FIN TICKET
		Handlebars.registerHelper('estilo_opciones_SA', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            if(nuevaCorrespondencia == corres.tipoIcono || (corres.tipoIcono && correspondenciaMPV == corres.tipoIcono.trim())){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// TICKET 9000003862
		Handlebars.registerHelper('estilo_opciones_SA_PA', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            if(correspondenciaMPV == corres.tipoIcono.trim() || nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET		
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_SA_PA_descarga', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
			//ticket 9000005111 dd mpv condicion
            if((correspondenciaMPV == corres.tipoIcono.trim() || nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (rol_jefe == true || (rol_gestor == true && (!(corres && corres.esConfidencial == "SI" && (corres.esAsignado == null || corres.esAsignado == "NO" || corres.esAsignado == "")))) || (rol_jefe == false && rol_gestor == false && (corres.esAsignado == "SI" || corres.esConfidencial == "NO")))){
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_SA_PA_copia', function(corres){
			if(corres.correlativo=="CRI-OFP-11196-2020"){
				console.log(corres);
				console.log((nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (rol_jefe == true || (!(corres && corres.esConfidencial == "SI"))));
			}
			if(!corres){
				return "display: none; cursor: default;"
			}
            //if((nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (rol_jefe == true || (rol_gestor == true && corres && corres.esConfidencial == "NO" && (corres.esAsignado == null || corres.esAsignado == "NO" || corres.esAsignado == "")))){
			//if((nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (rol_jefe == true || (rol_gestor == true && (!(corres && corres.esConfidencial == "SI"))))){
			//if((nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (rol_jefe == true || (!(corres && corres.esConfidencial == "SI")))){
			if((correspondenciaMPV == corres.tipoIcono.trim() || nuevaCorrespondencia == corres.tipoIcono || porAceptar == corres.tipoIcono) && (corres && corres.esConfidencial == "NO")){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET		
		Handlebars.registerHelper('estilo_opciones_NoSA', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            if(nuevaCorrespondencia != corres.tipoIcono && (corres.tipoIcono && correspondenciaMPV != corres.tipoIcono.trim())){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		
		//INICIO TICKET 9000004273
		Handlebars.registerHelper('estilo_opciones_NoSAR', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            if(correspondenciaMPV != corres.tipoIcono.trim() && nuevaCorrespondencia != corres.tipoIcono && corres.sVersionNum >= VERSION_BAND_ENTRADA_ASIG){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		//FIN TICKET 9000004273
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_NoSA_PA_copia', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            //if(nuevaCorrespondencia != corres.tipoIcono){
			//if((nuevaCorrespondencia != corres.tipoIcono && porAceptar != corres.tipoIcono) && (rol_jefe == true || (rol_gestor == true && (!(corres && corres.esConfidencial == "SI"))))){
			if(correspondenciaMPV != corres.tipoIcono.trim() && nuevaCorrespondencia != corres.tipoIcono && porAceptar != corres.tipoIcono && corres && corres.esConfidencial == "NO"){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		// TICKET 9000003862
		Handlebars.registerHelper('estilo_opciones_NoSA_PA', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
            if(correspondenciaMPV != corres.tipoIcono.trim() && nuevaCorrespondencia != corres.tipoIcono && porAceptar != corres.tipoIcono){//TICKET 9000005111
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		// TICKET 9000003780
		Handlebars.registerHelper('estilo_opciones_NoSA_PA_descarga', function(corres){
			if(!corres){
				return "display: none; cursor: default;"
			}
			//TICKET 9000005111 ADD CONDICION MPV
            if((correspondenciaMPV != corres.tipoIcono.trim() && nuevaCorrespondencia != corres.tipoIcono && porAceptar != corres.tipoIcono) && (rol_jefe == true || (rol_gestor == true && (!(corres && corres.esConfidencial == "SI" && (corres.esAsignado == null || corres.esAsignado == "NO" || corres.esAsignado == "")))) || (rol_jefe == false && rol_gestor == false && (corres.esAsignado == "SI" || corres.esConfidencial == "NO")))){
                return "display: block;";
            }
            return "display: none; cursor: default;";
        });
		// FIN TICKET
		// TICKET 9000003862
		Handlebars.registerHelper('estilo_PA', function(corres){
			if(ref.compTipoBandeja.val() == "DelGestor"){
				return "display: block;"
			}
            return "display: none; cursor: default;";
        });
		Handlebars.registerHelper('estilo_NoPA', function(corres){
			
			if(ref.compTipoBandeja.val() != "DelGestor"){
				return "display: block;"
			}
            return "display: none; cursor: default;";
        });
		
		//INICIO TICKET 9000004807
		Handlebars.registerHelper('checkDetalleSolcitud', function(corres){
			
			if(corres.detalleSolicitud != null && corres.detalleSolicitud != undefined){
				return ""+corres.detalleSolicitud;
			}
            return "";
        });
		//INICIO TICKET 9000004807
		
		Handlebars.registerHelper('estilo_NoPA_RHZASIG', function(corres){
			
			if((ref.compTipoBandeja.val() == "Pendientes" || ref.compTipoBandeja.val() == "EnAtencion") &&
					(correspondenciaMPV != corres.tipoIcono.trim() && nuevaCorrespondencia != corres.tipoIcono && corres.sVersionNum >= VERSION_BAND_ENTRADA_ASIG) &&
					(corres.esAsignado && corres.esAsignado == "SI")){//TICKET 9000005111
				return "display: block;"
			}
            return "display: none; cursor: default;";
        });
		//FIN TICKET 9000004273
		// FIN TICKET
		
		//Handlebars.registerHelper('estilo_opciones')
		
		Handlebars.registerHelper('url_decargar_doc_principal', function(correspondencia){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return ref.URL_BASE_CORRESPONDENCIA + "/descargar/documento-principal/" + correspondencia.correlativo;
			}else{
				return '../' + ref.URL_BASE_CORRESPONDENCIA + "/descargar/documento-principal/" + correspondencia.correlativo;
			}
        });
		var url = ref.compUrl.val();
		Handlebars.registerHelper('url_pagina_detalle_correspondencia', function(correspondencia){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/informacion/corr/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}else{
				return "../" + url + "/informacion/corr/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}
        });
		Handlebars.registerHelper('url_pagina_asignar_correspondencia', function(correspondencia){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/asignacion/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}else{
				return "../" + url + "/asignacion/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}
        });
		
		Handlebars.registerHelper('url_pagina_detalle_asignacion', function(asignacion){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/informacion/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}else{
				return "../" + url + "/informacion/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}
		});
		Handlebars.registerHelper('url_pagina_asignar_asignacion', function(asignacion){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/asignacion/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}else{
				return "../" + url + "/asignacion/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}
        });
		
		// TICKET 9000003514
		Handlebars.registerHelper('url_pagina_enviar_copia_correspondencia', function(correspondencia){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/copia/corr/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}else{
				return "../" + url + "/copia/corr/" + correspondencia.correlativo + "?workflow=" + correspondencia.workflowId;
			}
        });
		
		Handlebars.registerHelper('url_pagina_enviar_copia_asignacion', function(asignacion){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/copia/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}else{
				return "../" + url + "/copia/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}
		});
		// FIN TICKET
		
		Handlebars.registerHelper('esJefe', function(tipoIcono){
			if(rol_jefe == false){
				return "display: none; cursor: default;"
			}
            return "display: block:";
        });
		
		Handlebars.registerHelper('esGestor', function(){
			if(rol_gestor == false){
				return "display: none; cursor: default;";
			}
			return "display: block;";
		});
		
		Handlebars.registerHelper('estilo_GestorCompletar', function(correspondencia){
			if(correspondencia.esAsignado && correspondencia.esAsignado == "SI"){
				return "display: block;";
			}
			return "display: none; cursor: default;";
		});
		
		//INICIO TICKET 9000004273
		Handlebars.registerHelper('estilo_GestorRechazarAsig', function(correspondencia){
			if(correspondencia.esAsignado && correspondencia.esAsignado == "SI" && correspondencia.sVersionNum >= VERSION_BAND_ENTRADA_ASIG){
				return "display: block;";
			}
			return "display: none; cursor: default;";
		});
		//FIN TICKET 9000004273
		var plantillaScript = ref.plantillaCorrespondencias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'correspondencias' : correspondencias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	
	ModuloCorrespondencia.prototype.htmlAsignaciones = function(asignaciones, rol_jefe){
		var ref = this;
		var url = ref.compUrl.val();
		Handlebars.registerHelper('url_pagina_detalle_asignacion', function(asignacion){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/informacion/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}else{
				return "../" + url + "/informacion/asig/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}
		});
		Handlebars.registerHelper('url_pagina_asignar_asignacion', function(asignacion){
			var correlativo = $("#correlativoDef").val();
			if(correlativo==""){
				return url + "/asignacion/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}else{
				return "../" + url + "/asignacion/" + asignacion.idAsignacion + "?workflow=" + asignacion.workflowId;
			}
        });
		Handlebars.registerHelper('esJefe', function(tipoIcono){
			if(rol_jefe == false){
				return "display: none;"
			}
            return "display: block:";
        });
		Handlebars.registerHelper('esGestor', function(){
			if(rol_gestor == false){
				return "display: none; cursor: default;";
			}
			return "display: block;";
		});
		var plantillaScript = ref.plantillaAsignaciones.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'asignaciones' : asignaciones};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondencia.prototype.htmlFiltros = function(filtros){
		var ref = this;
		var items = filtros.slice(0);
		var _filtros = [];
		for(var i in items){
			var f = items[i];
			// TICKET 9000003862
			if(f.fieldId == 'TD_CodigoDepDest'){
				var objDependencia = $("#textDependenciaDestino option[value='" + f.value + "']");
				var textDependencia = objDependencia.text();
				f.text = 'Dependencia Destino: ' + textDependencia;
				//INICIO TICKET 9000003866
				if($("#textDependenciaDestinoBPAC").length > 0){
					objDependencia = $("#textDependenciaDestinoBPAC option[value='" + f.value + "']");
					textDependencia = objDependencia.text();
					f.text = 'Dependencia Destino: ' + textDependencia;
				}
				//FIN TICKET 9000003866
			}
			// FIN TICKET
			if(f.fieldId == 'TD_sAsunto'){
				f.text = 'Asunto: ' + f.value;
			}
			if(f.fieldId == 'TD_sAccion'){
				f.text = 'Acción: ' + f.value;
			}
			if(f.fieldId == 'TD_sRemitente'){
				f.text = 'Remitente: ' + f.value;
			}
			if(f.fieldId == 'TD_sCorrelativo'){
				f.text = 'Correlativo: ' + f.value;
			}
			if(f.fieldId == 'sNroDocInterno'){
				f.text = 'N. de documento: ' + f.value;
			}
			if(f.fieldId == 'TD_tFechaReg' && f.operator == '>='){
				var date_ = new Date(null);
				date_.setTime(f.value*1000);
				f.text = 'F. Desde: ' + date_.getDate()  + "/" + (date_.getMonth()+1) + "/" + date_.getFullYear();
			}
			if(f.fieldId == 'TD_tFechaReg' && f.operator == '<='){
				var date_ = new Date(null);
				date_.setTime(f.value*1000);
				f.text = 'F. Hasta: ' + date_.getDate()  + "/" + (date_.getMonth()+1) + "/" + date_.getFullYear();
			}
			_filtros.push(f);
		}
		var plantillaScript = ref.plantillaFiltros.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'filtros' : _filtros};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondencia(SISCORR_APP);
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