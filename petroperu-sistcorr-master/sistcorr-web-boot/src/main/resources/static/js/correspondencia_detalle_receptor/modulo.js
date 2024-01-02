var MODULO_CORRESPONDENCIA_DETALLE = (function(){
	var instance;
	function ModuloCorrespondenciaDetalle(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.URL_BASE_CORRESPONDENCIA = "../../correspondencias";
		this.URL_BASE_CORRESPONDENCIAS="app/correspondencias";
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		this.URL_BUSCAR_LUGARES = '../../../../app/emision/buscar/lugar';
		this.URL_BUSCAR_DEPENDENCIAS_UM = '../../../../app/emision/buscar/dependenciaUM';
		this.URL_CERRAR_CORRESPONDENCIA = 'app/correspondencias/asignaciones/cerrar';
		this.URL_COMPLETAR_CORRESPONDENCIA = 'app/correspondencias/asignaciones/completar';
		this.URL_RECHAZAR_ASIGNACION_CORRESPONDENCIA = 'app/correspondencias/asignaciones/rechazarasignacion';
		this.URL_ELIMINAR_DOCUMENTO = 'app/eliminar-documento';
		this.URL_CARGAR_DOCUMENTO = 'app/emision/registrar/adjunto';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.compAsuntoHeader = $("#textAsunto");
		this.panelFiltros = $(".masFiltros");
		this.panelFiltros2 = $(".masFiltros-2");
		this.subirDocumento = $(".subir_documento");
		this.archivoAdjunto = $("#arc_file");
		this.textDocumento = $("#text_documento");
		this.documentoEliminar = $(".documentoEliminar");
		this.formAdjuntar = $("#subirAdjuntos");
		this.listaDocumentos = $("#listaDocumentos");
		this.plantillaDocumento = $("#template-lista-documento");

		this.compBtnRechazarCorresReceptor = $("#btnRechazarCorresReceptor");
		this.compAbrirAceptarCorrespondencia = "btnAceptarCorrespondencia";
		this.compModalAceptarCorrespondencia = $("#modalAceptarCorrespondencia");
		this.compBtnAceptarCorrespondencia = $("#btnAceptarCorrespondencia");
		this.compAbrirRechazarCorrespondencia = $("#btnRechazarCorrespondencia");
		this.compModalRechazarCorrespondencia = $("#modalRechazarCorrespondencia");
		this.compBtnRechazarCorrespondencia = $("#btnRechazarCorrespondencia");
		this.compComentarioRechazarCorrespondencia = $("#textRechazarCorrespondencia");
		this.compModalAsignarCorrespondencia = $("#modalAsignacionDestinatario");
		this.compLugarTrabajo= $("#destInterno_lugarTrabajo");
		this.compDependencia= $("#destInterno_dependencia");
		this.compBtnAsignarDestinatarioDetalle= $("#btnAsignarDestinatarioDetalle"),
		this.compBtnRechazarCorrespondenciaDetalle= $("#btnRechazarCorrespondenciaDetalle"),
		this.btnBuscarFlujo = $("#btnBuscarFlujo");
		this.compAsignDestin = {
				myModal: $("#modalAsignacionDestinatario")
		};
		this.compRechazarCorrespReceptor = {
				myModal: $("#modalRechazarAsignacionCorresReceptor")
		};
		this.compBtnGuardarAsignacion = $("#btnGuardarAsignacion");
		this.compModalRegistrarObservacion = $("#modalRegistrarObservacion");
		this.compObservacionRegistrarCorrespondencia = $("#textRegistrarObservacion");
		this.compBtnRegistrarObservacion = $("#btnRegistrarObservacion");
		this.classAgregarObservacion = "agregarObservacion";
		this.URL_REGISTRAR_OBSERVACION = 'app/correspondencias/registrar/observacion';
		
		this.classMasTexto = 'masTexto';
		this.classMenosTexto = 'menosTexto';
		
		this.VISTA = {
			tabDatos: {
				id: $("#Datos-tab"),
				accordion:{
					datosRemitente: {
						id: $("#btnDatosRemitente"),
						acordion_head: $("#headingDatosRemitente"),
						acordion_body: $("#collapseDatosRemitente"),
						form: {
						}
					},
					datosCorrespondencia: {
						id: $("#btnDatosCorrespondencia"),
						acordion_body: $("#collapseDatosCorrespondencia"),
						form: {
						}
					}
				}
			},
			tabHistorico: {
				id: $("#Historico-tab"),
			},
			tabObservaciones: {
				id: $("#Observaciones-tab"),
			},
			tabDocumentos:{
				id: $("#Documentos-tab"),
			},
			btnRetroceder: $("#btnRetroceder"),
			btnAsignarCorrespondencia: $("#btnAsignarCorrespondencia"),
			compTipoBandeja : $("#tipoBandeja"),
			compCorrelativo: $("#correlativo"),
			compWobNum: $("#wobNum"),
			compNuevaCorrespondencia: $("#nuevaCorrespondencia"),
			compPorAceptar: $("#porAceptar"),
			btnAbrirCompletarCorrespondencia: $("#btnAbrirCompletarCorrespondencia"),
			btnAbrirRechazarAsigCorresp: $("#btnAbrirRechazarAsigCorresp"),//TICKET 9000004273
			compModalRechazarAsignacionCorres: $("#modalRechazarAsignacionCorres"),//TICKET 9000004273
			compTxtMotivoRechazarAsignacionCorres: $("#textMotivoRechazarAsignacionCorres"),//TICKET 9000004273
			compbtnRechazarAsignacionCorres: $("#btnRechazarAsignacionCorres"),//TICKET 9000004273
			btnAbrirCerrarCorrespondencia: $("#btnAbrirCerrarCorrespondencia"),
			compDescargarDocumento: $(".documentoDescarga"),
			compTamanioArchivo: $("#textTamanioArchivo"),
			compModalDescargarArchivo: $("#modalDescargarArchivo"),
			compBtnDescargarArchivo: $("#btnDescargaArchivoSi"),
			compModalCerrarCorrespondencia: $("#modalCerrarCorrespondencia"),
			compBtnCerrarCorrespondencia: $("#btnCerrarCorrespondencia"),
			compComentarioCerrarCorrespondencia: $("#textCerrarCorrespondencia"),
			// TICKET 900004044
			compChkRequiereDocumentoCerrar: $("#requiereRespuestaCerrar"),
			compTxtNumeroDocumentoCerrar: $("#textNumeroDocumentoCerrar"),
			// FIN TICKET
			compModalCompletarCorrespondencia: $("#modalCompletarCorrespondencia"),
			compComentarioCompletarCorrespondencia: $("#textCompletarCorrespondencia"),
			compbtnCompletarCorrespondencia: $("#btnCompletarCorrespondencia"),
			// TICKET 900004044
			compChkRequiereDocumentoCompletar: $("#requiereRespuestaCompletar"),
			compTxtNumeroDocumentoCompletar: $("#textNumeroDocumentoCompletar"),
			URL_DETALLE_DOCUMENTO_ENLACE: 'app/ver-detalle',
			// FIN TICKET
			btnEnviarCopia: $("#btnEnviarCopia"),
			compBtnCerrarDetalle: $("#btnCerrarDetalle"),
			
		};
		// TICKET 9000004044
		this.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA = "app/validarExisteDocumentoRespuestaCorrespondencia";
		this.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION = "app/validarExisteDocumentoRespuestaAsignacion";
		this.URL_NUEVA_CORRESPONDENCIA = "app/registro";
		this.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO = "app/verificarEstadoNumeroDocumento";
		this.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO = "app/enlazarCorrespondenciaNumeroDocumento";
		// FIN TICKET
	}
	
	ModuloCorrespondenciaDetalle.prototype.abrirDocumento = function(documento){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		//window.location.href = '../../../../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento;
		window.open('../../../../' + ref.URL_DESCARGAR_DOCUMENTO + "/" + documento, '_blank');
	};
	
	ModuloCorrespondenciaDetalle.prototype.cerrarCorrespondencia = function(correlativo, observacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion };
		return $.ajax({
			type		:	'PUT',
			url			:	'../../../../' + ref.URL_CERRAR_CORRESPONDENCIA + '/' + correlativo ,
			data		:	JSON.stringify(parametro),
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.rechazarCorrespondenciaMPV = function(correlativo, observacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion };
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazarMPV/' + correlativo,
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
				url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/rechazarMPV/' + correlativo,
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
	
	ModuloCorrespondenciaDetalle.prototype.completarCorrespondencia = function(idAsignacion, respuesta){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"respuesta": respuesta};
		return $.ajax({
			type		:	'PUT',
			url			:	'../../../../' + ref.URL_COMPLETAR_CORRESPONDENCIA + '/' + idAsignacion,
			data		:	JSON.stringify(parametro),
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	//INICIO TICKET 9000004273
	ModuloCorrespondenciaDetalle.prototype.rechazarAsignacionCorrespondencia = function(idAsignacion, motivo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": motivo};
		return $.ajax({
			type		:	'PUT',
			url			:	'../../../../' + ref.URL_RECHAZAR_ASIGNACION_CORRESPONDENCIA + '/' + idAsignacion,
			data		:	JSON.stringify(parametro),
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	//FIN TICKET 9000004273
	
	ModuloCorrespondenciaDetalle.prototype.eliminarDocumento = function(idDocumento, proceso, codigoTraza){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	'../../../../' + ref.URL_ELIMINAR_DOCUMENTO + '/' + idDocumento + "/" + codigoTraza + "/" + proceso,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	/*ModuloCorrespondenciaDetalle.prototype.cargarDocumento = function(idDocumento, proceso){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	'../../../../' + ref.URL_CARGAR_DOCUMENTO + '/' + idDocumento + "/" + proceso,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};*/
	
	ModuloCorrespondenciaDetalle.prototype.cargarAdjunto = function(correspondencia, adjuntos){
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		var principales = [];
		var fd = new FormData();
		for(i=0;i<adjuntos.length;i++){
			var adj = adjuntos[i];
			fd.append('archivos', adj);
		}
		if(correspondencia.confidencial == "SI"){
			correspondencia.confidencial = true;
		}else{
			correspondencia.confidencial = false;
		}
		
		fd.append('correspondencia', new Blob([JSON.stringify(correspondencia)], {
	        type: "application/json"
	    }));
		
		return $.ajax({
			type	:	'POST',
			url		: 	"../../../../" + ref.URL_CARGAR_DOCUMENTO,
			cache	:	false,
			data	:	fd,
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader(header, token);
		    }
		});
		
	};
	
	ModuloCorrespondenciaDetalle.prototype.aceptarCorrespondencia = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
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
	};
	
	ModuloCorrespondenciaDetalle.prototype.procesarAceptarCorrespondencia = function(nroDocumento, correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
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
	};
	
	ModuloCorrespondenciaDetalle.prototype.procesarRechazarCorrespondenciaMPV = function(nroDocumento, correlativo, comentario){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": comentario };
		return $.ajax({
			type		:	'PUT',
			url			:	'../' + ref.URL_BASE_CORRESPONDENCIA + '/asignaciones/procesarRechazarMPV/' + nroDocumento + '/' + correlativo,
			data		:	JSON.stringify(parametro),
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.rechazarCorrespondencia = function(correlativo, observacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion };
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
	};
	
	// TICKET 9000004044
	
	ModuloCorrespondenciaDetalle.prototype.validarExisteDocumentoRespuestaCorrespondencia = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'GET',
			url			:	'../../../../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA + "/" + correlativo,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.validarExisteDocumentoRespuestaAsignacion = function(idAsignacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'GET',
			url			:	'../../../../' + ref.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION + "/" + idAsignacion,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.irANuevaCorrespondencia = function(){
		var ref = this;
		window.location.replace('../../../../' + ref.URL_NUEVA_CORRESPONDENCIA);
		
	};
	
	ModuloCorrespondenciaDetalle.prototype.verificarEstadoNumeroDocumento = function(numeroDocumento){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'GET',
			url			:	'../../../../' + ref.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO + "/" + numeroDocumento,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.enlazarCorrespondenciaNumeroDocumento = function(correspondencia){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			:	'../../../../' + ref.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO,
			cache		:	false,
			data		:	JSON.stringify(correspondencia),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	}
	
	// FIN TICKET
	
	ModuloCorrespondenciaDetalle.prototype.registrarObservacion = function(correlativo, observacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"observacion": observacion};
		return $.ajax({
			type		:	'PUT',
			url			:	'../../../../' + ref.URL_REGISTRAR_OBSERVACION+ '/' + correlativo,
			data		:	JSON.stringify(parametro),
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.asignarDestinatario = function(correlativo, cgc,dependencia,accion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var parametro = {"cgc": cgc, "dependencia": dependencia, "accion": accion };
		var correlativoDef = $("#correlativoDef").val();
		if(correlativoDef==""){
			return $.ajax({
				type		:	'PUT',
				url			:	'../../../../' + ref.URL_BASE_CORRESPONDENCIAS + '/asignaciones/asignarDependenciaMPV/' + correlativo,
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
				url			:	'../../../../' + ref.URL_BASE_CORRESPONDENCIAS + '/asignaciones/asignarDependenciaMPV/' + correlativo,
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
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaDetalle(SISCORR_APP);
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