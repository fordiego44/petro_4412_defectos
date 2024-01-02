var modulo_mantenimiento_dependencia_externa = MODULO_MANTENIMIENTO_DEPENDENCIA_EXTERNA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA = {
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_DEPENDENCIA_EXTERNA'));
			if(filtros!=null){
		
				ref.modulo.filtroBusqueda.filtroNombre.val(filtros['nombreDependencia']);
				ref.modulo.filtroBusqueda.filtroNombre.change();
				
				ref.modulo.filtroBusqueda.filtroRuc.val(filtros['ruc']);
				ref.modulo.filtroBusqueda.filtroRuc.change();
				
				let valido = false;
				if(ref.modulo.filtroBusqueda.filtroRuc.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.filtroNombre.val() != ""){
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
			
			//Popup Editar
			ref.modulo.compModalDatos.codigoDepartamento.select2().change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
				ref.modulo.compModalDatos.codigoProvincia.val('0000');
				ref.modulo.compModalDatos.codigoProvincia.change();
				ref.modulo.compModalDatos.codigoDistrito.val('0000');
				ref.modulo.compModalDatos.codigoDistrito.change();
            	
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
				ref.modulo.compModalDatos.codigoDistrito.val('0000');
				ref.modulo.compModalDatos.codigoDistrito.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			 
			 ref.modulo.compModalDatos.codigoDistrito.select2({
					ajax: {
					    url: ref.modulo.URL_BUSCAR_DISTRITOS,
					    data: function (params) {
					        var query = {
					        		codDep: ref.modulo.compModalDatos.codigoDepartamento.val(),
					        		codProv: ref.modulo.compModalDatos.codigoProvincia.val(),
					        		q: params.term
					        }
					        return query;
					    },
					    processResults: function (respuesta) {
					    	
							console.log(respuesta.datos);
					    	ref.listas.distrito.agregarLista(respuesta.datos);
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
			 
			 
				ref.modulo.compModalDatos.codigoPais.select2().change(function(event){
					var $comp = $(event.currentTarget);
		        	ref.modSistcorr.select2_change($comp.attr('id'));
					ref.modulo.compModalDatos.codigoCiudad.val('0000');
					ref.modulo.compModalDatos.codigoCiudad.change();
				});	
				
			
				 ref.modulo.compModalDatos.codigoCiudad.select2({
						ajax: {
						    url: ref.modulo.URL_BUSCAR_CIUDADES,
						    data: function (params) {
						        var query = {
						        		codPais: ref.modulo.compModalDatos.codigoPais.val(),
						        		q: params.term
						        }
						        return query;
						    },
						    processResults: function (respuesta) {
						    	
								console.log(respuesta.datos);
						    	ref.listas.ciudad.agregarLista(respuesta.datos);
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
			*/	
			ref.modulo.lblFiltroNombre.click(function(){
				ref.modulo.filtroBusqueda.filtroNombre.focus();
			});
		
			ref.modulo.lblFiltroRuc.click(function(){
				ref.modulo.filtroBusqueda.filtroRuc.focus();
			});
			
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarDependenciaExterna();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				//event.preventDefault();
				ref.modulo.filtroBusqueda.filtroNombre.val("");
				ref.modulo.filtroBusqueda.filtroRuc.val("");

				ref.dataTableConsulta = null;
				$('#tablaDependenciaExterna').DataTable().clear().destroy();
		
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;
				ref.modulo.compModalDatos.ruc.val("");
				ref.modulo.compModalDatos.ruc.change();
				ref.modulo.compModalDatos.nombreDependencia.val("");
				ref.modulo.compModalDatos.nombreDependencia.change();
				ref.modulo.compModalDatos.direccion.val("");
				ref.modulo.compModalDatos.direccion.change();
				ref.modulo.compModalDatos.email.val("");
				ref.modulo.compModalDatos.email.change();
				
				ref.modulo.compModalDatos.codigoDepartamento.val("0");
				ref.modulo.compModalDatos.codigoDepartamento.change();
				ref.modulo.compModalDatos.codigoProvincia.val("0");
				ref.modulo.compModalDatos.codigoProvincia.change();
				ref.modulo.compModalDatos.codigoDistrito.val("0");
				ref.modulo.compModalDatos.codigoDistrito.change();

				ref.modulo.compModalDatos.codigoPais.val("0");
				ref.modulo.compModalDatos.codigoPais.change();
				ref.modulo.compModalDatos.codigoCiudad.val("0");
				ref.modulo.compModalDatos.codigoCiudad.change();

	
				
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
				var idDependencia = comp.attr("data-dependenciaid");
				
				for(var i in datosConsulta){
					var datoDependenca = datosConsulta[i];
					if(datoDependenca.id == idDependencia){
						ref.modulo.compModalDatos.id = datoDependenca.id;
						
						ref.modulo.compModalDatos.ruc.val(datoDependenca.ruc);
						ref.modulo.compModalDatos.ruc.change();
						ref.modulo.compModalDatos.nombreDependencia.val(datoDependenca.nombreDependencia);
						ref.modulo.compModalDatos.nombreDependencia.change();
						ref.modulo.compModalDatos.direccion.val(datoDependenca.direccion);
						ref.modulo.compModalDatos.direccion.change();
						ref.modulo.compModalDatos.email.val(datoDependenca.email);
						ref.modulo.compModalDatos.email.change();
						
						ref.modulo.compModalDatos.codigoDepartamento.val(datoDependenca.codigoDepartamento);
						ref.modulo.compModalDatos.codigoDepartamento.change();
						ref.modulo.compModalDatos.codigoProvincia.append("<option value='"+ datoDependenca.codigoProvincia +"' selected='selected'>" + datoDependenca.nombreProvincia + "</option>");	
						ref.modulo.compModalDatos.codigoProvincia.change();
						ref.modulo.compModalDatos.codigoDistrito.append("<option value='"+ datoDependenca.codigoDistrito +"' selected='selected'>" + datoDependenca.nombreDistrito + "</option>");	
						ref.modulo.compModalDatos.codigoDistrito.change();
						
						ref.modulo.compModalDatos.codigoPais.val(datoDependenca.codigoPais);
						ref.modulo.compModalDatos.codigoPais.change();
						ref.modulo.compModalDatos.codigoCiudad.append("<option value='"+ datoDependenca.codigoCiudad +"' selected='selected'>" + datoDependenca.nombreCiudad + "</option>");	
						ref.modulo.compModalDatos.codigoCiudad.change();
						
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
				var idDependencia = comp.attr("data-dependenciaid");
				ref.modulo.compModalDatos.id = idDependencia;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarDependenciaExterna();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateLugarTrabajo();
			});
			
			/*ref.modulo.compModalDatos.codigoDistrito.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});*/
		},
		

		
		buscarDependenciaExterna: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.filtroNombre.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.filtroRuc.val() != ""){
				valido = true;
			}

			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'nombreDependencia' : ref.modulo.filtroBusqueda.filtroNombre.val(),
						'ruc' : ref.modulo.filtroBusqueda.filtroRuc.val()
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_DEPENDENCIA_EXTERNA", JSON.stringify(filtrosBusqueda));
				setTimeout(function(){
					ref.inicializarTablaConsulta();
				}, 1000)
				
			//}	
		},
		
		
		eliminarDependenciaExterna: function(){
			var ref = this;
			ref.parametros = {
					'id' 				 : ref.modulo.compModalDatos.id,
					'ruc'       		 : '',
					'nombreDependencia'  : '',
					'direccion'     	 : '',
					'email'			     : '',
					'codigoDepartamento' : 0,
					'codigoProvincia'    : 0,
					'codigoDistrito'     : 0,
					'codigoPais'		 : 0,
					'codigoCiudad'		 : 0,
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
			if(ref.modulo.compModalDatos.ruc.val().trim() == "0"){
				ref.modSistcorr.notificar("ERROR", "El campo Ruc es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.ruc.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Ruc es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.nombreDependencia.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Nombre es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			ref.parametros = {
					'id' 					: ref.modulo.compModalDatos.id,
					'ruc'   				: ref.modulo.compModalDatos.ruc.val(),
					'nombreDependencia'	    : ref.modulo.compModalDatos.nombreDependencia.val(),
					'direccion'     		: ref.modulo.compModalDatos.direccion.val(),
					'email'     			: ref.modulo.compModalDatos.email.val(),
					'codigoDepartamento' 	: ref.modulo.compModalDatos.codigoDepartamento.val(),
					'codigoProvincia'    	: ref.modulo.compModalDatos.codigoProvincia.val(),					
					'codigoDistrito'   	 	: ref.modulo.compModalDatos.codigoDistrito.val(),
					'codigoPais'   	 		: ref.modulo.compModalDatos.codigoPais.val(),
					'codigoCiudad'        	: ref.modulo.compModalDatos.codigoCiudad.val(),
					'codigoAccion'     	    : ref.modulo.compModalDatos.accion
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
					'nombreDependencia' : ref.modulo.filtroBusqueda.filtroNombre.val(),
					'ruc' : ref.modulo.filtroBusqueda.filtroRuc.val()
		
			}
			ref.modulo.exportarExcel(filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_dependencia_externa.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_dependencia_externa.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_DEPENDENCIA_EXTERNA,
			        	"type": "GET",
			        	"data": {
							'nombreDependencia' : ref.modulo.filtroBusqueda.filtroNombre.val(),
							'ruc' : ref.modulo.filtroBusqueda.filtroRuc.val(),
						
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
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-dependenciaid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-dependenciaid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'ruc', title: 'RUC', defaultContent: ''},
			        	{data: 'nombreDependencia', title: 'Nombre', defaultContent: ''},
			        	{data: 'direccion', title: 'Dirección', defaultContent: ''},
			        	{data: 'nombreDepartamento', title: 'Departamento', defaultContent: ''},
			        	{data: 'nombreProvincia', title: 'Provincia', defaultContent: ''},
			        	{data: 'nombreDistrito', title: 'Distrito', defaultContent: ''}	,
			        	
			        	{data: 'nombrePais', title: 'País', defaultContent: ''}	,
			        	{data: 'nombreCiudad', title: 'Ciudad', defaultContent: ''}	,
			        	{data: 'email', title: 'Email', defaultContent: ''}	
			        	
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
					var tableOrder = $("#tablaDependenciaExterna").dataTable();
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
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.modulo = modulo_mantenimiento_dependencia_externa;
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.listas = {};
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.listas.departamento =  new LISTA_DATA([]);
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.listas.provincia =  new LISTA_DATA([]);
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.listas.distrito =  new LISTA_DATA([]);
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.listas.ciudad =  new LISTA_DATA([]);
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.rol_jefe = ES_JEFE;
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.rol_gestor = ES_GESTOR;
	MANTENIMIENTO_DEPENDENCIA_EXTERNA_VISTA.inicializar();
}, 500);