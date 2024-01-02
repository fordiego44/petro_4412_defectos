var modulo_403 = MODULO_403.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var _403_VISTA = {
		modSistcorr: null,
		modulo: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.modSistcorr.eventoTooltip();
			ref.iniciarEventos();
		},
		
		iniciarEventos: function(){
			var ref = this;
			ref.modulo.compBtnRetoceder.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
		}
};

setTimeout(function(){
	_403_VISTA.modSistcorr = modulo_sistcorr;
	_403_VISTA.modulo = modulo_403;
	_403_VISTA.inicializar();
}, 200);