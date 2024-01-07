/*9000004412*/
var MODULO_GENERAR_PLANILLAS= (function(){
	var instance;
	function ModuloGenerarPlanillas(SISCORR_APP){
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
				rmt_alcance:$("#rmt_alcance"),
				rmt_courier :$("#rmt_courier"),
				rmt_respuesta: $("#rmt_respuesta"),
				corr_urgente:$('input[name=corr_urgente]') 
		};
		
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.URL_GENERAR_PLANILLA = '../app/generarPlanilla';
		this.URL_LISTA_COURIERS = '../app/listaCouriers';
	}
	ModuloGenerarPlanillas.prototype.generarPlanilla = function(planilla){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		
		console.log("Generar Planilla");
		console.log(planilla);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_GENERAR_PLANILLA,
			cache	:	false,
			data	:	JSON.stringify(planilla),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json"); 
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	ModuloGenerarPlanillas.prototype.listaCouriers = function(alcance) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		var principales = [];
		
		console.log("lista couriers"); 
		console.log(alcance); 
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_LISTA_COURIERS,
			cache	:	false,
			data	:	JSON.stringify(alcance),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json"); 
			        xhr.setRequestHeader(header, token);
		    }
		});
	}
	function createInstance(SISCORR_APP) {
        var object = new ModuloGenerarPlanillas(SISCORR_APP);
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