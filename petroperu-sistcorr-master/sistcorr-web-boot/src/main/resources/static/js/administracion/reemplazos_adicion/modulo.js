var MODULO_REEMPLAZOS_ADICION = (function() {
	var instance;
	function ModuloReemplazosAdicion(SISCORR_APP) {
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.compBtnRegistrarAdicion = $("#btnRegistrarAdicion");
		this.btnRegistrarAdicion = $("#btnGuardarAdicion");
		this.comRegAdicion = {
			myModal : $("#modalRegReemAdicion"),
			cmbDependReemplazoAdicion : $("#cmbDependReemplazoAdicion"),
			cmbRolReemplazoAdicion : $("#cmbRolReemplazoAdicion"),
			cmbFuncionarioReemplazado : $("#cmbFuncionarioReemplazado"),
			cmbFuncReemplazar : $("#cmbFuncReemplazar"),
		}
		this.comElimAdicion = {
			myModal : $("#modalElimReemAdicion"),
		}
		
		
		this.comPopUpConfirmacion = $("#modalPopUpConfirmacion");
		this.comPopUpMensajeRolMenorReemplazo = $("#modalMensajeRolMenorReemplazo");
		this.comPopUpListaReemplazosEnAdicion = $("#modalListaReemplazosEnAdicion");
		this.comPopUpConfirmacionBorrarReemplazosEnAdicion = $("#modalConfirmacionBorrarReemplazosEnAdicion");

		this.objReemplazoAdicion = {
			id_reemplazo : 0,
			dependencia : $("#cmbDependReemplazoAdicion"),
			referencia : $("#txtReferencia"),
			rol : $("#cmbRolReemplazoAdicion"),
			usuarioSaliente : $("#cmbFuncionarioReemplazado"),
			usuarioEntrante : $("#cmbFuncReemplazar"),
			fechaReemplazoInicio : $("#txtFechaAdicionDesdeJG"),
			fechaReemplazoFin : $("#txtFechaAdicionHastaJG"),
			referencia : $("#txtReferencia"),
			accion :""
		};

		this.btnEliminarReemplazo = $(".btnEliminarReemplazo");
		this.btnEliminarReemplazoAdicion = $("#btnEliminarReemplazoAdicion");
		this.btnAceptarConfirmacion = $("#btnAceptarConfirmacion");
		this.btnAceptarMensajeRolMenorReemplazo = $("#btnAceptarMensajeRolMenorReemplazo");
		this.btnPreAceptarBorrarReemplazosAdicion = $("#btnPreAceptarBorrarReemplazosAdicion");
		this.btnAceptarBorrarReemplazosAdicion = $("#btnAceptarBorrarReemplazosAdicion");
		this.btnModificarReemplazo = $(".btnModificarReemplazo");
		this.componentesJG = {
			btnExportExcel : $("#btnExportarExcelJG"),
			btnFiltrosJG : $("#btnFiltrosJG"),
			dataTableRA: $("#tablaReemplazosEnAdicion"),
			dataTableConsulta : $("#tablaConsultaCorrespondenciasJG"),
			
			btnBuscar : $("#btnBuscarJG"),
			btnResetear : $("#btnResetearJG"),

			txtFechaAdicionDesdeJG : $("#txtFechaAdicionDesdeJG"),
			btnFechaDesde : $("#btnFechaDesde"),
			btnFechaHasta : $("#btnFechaHasta"),
			btnFechaAdicionDesde : $("#btnFechaAdicionDesde"),
			txtFechaDocumentoDesde : $("#txtFechaDocumentoDesdeJG"),
			txtFechaDocumentoHasta : $("#txtFechaDocumentoHastaJG"),
			btnFechaAdicionHasta : $("#btnFechaAdicionHasta"),
			txtFechaAdicionHastaJG : $("#txtFechaAdicionHastaJG"),
			cmbDependenciaFiltroAdicion : $("#cmbDependenciaFiltroAdicion"),
			cmbUsuarioSalienteFiltroAdicion : $("#cmbUsuarioSalienteFiltroAdicion"),
			cmbUsuarioEntranteFiltroAdicion : $("#cmbUsuarioEntranteFiltroAdicion"),
			cmbRolFiltroAdicion : $("#cmbRolFiltroAdicion"),
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_FILTRO_CMBDEPENDENCIAS_GG = '../app/buscar/filtroDependenciaGG';
		this.URL_FILTRO_JEFE_X_DEPENDENCIA='../app/buscar/funcionarioReemplazado';
		this.URL_FILTRO_FUNCIONARIO_APOYO='../app/buscar/funcionarioReemplazar';
		this.URL_CONSULTA_REEMPLAZO_ADICION = '../app/consultar-reemplazo-adicion';
		this.URL_BUSCAR_DEPENDENCIAS_ADICION = '../app/buscar/dependenciaAdicion';
		this.URL_BUSCAR_ROL_X_DEPENDENCIA = '../app/buscar/rolAdicionReemplazo';
		this.URL_BUSCAR_FUNCIONAR_REEMPLAZAR = '../app/buscar/funcionarioReemplazar';
		this.URL_ELIMINAR_REEMPLAZO = '../app/eliminarReemplazo';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION = '../app/grabarReemplazoAdicionSinConfirmar';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_1 = '../app/grabarReemplazoAdicionConConfirmacion1';
		this.URL_ADMINISTRA_REEMPLAZO_ADICION_CONFIR_2 = '../app/grabarReemplazoAdicionConConfirmacion2';
		this.URL_VALIDA_REEMPLAZO_ADICION = '../app/validarReemplazo';
		this.URL_BUSCAR_REEMPLAZOS_EN_ADICION = '../app/listarReemplazosAdicion';
		this.URL_ELIMINAR_REEMPLAZOS_CONFIR_4 = '../app/eliminarRemplazosYactualizarValorVarConConfirmacion4';
		this.EXPORT_EXCEL = '../app/exportar-reemplazo-adicion';

	}

	ModuloReemplazosAdicion.prototype.abrirMenu = function() {
		var ref = this;
		ref.btnAbrirmenu.click();
	};

	ModuloReemplazosAdicion.prototype.consultar = function(filtro) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_CONSULTA_JEFE_GESTOR,
			cache : false,
			data : JSON.stringify(filtro),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};

	ModuloReemplazosAdicion.prototype.listarDependencias = function() {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			url : ref.URL_LISTAR_DEPENDENCIAS_TODOS_JEFE_GESTOR,
			type : 'GET',
			cache : false,
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};

	ModuloReemplazosAdicion.prototype.eliminarReemplazoAdicion = function(
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

	ModuloReemplazosAdicion.prototype.grabarReemplazoAdicionSinConfirmar = function(
			parametros) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.URL_ADMINISTRA_REEMPLAZO_ADICION,
			cache : false,
			data : JSON.stringify(parametros),
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazosAdicion.prototype.grabarReemplazoAdicionConConfirmacion1 = function(
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
	
	ModuloReemplazosAdicion.prototype.grabarReemplazoAdicionConConfirmacion2 = function(
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
	
	ModuloReemplazosAdicion.prototype.eliminarRemplazosYactualizarValorVarConConfirmacion4 = function(
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

	ModuloReemplazosAdicion.prototype.validarReemplazo = function(parametros) {
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

	ModuloReemplazosAdicion.prototype.exportarExcel = function(filtro) {
		// debugger;
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		return $.ajax({
			type : 'POST',
			url : ref.EXPORT_EXCEL,
			cache : false,
			data : JSON.stringify(filtro),
			xhrFields : {
				responseType : 'blob'
			},
			beforeSend : function(xhr) {
				// xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};
	
	ModuloReemplazosAdicion.prototype.buscarReemplazosEnEdicion = function(filtro){
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
	};

	function createInstance(SISCORR_APP) {
		var object = new ModuloReemplazosAdicion(SISCORR_APP);
		return object;
	}
	return {
		getInstance : function(SISCORR_APP) {
			if (!instance) {
				instance = createInstance(SISCORR_APP);
				SISCORR_APP.fire('loaded', instance);
			}
			return instance;
		}
	};

})();