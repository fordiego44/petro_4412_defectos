var MODULO_LOGIN = (function(){
	var instance;
	function ModuloLogin(SISCORR_APP) {
		 this.SISCORR_APP = SISCORR_APP;
		 this.SISCORR_AUTHOR = 'Kenyo';
		 this.URL_BASE_CAPTCHA = './captcha';
		 this.imgCaptcha = $("#imgCaptcha");
		 this.inputCaptcha = $("#captcha");
		 this.inputIdCaptcha = $("#ramdomId");
		 this.inputNombre = $("#parametro_1");
		 this.btnGenerarCaptcha = $("#btnGenerarCaptcha");
		 this.btnIngresar = $("#btnIngresar");
		 this.frmLogin = $("#frmLogin");
	}
	
	ModuloLogin.prototype.generarCaptcha = function(){
		var ref = this;
		return $.ajax({
			type: 'GET',
			cache: false,
			url: ref.URL_BASE_CAPTCHA,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		    }
		});
	};
	
	
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloLogin(SISCORR_APP);
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