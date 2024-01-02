var modulo_consulta_reemplazos = MODULO_CONSULTA_REEMPLAZOS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_REEMPLAZOS_VISTA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
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
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.inicializarTablaConsultaDefecto([]);
			
			ref.modulo.componentes.btnFiltros.click();
			
			ref.modulo.componentes.btnExportExcel.click(function(){
				
				ref.exportarExcel();
			});
			
			ref.modulo.componentes.btnBuscar.click(function(){
				ref.buscarReemplazos();
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
				if(Object.keys(ref.filtro).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					console.log("buscar desde session storage");
					ref.update_form_filtros();
					ref.searchReemplazos();
				}
			}, 500);
			
		},
		
		resetFiltrosSecundarios: function(){
			var ref = this;
			ref.modulo.componentes.cmbDependenciaRemitente.val(null);
			ref.modulo.componentes.cmbDependenciaRemitente.change();
			ref.modulo.componentes.cmbDependenciaDestinatario.val(null);
			ref.modulo.componentes.cmbDependenciaDestinatario.change();
			ref.modulo.componentes.txtFechaDocumentoDesde.val(null);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(null);
			
			ref.modulo.componentes.cbmTipoCorrespondencia.val(0);
			ref.modulo.componentes.cbmTipoCorrespondencia.change();
			
		},
		
		resetarFiltros: function(){
			var ref = this;
			ref.filtro = {};
			
			ref.modulo.componentes.txtCorrelativo.val('');
			ref.modulo.componentes.txtCorrelativo.change();
			ref.modulo.componentes.cbmEstado.val(0);
			ref.modulo.componentes.cbmEstado.change();
			ref.modulo.componentes.txtFechaDocumentoDesde.val('');
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val('');
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			ref.modulo.componentes.txtNroDocumento.val('');
			ref.modulo.componentes.txtNroDocumento.change();
			ref.modulo.componentes.txtFecha.val('');
			ref.modulo.componentes.txtFecha.change();
			
			ref.modulo.componentes.cmbDependenciaRemitente.val(null);
			ref.modulo.componentes.cmbDependenciaRemitente.change();
			ref.modulo.componentes.cmbDependenciaDestinatario.val(null);
			ref.modulo.componentes.cmbDependenciaDestinatario.change();
			ref.modulo.componentes.cbmTipoCorrespondencia.val('');
			ref.modulo.componentes.cbmTipoCorrespondencia.change();
			ref.modulo.componentes.txtEntidadExterna.val('');
			ref.modulo.componentes.txtEntidadExterna.change();
			ref.modulo.componentes.cmbRol.val('');
			ref.modulo.componentes.cmbRol.change();
			ref.modulo.componentes.txtAsunto.val('');
			ref.modulo.componentes.txtAsunto.change();
			
			ref.resetFiltrosSecundarios();
			ref.modulo.componentes.btnMenosFiltros.click();
			ref.actualizarValoresPorDefecto();
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			//ref.buscarReemplazos();
			//ref.inicializarTabla([]);
		},
		
		seleccionarDependenciaDefault: function(){
			var ref = this;
			ref.listas.dependencias.agregarLista([{"id" : '0000', "text": 'Todas'}]);
			ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='0000' selected='selected'>Todas</option>");
			ref.modulo.componentes.cmbDependenciaRemitente.change();
		},
		
		obtenerFiltros: function(){
			var ref = this;
			var _filtro = JSON.parse(sessionStorage.getItem("FILTRO_CONSULTA_REMPLAZO")) || {};
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
			
			ref.modulo.componentes.cmbDependenciaRemitente.select2({
				//data: ref.funcionarios
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
			
			ref.modulo.componentes.cmbDependenciaDestinatario.select2({
				//data: ref.funcionarios
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
			
			ref.componentes.txtFecha = ref.modulo.componentes.txtFecha.datepicker({
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
			
			ref.modulo.componentes.btnFechaDocumentoDesde.click(function(){
				ref.modulo.componentes.txtFechaDocumentoDesde.click();
				ref.modulo.componentes.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaDocumentoHasta.click(function(){
				ref.modulo.componentes.txtFechaDocumentoHasta.click();
				ref.modulo.componentes.txtFechaDocumentoHasta.focus();
			});	
			
			ref.modulo.componentes.btnFecha.click(function(){
				ref.modulo.componentes.txtFecha.click();
				ref.modulo.componentes.txtFecha.focus();
			});
			
		},
		
		update_form_filtros: function(){
			var ref = this;
			
			ref.modulo.componentes.txtCorrelativo.val(ref.filtro.correlativo);
			ref.modulo.componentes.txtCorrelativo.change();
			if(ref.filtro.estado){
				ref.modulo.componentes.cbmEstado.val(ref.filtro.estado);
			}
			ref.modulo.componentes.txtFechaDocumentoDesde.val(ref.filtro.fechaDesde);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(ref.filtro.fechaHasta);
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			ref.modulo.componentes.txtFecha.val(ref.filtro.fecha);
			ref.modulo.componentes.txtFecha.change();
			ref.modulo.componentes.txtNroDocumento.val(ref.filtro.nroDocumento);
			ref.modulo.componentes.txtNroDocumento.change();
			
			if(ref.filtro.codDependenciaRemitente){
				ref.modulo.componentes.cmbDependenciaRemitente.val(ref.filtro.codDependenciaRemitente);
				ref.modulo.componentes.cmbDependenciaRemitente.change();
			}
			if(ref.filtro.codDependenciaDestinatario){
				ref.modulo.componentes.cmbDependenciaDestinatario.val(ref.filtro.codDependenciaDestinatario);
				ref.modulo.componentes.cmbDependenciaDestinatario.change();
			}
			if(ref.filtro.tipoCorrespondencia){
				ref.modulo.componentes.cbmTipoCorrespondencia.val(ref.filtro.tipoCorrespondencia);
			}
			ref.modulo.componentes.txtEntidadExterna.val(ref.filtro.entidadExterna);
			ref.modulo.componentes.txtEntidadExterna.change();
			if(ref.filtro.rol){
				ref.modulo.componentes.cmbRol.val(ref.filtro.rol);
			}
			ref.modulo.componentes.txtAsunto.val(ref.filtro.asunto);
			ref.modulo.componentes.txtAsunto.change();
			
		}, 
		
		
		buscarReemplazos: function(){
			var ref = this;
			ref.filtro = {};
			
			ref.filtro.correlativo = ref.modulo.componentes.txtCorrelativo.val();
			ref.filtro.estado = ref.modulo.componentes.cbmEstado.val() == 0 ? '' : ref.modulo.componentes.cbmEstado.val();
			ref.filtro.fechaDesde = ref.modulo.componentes.txtFechaDocumentoDesde.val();
			ref.filtro.fechaHasta = ref.modulo.componentes.txtFechaDocumentoHasta.val();
			ref.filtro.nroDocumento=ref.modulo.componentes.txtNroDocumento.val();
			ref.filtro.fecha=ref.modulo.componentes.txtFecha.val();
			
			ref.filtro.masFiltros = ref.masFiltros;
			
			if(ref.filtro.masFiltros == true) {
				ref.filtro.codDependenciaDestinatario = ref.modulo.componentes.cmbDependenciaDestinatario.val();
				ref.filtro.codDependenciaRemitente = ref.modulo.componentes.cmbDependenciaRemitente.val();
				ref.filtro.tipoCorrespondencia = ref.modulo.componentes.cbmTipoCorrespondencia.val();
				ref.filtro.entidadExterna=ref.modulo.componentes.txtEntidadExterna.val();
				ref.filtro.rol=ref.modulo.componentes.cmbRol.val();
				ref.filtro.asunto = ref.modulo.componentes.txtAsunto.val();
				ref.filtro.rol=ref.modulo.componentes.cmbRol.val();
			}
			
			ref.searchReemplazos();
			sessionStorage.setItem("FILTRO_CONSULTA_REMPLAZO", JSON.stringify(ref.filtro));
		},
		
		searchReemplazos: function(){
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
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_consulta_reemplazos.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_consulta_reemplazos.xlsx';
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
			        		//location.reload();
			        	}
			        },
			        cache: true,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		sessionStorage.setItem("bandeja_reemplazos", "reemplazos");
			        		return "<a href='consulta-correspondencia/informacion/corr/" + full.radicado + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>";
			        		//return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'radicado', title: 'Correlativo', defaultContent: ''},
			        	{data: 'nroDocInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRecepcion', title: 'Fecha Recepción', defaultContent: ''},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'roles', title: 'Rol', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_CG");
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
			        		var nroPag = sessionStorage.getItem("NroPag_CG");
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

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					//ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						//ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaCorrespondenciasGeneral").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					//var api = ref.dataTableConsulta.api();
					//var order = ref.dataTableConsulta.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						//ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					//ref.eventoDocumentoPrincipal();
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
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-correspondencia/informacion/corr/" + full.correlativo + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        		//&return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'radicado', title: 'Correlativo', defaultContent: ''},
			        	{data: 'nroDocInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRecepcion', title: 'Fecha Recepción', defaultContent: ''},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'roles', title: 'Rol', defaultContent: ''}
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
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-correspondencia/informacion/corr/" + full.correlativo + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        		//return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'radicado', title: 'Correlativo', defaultContent: ''},
			        	{data: 'nroDocInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRecepcion', title: 'Fecha Recepción', defaultContent: ''},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'roles', title: 'Rol', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.modulo.irADetalle(this.dataset.id);
				});
			}
		}
};

setTimeout(function() {
	CONSULTA_REEMPLAZOS_VISTA.modulo = modulo_consulta_reemplazos;
	CONSULTA_REEMPLAZOS_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTA_REEMPLAZOS_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CONSULTA_REEMPLAZOS_VISTA.jefe = ES_JEFE;
	CONSULTA_REEMPLAZOS_VISTA.listas = {};
	CONSULTA_REEMPLAZOS_VISTA.listas.dependencias = new LISTA_DATA([]);
	CONSULTA_REEMPLAZOS_VISTA.listas.originadores = new LISTA_DATA([]);
	CONSULTA_REEMPLAZOS_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	CONSULTA_REEMPLAZOS_VISTA.inicializar();
}, 200);