/*9000004276*/
var modulo_consulta_contrataciones = MODULO_CORRESPONDENCIA_CONSULTA_CONTRATACIONES.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_CONSULTA_VISTA_CONTRATACIONES = {
  moduloCC: null,
  modSistcorr: null,
  filtroCC: {},
  dataTableConsulta: null,

  inicializar: function() {
    var ref = this;
    ref.iniciarEventos();
    ref.inicializarcomponentesCC();
    ref.moduloCC.cargarVista();
    ref.iniciarTablaConsulta();
  },

  iniciarEventos: function() {
    var ref = this;

    ref.moduloCC.componentesCC.btnFiltrosCC.click();

    ref.moduloCC.componentesCC.btnExportExcel.click(function() {
      ref.exportarExcel();
    });

    ref.moduloCC.componentesCC.btnBuscar.click(function() {
      ref.buscarContrataciones();
    });

    ref.moduloCC.compCerrarSession.click(function() {
      ref.modSistcorr.cerrarSession();
    });

    ref.moduloCC.componentesCC.btnResetear.click(function() {
      ref.resetarfiltroCCs();
    });

    ref.moduloCC.btnAbrirTutoriales.click(function(event) {
      event.preventDefault();
      var t = $(this);
      window.location.replace("../" + ref.moduloCC.URL_TUTORIALES);
    });
  },

  inicializarcomponentesCC: function() {
    var ref = this;

    ref.moduloCC.componentesCC.cbmTipoProceso.select2()
      .on('select2:select', function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_change($comp.attr('id'))
      }).change(function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_change($comp.attr('id'));
      }).on('select2:open', function(event) {
        var $comp = $(event.currentTarget);
        ref.modSistcorr.select2_open($comp.attr('id'));
      }).on('select2:closing', function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_close($comp.attr('id'));
      });

    ref.moduloCC.componentesCC.cmbDependencia.select2()
      .on('select2:select', function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_change($comp.attr('id'))
      }).change(function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_change($comp.attr('id'));
      }).on('select2:open', function(event) {
        var $comp = $(event.currentTarget);
        ref.modSistcorr.select2_open($comp.attr('id'));
      }).on('select2:closing', function(event) {
        var $comp = $(event.currentTarget);
        if (ref.select2ValorCero($comp)) return;
        ref.modSistcorr.select2_close($comp.attr('id'));
      });
  },

  iniciarTablaConsulta: function() {
    var ref = this;
    ref.obtenerfiltroCCs();
    if (Object.keys(ref.filtroCC).length == 0) {
      ref.inicializarTablaConsultaDefecto();
    } else {
      console.log("actualizar y buscar contrataciones")
      ref.update_form_filtroCCs();
      ref.searchContrataciones();
    }
  },

  resetarfiltroCCs: function() {
    var ref = this;
    ref.filtroCC = {};
    ref.moduloCC.componentesCC.txtNroProceso.val('');
    ref.moduloCC.componentesCC.txtNroProceso.change();
    ref.moduloCC.componentesCC.cbmTipoProceso.val('');
    ref.moduloCC.componentesCC.cbmTipoProceso.change();
    ref.moduloCC.componentesCC.txtNroMemo.val('');
    ref.moduloCC.componentesCC.txtNroMemo.change();
    ref.moduloCC.componentesCC.cmbDependencia.val("0000");
    ref.moduloCC.componentesCC.cmbDependencia.change();
  },

  obtenerfiltroCCs: function() {
    var ref = this;
    var _filtroCC = ref.modSistcorr.getfiltrosConsultaContrataciones();
    ref.filtroCC = _filtroCC;
  },

  select2ValorCero: function(comp) {
    ref = this;
    if (comp.val() == 0) {
      $("#select2-" + comp.attr('id') + "-container").removeClass('select2-seleccionado-sistcorr');
      $("label[for='" + comp.attr('id') + "']").css('color', ref.modSistcorr.colorGrey);
      return true;
    }
    return false;
  },

  update_form_filtroCCs: function() {
    var ref = this;
    ref.moduloCC.componentesCC.txtNroProceso.val(ref.filtroCC.nroProceso);
    ref.moduloCC.componentesCC.txtNroProceso.change();
    ref.moduloCC.componentesCC.cbmTipoProceso.val(ref.filtroCC.tipoProceso);
    ref.moduloCC.componentesCC.cbmTipoProceso.change();
    ref.moduloCC.componentesCC.txtNroMemo.val(ref.filtroCC.nroMemo);
    ref.moduloCC.componentesCC.txtNroMemo.change();
    ref.moduloCC.componentesCC.cmbDependencia.val(ref.filtroCC.codDependencia);
    ref.moduloCC.componentesCC.cmbDependencia.change();
  },

  buscarContrataciones: function() {
    var ref = this;
    ref.filtroCC = {};

    ref.filtroCC.nroProceso = ref.moduloCC.componentesCC.txtNroProceso.val();
    ref.filtroCC.tipoProceso = ref.moduloCC.componentesCC.cbmTipoProceso.val();
    ref.filtroCC.nroMemo = ref.moduloCC.componentesCC.txtNroMemo.val();
    ref.filtroCC.codDependencia = ref.moduloCC.componentesCC.cmbDependencia.val();
    ref.searchContrataciones();

    ref.modSistcorr.setfiltrosConsultaContrataciones(ref.filtroCC);
  },

  searchContrataciones: function() {
    var ref = this;
    ref.modSistcorr.procesar(true);
    ref.inicializarTablaConsulta();
    ref.modSistcorr.procesar(false);
  },

  exportarExcel: function() {
    var ref = this;
    ref.modSistcorr.procesar(true);
    ref.moduloCC.exportarExcel(ref.filtroCC)
      .then(function(respuesta) {
        if (navigator.appVersion.toString().indexOf('.NET') > 0) {
          window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_contrataciones.xlsx');
        } else {
          var a = document.createElement('a');
          a.href = URL.createObjectURL(respuesta);
          a.download = 'reporte_contrataciones.xlsx';
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        }
        ref.modSistcorr.procesar(false);
      }).catch(function(error) {
        ref.modSistcorr.procesar(false);
        ref.modSistcorr.showMessageErrorRequest(error);
      });
  },

  inicializarTablaConsultaDefecto: function() {
    var ref = this;
    ref.dataTableConsulta = ref.moduloCC.componentesCC.dataTableConsulta.DataTable({
      "searching": false,
      "paging": false,
      "info": false,
      "language": {"zeroRecords": "No hay registros"},
      "order": [[1, 'desc']],
      "columnDefs": [{"targets": 0,"orderable": false}]
    });
  },
  
  inicializarTablaConsulta: function() {
    var ref = this;
    if (ref.dataTableConsulta) {
      ref.dataTableConsulta.destroy();
      ref.moduloCC.componentesCC.dataTableConsulta.empty();
      ref.dataTableConsulta = null;
      ref.inicializarTablaConsulta();
    } else {
      ref.moduloCC.componentesCC.dataTableConsulta.show();
      ref.dataTableConsulta = ref.moduloCC.componentesCC.dataTableConsulta.DataTable({
        "dom": DatatableAttachments.domsimple,
        "language": DatatableAttachments.language,
        "processing": true,
        "serverSide": true,
        "responsive": true,
        "ordering": true,
        ajax: {
          "url": ref.moduloCC.URL_CONSULTA_CONTRATACIONES_PAGINADO,
          "type": "GET",
          "data": ref.filtroCC,
          "dataFilter": function(result) {
            if (result != null && result != "null") {
              var response = JSON.parse(result);
              var dtFilter = {
                "draw": Number(response.datos[0].draw),
                "recordsFiltered": Number(response.datos[0].recordsFiltered),
                "recordsTotal": Number(response.datos[0].recordsTotal),
                "data": response.datos[0].listOfDataObjects || []
              }
            } else {
              var dtFilter = {
                "draw": 0,
                "recordsFiltered": 0,
                "recordsTotal": 0,
                "data": []
              };
            }
            return JSON.stringify(dtFilter);
          },
          'error': function(result) {
            console.log("Error Consulta Correspondencia");
            console.log(result);
            ref.modSistcorr.procesar(false);
          }
        },
        cache: true,
        "order": [[1, 'desc']],
        "columns": [
          {data: 'correlativo', title: '', defaultContent: '', orderable: false, render: function(data, type, full){
              return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.nroProceso + "' style='cursor:pointer'></i>"
          }},
          {data: 'nroProceso', title: 'Nro de proceso', defaultContent: ''},
          {data: 'tipoProceso', title: 'Tipo de Proceso', defaultContent: ''},
          {data: 'nroMemo', title: 'Número de Memo', defaultContent: ''},
          {data: 'dependencia', title: 'Dependencia', defaultContent: ''},
          {data: 'personaContacto', title: 'Persona Contacto', defaultContent: ''},
          {data: 'cantBases', title: 'Cantidad de Bases', defaultContent: ''},
          {data: 'valorBase', title: 'Valor Base', defaultContent: ''},
          {data: 'vbFechaHoraInicio', title: 'Venta Base: Fecha y hora Inicio', defaultContent: ''},
          {data: 'vbFechaHoraFin', title: 'Venta Base: Fecha y hora Fin', defaultContent: ''}
        ],
        "initComplete": function(settings, json) {
          setTimeout(function() {
            var order = sessionStorage.getItem("ColOrd_CC");
            console.log("order:" + order);
            if (order != null && order != "undefined") {
              var critOrder = order.split(",");
              console.log("ORDER SESSION:" + parseInt(critOrder[0]) + ",'" + critOrder[1] + "'");
              var colOrd = critOrder[1];
              ref.dataTableConsulta.order([parseInt(critOrder[0]), colOrd]).draw();
            }
          }, 1000);
          setTimeout(function() {
            ref.dataTableConsulta.responsive.rebuild();
            ref.dataTableConsulta.responsive.recalc();
            ref.modSistcorr.procesar(false);
            var nroPag = sessionStorage.getItem("NroPag_CC");
            console.log("NroPag:" + nroPag);
            if (nroPag == null) {
              nroPag = 0;
            }
            var origPag = sessionStorage.getItem("origPag");
            console.log("OrigPag:" + origPag);
            if (origPag == "verDetalleBS") {
              ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
              sessionStorage.removeItem("origPagCC");
            }
          }, 2000);
        }
      });

      ref.dataTableConsulta.on('responsive-display', function(e, datatable, row, showHide, update) {
        ref.updateEventosTabla();
      });

      ref.moduloCC.componentesCC.dataTableConsulta.on('page.dt', function() {
        var pagActual = ref.dataTableConsulta.page.info();
        sessionStorage.setItem("NroPag_CC", pagActual.page);
        setTimeout(function() {
          ref.updateEventosTabla();
        }, 1500);
      });

      ref.moduloCC.componentesCC.dataTableConsulta.on('order.dt', function() {
        var tableOrder = $("#tablaConsultaContrataciones").dataTable();
        var api = tableOrder.api();
        var order = tableOrder.api().order();
        console.log("Order by:" + order);
        if (order != "0,asc") {
          sessionStorage.setItem("ColOrd_CC", order);
        }
        setTimeout(function() {
          ref.updateEventosTabla();
        }, 1500);
      });

      setTimeout(function() {
        ref.updateEventosTabla();
      }, 1500);
    }
  },

  updateEventosTabla: function() {
    var ref = this;
    ref.modSistcorr.eventoTooltip();
    var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
    for (var i = 0; i < allBtnsDetalle.length; i++) {
      allBtnsDetalle[i].addEventListener('click', function() {
        ref.moduloCC.irADetalle(this.dataset.id);
      });
    }
  }
};

setTimeout(function() {
  CORRESPONDENCIA_CONSULTA_VISTA_CONTRATACIONES.moduloCC = modulo_consulta_contrataciones;
  CORRESPONDENCIA_CONSULTA_VISTA_CONTRATACIONES.modSistcorr = modulo_sistcorr;
  CORRESPONDENCIA_CONSULTA_VISTA_CONTRATACIONES.inicializar();
}, 500);