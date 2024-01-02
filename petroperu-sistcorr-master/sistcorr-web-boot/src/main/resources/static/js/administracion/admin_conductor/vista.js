
var modulo_admin_conductor= MODULO_ADMIN_CONDUCTOR.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);
var dataConstructor=null;

var ADMIN_CONDUCTOR_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {},
		filtroJG: {},
		masfiltroJGs: false,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		dependenciasJG: [],
		conductoresSelect: [],
		totalRegistros: 0,
		jefe: false,
		filtrosBusqueda: null,
		filtrosBusquedaSRest: null,
		sizeScreen: 550,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.inicializarcomponentesJG();
			ref.obtenerFiltros();
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				
				ref.exportarExcel();
			});
			
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(event){
				event.preventDefault();
				ref.buscarConductor();
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
			
		},
		
		resetarfiltroJGs: function(){
			var ref = this;
			ref.filtrosBusqueda = {};
			ref.filtrosBusquedaSRest = [];
			ref.moduloJG.componentesJG.cbmTareaExcepcion.val("0000");
			ref.moduloJG.componentesJG.cbmTareaExcepcion.change();
			ref.moduloJG.componentesJG.txtProceso.val('');
			ref.moduloJG.componentesJG.txtProceso.change();
			ref.moduloJG.componentesJG.txtRefPrincipal.val('');
			ref.moduloJG.componentesJG.txtRefPrincipal.change();
			ref.moduloJG.componentesJG.txtRefAlternativa.val('');
			ref.moduloJG.componentesJG.txtRefAlternativa.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val('');
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val('');
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change()
			ref.actualizarValoresPorDefecto();
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			ref.moduloJG.btnReintentarMasivo.prop("disabled", true);
			sessionStorage.removeItem('FILTROS_ADM_CONDUCTOR');
			$("#bodyTableConductores").html("");
		},
		
		seleccionarDependenciaDefault: function(){
			var ref = this;
			console.log("limpiando combo remitente");
			ref.inicializarTablaConsultaFromResponse([]);
		},
		
		inicializarcomponentesJG: function(){
			var ref = this;		
			
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
			
			ref.moduloJG.btnReintentarMasivo.click(function(){
				ref.reintentarConductorMasivo();
			});
			
			$(document).on('change','.inputSeleccionarWFId', function () {
				var ident = $(this).val();
				if(this.checked){
					ref.conductoresSelect.push(ident);
				}else{
					for(var ig = 0; ig < ref.conductoresSelect.length; ig++){
						if(ident == ref.conductoresSelect[ig]){
							ref.conductoresSelect.splice(ig, 1);
							break;
						}
					}
				}
				
				if(ref.conductoresSelect.length > 1){
					ref.moduloJG.btnReintentarMasivo.prop("disabled", false);
				}else{
					ref.moduloJG.btnReintentarMasivo.prop("disabled", true);
				}
			});
			
			$(document).on('click','#btnVerMasCond', function (event) {
				event.preventDefault();
				ref.searchConductorSiguientePagina();
			});
			
			$(document).on('click','.icon_view_detail', function () {
				ref.moduloJG.irADetalle(this.getAttribute('data-id'), 
						this.getAttribute('data-ultimoMensaje')
						, this.getAttribute('data-asuntoMail')
						, this.getAttribute('data-dtfechaExcepcion')
						, this.getAttribute('data-fechaExcepcion')
						, this.getAttribute('data-proceso')
						, this.getAttribute('data-referenciaAlternativa')
						, this.getAttribute('data-referenciaPrincipal')
						, this.getAttribute('data-textoMail')
						, this.getAttribute('data-version')
						
						);
			});
		},
		
		exportarConductor : function (){
			var ref=this;
		},
		
		
		buscarConductor: function(){
			var ref=this;
			var data = {};
			
			var tarea = ref.moduloJG.componentesJG.cbmTareaExcepcion.val() || "";
			var proceso = ref.moduloJG.componentesJG.txtProceso.val() || "";
			var refPrincipal = ref.moduloJG.componentesJG.txtRefPrincipal.val() || "";
			var refAlternativa = ref.moduloJG.componentesJG.txtRefAlternativa.val() || "";
			var fechaDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val() || "";
			var fechaHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val() || "";
			
			data.tarea = tarea;
			data.proceso = proceso;
			data.refPrincipal = refPrincipal;
			data.refAlternativa = refAlternativa;
			data.fechaDesdeTexto = "";
			data.fechaHastaTexto = "";
			data.fechaDesde = "";
			data.fechaHasta = "";
			var dataBusqSRest = [];
			
			if(tarea.length > 0){
				dataBusqSRest.push({'fieldId' : 'sProceso', "operator": "%LIKE%", "value": (tarea || "").toUpperCase()});
			}
			if(proceso.length > 0){
				dataBusqSRest.push({'fieldId' : 'sProceso', "operator": "%LIKE%", "value": (proceso || "").toUpperCase()});
			}
			if(refPrincipal.length > 0){
				dataBusqSRest.push({'fieldId' : 'sReferencia1', "operator": "%LIKE%", "value": refPrincipal || ""});
			}
			if(refAlternativa.length > 0){
				dataBusqSRest.push({'fieldId': 'sReferencia2', "operator": "%LIKE%", "value": (refAlternativa || "").toUpperCase()});
			}
			if(fechaDesde.length > 0){
				var datos = fechaDesde.split("/");
				var date_ = new Date(datos[2], datos[1] - 1, datos[0]).getTime() / 1000;
				dataBusqSRest.push({'fieldId': 'F_EnqueueTime', 'operator': '>=', 'value': date_});
				data.fechaDesde = date_+"";
				data.fechaDesdeTexto = fechaDesde;
			}
			if(fechaHasta.length > 0){
				var datos = fechaHasta.split("/");
				var date_ = new Date(datos[2], datos[1] - 1, datos[0]).getTime() / 1000;
				dataBusqSRest.push({'fieldId': 'F_EnqueueTime', 'operator': '<=', 'value': date_});
				data.fechaHasta = date_+"";
				data.fechaHastaTexto = fechaHasta;
			}
			
			ref.filtrosBusqueda = data;
			ref.filtrosBusquedaSRest = dataBusqSRest;
					
			ref.searchConductor();
			
			sessionStorage.setItem("FILTROS_ADM_CONDUCTOR", JSON.stringify(data));
		},
		
		obtenerFiltros: function(){
			var ref = this;
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_CONDUCTOR'));
			if(filtros!=null && filtros!=undefined){
				
				ref.moduloJG.componentesJG.cbmTareaExcepcion.val(filtros['tarea']);
				ref.moduloJG.componentesJG.cbmTareaExcepcion.change();
				
				ref.moduloJG.componentesJG.txtProceso.val(filtros['proceso']);
				ref.moduloJG.componentesJG.txtRefPrincipal.val(filtros['refPrincipal']);
				ref.moduloJG.componentesJG.txtRefAlternativa.val(filtros['refAlternativa']);
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(filtros['fechaDesdeTexto']);
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(filtros['fechaHastaTexto']);
				
				label = $("label[for='" + ref.moduloJG.componentesJG.txtProceso.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.moduloJG.componentesJG.txtRefPrincipal.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.moduloJG.componentesJG.txtRefAlternativa.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.moduloJG.componentesJG.txtFechaDocumentoDesde.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.moduloJG.componentesJG.txtFechaDocumentoHasta.attr('id') + "']");
				label.addClass('active');

				if(ref.moduloJG.componentesJG.cbmTareaExcepcion.val() != "0000" ||
						ref.moduloJG.componentesJG.txtRefPrincipal.val() != "" ||
						ref.moduloJG.componentesJG.txtRefAlternativa.val() != "" ||
						ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val() != "" ||
						ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val() != ""){
					setTimeout(function(){
						ref.moduloJG.componentesJG.btnBuscar.click();
					}, 500);
				}
				
			}
		},
		
		searchConductor: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			$("#bodyTableConductores").html("");
			ref.moduloJG.buscarConductor(ref.filtrosBusquedaSRest)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.totalRegistros = respuesta.datos[0].recordsTotal;
						ref.armarFilasTablaHTML(respuesta.datos[0].listOfDataObjects);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
			});
			/*var ref = this;
			ref.modSistcorr.procesar(true);
			ref.inicializarTablaConsulta();*/
		},
		
		reintentarConductorMasivo: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			
			ref.moduloJG.conductorReintentarMasivo(ref.conductoresSelect)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						for(var ij = 0; ij < respuesta.datos.length; ij++){
							if(respuesta.datos[ij].status == 200){
								ref.modSistcorr.notificar("OK", respuesta.datos[ij].userMessage, "Success");
							}else{
								ref.modSistcorr.notificar("ERROR", respuesta.datos[ij].userMessage, "Error");
							}
						}
						ref.buscarConductor();
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		searchConductorSiguientePagina: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			
			ref.moduloJG.buscarConductorSiguientePagina(ref.filtrosBusquedaSRest)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.totalRegistros = respuesta.datos[0].recordsTotal;
						ref.armarFilasTablaHTML(respuesta.datos[0].listOfDataObjects);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		armarFilasTablaHTML: function(data){
			var ref = this;
			let trTable = "", trsTable = "";
			var rowCount = $('#tablaConsultaCorrespondenciasJG >tbody >tr').length;
			var existVerMas = $("#btnVerMasCond").length;
			
			if(existVerMas > 0)
				rowCount = rowCount - 1;
			
			for(var k = 0; k < data.length; k++){
				trTable = "<tr>";
				
				trTable = trTable + "<td>" + 
				"<i class='far fa-list-alt icon_view_detail' data-toggle='tooltip' title='Clic para ver detalle' data-id='" + data[k].workflowId +"' data-ultimoMensaje='" + data[k].ultimoMensaje + 
        		"' data-asuntoMail='" + data[k].asuntoMail +
        		"' data-dtfechaExcepcion ='" + data[k].dtfechaExcepcion  +
        		"' data-fechaExcepcion ='" + data[k].fechaExcepcion +
        		"' data-proceso='" + data[k].proceso +
        		"' data-referenciaAlternativa ='" + data[k].referenciaAlternativa +
        		"' data-referenciaPrincipal ='" + data[k].referenciaPrincipal +
        		"' data-textoMail ='" + data[k].textoMail +
        		"' data-version ='" + data[k].version +
        		"' style='cursor:pointer'></i>";
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td align='center'>" + 
				"<input type='checkbox' class='inputSeleccionarWFId' style='width: 25px; height: 25px;' value='" + data[k].workflowId +"'" + 
        		" style='cursor:pointer'/>";
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td>" + 
				"<div style='width: 300px;'>"+data[k].ultimoMensaje.replace(/(\r\n|\n|\r)/gm, "")+"</div>";
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td>" + 
				data[k].proceso;
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td>" + 
				data[k].referenciaPrincipal;
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td>" + 
				data[k].referenciaAlternativa;
				trTable = trTable + "</td>";
				
				trTable = trTable + "<td>" + 
				data[k].fechaExcepcion;
				trTable = trTable + "</td>";
				
				trTable = trTable + "</tr>";
				
				trsTable = trsTable + trTable;
			}
			
			if(rowCount > 0){
				if((rowCount + data.length) < parseInt(ref.totalRegistros)){
					trsTable = trsTable + "<tr><td colspan='7' align='left'><p id='btnVerMasCond' style='cursor: pointer;'>Ver M&aacute;s <i class='fas fa-plus-circle' style='font-size: 20px;'></i></p></td></tr>";
				}
				$('#tablaConsultaCorrespondenciasJG >tbody >tr:last').remove();
			}else{
				if((rowCount + data.length) < parseInt(ref.totalRegistros))
					trsTable = trsTable + "<tr><td colspan='7' align='left'><p id='btnVerMasCond' style='cursor: pointer;'>Ver M&aacute;s <i class='fas fa-plus-circle' style='font-size: 20px;'></i></p></td></tr>";
			}
			
			$("#bodyTableConductores").append(trsTable);
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
				console.log("FILTROS BUSQUEDA:");
				console.log(ref.filtrosBusqueda);
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.moduloJG.URL_CONSULTA_CONDUCTOR,
			        	"type": "GET",
			        	"data": ref.filtrosBusqueda,
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
			        	}
			        },
			        cache: true,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'workflowId', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.workflowId +"' data-ultimoMensaje='" + full.ultimoMensaje + 
			        		"' data-asuntoMail='" + full.asuntoMail +
			        		"' data-dtfechaExcepcion ='" + full.dtfechaExcepcion  +
			        		"' data-fechaExcepcion ='" + full.fechaExcepcion +
			        		"' data-proceso='" + full.proceso +
			        		"' data-referenciaAlternativa ='" + full.referenciaAlternativa +
			        		"' data-referenciaPrincipal ='" + full.referenciaPrincipal +
			        		"' data-textoMail ='" + full.textoMail +
			        		"' data-version ='" + full.version +
			        		"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'workflowSeleccionarId', title: 'Seleccionar', defaultContent: '', render: function(data, type, full){
			        		return "<input type='checkbox' class='inputSeleccionarWFId' value='" + full.workflowId +"'" + 
			        		" style='cursor:pointer'/>"
			        	}},
			        	{data: 'ultimoMensaje', title: 'Último mensaje de error', defaultContent: ''},
			        	{data: 'proceso', title: 'Proceso', defaultContent: ''},
			        	{data: 'referenciaPrincipal', title: 'Referencia Principal',defaultContent: ''},
			        	{data: 'referenciaAlternativa', title: 'Referencia Alternativa', defaultContent: ''},
			        	{data: 'fechaExcepcion', title: 'Fecha Excepción',defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_Cond");
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
			        		var nroPag = sessionStorage.getItem("NroPag_Cond");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPagCond");
			        		console.log("origPagCond:" + origPag);
			        		if(origPag == "verDetalleCond"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagCond");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on('page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_Cond", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
					}, 1500);
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on('order.dt', function () {
					var tableOrder = $("#tablaConsultaCorrespondenciasJG").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_Cond", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 1500);
			}
		},
		
		inicializarTablaConsultaFromResponse: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.moduloJG.componentesJG.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsultaFromResponse(data);
			} else {
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'workflowId', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.workflowId +"' data-ultimoMensaje='" + full.ultimoMensaje + 
			        		"' data-asuntoMail='" + full.asuntoMail +
			        		"' data-dtfechaExcepcion ='" + full.dtfechaExcepcion  +
			        		"' data-fechaExcepcion ='" + full.fechaExcepcion +
			        		"' data-proceso='" + full.proceso +
			        		"' data-referenciaAlternativa ='" + full.referenciaAlternativa +
			        		"' data-referenciaPrincipal ='" + full.referenciaPrincipal +
			        		"' data-textoMail ='" + full.textoMail +
			        		"' data-version ='" + full.version +
			        		"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'workflowSeleccionarId', title: 'Seleccionar', defaultContent: '', render: function(data, type, full){
			        		return "<input type='checkbox' class='inputSeleccionarWFId' value='" + full.workflowId +"'" + 
			        		" style='cursor:pointer'/>"
			        	}},
			        	{data: 'ultimoMensaje', title: 'Último mensaje de error', defaultContent: ''},
			        	{data: 'proceso', title: 'Proceso', defaultContent: ''},
			        	{data: 'referenciaPrincipal', title: 'Referencia Principal',defaultContent: ''},
			        	{data: 'referenciaAlternativa', title: 'Referencia Alternativa', defaultContent: ''},
			        	{data: 'fechaExcepcion', title: 'Fecha Excepción',defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc();
	        		setTimeout(function() {
	        			console.log("Actualizando eventos por pagina");
						ref.updateEventosTabla();
					}, 2000);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 2000);
				
			}
		},
		
		exportarExcel: function(){
			var ref = this;
			
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_ADM_CONDUCTOR'));
			if(filtros!=null && filtros!=undefined){
				ref.modSistcorr.procesar(true);
				ref.moduloJG.exportarExcel({"searchCriteria": ref.filtrosBusquedaSRest, "total": ref.totalRegistros})
					.then(function(respuesta){
						if (navigator.appVersion.toString().indexOf('.NET') > 0){
							window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_conductor.xlsx');
						} else {
							var a = document.createElement('a');
		                    a.href = URL.createObjectURL(respuesta);
		                    a.download = 'reporte_conductor.xlsx';
		                    document.body.appendChild(a);
		                    a.click();
		                    document.body.removeChild(a);
						}
						ref.modSistcorr.procesar(false);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
			}else{
				ref.modSistcorr.notificar("ERROR", "Realice primero una busqueda.", "Error");
			}
		},
		
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					//ref.moduloJG.irADetalle(this.dataset.id);
					ref.moduloJG.irADetalle(this.getAttribute('data-id'), 
							this.getAttribute('data-ultimoMensaje')
							, this.getAttribute('data-asuntoMail')
							, this.getAttribute('data-dtfechaExcepcion')
							, this.getAttribute('data-fechaExcepcion')
							, this.getAttribute('data-proceso')
							, this.getAttribute('data-referenciaAlternativa')
							, this.getAttribute('data-referenciaPrincipal')
							, this.getAttribute('data-textoMail')
							, this.getAttribute('data-version')
							
							)
				});
			}
		}
};

setTimeout(function() {
	ADMIN_CONDUCTOR_VISTA.moduloJG = modulo_admin_conductor;
	ADMIN_CONDUCTOR_VISTA.modSistcorr = modulo_sistcorr;
	ADMIN_CONDUCTOR_VISTA.listas = {};
	ADMIN_CONDUCTOR_VISTA.listas.dependencias = new LISTA_DATA([]);
	ADMIN_CONDUCTOR_VISTA.listas.originadores = new LISTA_DATA([]);
	ADMIN_CONDUCTOR_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	ADMIN_CONDUCTOR_VISTA.inicializar();
}, 2000);