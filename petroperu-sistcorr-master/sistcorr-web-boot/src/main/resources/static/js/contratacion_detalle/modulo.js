/*9000004276*/
var MODULO_CONTRATACION_DETALLE = (function() {
	var instance;
	function ModuloContratacionDetalle(SISCORR_APP) {
		this.SISCORR_APP = SISCORR_APP;
		this.classMasTexto = 'masTexto';
		this.classMenosTexto = 'menosTexto';

		this.VISTA = {
			btnRetroceder: $("#btnRetroceder")
		};
	}

	function createInstance(SISCORR_APP) {
		var object = new ModuloContratacionDetalle(SISCORR_APP);
		return object;
	}
	return {
		getInstance: function(SISCORR_APP) {
			if (!instance) {
				instance = createInstance(SISCORR_APP);
				SISCORR_APP.fire('loaded', instance);
			}
			return instance;
		}
	};

})();