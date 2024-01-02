var MODULO_REEMPLAZOS_ADICION = (function(){
	var instance;
	function ModuloReemplazosAdicion(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.compBtnRegistrarAdicion= $("#btnRegistrarAdicion");
		this.btnModificarFuncionario =$("#btnModificarFuncionario");
		this.comRegAdicion = {
				myModal: $("#modalRegReemAdicion"),
				cmbDependReemplazoAdicion:$("#cmbDependReemplazoAdicion"),
				cmbRolReemplazoAdicion:$("#cmbRolReemplazoAdicion"),
				cmbFuncionarioReemplazado:$("#cmbFuncionarioReemplazado"),
				cmbFuncReemplazar:$("#cmbFuncReemplazar"),
				btnGuardarFuncionario:("#btnGuardarFuncionario"),
		}
		this.comElimFuncionario ={
				myModal:$("#modalElimFuncionario"),
		}
		
		this.comModificarAdicion={
				myModal: $("#modalRegReemAdicion"),
				cmbDependReemplazoAdicion:$("#cmbDependReemplazoAdicion"),
				cmbRolReemplazoAdicion:$("#cmbRolReemplazoAdicion"),
				cmbFuncionarioReemplazado:$("#cmbFuncionarioReemplazado"),
				cmbFuncReemplazar:$("#cmbFuncReemplazar"),
		}
		
		this.objetoFuncionarios = {
				id : 0,
				registro : $("#txtRegistro"),
				primerNombre : $("#txtPrimerNombre"),
				segundoNombre : $("#txtSegundoNombre"),
				apellidoPaterno : $("#txtApellidoPaterno"), 
				apellidoMaterno : $("#txtApellidoMaterno"),
				dependencia : $("#cmbDepend"),
				email: $("#txtEmail"),
				ficha: $("#txtFicha"),
				operacion: $("#txtOperacion"),
				tipoFuncionario: $("#txtFuncionario"),
				notificaciones: $("#cmbNotificaciones"),
				participa: $("#cmbParticipa"),
				activo: $("#cmbActivo"),
				supervisor: $("#cmbSupervisor"),
				accion :""
		};
		
		this.cmbDependencia= $("#cmbDependencia");
		this.btnEliminarFuncionario= $(".btnEliminarFuncionario");
		this.btnElimFuncionario=$("#btnElimFuncionario");
		this.btnModificarReemplazo = $(".btnModificarReemplazo");
		this.btnGuardarFuncionario=$("#btnGuardarFuncionario");
		this.componentesJG = {
			btnExportExcel: $("#btnExportarExcelJG"),
			btnFiltrosJG: $("#btnFiltrosJG"),
			dataTable: $("#tablaCorrespondenciasJG"),
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesdeJG"),
			txtFechaAdicionDesdeJG:$("#txtFechaAdicionDesdeJG"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			txtFechaAdicionHastaJG:$("#txtFechaAdicionHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),
			btnFechaAdicionDesde:$("#btnFechaAdicionDesde"),
			btnFechaAdicionHasta:$("#btnFechaAdicionHasta"),
			cbmTipoCorrespondencia: $("#cbmTipoCorrespondencia"),
			cbmTipoEmision: $("#cbmTipoEmision"),
			rbtnTipoDestinatario: $("input:radio[name='rbtnTipoDestinatario']:checked"),
			cbmFlujoFirma: $("#cbmFlujoFirma"),
			cbmConfidencialidad: $("#cbmConfidencialidad"),
			cbmUrgente: $("#cbmUrgente"),
			cbmDespachoFisico: $("#cbmDespachoFisico"),
			cbmDependenciaDestinatariaInterno: $("#cbmDependenciaDestinatariaInterno"),
			cbmDependenciaCopia: $("#cbmDependenciaCopia"),
			/*FILTRO BUSQUEDA*/
			cmbDependenciaFuncionario: $("#cmbDependencia"),
			txtRegistroFiltro:$("#txtRegistroFiltro"),
			txtNombres:$("#txtNombres"),
			txtApellidos:$("#txtApellidos"),
			btnBuscar: $("#btnBuscarFuncionario"),
			btnResetear: $("#btnResetearJG")
			/*MODAL AGREGAR MODIFICAR*/
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.URL_CONSULTA_REEMPLAZO_ADICION= '../app/consultar-reemplazo-adicion';
		this.URL_BUSCAR_DEPENDENCIAS_ADICION = '../app/buscar/dependenciaAdicion';
		this.URL_BUSCAR_ROL_X_DEPENDENCIA = '../app/buscar/rolAdicion';
		this.URL_BUSCAR_FUNCIONARIO_REEMPLAZADO='../app/buscar/funcionarioReemplazado';
		this.URL_BUSCAR_FUNCIONAR_REEMPLAZAR='../app/buscar/funcionarioReemplazar';
		this.URL_ELIMINAR_REEMPLAZO = '../app/buscar/eliminarReemplazo';
		
		/*ADMINISTRACION FUNCIONARIOS*/
		this.URL_CONSULTA_ADMIN_FUNCIONARIOS='../app/consultar-admin-funcionarios';
		this.URL_GUARDAR_FUNCIONARIOS='../app/guardar-funcioanarios';
		this.URL_ELIMINAR_FUNCIONARIO = '../app/eliminarFuncionario';
		this.EXPORT_EXCEL = '../app/consultar-funcionario-excel';
		
	}
	
	ModuloReemplazosAdicion.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloReemplazosAdicion.prototype.guardarFuncionarios= function(parametros){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_GUARDAR_FUNCIONARIOS,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
		
		
	};
	
	ModuloReemplazosAdicion.prototype.consultar = function(filtro){
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
	
	ModuloReemplazosAdicion.prototype.listarDependencias = function(){
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
	
	ModuloReemplazosAdicion.prototype.irADetalle = function(id){
		var ref = this;
		var url = window.location.origin;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		url = url + "/app/ver-detalle/" + id;
		window.location.replace(url);
	};
	
	ModuloReemplazosAdicion.prototype.eliminarFuncionario = function(parametros){
		var ref=this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_ELIMINAR_FUNCIONARIO,
			cache		:	false,
			data		:	JSON.stringify(parametros),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloReemplazosAdicion.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloReemplazosAdicion(SISCORR_APP);
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