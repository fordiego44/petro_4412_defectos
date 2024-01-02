var MODULO_ADMIN_EQUIPOS = (function(){
	var instance;
	function ModuloAdminEquipos(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.btnRegistrarDependencia = $("#btnRegistrarDependencia");
		this.btnExportExcel = $("#btnExportarExcel");
		this.btnBuscar= $("#btnBuscar");
		this.btnLimpiar= $("#btnLimpiar");
		this.dataTable = $("#tablaDependencias");
		
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_REGISTRAR_DEPENDENCIA = "app/registrarDependencia";
		this.URL_BUSCAR_DEPENDENCIAS = "../app/buscarDependencias";
		this.URL_EDITAR_DEPENDENCIAS = "app/modificarDependencia";
		this.URL_ELIMINAR_DEPENDENCIAS = "app/eliminarDependencia";
		this.URL_EXPORTAR_EXCEL = "../app/buscarDependenciasExcel";
		
		this.formulario = {
			txtCodigo: $("#cbmCodigo"),
			txtNombre: $("#cbmNombre"),
			cmbTipo: $("#cbmTipo"),
			cmbJefe: $("#cmbJefe"),
		};
		
		
		this.componentes = {
			btnExportExcel: $("#btnExportarExcel"),
			btnFiltros: $("#btnFiltros"),
			dataTable: $("#tablaCorrespondencias"),
			cboxConsiderarDepenOriginadora: $("#cboxConsiderarDepenOriginadora"),
			cmbDependenciaOriginadora: $("#cmbDependenciaOriginadora"),
			cmbDependenciaRemitente: $("#cmbDependenciaRemitente"),
			txtCorrelativo: $("#txtCorrelativo"),
			txtAsunto: $("#txtAsunto"),
			cbmEstado: $("#cbmEstado"),
			btnBuscar: $("#btnBuscar"),
			btnResetear: $("#btnResetear"),
			btnMasFiltros: $("#btnMasFiltros"),
			btnMenosFiltros: $("#btnMenosFiltros"),
			filtrosSecundarios: $(".filtro_secundario"),
			cmbNombreOriginador: $("#cmbNombreOriginador"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesde"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHasta"),
			btnFechaHasta: $("#btnFechaHasta"),
			cbmTipoCorrespondencia: $("#cbmTipoCorrespondencia"),
			cbmTipoEmision: $("#cbmTipoEmision"),
			rbtnTipoDestinatario: $("input:radio[name='rbtnTipoDestinatario']:checked"),
			cbmFlujoFirma: $("#cbmFlujoFirma"),
			cbmConfidencialidad: $("#cbmConfidencialidad"),
			cbmUrgente: $("#cbmUrgente"),
			cbmDespachoFisico: $("#cbmDespachoFisico"),
			cbmDependenciaDestinatariaInterno: $("#cbmDependenciaDestinatariaInterno"),
			cbmDependenciaDestinatariaExternaNacional : $("#cbmDependenciaDestinatariaExternaNacional"),
			txtDependenciasDestinatariaExternaInternacional : $("#txtDependenciasDestinatariaExterna"),
			txtDependenciasDestinatariaExternaNacional : $("#txtDependenciasDestinatariaExternaNacional"),
			cbmDependenciaCopia: $("#cbmDependenciaCopia")
		};
		this.URL_CONSULTA = '../app/emision/consultar-bandeja-salida';
		this.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS = '../app/emision/buscar/dependencia-usuario';
		this.URL_LISTAR_DEPENDENCIAS_REMITENTES = '../app/emision/buscar/dependencias-superiores';
		this.EXPORT_EXCEL = '../app/emision/consultar-bandeja-salida-excel';
		this.URL_LISTAR_ORIGINADORES = '../app/emision/buscar/todos-funcionarios';
		this.URL_LISTAR_TODAS_DEPENDENCIAS = '../app/emision/buscar/todos-dependencias';
		this.URL_LISTAR_TODAS_DEPENDENCIAS_EXT = '../app/emision/buscar/todos-dependencias-externas';
		
		
		this.URL_LISTAR_DEPENDENCIAS_TODOS = '../app/emision/buscar/dependencias-todas';
		
	}
	
	ModuloAdminEquipos.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloAdminEquipos.prototype.buscarDependencias = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_BUSCAR_DEPENDENCIAS,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	}
	
	ModuloAdminEquipos.prototype.exportarExcel = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_EXPORTAR_EXCEL,
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
	
	ModuloAdminEquipos.prototype.consultar = function(filtro){
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
	
	ModuloAdminEquipos.prototype.irAEditar = function(id, codigo){
		var ref = this;
		/*var url = window.location.origin;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		url = url + "/app/modificarDependencia/" + id;
		console.log("URL:" + url);
		window.location.replace(url);*/
		window.location.replace("../" + ref.URL_EDITAR_DEPENDENCIAS + "/" + id + "/" + codigo);
	};
	
	/*ModuloAdminEquipos.prototype.exportarExcel = function(filtro){
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
	};*/
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloAdminEquipos (SISCORR_APP);
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