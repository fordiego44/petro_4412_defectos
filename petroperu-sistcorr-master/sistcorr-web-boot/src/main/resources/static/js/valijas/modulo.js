var VALIJAS = (function(){
	var instance;
	function Valijas(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compCerrarSession = $(".closeSession");
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.btnLimpiar = $("#btnLimpiar");
		this.btnRegistrarValija = $("#btnRegistrarValija");
		
		
		this.componentes = {
				btnLimpiar: $("#btnLimpiar"),
				btnRegistrarValija: $("#btnRegistrarValija"),
				corrIdentificadorValija: $("#corrIdentificadorValija"),
				corrCourier: $("#corrCourier"),
				corrCentroGestionRemitente: $("#corrCentroGestionRemitente"),
				corrOrdenServicio: $("#corrOrdenServicio")
		};
		
		this.URL_REGISTRAR_VALIJA = '../app/registrarValija';
	}
	
	Valijas.prototype.registrarValijas = function(valija){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Valija Registro:");
		console.log(valija);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTRAR_VALIJA,
			cache	:	false,
			data	:	JSON.stringify(valija),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	Valijas.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	function createInstance(SISCORR_APP) {
        var object = new Valijas (SISCORR_APP);
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
