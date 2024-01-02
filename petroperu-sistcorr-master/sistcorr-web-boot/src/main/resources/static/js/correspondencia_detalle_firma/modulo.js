var MODULO_CORRESPONDENCIA_DETALLE = (function(){
	var instance;
	function ModuloCorrespondenciaDetalle(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.compRetroceder = $("#btnRetroceder");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.plantillaDestinatariosInternos = $("#template-destinatariosInternos");
		this.plantillaDestinatariosExternos = $("#template-destinatariosExterno");
		this.plantillaListaCopias = $("#template-copias");
		this.plantillaListaArchivosAdjuntos = $("#template-archivosAdjuntos");
		this.classDescargarArchivo = "btnDescargarArchivo";
		this.btnCompartir = $("#btnCompartir");
		this.btnHistorial = $("#btnHistorialCompartir");
		this.optCompartir = $("#modoCompartir");
		this.tdCompartir = "td-check-compartir";
		this.clsCompartirAll = $(".check-compartir-all");
		this.changeFlujoNombre =  $("#flujoNombre");
		this.codigoDocPagar = "81";//ticket 9000004765
		
		this.tabs = {
				tabDatos: {
					compHeader: $("Datos-tab"),
					acoordion: {
						remitente: {
							btnHead: $("#btnDatosRemitente"),
							compCorrelativo: $("#rmtCorrelativo"),
							compCorrelativoExterno: $("#rmtCorrelativoExterno"),
							compDependencia: $("#rmtDependencia"),
							compTipoCorrespondencia: $("#rmtTipoCorrespondencia"),
							compNombreRemitente: $("#rmtNombreRemitente"),
							compAsunto: $("#rmtAsunto"),
							compTipoCorrespondenciaOtros: $("#rmtTipoCorrespondenciaOtros"),
							compLugar: $("#rmtLugar"),
							compDependenciaOriginadora: $("#rmt_dependencia_originadora"),
							compOriginador: $("#rmt_originador"),
							compJerarquia:  $("#rmtJerarquia"),
							compRutaAprobacion:  $("#rmtRutaAprobacion"),
							compEsDocumentoRespuesta: $("#rmtEsDocumentoRespuesta"),
							compCorrelativoEntrada: $("#rmtCorrelativoEntrada"),
							compTipoAccion: $("#rmtTipoAccion"),
							compRespuesta: $("#rmtRespuesta")
						},
						correspondencia: {
							btnHead: $("#btnDatosCorrespondencia"),
							compFecha: $("#corrFecha"),
							compFirmaDigital:  $("#corrFirma"),
							compTipoEmision:  $("#corrTipoEmision"),
							compDespachoFisico:  $("#corrDespachiFisico"),
							compConfidencial:  $("#corrConfidencial"),
							compUrgente:  $("#corrUrgente"),
							compObservacion: $("#corrObservacion"),
							compEstado: $("#corrEstado"),
							compFecAct: $("#corrFecAct")
						}
					},
				},
				tabDestintarios: {
					compHeader: $("#Destinatario-tab"),
					compListaDestinatarios: $("#listaDestinatarios"),
					compListaCopias: $("#listaCopias")
				},
				tabArchivos: {
					compHeader: $("#Archivos-tab"),
					compListaArchivos: $("#listaArchivos"),
					btnEmitirFirma: $("#btnEmitirFirma")
				},
				tabFlujo: {
					compHeader: $("#Flujo-tab"),
					compListaFlujo: $("#listaFlujo")
				},
				tabRecepcion: {
					compNroEnvio: $("#nroEnvio"),
					compRecepciones: $("#datosRecepcion")
				}
		};
		this.URL_OBTENER_CORRESPONDENCIA = '../../app/emision/buscar/correspondencia';
		this.URL_DESCARGAR_ARCHIVO_ADJUNTO = '../../app/emision/descargar-archivo';
		this.URL_COMPARTIR_ARCHIVO = '../../app/compartir'
		this.URL_HISTORIAL_COMPARTIDO = '../../app/historial-compartido';
		this.URL_DESCARGAR_CORRESPONDENCIA = '../../app/descargar-archivos-correspondencia';
		this.URL_OBTENER_RECEPCION = '../../app/emision/obtener-recepcion';
		this.plantillaRecepcion = $("#template-recepcion");
		this.URL_ACTUALIZAR_CORREO_DEST_EXTERNO = '../../app/correspondencias/actualizar/correoDestinatarioExterno';
		// TICKET 9000004044
		this.classHistorialAsignacion = 'abrirHistorialAsignacion';
		this.modalHistorialAsignaciones = $('#modalHistorialAsignaciones');
		this.correlativoEntrada = $("#textCorrelativo");
		this.URL_HISTORIAL_ASIGNACIONES = '../../app/obtenerHistoricoAsignaciones';
		this.plantillaAsignaciones = $("#template-asignaciones");
		// FIN TICKET
	}
	
	ModuloCorrespondenciaDetalle.prototype.obtenerCorrespondencia = function(){
		var ref = this;
		var idCorrespondencia = ref.idCorrespondencia.val();
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
	},
	
	ModuloCorrespondenciaDetalle.prototype.actualizarCorreoDestExterno = function(identificador, correoDestin){
		var ref = this;
		var idCorrespondencia = ref.idCorrespondencia.val();
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_ACTUALIZAR_CORREO_DEST_EXTERNO+"?correo="+correoDestin+"&id=" + idCorrespondencia+"&codDE=" + identificador,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        
		        xhr.setRequestHeader(header, token);
		    }
		});
	},
	
	ModuloCorrespondenciaDetalle.prototype.htmlListaDestinatariosInternos = function(destinatarios){
		var ref = this;
		var plantillaScript = ref.plantillaDestinatariosInternos.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'destinatarios' : destinatarios};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaDetalle.prototype.htmlListaDestinatariosExternos = function(destinatarios, idEstadoCorrespondencia){
		var ref = this;
		Handlebars.registerHelper('esMostrarEditCorreoElect', function(correspondenciaExterno){
			var estilo = "display: none;";
			
			if(esUserResponsable == "S" && correspondenciaExterno.esCorreoElectronicoDestExterno && idEstadoCorrespondencia != 7)
				estilo = "display: ;cursor: pointer;";
			
            return estilo;
        });
		Handlebars.registerHelper('esMostrarReferenciaDest', function(correspondenciaExterno){
			var estilo = "display: none;";
			
			if(idEstadoCorrespondencia == 7)
				estilo = "display: ;cursor: pointer;";
			
            return estilo;
        });
		Handlebars.registerHelper('eq', function () {
		    const args = Array.prototype.slice.call(arguments, 0, -1);
		    return args.every(function (expression) {
		        return args[0] === expression;
		    });
		});
		var plantillaScript = ref.plantillaDestinatariosExternos.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'destinatarios' : destinatarios};
		var contenidoHTML = plantilla(contexto);
		$(document).on('mousedown', '.iconEditarCorreoDestinatario', function(event) {
		    event.preventDefault();
		}).on('click', '.iconEditarCorreoDestinatario', function(e) {
			var identAttr = ($(this).attr("id")).split("_")[1];
			$("#divCorreoDestinatarioEditar_" + identAttr).show();
			$("#divCorreoDestinatarioReadOnly_" + identAttr).hide();
			$("#correoDestinatarioEditar_" + identAttr).focus();
		});
		
		$(document).on('blur','.correoDestinatarioEditar',function(e){
			var thisKY = this;
			var identAttr = ($(thisKY).attr("id")).split("_")[1];
			$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
			$("#divCorreoDestinatarioEditar_" + identAttr).hide();
		});
		
		$(document).on('keyup','.correoDestinatarioEditar',function(e){
			var code = (e.keyCode ? e.keyCode : e.which);
			var thisKY = this;
			var identAttr = ($(thisKY).attr("id")).split("_")[1];
		    if (code === 13) {//ENTER
		    	var identificadorDX = identAttr;
		    	var correoDestinDX = $(thisKY).val();
		    	
		    	ref.modSistcorr.procesar(true);
		        ref.actualizarCorreoDestExterno(identificadorDX, correoDestinDX)
				.then(function(respuesta){
					if(respuesta.estado == true){
						$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
						$("#divCorreoDestinatarioEditar_" + identAttr).hide();
						$(thisKY).val(respuesta.datos[0].correoDestinatario);
						$("#spanCorreoDestinatarioTexto_" + identAttr).text(respuesta.datos[0].correoDestinatario);
						
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					setTimeout(function() {
						ref.modSistcorr.procesar(false);
					}, 250);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		    }else if(code === 27){//ESC
		    	$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
				$("#divCorreoDestinatarioEditar_" + identAttr).hide();
		    }
		});
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaDetalle.prototype.htmlListaCopias = function(copias){
		var ref = this;
		var plantillaScript = ref.plantillaListaCopias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'copias' : copias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaDetalle.prototype.htmlListaArchivosAdjuntos = function(adjuntos){
		var ref = this;
		
		Handlebars.registerHelper('icono_documento', function(adjunto){
			if(adjunto.contentType.indexOf("pdf") > -1){
				return "fa-file-pdf icon_pdf";
			} else if(adjunto.contentType.indexOf("word") > -1){
				return "fa-file-word icon_word";
			} else if(adjunto.contentType.indexOf("png") > -1){
				return "fa-file-image icon_image";
			}  else if(adjunto.contentType.indexOf("jpg") > -1){
				return "fa-file-image icon_image";
			} else 	if(adjunto.contentType.indexOf("spreadshee") > -1){
				return "fa-file-excel icon_excel";
			} else {
				return "fa-file icon_otro";
			}
		});
		
		Handlebars.registerHelper('ver_principal', function(adjunto){
			console.log(adjunto);
			if(adjunto.tipo == "PRINCIPAL"){
				return "display:block;";
			}else{
				return "display:none;";
			}
		});
		
		Handlebars.registerHelper('ver_principal_check', function(adjunto){
			console.log(adjunto);
			if(adjunto.tipo == "PRINCIPAL"){
				return "display:;";
			}else{
				return "display:none;";
			}
		});
		
		Handlebars.registerHelper('tamanio', function(adjunto){
			console.log(adjunto);
			let tam = Math.round(adjunto.tamanio*100)/100;
			return tam;
		});
		
		var plantillaScript = ref.plantillaListaArchivosAdjuntos.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'adjuntos' : adjuntos};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaDetalle.prototype.htmlRecepcion = function(recepcion){
		var ref = this;
		
		Handlebars.registerHelper('estilo_opciones_aceptada', function(recp){
			if(recp.estado == CONSTANTES_SISTCORR.DESTINO_RESPUESTA_ACEPTADO){
				return "display:;";
			}else{
				return "display:none;";
			}
		});
		
		Handlebars.registerHelper('estilo_opciones_rechazada', function(recp){
			if(recp.estado == CONSTANTES_SISTCORR.DESTINO_RESPUESTA_RECHAZADO){
				return "display:;";
			}else{
				return "display:none;";
			}
		});
		
		var plantillaScript = ref.plantillaRecepcion.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'recepcion' : recepcion};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaDetalle.prototype.descargarArchivo = function(idArchivAdjunto){
		var ref = this;
		window.open(ref.URL_DESCARGAR_ARCHIVO_ADJUNTO + "/" + idArchivAdjunto, '_blank');
	};
	
	ModuloCorrespondenciaDetalle.prototype.obtenerDestinatarioRespuesta = function(idCorrespondencia, nroEnvio){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_RECEPCION + '/' + idCorrespondencia + '/' + nroEnvio,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	// INICIO TICKET 9000003996
	ModuloCorrespondenciaDetalle.prototype.obtenerFlujo = function(){
		var ref = this;
		var nombre = $("#flujoNombre").val();
		var correspondencia = this.idCorrespondencia.val();
		var enlace ="../../app/actualizaflujo";
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();

		
		return $.ajax({
			type : "POST",
			url : enlace,
			data : JSON.stringify({
				'flujoNombre' : nombre,
				'idCorrespondencia' : correspondencia
			}),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    },
			
			success : function(result) {

				if (result) {
					$("#listaFlujo").empty();
					
					var html = '';
					
					$.each(result, function(index, firmante) {
						
						console.log("option: "+JSON.stringify(firmante));
						
						html += '<div  class="row">';
						
							html += '<div class="col-sm-12 col_firmante">';
							html += '<div class="card promoting-card card_firmante card_'+firmante.color+'">';
			        		
										html += '<div class="card-body d-flex flex-row">';
											html += '<div style="width: 100%">';
												html += '<strong class="card-title font-weight-bold mb-2">'+firmante.nombreFirmante+'</strong>';
											html += '</div>';
										html += '</div>';
					        
										html += '<div class="card-body card_body_firmante ">';
										html += '<p class="parrafo_flujo">Remitente: '+ firmante.dependenciaFirmante +'</p>';
										html += '<p class="parrafo_flujo" >Estado: '+ firmante.estadoDescripcion +'</p>';
										
										if(firmante.motivoRechazo != null){
											html += '<p class="parrafo_flujo">Motivo: '+firmante.motivoRechazo.descripcion+'</p>';
										}
										
										if(firmante.descripcionMotivoRechazo.length > 0 && firmante.usuarioModifica == firmante.codFirmante){
											html += '<div class="parrafo_flujo descripcion_rechazo">Descripci&oacute;n o sustento: ' + firmante.descripcionMotivoRechazo +'</div>';
										}
										
										if(firmante.descripcionMotivoRechazo.length > 0 && firmante.usuarioModifica != firmante.codFirmante){
											html += '<div class="parrafo_flujo descripcion_rechazo">Descripci&oacute;n o sustento: ' + firmante.descripcionMotivoRechazo + ' (Rechazado por: ' + firmante.usuarioModifica + ')</div>';
											
										}
													        
						        	
										html += '<p class="parrafo_flujo" >Fecha: '+ firmante.fecha +'</p>';
										html += '<p class="parrafo_flujo" >Solicitante: '+ firmante.solicitante +'</p>';
										html += '<br />';
										html += '</div>';
						        
								html += '</div>';
								html += '</div>';
		        	
								html += '</div>';
			        });
					
					$('#listaFlujo').html(html);
					
				} else {
					alert("No se encontro ");
				}
			},
			error: function() {
		        console.log("No se ha podido obtener la información");
		    }
		    
		    
		});
		
	};
	
	// FIN TICKET 9000003996
	
	
	
	ModuloCorrespondenciaDetalle.prototype.obtenerHistorialAsignaciones = function(correlativo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_HISTORIAL_ASIGNACIONES + '/' + correlativo,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaDetalle.prototype.htmlAsignaciones = function(asignaciones){
		var ref = this;
		
		var plantillaScript = ref.plantillaAsignaciones.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'asignacion' : asignaciones};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
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