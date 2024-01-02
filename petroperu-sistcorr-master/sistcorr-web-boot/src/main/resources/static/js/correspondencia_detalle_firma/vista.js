var modulo_correspondencia_detalle = MODULO_CORRESPONDENCIA_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);
var modulo_firma_digital = MODULO_FIRMA_DIGITAL.getInstance(SISTCORR);

var CORRESPONDENCIA_DETALLE_VISTA = {
		correspondenicaSeleccionada: null,
		archivosAdjuntos: [],
		destinatariosExternos: [],
		destinatariosInternos: [],
		copias: [],
		destinatariosDocPagar: [],//ticket 9000004765
		recepcion: [],
		modSistcorr: null,
		modulo: null,
		modFirmaDigital: null,
		sizeScreen: 500,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.obtenerCorrespondencia();
			ref.ocultarFirmaDigital();

		},
		//ticket 9000004962
		ocultarFirmaDigital: function(){
			var ref = this;
			var url_actual = window.location.href;
			url_actual = url_actual.replace('#!', '');
			var nav = JSON.parse(sessionStorage.getItem('SISTCORR_LOG'));
			var indice = 0;
			for(var i = nav.length -1; i >= 0; i--){
				if(nav[i] == url_actual){
					indice = i -1;
					var paginaAnterior = nav[indice];
					if(paginaAnterior.indexOf('undefined') > -1){
					} else {
						var paginaAnt = paginaAnterior.substring(paginaAnterior.lastIndexOf('/') + 1);
						if (paginaAnt == 'consulta-auditoria-salida'){
							document.getElementById('firmaDocumento').style.display = 'none';
						}
					}		
					break;
				}
			}
		},
		//Fin ticket 9000004962
		
		obtenerCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.obtenerCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.correspondenicaSeleccionada = respuesta.datos[0];
						ref.actualizarValores_Formulario();
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			
		},
		

		
		// TICKET 9000004044
		eventoRecepcion: function(){
			var ref = this;
			console.log("Inicializando abrir historial asignacion");
			$(".abrirHistorialAsignacion").click(function(){
				var t = $(this);
				var corr = t.data('id');
				console.log(corr);
				ref.modulo.modalHistorialAsignaciones.modal('show');
				ref.modulo.correlativoEntrada.val(corr);
				$("label[for='textCorrelativo']").addClass('active');
				ref.modulo.obtenerHistorialAsignaciones(corr)
						.then(function(respuesta){
							ref.modSistcorr.procesar(false);
							if(respuesta.estado == true){
								if(respuesta.datos.length == 0){
									$("#sinAsignaciones").show();
									$("#asignaciones").hide();
								}else{
									$("#sinAsignaciones").hide();
									ref.asignaciones = respuesta.datos;
									var html = ref.modulo.htmlAsignaciones(ref.asignaciones);
									//console.log(html);
									$("#asignaciones").html(html);
									$("#asignaciones").show();
								}
							}
						}).catch(function(error){
							ref.modSistcorr.procesar(false);
							ref.modSistcorr.showMessageErrorRequest(error);
						});
			});
		},
		// FIN TICKET
		
		inicializarEventoComponentes: function(){
			var ref = this;
			
			if(screen.width < ref.sizeScreen){
				ref.modulo.tabs.tabDestintarios.compHeader.html('Dest.');
				ref.modulo.tabs.tabArchivos.compHeader.html('Arch.');
				$("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compLugar.attr('id') + "']").text("Cent. de Gest. Corresp.");
				$("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compNombreRemitente.attr('id') + "']").text("Jefe. Depen. Remit.");
				//ref.modulo.compAsuntoHeader.css("max-width", "250px")
			}
			
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				 var $tab = $(e.currentTarget);
				 $('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
				 $tab.addClass('petro-tabs-activo');
			});
			
			$("." + ref.modulo.classDescargarArchivo).click(function(event){
				var $archivo = $(event.currentTarget);
				var id = $archivo.data("identificador");
				var permiso = $("#visualizar_archivo").val();
				if(permiso=="1"){
					ref.modulo.descargarArchivo(id);
				}else{
					ref.modSistcorr.notificar("ERROR", "Usted no tiene permiso para ver el documento solicitado (confidencial)", "Error");
				}
			});
			
			ref.modulo.compRetroceder.click(function(){
				sessionStorage.setItem("origPag", "verDetalleBS");
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
			
			// TICKET 9000003791
			ref.modulo.btnCompartir.click(function(){
				var modo = ref.modulo.optCompartir.val();
				if(modo == ""){
					ref.modSistcorr.notificar("ERROR", "Seleccione un modo de compartir el documento", "Error");
				}else{
					console.log("Compartir");
					var checks = 0;
					var ids = [];
					var size = 0;
					$(".check-compartir").each(function(){
						var t = this;
						console.log(t);
						var display = $(t).attr('style');
						console.log("Display:");
						console.log(display)
						if(this.checked && (display!="display:none;" && display!="display: none;")){
							checks++;
							ids.push($(t).attr('id'));
							size = size + parseFloat($(t).attr('size'));
						}
					});
					if(checks==0){
						ref.modSistcorr.notificar("ERROR", "Seleccione un archivo", "Error");
					}else{
						var ids_ = ids.join(",");
						var modo = ref.modulo.optCompartir.val();
						if(modo=="IMP"){
							window.open(ref.modulo.URL_DESCARGAR_CORRESPONDENCIA + "/" + ids_);
						}else if(modo=="DIR"){
							//window.open(ref.modulo.URL_COMPARTIR_ARCHIVO + "/" + ref.correspondenicaSeleccionada.id + "/" + ids_ + "/" + modo);
							window.location.href = ref.modulo.URL_COMPARTIR_ARCHIVO + "/" + ref.correspondenicaSeleccionada.id + "/" + ids_ + "/" + modo;
						}else if(modo=="ADJ"){
							if(size<15){
								//window.open(ref.modulo.URL_COMPARTIR_ARCHIVO + "/" + ref.correspondenicaSeleccionada.id + "/" + ids_ + "/" + modo);
								window.location.href = ref.modulo.URL_COMPARTIR_ARCHIVO + "/" + ref.correspondenicaSeleccionada.id + "/" + ids_ + "/" + modo;
							}else{
								ref.modSistcorr.notificar("ERROR", "El tamaño de los archivos excede los 15 MB permitidos por el servidor de correo", "Error");
							}
						}
					}
				}
			});
			
			ref.modulo.optCompartir.change(function(){
				var modo = ref.modulo.optCompartir.val();
				if(modo == ""){
					$("." + ref.modulo.tdCompartir).hide();
					$(".td-check-compartir-all").hide();
				}else{
					$("." + ref.modulo.tdCompartir).show();
					$(".td-check-compartir-all").show();
				}
			});
			
			ref.modulo.btnHistorial.click(function(){
				console.log("AQUI");
				window.location.href = ref.modulo.URL_HISTORIAL_COMPARTIDO + "/" + ref.correspondenicaSeleccionada.id;
			});
			
			ref.modulo.clsCompartirAll.click(function(){
				var check = ref.modulo.clsCompartirAll.prop('checked');
				if(check){
					$(".check-compartir").each(function(){
						var t = $(this);
						t.prop('checked', true);
					});
				}else{
					$(".check-compartir").each(function(){
						var t = $(this);
						t.prop('checked', false);
					});
				}
			});
			// FIN TICKET 9000003791
			
			// TICKET 9000004272
			ref.modulo.tabs.tabArchivos.btnEmitirFirma.click(function(event){
				var $comp = $(event.currentTarget);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref._procesoEmitirFirma(idCorrespondencia);
			});
			// FIN TICKET
			
			ref.modulo.tabs.tabRecepcion.compNroEnvio.change(function(){
				var cmb = $(this);
				console.log('Valor combo nro envio:' + cmb.val());

				var idCorrespondencia = ref.correspondenicaSeleccionada.id;
				var nroEnvio = $("#nroEnvio").val();
				ref.modulo.obtenerDestinatarioRespuesta(idCorrespondencia, nroEnvio)
					.then(function(respuesta){
						console.log("Respuesta Recepcion:");
						console.log(respuesta);
						if(respuesta.estado == true){
							console.log("true");
							ref.recepcion = respuesta.datos;
							var html = ref.modulo.htmlRecepcion(ref.recepcion);
							//console.log(html);
							ref.modulo.tabs.tabRecepcion.compRecepciones.html(html);
							setTimeout(function(){ref.eventoRecepcion();}, 500);
						}else{
							console.log("false");
							console.log(respuesta.estado == true);
							ref.recepcion = [];
						}
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
			});
			
			// INICIO TICKET 9000003996
			ref.modulo.changeFlujoNombre.change(function(){
				console.log("entrar");
				
				ref.modulo.obtenerFlujo().then(function(respuesta){
					
				}).catch(function(error){
					console.log("Error");
				});
			})
			
			// FIN TICKET 9000003996
			
			
			setTimeout(function() {
				ref.modulo.tabs.tabDatos.acoordion.correspondencia.btnHead.click();
			}, 100);
			
		},
		
		// TICKET 9000004272
		_procesoEmitirFirma: function(idCorrespodencia){
			var ref = this;
			ref.modFirmaDigital.generarZip(idCorrespodencia)
				.then(function(respuesta){
					var url_zip = respuesta;
					sessionStorage.setItem('procedenciaFirma', 'verDetalle');
					ref.modFirmaDigital.abrirFormularioFirma(idCorrespodencia);
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		// FIN TICKET
		

		
		actualizarValores_Formulario: function(){
			var ref = this;
			
			if(ref.correspondenicaSeleccionada.rutaAprobacion == false){
				$("#RutaAprobacion-tab").closest('li').hide();
			}else{
				$("#RutaAprobacion-tab").closest('li').show();
			}
			
			for(var i in ref.correspondenicaSeleccionada.adjuntos){
				var adjunto =  ref.modSistcorr.clonarObjecto({}, ref.correspondenicaSeleccionada.adjuntos[i]);
				adjunto.identificador = ref.correspondenicaSeleccionada.adjuntos[i].id;
				adjunto.file = null;
				adjunto.tipo = adjunto.principal == true ? 'PRINCIPAL' : 'SECUNDARIO';
				ref.archivosAdjuntos.push(adjunto); 
			}
			for(var i in ref.correspondenicaSeleccionada.detalleInterno){
				var interno = ref.modSistcorr.clonarObjecto({}, ref.correspondenicaSeleccionada.detalleInterno[i]);
				interno.identificador = ref.correspondenicaSeleccionada.detalleInterno[i].id;
				ref.destinatariosInternos.push(interno);
			}
			for(var i in ref.correspondenicaSeleccionada.detalleExterno){
				var externo = ref.modSistcorr.clonarObjecto({}, ref.correspondenicaSeleccionada.detalleExterno[i]);
				externo.identificador = ref.correspondenicaSeleccionada.detalleExterno[i].id;
				ref.destinatariosExternos.push(externo);
			}
			for(var i in ref.correspondenicaSeleccionada.detalleCopia){
				var copia =  ref.modSistcorr.clonarObjecto({}, ref.correspondenicaSeleccionada.detalleCopia[i]);
				copia.identificador = ref.correspondenicaSeleccionada.detalleCopia[i].id;
				ref.copias.push(copia);
			}
			//inicio ticket 9000004765
			for(var i in ref.correspondenicaSeleccionada.detalleCorrespDestDocPagar){
				var deta =  ref.modSistcorr.clonarObjecto({}, ref.correspondenicaSeleccionada.detalleCorrespDestDocPagar[i]);
				deta.identificador = ref.correspondenicaSeleccionada.detalleCorrespDestDocPagar[i].id;
				ref.destinatariosDocPagar.push(deta);
			}
			//fin ticket 9000004765
			
			//Datos
			var label;
			ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativo.val(ref.correspondenicaSeleccionada.correlativo.codigo);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativo.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativoExterno.val(ref.correspondenicaSeleccionada.fileNetCorrelativo);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativoExterno.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compDependenciaOriginadora.val(ref.correspondenicaSeleccionada.dependenciaOriginadora);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compDependenciaOriginadora.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compDependencia.val(ref.correspondenicaSeleccionada.dependencia);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compDependencia.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compLugar.val(ref.correspondenicaSeleccionada.lugarTrabajo);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compLugar.attr('id') + "']");
			label.addClass('active');
			
			ref.modulo.tabs.tabDatos.acoordion.remitente.compOriginador.val(ref.correspondenicaSeleccionada.originador);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compOriginador.attr('id') + "']");
			label.addClass('active');
			
			
			ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondencia.val(ref.correspondenicaSeleccionada.tipoCorrespondencia);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondencia.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compNombreRemitente.val(ref.correspondenicaSeleccionada.remitente);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compNombreRemitente.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.remitente.compAsunto.text(ref.correspondenicaSeleccionada.asunto);
			//label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compAsunto.attr('id') + "']");
			//label.addClass('active');
			if(ref.correspondenicaSeleccionada.codTipoCorrespondencia == '107' ){ //OTROS
				ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondenciaOtros.parent().show();
				ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondenciaOtros.val(ref.correspondenicaSeleccionada.tipoCorrespondenciaOtros);
				label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondenciaOtros.attr('id') + "']");
				label.addClass('active');
			} else {
				ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoCorrespondenciaOtros.parent().hide();
			}
			
			console.log((ref.correspondenicaSeleccionada.jerarquia == true ? "SI1": "NO2"));
			ref.modulo.tabs.tabDatos.acoordion.remitente.compJerarquia.val(ref.correspondenicaSeleccionada.jerarquia == true ? "SI": "NO");
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compJerarquia.attr('id') + "']");
			label.addClass('active');
			
			ref.modulo.tabs.tabDatos.acoordion.remitente.compRutaAprobacion.val(ref.correspondenicaSeleccionada.rutaAprobacion == true ? "SI": "NO");
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compRutaAprobacion.attr('id') + "']");
			label.addClass('active');
			
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFecha.val(ref.correspondenicaSeleccionada.fechaDocumento);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFecha.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFirmaDigital.val(ref.correspondenicaSeleccionada.firmaDigital == true ? "SI": "NO");
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFirmaDigital.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compTipoEmision.val(ref.correspondenicaSeleccionada.tipoEmision.nombre);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compTipoEmision.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compDespachoFisico.val(ref.correspondenicaSeleccionada.despachoFisico == true ? "SI": "NO");
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compDespachoFisico.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compConfidencial.val(ref.correspondenicaSeleccionada.confidencial == true ? "SI": "NO" );
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compConfidencial.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compUrgente.val(ref.correspondenicaSeleccionada.urgente == true ? "SI": "NO" );
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compUrgente.attr('id') + "']");
			label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compObservacion.text(ref.correspondenicaSeleccionada.observaciones );
			//label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compObservacion.attr('id') + "']");
			//label.addClass('active');
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compEstado.val(ref.correspondenicaSeleccionada.estado.descripcionEstado );
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compEstado.attr('id') + "']");
			label.addClass('active');
			var miliSeconds = ref.correspondenicaSeleccionada.fechaModifica;
			if(miliSeconds == null || miliSeconds == "null"){
				miliSeconds = ref.correspondenicaSeleccionada.fechaCrea;
			}
			var ultAct = new Date(miliSeconds);
			var min = ultAct.getMinutes();
			if(min<10){ min = "0" + min; }
			var hor = ultAct.getHours();
			if(hor<10){ hor = "0" + hor; }
			var day = ultAct.getDate();
			if(day<10){ day = "0" + day; }
			var month = ultAct.getMonth() + 1;
			if(month<10){ month = "0" + month; }
			var year = ultAct.getFullYear();
			ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFecAct.val(day + "/" + month + "/" + year + " " + hor + ":" + min);
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.correspondencia.compFecAct.attr('id') + "']");
			label.addClass('active');
			
			$('.descripcion_rechazo').addClass('more');
			setTimeout(function() {
				utilitario_textLarge.inicializar();
			}, 300);
			
			// TICKET 9000004044
			console.log("OBTENIENDO DATOS DE ENLACE");
			ref.modulo.tabs.tabDatos.acoordion.remitente.compEsDocumentoRespuesta.val(ref.correspondenicaSeleccionada.esDocumentoRespuesta==true?'SI':'NO');
			label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compEsDocumentoRespuesta.attr('id') + "']");
			label.addClass('active');
			if(ref.correspondenicaSeleccionada.esDocumentoRespuesta==false){
				ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativoEntrada.closest('div').hide();
				ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoAccion.closest('div').hide();
				ref.modulo.tabs.tabDatos.acoordion.remitente.compRespuesta.closest('div').hide();
			}else{
				ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativoEntrada.val(ref.correspondenicaSeleccionada.correlativoEntrada || '');
				label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compCorrelativoEntrada.attr('id') + "']");
				label.addClass('active');
				if(ref.correspondenicaSeleccionada.idAsignacion == null || ref.correspondenicaSeleccionada.idAsignacion == "null" || ref.correspondenicaSeleccionada.idAsignacion == 0){
					ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoAccion.closest('div').hide();
				}else{
					ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoAccion.val(ref.correspondenicaSeleccionada.tipo || '');
					label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compTipoAccion.attr('id') + "']");
					label.addClass('active');
				}
				ref.modulo.tabs.tabDatos.acoordion.remitente.compRespuesta.val(ref.correspondenicaSeleccionada.respuesta || '');
				label = $("label[for='" + ref.modulo.tabs.tabDatos.acoordion.remitente.compRespuesta.attr('id') + "']");
				label.addClass('active');
			}
			// FIN TICKET
			
			// TICKET 9000004272
			console.log("INGRESANDO ID PARA FUNCIONALIDAD BOTON FIRMAR");
			console.log(ref.correspondenicaSeleccionada.id);
			ref.modulo.tabs.tabArchivos.btnEmitirFirma.attr('data-idcorrespondencia', ref.correspondenicaSeleccionada.id);
			// FIN TICKET
			
			
			//Destinatarios
			if(ref.correspondenicaSeleccionada.tipoEmision.id == 1){
				ref.modulo.tabs.tabDestintarios.compListaDestinatarios.empty();
				var html = ref.modulo.htmlListaDestinatariosInternos(ref.destinatariosInternos);
				ref.modulo.tabs.tabDestintarios.compListaDestinatarios.html(html);
				ref.modSistcorr.eventoTooltip();
			} else {
				ref.modulo.tabs.tabDestintarios.compListaDestinatarios.empty();
				var html = ref.modulo.htmlListaDestinatariosExternos(ref.destinatariosExternos, ref.correspondenicaSeleccionada.estado.id);
				ref.modulo.tabs.tabDestintarios.compListaDestinatarios.html(html);
				ref.modSistcorr.eventoTooltip();
			}
			//inicio ticket 9000004765
			if(ref.correspondenicaSeleccionada.codTipoCorrespondencia == ref.modulo.codigoDocPagar && ref.destinatariosDocPagar.length > 0){
				$("#listaDestinatarios").html("<h5><b>"+ref.destinatariosDocPagar[0].destinarioDocPagar.correo+"</b></h5>");
				$('#dest_datos_copias').hide();
			}
			//fin ticket 9000004765
			//Copias
			ref.modulo.tabs.tabDestintarios.compListaCopias.empty();
			var html = ref.modulo.htmlListaCopias(ref.copias);
			ref.modulo.tabs.tabDestintarios.compListaCopias.html(html);
			
			
			//Archivos
			ref.modulo.tabs.tabArchivos.compListaArchivos.empty();
			var html = ref.modulo.htmlListaArchivosAdjuntos(ref.archivosAdjuntos);
			ref.modulo.tabs.tabArchivos.compListaArchivos.html(html);
			ref.modSistcorr.eventoTooltip();
			
			//Flujo
			
			
			setTimeout(function() {
				ref.inicializarEventoComponentes();
			}, 500);
			
			// Recepcion
			var nroEnvio = ref.correspondenicaSeleccionada.nroEnvio;
			if(nroEnvio>0){
				console.log("Nro Envio:" + nroEnvio)
				setTimeout(function(){
					$("#listaSinRecepcion").hide();
					$("#listaRecepcion").show();
					$("#nroEnvio").val(nroEnvio);
					$("#nroEnvio").change();
				}, 1000);
			}
			
		},
		

};

setTimeout(function(){
	CORRESPONDENCIA_DETALLE_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_DETALLE_VISTA.modulo = modulo_correspondencia_detalle;
	CORRESPONDENCIA_DETALLE_VISTA.modFirmaDigital = modulo_firma_digital;
	CORRESPONDENCIA_DETALLE_VISTA.modulo.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_DETALLE_VISTA.inicializar();
}, 500);