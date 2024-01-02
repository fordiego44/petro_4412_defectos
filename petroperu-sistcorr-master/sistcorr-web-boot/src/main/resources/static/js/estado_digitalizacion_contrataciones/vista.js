/*9000004412*/
var modulo_estado_digitalizacion_contrataciones = MODULO_ESTADO_DIGITALIZACION_CONTRATACIONES.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var ESTADO_DIGITALIZACION_CONTRATACIONES = {
		modSistcorr: null,
		modulo: null, 
		filtrosBusqueda: null,//4412 defecto 23
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		estadoDigitalizacion:{},
		procesando: false, 
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;			
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.inicializarTablaConsultaDefecto([]);	
			setTimeout(function(){
				ref.modSistcorr.procesar(false);
			}, 2000)
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.inicializarTablaConsultaDefecto([]);
			
			ref.modulo.componentesEstadoDigitalizacion.txtEstado.select2(); //4412 defecto 23
			
			ref.componentes.txtFechaDocumentoDesde = ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentes.txtFechaDocumentoHasta = ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.datepicker({
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
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaDocumentoDesde.click(function(){
				ref.modulo.componentes.txtFechaDocumentoDesde.click();
				ref.modulo.componentes.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaDocumentoHasta.click(function(){
				ref.modulo.componentes.txtFechaDocumentoHasta.click();
				ref.modulo.componentes.txtFechaDocumentoHasta.focus();
			});	
			
			ref.modulo.btnBuscar.click(function(event){
				console.log('Buscar Estado Digitalizacion');
				ref.buscarEstadoDigitilizacion();
			});
			//4412 defecto 23
			ref.modulo.btnExportarExcel.click(function(){
				var parametros = {    
						"nroProceso": ref.modulo.componentesEstadoDigitalizacion.txtProceso.val(), 
						"fechaDesde": ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.val(), 
						"fechaHasta": ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.val(), 
						"ruc": ref.modulo.componentesEstadoDigitalizacion.txtRUC.val(), 
						"estado": ref.modulo.componentesEstadoDigitalizacion.txtEstado.val()
					};
				ref.filtrosBusqueda = parametros;
				
				ref.exportarExcel();
			});
			
			ref.modulo.btnLimpiar.click(function(){
				ref.modulo.componentesEstadoDigitalizacion.txtProceso.val(""); 
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.val("");
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.val("");
				
				ref.modulo.componentesEstadoDigitalizacion.txtRUC.val(""); 
				ref.modulo.componentesEstadoDigitalizacion.txtEstado.val("SI");
				ref.modulo.componentesEstadoDigitalizacion.txtEstado.select2(); 
			});
			
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			//4412 defecto 23 FIN
			
			setTimeout(function() {		
				ref.obtenerFiltros();
				/*var isVerDetalle = sessionStorage.getItem(ref.storageVerDetalle);
				if(Object.keys(ref.filtroED).length == 0 || (isVerDetalle && isVerDetalle == "N")){
					ref.obtenerfiltroEDs();
					ref.inicializarTabla([]);
				} else {
					console.log("actualizar y buscar correspondencias")
					ref.update_form_filtroEDs();
					ref.searchCorrespondencias();
				}
				sessionStorage.setItem(ref.storageVerDetalle, "N");*/
			}, 1500);
			
		},	
		
		obtenerFiltros: function() {
		    var ref = this;
		    var _filtro = ref.modSistcorr.getfiltrosConsultaEstDigContrataciones();
		    ref.estadoDigitalizacion = _filtro;
		  },
		
		actualizarListaDependencias: function(){
				var ref =  this;
				ref.modSistcorr.procesar(true);
				ref.moduloED.listarDependencias()
				.then(function(respuesta){
					if(respuesta.estado == true){																	
						ref.listas.dependencias.datos = [];
						ref.listas.dependencias.agregarLista(respuesta.datos);
						ref.inicializarcomponentesED();
						ref.modSistcorr.procesar(false);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		inicializarcomponentesED: function(){
			var ref = this;		
			//debugger;
			ref.componentesED.cmbDependenciaRemitenteED = ref.modulo.componentesEstadoDigitalizacion.cmbDependenciaRemitenteED.select2({
				data: ref.listas.dependencias.datos
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
			
			ref.componentesED.txtFechaDocumentoDesde = ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentesED.txtFechaDocumentoHasta = ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.datepicker({
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
			
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaDesde.click(function(){
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.click();
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaHasta.click(function(){
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.click();
				ref.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.focus();
			});	
			
			ref.componentesED.txtFechaModificaDesde = ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaDesde.datepicker({
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
			
			ref.componentesED.txtFechaModificaHasta = ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaHasta.datepicker({
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
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaModificaDesde.click(function(){
				ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaDesde.click();
				ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaDesde.focus();
			});
			
			ref.modulo.componentesEstadoDigitalizacion.btnFechaModificaHasta.click(function(){
				ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaHasta.click();
				ref.modulo.componentesEstadoDigitalizacion.txtFechaModificaHasta.focus();
			}); 
		},
		
		buscarEstadoDigitilizacion: function(){ 
			console.log('buscarEstadoDigitilizacion');
			var ref=this;
			ref.estadoDigitalizacion= {};		
			ref.estadoDigitalizacion.nroProceso=this.modulo.componentesEstadoDigitalizacion.txtProceso.val();
			ref.estadoDigitalizacion.fechaDesde=this.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoDesde.val();
			ref.estadoDigitalizacion.fechaHasta=this.modulo.componentesEstadoDigitalizacion.txtFechaDocumentoHasta.val();
			ref.estadoDigitalizacion.ruc=this.modulo.componentesEstadoDigitalizacion.txtRUC.val();
			ref.estadoDigitalizacion.estado=this.modulo.componentesEstadoDigitalizacion.txtEstado.val();
			ref.searchEstadoDigitilizacion();
			
			ref.modSistcorr.setfiltrosConsultaEstDigContrataciones(ref.estadoDigitalizacion);
			
		},	
		
		searchEstadoDigitilizacion: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.inicializarTablaConsulta();
			ref.modSistcorr.procesar(false);
		},
		//4412 defecto 23
		exportarExcel: function(){
			//debugger;4412 defecto 23
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_correspondencia.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_correspondencia.xlsx';
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
		//4412 defecto 23 fin
		inicializarTablaConsultaDefecto: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsultaDefecto(data);
			} else {
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	/*{data: 'id', title: 'Elim.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarFuncionario' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id +"' data-registro='" + full.registro +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id', title: 'Mod.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-edit icon_editar btnModificarFuncionario'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id +"' style='cursor:pointer'></i>"
			        	}},*/
			        	{data: 'nroProceso', title: 'Nro. Proceso', defaultContent: ''},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'ruc', title: 'RUC', defaultContent: ''},
			        	{data: 'fechaDesde', title: 'Fecha', defaultContent: ''},
			        	{data: 'digitalizado', title: 'Digitalizado', defaultContent: ''}
			        ],
			        "order": [[ 2, 'desc' ]],
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
			    		/*var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaEventoDocumentoSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}*/
					},

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, dataTable, row, showHide, update ) {
					/*ref.updateEventosTabla();
					ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();*/
				});
				
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc();
	        		setTimeout(function() {
						/*ref.updateEventosTabla();
						ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();*/
					}, 1500);
				});
				
				setTimeout(function() {
					/*ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
					ref.updateEventosTabla();*/
				}, 1500);
			}
		},
		  
		  inicializarTabla: function(data){
				var ref = this;
				if(ref.dataTable){
					ref.dataTable.destroy();
					ref.modulo.componentesEstadoDigitalizacion.dataTable.empty();
					ref.dataTable = null;
					ref.inicializarTabla(data);
				} else {
					ref.modulo.componentesEstadoDigitalizacion.dataTable.show();
					ref.dataTable = ref.modulo.componentesEstadoDigitalizacion.dataTable.DataTable({
						"dom": DatatableAttachments.domsimple,
						"language": DatatableAttachments.language,
				        "responsive": true,
				        "pageLength": 10,
				        "data": data,
				        "columns": [
				        	/*{data: 'id', title: 'Elim.', defaultContent: '', render: function(data, type, full){
				        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarFuncionario' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id +"' data-registro='" + full.registro +"' style='cursor:pointer'></i>"
				        	}},
				        	{data: 'id', title: 'Mod.', defaultContent: '', render: function(data, type, full){
				        		return "<i class='far fas fa-edit icon_editar btnModificarFuncionario'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id +"' style='cursor:pointer'></i>"
				        	}},*/
				        	{data: 'nroProceso', title: 'Nro. Proceso', defaultContent: ''},
				        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
				        	{data: 'ruc', title: 'RUC', defaultContent: ''},
				        	{data: 'fechaDesde', title: 'Fecha', defaultContent: ''},
				        	{data: 'digitalizado', title: 'Digitalizado', defaultContent: ''}
				        ],
				        "order": [[ 1, 'desc' ]],
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
				    		/*var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaEventoDocumentoSalida");
							if(data.id_correspondencia == corresSeleccionado){
								$('td', row).css({
									'background-color':'#f2f0d7'
								});
							}*/
						},

					});
					
					ref.dataTable.on('responsive-display', function ( e, dataTable, row, showHide, update ) {
						/*ref.updateEventosTabla();
						ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();*/
					});
					
					ref.modulo.componentesEstadoDigitalizacion.dataTable.on( 'page.dt', function () {
						/*ref.dataTable.responsive.rebuild();
		        		ref.dataTable.responsive.recalc();
		        		setTimeout(function() {
							ref.updateEventosTabla();
							ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
							ref.eventoDescargarDocumento();
						}, 1500);*/
					});
					
					setTimeout(function() {
						/*ref.updateEventosTabla();
						ref.moduloED.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();*/
					}, 1500);
				}
			},
			
			
		inicializarTablaConsulta: function(){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_CONSULTA_EST_DIG_CONTRATACIONES,
			        	"type": "GET",
			        	"data": ref.estadoDigitalizacion,
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
			        			datosConsulta = response.datos[0].listOfDataObjects;
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
			        	/*{data: 'id', title: 'Elim.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarFuncionario' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id +"' data-registro='" + full.registro +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id', title: 'Mod.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-edit icon_editar btnModificarFuncionario'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id +"' style='cursor:pointer'></i>"
			        	}},*/
			        	{data: 'nroProceso', title: 'Nro. Proceso', defaultContent: ''},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'ruc', title: 'RUC', defaultContent: ''},
			        	{data: 'fechaDesde', title: 'Fecha', defaultContent: ''},
			        	{data: 'digitalizado', title: 'Digitalizado', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_JG");
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
			        		var nroPag = sessionStorage.getItem("NroPag_JG");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPag");
			        		console.log("OrigPag:" + origPag);
			        		/*if(origPag == "verDetalleBS"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagJG");
			        		}*/
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();*/
				});
				
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_JG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();*/
					}, 1500);
				});
				
				ref.modulo.componentesEstadoDigitalizacion.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaEstadoDigitalizacion").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_JG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();*/
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();*/
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			/*var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					sessionStorage.setItem(ref.storageVerDetalle, "S");//TICKET 9000004710
					sessionStorage.setItem("_itemSelec_ConsultaEventoDocumentoSalida", this.dataset.id);//TICKET 9000004808
					ref.moduloED.irADetalle(this.dataset.id);
				});
			}*/
		},
		eventoDescargarDocumento: function(){
			var ref = this;
			ref.moduloED.btnDescargarDocumento.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-id");
				console.log("CLICK");
				ref.obtenerCorrespondencia(correlativo);
			});
		},
		
		obtenerCorrespondencia: function(correlativo){
			var ref = this;
			ref.moduloED.obtenerCorrespondencia(correlativo)
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
							if(cantfirma >= 1){
								ref.moduloED.descargarArchivoFirmaDigital(correlativo);
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
};

setTimeout(function() {
	ESTADO_DIGITALIZACION_CONTRATACIONES.modSistcorr = modulo_sistcorr;
	ESTADO_DIGITALIZACION_CONTRATACIONES.modulo = modulo_estado_digitalizacion_contrataciones;
	ESTADO_DIGITALIZACION_CONTRATACIONES.modulo.modSistcorr = modulo_sistcorr; 
	 
	ESTADO_DIGITALIZACION_CONTRATACIONES.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	ESTADO_DIGITALIZACION_CONTRATACIONES.inicializar();
}, 200);