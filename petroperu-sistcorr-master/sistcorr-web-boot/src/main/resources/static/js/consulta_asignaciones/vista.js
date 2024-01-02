var modulo_consulta_asignaciones = MODULO_CONSULTA_ASIGNACIONES.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONSULTA_ASIGNACIONES_VISTA = {
		bandeja: [],
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
			//ref.mostrarAsignaciones();
			//ref.eventoSeleccionarCorrespondencia();
			ref.modSistcorr.eventoTooltip();
			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_CONSULTA_ASIGNACION'));
			if(filtros!=null){
				ref.modulo.filtroBusqueda.correlativo.val(filtros['correlativo']);
				ref.modulo.filtroBusqueda.correlativo.change();
				ref.modulo.filtroBusqueda.codigoEstado.val(filtros['codigoEstado']);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(filtros['numeroDocumentoInterno']);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.change();
				ref.modulo.filtroBusqueda.fechaAsignacionDesde.val(filtros['fechaAsignacionDesde']);
				ref.modulo.filtroBusqueda.fechaAsignacionDesde.change();
				ref.modulo.filtroBusqueda.fechaAsignacionHasta.val(filtros['fechaAsignacionHasta']);
				ref.modulo.filtroBusqueda.fechaAsignacionHasta.change();
				ref.modulo.filtroBusqueda.tipoAccion.val(filtros['tipoAccion']);
				ref.modulo.filtroBusqueda.dependenciaAsignante.val(filtros['dependenciaAsignante']);
				ref.modulo.filtroBusqueda.dependenciaAsignante.select2();
				ref.modulo.filtroBusqueda.personaAsignada.val(filtros['personaAsignada']);
				ref.modulo.filtroBusqueda.personaAsignada.select2();
				ref.modulo.filtroBusqueda.fechaVencimientoDesde.val(filtros['fechaVencimientoDesde']);
				ref.modulo.filtroBusqueda.fechaVencimientoDesde.change();
				ref.modulo.filtroBusqueda.fechaVencimientoHasta.val(filtros['fechaVencimientoHasta']);
				ref.modulo.filtroBusqueda.fechaVencimientoHasta.change();
				let valido = false;
				if(ref.modulo.filtroBusqueda.correlativo.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.codigoEstado.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.numeroDocumentoInterno.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaAsignacionDesde.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaAsignacionHasta.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.tipoAccion.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.dependenciaAsignante.val() != "0"){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.personaAsignada.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaVencimientoDesde.val() != ""){
					valido = true;
				}
				if(ref.modulo.filtroBusqueda.fechaVencimientoHasta.val() != ""){
					valido = true;
				}
				if(valido){
					setTimeout(function(){
						ref.modulo.compBusqueda.btnBuscar.click();
					},1000);
				}else{
					//ref.inicializarTabla([]);
					//ref.abrirModalBusqueda();
					ref.inicializarTablaConsulta();
				}
			}else{
				setTimeout(function(){
					console.log("INICIALIZANDO TABLA");
					//ref.inicializarTabla([]);
					ref.inicializarTablaConsulta();
				}, 1000)
				
				//ref.abrirModalBusqueda();
			}
			
		},
		
		inicializarComponentes: function(){
			console.log("Vista>>inicializarComponentes");
			var ref = this;
			
			ref.modulo.filtroBusqueda.dependenciaAsignante.select2({
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
			
			ref.modulo.filtroBusqueda.personaAsignada.select2({
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
			ref.modulo.compBusqueda.tipoAccion.empty();
			ref.modulo.compBusqueda.tipoAccion.append("<option selected value=''>Seleccione</option>");
			for(var index in acciones){
				var accion = acciones[index];
				ref.modulo.compBusqueda.tipoAccion.append("<option value='"+accion.codigoAccion+"'>" + accion.accion + "</option>");
			}*/
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.compBusqueda.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.modulo.btnAbrirBusqueda.click(function(event){
				console.log("Abrir modal");
				//ref.abrirModalBusqueda();
			});
			ref.modulo.panelFiltros.click(function(){
				ref.cambiarPanel();
			});
			
			ref.modulo.btnVerificarDocumentoPrincipalAsignacion.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-asignacion");
				ref.verificarDocumentoPrincipal(correlativo);
			});
			
			ref.modulo.asigFechaAsignacionDesde.datepicker({
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
			
			ref.modulo.btnFechaAsignacionDesde.click(function(){
				ref.modulo.asigFechaAsignacionDesde.focus();
			});
			
			/*inicio TICKET 9000004269*/
			ref.modulo.lblAsigCorrelativo.click(function(){
				ref.modulo.filtroBusqueda.correlativo.focus();
			});
			/*inicio TICKET 9000004269*/
			
			ref.modulo.asigFechaAsignacionHasta.datepicker({
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
			
			ref.modulo.btnFechaAsignacionHasta.click(function(){
				ref.modulo.asigFechaAsignacionHasta.focus();
			});
			
			ref.modulo.asigFechaVencimientoDesde.datepicker({
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
			
			ref.modulo.btnFechaVencimientoDesde.click(function(){
				ref.modulo.asigFechaVencimientoDesde.focus();
			});
			
			ref.modulo.asigFechaVencimientoHasta.datepicker({
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
			
			ref.modulo.btnFechaVencimientoHasta.click(function(){
				ref.modulo.asigFechaVencimientoHasta.focus();
			});
			
			ref.modulo.compBusqueda.btnBuscar.click(function(){
				//ref.validarCampos();
				ref.buscarAsignaciones();
			});
			
			ref.modulo.compBusqueda.btnLimpiar.click(function(){
				ref.modulo.filtroBusqueda.correlativo.val("");
				ref.modulo.filtroBusqueda.codigoEstado.val("0");
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val("");
				ref.modulo.filtroBusqueda.fechaAsignacionDesde.val("");
				ref.modulo.filtroBusqueda.fechaAsignacionHasta.val("");
				ref.modulo.filtroBusqueda.tipoAccion.val("");
				ref.modulo.filtroBusqueda.dependenciaAsignante.val("0");
				ref.modulo.filtroBusqueda.dependenciaAsignante.select2();
				ref.modulo.filtroBusqueda.personaAsignada.val("");
				ref.modulo.filtroBusqueda.personaAsignada.select2();
				ref.modulo.filtroBusqueda.fechaVencimientoDesde.val("");
				ref.modulo.filtroBusqueda.fechaVencimientoHasta.val("");
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

			var filtros = JSON.parse(sessionStorage.getItem('FILTROS_CONSULTA_ASIGNACION'));
			if(filtros!=null){

				ref.modulo.filtroBusqueda.correlativo.val(filtros['correlativo']);
				ref.modulo.filtroBusqueda.codigoEstado.val(filtros['codigoEstado']);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(filtros['numeroDocumentoInterno']);
				ref.modulo.filtroBusqueda.fechaAsignacionDesde.val(filtros['fechaAsignacionDesde']);
				ref.modulo.filtroBusqueda.fechaAsignacionDesde.change();
				ref.modulo.filtroBusqueda.fechaAsignacionHasta.val(filtros['fechaAsignacionHasta']);
				ref.modulo.filtroBusqueda.fechaAsignacionHasta.change();
				ref.modulo.filtroBusqueda.tipoAccion.val(filtros['tipoAccion']);
				ref.modulo.filtroBusqueda.dependenciaAsignante.val(filtros['dependenciaAsignante']);
				ref.modulo.filtroBusqueda.dependenciaAsignante.select2();
				ref.modulo.filtroBusqueda.personaAsignada.val(filtros['personaAsignada']);
				ref.modulo.filtroBusqueda.personaAsignada.select2();
				ref.modulo.filtroBusqueda.fechaVencimientoDesde.val(filtros['fechaVencimientoDesde']);
				ref.modulo.filtroBusqueda.fechaVencimientoDesde.change()
				ref.modulo.filtroBusqueda.fechaVencimientoHasta.val(filtros['fechaVencimientoHasta']);
				ref.modulo.filtroBusqueda.fechaVencimientoHasta.change();
			}
			
			$(".panel-1").css('display', 'block');
			$(".panel-2").css('display', 'none');
			$(".labelTextArea.masFiltros").html("Más Filtros");
			ref.modulo.compBusqueda.myModal.modal('show');
			setTimeout(function() {
				console.log('focus');
				ref.modSistcorr.procesar(false);
				ref.modulo.filtroBusqueda.numeroDocumentoInterno.change();
				document.getElementById(ref.modulo.filtroBusqueda.correlativo.attr('id')).focus();
				/*document.getElementById(ref.modulo.filtroBusqueda.fechaAsignacionDesde.attr('id')).focus();
				$("#ui-datepicker-div").hide();
				setTimeout(function(){
					document.getElementById(ref.modulo.filtroBusqueda.fechaAsignacionHasta.attr('id')).focus();
					$("#ui-datepicker-div").hide();
					setTimeout(function(){
						$("#ui-datepicker-div").hide();
						document.getElementById(ref.modulo.filtroBusqueda.correlativo.attr('id')).focus();
					},50);
				},0);*/
			}, 1000);
						
		},
		
		cambiarPanel: function(){
			var ref = this;
			var display = $(".panel-2").css('display');
			if(display == "none"){
				$(".labelTextArea.masFiltros").html("Menos Filtros");
				$(".panel-1").css('display', 'none');
				$(".panel-2").css('display', '');
				//document.getElementById(ref.modulo.filtroBusqueda.fechaVencimientoDesde.attr('id')).focus();
				$("#ui-datepicker-div").hide();
				//document.getElementById(ref.modulo.filtroBusqueda.fechaVencimientoHasta.attr('id')).focus();
				//setTimeout(function(){$("#ui-datepicker-div").hide();},50);
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
		
		mostrarAsignaciones: function(){
			var ref = this;
			var htmlAsignaciones = ref.modulo.htmlAsignaciones(ref.asignaciones, ref.rol_jefe, ref.rol_gestor);
			if(ref.asignaciones.length == 0){
				ref.modulo.compSinResultados.show();
			} else {
				ref.modulo.compSinResultados.hide();
			}
			//ref.modulo.vistaAsignaciones.attr("style", "display:none");
			ref.modulo.vistaAsignaciones.html(htmlAsignaciones);
			ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
			ref.eventoDocumentoPrincipal();
			ref.eventoSeleccionarCorrespondencia();
			ref.modSistcorr.eventoTooltip();
			/*setTimeout(function(){
				ref.modulo.vistaAsignaciones.attr("style", "display:block");
			}, 500);*/
		},
		
		eventoSeleccionarCorrespondencia: function(){
			var ref = this;
			
			$('.'+ref.modulo.classCompCorrespondencia).click(function(event){
				
				var $columnaTarea = $(event.currentTarget);
				var correlativo = $columnaTarea.data("id");
				$(".promoting-card").removeClass("tarea_seleccionada");
				//$("#CORRESPONDENCIA_" + correlativo).addClass("tarea_seleccionada");
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
			ref.modulo.btnVerificarDocumentoPrincipalAsignacion.off('click').on('click', function(){
				var comp = $(this);
				var correlativo = comp.attr("data-asignacion");
				ref.verificarDocumentoPrincipal(correlativo);
			});
		},
		
		buscarAsignaciones: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			let valido = false;
			if(ref.modulo.filtroBusqueda.correlativo.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.codigoEstado.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.numeroDocumentoInterno.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaAsignacionDesde.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaAsignacionHasta.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.tipoAccion.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.dependenciaAsignante.val() != "0"){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.personaAsignada.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaVencimientoDesde.val() != ""){
				valido = true;
			}
			if(ref.modulo.filtroBusqueda.fechaVencimientoHasta.val() != ""){
				valido = true;
			}
			if(!valido){
				ref.modSistcorr.notificar("ERROR", "Ingrese al menos un criterio de búsqueda", "Error");
				ref.modSistcorr.procesar(false);
			}else{
				var filtrosBusqueda = {
						'correlativo' : ref.modulo.filtroBusqueda.correlativo.val(),
						'codigoEstado' : ref.modulo.filtroBusqueda.codigoEstado.val(),
						'numeroDocumentoInterno' : ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(),
						'fechaAsignacionDesde' : ref.modulo.filtroBusqueda.fechaAsignacionDesde.val(),
						'fechaAsignacionHasta' : ref.modulo.filtroBusqueda.fechaAsignacionHasta.val(),
						'tipoAccion' : ref.modulo.filtroBusqueda.tipoAccion.val(),
						'dependenciaAsignante' : ref.modulo.filtroBusqueda.dependenciaAsignante.val(),
						'personaAsignada' : ref.modulo.filtroBusqueda.personaAsignada.val(),
						'fechaVencimientoDesde' : ref.modulo.filtroBusqueda.fechaVencimientoDesde.val(),
						'fechaVencimientoHasta' : ref.modulo.filtroBusqueda.fechaVencimientoHasta.val(),
				}
				ref.filtrosBusqueda = filtrosBusqueda;
				sessionStorage.setItem("FILTROS_CONSULTA_ASIGNACION", JSON.stringify(filtrosBusqueda));
				ref.inicializarTablaConsulta();
				/*ref.modulo.buscarAsignaciones(this.modulo.filtroBusqueda)
					.then(function(respuesta){
						console.log("Respuesta.estado:" + respuesta.estado)
						if(respuesta.estado == true){
							ref.asignaciones = respuesta.datos;
							console.log("Datos:" + respuesta.datos.length);
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							//ref.mostrarAsignaciones();
							ref.inicializarTabla(ref.asignaciones);
							ref.modSistcorr.procesar(false);
							ref.modulo.compBusqueda.myModal.modal('hide');
						}else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							ref.modSistcorr.procesar(false);
						}
						ref.modulo.compBusqueda.myModal.modal('hide');
						//ref.mostrarAsignaciones();
						ref.inicializarTabla(ref.asignaciones);
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modulo.compBusqueda.myModal.modal('hide');
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
	                    a.download = 'reporte_consulta_asignaciones.xlsx';
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
				ref.modulo.compBusqueda.dataTableConsulta.show();
				ref.dataTableConsulta = ref.modulo.compBusqueda.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_BUSCAR_ASIGNACIONES_CONSULTA,
			        	"type": "GET",
			        	"data": {
			        		'correlativo' : ref.modulo.filtroBusqueda.correlativo.val(),
							'codigoEstado' : ref.modulo.filtroBusqueda.codigoEstado.val(),
							'numeroDocumentoInterno' : ref.modulo.filtroBusqueda.numeroDocumentoInterno.val(),
							'fechaAsignacionDesde' : ref.modulo.filtroBusqueda.fechaAsignacionDesde.val(),
							'fechaAsignacionHasta' : ref.modulo.filtroBusqueda.fechaAsignacionHasta.val(),
							'tipoAccion' : ref.modulo.filtroBusqueda.tipoAccion.val(),
							'dependenciaAsignante' : ref.modulo.filtroBusqueda.dependenciaAsignante.val(),
							'personaAsignada' : ref.modulo.filtroBusqueda.personaAsignada.val(),
							'fechaVencimientoDesde' : ref.modulo.filtroBusqueda.fechaVencimientoDesde.val(),
							'fechaVencimientoHasta' : ref.modulo.filtroBusqueda.fechaVencimientoHasta.val(),
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
			        	{data: 'id_correspondencia', title: 'Doc. P.', defaultContent: '', render: function(data, type, full){
			        		if(ref.rol_jefe == true || (ref.rol_gestor == true && (!(full.esConfidencial == "SI" && (full.esAsignado == null || full.esAsignado == "NO" || full.esAsignado == "")))) || (ref.rol_jefe == false && ref.rol_gestor == false && (full.esAsignado == "SI" || full.esConfidencial == "NO"))){
			        			return "<i class='fas fa-eye icon_view icon_add_document btnVerificarDocPrincipal_Asignacion' data-asignacion='"+full.correlativo+"' data-toggle='tooltip' title=''Ver documento principal'></i>"
			        		}
			        		return '';
			        	}},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-asignaciones/informacion/asig/" + full.idAsignacion + "?workflow=" + full.correlativo + "' data-toggle='tooltip' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        	}},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'numeroDocumentoInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaDocumentoProc', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		if(full.correlativo=="CRI-OFP-08373-2018"){
			        			console.log(full);
			        		}
			        		var fd = full.fechaDocumento;
			        		if(fd=="null" || fd==null){
			        			fd = "";
			        		}
			        		return '<span style="display:none;">'+fd+'</span>' + full.fechaDocumentoProc
			        	}},
			        	{data: 'fechaRecepcionProc', title: 'Fecha Recepción', defaultContent: '', render: function(data, type, full){
			        		var fr = full.fechaRecepcion;
			        		if(fr=="null" || fr==null){
			        			fr = "";
			        		}
			        		return '<span style="display:none;">'+fr+'</span>' + full.fechaRecepcionProc
			        	}},
			        	{data: 'remitente', title: 'Remitente', defaultContent: ''},
			        	{data: 'fechaAsignacionProc', title: 'Fecha Asignación', defaultContent: '', render: function(data, type, full){
			        		var fa = full.fechaAsignacion;
			        		if(fa=="null" || fa==null){
			        			fa = "";
			        		}
			        		return '<span style="display:none;">'+fa+'</span>' + full.fechaAsignacionProc
			        	}},
			        	{data: 'asignado', title: 'Persona Asignada', defaultContent: ''},
			        	{data: 'textoAsig', title: 'Detalle Requerimiento', defaultContent: ''},
			        	{data: 'solicitante', title: 'Solicitante', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaPlazoRespuestaProc', title: 'Fecha Vencimiento', defaultContent: '', render: function(data, type, full){
			        		var fpr = full.fechaPlazoRespuesta;
			        		if(fpr=="null" || fpr==null){
			        			fpr = "";
			        		}
			        		return '<span style="display:none;">'+fpr+'</span>' + full.fechaPlazoRespuestaProc
			        	}},
			        	{data: 'documentoRespuesta', title: 'Documento Respuesta', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_CA");
			        		console.log("order:" + order);
			        		if(order!=null && order != "undefined"){
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
			        		var nroPag = sessionStorage.getItem("NroPag_CA");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPagCA");
			        		console.log("OrigPag:" + origPag);
			        		if(origPag == "verDetalle"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagCA");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_CA", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaConsultaAsignaciones").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					//var api = ref.dataTableConsulta.api();
					//var order = ref.dataTableConsulta.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_CA", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
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
			        	{data: 'id_correspondencia', title: 'Doc. P.', defaultContent: '', render: function(data, type, full){
			        		if(ref.rol_jefe == true || (ref.rol_gestor == true && (!(full.esConfidencial == "SI" && (full.esAsignado == null || full.esAsignado == "NO" || full.esAsignado == "")))) || (ref.rol_jefe == false && ref.rol_gestor == false && (full.esAsignado == "SI" || full.esConfidencial == "NO"))){
			        			return "<i class='fas fa-eye icon_view icon_add_document btnVerificarDocPrincipal_Asignacion' data-asignacion='"+full.correlativo+"' data-toggle='tooltip' title=''Ver documento principal'></i>"
			        		}
			        		return '';
			        	}},
			        	{data: 'id_correspondencia', title: 'Ver', defaultContent: '', render: function(data, type, full){
			        		return "<a href='consulta-asignaciones/informacion/asig/" + full.idAsignacion + "?workflow=" + full.correlativo + "' data-toggle='tooltip' title='Abrir correspondencia'><i class='far fa-list-alt icon_view_detail'></i></a>"
			        	}},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'numeroDocumentoInterno', title: 'Nro. Documento', defaultContent: ''},
			        	{data: 'asunto', title: 'Asunto', defaultContent: ''},
			        	{data: 'fechaDocumentoProc', title: 'Fecha Documento', defaultContent: '', render: function(data, type, full){
			        		if(full.correlativo=="CRI-OFP-08373-2018"){
			        			console.log(full);
			        		}
			        		var fd = full.fechaDocumento;
			        		if(fd=="null" || fd==null){
			        			fd = "";
			        		}
			        		return '<span style="display:none;">'+fd+'</span>' + full.fechaDocumentoProc
			        	}},
			        	{data: 'fechaRecepcionProc', title: 'Fecha Recepción', defaultContent: '', render: function(data, type, full){
			        		var fr = full.fechaRecepcion;
			        		if(fr=="null" || fr==null){
			        			fr = "";
			        		}
			        		return '<span style="display:none;">'+fr+'</span>' + full.fechaRecepcionProc
			        	}},
			        	{data: 'remitente', title: 'Remitente', defaultContent: ''},
			        	{data: 'fechaAsignacionProc', title: 'Fecha Asignación', defaultContent: '', render: function(data, type, full){
			        		var fa = full.fechaAsignacion;
			        		if(fa=="null" || fa==null){
			        			fa = "";
			        		}
			        		return '<span style="display:none;">'+fa+'</span>' + full.fechaAsignacionProc
			        	}},
			        	{data: 'asignado', title: 'Persona Asignada', defaultContent: ''},
			        	{data: 'textoAsig', title: 'Detalle Requerimiento', defaultContent: ''},
			        	{data: 'solicitante', title: 'Solicitante', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        	{data: 'fechaPlazoRespuestaProc', title: 'Fecha Vencimiento', defaultContent: '', render: function(data, type, full){
			        		var fpr = full.fechaPlazoRespuesta;
			        		if(fpr=="null" || fpr==null){
			        			fpr = "";
			        		}
			        		return '<span style="display:none;">'+fpr+'</span>' + full.fechaPlazoRespuestaProc
			        	}},
			        	{data: 'documentoRespuesta', title: 'Documento Respuesta', defaultContent: ''}
			        ],
			        "columnDefs": [{
			        	"targets": [0, 1],
			        	"orderable": false
			        }],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.modulo.compBusqueda.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.modulo.compBusqueda.dataTable.on( 'page.dt', function () {
					//ref.dataTable.responsive.rebuild();
	        		//ref.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.modulo.compBusqueda.dataTable.on( 'order.dt', function () {
					//ref.modulo.compBusqueda.dataTable.responsive.rebuild();
	        		//ref.modulo.compBusqueda.dataTable.responsive.recalc();
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.modulo.btnVerificarDocumentoPrincipalAsignacion = $(".btnVerificarDocPrincipal_Asignacion");
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
	CONSULTA_ASIGNACIONES_VISTA.modSistcorr = modulo_sistcorr;
	CONSULTA_ASIGNACIONES_VISTA.modulo = modulo_consulta_asignaciones;
	CONSULTA_ASIGNACIONES_VISTA.rol_jefe = ES_JEFE;
	CONSULTA_ASIGNACIONES_VISTA.rol_gestor = ES_GESTOR;
	CONSULTA_ASIGNACIONES_VISTA.inicializar();
}, 500);