var modulo_compartir_correspondencia = MODULO_COMPARTIR_CORRESPONDENCIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var COMPARTIR_CORRESPONDENCIA = {
	correspondencia: null,
	archivos: null,
	modSistcorr: modulo_sistcorr,
	modulo: modulo_compartir_correspondencia,
	sizeScreen: 550,
	
	inicializar: function(){
		var ref = this;
		ref.modSistcorr.cargarVista();
		ref.iniciarEventos();
		ref.obtenerArchivos();
	},
	
	iniciarEventos: function(){
		var ref = this;
		ref.modulo.btnCompartir.click(function(){
			if(ref.validarCampos())
				ref.modulo.mdlCompartir.modal('show');
		});
		ref.modulo.btnCompartirFooter.click(function(){
			if(ref.validarCampos())
				ref.modulo.mdlCompartir.modal('show');
		});
		ref.modulo.btnCompartirSi.click(function(){
			ref.compartirCorrespondencia();
		});
		ref.modulo.btnRetroceder.click(function(){
			sessionStorage.setItem("tab", "Archivos-tab");
			ref.modSistcorr.retroceder();
		});
		console.log("Width:" + screen.width)
		console.log("SizeScreen:" + ref.sizeScreen)
		if(screen.width < ref.sizeScreen){
			console.log("Title: " + $("#mainTitle").html() );
			var correlativo = $("#mainTitle").html().substring(30);
			$("#mainTitle").text("Compartir - " + correlativo);
		}
		$('[data-toggle="tooltip"]').tooltip('update')
		
	},
	
	obtenerArchivos: function(){
		var ref = this;
		ref.modulo.obtenerArchivos()
			.then(function(respuesta){
				console.log("Respuesta:")
				console.log(respuesta);
				ref.modulo.archivos = respuesta.datos;
				ref.mostrarClaves();
			}).catch(function(error){
				console.log("Error:")
				console.log(error);
			});
	},
	
	compartirCorrespondencia: function(){
		var ref = this;
		ref.modulo.compartirCorrespondencia()
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					setTimeout(function(){
						ref.modulo.btnRetroceder.click();
					}, 2000);
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				ref.modulo.mdlCompartir.modal('hide');
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.notificar("ERROR", error, "Error");
				ref.modulo.mdlCompartir.modal('hide');
			});
	},
	
	mostrarClaves: function(){
		var ref = this;
		for(var i=0;i<ref.modulo.archivos.length;i++){
			var archivo = ref.modulo.archivos[i];
			console.log(archivo);
			$("#key_" + archivo['id']).html(archivo['key']);
		}
	},
	
	validarCampos: function(){
		var ref = this;
		var estado = true;
		var destinatarios = ref.modulo.destinatario.val();
		if(destinatarios.trim()==""){
			ref.modSistcorr.notificar("ERROR", "Ingrese el destinatario", "Error");
			estado = false;
			return estado;
		}
		var dest = destinatarios.split(",").join(";").split(";");
		for(var i=0;i<dest.length;i++){
			if(dest[i].trim!="" && /\S+@\S+\.\S+/.test(dest[i])==false){
				ref.modSistcorr.notificar("ERROR", "Destinatario ingresado no válido", "Error");
				estado = false;
				return estado;
			}
		}
		ref.modulo.destinatario.val(dest.join(";"));
		var copias = ref.modulo.copia.val(); 
		/*if(copias.trim()==""){
			ref.modSistcorr.notificar("ERROR", "Ingrese la copia", "Error");
			estado = false;
			return estado;
		}*/
		var cop = copias.split(",").join(";").split(";");
		for(var i=0;i<cop.length;i++){
			if(cop[i].trim()!="" && /\S+@\S+\.\S+/.test(cop[i])==false){
				ref.modSistcorr.notificar("ERROR", "Copia ingresada no válida", "Error");
				estado = false;
				return estado;
			}
		}
		ref.modulo.copia.val(cop.join(";"));
		if(ref.modulo.asunto.val()==""){
			ref.modSistcorr.notificar("ERROR", "Ingrese el asunto", "Error");
			estado = false;
			return estado;
		}
		if(ref.modulo.contenido.val()==""){
			ref.modSistcorr.notificar("ERROR", "Ingrese el contenido", "Error");
			estado = false;
			return estado;
		}
		return estado;
	}
};

setTimeout(function(){
	console.log("Correspondencia Vista:" + CORRESPONDENCIA);
	COMPARTIR_CORRESPONDENCIA.correspondencia = CORRESPONDENCIA;
	console.log(COMPARTIR_CORRESPONDENCIA.correspondencia);
	COMPARTIR_CORRESPONDENCIA.archivos = ARCHIVOS;
	COMPARTIR_CORRESPONDENCIA.errores = ERRORES || [];
	COMPARTIR_CORRESPONDENCIA.inicializar();
	COMPARTIR_CORRESPONDENCIA.modSistcorr = modulo_sistcorr;
}, 200);