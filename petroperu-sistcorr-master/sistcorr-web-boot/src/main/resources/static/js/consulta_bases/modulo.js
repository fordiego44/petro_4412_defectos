/*9000004412*/
var MODULO_CONSULTA_BASE = (function(){
	var instance;
	function ModuloConsultaBase(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnValidarVentasBases = $("#btnGuardarValidarVentasBases");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compModalConfirmarRegistro = $("#modalConfirmarRegistro");
		this.compBtnCancelar = $("#btnCancelarCorrespondencia"); 
		this.btnImprimirEtiqueta=$("#btnImprimirEtiqueta");
		this.btnImprimirDocOriginal=$("#btnImprimirDocOriginal");
		this.modalConfirmarVentaBases = $("#modalConfirmarRegistro");
		
		this.btnValidarSUNAT = $("#validarSUNAT");
		
		this.componentesValidarVentasBases = {
				txtRucSunat: $("#rmt_ruc"),
				txtSunat:$("#rmt_proveedor"),
				cmpProceso: $("#rmt_proceso"),
				cmpHora:$("#rmt_hora"),
		};
		
		this.URL_BUSCAR_ENTIDADES_EXTERNA_SUNAT = '../app/correspondencias/consultar/EntidadesExternaNacionalSunat';
		this.URL_VALIDAR_CONSULTA_VENTAS_BASES='../app/validarConsultaVentasBases';
		this.URL_REGISTAR_CONSULTA_VENTAS_BASES='../app/registrarConsultaVentasBases';
	}
	
	ModuloConsultaBase.prototype.buscarEntidadExternaNacionalSunat = function(txtRucRazonSocial){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var tipo="RUC";
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_BUSCAR_ENTIDADES_EXTERNA_SUNAT+"?rucRazonSocial="+txtRucRazonSocial+"&tipo=" + tipo,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaBase.prototype.validaConsultaVentasBasesFueraHora = function(consultaVentaBase){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Ventas Bases Validar:");
		console.log(consultaVentaBase);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_VALIDAR_CONSULTA_VENTAS_BASES,
			cache	:	false,
			data	:	JSON.stringify(consultaVentaBase),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaBase.prototype.registrarConsultaVentasBases = function(consultaVentaBase){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Ventas Bases Registro:");
		console.log(consultaVentaBase);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTAR_CONSULTA_VENTAS_BASES,
			cache	:	false,
			data	:	JSON.stringify(consultaVentaBase),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloConsultaBase.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloConsultaBase(SISCORR_APP);
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