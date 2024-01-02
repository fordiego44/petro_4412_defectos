var modulo_consulta_correspondencia_jefe_gestor = MODULO_CORRESPONDENCIA_CONSULTA_JEFE_GESTOR.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {},
		filtroJG: {},
		storageVerDetalle: "VER_DETALLE_JG",//TICKET 9000004710
		masfiltroJGs: false,
		dataTable: null,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		dependenciasJG: [],
		jefe: false,
		inicializar: function(){
			var ref = this;
	
			
			ref.modSistcorr.cargarVista();
			ref.actualizarListaDependencias();
			ref.iniciarEventos();
			//ref.inicializarTabla([]);
			ref.inicializarTablaConsultaDefecto([]);
	
			setTimeout(function(){
				ref.modSistcorr.procesar(false);
			}, 2000)
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				/*if(ref.filtroJG.considerarOriginadora == false && ref.filtroJG.codDependenciaRemitente == null){
					ref.modSistcorr.notificar("ERROR", 'Debe de seleccionar una dependencia remitente', "Error");
					ref.inicializarTabla([]);
					return;
				}*/
				ref.exportarExcel();
			});
			
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				if(ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val() == null || ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val() == undefined){
					ref.modSistcorr.notificar("ERROR", 'Por favor seleccione una Dependencia Remitente', "Error");
					ref.inicializarTabla([]);
					return;
				}
				ref.buscarCorrespondencias();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltroJGs();
			});			
			
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			//inicio Comentado por el ticket 9000004710
			setTimeout(function() {		
				ref.obtenerfiltroJGs();
				var isVerDetalle = sessionStorage.getItem(ref.storageVerDetalle);
				if(Object.keys(ref.filtroJG).length == 0 || (isVerDetalle && isVerDetalle == "N")){
					//ref.actualizarValoresPorDefecto();
					ref.obtenerfiltroJGs();
					ref.inicializarTabla([]);
				} else {
					console.log("actualizar y buscar correspondencias")
					ref.update_form_filtroJGs();
					ref.searchCorrespondencias();
				}
				sessionStorage.setItem(ref.storageVerDetalle, "N");
			}, 1500);
			//fin Comentado por el ticket 9000004710
			
		},
		
		resetfiltroJGsSecundarios: function(){
			var ref = this;
			ref.moduloJG.componentesJG.cmbNombreOriginador.val(null);
			ref.moduloJG.componentesJG.cmbNombreOriginador.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(null);
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(null);
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			
			ref.moduloJG.componentesJG.cbmTipoEmision.val(0);
			ref.moduloJG.componentesJG.cbmTipoEmision.change();
			
			ref.moduloJG.componentesJG.cbmTipoCorrespondencia.val(0);
			ref.moduloJG.componentesJG.cbmTipoCorrespondencia.change();
			ref.moduloJG.componentesJG.cbmDependenciaDestinatariaInterno.val(null);
			ref.moduloJG.componentesJG.cbmDependenciaDestinatariaInterno.change();
			//ref.moduloJG.componentesJG.cbmDependenciaDestinatariaExternaNacional.val(null);
			//ref.moduloJG.componentesJG.cbmDependenciaDestinatariaExternaNacional.change();
			ref.moduloJG.componentesJG.txtDependenciasDestinatariaExternaNacional.val(null);
			ref.moduloJG.componentesJG.txtDependenciasDestinatariaExternaNacional.change();
			
			ref.moduloJG.componentesJG.txtDependenciasDestinatariaExternaInternacional.val(null);
			ref.moduloJG.componentesJG.txtDependenciasDestinatariaExternaInternacional.change();
			ref.moduloJG.componentesJG.cbmDependenciaCopia.val(null);
			ref.moduloJG.componentesJG.cbmDependenciaCopia.change();
			
			// TICKET 9000004808
			ref.moduloJG.componentesJG.txtFechaModificaDesde.val(null);
			ref.moduloJG.componentesJG.txtFechaModificaDesde.change();
			ref.moduloJG.componentesJG.txtFechaModificaHasta.val(null);
			ref.moduloJG.componentesJG.txtFechaModificaHasta.change();
			//
			
			$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
			$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
			
		},
		
		resetarfiltroJGs: function(){
			var ref = this;
			ref.filtroJG = {};
			ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val("0000");
			ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
			ref.moduloJG.componentesJG.txtCorrelativo.val('');
			ref.moduloJG.componentesJG.txtCorrelativo.change();
			ref.moduloJG.componentesJG.txtAsunto.val('');
			ref.moduloJG.componentesJG.txtAsunto.change();
			ref.moduloJG.componentesJG.cbmEstado.val(0);
			ref.moduloJG.componentesJG.cbmEstado.change();
			ref.resetfiltroJGsSecundarios();			
			ref.actualizarValoresPorDefecto();
			
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			
			//ref.moduloJG.componentesJG.cboxConsiderarDepenOriginadora.prop('checked', false );
			//ref.moduloJG.componentesJG.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
			if(ref.dependenciasUsuario.length > 0){
				var _dep = ref.dependenciasUsuario[0];
				//ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);				
				
				ref.seleccionarDependenciaDefault();
				
				ref.buscarCorrespondencias();
			} else {
				ref.inicializarTabla([]);
			}
		},
		
		seleccionarDependenciaDefault: function(){
			var ref = this;
			console.log("limpiando combo remitente");
			//ref.listas.dependencias.agregarLista([{"id" : '0000', "text": 'Seleccione'}]);
			//ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.append("<option value='0000' selected='selected'>Seleccione</option>");
//			ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val('0000');
//			ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
			ref.inicializarTabla([]);
		},
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = ref.modSistcorr.getFiltrosJGConsultaCorrespondencia();
			ref.filtroJG = _filtroJG;			
		},
		
		actualizarListaDependencias: function(){
				var ref =  this;
				ref.modSistcorr.procesar(true);
				ref.moduloJG.listarDependencias()
				.then(function(respuesta){
					if(respuesta.estado == true){																	
						ref.listas.dependencias.datos = [];
						ref.listas.dependencias.agregarLista(respuesta.datos);
						ref.inicializarcomponentesJG();
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
		
		inicializarcomponentesJG: function(){
			var ref = this;		
			//debugger;
			ref.componentesJG.cmbDependenciaRemitenteJG = ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.select2({
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
			
			//--ticket 9000004808
			ref.componentesJG.txtFechaModificaDesde = ref.moduloJG.componentesJG.txtFechaModificaDesde.datepicker({
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
			
			ref.componentesJG.txtFechaModificaHasta = ref.moduloJG.componentesJG.txtFechaModificaHasta.datepicker({
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
			
			ref.moduloJG.componentesJG.btnFechaModificaDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaModificaDesde.click();
				ref.moduloJG.componentesJG.txtFechaModificaDesde.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaModificaHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaModificaHasta.click();
				ref.moduloJG.componentesJG.txtFechaModificaHasta.focus();
			});
			// fin ticket 9000004808
			 
		},
		
		update_form_filtroJGs: function(){
			var ref = this;			
		
			if(ref.filtroJG.codDependenciaRemitente){
				var lista = [];
				var count = 0;				
				if(ref.listas.dependencias.datos.length > 0){
					lista = ref.listas.dependencias.datos;
				
					//for(var index in lista){
					for(var i = 0; i < lista.length; i++){
						if (lista[i].id == ref.filtroJG.codDependenciaRemitente){
							count = count +1;
						}
					}					
					console.log("count ->"+count);
					if (count == 0){
						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val("0000");
						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
						ref.inicializarTabla([]);
						ref.filtroJG.codDependenciaRemitente = null;
						
						//$("#cmbDependenciaRemitenteJG").val(ref.filtroJG.codDependenciaRemitente).trigger("change");
//						ref.listas.dependencias.agregarLista([{"id" : ref.filtroJG.codDependenciaRemitente, "text": ref.filtroJG.nombreDependenciaRemitente}]);
//						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.append("<option value='"+ ref.filtroJG.codDependenciaRemitente +"' selected='selected'>" + ref.filtroJG.nombreDependenciaRemitente + "</option>");
//						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
					}else{
//						setTimeout(()=>{
						ref.componentesJG.cmbDependenciaRemitenteJG = ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.select2({
							data: ref.listas.dependencias.datos
						});
							console.log("Se setea combo remitente");
							ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val(ref.filtroJG.codDependenciaRemitente);
							//ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG=ref.filtroJG.codDependenciaRemitente;
							ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
//						}, 2000);
						console.log("Se setea combo remitente "+ref.filtroJG.codDependenciaRemitente );
						console.log("Se setea combo remitente 2 "+ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG); 
//						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val(ref.filtroJG.codDependenciaRemitente);
//						ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
					}
				}else{
					ref.seleccionarDependenciaDefault();
				}				
				//$("#cmbDependenciaRemitenteJG").val(ref.filtroJG.codDependenciaRemitente).trigger("change");
				//ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.append("<option value='"+ ref.filtroJG.codDependenciaRemitente +"' selected='selected'>" + ref.filtroJG.nombreDependenciaRemitente + "</option>");
				//ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.change();
			} else {
				ref.seleccionarDependenciaDefault();
			}
			
			ref.moduloJG.componentesJG.txtCorrelativo.val(ref.filtroJG.correlativo);
			ref.moduloJG.componentesJG.txtCorrelativo.change();
			ref.moduloJG.componentesJG.txtAsunto.val(ref.filtroJG.asunto);
			ref.moduloJG.componentesJG.txtAsunto.change();
			if(ref.filtroJG.estado){
				ref.moduloJG.componentesJG.cbmEstado.val(ref.filtroJG.estado);
			}			
			
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(ref.filtroJG.fechaDesde);
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(ref.filtroJG.fechaHasta);
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			
			// Ticket 9000004808
			ref.moduloJG.componentesJG.txtFechaModificaDesde.val(ref.filtroJG.fechaModificaDesde);
			ref.moduloJG.componentesJG.txtFechaModificaDesde.change();
			ref.moduloJG.componentesJG.txtFechaModificaHasta.val(ref.filtroJG.fechaModificaHasta);
			ref.moduloJG.componentesJG.txtFechaModificaHasta.change();
			// Fin Ticket 

			
//			if(ref.filtroJG.tipoCorrespondencia){
//				ref.moduloJG.componentesJG.cbmTipoCorrespondencia.val(ref.filtroJG.tipoCorrespondencia);
//			}
		}, 
		
		
		buscarCorrespondencias: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.codDependenciaRemitente = ref.moduloJG.componentesJG.cmbDependenciaRemitenteJG.val();
			if(ref.filtroJG.codDependenciaRemitente){
				if(ref.filtroJG.codDependenciaRemitente == '0000'){
					ref.filtroJG.codDependenciaRemitente = null; 
				} else {
					ref.filtroJG.nombreDependenciaRemitente = ref.listas.dependencias.buscarPorId(ref.filtroJG.codDependenciaRemitente).text;
				}
			}
			ref.filtroJG.correlativo = ref.moduloJG.componentesJG.txtCorrelativo.val();
			ref.filtroJG.asunto = ref.moduloJG.componentesJG.txtAsunto.val();
			ref.filtroJG.estado = ref.moduloJG.componentesJG.cbmEstado.val() == 0 ? '' : ref.moduloJG.componentesJG.cbmEstado.val();
			ref.filtroJG.fechaDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val();
			ref.filtroJG.fechaHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val();
			
			ref.filtroJG.fechaModificaDesde = ref.moduloJG.componentesJG.txtFechaModificaDesde.val();
			ref.filtroJG.fechaModificaHasta = ref.moduloJG.componentesJG.txtFechaModificaHasta.val();	
			ref.searchCorrespondencias();
			
			ref.modSistcorr.setfiltrosJGConsultaCorrespondencia(ref.filtroJG);
		},
		
		searchCorrespondencias: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			if(ref.filtroJG.codDependenciaRemitente != null) {
				ref.inicializarTablaConsulta();
			/*ref.moduloJG.consultar(ref.filtroJG)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.inicializarTabla(respuesta.datos || []);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.inicializarTabla([]);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});*/
			}else{
				//ref.inicializarTabla([]);
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.modSistcorr.procesar(false);
			}
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.exportarExcel(ref.filtroJG)
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
			        	"url": ref.moduloJG.URL_CONSULTA_JEFE_GESTOR_PAGINADO,
			        	"type": "GET",
			        	"data": ref.filtroJG,
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
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
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
			        ],
			        "order": [[ 2, 'desc' ]],
			        "columnDefs": [ 
			        	  {className : 'dtr-control td-ajuste',  targets: [0]},
				          {orderable : false,  targets: [0, 1]}
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
			    	"createdRow":function(row,data,index){
			    		var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaJefeGestorSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					},

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
					//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					//ref.eventoDocumentoPrincipal();
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_JG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
						//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						//ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaConsultaCorrespondenciasJG").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					//var api = ref.dataTableConsulta.api();
					//var order = ref.dataTableConsulta.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_JG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
						//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						//ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
					//ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					//ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		inicializarTabla: function(data){
			var ref = this;
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.moduloJG.componentesJG.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
//				ref.moduloJG.componentesJG.dataTable.show();
//				ref.dataTable = ref.moduloJG.componentesJG.dataTable.DataTable({
//					"dom": DatatableAttachments.domsimple,
//					"language": DatatableAttachments.language,
//			        "responsive": true,
//			        "pageLength": 10,
//			        "data": data,
//			        "columns": [
//			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
//			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
//			        	}},
//			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
//			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
//			        		var max = 0;
//			        		if(screen.width > 400) {
//			        			max = 150;
//			        		} else {
//			        			max = 15;
//			        		}
//			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
//			        		return text;
//			        	}},
//			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
//			        		var dest = "";
//			        		if(full.cantidad<=1){
//			        			dest = full.destinatario_dependencia;
//			        		}else{
//			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
//			        		}
//			        		return dest;
//			        	}},
//			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
//			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
//			        	{data: 'estado', title: 'Estado', defaultContent: ''},
//			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
//			        		var anio = full.fechaDocumento.substring(6, 10);
//			        		var mes = full.fechaDocumento.substring(3, 5);
//			        		var dia = full.fechaDocumento.substring(0, 2);
//			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
//			        	}},
//			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
//			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
//			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
//			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
//			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
//			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
//			        		if(full.firmaDigital == true){
//			        			return "DIGITAL";
//			        		}
//			        		return "MANUAL";
//			        	}},
//			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
//			        		if(full.despachoFisico == true){
//			        			return "SI";
//			        		}
//			        		return "NO";
//			        	}},
//			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
//			        		if(full.urgente == true){
//			        			return "SI";
//			        		}
//			        		return "NO";
//			        	}},
//			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
//			        		if(full.confidencial == true){
//			        			return "SI";
//			        		}
//			        		return "NO";
//			        	}},
//			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
//			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
//			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
//			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
//			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
//			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
//			        		var hora = full.fechaUltActualizacion.substring(11, 13);
//			        		var anio = full.fechaUltActualizacion.substring(6, 10);
//			        		var mes = full.fechaUltActualizacion.substring(3, 5);
//			        		var dia = full.fechaUltActualizacion.substring(0, 2);
//			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
//			        	}}
//			        ],
//			        "initComplete": function(settings, json){
//			        	setTimeout(function() {
//			        		ref.dataTable.responsive.rebuild();
//			        		ref.dataTable.responsive.recalc();
//			        		ref.modSistcorr.procesar(false);
//			        	}, 1000);
//			        },
//
//				});
//				
//				ref.dataTable.on('responsive-display', function ( e, dataTable, row, showHide, update ) {
//					ref.updateEventosTabla();
//				});
//				
//				ref.moduloJG.componentesJG.dataTable.on( 'page.dt', function () {
//					ref.dataTable.responsive.rebuild();
//	        		ref.dataTable.responsive.recalc();
//	        		setTimeout(function() {
//						ref.updateEventosTabla();
//					}, 1500);
//				});
//				
//				setTimeout(function() {
//					ref.updateEventosTabla();
//				}, 1500);
			} else {
				ref.moduloJG.componentesJG.dataTable.show();
				ref.dataTable = ref.moduloJG.componentesJG.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			         	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
			        ],
			        "order": [[ 2, 'desc' ]],
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
			    		var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaJefeGestorSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					},

				});
				
				ref.dataTable.on('responsive-display', function ( e, dataTable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				});
				
				ref.moduloJG.componentesJG.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				}, 1500);
			}
		},
		
		inicializarTablaConsultaDefecto: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.moduloJG.componentesJG.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsultaDefecto(data);
			} else {
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: '', title: '', defaultContent: ''},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			         	{data: 'id_correspondencia', title: 'Doc', defaultContent: '', render: function(data, type, full){
			        		return "<i class='fas fa-download icon_view icon_add_document btnDescargarDocumento'  data-toggle='tooltip' title='Clic para descargar los documentos' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dep. Remitente Aprobadora', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		var anio = full.fechaDocumento.substring(6, 10);
			        		var mes = full.fechaDocumento.substring(3, 5);
			        		var dia = full.fechaDocumento.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + '</span>' + full.fechaDocumento;
			        	}},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        	{data: 'responsable', title: 'Responsable', defaultContent: ''},
			        	{data: 'fechaUltActualizacion', title: 'Fecha y Hora Ult. Actualiz.', defaultContent: '', render: function(data, type, full){
			        		var minuto = full.fechaUltActualizacion.substring(14, 16);
			        		var hora = full.fechaUltActualizacion.substring(11, 13);
			        		var anio = full.fechaUltActualizacion.substring(6, 10);
			        		var mes = full.fechaUltActualizacion.substring(3, 5);
			        		var dia = full.fechaUltActualizacion.substring(0, 2);
			        		return '<span class="fecha_oculta">' + anio + "/" + mes + "/" + dia + " " + hora + ":" + minuto + '</span>' + full.fechaUltActualizacion;
			        	}}
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
			    		var corresSeleccionado = sessionStorage.getItem("_itemSelec_ConsultaJefeGestorSalida");
						if(data.id_correspondencia == corresSeleccionado){
							$('td', row).css({
								'background-color':'#f2f0d7'
							});
						}
					},

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, dataTable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
						ref.eventoDescargarDocumento();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.moduloJG.btnDescargarDocumento = $(".btnDescargarDocumento");
					ref.eventoDescargarDocumento();
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
					sessionStorage.setItem(ref.storageVerDetalle, "S");//TICKET 9000004710
					sessionStorage.setItem("_itemSelec_ConsultaJefeGestorSalida", this.dataset.id);//TICKET 9000004808
					ref.moduloJG.irADetalle(this.dataset.id);
				});
			}
		},
		//inicio ticket 9000004808
		eventoDescargarDocumento: function(){
			var ref = this;
			ref.moduloJG.btnDescargarDocumento.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-id");
				console.log("CLICK");
				ref.obtenerCorrespondencia(correlativo);
			});
		},
		
		obtenerCorrespondencia: function(correlativo){
			var ref = this;
			//ref.modSistcorr.procesar(true);
			ref.moduloJG.obtenerCorrespondencia(correlativo)
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
								ref.moduloJG.descargarArchivoFirmaDigital(correlativo);
							}else{
								ref.modSistcorr.notificar("OK", "La correspondencia no tiene ningún archivo adjunto que requiera firma digital.", "Success");
							}
						}else{
							ref.modSistcorr.notificar("OK", "La correspondencia no tiene ningún archivo adjunto que requiera firma digital.", "Success");
						}
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					//ref.modSistcorr.procesar(false);
				}).catch(function(error){
					//ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			
		},//Fin ticket 9000004808
		
};

setTimeout(function() {
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.moduloJG = modulo_consulta_correspondencia_jefe_gestor;
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.jefe = ES_JEFE;
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.listas = {};
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.listas.dependencias = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.listas.originadores = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.listas.dependencias_ext = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA_JEFE_GESTOR.inicializar();
}, 200);