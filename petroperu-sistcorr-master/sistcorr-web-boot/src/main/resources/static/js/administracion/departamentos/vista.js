var modulo_consulta_asignaciones = MODULO_CONSULTA_ASIGNACIONES.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_ASIGNACIONES_VISTA = {
		modSistcorr: null,
		modulo: null,
		errores: [],
		rol_jefe: false,
		rol_gestor: false,
		filtrosBusqueda: null,
		filtro_correlativo: null,
		datosConsulta: [],
		dataTable: null,
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarComponentes();
			ref.iniciarEventos();
			ref.modSistcorr.eventoTooltip();
			ref.obtenerFiltros();				
		},
		
		obtenerFiltros: function(){
			var ref = this;
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_DEPARTAMENTO'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.departamento.val(filtros['departamento']);
				ref.modulo.filtroBusqueda.departamento.change();

				let valido = false;
				if(ref.modulo.filtroBusqueda.departamento.val() != ""){
					valido = true;
				}

				if(valido){
					ref.modulo.compBusqueda.btnBuscar.click();
				}else{
					setTimeout(function(){
						ref.inicializarTablaConsulta();
					}, 1000)
				}
			}else{
				setTimeout(function(){
					console.log("INICIALIZANDO TABLA");
					// ref.inicializarTabla([]);
					ref.inicializarTablaConsulta();
				}, 1000)
				
			}		
		},
		
		
		inicializarComponentes: function(){
			console.log("Vista>>inicializarComponentes");
			var ref = this;
		
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.compBusqueda.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.modulo.btnAbrirBusqueda.click(function(event){
				console.log("Abrir modal");
				// ref.abrirModalBusqueda();
			});
			
			ref.modulo.btnRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
			
			ref.modulo.btnVerificarDocumentoPrincipalAsignacion.off('click').on('click', function(){

				
				var comp = $(this);
				var idDepartamento = comp.attr("data-departamentoid");
				setTimeout(function(){
					ref.modulo.compModalAddUpdate.modal('show');
				}, 300);
			});
			
			ref.modulo.btnEliminar.off('click').on('click', function(){
				var comp = $(this);
				var idDepartamento = comp.attr("data-departamentoid");
				setTimeout(function(){
					ref.modulo.compModalEliminarDepartamento.modal('show');
				}, 300);
	
			});
					
			ref.modulo.btnEliminarDepartamento.off('click').on('click', function(){
				var comp = $(this);
				var idDepartamento = comp.attr("data-departamentoid");
	
			});
			

			ref.modulo.lblDepartamento.click(function(){
				ref.modulo.filtroBusqueda.departamento.focus();
			});
	
		
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				// ref.validarCampos();
				ref.buscarDepartamentos();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				ref.modulo.filtroBusqueda.departamento.val("");
				ref.modulo.filtroBusqueda.departamento.change();
				ref.dataTableConsulta = null;
				$('#tablaDepartamentosGeograficos').DataTable().clear().destroy();
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.objetoDepartamento.accion = "I";
				ref.modulo.objetoDepartamento.codigoDepartamento.val("");
				ref.modulo.objetoDepartamento.codigoDepartamento.change();
				ref.modulo.objetoDepartamento.departamento.val("");
				ref.modulo.objetoDepartamento.departamento.change();
				setTimeout(function(){
					ref.modulo.compModalAddUpdate.modal('show');
				}, 300);
			});
			
			ref.modulo.compBusqueda.btnFiltros.click();
			ref.modulo.compBusqueda.btnMenosFiltros.click();
		},
		

		eventoDocumentoPrincipal: function(){
			var ref = this;
			ref.modulo.btnVerificarDocumentoPrincipalAsignacion.off('click').on('click', function(){
					
				var comp = $(this);
				var idDepartamento = comp.attr("data-departamentoid");
				
				for(var i in datosConsulta){
					var depa = datosConsulta[i];
					if(depa.id == idDepartamento){
						ref.modulo.objetoDepartamento.id = depa.id;
						ref.modulo.objetoDepartamento.accion = "U";
						ref.modulo.objetoDepartamento.codigoDepartamento.val(depa.codigoDepartamento);
						ref.modulo.objetoDepartamento.codigoDepartamento.change();
						ref.modulo.objetoDepartamento.departamento.val(depa.departamento);
						ref.modulo.objetoDepartamento.departamento.change();
						break;
					}
				}
				
				setTimeout(function(){
					ref.modulo.compModalAddUpdate.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminar.off('click').on('click', function(){
				var comp = $(this);
				var idDepartamento = comp.attr("data-departamentoid");
				ref.modulo.objetoDepartamento.id = idDepartamento;
				setTimeout(function(){
					ref.modulo.compModalEliminarDepartamento.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarDepartamento.off('click').on('click', function(){
				ref.eliminarDepartamento();
			}),
			
			ref.modulo.btnGrabar.off('click').on('click', function(){
				ref.insertUpdateDepartamento();
			});
		
			ref.modulo.objetoDepartamento.codigoDepartamento.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});
			
		},
		

		
		buscarDepartamentos: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.departamento.val() != ""){
				valido = true;
			}
	
				var filtrosBusqueda = {
						'departamento' : ref.modulo.filtroBusqueda.departamento.val(),
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_DEPARTAMENTO", JSON.stringify(filtrosBusqueda));
				ref.inicializarTablaConsulta();
				
		},
		
		
		eliminarDepartamento: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.objetoDepartamento.id,
					'departamento' : '',
					'codigoAccion' : 'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudDepartamentoGeogradifo(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalEliminarDepartamento.modal('hide');
							// ref.mostrarAsignaciones();
							ref.inicializarTablaConsulta();
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compModalEliminarDepartamento.modal('hide');
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		insertUpdateDepartamento: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.objetoDepartamento.codigoDepartamento.val().trim() == ""){
				// valido = true;
				ref.modSistcorr.notificar("ERROR", "El campo Código es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}

			if(ref.modulo.objetoDepartamento.departamento.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Nombre es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			ref.parametros = {
					'id' : 	ref.modulo.objetoDepartamento.id,
					'codigoDepartamento': parseInt(ref.modulo.objetoDepartamento.codigoDepartamento.val()),
					'departamento' : ref.modulo.objetoDepartamento.departamento.val(),
					'codigoAccion' : ref.modulo.objetoDepartamento.accion
			};
				ref.modulo.crudDepartamentoGeogradifo(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modulo.compModalAddUpdate.modal('hide');
							ref.modSistcorr.procesar(false);
							setTimeout(function(){
								ref.inicializarTablaConsulta();
							}, 1000);
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compModalAddUpdate.modal('hide');
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		
		insertaDepartamento: function(){
			var ref = this;
			ref.parametros = {
					'codigoDepartamento': ref.modulo.objetoDepartamento.codigoDepartamento.val(),
					'departamento' :ref.modulo.objetoDepartamento.departamento.val(),
					'codigoAccion' : 'I'
			};
				ref.modulo.crudDepartamentoGeogradifo(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						ref.modulo.compModalAddUpdate.modal('hide');
						ref.inicializarTablaConsulta();
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compModalAddUpdate.modal('hide');
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		
		exportarExcel: function(){
			var ref = this;
			if(!(ref.filtrosBusqueda != undefined)){
				ref.filtrosBusqueda = {'departamento' : ref.modulo.filtroBusqueda.departamento.val()};
			}
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_departamentos_geograficos.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_departamentos_geograficos.xlsx';
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
				ref.modulo.compBusqueda.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.modulo.compBusqueda.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.compBusqueda.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_BUSCAR_DEPARTAMENTOS_GEOGRAFICOS,
			        	"type": "GET",
			        	"data": {
			        		'departamento' : ref.modulo.filtroBusqueda.departamento.val(),
						
			        	},
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
			        		// location.reload();
			        	}
			        },
			        cache: true,
			        // "pageLength": 10,
			        "columns": [
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        			return "<i class='fas fa-edit icon_editar btnVerificarDocPrincipal_Asignacion' data-departamentoid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"
			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnEliminar' data-departamentoid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'codigoDepartamento', title: 'Código Departamento', defaultContent: ''},
			        	{data: 'departamento', title: 'Nombre Departamento', defaultContent: ''}
			        	
			        ],
			        "columnDefs": [

		                {width     : "25px", targets: [0, 1]},
		                {orderable : false,  targets: [0, 1]}
		            ],
			
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_CA");
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
			        		var nroPag = sessionStorage.getItem("NroPag_CA");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPagCA");
			        		console.log("OrigPag:" + origPag);
			        		if(origPag == "verDetalle"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagCA");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					ref.modulo.btnEliminar = $(".btnEliminar");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CA", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.modulo.btnEliminar = $(".btnEliminar");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaDepartamentosGeograficos").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					// var api = ref.dataTableConsulta.api();
					// var order = ref.dataTableConsulta.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CA", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.modulo.btnEliminar = $(".btnEliminar");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					ref.modulo.btnEliminar = $(".btnEliminar");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					// ref.modulo.irADetalle(this.dataset.id);
				});
			}
		}
};

setTimeout(function(){
	CONSULTA_ASIGNACIONES_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTA_ASIGNACIONES_VISTA.modulo = modulo_consulta_asignaciones;
	CONSULTA_ASIGNACIONES_VISTA.inicializar();
}, 500);