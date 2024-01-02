/*9000004412*/
var MODULO_REGISTRAR_INGRESO = (function(){
	var instance;
	function ModuloRegistrarIngreso(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");    
		this.btnRegistarIngreso=$("#btnRegistrarIngreso");
		this.btnLimpiarIngreso=$("#btnLimpiarIngreso");
		
		
		//datatable
	      
	      this.componentesCC = {
	    	       
	    	      //datatable
	    	      dataTableConsulta : $("#tablaConsultaComprobantes"),
	    	      btnRegistarIngreso:$("#btnRegistrarIngreso"),
				  btnLimpiarIngreso :$("#btnLimpiarIngreso"),
				  correlativo:$("#corrCorrelativo"),
				  cmpFuncionario :$("#corrFuncionario"),
				  dataTableRegistrarIngreso : $("#tableRegistrarIngreso")
	    };
	      
	      this.URL_REGISTRAR_TRACKING = '../app/registrarTrackingIngreso'; 
	      this.URL_BUSQUEDA_TRACKING_INGRESO='../app/busquedaTrackingIngeso';
	}
	
	
	 
	ModuloRegistrarIngreso.prototype.registarTracking = function(tracking){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("Registrar Tracking");
		console.log(tracking);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTRAR_TRACKING,
			cache	:	false,
			data	:	JSON.stringify(tracking),
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
        var object = new ModuloRegistrarIngreso(SISCORR_APP);
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