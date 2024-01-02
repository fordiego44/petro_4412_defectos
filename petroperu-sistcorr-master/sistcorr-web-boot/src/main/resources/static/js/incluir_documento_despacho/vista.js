/*9000004412*/
var modulo_incluir_documento_despacho = MODULO_INCLUIR_DOCUMENTO_DESPACHO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var INCLUIR_DOCUMENTO_DESPACHO = {
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
	INCLUIR_DOCUMENTO_DESPACHO.modSistcorr = modulo_sistcorr;
	INCLUIR_DOCUMENTO_DESPACHO.modulo = modulo_incluir_documento_despacho;
	INCLUIR_DOCUMENTO_DESPACHO.modulo.modSistcorr = modulo_sistcorr;  
	INCLUIR_DOCUMENTO_DESPACHO.inicializar();
}, 500);
