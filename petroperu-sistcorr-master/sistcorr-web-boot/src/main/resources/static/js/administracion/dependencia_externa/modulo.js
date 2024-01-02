var MODULO_MANTENIMIENTO_DEPENDENCIA_EXTERNA = (function(){
	var instance;
	function ModuloMantenimientoDependenciaExterna(SISCORR_APP){
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
			dataTableConsulta: $("#tablaDependenciaExterna"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		
		//this.lblFiltroEstado = $("#lblFiltroEstado");
		this.lblFiltroRuc = $("#lblFiltroRuc");
		this.lblFiltroNombre = $("#lblFiltroNombre");
		this.filtroBusqueda = {
			 filtroNombre: $("#textFiltroNombre"),
			 filtroRuc: $("#textFiltroRuc")
		};
		this.compModalDatos = {
				id : 0,
				ruc					: $("#textRuc"),
				nombreDependencia	: $("#textNombre"),
				direccion          	: $("#textDireccion"),
				email     			: $("#textEmail"),
				codigoDepartamento	: $("#cmbDepartamento"),
				codigoProvincia		: $("#cmbProvincia"),
				codigoDistrito		: $("#cmbDistrito"),
				codigoPais			: $("#cmbPais"),
				codigoCiudad		: $("#cmbCiudad"),
				accion: ""
		
		};
		this.btnRegistrar = $("#btnRegistrar");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.EXPORT_EXCEL = '../configuracion/consultarExcelDependenciaExterna';
		this.URL_BUSCAR_DEPENDENCIA_EXTERNA = '../configuracion/consultarDependenciaExterna';
		this.URL_CRUD_MANTENIMIENTO = '../configuracion/crudDependenciaExterna';
		
		this.URL_CMB_DEPARTAMENTO = '../../app/emision/buscar/departamento';
		this.URL_BUSCAR_PROVINCIAS = '../../app/emision/buscar/provincia';
		this.URL_BUSCAR_DISTRITOS = '../../app/emision/buscar/distrito';
		this.URL_BUSCAR_CIUDADES = '../configuracion/buscar/ciudad';
		
	};
	
	
	
	ModuloMantenimientoDependenciaExterna.prototype.crudLugarTrabajo = function(parametros){
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
	
			
	ModuloMantenimientoDependenciaExterna.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloMantenimientoDependenciaExterna(SISCORR_APP);
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
