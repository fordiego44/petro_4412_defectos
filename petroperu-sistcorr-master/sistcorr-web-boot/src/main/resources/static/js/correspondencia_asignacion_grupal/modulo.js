var MODULO_CORRESPONDENCIA_ASIGNACION_GRUPAL = (function(){
	var instance;
	function ModuloCorrespondenciaAsignacionGrupal(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.wobNum = $("#wobNum");
		this.correlativo = $("#correlativo");
		this.wobNum = $("#wobNum");
		this.tipoBandeja = $("#tipoBandeja");
		this.VISTA = {
				btnRetroceder			: $("#btnRetroceder"),
				form: {
					compAccion			: $("#compAccion"),
					compAquienSeDirige	: $("#compAquienSeDirige"),
					compFechaTope		: $("#compFechaTope"),
					compDetalleReq		: $("#compDetalleReq"),
					compBtnFechaTope		: $("#btnFechaTope")
				},
				opciones: {
					btnAsignar			: $("#btnAsignar"),
					opcAsignar			: $("#opcion_asignar"),
					btnEnviar			: $("#btnEnviar"),
					btnEnviarAD			: $("#btnEnviarAD"),//ticket 9000004807
					btnEnviarAsignaciones: $("#btnEnviarAsignacionGrupal"),
					btnEnviarAsigDatos	: $("#btnEnviarss")
				},
				compAsignaciones		: $("#listaAsignaciones"),
				compSinAsignaciones		: $("#listaAsignacionesVacia"),
				compPlantillaAsignaciones : $("#template-asignaciones"),
				compOpcionesListaAsignaciones: $("#opcionesListaAsignaciones"),
		};
		this.compModalBorrarAsignacion	= $("#modalBorraAsignacion");
		this.compBtnBorrarAsignacion	= $("#btnBorrarAsignacionSi");
		this.compEliminarAsignacion = "btnEliminarAsignacion";
		this.URL_FUNCIONARIOS = 'app/correspondencias/funcionarios';
		this.URL_TEMPORAL_ASIGNACION_GRUPAL = 'app/correspondencias/asignacionGrupal/temporal';
		this.URL_AGREGAR_ASIGNACION_GRUPAL = 'app/correspondencias/asignacionGrupal/agregar';
		this.URL_ENVIAR_ASIGNACION_GRUPAL = 'app/correspondencias/asignacionGrupal/enviar';
		this.URL_ELIMINAR_ASIGNACION_GRUPAL = 'app/correspondencias/asignacionGrupal/eliminar';
	}
	
	
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.recuperarFuncionarios = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var wobNum = ref.wobNum.val();
		var urll = '../../../' + ref.URL_FUNCIONARIOS + "/" + wobNum;
		return $.ajax({
			type	:	'GET',
			url		:	'../../../' + ref.URL_FUNCIONARIOS + "/" + wobNum,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.recuperarTemporalAsignaciones = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativo = ref.correlativo.val();
		return $.ajax({
			type	:	'GET',
			url		:	'../../../' + ref.URL_TEMPORAL_ASIGNACION_GRUPAL,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.agregarAsignacion = function(accion, responsable, detalle, fechaTope, idPadre){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var asignacion = {
			'correlativo': ref.correlativo.val(),
			'codigoAccion': accion,
			'usuarioAsignado': responsable,
			'detalleSolicitud': detalle,
			'fechaLimite': fechaTope,
			'idPadre': idPadre
		};
		return $.ajax({
			type	:	'POST',
			url		:	'../../../' + ref.URL_AGREGAR_ASIGNACION_GRUPAL,
			cache	:	false,
			data	:	JSON.stringify(asignacion),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.enviarAsignaciones = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var correlativo = ref.correlativo.val();
		var wobNum = ref.wobNum.val();
		return $.ajax({
			type		:	'POST',
			url			:	'../../../' + ref.URL_ENVIAR_ASIGNACION_GRUPAL,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.eliminarAsignacion = function(asignacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'DELETE',
			url			:	'../../../' + ref.URL_ELIMINAR_ASIGNACION_GRUPAL + "/" + asignacion.idAsignacion,
			cache		:	false,
			beforeSend	: 	function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	
	
	ModuloCorrespondenciaAsignacionGrupal.prototype.htmlTemporalAsignaciones = function(asignaciones){
		var ref = this;
		var plantillaScript = ref.VISTA.compPlantillaAsignaciones.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'asignaciones' : asignaciones};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaAsignacionGrupal(SISCORR_APP);
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