/*9000004412*/
var modulo_consulta_valijas_recibidas = MODULO_CONSULTA_VALIJAS_RECIBIDAS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_VALIJAS_RECIBIDAS = {
		modSistcorr: null,
		modulo: null, 
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		valijasRecibidas:{},
		procesando: false,
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.inicializarTablaConsultaDefecto([]);
			
			ref.componentes.txtFechaDocumentoDesde = ref.modulo.componentes.txtFechaDocumentoDesde.datepicker({
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
			
			ref.modulo.componentes.btnLimpiar.click(function(){
			
				ref.modulo.componentes.rmt_estado.val("0");
				ref.modulo.componentes.rmt_estado.change();
				ref.modulo.componentes.rmt_remitente.val("0");
				ref.modulo.componentes.rmt_remitente.change();
				ref.modulo.componentes.rmt_recibe.val("0");
				ref.modulo.componentes.rmt_recibe.change();
				ref.modulo.componentes.rmt_courier.val("0");
				ref.modulo.componentes.rmt_courier.change();
				ref.modulo.componentes.txtCorrelativo.val("");
				ref.modulo.componentes.txtFechaDocumentoDesde.val("");
				ref.modulo.componentes.txtFechaDocumentoHasta.val("");
			}				
		);
			
			ref.componentes.txtFechaDocumentoHasta = ref.modulo.componentes.txtFechaDocumentoHasta.datepicker({
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
			
			ref.modulo.componentes.btnFechaDocumentoDesde.click(function(){
				ref.modulo.componentes.txtFechaDocumentoDesde.click();
				ref.modulo.componentes.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaDocumentoHasta.click(function(){
				ref.modulo.componentes.txtFechaDocumentoHasta.click();
				ref.modulo.componentes.txtFechaDocumentoHasta.focus();
			});	
			
			ref.modulo.btnBuscar.click(function(event){
				console.log('Buscar Valijas Recibidas');
				ref.buscarValijasRecibidas();
			});
			
			setTimeout(function() {		
				ref.obtenerFiltros();
			}, 1500);
		 
		},	
		
		obtenerFiltros: function() {
		    var ref = this;
		    var _filtro = ref.modSistcorr.getfiltrosConsultaValijasRecibidas();
		    ref.despacho = _filtro;
		},
		
		buscarValijasRecibidas: function(){
			console.log('buscarValijasRecibidas');
			var ref=this;
			ref.valijasRecibidas= {};
			
			ref.valijasRecibidas.nroCorrelativo=this.modulo.componentes.txtCorrelativo.val();
		    ref.valijasRecibidas.codEstado=this.modulo.componentes.rmt_estado.val();
		    ref.valijasRecibidas.fechaDesde=this.modulo.componentes.txtFechaDocumentoDesde.val();
		    ref.valijasRecibidas.fechaHasta=this.modulo.componentes.txtFechaDocumentoHasta.val();
		    ref.valijasRecibidas.cgcRemitente=this.modulo.componentes.rmt_remitente.val();
		    ref.valijasRecibidas.cgcReceptor=this.modulo.componentes.rmt_recibe.val();
		    ref.valijasRecibidas.courier=this.modulo.componentes.rmt_courier.val();
		    ref.searchValijasRecibidas();
		    
		    ref.modSistcorr.setfiltrosConsultaValijasRecibidas(ref.valijasRecibidas);
		},
		
		searchValijasRecibidas: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.inicializarTablaConsulta();
			ref.modSistcorr.procesar(false);
		},
		
		inicializarTablaConsultaDefecto: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentes.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsultaDefecto(data);
			} else {
				ref.modulo.componentes.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.componentes.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'ver', title: 'Ver', defaultContent: ''},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'fecha', title: 'Fecha', defaultContent: ''},
			        	{data: 'recibe', title: 'CGC Recibe', defaultContent: ''},
			        	{data: 'remitente', title: 'CGC Remitente', defaultContent: ''},
			        	{data: 'courier', title: 'Courier', defaultContent: ''},
			        	{data: 'guia', title: 'Guía', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'cantidad', title: 'Cantidad', defaultContent: ''} 
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					ref.dataTableConsulta.responsive.rebuild();
	        		ref.dataTableConsulta.responsive.recalc(); 
	        		setTimeout(function() {
						 
					}, 1500);
				});
				
				setTimeout(function() {
					 
				}, 1500);
			}
		},
		
		
		inicializarTablaConsulta: function(){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.componentes.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.modulo.componentes.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.componentes.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_CONSULTA_VALIJAS_RECIBIDAS,
			        	"type": "GET",
			        	"data": ref.valijasRecibidas,
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
			        	/*{data: 'id', title: 'Elim.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarFuncionario' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id +"' data-registro='" + full.registro +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id', title: 'Mod.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-edit icon_editar btnModificarFuncionario'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id +"' style='cursor:pointer'></i>"
			        	}},*/
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'fecha', title: 'Fecha', defaultContent: ''},
			        	{data: 'cgcRecibe', title: 'CGC Recibe', defaultContent: ''},
			        	{data: 'cgcRemitente', title: 'CGC Remitente', defaultContent: ''},
			        	{data: 'courier', title: 'Courier', defaultContent: ''},
			        	{data: 'guia', title: 'Guia', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'cantidad', title: 'Cantidad', defaultContent: ''},
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
			        		/*if(origPag == "verDetalleBS"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagJG");
			        		}*/
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();*/
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_JG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();*/
					}, 1500);
				});
				
				ref.modulo.componentes.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaDespachoGeneral").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_JG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();*/
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					/*ref.modulo.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.modulo.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();*/
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			/*var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					sessionStorage.setItem(ref.storageVerDetalle, "S");//TICKET 9000004710
					sessionStorage.setItem("_itemSelec_ConsultaEventoDocumentoSalida", this.dataset.id);//TICKET 9000004808
					ref.moduloED.irADetalle(this.dataset.id);
				});
			}*/
		},

};

setTimeout(function(){
	CONSULTA_VALIJAS_RECIBIDAS.modSistcorr = modulo_sistcorr;
	CONSULTA_VALIJAS_RECIBIDAS.modulo = modulo_consulta_valijas_recibidas;
	CONSULTA_VALIJAS_RECIBIDAS.modulo.modSistcorr = modulo_sistcorr; 
	 
	CONSULTA_VALIJAS_RECIBIDAS.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CONSULTA_VALIJAS_RECIBIDAS.inicializar();
}, 500);
