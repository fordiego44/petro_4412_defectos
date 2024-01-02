var MODULO_CONDUCTOR_EDICION = (function(){
	var instance;
	function ModuloConductorEdicion(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#main_correspondencia");
		this.compRetroceder = $("#btnRetroceder");
		
		this.btnReintentar= $("#btnReintentarPaso");
		this.btnSaltarPaso= $("#btnSaltarPasoPaso");
		this.btnTerminar= $("#btnTerminarPaso");
		
		this.componentesConductor={
				workflowId :$("#workflowId"),
				txtProceso: $("#txtProceso"),
				txtVersion: $("#txtVersion"),
				txtReferenciaPrincipal: $("#txtReferenciaPrincipal"),
				txtReferenciaAlternativa: $("#txtReferenciaAlternativa"),
				txtMensajeError: $("#txtMensajeError"),
		}
		this.tabs = {
				tabDatos: {
					compHeader: $("#Datos-tab"),
					compBody: $("#contenidoAccordionDatos"),
					accordion: {
						
					}
				},
				tabFlujo: {
					
				}
		};
		this.compCerrarSession = $(".closeSession");
		this.URL_REINTENTAR_CONDUCTOR='../app/reintentar-conductor';
		this.URL_AVANZAR_PASO_CONDUCTOR='../app/saltar-paso-conductor';
		this.URL_TERMINAR_PASO_CONDUCTOR='../app/terminar-paso-conductor';
	}
	
	ModuloConductorEdicion.prototype.reintentarConductor = function(id){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	'../'+ref.URL_REINTENTAR_CONDUCTOR+'/'+id,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
		
	};
	
	ModuloConductorEdicion.prototype.saltarPasoConductor = function(id){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	'../'+ref.URL_AVANZAR_PASO_CONDUCTOR+'/'+id,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
		
		
	};
	
	ModuloConductorEdicion.prototype.terminarPasoConductor = function(id){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'POST',
			url			: 	'../'+ref.URL_TERMINAR_PASO_CONDUCTOR+'/'+id,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloConductorEdicion(SISCORR_APP);
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