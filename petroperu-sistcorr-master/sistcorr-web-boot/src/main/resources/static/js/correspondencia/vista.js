var modulo_correspondecia = MODULO_CORRESPONDENCIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_VISTA = {
		acciones: [],
		dependencias: [],
		dependenciasBPAC: [],//TICKET 9000003866
		bandeja: {correspondencias:[], asignaciones:[]},
		modSistcorr: null,
		modulo: null,
		errores: [],
		searchDefDDest: false,//ticket 9000003866
		rol_jefe: false,
		rol_gestor: false,
		tamanioMaxArchivo: 10,
		limit_export_be_advert: 1000,
		tareaSeleccionada : null,
		correspondenciaSeleccionada: null,
		esDeEmisionSeleccionada: '',
		numeroDocumentoSeleccionado: '',
		correlativoSeleccionado: '',
		asignacionSeleccionada: null,
		filtrosBusqueda: null,
		// TICKET 900000SIST
		pagina: 0,
		cantidad: 100,
		total: 0,
		// FIN TICKET 900000SIST
		// TICKET 9000004270
		workflowIdSeleccionado: '',
		// FIN TICKET
		// TICKET 9000004494
		sessionPaginado: '',
		respuestaBusqueda: null,
		cantidadPrev: 0,
		totalObtenidos: 0,
		itemsPorPagina: 0,
		totalMostrados: 0,
		totalCorrespondencias: 0,
		// FIN TICKET
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.setDependenciasDestino(ref.dependencias);
			ref.modSistcorr.setDependenciasDestinoBPAC(ref.dependenciasBPAC);//TICKET 9000003866
			if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
				ref.verificacionPrevia();
			}
			
			if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
				ref.actualizarCombosDependenciaDestino();
			}
			//INICIO TICKET 9000003866
			/*if(((ref.modulo.compTipoBandeja.val() == "Pendientes" || ref.modulo.compTipoBandeja.val() == "EnAtencion" || ref.modulo.compTipoBandeja.val() == "Completadas") && 
						(ref.rol_jefe == true || ref.rol_gestor == true))){
				ref.verificacionPreviaBPAC();
				ref.actualizarCombosDependenciaDestinoBPAC();
			}*/
			//FIN TICKET 9000003866
			console.log("FILTROS BANDEJA:");
			if(ref.modulo.compCorrelativo.val()==""){
				if(ref.modSistcorr.getFiltros(CORRESPONDENCIA_VISTA.modulo.compTipoBandeja.val()).length == 0){
					console.log("No tiene filtros de búsqueda en sesión");
					var d = new Date();
					var m = d.getMonth();
					// NUMERO DE MESES PREVIOS AL ACTUAL PARA BUSQUEDA POR DEFECTO (HARD CORE, CODIGO EN DURO)
					d.setMonth(d.getMonth() - CONSTANTES_SISTCORR.MESES_PREVIOS);
					if (d.getMonth() == m - 2 || d.getMonth == m + 10) d.setDate(0);
					d.setHours(0, 0, 0);
					d.setMilliseconds(0);
					console.log(d);
					ref.modulo.compBusqueda.fechaDesde.val(d.getDate()  + "/" + (d.getMonth()+1) + "/" + d.getFullYear());
					ref.modulo.compBusqueda.fechaDesde.change();
					ref.obtenerFiltrosBusqueda();
				}else{
					console.log("Tiene un filtro de búsqueda en sesión");
				}
			}else{
				var data = [];
				data.push({'fieldId' : 'TD_sCorrelativo', "operator": "=", "value": (ref.modulo.compCorrelativo.val() || "").toUpperCase()});
				ref.filtrosBusqueda = data;
				ref.modSistcorr.setFiltros(data, ref.modulo.compTipoBandeja.val());
				console.log("Tiene como filtro de búsqueda el correlativo")
			}
			ref.obtenerBandeja();
			ref.modSistcorr.cargarVista();
			ref.modSistcorr.eventoTooltip();
			if(ref.modSistcorr.getCantidadLog() == 1){
				//ref.modulo.abrirMenu();
			}
			ref.iniciarEventos();
			ref.mostrarErrores();
			setTimeout(function(){
				$(".div_eventos>div:nth-child(2n)").css('background-color', 'white');
				$(".div_eventos>div:nth-child(2n+1)").css('background-color', 'black');
			}, 2000);
		},
		
		verificacionPrevia: function(){
			var ref = this;
			var dependencias = ref.modSistcorr.getDependenciasDestino();
			console.log("Cantidad de dependencias:" + dependencias.length);
			if(dependencias.length==1){
				ref.filtrosBusqueda = ref.modSistcorr.getFiltros(ref.modulo.compTipoBandeja.val()) || [];
				console.log("Filtros Busqueda:");
				console.log(ref.filtrosBusqueda);
				console.log(ref.filtrosBusqueda.length);
				if(ref.filtrosBusqueda.length == 0){
					var valor = "";
					var dependencias = ref.modSistcorr.getDependenciasDestino();
					for(var index in dependencias){
						var dependencia = dependencias[index];
						valor = dependencia.codigo;
					}
					ref.modulo.dependenciaDefecto = valor
					console.log("Valor Dependencia:" + ref.modulo.dependenciaDefecto)
					ref.obtenerFiltrosBusqueda();
				}
			}
		},
		
		//INICIO TICKET 9000003866
		verificacionPreviaBPAC: function(){
			var ref = this;
			var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
			console.log("Cantidad de dependencias:" + dependencias.length);
			if(dependencias.length==1){
				ref.filtrosBusqueda = ref.modSistcorr.getFiltros(ref.modulo.compTipoBandeja.val()) || [];
				console.log("Filtros Busqueda:");
				console.log(ref.filtrosBusqueda);
				console.log(ref.filtrosBusqueda.length);
				if(ref.filtrosBusqueda.length == 0){
					var valor = "";
					var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
					for(var index in dependencias){
						var dependencia = dependencias[index];
						valor = dependencia.codigo;
					}
					ref.modulo.dependenciaDefecto = valor;
					console.log("Valor Dependencia:" + ref.modulo.dependenciaDefecto)
					ref.obtenerFiltrosBusqueda();
				}
			}
		},
		//FIN TICKET 9000003866
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modSistcorr.setDependenciasDestino(ref.dependencias);
			
			ref.modSistcorr.setAcciones(ref.acciones);			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.validarCamposModalBusqueda();
			
			ref.modulo.btnAbrirBusqueda.click(function(event){
				ref.abrirModalBusqueda();
			});
			
			ref.modulo.compBusqueda.btnBuscar.click(function(event){
				ref.searchDefDDest = true;//ticket 9000003866
				ref.modulo.dependenciaDefecto = "";
				ref.obtenerFiltrosBusqueda();
				ref.mostrarFiltros();
				ref.buscarCorrespondenciasAsignaciones();
			});
			
			//inicio ticket 9000004710
			ref.modulo.btnActualizarBandeja.click(function(event){
				ref.buscarCorrespondenciasAsignaciones();
			});
			//fin ticket 9000004710
			
			//inicio ticket 9000004807
			ref.modulo.btnExportarExcelCorrespondencia.click(function(event){
				ref.exportarExcelCorrespondenciasAsignaciones();
			});
			ref.modulo.btnContinuarBusqCA.click(function(event){
				ref.exportarExcelCorrespondenciasAsignacionesTodo();
			});
			//fin ticket 9000004807
			
			ref.actualizarCombosTipoAccion();
			
			ref.modulo.compbtnCompletarCorrespondencia.click(function(event){
				// TICKET 9000004044
				//ref.completarCorrespondencia();
				ref.tieneDocumentoRespuestaEnCursoAsignacion("completarcorrespondencia");
				// FIN TICKET
			});
			
			//INICIO TICKET 9000004273
			ref.modulo.compbtnRechazarAsignacionCorres.click(function(event){
				ref.tieneDocumentoRespuestaEnCursoAsignacion("rechazarasignacion");
			});
			//FIN TICKET 9000004273
			
			ref.modulo.compBtnCerrarCorrespondencia.click(function(event){
				// TICKET 9000004044
				//ref.cerrarCorrespondencia();
				ref.tieneDocumentoRespuestaEnCursoCorrespondencia();
				// FIN TICKET
			});
			
			ref.modulo.compBtnAceptarCorrespondencia.click(function(event){
				ref.aceptarCorrespondencia();
			});
			
			ref.modulo.compBtnRechazarCorrespondencia.click(function(event){
				ref.rechazarCorrespondencia();
			});

			
			setTimeout(function() {
				ref.modSistcorr.eventosS2();	
				//ref.modSistcorr.eventoSelect();
			}, 200);
			
			ref.modSistcorr.accordionMenu.click(function(event){
				var t = $(this);
				var id = t.attr('data-id');
				console.log("Id ocultar:" + id);
				ocultarAcordeon(id);
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.compBusqueda.fechaDesde.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            },
	        });
			
			ref.modulo.compBusqueda.btnFechaDesde.click(function(){
				ref.modulo.compBusqueda.fechaDesde.click();
				ref.modulo.compBusqueda.fechaDesde.focus();
			});
			
			ref.modulo.compBusqueda.fechaHasta.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            },
	        });
			
			ref.modulo.compBusqueda.btnFechaHasta.click(function(){
				ref.modulo.compBusqueda.fechaHasta.click();
				ref.modulo.compBusqueda.fechaHasta.focus();
			});
			
			ref.modulo.compVerMas.click(function(){
				// TICKET 9000004494
				//ref.mostrarBandeja2();
				ref.modSistcorr.procesar(true);	
				ref.modulo.compVerMas.hide();
				var llamarSiguiente = false;
				if(ref.sessionPaginado != null){
					for(var fi = 0; fi < ref.sessionPaginado.length; fi++){
						var sigPag = ref.sessionPaginado[fi];
						if(sigPag.continuationData != null){
							llamarSiguiente = true;
						}
					}
				}
				if((ref.pagina + 1) * ref.itemsPorPagina <= ref.bandeja.correspondencias.length){
					ref.mostrarBandeja2();
					console.log("Mostrando Bandeja");
				}else{
					if(llamarSiguiente){
						console.log("Obteniendo correspondencias");
						ref.modulo.filtrarCorrespondenciasSiguiente(ref.respuestaBusqueda)
							.then(function(respuesta){
								ref.bandeja.correspondencias = ref.bandeja.correspondencias.concat(respuesta.datos[0].detalleCorrespondencias);
								ref.cantPrevia = ref.cantidad;
								ref.cantidad = ref.cantidad + respuesta.datos[0].detalleCorrespondencias.length;
								ref.respuestaBusqueda = respuesta.datos[0];
								ref.sessionPaginado = respuesta.datos[0].continuationData;
								var sessTemp = ref.sessionPaginado;
								for(var i = sessTemp.length - 1; i >= 0; i--){
									var filaSess = sessTemp[i];
									var cd = filaSess.continuationData;
									if(cd == null){
										sessTemp.splice(i, 1);
									}
								}
								ref.sessionPaginado = sessTemp;
								ref.mostrarBandeja2();
								ref.modSistcorr.procesar(false);
							}).catch(function(error){
								ref.modSistcorr.procesar(false);
								ref.modSistcorr.showMessageErrorRequest(error);
							});
					}else{
						ref.mostrarBandeja2();
					}
					ref.modulo.compBusqueda.myModal.modal('hide');
				}
				//
			});
			
			// TICKET 900004044
			ref.modulo.compChkRequiereDocumentoCompletar.change(function(){
				var t = this;
				var activo = $(t).prop('checked');
				if(activo){
					ref.modulo.compTxtNumeroDocumentoCompletar.removeAttr("readonly");
					ref.modulo.compTxtNumeroDocumentoCompletar.focus();
				}else{
					ref.modulo.compTxtNumeroDocumentoCompletar.val("");
					ref.modulo.compTxtNumeroDocumentoCompletar.change();
					ref.modulo.compTxtNumeroDocumentoCompletar.attr("readonly", "readonly");
				}
			});
			
			ref.modulo.compChkRequiereDocumentoCerrar.change(function(){
				var t = this;
				console.log("Cambio check cerrar");
				var activo = $(t).prop('checked');
				if(activo){
					ref.modulo.compTxtNumeroDocumentoCerrar.removeAttr("readonly");
					ref.modulo.compTxtNumeroDocumentoCerrar.focus();
				}else{
					ref.modulo.compTxtNumeroDocumentoCerrar.val("");
					ref.modulo.compTxtNumeroDocumentoCerrar.change();
					ref.modulo.compTxtNumeroDocumentoCerrar.attr("readonly", "readonly");
				}
			});
			// FIN TICKET
	
			// TICKET 900004497
			ref.modulo.btnAsignacionGrupal.click(function(e){	
				
				ref.validarAsignacionGrupal();
								
			});
			// FIN TICKET
			
		},
		
		validarAsignacionGrupal: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	

			var bandeja = ref.modulo.compTipoBandeja.val();
			
			var listCorrespondencia= [];
			//const checkboxes = document.querySelectorAll(`input[name="${'correspondencia'}"]:checked`);
			var vIdPadre=0
			/*checkboxes.forEach((checkbox) => {
				    if (checkbox.getAttribute('data-sectId').length > 0){
				    	vIdPadre=checkbox.getAttribute('data-sectId');
				    }else{
				    	vIdPadre=0;
				    }
			        listCorrespondencia.push({
			        	correlativo: checkbox.value,
			        	idPadre: vIdPadre,
			        	workflowId: checkbox.getAttribute('data-sectWorkflowId')
			          }	
			        )
			});*/
			$("." + ref.modulo.classCheckAsignacionGrupal).each(function(){
				var corrInd = $(this);
				if(corrInd.prop('checked') == true){
					  if (corrInd.attr('data-sectId').length > 0){
					    	vIdPadre=corrInd.attr('data-sectId');
					    }else{
					    	vIdPadre=0;
					    }
				        listCorrespondencia.push({
				         	correlativo: corrInd.attr('value'),
				        	idPadre: vIdPadre,
				        	workflowId: corrInd.attr('data-sectWorkflowId')
				          }	
				        )
				}
			});
			
			var bandeja = ref.modulo.compTipoBandeja.val();
			ref.modulo.validarAsignacionGrupal(listCorrespondencia)
			.then(function(respuesta){
				if(respuesta.estado == true){  
					//document.location.href="../app/" + bandeja + "/asignacionGrupal";
					var idAsignacion;
					//let padre = listCorrespondencia[0].idPadre
		
					//if (listCorrespondencia[0].idPadre == "0"){
						idAsignacion =listCorrespondencia[0].correlativo;
					//}else{
					//	idAsignacion = listCorrespondencia[0].idPadre;
					//}
					document.location.href="../app/" + bandeja + "/asignacionGrupal/" + idAsignacion + "?workflow=" +listCorrespondencia[0].workflowId ;
				}else{
					ref.modSistcorr.notificar("ERROR",respuesta.mensaje, "Error");
				}
				
				ref.modSistcorr.procesar(false);
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
			
		},
		// TICKET 9000004044
		requiereRespuestaCompletar: function(){
			var ref = this;
			var checkDoc = ref.modulo.compChkRequiereDocumentoCompletar.prop('checked');
			return checkDoc;
		},
		
		requiereRespuestaCerrar: function(){
			var ref = this;
			var checkDoc = ref.modulo.compChkRequiereDocumentoCerrar.prop('checked');
			return checkDoc;
		},
		
		tieneDocumentoRespuestaEnCursoCorrespondencia: function(){
			var ref = this;
			ref.modulo.validarExisteDocumentoRespuestaCorrespondencia(ref.correlativoSeleccionado)
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
			ref.modulo.validarExisteDocumentoRespuestaAsignacion((ref.correspondenciaRechazarAsignacion?(ref.correspondenciaRechazarAsignacion.idAsignacion):(ref.correspondenciaPorCompletar.idAsignacion)))
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
					var nroDoc = ref.modulo.compTxtNumeroDocumentoCerrar.val();
					if(nroDoc == ""){
						sessionStorage.setItem("urlOrigen", "bandejaEntrada");
						sessionStorage.setItem("accionPrecedente", "cerrarCorrespondencia");
						sessionStorage.setItem("correlativo", ref.correspondenciaPorCerrar.correlativo);
						sessionStorage.setItem("comentario", ref.modulo.compComentarioCerrarCorrespondencia.val() || '');
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
								var comentario = ref.modulo.compComentarioCerrarCorrespondencia.val() || '';
								var correspondencia = {'numeroDocumento': nroDoc, 'correlativo': ref.correspondenciaPorCerrar.correlativo, 'idAsignacion': 0, 'tipoAccion': '', 'respuesta': comentario};
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
					var nroDoc = ref.modulo.compTxtNumeroDocumentoCompletar.val();
					if(nroDoc == ""){
						var comentario = ref.modulo.compComentarioCompletarCorrespondencia.val() || '';
						var acciones = ref.modSistcorr.getAcciones();
						var accionCorrespondencia;
						for(var i in acciones){
							var accion = acciones[i];
							if(accion.codigoAccion == ref.correspondenciaPorCompletar.codigoAccion){
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
						sessionStorage.setItem("correlativo", ref.correspondenciaPorCompletar.correlativo);
						sessionStorage.setItem("comentario", ref.modulo.compComentarioCompletarCorrespondencia.val() || '');
						sessionStorage.setItem("idAsignacion", ref.correspondenciaPorCompletar.idAsignacion);
						sessionStorage.setItem("tipoAccion", ref.correspondenciaPorCompletar.tipoAccion);
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
										var comentario = ref.modulo.compComentarioCompletarCorrespondencia.val() || '';
										var acciones = ref.modSistcorr.getAcciones();
										var accionCorrespondencia;
										for(var i in acciones){
											var accion = acciones[i];
											if(accion.codigoAccion == ref.correspondenciaPorCompletar.codigoAccion){
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
										var comentario = ref.modulo.compComentarioCompletarCorrespondencia.val() || '';
										var correspondencia = {'numeroDocumento': nroDoc, 'correlativo': ref.correspondenciaPorCompletar.correlativo, 'idAsignacion': ref.correspondenciaPorCompletar.idAsignacion, 'tipoAccion': ref.correspondenciaPorCompletar.tipoAccion, 'respuesta': comentario};
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
					if(accionbtn == "completarcorrespondencia")//TICKET 9000004273
						ref.completarCorrespondencia();
					else ref.rechazarAsignacionCorrespondencia();
				}
			}else{
				ref.modSistcorr.procesar(false);
				if(accionbtn == "rechazarasignacion")//TICKET 9000004273
					ref.modSistcorr.notificar("ERROR", "No se puede realizar el rechazo de la correspondencia debido a que tiene un documento respuesta en curso.", "Error");//TICKET 9000004273
				else ref.modSistcorr.notificar("ERROR", "La asignación que intenta Completar tiene un documento respuesta en curso", "Error");
			}
		},
		// FIN TICKET
		
		validarCamposModalBusqueda: function(){
			var ref = this;
			if(ref.modulo.compTipoBandeja.val() == "DelGestor"){
				ref.modulo.compBusqueda.dependenciaDestino.closest('div').show();
				ref.modulo.compBusqueda.tipoAccion.closest('div').hide();
			}else{
				ref.modulo.compBusqueda.dependenciaDestino.closest('div').hide();
				ref.modulo.compBusqueda.tipoAccion.closest('div').show();
			}
			
			//inicio ticket 9000003866
			if(((ref.modulo.compTipoBandeja.val() == "Pendientes" || 
					ref.modulo.compTipoBandeja.val() == "EnAtencion" || 
					ref.modulo.compTipoBandeja.val() == "Completadas") && 
					(ref.rol_jefe == true || ref.rol_gestor == true))){
				ref.modulo.compBusqueda.dependenciaDestinoBPAC.closest('div').show();
				
				ref.modulo.compBusqueda.dependenciaDestinoBPAC.select2({
					
				}).on('select2:select', function(event){
					var $comp = $(event.currentTarget);
	            	ref.modSistcorr.select2_change($comp.attr('id'))
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
			}
			//fin ticket 9000003866
		},
		
		
		abrirModalBusqueda: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
				ref.modulo.compBusqueda.dependenciaDestino.val(ref.dependenciaDefecto);
			}
			//inicio ticket 9000003866
			if(((ref.modulo.compTipoBandeja.val() == "Pendientes" || 
					ref.modulo.compTipoBandeja.val() == "EnAtencion" || 
					ref.modulo.compTipoBandeja.val() == "Completadas") && 
					(ref.rol_jefe == true || ref.rol_gestor == true))){
				
				var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
				
				if(dependencias.length==1 && !ref.searchDefDDest){
					var valor = "";
					for(var index in dependencias){
						valor = dependencias[index].codigo;
					}
					ref.modulo.compBusqueda.dependenciaDestinoBPAC.val(valor);
					ref.modulo.compBusqueda.dependenciaDestinoBPAC.change();
				}else{
					ref.modulo.compBusqueda.dependenciaDestinoBPAC.val(ref.dependenciaDefecto);
					ref.modulo.compBusqueda.dependenciaDestinoBPAC.change();
				}
				
			}
			//fin ticket 9000003866
			ref.modulo.compBusqueda.asustoCorrespondencia.val("");
			ref.modulo.compBusqueda.tipoAccion.val("");
			ref.modulo.compBusqueda.remitente.val("");
			ref.modulo.compBusqueda.correlativo.val("");
			ref.modulo.compBusqueda.numeroDocumento.val("");
			ref.modulo.compBusqueda.fechaDesde.val("");
			ref.modulo.compBusqueda.fechaHasta.val("");
			if(ref.filtrosBusqueda.length > 0){
				for(var i in ref.filtrosBusqueda){
					
					var field = ref.filtrosBusqueda[i];
					if(field.fieldId == 'TD_sAsunto'){
						ref.modulo.compBusqueda.asustoCorrespondencia.val(field.value);
						ref.modulo.compBusqueda.asustoCorrespondencia.change();
					} else 	if(field.fieldId == 'TD_sAccion'){
						ref.modulo.compBusqueda.tipoAccion.val(field.value);
						ref.modulo.compBusqueda.tipoAccion.change();
					} else if(field.fieldId == 'TD_sRemitente'){
						ref.modulo.compBusqueda.remitente.val(field.value);
						ref.modulo.compBusqueda.remitente.change();
					} else if(field.fieldId == 'TD_sCorrelativo'){
						ref.modulo.compBusqueda.correlativo.val(field.value);
						ref.modulo.compBusqueda.correlativo.change();
					} else if(field.fieldId == 'sNroDocInterno'){
						ref.modulo.compBusqueda.numeroDocumento.val(field.value);
						ref.modulo.compBusqueda.numeroDocumento.change();
					} else if(field.fieldId == 'TD_CodigoDepDest' && ref.modulo.compTipoBandeja.val()=="DelGestor"){
						ref.modulo.compBusqueda.dependenciaDestino.val(field.value);
						ref.modulo.compBusqueda.dependenciaDestino.change();
					}else if(field.fieldId == 'TD_CodigoDepDest' && ((ref.modulo.compTipoBandeja.val() == "Pendientes" || 
							ref.modulo.compTipoBandeja.val() == "EnAtencion" || ref.modulo.compTipoBandeja.val() == "Completadas") && 
							(ref.rol_jefe == true || ref.rol_gestor == true))){//inicio ticket 9000003866
						ref.modulo.compBusqueda.dependenciaDestinoBPAC.val(field.value);
						ref.modulo.compBusqueda.dependenciaDestinoBPAC.change();
					}//fin ticket 9000003866
					else if(field.fieldId == 'TD_tFechaReg' && field.operator == '>='){
						var date_ = new Date(null);
						date_.setTime(field.value*1000);
						var d = date_.getDate();
						if(d<10){
							d = '0'+ d;
						}
						var m = date_.getMonth()+1;
						if(m<10){
							m = '0' + m;
						}
						ref.modulo.compBusqueda.fechaDesde.val(d + "/" + m + "/" + date_.getFullYear());
						ref.modulo.compBusqueda.fechaDesde.change();
					} else if(field.fieldId == 'TD_tFechaReg' && field.operator == '<='){
						var date_ = new Date(null);
						date_.setTime(field.value*1000);
						var d = date_.getDate();
						if(d<10){
							d = '0'+ d;
						}
						var m = date_.getMonth()+1;
						if(m<10){
							m = '0' + m;
						}
						ref.modulo.compBusqueda.fechaHasta.val(d  + "/" + m + "/" + date_.getFullYear());
						ref.modulo.compBusqueda.fechaHasta.change();
					}
				}
			} /*else {
				if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
					ref.modulo.compBusqueda.dependenciaDestino.val(ref.modulo.dependenciaDefecto);
				}
				ref.modulo.compBusqueda.asustoCorrespondencia.val("");
				ref.modulo.compBusqueda.tipoAccion.val("");
				ref.modulo.compBusqueda.remitente.val("");
				ref.modulo.compBusqueda.numeroDocumento.val("");
			}*/
			ref.modulo.compBusqueda.myModal.modal('show');
			setTimeout(function() {
				console.log('focus');
				ref.modSistcorr.procesar(false);
				document.getElementById(ref.modulo.compBusqueda.asustoCorrespondencia.attr('id')).focus();
			}, 1500);
						
		},
		
		obtenerFiltrosBusqueda: function(){
			var ref = this;
			// TICKET 9000003862
			if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
				var dependenciaDestino = ref.modulo.compBusqueda.dependenciaDestino.val() || "";
				console.log("dependenciaDestino:" + dependenciaDestino);
				if(dependenciaDestino == ""){
					dependenciaDestino = ref.modulo.dependenciaDefecto;
					console.log("Dep Def:" + ref.modulo.dependenciaDefecto);
				}
				console.log("obtenerFiltrosBusqueda:" + dependenciaDestino);
			}
			// FIN TICKET
			// TICKET 9000003866
			if(((ref.modulo.compTipoBandeja.val() == "Pendientes" || ref.modulo.compTipoBandeja.val() == "EnAtencion" || ref.modulo.compTipoBandeja.val() == "Completadas") && 
					(ref.rol_jefe == true || ref.rol_gestor == true))){
				var dependenciaDestinoBPAC = ref.modulo.compBusqueda.dependenciaDestinoBPAC.val() || "";
				console.log("dependenciaDestinoBPAC:" + dependenciaDestinoBPAC);
				if(dependenciaDestinoBPAC == ""){
					dependenciaDestinoBPAC = ref.modulo.dependenciaDefecto;
					console.log("Dep Def:" + ref.modulo.dependenciaDefecto);
				}
				console.log("obtenerFiltrosBusqueda:" + dependenciaDestinoBPAC);
			}
			// FIN TICKET
			var asunto = ref.modulo.compBusqueda.asustoCorrespondencia.val() || "";
			var tipoAccion = ref.modulo.compBusqueda.tipoAccion.val() || "";
			var remitente = ref.modulo.compBusqueda.remitente.val() || "";
			var correlativo = ref.modulo.compBusqueda.correlativo.val() || "";
			var numeroDocumento = ref.modulo.compBusqueda.numeroDocumento.val() || "";
			var fechaDesde = ref.modulo.compBusqueda.fechaDesde.val() || "";
			var fechaHasta = ref.modulo.compBusqueda.fechaHasta.val() || "";
			var data = [];
			// TICKET 9000003862
			if(ref.modulo.compTipoBandeja.val()=="DelGestor"){
				if(dependenciaDestino.length > 0){
					data.push({'fieldId' : 'TD_CodigoDepDest', "operator": "=", "value": (dependenciaDestino || "").toUpperCase()});
				}
			}
			// FIN TICKET 
			// TICKET 9000003866
			if(((ref.modulo.compTipoBandeja.val() == "Pendientes" || ref.modulo.compTipoBandeja.val() == "EnAtencion" || ref.modulo.compTipoBandeja.val() == "Completadas") && 
					(ref.rol_jefe == true || ref.rol_gestor == true))){
				if(dependenciaDestinoBPAC.length > 0){
					data.push({'fieldId' : 'TD_CodigoDepDest', "operator": "=", "value": (dependenciaDestinoBPAC || "").toUpperCase()});
				}
			}
			// FIN TICKET
			if(asunto.length > 0){
				data.push({'fieldId' : 'TD_sAsunto', "operator": "%LIKE%", "value": (asunto || "").toUpperCase()});
			}
			if(tipoAccion.length > 0){
				data.push({'fieldId' : 'TD_sAccion', "operator": "=", "value": tipoAccion || ""});
			}
			if(remitente.length > 0){
				data.push({'fieldId': 'TD_sRemitente', "operator": "%LIKE%", "value": (remitente || "").toUpperCase()});
			}
			if(correlativo.length > 0){
				data.push({'fieldId' : 'TD_sCorrelativo', "operator": "=", "value": (correlativo || "").toUpperCase()});
			}
			if(numeroDocumento.length > 0){
				data.push({'fieldId' : 'sNroDocInterno', "operator": "%LIKE%", "value": (numeroDocumento || "").toUpperCase()});
			}
			if(fechaDesde.length > 0){
				var datos = fechaDesde.split("/");
				var date_ = new Date(datos[2], datos[1] - 1, datos[0]).getTime() / 1000;
				data.push({'fieldId': 'TD_tFechaReg', 'operator': '>=', 'value': date_});
			}
			if(fechaHasta.length > 0){
				var datos = fechaHasta.split("/");
				var date_ = new Date(datos[2], datos[1] - 1, datos[0]).getTime() / 1000;
				data.push({'fieldId': 'TD_tFechaReg', 'operator': '<=', 'value': date_});
			}
			console.log(data);
			ref.filtrosBusqueda = data;
			ref.modSistcorr.setFiltros(data, ref.modulo.compTipoBandeja.val());
			console.log("Asignado a sesion:" + ref.modulo.compTipoBandeja.val())
		},
		
		
		buscarCorrespondenciasAsignaciones: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.modulo.compVerMas.hide();
			ref.modulo.buscarCorrespondenciasAsignaciones(ref.filtrosBusqueda)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.bandeja.correspondencias = respuesta.datos[0].detalleCorrespondencias;
					ref.itemsPorPagina = respuesta.datos[0].totalItemxPagina;
					ref.totalCorrespondencias = respuesta.datos[0].totalTareas;
					ref.respuestaBusqueda = respuesta.datos[0];
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				//ref.mostrarBandeja();
				console.log("MOSTRAR BANDEJA");
				ref.pagina = 0;
				ref.cantidad = ref.bandeja.correspondencias.length;
				//ref.total = ref.bandeja.correspondencias.length;
				ref.total = respuesta.datos[0].totalTareas;
				ref.sessionPaginado = respuesta.datos[0].continuationData;
				var sessTemp = ref.sessionPaginado;
				for(var i = sessTemp.length - 1; i >= 0; i--){
					var filaSess = sessTemp[i];
					var cd = filaSess.continuationData;
					if(cd == null){
						sessTemp.splice(i, 1);
					}
				}
				ref.sessionPaginado = sessTemp;
				ref.modulo.compListaCorrespondencia.empty();
				$("." + ref.modulo.cantidadTotales).html("");
				ref.mostrarBandeja2();
				ref.modSistcorr.procesar(false);
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
			ref.modulo.compBusqueda.myModal.modal('hide');
		},
		
		//INICIO TICKET 9000004807
		exportarExcelCorrespondenciasAsignaciones: function(){
			var ref = this;
			
			if(ref.filtrosBusqueda != undefined && ref.filtrosBusqueda != null && ref.filtrosBusqueda.length > 0 && $(".col_tarea").length <= ref.limit_export_be_advert ){
				ref.modSistcorr.procesar(true);	
				
				ref.modulo.exportarExcelCorrespondenciasAsignaciones(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_bandeja_entrada.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_bandeja_entrada.xlsx';
	                    document.body.appendChild(a);
	                    a.click();
	                    document.body.removeChild(a);
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			}else{
				ref.modulo.compModalAdvertenciaExportar.modal('show');
			}
		},
		
		exportarExcelCorrespondenciasAsignacionesTodo: function(){
			var ref = this;
			
			ref.modSistcorr.procesar(true);	
			
			ref.modulo.exportarExcelCorrespondenciasAsignaciones(ref.filtrosBusqueda)
			.then(function(respuesta){
				
				if (navigator.appVersion.toString().indexOf('.NET') > 0){
					window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_bandeja_entrada.xlsx');
				} else {
					var a = document.createElement('a');
                    a.href = URL.createObjectURL(respuesta);
                    a.download = 'reporte_bandeja_entrada.xlsx';
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
				}
				setTimeout(function() {
					ref.modulo.compModalAdvertenciaExportar.modal('hide');
					ref.modSistcorr.procesar(false);
				}, 500);
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		//FIN TICKET 9000004807
		
		obtenerBandeja: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.correspondencias = [];
			console.log("Obtener filtros busqueda");
			ref.filtrosBusqueda = ref.modSistcorr.getFiltros(ref.modulo.compTipoBandeja.val()) || [];
			
			console.log("BUSCAR");
			ref.mostrarFiltros();
			console.log("Inicio de Petición");
			console.log(new Date());
			ref.buscarCorrespondenciasAsignaciones();
		
			/*if(ref.filtrosBusqueda.length == 0){
				console.log("LISTAR");
				ref.modulo.listarCorrespondenciasAsignaciones()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.bandeja = respuesta.datos[0];
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.mostrarBandeja();
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					console.log(error);
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			} else{
				console.log("BUSCAR");
				ref.mostrarFiltros();
				ref.buscarCorrespondenciasAsignaciones();
			}*/
			
			
		},
		
		mostrarBandeja: function(){
			var ref = this;
			console.log("Inicio renderizado");
			console.log(new Date());
			ref.mostrarCorrespondencias(ref.bandeja.correspondencias);
			ref.mostrarAsignaciones(ref.bandeja.asignaciones);
			if(ref.bandeja.correspondencias.length == 0){
				ref.modulo.compSinResultados.show();
			} else {
				ref.modulo.compSinResultados.hide();
			}
			console.log("Fin renderizado");
			console.log(new Date());
		},
		
		mostrarBandeja2: function(){
			var ref = this;
			//ref.mostrarCorrespondencias(ref.bandeja.correspondencias.slice(ref.pagina*ref.cantidad, (ref.pagina+1)*ref.cantidad));
			//ref.mostrarCorrespondencias(ref.bandeja.correspondencias.slice(ref.cantPrevia, ref.cantidad));
			ref.mostrarCorrespondencias(ref.bandeja.correspondencias.slice(ref.pagina*ref.itemsPorPagina, (ref.pagina+1)*ref.itemsPorPagina));
			ref.modulo.compVerMas.show();
			ref.pagina++;
			ref.modulo.compSinResultados.hide();
			//var cantidadMostrados = ref.pagina*ref.cantidad;
			//var cantidadMostrados = ref.cantidad;
			if(ref.pagina * ref.itemsPorPagina >= ref.bandeja.correspondencias.length){
				ref.totalMostrados = ref.bandeja.correspondencias.length;
			}else{
				ref.totalMostrados = ref.pagina * ref.itemsPorPagina;
			}
			if(ref.totalMostrados > ref.total){
				totalMostrados = ref.total;
			}
			$("." + ref.modulo.cantidadTotales).html("Mostrando " + ref.totalMostrados + " de " + ref.totalCorrespondencias);
			if(ref.totalMostrados >= ref.totalCorrespondencias){
				ref.modulo.compVerMas.hide();
			}
			if(ref.bandeja.correspondencias.length == 0){
				ref.modulo.compSinResultados.show();
			} else {
				ref.modulo.compSinResultados.hide();
			}
			ref.modSistcorr.procesar(false);
			// TICKET 9000004270
			//ref._inicializarEventosCorrespondencia();
			setTimeout(function() {
				var _idSeleccionado = ref.modSistcorr.getBandejaEntradaFirmaSeleccionada();
				if(_idSeleccionado != ""){
					$("#CORRESPONDENCIA_" + _idSeleccionado).addClass("tarea_seleccionda");
					$("#CORRESPONDENCIA_FOOTER_" + _idSeleccionado).css("display", "block");
					ref.workflowIdSeleccionado = _idSeleccionado;
					$("#CORRESPONDENCIA_BODY_" + _idSeleccionado).click();
				}
			}, 1000);
			// FIN TICKET
		},
		
		// TICKET 9000004270
		_inicializarEventosCorrespondencia: function(){
			var ref = this;
			$("."+ ref.modulo.classCompCorrespondencia).click(function(event){
				var $columnaCorrespondencia = $(event.currentTarget);
				var seleccionado = $columnaCorrespondencia.data("workflowid");
				
				if(!ref.workflowIdSeleccionado){
					$("#CORRESPONDENCIA_" + seleccionado).addClass("tarea_seleccionda");
					$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css("display", "block");
					ref.workflowIdSeleccionado = seleccionado;
				} else{
					var isVisibleFooter = $("#CORRESPONDENCIA_FOOTER_" + seleccionado).css("display") == "block";
					$("." + ref.modulo.classTareaFooter).css("display", "none");
					if(!isVisibleFooter){
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css('display', 'block');
					}else {
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css('display', 'none');
					}
					$("#CORRESPONDENCIA_" +  ref.correspondenciaSeleccionada).removeClass('correspondencia_seleccionda');
	        		$("#CORRESPONDENCIA_" +  seleccionado).addClass('tarea_seleccionda');
	        		$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css("display", "block");
					ref.workflowIdSeleccionado = seleccionado;
				}
				ref.modSistcorr.setBandejaEntradaFirmaSeleccionada(ref.workflowIdSeleccionado);
			});
			
			//ref.iniciarlizarEventosOpciones();
		},
		// FIN TICKET
		
		mostrarCorrespondencias: function(correspondencias){
			var ref = this;
			//ref.modulo.compListaCorrespondencia.empty();
			var contenidoHTML = ref.modulo.htmlCorrespondencias(correspondencias, ref.rol_jefe, ref.rol_gestor);
			//console.log("CONTENIDO HTML:")
			//console.log(contenidoHTML);
			//ref.modulo.compListaCorrespondencia.html(contenidoHTML);
			ref.modulo.compListaCorrespondencia.append(contenidoHTML);
			ref.eventoSeleccionarCorrespondencia();
			ref.textoMas.inicializar();
		},
		
		mostrarAsignaciones: function(asignaciones){
			var ref = this;
			ref.modulo.compListaAsignaciones.empty();
			var contenidoHTML = ref.modulo.htmlAsignaciones(asignaciones, ref.rol_jefe);
			ref.modulo.compListaAsignaciones.html(contenidoHTML);
			ref.eventoSeleccionarAsignacion();
			ref.modSistcorr.eventoTooltip();
			ref.textoMas.inicializar();
		},
		
		eventoSeleccionarCorrespondencia: function(){
			var ref = this;
			
			$('.'+ref.modulo.classCompCorrespondencia).off('click').on('click', function(event){
				if(ref.asignacionSeleccionada){
					$("#ASIGNACION_" +  ref.asignacionSeleccionada).removeClass('tarea_seleccionda');
				}
				// TICKET 9000004270
				if(ref.workflowIdSeleccionado){
					$("#CORRESPONDENCIA_" +  ref.workflowIdSeleccionado).removeClass('tarea_seleccionda');
					$("#CORRESPONDENCIA_FOOTER_" + ref.workflowIdSeleccionado).css('display', 'none');
				}
				// FIN TICKET
				
				var $columnaTarea = $(event.currentTarget);
				var tipoSeleccionado = $columnaTarea.data("tipoIcono");
				var seleccionado = $columnaTarea.data("workflowid");
				var correlativoSeleccionado = $columnaTarea.data("correlativo");
				var idAsignacionSeleccionado = $columnaTarea.data("idasignacion");
				var esDeEmision = $columnaTarea.data("esdeemision");
				var nroDocumento = $columnaTarea.data("nrodocumento");
				var correlativo = $columnaTarea.data("correlativo");
				
				if(!ref.correspondenciaSeleccionada){
					$("#CORRESPONDENCIA_" + seleccionado).addClass("tarea_seleccionda");
					$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css("display", "block");
					ref.correspondenciaSeleccionada = seleccionado;
					ref.esDeEmisionSeleccionada = esDeEmision;
					ref.numeroDocumentoSeleccionado = nroDocumento;
					ref.correlativoSeleccionado = correlativo;
				}else{
					var isVisibleFooter = $("#CORRESPONDENCIA_FOOTER_" + seleccionado).css("display") == "block";
					$('.'+ref.modulo.classTareaFooter).css("display", "none");
					if(!isVisibleFooter){
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css('display', 'block');
					}else {
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado).css('display', 'none');
					}
					$("#CORRESPONDENCIA_" +  ref.correspondenciaSeleccionada).removeClass('tarea_seleccionda');
	        		$("#CORRESPONDENCIA_" +  seleccionado).addClass('tarea_seleccionda');
					ref.correspondenciaSeleccionada = seleccionado;
					ref.esDeEmisionSeleccionada = esDeEmision;
					ref.numeroDocumentoSeleccionado = nroDocumento;
					ref.correlativoSeleccionado = correlativo;
				}
				ref.workflowIdSeleccionado = seleccionado;
				ref.modSistcorr.setBandejaEntradaFirmaSeleccionada(ref.workflowIdSeleccionado);
			});
			
			
			$("."+ref.modulo.compAbrirCerrarCorrespondencia).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				var workflowId = $comp.data("workflowid");
				ref.correspondenciaPorCerrar = {
						'correlativo' : 	correlativo	,
						'workflowId'  :		workflowId
				};
				console.log("correspondencia por cerrar:");
				console.log(ref.correspondenciaPorCerrar);
				ref.abrirModalCerrarCorrespondencia();
			});
			
			$("."+ref.modulo.compAbrirAceptarCorrespondencia).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				ref.correspondenciaPorAceptar = {
						'correlativo' : 	correlativo
				};
				ref.abrirModalAceptarCorrespondencia();
			});
			
			$("."+ref.modulo.compAbrirRechazarCorrespondencia).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				ref.correspondenciaPorRechazar = {
						'correlativo' : 	correlativo
				};
				ref.abrirModalRechazarCorrespondencia();
			});
			
			$("."+ref.modulo.compAbrirCompletarCorrespondencia).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				var idAsignacion = $comp.data("idasigancion");
				var codigoAccion = $comp.data("accion");
				var workflowId = $comp.data("workflowid");
				var tipoAccion = $comp.data("tipoaccion");
				ref.correspondenciaPorCompletar = {
					'correlativo' : 	correlativo,
					'idAsignacion':	idAsignacion,
					'codigoAccion' : codigoAccion,
					'workflowId'  :		workflowId,
					'tipoAccion'  :		tipoAccion
				};
				ref.abrirModalCompletarCorrespondencia();
			});
			
			/*INICIO TICKET 9000004273*/
			$("."+ref.modulo.compAbrirRechazarAsignacionCorresp).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				var idAsignacion = $comp.data("idasigancion");
				var codigoAccion = $comp.data("accion");
				var workflowId = $comp.data("workflowid");
				var tipoAccion = $comp.data("tipoaccion");
				ref.correspondenciaRechazarAsignacion = {
					'correlativo' : 	correlativo,
					'idAsignacion':	idAsignacion,
					'codigoAccion' : codigoAccion,
					'workflowId'  :		workflowId,
					'tipoAccion'  :		tipoAccion
				};
				ref.abrirModalRechazarAsigCorrespondencia();
			});
			/*FIN TICKET 9000004273*/
			$(ref.modulo.classVerificarDocumentoPrincipalCorrespondencia).click(function(){
				var $comp = $(this);
				var correlativo = $comp.data("correlativo");
				ref.verificarDocumentoPrincipal(correlativo);
			});
			
			$(ref.modulo.classVerificarDocumentoPrincipalAsignacion).click(function(){
				var $comp = $(this);
				var idAsignacion = $comp.data("idasigancion");
				var correlativo = $comp.data("correlativo");
				ref.verificarDocumentoPrincipal(correlativo);
			});
			
			
			/*INICIO TICKET 9000004497*/
			/*$("."+ref.modulo.compActivarSeleccionMultiple).click(function(){
				const checkboxes = document.querySelectorAll(`input[name="${'correspondencia'}"]:checked`);
				if (checkboxes.length > 1) {
					$('#btnAsignacionGrupal').show();
				}else{
					$('#btnAsignacionGrupal').hide();
				}
			});*/
			$("." + ref.modulo.classCheckAsignacionGrupal).change(function(event){
				var corrSelec = 0;
				$("." + ref.modulo.classCheckAsignacionGrupal).each(function(){
					var corrInd = $(this);
					if(corrInd.prop('checked') == true){
						corrSelec++;
						console.log("correspondencia: " + corrInd.attr('value'));
					}
				});
				console.log("SELECCIONADOS: " + corrSelec);
				if(corrSelec > 1){
					ref.modulo.btnAsignacionGrupal.show();
				}else{
					ref.modulo.btnAsignacionGrupal.hide();
				}
			});
			/*FIN*/
			
		},
		
		eventoSeleccionarAsignacion: function(){
			var ref = this;
			$('.'+ref.modulo.classCompAsignacion).click(function(event){
				if(ref.correspondenciaSeleccionada){
					$("#CORRESPONDENCIA_" +  ref.correspondenciaSeleccionada).removeClass('tarea_seleccionda');
				}
				var $columnaTarea = $(event.currentTarget);
				var seleccionado = $columnaTarea.data("idasigancion");
				var esDeEmision = $columnatarea.data("esdeemision"); 
				
				if(!ref.asignacionSeleccionada){
					$("#ASIGNACION_" + seleccionado).addClass("tarea_seleccionda");
					$("#ASIGNACION_FOOTER_" + seleccionado).css("display", "block");
					ref.asignacionSeleccionada = seleccionado;
					ref.esDeEmisionSeleccionada = esDeEmision;
				}else{
					var isVisibleFooter = $("#ASIGNACION_FOOTER_" + seleccionado).css("display") == "block";
					$('.'+ref.modulo.classTareaFooter).css("display", "none");
					if(!isVisibleFooter){
						$("#ASIGNACION_FOOTER_" + seleccionado).css('display', 'block');
					}else {
						$("#ASIGNACION_FOOTER_" + seleccionado).css('display', 'none');
					}
					$("#ASIGNACION_" +  ref.asignacionSeleccionada).removeClass('tarea_seleccionda');
	        		$("#ASIGNACION_" +  seleccionado).addClass('tarea_seleccionda');
					ref.asignacionSeleccionada = seleccionado;
					ref.esDeEmisionSeleccionada = esDeEmision;
				}
			});
			
			
			
			
		},
		
		actualizarCombosDependenciaDestino: function(){
			var ref = this;
			var dependencias = ref.modSistcorr.getDependenciasDestino();
			var selected = false;
			if(dependencias.length==1){
				selected = true;
			}
			ref.modulo.compBusqueda.dependenciaDestino.empty();
			if(selected){
				ref.modulo.compBusqueda.dependenciaDestino.append("<option value=''>Seleccione</option>");
			}else{
				ref.modulo.compBusqueda.dependenciaDestino.append("<option selected value=''>Seleccione</option>");
			}
			var val = 0;
			for(var index in dependencias){
				var dependencia = dependencias[index];
				if(selected){
					val = dependencia.codigo;
					ref.modulo.compBusqueda.dependenciaDestino.append("<option value='"+dependencia.codigo+"' selected='selected'>" + dependencia.descripcion + "</option>");
				}else{
					ref.modulo.compBusqueda.dependenciaDestino.append("<option value='"+dependencia.codigo+"'>" + dependencia.descripcion + "</option>");
				}
			}
			if(selected){
				ref.modulo.dependenciaDefecto = val;
			}
			
			ref.modulo.compBusqueda.dependenciaDestino.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
		},
		
		//INICIO TICKET 9000003866
		actualizarCombosDependenciaDestinoBPAC: function(){
			var ref = this;
			var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
			var selected = false;
			if(dependencias.length==1){
				selected = true;
			}
			var val = 0;
			for(var index in dependencias){
				var dependencia = dependencias[index];
				if(selected){
					val = dependencia.codigo;
				}
			}
			if(selected){
				ref.modulo.dependenciaDefecto = val;
			}
			
		},
		//FIN TICKET 9000003866
		
		actualizarCombosTipoAccion: function(){
			var ref = this;
			var acciones = ref.modSistcorr.getAcciones();
			ref.modulo.compBusqueda.tipoAccion.empty();
			ref.modulo.compBusqueda.tipoAccion.append("<option selected value=''>Seleccione</option>");
			for(var index in acciones){
				var accion = acciones[index];
				ref.modulo.compBusqueda.tipoAccion.append("<option value='"+accion.accion+"'>" + accion.accion + "</option>");
			}
			
			ref.modulo.compBusqueda.tipoAccion.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
		},
		
		abrirModalCompletarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compComentarioCompletarCorrespondencia.val("");
			// TICKET 900004044
			ref.modulo.compChkRequiereDocumentoCompletar.prop('checked', true);
			ref.modulo.compTxtNumeroDocumentoCompletar.removeAttr('readonly');
			ref.modulo.compTxtNumeroDocumentoCompletar.val("");
			// FIN TICKET
			ref.modulo.compModalCompletarCorrespondencia.modal('show');
			setTimeout(function() {
				// TICKET 9000004044
				//ref.modulo.compComentarioCompletarCorrespondencia.focus();
				ref.modulo.compTxtNumeroDocumentoCompletar.focus();
				// FIN TICKET
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		abrirModalRechazarAsigCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compTxtMotivoRechazarAsignacionCorres.val("");
			
			ref.modulo.compModalRechazarAsignacionCorres.modal('show');
			setTimeout(function() {
				ref.modulo.compTxtMotivoRechazarAsignacionCorres.focus();
				ref.modSistcorr.procesar(false);
			}, 500);
			
		},
		
		abrirModalCerrarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compComentarioCerrarCorrespondencia.val("");
			// TICKET 9000004044
			ref.modulo.compChkRequiereDocumentoCerrar.prop('checked', true);
			ref.modulo.compTxtNumeroDocumentoCerrar.removeAttr('readonly');
			ref.modulo.compTxtNumeroDocumentoCerrar.val("");
			// FIN TICKET
			ref.modulo.compModalCerrarCorrespondencia.modal('show');
			setTimeout(function() {
				// TICKET 9000004044
				//ref.modulo.compComentarioCerrarCorrespondencia.focus();
				ref.modulo.compTxtNumeroDocumentoCerrar.focus();
				// FIN TICKET
				ref.modSistcorr.procesar(false);
			}, 500);
			
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
		
		verificarDocumentoPrincipal: function(correlativo){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.obtenerInformacionDocumentoPrincipal(correlativo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						var informacion = respuesta.datos[0];
						var tamanio = informacion.tamano;
						tamanio = tamanio.replace(" MB", "");
						tamanio = tamanio.trim();
						tamanio = Number(tamanio) || 0;
						var tamanioMax = ref.tamanioMaxArchivo;
						if(tamanio > tamanioMax){
							ref.modulo.compModalDescargarArchivo.modal('show');
							ref.modSistcorr.procesar(false);
							ref.modulo.compBtnDescargarArchivo.click(function(){
								ref.modulo.abrirDocumento(informacion.id);
								ref.modulo.compModalDescargarArchivo.modal('hide')
							});
						} else{
							ref.modSistcorr.procesar(false);
							ref.modulo.abrirDocumento(informacion.id);
						}
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			
		},
		
		completarCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaPorCompletar){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener informacide la correspondencia", "Warning");
				return;
			}
			var comentario = ref.modulo.compComentarioCompletarCorrespondencia.val() || '';
			var acciones = ref.modSistcorr.getAcciones();
			var accionCorrespondencia;
			for(var i in acciones){
				var accion = acciones[i];
				if(accion.codigoAccion == ref.correspondenciaPorCompletar.codigoAccion){
					accionCorrespondencia = accion;
					break;
				}
			}
			if(!accionCorrespondencia){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "Correspondencia no tiene tipo de acción", "Warning");
				return;
			}
			if(accionCorrespondencia.requiereRespuesta == 'SI' && comentario.length == ''){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "Ingrese texto de respuesta", "Warning");
				ref.modulo.compComentarioCerrarCorrespondencia.focus();
				return;
			}
			ref.modSistcorr.procesar(true);
			var documentoRespuesta = ref.modulo.compTxtNumeroDocumentoCompletar.val();
			ref.modulo.completarCorrespondencia(ref.correspondenciaPorCompletar.idAsignacion, comentario, documentoRespuesta)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						$('#CORRESPONDENCIA_' + ref.correspondenciaPorCompletar.workflowId).remove();
						var mensajes = respuesta.mensaje;
						mensajes = respuesta.mensaje.split("|||");
						for(var i=0;i<mensajes.length;i++){
							ref.modSistcorr.notificar("OK", mensajes[i], "Success");
						}
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalCompletarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		//INICIO TICKET 9000004273
		rechazarAsignacionCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaRechazarAsignacion){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener información de la correspondencia", "Warning");
				return;
			}
			var motivo = ref.modulo.compTxtMotivoRechazarAsignacionCorres.val() || '';
			var acciones = ref.modSistcorr.getAcciones();
			var accionCorrespondencia;
			for(var i in acciones){
				var accion = acciones[i];
				if(accion.codigoAccion == ref.correspondenciaRechazarAsignacion.codigoAccion){
					accionCorrespondencia = accion;
					break;
				}
			}
			if(!accionCorrespondencia){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "Correspondencia no tiene tipo de acción", "Warning");
				return;
			}
			if(motivo.length == ''){//accionCorrespondencia.requiereRespuesta == 'SI' && motivo.length == ''
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", "Ingrese motivo de rechazo", "Warning");
				ref.modulo.compTxtMotivoRechazarAsignacionCorres.focus();
				return;
			}
			ref.modSistcorr.procesar(true);
			
			ref.modulo.rechazarAsignacionCorrespondencia(ref.correspondenciaRechazarAsignacion.idAsignacion, motivo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						$('#CORRESPONDENCIA_' + ref.correspondenciaRechazarAsignacion.workflowId).remove();
						/*var mensajes = respuesta.mensaje;
						mensajes = respuesta.mensaje.split("|||");
						for(var i=0;i<mensajes.length;i++){
							ref.modSistcorr.notificar("OK", mensajes[i], "Success");
						}*/
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalRechazarAsignacionCorres.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		//FIN TICKET 9000004273
		
		cerrarCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaPorCerrar){
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener información de la correspondencia", "Warning");
				return;
			}
			var comentario = ref.modulo.compComentarioCerrarCorrespondencia.val() || '';
			ref.modSistcorr.procesar(true);
			var documentoRespuesta = ref.modulo.compTxtNumeroDocumentoCerrar.val();
			ref.modulo.cerrarCorrespondencia(ref.correspondenciaPorCerrar.correlativo, comentario, documentoRespuesta)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						$('#CORRESPONDENCIA_' + ref.correspondenciaPorCerrar.workflowId).remove();
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalCerrarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		aceptarCorrespondencia: function(){
			var ref = this;
			if(!ref.correspondenciaPorAceptar){
				ref.modSistcorr.notificar("ERROR", "No se pudo obtener información de la correspondencia", "Warning");
				return;
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.aceptarCorrespondencia(ref.correspondenciaPorAceptar.correlativo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						// TICKET 9000003997
						if(ref.esDeEmisionSeleccionada=="SI"){
							ref.modulo.procesarAceptarCorrespondencia(ref.numeroDocumentoSeleccionado, ref.correlativoSeleccionado)
								.then(function(respuesta){
									if(respuesta.estado == true){
										if(respuesta.mensaje != ""){
											ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
										}
									}else{
										ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
									}
								}).catch(function(error){
									ref.modSistcorr.procesar(false);
									ref.modSistcorr.showMessageErrorRequest(error);
								});
						}
						// FIN TICKET
						ref.obtenerBandeja();
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
			ref.modulo.rechazarCorrespondencia(ref.correspondenciaPorRechazar.correlativo, comentario)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						// TICKET 9000003997
						if(ref.esDeEmisionSeleccionada=="SI"){
							ref.modulo.procesarRechazarCorrespondencia(ref.numeroDocumentoSeleccionado, ref.correlativoSeleccionado, comentario)
								.then(function(respuesta){
									console.log(respuesta)
								}).catch(function(error){
									ref.modSistcorr.procesar(false);
									ref.modSistcorr.showMessageErrorRequest(error);
								});
						}
						// FIN TICKET
						ref.obtenerBandeja();
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
		
		mostrarErrores: function(){
			var ref = this;
			for(var i in ref.errores){
				ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");
			}
		},
		
		mostrarFiltros: function(){
			var ref = this;
			ref.modulo.compFiltrosBusqueda.empty();
			var contenidoHTML = ref.modulo.htmlFiltros(ref.filtrosBusqueda || []);
			ref.modulo.compFiltrosBusqueda.html(contenidoHTML);
			ref.modSistcorr.eventoTooltip();
			$('.icon_eliminar_filtro').click(function(){
				var $comp = $(this);
				var field = $comp.data("field");
				$('#icon_filtro_'+field).tooltip('hide');
				console.log(ref.filtrosBusqueda);
				for(var i in ref.filtrosBusqueda){
					if(ref.filtrosBusqueda[i].fieldId == field){
						ref.filtrosBusqueda.splice(i, 1); 
					}
					if(field=="TD_CodigoDepDest"){
						ref.dependenciaDefecto = "";
					}
				}
				console.log(ref.filtrosBusqueda);
				ref.modSistcorr.setFiltros(ref.filtrosBusqueda, ref.modulo.compTipoBandeja.val());
				ref.buscarCorrespondenciasAsignaciones();
				$('#filtro_'+field).remove();
			});
		},
};

setTimeout(function(){
	CORRESPONDENCIA_VISTA.acciones = LISTA_ACCIONES;
	CORRESPONDENCIA_VISTA.dependencias = LISTA_DEPENDENCIAS;
	CORRESPONDENCIA_VISTA.dependenciasBPAC = LISTA_DEPENDENCIAS_BPAC;//TICKET 9000003866
	CORRESPONDENCIA_VISTA.rol_jefe = ES_JEFE;
	CORRESPONDENCIA_VISTA.rol_gestor = ES_GESTOR;
	CORRESPONDENCIA_VISTA.modulo = modulo_correspondecia;
	CORRESPONDENCIA_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_VISTA.tamanioMaxArchivo = TAMANIO_MAX_ARCHIVO;
	CORRESPONDENCIA_VISTA.limit_export_be_advert = VALOR_LIM_MOSTRAR_MSJ_EXPORT_BE;
	CORRESPONDENCIA_VISTA.errores = [];
	CORRESPONDENCIA_VISTA.textoMas = utilitario_textLarge;
	CORRESPONDENCIA_VISTA.inicializar();
}, 500);