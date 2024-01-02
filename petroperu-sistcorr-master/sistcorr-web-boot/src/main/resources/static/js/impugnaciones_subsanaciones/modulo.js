/*9000004412*/
var MODULO_IMPUGNACIONES_SUBSANACIONES = (function(){
	var instance;
	function ModuloImpugnacionesSubsanaciones(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.compBtnGuardar = $("#btnGuardarCorrespondencia");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compBtnGuardar = $("#btnGuardarCorrespondencia");
		this.compModalConfirmarRegistro = $("#modalConfirmarRegistro");
		this.compBtnCancelar = $("#btnCancelarCorrespondencia"); 
		
		this.btnValidarSUNAT = $("#validarSUNAT");

		//INICIO defecto 14
		this.componentesValidarImpugnacionesSubsanaciones = {
			txtRucSunat: $("#rmt_ruc"),
			txtSunat:$("#rmt_proveedor"),
			cmpProceso: $("#rmt_proceso"),
			cmpTipoRecepcion: $("#rmt_tipo"),
			cmpAdmisible: $("#rmt_admisible"),
			
		};

		this.URL_BUSCAR_ENTIDADES_EXTERNA_SUNAT = '../app/correspondencias/consultar/EntidadesExternaNacionalSunat';
		//FIN defecto 14
	}

	//INICIO defecto 14
	ModuloImpugnacionesSubsanaciones.prototype.buscarEntidadExternaNacionalSunat = function(txtRucRazonSocial){
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
	//FIN defecto 14
	
	ModuloImpugnacionesSubsanaciones.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloImpugnacionesSubsanaciones(SISCORR_APP);
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