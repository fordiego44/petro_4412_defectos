var modulo_correspondencia_edicion = MODULO_CORRESPONDENCIA_EDICION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_EDICION_VISTA = {
		modSistcorr: null,
		modulo: null,
		tiposCorrespondencia: [],
		correspondenciaSelecionada: {},
		archivosAdjuntos: [],
		destinatariosExternos: [],
		destinatariosInternos: [],
		destinatariosDocPagar: [],//ticket 9000004765
		copias: [],
		rutaAprobacion: [],
		esEdicion: false,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		indiceURLS: 0,
		archivosSeleccionados: [],
		permiteMulitpleDestinatarios: false,
		correspondenciaInterna: false,
		correspondenciaExterna: false,
		esTipCorrespDocPagarCheckAnt: false,
		dependenciasUsuario: [],
		dependenciasAdicional: [],
		flagCargarPagina: false,//TICKET 9000004269
		idRowSelected: 0,
		habilitarDependenciasSuperiores: false,
		tablaRutaEdicion: false,
		sizeScreen: 500,
		URL_BUSCAR_LUGAR_X_DEPENDENCIA: ['../app/emision/buscar/lugar-dependencia', '../../app/emision/buscar/lugar-dependencia'],
		procesando: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.indiceURLS = ref.esEdicion == false ? 0 : 1;
			ref.inicializarComponentes();
			if(ref.esEdicion == false){
				ref.habilitarDependenciasSuperiores = true;
				ref.tablaRutaEdicion = true;
				ref.flagCargarPagina = true;//TICKET 9000004269
				ref.inicializarCorrespondencia();
				// TICKET 9000004044
				ref.verificacionOrigen();
				// FIN TICKET
				ref.flagCargarPagina = false;//TICKET 9000004269
			} else {
				ref.obtenerCorrespondencia();
			}
		},
		
		// TICKET 9000004044
		verificacionOrigen: function(){
			var ref = this;
			var origen = sessionStorage.getItem('urlOrigen');
			console.log("ORIGEN:" + origen);
			if(origen=="bandejaNuevos"){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuesta.val(0);
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.val("NO");
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.change();
				// OCULTANDO CAMPOS
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.closest('div').hide();
				$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.attr('id') + "']").hide();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.closest('div').hide();
				$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.attr('id') + "']").hide();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.closest('div').hide();
				// FIN OCULTANDO CAMPOS
			}else{
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuesta.val(1);
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.val("SI");
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.change();
				// OBTENCION DE DATOS PROVENIENTES DE BANDEJA DE ENTRADA
				var accionPrecedente = sessionStorage.getItem('accionPrecedente');
				if(accionPrecedente == "cerrarCorrespondencia"){
					var corrEntr = sessionStorage.getItem('correlativo');
					var comentario = sessionStorage.getItem('comentario');
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compIdAsignacion.val(0);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.val(corrEntr);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.change();
					//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.removeClass('invalid');
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.val(comentario);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.change();
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.hide();
					$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.attr('id') + "']").hide();
					//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.removeClass('invalid');
				}
				if(accionPrecedente == "completarCorrespondencia"){
					var corrEntr = sessionStorage.getItem('correlativo');
					var comentario = sessionStorage.getItem('comentario');
					var idAsignacion = sessionStorage.getItem('idAsignacion');
					var tipoAccion = sessionStorage.getItem('tipoAccion');
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compIdAsignacion.val(idAsignacion);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.val(corrEntr);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.change();
					//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.removeClass('invalid');
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.val(tipoAccion);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.change();
					//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.removeClass('invalid');
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.val(comentario);
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.change();
					//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.removeClass('invalid');
				}
			}
		},
		// FIN TICKET
		
		/*INICIO TICKET 9000003934*/
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.modulo.irADetalle(this.dataset.id);
				});
			}
		},
		
		validarBusquedaEntidadExternaNacionalSunat: function(){
			var ref = this;
			var tipo = ref.modulo.tabs.tabDestinatario.externos.compTipoBuscarEntidadExternaNacionalSunat.filter(":checked").val();
			var txtSearchEntidadExterna = ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val();
			
			if(tipo == "RUC" && (txtSearchEntidadExterna.length == 0 || txtSearchEntidadExterna.length < 11 || txtSearchEntidadExterna.length > 11)){
				ref.modSistcorr.notificar("ERROR", "Por favor ingrese un RUC v&aacute;lido", "Error");
				return false;
			}else if(tipo == "RZ" && (txtSearchEntidadExterna.length == 0 || txtSearchEntidadExterna.length < 4 || txtSearchEntidadExterna.length > 100)){
				if(txtSearchEntidadExterna.length < 4)
					ref.modSistcorr.notificar("ERROR", "Para realizar la b&uacute;squeda debe ingresar como m&iacute;nimo 4 caracteres", "Error");
				else
					ref.modSistcorr.notificar("ERROR", "Para realizar la b&uacute;squeda debe ingresar como m&aacute;ximo 100 caracteres", "Error");
				return false;
			}
			
			return true;
		},
		
		inicializarTabla: function(data){
			var ref = this;
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.componentes.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				
				ref.modulo.componentes.dataTable.show();
				ref.dataTable = ref.modulo.componentes.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 4,
			        "data": data,
			        "columns": [
			        	{data: 'ddp_numruc', title: 'RUC', defaultContent: ''},
			        	{data: 'ddp_nombre', title: 'RAZON SOCIAL', defaultContent: ''},
			        	{data: '_id', title: 'SEL', defaultContent: '', align: 'center', render: function(data, type, full){
			        		
			        		return '<div class="custom-control custom-radio"><input type="radio" id="selectEXS'+full._id+'" data-ruc="'+full.ddp_numruc+'" data-rz="'+full.ddp_nombre.trim()+'" name="seleccionarEntidadExternaNacional" class="custom-control-input" value="'+full._id+'" /> <label class="custom-control-label text-muted cursor-pointer" for="selectEXS'+full._id+'"> &nbsp;</label></div>'
			        	}},
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.modulo.componentes.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 1500);
			}
		},
		/*FIN TICKET 9000003934*/
		
		validarDestinatariosCopias: function() {
			var ref = this;
			var validarDestinatarios = false;
			for(var i=0;i<ref.tiposCorrespondencia.length;i++){
				if(ref.tiposCorrespondencia[i]['codigo']==ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val()){
					console.log(ref.tiposCorrespondencia[i]);
					//if(ref.tiposCorrespondencia[i]['destinatario']=="1"){
					if(ref.tiposCorrespondencia[i]['destinatario']=="1" || ref.tiposCorrespondencia[i]['destinatario']=="2"){
						validarDestinatarios = true;
						if(!ref.tiposCorrespondencia[i]['multipleDestinatario']){
							var indic = 1;
							$("." + ref.modulo.classBtnEliminarDestinatarioInterno).each(function(){
								if(indic==0){
									var t = $(this);
									t.click();
								}else{
									indic = 0;
								}
							});
							indic = 1;
							$("." + ref.modulo.classBtnEliminarDestinatarioExterno).each(function(){
								if(indic==0){
									var t = $(this);
									t.click();
								}else{
									indic = 0;
								}
							});
							if(ref.tiposCorrespondencia[i]['intena']){
								$(".dependencias_nivel").hide();
							}
						}else{
							if(ref.tiposCorrespondencia[i]['intena']){
								$(".dependencias_nivel").show();
								console.log("Validando datos");
								ref.modulo.tabs.tabDestinatario.internos.compDependencias2.prop("checked", false);
								ref.modulo.tabs.tabDestinatario.internos.compDependencias3.prop("checked", false);
							}
						}
						if(ref.tiposCorrespondencia[i]['intena']){
							console.log("Eliminar destinatarios externos");
							$("." + ref.modulo.classBtnEliminarDestinatarioExterno).each(function(){
								var t = $(this);
								t.click();
								var dependencia_ = t.data("identificador");
								console.log("Dependencia:" + dependencia_)
								ref._eliminarDependencia_externo(dependencia_);
							});
						}
						if(ref.tiposCorrespondencia[i]['externa']){
							console.log("Eliminar destinatarios internos");
							$("." + ref.modulo.classBtnEliminarDestinatarioInterno).each(function(){
								var t = $(this);
								t.click();
								var dependencia_ = t.data("identificador");
								console.log("Dependencia:" + dependencia_)
								ref._eliminarDependencia_interno(dependencia_);
							});
						}
					}
					if(ref.tiposCorrespondencia[i]['destinatario']=="0"){
						//setTimeout(function(){
							$("." + ref.modulo.classBtnEliminarDestinatarioInterno).each(function(){
								var t = $(this);
								t.click();
								var dependencia_ = t.data("identificador");
								ref._eliminarDependencia_interno(dependencia_);
							});
							$("." + ref.modulo.classBtnEliminarDestinatarioExterno).each(function(){
								var t = $(this);
								t.click();
								var dependencia_ = t.data("identificador");
								ref._eliminarDependencia_interno(dependencia_);
							});
						//}, 500);
					}
					console.log("Tipo de Correspondencia seleccionada")
					console.log(ref.tiposCorrespondencia[i]['copia']);
					if(!ref.tiposCorrespondencia[i]['copia']){
						//setTimeout(function(){
							$("." + ref.modulo.classBtnEliminarCopia).each(function(){
								console.log("1")
								var t = $(this);
								t.click();
							});
						//}, 500);
					}
				}
			}
			/*if(validarDestinatarios){
				console.log("Validando Destinatarios:")
				var cantDest = ref.correspondencia.detalleInterno.length + ref.correspondencia.detalleExterno.length;
				if(cantDest==0){
					ref.modSistcorr.notificar('ERROR', 'Debe ingresar al menos un destinatario.', 'Warning');
					return false;
				}
			}*/
		},
		
		inicializarSelectFirmanteRutaAprobacion: function(){
			var ref = this;
			ref.componentes.combosS2.rmtDependenciaRutaAprobacion = ref.modulo.tabs.tabRutaAprobacion.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS_TODAS[ref.indiceURLS],
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(event){
				//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
				//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codUsuario', '');
				//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
				//ref._obtenerLugarXDependencia();
				//ref._obtnerFirmante();
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
				console.log("Cambio dependencia ruta aprobacion");
				var a = $(this);
				console.log(a.val());
				if(a.val()!=null){
					ref._obtenerJefeDependenciaRutaAprobacion();
	            	//ref.modSistcorr.select2_change($comp.attr('id'));
	            	//ref._obtenerLugarXDependencia();
				}
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
		},
		
		inicializarSelectPersonaRutaAprobacion: function(){
			var ref = this;

			
			ref.componentes.combosS2.rmtPersonaRutaAprobacion = ref.modulo.tabs.tabRutaAprobacion.compPersona.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PERSONAS_TODAS[ref.indiceURLS],
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.persona.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.inicializarSelectFirmanteRutaAprobacion();
			
			ref.inicializarSelectPersonaRutaAprobacion();
			
			ref.inicializarTabla();/*TICKET 9000003934*/
			
			ref.modulo.tabs.tabRutaAprobacion.compDependencia.attr('disabled', true);
			ref.modulo.tabs.tabRutaAprobacion.compPersona.attr('disabled', true);
			
			$(document).on('change','input:radio[name=seleccionarEntidadExternaNacional]', function(){
			    // Do something interesting here
				var iden = $(this).val();
				var rucSel = $(this).data('ruc');
				var rzSel = $(this).data('rz');
				ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.focus();
				ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.val(rzSel);
				$("#codEntidadExternaPorRuc").val(rucSel);//AQUI
				$("#modalSearchRucRazonSocialEntidadExterna").modal("hide");
			});
			
			$('input[name=corr_primerFirmante]').change(function(){
				var obj = $(this);
				var activeRutaAprobacion = $("input[name=corr_rutaAprobacion]:checked").val();
				if(activeRutaAprobacion == 1){
					console.log(obj.prop('checked'));
					if(obj.prop('checked')){
						console.log("Agregar primer firmante");
						var primer_firmante = $("#corr_primerFirmante").prop('checked');
						var ruta_aprobacion = $("#corr_ruta_aprobacion_si").prop('checked');
						var estado = CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR;
						if(ref.correspondencia.estado.id!=0){
							estado = ref.correspondencia.estado.id;
						}
						if(primer_firmante == true && ruta_aprobacion == true && estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA && estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && 
								estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA && estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA && estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR && 
								estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && estado!=CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA){
							ref._agregarPrimerFirmante();
						}
					}else{
						console.log("Eliminar primer firmante");
						ref._eliminarPrimerFirmante();
					}
				}
			});
			
			ref.modulo.tabs.tabRutaAprobacion.compBtnModalAgregarFirm.click(function(e){
				$("#firmanteTipo_jefe").prop('checked', false);
				$("#firmanteTipo_persona").prop('checked', false);
				$("#firmante_dependencia").val("");
				$("#firmante_dependencia").change();
				$("#firmante_nombre").val("");
				$("#firmante_nombre").change();
				ref.inicializarSelectFirmanteRutaAprobacion();
				ref.inicializarSelectPersonaRutaAprobacion();
				$("label[for=firmante_dependencia]").removeClass('active');
				$("#firmante_dependencia").parent().show();
				console.log("Tamaño ruta:" + ref.rutaAprobacion.length)
				if(ref.rutaAprobacion.length > 0){
					var firmanteFinal = ref.rutaAprobacion[ref.rutaAprobacion.length - 1].firmante;
					if(firmanteFinal && firmanteFinal.estado && (firmanteFinal.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA || 
							firmanteFinal.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || 
							firmanteFinal.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA)){
						console.log("Cancelando")
						setTimeout(function(){
							$("#btnCancelarAgregarFirmante").click();
						}, 700)
						ref.modSistcorr.notificar('ERROR', 'No se puede agregar registros ya que se encuentra en el último firmante.', 'Error');
						return;
					}
				}
			});
			
			/*ref.componentes.combosS2.rmtLugarTrabajo = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES[ref.indiceURLS],
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
			}).on('select2:select', function(event){
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
			});*/
			
			$("label[for='rmt_tipoCorrespondencia']").click(function(){
				console.log('clickkkk');
				setTimeout(function() {
					$("label[for='rmt_tipoCorrespondencia']").addClass('active');
				}, 300);
			});
			
			if(ref.esEdicion == false){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.removeAttr('disabled');
			}
			
			ref.componentes.combosS2.rmtDependenciaOriginadora = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.select2({
				ajax: {
					url: ref.modulo.URL_BUSCAR_DEPENDENCIAS_X_USUARIO[ref.indiceURLS],
					 data: function (params) {
					        var query = {
					        		q: params.term
					        }
					        return query;
					 },
					 processResults: function (respuesta) {
					    	ref.listas.dependencia.agregarLista(respuesta.datos);
					    	return {results: respuesta.datos};
					 }  
				}
			}).on('select2:select', function(event){
				var _depseleccionado = ref.listas.dependencia.buscarPorId(ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val());
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.append("<option value='"+_depseleccionado.id+"' selected='selected'>"+_depseleccionado.text+"</option>");
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change();
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'))
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
            	var codDependencia = $("#rmt_dependencia_originadora").val();
            	ref.modulo.obtenerFirmante(ref.indiceURLS, codDependencia)
            		.then(function(respuesta){
            			if(respuesta.estado==true && respuesta.datos.length > 0){
            				$("#rmt_nombre_originadora").val(respuesta.datos[0].codigo);
            			}
            		}).catch(function(error){
            			
            		});
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			
			ref.componentes.combosS2.rmtDependencia = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.select2({
				ajax: {
				    url: function(){
				    	console.log("Bandera:");
				    	console.log(ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.prop('checked'));
				    	if(ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.prop('checked')){
				    		console.log("TODAS");
				    		return ref.modulo.URL_BUSCAR_DEPENDENCIAS_TODAS[ref.indiceURLS]; 
				    	}else{
				    		console.log("ORIGINAL");
				    		return ref.modulo.URL_BUSCAR_DEPENDENCIAS_SUPERIORES[ref.indiceURLS];
				    	}
				    },
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencia.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).on('select2:select', function(event){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codUsuario', '');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
				ref._obtenerLugarXDependencia();
				ref._obtnerFirmante();
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
            	ref._obtenerLugarXDependencia();
				ref._obtnerFirmante();
				setTimeout(function(){
					console.log("ACTUALIZANDO TABLA DE RUTA DE APROBACION DESDE EL COMBO DE LA DEPENDENCIA REMITENTE");
					//if($("input[name=corr_rutaAprobacion]:checked").val()==1){
					if($("input[name=corr_rutaAprobacion]:checked").val()==1 && ref.esEdicion == false){
						ref._generarTablaRutaAprobacionGeneral();
					}
				}, 1000);
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			
			
			ref.componentes.combosS2.destInternoTrabado = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES[ref.indiceURLS],
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
				ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
				ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
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
			
			ref.componentes.combosS2.destInternoDependencia =  ref.modulo.tabs.tabDestinatario.internos.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS_UM[ref.indiceURLS],
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
			
			ref.componentes.combosS2.destExternoPais = ref.modulo.tabs.tabDestinatario.externos.compPais.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PAISES[ref.indiceURLS],
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
			
			ref.componentes.combosS2.destExternoDepartamento = ref.modulo.tabs.tabDestinatario.externos.compDepartamento.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPARTAMENTOS[ref.indiceURLS],
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
			}).on('select2:select', function(event){
				ref.modulo.tabs.tabDestinatario.externos.compProvincia.val('');
				ref.modulo.tabs.tabDestinatario.externos.compProvincia.change();
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.val('');
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
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
			
			ref.componentes.combosS2.destExternoProvincia = ref.modulo.tabs.tabDestinatario.externos.compProvincia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PROVINCIAS[ref.indiceURLS],
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
			}).on('select2:select', function(event){
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.val('');
				ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
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
			
			ref.componentes.combosS2.destExternoDistrito = ref.modulo.tabs.tabDestinatario.externos.compDistrito.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DISTRITOS[ref.indiceURLS],
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
			
			ref.componentes.combosS2.destExternoDepenciaNac = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPEN_EXTERNA[ref.indiceURLS],
				    data: function (params) {
				        var query = {
				        		//codDep: ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val(),
				        		//codProv: ref.modulo.tabs.tabDestinatario.externos.compProvincia.val(),
				        		//codDist: ref.modulo.tabs.tabDestinatario.externos.compDistrito.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependenciaExterna.agregarLista(respuesta.datos);
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
			
			ref.componentes.combosS2.copiaLugarTrabajo = ref.modulo.tabs.tabCopias.compLugarTrabajo.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_LUGARES[ref.indiceURLS],
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
			}).on('select2:select', function(event){
				ref.modulo.tabs.tabCopias.compDependencia.val('');
				ref.modulo.tabs.tabCopias.compDependencia.change();
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
			
			ref.componentes.combosS2.copiaDependencia = ref.modulo.tabs.tabCopias.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS[ref.indiceURLS],
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
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            	//var label = $("label[for='" + $comp.attr('id') + "']");
	            	//label.addClass('active');
	            },
	        });
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDocumento.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.focus();
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.select2({
				
			}).change(function(event){
				var selected = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val();
				if(selected == "107"){ //OTROS
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.parent().show();
				} else {
					//inicio ticket 9000004765
					if(selected != ref.modulo.codigoDocPagar){
						$('#myClassicTabOrange a[href="#contenidoCopias"]').parent().show();
						if(ref.esTipCorrespDocPagarCheckAnt)
							ref.destinatariosInternos = [];
						
						ref.esTipCorrespDocPagarCheckAnt = false;
					}
					if(selected == ref.modulo.codigoDocPagar){
						ref.modulo.tabs.tabDestinatario.internos.compDestinatarioDocPagar.prop('checked', false);
						$('#myClassicTabOrange a[href="#contenidoCopias"]').parent().hide();
					}else if(selected == '1'){//fin ticket 9000004765
						ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "checked", true );
						ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "disabled", true );
					} else {
						ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "checked", false );
						ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "disabled", false );
					}
					ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.parent().hide();
				}
				for(var i in ref.tiposCorrespondencia){
					var tipo = ref.tiposCorrespondencia[i];
					if(tipo.id == selected){
						ref.permiteMulitpleDestinatarios = tipo.multipleDestinatario || false;
						ref.correspondenciaInterna = tipo.intena || false;
						ref.correspondenciaExterna = tipo.externa || false;
						ref.requiereCopia = tipo.copia || false;		//adicion JE 9-3874				
						ref.requiereDestinatario = tipo.destinatario ;	//adicion JE 9-3874
						break;
					}
				}
				ref._actualizarComponentesPorTipoCorrespondencia();
				var $comp = $(event.currentTarget);
				ref.modSistcorr.select2_change($comp.attr('id'));
				ref.validarDestinatariosCopias();
			}).focus(function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.select2_change($comp.attr('id'));
			}).focusout(function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.select2_change($comp.attr('id'));
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change(function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).focusout(function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			})
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.textArea_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.textArea_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.textArea_focusOut($comp.attr('id'));
			});
			
			
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.textArea_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.textArea_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.textArea_focusOut($comp.attr('id'));
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compDireccion.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			ref.modulo.compFirmante.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.inputText_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.inputText_focusOut($comp.attr('id'));
			});
			
			$("input:radio[name=corr_tipoEmision]").change(function(){
				var _val = $("input:radio[name=corr_tipoEmision]:checked").val();
				if(_val == CONSTANTES_SISTCORR.EMISION_INTERNA){
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "checked", false );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "disabled", false );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.closest('div').closest('div').closest('div').closest('div').show()
					$("#despachoFisico").show();
				} else if(_val == CONSTANTES_SISTCORR.EMISION_EXTERNA){
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "checked", true );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop( "disabled", true );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.closest('div').closest('div').closest('div').closest('div').hide()
					$("#despachoFisico").hide();
				}
			});
			
			
			$("input:radio[name=corr_firmaDigital]").change(function(){
				var _val = $("input:radio[name=corr_firmaDigital]:checked").val();
				_val = (_val == 'true');
				
				if(_val == true){
					console.log('firmadigital activado');
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( "disabled", false ); 
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked", true );
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("disabled", false )
					// TICKET 9000003943
					$("#corr_ruta_aprobacion_no").removeAttr('disabled');
					$("#corr_ruta_aprobacion_si").removeAttr('disabled');
					// FIN TICKET
					// TICKET 9000004270
					$(".archivo-principal").removeAttr('disabled');
					// FIN TICKET
				} else {
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked", false );
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("disabled", true )
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( "disabled", true ); //eliminacion 9-3874
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( 'checked', false ); // eliminacion 9-3874
					// TICKET 9000003943
					//$("#corr_ruta_aprobacion_no").click();
					
					/*$("#corr_ruta_aprobacion_no").prop('checked', true);
					$("#corr_ruta_aprobacion_no").attr('disabled', true);
					$("#corr_ruta_aprobacion_si").attr('disabled', true);
					$("#RutaAprobacion-tab").closest('li').hide();*/
					
					/*inicio TICKET 9000004269*/
					
					if(ref.flagCargarPagina){
						setTimeout(function(){
							$("#RutaAprobacion-tab").closest('li').show();
							ref._generarTablaRutaAprobacionGeneral();
						}, 500);
					}else{
						$("#corr_ruta_aprobacion_no").prop('checked', true);
						$("#corr_ruta_aprobacion_no").attr('disabled', true);
						$("#corr_ruta_aprobacion_si").attr('disabled', true);
						$("#RutaAprobacion-tab").closest('li').hide();
					}
					/*$("#corr_ruta_aprobacion_si").prop('checked', true);
					$("#corr_ruta_aprobacion_si").attr('disabled', true);
					$("#corr_ruta_aprobacion_no").attr('disabled', true);
					$("#RutaAprobacion-tab").closest('li').show();*/
					/*fin TICKET 9000004269*/
					ref.rutaAprobacion = [];
					//FIN TICKET
					// TICKET 9000004270
					$(".archivo-principal").prop('checked', false);
					$(".archivo-principal").attr('disabled', true);
					for(var i=0;i<ref.archivosAdjuntos.length;i++){
						ref.archivosAdjuntos[i].principal = false;
						ref.archivosAdjuntos[i].tipo = 'SECUNDARIO';
					}
					// FIN TICKET
				}
				if(ref.esEdicion == true){
					//ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop("disabled", true ); eliminacion 9-3874
				}
			});
			
			/*ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.change(function(){
				var _val = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.is(':checked');
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked", false );
				if(_val == true){
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("disabled", false );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( "disabled", false );
				} else {
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("disabled", true );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( "disabled", true );
					ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop( 'checked', false );
				}
				
			});*/
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.change(function(){
				var t = $(this);
				$("#rmt_dependencia").val("")
				$("#select2-rmt_dependencia-container").html("[SELECCIONE]");
				$("#rmt_lugarTrabajo").val("");
				$("#textoLugarTrabajo").html("--------------------");
				$("#rmt_nombre").val("");
				$("#textoJefeDependenciaRemitente").html("--------------------");
			});
			
			// TICKET 9000003943
			ref.modulo.tabs.tabRutaAprobacion.compBtnAgregarFirm.click(function(){
				var t = $(this);
				var posicion = ref.rutaAprobacion.length - 1;
				console.log("Cantidad:" + posicion);
				var tipo = "";
				var nombreTipo = "";
				var dependencia = "";
				var nombreDependencia = "";
				var usuario = "";
				var nombreUsuario = "";
				if($("#firmanteTipo_jefe").prop("checked") || $("#firmanteTipo_persona").prop("checked")){
					if($("#firmanteTipo_jefe").prop("checked")){
						tipo = $("#firmanteTipo_jefe").val();
						nombreTipo = "Jefe Dependencia";
						dependencia = $("#firmante_dependencia").val();
						$("#firmante_dependencia").parent().show();
						if(dependencia == null || dependencia.trim() == ""){
							ref.modSistcorr.notificar("ERROR", "Seleccione una dependencia", "Error");
							return;
						}
						nombreDependencia = $("#firmante_dependencia option:selected").text();
						usuario = $("#firmante_nombre").val();
						if(usuario == null || usuario.trim()==""){
							ref.modSistcorr.notificar("ERROR", "La dependencia seleccionada no cuenta con un jefe", "Error");
							return;
						}
						nombreUsuario = $("#firmante_nombre option:selected").text();
					}
					if($("#firmanteTipo_persona").prop("checked")){
						tipo = $("#firmanteTipo_persona").val();
						nombreTipo = "Participante";
						usuario = $("#firmante_nombre").val();
						$("#firmante_dependencia").parent().hide();
						if(usuario == null || usuario.trim() == ""){
							ref.modSistcorr.notificar("ERROR", "Seleccione una persona", "Error");
							return;
						}
						nombreUsuario = $("#firmante_nombre option:selected").text();
					}
				}else{
					ref.modSistcorr.notificar("ERROR", "Seleccione un tipo de firmante", "Error");
					return;
				}
				if(usuario==null){
					usuario = "";
				}
				ref._agregarRutaAprobacion(posicion, tipo, nombreTipo, dependencia, nombreDependencia, usuario, nombreUsuario);
				ref._refrescarTablaRutaAprobacion();
			});
			
			ref.modulo.tabs.tabRutaAprobacion.compTipoFirmanteJefe.click(function(){
				var t = $(this);
				console.log("Jefe Dependencia seleccionado");
				ref.modulo.tabs.tabRutaAprobacion.compDependencia.removeAttr('disabled');
				ref.modulo.tabs.tabRutaAprobacion.compPersona.val("0");
				ref.inicializarSelectPersonaRutaAprobacion();
				ref.modulo.tabs.tabRutaAprobacion.compPersona.change();
				ref.modulo.tabs.tabRutaAprobacion.compPersona.attr('disabled', true);
				
				$("#firmante_dependencia").parent().show();
				// TCKT 9000003997 - DEFECTO
				var dependenciaActual = ref.modulo.tabs.tabRutaAprobacion.compTipoFirmanteJefe.val();
				console.log(dependenciaActual);
				if(dependenciaActual.trim()!="" && dependenciaActual.trim()!="0"){
					ref._obtenerJefeDependenciaRutaAprobacion();
				}
				// FIN TCKT
			});
			
			ref.modulo.tabs.tabRutaAprobacion.compTipoFirmantePersona.click(function(){
				var t = $(this);
				ref.modulo.tabs.tabRutaAprobacion.compDependencia.val("");
				ref.modulo.tabs.tabRutaAprobacion.compDependencia.change();
				ref.inicializarSelectFirmanteRutaAprobacion();
				ref.modulo.tabs.tabRutaAprobacion.compDependencia.attr('disabled', true);
				ref.modulo.tabs.tabRutaAprobacion.compPersona.removeAttr('disabled');
				
				$("#firmante_dependencia").parent().hide();
			});
			// FIN TICKET
			
			ref.modSistcorr.eventosS2Remote();	
			ref.modSistcorr.eventoDatePicker();
			ref.modSistcorr.eventoTextArea();
			ref.modSistcorr.eventoSelect();
			ref.modSistcorr.eventoInputText();
			setTimeout(function() {
				ref.inicializarEventosComponentes();
				console.log("CHECK FIRMA");
				if(ref.correspondencia && ref.correspondencia.id == 0){
					$("#corr_flujo_firma_digital").click();
					$("#corr_primerFirmante").click();
					$("#RutaAprobacion-tab").closest('li').hide();
					$("#Flujo-tab").closest('li').hide();
				}
			}, 500);
			
			ref.modulo.tabs.tabDestinatario.internos.compDependencias2.change(function(){
				var t = $(this);
				var seleccionado = t.prop('checked');
				if(seleccionado){
					console.log("Deseleccionando dep 3");
					ref.modulo.tabs.tabDestinatario.internos.compDependencias3.prop('checked', false);
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.prop('disabled', true);
				}else{
					var seleccionado2 = ref.modulo.tabs.tabDestinatario.internos.compDependencias3.prop('checked');
					if(!seleccionado2){
						ref.modulo.tabs.tabDestinatario.internos.compDependencia.removeAttr('disabled');
					}
				}
			});
			
			ref.modulo.tabs.tabDestinatario.internos.compDependencias3.change(function(){
				var t = $(this);
				var seleccionado = t.prop('checked');
				if(seleccionado){
					console.log("Deseleccionando dep 2")
					ref.modulo.tabs.tabDestinatario.internos.compDependencias2.prop('checked', false);
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
					ref.modulo.tabs.tabDestinatario.internos.compDependencia.prop('disabled', true);
				}else{
					var seleccionado2 = ref.modulo.tabs.tabDestinatario.internos.compDependencias2.prop('checked');
					if(!seleccionado2){
						ref.modulo.tabs.tabDestinatario.internos.compDependencia.removeAttr('disabled');
					}
				}
			});
		},
		
		isNumericStr: function(n){
			
			return !isNaN(parseFloat(n)) && isFinite(n);
		},
		
		validarStrNum: function(str){
			
			if(str != null &&
					str != undefined &&
					typeof str === 'string' &&
					str.trim() != "")
				return true;
			return false;
		},
		
		
		inicializarEventosComponentes: function(){
			var ref = this;
			
			ref.modulo.tabs.tabDatos.compHeader.addClass('petro-tabs-activo');
			ref.modulo.tabs.tabDatos.compBody.show();
			ref.modulo.tabs.tabDestinatario.compBody.hide();
			ref.modulo.tabs.tabCopias.compBody.hide();
			
			setTimeout(function() {
				ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compHead.click();
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compHead.click();
			}, 500);
			
			if(screen.width < ref.sizeScreen){
				$("#mainTitle").text("Mod. de correspondencia");
				$("#lblLUgarTrabajo").text("Cent. de Gest. Corresp.");
				$("#lblJefeDependencia").text("Jefe Depen. Remit.");
				$("#lblCaractCorr").text("Caract. de corresp.");
				
			}
			
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				if (!e.target.dataset.tab) {
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
				     //ref.obtenerRegistroCorrespondencia();
				 }
				    
				 if (e.target.dataset.tab == "copias") {
				     $("#contenidoAccordionDatos").hide();
				     $("#contenidoHistorialDestinatario").hide();
				     $("#contenidoCopias").show();
				     //ref.obtenerRegistroCorrespondencia();
				 }
				 //ref._validar_Formulario();
				 console.log("_validar_Formulario_Destinatario");
				 ref._validar_Formulario_Destinatario();
			});
			
			//TICKET 9000003934
			$(document).keydown(function(e) {
		        if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = true;
		    }).keyup(function(e) {
		        if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = false;
		    });
			
			$(document).on('keydown','.textoIptSearchRuc', function (e) {
			    
				// Allow: backspace, delete, tab, escape, enter and .
			    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
			        
			    	(e.keyCode === 65 && e.ctrlKey === true) ||
			            // Allow: Ctrl+v
			        (e.keyCode === vKey && ctrlDown) ||
			            // Allow: Ctrl+c
			        (e.keyCode === cKey && ctrlDown) ||
			            // Allow: home, end, left, right, down, up
			        (e.keyCode >= 35 && e.keyCode <= 40)) {
			    	
			    	// let it happen, don't do anything
			        return true;
			    }
			    if((e.keyCode === vKey && ctrlDown))
			    	return true;
			    // Ensure that it is a number and stop the keypress
			    if (((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)))
			    	e.preventDefault();
			});
			
			$(document).on('keyup','.textoIptSearchRuc', function (e) {
				var code = (e.keyCode ? e.keyCode : e.which);
				var valor = ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val();
				
				if(!(ref.validarStrNum(valor) && ref.isNumericStr(valor))){
					
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val("");
				}else{
					
				    if (code === 13) {//ENTER 
				    	if(ref.validarBusquedaEntidadExternaNacionalSunat()){
				    		setTimeout(function() {
								ref._buscarEntidadExternaNacionalSunat();
							}, 200);
				    	}
				    }
				}
				
		    });
			/*.on('paste', function(evt) {
		    	ref.catchPaste(evt, this, function(clipData, element) {
		    		if(!(ref.validarStrNum(clipData) && ref.isNumericStr(clipData))){
						ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val("");
					}
		    	});
		    })*/
			
			//TICKET 9000003934
			$(document).on('keyup','.textoIptSearchRazonSocial', function (e) {
				var code = (e.keyCode ? e.keyCode : e.which);
				if (code === 13) {//ENTER 
			    	if(ref.validarBusquedaEntidadExternaNacionalSunat()){
			    		setTimeout(function() {
							ref._buscarEntidadExternaNacionalSunat();
						}, 200);
			    	}
			    }
			});
			
			//TICKET 9000003934
			ref.modulo.tabs.tabDestinatario.externos.compModal.on('hidden.bs.modal', function (e) {
				ref.inicializarTabla([]);
				ref._setDefaultValueDestinarioExterno();
			});
			
			//TICKET 9000003934
			ref.modulo.tabs.tabDestinatario.externos.compModalSearchRucRazonSocialEntidadExterna.on('hidden.bs.modal', function (e) {
				ref.inicializarTabla([]);
				ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val("");
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compTipoBuscarEntidadExternaNacionalSunat.change(function(){
				var val = $(this).val();
				ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val("");
				ref.inicializarTabla([]);
				if(val == "RUC"){
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.removeClass("textoIptSearchRazonSocial");
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.removeClass("textoIptSearchRuc");
					
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.addClass("textoIptSearchRuc");
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.attr("maxlength", '11');
				}else if(val == "RZ"){
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.removeClass("textoIptSearchRazonSocial");
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.removeClass("textoIptSearchRuc");
					
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.addClass("textoIptSearchRazonSocial");
					ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.attr("maxlength", '100');
				}
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compHead.click();
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compHead.click();
			ref.modulo.tabs.tabDestinatario.compInterno.show();
			ref.modulo.tabs.tabDestinatario.compExterno.hide();
			ref.modulo.tabs.tabDestinatario.compInternoMultiple.hide();
			
			ref.modulo.tabs.tabDestinatario.externos.compTipo.change(function(){
				
				/*var val = this.value;
				val = (val == "true");
				if(val == true){ //NACIONAL
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDireccion.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().show();
				}
				
				if(val == false){//INTERNACIONAL
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDireccion.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().hide();
				}*/
				
				ref._limpiarCamposDestinarioExterno();
				var val = $(this).val();
				var tipoEnvio = (val == "true");
				
				var tipoDestinatario = ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.filter(":checked").val();//TICKET 9000003934
				var isDespFisicoExterno = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");//TICKET 9000003934
				var isDespCorreoElectronico = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");//TICKET 9000003934
				var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");//TICKET 9000003934
				
				//TICKET 9000003934
				ref._showHideElementsDestinatarioExternoByTDTEConfDespacho(tipoDestinatario, isDespFisicoExterno, isDespCorreoElectronico, tipoEnvio, isIndicadorEntidadExternRucRZ);
				
				/*if(val == true && compTipoEntidadVal == "EE" && (isDespFisicoExterno && !isDespCorreoElectronico)){ //NACIONAL 
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDireccion.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().show();
				}
				
				if(val == false){//INTERNACIONAL
					ref.modulo.tabs.tabDestinatario.externos.compPais.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDepartamento.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compProvincia.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDistrito.parent().hide();
					ref.modulo.tabs.tabDestinatario.externos.compDireccion.parent().show();
					ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().hide();
				}*/
				
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compBtnModalSearchRZRUCEntidadExterna.click(function(){
				
				ref.inicializarTabla([]);
				if(ref.validarBusquedaEntidadExternaNacionalSunat()){
					setTimeout(function() {
						ref._buscarEntidadExternaNacionalSunat();
					}, 200);
				}
			});	
			
			//inicio TICKET 9000004765
			ref.modulo.tabs.tabDestinatario.internos.compDestinatarioDocPagar.change(function(){
				ref.esTipCorrespDocPagarCheckAnt = true;
				ref.destinatariosDocPagar = [];
				ref.correspondencia.detalleCorrespDestDocPagar = [];
				var d = new Date();
				var destinatarioDocPagar  = {};
				destinatarioDocPagar.identificador = d.getTime();
				destinatarioDocPagar.id = 0;
				destinatarioDocPagar.correspondencia = {};
				destinatarioDocPagar.destinarioDocPagar = {id: ref.modulo.tabs.tabDestinatario.internos.compDestinatarioDocPagar.filter(":checked").val()};
				
				ref.destinatariosDocPagar.push(destinatarioDocPagar);
			});
			//fin TICKET 9000004765
			
			//TICKET 9000003934
			ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.change(function(){
				ref._limpiarCamposDestinarioExterno();
				var val = this.value;
				var tipoEnvio = ref.modulo.tabs.tabDestinatario.externos.compTipo.filter(":checked").val();
				var isDespFisicoExterno = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");
				var isDespCorreoElectronico = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");
				var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");
				
				tipoEnvio = (tipoEnvio == "true");
				
				ref._showHideElementsDestinatarioExternoByTDTEConfDespacho(val, isDespFisicoExterno, isDespCorreoElectronico, tipoEnvio, isIndicadorEntidadExternRucRZ);
			});
			
			//TICKET 9000003934
			ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronicoAndDespFisico.change(function() {
				ref._limpiarCamposDestinarioExterno();
				var idNombreCEDF = $(this).attr("id");
				var tipoDestinatario = ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.filter(":checked").val();
				var tipoEnvio = ref.modulo.tabs.tabDestinatario.externos.compTipo.filter(":checked").val();
				var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");
				var isDespFisicoExterno = false;
				var isDespCorreoElectronico = false;
				
				if(idNombreCEDF == ref.modulo.tabs.tabDestinatario.externos.idDespFisicoExterno){
					isDespFisicoExterno = this.checked;
					isDespCorreoElectronico = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");
				}
				else if(idNombreCEDF == ref.modulo.tabs.tabDestinatario.externos.idDespCorreoElectronico){
					isDespCorreoElectronico = this.checked;
					isDespFisicoExterno = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");
				}
				
				tipoEnvio = (tipoEnvio == "true");
				
				ref._showHideElementsDestinatarioExternoByTDTEConfDespacho(tipoDestinatario, isDespFisicoExterno, isDespCorreoElectronico, tipoEnvio, isIndicadorEntidadExternRucRZ);
		    });
			
			//TICKET 9000003934
			ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.click(function(){
				ref._limpiarCamposDestinarioExterno();
				ref._showHideRazonSocialByIndicadorEntidadExternRucRZ(this.checked);
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.change(function(e){
				ref.archivosSeleccionados = e.target.files;
				// TICKET 900004270
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.submit();
				// FIN TICKET
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.submit(function(e){
				/*e.preventDefault();
				if(ref.archivosSeleccionados.length > 0){
					var maxSizeMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size');
					var maxSize = maxSizeMB * 1048576;
					var tamanioArchivo = ref.archivosSeleccionados[0].size;
					if(tamanioArchivo > maxSize){
						ref.modSistcorr.notificar("ERROR", 'El tamampermitido es de ' + maxSizeMB + " MB", "Error");
						return false;
					} else{
						ref._agregarArchivoAdjunto();
					}
				}*/
				e.preventDefault();
				if(ref.archivosSeleccionados.length > 0){
					var esPrincipal = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked");
					var maxSizeMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size');
					var maxSizeSinFirmaDigitalMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size-sin-firma-digital');
					var maxSize = maxSizeMB * 1048576;
					var maxSizeSinFirmaDigital = maxSizeSinFirmaDigitalMB * 1048576;
					var tipCorres = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compTipoEmisionD.filter(":checked").val();
					
					// TICKET 9000004270
					var d = new Date();
					var identificadorIterativo = d.getTime();
					// FIN TICKET
					for(var indice = 0; indice < ref.archivosSeleccionados.length; indice++){
						var tamanioArchivo = ref.archivosSeleccionados[indice].size;
						var tamanioMaximo = 0;
						var tamanioMaximoMB = 0;
						
						
						
						if(tipCorres == "2" && !esPrincipal){
							tamanioMaximo = maxSizeSinFirmaDigital;
							tamanioMaximoMB = maxSizeSinFirmaDigitalMB;
						}else{
							tamanioMaximo = maxSize;
							tamanioMaximoMB = maxSizeMB;
						}
						
						console.log("Tamaño del archivo " + ref.archivosSeleccionados[indice].name + ": " + tamanioArchivo + " (" + tamanioMaximo + ")");
						
						if(tamanioArchivo > tamanioMaximo){
							ref.modSistcorr.notificar("ERROR", 'El tamaño permitido es de ' + tamanioMaximoMB + " MB, el archivo " + ref.archivosSeleccionados[indice].name + " excede dicho tamaño.", "Error");
							//return false;
						} else{
							// TICKET 9000004270
							//ref._agregarArchivoAdjunto();
							//var d = new Date();
							var adjunto  = {};
							//adjunto.identificador = d.getTime();
							adjunto.identificador = identificadorIterativo;
							var nombreArcDP = ref.archivosSeleccionados[indice].name;
							//adjunto.principal = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked");
							adjunto.principal = ((ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val() == ref.modulo.codigoDocPagar && (nombreArcDP && nombreArcDP.indexOf(".pdf", nombreArcDP.length - 4) >= 0))?(true):(false));//TICKET 9000004904
							adjunto.file = ref.archivosSeleccionados[indice];
							adjunto.tipo = (adjunto.principal == true ? 'PRINCIPAL' : 'SECUNDARIO');//TICKET 9000004904
							
							//adjunto.tipo = 'SECUNDARIO';//TICKET 9000004904 COMENTADO
							adjunto.nombre = ref.archivosSeleccionados[indice].name;
							adjunto.contentType = ref.archivosSeleccionados[indice].type;
							adjunto.tamanio = tamanioArchivo/1048576;
							adjunto.indicadorRemoto = 'N';
							ref.archivosAdjuntos.push(adjunto);
							identificadorIterativo++;
							// FIN TICKET
						}
					}
				}
				// TICKET 9000004270
				ref._actualizarListaArchivosAdjuntos();
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.trigger("reset");
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compModal.modal('hide');
				// FIN TICKET
			});
			
			ref.modulo.tabs.tabDestinatario.internos.compBtnAgregarDest.click(function(){
				if(ref._validarDestinatarioInterno() == true){
					ref._agregarDestinatarioInterno();
					ref.modulo.tabs.tabDestinatario.internos.compModal.modal('hide');
				} else {
					return;
				}
			});
			
			ref.modulo.tabs.tabDestinatario.internos.compBtnCancelarAgregar.click(function(){
				ref._limpiarFormularioDestinarioInterno();
			});
			
			ref.modulo.tabs.tabDestinatario.internos.compBtnCloseModal.click(function(){
				ref._limpiarFormularioDestinarioInterno();
			});
			
			
			
			ref.modulo.tabs.tabDestinatario.externos.compBtnAgregarDest.click(function(){
				if(ref._validarDestinatarioExterno() == true){
					ref._agregarDestinatarioExterno();
					ref.modulo.tabs.tabDestinatario.externos.compModal.modal('hide');
				} else{
					return;
				}
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compBtnCancelarAgregar.click(function(){
				ref._limpiarFormularioDestinarioExterno();
			});
			
			ref.modulo.tabs.tabDestinatario.externos.compBtnCloseModal.click(function(){
				ref._limpiarFormularioDestinarioExterno();
			});
			
			
			
			ref.modulo.tabs.tabCopias.compBtnAgregar.click(function(){
				if(ref._validarCopia() == true){
					ref._agregarCopia();
					ref.modulo.tabs.tabCopias.compModal.modal('hide');
				} else {
					return;
				}
			});
			
			ref.modulo.tabs.tabCopias.compBtnCancelarAgregar.click(function(){
				ref._limpiarFormularioCopia();
			});
			
			ref.modulo.tabs.tabCopias.compBtnCloseModal.click(function(){
				ref._limpiarFormularioCopia();
			});
			
			
			
			ref.modulo.compBtnCancelar.click(function(){
				ref.modSistcorr.procesar(true);
				if(ref.correspondencia.id == 0){
					ref.modulo.abrirBandejaPendiente(ref.esEdicion);
				} else {
					if(ref.correspondencia.firmaDigital == true){
						if(ref.correspondencia.estado.bandeja == 'firmado'){
							ref.modulo.abrirBandejaFirmado(ref.esEdicion);
						} else if(ref.correspondencia.estado.bandeja == 'pendiente'){
							ref.modulo.abrirBandejaPendiente(ref.esEdicion);
						}
					} else {
						ref.modulo.abrirBandejaPendiente(ref.esEdicion);
					}
				}
			})
			
			ref.modulo.compBtnGuardar.click(function(){
				ref.modulo.compModalConfirmarRegistro.modal('show');	
			});	
			
			
			ref.modulo.btnConfirmarRegistro.click(function(){
				ref.modulo.compModalConfirmarRegistro.modal('hide');
				setTimeout(function() {
					if(ref.esEdicion == false){
						ref._registrarCorrespondencia();
					} else{
						ref._modificarCorrespondencia();
					}
				}, 200);
			});
			
			ref.modulo.btnConfirmarAsignarFirmaSi.click(function(){
				ref.modulo.compModalConfirmarAsignarFirma.modal('hide');				
				setTimeout(function() {
					ref.modulo.compModalSeleccionarFirmante.modal('show');
				}, 200);
				
			});
			
			ref.modulo.btnConfirmarAsignarFirmaNo.click(function(){
				ref.modSistcorr.procesar(true);
				if(ref.correspondenciaRespuesta.firmaDigital == true){
					if(ref.correspondenciaRespuesta.estado.bandeja == 'firmado'){
						ref.modulo.abrirBandejaFirmado(ref.esEdicion);
					} else if(ref.correspondenciaRespuesta.estado.bandeja == 'pendiente'){
						ref.modulo.abrirBandejaPendiente(ref.esEdicion);
					}
				} else {
					ref.modulo.abrirBandejaPendiente(ref.esEdicion);
				}
			});
			
			ref.modulo.btnSeleccionarFirmanteAceptar.click(function(){
				if(ref.modulo.compFirmante.val() == 'remitente'){
					ref.modulo.compModalSeleccionarFirmante.modal('hide');
					ref._asignarFirmanteAutomatico();
				} else if(ref.modulo.compFirmante.val() == 'otros'){
					ref.modulo.compModalSeleccionarFirmante.modal('hide');
					ref.modulo.abrirAsignarFirma(ref.correspondenciaRespuesta.id, ref.esEdicion);
				} else {
					ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una opción', "Error");
				}
				
			});
			
			ref.modulo.btnSeleccionarFirmanteRutaAceptar.click(function(){
				ref._asignarFirmanteRutaAprobacion()
				
			});
			
			ref.modulo.btnSeleccionarFirmanteCancelar.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modulo.abrirBandejaPendiente(ref.esEdicion);
			});
			
			ref.modulo.btnSeleccionarFirmanteRutaCancelar.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modulo.abrirBandejaPendiente(ref.esEdicion);
			});
			
			setTimeout(function(){
				$("." + ref.modulo.classDescargarArchivo).click(function(event){
					var $archivo = $(event.currentTarget);
					var id = $archivo.data("identificador");
					ref.modulo.descargarArchivo(id, ref.esEdicion);
				});
			}, 1000);
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.btnOpenModalNuevoArchivo.click(function(){
				var _val = $("input:radio[name=corr_firmaDigital]:checked").val();
				_val = (_val == 'true');
				if(_val == true){
					ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked", true );
				}
				// TICKET 9000004270
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.click();
				// FIN TICKET
			});
			
			$("input[name="+ref.modulo.nameRutaAprobacion+"]").change(function(){
				var obj = $(this);
				setTimeout(function(){
					console.log("Valor obtenido:" + obj.val());
					if(obj.val()==1){
						$("#RutaAprobacion-tab").closest('li').show();
						if(ref.rutaAprobacion.length==0){
							ref._generarTablaRutaAprobacionGeneral();
						}
					}else if(obj.val()==0){
						$("#RutaAprobacion-tab").closest('li').hide();
					}
				}, 500)
			});
			
			$("#corr_ruta_aprobacion_no").on('click', function(event){
				/*if(e.preventDefault){
					console.log("Funciona el preventDefault");
				}else{
					console.log("No funciona el preventDefault");
				}*/
				//event.returnValue = false;
				event.preventDefault();
				$("input[name="+ref.modulo.nameRutaAprobacion+"]").val("1");
				$("#corr_ruta_aprobacion_si").prop('checked', true);
				$("#modalOcultarRutaAprobacion").modal('show');
			});
			
			$("#btnOcultarRutaAprobacionAceptar").click(function(){
				$("#corr_ruta_aprobacion_no").prop('checked', true);
				$("#RutaAprobacion-tab").closest('li').hide();
				$("#btnOcultarRutaAprobacionCancelar").click();
				ref.rutaAprobacion = [];
			});
			
		},
		
		//TICKET 9000003934
		_showHideElementsDestinatarioExternoByTDTEConfDespacho: function(tipoDestinatario, isDespachoFisico, isDespachoEmail, tipoEnvio, isIndicadorEntidadExternRucRZ){
			
			var ref = this;
			
			var confFields = ref.modulo.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO || [];
			var namePropertyConfig = "";
			namePropertyConfig = "" + tipoDestinatario + ((isDespachoFisico)?("_DF"):("")) + ((isDespachoEmail)?("_CE"):("")) + ((tipoEnvio)?("_EN"):("_EI"));
			
			var showHideFieldList = confFields[namePropertyConfig];
			var acumNumParent = 1;
			if(showHideFieldList){
				
				for(var k = 0; k < showHideFieldList.length; k++){
					var objectElementShowHide = $("#"+showHideFieldList[k].idField);
					acumNumParent = 1;
					//Llegar hasta el obtjeto que se va ocultar o mostrar
					while(acumNumParent <= showHideFieldList[k].numParent){
						objectElementShowHide = objectElementShowHide.parent();
						acumNumParent++;
					}
					if(showHideFieldList[k].hasOwnProperty("md")){
						objectElementShowHide.removeClass();
						objectElementShowHide.addClass("form-group col-md-"+showHideFieldList[k].md);
					}
					if(showHideFieldList[k].accion == "show"){
						objectElementShowHide.show();
					}else{
						objectElementShowHide.hide();
					}
					objectElementShowHide = null;
				}
				if(tipoEnvio && tipoDestinatario == "EE")
					ref._showHideRazonSocialByIndicadorEntidadExternRucRZ(isIndicadorEntidadExternRucRZ);
			}
		},
		
		_showHideRazonSocialByIndicadorEntidadExternRucRZ: function(isIndicadorEntidadExternRucRZ){
			var ref = this;
			if(isIndicadorEntidadExternRucRZ) {
				ref.modulo.tabs.tabDestinatario.externos.compBtnDependenciaNacRUCRazonSocialSunat.parent().show();
				ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().parent().hide();
	        }else{
	        	ref.modulo.tabs.tabDestinatario.externos.compBtnDependenciaNacRUCRazonSocialSunat.parent().hide();
				ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.parent().parent().show();
	        }
		},
		
		_generarTablaRutaAprobacionGeneral: function(){
			var ref = this;
			if(ref.habilitarDependenciasSuperiores == true){
				var jerarquia = $("#corr_jerarquia").prop('checked'); 
				if(jerarquia == false){
					ref._generarTablaRutaAprobacion();
				}else{
					ref.rutaAprobacion = [];
					
					var dep = $("#rmt_dependencia").val();
					if(dep.trim() != ""){
						var d = new Date();
						var aprob = {};
						var newOrden = 0;
						aprob.orden = ref.rutaAprobacion.length + 1;
						aprob.id = d.getTime() + dep;
						aprob.tipoFirmante = "1";
						aprob.nombreTipo = "Jefe Dependencia";
						aprob.codDependencia = dep;
						aprob.dependenciaNombre = $("#rmt_dependencia option:selected").text();
						aprob.usuario = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data("codusuario");
						aprob.usuarioNombre = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val();
						aprob.estado = "PENDIENTE";
						ref.rutaAprobacion.splice(ref.rutaAprobacion.length, 0, aprob);
						console.log("Aprob");
						console.log(aprob);
					}
					var primerFirmante = $("#corr_primerFirmante").prop('checked');
					if(primerFirmante==true){
						ref._agregarPrimerFirmante();
					}
					ref.rutaAprobacion.sort(function(a,b){
						if(a.orden > b.orden){
							return 1;
						}
						if(a.orden < b.orden){
							return -1;
						}
						return 0;
					})
					ref._refrescarTablaRutaAprobacion();
				}
			}
		},
		
		inicializarCorrespondencia: function(){
			var ref = this;
			ref.correspondencia = {
				id: 0,
				despachoFisico: false,	
				confidencial: false, 
				urgente: false,	
				firmaDigital: false,
				primerFirmante: false,
				detalleInterno: [],
				detalleExterno: [],
				detalleCorrespDestDocPagar: [],//ticket 9000004765
				detalleCopia: [],
				archivos: [],
				nroFlujo: 1,
				estado: {bandeja: 'pendiente'}
			};
			ref.archivosAdjuntos = [];
			ref.destinatariosExternos= [];
			ref.destinatariosInternos= [];
			ref.copias = [];
			ref.actualizarValoresPorDefecto_Formulario();
		},
		
		obtenerCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.archivosAdjuntos = [];
			ref.destinatariosExternos= [];
			ref.destinatariosInternos= [];
			ref.copias = [];
			ref.modulo.obtenerCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.correspondencia = respuesta.datos[0];
						ref.listas.dependencia.agregarLista([{'id': ref.correspondencia.codDependenciaOriginadora, 'text': ref.correspondencia.dependenciaOriginadora}]);
						ref.actualizarValores_Formulario();
						ref.validarDestinatariosCopias();
						var estado = respuesta.datos[0].estado.id;
						var firma_digital = respuesta.datos[0].firmaDigital;
						var ruta_aprobacion = respuesta.datos[0].rutaAprobacion;
						console.log("Estado: " + estado);
						console.log("Firma Digital: " + firma_digital);
						console.log("Ruta Aprobacion: " + ruta_aprobacion);
						if(ruta_aprobacion==false){
							$("#RutaAprobacion-tab").closest('li').hide();
						}
						if(firma_digital == true && ruta_aprobacion == true && (estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA || estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || 
								estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA || estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA || estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR || 
								estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA || estado==CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA)){
							setTimeout(function(){
								ref._desactivarCamposFormulario();
							}, 1000);
						}
						if(ref.correspondencia.rutaAprobacion==true){
							ref.modulo.obtenerRutaAprobacion()
								.then(function(respuesta){
									ref.tablaRutaEdicion = true;
									if(respuesta.estado==true){
										setTimeout(function(){
											ref.rutaAprobacion = respuesta.datos;
											ref.rutaAprobacion.sort(function(a,b){
												if(a.orden > b.orden){
													return 1;
												}
												if(a.orden < b.orden){
													return -1;
												}
												return 0;
											})
											ref._refrescarTablaRutaAprobacion();
											ref.habilitarDependenciasSuperiores = true;
										}, 1000);
									}
								}).catch(function(error){
									ref.tablaRutaEdicion = true;
									ref.modSistcorr.procesar(false);
									ref.modSistcorr.showMessageErrorRequest(error);
									ref.habilitarDependenciasSuperiores = true;
								});
						}else{
							ref.tablaRutaEdicion = true;
							ref.habilitarDependenciasSuperiores = true;
						}
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.habilitarDependenciasSuperiores = true;
				});
		},
		
		_desactivarCamposFormulario: function(){
			var ref = this;
			$("#corr_ruta_aprobacion_si").prop('disabled', true);
			$("#corr_ruta_aprobacion_no").prop('disabled', true);
			$("#rmt_fechaDocumento").prop('disabled', true);
			$("#btnRmt_FechaDocumento").prop('disabled', true);
			$("#btnRmt_FechaDocumento").off('click');
			$("#rmt_tipoCorrespondencia").prop('disabled', true);
			//$("#rmt_tipoCorrespondencia").change();
			$("#rmt_tipoCorrespondenciaOtros").prop('disabled', true);
			$("#rmt_asunto").prop('disabled', true);
			$("#corr_tipoemisionInterna").prop('disabled', true);
			$("#corr_tipoemisionExterna").prop('disabled', true);
			$("#corr_flujo_firma_manual").prop('disabled', true);
			$("#corr_flujo_firma_digital").prop('disabled', true);
			$("#corr_primerFirmante").prop('disabled', true);
			$("#corr_confidencial").prop('disabled', true);
			$("#corr_urgente").prop('disabled', true);
			$("#corr_despachoFisico").prop('disabled', true);
			$("#corr_observacion").prop('disabled', true);
			$("#btnOpenModalNuevoArchivo").prop('disabled', true);
			$(".btnEliminarAdjunto").prop('disabled', true);
			$(".btnEliminarAdjunto").off('click');
			$("#btnOpenModalDI").prop('disabled', true);
			$("#btnOpenModalDE").prop('disabled', true);
			$("#btnOpenModalCopias").prop('disabled', true);
			$(".btnEliminarDestinatarioInterno").off('click');
			$(".btnEliminarDestinatarioExterno").off('click');
			// TICKET 9000004270
			$(".archivo-principal").off('change');
			$(".archivo-principal").attr('disabled', true);
			
			// FIN TICKET
			//inicio ticket 9000004765
			$(".correosDestDocPagar").off('change');
			$(".correosDestDocPagar").attr('disabled', true);
			//fin ticket 9000004765
		},
		
		actualizarValoresPorDefecto_Formulario: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.prop('disabled', 'disabled');
			if(ref.dependenciasUsuario.length == 1){
				var _dep = ref.dependenciasUsuario[0];
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.append("<option value='"+_dep.id+"' selected='selected'>"+_dep.text+"</option>");
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.change();
				
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.append("<option value='"+_dep.id+"' selected='selected'>"+_dep.text+"</option>");
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change();
				
				ref.listas.dependencia.agregarLista([{'id': _dep.id, 'text': _dep.text}]);
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.prop('disabled', 'disabled');
			}
			$("input:radio[name=corr_tipoEmision][value='1']").prop('checked', true);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop('checked', ref.correspondencia.despachoFisico);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compConfidencial.prop('checked', ref.correspondencia.confidencial);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compUrgente.prop('checked', ref.correspondencia.urgente);
			$("input:radio[name=corr_firmaDigital][value='"+ref.correspondencia.firmaDigital+"']").prop('checked', true);
			$("input:radio[name=corr_firmaDigital]").change();
			//ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.prop('checked', ref.correspondencia.firmaDigital);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop('checked', ref.correspondencia.primerFirmante);
			//ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.change();
			ref._actualizarListaArchivosAdjuntos();
			ref._actualizarListaDestinatariosInternos();
			ref._actualizarListaDestinatariosExternos();
			ref._actualizarListaCopias();
		},
		
		actualizarValores_Formulario: function(){
			var ref = this;
			for(var i in ref.correspondencia.adjuntos){
				var adjunto = ref.modSistcorr.clonarObjecto({}, ref.correspondencia.adjuntos[i]);
				adjunto.identificador = ref.correspondencia.adjuntos[i].id;
				adjunto.file = null;
				adjunto.tipo = adjunto.principal == true ? 'PRINCIPAL' : 'SECUNDARIO';
				ref.archivosAdjuntos.push(adjunto); 
			}
			for(var i in ref.correspondencia.detalleInterno){
				var interno = ref.modSistcorr.clonarObjecto({}, ref.correspondencia.detalleInterno[i]);
				interno.identificador = ref.correspondencia.detalleInterno[i].id;
				ref.destinatariosInternos.push(interno);
			}
			for(var i in ref.correspondencia.detalleExterno){
				var externo = ref.modSistcorr.clonarObjecto({}, ref.correspondencia.detalleExterno[i]);
				externo.identificador = ref.correspondencia.detalleExterno[i].id;
				ref.destinatariosExternos.push(externo);
			}
			for(var i in ref.correspondencia.detalleCopia){
				var copia =  ref.modSistcorr.clonarObjecto({}, ref.correspondencia.detalleCopia[i]);
				copia.identificador = ref.correspondencia.detalleCopia[i].id;
				ref.copias.push(copia);
			}
			
			//inicio ticket 9000004765
			for(var i in ref.correspondencia.detalleCorrespDestDocPagar){
				var deta =  ref.modSistcorr.clonarObjecto({}, ref.correspondencia.detalleCorrespDestDocPagar[i]);
				deta.identificador = ref.correspondencia.detalleCorrespDestDocPagar[i].id;
				ref.destinatariosDocPagar.push(deta);
			}
			//fin ticket 9000004765
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.append("<option value='"+ref.correspondencia.codDependenciaOriginadora+"' selected='selected'>"+ref.correspondencia.dependenciaOriginadora+"</option>");
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.append("<option value='"+ref.correspondencia.codLugarTrabajo+"' selected='selected'>"+ref.correspondencia.lugarTrabajo+"</option>");
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.append("<option value='"+ref.correspondencia.codDependencia+"' selected='selected'>"+ref.correspondencia.dependencia+"</option>");
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(ref.correspondencia.remitente);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codUsuario', ref.correspondencia.codRemitente);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val(ref.correspondencia.codTipoCorrespondencia);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.change();
			//inicio ticket 9000004765
			if(ref.correspondencia.codTipoCorrespondencia == ref.modulo.codigoDocPagar && ref.correspondencia.detalleCorrespDestDocPagar.length > 0){
				ref.modulo.tabs.tabDestinatario.internos.compDestinatarioDocPagar.filter('[value='+ref.correspondencia.detalleCorrespDestDocPagar[0].destinarioDocPagar.id+']').prop('checked', true);
				ref.modulo.tabs.tabDestinatario.internos.compDestinatarioDocPagar.change();
			}
			//fin ticket 9000004765
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.val(ref.correspondencia.tipoCorrespondenciaOtros);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.change();
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.prop('checked', ref.correspondencia.jerarquia);
			if(ref.correspondencia.rutaAprobacion==true){
				$("#corr_ruta_aprobacion_si").click();
			}else{
				//$("#corr_ruta_aprobacion_no").click();
				$("#corr_ruta_aprobacion_no").prop('checked', true);
			}
			
			setTimeout(function() {
				console.log("Correspondencia remitente:" + ref.correspondencia.remitente);
				console.log("Correspondencia remitente 2:" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val());
				if(ref.correspondencia.remitente != ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val()){
					ref.modSistcorr.procesar(true);
					if(ref._validar_Formulario() == true){
						ref.modulo.modificarRemitenteCorrespondencia(ref.correspondencia.id, ref.correspondencia.codRemitente)
						//ref.modulo.modificarCorrespondencia(ref.correspondencia, ref.archivosAdjuntos)
							.then(function(respuesta){
								if(respuesta.estado == true){
									ref.modSistcorr.notificar('OK', 'Se acaba de actualizar el campo Jefe Dependencia Remitente a ' + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(), 'Success');
									//ref.correspondenciaRespuesta = respuesta.datos[0];
								} else {
									ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
								}
								ref.modSistcorr.procesar(false);
							}).catch(function(error){
								ref.modSistcorr.procesar(false);
								ref.modSistcorr.showMessageErrorRequest(error);
							});
						
					} else {
						ref.modSistcorr.procesar(false);
					}
					
				}
			}, 1500);
			//ref.componentes.datePikers.fechaDocumento.destroy()
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val(ref.correspondencia.fechaDocumento);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.change();
			//ref.componentes.datePikers.fechaDocumento.value(ref.correspondencia.fechaDocumento);
			/*ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.datepicker({
				format: 'dd/mm/yyyy',
				locale: 'es-es',
	            autoclose: true,
	            startView: 0,
	            todayBtn: "linked",
	            todayHighlight: true,
	            //value: ref.correspondencia.fechaDocumento,
	            change: function(event){
	            	var $comp = $(event.currentTarget);
	            	console.log(event);
	            	ref.modSistcorr.datePicker_change($comp.attr('id'));
	            },
	            open: function(event){
	            	var $comp = $(event.currentTarget);
	            	ref.modSistcorr.datePicker_open($comp.attr('id'));
	            },
	            close: function(event){
	            	var $comp = $(event.currentTarget);
	            	ref.modSistcorr.datePicker_close($comp.attr('id'));
	            }
	        });*/
			
			/*setTimeout(function(){
				ref.componentes.datePikers.fechaDocumento.val(ref.correspondencia.fechaDocumento).change();
			}, 500);*/
			
			ref.listas.lugarTrabajo.agregarLista([{'id': ref.correspondencia.codLugarTrabajo, 'text': ref.correspondencia.lugarTrabajo}]);
			ref.listas.dependencia.agregarLista([{'id': ref.correspondencia.codDependencia, 'text': ref.correspondencia.dependencia}]);
			
			if(ref.esEdicion){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.prop('disabled', 'disabled');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.prop('disabled', 'disabled');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.prop('disabled', 'disabled');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.prop('disabled', 'disabled');
			}
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.change();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val(ref.correspondencia.fechaDocumento);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.val(ref.correspondencia.asunto);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.change();
			$("input:radio[name=corr_tipoEmision][value='"+ref.correspondencia.tipoEmision.id+"']").prop('checked', true);
			if(ref.correspondencia.tipoEmision.id==2){
				$("#despachoFisico").hide();
			}
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.prop('checked', ref.correspondencia.despachoFisico);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compConfidencial.prop('checked', ref.correspondencia.confidencial);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compUrgente.prop('checked', ref.correspondencia.urgente);
			$("input:radio[name=corr_firmaDigital][value='"+ref.correspondencia.firmaDigital+"']").prop('checked', true);
			$("input:radio[name=corr_firmaDigital]").change();
			//ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.prop('checked', ref.correspondencia.firmaDigital);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.prop('checked', ref.correspondencia.primerFirmante);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.val(ref.correspondencia.observaciones);
			ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.change();
			ref._actualizarListaArchivosAdjuntos();
			ref._actualizarListaDestinatariosInternos();
			ref._actualizarListaDestinatariosExternos();
			ref._actualizarListaCopias();
			ref.listas.lugarTrabajo.agregarLista([{"id": ref.correspondencia.codLugarTrabajo, "text": ref.correspondencia.lugarTrabajo}]);
			ref.listas.dependencia.agregarLista([{"id": ref.correspondencia.codDependencia, "text": ref.correspondencia.dependencia}]);
			
			if(ref.correspondencia.firmaDigital == true){
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop( "disabled", false );
			} else {
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop( "disabled", true );
			}

			//adicion 9-3874
			if (ref.correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR  &&
				ref.correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_FIRMA_MANUAL &&	
				ref.correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_DOCUMENTOS) {
				if(ref.correspondencia.firmaDigital == false){
					$("input[name=corr_firmaDigital]").attr("disabled",true);
					$("input[name=corr_primerFirmante]").attr("disabled",true);
				}
			} else {
				if(ref.correspondencia.firmaDigital == true){
					if (ref.correspondencia.adjuntos.length > 0 ){
						for(var i in ref.archivosAdjuntos){
							if(ref.archivosAdjuntos[i].tipo == 'PRINCIPAL'){
								$("input[name=corr_firmaDigital]").attr("disabled",true);
							}
						}						
					}					
				}
			}
			//fin adicion 9-3874			
			
			// TICKET 9000004044
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuesta.val(ref.correspondencia.estadoDocumentoRespuesta);
			if(ref.correspondencia.estadoDocumentoRespuesta == '1'){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.val('SI');
			}else{
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuestaTexto.val('NO');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.closest('div').hide();
				$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.attr('id') + "']").hide();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.closest('div').hide();
				$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.attr('id') + "']").hide();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.closest('div').hide();
			}
			$("label[for='rmt_esDocumentoRespuestaTexto']").addClass('active');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compIdAsignacion.val(ref.correspondencia.idAsignacion || 0);
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.val(ref.correspondencia.correlativoEntrada || '');
			$("label[for='rmt_correlativoEntrada']").addClass('active');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.val(ref.correspondencia.tipo || '');
			$("label[for='rmt_tipoAccion']").addClass('active');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.val(ref.correspondencia.respuesta || '');
			$("label[for='rmt_respuesta']").addClass('active');
			if(ref.correspondencia.estadoDocumentoRespuesta == '1' && ref.modulo.tabs.tabDatos.accordion.datosRemitente.compIdAsignacion.val() == '0'){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.hide();
				$("label[for='" + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.attr('id') + "']").hide();
			}
			// FIN TICKET
		},

		_obtnerFirmante: function() {
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', 'www');
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
			ref.modulo.obtenerFirmante(ref.indiceURLS, ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val())
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(respuesta.datos[0].text);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', respuesta.datos[0].id);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTextoJefeDependencia.text(respuesta.datos[0].text);
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
					}
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		// TICKET 9000003943
		_obtenerJefeDependenciaRutaAprobacion: function() {
			var ref = this;
			//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val('');
			//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', 'www');
			//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.change();
			ref.modulo.obtenerFirmante(ref.indiceURLS, ref.modulo.tabs.tabRutaAprobacion.compDependencia.val())
				.then(function(respuesta){
					console.log("obtenerJefeDependenciaRutaAprobacion");
					console.log(respuesta);
					if(respuesta.estado == true){
						console.log(respuesta.datos[0]);
						if(respuesta.datos[0] != null){
							$("#firmante_nombre").removeAttr('disabled');
							$("#firmante_nombre").append('<option value="'+respuesta.datos[0].codigo+'">'+respuesta.datos[0].descripcion+'</option>');
							$("#firmante_nombre").val(respuesta.datos[0].codigo);
							$("#firmante_nombre").change();
							$("#firmante_nombre").attr('disabled', true);
							$("label[for=firmante_nombre]").addClass('active');
						}else{
							$("#firmante_nombre").removeAttr('disabled');
							$("#firmante_nombre").val("");
							$("#firmante_nombre").change();
							$("#firmante_nombre").attr('disabled', true);
							$("label[for=firmante_nombre]").removeClass('active');
						}
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(respuesta.datos[0].text);
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data('codusuario', respuesta.datos[0].id);
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTextoJefeDependencia.text(respuesta.datos[0].text);
					}
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		// FIN TICKET
		
		_obtenerLugarXDependencia: function(){
			var ref = this;
			ref.modulo.obtenerLugar(ref.indiceURLS, ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val())
				.then(function(respuesta){
					if(respuesta.estado == true){
						var _lugar = respuesta.datos[0];
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.val(_lugar.id);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.data('nombreLugar', _lugar.text);
						ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTextoLugarTrabajo.text(_lugar.text);
						ref._obtnerFirmante();
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.append("<option value='"+_lugar.id+"' selected='selected'>"+_lugar.text+"</option>");
						//ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.change();
						
						ref.listas.lugarTrabajo.agregarLista([{'id': _lugar.id, 'text': _lugar.text}]);
					}
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		_agregarArchivoAdjunto: function(){
			var ref = this;
			if(ref.archivosSeleccionados.length > 0){
				var d = new Date();
				var adjunto  = {};
				adjunto.identificador = d.getTime();
				adjunto.principal = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked");
				adjunto.file = ref.archivosSeleccionados[0];
				adjunto.tipo = adjunto.principal == true ? 'PRINCIPAL' : 'SECUNDARIO';
				adjunto.nombre = ref.archivosSeleccionados[0].name;
				adjunto.contentType = ref.archivosSeleccionados[0].type;
				ref.archivosAdjuntos.push(adjunto);
				ref._actualizarListaArchivosAdjuntos();
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.trigger("reset");
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compModal.modal('hide');
				return false;
			}
			return true;
		},
		
		// TICKET 9000003943
		_agregarRutaAprobacion: function(posicion, tipo, nombreTipo, dependencia, nombreDependencia, usuario, nombreUsuario){
			var ref = this;
			var encontradoUsuario = false;
			var encontradoDependencia = false;
			for(var j=0;j<ref.rutaAprobacion.length;j++){
				if(ref.rutaAprobacion[j].usuario == usuario && ref.rutaAprobacion[j].usuario.trim() != "" && (ref.rutaAprobacion[j].estado == 'Pendiente' || ref.rutaAprobacion[j].estado == 'PENDIENTE' || 
						(ref.rutaAprobacion[j].firmante && (ref.rutaAprobacion[j].firmante.estado.id==CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA || ref.rutaAprobacion[j].firmante.estado.id==CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA)))){
					encontradoUsuario = true;
				}
				if(tipo=="1" && dependencia==ref.rutaAprobacion[j].codDependencia && (ref.rutaAprobacion[j].estado == 'Pendiente' || ref.rutaAprobacion[j].estado == 'PENDIENTE' || 
						ref.rutaAprobacion[j].firmante.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA || ref.rutaAprobacion[j].firmante.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || 
						ref.rutaAprobacion[j].firmante.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA)){
					encontradoDependencia = true;
				}
			}
			if(encontradoUsuario==true || encontradoDependencia==true){
				if(encontradoUsuario){
					ref.modSistcorr.notificar('ERROR', 'El usuario indicado ya se encuentra en la ruta de Aprobación.', 'Warning');
				}else{
					ref.modSistcorr.notificar('ERROR', 'La dependencia indicada ya se encuentra en la ruta de Aprobación.', 'Warning');
				}
			}else{
				var d = new Date();
				var aprob = {};
				var newOrden = 0;
				if(ref.idRowSelected==0){
					newOrden = ref.rutaAprobacion.length;
					ref.rutaAprobacion[ref.rutaAprobacion.length-1].orden = ref.rutaAprobacion[ref.rutaAprobacion.length-1].orden + 1;
				}else{
					for(var i=0;i<ref.rutaAprobacion.length;i++){
						if(ref.rutaAprobacion[i].id == ref.idRowSelected){
							newOrden = ref.rutaAprobacion[i].orden + 1;
							//aprob.orden = ref.rutaAprobacion[i].orden + 1;
						}
					}
					for(var i=0;i<ref.rutaAprobacion.length;i++){
						if(ref.rutaAprobacion[i].orden >= newOrden){
							ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden + 1;
						}
					}
				}
				
				aprob.id = d.getTime() + dependencia;
				aprob.orden = newOrden;
				aprob.tipoFirmante = tipo;
				aprob.nombreTipo = nombreTipo;
				aprob.codDependencia = dependencia;
				aprob.dependenciaNombre = nombreDependencia;
				aprob.usuario = usuario;
				aprob.usuarioNombre = nombreUsuario;
				aprob.estado = "PENDIENTE";
				ref.rutaAprobacion.splice(posicion, 0, aprob);
				ref.rutaAprobacion.sort(function(a,b){
					if(a.orden > b.orden){
						return 1;
					}
					if(a.orden < b.orden){
						return -1;
					}
					return 0;
				})
				ref.modulo.tabs.tabRutaAprobacion.compBtnCloseModal.click();
			}
		},
		
		_refrescarTablaRutaAprobacion: function(){
			var ref = this;
			ref.idRowSelected = 0;
			if($("input[name=corr_rutaAprobacion]:checked").val()==1){
				var primerPendiente = true;
				var ultimoPendiente = true;
				var codigoHtml = "";
				var primer = 0;
				var ultimoFirmanteAsignado = -1;
				for(var k=0;k<ref.rutaAprobacion.length;k++){
					if(ref.rutaAprobacion[k].firmante != null){
						ultimoFirmanteAsignado = k;
					}
				}
				console.log("ultimo firmante asignado:" + ultimoFirmanteAsignado);
				var primerFirmanteVal = $("#corr_primerFirmante").prop('checked'); 
				var primerFirmante = 0;
				if(primerFirmanteVal==true){
					var username = $("#main_username").val();
					if(ref.rutaAprobacion.length > 0 && username==ref.rutaAprobacion[0].usuario){
						primer = primer + 1;
						primerFirmante = 1;
					}else{
						primerFirmante = 0;
					}
				}else{
					primerFirmante = 0;
				}
				console.log("Primer Firmante:" + primerFirmante);
				console.log("primer:" + primer);
				var ultimo = ref.rutaAprobacion.length - 2;
				console.log("ultimo:" + ultimo);
				for(var i=0;i<ref.rutaAprobacion.length;i++){
					var aprob = ref.rutaAprobacion[i];
					var class_ = "";
					if(aprob.estado=="PENDIENTE" || aprob.estado=="Pendiente"){
						class_ = "col_select";
						if(i>ultimo){
							class_ = "";
						}
						if(aprob.nombreTipo=="Participante"){
							codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td style="text-align: center;" class="'+class_+'">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+' / '+aprob.usuarioNombre+'</td><td class="'+class_+'">Pendiente</td>';
						}else{
							if(aprob.usuarioNombre.trim()==""){
								codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td style="text-align: center;" class="'+class_+'">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+' / '+aprob.dependenciaNombre+'</td><td class="'+class_+'">Pendiente</td>';
							}else{
								codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td style="text-align: center;" class="'+class_+'">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+' / '+aprob.dependenciaNombre+' ('+aprob.usuarioNombre+')</td><td class="'+class_+'">Pendiente</td>';
							}
						}
						if(i<=primer || i>ultimo){
							codigoHtml = codigoHtml + '<td align="center"></td>';
						}else{
							codigoHtml = codigoHtml + '<td align="center" style="text-align: center;"><i data-toggle="tooltip" class="far fa-arrow-alt-circle-up icon_subir_aprobador" data-id="'+aprob.id+'" data-toggle="tooltip" title="Subir" data-original-title="Subir"></i></td>';
						}
						if(i>=ultimo || i < primerFirmante){
							codigoHtml = codigoHtml + '<td></td>';
						}else{
							codigoHtml = codigoHtml + '<td><i data-toggle="tooltip" style="text-align: center;" class="far fa-arrow-alt-circle-down icon_bajar_aprobador" data-id="'+aprob.id+'" data-toggle="tooltip" title="Bajar" data-original-title="Bajar"></i></td>';
						}
						if(i>ultimo || i < primerFirmante){
							codigoHtml = codigoHtml + '<td></td></tr>';
						}else{
							codigoHtml = codigoHtml + '<td><i data-toggle="tooltip" style="text-align: center;" class="far fa-trash-alt icon_eliminar_aprobador" data-id="'+aprob.id+'" data-toggle="tooltip" title="Eliminar" data-original-title="Eliminar aprobador"></i></td></tr>';
						}
					}else{
						if(primer==1 && i==0){
							
						}else{
							primer = primer + 1;
						}
						if(i==ultimoFirmanteAsignado && i < ref.rutaAprobacion.length - 1){
							class_ = "col_select";
						}
						if(aprob.nombreTipo=="Participante"){
							codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td class="'+class_+'" style="text-align: center;">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+" / "+aprob.usuarioNombre+'</td><td class="'+class_+'">'+aprob.firmante.estadoDescripcion+"</td><td></td><td></td><td></td></tr>";
						}else{
							if(aprob.usuarioNombre.trim()==""){
								codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td class="'+class_+'" style="text-align: center;">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+" / "+aprob.dependenciaNombre+'</td><td class="'+class_+'">'+aprob.firmante.estadoDescripcion+"</td><td></td><td></td><td></td></tr>";
							}else{
								codigoHtml = codigoHtml + '<tr id="id_'+aprob.id+'"><td class="'+class_+'" style="text-align: center;">'+(i+1)+'</td><td class="'+class_+'">'+aprob.nombreTipo+" / "+aprob.dependenciaNombre+' ('+aprob.usuarioNombre+')</td><td class="'+class_+'">'+aprob.firmante.estadoDescripcion+"</td><td></td><td></td><td></td></tr>";
							}
						}
					}
				}
				$("#tblFirmantes tbody").html(codigoHtml);
				setTimeout(function(){
					ref._activarEventosTablaRutaAprobacion();
				}, 1000)
				//ref.modulo.tabs.tabRutaAprobacion.compTblFirmantes.
			}
		},
		
		_activarEventosTablaRutaAprobacion: function(){
			var ref = this;
			console.log("Activar eventos");
			$(".col_select").off('click').on('click', function(){
				var objTd = $(this);
				var objTr = objTd.closest('tr');
				if(ref.idRowSelected==0){
					ref.idRowSelected = objTr.attr('id').substring(3);
					objTr.addClass('rowSelectTablaRutaAprobacion');
				}else{
					if(ref.idRowSelected==objTr.attr('id').substring(3)){
						$("#id_" + ref.idRowSelected).removeClass('rowSelectTablaRutaAprobacion');
						ref.idRowSelected = 0;
					}else{
						$("#id_" + ref.idRowSelected).removeClass('rowSelectTablaRutaAprobacion');
						ref.idRowSelected = objTr.attr('id').substring(3);
						objTr.addClass('rowSelectTablaRutaAprobacion');
					}
				} 
			});
			$(".icon_eliminar_aprobador").off('click').on('click', function(){
				var obj = $(this);
				console.log("Eliminar");
				console.log(obj.data('id'));
				$("#idFirmanteEliminar").val(obj.data('id'));
				$("#modalEliminarRegistroRutaAprobacion").modal('show');
			});
			$(".icon_subir_aprobador").off('click').on('click', function(){
				console.log("Subir");
				var obj = $(this);
				ref._subirRegistroRutaAprobacion(obj.data('id'));
			});
			$(".icon_bajar_aprobador").off('click').on('click', function(){
				console.log("Bajar");
				var obj = $(this);
				ref._bajarRegistroRutaAprobacion(obj.data('id'));
			});
			$("#btnConfirmarEliminarRegistroRutaAprobacionSi").off('click').on('click', function(){
				var idElem = $("#idFirmanteEliminar").val();
				ref._eliminarRegistroRutaAprobacion(idElem);
				$("#btnConfirmarEliminarRegistroRutaAprobacionNo").click();
			});
			$("#btnConfirmarEliminarRegistroRutaAprobacionNo").on('click', function(){
				$("#idFirmanteEliminar").val('');
			});
		},
		
		_eliminarRegistroRutaAprobacion: function(idRegistro){
			var ref = this;
			var orden = 0;
			var i=0;
			var encontrado = false;
			while(i<ref.rutaAprobacion.length && encontrado == false){
				console.log(ref.rutaAprobacion[i].id);
				console.log(idRegistro);
				if(ref.rutaAprobacion[i].id == idRegistro){
					orden = ref.rutaAprobacion[i].orden;
					encontrado = true;
				}
				i++;
			}
			console.log("Orden eliminado:" + orden)
			var userDep = $("#rmt_nombre_originadora").val();
			for(i=0;i<ref.rutaAprobacion.length;i++){
				if(ref.rutaAprobacion[i].orden > orden){
					ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden - 1; 
				}
			}
			var result = ref.rutaAprobacion.filter(function(elem){
				   return elem.id != idRegistro; 
				});
			console.log(result);
			ref.rutaAprobacion = result;
			ref._refrescarTablaRutaAprobacion();
			ref.idRowSelected = 0;
		},
		
		_subirRegistroRutaAprobacion: function(idRegistro){
			var ref = this;
			var orden = 0;
			var i=0;
			var encontrado = false;
			while(i<ref.rutaAprobacion.length && encontrado == false){
				console.log(ref.rutaAprobacion[i].id);
				console.log(idRegistro);
				if(ref.rutaAprobacion[i].id == idRegistro){
					orden = ref.rutaAprobacion[i].orden;
					encontrado = true;
				}
				i++;
			}
			for(i=0;i<ref.rutaAprobacion.length;i++){
				if(ref.rutaAprobacion[i].orden + 1 == orden){
					ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden + 1; 
				}else{
					if(ref.rutaAprobacion[i].orden == orden){
						ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden - 1; 
					}
				}
			}
			ref.rutaAprobacion.sort(function(a,b){
				if(a.orden > b.orden){
					return 1;
				}
				if(a.orden < b.orden){
					return -1;
				}
				return 0;
			})
			ref._refrescarTablaRutaAprobacion();
		},
		
		_bajarRegistroRutaAprobacion: function(idRegistro){
			var ref = this;
			var orden = 0;
			var i=0;
			var encontrado = false;
			while(i<ref.rutaAprobacion.length && encontrado == false){
				console.log(ref.rutaAprobacion[i].id);
				console.log(idRegistro);
				if(ref.rutaAprobacion[i].id == idRegistro){
					orden = ref.rutaAprobacion[i].orden;
					encontrado = true;
				}
				i++;
			}
			for(i=0;i<ref.rutaAprobacion.length;i++){
				if(ref.rutaAprobacion[i].orden - 1 == orden){
					ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden - 1; 
				}else{
					if(ref.rutaAprobacion[i].orden == orden){
						ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden + 1; 
					}
				}
			}
			ref.rutaAprobacion.sort(function(a,b){
				if(a.orden > b.orden){
					return 1;
				}
				if(a.orden < b.orden){
					return -1;
				}
				return 0;
			})
			ref._refrescarTablaRutaAprobacion();
		},
		
		_generarTablaRutaAprobacion: function(){
			var ref = this;
			var dependenciaOrigen = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val();
			var dependenciaFin = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val();
			console.log("Dependencia originadora:" + dependenciaOrigen);
			console.log("Dependencia remitente:" + dependenciaFin);
			ref.rutaAprobacion = [];
			if(dependenciaOrigen)//TICKET 9000004269
			ref.modulo.listarDependenciasSuperiores(dependenciaOrigen, ref.indiceURLS)
				.then(function(respuesta){
					if(respuesta.estado == true){
						console.log("Respuesta:");
						console.log(respuesta);
						var encontrado = false;
						var i = 0;
						while(i<respuesta.datos.length && encontrado == false){
							var d = new Date();
							var aprob = {};
							aprob.orden = i + 1;
							aprob.id = d.getTime() + respuesta.datos[i].id;
							aprob.tipoFirmante = "1";
							aprob.nombreTipo = "Jefe Dependencia";
							aprob.codDependencia = respuesta.datos[i].id;
							aprob.dependenciaNombre = respuesta.datos[i].descripcion;
							aprob.usuario = "";
							aprob.usuarioNombre = "";
							aprob.estado = "PENDIENTE";
							ref.rutaAprobacion.splice(i+1, 0, aprob);
							ref.dependenciasAdicional.splice(0, 0, respuesta.datos[i].id);
							if(respuesta.datos[i].codigo==ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val()){
								encontrado = true;
							}
							i++;
						}
						if(ref.tablaRutaEdicion){
							ref._actualizarRutaAprobacion();
						}
						setTimeout(function(){
							ref._agregarPrimerFirmante();
							ref._refrescarTablaRutaAprobacion();
						}, 1000);
					}
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		_actualizarRutaAprobacion: function(){
			var ref = this;
			for(var i=0;i<ref.rutaAprobacion.length;i++){
				var codDep = ref.rutaAprobacion[i].codDependencia;
				try{
					ref.modulo.obtenerFirmanteRutaAprobacion(ref.indiceURLS, ref.rutaAprobacion[i].codDependencia)
						.then(function(respuesta, codDep){
							console.log("Respuesta:");
							console.log(respuesta)
							if(respuesta.datos.length > 0){
								for(var j=0;j<ref.rutaAprobacion.length;j++){
									if(respuesta.datos[0] && ref.rutaAprobacion[j].codDependencia==respuesta.datos[0].codSup && respuesta.datos[0] != null && ref.rutaAprobacion[j].tipoFirmante == 1){
										ref.rutaAprobacion[j].usuario = respuesta.datos[0].codigo;
										ref.rutaAprobacion[j].usuarioNombre = respuesta.datos[0].descripcion;
									}
								}
							}
							ref._refrescarTablaRutaAprobacion();
						}).catch(function(error){
							//ref.modSistcorr.showMessageErrorRequest(error);
						});
				}catch(error){
					console.log(error);
				}
			}
		},
		
		_agregarPrimerFirmante: function(){
			var ref = this;
			var primer = $("#corr_primerFirmante").prop('checked');
			if(primer==true){
				var username = $("#main_username").val();
				var nombreUsuario = $("#main_nombreUsuario").val();
				var encontrado = false;
				for(var i=0;i<ref.rutaAprobacion.length;i++){
					console.log(ref.rutaAprobacion[i].usuario);
					console.log(username);
					console.log(ref.rutaAprobacion[i].usuario == username);
					if(ref.rutaAprobacion[i].usuario == username){
						encontrado = true;
					}
				}
				console.log("Agregar primer firmante");
				console.log(encontrado);
				console.log(ref.rutaAprobacion);
				console.log(username);
				if(!encontrado){
					for(var i=0;i<ref.rutaAprobacion.length;i++){
						ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden + 1;
					}
					var codDependencia = $("#rmt_dependencia_originadora").val();
					var dependenciaNombre = $("#rmt_dependencia_originadora").text();
					var d = new Date();
					var aprob = {};
					aprob.orden = 1;
					aprob.id = d.getTime() + codDependencia;
					aprob.tipoFirmante = 2;
					aprob.nombreTipo = "Participante";
					aprob.codDependencia = "";
					aprob.dependenciaNombre = "";
					aprob.usuario = username;
					aprob.usuarioNombre = nombreUsuario;
					aprob.estado = "PENDIENTE";
					ref.rutaAprobacion.splice(0, 0, aprob);
				}
				ref._refrescarTablaRutaAprobacion();
			}
		},
		
		_eliminarPrimerFirmante: function(){
			var ref = this;
			var username = $("#main_username").val();
			var primerUser = "";
			if(ref.rutaAprobacion.length>0){
				primerUser = ref.rutaAprobacion[0].usuario;
			}
			var userDep = $("#rmt_nombre_originadora").val();
			var jerarquia = $("#corr_jerarquia").prop('checked');
			if(jerarquia){
				if(primerUser == username){
					var idRegistro = ref.rutaAprobacion[0].id;
					for(var i=1;i<ref.rutaAprobacion.length;i++){
						ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden - 1; 
					}
					var result = ref.rutaAprobacion.filter(function(elem){
						   return elem.id != idRegistro; 
						});
					ref.rutaAprobacion = result;
					ref._refrescarTablaRutaAprobacion();
				}
			}else{
				if(primerUser == username && username != userDep){
					var idRegistro = ref.rutaAprobacion[0].id;
					for(var i=1;i<ref.rutaAprobacion.length;i++){
						ref.rutaAprobacion[i].orden = ref.rutaAprobacion[i].orden - 1; 
					}
					var result = ref.rutaAprobacion.filter(function(elem){
						   return elem.id != idRegistro; 
						});
					ref.rutaAprobacion = result;
					ref._refrescarTablaRutaAprobacion();
				}
			}
		},
		// FIN TICKET
		
		_validarDestinatarioInterno: function(){
			var ref = this;
			var nivel2 = $("#destInterno_dependencias2").prop('checked');
			var nivel3 = $("#destInterno_dependencias3").prop('checked');
			if(ref.permiteMulitpleDestinatarios == false  && ref.destinatariosInternos.length >= 1){
				ref.modSistcorr.notificar('ERROR', 'Solo se puede agregar un destinatario', 'Warning');
				return false;
			}
			if(!ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val()){
				if(!nivel2 && !nivel3){
					ref.modSistcorr.notificar('ERROR', 'Seleccione Centro de Gestión de Correspondencia', 'Warning');
					return false;
				}
			}
			console.log("Dependencia2:" + nivel2);
			console.log("Dependencia3:" + nivel3);
			if(!nivel2 && !nivel3){
				if(!ref.modulo.tabs.tabDestinatario.internos.compDependencia.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
					return false
				}
			}else{
				console.log("Algun nivel seleccionado");
			}
			console.log("Permite multiple:" + ref.permiteMulitpleDestinatarios);
			if(ref.permiteMulitpleDestinatarios == true){
				var cgc = $("#destInterno_lugarTrabajo").val();
				var nivel = "0";
				if(nivel2){
					nivel = "3";
				}
				if(nivel3){
					nivel = "4";
				}
				if(nivel2 || nivel3){
					ref.modulo.listarDependenciasNivel(cgc, nivel, ref.indiceURLS)
					.then(function(respuesta){
						console.log("Respuesta 200");
						if(respuesta.estado == true){
							var listaDependencias = respuesta.datos;
							if(listaDependencias.length==0){
								ref.modSistcorr.procesar(false);
								ref.modSistcorr.notificar("ERROR", "El centro de gestión no cuenta con dependencias para agregar.", "Error");
								return false;
							}
							console.log("Dependencias:");
							console.log(listaDependencias);
							ref.modSistcorr.procesar(false);
						} else {
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						}
						ref.modSistcorr.procesar(false);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
				}else{
					if(ref.modulo.tabs.tabDestinatario.internos.compDependencia.val() == ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val()){
						ref.modSistcorr.notificar('ERROR', 'No puede agregar una dependencia igual a la remitente, seleccione otra', 'Warning');
						return false
					}
				}
			}else{
				if(ref.modulo.tabs.tabDestinatario.internos.compDependencia.val() == ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val()){
					ref.modSistcorr.notificar('ERROR', 'No puede agregar una dependencia igual a la remitente, seleccione otra', 'Warning');
					return false
				}
			}
			var existe = false;
			var codDependencia = ref.modulo.tabs.tabDestinatario.internos.compDependencia.val();
			for(var i in ref.destinatariosInternos){
				if(ref.destinatariosInternos[i].codDependencia ==  codDependencia){
					existe = true;
					break;
				}
			}
			if(existe == true){
				ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
				return false;
			}
			return true;
		},
		
		_agregarDestinatarioInterno: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			//if(ref._validarDestinatarioInterno == true){
			if(ref.permiteMulitpleDestinatarios == false){
				var d = new Date();
				var destinatario  = {};
				destinatario.identificador = d.getTime();
				destinatario.id = 0;
				destinatario.codLugarTrabajo = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val();
				destinatario.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(destinatario.codLugarTrabajo).text;
				destinatario.codDependencia = ref.modulo.tabs.tabDestinatario.internos.compDependencia.val();
				destinatario.dependencia = ref.listas.dependencia.buscarPorId(destinatario.codDependencia).text;
				ref.destinatariosInternos.push(destinatario);
				ref._limpiarFormularioDestinarioInterno();
				ref._actualizarListaDestinatariosInternos();
			//}
			}else{
				var cgc = $("#destInterno_lugarTrabajo").val();
				var nivel2 = $("#destInterno_dependencias2").prop('checked');
				var nivel3 = $("#destInterno_dependencias3").prop('checked');
				var nivel = "0";
				if(nivel2){
					nivel = "3";
				}
				if(nivel3){
					nivel = "4";
				}
				if(nivel2 || nivel3){
					ref.modulo.listarDependenciasNivel(cgc, nivel, ref.indiceURLS)
						.then(function(respuesta){
							if(respuesta.estado == true){
								var listaDependencias = respuesta.datos;
								var repetido = false;
								for(var i=0;i<listaDependencias.length;i++){
									var d = new Date();
									var destinatario  = {};
									destinatario.identificador = d.getTime() + "-" + listaDependencias[i].codigo;
									destinatario.id = 0;
									destinatario.codLugarTrabajo = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val();
									console.log("CodLugarTrabajo:" + destinatario.codLugarTrabajo);
									if(destinatario.codLugarTrabajo!=null && destinatario.codLugarTrabajo!="null" && destinatario.codLugarTrabajo!=""){
										destinatario.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(destinatario.codLugarTrabajo).text;
									}else{
										console.log(listaDependencias[i]);
										destinatario.codLugarTrabajo = listaDependencias[i].codigoAux;
										destinatario.lugarTrabajo = listaDependencias[i].descripcionAux;
									}
									destinatario.codDependencia = listaDependencias[i].codigo;
									destinatario.dependencia = listaDependencias[i].descripcion;
									var existe = false;
									for(var j in ref.destinatariosInternos){
										if(ref.destinatariosInternos[j].codDependencia ==  destinatario.codDependencia){
											repetido = true;
											existe = true;
											console.log("Dependencia existente:" + ref.destinatariosInternos[j].codDependencia + "||" + destinatario.dependencia + "||" + j);
										}
									}
									if(existe==false){
										ref.destinatariosInternos.push(destinatario);
									}
								}
								ref._limpiarFormularioDestinarioInterno();
								ref._actualizarListaDestinatariosInternos();
								if(repetido){
									ref.modSistcorr.notificar("OK", "Las dependencias se agregaron correctamente. Las dependencias que ya se encontraban en la lista no han sido duplicadas.", "Success");
								}
								ref.modSistcorr.procesar(false);
							} else {
								ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							}
							ref.modSistcorr.procesar(false);
						}).catch(function(error){
							ref.modSistcorr.procesar(false);
							ref.modSistcorr.showMessageErrorRequest(error);
						});
				}else{
					var d = new Date();
					var destinatario  = {};
					destinatario.identificador = d.getTime();
					destinatario.id = 0;
					destinatario.codLugarTrabajo = ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val();
					destinatario.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(destinatario.codLugarTrabajo).text;
					destinatario.codDependencia = ref.modulo.tabs.tabDestinatario.internos.compDependencia.val();
					destinatario.dependencia = ref.listas.dependencia.buscarPorId(destinatario.codDependencia).text;
					ref.destinatariosInternos.push(destinatario);
					ref._limpiarFormularioDestinarioInterno();
					ref._actualizarListaDestinatariosInternos();
				}
			}
			ref.modSistcorr.procesar(false);
		},
		
		_limpiarFormularioDestinarioInterno: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.val('');
			ref.modulo.tabs.tabDestinatario.internos.compDependencia.val('');
			
			ref.modulo.tabs.tabDestinatario.internos.compLugarTrabajo.change();
			ref.modulo.tabs.tabDestinatario.internos.compDependencia.change();
			ref.modulo.tabs.tabDestinatario.internos.compDependencia.removeAttr('disabled');
			
			ref.modulo.tabs.tabDestinatario.internos.compDependencias2.prop('checked', false);
			ref.modulo.tabs.tabDestinatario.internos.compDependencias3.prop('checked', false);
		},
		
		_validarDestinatarioExterno: function(){
			/*var ref = this;
			if(ref.permiteMulitpleDestinatarios == false  && ref.destinatariosExternos.length >= 1){
				ref.modSistcorr.notificar('ERROR', 'Solo se puede agregar un destinatario', 'Warning');
				return false;
			}
			var esNacional = ($("input:radio[name=desExterno_tipo]:checked").val() == 'true');
			if(esNacional){
				if(!ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione departamento', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compProvincia.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione provincia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDistrito.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione distrito', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDireccion.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese dirección', 'Warning');
					return false;
				}
				if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
					if(!ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val()){
						ref.modSistcorr.notificar('ERROR', 'Ingrese destinatario', 'Warning');
						return false;
					}
				}
				var existe = false;
				var codDependencia =  ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
				for(var i in ref.destinatariosExternos){
					if(ref.destinatariosExternos[i].codDependenciaNacional == codDependencia ){
						existe = true;
						break;
					}
				}
				if(existe == true){
					ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
					return false;
				}
				return true;
			} else{
				if(!ref.modulo.tabs.tabDestinatario.externos.compPais.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione país', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese nombre de la dependencia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDireccion.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese dirección', 'Warning');
					return false;
				}
				if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
					if(!ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val()){
						ref.modSistcorr.notificar('ERROR', 'Ingrese destinatario', 'Warning');
						return false;
					}
				}
				var dependencia = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
				var existe = false;
				for(var i in ref.destinatariosExternos){
					if(ref.destinatariosExternos[i].dependencia.toLowerCase() == dependencia.toLowerCase()){
						existe = true;
						break;
					}
				}
				if(existe == true){
					ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
					return false;
				}
				return true;
			}*/
			
			var ref = this;//claulate
			if(ref.permiteMulitpleDestinatarios == false  && ref.destinatariosExternos.length >= 1){
				ref.modSistcorr.notificar('ERROR', 'Solo se puede agregar un destinatario', 'Warning');
				return false;
			}
			
			var tipoDestinatario = ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.filter(":checked").val();
			var tipoEnvio = ref.modulo.tabs.tabDestinatario.externos.compTipo.filter(":checked").val() == "true";
			var isDespachoFisico = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");
			var isDespachoEmail = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");
			var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");
			
			var confFields = ref.modulo.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO || [];
			var namePropertyConfig = "";
			namePropertyConfig = "" + tipoDestinatario + ((isDespachoFisico)?("_DF"):("")) + ((isDespachoEmail)?("_CE"):("")) + ((tipoEnvio)?("_EN"):("_EI"));
			
			var arrayPropertiesDestiExterno = confFields[namePropertyConfig];
			var isValido = true, namePropertyDependencia = "", valueNamePropertyDependencia = "", existe = false;
			
			if(arrayPropertiesDestiExterno && arrayPropertiesDestiExterno.length > 0){
				if(tipoEnvio && tipoDestinatario == "EE"){
					namePropertyDependencia = "codDependenciaNacional";
					if(isIndicadorEntidadExternRucRZ){
						
						valueNamePropertyDependencia = ref.modulo.tabs.tabDestinatario.externos.compCodDependenciaNacRUCRazonSocialSunat.val();
						if(!ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.val() || !ref.modulo.tabs.tabDestinatario.externos.compCodDependenciaNacRUCRazonSocialSunat.val()){
							ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
							return false;
						}
					}else{
						valueNamePropertyDependencia = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
						if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val()){
							ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
							return false;
						}
					}
				}else if(!tipoEnvio){
					
					if(isDespachoFisico){
						if(!ref.modulo.tabs.tabDestinatario.externos.compPais.val()){
							ref.modSistcorr.notificar('ERROR', 'Seleccione pa&iacute;s', 'Warning');
							return false;
						}
					}
					
					if(tipoDestinatario == "EE"){
						namePropertyDependencia = "dependencia";
						valueNamePropertyDependencia = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
						if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val()){
							ref.modSistcorr.notificar('ERROR', 'Ingrese dependencia', 'Warning');
							return false;
						}
					}
				}
				
				for(var k in arrayPropertiesDestiExterno){
					
					if(arrayPropertiesDestiExterno[k].hasOwnProperty("isValidate") && arrayPropertiesDestiExterno[k].isValidate == 1){
						if((arrayPropertiesDestiExterno[k].typeElement == "select" || arrayPropertiesDestiExterno[k].typeElement == "text") 
								&& (!$("#" + arrayPropertiesDestiExterno[k].idField).val() || $("#" + arrayPropertiesDestiExterno[k].idField).val() == "")){
							ref.modSistcorr.notificar('ERROR', arrayPropertiesDestiExterno[k].msgValidate, 'Warning');
							isValido = false;
							break;
						}
					}
				}
				if(namePropertyDependencia != "")
				for(var i in ref.destinatariosExternos){
					if(ref.destinatariosExternos[i][namePropertyDependencia] != null && 
							ref.destinatariosExternos[i][namePropertyDependencia] != undefined &&
							ref.destinatariosExternos[i][namePropertyDependencia].toLowerCase() == valueNamePropertyDependencia.toLowerCase()){
						existe = true;
						break;
					}
				}
				if(isValido && existe){
					ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
					return false;
				}
				
				return isValido;
			}
			
			/*var esNacional = ($("input:radio[name=desExterno_tipo]:checked").val() == 'true');
			if(esNacional){
				if(!ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione departamento', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compProvincia.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione provincia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDistrito.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione distrito', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDireccion.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese dirección', 'Warning');
					return false;
				}
				if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
					if(!ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val()){
						ref.modSistcorr.notificar('ERROR', 'Ingrese destinatario', 'Warning');
						return false;
					}
				}
				var existe = false;
				var codDependencia =  ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
				for(var i in ref.destinatariosExternos){
					if(ref.destinatariosExternos[i].codDependenciaNacional == codDependencia ){
						existe = true;
						break;
					}
				}
				if(existe == true){
					ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
					return false;
				}
				return true;
			} else{
				if(!ref.modulo.tabs.tabDestinatario.externos.compPais.val()){
					ref.modSistcorr.notificar('ERROR', 'Seleccione país', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese nombre de la dependencia', 'Warning');
					return false;
				}
				if(!ref.modulo.tabs.tabDestinatario.externos.compDireccion.val()){
					ref.modSistcorr.notificar('ERROR', 'Ingrese dirección', 'Warning');
					return false;
				}
				if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
					if(!ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val()){
						ref.modSistcorr.notificar('ERROR', 'Ingrese destinatario', 'Warning');
						return false;
					}
				}
				var dependencia = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
				var existe = false;
				for(var i in ref.destinatariosExternos){
					if(ref.destinatariosExternos[i].dependencia.toLowerCase() == dependencia.toLowerCase()){
						existe = true;
						break;
					}
				}
				if(existe == true){
					ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
					return false;
				}
				return true;
			}*/
		},
		
		_limpiarFormularioDestinarioExterno: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compDireccion.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val('');
			ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val('');
			ref.modulo.tabs.tabDestinatario.externos.compDespCorreoDestino.val('');//TICKET 9000003934
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
		
		//TICKET 9000003934
		_setDefaultValueDestinarioExterno: function(){
			
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.val(['EE']);
			$('.cbxConfDespacho').prop('checked', false);
			
			ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.val("");
			$("#codEntidadExternaPorRuc").val("");
			ref.modulo.tabs.tabDestinatario.externos.compTipo.val(['true']);
			ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.prop('checked', true);
			
			var tipoEnvio = ref.modulo.tabs.tabDestinatario.externos.compTipo.filter(":checked").val() == "true";
			var tipoDestinatario = ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.filter(":checked").val();
			var isDespFisicoExterno = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");
			var isDespCorreoElectronico = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");
			var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");
			
			ref._showHideElementsDestinatarioExternoByTDTEConfDespacho(tipoDestinatario, isDespFisicoExterno, isDespCorreoElectronico, tipoEnvio, isIndicadorEntidadExternRucRZ);
		},
		
		//TICKET 9000003934
		catchPaste: function(evt, elem, callback){
			if (navigator.clipboard && navigator.clipboard.readText) {
			    // modern approach with Clipboard API
			    navigator.clipboard.readText().then(callback);
			  } else if (evt.originalEvent && evt.originalEvent.clipboardData) {
			    // OriginalEvent is a property from jQuery, normalizing the event object
			    callback(evt.originalEvent.clipboardData.getData('text'));
			  } else if (evt.clipboardData) {
			    // used in some browsers for clipboardData
			    callback(evt.clipboardData.getData('text/plain'));
			  } else if (window.clipboardData) {
			    // Older clipboardData version for Internet Explorer only
			    callback(window.clipboardData.getData('Text'));
			  } else {
			    // Last resort fallback, using a timer
			    setTimeout(function() {
			      callback(elem.value)
			    }, 100);
			  }
		},
		
		//TICKET 9000003934
		_limpiarCamposDestinarioExterno: function(){
			
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.val("");
			ref.modulo.tabs.tabDestinatario.externos.compCodDependenciaNacRUCRazonSocialSunat.val("");
			
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val("");
			ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.change();
			
			ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val("");
			ref.modulo.tabs.tabDestinatario.externos.compDepartamento.change();
			ref.modulo.tabs.tabDestinatario.externos.compProvincia.val("");
			ref.modulo.tabs.tabDestinatario.externos.compProvincia.change();
			ref.modulo.tabs.tabDestinatario.externos.compDistrito.val("");
			ref.modulo.tabs.tabDestinatario.externos.compDistrito.change();
			
			ref.modulo.tabs.tabDestinatario.externos.compPais.val("");
			ref.modulo.tabs.tabDestinatario.externos.compPais.change();
			
			ref.modulo.tabs.tabDestinatario.externos.compDireccion.val("");
			ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val("");
			ref.modulo.tabs.tabDestinatario.externos.compDespCorreoDestino.val("");
		},
		
		_agregarDestinatarioExterno: function(){
			/*var ref = this;
			ref.modSistcorr.procesar(true);
			//if(ref._validarDestinatarioExterno() == true){
				var d = new Date();
				var destinatario = {};
				destinatario.identificador = d.getTime();
				destinatario.id = 0;
				destinatario.nacional = ($("input:radio[name=desExterno_tipo]:checked").val() == 'true');
				if(destinatario.nacional == true){
					destinatario.codPais = null;
					destinatario.pais = null;
					destinatario.codDepartamento = ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val();
					destinatario.departamento = ref.listas.departamento.buscarPorId(destinatario.codDepartamento).text;
					destinatario.codProvincia = ref.modulo.tabs.tabDestinatario.externos.compProvincia.val();
					destinatario.provincia = ref.listas.provincia.buscarPorId(destinatario.codProvincia).text;
					destinatario.codDistrito = ref.modulo.tabs.tabDestinatario.externos.compDistrito.val();
					destinatario.distrito = ref.listas.distrito.buscarPorId(destinatario.codDistrito).text;
					destinatario.codDependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
					destinatario.dependenciaNacional = ref.listas.dependenciaExterna.buscarPorId(destinatario.codDependenciaNacional).text;
					destinatario.dependenciaInternacional = null;
				}else{
					destinatario.codPais = ref.modulo.tabs.tabDestinatario.externos.compPais.val();
					destinatario.pais = ref.listas.pais.buscarPorId(destinatario.codPais).text;
					destinatario.codDepartamento = null;
					destinatario.departamento = null;
					destinatario.codProvincia = null;
					destinatario.provincia = null;
					destinatario.codDistrito = null;
					destinatario.distrito = null;		
					destinatario.dependenciaInternacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
				}
				
				destinatario.direccion = ref.modulo.tabs.tabDestinatario.externos.compDireccion.val();
				destinatario.nombreDestinatario = ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val();
				
				destinatario.dependencia = destinatario.nacional == true ? destinatario.dependenciaNacional : destinatario.dependenciaInternacional;
				destinatario.lugar = destinatario.nacional == true ? (destinatario.departamento + ' - ' + destinatario.provincia + ' - ' + destinatario.distrito) : (destinatario.pais);
			
				ref.destinatariosExternos.push(destinatario);
				ref._limpiarFormularioDestinarioExterno();
				ref._actualizarListaDestinatariosExternos();
				
			//}

			ref.modSistcorr.procesar(false);*/
			
			
			
			
			var ref = this;
			ref.modSistcorr.procesar(true);
		
			var d = new Date();
			var destinatario = {};
			var timeNowIde = d.getTime();
			destinatario.identificadorAxu = timeNowIde;
			destinatario.identificador = timeNowIde;
			destinatario.id = 0;
			
			var tipoDestinatario = ref.modulo.tabs.tabDestinatario.externos.compTipoDestinatario.filter(":checked").val();
			var tipoEnvio = ref.modulo.tabs.tabDestinatario.externos.compTipo.filter(":checked").val() == "true";
			var isDespachoFisico = ref.modulo.tabs.tabDestinatario.externos.compDespFisicoExterno.is(":checked");
			var isDespachoEmail = ref.modulo.tabs.tabDestinatario.externos.compDespCorreoElectronico.is(":checked");
			var isIndicadorEntidadExternRucRZ = ref.modulo.tabs.tabDestinatario.externos.compIndicadorEntidadExternRucRZ.is(":checked");
			
			var confFields = ref.modulo.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO || [];
			var namePropertyConfig = "";
			namePropertyConfig = "" + tipoDestinatario + ((isDespachoFisico)?("_DF"):("")) + ((isDespachoEmail)?("_CE"):("")) + ((tipoEnvio)?("_EN"):("_EI"));
			
			var arrayPropertiesDestiExterno = confFields[namePropertyConfig];
			var isValido = true, namePropertyDependencia = "", valueNamePropertyDependencia = "", existe = false;
			var isSetValueDependenci = false;
			
			destinatario.dependencia = "";
			destinatario.lugar = "";
			destinatario.esEntidadExternaConRuc = null;
			destinatario.tipoDestinatario = (tipoDestinatario == "EE"?("X"):("P"));
			destinatario.esDespachoFisicoDestExterno = isDespachoFisico;
			destinatario.esCorreoElectronicoDestExterno = isDespachoEmail;
			destinatario.nacional = tipoEnvio;
			
			if(arrayPropertiesDestiExterno && arrayPropertiesDestiExterno.length > 0){
				
				if(tipoEnvio && tipoDestinatario == "EE"){
					destinatario.dependenciaInternacional = null;
					if(isIndicadorEntidadExternRucRZ){
						destinatario.codDependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compCodDependenciaNacRUCRazonSocialSunat.val();
						destinatario.dependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compRZDependenciaNacSunat.val();
					}else{
						destinatario.codDependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
						destinatario.dependenciaNacional = ref.listas.dependenciaExterna.buscarPorId(destinatario.codDependenciaNacional).text;
					}
					destinatario.dependencia = destinatario.dependenciaNacional;
					destinatario.esEntidadExternaConRuc = isIndicadorEntidadExternRucRZ;
				}else if(!tipoEnvio){
					if(isDespachoFisico){
						destinatario.codPais = ref.modulo.tabs.tabDestinatario.externos.compPais.val();
						destinatario.pais = ref.listas.pais.buscarPorId(destinatario.codPais).text;
					}
					if(tipoDestinatario == "EE"){
						destinatario.dependenciaInternacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
						destinatario.dependencia = destinatario.dependenciaInternacional;
						destinatario.codDependenciaNacional = null;
						destinatario.dependenciaNacional = null;
					} 
				}
				
				for(var k in arrayPropertiesDestiExterno){
					
					if(arrayPropertiesDestiExterno[k].hasOwnProperty("isValidate") && arrayPropertiesDestiExterno[k].isValidate == 1){
						if((arrayPropertiesDestiExterno[k].typeElement == "select" || arrayPropertiesDestiExterno[k].typeElement == "text")){
							
							destinatario[arrayPropertiesDestiExterno[k].fieldBD] = $("#" + arrayPropertiesDestiExterno[k].idField).val();
							if(arrayPropertiesDestiExterno[k].typeElement == "select" 
								&& arrayPropertiesDestiExterno[k].isGuardarValueAndText 
								&& arrayPropertiesDestiExterno[k].nameListBusByCode != "")
								destinatario[arrayPropertiesDestiExterno[k].fieldTextBD] = ref.listas[arrayPropertiesDestiExterno[k].nameListBusByCode+""].buscarPorId(destinatario[arrayPropertiesDestiExterno[k].fieldBD]).text;
						}
					}
				}
				
				if(tipoEnvio && tipoDestinatario == "PN")
					destinatario.dependenciaNacional = "Persona Natural";//TICKET 9000003934 15122020
			}
				
			/*destinatario.nacional = ($("input:radio[name=desExterno_tipo]:checked").val() == 'true');
			if(destinatario.nacional == true){
				destinatario.codPais = null;
				destinatario.pais = null;
				destinatario.codDepartamento = ref.modulo.tabs.tabDestinatario.externos.compDepartamento.val();
				destinatario.departamento = ref.listas.departamento.buscarPorId(destinatario.codDepartamento).text;
				destinatario.codProvincia = ref.modulo.tabs.tabDestinatario.externos.compProvincia.val();
				destinatario.provincia = ref.listas.provincia.buscarPorId(destinatario.codProvincia).text;
				destinatario.codDistrito = ref.modulo.tabs.tabDestinatario.externos.compDistrito.val();
				destinatario.distrito = ref.listas.distrito.buscarPorId(destinatario.codDistrito).text;
				destinatario.codDependenciaNacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaNac.val();
				destinatario.dependenciaNacional = ref.listas.dependenciaExterna.buscarPorId(destinatario.codDependenciaNacional).text;
				destinatario.dependenciaInternacional = null;
			}else{
				destinatario.codPais = ref.modulo.tabs.tabDestinatario.externos.compPais.val();
				destinatario.pais = ref.listas.pais.buscarPorId(destinatario.codPais).text;
				destinatario.codDepartamento = null;
				destinatario.departamento = null;
				destinatario.codProvincia = null;
				destinatario.provincia = null;
				destinatario.codDistrito = null;
				destinatario.distrito = null;		
				destinatario.dependenciaInternacional = ref.modulo.tabs.tabDestinatario.externos.compDependenciaInter.val();
			}
			
			destinatario.direccion = ref.modulo.tabs.tabDestinatario.externos.compDireccion.val();
			destinatario.nombreDestinatario = ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.val();
			destinatario.dependencia = destinatario.nacional == true ? destinatario.dependenciaNacional : destinatario.dependenciaInternacional;
			destinatario.lugar = destinatario.nacional == true ? (destinatario.departamento + ' - ' + destinatario.provincia + ' - ' + destinatario.distrito) : (destinatario.pais);*/
			
			if(destinatario.dependencia == "" && tipoDestinatario == "PN")
				destinatario.dependencia = destinatario.nombreDestinatario;
			
			destinatario.lugar = ((tipoEnvio && isDespachoFisico)?(destinatario.departamento + ' - ' + destinatario.provincia + ' - ' + destinatario.distrito) : ((isDespachoFisico)?(destinatario.pais):("")));
		
			ref.destinatariosExternos.push(destinatario);
			ref._limpiarFormularioDestinarioExterno();
			ref._actualizarListaDestinatariosExternos();

			ref.modSistcorr.procesar(false);
		},
		
		_validarCopia: function(){
			var ref = this;
			if(!ref.modulo.tabs.tabCopias.compLugarTrabajo.val()){
				ref.modSistcorr.notificar('ERROR', 'Seleccione Centro de Gestión de Correspondencia', 'Warning');
				return false;
			}
			if(!ref.modulo.tabs.tabCopias.compDependencia.val()){
				ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
				return false;
			}
			if(ref.modulo.tabs.tabCopias.compDependencia.val() == ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val()){
				ref.modSistcorr.notificar('ERROR', 'No puede agregar una dependencia igual a la remitente, seleccione otra', 'Warning');
				return false
			}
			var existe = false;
			var codDependencia =  ref.modulo.tabs.tabCopias.compDependencia.val();
			for(var i in ref.copias){
				if(ref.copias[i].codDependencia == codDependencia){
					existe = true;
					break;
				}
			}
			if(existe == true){
				ref.modSistcorr.notificar('ERROR', 'La dependencia fue agregada anteriormente, seleccione otra', 'Warning');
				return false;
			}
			
			return true;
		},
		
		_limpiarFormularioCopia: function(){
			var ref = this;
			ref.modulo.tabs.tabCopias.compLugarTrabajo.val('');
			ref.modulo.tabs.tabCopias.compDependencia.val('');
			
			ref.modulo.tabs.tabCopias.compLugarTrabajo.change();
			ref.modulo.tabs.tabCopias.compDependencia.change();
		},
		
		_agregarCopia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			//if(ref._validarCopia() == true){
				var d = new Date();
				var copia = {};
				copia.identificador = d.getTime();
				copia.codLugarTrabajo =  ref.modulo.tabs.tabCopias.compLugarTrabajo.val()
				copia.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(copia.codLugarTrabajo).text;
				copia.codDependencia = ref.modulo.tabs.tabCopias.compDependencia.val();
				copia.dependencia =  ref.listas.dependencia.buscarPorId(copia.codDependencia).text;
				copia.id =  0;
				ref.copias.push(copia);
				ref._limpiarFormularioCopia();
				ref._actualizarListaCopias();
			//}
			ref.modSistcorr.procesar(false);
		},
		
		_actualizarListaDestinatariosInternos: function(){
			var ref = this;
			console.log("ACTUALIZAR DESTINATARIOS INTERNOS:");
			console.log(ref.destinatariosInternos);
			ref.modulo.tabs.tabDestinatario.internos.compListaDestinatarios.empty();
			var html = ref.modulo.htmlListaDestinatariosInternos(ref.destinatariosInternos);
			console.log("HTML:");
			console.log(html);
			ref.modulo.tabs.tabDestinatario.internos.compListaDestinatarios.html(html);
			
			$("." + ref.modulo.classBtnEliminarDestinatarioInterno).click(function(event){
				var $comp = $(event.currentTarget);
				var dependencia = $comp.data("identificador");
				ref._eliminarDependencia_interno(dependencia);
			});
			
			ref.modSistcorr.eventoTooltip();
		},
		
		_actualizarListaDestinatariosExternos: function(){
			var ref = this;
			ref.modulo.tabs.tabDestinatario.externos.compListaDestinatarios.empty();
			var html = ref.modulo.htmlListaDestinatariosExternos(ref.destinatariosExternos);
			ref.modulo.tabs.tabDestinatario.externos.compListaDestinatarios.html(html);
			
			$("." + ref.modulo.classBtnEliminarDestinatarioExterno).click(function(event){
				var $comp = $(event.currentTarget);
				var dependencia = $comp.data("identificador");
				ref._eliminarDependencia_externo(dependencia);
			});
			
			ref.modSistcorr.eventoTooltip();
		},
		
		_actualizarListaCopias: function(){
			var ref = this;
			ref.modulo.tabs.tabCopias.compListaCopias.empty();
			var html = ref.modulo.htmlListaCopias(ref.copias);
			ref.modulo.tabs.tabCopias.compListaCopias.html(html);
			console.log("_actualizarListaCopias")
			$("." + ref.modulo.classBtnEliminarCopia).click(function(event){
				var $comp = $(event.currentTarget);
				var dependencia = $comp.data("identificador");
				ref._eliminarDependencia_copia(dependencia);
			});
			
			ref.modSistcorr.eventoTooltip();
		},
		
		_actualizarListaArchivosAdjuntos: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.empty();
			var html = ref.modulo.htmlListaArchivosAdjuntos(ref.archivosAdjuntos);
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.html(html);
			
			// TICKET 9000004270
			for(var ind = 0; ind < ref.archivosAdjuntos.length; ind++){
				if(ref.archivosAdjuntos[ind].tipo == "PRINCIPAL"){
					$("#corr_archivo_" + ref.archivosAdjuntos[ind].identificador).attr('checked', true);
				}else{
					$("#corr_archivo_" + ref.archivosAdjuntos[ind].identificador).removeAttr('checked');
				}
			}
			// FIN TICKET
			
			//Eventos
			$("." + ref.modulo.classBtnEliminarAdjunto).click(function(event){
				var $comp = $(event.currentTarget);
				var idArchivoAdjunto = $comp.data("identificador");
				console.log("Eliminar id:" + idArchivoAdjunto);
				ref._eliminarArchivoAdjunto(idArchivoAdjunto);
			});
			
			// TICKET 9000004270
			var _val = $("input:radio[name=corr_firmaDigital]:checked").val();
			_val = (_val == 'true');
			console.log("Valor:" + _val)
			if(!_val){
				$(".archivo-principal").attr('checked', false);
				$(".archivo-principal").attr('disabled', true);
			}
			
			$(".archivo-principal").change(function(event){
				var $comp = $(event.currentTarget);
				var name_id = $comp.attr('id');
				var id = name_id.substring(13);
				console.log();
				for(var i=0;i<ref.archivosAdjuntos.length;i++){
					if(ref.archivosAdjuntos[i].identificador == id){
						if($comp.prop('checked')){
							ref.archivosAdjuntos[i].principal = true;
							ref.archivosAdjuntos[i].tipo = 'PRINCIPAL';
						}else{
							ref.archivosAdjuntos[i].principal = false;
							ref.archivosAdjuntos[i].tipo = 'SECUNDARIO';
						}
					}
				}
			});
			// FIN TICKET
			
			ref.modSistcorr.eventoTooltip();
		},
		
		_registrarCorrespondencia: function(){
			var ref = this;
			ref.correspondenciaRespuesta = null;
			ref.modSistcorr.procesar(true);
			console.log("Iniciando validacion de registro de correspondencia");
			if(ref._validar_Formulario() == true){
				ref.correspondencia.nroEnvio = 0;
				console.log("Registrando correspondencia");
				if(ref.correspondencia.tipoEmision.id == CONSTANTES_SISTCORR.EMISION_EXTERNA)//TICKET 9000003934
					ref.correspondencia.despachoFisico = false;//TICKET 9000003934
				ref._actualizarCorreosDestinatarios();
				ref.modulo.registrarCorrespondencia(ref.correspondencia, ref.archivosAdjuntos, ref.rutaAprobacion)
					.then(function(respuesta){
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.correspondenciaRespuesta = respuesta.datos[0];
							console.log("Correspondencia Editada.");
							console.log(respuesta);
							ref.modSistcorr.setBandejaFirmaSeleccionada(respuesta.datos[0].id);
							if(ref._necesitaAsignar(ref.correspondenciaRespuesta)){
								console.log("Respuesta Ruta Aprobación:");
								console.log(ref.correspondenciaRespuesta.rutaAprobacion);
								console.log(ref.correspondenciaRespuesta.rutaAprobacion==false);
								if(ref.correspondenciaRespuesta.rutaAprobacion==false){
									setTimeout(function() {
										ref.modulo.compModalConfirmarAsignarFirma.modal('show');
									}, 200);
								}else{
									setTimeout(function() {
										ref.modulo.compModalConfirmarAsignarRutaAprobacion.modal('show');
									}, 200);
								}
								
							} else{
								if(ref.correspondenciaRespuesta.firmaDigital == true){
									if(ref.correspondenciaRespuesta.estado.bandeja == 'firmado'){
										setTimeout(function() {
											ref.modulo.abrirBandejaFirmado(ref.esEdicion);
										}, 1000);
									} else {
										setTimeout(function() {
											ref.modulo.abrirBandejaPendiente(ref.esEdicion);
										}, 1000);
									}
								} else {
									setTimeout(function() {
										ref.modulo.abrirBandejaPendiente(ref.esEdicion);
									}, 1000);
								}
								
							}
							ref.modSistcorr.procesar(false);
						} else {
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						}
						ref.modSistcorr.procesar(false);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
			} else {
				ref.modSistcorr.procesar(false);
			}
			
		},
		
		_buscarEntidadExternaNacionalSunat: function(){
			var ref = this;
			
			ref.modSistcorr.procesar(true);
			
			console.log("Iniciando busqueda entidad externa sunat");
			var tipo = ref.modulo.tabs.tabDestinatario.externos.compTipoBuscarEntidadExternaNacionalSunat.filter(":checked").val();
			var txtSearchEntidadExterna = ref.modulo.tabs.tabDestinatario.externos.compSearchModalRZRUCEntidadExterna.val();
			ref.modulo.buscarEntidadExternaNacionalSunat(txtSearchEntidadExterna, tipo)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					
					if(tipo == "RUC")
						ref.inicializarTabla(respuesta.datos[0].datosPrincipales || []);
					else
						ref.inicializarTabla(respuesta.datos[0].buscaRazonSocialReturnPrincipal.datosPrincipalesResponseSunatRZ || []);
					
					ref.modSistcorr.procesar(false);
				} else {
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				ref.modSistcorr.procesar(false);
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		_modificarCorrespondencia: function(){
			var ref = this;
			ref.correspondenciaRespuesta = null;
			ref.modSistcorr.procesar(true);
			if(ref._validar_Formulario() == true){
				if(ref.correspondencia.tipoEmision.id == CONSTANTES_SISTCORR.EMISION_EXTERNA)//TICKET 9000003934
					ref.correspondencia.despachoFisico = false;//TICKET 9000003934
				ref._actualizarCorreosDestinatarios();
				ref.modulo.modificarCorrespondencia(ref.correspondencia, ref.archivosAdjuntos, ref.rutaAprobacion)
					.then(function(respuesta){
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.correspondenciaRespuesta = respuesta.datos[0];
							if(ref._necesitaAsignar(ref.correspondenciaRespuesta)){
								console.log("Respuesta Ruta Aprobación:");
								console.log(ref.correspondenciaRespuesta.rutaAprobacion);
								console.log(ref.correspondenciaRespuesta.rutaAprobacion==false);
								if(ref.correspondenciaRespuesta.rutaAprobacion==false){
									ref.modulo.compModalConfirmarAsignarFirma.modal('show');
								}else{
									ref.modulo.compModalConfirmarAsignarRutaAprobacion.modal('show');
								}
							} else{
								if(ref.correspondenciaRespuesta.firmaDigital == true){
									if(ref.correspondenciaRespuesta.estado.bandeja == 'firmado'){
										setTimeout(function() {
											ref.modulo.abrirBandejaFirmado(ref.esEdicion);
										}, 1000);
									} else {
										setTimeout(function() {
											ref.modulo.abrirBandejaPendiente(ref.esEdicion);
										}, 1000);
									}
								} else {
									setTimeout(function() {
										ref.modulo.abrirBandejaPendiente(ref.esEdicion);
									}, 1000);
								}
								
							}
						} else {
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						}
						ref.modSistcorr.procesar(false);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
				
			} else {
				ref.modSistcorr.procesar(false);
			}
			
		},
		
		_actualizarCorreosDestinatarios: function(){
			var ref = this;
			if(ref.correspondencia.detalleExterno != undefined && ref.correspondencia.detalleExterno != null){
				for (index = 0; index < ref.correspondencia.detalleExterno.length; ++index) {
					if(ref.correspondencia.detalleExterno[index].esCorreoElectronicoDestExterno){
						if(ref.correspondencia.detalleExterno[index].id == 0){
							ref.correspondencia.detalleExterno[index].correoDestinatario = $("#correoDestinatarioEditar_"+ref.correspondencia.detalleExterno[index].identificadorAxu).val();
						}else{
							ref.correspondencia.detalleExterno[index].correoDestinatario = $("#correoDestinatarioEditar_"+ref.correspondencia.detalleExterno[index].id).val();
						}
					}
				}
			}
		},
		
		_validar_Formulario_Destinatario: function(){
			console.log("Validando Formulario Destinatario");
			var ref = this;
			var idTipoEmision = $("input:radio[name=corr_tipoEmision]:checked").val();
			//inicio ticket 9000004765
			var selected = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val();
			//inicio ticket 9000004765
			if(selected != ref.modulo.codigoDocPagar) ref.modulo.tabs.tabDestinatario.compDestDocPagar.hide();
			if(selected == ref.modulo.codigoDocPagar){
				ref.modulo.tabs.tabDestinatario.compInterno.hide();
				ref.modulo.tabs.tabDestinatario.compExterno.hide();
				ref.modulo.tabs.tabDestinatario.compInternoMultiple.hide();
				ref.modulo.tabs.tabDestinatario.compDestDocPagar.show();
				
				ref.correspondencia.detalleInterno = [];
				ref.correspondencia.detalleExterno = [];
				ref.correspondencia.detalleCorrespDestDocPagar = ref.correspondencia.detalleCorrespDestDocPagar || [];
			}else if(idTipoEmision == 1){//fin ticket 9000004765 //INTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.show();
				ref.modulo.tabs.tabDestinatario.compExterno.hide();
				ref.correspondencia.detalleInterno = ref.correspondencia.detalleInterno || [];
				ref.correspondencia.detalleExterno = [];
				ref.correspondencia.detalleCorrespDestDocPagar = [];//ticket 9000004765
			} else if(idTipoEmision == 2){ //EXTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.hide();
				ref.modulo.tabs.tabDestinatario.compExterno.show();
				ref.correspondencia.detalleExterno = ref.correspondencia.detalleExterno || [];
				ref.correspondencia.detalleInterno = [];
				ref.correspondencia.detalleCorrespDestDocPagar = [];//ticket 9000004765
			}
			console.log("Fin Validando Formulario Destinatario");
		},
		
		_validar_Formulario: function(){
			var ref = this;
			
			ref.correspondencia.codDependenciaOriginadora = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependenciaOriginadora.val();
			if(!ref.correspondencia.codDependenciaOriginadora){
				ref.modSistcorr.notificar('ERROR', 'Seleccione originadora - Datos', 'Warning');
				return false;
			}
			ref.correspondencia.dependenciaOriginadora = ref.listas.dependencia.buscarPorId(ref.correspondencia.codDependenciaOriginadora).text; 
			
			ref.correspondencia.codLugarTrabajo = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compLugarTrabajo.val();	
			if(!ref.correspondencia.codLugarTrabajo){
				ref.modSistcorr.notificar('ERROR', 'Seleccione Centro de Gestión de Correspondencia - Datos', 'Warning');
				return false;
			}
			ref.correspondencia.lugarTrabajo = ref.listas.lugarTrabajo.buscarPorId(ref.correspondencia.codLugarTrabajo).text;
			ref.correspondencia.codDependencia = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val();
			if(!ref.correspondencia.codDependencia){
				ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia - Datos', 'Warning');
				return false;
			}
			ref.correspondencia.dependencia = ref.listas.dependencia.buscarPorId(ref.correspondencia.codDependencia).text;
			ref.correspondencia.codRemitente = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.data("codusuario");
			let nomJefeDepRemitente = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val();//ticket 9000004410
			if(!ref.correspondencia.codRemitente){
				ref.modSistcorr.notificar('ERROR', 'Ingrese remitente - Datos', 'Warning');
				return false;
			}
			//inicio ticket 9000004410
			if(!nomJefeDepRemitente || nomJefeDepRemitente.trim() == "" || nomJefeDepRemitente.trim() == "SINJEFE"){
				ref.modSistcorr.notificar('ERROR', 'La dependencia remitente no tiene asignado un jefe', 'Warning');
				return false;
			}
			//fin ticket 9000004410
			ref.correspondencia.remitente = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val();	
			ref.correspondencia.codTipoCorrespondencia =  ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val();	
			ref.correspondencia.tipoCorrespondencia = $("#rmt_tipoCorrespondencia option:selected").text();
			
			if(!ref.correspondencia.tipoCorrespondencia || ref.correspondencia.tipoCorrespondencia =='' || ref.correspondencia.tipoCorrespondencia == 'Seleccione'){
				ref.modSistcorr.notificar('ERROR', 'Seleccione tipo de correspondencia', 'Warning');
				return false;
			}
			
			ref.correspondencia.tipoCorrespondenciaOtros = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.val();
			if(!ref.correspondencia.tipoCorrespondenciaOtros && ref.correspondencia.codTipoCorrespondencia == '107'){
				ref.modSistcorr.notificar('ERROR', 'Ingrese tipo de correspondencia Otros', 'Warning');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondenciaOtros.focus();
				return false;
			}
			
			ref.correspondencia.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val();	
			ref.correspondencia.fechaDocumento = ref.correspondencia.fechaDocumento;
			if(!ref.correspondencia.fechaDocumento || ref.correspondencia.fechaDocumento == ''){
				ref.modSistcorr.notificar('ERROR', 'Ingrese fecha del documento', 'Warning');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.focus();
				return false;
			}
			ref.correspondencia.asunto = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.val();
			if(!ref.correspondencia.asunto){
				ref.modSistcorr.notificar('ERROR', 'Ingrese asunto', 'Warning');
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAsunto.focus();
				return false;
			}
			ref.correspondencia.tipoEmision = {};
			ref.correspondencia.tipoEmision.id = $("input:radio[name=corr_tipoEmision]:checked").val();
			ref.correspondencia.despachoFisico = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compDespachoFisico.is(':checked');	
			ref.correspondencia.confidencial = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compConfidencial.is(':checked'); 
			ref.correspondencia.urgente = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compUrgente.is(':checked'); 
			ref.correspondencia.firmaDigital = $("input:radio[name=corr_firmaDigital]:checked").val();
			//ref.correspondencia.firmaDigital = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compFirmaDigital.is(':checked'); 
			ref.correspondencia.primerFirmante = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compPrimerFirmante.is(':checked'); 
			ref.correspondencia.observaciones = ref.modulo.tabs.tabDatos.accordion.datosCorrespondencia.compObservacion.val();
			ref.correspondencia.jerarquia = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compJerarquia.prop('checked');
			//ref.correspondencia.rutaAprobacion = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRutaAprobacion.val();
			/*if($("input[name=corr_rutaAprobacion]:checked").val()=="1"){
				ref.correspondencia.rutaAprobacion = true;
			}
			if($("input[name=corr_rutaAprobacion]:checked").val()=="0"){
				ref.correspondencia.rutaAprobacion = false;
			}*/
			if($("#corr_ruta_aprobacion_si").prop('checked') == "true" || $("#corr_ruta_aprobacion_si").prop('checked') == true){
				ref.correspondencia.rutaAprobacion = true;
			}else{
				ref.correspondencia.rutaAprobacion = false;
			}
			
			if(ref.correspondencia.rutaAprobacion == false){
				ref.rutaAprobacion = [];
			}
			
			console.log("Tipo Emision:" + ref.correspondencia.tipoEmision);
			//inicio ticket 9000004765
			if(ref.correspondencia.codTipoCorrespondencia == ref.modulo.codigoDocPagar){
				ref.modulo.tabs.tabDestinatario.compInterno.hide();
				ref.modulo.tabs.tabDestinatario.compExterno.hide();
				ref.modulo.tabs.tabDestinatario.compInternoMultiple.hide();
				ref.modulo.tabs.tabDestinatario.compDestDocPagar.show();
				ref.correspondencia.detalleInterno = [];
				ref.correspondencia.detalleExterno = [];
				ref.correspondencia.detalleCorrespDestDocPagar = ref.correspondencia.detalleCorrespDestDocPagar || [];
				
				var maxSizeSinFirmaDigitalMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size-sin-firma-digital');
				var maxSizeSinFirmaDigital = maxSizeSinFirmaDigitalMB * 1048576;
				
				//ticket 9000004904 comentado
				/*if(ref.archivosAdjuntos.length == 0 || ref.archivosAdjuntos.length > 1 || !ref.archivosAdjuntos[0].principal || (ref.archivosAdjuntos[0].tamanio * 1048576) > maxSizeSinFirmaDigital){
					ref.modSistcorr.notificar("ERROR", 'Para el tipo de correspondencia seleccionado es obligatorio adjuntar como máximo un archivo PDF que requiera firma digital con un tamaño máximo de ' + maxSizeSinFirmaDigitalMB + "MB.", "Error");
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}*/
				//inicio ticket 9000004904
				if(ref.archivosAdjuntos.length == 0){
					ref.modSistcorr.notificar("ERROR", 'Para el tipo de correspondencia seleccionado es obligatorio adjuntar como mínimo un archivo PDF que requiera firma digital con un tamaño máximo de ' + maxSizeSinFirmaDigitalMB + "MB.", "Error");
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}
				var esValidoAdjunto = true;
				var sumTamArcAdjDocPagar = 0;
				for(var ik = 0; ik < ref.archivosAdjuntos.length; ik++){
					sumTamArcAdjDocPagar = sumTamArcAdjDocPagar + (ref.archivosAdjuntos[ik].tamanio * 1048576);
					if(!ref.archivosAdjuntos[ik].principal){
						esValidoAdjunto = false;
						break;
					}
				}
				
				if(!esValidoAdjunto || sumTamArcAdjDocPagar > maxSizeSinFirmaDigital){
					ref.modSistcorr.notificar("ERROR", 'Para el tipo de correspondencia seleccionado es obligatorio que todos los archivos adjuntos sean PDF y requieran firma digital con un tamaño máximo de ' + maxSizeSinFirmaDigitalMB + "MB.", "Error");
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}
				//fin ticket 9000004904
				
			}else if(ref.correspondencia.tipoEmision.id == 1){//fin ticket 9000004765 //INTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.show();
				ref.modulo.tabs.tabDestinatario.compExterno.hide();
				ref.correspondencia.detalleInterno = ref.correspondencia.detalleInterno || [];
				ref.correspondencia.detalleExterno = [];
			} else if(ref.correspondencia.tipoEmision.id == 2){ //EXTERNO
				ref.modulo.tabs.tabDestinatario.compInterno.hide();
				ref.modulo.tabs.tabDestinatario.compExterno.show();
				ref.correspondencia.detalleExterno = ref.correspondencia.detalleExterno || [];
				ref.correspondencia.detalleInterno = [];
				// TICKET 9000004270
				var todos = true;
				var maxSizeSinFirmaDigitalMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size-sin-firma-digital');
				var maxSizeSinFirmaDigital = maxSizeSinFirmaDigitalMB * 1048576;
				for(var indiceArc = 0; indiceArc < ref.archivosAdjuntos.length; indiceArc++){
					//console.log("Nombre:" + ref.archivosAdjuntos[indiceArc].nombre + ", Tamaño: " + (ref.archivosAdjuntos[indiceArc].tamanio * 1048576) + " - " + maxSizeSinFirmaDigital);
					if(!ref.archivosAdjuntos[indiceArc].principal && (ref.archivosAdjuntos[indiceArc].tamanio * 1048576) > maxSizeSinFirmaDigital){
						ref.modSistcorr.notificar("ERROR", 'El tamaño permitido para un archivo que no requiere firma digital en una correspondencia externa es de ' + maxSizeSinFirmaDigitalMB + " MB, el archivo " + ref.archivosAdjuntos[indiceArc].nombre + " excede dicho tamaño.", "Error");
						todos = false;
					}
				}
				if(!todos){
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}
				// FIN TICKET
			}
			
			ref.correspondencia.detalleCopia = ref.correspondencia.detalleCopia || [];
			ref.correspondencia.adjuntos = ref.correspondencia.adjuntos || [];
			
			if(ref.correspondencia.tipoCorrespondencia == 'CARTA'){
				ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.parent().show();
			} else {
				ref.modulo.tabs.tabDestinatario.externos.compNombreDestinatario.parent().hide();
			}
			ref.correspondencia.detalleInterno = [];
			ref.correspondencia.detalleExterno = [];
			ref.correspondencia.detalleCopia = [];
			ref.correspondencia.adjuntos = [];
			for(var i in ref.destinatariosInternos){
				var det = ref.modSistcorr.clonarObjecto({}, ref.destinatariosInternos[i]);
				delete det.identificador;
				ref.correspondencia.detalleInterno.push(det);
			}
			for(var i in ref.destinatariosExternos){
				var det = ref.modSistcorr.clonarObjecto({}, ref.destinatariosExternos[i]);
				delete det.identificador;
				ref.correspondencia.detalleExterno.push(det);
			}
			for(var i in ref.copias){
				var det = ref.modSistcorr.clonarObjecto({}, ref.copias[i]);
				delete det.identificador;
				ref.correspondencia.detalleCopia.push(det);
			}
			//inicio ticket 9000004765
			ref.correspondencia.detalleCorrespDestDocPagar = [];
			for(var i in ref.destinatariosDocPagar){
				var det = ref.modSistcorr.clonarObjecto({}, ref.destinatariosDocPagar[i]);
				delete det.identificador;
				ref.correspondencia.detalleCorrespDestDocPagar.push(det);
			}
			//fin ticket 9000004765
			
			var validarDestinatarios = false;
			for(var i=0;i<ref.tiposCorrespondencia.length;i++){
				if(ref.tiposCorrespondencia[i]['codigo']==ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoCorrespondencia.val()){
					console.log(ref.tiposCorrespondencia[i]);
					if(ref.tiposCorrespondencia[i]['destinatario']=="1"){
						validarDestinatarios = true;
					}
					if(ref.tiposCorrespondencia[i]['destinatario']=="0"){
						$("." + ref.modulo.classBtnEliminarDestinatarioInterno).each(function(){
							var t = $(this);
							t.click();
						});
						$("." + ref.modulo.classBtnEliminarDestinatarioExterno).each(function(){
							var t = $(this);
							t.click();
						});
					}
					if(!ref.tiposCorrespondencia[i]['copia']){
						$("." + ref.modulo.classBtnEliminarCopia).each(function(){
							var t = $(this);
							t.click();
						});
					}
				}
			}
			if(validarDestinatarios){
				console.log("Validando Destinatarios:")
				var cantDest = ref.correspondencia.detalleInterno.length + ref.correspondencia.detalleExterno.length;
				if(cantDest==0){
					ref.modSistcorr.notificar('ERROR', 'Debe ingresar al menos un destinatario.', 'Warning');
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}
			}
			
			//inicio ticket 9000004765
			if(ref.correspondencia.codTipoCorrespondencia == ref.modulo.codigoDocPagar){
				var cantDest = ref.correspondencia.detalleCorrespDestDocPagar.length;
				if(cantDest==0){
					ref.modSistcorr.notificar('ERROR', 'Debe seleccionar al menos un destinatario.', 'Warning');
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
					}, 500);
					return false;
				}
			}
			//fin ticket 9000004765
			
			// TICKET 9000004044
			ref.correspondencia.esDocumentoRespuesta = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compEsDocumentoRespuesta.val()==1?true:false;
			var idAsignacion = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compIdAsignacion.val();
			if(idAsignacion != "0"){
				ref.correspondencia.idAsignacion = idAsignacion;
				ref.correspondencia.tipo = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoAccion.val();
			}
			ref.correspondencia.correlativoEntrada = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compCorrelativoEntrada.val();
			ref.correspondencia.estadoDocumentoRespuesta = ref.correspondencia.esDocumentoRespuesta==true?1:0;
			ref.correspondencia.respuesta = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compRespuesta.val();
			
			// FIN TICKET
			
			// TICKET 9000004510
			var validoArchivosLocal = true;
			for(var i=0;i<ref.archivosAdjuntos.length;i++){
				if(ref.archivosAdjuntos[i].principal == true){
					if(ref.archivosAdjuntos[i].indicadorRemoto == "S"){
						validoArchivosLocal = false;
					}
				}
			}
			if(!validoArchivosLocal){
				ref.modSistcorr.notificar('ERROR', 'No se puede continuar con el proceso, los archivos para la firma digital deben encontrarse localmente.', 'Warning');
				setTimeout(function(){
					ref.modSistcorr.procesar(false);
				}, 500);
				return false;
			}
			// FIN TICKET
			
			console.log('CORRESPONDENCIA', ref.correspondencia);
			return true;
		},
		
		_asignarFirmanteAutomatico: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.asignarFirmanteAutomatico(ref.correspondenciaRespuesta.id, ref.esEdicion)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function() {
							ref.modulo.abrirBandejaFirmado(ref.esEdicion);
						}, 3000);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						setTimeout(function() {
							ref.modulo.abrirAsignarFirma(ref.correspondenciaRespuesta.id, ref.esEdicion);
						}, 3000);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					setTimeout(function() {
						ref.modulo.abrirAsignarFirma(ref.correspondenciaRespuesta.id, ref.esEdicion);
					}, 3000);
				})
		},
		
		_asignarFirmanteRutaAprobacion: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.asignarFirmanteInicialRutaAprobacion(ref.correspondenciaRespuesta.id, ref.esEdicion)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function() {
							ref.modulo.abrirBandejaFirmado(ref.esEdicion);
						}, 3000);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
						$("#modalConfirmarInicioRutaAprobacion").modal('hide');
						setTimeout(function() {
							//ref.modulo.abrirAsignarFirma(ref.correspondenciaRespuesta.id, ref.esEdicion);
							//ref.modulo.abrirBandejaFirmado(ref.esEdicion);
						}, 3000);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					setTimeout(function() {
						//ref.modulo.abrirAsignarFirma(ref.correspondenciaRespuesta.id, ref.esEdicion);
						ref.modulo.abrirBandejaFirmado(ref.esEdicion);
					}, 3000);
				})
		},
		
		_eliminarArchivoAdjunto: function(idArchivoAdjunto){
			var ref = this;
			ref.modSistcorr.procesar(true);
			for(var i in ref.archivosAdjuntos){
				if(ref.archivosAdjuntos[i].identificador == idArchivoAdjunto){
					ref.archivosAdjuntos.splice(i, 1);
				}
			}
			setTimeout(function() {
				$('#adjunto_' + idArchivoAdjunto).remove();
				ref.modSistcorr.procesar(false);
			}, 600);
			console.log("Cerrar porocesando");
			ref.modSistcorr.procesar(false);
		},
		
		_eliminarDependencia_interno: function(idDependencia){
			var ref = this;
			ref.modSistcorr.procesar(true);
			for(var i in ref.destinatariosInternos){
				if(ref.destinatariosInternos[i].identificador == idDependencia){
					ref.destinatariosInternos.splice(i, 1);
				}
			}
			setTimeout(function(){
				$('#destinatarioInterno_' + idDependencia).remove();
				ref.modSistcorr.procesar(false);
			}, 600);
		},
		
		_eliminarDependencia_externo: function(idDependencia){
			var ref = this;
			ref.modSistcorr.procesar(true);
			for(var i in ref.destinatariosExternos){
				if(ref.destinatariosExternos[i].identificador == idDependencia){
					ref.destinatariosExternos.splice(i, 1);
				}
			}
			setTimeout(function(){
				$('#destinatarioExterno_' + idDependencia).remove();
				ref.modSistcorr.procesar(false);
			}, 600);
		},
		
		_eliminarDependencia_copia: function(idDependencia){
			var ref = this;
			ref.modSistcorr.procesar(true);
			for(var i in ref.copias){
				if(ref.copias[i].identificador == idDependencia){
					ref.copias.splice(i, 1);
				}
			}
			setTimeout(function() {
				$('#destinatarioCopia_' + idDependencia).remove();
			}, 600);
			ref.modSistcorr.procesar(false);
		},
		
		_necesitaAsignar: function(_correspondencia){
			var ref = this;
			var necesitaAsignar = false;
			if(_correspondencia){
				if(_correspondencia.rutaAprobacion==false){
					for(var i in _correspondencia.adjuntos){
						if(_correspondencia.adjuntos[i].principal == true && _correspondencia.firmaDigital == true && 
								_correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR && _correspondencia.primerFirmante == false){
							necesitaAsignar = true;
							break;
						}
					}
				}else{
					necesitaAsignar = true;
					var firmanteAsignado = false;
					var indice = 0;
					for(var j=0;j<ref.rutaAprobacion.length;j++){
						if(ref.rutaAprobacion[j].firmante){
							firmanteAsignado = true;
							indice = j;
						}
					}
					var soyPrimer = false;
					if(firmanteAsignado==false){
						necesitaAsignar = false;
						if(ref.rutaAprobacion[indice].usuario == $("#main_username").val() && (_correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || 
								_correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR || _correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR|| 
								_correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA)){
							//necesitaAsignar = false;
							soyPrimer = true;
						}
						var archivoPrincipal = false;
						for(var i in _correspondencia.adjuntos){
							if(_correspondencia.adjuntos[i].principal == true && _correspondencia.firmaDigital == true && 
									_correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR){
								archivoPrincipal = true;
								break;
							}
						}
						if(soyPrimer == false && archivoPrincipal){
							necesitaAsignar = true;
						}
					}else{
						if(ref.rutaAprobacion[indice].firmante.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA){
							necesitaAsignar = false;
						}
					}
				}
			}
			return necesitaAsignar;
		},
		
		// TICKET 9000003943
		/*_sugerirRutaAprobacion: function(){
			var ref = this;
			var codDependOrig = $("#rmt_dependencia_originadora").val();
			ref.modulo.listarDependenciasSuperiores(codDependOrig, ref.indiceURLS)
				.then(function(respuesta){
					if(respuesta.estado == true){
						console.log(respuesta);
						ref.modSistcorr.notificar('OK', 'Se acaba de actualizar el campo Jefe Dependencia Remitente a ' + ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNombre.val(), 'Success');
						//ref.correspondenciaRespuesta = respuesta.datos[0];
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},*/
		// FIN TICKET
		
		_actualizarComponentesPorTipoCorrespondencia: function(){
			var ref = this;
			if(ref.correspondenciaInterna == true && ref.correspondenciaExterna == true){
				$("input:radio[name=corr_tipoEmision]").prop( "disabled", false );
			} else if(ref.correspondenciaInterna == true && ref.correspondenciaExterna == false){
				$("input:radio[name=corr_tipoEmision][value='"+CONSTANTES_SISTCORR.EMISION_INTERNA+"']").prop('checked', true);
				$("input:radio[name=corr_tipoEmision]").prop( "disabled", true );
				$("#despachoFisico").show();
			} else if(ref.correspondenciaInterna == false && ref.correspondenciaExterna == true){
				$("input:radio[name=corr_tipoEmision][value='"+CONSTANTES_SISTCORR.EMISION_EXTERNA+"']").prop('checked', true);
				$("input:radio[name=corr_tipoEmision]").prop( "disabled", true );
				$("#despachoFisico").hide();
			} else{
				$("input:radio[name=corr_tipoEmision]").prop( "disabled", true );
			}
			
			//adicion 9-3874
			if (ref.requiereCopia) {
				 $('#btnOpenModalCopias').attr("disabled", false);
				 $('#btnOpenModalCopias').addClass("btn-petroperu-verde");
			} else {
				 $('#btnOpenModalCopias').attr("disabled", true);
				 $('#btnOpenModalCopias').removeClass("btn-petroperu-verde");
			}
			
			if (ref.requiereDestinatario == 0) {
				$('#btnOpenModalDI').removeClass('btn-petroperu-verde');
				$('#btnOpenModalDE').removeClass('btn-petroperu-verde');
				 $('#btnOpenModalDI').attr("disabled", true) ;	
				 $('#btnOpenModalDE').attr("disabled", true) ;
			} else if (ref.requiereDestinatario == 1) {
				$('#btnOpenModalDI').addClass('btn-petroperu-verde');
				$('#btnOpenModalDE').addClass('btn-petroperu-verde');
				 $('#btnOpenModalDI').attr("disabled", false) ;	
				 $('#btnOpenModalDE').attr("disabled", false) ;				
			} else {
				$('#btnOpenModalDI').addClass('btn-petroperu-verde');
				$('#btnOpenModalDE').addClass('btn-petroperu-verde');
				 $('#btnOpenModalDI').attr("disabled", false) ;	
				 $('#btnOpenModalDE').attr("disabled", false) ;
			}			
			//fin adicion 9-3874				
		},
		requiereCopia: false,  // adicion 9-3874
		requiereDestinatario: 2  // adicion 9-3874				

};

setTimeout(function(){
	CORRESPONDENCIA_EDICION_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_EDICION_VISTA.modulo = modulo_correspondencia_edicion;
	CORRESPONDENCIA_EDICION_VISTA.modulo.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_EDICION_VISTA.esEdicion = EDICION || false;
	CORRESPONDENCIA_EDICION_VISTA.listas = {};
	CORRESPONDENCIA_EDICION_VISTA.listas.lugarTrabajo = new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.dependencia =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.persona =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.dependenciaExterna =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.pais =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.departamento =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.provincia =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.listas.distrito =  new LISTA_DATA([]);
	CORRESPONDENCIA_EDICION_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	CORRESPONDENCIA_EDICION_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CORRESPONDENCIA_EDICION_VISTA.inicializar();
}, 500);
