/*9000004276*/
var modulo_comprobante_detalle = MODULO_COMPROBANTE_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var COMPROBANTE_DETALLE_VISTA = {
	modSistcorr: modulo_sistcorr,
	modulo: modulo_comprobante_detalle,
	errores: [],

	inicializar: function() {
		var ref = this;
		ref.modSistcorr.cargarVista();
		ref.iniciarEventos();
		ref.mostrarErrores();
	},

	iniciarEventos: function() {
		var ref = this;
		ref.modSistcorr.eventoTooltip();

		$("#Datos-tab").addClass('petro-tabs-activo');

		$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
			if (!e.target.dataset.tab) {
				return;
			}

			var $tab = $(e.currentTarget);
			$('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
			$tab.addClass('petro-tabs-activo');
		});

		ref.modulo.VISTA.btnRetroceder.click(function() {
			ref.modSistcorr.procesar(true);
			sessionStorage.setItem("origPag", "verDetalle");
			console.log(sessionStorage.getItem("origPag"));
			window.location.replace("../../app/consulta-comprobantes");
		});

		ref.modulo.VISTA.btnEnviarCopia.click(function() {
			ref.irEnviarCopia();
		})

		$("." + ref.modulo.classAgregarObservacion).click(function(event) {
			ref.abrirModalAgregarObservacion();
		});

		ref.modulo.compBtnRegistrarObservacion.click(function(event) {
			ref.agregarObservacion();
		});

		$("." + ref.modulo.classMasTexto).click(function(event) {
			var $columnaTarea = $(event.currentTarget);
			$columnaTarea.parent().attr("style", "display:none");
			$columnaTarea.parent().next().attr("style", "display:block");
		});

		$("." + ref.modulo.classMenosTexto).click(function(event) {
			var $columnaTarea = $(event.currentTarget);
			$columnaTarea.parent().attr("style", "display:none");
			$columnaTarea.parent().prev().attr("style", "display:block");
		});
	},


	irEnviarCopia: function() {
		var ref = this;
		ref.modSistcorr.procesar(true);
		sessionStorage.setItem('urlBack', '../../../../app/ver-detalle-comprobante/' + ref.modulo.VISTA.compCorrelativo.val())
		window.location.replace("../../app/enviar/comprobante/copia/" + ref.modulo.VISTA.compCorrelativo.val());
	},

	mostrarErrores: function() {
		var ref = this;
		for (var i in ref.errores) {
			var err = ref.errores[i];
			if (err) {
				ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");
			}
		}
	},

	abrirModalAgregarObservacion: function() {
		var ref = this;
		ref.modulo.compObservacionRegistrarCorrespondencia.val("");
		ref.modulo.compModalRegistrarObservacion.modal('show');
		ref.modulo.compObservacionRegistrarCorrespondencia.focus();
	},

	agregarObservacion: function() {
		var ref = this;
		var correlativo = ref.modulo.VISTA.compCorrelativo.val() || '';
		var observacion = ref.modulo.compObservacionRegistrarCorrespondencia.val() || '';
		if (correlativo.trim() == "") {
			ref.modSistcorr.notificar("ERROR", "Correlativo Incorrecto", "Warning");
			return;
		}
		if (observacion.trim() == "") {
			ref.modSistcorr.notificar("ERROR", "Debe ingresar una observación.", "Warning");
			return;
		}
		ref.modSistcorr.procesar(true);
		ref.modulo.registrarObservacion(correlativo, observacion)
			.then(function(respuesta) {
				if (respuesta.estado == true) {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modulo.compModalRegistrarObservacion.modal('hide');
					sessionStorage.setItem("tab", "Observaciones-tab");
					location.reload();
				} else {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
			}).catch(function(error) {
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	}

};


setTimeout(function() {
	COMPROBANTE_DETALLE_VISTA.modSistcorr = modulo_sistcorr;
	COMPROBANTE_DETALLE_VISTA.inicializar();
}, 200);