var MODULO_REEMPLAZOS_TOTAL = (function() {
	var instance;
	function ModuloReemplazosTotal(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.compBtnRegistrarTotal= $("#btnRegistrarTotal");
		this.btnGuardarTotal=$("#btnGuardarTotal");
		this.btnEliminarReemplazo = $(".btnEliminarReemplazo");
		this.btnEliminarReemplazoTotal = $("#btnEliminarReemplazoTotal");
		this.btnModificarReemplazo = $(".btnModificarReemplazo");
		
		this.btnAceptarConfirmacion = $("#btnAceptarConfirmacion");
		this.btnAceptarMensajeRolMenorReemplazo = $("#btnAceptarMensajeRolMenorReemplazo");
		this.btnPreAceptarBorrarReemplazosTotal = $("#btnPreAceptarBorrarReemplazosTotal");
		this.btnAceptarBorrarReemplazosTotal = $("#btnAceptarBorrarReemplazosTotal");
		this.btnAceptarMensajeEliminarReemplazosAdicionAgregarTotal = $("#btnAceptarMensajeEliminarReemplazosAdicionAgregarTotal");
		
		this.comRegTotal = {
				myModal: $("#modalRegReemTotal"),
				cmbDependenciaReempTotal : $("#cmbDependenciaReempTotal"),
				cmbRolReempTotal : $("#cmbRolReempTotal"),
				cmbFuncionarioReempTotal : $("#cmbFuncionarioReempTotal"),
				cmbFuncReempTotal : $("#cmbFuncReempTotal"),
		}
		
		this.comPopUpConfirmacion = $("#modalPopUpConfirmacion");
		this.comPopUpMensajeRolMenorReemplazo = $("#modalMensajeRolMenorReemplazo");
		this.comPopUpMensajeEliminarReemplazosAdicionAgregarTotal = $("#modalMensajeEliminarReemplazosAdicionAgregarTotal");
		this.comPopUpListaReemplazosTotal = $("#modalListaReemplazosTotal");
		this.comPopUpConfirmacionBorrarReemplazosTotal = $("#modalConfirmacionBorrarReemplazosTotal");
		
		this.comElimTotal = {
				myModal : $("#modalElimReemTotal"),
		}
		this.objReemplazoTotal = {
				id_reemplazo : 0,
				dependencia : $("#cmbDependenciaReempTotal"),
				referencia : $("#txtReferencia"),
				rol : $("#cmbRolReempTotal"),
				usuarioSaliente : $("#cmbFuncionarioReempTotal"),
				usuarioEntrante : $("#cmbFuncReempTotal"),
				fechaReemplazoInicio : $("#txtFechaReemplazoTotalInicio"),
				fechaReemplazoFin : $("#txtFechaReemplazoTotalHasta"),
				referencia : $("#txtReferencia"),
				accion :""
		};
		this.componentesJG = {
			btnExportExcel: $("#btnExportarExcelJG"),
			btnFiltrosJG: $("#btnFiltrosJG"),
			dataTable: $("#tablaCorrespondenciasJG"),
			dataTableRA: $("#tablaReemplazosTotal"),
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesdeJG"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),
			/*REEMPLAZOS TOTAL*/
			btnBuscar: $("#btnBuscarJG"),
			btnResetear: $("#btnResetearJG"),
			cmbUsuarioSaliente: $("#cmbUsuarioSaliente"),
			cmbUsuarioEntrante: $("#cmbUsuarioEntrante"),
			cmbRol: $("#cmbRol"),
			cmbDependencia: $("#cmbDependencia"),
			cmbFuncionarioReempTotal: $("#cmbFuncionarioReempTotal"),
			cmbFuncReempTotal: $("#cmbFuncReempTotal"),
			txtFechaReemplazoTotalInicio: $("#txtFechaReemplazoTotalInicio"),
			txtFechaReemplazoTotalHasta: $("#txtFechaReemplazoTotalHasta"),
			btnFechaDesdeTotal: $("#btnFechaDesdeTotal"),
			btnFechaHastTotal: $("#btnFechaHastTotal"),
			
			
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		/*Reemplazos Total*/
		this.URL_FILTRO_CMBDEPENDENCIAS_GG = '../app/buscar/filtroDependenciaGG';
		this.URL_FILTRO_JEFE_X_DEPENDENCIA='../app/buscar/funcionarioReemplazado';
		this.URL_FILTRO_FUNCIONARIO_APOYO='../app/buscar/funcionarioReemplazar';
		this.URL_CONSULTA_REEMPLAZO_TOTAL= '../app/consultar-reemplazo-total';
		this.URL_BUSCAR_ROL_X_DEPENDENCIA = '../app/buscar/rolAdicionReemplazo';
		this.URL_BUSCAR_DEPENDENCIAS_TOTAL = '../app/buscar/dependenciaAdicion';
		this.EXPORT_EXCEL = '../app/exportar-reemplazo-total';
		this.URL_OBTENER_VALOR_VAR ='../app/obtener_valor_var';
		this.URL_VALIDA_REEMPLAZO_ADICION = '../app/validarReemplazo';
		this.URL_ELIMINAR_REEMPLAZO = '../app/eliminarReemplazo';
		this.URL_ADMINISTRA_REEMPLAZO_TOTAL = '../app/grabarReemplazoTotalSinConfirmar';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_1 = '../app/grabarReemplazoAdicionConConfirmacion1';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_2 = '../app/grabarReemplazoAdicionConConfirmacion2';
		this.URL_ELIMINAR_REEMPLAZOS_CONFIR_4 = '../app/eliminarRemplazosYactualizarValorVarConConfirmacion4';
		this.URL_VALIDAR_USUARIO_MEJOR_CARGO = '../app/validarSiUsuarioEsDeMejorCargo';
		this.URL_BUSCAR_REEMPLAZOS_EN_ADICION = '../app/listarReemplazosAdicion';
	}
	
	ModuloReemplazosTotal.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloReemplazosTotal.prototype.consultar = function(filtro){
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
	
	ModuloReemplazosTotal.prototype.grabarReemplazoTotalSinConfirmar = function(parametros){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_ADMINISTRA_REEMPLAZO_TOTAL,
			cache	:	false,
			data    : JSON.stringify(parametros),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloReemplazosTotal.prototype.grabarReemplazoTotalConConfirmacion1 = function(
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
	
	ModuloReemplazosTotal.prototype.grabarReemplazoTotalConConfirmacion2 = function(
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
	
	ModuloReemplazosTotal.prototype.eliminarRemplazosYactualizarValorVarConConfirmacion4 = function(
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
	
	ModuloReemplazosTotal.prototype.buscarReemplazosEnTotal = function(filtro){
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
	
	ModuloReemplazosTotal.prototype.eliminarReemplazoTotal = function(parametros) {
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
	
	ModuloReemplazosTotal.prototype.validarReemplazo = function(parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_VALIDA_REEMPLAZO_ADICION,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});

	};
	
	ModuloReemplazosTotal.prototype.validarSiUsuarioEsDeMejorCargo = function(parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_VALIDAR_USUARIO_MEJOR_CARGO,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	
	ModuloReemplazosTotal.prototype.listarDependencias = function(){
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
	
	ModuloReemplazosTotal.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloReemplazosTotal(SISCORR_APP);
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