var valijas = VALIJAS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var VISTA_VALIJAS = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		valijas: {},
		masFiltros: false,
		dataTable: null,
		dependenciasUsuario: [],
		jefe: false,
		sizeScreen: 550,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			console.log("Iniciando eventos");
			ref.iniciarEventos();
			console.log("Inicializando componentes");
			//ref.inicializarComponentes();
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.componentes.btnLimpiar.click(function(){
					ref.modulo.componentes.corrCentroGestionRemitente.val("0");
					ref.modulo.componentes.corrCentroGestionRemitente.change();
					ref.modulo.componentes.corrCourier.val("0");
					ref.modulo.componentes.corrCourier.change();
					ref.modulo.componentes.corrIdentificadorValija.val("");
					ref.modulo.componentes.corrOrdenServicio.val("");
				}				
			);
			
			ref.modulo.componentes.btnRegistrarValija.click(function(){
				console.log('btnRegistrarValija');
				ref.procesoRegistrarValijas();
			});
		},
		
		procesoRegistrarValijas: function(){
			console.log('procesoRegistrarValijas');
			var ref = this;
			ref.valijas.codCentroGestionRemitente=ref.modulo.componentes.corrCentroGestionRemitente.val();
			ref.valijas.codCourier=ref.modulo.componentes.corrCourier.val();
			ref.valijas.identificadorValija=ref.modulo.componentes.corrIdentificadorValija.val();
			ref.valijas.ordenServicio=ref.modulo.componentes.corrOrdenServicio.val();
			var _valijas = ref.valijas;
			console.log('se llena el objeto comprobantes');
			ref.modulo.registrarValijas(_valijas)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("Valija Grabada.");
					console.log(respuesta);
					setTimeout(function() {
						//ref.modulo.abrirEdicion(respuesta.datos[0]);
					}, 2000);
				} else {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},	
};

setTimeout(function() {
	VISTA_VALIJAS.modulo = valijas;
	VISTA_VALIJAS.modSistcorr = modulo_sistcorr;
	VISTA_VALIJAS.inicializar();
}, 200);