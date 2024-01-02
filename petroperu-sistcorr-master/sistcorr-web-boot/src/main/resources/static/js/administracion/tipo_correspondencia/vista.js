var modulo_mantenimiento_lugar_trabajo = MODULO_MANTENIMIENTO_LUGAR_TRABAJO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var MANTENIMIENTO_LUGAR_TRABAJO_VISTA = {
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
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_TIPO_CORRESPONDENCIA'));
			if(filtros!=null){
		
				ref.modulo.filtroBusqueda.filtroNombre.val(filtros['nombreTipoCorr']);
				ref.modulo.filtroBusqueda.filtroNombre.change();
				
				let valido = false;

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
					
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarTipoAccion();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
			
				ref.modulo.filtroBusqueda.filtroNombre.val("");

				ref.dataTableConsulta = null;
				$('#tablaTipoAccion').DataTable().clear().destroy();
		
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.btnRegistrar.click(function(event){
				ref.modulo.compModalDatos.accion = "I";
				ref.modulo.compModalDatos.id = 0;

				ref.modulo.compModalDatos.codigoTipoCorr.val("");
				ref.modulo.compModalDatos.codigoTipoCorr.change();
				ref.modulo.compModalDatos.nombreTipoCorr.val("");
				ref.modulo.compModalDatos.nombreTipoCorr.change();
				
				ref.modulo.compModalDatos.mAplicaEnvInterna.val("");
				ref.modulo.compModalDatos.mAplicaEnvInterna.change();
				ref.modulo.compModalDatos.mAplicaEnvExterna.val("");
				ref.modulo.compModalDatos.mAplicaEnvExterna.change();
				ref.modulo.compModalDatos.mAplicaRecInterna.val("");
				ref.modulo.compModalDatos.mAplicaRecInterna.change();
				ref.modulo.compModalDatos.mAplicaRecExterna.val("");
				ref.modulo.compModalDatos.mAplicaRecExterna.change();
				
				ref.modulo.compModalDatos.mRequiereFecha.val("");
				ref.modulo.compModalDatos.mRequiereFecha.change();
				ref.modulo.compModalDatos.mFinalizaAceptar.val("");
				ref.modulo.compModalDatos.mFinalizaAceptar.change();
				ref.modulo.compModalDatos.mManualCorresp.val("");
				ref.modulo.compModalDatos.mManualCorresp.change();
				
				ref.modulo.compModalDatos.secuencia.val("");
				ref.modulo.compModalDatos.secuencia.change();
				ref.modulo.compModalDatos.mMultiple.val("");
				ref.modulo.compModalDatos.mMultiple.change();
				
				ref.modulo.compModalDatos.reqCopia.val("");
				ref.modulo.compModalDatos.reqCopia.change();
				ref.modulo.compModalDatos.reqDest.val("");
				ref.modulo.compModalDatos.reqDest.change();
	
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
				var idTipoCorrespondencia = comp.attr("data-tipocorespondenciaid");
				
				for(var i in datosConsulta){
					var datoTipoCorr = datosConsulta[i];
					if(datoTipoCorr.id == idTipoCorrespondencia){
						ref.modulo.compModalDatos.id = datoTipoCorr.id;
						ref.modulo.compModalDatos.codigoTipoCorr.val(datoTipoCorr.codigoTipoCorr);
						ref.modulo.compModalDatos.codigoTipoCorr.change();
						ref.modulo.compModalDatos.nombreTipoCorr.val(datoTipoCorr.nombreTipoCorr);
						ref.modulo.compModalDatos.nombreTipoCorr.change();
						ref.modulo.compModalDatos.mAplicaEnvInterna.val(datoTipoCorr.mAplicaEnvInterna);
						ref.modulo.compModalDatos.mAplicaEnvInterna.change();
						ref.modulo.compModalDatos.mAplicaEnvExterna.val(datoTipoCorr.mAplicaEnvExterna);
						ref.modulo.compModalDatos.mAplicaEnvExterna.change();
						ref.modulo.compModalDatos.mAplicaRecInterna.val(datoTipoCorr.mAplicaRecInterna);
						ref.modulo.compModalDatos.mAplicaRecInterna.change();
						ref.modulo.compModalDatos.mAplicaRecExterna.val(datoTipoCorr.mAplicaRecExterna);
						ref.modulo.compModalDatos.mAplicaRecExterna.change();
						
						ref.modulo.compModalDatos.mRequiereFecha.val(datoTipoCorr.mRequiereFecha);
						ref.modulo.compModalDatos.mRequiereFecha.change();
						ref.modulo.compModalDatos.mFinalizaAceptar.val(datoTipoCorr.mFinalizaAceptar);
						ref.modulo.compModalDatos.mFinalizaAceptar.change();						
						ref.modulo.compModalDatos.mManualCorresp.val(datoTipoCorr.mManualCorresp);
						ref.modulo.compModalDatos.mManualCorresp.change();
						ref.modulo.compModalDatos.secuencia.val(datoTipoCorr.secuencia);
						ref.modulo.compModalDatos.secuencia.change();
						
						ref.modulo.compModalDatos.mMultiple.val(datoTipoCorr.mMultiple);
						ref.modulo.compModalDatos.mMultiple.change();
						ref.modulo.compModalDatos.reqCopia.val(datoTipoCorr.reqCopia);
						ref.modulo.compModalDatos.reqCopia.change();
						ref.modulo.compModalDatos.reqDest.val(datoTipoCorr.reqDest);
						ref.modulo.compModalDatos.reqDest.change();
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
				var idTipoCorresp = comp.attr("data-tipocorespondenciaid");
				ref.modulo.compModalDatos.id = idTipoCorresp;
				setTimeout(function(){
					ref.modulo.compModalEliminar.modal('show');
				}, 300);
			}),
			
			ref.modulo.btnEliminarRegistro.off('click').on('click', function(){
				ref.eliminarTipoAccion();
			}),
			
			ref.modulo.btnGrabarRegistro.off('click').on('click', function(){
				ref.insertUpdateTipoAccion();
			});
			
			ref.modulo.compModalDatos.codigoTipoCorr.on('input', function () {
			    this.value = this.value.replace(/[^0-9]/g,'');
			});
		},
		

		
		buscarTipoAccion: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;

			if(ref.modulo.filtroBusqueda.filtroNombre.val() != ""){
				valido = true;
			}

			/*if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{*/
				var filtrosBusqueda = {
						'nombreTipoCorr' : ref.modulo.filtroBusqueda.filtroNombre.val()
			
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_ADM_TIPO_CORRESPONDENCIA", JSON.stringify(filtrosBusqueda));
				setTimeout(function(){
					ref.inicializarTablaConsulta();
				}, 1000)
				
			//}	
		},
		
		
		eliminarTipoAccion: function(){
			var ref = this;
			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'nombreTipoCorr' 		: 0,
					'mAplicaEnvInterna'    	: '',
					'mAplicaEnvExterna'     : '',
					'mAplicaRecInterna'     : '',
					'mAplicaRecExterna'     : '',
					'mRequiereFecha'     	: '',
					'mFinalizaAceptar'     	: '',
					'mManualCorresp'     	: '',
					'secuencia'     		: '',
					'mMultiple'     		: '',
					'reqCopia'     			: '',
					'reqDest'     			: '',
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
		
		insertUpdateTipoAccion: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.modulo.compModalDatos.codigoTipoCorr.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Código es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(ref.modulo.compModalDatos.nombreTipoCorr.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Descripción es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			

			ref.parametros = {
					'id' : 	ref.modulo.compModalDatos.id,
					'codigoTipoCorr'	 : ref.modulo.compModalDatos.codigoTipoCorr.val(),
					'nombreTipoCorr'   : ref.modulo.compModalDatos.nombreTipoCorr.val(),					
					'mAplicaEnvInterna'   	 : ref.modulo.compModalDatos.mAplicaEnvInterna.val(),
					'mAplicaEnvExterna'   	 : ref.modulo.compModalDatos.mAplicaEnvExterna.val(),
					'mAplicaRecInterna'   : ref.modulo.compModalDatos.mAplicaRecInterna.val(),
					'mAplicaRecExterna'  : ref.modulo.compModalDatos.mAplicaRecExterna.val(),
					
					'mRequiereFecha'   : ref.modulo.compModalDatos.mRequiereFecha.val(),					
					'mFinalizaAceptar'   	 : ref.modulo.compModalDatos.mFinalizaAceptar.val(),
					'mManualCorresp'   	 : ref.modulo.compModalDatos.mManualCorresp.val(),
					'secuencia'   : ref.modulo.compModalDatos.secuencia.val(),
					'mMultiple'  : ref.modulo.compModalDatos.mMultiple.val(),	
					
					'reqCopia'   : ref.modulo.compModalDatos.reqCopia.val(),					
					'reqDest'   	 : ref.modulo.compModalDatos.reqDest.val(),
					
					'codigoAccion'         : ref.modulo.compModalDatos.accion
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
					'nombreTipoCorr' : ref.modulo.filtroBusqueda.filtroNombre.val()
			}
			ref.modulo.exportarExcel(filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_tipo_correspondencia.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_tipo_correspondencia.xlsx';
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
			        	"url": ref.modulo.URL_BUSCAR_LUGAR_TRABAJO,
			        	"type": "GET",
			        	"data": {
							'nombreTipoCorr' : ref.modulo.filtroBusqueda.filtroNombre.val(),
						
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
			        		return "<i class='fas fa-edit icon_editar btnGrillaEditar' data-tipocorespondenciaid='"+full.id+"' data-toggle='tooltip' title='Editar'></i>"

			        	}},
			        	{data: 'id', title: '', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-trash-alt icon_eliminar btnGrillaEliminar' data-tipocorespondenciaid='"+full.id+"' data-toggle='tooltip' title='Eliminar'></i>"
			        	}},
			        	{data: 'id', title: 'Id', defaultContent: ''},
			        	{data: 'codigoTipoCorr', title: 'Código', defaultContent: ''},
			        	{data: 'nombreTipoCorr', title: 'Tipo de Correspondencia', defaultContent: ''},
			        	{data: 'mAplicaEnvInterna', title: 'Aplica Env Interna', defaultContent: ''},
			        	{data: 'mAplicaEnvExterna', title: 'Aplica Env Externa', defaultContent: ''},
			        	{data: 'mAplicaRecInterna', title: 'Aplica Rec Interna', defaultContent: ''},
			        	{data: 'mAplicaRecExterna', title: 'Aplica Rec Externa', defaultContent: ''},
			        	
			        	{data: 'mRequiereFecha', title: 'Requiere Fecha', defaultContent: ''},
			        	{data: 'mFinalizaAceptar', title: 'Marca Finaliza Gestor', defaultContent: ''},
			        	{data: 'mManualCorresp', title: 'Manual Corresp', defaultContent: ''},
			        	{data: 'secuencia', title: 'Secuencia', defaultContent: ''},
			        	{data: 'mMultiple', title: 'Multiple', defaultContent: ''},
			        	{data: 'reqCopia', title: 'Requiere Copia', defaultContent: ''},
			        	{data: 'reqDest', title: 'Requiere Dest', defaultContent: ''}
			        	
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
					var tableOrder = $("#tablaTipoAccion").dataTable();
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
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.modSistcorr = modulo_sistcorr;
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.modulo = modulo_mantenimiento_lugar_trabajo;
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.listas = {};
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.listas.departamento =  new LISTA_DATA([]);
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.listas.provincia =  new LISTA_DATA([]);
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.listas.distrito =  new LISTA_DATA([]);
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.rol_jefe = ES_JEFE;
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.rol_gestor = ES_GESTOR;
	MANTENIMIENTO_LUGAR_TRABAJO_VISTA.inicializar();
}, 500);