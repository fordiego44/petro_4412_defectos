var MODULO_MANTENIMIENTO_LUGAR_TRABAJO = (function(){
	var instance;
	function ModuloMantenimientoLugarTrabajo(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'SJ';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");

		this.compSinResultados = $("#sinResultados");

		
		this.btnGrillaEditar = $(".btnGrillaEditar");
		this.btnGrillaEliminar = $(".btnGrillaEliminar");
		
		this.btnEliminarRegistro = $("#btnEliminar")
		this.btnGrabarRegistro = $("#btnGrabar");
			
		this.compModalEliminar = $("#modalEliminarDatos");
		this.compModalActualizar = $("#modalActualizarDatos");
	
		this.compCerrarSession = $(".closeSession");
		this.btnRetroceder = $("#btnRetroceder");

		this.compBusqueda = {
			btnExportExcel: $("#btnExportarExcel"),
			btnMasFiltros: $("#btnMasFiltros"),
			btnMenosFiltros: $("#btnMenosFiltros"),
			btnFiltros: $("#btnFiltros"),
			dataTableConsulta: $("#tablaTipoAccion"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		
		this.lblFiltroNombre = $("#lblFiltroNombre");
		this.filtroBusqueda = {
			 filtroNombre: $("#textFiltroNombre")
		};
		this.compModalDatos = {
				id : 0,
				codigoAccion: $("#textCodigo"),
				nombreAccion: $("#textNombre"),
				mTextoReq: $("#cmbTextReq"),
				mMultipli: $("#cmbMultiple"),
				mReqTextoRta: $("#cmbMarcaReq"),
				mEnviaMailRta: $("#cmbMarcaEmailRta"),
				accion: ""
		
		};
		this.btnRegistrar = $("#btnRegistrar");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.EXPORT_EXCEL = '../configuracion/consultarExcelTipoAccion';
		this.URL_BUSCAR_LUGAR_TRABAJO = '../configuracion/consultarTipoAccion';
		this.URL_CRUD_MANTENIMIENTO = '../configuracion/crudTipoAccion';
		
		
	};
	
	
	
	ModuloMantenimientoLugarTrabajo.prototype.crudLugarTrabajo = function(parametros){
		var ref = this;
		//var parametros = {"id": filtro.id.valueOf()};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_CRUD_MANTENIMIENTO,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
			
	ModuloMantenimientoLugarTrabajo.prototype.exportarExcel = function(filtro){
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
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloMantenimientoLugarTrabajo(SISCORR_APP);
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
