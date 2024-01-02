var MODULO_DEPENDENCIA_UNIDAD = (function(){
	var instance;
	function ModuloDependenciaUnidad(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.btnRegistrarDependencia = $("#btnRegistrarDependencia");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_REGISTRAR_DEPENDENCIA = "app/registrarDependencia";
		this.URL_LISTAR_DEPENDENCIAS = "app/adminEquipos";
		this.btnBuscarIntegrante = $("#btnBuscarIntegrante");
		this.modalAgregarIntegrante = $("#modalAgregarIntegrante");
		this.modalConfirmarRegistro = $("#modalConfirmarRegistro");
		this.btnConfirmarRegistro = $("#btnConfirmarRegistro");
		//this.registroIntegrante = $("#integrante");
		//this.btnGuardarIntegrante = $("#btnGuardarIntegrante");
		this.dataTable = $("#tablaIntegrantes");
		this.btnGuardarDependencia = $("#btnGuardarDependencia");
		this.btnCancelarDependencia = $("#btnCancelarDependencia");
		this.formulario = {
			idDependenciaUnidadMatricial: $("#idDependenciaUnidadMatricial"),
			codigoDependenciaInicial: $("#codigoDependencia"),
			tipo: $("#cbmTipo"),
			tipoUnidadMatricial: $("#cbmTipoUnidadMatricial"),
			codigo: $("#cbmCodigo"),
			siglas: $("#cbmSiglas"),
			nombre: $("#cbmNombre"),
			estado: $("#cbmEstado"),
			jerarquia: $("#cbmJerarquia"),
			jefe: $("#cbmJefe"),
			lugarTrabajo: $("#cbmLugarTrabajo"),
			dependenciaSuperior: $("#cbmDependenciaSuperior"),
			descripcionCargo: $("#cbmDescripcionCargo"),
			sinJefe: $("#dep_sinJefe")//ticket 9000004410
		};
		this.formularioIntegrante = {
			registroIntegrante: $("#integrante"),
			btnGuardarIntegrante: $("#btnGuardarIntegrante")
		};
		this.URL_GUARDAR_DEPENDENCIA = "../app/guardarDependencia";
		this.URL_LISTAR_DEPENDENCIAS = "app/adminEquipos";
		this.URL_OBTENER_DEPENDENCIA = "app/obtenerDependencia";
		this.URL_OBTENER_INTEGRANTES = "app/obtenerIntegrantes";
		this.URL_VERIFICAR_SIGLAS = "app/verificarSiglas";
		this.URL_VERIFICAR_DATOS = "app/verificarDatos";
		
		this.URL_CONSULTA = '../app/emision/consultar-bandeja-salida';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS = '../app/emision/buscar/dependencia-usuario';
		this.URL_LISTAR_DEPENDENCIAS_REMITENTES = '../app/emision/buscar/dependencias-superiores';
		this.EXPORT_EXCEL = '../app/emision/consultar-bandeja-salida-excel';
		this.URL_LISTAR_ORIGINADORES = '../app/emision/buscar/todos-funcionarios';
		this.URL_LISTAR_TODAS_DEPENDENCIAS = '../app/emision/buscar/todos-dependencias';
		this.URL_LISTAR_TODAS_DEPENDENCIAS_EXT = '../app/emision/buscar/todos-dependencias-externas';
		
		
		this.URL_LISTAR_DEPENDENCIAS_TODOS = '../app/emision/buscar/dependencias-todas';
		
	}
	
	ModuloDependenciaUnidad.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloDependenciaUnidad.prototype.consultar = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.irAEliminar = function(id){
		var ref = this;
		var url = window.location.origin;
		/*var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		url = url + "/app/ver-detalle/" + id;
		window.location.replace(url);*/
	};
	
	ModuloDependenciaUnidad.prototype.exportarExcel = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.EXPORT_EXCEL,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			xhrFields:{
                responseType: 'blob'
            },
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.guardarDependencia = function(dependencia){
		console.log("Guardando dependencia");
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var url = ref.URL_GUARDAR_DEPENDENCIA
		if(dependencia.idDependenciaUnidadMatricial != "0"){
			url = "../../" + url;
		}
		
		return $.ajax({
			type	:	'POST',
			url		:	url,
			cache	:	false,
			data	:	JSON.stringify(dependencia),
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.obtenerDependencia = function(codigo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		:	'../../../' + ref.URL_OBTENER_DEPENDENCIA + '/' + codigo,
			cache	:	false,
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.obtenerIntegrantes = function(codigo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		:	'../../../' + ref.URL_OBTENER_INTEGRANTES + '/' + codigo,
			cache	:	false,
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.verificarSiglas = function(id, siglas){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var url = "";
		if(id!="0"){
			url = '../../../' + ref.URL_VERIFICAR_SIGLAS;
		}else{
			url = '../' + ref.URL_VERIFICAR_SIGLAS;
		}
		console.log("URL:" + url);
		return $.ajax({
			type	:	'GET',
			url		:	url + '/' + siglas,
			cache	:	false,
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloDependenciaUnidad.prototype.verificarDatos = function(id, codigo, siglas){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var url = "";
		if(id!="0"){
			url = '../../../' + ref.URL_VERIFICAR_DATOS;
		}else{
			url = '../' + ref.URL_VERIFICAR_DATOS;
		}
		console.log("URL:" + url);
		return $.ajax({
			type	:	'GET',
			url		:	url + '/' + id + '/' + codigo + '/' + siglas,
			cache	:	false,
			beforeSend: function(xhr) {
				//xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloDependenciaUnidad (SISCORR_APP);
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