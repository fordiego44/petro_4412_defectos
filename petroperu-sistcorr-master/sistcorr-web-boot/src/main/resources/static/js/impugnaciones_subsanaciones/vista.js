/*9000004412*/
var modulo_impugnaciones_subsanaciones = MODULO_IMPUGNACIONES_SUBSANACIONES.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var IMPUGNACIONES_SUBSANACIONES_VISTA = {
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
			//INICIO defecto 14
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtSunat.prop('disabled', true);
			
			ref.modulo.btnValidarSUNAT.click(function(){
				console.log("btnValidarSunat");
				var rucSunat =ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtRucSunat.val();
				console.log("Ruc Sunat"+rucSunat);
				ref.modSistcorr.procesar(true);
				ref.modulo.buscarEntidadExternaNacionalSunat(rucSunat)
				.then(function(respuesta){
					if(respuesta.estado == true){
						if (respuesta.datos[0].datosPrincipales.length==0){
							ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtSunat.val('');
							ref.modSistcorr.notificar("ERROR", "Ruc ingresado no tiene Razón Social", "Error");
						}else{
							ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtSunat.val(respuesta.datos[0].datosPrincipales[0].ddp_nombre);
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						}
						ref.modSistcorr.procesar(false);
					} else {
						//ref.modulo.componentesValidarVentasBases.txtSunat.val('RIVERCON');
						//ref.modulo.componentesValidarVentasBases.txtSunat.val('RIVERCON COM.');
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					//ref.modulo.componentesValidarVentasBases.txtSunat.val('RIVERCON');
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			});	

			ref.modulo.compBtnCancelar.click(function(){
				ref.limpiarFormulario();
			})
			//FIN defecto 14
			
			ref.modulo.compBtnGuardar.click(function(){
				ref.modulo.compModalConfirmarRegistro.modal('show');	
			});
		},
		
		limpiarFormulario: function(){
			var ref = this;
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtRucSunat.val('');
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.txtSunat.val('');
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.cmpProceso.change();
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.cmpTipoRecepcion.change();
			ref.modulo.componentesValidarImpugnacionesSubsanaciones.cmpAdmisible.change();

		}

};

setTimeout(function(){
	IMPUGNACIONES_SUBSANACIONES_VISTA.modSistcorr = modulo_sistcorr;
	IMPUGNACIONES_SUBSANACIONES_VISTA.modulo = modulo_impugnaciones_subsanaciones;
	IMPUGNACIONES_SUBSANACIONES_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	IMPUGNACIONES_SUBSANACIONES_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	IMPUGNACIONES_SUBSANACIONES_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	IMPUGNACIONES_SUBSANACIONES_VISTA.inicializar();
}, 500);
