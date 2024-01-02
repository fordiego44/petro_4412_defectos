var modulo_consultalog= MODULO_CONSULTALOG.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTALOG_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {combosSimples:{}, combosS2: {}, datePikers:{}},
		filtroJG: {},
		masfiltroJGs: false,
		dataTable: null,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		datosConsulta: [],
		dependenciasJG: [],
		jefe: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.inicializarcomponentesJG();
			setTimeout(function(){
				ref.setFormCombosFiltros();
			}, 2000);
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				ref.buscarLog();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltro();
			});			
			
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			ref.moduloJG.compBtnRegistrarAdicion.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				ref.abrirModalRegAdicion();
			});
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReempAdicion = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comModificarAdicion.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReempAdicion = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comElimAdicion.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnEliminarReemplazoAdicion.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
			});
			
			/*ref.moduloJG.btnModificarReemplazoAdicion.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
			});*/
			
			setTimeout(function() {				
				ref.obtenerfiltroJGs();
				if(Object.keys(ref.filtroJG).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					console.log("actualizar y buscar correspondencias")
					ref.searchLog();
				}
			}, 1500);
			
		},
		
		setFormCombosFiltros: function(){
			var ref = this;
			
			if(ref.filtroJG != undefined && ref.filtroJG.tabla != undefined){
				ref.moduloJG.componentesJG.cmbTablaFiltro.val(ref.filtroJG.tabla);
				ref.moduloJG.componentesJG.cmbTablaFiltro.change();
				
				ref.moduloJG.componentesJG.cmbAccionFiltro.val(ref.filtroJG.accion);
				ref.moduloJG.componentesJG.cmbAccionFiltro.change();

				ref.moduloJG.componentesJG.txtUsuarioFiltro.val(ref.filtroJG.usuario);
			
				label = $("label[for='" + ref.moduloJG.componentesJG.txtUsuarioFiltro.attr('id') + "']");
				label.addClass('active');

				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(ref.filtroJG.fechaDesde);
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(ref.filtroJG.fechaHasta);
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			}
		},
		
		eliminarReemplazoAdicion: function(){
			var ref=this;
			var comp = $(this);
			ref.parametros={
					'idReemplazos' : ref.moduloJG.objetoReemplazoAdicion.idReemplazos
			};			
			ref.modSistcorr.procesar(true);			
			ref.moduloJG.eliminarReemplazoAdicion(ref.parametros)
				.then(function(respuesta){
					console.log("Respuesta.estado:" + respuesta.estado)
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modSistcorr.procesar(false);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
					ref.moduloJG.comElimAdicion.modal('hide');
					ref.inicializarTablaConsulta();
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comElimAdicion.modal('hide');
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		resetarfiltro: function(){
			var ref = this;
			ref.filtroJG = {};
			ref.moduloJG.componentesJG.cmbTablaFiltro.val("");
			ref.moduloJG.componentesJG.cmbTablaFiltro.change();
			ref.moduloJG.componentesJG.cmbAccionFiltro.val("");
			ref.moduloJG.componentesJG.cmbAccionFiltro.change();
			ref.moduloJG.componentesJG.txtUsuarioFiltro.val("");
			ref.moduloJG.componentesJG.txtUsuarioFiltro.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			ref.actualizarValoresPorDefecto();
			
		},
		
		abrirModalRegAdicion : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comRegAdicion.myModal.modal('show');
			setTimeout(function() {
				ref.iniciarCombosAutoCompletados();
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		abrirModalElimAdicion : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comElimAdicion.myModal.modal('show');
			setTimeout(function() {
				console.log('abre modal eliminar');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			ref.inicializarTablaConsulta();
		},
		
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = JSON.parse(sessionStorage.getItem("FILTRO_ADM_LOG")) || {};
			ref.filtroJG = _filtroJG;			
		},
		
		
		inicializarcomponentesJG: function(){
			var ref = this;		
			//debugger;
			
			ref.componentesJG.txtFechaDocumentoDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentesJG.txtFechaDocumentoHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.focus();
			});	
			 
		},
		
		
		buscarLog: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.tabla=ref.moduloJG.componentesJG.cmbTablaFiltro.val();
			ref.filtroJG.tablaTexto=  $("#cmbTablaFiltro option:selected").text();
			ref.filtroJG.usuario=ref.moduloJG.componentesJG.txtUsuarioFiltro.val();
			ref.filtroJG.accion=ref.moduloJG.componentesJG.cmbAccionFiltro.val();
			ref.filtroJG.accionTexto=  $("#cmbAccionFiltro option:selected").text();
			ref.filtroJG.fechaDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val();
			ref.filtroJG.fechaHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val();			
			ref.searchLog();
			
			sessionStorage.setItem("FILTRO_ADM_LOG", JSON.stringify(ref.filtroJG));
		},
		
		searchLog: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.inicializarTablaConsulta();
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.exportarExcel(ref.filtroJG)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_log.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_log.xlsx';
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
				ref.moduloJG.componentesJG.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.moduloJG.URL_CONSULTA_LOG,
			        	"type": "GET",
			        	"data": ref.filtroJG,
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
			        	{data: 'nombreTabla', title: 'Tabla Afectada', defaultContent: ''},
			        	{data: 'tipoTransaccion', title: 'Accion Realizada', defaultContent: ''},
			        	{data: 'usuario', title: 'Usuario que Ejecuta la Acción', defaultContent: ''},
			        	{data: 'fecha', title: 'Fecha', defaultContent: ''},
			        	{data: 'idArtefacto', title: 'Id Artefacto', defaultContent: ''},
			        	{data: 'mensaje', title: 'Detalle', defaultContent: ''}
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
			        		if(origPag == "verDetalleBS"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagJG");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_JG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();						
					}, 1500);
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaConsultaCorrespondenciasJG").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_JG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();						
					}, 1500);
				} );
				
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
					ref.moduloJG.irADetalle(this.dataset.id);
				});
			}
		}
};

setTimeout(function() {
	CONSULTALOG_VISTA.moduloJG = modulo_consultalog;
	CONSULTALOG_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTALOG_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CONSULTALOG_VISTA.jefe = ES_JEFE;
	CONSULTALOG_VISTA.listas = {};
	CONSULTALOG_VISTA.listas.dependencias = new LISTA_DATA([]);
	CONSULTALOG_VISTA.listas.originadores = new LISTA_DATA([]);
	CONSULTALOG_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	CONSULTALOG_VISTA.inicializar();
}, 200);