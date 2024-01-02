var modulo_admin_equipos = MODULO_ADMIN_EQUIPOS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var ADMIN_EQUIPOS_VISTA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		masFiltros: false,
		dataTable: null,
		dependenciasUsuario: [],
		jefe: false,
		sizeScreen: 550,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			console.log("Iniciando eventos");
			ref.iniciarEventos();
			console.log("Inicializando componentes");
			ref.inicializarComponentes();
		},
		
		iniciarEventos: function(){
			var ref = this;
			console.log("Dentro inicio eventos");
			
			console.log("Width:" + screen.width)
			console.log("SizeScreen:" + ref.sizeScreen)
			if(screen.width < ref.sizeScreen){
				$(".mainTitle").text("Depend. - Unidades Matric.");
			}
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modSistcorr.accordionMenu.click(function(event){
				var t = $(this);
				var id = t.attr('data-id');
				console.log("Id ocultar:" + id);
				ocultarAcordeon(id);
			});
			
			ref.modulo.btnRegistrarDependencia.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_REGISTRAR_DEPENDENCIA);
			});
			
			ref.modulo.btnLimpiar.click(function(event){
				event.preventDefault();
				console.log("Limpiar");
				ref.limpiarFiltros();
				ref.inicializarTabla([]);
			});
			
			ref.modulo.btnBuscar.click(function(event){
				event.preventDefault();
				ref.buscarDependencias();
			});
			
			ref.modulo.formulario.cmbTipo.select2();
			
			ref.modulo.formulario.cmbJefe.select2();
			
			ref.modulo.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
		},
		
		inicializarComponentes: function(){
			var ref = this;
			console.log("Dentro inicio componentes");
			ref.modulo.btnBuscar.click();
		},
		
		limpiarFiltros: function(){
			var ref = this;
			ref.modulo.formulario.txtCodigo.val("");
			ref.modulo.formulario.txtNombre.val("");
			ref.modulo.formulario.cmbTipo.val("");
			ref.modulo.formulario.cmbTipo.change();
			ref.modulo.formulario.cmbJefe.val("");
			ref.modulo.formulario.cmbJefe.change();
		},
		
		buscarDependencias: function(){
			var ref = this;
			ref.filtro.codigoDependencia = ref.modulo.formulario.txtCodigo.val();
			ref.filtro.nombreDependencia = ref.modulo.formulario.txtNombre.val();
			ref.filtro.tipo = ref.modulo.formulario.cmbTipo.val();
			ref.filtro.jefe = ref.modulo.formulario.cmbJefe.val();
			console.log("Inicio buscar");
			ref.modSistcorr.procesar(true);
			ref.modulo.buscarDependencias(ref.filtro)
					.then(function(respuesta){
						console.log(respuesta)
						ref.modSistcorr.procesar(false);
						if(respuesta.estado == true){
							console.log("true");
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.inicializarTabla(respuesta.datos);
						} else {
							console.log("false");
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.inicializarTabla([]);
						}
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		inicializarTabla: function(data){
			var ref = this;
			console.log("Inicializando Tabla")
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.dataTable.show();
				ref.dataTable = ref.modulo.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'idDependenciaUnidadMatricial', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-edit icon_view_edit' data-toggle='tooltip' title='Clic para editar dependencia' data-id='" + full.idDependenciaUnidadMatricial +"' data-codigo='" + full.codigo + "' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'codigo', title: 'Código', defaultContent: ''},
			        	{data: 'nombre', title: 'Nombre', defaultContent: ''},
			        	{data: 'tipo', title: 'Tipo', defaultContent: ''},
			        	{data: 'jefe', title: 'Jefe', defaultContent: ''},
			        	{data: 'cantidadIntegrantes', title: 'Integrantes', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: '', render: function(data, type, full){
			        		if(full.estado == 'SI'){
			        			return 'Activo';
			        		}else{
			        			return 'Inactivo';
			        		}
			        	}}
			        ],
			        "columnDefs": [{
			        	"targets": [0],
			        	"width": '15px',
			        	"orderable": false
			        }],
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
				
				//inicio ticket 9000004410
				ref.dataTable.on('order.dt', function () {
					
	        		setTimeout(function() {
	        			console.log("Actualizando eventos por ordenamiento");
						ref.updateEventosTabla();
					}, 500);
				});
				//fin ticket 9000004410
				
				ref.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
	        			console.log("Actualizando eventos por pagina");
						ref.updateEventosTabla();
					}, 1000);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_edit');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.modulo.irAEditar(this.getAttribute('data-id'), this.getAttribute('data-codigo'));
				});
			}
		},
		
		// BORRAR
		
		iniciarEventos2: function(){
			var ref = this;
			
			ref.modulo.componentes.btnFiltros.click();
			
			ref.modulo.componentes.btnExportExcel.click(function(){
				/*if(ref.filtro.considerarOriginadora == false && ref.filtro.codDependenciaRemitente == null){
					ref.modSistcorr.notificar("ERROR", 'Debe de seleccionar una dependencia remitente', "Error");
					ref.inicializarTabla([]);
					return;
				}*/
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
			
			ref.modulo.componentes.btnResetear.click(function(){
				ref.resetarFiltros();
			});
			
			$("#cboxConsiderarDepenOriginadora").change(function(){
				var _val =  ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
				if(_val){
					ref.seleccionarDependenciaDefault();
					var idDep = $("#cmbDependenciaOriginadora").val();
					$("#cmbDependenciaOriginadora option[value='0']").remove();
					if(idDep=="0"){
						if(ref.dependenciasUsuario.length>0){
							var _dep = ref.dependenciasUsuario[0];
							ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);
							ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"' selected='selected'>" + _dep.text + "</option>");
							ref.modulo.componentes.cmbDependenciaOriginadora.change();
						}
					}
					if(!ref.jefe){
						ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
						ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									//console.log(datos);
									//console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
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
					}
				}else{
					if(ref.jefe){
						ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
						ref.modulo.componentes.cmbDependenciaOriginadora.val("0");
						ref.modulo.componentes.cmbDependenciaOriginadora.change();
					}else{
						console.log("NO SELECCIONADO, NO SOY JEFE")
						ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
						ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									console.log(datos);
									console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
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
						ref.modulo.componentes.cmbDependenciaOriginadora.val("0");
						ref.modulo.componentes.cmbDependenciaOriginadora.change();
						ref.modulo.componentes.cmbDependenciaOriginadora.attr('disabled', true);
					}
				}
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			setTimeout(function() {
				ref.obtenerFiltros();
				if(Object.keys(ref.filtro).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					ref.update_form_filtros();
					ref.searchCorrespondencias();
				}
			}, 500);
			
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
			//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val(null);
			//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
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
			ref.resetFiltrosSecundarios();
			ref.modulo.componentes.btnMenosFiltros.click();
			ref.actualizarValoresPorDefecto();
			
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			// TICKET 9000003974
			ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', false );
			ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
			if(ref.dependenciasUsuario.length > 0){
				var _dep = ref.dependenciasUsuario[0];
				ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);
				//ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"' selected='selected'>" + _dep.text + "</option>"); ;
				//ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"'>" + _dep.text + "</option>");
				ref.modulo.componentes.cmbDependenciaOriginadora.change();
				
				ref.seleccionarDependenciaDefault();
				
				ref.buscarCorrespondencias();
			} else {
				ref.inicializarTabla([]);
			}
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
		
		/*inicializarComponentes: function(){
			var ref = this;
			
			ref.componentes.cmbDependenciaOriginadora = ref.modulo.componentes.cmbDependenciaOriginadora.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
					data: function(params){
						var query = {
				        		q: params.term
				        };
				        return query;
					},
					processResults: function (respuesta) {
						var datos = [];
						var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
						if(!_val){
							datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
						}
						for(var arr in respuesta.datos){
							datos.push(respuesta.datos[arr]);
						}
						console.log(datos);
						console.log(respuesta.datos);
						ref.listas.dependencias.agregarLista(datos);
						return {results: datos};
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
					//url: ref.modulo.URL_LISTAR_DEPENDENCIAS_REMITENTES,
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
			 
			 if(!ref.jefe){
				 var _val =  ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
				 console.log("NO SOY JEFE");
				 console.log("VAL:" + _val);
				 if(!_val){
					 ref.modulo.componentes.cmbDependenciaOriginadora.attr('disabled', true); 
				 }else{
					 ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
					 ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									console.log(datos);
									console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
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
				 }
			 }
		},*/
		
		update_form_filtros: function(){
			var ref = this;
			
			ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', ref.filtro.considerarOriginadora );
			
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
			
			
			/*if(ref.filtro.codDestinatario){
				if(ref.filtro.tipoEmision == 0){
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val('');
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
					ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val('');
					ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val('');
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
				} else if(ref.filtro.tipoEmision == 1) { // DEstinatario Interno
					ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
				} else { // Destinatario Externo
					if(ref.filtro.destinatarioNacional == true){ //NAcional
						ref.listas.dependencias_ext.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
						ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
						ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
					} else { // Internacional
						ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
						ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
					}	
				}				
			}*/ //COMENTADO POR TICKET 9000003934
			
			
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
					//ref.listas.dependencias_ext.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
					//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
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
			
			ref.filtro.considerarOriginadora = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
			
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
						/*ref.filtro.codDestinatario = ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val();
						if(ref.filtro.codDestinatario){
							ref.filtro.nombreDestinatario = ref.listas.dependencias_ext.buscarPorId(ref.filtro.codDestinatario).text;
						}*/
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
			/*if(ref.filtro.considerarOriginadora == false && ref.filtro.codDependenciaRemitente == null){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una dependencia remitente', "Error");
				ref.inicializarTabla([]);
				return;
			}*/
			ref.modulo.consultar(ref.filtro)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.inicializarTabla(respuesta.datos || []);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.inicializarTabla([]);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.filtro.codigoDependencia = ref.modulo.formulario.txtCodigo.val();
			ref.filtro.nombreDependencia = ref.modulo.formulario.txtNombre.val();
			ref.filtro.tipo = ref.modulo.formulario.cmbTipo.val();
			ref.filtro.jefe = ref.modulo.formulario.cmbJefe.val();
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtro)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_dependencia_unidadmatricial.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_dependencia_unidadmatricial.xlsx';
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
		
		/*inicializarTabla: function(data){
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
			        	{data: 'id_correspondencia', title: 'Edi', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
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
			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
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
		},*/
};

setTimeout(function() {
	ADMIN_EQUIPOS_VISTA.modulo = modulo_admin_equipos;
	ADMIN_EQUIPOS_VISTA.modSistcorr = modulo_sistcorr;
	ADMIN_EQUIPOS_VISTA.inicializar();
}, 200);