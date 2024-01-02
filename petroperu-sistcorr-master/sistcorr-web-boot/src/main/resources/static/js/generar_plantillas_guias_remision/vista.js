/*9000004412*/
var modulo_generar_plantillas_guias_remision = MODULO_GENERAR_PLANTILLAS_GUIAS_REMISION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var GENERAR_PLANTILLAS_GUIAS_REMISION = {
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
			
		 
		},	
		 

};

setTimeout(function(){
	GENERAR_PLANTILLAS_GUIAS_REMISION.modSistcorr = modulo_sistcorr;
	GENERAR_PLANTILLAS_GUIAS_REMISION.modulo = modulo_generar_plantillas_guias_remision;
	GENERAR_PLANTILLAS_GUIAS_REMISION.modulo.modSistcorr = modulo_sistcorr; 
	 
	GENERAR_PLANTILLAS_GUIAS_REMISION.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	GENERAR_PLANTILLAS_GUIAS_REMISION.inicializar();
}, 500);
