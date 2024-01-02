/*9000004412*/
var modulo_generar_planillas = MODULO_GENERAR_PLANILLAS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var GENERAR_PLANILLAS = {
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
	GENERAR_PLANILLAS.modSistcorr = modulo_sistcorr;
	GENERAR_PLANILLAS.modulo = modulo_generar_planillas;
	GENERAR_PLANILLAS.modulo.modSistcorr = modulo_sistcorr; 
	 
	GENERAR_PLANILLAS.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	GENERAR_PLANILLAS.inicializar();
}, 500);
