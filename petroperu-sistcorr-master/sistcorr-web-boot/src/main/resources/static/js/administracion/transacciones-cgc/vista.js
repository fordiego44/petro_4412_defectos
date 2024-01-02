var modulo_mantenimiento_transacciones_cgc = MODULO_MANTENIMIENTO_TRANSACCIONES_CGC.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_TRANSACCIONES_VISTA = {
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_TRSANSACION_CGC'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.filtroTipoTrans.val(filtros['tipoTransaccion']);
				ref.modulo.filtroBusqueda.filtroTipoTrans.change();
				ref.modulo.filtroBusqueda.filtroCgcOrigen.val(filtros['cgcOrigen']);
				ref.modulo.filtroBusqueda.filtroCgcOrigen.change();
				ref.modulo.filtroBusqueda.filtroCgcDestino.val(filtros['cgcDestino']);
				ref.modulo.filtroBusqueda.filtroCgcDestino.change();

				let valido = false;
				if(ref.modulo.filtroBusqueda.filtroTipoTrans.val() != ""){
					valido = true;
				}
				
				if(ref.modulo.filtroBusqueda.filtroCgcOrigen.val() != ""){
					valido = true;
				}

				if(ref.modulo.filtroBusqueda.filtroCgcDestino.val() != ""){
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
				ref.modulo.filtroBusqueda.filtroTipoTrans.focus();
			});
		
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarUsuarioCgc();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				//event.preventDefault();
				ref.modulo.filtroBusqueda.filtroTipoTrans.val("");
				ref.modulo.filtroBusqueda.filtroCgcOrigen.val("");
				ref.modulo.filtroBusqueda.filtroCgcOrigen.change();
				ref.modulo.filtroBusqueda.filtroCgcDestino.val("");
				ref.modulo.filtroBusqueda.filtroCgcDestino.change();
				ref.dataTableConsulta = null;
				$('#tablaTransaccionCGC').DataTable().clear().destroy();
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;
				ref.modulo.compModalDatos.tipoTransaccion.val("");
				ref.modulo.compModalDatos.tipoTransaccion.change();
				ref.modulo.compModalDatos.cgcOrigen.val("");
				ref.modulo.compModalDatos.cgcOrigen.change();
				
				ref.modulo.compModalDatos.cgcDestino.val("");
				ref.modulo.compModalDatos.cgcDestino.change();
				ref.modulo.compModalDatos.codigoNumerador.val("");
				ref.modulo.compModalDatos.codigoNumerador.change();
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
				var idTransCgc = comp.attr("data-transscgid");
				
				for(var i in datosConsulta){
					var datoTransCgc = datosConsulta[i];
					if(datoTransCgc.id == idTransCgc){
						ref.modulo.compModalDatos.id = datoTransCgc.id;
						ref.modulo.compModalDatos.tipoTransaccion.val(datoTransCgc.tipoTransaccion);
						ref.modulo.compModalDatos.tipoTransaccion.change();
						ref.modulo.compModalDatos.cgcOrigen.val(datoTransCgc.cgcOrigen);
						ref.modulo.compModalDatos.cgcOrigen.change();
						
						ref.modulo.compModalDatos.cgcDestino.val(datoTransCgc.cgcDestino);
						ref.modulo.compModalDatos.cgcDestino.change();
						ref.modulo.compModalDatos.codigoNumerador.val(datoTransCgc.codigoNumerador);
						ref.modulo.compModalDatos.codigoNumerador.change();
						
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
				var idTransCgc = comp.attr("data-transscgid");
				ref.modulo.compModalDatos.id = idTransCgc;
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
			if(ref.modulo.filtroBusqueda.filtroTipoTrans.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroCgcOrigen.val() != ""){
				valido = true;
			}

			if(ref.modulo.filtroBusqueda.filtroCgcDestino.val() != ""){
				valido = true;
			}
			
			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'tipoTransaccion' : ref.modulo.filtroBusqueda.filtroTipoTrans.val(),
						'cgcOrigen' : ref.modulo.filtroBusqueda.filtroCgcOrigen.val(),
					    'cgcDestino' :ref.modulo.filtroBusqueda.filtroCgcDestino.val()
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_TRSANSACION_CGC", JSON.stringify(filtrosBusqueda));
				setTimeout(function(){
					ref.inicializarTablaConsulta();
				}, 1000)
				
			//}	
		},
		
		
		eliminarUsuarioCGC: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'tipoTransaccion' 		: "",
					'cgcOrigen'    			: "",
					'cgcDestino'   			: "",
					'codigoNumerador'		: "",
					'codigoAccion'       	:'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudTransaccionesCGC(ref.parametros)
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
			if(ref.modulo.compModalDatos.tipoTransaccion.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Tipo Transacción es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.cgcOrigen.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo CGC Origen es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}

			/*if(ref.modulo.compModalDatos.cgcDestino.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo CGC Destino es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}*/
				
			if(ref.modulo.compModalDatos.codigoNumerador.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Numerador es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
				
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'tipoTransaccion' 	: ref.modulo.compModalDatos.tipoTransaccion.val(),
					'cgcOrigen'  		: ref.modulo.compModalDatos.cgcOrigen.val(),
					'cgcDestino'  		: ref.modulo.compModalDatos.cgcDestino.val(),
					'codigoNumerador'  	: ref.modulo.compModalDatos.codigoNumerador.val(),
					'codigoAccion'  	: ref.modulo.compModalDatos.accion
			};
				ref.modulo.crudTransaccionesCGC(ref.parametros)
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
					'tipoTransaccion': ref.modulo.filtroBusqueda.filtroTipoTrans.val(),
					'cgcOrigen'  	 : ref.modulo.filtroBusqueda.filtroCgcOrigen.val(),
					'cgcDestino' 	 : ref.modulo.filtroBusqueda.filtroCgcDestino.val()
			}
			ref.modulo.exportarExcel(filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_transacciones_cgc.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_transacciones_cgc.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_TRANSACCIONES_CGC,
			        	"type": "GET",
			        	"data": {
							'tipoTransaccion' : ref.modulo.filtroBusqueda.filtroTipoTrans.val(),
							'cgcOrigen' : ref.modulo.filtroBusqueda.filtroCgcOrigen.val(),
							'cgcDestino' : ref.modulo.filtroBusqueda.filtroCgcDestino.val(),
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
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-transscgid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-transscgid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'tipoTransaccion', title: 'Tipo Transacción', defaultContent: ''},
			        	{data: 'cgcOrigen', title: 'CGC Origen', defaultContent: ''},
			        	{data: 'cgcDestino', title: 'CGC Destino', defaultContent: ''},
			        	{data: 'codigoNumerador', title: 'Numerador', defaultContent: ''}
			        	
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
					var tableOrder = $("#tablaTransaccionCGC").dataTable();
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
	MANTENIMIENTO_TRANSACCIONES_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_TRANSACCIONES_VISTA.modulo = modulo_mantenimiento_transacciones_cgc;
	MANTENIMIENTO_TRANSACCIONES_VISTA.rol_jefe = ES_JEFE;
	MANTENIMIENTO_TRANSACCIONES_VISTA.rol_gestor = ES_GESTOR;
	MANTENIMIENTO_TRANSACCIONES_VISTA.inicializar();
}, 500);