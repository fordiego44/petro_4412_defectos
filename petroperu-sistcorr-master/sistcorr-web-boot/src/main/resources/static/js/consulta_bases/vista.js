/*9000004412*/
var modulo_consulta_base = MODULO_CONSULTA_BASE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_BASE_VISTA = {
		modSistcorr: null,
		modulo: null,
		consultaVentaBase: {},
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
			//Inicio defecto 12
			ref.modulo.componentesVentasBases.cmpProceso.select2();
			//Fin defecto 12
			
			ref.modulo.componentesValidarVentasBases.txtSunat.prop('disabled', true);
			
			ref.modulo.btnValidarSUNAT.click(function(){
				console.log("btnValidarSunat");
				var rucSunat =ref.modulo.componentesValidarVentasBases.txtRucSunat.val();
				console.log("Ruc Sunat"+rucSunat);
				ref.modSistcorr.procesar(true);
				ref.modulo.buscarEntidadExternaNacionalSunat(rucSunat)
				.then(function(respuesta){
					if(respuesta.estado == true){
						if (respuesta.datos[0].datosPrincipales.length==0){
							ref.modulo.componentesValidarVentasBases.txtSunat.val('');
							ref.modSistcorr.notificar("ERROR", "Ruc ingresado no tiene Razón Social", "Error");
						}else{
							ref.modulo.componentesValidarVentasBases.txtSunat.val(respuesta.datos[0].datosPrincipales[0].ddp_nombre);
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
			
			ref.modulo.btnValidarVentasBases.click(function(){
				console.log('Validar Ventas Bases');
				ref.validarVentasBases();
				
			});
			
			//Inicio defecto 12
			ref.modulo.compBtnCancelar.click(function(){
				ref.limpiarFormulario();
			})
			//Fin defecto 12
			
		},

		//Inicio defecto 12
		limpiarFormulario: function(){
			var ref = this;
			ref.modulo.componentesVentasBases.txtRucSunat.val('');
			ref.modulo.componentesVentasBases.txtSunat.val('');
			ref.modulo.componentesVentasBases.cmpProceso.val();
			ref.modulo.componentesVentasBases.cmpHora.val();

		},
		//Fin defecto 12		
		
		validarVentasBases: function(){
			console.log('validarVentasBases');
			var ref = this;
			ref.consultaVentaBase.ruc=ref.modulo.componentesValidarVentasBases.txtRucSunat.val();
			ref.consultaVentaBase.nombreProveedor=ref.modulo.componentesValidarVentasBases.txtSunat.val();
			ref.consultaVentaBase.nroProceso=ref.modulo.componentesValidarVentasBases.cmpProceso.val();
			ref.consultaVentaBase.fueraHora=ref.modulo.componentesValidarVentasBases.cmpHora.val();
			var _consultaVentaBase = ref.consultaVentaBase;
			console.log('se llena el objeto venta base');
			ref.modulo.validaConsultaVentasBasesFueraHora(_consultaVentaBase)
			.then(function(respuesta){
				if(respuesta.estado == true){
					//ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("VentaBases Validada.");
					console.log(respuesta);
					setTimeout(function() {
						debugger;
						ref.guardarConsultaVentasBases();
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
		
		guardarConsultaVentasBases: function(){
			console.log('guardarConsultaVentasBases')
			var ref = this;
			ref.consultaVentaBase.ruc=ref.modulo.componentesValidarVentasBases.txtRucSunat.val();
			ref.consultaVentaBase.nombreProveedor=ref.modulo.componentesValidarVentasBases.txtSunat.val();
			ref.consultaVentaBase.nroProceso=ref.modulo.componentesValidarVentasBases.cmpProceso.val();
			ref.consultaVentaBase.fueraHora=ref.modulo.componentesValidarVentasBases.cmpHora.val();
			var _consultaVentaBase = ref.consultaVentaBase;
			console.log('se llena el objeto consulta venta base');
			ref.modulo.registrarConsultaVentasBases(_consultaVentaBase)
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
	CONSULTA_BASE_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTA_BASE_VISTA.modulo = modulo_consulta_base;
	CONSULTA_BASE_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	CONSULTA_BASE_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	CONSULTA_BASE_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CONSULTA_BASE_VISTA.inicializar();
}, 500);
