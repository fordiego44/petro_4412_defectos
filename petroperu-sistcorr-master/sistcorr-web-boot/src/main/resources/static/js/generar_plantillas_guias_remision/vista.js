/*9000004412*/
var modulo_generar_plantillas_guias_remision = MODULO_GENERAR_PLANTILLAS_GUIAS_REMISION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var GENERAR_PLANTILLAS_GUIAS_REMISION = {
		modSistcorr: null,
		modulo: null, 
		componentes: {},
		data: {},
		procesando: false,
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
			ref.iniciarEventos(); 
		},
		
		inicializarComponentes: function(){
			var ref = this;
			document.getElementById("rmt_respuesta").disabled = true; 

		},	
		iniciarEventos: function(){
			var ref = this;
			 
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});

			ref.modulo.componentes.btnLimpiar.click(function(){
				ref.resetarfiltro();
			});
			ref.modulo.componentes.btnGenerar.click(function(event){
				console.log('generar Planilla');
				ref.generarPlanillaGuiaRemision();
			});
			 
		},
		generarPlanillaGuiaRemision: function(){
			console.log('generarPlanillaGuiaRemision');
			var ref=this;

			ref.data.trabajo = this.modulo.componentes.rmt_trabajo.val(); 
			ref.data.courier = this.modulo.componentes.rmt_courier.val();
 			  
			ref.modSistcorr.procesar(true);
			var _data = ref.data;
			 
			console.log('generando planilla guia de remision ...', _data);
			
			ref.modulo.generarPlanillaGuiaRemision(_data)
			.then(function(respuesta){
				console.log(respuesta);
				if(respuesta.estado == true){
					
					if(respuesta.mensaje.indexOf("SE HAN GENERADO LAS PLANILLAS DE GUIAS") != -1) {
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error"); 
					}
					console.log("Plantilla generada."); 
					ref.modSistcorr.procesar(false); 
					ref.modulo.componentes.rmt_respuesta.val(respuesta.mensaje);
					 
				} else { 
					ref.modSistcorr.procesar(false); 
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		resetarfiltro: function() {
			var ref = this; 
			console.log("resetear")
			ref.modulo.componentes.rmt_trabajo.val(0);
			ref.modulo.componentes.rmt_courier.val(0); 
			ref.modulo.componentes.rmt_respuesta.val("");  
		} 
		
};

setTimeout(function(){
	GENERAR_PLANTILLAS_GUIAS_REMISION.modSistcorr = modulo_sistcorr;
	GENERAR_PLANTILLAS_GUIAS_REMISION.modulo = modulo_generar_plantillas_guias_remision;
	GENERAR_PLANTILLAS_GUIAS_REMISION.modulo.modSistcorr = modulo_sistcorr;  
	GENERAR_PLANTILLAS_GUIAS_REMISION.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	GENERAR_PLANTILLAS_GUIAS_REMISION.inicializar();
}, 500);
