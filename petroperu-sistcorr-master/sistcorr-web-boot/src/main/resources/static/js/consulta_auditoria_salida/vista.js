var modulo_consulta_correspondencia = MODULO_CORRESPONDENCIA_CONSULTA_AUDITORIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_CONSULTA_AUDITORIA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		storageVerDetalle: "VER_DETALLE",
		masFiltros: false,
		dataTable: null,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		jefe: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarComponentes();
			ref.iniciarEventos();
			//ref.itemSeleccionado();
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.inicializarTablaConsultaDefecto([]);
			
			ref.modulo.componentes.btnFiltros.click();
			
			ref.modulo.componentes.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			
			ref.modulo.componentes.btnBuscar.click(function(){
				ref.buscarCorrespondencias();
			});
			
			ref.modulo.componentes.btnMasFiltros.click(function(){
				ref.masFiltros = true;
				ref.modulo.componentes.btnMasFiltros.hide();
				ref.modulo.componentes.btnMenosFiltros.show();
				ref.modulo.componentes.filtrosSecundarios.show();
				ref.resetFiltrosSecundarios();
			});
			
			ref.modulo.componentes.btnMenosFiltros.click(function(){
				ref.masFiltros = false;
				ref.modulo.componentes.btnMenosFiltros.hide();
				ref.modulo.componentes.btnMasFiltros.show();
				ref.modulo.componentes.filtrosSecundarios.hide();
			});
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.componentes.btnResetear.click(function(){
				ref.resetarFiltros();
			});
						
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			setTimeout(function() {
				ref.obtenerFiltros();
				var isVerDetalle = sessionStorage.getItem(ref.storageVerDetalle);
				if(Object.keys(ref.filtro).length == 0 || (isVerDetalle && isVerDetalle == "N")){
					//ref.actualizarValoresPorDefecto();
					ref.modSistcorr.procesar(true);
					ref.obtenerFiltros();
					
					ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0' selected='selected'>Todas</option>");
					ref.modulo.componentes.cmbDependenciaOriginadora.change();
					ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='0000' selected='selected'>Todas</option>");
					ref.modulo.componentes.cmbDependenciaRemitente.change();
					
					ref.inicializarTabla([]);
					
					setTimeout(function() {
						ref.modSistcorr.procesar(false);
					}, 500);
				} else {
					ref.update_form_filtros();
					ref.searchCorrespondencias();
				}
				sessionStorage.setItem(ref.storageVerDetalle, "N");
			}, 500);
								
		},
		eventoDescargarDocumento: function(){
			var ref = this;
			ref.modulo.btnDescargarDocumento.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-id");
				console.log("CLICK");
				ref.obtenerCorrespondencia(correlativo);
			});
		},
		
		obtenerCorrespondencia: function(correlativo){
			var ref = this;
			ref.modulo.obtenerCorrespondencia(correlativo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.adjuntos = respuesta.datos[0].adjuntos;
						if (ref.adjuntos.length > 0){
							let cantfirma =0;
							for (var i=0; i< ref.adjuntos.length; i++) {
								
								if (ref.adjuntos[i].principal){
									   cantfirma = 1;
								}
							}
							if(cantfirma == 1){
								ref.modulo.descargarArchivoFirmaDigital(correlativo);
							}else{
								ref.modSistcorr.notificar("OK", "La correspondencia no tiene ningún archivo adjunto que requiera firma digital.", "Success");
							}
						}else{
							ref.modSistcorr.notificar("OK", "La correspondencia no tiene ningún archivo adjunto que requiera firma digital.", "Success");
						}
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			
		},
		
		resetFiltrosSecundarios: function(){
			var ref = this;
			ref.modulo.componentes.cmbNombreOriginador.val(null);
			ref.modulo.componentes.cmbNombreOriginador.change();
			ref.modulo.componentes.txtFechaDocumentoDesde.val(null);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(null);
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			
			ref.modulo.componentes.cbmTipoEmision.val(0);
			ref.modulo.componentes.cbmTipoEmision.change();
			
			ref.modulo.componentes.cbmTipoCorrespondencia.val(0);
			ref.modulo.componentes.cbmTipoCorrespondencia.change();
			ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val(null);
			ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
			ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val(null);
			ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.change();
			
			ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(null);
			ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			ref.modulo.componentes.cbmDependenciaCopia.val(null);
			ref.modulo.componentes.cbmDependenciaCopia.change();
			
			ref.modulo.componentes.cbmFlujoFirma.val(2);
			ref.modulo.componentes.cbmFlujoFirma.change();
			
			ref.modulo.componentes.cbmConfidencialidad.val(2);
			ref.modulo.componentes.cbmConfidencialidad.change();
			
			ref.modulo.componentes.cbmUrgente.val(2);
			ref.modulo.componentes.cbmUrgente.change();
			
			ref.modulo.componentes.cbmDespachoFisico.val(2);
			ref.modulo.componentes.cbmDespachoFisico.change();
			
			$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
			$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
			
		},
		
		resetarFiltros: function(){
			var ref = this;
			ref.filtro = {};
			ref.modulo.componentes.cmbDependenciaRemitente.val(null);
			ref.modulo.componentes.cmbDependenciaRemitente.change();
			ref.modulo.componentes.txtCorrelativo.val('');
			ref.modulo.componentes.txtCorrelativo.change();
			ref.modulo.componentes.txtAsunto.val('');
			ref.modulo.componentes.txtAsunto.change();
			ref.modulo.componentes.cbmEstado.val(0);
			ref.modulo.componentes.cbmEstado.change();
			ref.modulo.componentes.txtFechaModificaDesde.val('');
			ref.modulo.componentes.txtFechaModificaDesde.change();
			ref.modulo.componentes.txtFechaModificaHasta.val('');
			ref.modulo.componentes.txtFechaModificaHasta.change();
			//
			ref.resetFiltrosSecundarios();
			ref.modulo.componentes.btnMenosFiltros.click();
			ref.actualizarValoresPorDefecto();
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			//ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', false );
			ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0' selected='selected'>Todas</option>");
			ref.modulo.componentes.cmbDependenciaOriginadora.change();
			ref.seleccionarDependenciaDefault();
			ref.buscarCorrespondencias();
			
			
			/*if(ref.dependenciasUsuario.length > 0){
				var _dep = ref.dependenciasUsuario[0];
				ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);
				ref.modulo.componentes.cmbDependenciaOriginadora.change();
				
				ref.seleccionarDependenciaDefault();
				
				ref.buscarCorrespondencias();
			} else {*/
				//ref.inicializarTabla([]);
			//}
		},
		
		seleccionarDependenciaDefault: function(){
			var ref = this;
			ref.listas.dependencias.agregarLista([{"id" : '0000', "text": 'Todas'}]);
			ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='0000' selected='selected'>Todas</option>");
			ref.modulo.componentes.cmbDependenciaRemitente.change();
		},
		
		obtenerFiltros: function(){
			var ref = this;
			var _filtro = ref.modSistcorr.getFiltrosConsultaCorrespondencia();
			ref.filtro = _filtro;
			ref.masFiltros = ref.filtro.masFiltros || false;
			
			if(ref.masFiltros == true){
				ref.modulo.componentes.btnMasFiltros.click();
			} else {
				ref.modulo.componentes.btnMenosFiltros.click();
			}
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.componentes.cmbDependenciaOriginadora = ref.modulo.componentes.cmbDependenciaOriginadora.select2({
				ajax: {
					//url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS_UM,
				    url: ref.modulo.URL_LISTAR_DEPENDENCIAS_TODOS,
					
					data: function(params){
						var query = {
				        		//q: params.term
								codDependencia: ref.modulo.componentes.cmbDependenciaOriginadora.val(),
					        	q: params.term	
				        };
				        return query;
					},
					processResults: function (respuesta) {
						
						var _default = {id: '0', text: 'Todas'};
						respuesta.datos.unshift(_default);
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
						
						/*var datos = [];
						for(var arr in respuesta.datos){
							datos.push(respuesta.datos[arr]);
						}
						console.log(datos);
						console.log(respuesta.datos);
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: datos};*/
					}
				}
			}).on('select2:select', function(event){
				ref.seleccionarDependenciaDefault();
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
			
			
			ref.componentes.cmbDependenciaRemitente = ref.modulo.componentes.cmbDependenciaRemitente.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_DEPENDENCIAS_TODOS,
					data: function(params){
						var query = {
							codDependencia: ref.modulo.componentes.cmbDependenciaOriginadora.val(),
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						var _default = {id: '0000', text: 'Todas'};
						respuesta.datos.unshift(_default);
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			
			ref.componentes.txtFechaDocumentoDesde = ref.modulo.componentes.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentes.txtFechaDocumentoHasta = ref.modulo.componentes.txtFechaDocumentoHasta.datepicker({
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
			
			ref.componentes.txtFechaModificaDesde = ref.modulo.componentes.txtFechaModificaDesde.datepicker({
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
			
			ref.componentes.txtFechaModificaHasta = ref.modulo.componentes.txtFechaModificaHasta.datepicker({
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
			
			ref.componentes.cbmNombreOriginador = ref.modulo.componentes.cmbNombreOriginador.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_ORIGINADORES,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.originadores.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaDestinatariaInterno = ref.modulo.componentes.cbmDependenciaDestinatariaInterno.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaCopia = ref.modulo.componentes.cbmDependenciaCopia.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaDestinatariaExternaNacional = ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS_EXT,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias_ext.agregarLista(respuesta.datos);
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
			});
			
			ref.modulo.componentes.btnFechaDesde.click(function(){
				ref.modulo.componentes.txtFechaDocumentoDesde.click();
				ref.modulo.componentes.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaHasta.click(function(){
				ref.modulo.componentes.txtFechaDocumentoHasta.click();
				ref.modulo.componentes.txtFechaDocumentoHasta.focus();
			});
			
			//--ticket 9000004808
			ref.modulo.componentes.btnFechaModificaDesde.click(function(){
				ref.modulo.componentes.txtFechaModificaDesde.click();
				ref.modulo.componentes.txtFechaModificaDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaModificaHasta.click(function(){
				ref.modulo.componentes.txtFechaModificaHasta.click();
				ref.modulo.componentes.txtFechaModificaHasta.focus();
			});
			// Fin ticket 9000004808
			ref.modulo.componentes.cbmTipoEmision.change(function(){
				var value = ref.modulo.componentes.cbmTipoEmision.val();
				if(value == 0){ // TODOS
					$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
					$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', true);
					$("#dependenciaDestinatariaInterna").hide();
					$("#dependenciaDestinatariaExternaNacional").hide();
					$("#dependenciaDestinatariaExternaInternaNacional").hide();
				} else if(value == 1){ //Interna
					$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
					$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', true);
					$("#dependenciaDestinatariaInterna").show();
					$("#dependenciaDestinatariaExternaNacional").hide();
					$("#dependenciaDestinatariaExternaInternaNacional").hide();
				} else { //Externa
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', false);
					$("#dependenciaDestinatariaInterna").hide();
					var _valTipoDestinatario = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
					if(!_valTipoDestinatario){
						$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', true);
						$("input:radio[name=rbtnTipoDestinatario]").change();
					} else {
						$("input:radio[name=rbtnTipoDestinatario][value='"+_valTipoDestinatario+"']").prop('checked', true);
						$("input:radio[name=rbtnTipoDestinatario]").change();
					}
				}
			});
			
			 $('input:radio[name=rbtnTipoDestinatario]').change(function() {
				 var value = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
				 value = value == 'true' ? true : false;
				 var valueTipoEmision = ref.modulo.componentes.cbmTipoEmision.val();
				 if(value == true && valueTipoEmision == 2){ // EXTERNO NACIONAL
					 $("#dependenciaDestinatariaInterna").hide();
					 $("#dependenciaDestinatariaExternaNacional").show();
					 $("#dependenciaDestinatariaExternaInternaNacional").hide();
				 } else if(value == false && valueTipoEmision == 2) { // EXTERNO INTERNACIONAL
					 $("#dependenciaDestinatariaInterna").hide();
					 $("#dependenciaDestinatariaExternaNacional").hide();
					 $("#dependenciaDestinatariaExternaInternaNacional").show();
				 } else {
					 
				 }
			 });
			 
		},
		
		update_form_filtros: function(){
			var ref = this;
						
			if(ref.filtro.codDependenciaOriginadora){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDependenciaOriginadora, "text": ref.filtro.nombreDependenciaOriginadora}]);
				ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ ref.filtro.codDependenciaOriginadora +"' selected='selected'>" + ref.filtro.nombreDependenciaOriginadora + "</option>");	
				ref.modulo.componentes.cmbDependenciaOriginadora.change();	
				if(ref.filtro.considerarOriginadora){
					ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
				}
			} 
			
		
			if(ref.filtro.codDependenciaRemitente){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDependenciaRemitente, "text": ref.filtro.nombreDependenciaRemitente}]);
				ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='"+ ref.filtro.codDependenciaRemitente +"' selected='selected'>" + ref.filtro.nombreDependenciaRemitente + "</option>");
				ref.modulo.componentes.cmbDependenciaRemitente.change();
			} else {
				ref.seleccionarDependenciaDefault();
			}
			
			ref.modulo.componentes.txtCorrelativo.val(ref.filtro.correlativo);
			ref.modulo.componentes.txtCorrelativo.change();
			ref.modulo.componentes.txtAsunto.val(ref.filtro.asunto);
			ref.modulo.componentes.txtAsunto.change();
			ref.modulo.componentes.txtFechaModificaDesde.val(ref.filtro.fechaModificaDesde);
			ref.modulo.componentes.txtFechaModificaDesde.change();
			ref.modulo.componentes.txtFechaModificaHasta.val(ref.filtro.fechaModificaHasta);
			ref.modulo.componentes.txtFechaModificaHasta.change();
			// Fin
			if(ref.filtro.estado){
				ref.modulo.componentes.cbmEstado.val(ref.filtro.estado);
			}
			
			
			if(ref.filtro.codNombreOriginador){
				ref.listas.originadores.agregarLista([{"id" : ref.filtro.codNombreOriginador, "text": ref.filtro.nombreOriginador}]);
				ref.modulo.componentes.cmbNombreOriginador.append("<option value='"+ ref.filtro.codNombreOriginador +"' selected='selected'>" + ref.filtro.nombreOriginador + "</option>");
				ref.modulo.componentes.cmbNombreOriginador.change();
			}
			
			
			ref.modulo.componentes.txtFechaDocumentoDesde.val(ref.filtro.fechaDesde);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(ref.filtro.fechaHasta);
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			if(ref.filtro.tipoCorrespondencia){
				ref.modulo.componentes.cbmTipoCorrespondencia.val(ref.filtro.tipoCorrespondencia);
			}
			
			ref.modulo.componentes.cbmTipoEmision.val(ref.filtro.tipoEmision);
			ref.modulo.componentes.cbmTipoEmision.change();
			
			
			$("input[name='rbtnTipoDestinatario'][value='"+ref.filtro.destinatarioNacional+"']").prop('checked', true);
			$("input:radio[name='rbtnTipoDestinatario']").change();
			
			
			ref.modulo.componentes.cbmFlujoFirma.val(ref.filtro.firmaDigital);
			ref.modulo.componentes.cbmFlujoFirma.change();
			
			ref.modulo.componentes.cbmConfidencialidad.val(ref.filtro.confidencial);
			ref.modulo.componentes.cbmConfidencialidad.change();
			
			ref.modulo.componentes.cbmUrgente.val(ref.filtro.urgente);
			ref.modulo.componentes.cbmUrgente.change();
			
			ref.modulo.componentes.cbmDespachoFisico.val(ref.filtro.despachoFisico);
			ref.modulo.componentes.cbmDespachoFisico.change();
						
			
			if(ref.filtro.codDestinatario && ref.filtro.tipoEmision == 0){
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val('');
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
				ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val('');
				ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val('');
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			} else if(ref.filtro.codDestinatario && ref.filtro.tipoEmision == 1) { // DEstinatario Interno
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
			} else { // Destinatario Externo
				if(ref.filtro.destinatarioNacional == true){ //NAcional
					ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val(ref.filtro.nombreDestinatario);
					ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.change();
				} else { // Internacional
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
				}	
			}
			
			if(!ref.filtro.codDestinatario && ref.filtro.nombreDestinatario){
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			}
			
			if(ref.filtro.codCopia){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codCopia, "text": ref.filtro.nombreCopia}]);
				ref.modulo.componentes.cbmDependenciaCopia.append("<option value='"+ ref.filtro.codCopia +"' selected='selected'>" + ref.filtro.nombreCopia + "</option>");
				ref.modulo.componentes.cbmDependenciaCopia.change();
			}		
			
		}, 
		
		
		buscarCorrespondencias: function(){
			var ref = this;
			ref.filtro = {};
						
			ref.filtro.codDependenciaOriginadora = ref.modulo.componentes.cmbDependenciaOriginadora.val();
			if(ref.filtro.codDependenciaOriginadora!="0"){
				ref.filtro.nombreDependenciaOriginadora = ref.listas.dependencias.buscarPorId(ref.filtro.codDependenciaOriginadora).text;
			}else{
				ref.filtro.nombreDependenciaOriginadora = "Todas";
			}
			ref.filtro.codDependenciaRemitente = ref.modulo.componentes.cmbDependenciaRemitente.val();
			if(ref.filtro.codDependenciaRemitente){
				if(ref.filtro.codDependenciaRemitente == '0000'){
					ref.filtro.codDependenciaRemitente = null; 
				} else {
					ref.filtro.nombreDependenciaRemitente = ref.listas.dependencias.buscarPorId(ref.filtro.codDependenciaRemitente).text;
				}
			}
			ref.filtro.correlativo = ref.modulo.componentes.txtCorrelativo.val();
			ref.filtro.asunto = ref.modulo.componentes.txtAsunto.val();
			ref.filtro.estado = ref.modulo.componentes.cbmEstado.val() == 0 ? '' : ref.modulo.componentes.cbmEstado.val();

			ref.filtro.fechaModificaDesde = ref.modulo.componentes.txtFechaModificaDesde.val();
			ref.filtro.fechaModificaHasta = ref.modulo.componentes.txtFechaModificaHasta.val();
			//FIN
			ref.filtro.masFiltros = ref.masFiltros;
			
			if(ref.filtro.codDependenciaOriginadora=="0"){
				ref.filtro.todos = true;
			}else{
				ref.filtro.todos = false;
			}
			
			if(ref.filtro.masFiltros == true) {
				ref.filtro.codNombreOriginador = ref.modulo.componentes.cmbNombreOriginador.val();
				if(ref.filtro.codNombreOriginador){
					ref.filtro.nombreOriginador = ref.listas.originadores.buscarPorId(ref.filtro.codNombreOriginador).text;
				}
				
				ref.filtro.fechaDesde = ref.modulo.componentes.txtFechaDocumentoDesde.val();
				ref.filtro.fechaHasta = ref.modulo.componentes.txtFechaDocumentoHasta.val();
				ref.filtro.tipoCorrespondencia = ref.modulo.componentes.cbmTipoCorrespondencia.val();
				ref.filtro.tipoEmision = ref.modulo.componentes.cbmTipoEmision.val();
				if(ref.filtro.tipoEmision == 0) {
					ref.filtro.destinatarioNacional = true;
				} else if(ref.filtro.tipoEmision == 1){
					ref.filtro.destinatarioNacional = true;
					ref.filtro.codDestinatario = ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val();
					if(ref.filtro.codDestinatario){
						ref.filtro.nombreDestinatario = ref.listas.dependencias.buscarPorId(ref.filtro.codDestinatario).text;
					}
				} else {
					 var destinatarioNacional = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
					 destinatarioNacional = destinatarioNacional == 'true' ? true : false;
					ref.filtro.destinatarioNacional = destinatarioNacional;
					if(ref.filtro.destinatarioNacional == true){
						ref.filtro.codDestinatario = "";
						ref.filtro.nombreDestinatario = ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val();
					} else {
						ref.filtro.nombreDestinatario = ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val();
					}
				}
				
			
				
				
				ref.filtro.firmaDigital = ref.modulo.componentes.cbmFlujoFirma.val()
				ref.filtro.confidencial = ref.modulo.componentes.cbmConfidencialidad.val();
				ref.filtro.urgente = ref.modulo.componentes.cbmUrgente.val();
				ref.filtro.despachoFisico = ref.modulo.componentes.cbmDespachoFisico.val();
				
				ref.filtro.codCopia = ref.modulo.componentes.cbmDependenciaCopia.val();
				if(ref.filtro.codCopia){
					ref.filtro.nombreCopia = ref.listas.dependencias.buscarPorId(ref.filtro.codCopia).text;
				}
			}
			
			ref.searchCorrespondencias();
			
			ref.modSistcorr.setFiltrosConsultaCorrespondencia(ref.filtro);
		},
		
		searchCorrespondencias: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);

			ref.inicializarTablaConsulta();

		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtro)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_correspondencia_auditoria.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_correspondencia_auditoria.xlsx';
	                    document.body.appendChild(a);
	                    a.click();
	                    document.body.removeChild(a);
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		inicializarTablaConsulta: function(){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentes.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.modulo.componentes.dataTableConsulta.show();
				console.log("FILTROS BUSQUEDA:");
				console.log(ref.filtro);
				ref.dataTableConsulta = ref.modulo.componentes.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_CONSULTA_PAGINADO,
			        	"type": "GET",
			        	"data": ref.filtro,
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
			        			var dtFilter = {
			        				"draw": Number(response.datos[0].draw),
			        				"recordsFiltered": Number(response.datos[0].recordsFiltered),
			        				"recordsTotal": Number(response.datos[0].recordsTotal),
			        				"data": response.datos[0].listOfDataObjects || []
			        			}
			        		}else{
			        			var dtFilter = {
			        					"draw": 0,
				        				"recordsFiltered": 0,
				        				"recordsTotal": 0,
				        				"data": []
			        			};
			        		}
			        		return JSON.stringify( dtFilter );
			        	},
			        	'error': function(result){
			        		console.log("Error Consulta Correspondencia");
			        		console.log(result);
			        		ref.modSistcorr.procesar(false);
			        	}
			        },
			        cache: true,
			        "columns": [
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
			        ],
			        "order": [ 2, 'desc' ],
			        "columnDefs": [ 
			        	   {className : 'dtr-control td-ajuste',  targets: [0]},
				           {orderable : false,  targets: [0, 1]}
				         ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_CAS");
			        		console.log("order:" + order);
			        		if(order!=null && order != "undefined"){
				        		var critOrder = order.split(",");
				        		console.log("ORDER SESSION:" + parseInt(critOrder[0]) + ",'" + critOrder[1] + "'");
				        		var colOrd = critOrder[1];
				        		ref.dataTableConsulta.order([parseInt(critOrder[0]), colOrd]).draw();
			        		}
			        	}, 1000);
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        		var nroPag = sessionStorage.getItem("NroPag_CAS");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPag");
			        		console.log("OrigPag:" + origPag);
			        		if(origPag == "verDetalleBS"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagJG");
			        		}
			        	}, 2000);
			        },

			        "createdRow":function(row,data,index){
			        	var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaAuditoriaGeneralSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					}
				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CAS", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaCorrespondenciasGeneralAuditoria").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CAS", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				}, 1500);
			}
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
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
			        ],
			        "order": [ 2, 'desc' ],
			        "columnDefs": [ 
				           {className : 'dtr-control td-ajuste',  targets: [0]},
				           {orderable : false,  targets: [0, 1]}
				          ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },
			        "createdRow":function(row,data,index){
			        	var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaAuditoriaGeneralSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					}
				});
				
				ref.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				});
				
				ref.modulo.componentes.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				}, 1500);
			}
		},
		
		inicializarTablaConsultaDefecto: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentes.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsultaDefecto(data);
			} else {
				ref.modulo.componentes.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.componentes.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
			        ],
			        "order": [ 2, 'desc' ],
			        "columnDefs": [ 
			        	  {className : 'dtr-control td-ajuste',  targets: [0]},
				          {orderable : false,  targets: [0, 1]}
				        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },
			    	"createdRow":function(row,data,index){
			    		var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaAuditoriaGeneralSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					},
	

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					sessionStorage.setItem(ref.storageVerDetalle, "S");
					sessionStorage.setItem("_itemSelec_ConsultaAuditoriaGeneralSalida", this.dataset.id);
					ref.modulo.irADetalle(this.dataset.id);
				});
			}
		},
				
};

setTimeout(function() {
	CORRESPONDENCIA_CONSULTA_AUDITORIA.modulo = modulo_consulta_correspondencia;
	CORRESPONDENCIA_CONSULTA_AUDITORIA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_CONSULTA_AUDITORIA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CORRESPONDENCIA_CONSULTA_AUDITORIA.jefe = ES_JEFE;
	CORRESPONDENCIA_CONSULTA_AUDITORIA.listas = {};
	CORRESPONDENCIA_CONSULTA_AUDITORIA.listas.dependencias = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_AUDITORIA.listas.originadores = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_AUDITORIA.listas.dependencias_ext = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_AUDITORIA.inicializar();
}, 200);