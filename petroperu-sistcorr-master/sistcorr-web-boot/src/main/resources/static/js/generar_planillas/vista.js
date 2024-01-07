/*9000004412*/
var modulo_generar_planillas = MODULO_GENERAR_PLANILLAS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var GENERAR_PLANILLAS = {
		modSistcorr: null,
		modulo: null, 
		componentes: {},
		procesando: false,
		data: {},
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
				ref.generarPlanilla();
			});
			ref.modulo.componentes.rmt_alcance.change(function(e) {
				ref.listaCourier(e)
			})
		},
		
		generarPlanilla: function(){
			console.log('generarPlanilla');
			var ref=this;
			
			ref.data.alcance = this.modulo.componentes.rmt_alcance.val();
			ref.data.courier = this.modulo.componentes.rmt_courier.val(); 
			ref.data.urgente = this.modulo.componentes.corr_urgente.is(':checked') === true ? "SI" : "NO"; 
			  
			ref.modSistcorr.procesar(true);
			var _data = ref.data;
			 
			console.log('generando planilla...', _data);
			
			ref.modulo.generarPlanilla(_data)
			.then(function(respuesta){
				console.log(respuesta);
				if(respuesta.estado == true){
					
					if(respuesta.mensaje.indexOf("SE HA GENERADO LA PLANILLA")!= -1) {
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error"); 
					}
					console.log("Plantilla generada."); 
					ref.modSistcorr.procesar(false); 
					ref.modulo.componentes.rmt_respuesta.val(respuesta.mensaje);
					 
				} else { 
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		listaCourier: function(e) {
			console.log(e)
			var ref=this;
			ref.data.alcance = this.modulo.componentes.rmt_alcance.val();
			 
			var _data = ref.data;
			 
			ref.modulo.listaCouriers(_data)
			.then(function(respuesta){
				console.log(respuesta)
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			}); 
		},
		resetarfiltro: function() {
			var ref = this; 
			ref.modulo.componentes.rmt_alcance.val(0);
			ref.modulo.componentes.rmt_courier.val(0);
			ref.modulo.componentes.corr_urgente.val(0); 
			ref.modulo.componentes.rmt_respuesta.val("");  
		}  
}; 
setTimeout(function(){
	GENERAR_PLANILLAS.modSistcorr = modulo_sistcorr;
	GENERAR_PLANILLAS.modulo = modulo_generar_planillas;
	GENERAR_PLANILLAS.modulo.modSistcorr = modulo_sistcorr;  
	GENERAR_PLANILLAS.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	GENERAR_PLANILLAS.inicializar();
}, 500);
