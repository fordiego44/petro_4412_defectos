/*9000004276*/
var MODULO_COMPROBANTE_DETALLE = (function() {
	var instance;

	function ModuloComprobanteDetalle(SISCORR_APP) {
		this.SISCORR_APP = SISCORR_APP;
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.classAgregarObservacion = "agregarObservacion";
		this.compBtnRegistrarObservacion = $("#btnRegistrarObservacion");
		this.compObservacionRegistrarCorrespondencia = $("#textRegistrarObservacion");
		this.compModalRegistrarObservacion = $("#modalRegistrarObservacion");
		this.classMasTexto = 'masTexto';
		this.classMenosTexto = 'menosTexto';

		this.VISTA = {
			btnRetroceder: $("#btnRetroceder"),
			btnEnviarCopia: $("#btnEnviarCopia"),
			compCorrelativo: $("#correlativo")
		};

		this.URL_REGISTRAR_OBSERVACION_COMPROBANTE = 'app/registrar/observacion-comprobante';
	}

	ModuloComprobanteDetalle.prototype.registrarObservacion = function(correlativo, observacion) {
		var ref = this;
		var token = ref.csrfToken.val();
		var header = ref.csrfHeader.val();
		var parametro = { "observacion": observacion };
		return $.ajax({
			type: 'PUT',
			url: '../../' + ref.URL_REGISTRAR_OBSERVACION_COMPROBANTE + '/' + correlativo,
			data: JSON.stringify(parametro),
			cache: false,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
				xhr.setRequestHeader(header, token);
			}
		});
	};

	function createInstance(SISCORR_APP) {
		var object = new ModuloComprobanteDetalle(SISCORR_APP);
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