/*9000004412 defecto julio */
var modulo_asociar_orden_servicio = MODULO_ASOCIAR_ORDEN_SERVICIO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var ASOCIAR_ORDEN_SERVICIO = {
		modSistcorr: null,
		modulo: null, 
		componentes: {},
		ordenServicio: {},
		procesando: false,
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
			ref.iniciarEventos(); //DEFECTO 21 JC
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.modulo.componentes.btnAsociarOrdenServicio.click(function(event){
				console.log('Registar Salida');
				ref.asociarOrdenServicio();
			});
		},	
		
		asociarOrdenServicio: function(){
			console.log('asociarOrdenServicio');
			var ref=this;
			
			ref.ordenServicio.correlativo=this.modulo.componentes.txtCorrelativo.val();
			ref.ordenServicio.courier=this.modulo.componentes.txtCourier.val();
			
			ref.modSistcorr.procesar(true);
			var _ordenServicio = ref.ordenServicio;
			console.log('se llena orden servicio');
			ref.modulo.asociarOrdenServicio(_ordenServicio)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("Servicio Asociado.");
					console.log(respuesta);
					setTimeout(function() {
						ref.inicializarTablaRegistrarSalida(ref.tracking.correlativo);
					}, 2000);
					ref.modSistcorr.procesar(false);
				} else {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		//DEFECTO INICIO 21 JC
		iniciarEventos: function(){
			var ref = this;
			 
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			}); 
			ref.modulo.componentes.btnResetear.click(function(){
				ref.resetarfiltro();
			});
		},
		resetarfiltro: function() {
			var ref = this; 
			ref.modulo.componentes.txtCourier.val("");
			ref.modulo.componentes.txtCorrelativo.val("");
		}
		//DEFECTO FIN 21 JC
};

setTimeout(function(){
	ASOCIAR_ORDEN_SERVICIO.modSistcorr = modulo_sistcorr;
	ASOCIAR_ORDEN_SERVICIO.modulo = modulo_asociar_orden_servicio;
	ASOCIAR_ORDEN_SERVICIO.modulo.modSistcorr = modulo_sistcorr;
	ASOCIAR_ORDEN_SERVICIO.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	ASOCIAR_ORDEN_SERVICIO.inicializar();
}, 500);
