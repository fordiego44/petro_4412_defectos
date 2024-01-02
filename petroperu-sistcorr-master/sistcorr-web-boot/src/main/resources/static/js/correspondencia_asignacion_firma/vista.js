var modulo_correspondencia_asignacion_firma = MODULO_CORRESPONDENCIA_ASIGNAR_FIRMA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_ASIGNACION_FIRMA = {
		modSistcorr: null,
		modulo: null,
		correspondenciaSeleccionada: {},
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		firmantes: [],
		sizeScreen: 500,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarComponentes();
			ref.obtenerCorrespondencia();
			ref.obtenerFirmantes();
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.modulo.tabs.tabDatos.compHeader.addClass('petro-tabs-activo');
			ref.modulo.tabs.tabDatos.compBody.show();
			ref.modulo.tabs.tabFlujo.compBody.hide();
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				if (!e.target.dataset.tab) {
			        return;
			    }
				 var $tab = $(e.currentTarget);
				 $('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
				 $tab.addClass('petro-tabs-activo');
				 if (e.target.dataset.tab == "datos") {
					ref.modulo.tabs.tabDatos.compBody.show();
					ref.modulo.tabs.tabFlujo.compBody.hide();
				 }
				 if (e.target.dataset.tab == "flujo") {
					ref.modulo.tabs.tabDatos.compBody.hide();
					ref.modulo.tabs.tabFlujo.compBody.show();
				 }
			});
			
			ref.componentes.combosS2.lugarTrabajo = ref.modulo.tabs.tabDatos.compLugarTrabajo.select2({
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
				ref.modulo.tabs.tabDatos.compDependencia.val('');
				ref.modulo.tabs.tabDatos.compDependencia.change();
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
			
			ref.componentes.combosS2.dependencia = ref.modulo.tabs.tabDatos.compDependencia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_DEPENDENCIAS_UM,
				    data: function (params) {
				        var query = {
				        		codLugar: ref.modulo.tabs.tabDatos.compLugarTrabajo.val(),
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
				ref.modulo.tabs.tabDatos.comResponsableFirma.val('');
				ref.modulo.tabs.tabDatos.comResponsableFirma.change();
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
			
			ref.componentes.combosS2.funcionario = ref.modulo.tabs.tabDatos.comResponsableFirma.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_FUNCIONARIOS,
				    data: function (params) {
				        var query = {
				        		codDep: ref.modulo.tabs.tabDatos.compDependencia.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.funcionario.agregarLista(respuesta.datos);
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
			
			ref.modulo.btnRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				// TICKET 9000003937
				var _nav = JSON.parse(sessionStorage.getItem(ref.modSistcorr.logNavegacion));
				console.log("Size:" + _nav.length);
				console.log("Destino:" + _nav[_nav.length-2]);
				var destino = _nav[_nav.length-2];
				if(destino.indexOf('emision-firma')>=0){
					sessionStorage.setItem('retroceder-asignacion', '1');
				}
				// FIN TICKET 9000003937
				ref.modSistcorr.retroceder();
			});
			
			ref.modulo.btnGuardarAsignacion.click(function(){
				if(ref._validarFirmante() == true){
					ref.modulo.validarFirmante(ref.firmante)
						.then(function(respuesta){
							if(respuesta.estado == true){
								ref.modulo.compTextoConfirmarAsignacion.html(respuesta.mensaje);
								setTimeout(function(){
									ref.modulo.compModalConfirmarAsignacion.modal('show');
								}, 200);
							} else{
								ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							}
						}).catch(function(error){
							ref.modSistcorr.showMessageErrorRequest(error);
						});
				}
			});
			
			ref.modulo.btnConfirmarAsignacionSi.click(function(){
				ref._agregarFirmante();
			});
		},
		
		obtenerCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.obtenerCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.correspondenciaSeleccionada = respuesta.datos[0];
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
		
		obtenerFirmantes: function(){
			var ref = this;
			ref.modulo.obtenerFlujoFirmantes()
				.then(function(respuesta){
					ref.firmantes = respuesta.datos;
					ref._actualizarListaFirmantes();
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		actualizarValores_Formulario: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.compLugarTrabajo.append("<option value='"+ref.correspondenciaSeleccionada.codLugarTrabajo+"' selected='selected'>"+ref.correspondenciaSeleccionada.lugarTrabajo+"</option>");
			//ref.modulo.tabs.tabDatos.compDependencia.append("<option value='"+ref.correspondenciaSeleccionada.codDependencia+"' selected='selected'>"+ref.correspondenciaSeleccionada.dependencia+"</option>");
			ref.modulo.tabs.tabDatos.compLugarTrabajo.change();
			//ref.modulo.tabs.tabDatos.compDependencia.change();
			
			ref.listas.lugarTrabajo.agregarLista([{'id': ref.correspondenciaSeleccionada.codLugarTrabajo, 'text': ref.correspondenciaSeleccionada.lugarTrabajo}]);
			//ref.listas.dependencia.agregarLista([{'id': ref.correspondenciaSeleccionada.codDependencia, 'text': ref.correspondenciaSeleccionada.dependencia}]);
		},
		
		_obtnerFirmante: function() {
			var ref = this;
			ref.modulo.tabs.tabDatos.comNombreRemitente.val('');
			ref.modulo.tabs.tabDatos.comNombreRemitente.data('codusuario', 'www');
			ref.modulo.obtenerFirmante(ref.modulo.tabs.tabDatos.compDependencia.val())
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modulo.tabs.tabDatos.comNombreRemitente.val(respuesta.datos[0].text);
						ref.modulo.tabs.tabDatos.comNombreRemitente.data('codusuario', respuesta.datos[0].id);
						ref.modulo.tabs.tabDatos.comNombreRemitente.addClass('valid');
						var id = ref.modulo.tabs.tabDatos.comNombreRemitente.attr('id');
						var label = $("label[for='" + id + "']");
						label.addClass('active');
						label.css('color', '#757575');
					}
				}).catch(function(error){
					console.log('error', error);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		_validarFirmante: function(){
			var ref = this;
			ref.firmante = {};
			ref.firmante.nroFlujo = ref.correspondenciaSeleccionada.nroFlujo;
			ref.firmante.codLugarTrabajoFirmante = ref.modulo.tabs.tabDatos.compLugarTrabajo.val();
			if(!ref.firmante.codLugarTrabajoFirmante){
				ref.modSistcorr.notificar('ERROR', 'Seleccione lugar de trabajo', 'Warning');
				return false;
			}
			ref.firmante.lugarTrabajoFirmante = ref.listas.lugarTrabajo.buscarPorId(ref.firmante.codLugarTrabajoFirmante).text;
			
			ref.firmante.codDependenciaFirmante = ref.modulo.tabs.tabDatos.compDependencia.val();
			if(!ref.firmante.codDependenciaFirmante){
				ref.modSistcorr.notificar('ERROR', 'Seleccione dependencia', 'Warning');
				return false;
			}
			ref.firmante.dependenciaFirmante = ref.listas.dependencia.buscarPorId(ref.firmante.codDependenciaFirmante).text;
			
			ref.firmante.codFirmante = ref.modulo.tabs.tabDatos.comResponsableFirma.val();
			if(!ref.firmante.codFirmante){
				ref.modSistcorr.notificar('ERROR', 'Seleccione funcionario', 'Warning');
				return false;
			}
			ref.firmante.nombreFirmante = ref.listas.funcionario.buscarPorId(ref.firmante.codFirmante).text;			
			return true;
		},
		
		_agregarFirmante: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.asignarFirmante(ref.firmante)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.firmantes.push(respuesta.datos[0]);
						ref._actualizarListaFirmantes();
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function(){
							ref.modulo.abrirBandejaFirmado();
						}, 2000);
					} else {
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalConfirmarAsignacion.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		_actualizarListaFirmantes: function(){
			var ref = this;
			ref.modulo.tabs.tabFlujo.compListaFlujo.empty();
			var html = ref.modulo.htmlFlujoFirmantes(ref.firmantes);
			ref.modulo.tabs.tabFlujo.compListaFlujo.html(html);
			ref.modSistcorr.eventoTooltip();
		},
};

setTimeout(function(){
	CORRESPONDENCIA_ASIGNACION_FIRMA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_ASIGNACION_FIRMA.modulo = modulo_correspondencia_asignacion_firma;
	CORRESPONDENCIA_ASIGNACION_FIRMA.listas = {};
	CORRESPONDENCIA_ASIGNACION_FIRMA.listas.lugarTrabajo = new LISTA_DATA([]);
	CORRESPONDENCIA_ASIGNACION_FIRMA.listas.dependencia = new LISTA_DATA([]);
	CORRESPONDENCIA_ASIGNACION_FIRMA.listas.funcionario = new LISTA_DATA([]);
	CORRESPONDENCIA_ASIGNACION_FIRMA.inicializar();
}, 500);
