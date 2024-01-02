var modulo_documentos_firmados = MODULO_DOCUMENTOS_FIRMADOS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var DOCUMENTOS_FIRMADOS = {
		modSistcorr: null,
		modulo: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.modSistcorr.eventoTooltip();
			
			ref.modulo.btnRetroceder.click(function(){
				window.location.href = "../../app/pendientes"
			});
		}
}

setTimeout(function(){
	DOCUMENTOS_FIRMADOS.modSistcorr = modulo_sistcorr;
	DOCUMENTOS_FIRMADOS.modulo = modulo_documentos_firmados;
	DOCUMENTOS_FIRMADOS.inicializar();
}, 500);