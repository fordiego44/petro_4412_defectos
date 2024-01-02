var MODULO_REEMPLAZO_APOYO = (function(){
	var instance;
	function ModuloReemplazoApoyo(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.comRegApoyo = {
				myModal: $("#modalRegReemApoyo"),
		}
		this.comElimApoyo = {
				myModal : $("#modalElimReemApoyo"),
			}
		this.objReemplazoApoyo = {
				id_reemplazo : 0,
				dependencia : $("#cmbDependReemplazoApoyo"),
				referencia : $("#txtReferenciaApoyo"), 
				usuarioSaliente : $("#cmbJefeDependencia"),
				usuarioEntrante: $("#cmbFuncApoyo"),
				fechaReemplazoInicio : $("#txtFechaApoyoDesde"),
				fechaReemplazoFin: $("#txtFechaApoyoHasta"),
				referencia : $("#txtReferencia"),
				accion :""
		};
		this.btnModificarReemplazo = $(".btnModificarReemplazo");
		this.btnEliminarReemplazo = $(".btnEliminarReemplazo");
		this.btnEliminarReemplazoApoyo = $("#btnEliminarReemplazoApoyo");
		
		this.btnAceptarConfirmacion = $("#btnAceptarConfirmacion");
		this.btnPreAceptarBorrarReemplazosApoyo = $("#btnPreAceptarBorrarReemplazosApoyo");
		this.btnAceptarBorrarReemplazosApoyo = $("#btnAceptarBorrarReemplazosApoyo");
		
		this.comPopUpConfirmacion = $("#modalPopUpConfirmacion");
		this.comPopUpListaReemplazosApoyo = $("#modalListaReemplazosApoyo");
		this.comPopUpConfirmacionBorrarReemplazosApoyo = $("#modalConfirmacionBorrarReemplazosApoyo");
		
		this.componentesJG = {
			btnExportExcel: $("#btnExportarExcelJG"),
			btnFiltrosJG: $("#btnFiltrosJG"),
			dataTableRA: $("#tablaReemplazosApoyo"),
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesdeJG"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),		
			/*Reemplazo GG*/
			btnRegistrarApoyo: $("#btnRegistrarApoyo"),			
			btnBuscar: $("#btnBuscarFiltroGG"),
			cmbDependenciaFiltroGG :$("#cmbDependenciaFiltroGG"),
			cmbJefeDependenciaFiltroGG:$("#cmbJefeDependenciaFiltroGG"),
			cmbFuncionarioApoyoFiltroGG:$("#cmbFuncionarioApoyoFiltroGG"),
			txtFecInicioGGFiltro:$("#txtFecInicioGGFiltro"),
			btnFechaInicioFiltro:$("#btnFechaInicioFiltro"),
			txtFecFinGGFiltro:$("#txtFecFinGGFiltro"),
			btnFechaFinFiltro:$("#btnFechaFinFiltro"),
			btnResetear: $("#btnResetearGG"),
			/*Reemplazo GG - Ventana Registro*/
			txtFechaApoyoDesde:$("#txtFechaApoyoDesde"),
			btnFechaApoyoDesde:$("#btnFechaApoyoDesde"),
			txtFechaApoyoHasta:$("#txtFechaApoyoHasta"),
			btnFechaApoyoHasta:$("#btnFechaApoyoHasta"),
			cmbDependReemplazoApoyo:$("#cmbDependReemplazoApoyo"),
			cmbJefeDependencia:$("#cmbJefeDependencia"),
			cmbFuncApoyo:$("#cmbFuncApoyo"),
			btnGuardarApoyo:$("#btnGuardarApoyo"),
			
		};
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		/*Reemplazo GG*/
		this.URL_FILTRO_CMBDEPENDENCIAS_GG = '../app/buscar/filtroDependenciaGG';
		this.URL_FILTRO_JEFE_X_DEPENDENCIA='../app/buscar/funcionarioReemplazado';
		this.URL_FILTRO_FUNCIONARIO_APOYO='../app/buscar/funcionarioReemplazar';
		this.URL_CONSULTA_REEMPLAZO_APOYO= '../app/consultar-reemplazo-apoyo';
		this.URL_VALIDA_REEMPLAZO_ADICION='../app/validarReemplazo';
		this.URL_ELIMINAR_REEMPLAZO = '../app/eliminarReemplazo';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION = '../app/buscar/adminReemplazoApoyo';
		this.EXPORT_EXCEL = '../app/exportar-reemplazo-apoyo';
		
		this.URL_ADMINISTRA_REEMPLAZO_APOYO = '../app/grabarReemplazoApoyoSinConfirmar';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_1 = '../app/grabarReemplazoAdicionConConfirmacion1';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_2 = '../app/grabarReemplazoAdicionConConfirmacion2';
		this.URL_ELIMINAR_REEMPLAZOS_CONFIR_4 = '../app/eliminarRemplazosYactualizarValorVarConConfirmacion4';
		this.URL_BUSCAR_REEMPLAZOS_EN_ADICION = '../app/listarReemplazosAdicion';
		
	}
	
	ModuloReemplazoApoyo.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloReemplazoApoyo.prototype.eliminarReemplazoApoyo = function(
			parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_ELIMINAR_REEMPLAZO,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazoApoyo.prototype.validarReemplazo=function(parametros){
		var ref=this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_VALIDA_REEMPLAZO_ADICION,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
		
	};
	
	ModuloReemplazoApoyo.prototype.grabarReemplazoApoyoSinConfirmar = function(parametros){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_ADMINISTRA_REEMPLAZO_APOYO,
			cache	:	false,
			data    : JSON.stringify(parametros),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloReemplazoApoyo.prototype.grabarReemplazoApoyoConConfirmacion1 = function(
			parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_1,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazoApoyo.prototype.grabarReemplazoApoyoConConfirmacion2 = function(
			parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_2,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazoApoyo.prototype.eliminarRemplazosYactualizarValorVarConConfirmacion4 = function(
			parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_ELIMINAR_REEMPLAZOS_CONFIR_4,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazoApoyo.prototype.buscarReemplazosEnApoyo = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_BUSCAR_REEMPLAZOS_EN_ADICION,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	}
	
	ModuloReemplazoApoyo.prototype.consultar = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA_JEFE_GESTOR,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloReemplazoApoyo.prototype.listarDependencias = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({					
			url: ref.URL_LISTAR_DEPENDENCIAS_TODOS_JEFE_GESTOR,
			type: 'GET',
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }/*,
			processResults: function (respuesta) {				
				ref.listas.dependencias.agregarLista(respuesta.datos);
				return {results: respuesta.datos};
			}*/
		});
	};
	
	ModuloReemplazoApoyo.prototype.exportarExcel = function(filtro){
		//debugger;
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
        var object = new ModuloReemplazoApoyo(SISCORR_APP);
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