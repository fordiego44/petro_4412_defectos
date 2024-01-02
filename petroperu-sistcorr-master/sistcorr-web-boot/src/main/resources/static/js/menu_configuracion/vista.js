var modulo_correspondecia = MODULO_CORRESPONDENCIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_VISTA = {
		acciones: [],
		dependencias: [],
		bandeja: {correspondencias:[], asignaciones:[]},
		modSistcorr: null,
		modulo: null,
		errores: [],
		rol_jefe: false,
		rol_gestor: false,
		tamanioMaxArchivo: 10,
		tareaSeleccionada : null,
		correspondenciaSeleccionada: null,
		esDeEmisionSeleccionada: '',
		numeroDocumentoSeleccionado: '',
		correlativoSeleccionado: '',
		asignacionSeleccionada: null,
		filtrosBusqueda: null,
		// TICKET 900000SIST
		pagina: 0,
		cantidad: 100,
		total: 0,
		// FIN TICKET 900000SIST
		// TICKET 9000004270
		workflowIdSeleccionado: '',
		// FIN TICKET
		// TICKET 9000004494
		sessionPaginado: '',
		respuestaBusqueda: null,
		cantidadPrev: 0,
		totalObtenidos: 0,
		itemsPorPagina: 0,
		totalMostrados: 0,
		totalCorrespondencias: 0,
		// FIN TICKET
		inicializar: function(){
			var ref = this;

	
			ref.modSistcorr.cargarVista();
			ref.modSistcorr.eventoTooltip();
			if(ref.modSistcorr.getCantidadLog() == 1){
				//ref.modulo.abrirMenu();
			}
			ref.iniciarEventos();
			ref.mostrarErrores();
			setTimeout(function(){
				$(".div_eventos>div:nth-child(2n)").css('background-color', 'white');
				$(".div_eventos>div:nth-child(2n+1)").css('background-color', 'black');
			}, 2000);
		},
		

		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modSistcorr.setDependenciasDestino(ref.dependencias);
			
			ref.modSistcorr.setAcciones(ref.acciones);			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			
			ref.modulo.btnAbrirBusqueda.click(function(event){
				ref.abrirModalBusqueda();
			});
	
	
			
			ref.modulo.compbtnCompletarCorrespondencia.click(function(event){
				// TICKET 9000004044
				//ref.completarCorrespondencia();
				ref.tieneDocumentoRespuestaEnCursoAsignacion("completarcorrespondencia");
				// FIN TICKET
			});
			
	
	

			
			setTimeout(function() {
				ref.modSistcorr.eventosS2();	
				//ref.modSistcorr.eventoSelect();
			}, 200);
			
			ref.modSistcorr.accordionMenu.click(function(event){
				var t = $(this);
				var id = t.attr('data-id');
				console.log("Id ocultar:" + id);
				ocultarAcordeon(id);
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			


			
	
		},
		
	
		
		mostrarErrores: function(){
			var ref = this;
			for(var i in ref.errores){
				ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");
			}
		},
		

};

setTimeout(function(){

	CORRESPONDENCIA_VISTA.modulo = modulo_correspondecia;
	CORRESPONDENCIA_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_VISTA.errores = [];
	CORRESPONDENCIA_VISTA.inicializar();
}, 500);