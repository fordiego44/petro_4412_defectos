/*9000004412*/
var modulo_solicitud_despacho = MODULO_SOLICITUD_DESPACHO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var SOLICITUD_DESPACHO = {
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
			
			ref.modulo.compBtnGuardar.click(function(){
				ref.modulo.compModalConfirmarRegistro.modal('show');	
			});	
		 
		} 
	 

};

setTimeout(function(){
	SOLICITUD_DESPACHO.modSistcorr = modulo_sistcorr;
	SOLICITUD_DESPACHO.modulo = modulo_solicitud_despacho;
	SOLICITUD_DESPACHO.modulo.modSistcorr = modulo_sistcorr; 
	  
	SOLICITUD_DESPACHO.inicializar();
}, 500);
