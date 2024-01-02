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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_PROV_LUGAR_TRABAJO'));
			if(filtros!=null){
		
				ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val(filtros['codigoLugar']);	
				ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.change();
				ref.modulo.filtroBusqueda.filtroCodDepartamento.val(filtros['codigoDepartamento']);
				ref.modulo.filtroBusqueda.filtroCodDepartamento.change();
				ref.modulo.filtroBusqueda.filtroCodProvincia.append("<option value='"+ filtros.codigoProvincia +"' selected='selected'>" + filtros.nombreProvincia + "</option>");	
				ref.modulo.filtroBusqueda.filtroCodProvincia.change();


				let valido = false;

				if(ref.modulo.filtroBusqueda.filtroCodDepartamento.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.filtroCodProvincia.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val() != ""){
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
			ref.modulo.filtroBusqueda.filtroCodDepartamento.select2().change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
				ref.modulo.filtroBusqueda.filtroCodProvincia.val("");
				ref.modulo.filtroBusqueda.filtroCodProvincia.change();
			});			
				
			 ref.modulo.filtroBusqueda.filtroCodProvincia.select2({
					ajax: {
					    url: ref.modulo.URL_BUSCAR_PROVINCIAS,
					    data: function (params) {
					        var query = {
					        		codDep: ref.modulo.filtroBusqueda.filtroCodDepartamento.val(),
					        		q: params.term
					        }
					        return query;
					    },
					    processResults: function (respuesta) {
					    	
							console.log(respuesta.datos);
					    	ref.listas.provincia.agregarLista(respuesta.datos);
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
			
			
			
			//Popup Editar
			ref.modulo.compModalDatos.codigoDepartamento.select2().change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
				ref.modulo.compModalDatos.codigoProvincia.val("");
				ref.modulo.compModalDatos.codigoProvincia.change();
            	
			});			
			
			
			 ref.modulo.compModalDatos.codigoProvincia.select2({
				ajax: {
				    url: ref.modulo.URL_BUSCAR_PROVINCIAS,
				    data: function (params) {
				        var query = {
				        		codDep: ref.modulo.compModalDatos.codigoDepartamento.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
						console.log(respuesta.datos);
				    	ref.listas.provincia.agregarLista(respuesta.datos);
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
				
			ref.modulo.lblFiltroNombre.click(function(){
				ref.modulo.filtroBusqueda.filtroNombre.focus();
			});
		*/
			
			
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarLugarTrabajo();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				//event.preventDefault();
				ref.modulo.filtroBusqueda.filtroCodDepartamento.val("");
				ref.modulo.filtroBusqueda.filtroCodDepartamento.change();
				ref.modulo.filtroBusqueda.filtroCodProvincia.val("");
				ref.modulo.filtroBusqueda.filtroCodProvincia.change();
				ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val("");
				ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.change();

				ref.dataTableConsulta = null;
				$('#tablaProvinciaLugarTrabajo').DataTable().clear().destroy();
		
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;
				ref.modulo.compModalDatos.codigoDepartamento.val("");
				ref.modulo.compModalDatos.codigoDepartamento.change();
				ref.modulo.compModalDatos.codigoProvincia.val("");
				ref.modulo.compModalDatos.codigoProvincia.change();
				ref.modulo.compModalDatos.codigoLugar.val("");
				ref.modulo.compModalDatos.codigoLugar.change();

	
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
				var idLugarTrabajo = comp.attr("data-lugartrabajoid");
				
				for(var i in datosConsulta){
					var datoLugarTrab = datosConsulta[i];
					if(datoLugarTrab.id == idLugarTrabajo){
						ref.modulo.compModalDatos.id = datoLugarTrab.id;
						ref.modulo.compModalDatos.codigoDepartamento.val(datoLugarTrab.codigoDepartamento);
						ref.modulo.compModalDatos.codigoDepartamento.change();
			
						ref.modulo.compModalDatos.codigoProvincia.append("<option value='"+ datoLugarTrab.codigoProvincia +"' selected='selected'>" + datoLugarTrab.nombreProvincia + "</option>");	
						ref.modulo.compModalDatos.codigoProvincia.change();
						
						ref.modulo.compModalDatos.codigoLugar.val(datoLugarTrab.codigoLugar);
						ref.modulo.compModalDatos.codigoLugar.change();

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
				var idLugarTrabajo = comp.attr("data-lugartrabajoid");
				ref.modulo.compModalDatos.id = idLugarTrabajo;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarLugarTrabajo();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateLugarTrabajo();
			});
			
			/*ref.modulo.compModalDatos.codigoDistrito.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});*/
		},
		

		
		buscarLugarTrabajo: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.filtroCodDepartamento.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroCodProvincia.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val() != ""){
				valido = true;
			}
			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'codigoDepartamento' : ref.modulo.filtroBusqueda.filtroCodDepartamento.val(),
						'codigoProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.val(),
						'nombreProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.find("option:selected").text(),
						'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val()
						
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_PROV_LUGAR_TRABAJO", JSON.stringify(filtrosBusqueda));
				setTimeout(function(){
					ref.inicializarTablaConsulta();
				}, 1000)
				
			//}	
		},
		
		
		eliminarLugarTrabajo: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoDepartamento' : 0,
					'codigoProvincia'    : 0,
					'codigoLugar'        : '',
					'codigoAccion'       : 'D'
			};
			ref.modSistcorr.procesar(true);
		
				ref.modulo.crudLugarTrabajo(ref.parametros)
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
		
		insertUpdateLugarTrabajo: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compModalDatos.codigoDepartamento.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Departamento es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.codigoProvincia.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Provincia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			

			if(ref.modulo.compModalDatos.codigoLugar.val() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Lugar Trabajo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoDepartamento' : ref.modulo.compModalDatos.codigoDepartamento.val(),
					'codigoProvincia'    : ref.modulo.compModalDatos.codigoProvincia.val(),					
					'codigoLugar'   	 : ref.modulo.compModalDatos.codigoLugar.val(),
					'codigoAccion'       : ref.modulo.compModalDatos.accion
			};
				ref.modulo.crudLugarTrabajo(ref.parametros)
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
					'codigoDepartamento' : ref.modulo.filtroBusqueda.filtroCodDepartamento.val(),
					'codigoProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.val(),
					'nombreProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.find("option:selected").text(),
					'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val()
					
		
			}
			ref.modulo.exportarExcel(filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_lugar_trabajo.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_lugar_trabajo.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_PROV_LUGAR_TRABAJO,
			        	"type": "GET",
			        	"data": {
							'codigoDepartamento' : ref.modulo.filtroBusqueda.filtroCodDepartamento.val(),
							'codigoProvincia' : ref.modulo.filtroBusqueda.filtroCodProvincia.val(),
							'codigoLugar' : ref.modulo.filtroBusqueda.filtroCodLugarTrabajo.val(),
						
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
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-lugartrabajoid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-lugartrabajoid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'nombreDepartamento', title: 'Departamento', defaultContent: ''},
			        	{data: 'nombreProvincia', title: 'Provincia', defaultContent: ''},
			        	{data: 'nombreLugar', title: 'Nombre Lugar', defaultContent: ''}			        	
			        	
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
					var tableOrder = $("#tablaProvinciaLugarTrabajo").dataTable();
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
	MANTENIMIENTO_DISTRITO_VISTA.listas = {};
	MANTENIMIENTO_DISTRITO_VISTA.listas.departamento =  new LISTA_DATA([]);
	MANTENIMIENTO_DISTRITO_VISTA.listas.provincia =  new LISTA_DATA([]);
	MANTENIMIENTO_DISTRITO_VISTA.listas.distrito =  new LISTA_DATA([]);
	MANTENIMIENTO_DISTRITO_VISTA.inicializar();
}, 500);