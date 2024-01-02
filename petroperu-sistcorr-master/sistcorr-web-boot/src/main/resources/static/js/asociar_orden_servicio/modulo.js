/*9000004412  defecto julio*/
var MODULO_ASOCIAR_ORDEN_SERVICIO= (function(){
	var instance;
	function ModuloAsociarOrdenServicio(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		
		 //datatable
		this.componentes = {  
				txtCorrelativo:$("#txtCorrelativo"),
				txtCourier:$("#txtCourier"),
				btnResetear:$("#btnResetear"),
				btnAsociarOrdenServicio:$("#btnRegistrar")
		};
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales"); 
		this.URL_TUTORIALES = "app/tutoriales"; 
		this.URL_ASOCIAR_ORDEN_SERVICIO = '../app/asociarOrdenServicio';
	}
	
	ModuloAsociarOrdenServicio.prototype.asociarOrdenServicio = function(ordenServicio){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Asociar Orden Servicio");
		console.log(ordenServicio);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_ASOCIAR_ORDEN_SERVICIO,
			cache	:	false,
			data	:	JSON.stringify(ordenServicio),
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
        var object = new ModuloAsociarOrdenServicio(SISCORR_APP);
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