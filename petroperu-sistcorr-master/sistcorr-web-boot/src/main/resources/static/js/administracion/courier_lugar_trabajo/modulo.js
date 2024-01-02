var MODULO_MANTENIMIENTO_COURIER_LUGAR_TRABAJO = (function(){
	var instance;
	function ModuloMantenimientoCourierLugarTrabajo(SISCORR_APP){
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
			dataTableConsulta: $("#tablaCourierLugarTrabajo"),
			btnBuscar: $("#btnBuscar"),
			btnLimpiar: $("#btnLimpiar")
		};
		
		//this.lblFiltroEstado = $("#lblFiltroEstado");
		this.lblFiltroNombre = $("#lblFiltroNombre");
		this.filtroBusqueda = {
			 filtroCourier: $("#cmbFiltroCourier"),
			 filtroCodigoCGC  : $("#cmbFiltroCGC"),
		};
		this.compModalDatos = {
				id : 0,
				codigoCGC: $("#cmbCGC"),
				codigoCourier : $("#cmbCourier"),
				alcance : $("#cmbAlcance"),
				accion   : ""
		
		};
		this.btnRegistrar = $("#btnRegistrar");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.EXPORT_EXCEL = '../configuracion/consultarExcelCourierLugarTrabajo';

		this.URL_BUSCAR_COURIER_LUGAR_TRABAJO = '../configuracion/consultarCourierLugarTrabajo';
		this.URL_CRUD_MANTENIMIENTO = '../configuracion/crudCourierLugarTrabajo';
		
	};
		
	ModuloMantenimientoCourierLugarTrabajo.prototype.crudCourierLugarTrabajo = function(parametros){
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
	
			
	ModuloMantenimientoCourierLugarTrabajo.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloMantenimientoCourierLugarTrabajo(SISCORR_APP);
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
