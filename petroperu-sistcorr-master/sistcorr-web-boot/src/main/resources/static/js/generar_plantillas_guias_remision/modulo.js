/*9000004412*/
var MODULO_GENERAR_PLANTILLAS_GUIAS_REMISION= (function(){
	var instance;
	function ModuloGenerarPlantillasGuiasRemision(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		
		 //datatable
		this.componentes = {   
				btnGenerar:$("#btnGenerar"),
				btnLimpiar :$("#btnResetear"),
				btnVer: $("#btnVer"),
				rmt_courier: $("#rmt_courier"),
				rmt_trabajo: $("#rmt_trabajo"),
				rmt_respuesta: $("#rmt_respuesta"),
		}; 
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_GENERAR_PLANILLA_GUIA_REMISION = '../app/generarPlanillaGuiaRemision';
  
	}
	ModuloGenerarPlantillasGuiasRemision.prototype.generarPlanillaGuiaRemision = function(data){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		
		console.log("Generar Planilla Guia Remision");
		console.log(data);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_GENERAR_PLANILLA_GUIA_REMISION,
			cache	:	false,
			data	:	JSON.stringify(data),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json"); 
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloGenerarPlantillasGuiasRemision(SISCORR_APP);
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