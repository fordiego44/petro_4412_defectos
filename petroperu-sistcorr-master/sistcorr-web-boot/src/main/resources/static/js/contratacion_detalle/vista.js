/*9000004276*/
var modulo_contratacion_detalle = MODULO_CONTRATACION_DETALLE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONTRATACION_DETALLE_VISTA = {
	modSistcorr: modulo_sistcorr,
	modulo: modulo_contratacion_detalle,
	errores: [],

	inicializar: function() {
		var ref = this;
		ref.modSistcorr.cargarVista();
		ref.iniciarDatatables();
		ref.iniciarEventos();
		ref.mostrarErrores();
	},

	iniciarDatatables: function() {
		$("#tablaBasesVendidas").DataTable({
			"lengthChange": false,
			"searching": false,
			"info": false,
			"language": { "zeroRecords": "No hay registros" }
		});
		$("#tablaConsultaBases").DataTable({
			"lengthChange": false,
			"searching": false,
			"info": false,
			"language": { "zeroRecords": "No hay registros" }
		});
		$("#tablaPropuestas").DataTable({
			"lengthChange": false,
			"searching": false,
			"info": false,
			"language": { "zeroRecords": "No hay registros" }
		});
		$("#tablaImpugnaciones").DataTable({
			"lengthChange": false,
			"searching": false,
			"info": false,
			"language": { "zeroRecords": "No hay registros" }
		});
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
			window.location.replace("../../app/consulta-contrataciones");
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

	mostrarErrores: function() {
		var ref = this;
		for (var i in ref.errores) {
			var err = ref.errores[i];
			if (err) {
				ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");
			}
		}
	}
};

setTimeout(function() {
	CONTRATACION_DETALLE_VISTA.modSistcorr = modulo_sistcorr;
	CONTRATACION_DETALLE_VISTA.inicializar();
}, 200);