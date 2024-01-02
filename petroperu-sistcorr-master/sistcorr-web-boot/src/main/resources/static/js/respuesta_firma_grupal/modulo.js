var MODULO_RESPUESTA_FIRMA = (function(){
	var instance;
	function ModuloRespuestaFirma(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#idCorrespondencia");
		this.estadoCorrespondencia = $("#estadoCorrespondencia");
		this.rutaAprobacion = $("#rutaAprobacion");
		this.cantidadDestinatarios = $("#cantidadDestinatarios");
		this.cantidadDestinatariosInternos = $("#cantidadDestinatariosInternos");
		this.modalEnviarCorrespondencia = $("#modalEnviarCorrespondencia");
		//this.modalAsignarSiguienteFirmante = $("#modalAsignarSiguienteFirmante");
		this.btnEnviarCorrespondencia = $("#btnEnviarCorrespondencia");
		//this.btnAsignarSiguienteFirmante = $("#btnAsignarSiguienteFirmante");
		this.btnRegresarBandeja = $("#btnRegresarBandeja");
		this.URL_BANDEJA = '../../app/lista-documentos/firmados';
		this.URL_ENVIAR_CORRESPONDENCIA_GRUPAL = '../../app/emision/enviar-correspondencia-grupal';
		this.URL_PROCESAR_EXISTENCIA_ENLACE_GRUPAL = '../../app/emision/procesar-existencia-enlace-grupal';
		
		this.idCorrespondenciaPrc = $("#idsCorrespondenciasPrc");
		this.idCorrespondenciaEnv = $("#idsCorrespondenciasEnv");
		this.idCorrespondenciaErr = $("#idsCorrespondenciasErr");
		this.msjCorrespondenciasErr = $("#msjCorrespondenciasErr");
	}
	
	ModuloRespuestaFirma.prototype.regresarBandeja = function(){
		var ref = this;
		window.location.href = ref.URL_BANDEJA;
	};
	
	ModuloRespuestaFirma.prototype.enviarCorrespondenciaGrupal = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondencia = ref.idCorrespondenciaEnv.val();
		return $.ajax({
			type	:	'PUT',
			url		: 	ref.URL_ENVIAR_CORRESPONDENCIA_GRUPAL + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloRespuestaFirma.prototype.procesarExistenciaEnlaceGrupal = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondencia = ref.idCorrespondenciaPrc.val();
		return $.ajax({
			type	:	'PUT',
			url		: 	ref.URL_PROCESAR_EXISTENCIA_ENLACE_GRUPAL + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloRespuestaFirma(SISCORR_APP);
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