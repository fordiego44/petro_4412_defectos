var modulo_mantenimiento_usuario_cgc = MODULO_MANTENIMIENTO_USUARIO_CGC.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_DISTRITO_VISTA = {
		modSistcorr: null,
		modulo: null,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		errores: [],
		rol_jefe: false,
		rol_gestor: false,
		datosConsulta: [],
		//dataTable: null,
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_USUARIO_CGC'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.filtroNomGestor.val(filtros['nombreGestor']);
				ref.modulo.filtroBusqueda.filtroNomGestor.change();
				ref.modulo.filtroBusqueda.filtroCodigoCGC.val(filtros['codigoCGC']);
				ref.modulo.filtroBusqueda.filtroCodigoCGC.change();

				
				let valido = false;
				if(ref.modulo.filtroBusqueda.filtroNomGestor.val() != ""){
					valido = true;
				}

				if(ref.modulo.filtroBusqueda.filtroCodigoCGC.val() != ""){
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
					ref.inicializarTablaConsulta();
				}, 1000)
				
			}			
		},
		
		inicializarComponentes: function(){
			
			console.log("Vista>>inicializarComponentes");
			var ref = this;
			ref.componentes.combosS2.codigoCGC = ref.modulo.compModalDatos.codigoCGC.select2({ 
				
			})
			.change(function(event){
				var $comp = $(event.currentTarget);
				
			}).on('select2:open', function(event){
				
			}).on('select2:closing', function (event) {
				
			});
			
			ref.componentes.combosS2.registro = ref.modulo.compModalDatos.registro.select2({ 
				
			})
			.change(function(event){
				var $comp = $(event.currentTarget);
				
			}).on('select2:open', function(event){
				
			}).on('select2:closing', function (event) {
				
			});
			
			ref.componentes.combosS2.busqCodigoCgc = ref.modulo.filtroBusqueda.filtroCodigoCGC.select2({ 
				
			})
			.change(function(event){
				var $comp = $(event.currentTarget);
				
			}).on('select2:open', function(event){
				
			}).on('select2:closing', function (event) {
				
			});
		},
				
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.compBusqueda.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
		
			ref.modulo.btnRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
			
			ref.modulo.btnGrillaEditar.off('click').on('click', function(){

				
				var comp = $(this);
				setTimeout(function(){
					ref.modulo.compModalActualizar.modal('show');
				}, 300);
			});
			
			ref.modulo.btnGrillaEliminar.off('click').on('click', function(){
				var comp = $(this);
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
	
			});
					
		/*	ref.modulo.lblFiltroEstado.click(function(){
				ref.modulo.filtroBusqueda.filtroEstado.focus();
			});
			*/	
			ref.modulo.lblFiltroNombre.click(function(){
				ref.modulo.filtroBusqueda.filtroNomGestor.focus();
			});
		
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarUsuarioCgc();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				//event.preventDefault();
				ref.modulo.filtroBusqueda.filtroNomGestor.val("");
				ref.modulo.filtroBusqueda.filtroCodigoCGC.val("");
				ref.modulo.filtroBusqueda.filtroCodigoCGC.change();
				ref.dataTableConsulta = null;
				$('#tablaUsuarioCGC').DataTable().clear().destroy();
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;
				ref.modulo.compModalDatos.codigoCGC.val("");
				ref.modulo.compModalDatos.codigoCGC.change();
				ref.modulo.compModalDatos.registro.val("");
				ref.modulo.compModalDatos.registro.change();
				setTimeout(function(){
					ref.modulo.compModalActualizar.modal('show');
				}, 100);
			});
			
			ref.modulo.compBusqueda.btnFiltros.click();
			ref.modulo.compBusqueda.btnMenosFiltros.click();
			
		},

	 
		eventoDocumentoPrincipal: function(){
			var ref = this;
			ref.modulo.btnGrillaEditar.off('click').on('click', function(){
					
				var comp = $(this);
				var idUsuarioCgc = comp.attr("data-usuariocgcid");
				
				for(var i in datosConsulta){
					var datoUsuarioCgc = datosConsulta[i];
					if(datoUsuarioCgc.id == idUsuarioCgc){
						ref.modulo.compModalDatos.id = datoUsuarioCgc.id;
						ref.modulo.compModalDatos.codigoCGC.val(datoUsuarioCgc.codigoCGC);
						ref.modulo.compModalDatos.codigoCGC.change();
						ref.modulo.compModalDatos.registro.val(datoUsuarioCgc.registro.toUpperCase());
						ref.modulo.compModalDatos.registro.change();
						ref.modulo.compModalDatos.accion = "U";
						break;
					}
				}
				
				setTimeout(function(){
					ref.modulo.compModalActualizar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnGrillaEliminar.off('click').on('click', function(){
				var comp = $(this);
				var idUsuarioCgc = comp.attr("data-usuariocgcid");
				ref.modulo.compModalDatos.id = idUsuarioCgc;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarUsuarioCGC();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateUsuarioCGC();
			});
			
		},
		

		
		buscarUsuarioCgc: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.filtroNomGestor.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroCodigoCGC.val() != ""){
				valido = true;
			}

			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'codigoCGC' : ref.modulo.filtroBusqueda.filtroCodigoCGC.val(),
						'nombreGestor' : ref.modulo.filtroBusqueda.filtroNomGestor.val()
					//	'nombreProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.find("option:selected").text()
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_USUARIO_CGC", JSON.stringify(filtrosBusqueda));
				setTimeout(function(){
					ref.inicializarTablaConsulta();
				}, 1000)
				
			//}	
		},
		
		
		eliminarUsuarioCGC: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoCGC' : "",
					'registro'    : "",
					'codigoAccion'       : 'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudUsuarioCGC(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalEliminar.modal('hide');
							setTimeout(function(){
								ref.inicializarTablaConsulta();
							}, 1000);
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compModalEliminar.modal('hide');
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		insertUpdateUsuarioCGC: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compModalDatos.codigoCGC.val() == undefined || ref.modulo.compModalDatos.codigoCGC.val() == null || ref.modulo.compModalDatos.codigoCGC.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo CGC es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.registro.val() == undefined || ref.modulo.compModalDatos.registro.val() == null || ref.modulo.compModalDatos.registro.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
					
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoCGC' 	: ref.modulo.compModalDatos.codigoCGC.val(),
					'registro'  	: ref.modulo.compModalDatos.registro.val(),					
					'codigoAccion'  : ref.modulo.compModalDatos.accion
			};
				ref.modulo.crudUsuarioCGC(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalActualizar.modal('hide');
							setTimeout(function(){
								ref.inicializarTablaConsulta();
							}, 1000);
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compModalActualizar.modal('hide');
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
				
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			var filtrosBusqueda = {
					'codigoCGC' : ref.modulo.filtroBusqueda.filtroCodigoCGC.val(),
					'nombreGestor' : ref.modulo.filtroBusqueda.filtroNomGestor.val()
			}
			ref.modulo.exportarExcel(filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_usuario_cgc.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_usuario_cgc.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_USUARIO_CGC,
			        	"type": "GET",
			        	"data": {
							'codigoCGC' : ref.modulo.filtroBusqueda.filtroCodigoCGC.val(),
							'nombreGestor' : ref.modulo.filtroBusqueda.filtroNomGestor.val(),
						
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
			        		console.log("Error Consulta Estado");
			        		console.log(result);
			        		ref.modSistcorr.procesar(false);
			        		//location.reload();
			        	}
			        },
			        cache: false,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-usuariocgcid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-usuariocgcid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'nombreCGC', title: 'Nombre CGC', defaultContent: ''},
			        	{data: 'nombreGestor', title: 'Nombre Usuario', defaultContent: ''}
			        	
			        ],
			        "columnDefs": [
			        	//{visible   : false,  targets: [0]},
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
					//ref.updateEventosTabla();
					ref.modulo.btnGrillaEditar = $(".btnGrillaEditar");
					ref.modulo.btnGrillaEliminar = $(".btnGrillaEliminar");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CA", pagActual.page);
	        		setTimeout(function() {
						//ref.updateEventosTabla();
						ref.modulo.btnGrillaEditar = $(".btnGrillaEditar");
						ref.modulo.btnGrillaEliminar = $(".btnGrillaEliminar");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaUsuarioCGC").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CA", order);
					}
	        		setTimeout(function() {
						//ref.updateEventosTabla();
						ref.modulo.btnGrillaEditar = $(".btnGrillaEditar");
						ref.modulo.btnGrillaEliminar = $(".btnGrillaEliminar");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					//ref.updateEventosTabla();
					ref.modulo.btnGrillaEditar = $(".btnGrillaEditar");
					ref.modulo.btnGrillaEliminar = $(".btnGrillaEliminar");
					ref.eventoDocumentoPrincipal();
				}, 1500);
				
			}
		},
		
		
		
};

setTimeout(function(){
	MANTENIMIENTO_DISTRITO_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_DISTRITO_VISTA.modulo = modulo_mantenimiento_usuario_cgc;
	MANTENIMIENTO_DISTRITO_VISTA.rol_jefe = ES_JEFE;
	MANTENIMIENTO_DISTRITO_VISTA.rol_gestor = ES_GESTOR;
	MANTENIMIENTO_DISTRITO_VISTA.inicializar();
}, 500);