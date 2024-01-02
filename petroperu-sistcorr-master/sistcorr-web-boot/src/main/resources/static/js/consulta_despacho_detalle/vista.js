/*9000004412*/
var modulo_consulta_despacho_detalle = MODULO_CONSULTA_DESPACHO_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_DESPACHO_DETALLE = {
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
			
			   
		 
		} 
	 

};

setTimeout(function(){
	CONSULTA_DESPACHO_DETALLE.modSistcorr = modulo_sistcorr;
	CONSULTA_DESPACHO_DETALLE.modulo = modulo_consulta_despacho_detalle;
	CONSULTA_DESPACHO_DETALLE.modulo.modSistcorr = modulo_sistcorr; 
	  
	CONSULTA_DESPACHO_DETALLE.inicializar();
}, 500);
