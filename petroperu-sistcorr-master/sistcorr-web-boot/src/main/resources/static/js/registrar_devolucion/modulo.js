/*9000004412 defecto julio*/
var MODULO_REGISTRAR_DEVOLUCION= (function(){
	var instance;
	function ModuloRegistrarDevolucion(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");   
		
		 //datatable

		 this.objReemplazoTotal = { 
			txtCorrelativo: $("#txtCorrelativo"),
			rmt_motivo    : $("#rmt_motivo")
		};

		this.componentes = {
				btnRegistrarDevolucion:$("#btnRegistrar"),
				btnLimpiar :$("#btnResetear"),
				correlativo:$("#txtCorrelativo"),
				codMotivo :$("#rmt_motivo")
		};

		this.btnAbrirTutoriales = $("#btnAbrirTutoriales"); 
		this.URL_TUTORIALES = "app/tutoriales"; 
		this.URL_REGISTRAR_DEVOLUCION = '../app/registrarDevolucion';
	}
	
	ModuloRegistrarDevolucion.prototype.registrarDevolucion = function(devolucion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Registrar Devolucion");
		console.log(devolucion);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTRAR_DEVOLUCION,
			cache	:	false,
			data	:	JSON.stringify(devolucion),
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
        var object = new ModuloRegistrarDevolucion(SISCORR_APP);
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