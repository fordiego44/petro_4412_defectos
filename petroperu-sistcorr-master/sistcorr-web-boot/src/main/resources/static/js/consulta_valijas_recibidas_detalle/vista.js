/*9000004412*/
var modulo_consulta_valijas_recibidas_detalle = MODULO_CONSULTA_VALIJAS_RECIBIDAS_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_VALIJAS_RECIBIDAS_DETALLE = {
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
			        	{data: ' ', title: ' ', defaultContent: ''},
			        	{data: 'radicado', title: 'Radicado', defaultContent: ''},
			        	{data: 'correspondencia', title: 'Tipo Correspondencia', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dependencia Destino', defaultContent: ''},
			        	{data: 'persona', title: 'Persona Destino', defaultContent: ''},
			        	{data: 'dependencia_remitente', title: 'Dependencia Remitente', defaultContent: ''},
			        	{data: 'remitente', title: 'Remitente', defaultContent: ''} 
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
	CONSULTA_VALIJAS_RECIBIDAS_DETALLE.modSistcorr = modulo_sistcorr;
	CONSULTA_VALIJAS_RECIBIDAS_DETALLE.modulo = modulo_consulta_valijas_recibidas_detalle;
	CONSULTA_VALIJAS_RECIBIDAS_DETALLE.modulo.modSistcorr = modulo_sistcorr; 
	  
	CONSULTA_VALIJAS_RECIBIDAS_DETALLE.inicializar();
}, 500);
