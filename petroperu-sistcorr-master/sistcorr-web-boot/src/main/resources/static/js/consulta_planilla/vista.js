/*9000004412*/
var modulo_consulta_planilla = MODULO_CONSULTA_PLANILLA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_PLANILLA = {
		modSistcorr: null,
		modulo: null, 
		componentes: {},
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
			        	{data: 'correlativo_pL', title: 'Correlativo PL', defaultContent: ''},
			        	{data: 'fecha', title: 'Fecha y Hora Panilla', defaultContent: ''},
			        	{data: 'cgc', title: 'Codigo CGC', defaultContent: ''},
			        	{data: 'usuario_generador', title: 'Usuario Generador', defaultContent: ''},
			        	{data: 'courier', title: 'Courier', defaultContent: ''},
			        	{data: 'orden_dervicio', title: 'Orden Servicio', defaultContent: ''}, 
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

};

setTimeout(function(){
	CONSULTA_PLANILLA.modSistcorr = modulo_sistcorr;
	CONSULTA_PLANILLA.modulo = modulo_consulta_planilla;
	CONSULTA_PLANILLA.modulo.modSistcorr = modulo_sistcorr; 
	 
	CONSULTA_PLANILLA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CONSULTA_PLANILLA.inicializar();
}, 500);
