var modulo_respuesta_firma = MODULO_RESPUESTA_FIRMA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var RESPUESTA_FIRMA = {
		modSistcorr: null,
		modulo: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.iniciarEventosComponentes();
			ref.verificarRespuesta();
		},
		
		iniciarEventosComponentes: function(){
			var ref = this;
			ref.modulo.btnRegresarBandeja.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modulo.regresarBandeja();
			});
			
			ref.modulo.btnEnviarCorrespondencia.click(function(){
				ref._enviarCorrespondenciaGrupal();
			});
		},
		
		verificarRespuesta: function(){
			var ref = this;
			var estado = ref.modulo.estadoCorrespondencia.val();
			var rutaAprobacion = ref.modulo.rutaAprobacion.val();
			var cantidad = ref.modulo.cantidadDestinatarios.val();
			var cantidadInternos = ref.modulo.cantidadDestinatariosInternos.val();
			console.log("VerificarRespuesta:" + estado + "||" + cantidad)
			/*if((estado == CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA || estado == CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA) && cantidad > 0){
				if(cantidadInternos == 0){
					ref._procesarExistenciaEnlace();
				}
				setTimeout(function() {
					ref.modulo.modalEnviarCorrespondencia.modal('show');
				}, 500);				
			}else{
				if((estado == CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA || estado == CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA) && cantidad == 0){
					console.log("LLAMADA A FUNCION PROCESAR EXISTENCIA ENLACE");
					ref._procesarExistenciaEnlace();
				}
			}*/
			var corrPrc = ref.modulo.idCorrespondenciaPrc.val();
			var corrEnv = ref.modulo.idCorrespondenciaEnv.val();
			if(corrPrc != undefined && corrPrc != ""){
				ref._procesarExistenciaEnlaceGrupal();
			}
			setTimeout(function() {
				if(corrEnv != undefined && corrEnv != ""){
					ref.modulo.modalEnviarCorrespondencia.modal('show');
				}
			}, 500);
			if(estado == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && rutaAprobacion == "0"){
				var retroc = sessionStorage.getItem('retroceder-asignacion');
				console.log("Es retroceder:");
				console.log(retroc);
				if(retroc && retroc == '1'){
					sessionStorage.removeItem('retroceder-asignacion');
				}else{
					setTimeout(function() {
						//ref.modulo.modalAsignarSiguienteFirmante.modal('show');
					}, 500);
				}
			}else{
				
			}
			
			var corrErr = ref.modulo.idCorrespondenciaErr.val();
			var msjErr = ref.modulo.msjCorrespondenciasErr.val();
			if(corrErr != undefined && msjErr != undefined && msjErr != ""){
				ref.modSistcorr.notificar("ERROR", msjErr, "Error");
			}
		},
		
		_enviarCorrespondenciaGrupal: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.enviarCorrespondenciaGrupal()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.modalEnviarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.modalEnviarCorrespondencia.modal('hide');
				});
		},
		
		_procesarExistenciaEnlaceGrupal: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.procesarExistenciaEnlaceGrupal()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						//ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}else{
						ref.modSistcorr.procesar(false);
						if(respuesta.mensaje != null && respuesta.mensaje != ""){
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						}
					}
					//ref.modulo.modalEnviarCorrespondencia.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.modalEnviarCorrespondencia.modal('hide');
				});
		}
};

setTimeout(function() {
	RESPUESTA_FIRMA.modSistcorr = modulo_sistcorr;
	RESPUESTA_FIRMA.modulo = modulo_respuesta_firma;
	RESPUESTA_FIRMA.inicializar();
}, 500);