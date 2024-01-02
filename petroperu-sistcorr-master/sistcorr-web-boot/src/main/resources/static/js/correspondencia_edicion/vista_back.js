var modulo_correspondencia_edicion = MODULO_CORRESPONDENCIA_EDICION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_EDICION_VISTA = {
		modSistcorr: modulo_sistcorr,
		modulo: null,
		correspondencia: {},
		adjuntos: [],
		listaArchivosAdjuntos:[],
		listaCopias: [],
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.obtenerCorrespondencia();		
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.tabs.tabDatos.compHeader.addClass('petro-tabs-activo');
			ref.modulo.tabs.tabDatos.compBody.show();
			ref.modulo.tabs.tabDestinatario.compBody.hide();
			ref.modulo.tabs.tabCopias.compBody.hide();
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				if (!e.target.dataset.tab) {
			        return;
			    }
				if(ref.obtenerRegistroCorrespondencia() == false){
					return;
				}
				 var $tab = $(e.currentTarget);
				 $('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
				 $tab.addClass('petro-tabs-activo');
				 if (e.target.dataset.tab == "datos") {
				      $("#contenidoAccordionDatos").show();
				      $("#contenidoHistorialDestinatario").hide();
				      $("#contenidoCopias").hide();
				 } 
				    
				 if (e.target.dataset.tab == "destinatario") {
				     $("#contenidoAccordionDatos").hide();
				     $("#contenidoHistorialDestinatario").show();
				     $("#contenidoCopias").hide();
				     ref.obtenerRegistroCorrespondencia();
				 }
				    
				 if (e.target.dataset.tab == "copias") {
				     $("#contenidoAccordionDatos").hide();
				     $("#contenidoHistorialDestinatario").hide();
				     $("#contenidoCopias").show();
				     ref.obtenerRegistroCorrespondencia();
				 }
				
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compHead.click();
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compHead.click();
			
			ref.modulo.tabs.tabDestinatario.compInterno.show();
			ref.modulo.tabs.tabDestinatario.compExterno.hide();
			
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.change(function(e){
				ref.archivosSeleccionados = e.target.files;
			});
			
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.submit(function(e){
				e.preventDefault();
				
				if(ref.archivosSeleccionados != undefined){
					var d = new Date();
					var id = d.getTime();
					var adjunto = {};
					adjunto.identificador = id;
					adjunto.principal = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked");
					adjunto.file = ref.archivosSeleccionados[0];
					adjunto.tipo = adjunto.principal == true ? 'PRINCIPAL' : 'SECUNDARIO';
					adjunto.nombre = ref.archivosSeleccionados[0].name;
					
					ref.adjuntos.push(adjunto);
					ref.actulizarListaArchivosAdjuntos();
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.trigger("reset");
					return false;
				}
			});
			
			ref.modulo.tabs.tabDestinatario.internos.compBtnAgregarDest.click(function(){
				var d = new Date();
				var id = d.getTime();
				var destinatarioInterno = {}
				destinatarioInterno.identificador = id;
				destinatarioInterno.codLugarTrabajo = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val();
				if(!destinatarioInterno.codLugarTrabajo){
					ref.modSistcorr.notificar('ERROR', 'Seleccione lugar de trabajo', 'Warning');
					return;
				}
				destinatarioInterno.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(destinatarioInterno.codLugarTrabajo).text;
				destinatarioInterno.codDependencia = ref.modulo.tabs.tabDestinatario.internos.compDependencia.val();
				if(!destinatarioInterno.codDependencia){
					ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
					return;
				}
				destinatarioInterno.dependencia = ref.listas.dependencia.buscarPorId(destinatarioInterno.codDependencia).text;
				
				ref.correspondencia.detalleInterno.push(destinatarioInterno);
				ref.limpiarFormularioDestinatarioInterno();
				ref.actualizarListaDestinatariosInternos();
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compBtnAgregarDest.click(function(){
				var d = new Date();
				var id = d.getTime();
				var destinatarioExterno = {};
				destinatarioExterno.identificador = id;
				destinatarioExterno.nacional = ($("input:radio[name=desExterno_tipo]:checked").val() == 'true');
				if(destinatarioExterno.nacional == true){
					destinatarioExterno.codPais = null;
					destinatarioExterno.pais = null;
					destinatarioExterno.codDepartamento = ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val();
					destinatarioExterno.departamento = ref.listas.departamento.buscarPorId(destinatarioExterno.codDepartamento).text;
					destinatarioExterno.codProvincia = ref.modulo.tabs.tabDestinatario.externos.compProvincia.val();
					destinatarioExterno.provincia = ref.listas.provincia.buscarPorId(destinatarioExterno.codProvincia).text;
					destinatarioExterno.codDistrito = ref.modulo.tabs.tabDestinatario.externos.compDistrito.val();
					destinatarioExterno.distrito = ref.listas.distrito.buscarPorId(destinatarioExterno.codDistrito).text;
					destinatarioExterno.codDependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
					destinatarioExterno.dependenciaNacional = ref.listas.dependenciaExterna.buscarPorId(destinatarioExterno.codDependenciaNacional).text;
					destinatarioExterno.dependenciaInternacional = null;
				} else{
					destinatarioExterno.codPais = ref.modulo.tabs.tabDestinatario.externos.compPais.val();
					destinatarioExterno.pais = ref.listas.pais.buscarPorId(destinatarioExterno.codPais).text;
					destinatarioExterno.codDepartamento = null;
					destinatarioExterno.departamento = null;
					destinatarioExterno.codProvincia = null;
					destinatarioExterno.provincia = null;
					destinatarioExterno.codDistrito = null;
					destinatarioExterno.distrito = null;		
					destinatarioExterno.dependenciaInternacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
				}
				
				
				destinatarioExterno.direccion = ref.modulo.tabs.tabDestinatario.externos.compDireccion.val();
				
				
				destinatarioExterno.nombreDestinatario = ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val();
				
				destinatarioExterno.dependencia = destinatarioExterno.nacional == true ? destinatarioExterno.dependenciaNacional : destinatarioExterno.dependenciaInternacional;
				destinatarioExterno.lugar = destinatarioExterno.nacional == true ? (destinatarioExterno.codDepartamento + ' - ' + destinatarioExterno.provincia + ' - ' + destinatarioExterno.distrito) : (destinatarioExterno.pais);
				
				ref.correspondencia.detalleExterno.push(destinatarioExterno);
				ref.limpiarFormularioDestinatarioExterno();
				ref.actualizarListaDestinatariosExternos();
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compTipo.change(function(){
				var val = this.value;
				val = (val == "true");
				if(val == true){ //NACIONAL
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().show();
				}
				
				if(val == false){//INTERNACIONAL
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().hide();
				}
				ref.limpiarFormularioDestinatarioExterno();
				
			});
					
			
			ref.modulo.tabs.tabCopias.compBtnAgregar.click(function(){
				var d = new Date();
				var id = d.getTime();
				var destinatarioCopia = {};
				destinatarioCopia.identificador =  id;
				destinatarioCopia.codLugarTrabajo =  ref.modulo.tabs.tabCopias.compLugarTrabajo.val()
				destinatarioCopia.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(destinatarioCopia.codLugarTrabajo).text;
				destinatarioCopia.codDependencia = ref.modulo.tabs.tabCopias.compDependencia.val();
				destinatarioCopia.dependencia =  ref.listas.dependencia.buscarPorId(destinatarioCopia.codDependencia).text;
				if(destinatarioCopia.codLugarTrabajo == null){
					ref.modSistcorr.notificar('ERROR', 'Seleccione lugar de trabajo', 'Warning');
					return;
				}
				if(destinatarioCopia.codDependencia == null){
					ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
					return;
				}
				ref.correspondencia.detalleCopia.push(destinatarioCopia);
				ref.actualizarListaCopias();
				ref.limpiarFormularioCopias();
			});
			
			ref.modulo.compBtnGuardar.click(function(){
				ref.procesoRegistroCorrespondencia();
				
			});
			
			
			setTimeout(function() {
				ref.modSistcorr.eventoTooltip();
				ref.modSistcorr.eventosS2Remote();	
				ref.modSistcorr.eventoDatePicker();
				ref.modSistcorr.eventoTextArea();
				ref.modSistcorr.eventoSelect();
			}, 500);
		},
		
		obtenerCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.obtenerCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.correspondencia = respuesta.datos[0];
						ref.inicializarCorrespondencia();
						ref.iniciarEventos();
						setTimeout(function() {
							ref.iniciarCombosAutoCompletados();
						}, 300);
					} else{
						ref.modSistcorr.notificar("Error", respuesta.mensaje, "Warning");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", ref.modSistcorr.mensajeErrorPeticion, "Error");
				});
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
			ref.autocomplete = {};
			ref.autocomplete.rmtLugarTrabajoSelect2 = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES,
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.lugarTrabajo.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val('');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change();
			});
			
			ref.autocomplete.rmtDependenciaSelect2 = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS,
				    data: function (params) {
				        var query = {
				        		codLugar: ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codUsuario', '');
				ref.obtnerFirmante();
			});
			
			ref.autocomplete.destInternoTrabajoSelect2 = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES,
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
			}).on('select2:select', function(){
				ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
				ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
			});
			
			ref.autocomplete.desInternoDependenciaSelect2 = ref.modulo.tabs.tabDestinatario.internos.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS,
				    data: function (params) {
				        var query = {
				        		codLugar: ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
			ref.autocomplete.desExternoPais = ref.modulo.tabs.tabDestinatario.externos.compPais.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PAISES,
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.pais.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
			ref.autocomplete.desExternoDepartamento = ref.modulo.tabs.tabDestinatario.externos.compDepartamento.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPARTAMENTOS,
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.departamento.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(){
				ref.modulo.tabs.tabDestinatario.externos.compProvincia.val('');
				ref.modulo.tabs.tabDestinatario.externos.compProvincia.change();
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.val('');
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
			});
			
			ref.autocomplete.desExternoProvincia = ref.modulo.tabs.tabDestinatario.externos.compProvincia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PROVINCIAS,
				    data: function (params) {
				        var query = {
				        		codDep: ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.provincia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(){
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.val('');
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
			});
			
			ref.autocomplete.desExternoDistrito = ref.modulo.tabs.tabDestinatario.externos.compDistrito.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DISTRITOS,
				    data: function (params) {
				        var query = {
				        		codDep: ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val(),
				        		codProv: ref.modulo.tabs.tabDestinatario.externos.compProvincia.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.distrito.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
			ref.autocomplete.desExternoDepExterna = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPEN_EXTERNA,
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependenciaExterna.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
			ref.autocomplete.copiaLugarTrabajoSelect2 = ref.modulo.tabs.tabCopias.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES,
				    data: function (params) {
				        var query = {
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.lugarTrabajo.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
			ref.autocomplete.copiaDependenciaSelect2 = ref.modulo.tabs.tabCopias.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS,
				    data: function (params) {
				        var query = {
				        		codLugar: ref.modulo.tabs.tabCopias.compLugarTrabajo.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			});
			
		},
		
		obtnerFirmante: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', 'www');
			ref.modulo.obtenerFirmante(ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val())
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(respuesta.datos[0].text);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', respuesta.datos[0].id);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.addClass('valid');
						var id = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.attr('id');
						var label = $("label[for='" + id + "']");
						label.addClass('active');
						label.css('color', '#757575');
					}
				}).catch(function(error){
					console.log('error', error);
				});
		},
		
		actualizarListaDestinatariosInternos: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.internos.compListaDestinatarios.empty();
			var html = ref.modulo.htmlListaDestinatariosInternos(ref.correspondencia.detalleInterno);
			ref.modulo.tabs.tabDestinatario.internos.compListaDestinatarios.html(html);
			ref.modSistcorr.eventoTooltip();
		},
		
		actualizarListaDestinatariosExternos: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compListaDestinatarios.empty();
			var html = ref.modulo.htmlListaDestinatariosExternos(ref.correspondencia.detalleExterno);
			ref.modulo.tabs.tabDestinatario.externos.compListaDestinatarios.html(html);
			ref.modSistcorr.eventoTooltip();
		},
		
		actulizarListaArchivosAdjuntos: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.empty();
			var html = ref.modulo.htmlListaArchivosAdjuntos(ref.adjuntos);
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.html(html);
		},
		
		actualizarListaCopias: function(){
			var ref = this;
			ref.modulo.tabs.tabCopias.compListaCopias.empty();
			var html = ref.modulo.htmlListaCopias(ref.correspondencia.detalleCopia);
			ref.modulo.tabs.tabCopias.compListaCopias.html(html);
		},
		
		obtenerRegistroCorrespondencia: function(){
			var ref = this;
			ref.correspondencia.codLugarTrabajo = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.val();	
			if(!ref.correspondencia.codLugarTrabajo){
				ref.modSistcorr.notificar('ERROR', 'Seleccione lugar de trabajo - Datos', 'Warning');
				return;
			}
			ref.correspondencia.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(ref.correspondencia.codLugarTrabajo).text;
			ref.correspondencia.codDependencia = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val();
			if(!ref.correspondencia.codDependencia){
				ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia - Datos', 'Warning');
				return;
			}
			ref.correspondencia.dependencia = ref.listas.dependencia.buscarPorId(ref.correspondencia.codDependencia).text; 
			ref.correspondencia.codRemitente = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data("codusuario");
			if(!ref.correspondencia.codRemitente){
				ref.modSistcorr.notificar('ERROR', 'Ingrese remitente - Datos', 'Warning');
				return;
			}
			ref.correspondencia.remitente = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val();	
			ref.correspondencia.codTipoCorrespondencia =  ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val();	
			ref.correspondencia.tipoCorrespondencia = $("#rmt_tipoCorrespondencia option:selected").text();
			ref.correspondencia.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val();	
			ref.correspondencia.fechaDocumento = ref.correspondencia.fechaDocumento;
			ref.correspondencia.asunto = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.val();
			ref.correspondencia.tipoEmision.idTipoEmision = $("input:radio[name=corr_tipoEmision]:checked").val();
			ref.correspondencia.despachoFisico = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.is(':checked');	
			ref.correspondencia.confidencial = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compConfidencial.is(':checked'); 
			ref.correspondencia.urgente = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compUrgente.is(':checked'); 
			ref.correspondencia.firmaDigital = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.is(':checked'); 
			ref.correspondencia.observaciones = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.val();
			
			if(ref.correspondencia.tipoEmision.idTipoEmision == 1){//INTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.show();
				ref.modulo.tabs.tabDestinatario.compExterno.hide();
				ref.correspondencia.detalleInterno = ref.correspondencia.detalleInterno || [];
				ref.correspondencia.detalleExterno = [];
			} else if(ref.correspondencia.tipoEmision.idTipoEmision == 2){ //EXTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.hide();
				ref.modulo.tabs.tabDestinatario.compExterno.show();
				ref.correspondencia.detalleExterno = ref.correspondencia.detalleExterno || [];
				ref.correspondencia.detalleInterno = [];
			}
			
			ref.correspondencia.detalleCopia = ref.correspondencia.detalleCopia || [];
			ref.correspondencia.archivos = ref.correspondencia.archivos || [];
			
			if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
				ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.parent().show();
			} else {
				ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.parent().hide();
			}
			console.log('CORRESPONDENCIA', ref.correspondencia);
			return true;
		},
		
		limpiarFormularioDestinatarioExterno: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compDireccion.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val('');
			ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val('');
			ref.modulo.tabs.tabDestinatario.externos.compPais.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val('');
			ref.modulo.tabs.tabDestinatario.externos.compProvincia.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDistrito.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val('');
			
			ref.modulo.tabs.tabDestinatario.externos.compPais.change();
			ref.modulo.tabs.tabDestinatario.externos.compDepartamento.change();
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.change();	
			ref.modulo.tabs.tabDestinatario.externos.compProvincia.change();
			ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
		},
		
		limpiarFormularioDestinatarioInterno: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val('');
			ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
			
			ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.change();
			ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
		},
		
		limpiarFormularioCopias: function(){
			var ref = this;
			ref.modulo.tabs.tabCopias.compLugarTrabajo.val('');
			ref.modulo.tabs.tabCopias.compDependencia.val('');
			
			ref.modulo.tabs.tabCopias.compLugarTrabajo.change();
			ref.modulo.tabs.tabCopias.compDependencia.change();
		},
		
		mostrarValoresEnFormulario: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.append("<option value='"+ref.correspondencia.codLugarTrabajo+"' selected='selected'>"+ref.correspondencia.lugarTrabajo+"</option>");
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.append("<option value='"+ref.correspondencia.codDependencia+"' selected='selected'>"+ref.correspondencia.dependencia+"</option>");
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(ref.correspondencia.remitente);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codUsuario', ref.correspondencia.codRemitente);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val(ref.correspondencia.codTipoCorrespondencia);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.datepicker({
				format: 'dd/mm/yyyy',
				locale: 'es-es',
	            autoclose: true,
	            startView: 0,
	            todayBtn: "linked",
	            todayHighlight: true,
	            value: ref.correspondencia.fechaDocumento
	        });
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val(ref.correspondencia.fechaDocumento);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.val(ref.correspondencia.asunto);
			$("input:radio[name=corr_tipoEmision][value='"+ref.correspondencia.tipoEmision.idTipoEmision+"']").prop('checked', true);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop('checked', ref.correspondencia.despachoFisico);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compConfidencial.prop('checked', ref.correspondencia.confidencial);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compUrgente.prop('checked', ref.correspondencia.urgente);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.prop('checked', ref.correspondencia.firmaDigital);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.val(ref.correspondencia.observaciones);
			
			ref.actualizarListaDestinatariosInternos();
			ref.actualizarListaDestinatariosExternos();
			ref.actualizarListaCopias();
			ref.actulizarListaArchivosAdjuntos();
			
			ref.listas.lugarTrabajo.agregarLista([{"id": ref.correspondencia.codLugarTrabajo, "text": ref.correspondencia.lugarTrabajo}]);
			ref.listas.dependencia.agregarLista([{"id": ref.correspondencia.codDependencia, "text": ref.correspondencia.dependencia}]);
			
		},
		
		inicializarCorrespondencia: function(){
			var ref = this;
			for(var i in ref.correspondencia.detalleInterno){
				ref.correspondencia.detalleInterno[i].identificador = ref.correspondencia.detalleInterno[i].idDestinatarioInterno;
			}
			for(var i in ref.correspondencia.detalleExterno){
				ref.correspondencia.detalleExterno[i].identificador = ref.correspondencia.detalleExterno[i].idDestinatarioExterno;
			}
			for(var i in ref.correspondencia.detalleCopia){
				ref.correspondencia.detalleCopia[i].identificador = ref.correspondencia.detalleCopia[i].idDestinatarioCopia;
			}
			ref.adjuntos = [];
			for(var i in ref.correspondencia.adjuntos){
				ref.adjuntos.push({
					identificador: ref.correspondencia.adjuntos[i].idArchivoAdjunto,
					principal: ref.correspondencia.adjuntos[i].principal,
					nombre: ref.correspondencia.adjuntos[i].nombre,
					id: ref.correspondencia.adjuntos[i].idArchivoAdjunto
				});
			}
			
			ref.mostrarValoresEnFormulario();
		},
		
		procesoRegistroCorrespondencia: function(){
			var ref = this;
			var correspondencia_registrada = null;
			ref.modSistcorr.procesar(true);
			var _correspondencia = ref.correspondencia;
			for(var i in _correspondencia.detalleInterno){
				delete _correspondencia.detalleInterno[i].identificador;
			}
			for(var i in _correspondencia.detalleExterno){
				delete _correspondencia.detalleExterno[i].identificador;
			}
			for(var i in _correspondencia.detalleCopia){
				delete _correspondencia.detalleCopia[i].identificador;
			}
			ref.modulo.registrarCorrespondencia(_correspondencia, ref.adjuntos)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Warning");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", ref.modSistcorr.mensajeErrorPeticion, "Error");
				});
		},
};

setTimeout(function(){
	CORRESPONDENCIA_EDICION_VISTA.modulo = modulo_correspondencia_edicion;
	CORRESPONDENCIA_EDICION_VISTA.errores = ERRORES || [];
	CORRESPONDENCIA_EDICION_VISTA.inicializar();
	CORRESPONDENCIA_EDICION_VISTA.listas = {};
	CORRESPONDENCIA_EDICION_VISTA.edicion = EDICION || false;
	CORRESPONDENCIA_EDICION_VISTA.listas.lugarTrabajo = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.dependencia = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.dependenciaExterna = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.pais = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.departamento = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.provincia = Object.create(LISTA_DATA);
	CORRESPONDENCIA_EDICION_VISTA.listas.distrito = Object.create(LISTA_DATA);
}, 200);