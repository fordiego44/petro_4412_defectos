var modulo_consulta_correspondencia = MODULO_CONSULTA_CORRESPONDENCIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_CORRESPONDENCIA_VISTA = {
		bandeja: [],
		correspondencias: [],
		correspondenciaSeleccionada: null,
		modSistcorr: null,
		modulo: null,
		modFirmaDigital: null,
		errores: [],
		rol_jefe: false,
		rol_gestor: false,
		filtrosBusqueda: null,
		filtro_correlativo: null,
		dataTable: null,
		dataTableConsulta: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			//ref.modulo.compFiltrosBusqueda.html("ABCDEFGHIJ")
			ref.inicializarComponentes();
			ref.iniciarEventos();
			//ref.eventoSeleccionarCorrespondencia();
			//ref.mostrarCorrespondencias();
			//ref.abrirModalBusqueda();
			ref.modSistcorr.eventoTooltip();
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_CONSULTA_CORRESPONDENCIA_AUDITORIA'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.correlativo.val(filtros['correlativo']);
				ref.modulo.filtroBusqueda.correlativo.change();
				ref.modulo.filtroBusqueda.codigoEstado.val(filtros['codigoEstado']);
				ref.modulo.filtroBusqueda.fechaRegistroDesde.val(filtros['fechaRegistroDesde']);
				ref.modulo.filtroBusqueda.fechaRegistroDesde.change();
				ref.modulo.filtroBusqueda.fechaRegistroHasta.val(filtros['fechaRegistroHasta']);
				ref.modulo.filtroBusqueda.fechaRegistroHasta.change();
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(filtros['numeroDocumentoInterno']);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.change();
				ref.modulo.filtroBusqueda.fechaDocumentoInterno.val(filtros['fechaDocumentoInterno']);
				ref.modulo.filtroBusqueda.fechaDocumentoInterno.change();
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val(filtros['codigoDependenciaRemitente']);
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.select2();
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.val(filtros['codigoDependenciaDestino']);
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.select2();
				ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val(filtros['codigoTipoCorrespondencia']);
				ref.modulo.filtroBusqueda.nombreDependenciaExterna.val(filtros['nombreDependenciaExterna']);
				ref.modulo.filtroBusqueda.nombreDependenciaExterna.change();
				ref.modulo.filtroBusqueda.guiaRemision.val(filtros['guiaRemision']);
				ref.modulo.filtroBusqueda.guiaRemision.change();
				ref.modulo.filtroBusqueda.asunto.val(filtros['asunto']);
				ref.modulo.filtroBusqueda.asunto.change();
				let valido = false;
				if(ref.modulo.filtroBusqueda.correlativo.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.codigoEstado.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaRegistroDesde.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaRegistroHasta.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.numeroDocumentoInterno.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaDocumentoInterno.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.codigoDependenciaDestino.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.nombreDependenciaExterna.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.guiaRemision.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.asunto.val() != ""){
					valido = true;
				}
				if(valido){
					setTimeout(function(){
						ref.modulo.compBusqueda.btnBuscar.click();
					},1000);
				}else{
					//ref.inicializarTabla([]);
					ref.inicializarTablaConsultaDefecto();
					//ref.abrirModalBusqueda();
				}
			}else{
				console.log("SIN FILTROS");
				ref.inicializarTablaConsultaDefecto();
				//ref.inicializarTabla([]);
				//ref.abrirModalBusqueda();
			}
			//ref.buscarCorrespondencias();
		},
		
		inicializarComponentes: function(){
			console.log("Vista>>inicializarComponentes");
			var ref = this;
			
			ref.modulo.filtroBusqueda.codigoDependenciaRemitente.select2({
				//data: ref.funcionarios
			}).on('select2:select', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			
			ref.modulo.filtroBusqueda.codigoDependenciaDestino.select2({
				//data: ref.funcionarios
			}).on('select2:select', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
			}).change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_close($comp.attr('id'));
			});
			/*var acciones = ref.modSistcorr.getAcciones();
			ref.modulo.corrEstado.empty();
			ref.modulo.corrEstado.append("<option disabled selected></option>");
			for(var index in acciones){
				var accion = acciones[index];
				ref.modulo.corrEstado.append("<option value='"+accion.codigoAccion+"'>" + accion.accion + "</option>");
			}*/
			//ref.modulo.corrEstado.append("<option value='"+"0"+"'>" + "[SELECCIONE]" + "</option>");
			//ref.modulo.corrEstado.append("<option value='"+"-1"+"'>" + "VALOR PRUEBA" + "</option>");
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.compBusqueda.btnExportExcel.click(function(){
				var procedencia = "NC";
				if(ref.modulo.filtroBusqueda.procedencia.is(':checked')){
					procedencia = "";
				}
				var parametros = {"correlativo": ref.modulo.filtroBusqueda.correlativo.val(), "codigoEstado": ref.modulo.filtroBusqueda.codigoEstado.val(), 
						"fechaRegistroDesde": ref.modulo.filtroBusqueda.fechaRegistroDesde.val(), "fechaRegistroHasta": ref.modulo.filtroBusqueda.fechaRegistroHasta.val(), 
						"numeroDocumentoInterno": ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(), "fechaDocumentoInterno": ref.modulo.filtroBusqueda.fechaDocumentoInterno.val(), 
						"codigoDependenciaRemitente": ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val(), "codigoDependenciaDestino": ref.modulo.filtroBusqueda.codigoDependenciaDestino.val(), 
						"codigoTipoCorrespondencia": ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val(), "nombreDependenciaExterna": ref.modulo.filtroBusqueda.nombreDependenciaExterna.val(), 
						"guiaRemision": ref.modulo.filtroBusqueda.guiaRemision.val(), "asunto": ref.modulo.filtroBusqueda.asunto.val(), "procedencia": procedencia};
				ref.filtrosBusqueda = parametros;
				ref.exportarExcel();
			});
			
			ref.modulo.btnAbrirBusqueda.click(function(event){
				ref.abrirModalBusqueda();
			});
			ref.modulo.panelFiltros.click(function(){
				ref.cambiarPanel();
			});
			ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia.click(function(){
				var comp = $(this);
				var correlativo = comp.attr("data-correlativo");
				ref.verificarDocumentoPrincipal(correlativo);
			});
			
			ref.modulo.compFechaRegistroDesde.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            	ref.modulo.compFechaRegistroDesde.focus();//TICKET 9000004269
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            },
	        });
			
			ref.modulo.btnFechaRegistroDesde.click(function(){
				ref.modulo.compFechaRegistroDesde.focus();
			});
			
			ref.modulo.compFechaRegistroHasta.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            	ref.modulo.compFechaRegistroHasta.focus();//TICKET 9000004269
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            },
	        });
			
			ref.modulo.btnFechaRegistroHasta.click(function(){
				ref.modulo.compFechaRegistroHasta.focus();
			});
			
			/*inicio TICKET 9000004269*/
			ref.modulo.btnFechaDesde.click(function(){
				ref.modulo.compFechaRegistroDesde.focus();
			});
			ref.modulo.lblFechaDocumentoDesde.click(function(){
				ref.modulo.compFechaRegistroDesde.focus();
			});
			ref.modulo.lblCorrelativo.click(function(){
				ref.modulo.filtroBusqueda.correlativo.focus();
			});
			ref.modulo.lblFechaDocumentoHasta.click(function(){
				ref.modulo.compFechaRegistroHasta.focus();
			});
			ref.modulo.btnFechaHasta.click(function(){
				ref.modulo.compFechaRegistroHasta.focus();
			});
			/*fin TICKET 9000004269*/
			
			ref.modulo.compFechaDocumentoInterno.datepicker({
				regional: 'es',
	            firstDay: 7,
	            onClose : function(event){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_close($comp.id);
	            },
	            onSelect: function(){
	            	var $comp = this;
	            	ref.modSistcorr.datePicker_change($comp.id);
	            },
	        });
			
			ref.modulo.btnFechaDocumentoInterno.click(function(){
				ref.modulo.compFechaDocumentoInterno.focus();
			});
			
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				ref.buscarCorrespondencias();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				ref.modulo.filtroBusqueda.correlativo.val("");
				ref.modulo.filtroBusqueda.codigoEstado.val("0");
				ref.modulo.filtroBusqueda.fechaRegistroDesde.val("");
				ref.modulo.filtroBusqueda.fechaRegistroHasta.val("");
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val("");
				ref.modulo.filtroBusqueda.fechaDocumentoInterno.val("");
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val("0");
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.select2();
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.val("0");
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.select2();
				ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val("0");
				ref.modulo.filtroBusqueda.nombreDependenciaExterna.val("");
				ref.modulo.filtroBusqueda.guiaRemision.val("");
				ref.modulo.filtroBusqueda.asunto.val("");
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			ref.modulo.compBusqueda.btnMasFiltros.click(function(){
				ref.masFiltros = true;
				ref.modulo.compBusqueda.btnMasFiltros.hide();
				ref.modulo.compBusqueda.btnMenosFiltros.show();
				ref.modulo.compBusqueda.filtrosSecundarios.show();
				//ref.resetFiltrosSecundarios();
			});
			
			ref.modulo.compBusqueda.btnMenosFiltros.click(function(){
				ref.masFiltros = false;
				ref.modulo.compBusqueda.btnMenosFiltros.hide();
				ref.modulo.compBusqueda.btnMasFiltros.show();
				ref.modulo.compBusqueda.filtrosSecundarios.hide();
			});
			
			ref.modulo.compBusqueda.btnFiltros.click();
			ref.modulo.compBusqueda.btnMenosFiltros.click();
			
			$(".abrir_correspondencia").off('click').on('click', function(){
				e.preventDefault();
				var obj = this;
				var esAsignado = obj.data('esasignado');
				console.log("Asignado:" + esAsignado);
			});
		},
		
		abrirModalBusqueda: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			/*if(ref.filtrosBusqueda.length > 0){
				console.log('tiene filtros');
				for(var i in ref.filtrosBusqueda){
					
					var field = ref.filtrosBusqueda[i];
					if(field.fieldId == 'TD_sAsunto'){
						ref.modulo.compBusqueda.asustoCorrespondencia.val(field.value);
					} else 	if(field.fieldId == 'TD_sAccion'){
						ref.modulo.compBusqueda.tipoAccion.val(field.value);
						ref.modulo.compBusqueda.tipoAccion.change();
					} else if(field.fieldId == 'TD_sRemitente'){
						ref.modulo.compBusqueda.remitente.val(field.value);
					} else if(field.fieldId == 'sNroDocInterno'){
						ref.modulo.compBusqueda.numeroDocumento.val(field.value);
					}
				}
			} else {
				ref.modulo.compBusqueda.asustoCorrespondencia.val("");
				ref.modulo.compBusqueda.tipoAccion.val("");
				ref.modulo.compBusqueda.remitente.val("");
				ref.modulo.compBusqueda.numeroDocumento.val("");
			}*/
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_CONSULTA_CORRESPONDENCIA_AUDITORIA'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.correlativo.val(filtros['correlativo']);
				ref.modulo.filtroBusqueda.codigoEstado.val(filtros['codigoEstado']);
				ref.modulo.filtroBusqueda.fechaRegistroDesde.val(filtros['fechaRegistroDesde']);
				ref.modulo.filtroBusqueda.fechaRegistroDesde.change();
				ref.modulo.filtroBusqueda.fechaRegistroHasta.val(filtros['fechaRegistroHasta']);
				ref.modulo.filtroBusqueda.fechaRegistroHasta.change();
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(filtros['numeroDocumentoInterno']);
				ref.modulo.filtroBusqueda.fechaDocumentoInterno.val(filtros['fechaDocumentoInterno']);
				ref.modulo.filtroBusqueda.fechaDocumentoInterno.change();
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val(filtros['codigoDependenciaRemitente']);
				ref.modulo.filtroBusqueda.codigoDependenciaRemitente.select2();
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.val(filtros['codigoDependenciaDestino']);
				ref.modulo.filtroBusqueda.codigoDependenciaDestino.select2();
				ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val(filtros['codigoTipoCorrespondencia']);
				ref.modulo.filtroBusqueda.nombreDependenciaExterna.val(filtros['nombreDependenciaExterna']);
				ref.modulo.filtroBusqueda.guiaRemision.val(filtros['guiaRemision']);
				ref.modulo.filtroBusqueda.asunto.val(filtros['asunto']);
			}
			$(".panel-1").css('display', 'block');
			$(".panel-2").css('display', 'none');
			$(".labelTextArea.masFiltros").html("Más Filtros");
			ref.modulo.compBusqueda.myModal.modal('show');
			setTimeout(function() {
				console.log('focus');
				ref.modSistcorr.procesar(false);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.change();
				ref.modulo.filtroBusqueda.nombreDependenciaExterna.change();
				ref.modulo.filtroBusqueda.guiaRemision.change();
				ref.modulo.filtroBusqueda.asunto.change();
				document.getElementById(ref.modulo.filtroBusqueda.correlativo.attr('id')).focus();
			}, 1500);
						
		},
		
		cambiarPanel: function(){
			var display = $(".panel-2").css('display');
			if(display == "none"){
				$(".labelTextArea.masFiltros").html("Menos Filtros");
				$(".panel-1").css('display', 'none');
				$(".panel-2").css('display', '');
				$(".icono_mas_filtros").attr('class', 'icono_mas_filtros fa fa-chevron-up pr-2')
			}else{
				$(".labelTextArea.masFiltros").html("Más Filtros");
				$(".panel-1").css('display', '');
				$(".panel-2").css('display', 'none');
				$(".icono_mas_filtros").attr('class', 'icono_mas_filtros fa fa-chevron-down pr-2')
			}
		},
		
		verificarDocumentoPrincipal: function(correlativo){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.obtenerInformacionDocumentoPrincipal(correlativo)
				.then(function(respuesta){
					if(respuesta.estado == true){
						var informacion = respuesta.datos[0];
						var tamanio = informacion.tamano;
						tamanio = tamanio.replace(" MB", "");
						tamanio = tamanio.trim();
						tamanio = Number(tamanio) || 0;
						var tamanioMax = ref.tamanioMaxArchivo;
						if(tamanio > tamanioMax){
							ref.modulo.compModalDescargarArchivo.modal('show');
							ref.modSistcorr.procesar(false);
							ref.modulo.compBtnDescargarArchivo.click(function(){
								ref.modulo.abrirDocumento(informacion.id);
								ref.modulo.compModalDescargarArchivo.modal('hide')
							});
						} else{
							ref.modSistcorr.procesar(false);
							ref.modulo.abrirDocumento(informacion.id);
						}
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			
		},
		
		mostrarCorrespondencias: function(){
			var ref = this;
			var htmlCorrespondencias_ = ref.modulo.htmlCorrespondencias(ref.correspondencias, ref.rol_jefe, ref.rol_gestor);
			if(ref.correspondencias.length == 0){
				ref.modulo.compSinResultados.show();
			} else {
				ref.modulo.compSinResultados.hide();
			}
			ref.modulo.vistaCorrespondencias.html(htmlCorrespondencias_);
			ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
			ref.eventoDocumentoPrincipal();
			ref.eventoSeleccionarCorrespondencia();
			ref.modSistcorr.eventoTooltip();
		},
		
		eventoSeleccionarCorrespondencia: function(){
			var ref = this;
			
			$('.'+ref.modulo.classCompCorrespondencia).click(function(event){
				
				var $columnaTarea = $(event.currentTarget);
				var correlativo = $columnaTarea.data("id");
				$(".promoting-card").removeClass("tarea_seleccionada");
				var divPrincipal = $columnaTarea.parent();
				divPrincipal.addClass("tarea_seleccionada");
				var divFooter = divPrincipal.find(".footer_tarea");
				var estiloDisplay = divFooter.css("display");
				$(".footer_tarea").css("display", "none");
				if(estiloDisplay == "none"){
					divFooter.css("display", "block");
				}else{
					divFooter.css("display", "none");
				}
			});
			
		},
		
		eventoDocumentoPrincipal: function(){
			var ref = this;
			ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-correlativo");
				console.log("CLICK");
				ref.verificarDocumentoPrincipal(correlativo);
			});
		},
		
		buscarCorrespondencias: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.correlativo.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.codigoEstado.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaRegistroDesde.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaRegistroHasta.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.numeroDocumentoInterno.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaDocumentoInterno.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.codigoDependenciaDestino.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.nombreDependenciaExterna.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.guiaRemision.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.asunto.val() != ""){
				valido = true;
			}
			if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				setTimeout(function(){
					ref.modSistcorr.procesar(false);
				}, 500);
			}else{
				var filtrosBusqueda = {
						'correlativo' : ref.modulo.filtroBusqueda.correlativo.val(),
						'codigoEstado' : ref.modulo.filtroBusqueda.codigoEstado.val(),
						'fechaRegistroDesde' : ref.modulo.filtroBusqueda.fechaRegistroDesde.val(),
						'fechaRegistroHasta' : ref.modulo.filtroBusqueda.fechaRegistroHasta.val(),
						'numeroDocumentoInterno' : ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(),
						'fechaDocumentoInterno' : ref.modulo.filtroBusqueda.fechaDocumentoInterno.val(),
						'codigoDependenciaRemitente' : ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val(),
						'codigoDependenciaDestino' : ref.modulo.filtroBusqueda.codigoDependenciaDestino.val(),
						'codigoTipoCorrespondencia' : ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val(),
						'nombreDependenciaExterna' : ref.modulo.filtroBusqueda.nombreDependenciaExterna.val(),
						'guiaRemision' : ref.modulo.filtroBusqueda.guiaRemision.val(),
						'asunto' : ref.modulo.filtroBusqueda.asunto.val(),
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_CONSULTA_CORRESPONDENCIA_AUDITORIA", JSON.stringify(filtrosBusqueda));
				ref.inicializarTablaConsulta();
				/*ref.modulo.buscarCorrespondencias(this.modulo.filtroBusqueda)
					.then(function(respuesta){
						if(respuesta.estado == true){
							ref.correspondencias = respuesta.datos;
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							//ref.mostrarCorrespondencias();
							ref.inicializarTabla(ref.correspondencias);
							ref.modSistcorr.procesar(false);
							ref.modulo.compBusqueda.myModal.modal('hide');
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						ref.modulo.compBusqueda.myModal.modal('hide');
						//ref.mostrarCorrespondencias();
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});*/
			}
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtrosBusqueda)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_correspondencia.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_consulta_correspondencia.xlsx';
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
		
		inicializarTablaConsulta: function(){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.compBusqueda.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				var procedencia = "NC";
				if(ref.modulo.filtroBusqueda.procedencia.is(':checked')){
					procedencia = "";
				}
				ref.modulo.filtroBusqueda.valProcedencia = procedencia;
				ref.modulo.compBusqueda.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.compBusqueda.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_BUSCAR_CORRESPONDENCIAS_CONSULTA_AUDITORIA,
			        	"type": "GET",
			        	"data": {
				        		'correlativo' : ref.modulo.filtroBusqueda.correlativo.val(),
								'codigoEstado' : ref.modulo.filtroBusqueda.codigoEstado.val(),
								'fechaRegistroDesde' : ref.modulo.filtroBusqueda.fechaRegistroDesde.val(),
								'fechaRegistroHasta' : ref.modulo.filtroBusqueda.fechaRegistroHasta.val(),
								'numeroDocumentoInterno' : ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(),
								'fechaDocumentoInterno' : ref.modulo.filtroBusqueda.fechaDocumentoInterno.val(),
								'codigoDependenciaRemitente' : ref.modulo.filtroBusqueda.codigoDependenciaRemitente.val(),
								'codigoDependenciaDestino' : ref.modulo.filtroBusqueda.codigoDependenciaDestino.val(),
								'codigoTipoCorrespondencia' : ref.modulo.filtroBusqueda.codigoTipoCorrespondencia.val(),
								'nombreDependenciaExterna' : ref.modulo.filtroBusqueda.nombreDependenciaExterna.val(),
								'guiaRemision' : ref.modulo.filtroBusqueda.guiaRemision.val(),
								'asunto' : ref.modulo.filtroBusqueda.asunto.val(),
								'procedencia' : ref.modulo.filtroBusqueda.valProcedencia
			        	},
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
			        			var dtFilter = {
			        				"draw": Number(response.datos[0].draw),
			        				"recordsFiltered": Number(response.datos[0].recordsFiltered),
			        				"recordsTotal": Number(response.datos[0].recordsTotal),
			        				"data": response.datos[0].listOfDataObjects || []
			        			}
			        		}else{
			        			var dtFilter = {
			        					"draw": 0,
				        				"recordsFiltered": 0,
				        				"recordsTotal": 0,
				        				"data": []
			        			};
			        		}
			        		return JSON.stringify( dtFilter );
			        	},
			        	'error': function(result){
			        		console.log("Error Consulta Correspondencia");
			        		console.log(result);
			        		ref.modSistcorr.procesar(false);
			        		//location.reload();
			        	}
			        },
			        cache: true,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id_correspondencia', title: '', defaultContent: '', render: function(data, type, full){
			        		if(full.tipoIcono == 'NC'){
			        			return "<i class='fas fa-arrow-alt-circle-right' style='color: #008000; font-size: 24px;'></i>"
			        		}
			        		if(full.tipoIcono == "EM"){
			        			return "<i class='fas fa-arrow-alt-circle-left' style='color: #FF0000; font-size: 24px;'></i>"
			        		}
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc. P.', defaultContent: '', render: function(data, type, full){
			        		if(ref.rol_jefe == true || (ref.rol_gestor == true && (!(full.esConfidencial == "SI" && (full.esAsignado == null || full.esAsignado == "NO" || full.esAsignado == "")))) || (ref.rol_jefe == false && ref.rol_gestor == false && (full.esAsignado == "SI" || full.esConfidencial == "NO"))){
			        			return "<i class='fas fa-eye icon_view icon_add_document btnVerificarDocPrincipal_Correspondencia' data-correlativo='" + full.correlativo + "' data-toggle='tooltip' title='Ver documento principal'></i>"
			        		}
			        		return '';
			        	}},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-auditoria/informacion/corr/" + full.correlativo + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        	}},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'numeroDocumentoInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRadicadoProc', title: 'Fecha Recepción', defaultContent: '', render: function(data, type, full){
			        		return '<span style="display:none;">'+full.fechaRadicado+'</span>' + full.fechaRadicadoProc
			        	}},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo Correspondencia', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_CCA");
			        		if(order!=null){
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
			        		var nroPag = sessionStorage.getItem("NroPag_CCA");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPag");
			        		console.log("OrigPag:" + origPag);
			        		if(origPag == "verDetalle"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPag");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CCA", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaConsultasCorrespondencias").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					//var api = ref.dataTableConsulta.api();
					//var order = ref.dataTableConsulta.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CCA", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		inicializarTabla: function(data){
			var ref = this;
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.compBusqueda.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.compBusqueda.dataTable.show();
				ref.dataTable = ref.modulo.compBusqueda.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'id_correspondencia', title: '', defaultContent: '', render: function(data, type, full){
			        		if(full.tipoIcono == 'NC'){
			        			return "<i class='fas fa-arrow-alt-circle-right' style='color: #008000; font-size: 24px;'></i>"
			        		}
			        		if(full.tipoIcono == "EM"){
			        			return "<i class='fas fa-arrow-alt-circle-left' style='color: #FF0000; font-size: 24px;'></i>"
			        		}
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc. P.', defaultContent: '', render: function(data, type, full){
			        		if(ref.rol_jefe == true || (ref.rol_gestor == true && (!(full.esConfidencial == "SI" && (full.esAsignado == null || full.esAsignado == "NO" || full.esAsignado == "")))) || (ref.rol_jefe == false && ref.rol_gestor == false && (full.esAsignado == "SI" || full.esConfidencial == "NO"))){
			        			return "<i class='fas fa-eye icon_view icon_add_document btnVerificarDocPrincipal_Correspondencia' data-correlativo='" + full.correlativo + "' data-toggle='tooltip' title='Ver documento principal'></i>"
			        		}
			        		return '';
			        	}},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-auditoria/informacion/corr/" + full.correlativo + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        	}},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'numeroDocumentoInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRadicadoProc', title: 'Fecha Recepción', defaultContent: '', render: function(data, type, full){
			        		return '<span style="display:none;">'+full.fechaRadicado+'</span>' + full.fechaRadicadoProc
			        	}},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo Correspondencia', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },
			        "columnDefs": [{
			        	"targets": [0, 1, 2],
			        	"orderable": false
			        }],

				});
				
				ref.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTable.on( 'page.dt', function () {
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTable.on( 'order.dt', function () {
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		inicializarTablaConsultaDefecto: function(data){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.modulo.compBusqueda.dataTableConsulta.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.compBusqueda.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.compBusqueda.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "autoWidth": true,
			        "columns": [
			        	{data: 'id_correspondencia', title: '', defaultContent: '', render: function(data, type, full){
			        		if(full.tipoIcono == 'NC'){
			        			return "<i class='fas fa-arrow-alt-circle-right' style='color: #008000; font-size: 24px;'></i>"
			        		}
			        		if(full.tipoIcono == "EM"){
			        			return "<i class='fas fa-arrow-alt-circle-left' style='color: #FF0000; font-size: 24px;'></i>"
			        		}
			        	}},
			        	{data: 'id_correspondencia', title: 'Doc. P.', defaultContent: '', render: function(data, type, full){
			        		if(ref.rol_jefe == true || (ref.rol_gestor == true && (!(full.esConfidencial == "SI" && (full.esAsignado == null || full.esAsignado == "NO" || full.esAsignado == "")))) || (ref.rol_jefe == false && ref.rol_gestor == false && (full.esAsignado == "SI" || full.esConfidencial == "NO"))){
			        			return "<i class='fas fa-eye icon_view icon_add_document btnVerificarDocPrincipal_Correspondencia' data-correlativo='" + full.correlativo + "' data-toggle='tooltip' title='Ver documento principal'></i>"
			        		}
			        		return '';
			        	}},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-auditoria/informacion/corr/" + full.correlativo + "?workflow=' data-esAsignado='" + full.esAsignado + "' data-toggle='tooltip' class='abrir_correspondencia' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        	}},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'numeroDocumentoInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaRadicadoProc', title: 'Fecha Recepción', defaultContent: '', render: function(data, type, full){
			        		return '<span style="display:none;">'+full.fechaRadicado+'</span>' + full.fechaRadicadoProc
			        	}},
			        	{data: 'origen', title: 'Remitente', defaultContent: ''},
			        	{data: 'destino', title: 'Destino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'tipoCorrespondencia', title: 'Tipo Correspondencia', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		console.log("INIT COMPLETE");
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },
			        "columnDefs": [{
			        	"targets": [0, 1, 2],
			        	"orderable": false
			        }],

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'page.dt', function () {
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'order.dt', function () {
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalCorrespondencia = $(".btnVerificarDocPrincipal_Correspondencia");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					//ref.modulo.irADetalle(this.dataset.id);
				});
			}
		}
		
};

setTimeout(function(){
	CONSULTA_CORRESPONDENCIA_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTA_CORRESPONDENCIA_VISTA.modulo = modulo_consulta_correspondencia;
	CONSULTA_CORRESPONDENCIA_VISTA.rol_jefe = ES_JEFE;
	CONSULTA_CORRESPONDENCIA_VISTA.rol_gestor = ES_GESTOR;
	CONSULTA_CORRESPONDENCIA_VISTA.inicializar();
}, 500);