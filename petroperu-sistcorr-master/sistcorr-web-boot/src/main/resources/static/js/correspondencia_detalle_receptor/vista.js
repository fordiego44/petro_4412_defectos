var modulo_correspondencia_detalle = MODULO_CORRESPONDENCIA_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_DETALLE_VISTA = {
		correspondencia: null,
		modSistcorr: modulo_sistcorr,
		modulo: modulo_correspondencia_detalle,
		tamanioMaxArchivo: 10,
		sizeScreen: 500,
		bandeja: null,
		rol_jefe: false,
		rol_gestor: false,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		errores: [],
		archivosSeleccionados: [],
		idDocumentoEliminar: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.mostrarErrores();
			setTimeout(function(){
				console.log("Inicializando")
				utilitario_textLarge.inicializar();
				console.log("Fin inicializaando")
			}, 5000);
		},
		
		iniciarEventos: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			if(screen.width < ref.sizeScreen){
				$("#Historico-tab").html('Hist. de Asig.');
				ref.modulo.compAsuntoHeader.css("max-width", "250px")
			}
			
			$("#Datos-tab").addClass('petro-tabs-activo');
			$("#contenidoAccordionDatos").show();
	        $("#contenidoHistorialAsignaciones").hide();
	        $("#contenidoDocumentos").hide();
			
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			    if (!e.target.dataset.tab) {
			        return;
			    }
			    
			    var $tab = $(e.currentTarget);
			    $('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
			    $tab.addClass('petro-tabs-activo');
			    if (e.target.dataset.tab == "datos") {
			        $("#contenidoAccordionDatosReceptor").show();
			        ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_head.show();
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_body.show();
			        $("#contenidoDocumentos").hide();
			    
			    } 
			    
			    if (e.target.dataset.tab == "documentos") {
			        $("#contenidoAccordionDatosReceptor").hide();
			        $("#contenidoDocumentos").show();
			    }
			});
			
			$(".abrirDocumentoEnlace").click(function(){
				var t = $(this);
				console.log("Abriendo enlace");
				console.log(t.html());
				var numDoc = $("#idDocumentoEnlace").val();
				window.location.replace("../../../../" + ref.modulo.VISTA.URL_DETALLE_DOCUMENTO_ENLACE + "/" + numDoc);
			});
			
			$(".abrirDocumentoEnlaceAsignacion").click(function(){
				var t = $(this);
				console.log("Abriendo enlace");
				console.log(t.data('id'));
				var numDoc = t.data('id');
				window.location.replace("../../../../" + ref.modulo.VISTA.URL_DETALLE_DOCUMENTO_ENLACE + "/" + numDoc);
			});
			
			ref.modulo.VISTA.btnRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				sessionStorage.setItem("origPag", "verDetalle");
				console.log(sessionStorage.getItem("origPag"));
				window.location.replace("../../../../app/" + ref.bandeja);
				//ref.modSistcorr.retroceder();
			});
			
			ref.modulo.compBtnGuardarAsignacion.click(function(event){
				ref.asignarDestinatario();
			});
			
			
			ref.modulo.VISTA.compDescargarDocumento.click(function(event){
				var $documento = $(event.currentTarget);
				var idDocumento = $documento.data("id");
				var sizeDocumento = $documento.data("size");
				ref.verificarDocumento(idDocumento, sizeDocumento);
			});
			
			ref.modulo.VISTA.btnAbrirCompletarCorrespondencia.click(function(){
				ref.abrirModalCompletarCorrespondencia();
			});
			
			//INICIO TICKET 9000004273
			ref.modulo.VISTA.btnAbrirRechazarAsigCorresp.click(function(){
				ref.abrirModalRechazarAsigCorrespondencia();
			});
			//FIN TICKET 9000004273
			
			//INICIO TICKET 9000004273
			ref.modulo.VISTA.compbtnRechazarAsignacionCorres.click(function(event){
				ref.tieneDocumentoRespuestaEnCursoAsignacion("rechazarasignacion");
			});
			//FIN TICKET 9000004273
			
			ref.modulo.VISTA.btnAbrirCerrarCorrespondencia.click(function(){
				ref.abrirModalCerrarCorrespondencia();
			});
			
			ref.modulo.VISTA.compBtnCerrarCorrespondencia.click(function(){
				// TICKET 9000004044
				//ref.cerrarCorrespondencia();
				ref.tieneDocumentoRespuestaEnCursoCorrespondencia();
				// FIN TICKET
			});
			
			ref.modulo.compBtnRechazarCorresReceptor.click(function(event){
				ref.rechazarCorrespondencia();
			});
			
			ref.modulo.VISTA.compbtnCompletarCorrespondencia.click(function(){
				// TICKET 9000004044
				//ref.completarCorrespondencia();
				ref.tieneDocumentoRespuestaEnCursoAsignacion("completarcorrespondencia");
				// FIN TICKET
			}); 
			
			
			ref.modulo.compBtnAsignarDestinatarioDetalle.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				var correlativo = ref.correspondencia;
				ref.correspondenciaPorAsignar = {
						'correlativo' : 	correlativo.correlativo
				};
				ref.abrirModalAsignarDestinatario();
			});
			
			ref.modulo.compBtnRechazarCorrespondenciaDetalle.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				var correlativo = ref.correspondencia;
				ref.correspondenciaPorRechazar = {
						'correlativo' : 	correlativo,
						'dependenciaDestino' : "",
				};
				ref.abrirModalRechazarCorrespondenciaReceptor();
			});
			
			ref.modulo.VISTA.compBtnCerrarDetalle.click(function(event){
				ref.modSistcorr.procesar(true);
				window.location.replace("../../../../app/" + ref.bandeja);
			});
			
			if(ref.correspondencia){
				// TICKET 9000003862
				//if(ref.correspondencia.nuevaCorrespondencia == true){
				if(ref.correspondencia.nuevaCorrespondencia == true || ref.correspondencia.porAceptar == true){
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_body.removeClass("show");
					ref.modulo.VISTA.tabDatos.accordion.datosCorrespondencia.acordion_body.addClass("show");
					
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_head.hide();
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_body.hide();
				}else{
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_head.show();
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_body.show();
					
					ref.modulo.VISTA.tabDatos.accordion.datosRemitente.acordion_body.addClass("show");
					ref.modulo.VISTA.tabDatos.accordion.datosCorrespondencia.acordion_body.addClass("show");
				}
			}
			
			ref.textoMas.inicializar();
			
			ref.inicializarPaneles();
			
			ref.modulo.panelFiltros.click(function(){
				ref.cambiarPanel();
			});
			
			ref.modulo.panelFiltros2.click(function(){
				ref.cambiarPanel2();
			});
			
			ref.modulo.textDocumento.click(function(){
				ref.abrirTypeFile();
			});
			
			ref.modulo.archivoAdjunto.change(function(e){
				ref.modulo.textDocumento.val(ref.modulo.archivoAdjunto.val());
				ref.archivosSeleccionados = e.target.files;
			});
			
			ref.modulo.subirDocumento.click(function(){
				ref.modulo.formAdjuntar.submit();
			});
			
			ref.modulo.documentoEliminar.click(function(){
				var eli = $(this);
				var id = eli.attr("data-id");
				ref.idDocumentoEliminar = id;
				$("#modalEliminarArchivo").modal("show");
			});
			
			ref.modulo.formAdjuntar.submit(function(e){
				e.preventDefault();
				if(ref.archivosSeleccionados.length > 0){
					var maxSizeMB = ref.modulo.archivoAdjunto.data('max-size');
					var maxSize = maxSizeMB * 1048576;
					var tamanioArchivo = ref.archivosSeleccionados[0].size;
					if(tamanioArchivo > maxSize){
						ref.modSistcorr.notificar("ERROR", 'El tamaño máximo permitido es de ' + maxSizeMB + " MB", "Error");
						return false;
					} else{
						$("#modalCargarArchivo").modal('show');
					}
				}else{
					ref.modSistcorr.notificar("ERROR", "Debe seleccionar un archivo", "Error");
				}
			});
			
			$("#btnCargaArchivoSi").click(function(){
				ref.cargarArchivos();
			});
			
			$("." + ref.modulo.classMasTexto).click(function(event){
				var $columnaTarea = $(event.currentTarget);
				$columnaTarea.parent().attr("style", "display:none");
				$columnaTarea.parent().next().attr("style", "display:block");
			});
			
			$("." + ref.modulo.classMenosTexto).click(function(event){
				var $columnaTarea = $(event.currentTarget);
				$columnaTarea.parent().attr("style", "display:none");
				$columnaTarea.parent().prev().attr("style", "display:block");
			});
			
			$("#btnEliminarArchivoSi").click(function(){
				ref.eliminarDocumento();
			});
			
			$("."+ref.modulo.compAbrirAceptarCorrespondencia).click(function(e){
				e.preventDefault();
				var $comp = $(this);
				console.log(ref.correspondencia);
				var correlativo = ref.correspondencia;
				ref.correspondenciaPorAceptar = {
						'correlativo' : 	correlativo
				};
				ref.abrirModalAceptarCorrespondencia();
			});
			
			ref.modulo.compBtnAceptarCorrespondencia.click(function(event){
				ref.aceptarCorrespondencia();
			});
			
			ref.modulo.compBtnRechazarCorrespondencia.click(function(event){
				ref.rechazarCorrespondencia();
			});
			
			$("." + ref.modulo.classAgregarObservacion).click(function(event){
				ref.abrirModalAgregarObservacion();
			});
			
			ref.modulo.compBtnRegistrarObservacion.click(function(event){
				ref.agregarObservacion();
			});
			
			// TICKET 900004044
			ref.modulo.VISTA.compChkRequiereDocumentoCompletar.change(function(){
				var t = this;
				var activo = $(t).prop('checked');
				if(activo){
					ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.removeAttr("readonly");
					ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.focus();
				}else{
					ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.val("");
					ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.change();
					ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.attr("readonly", "readonly");
				}
			});
			
			ref.modulo.VISTA.compChkRequiereDocumentoCerrar.change(function(){
				var t = this;
				console.log("Cambio check cerrar");
				var activo = $(t).prop('checked');
				if(activo){
					ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.removeAttr("readonly");
					ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.focus();
				}else{
					ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.val("");
					ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.change();
					ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.attr("readonly", "readonly");
				}
			});
			// FIN TICKET
		},	
		// TICKET 9000004044
		requiereRespuestaCompletar: function(){
			var ref = this;
			var checkDoc = ref.modulo.VISTA.compChkRequiereDocumentoCompletar.prop('checked');
			return checkDoc;
		},
		
		asignarDestinatario: function(){
			var ref = this;
			var cgc = ref.modulo.compLugarTrabajo.val();
			var dependencia = ref.modulo.compDependencia.val();
			var dependenciaDestino=ref.correspondencia.dependenciaDestino;
			var accion="";
			if (dependenciaDestino==""){
				accion="A";
			}else{
				accion="R";
			}
			
			ref.modSistcorr.procesar(true);
			
			ref.modulo.asignarDestinatario(ref.correspondencia.correlativo, cgc,dependencia,accion)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					//ref.obtenerBandeja();
					setTimeout(function() {
						ref.modulo.VISTA.btnRetroceder.click();
					}, 3000);
					
				}else{
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				ref.modulo.compModalAsignarCorrespondencia.modal('hide');
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		requiereRespuestaCerrar: function(){
			var ref = this;
			var checkDoc = ref.modulo.VISTA.compChkRequiereDocumentoCerrar.prop('checked');
			return checkDoc;
		},
		
		tieneDocumentoRespuestaEnCursoCorrespondencia: function(){
			var ref = this;
			ref.modulo.validarExisteDocumentoRespuestaCorrespondencia(ref.correspondencia.correlativo)
					.then(function(respuesta){
						ref.modSistcorr.procesar(false);
						ref.validarDocRptaCursoCorr(respuesta.estado);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
				
		tieneDocumentoRespuestaEnCursoAsignacion: function(accion){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.validarExisteDocumentoRespuestaAsignacion(ref.correspondencia.idAsignacion)
					.then(function(respuesta){
						//ref.modSistcorr.procesar(false);
						ref.validarDocRptaCursoAsig(respuesta.estado, accion);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		validarDocRptaCursoCorr: function(estado){
			var ref = this;
			if(estado == false || estado == "false"){
				if(ref.requiereRespuestaCerrar()){
					var nroDoc = ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.val();
					if(nroDoc == ""){
						sessionStorage.setItem("urlOrigen", "bandejaEntrada");
						sessionStorage.setItem("accionPrecedente", "cerrarCorrespondencia");
						sessionStorage.setItem("correlativo", ref.correspondencia.correlativo);
						sessionStorage.setItem("comentario", ref.modulo.VISTA.compComentarioCerrarCorrespondencia.val() || '');
						ref.modulo.irANuevaCorrespondencia();
					}else{
						ref.modulo.verificarEstadoNumeroDocumento(nroDoc)
						.then(function(respuesta){
							console.log("Respuesta:");
							console.log(respuesta);
							if(respuesta.estado == false || respuesta.estado == "false"){
								ref.modSistcorr.procesar(false);
								ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							}else{
								var comentario = ref.modulo.VISTA.compComentarioCerrarCorrespondencia.val() || '';
								var correspondencia = {'numeroDocumento': nroDoc, 'correlativo': ref.correspondencia.correlativo, 'idAsignacion': 0, 'tipoAccion': '', 'respuesta': comentario};
								ref.modulo.enlazarCorrespondenciaNumeroDocumento(correspondencia)
										.then(function(respuesta){
											console.log(respuesta);
											if(respuesta.estado == true || respuesta.estado == "true"){
												ref.cerrarCorrespondencia();
											}else{
												ref.modSistcorr.procesar(false);
												ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
											}
										}).catch(function(error){
											ref.modSistcorr.procesar(false);
											ref.modSistcorr.showMessageErrorRequest(error);
										})
							}
						}).catch(function(error){
							ref.modSistcorr.procesar(false);
							ref.modSistcorr.showMessageErrorRequest(error);
						});
					}
				}else{
					ref.cerrarCorrespondencia();
				}
			}else{
				ref.modSistcorr.notificar("ERROR", "La correspondencia que intenta Cerrar tiene un documento respuesta en curso", "Error");
			}
		},
		
		validarDocRptaCursoAsig: function(estado, accionbtn){
			var ref = this;
			if(estado == false || estado == "false"){
				if(accionbtn == "completarcorrespondencia" && ref.requiereRespuestaCompletar()){
					var nroDoc = ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.val();
					if(nroDoc == ""){
						var comentario = ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val() || '';
						var acciones = ref.modSistcorr.getAcciones();
						var accionCorrespondencia;
						for(var i in acciones){
							var accion = acciones[i];
							if(accion.codigoAccion == ref.correspondencia.codigoAccion){
								accionCorrespondencia = accion;
								break;
							}
						}
						if(!accionCorrespondencia){
							ref.modSistcorr.notificar("ERROR", "Correspondencia no tiene tipo de acción", "Warning");
							ref.modSistcorr.procesar(false);
							return;
						}
						if(accionCorrespondencia.requiereRespuesta == 'SI' && comentario.length == ''){
							ref.modulo.compComentarioCerrarCorrespondencia.focus();
							ref.modSistcorr.procesar(false);
							ref.modSistcorr.notificar("ERROR", "Ingrese texto de respuesta", "Warning");
							return;
						}
						sessionStorage.setItem("urlOrigen", "bandejaEntrada");
						sessionStorage.setItem("accionPrecedente", "completarCorrespondencia");
						sessionStorage.setItem("correlativo", ref.correspondencia.correlativo);
						sessionStorage.setItem("comentario", ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val() || '');
						sessionStorage.setItem("idAsignacion", ref.correspondencia.idAsignacion);
						sessionStorage.setItem("tipoAccion", ref.correspondencia.tipoAccion);
						ref.modulo.irANuevaCorrespondencia();
					}else{
						ref.modulo.verificarEstadoNumeroDocumento(nroDoc)
								.then(function(respuesta){
									console.log("Respuesta:");
									console.log(respuesta);
									if(respuesta.estado == false || respuesta.estado == "false"){
										ref.modSistcorr.procesar(false);
										ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
									}else{
										var comentario = ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val() || '';
										var acciones = ref.modSistcorr.getAcciones();
										var accionCorrespondencia;
										for(var i in acciones){
											var accion = acciones[i];
											if(accion.codigoAccion == ref.correspondencia.codigoAccion){
												accionCorrespondencia = accion;
												break;
											}
										}
										if(!accionCorrespondencia){
											ref.modSistcorr.notificar("ERROR", "Correspondencia no tiene tipo de acción", "Warning");
											ref.modSistcorr.procesar(false);
											return;
										}
										if(accionCorrespondencia.requiereRespuesta == 'SI' && comentario.length == ''){
											ref.modulo.VISTA.compComentarioCerrarCorrespondencia.focus();
											ref.modSistcorr.procesar(false);
											ref.modSistcorr.notificar("ERROR", "Ingrese texto de respuesta", "Warning");
											return;
										}
										var comentario = ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val() || '';
										var correspondencia = {'numeroDocumento': nroDoc, 'correlativo': ref.correspondencia.correlativo, 'idAsignacion': ref.correspondencia.idAsignacion, 'tipoAccion': ref.correspondencia.tipoAccion, 'respuesta': comentario};
										ref.modulo.enlazarCorrespondenciaNumeroDocumento(correspondencia)
												.then(function(respuesta){
													console.log(respuesta);
													if(respuesta.estado == true || respuesta.estado == "true"){
														ref.completarCorrespondencia();
													}else{
														ref.modSistcorr.procesar(false);
														ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
													}
												}).catch(function(error){
													ref.modSistcorr.procesar(false);
													ref.modSistcorr.showMessageErrorRequest(error);
												})
									}
								}).catch(function(error){
									ref.modSistcorr.procesar(false);
									ref.modSistcorr.showMessageErrorRequest(error);
								});
					}
				}else{
					//ref.completarCorrespondencia();
					if(accionbtn == "completarcorrespondencia")
						ref.completarCorrespondencia();
					else ref.rechazarAsignacionCorrespondencia();
				}
			}else{
				ref.modSistcorr.procesar(false);
				//ref.modSistcorr.notificar("ERROR", "La asignación que intenta Completar tiene un documento respuesta en curso", "Error");
				if(accionbtn == "rechazarasignacion")//TICKET 9000004273
					ref.modSistcorr.notificar("ERROR", "No se puede realizar el rechazo de la correspondencia debido a que tiene un documento respuesta en curso.", "Error");//TICKET 9000004273
				else ref.modSistcorr.notificar("ERROR", "La asignación que intenta Completar tiene un documento respuesta en curso", "Error");
			}
		},
		// FIN TICKET
		
		cargarArchivos: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.cargarAdjunto(ref.correspondencia, ref.archivosSeleccionados)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("Exito", respuesta.mensaje, "Success");
						
						let documentTamano = Number.parseInt(respuesta.datos[0].tamano);
						documentTamano = Math.round(documentTamano / 1000);
						documentTamano = documentTamano / 1000;
						let tamano = documentTamano  + " MB";
						
						let tipoDocumento = respuesta.datos[0].mimeType;
						let documentClass = "fas fa-file icon_otro mr-3";
						if(tipoDocumento.includes('pdf')){
							documentClass = "fas fa-file-pdf icon_pdf mr-3";
						}else if(tipoDocumento.includes('excel')){
							documentClass = "fas fa-file-excel icon_excel mr-3";
						}else if(tipoDocumento.includes('word')){
							documentClass = "fas fa-file-word icon_word mr-3";
						}else if(tipoDocumento.includes('image')){
							documentClass = "fas fa-file-image icon_image mr-3";
						}
						var plantillaScript = ref.modulo.plantillaDocumento.html();
						var plantilla = Handlebars.compile(plantillaScript);
						var contexto = {'docId' : respuesta.datos[0].id, 'docTamano' : tamano, 'docDocumentTitle' : respuesta.datos[0].documentTitle, 'docClass' : documentClass};
						var contenidoHTML = plantilla(contexto);
						ref.modulo.listaDocumentos.append(contenidoHTML);
						
						$(".documentoDescarga").click(function(event){
							var $documento = $(event.currentTarget);
							var idDocumento = $documento.data("id");
							var sizeDocumento = $documento.data("size");
							console.log("DESCARGANDO DOCUMENTO");
							ref.verificarDocumento(idDocumento, sizeDocumento);
						});
						
						$(".documentoEliminar").off('click').on('click', function(){
							var eli = $(this);
							var id = eli.attr("data-id");
							ref.idDocumentoEliminar = id
							$("#modalEliminarArchivo").modal('show');
						});
						
						ref.modSistcorr.procesar(false);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.notificar("Error", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				});
			$("#modalCargarArchivo").modal('hide');
			ref.modulo.archivoAdjunto.val('');
			ref.modulo.textDocumento.val('');
		},
		
		abrirTypeFile: function(){
			var ref = this;
			ref.modulo.archivoAdjunto.click();
		},
		
		inicializarPaneles: function(){
			$(".panel-2").css('display', 'none');
			$(".panel-3").css('display', 'none');
		},
		
		cambiarPanel: function(){
			var display = $(".panel-2").css('display');
			if(display == "none"){
				//$(".panel-1").css('display', 'none');
				$(".verMas").html("<b>Ver Menos</b>");
				$(".panel-2").css('display', '');
				$(".icono_mas_filtros").attr('class', 'icono_mas_filtros fa fa-minus-square')
			}else{
				//$(".panel-1").css('display', '');
				$(".verMas").html("<b>Ver M&aacute;s</b>");
				$(".panel-2").css('display', 'none');
				$(".icono_mas_filtros").attr('class', 'icono_mas_filtros fa fa-plus-square')
			}
		},
		
		cambiarPanel2: function(){
			var display = $(".panel-3").css('display');
			if(display == "none"){
				//$(".panel-1").css('display', 'none');
				$(".verMas-2").html("<b>Ver Menos</b>");
				$(".panel-3").css('display', '');
				$(".icono_mas_filtros_2").attr('class', 'icono_mas_filtros_2 fa fa-minus-square')
			}else{
				//$(".panel-1").css('display', '');
				$(".verMas-2").html("<b>Ver M&aacute;s</b>");
				$(".panel-3").css('display', 'none');
				$(".icono_mas_filtros_2").attr('class', 'icono_mas_filtros_2 fa fa-plus-square')
			}
		},
		
		verificarDocumento: function(documento, tamanio){
			var ref = this;
			if(ref.rol_jefe==true || 
					(ref.rol_gestor == true && (!(ref.correspondencia.confidencial == "SI" && (ref.correspondencia.esAsignado == null || ref.correspondencia.esAsignado == "NO" || ref.correspondencia.esAsignado == "")))) || 
					(ref.rol_jefe == false && ref.rol_gestor == false && (ref.correspondencia.esAsignado == "SI" || ref.correspondencia.confidencial == "NO"))){
				tamanio = tamanio.replace(" MB", "");
				tamanio = tamanio.trim();
				tamanio = Number(tamanio) || 0;
				var tamanioMax = ref.tamanioMaxArchivo;
				if(tamanio > tamanioMax){
					ref.modulo.VISTA.compModalDescargarArchivo.modal('show');
					ref.modulo.VISTA.compBtnDescargarArchivo.click(function(){
						ref.modulo.VISTA.compModalDescargarArchivo.modal('hide')
						setTimeout(function () {
							ref.modulo.abrirDocumento(documento);
						}, 200);
					});
				}else{
					ref.modulo.abrirDocumento(documento);
				}
			}else{
				ref.modSistcorr.notificar("ERROR", "Usted no tiene permiso para ver el documento solicitado (confidencial)", "Error");
			}
			
		},
		
		abrirModalCompletarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val("");
			// TICKET 900004044
			ref.modulo.VISTA.compChkRequiereDocumentoCompletar.attr('checked', true);
			ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.val("");
			// FIN TICKET
			ref.modulo.VISTA.compModalCompletarCorrespondencia.modal('show');
			setTimeout(function() {
				// TICKET 900004044
				//ref.modulo.VISTA.compComentarioCompletarCorrespondencia.focus();
				ref.modulo.VISTA.compTxtNumeroDocumentoCompletar.focus();
				// FIN TICKET
				ref.modSistcorr.procesar(false);
			}, 500);
			
			
		},
		
		//INICIO TICKET 9000004273
		abrirModalRechazarAsigCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.VISTA.compTxtMotivoRechazarAsignacionCorres.val("");
			
			ref.modulo.VISTA.compModalRechazarAsignacionCorres.modal('show');
			setTimeout(function() {
				
				ref.modulo.VISTA.compTxtMotivoRechazarAsignacionCorres.focus();
				
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		//FIN TICKET 9000004273
		
		abrirModalCerrarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.VISTA.compComentarioCerrarCorrespondencia.val("");
			// TICKET 9000004044
			ref.modulo.VISTA.compChkRequiereDocumentoCerrar.prop('checked', true);
			ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.removeAttr('readonly');
			ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.val("");
			// FIN TICKET
			ref.modulo.VISTA.compModalCerrarCorrespondencia.modal('show');
			setTimeout(function() {
				// TICKET 9000004044
				//ref.modulo.compComentarioCerrarCorrespondencia.focus();
				ref.modulo.VISTA.compTxtNumeroDocumentoCerrar.focus();
				// FIN TICKET	
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		cerrarCorrespondencia: function(){
			var ref = this;
			var comentario = ref.modulo.VISTA.compComentarioCerrarCorrespondencia.val() || '';
			ref.modSistcorr.procesar(true);
			ref.modulo.cerrarCorrespondencia(ref.correspondencia.correlativo, comentario)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function(){
							ref.modulo.VISTA.btnRetroceder.click();
						}, 4000);
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.VISTA.compModalCerrarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		completarCorrespondencia: function(){
			var ref = this;
			var comentario = ref.modulo.VISTA.compComentarioCompletarCorrespondencia.val() || '';
			var acciones = ref.modSistcorr.getAcciones();
			var accionCorrespondencia;
			for(var i in acciones){
				var accion = acciones[i];
				if(accion.codigoAccion == ref.correspondencia.codigoAccion){
					accionCorrespondencia = accion;
					break;
				}
			}
			if(accionCorrespondencia.requiereRespuesta == 'SI' && comentario.length == ''){
				ref.modSistcorr.procesar(false);
				ref.modulo.VISTA.compComentarioCompletarCorrespondencia.focus();
				ref.modSistcorr.notificar("ERROR", "Ingrese texto de respuesta", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.completarCorrespondencia(ref.correspondencia.idAsignacion, comentario)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function(){
							ref.modulo.VISTA.btnRetroceder.click();
						}, 4000);
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.VISTA.compModalCompletarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		//INICIO TICKET 9000004273
		rechazarAsignacionCorrespondencia: function(){
			var ref = this;
			var motivo = ref.modulo.VISTA.compTxtMotivoRechazarAsignacionCorres.val() || '';
			var acciones = ref.modSistcorr.getAcciones();
			var accionCorrespondencia;
			for(var i in acciones){
				var accion = acciones[i];
				if(accion.codigoAccion == ref.correspondencia.codigoAccion){
					accionCorrespondencia = accion;
					break;
				}
			}
			if(motivo.length == ''){//accionCorrespondencia.requiereRespuesta == 'SI' && comentario.length == ''
				ref.modSistcorr.procesar(false);
				ref.modulo.VISTA.compTxtMotivoRechazarAsignacionCorres.focus();
				ref.modSistcorr.notificar("ERROR", "Ingrese motivo de rechazo", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.rechazarAsignacionCorrespondencia(ref.correspondencia.idAsignacion, motivo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function(){
							ref.modulo.VISTA.btnRetroceder.click();
						}, 4000);
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.VISTA.compModalRechazarAsignacionCorres.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		//FIN TICKET 9000004273
		
		irEnviarCopia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			var tipo = "";
			var id = "";
			// TICKET 9000003862
			//if(ref.correspondencia.nuevaCorrespondencia){
			if(ref.correspondencia.nuevaCorrespondencia || ref.correspondencia.porAceptar){
				tipo = "corr";
				id = ref.correspondencia.correlativo;
			}else{
				tipo = "asig";
				id = ref.correspondencia.idAsignacion;
			}
			sessionStorage.setItem('urlBack', '../../../../app/' + ref.bandeja + '/informacion/' + tipo + '/' + id + "?workflow=" + ref.modulo.VISTA.compWobNum.val())
			window.location.replace("../../../../app/" + ref.bandeja + "/copia/corr/" + ref.modulo.VISTA.compCorrelativo.val() + "?workflow=" + ref.modulo.VISTA.compWobNum.val());
		},
		
		mostrarErrores: function(){
			var ref = this;
			for(var i in ref.errores){
				var err = ref.errores[i];
				if(err){
					ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");	
				}
				
			}
		},
		
		eliminarDocumento: function(){
			var ref = this;
			var proceso = $("#proceso").val();
			var codigoTraza = $("#codigoTraza").val();
			ref.modulo.eliminarDocumento(ref.idDocumentoEliminar, proceso, codigoTraza)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						$("#li_" + $.escapeSelector(ref.idDocumentoEliminar)).remove();
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					$("#modalEliminarArchivo").modal('hide');
					ref.idDocumentoEliminar = null;
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.idDocumentoEliminar = null;
				});
		},
		
		abrirModalAceptarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compModalAceptarCorrespondencia.modal('show');
			setTimeout(function() {
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		abrirModalRechazarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compComentarioRechazarCorrespondencia.val("");
			ref.modulo.compModalRechazarCorrespondencia.modal('show');
			setTimeout(function() {
				ref.modulo.compComentarioRechazarCorrespondencia.focus();
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		aceptarCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaPorAceptar){
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener información de la correspondencia", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			console.log(ref.correspondenciaPorAceptar);
			console.log(ref.correspondenciaPorAceptar.correlativo);
			console.log(ref.correspondenciaPorAceptar.correlativo.correlativo);
			console.log("Accion:" + ref.correspondenciaPorAceptar.correlativo.correlativo);
			ref.modulo.aceptarCorrespondencia(ref.correspondenciaPorAceptar.correlativo.correlativo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modulo.procesarAceptarCorrespondencia(ref.correspondencia.numeroDocumento, ref.correspondencia.correlativo)
							.then(function(respuesta){
								if(respuesta.estado == true){
									if(respuesta.mensaje != ""){
										ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
									}
								}else{
									ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
								}
								ref.modulo.VISTA.btnRetroceder.click();
							}).catch(function(error){
								ref.modSistcorr.procesar(false);
								//ref.modSistcorr.showMessageErrorRequest(error);
								ref.modulo.VISTA.btnRetroceder.click();
							});
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalAceptarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		rechazarCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaPorRechazar){
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener información de la correspondencia", "Warning");
				return;
			}
			var comentario = ref.modulo.compComentarioRechazarCorrespondencia.val() || '';
			if(comentario.trim() == ""){
				ref.modSistcorr.notificar("ERROR", "Debe ingresar una observación.", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.rechazarCorrespondenciaMPV(ref.correspondenciaPorRechazar.correlativo.correlativo, comentario)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						/*ref.modulo.procesarRechazarCorrespondenciaMPV(ref.correspondencia.numeroDocumento, ref.correspondencia.correlativo, comentario)
							.then(function(respuesta){
								console.log(respuesta)
							}).catch(function(error){
								ref.modSistcorr.procesar(false);
								//ref.modSistcorr.showMessageErrorRequest(error);
								
							});*/
						setTimeout(function() {
							ref.modulo.VISTA.btnRetroceder.click();
						}, 3000);
						
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalRechazarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		abrirModalAgregarObservacion: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compObservacionRegistrarCorrespondencia.val("");
			ref.modulo.compModalRegistrarObservacion.modal('show');
			setTimeout(function() {
				ref.modulo.compObservacionRegistrarCorrespondencia.focus();
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		agregarObservacion: function(){
			var ref = this;
			var observacion = ref.modulo.compObservacionRegistrarCorrespondencia.val() || '';
			if(observacion.trim() == ""){
				ref.modSistcorr.notificar("ERROR", "Debe ingresar una observación.", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.registrarObservacion(ref.correspondencia.correlativo, observacion)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modulo.compModalRegistrarObservacion.modal('hide');
						sessionStorage.setItem("tab", "Observaciones-tab");
						location.reload();
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalRechazarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		recargarObservaciones: function(){
			
		},
		
		abrirModalAsignarDestinatario : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compAsignDestin.myModal.modal('show');
			setTimeout(function() {
				ref.iniciarCombosAutoCompletados();
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 1500);
		},
		
		abrirModalRechazarCorrespondenciaReceptor : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compRechazarCorrespReceptor.myModal.modal('show');
			setTimeout(function() {
				ref.modSistcorr.procesar(false);
			}, 1500);
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
			
			ref.componentes.combosS2.destInternoTrabado = ref.modulo.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES, //'http://localhost:8083/sistcorr/app/emision/buscar/lugar'
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.lugarTrabajo.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    },
				}
			}).on('select2:select', function(event){
				ref.modulo.compDependencia.val('');
				ref.modulo.compDependencia.change();
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			
			ref.componentes.combosS2.destInternoDependencia =  ref.modulo.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS_UM,
				    data: function (params) {
				        var query = {
				        		codLugar: ref.modulo.compLugarTrabajo.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			
		},
};


setTimeout(function(){
	CORRESPONDENCIA_DETALLE_VISTA.correspondencia = CORRESPONDENCIA;
	CORRESPONDENCIA_DETALLE_VISTA.tamanioMaxArchivo = TAMANIO_MAX_ARCHIVO;
	CORRESPONDENCIA_DETALLE_VISTA.bandeja = BANDEJA;
	CORRESPONDENCIA_DETALLE_VISTA.errores = ERRORES || [];
	CORRESPONDENCIA_DETALLE_VISTA.rol_jefe = ES_JEFE;
	CORRESPONDENCIA_DETALLE_VISTA.rol_gestor = ES_GESTOR;
	CORRESPONDENCIA_DETALLE_VISTA.textoMas = utilitario_textLarge;
	CORRESPONDENCIA_DETALLE_VISTA.inicializar();
	CORRESPONDENCIA_DETALLE_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_DETALLE_VISTA.listas = {};
	CORRESPONDENCIA_DETALLE_VISTA.listas.lugarTrabajo =  new LISTA_DATA([]);
	CORRESPONDENCIA_DETALLE_VISTA.listas.dependencia =  new LISTA_DATA([]);
}, 200);