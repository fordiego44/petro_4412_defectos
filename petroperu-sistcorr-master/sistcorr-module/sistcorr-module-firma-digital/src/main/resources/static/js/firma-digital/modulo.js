var MODULO_FIRMA_DIGITAL = (function(){
	var instance;	
	function ModuloFirmaDigital(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		 this.SISCORR_AUTHOR = 'Kenyo';
		 this.URL_GENERAR_ZIP = "../../app/firma-digital/zipear";
	}
	
	ModuloFirmaDigital.prototype.crearCookie = function(c_name,value,exdays){
		var ref = this;
		var exdate=new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
		document.cookie=c_name + "=" + c_value;
	};
	
	ModuloFirmaDigital.prototype.generarZip = function(idCorrespondencia){
		var ref = this;
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_GENERAR_ZIP + "/" + idCorrespondencia,
			cache	:	false,
		});
	};
	
	ModuloFirmaDigital.prototype.abrirFormularioFirma = function(idCorrespondencia){
		var ref = this;
		window.location.href = "../../app/firma-digital/" + idCorrespondencia;
		/*window.open("../../app/firma-digital/" + idCorrespondencia, 
				'popUpWindow',
				'height=300,width=500,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes');*/
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloFirmaDigital(SISCORR_APP);
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