/*9000004412*/
var modulo_recibir_propuesta = MODULO_RECIBIR_PROPUESTA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var RECIBIR_PROPUESTA_VISTA = {
		modSistcorr: null,
		modulo: null,
		tiposCorrespondencia: [],
		correspondenciaSelecionada: {},  
		procesando: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
		},
		
		inicializarComponentes: function(){
			var ref = this; 

			//Inicio defecto 13
			ref.modulo.componentesRecibirPropuesta.cmpProceso.select2();
			//Fin defecto 13
			
			ref.modulo.btnValidarSUNAT.click(function(){
				 console.log("???");
			});	

			//Inicio defecto 11
			ref.modulo.compBtnCancelar.click(function(){
				ref.limpiarFormulario();
			})
			//Fin defecto 11
		},
		
		
		//Inicio defecto 11
		limpiarFormulario: function(){
			var ref = this;
			ref.modulo.componentesVentasBases.txtRucSunat.val('');
			ref.modulo.componentesVentasBases.txtSunat.val('');
			ref.modulo.componentesVentasBases.cmpProceso.val();
			ref.modulo.componentesVentasBases.cmpHora.val();

		},
		//Fin defecto 11

};

setTimeout(function(){
	RECIBIR_PROPUESTA_VISTA.modSistcorr = modulo_sistcorr;
	VENTA_BASRECIBIR_PROPUESTA_VISTAE_VISTA.modulo = modulo_recibir_propuesta;
	RECIBIR_PROPUESTA_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	RECIBIR_PROPUESTA_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	RECIBIR_PROPUESTA_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	RECIBIR_PROPUESTA_VISTA.inicializar();
}, 500);
