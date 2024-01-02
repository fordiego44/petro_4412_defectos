var MODULO_ADMIN_CONDUCTOR = (function(){
	var instance;
	function ModuloAdminConductor(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Jhon';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnReintentarMasivo = $("#btnReintentarMasivo");
		this.objConductor={
				id: 0,
				ultimoMensaje: '',
				asuntoMail: '',
				dtfechaExcepcion: '',
				fechaExcepcion: '',
				proceso: '',
				referenciaAlternativa: '',
				referenciaPrincipal: '',
				textoMail: '',
				version: ''
		}
		
		this.componentesJG = {
			
			dataTableConsulta: $("#tablaConsultaCorrespondenciasJG"),
			/*CONDUCTOR*/
			cbmTareaExcepcion: $("#cmbTareaExcepcion"),
			txtProceso:$("#txtProceso"),
			txtRefPrincipal:$("#txtRefPrincipal"),
			txtRefAlternativa:$("#txtRefAlternativa"),
			cmbDependenciaRemitenteJG: $("#cmbDependenciaRemitenteJG"),
			txtFechaDocumentoDesde: $("#txtFechaDocumentoDesdeJG"),
			btnFechaDesde: $("#btnFechaDesde"),
			txtFechaDocumentoHasta: $("#txtFechaDocumentoHastaJG"),
			btnFechaHasta: $("#btnFechaHasta"),
			btnBuscar: $("#btnBuscarJG"),
			btnResetear: $("#btnResetearJG"),
			btnExportExcel: $("#btnExportarExcelJG"),
		};
		
		this.EXPORT_EXCEL = '../app/exportar-conductor';
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_CONSULTA_CONDUCTOR="../app/buscarConductor";
		this.URL_CONSULTA_CONDUCTOR_SIG_PAG="../app/buscarConductorSiguientePagina";
		this.URL_REINTENTAR_CONDUCTOR_MASIVO="../app/conductorReintentarMasivo";
		this.URL_NUEVO_CONDUCTOR = '../app/registroConductor';
	}
	
	ModuloAdminConductor.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	
	ModuloAdminConductor.prototype.buscarConductor=function(filtro){
		var ref = this;
		var data = {'searchCriteria'	: 	filtro};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA_CONDUCTOR,
			cache	:	false,
			data	:	JSON.stringify(data),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloAdminConductor.prototype.buscarConductorSiguientePagina=function(filtro){
		var ref = this;
		var data = {'searchCriteria'	: 	filtro};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_CONSULTA_CONDUCTOR_SIG_PAG,
			cache	:	false,
			data	:	JSON.stringify(data),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloAdminConductor.prototype.conductorReintentarMasivo = function(filtro){
		var ref = this;
		var data = {'listaWorkflowIds'	: 	filtro};
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		:	ref.URL_REINTENTAR_CONDUCTOR_MASIVO,
			cache	:	false,
			data	:	JSON.stringify(data),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloAdminConductor.prototype.irADetalle = function(id,ultimoMensaje,asuntoMail,dtfechaExcepcion,fechaExcepcion,
			proceso,referenciaAlternativa,referenciaPrincipal,textoMail,version){
		console.log('ultimoMensaje'+ultimoMensaje);
		var ref = this;
		var url = window.location.origin;
	
		this.objConductor.id=id;
		this.objConductor.ultimoMensaje=ultimoMensaje;
		this.objConductor.asuntoMail=asuntoMail;
		this.objConductor.dtfechaExcepcion=dtfechaExcepcion;
		this.objConductor.fechaExcepcion=fechaExcepcion;
		this.objConductor.proceso=proceso;
		this.objConductor.referenciaAlternativa=referenciaAlternativa;
		this.objConductor.referenciaPrincipal=referenciaPrincipal;
		this.objConductor.textoMail=textoMail;
		this.objConductor.version=version;
		
		sessionStorage.setItem("OBJ_CONDUCTOR",JSON.stringify(this.objConductor));
		var refAxu = "0";
		if(referenciaPrincipal != null && referenciaPrincipal != undefined && referenciaPrincipal != "")
			refAxu = referenciaPrincipal;
		else if(referenciaAlternativa != null && referenciaAlternativa != undefined && referenciaAlternativa != "") refAxu = referenciaAlternativa;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		
		url=url+"/app/detalleConductor/"+refAxu;
		
		window.location.replace(url);
	};
	
	ModuloAdminConductor.prototype.exportarExcel = function(filtro){
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
        var object = new ModuloAdminConductor(SISCORR_APP);
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