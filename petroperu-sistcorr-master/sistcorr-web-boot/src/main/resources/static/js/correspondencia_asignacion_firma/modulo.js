var MODULO_CORRESPONDENCIA_ASIGNAR_FIRMA = (function(){
	var instance;
	function ModuloCorrespondenciaAsignarFirma(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnGuardar = $("#btnGuardar");
		this.btnRetroceder = $("#btnRetroceder");
		this.btnGuardarAsignacion = $("#btnGuardarAsignacion");
		this.plantillaFlujo = $("#template-flujo");
		this.compModalConfirmarAsignacion = $("#modalConfirmarAsignacion");
		this.compTextoConfirmarAsignacion = $("#textoConfirmarAsignacion");
		this.btnConfirmarAsignacionSi = $("#btnConfirmarAsignacionSi");
		this.tabs = {
			tabDatos: {
				compHeader: $("#Datos-tab"),
				compBody: $("#contenidoDatos"),
				compLugarTrabajo: $("#lugarTrabajo"),
				compDependencia: $("#dependencia"),
				comResponsableFirma: $("#responsableFirma")
			},
			tabFlujo: {
				compHeader: $("#Flujo-tab"),
				compBody: $("#contenidoFlujo"),
				compListaFlujo: $("#listaFlujo")
			}
		};
		this.URL_OBTENER_CORRESPONDENCIA = '../../app/emision/buscar/correspondencia';
		this.URL_BUSCAR_LUGARES = '../../app/emision/buscar/lugar';
		this.URL_BUSCAR_DEPENDENCIAS = '../../app/emision/buscar/dependencia';
		// TICKET 9000003944
		this.URL_BUSCAR_DEPENDENCIAS_UM = '../../app/emision/buscar/dependenciaUM';
		// FIN TICKET
		this.URL_BUSCAR_FUNCIONARIOS = '../../app/emision/buscar/funcionarios';
		this.URL_OBTENER_FIRMANTE = '../../app/emision/obtener/funcionario_jefe';
		this.URL_ASIGNAR_FIRMANTE = '../../app/emision/agregar-firmante';
		this.URL_OBTENER_FLUJO = '../../app/emision/obtener-flujo';
		this.URL_VALIDAR_FIRMANTE = '../../app/emision/validar-firmante';
		this.URL_BANDEJA_FIRMADOS = '../../app/lista-documentos/firmados';
	};
	
	
	ModuloCorrespondenciaAsignarFirma.prototype.obtenerCorrespondencia = function(){
		var ref = this;
		var idCorrespondencia = ref.idCorrespondencia.val();
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_CORRESPONDENCIA + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignarFirma.prototype.obtenerFirmante = function(codDependencia){
		var ref = this;
		if(!codDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_FIRMANTE + '/' + codDependencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignarFirma.prototype.asignarFirmante = function( firmante){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondencia = ref.idCorrespondencia.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_ASIGNAR_FIRMANTE + "/" + idCorrespondencia,
			cache	:	false,
			data	:	JSON.stringify(firmante),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	
	ModuloCorrespondenciaAsignarFirma.prototype.validarFirmante = function(firmante){
		var ref = this;
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondencia = ref.idCorrespondencia.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_VALIDAR_FIRMANTE + "/" + idCorrespondencia,
			cache	:	false,
			data	:	JSON.stringify(firmante),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignarFirma.prototype.obtenerFlujoFirmantes = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondencia = ref.idCorrespondencia.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_FLUJO + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignarFirma.prototype.htmlFlujoFirmantes = function(firmantes){
		var ref = this;
		var plantillaScript = ref.plantillaFlujo.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'firmantes' : firmantes};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaAsignarFirma.prototype.abrirBandejaFirmado = function(){
		var ref = this;
		window.location.href = ref.URL_BANDEJA_FIRMADOS;
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaAsignarFirma(SISCORR_APP);
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