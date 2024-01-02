var modulo_login = MODULO_LOGIN.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var LOGIN_VISTA = {
		modSistcorr: null,
		modulo: null,
		inicializar: function(){
			var ref = this;
			
			ref.modSistcorr.clearSession();
			ref.generarImagenCaptcha();
			ref.modulo.btnGenerarCaptcha.click(function(){
				ref.generarImagenCaptcha();
			});
			
			ref.modulo.frmLogin.submit(function(){
				ref.modSistcorr.procesar(true);
			});
			/*setTimeout(function(){
				location.reload();
			}, 6000000);*/
		},
		
		generarImagenCaptcha: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.inputIdCaptcha.val("");
			ref.modulo.inputCaptcha.val("");
			ref.modulo.imgCaptcha.hide();
			ref.modulo.generarCaptcha()
				.then(function(resultado){
					if(resultado.estado == true){
						var data = resultado.datos[0];
						ref.modulo.inputIdCaptcha.val(data.id);
						ref.modulo.imgCaptcha.attr("src", 'data:image/png;base64,' + data.capatcha);
						ref.modulo.imgCaptcha.show();
						setTimeout(function(){
							ref.modulo.inputNombre.focus();
						}, 900);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", ref.modSistcorr.mensajeErrorPeticion, "Error");
				});
		}
};


setTimeout(function(){
	LOGIN_VISTA.modSistcorr = modulo_sistcorr;
	LOGIN_VISTA.modulo = modulo_login;
	LOGIN_VISTA.inicializar();
}, 500);
