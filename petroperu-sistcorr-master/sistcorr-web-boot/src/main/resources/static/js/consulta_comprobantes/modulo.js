/*9000004276*/
var MODULO_CORRESPONDENCIA_CONSULTA_COMPROBANTES = (function() {
  var instance;

  function ModuloConsultaComprobantes(SISCORR_APP) {
    this.SISCORR_APP = SISCORR_APP;
    this.SISCORR_AUTHOR = 'Jhon';
    this.csrfToken = $("#csrfToken");
    this.csrfHeader = $("#csrfHeader");
    this.btnAbrirmenu = $("#btnAbrirMenu");
    this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
    this.compCerrarSession = $(".closeSession");
    this.divPreload = $("#contenidoPreload");
    this.divCuerpoModulo = $("#cuerpoModulo");
    this.componentesCC = {
      //botones cabecera
      btnExportExcel: $("#btnExportarExcelCC"),
      //boton cuadro filtros
      btnFiltrosCC: $("#btnFiltrosCC"),
      //campos filtros
      txtCorrelativo: $("#txtCorrelativoCC"),
      cbmEstado: $("#cbmEstadoCC"),
      txtNroBatch: $("#txtNroBatchCC"),
      cmbDependencia: $("#cmbDependenciaCC"),
      txtRucProveedor: $("#txtRucProveedorCC"),
      txtNumComprobante: $("#txtNumComprobanteCC"),
      txtFechaDocumentoDesde: $("#txtFechaComprobanteDesdeCC"),
      txtFechaDocumentoHasta: $("#txtFechaComprobanteHastaCC"),
      txtRazonSocial: $("#txtRazonSocialCC"),
      //botones filtro
      btnFechaDesde: $("#btnFechaDesdeCC"),
      btnFechaHasta: $("#btnFechaHastaCC"),
      btnBuscar: $("#btnBuscarCC"),
      btnResetear: $("#btnResetearCC"),
      //datatable
      dataTableConsulta: $("#tablaConsultaComprobantes")
    };

    this.URL_CONSULTA_COMPROBANTES_PAGINADO = '../app/consultar-comprobantes-paginado';
    this.EXPORT_EXCEL = '../app/consultar-comprobantes-excel';
    this.URL_TUTORIALES = "app/tutoriales";
  }

  ModuloConsultaComprobantes.prototype.abrirMenu = function() {
    var ref = this;
    ref.btnAbrirmenu.click();
  };

  ModuloConsultaComprobantes.prototype.irADetalle = function(correlativo) {
    var ref = this;
    var url = window.location.origin;
    var path = window.location.pathname.trim().split("/");
    for (var i = 1; i <= path.length; i++) {
      if (path[i] == 'app') {
        break;
      } else {
        url = url + "/" + path[i];
      }
    }
    url = url + "/app/ver-detalle-comprobante/" + correlativo;
    window.location.replace(url);
  };

  ModuloConsultaComprobantes.prototype.exportarExcel = function(filtro) {
    var ref = this;
    var token = ref.csrfToken.val();
    var header = ref.csrfHeader.val();
    return $.ajax({
      type: 'POST',
      url: ref.EXPORT_EXCEL,
      cache: false,
      data: JSON.stringify(filtro),
      xhrFields: {
        responseType: 'blob'
      },
      beforeSend: function(xhr) {
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader(header, token);
      }
    });
  };

  ModuloConsultaComprobantes.prototype.blankDatePicker = function(idComponente) {
    var $comp = $("#" + idComponente);
    $comp.addClass('form-control');
    var label = $("label[for='" + $comp.attr('id') + "']");
    label.removeClass('active');
    $comp.removeClass('invalid');
    $comp.removeClass('valid');
  };

  ModuloConsultaComprobantes.prototype.cargarVista = function(){
		var ref = this;
    ref.divPreload.css('display', 'none');
    ref.divCuerpoModulo.css('display','block');
	};

  function createInstance(SISCORR_APP) {
    var object = new ModuloConsultaComprobantes(SISCORR_APP);
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