var MODULO_COMPARTIR_CORRESPONDENCIA = (function(){
	var instance;
	function ModuloCompartirCorrespondencia(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Winston';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnRetroceder = $("#btnRetroceder");
		this.btnCompartir = $("#btnCompartir");
		this.btnCompartirFooter = $("#btnCompartirFooter");
		this.mdlCompartir = $("#modalCompartirCorrespondencia");
		this.mdlCancelar = $("#modalCancelarCompartir");
		this.btnCompartirSi = $("#btnCompartirSi");
		this.destinatario = $("#destinatarioCompartir");
		this.copia = $("#copiaCompartir");
		this.asunto = $("#asuntoCompartir");
		this.contenido = $("#contenidoCompartir");
		this.correlativo = $("#correlativo");
		this.idCorrespondencia = $("#idCorrespondencia");
		this.ids = $("#ids");
		this.modoCompartir = $("#modoCompartir");
		this.URL_OBTENER_ARCHIVOS = "../../../obtener-archivos";
		this.URL_COMPARTIR_CORRESPONDENCIA = "../../../compartir-correspondencia";
		this.archivos = null;
	};
	
	ModuloCompartirCorrespondencia.prototype.compartirCorrespondencia = function(){
		var ref = this;
		var correspondenciaCompartida = {
			"idCorrespondencia": ref.idCorrespondencia.val(),
			"correlativo": ref.correlativo.val(), 
			"destinatarios": ref.destinatario.val(),
			"copias": ref.copia.val(),
			"asunto": ref.asunto.val(),
			"contenido": ref.contenido.val(),
			"modoCompartido": ref.modoCompartir.val()
		}
		var data = [];
		console.log("Archivos:");
		console.log(ref.archivos);
		for (var i = 0;i<ref.archivos.length;i++){
			var archivo = ref.archivos[i];
			data.push(archivo);
		}
		var historial_archivos = {"correspondencia": correspondenciaCompartida, "archivos": data};
		console.log("Historial Archivos:");
		console.log(historial_archivos);
		/*var parametros = {"correlativo": filtro.correlativo.val(), "codigoEstado": filtro.codigoEstado.val(), "fechaRegistroDesde": filtro.fechaRegistroDesde.val(), 
				"fechaRegistroHasta": filtro.fechaRegistroHasta.val(), "numeroDocumentoInterno": filtro.numeroDocumentoInterno.val(), 
				"fechaDocumentoInterno": filtro.fechaDocumentoInterno.val(), "codigoDependenciaRemitente": filtro.codigoDependenciaRemitente.val(), 
				"codigoDependenciaDestino": filtro.codigoDependenciaDestino.val(), "codigoTipoCorrespondencia": filtro.codigoTipoCorrespondencia.val(), 
				"nombreDependenciaExterna": filtro.nombreDependenciaExterna.val(), "guiaRemision": filtro.guiaRemision.val(), "asunto": filtro.asunto.val()};*/
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_COMPARTIR_CORRESPONDENCIA,
			cache		:	false,
			data		:	JSON.stringify(historial_archivos),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCompartirCorrespondencia.prototype.obtenerArchivos = function(){
		var ref = this;
		var ids = ref.ids.val();
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_ARCHIVOS + '/' + ids,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCompartirCorrespondencia(SISCORR_APP);
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