/*9000004412*/
var modulo_venta_base = MODULO_VENTA_BASE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var VENTA_BASE_VISTA = {
		modSistcorr: null,
		modulo: null,
		ventaBase: {},
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
			//Inicio defecto 11
			ref.modulo.componentesVentasBases.cmpProceso.select2();
			//Fin defecto 11
			ref.modulo.componentesVentasBases.txtSunat.prop('disabled', true);
			ref.modulo.btnValidarSUNAT.click(function(){
				console.log("btnValidarSunat");
				var rucSunat =ref.modulo.componentesVentasBases.txtRucSunat.val();
				console.log("Ruc Sunat"+rucSunat);
				ref.modSistcorr.procesar(true);
				ref.modulo.buscarEntidadExternaNacionalSunat(rucSunat)
				.then(function(respuesta){
					if(respuesta.estado == true){
						if (respuesta.datos[0].datosPrincipales.length==0){
							ref.modulo.componentesVentasBases.txtSunat.val('');
							ref.modSistcorr.notificar("ERROR", "Ruc ingresado no tiene Razón Social", "Error");
						}else{
							ref.modulo.componentesVentasBases.txtSunat.val(respuesta.datos[0].datosPrincipales[0].ddp_nombre);
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						}
						ref.modSistcorr.procesar(false);
					} else {
						//ref.modulo.componentesVentasBases.txtSunat.val('RIVERCON');
						//ref.modulo.componentesVentasBases.txtSunat.val('RIVERCON COM.');
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					//ref.modulo.componentesVentasBases.txtSunat.val('RIVERCON');
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			});	
			
			ref.modulo.btnValidarVentasBases.click(function(){
				console.log('Validar Ventas Bases');
				ref.validarVentasBases();
				
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
		
		validarVentasBases: function(){
			console.log('validarVentasBases');
			var ref = this;
			ref.ventaBase.ruc=ref.modulo.componentesVentasBases.txtRucSunat.val();
			ref.ventaBase.nombreProveedor=ref.modulo.componentesVentasBases.txtSunat.val();
			ref.ventaBase.nroProceso=ref.modulo.componentesVentasBases.cmpProceso.val();
			ref.ventaBase.fueraHora=ref.modulo.componentesVentasBases.cmpHora.val();
			var _ventaBase = ref.ventaBase;
			console.log('se llena el objeto venta base');
			ref.modulo.validaVentasBasesFueraHora(_ventaBase)
			.then(function(respuesta){
				if(respuesta.estado == true){
					//ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("VentaBases Validada.");
					console.log(respuesta);
					setTimeout(function() {
						debugger;
						ref.guardarVentasBases();
						//ref.modulo.modalConfirmarVentaBases.modal('show');
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
		
		guardarVentasBases: function(){
			console.log('Guardar Ventas bases')
			var ref = this;
			ref.ventaBase.ruc=ref.modulo.componentesVentasBases.txtRucSunat.val();
			ref.ventaBase.nombreProveedor=ref.modulo.componentesVentasBases.txtSunat.val();
			ref.ventaBase.nroProceso=ref.modulo.componentesVentasBases.cmpProceso.val();
			ref.ventaBase.fueraHora=ref.modulo.componentesVentasBases.cmpHora.val();
			var _ventaBase = ref.ventaBase;
			console.log('se llena el objeto venta base');
			ref.modulo.registrarVentasBases(_ventaBase)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("VentaBases Validada.");
					console.log(respuesta);
					setTimeout(function() {
						debugger;
						ref.modulo.modalConfirmarVentaBases.modal('show');
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

setTimeout(function(){
	VENTA_BASE_VISTA.modSistcorr = modulo_sistcorr;
	VENTA_BASE_VISTA.modulo = modulo_venta_base;
	VENTA_BASE_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	VENTA_BASE_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	VENTA_BASE_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	VENTA_BASE_VISTA.inicializar();
}, 500);
