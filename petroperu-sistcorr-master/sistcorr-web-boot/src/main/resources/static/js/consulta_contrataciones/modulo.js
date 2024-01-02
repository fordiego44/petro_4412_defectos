/*9000004276*/
var MODULO_CORRESPONDENCIA_CONSULTA_CONTRATACIONES = (function() {
  var instance;

  function ModuloConsultaContrataciones(SISCORR_APP) {
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
      txtNroProceso: $("#txtNroProceso"),
      cbmTipoProceso: $("#cbmTipoProceso"),
      txtNroMemo: $("#txtNroMemo"),
      cmbDependencia: $("#cmbDependenciaCC"),
      //botones filtro
      btnBuscar: $("#btnBuscarCC"),
      btnResetear: $("#btnResetearCC"),
      //datatable
      dataTableConsulta: $("#tablaConsultaContrataciones")
    };

    this.URL_CONSULTA_CONTRATACIONES_PAGINADO = '../app/consultar-contrataciones-paginado';
    this.EXPORT_EXCEL = '../app/consultar-contrataciones-excel';
    this.URL_TUTORIALES = "app/tutoriales";
  }

  ModuloConsultaContrataciones.prototype.abrirMenu = function() {
    var ref = this;
    ref.btnAbrirmenu.click();
  };

  ModuloConsultaContrataciones.prototype.irADetalle = function(nroProceso) {
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
    url = url + "/app/ver-detalle-contratacion/" + nroProceso;
    window.location.replace(url);
  };

  ModuloConsultaContrataciones.prototype.exportarExcel = function(filtro) {
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

  ModuloConsultaContrataciones.prototype.cargarVista = function(){
		var ref = this;
    ref.divPreload.css('display', 'none');
    ref.divCuerpoModulo.css('display','block');
	};

  function createInstance(SISCORR_APP) {
    var object = new ModuloConsultaContrataciones(SISCORR_APP);
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