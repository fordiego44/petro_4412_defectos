var modulo_mantenimiento_cgc = MODULO_MANTENIMIENTO_CGC.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_CGC_VISTA = {
		modSistcorr: null,
		modulo: null,
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_CGC'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.filtroNomCGC.val(filtros['nombreCGC']);
				ref.modulo.filtroBusqueda.filtroNomCGC.change();
				
				ref.modulo.filtroBusqueda.filtroCodLugar.val(filtros['codigoLugar']);
				ref.modulo.filtroBusqueda.filtroCodLugar.select2();

				let valido = false;
				if(ref.modulo.filtroBusqueda.filtroNomCGC.val() != ""){
					valido = true;
				}

				if(ref.modulo.filtroBusqueda.filtroCodLugar.val() != ""){
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
				ref.modulo.filtroBusqueda.filtroNomCGC.focus();
			});
		
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarCgc();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				ref.modulo.filtroBusqueda.filtroNomCGC.val("");
				ref.modulo.filtroBusqueda.filtroCodLugar.val("0");
				ref.modulo.filtroBusqueda.filtroCodLugar.select2();
				ref.dataTableConsulta = null;
				$('#tablaCgc').DataTable().clear().destroy();
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
				ref.modulo.compModalDatos.nombreCGC.val("");
				ref.modulo.compModalDatos.nombreCGC.change();
				ref.modulo.compModalDatos.codigoLugar.val("0");
				ref.modulo.compModalDatos.codigoLugar.select2();
				ref.modulo.compModalDatos.tipoRotulo.val("");
				ref.modulo.compModalDatos.tipoRotulo.change();
				ref.modulo.compModalDatos.mCodigoBarras.val("");
				ref.modulo.compModalDatos.mCodigoBarras.change();
				ref.modulo.compModalDatos.impresora.val("");
				ref.modulo.compModalDatos.impresora.change();
				ref.modulo.compModalDatos.tipoImpresora.val("");
				ref.modulo.compModalDatos.tipoImpresora.change();
				ref.modulo.compModalDatos.mComputarizado.val("");
				ref.modulo.compModalDatos.mComputarizado.change();
				ref.modulo.compModalDatos.codigoERP.val("");
				ref.modulo.compModalDatos.codigoERP.change();
			
				setTimeout(function(){
					ref.modulo.compModalActualizar.modal('show');
				}, 300);
			});
			
			ref.modulo.compBusqueda.btnFiltros.click();
			ref.modulo.compBusqueda.btnMenosFiltros.click();
			
		},

			
		eventoDocumentoPrincipal: function(){
			var ref = this;
			ref.modulo.btnGrillaEditar.off('click').on('click', function(){
					
				var comp = $(this);
				var idCgc = comp.attr("data-cgcid");
				
				for(var i in datosConsulta){
					var datoCgc = datosConsulta[i];
					if(datoCgc.id == idCgc){
						ref.modulo.compModalDatos.id = datoCgc.id;
						ref.modulo.compModalDatos.codigoCGC.val(datoCgc.codigoCGC);
						ref.modulo.compModalDatos.codigoCGC.change();
						ref.modulo.compModalDatos.nombreCGC.val(datoCgc.nombreCGC);
						ref.modulo.compModalDatos.nombreCGC.change();
						ref.modulo.compModalDatos.codigoLugar.val(datoCgc.codigoLugar);
						ref.modulo.compModalDatos.codigoLugar.select2();
						
						ref.modulo.compModalDatos.tipoRotulo.val(datoCgc.tipoRotulo);
						ref.modulo.compModalDatos.tipoRotulo.change();
						ref.modulo.compModalDatos.mCodigoBarras.val(datoCgc.mCodigoBarras);
						ref.modulo.compModalDatos.mCodigoBarras.change();
						ref.modulo.compModalDatos.impresora.val(datoCgc.impresora);
						ref.modulo.compModalDatos.impresora.change();
						ref.modulo.compModalDatos.tipoImpresora.val(datoCgc.tipoImpresora);
						ref.modulo.compModalDatos.tipoImpresora.change();
						ref.modulo.compModalDatos.mComputarizado.val(datoCgc.mComputarizado);
						ref.modulo.compModalDatos.mComputarizado.change();
						ref.modulo.compModalDatos.codigoERP.val(datoCgc.codigoERP);
						ref.modulo.compModalDatos.codigoERP.change();
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
				var idCgc = comp.attr("data-cgcid");
				ref.modulo.compModalDatos.id = idCgc;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarPais();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateCgc();
			});
		
			/*ref.modulo.compModalDatos.codigoComprobante.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});*/
			
		},
		

		
		buscarCgc: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.filtroNomCGC.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroCodLugar.val() != "0"){
				valido = true;
			}
			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'nombreCGC' : ref.modulo.filtroBusqueda.filtroNomCGC.val(),
						'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugar.val(),
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_CGC", JSON.stringify(filtrosBusqueda));

				setTimeout(function(){
					ref.inicializarTablaConsulta();
				},1000);
			//}	
		},
		
		
		eliminarPais: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoCGC'     : '',
					'nombreCGC'     : '',
					'codigoLugar'   : '',
					'tipoRotulo'    : '',
					'mCodigoBarras' : '',
					'impresora'     : '',
					'tipoImpresora' : '',
					'mComputarizado': '',
					'codigoERP'     : '',
					'codigoAccion' : 'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudCgc(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalEliminar.modal('hide');
							setTimeout(function(){
								ref.inicializarTablaConsulta();
							},1000);
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
		
		insertUpdateCgc: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compModalDatos.codigoCGC.val().trim() == ""){
				//valido = true;
				ref.modSistcorr.notificar("ERROR", "El campo Código es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}

			if(ref.modulo.compModalDatos.nombreCGC.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Nombre es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			if(ref.modulo.compModalDatos.codigoLugar.val() == "0"){
				ref.modSistcorr.notificar("ERROR", "El campo Lugar Trabajo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			if(ref.modulo.compModalDatos.mComputarizado.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Marca CGC Computarizado es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			var ddd = ref.modulo.compModalDatos.codigoLugar.val();
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoCGC'     : ref.modulo.compModalDatos.codigoCGC.val(),
					'nombreCGC'     : ref.modulo.compModalDatos.nombreCGC.val(),
					'codigoLugar'   : ref.modulo.compModalDatos.codigoLugar.val(),
					'tipoRotulo'    : ref.modulo.compModalDatos.tipoRotulo.val(),
					'mCodigoBarras' : ref.modulo.compModalDatos.mCodigoBarras.val(),
					'impresora'     : ref.modulo.compModalDatos.impresora.val(),
					'tipoImpresora' : ref.modulo.compModalDatos.tipoImpresora.val(),
					'mComputarizado': ref.modulo.compModalDatos.mComputarizado.val(),
					'codigoERP'     : ref.modulo.compModalDatos.codigoERP.val(),
					'codigoAccion'  : ref.modulo.compModalDatos.accion
			};
				ref.modulo.crudCgc(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalActualizar.modal('hide');
							setTimeout(function(){
								ref.inicializarTablaConsulta();
							},1000);
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
			if(!(ref.filtrosBusqueda != undefined)){
				ref.filtrosBusqueda = {
						'nombreCGC' : ref.modulo.filtroBusqueda.filtroNomCGC.val(),
						'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugar.val(),
			
				}
			}
			
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_cgc.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_cgc.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_CGC,
			        	"type": "GET",
			        	"data": {
			        		'nombreCGC' : ref.modulo.filtroBusqueda.filtroNomCGC.val(),
			        		'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugar.val()
						
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
			        		console.log("Error Consulta CGC");
			        		console.log(result);
			        		ref.modSistcorr.procesar(false);
			        		//location.reload();
			        	}
			        },
			        cache: false,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-cgcid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-cgcid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'codigoCGC', title: 'Código CGC', defaultContent: ''},
			        	{data: 'nombreCGC', title: 'Nombre CGC', defaultContent: ''},
			        	{data: 'tipoRotulo', title: 'Tipo Rótulo', defaultContent: ''},
			        	{data: 'mCodigoBarras', title: 'Código Barras', defaultContent: ''},
			        	{data: 'impresora', title: 'Impresora', defaultContent: ''},
			        	{data: 'tipoImpresora', title: 'Tipo Impresora', defaultContent: ''},
			        	{data: 'nombreLugar', title: 'Lugar de Trabajo', defaultContent: ''},
			        	{data: 'mComputarizado', title: 'Marca CGC Compu', defaultContent: ''},
			        	{data: 'codigoERP', title: 'Código ERP', defaultContent: ''}
			        	
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
					var tableOrder = $("#tablaCgc").dataTable();
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
	MANTENIMIENTO_CGC_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_CGC_VISTA.modulo = modulo_mantenimiento_cgc;
	MANTENIMIENTO_CGC_VISTA.inicializar();
}, 500);