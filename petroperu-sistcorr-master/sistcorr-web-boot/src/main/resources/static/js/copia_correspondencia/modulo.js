var MODULO_COPIA_CORRESPONDENCIA = (function(){
	var instance;
	function ModuloCopiaCorrespondencia(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Winston';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAgregarContacto = $("#btnCopyContact");
		this.btnEnviarCopia = $("#btnSendCopy");
		this.btnEnviarCopiaCab = $("#btnSendCopyCab");
		this.corrCopia = $("#corrCopia");
		this.correlativo = $("#correlativo");
		this.btnEliminarContacto = $(".removeContacto");
		this.compSinResultados = $("#sinResultados");
		this.corrTexto = $("#corrTexto");
		this.cantDestinatarios = $("#cantDestinatarios");
		this.btnRetroceder = $("#btnRetroceder"),
		this.plantillaCorrespondencias = $("#template-copia-correspondencia");
		this.URL_AGREGAR_CONTACTO = "app/correspondencias/agregarContacto";
		this.URL_ELIMINAR_CONTACTO = "app/correspondencias/eliminarContacto";
		this.URL_ENVIAR_COPIA_CORRESPONDENCIA = "app/correspondencias/enviarCopiaCorrespondencia";
		this.btnEnviarCopiaSi = $("#btnEnviarCopiaSi");
		this.btnEliminarCopiaSi = $("#btnEliminarCopiaSi");
		this.vistaCopiaCorrespondencia = $(".div_destinatarios");
		//this.compFiltrosBusqueda = $("#compFiltrosBusqueda");
		/*this.btnFiltrar = $("#btnFiltrar");
		this.corrEstado = $("#corrEstado");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.panelFiltros = $(".masFiltros");
		this.plantillaCorrespondencias = $("#template-correspondencias");
		this.compBusqueda = {
			myModal: $("#modalBuscar"),
			//asustoCorrespondencia: $("#textAsuntoCorrespondencia"),
			//tipoAccion: $("#textTipoAccion"),
			//remitente: $("#textRemitente"),
			//numeroDocumento: $("#textNumeroDoc"),
			btnBuscar: $("#btnBuscar")
		};*/
	};
	
	ModuloCopiaCorrespondencia.prototype.agregarContacto = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var asignacion = {
			'correlativo': ref.correlativo.val(),
			'contacto': ref.corrCopia.val()
		};
		return $.ajax({
			type	:	'GET',
			url		:	'../../../../' + ref.URL_AGREGAR_CONTACTO + "/" + ref.correlativo.val() + "/" + ref.corrCopia.val(),
			cache	:	false,
			//data	:	JSON.stringify(asignacion),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCopiaCorrespondencia.prototype.eliminarContacto = function(usuario){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		:	'../../../../' + ref.URL_ELIMINAR_CONTACTO + "/" + ref.correlativo.val() + "/" + usuario,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	// TICKET 7000003426
	ModuloCopiaCorrespondencia.prototype.enviarCopiaCorrespondencia = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var texto = ref.corrTexto.val();
		console.log("Texto:" + texto);
		//if(texto=="")//{
			//texto = "without{}text";
		//}
		console.log("Texto:" + texto);
		var parametro = {"descripcion": texto };
		return $.ajax({
			type	:	'POST',
			url		:	'../../../../' + ref.URL_ENVIAR_COPIA_CORRESPONDENCIA + "/" + ref.correlativo.val(),
			cache	:	false,
			data	:	JSON.stringify(parametro),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	// FIN TICKET
	
	ModuloCopiaCorrespondencia.prototype.htmlCopiaCorrespondencias = function(usuarios){
		var ref = this;
		var plantillaScript = ref.plantillaCorrespondencias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'usuarios' : usuarios};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCopiaCorrespondencia(SISCORR_APP);
        return object;
    }
    return {
        getInstance: function (SISCORR_APP) {
            if (!instance) {
                instance = createInstance(SISCORR_APP);
                SISCORR_APP.fire('loaded', instance);
            }
            return instance;
        }
    };
})();
