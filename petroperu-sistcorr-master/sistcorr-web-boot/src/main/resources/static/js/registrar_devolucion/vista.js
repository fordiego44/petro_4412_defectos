/*9000004412 defecto julio */
var modulo_registrar_devolucion = MODULO_REGISTRAR_DEVOLUCION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REGISTRAR_DEVOLUCION = {
		moduloJG: null,
		modSistcorr: null,
		modulo: null, 
		componentes: {},
		devolucion:{},
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
			
			ref.modulo.componentes.btnRegistrarDevolucion.click(function(event){
				console.log('Registar Devolucion');
				ref.registrarDevolucion();
			});
		},	
		
		registrarDevolucion: function(){
			console.log('registrarDevolucion');
			var ref=this;
			
			ref.devolucion.correlativo=this.modulo.componentes.correlativo.val();
			ref.devolucion.codMotivo=this.modulo.componentes.codMotivo.val();
			
			ref.modSistcorr.procesar(true);
			var _devolucion = ref.devolucion;
			console.log('se llena el objeto devolucion');
			ref.modulo.registrarDevolucion(_devolucion)
			.then(function(respuesta){
				if(respuesta.estado == true){
					console.log("Devolucion Grabado.");
					console.log(respuesta);
					setTimeout(function() {
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
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
		},
		resetarfiltro: function() {
			var ref = this; 
			ref.modulo.componentes.correlativo.val("");
			ref.modulo.componentes.codMotivo.val(0);
		} 
};

setTimeout(function(){
	REGISTRAR_DEVOLUCION.modSistcorr = modulo_sistcorr;
	REGISTRAR_DEVOLUCION.modulo = modulo_registrar_devolucion;
	REGISTRAR_DEVOLUCION.modulo.modSistcorr = modulo_sistcorr;  
	REGISTRAR_DEVOLUCION.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	REGISTRAR_DEVOLUCION.inicializar();
}, 500);
