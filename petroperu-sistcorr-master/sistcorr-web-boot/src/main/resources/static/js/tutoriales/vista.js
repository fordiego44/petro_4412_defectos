var modulo_consulta_correspondencia = MODULO_CORRESPONDENCIA_CONSULTA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_CONSULTA_VISTA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		masFiltros: false,
		dataTable: null,
		dependenciasUsuario: [],
		URL_DESCARGAR_ARCHIVO: 'descargar/tutorial/',
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarComponentes();
			ref.iniciarEventos();
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			$("." + ref.modulo.compAbrirArchivo).click(function(){
				var t = $(this);
				var valor = t.data('download');
				console.log(valor);
				let a = window.open(ref.URL_DESCARGAR_ARCHIVO + valor, '_blank');
				setTimeout(function() {
					a.close();
				}, 3000);
				
			});
			
			ref.modulo.compRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
			
		},
		
		inicializarComponentes: function(){
			var ref = this;
		},
		
		update_form_filtros: function(){
			var ref = this;
			
			ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', ref.filtro.considerarOriginadora );
			
			if(ref.filtro.codDependenciaOriginadora){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDependenciaOriginadora, "text": ref.filtro.nombreDependenciaOriginadora}]);
				ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ ref.filtro.codDependenciaOriginadora +"' selected='selected'>" + ref.filtro.nombreDependenciaOriginadora + "</option>");	
				ref.modulo.componentes.cmbDependenciaOriginadora.change();	
			} 
			
		
			if(ref.filtro.codDependenciaRemitente){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDependenciaRemitente, "text": ref.filtro.nombreDependenciaRemitente}]);
				ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='"+ ref.filtro.codDependenciaRemitente +"' selected='selected'>" + ref.filtro.nombreDependenciaRemitente + "</option>");
				ref.modulo.componentes.cmbDependenciaRemitente.change();
			} else {
				ref.seleccionarDependenciaDefault();
			}
			
			ref.modulo.componentes.txtCorrelativo.val(ref.filtro.correlativo);
			ref.modulo.componentes.txtCorrelativo.change();
			ref.modulo.componentes.txtAsunto.val(ref.filtro.asunto);
			ref.modulo.componentes.txtAsunto.change();
			if(ref.filtro.estado){
				ref.modulo.componentes.cbmEstado.val(ref.filtro.estado);
			}
			
			
			if(ref.filtro.codNombreOriginador){
				ref.listas.originadores.agregarLista([{"id" : ref.filtro.codNombreOriginador, "text": ref.filtro.nombreOriginador}]);
				ref.modulo.componentes.cmbNombreOriginador.append("<option value='"+ ref.filtro.codNombreOriginador +"' selected='selected'>" + ref.filtro.nombreOriginador + "</option>");
				ref.modulo.componentes.cmbNombreOriginador.change();
			}
			
			
			ref.modulo.componentes.txtFechaDocumentoDesde.val(ref.filtro.fechaDesde);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(ref.filtro.fechaHasta);
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			if(ref.filtro.tipoCorrespondencia){
				ref.modulo.componentes.cbmTipoCorrespondencia.val(ref.filtro.tipoCorrespondencia);
			}
			
			ref.modulo.componentes.cbmTipoEmision.val(ref.filtro.tipoEmision);
			ref.modulo.componentes.cbmTipoEmision.change();
			
			
			$("input[name='rbtnTipoDestinatario'][value='"+ref.filtro.destinatarioNacional+"']").prop('checked', true);
			$("input:radio[name='rbtnTipoDestinatario']").change();
			
			
			ref.modulo.componentes.cbmFlujoFirma.val(ref.filtro.firmaDigital);
			ref.modulo.componentes.cbmFlujoFirma.change();
			
			ref.modulo.componentes.cbmConfidencialidad.val(ref.filtro.confidencial);
			ref.modulo.componentes.cbmConfidencialidad.change();
			
			ref.modulo.componentes.cbmUrgente.val(ref.filtro.urgente);
			ref.modulo.componentes.cbmUrgente.change();
			
			ref.modulo.componentes.cbmDespachoFisico.val(ref.filtro.despachoFisico);
			ref.modulo.componentes.cbmDespachoFisico.change();
			
			
			if(ref.filtro.codDestinatario){
				if(ref.filtro.tipoEmision == 0){
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val('');
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
					ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val('');
					ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val('');
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
				} else if(ref.filtro.tipoEmision == 1) { // DEstinatario Interno
					ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
					ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
				} else { // Destinatario Externo
					if(ref.filtro.destinatarioNacional == true){ //NAcional
						ref.listas.dependencias_ext.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
						ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
						ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
					} else { // Internacional
						ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
						ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
					}	
				}				
			}
			
			if(!ref.filtro.codDestinatario && ref.filtro.nombreDestinatario){
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			}
			
			if(ref.filtro.codCopia){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codCopia, "text": ref.filtro.nombreCopia}]);
				ref.modulo.componentes.cbmDependenciaCopia.append("<option value='"+ ref.filtro.codCopia +"' selected='selected'>" + ref.filtro.nombreCopia + "</option>");
				ref.modulo.componentes.cbmDependenciaCopia.change();
			}		
			
		}, 
		
		
		buscarCorrespondencias: function(){
			var ref = this;
			ref.filtro = {};
			
			ref.filtro.considerarOriginadora = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
			
			ref.filtro.codDependenciaOriginadora = ref.modulo.componentes.cmbDependenciaOriginadora.val();
			if(ref.filtro.codDependenciaOriginadora){
				ref.filtro.nombreDependenciaOriginadora = ref.listas.dependencias.buscarPorId(ref.filtro.codDependenciaOriginadora).text;
			}
			ref.filtro.codDependenciaRemitente = ref.modulo.componentes.cmbDependenciaRemitente.val();
			if(ref.filtro.codDependenciaRemitente){
				if(ref.filtro.codDependenciaRemitente == '0000'){
					ref.filtro.codDependenciaRemitente = null; 
				} else {
					ref.filtro.nombreDependenciaRemitente = ref.listas.dependencias.buscarPorId(ref.filtro.codDependenciaRemitente).text;
				}
			}
			ref.filtro.correlativo = ref.modulo.componentes.txtCorrelativo.val();
			ref.filtro.asunto = ref.modulo.componentes.txtAsunto.val();
			ref.filtro.estado = ref.modulo.componentes.cbmEstado.val() == 0 ? '' : ref.modulo.componentes.cbmEstado.val();
			ref.filtro.masFiltros = ref.masFiltros;
			
			if(ref.filtro.masFiltros == true) {
				ref.filtro.codNombreOriginador = ref.modulo.componentes.cmbNombreOriginador.val();
				if(ref.filtro.codNombreOriginador){
					ref.filtro.nombreOriginador = ref.listas.originadores.buscarPorId(ref.filtro.codNombreOriginador).text;
				}
				
				ref.filtro.fechaDesde = ref.modulo.componentes.txtFechaDocumentoDesde.val();
				ref.filtro.fechaHasta = ref.modulo.componentes.txtFechaDocumentoHasta.val();
				ref.filtro.tipoCorrespondencia = ref.modulo.componentes.cbmTipoCorrespondencia.val();
				ref.filtro.tipoEmision = ref.modulo.componentes.cbmTipoEmision.val();
				if(ref.filtro.tipoEmision == 0) {
					ref.filtro.destinatarioNacional = true;
				} else if(ref.filtro.tipoEmision == 1){
					ref.filtro.destinatarioNacional = true;
					ref.filtro.codDestinatario = ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val();
					if(ref.filtro.codDestinatario){
						ref.filtro.nombreDestinatario = ref.listas.dependencias.buscarPorId(ref.filtro.codDestinatario).text;
					}
				} else {
					 var destinatarioNacional = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
					 destinatarioNacional = destinatarioNacional == 'true' ? true : false;
					ref.filtro.destinatarioNacional = destinatarioNacional;
					if(ref.filtro.destinatarioNacional == true){
						ref.filtro.codDestinatario = ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val();
						if(ref.filtro.codDestinatario){
							ref.filtro.nombreDestinatario = ref.listas.dependencias_ext.buscarPorId(ref.filtro.codDestinatario).text;
						}
					} else {
						ref.filtro.nombreDestinatario = ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val();
					}
				}
				
			
				
				
				ref.filtro.firmaDigital = ref.modulo.componentes.cbmFlujoFirma.val()
				ref.filtro.confidencial = ref.modulo.componentes.cbmConfidencialidad.val();
				ref.filtro.urgente = ref.modulo.componentes.cbmUrgente.val();
				ref.filtro.despachoFisico = ref.modulo.componentes.cbmDespachoFisico.val();
				
				ref.filtro.codCopia = ref.modulo.componentes.cbmDependenciaCopia.val();
				if(ref.filtro.codCopia){
					ref.filtro.nombreCopia = ref.listas.dependencias.buscarPorId(ref.filtro.codCopia).text;
				}
			}
			
			ref.searchCorrespondencias();
			
			ref.modSistcorr.setFiltrosConsultaCorrespondencia(ref.filtro);
		},
		
		searchCorrespondencias: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			if(ref.filtro.considerarOriginadora == false && ref.filtro.codDependenciaRemitente == null){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una dependencia remitente', "Error");
				ref.inicializarTabla([]);
				return;
			}
			ref.modulo.consultar(ref.filtro)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.inicializarTabla(respuesta.datos || []);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.inicializarTabla([]);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtro)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_correspondencia.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_correspondencia.xlsx';
	                    document.body.appendChild(a);
	                    a.click();
	                    document.body.removeChild(a);
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		inicializarTabla: function(data){
			var ref = this;
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.componentes.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.componentes.dataTable.show();
				ref.dataTable = ref.modulo.componentes.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fa-list-alt icon_view_detail'  data-toggle='tooltip' title='Clic para ver detalle' data-id='" + full.id_correspondencia +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'correlativo', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 400) {
			        			max = 150;
			        		} else {
			        			max = 15;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        	}},
			        	{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: '', render: function(data, type, full){
			        		console.log(full);
			        		var dest = "";
			        		if(full.cantidad<=1){
			        			dest = full.destinatario_dependencia;
			        		}else{
			        			dest = full.destinatario_dependencia + " <strong class='espera'>(*)</strong>";
			        		}
			        		return dest;
			        	}},
			        	//{data: 'destinatario_dependencia', title: 'Destinatario', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dep. Remitente', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaDocumento', title: 'Fecha Documento', defaultContent: ''},
			        	{data: 'dependenciaOriginadora', title: 'Dep. Originadora', defaultContent: ''},
			        	{data: 'originador', title: 'Nom. Originador', defaultContent: ''},
			        	{data: 'lugarTrabajo', title: 'Centro. Gest. Corresp.', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo de Corresp.', defaultContent: ''},
			        	{data: 'emision_nombre', title: 'Tipo de Emisión', defaultContent: ''},
			        	{data: 'firmaDigital', title: 'Flujo de Firma', defaultContent: '', render: function(data, type, full){
			        		if(full.firmaDigital == true){
			        			return "DIGITAL";
			        		}
			        		return "MANUAL";
			        	}},
			        	{data: 'despachoFisico', title: 'Despacho Físico', defaultContent: '', render: function(data, type, full){
			        		if(full.despachoFisico == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'urgente', title: 'Urgente', defaultContent: '', render: function(data, type, full){
			        		if(full.urgente == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'confidencial', title: 'Confidencial', defaultContent: '', render: function(data, type, full){
			        		if(full.confidencial == true){
			        			return "SI";
			        		}
			        		return "NO";
			        	}},
			        	{data: 'destinatario_cgc', title: 'CGC Destinatario', defaultContent: ''},
			        	{data: 'copia_dependencia', title: 'Copia', defaultContent: ''},
			        	{data: 'copia_cgc', title: 'CGC Copia', defaultContent: ''},
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.modulo.componentes.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.modulo.irADetalle(this.dataset.id);
				});
			}
		}
};

setTimeout(function() {
	CORRESPONDENCIA_CONSULTA_VISTA.modulo = modulo_consulta_correspondencia;
	CORRESPONDENCIA_CONSULTA_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_CONSULTA_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CORRESPONDENCIA_CONSULTA_VISTA.listas = {};
	CORRESPONDENCIA_CONSULTA_VISTA.listas.dependencias = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA.listas.originadores = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	CORRESPONDENCIA_CONSULTA_VISTA.inicializar();
}, 200);