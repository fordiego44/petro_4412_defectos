var modulo_mantenimiento_numerador = MODULO_MANTENIMIENTO_NUMERADOR.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_NUMERADOR_VISTA = {
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_CONSULTA_NUMERADOR'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.filtroNombre.val(filtros['codigoNumerador']);
				ref.modulo.filtroBusqueda.filtroNombre.change();

				let valido = false;
				if(ref.modulo.filtroBusqueda.filtroNombre.val() != ""){
					valido = true;
				}

				if(valido){
					setTimeout(function(){
						ref.modulo.compBusqueda.btnBuscar.click();
					},1000);
				}else{
					setTimeout(function(){
						console.log("INICIALIZANDO TABLA");
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
					
			/*ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				var comp = $(this);
				var idDepartamento = comp.attr("data-numeradorid");
	
			});
			*/

			ref.modulo.lblFiltroNombre.click(function(){
				ref.modulo.filtroBusqueda.filtroNombre.focus();
			});
	
		
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarNumerador();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				ref.modulo.filtroBusqueda.filtroNombre.val("");
	
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;
				ref.modulo.compModalDatos.codigoNumerador.val("");
				ref.modulo.compModalDatos.codigoNumerador.change();
				ref.modulo.compModalDatos.ultimoCorrelativo.val("");
				ref.modulo.compModalDatos.ultimoCorrelativo.change();
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
				var idNumerador = comp.attr("data-numeradorid");
				
				for(var i in datosConsulta){
					var data = datosConsulta[i];
					if(data.id == idNumerador){
						ref.modulo.compModalDatos.id = data.id;
						ref.modulo.compModalDatos.accion = "U";
						ref.modulo.compModalDatos.codigoNumerador.val(data.codigoNumerador);
						ref.modulo.compModalDatos.codigoNumerador.change();
						ref.modulo.compModalDatos.ultimoCorrelativo.val(data.ultimoCorrelativo);
						ref.modulo.compModalDatos.ultimoCorrelativo.change();
						break;
					}
				}
				
				setTimeout(function(){
					ref.modulo.compModalActualizar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnGrillaEliminar.off('click').on('click', function(){
				var comp = $(this);
				var idNumerador = comp.attr("data-numeradorid");
				ref.modulo.compModalDatos.id = idNumerador;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarNumerador();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateNumerador();
			});
		
			ref.modulo.compModalDatos.codigoNumerador.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});
			
			ref.modulo.compModalDatos.ultimoCorrelativo.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});
			
			$("#textFiltroNombre").on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});
		},
		

		
		buscarNumerador: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.filtroNombre.val() != ""){
				valido = true;
			}
			
				var filtrosBusqueda = {
						'codigoNumerador' : ref.modulo.filtroBusqueda.filtroNombre.val(),
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_CONSULTA_NUMERADOR", JSON.stringify(filtrosBusqueda));
				ref.inicializarTablaConsulta();
			//}	
		},
		
		
		eliminarNumerador: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoNumerador' : 0,
					'codigoAccion' : 'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudNumerador(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalEliminar.modal('hide');
							ref.inicializarTablaConsulta();
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
		
		insertUpdateNumerador: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compModalDatos.codigoNumerador.val() == ""){
				//valido = true;
				ref.modSistcorr.notificar("ERROR", "El campo Código es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}

			if(ref.modulo.compModalDatos.ultimoCorrelativo.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Último Correlativo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoNumerador': ref.modulo.compModalDatos.codigoNumerador.val(),
					'ultimoCorrelativo' : ref.modulo.compModalDatos.ultimoCorrelativo.val(),
					'codigoAccion' : ref.modulo.compModalDatos.accion
			};
				ref.modulo.crudNumerador(ref.parametros)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.modulo.compModalActualizar.modal('hide');
							ref.inicializarTablaConsulta();
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
				ref.filtrosBusqueda = {'codigoNumerador' : (ref.modulo.filtroBusqueda.filtroNombre.val().trim() == "")?("0"):(ref.modulo.filtroBusqueda.filtroNombre.val().trim())};
			}
			
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_numeradores.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_numeradores.xlsx';
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
				var asdasd = ref.modulo.filtroBusqueda.filtroNombre.val();
				ref.dataTableConsulta = ref.modulo.compBusqueda.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_BUSCAR_NUMERADOR,
			        	"type": "GET",
			        	"data": {
			        		'codigoNumerador' : ref.modulo.filtroBusqueda.filtroNombre.val(),
						
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
			        		//location.reload();
			        	}
			        },
			        cache: false,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-numeradorid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-numeradorid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'codigoNumerador', title: 'Código Numerador', defaultContent: ''},
			        	{data: 'ultimoCorrelativo', title: 'Último correlativo', defaultContent: ''}
			        	
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
					var tableOrder = $("#tablaNumerador").dataTable();
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
	MANTENIMIENTO_NUMERADOR_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_NUMERADOR_VISTA.modulo = modulo_mantenimiento_numerador;
	MANTENIMIENTO_NUMERADOR_VISTA.rol_jefe = ES_JEFE;
	MANTENIMIENTO_NUMERADOR_VISTA.rol_gestor = ES_GESTOR;
	MANTENIMIENTO_NUMERADOR_VISTA.inicializar();
}, 500);