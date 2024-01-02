var modulo_lista_documentos = MODULO_LISTA_DOCUMENTOS.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);
var modulo_firma_digital = MODULO_FIRMA_DIGITAL.getInstance(SISTCORR);

jQuery.fn.toggleOption = function( show ) {
    jQuery( this ).toggle( show );
    if( show ) {
        if( jQuery( this ).parent( 'span.toggleOption' ).length )
            jQuery( this ).unwrap( );
    } else {
        jQuery( this ).wrap( '<span class="toggleOption" style="display: none;" />' );
    }
};

var LISTA_DOCUMENTOS_VISTA = {
		bandeja: [],
		correspondenciaSeleccionada: null,
		modSistcorr: null,
		modulo: null,
		modFirmaDigital: null,
		errores: [],
		filtrosBusqueda: null,
		dependenciasBPAC: [],//TICKET 9000003866
		filtro_correlativo: null,
		motivosRechazo: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.filtrosBusqueda = ref.modSistcorr.getFiltrosCorrespondencia(ref.modulo.bandeja.val()) || {};
			ref.modSistcorr.setDependenciasDestinoBPAC(ref.dependenciasBPAC);//TICKET 9000003866
			if(ref.filtro_correlativo != ''){
				ref.filtrosBusqueda.correlativo = ref.filtro_correlativo;
			}
			if(ref.filtrosBusqueda && ref.filtrosBusqueda.length > 0 && ref.filtrosBusqueda.usuario != "0" && ref.filtrosBusqueda.usuario != ""){
				ref.misPendientes = false;
			}
			if(ref.declinados == true){
				ref.filtrosBusqueda.declinados = ref.declinados;
			}
			if(ref.rechazados == true){
				ref.filtrosBusqueda.rechazados = ref.rechazados;
			}
			if(ref.misPendientes == true){
				// TICKET 9000003994 - POST (Condicional)
				if(!ref.filtrosBusqueda.usuario || (ref.filtrosBusqueda.usuario && ref.filtrosBusqueda.usuario == "0")){
					ref.filtrosBusqueda.misPendientes = ref.misPendientes;
				}
			}
			if(ref.filtrosBusqueda){
				ref.modSistcorr.setFiltrosCorrespondencia(ref.filtrosBusqueda, ref.modulo.bandeja.val());
			}
			
			// TICKET 9000003994 AÑADIENDO LOS CAMPOS A LOS CAMPOS DEL FORMULARIO
			
			//INICIO TICKET 9000003866
			ref.modulo.compFiltroDependenciaDestinoOrg.val(ref.filtrosBusqueda.dependenciaOriginadora || '0');
			ref.modulo.compFiltroDependenciaDestinoOrg.change();
			ref.modulo.compFiltroTipoCorrespondencia.val(ref.filtrosBusqueda.tipoCorrespondencia || '0');
			ref.modulo.compFiltroTipoCorrespondencia.change();
			//FIN TICKET 9000003866
			
			ref.modulo.compFiltroDependencia.val(ref.filtrosBusqueda.usuario || '0');
			ref.modulo.compFiltroDependencia.change();
			ref.modulo.compFiltroCorrelativo.val(ref.filtrosBusqueda.correlativo || '');
			ref.modulo.compFiltroCorrelativo.change();
			ref.modulo.compFiltroAsunto.val(ref.filtrosBusqueda.asunto || '');
			ref.modulo.compFiltroAsunto.change();
			ref.modulo.compFiltroRechazado.val(ref.filtrosBusqueda.rechazados || false);
			ref.modulo.compFiltroMisPendientes.val(ref.filtrosBusqueda.misPendientes || false);
			ref.modulo.compFiltroDeclinado.prop('checked', ref.filtrosBusqueda.declinados || false);
			ref.modulo.compFiltroRechazado.prop('checked', ref.filtrosBusqueda.rechazados || false);
			ref.modulo.compFiltroMisPendientes.prop('checked', ref.filtrosBusqueda.misPendientes || false);
			ref.modulo.compFiltroFechaInicio.val(ref.filtrosBusqueda.fechaInicio || '');
			ref.modulo.compFiltroFechaInicio.change();
			ref.modulo.compFiltroFechaFin.val(ref.filtrosBusqueda.fechaFin || '');
			ref.modulo.compFiltroFechaFin.change();
			// FIN TICKET
			ref.modSistcorr.accordionMenu.click(function(event){
				var t = $(this);
				var id = t.attr('data-id');
				console.log("Id ocultar:" + id);
				ocultarAcordeon(id);
			});
			ref.verificacionPrevia();//ticket 9000003866
			ref.obtenerBandeja();
			ref.inicializarComponentes();
			ref.modulo.btnFirmaGrupal.click(function(event){
				event.preventDefault();
				var correlativos = [];
				$("." + ref.modulo.classFirmaGrupal).each(function(){
					var corrInd = $(this);
					if(corrInd.prop('checked') == true){
						correlativos.push(corrInd.attr("data-idcorrespondencia"));
					}
				});
				var cant = correlativos.length;
				correlativos = correlativos.join(",");
				if(cant > 1){
					ref.validarNivelFirma(correlativos);
				}
			});
		},
		
		//INICIO TICKET 9000003866
		verificacionPrevia: function(){
			var ref = this;
			var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
			
			var valorBandeja = $("#bandeja").val();
			
			if(valorBandeja == "pendiente"){
				console.log("Cantidad de dependencias:" + dependencias.length);
				if(dependencias.length==1){
					ref.filtrosBusqueda = ref.modSistcorr.getFiltrosCorrespondencia(ref.modulo.bandeja.val()) || {};
					console.log("Filtros Busqueda:");
					var valor = "";
					var dependencias = ref.modSistcorr.getDependenciasDestinoBPAC();
					for(var index in dependencias){
						var dependencia = dependencias[index];
						valor = dependencia.codigo;
					}
					
					var _filtro = {}
					_filtro.dependenciaOriginadora = valor;
					_filtro.tipoCorrespondencia = ref.modulo.compFiltroTipoCorrespondencia.val();
					_filtro.correlativo = ref.modulo.compFiltroCorrelativo.val();
					_filtro.asunto = ref.modulo.compFiltroAsunto.val();
					_filtro.rechazados = ref.modulo.compFiltroRechazado.is(':checked'); 
					_filtro.misPendientes = ref.modulo.compFiltroMisPendientes.is(':checked');
					_filtro.declinados = ref.modulo.compFiltroDeclinado.is(':checked'); 
					_filtro.fechaInicio = ref.modulo.compFiltroFechaInicio.val();
					_filtro.fechaFin = ref.modulo.compFiltroFechaFin.val();
					_filtro.usuario = ref.modulo.compFiltroDependencia.val();
					ref.filtrosBusqueda = _filtro;
					ref.modSistcorr.setFiltrosCorrespondencia(_filtro, ref.modulo.bandeja.val());
				}
			}
		},
		//FIN TICKET 9000003866
		
		obtenerBandeja: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.bandeja = [];
			ref.filtrosBusqueda = ref.modSistcorr.getFiltrosCorrespondencia(ref.modulo.bandeja.val()) || {};
			ref._mostrarFiltros();
			ref.modulo.listarCorrespondencias(ref.filtrosBusqueda)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.bandeja = respuesta.datos;
						ref._actualizarBandeja();
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Warning");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		_actualizarBandeja: function(){
			var ref = this;
			if(ref.bandeja.length == 0){
				ref.modulo.compSinResultados.show();
			} else{
				ref.modulo.compSinResultados.hide();
			}
			var contenidoHtml = ref.modulo.htmlCorrespondencias(ref.bandeja);
			ref.modulo.compListaDocumentos.html(contenidoHtml);
			ref.modSistcorr.eventoTooltip();
			ref._inicializarEventosCorrespondencia();
			setTimeout(function() {
				var _idSeleccionado = ref.modSistcorr.getBandejaFirmaSeleccionada();
				console.log("_idSeleccionado:" + _idSeleccionado)
				if(_idSeleccionado != ""){
					$("#CORRESPONDENCIA_" + _idSeleccionado).addClass("correspondencia_seleccionda");
					$("#CORRESPONDENCIA_FOOTER_" + _idSeleccionado).css("display", "block");
					ref.correspondenciaSeleccionada = _idSeleccionado;
				}
			}, 1000);
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../../" + ref.modulo.URL_TUTORIALES);
			});
			
			var esGestor = $("#esGestor").val();
			var valorBandeja = $("#bandeja").val();
			if((esGestor == false || esGestor == "false") || valorBandeja != "firmado"){
				ref.modulo.compFiltroDependencia.val("0");
				ref.modulo.compFiltroDependencia.parent().hide();
			}
			//inicio ticket 9000003866
			if(valorBandeja == "pendiente"){
				ref.modulo.compFiltroDependenciaDestinoOrg.select2({
					
				}).on('select2:select', function(event){
					var $comp = $(event.currentTarget);
	            	ref.modSistcorr.select2_change($comp.attr('id'))
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
				
				ref.modulo.compFiltroTipoCorrespondencia.select2({
					
				}).change(function(event){
					var $comp = $(event.currentTarget);
					ref.modSistcorr.select2_change($comp.attr('id'));
				}).focus(function(event){
					var $comp = $(event.currentTarget);
					ref.modSistcorr.select2_change($comp.attr('id'));
				}).focusout(function(event){
					var $comp = $(event.currentTarget);
					ref.modSistcorr.select2_change($comp.attr('id'));
				});
			}
			//fin ticket 9000003866
			setTimeout(function(){
				ref.inicializarEventosComponentes();
			}, 500);
		},
		
		inicializarEventosComponentes: function(){
			var ref = this;
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.btnRechzarSi.click(function(){
				if(ref._validar_RechazarCorrespondencia()){
					ref._rechazarCorrespondencia();
				}
				
			});
			
			// TICKET 9000003996 - POST
			
			ref.modulo.btnRechzarResponsableSi.click(function(){
				if(ref._validar_RechazarCorrespondenciaResponsable()){
					ref._rechazarCorrespondenciaResponsable();
				}
				
			});
			
			ref.modulo.btnRechzarFirmadoSi.click(function(){
				if(ref._validar_RechazarCorrespondenciaFirmado()){
					ref._rechazarCorrespondenciaFirmado();
				}
				
			});
			
			// FIN TICKET
			
			//TICKET 9000004411 COMENT btnRechzarSi.click
			/*ref.modulo.btnRechzarSi.click(function(){
				if(ref._validar_RechazarCorrespondencia()){
					ref._rechazarCorrespondencia();
				}
				
			});*/
			
			ref.modulo.btnAbrirBusqueda.click(function(){
				ref._abrirModalBusqueda();
			});
			
			ref.modulo.btnFiltrar.click(function(){
				ref._filtrarCorrespondencias();
			});
			
			ref.modulo.btnNuevoDocumento.click(function(){
				ref.modSistcorr.procesar(true);
				sessionStorage.setItem('urlOrigen', 'bandejaNuevos');
				ref.modulo.abrirNuevoDocumento();
			});
			
			//inicio ticket 9000004807
			ref.modulo.btnExportarExcelCorrespondenciaBS.click(function(event){
				ref.exportarExcelCorrespondenciasBS();
			});
			//fin ticket 9000004807
			
			ref.modulo.btnReasignarCorrespondencia.click(function(){
				ref._reasignarCorrespondencia();
			});
			
			ref.modulo.btnEnviarCorrespondencia.click(function(){
				ref._enviarCorrespondencia();
			});
			
			ref.modulo.btnDeclinarCorrespondencia.click(function(){
				ref._declinarCorrespondencia();
			});
			
			ref.funcionarios = ref.modulo.compUsuarioReemplazante.select2({
				ajax: {
				    url: ref.modulo.URL_LISTAR_REEMPLAZANTES,
				    data: function (params) {
				        var query = {
				        		correspondencia : ref.modulo.compReasignarCorrespondencia.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.reemplazantes.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
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
			
			 ref.modulo.compFiltroFechaInicio.datepicker({
				 regional: 'es',
		            firstDay: 7,
		            onClose : function(event){
		            	var $comp = this;
		            	ref.modSistcorr.datePicker_close($comp.id);
		            },
		            onSelect: function(){
		            	var $comp = this;
		            	ref.modSistcorr.datePicker_change($comp.id);
		            	//var label = $("label[for='" + $comp.attr('id') + "']");
		            	//label.addClass('active');
		            },
		        });
			 
			 ref.modulo.comBtnFiltroFechaInicio.click(function(){
				 ref.modulo.compFiltroFechaInicio.focus();
			 });
			
			 ref.modulo.compFiltroFechaFin.datepicker({
		        	regional: 'es',
		            firstDay: 7,
		            onClose : function(event){
		            	var $comp = this;
		            	ref.modSistcorr.datePicker_close($comp.id);
		            },
		            onSelect: function(){
		            	var $comp = this;
		            	ref.modSistcorr.datePicker_change($comp.id);
		            	//var label = $("label[for='" + $comp.attr('id') + "']");
		            	//label.addClass('active');
		            },
		        });
			 
			 ref.modulo.compBtnFiltroFechaFin.click(function(){
				 ref.modulo.compFiltroFechaFin.focus();
			 });
			 
			 ref.modulo.btnAsignarSiguienteFirmante.click(function(){
				 setTimeout(function(){
					 window.location.replace("../../app/asignacion-firma/" + ref.modulo.compIdCorrespondenciaAsignar.val());
				 }, 500);
			 });
			 
			 ref.modulo.compFiltroDependencia.change(function(){
				 var t = $(this);
				 var valor = t.val();
				 console.log("Cambiando:" + valor);
				 if(valor=="0"){
					 ref.modulo.compFiltroMisPendientes.removeAttr('disabled');
					 ref.modulo.compFiltroMisPendientes.prop("checked", true);
				 }else{
					 setTimeout(function(){
						 ref.modulo.compFiltroMisPendientes.prop("checked", false);
						 ref.modulo.compFiltroMisPendientes.prop("disabled", true); 
					 }, 500);
				 }
			 });
		},
		
		
		iniciarlizarEventosOpciones: function(){
			var ref = this;
			$("." + ref.modulo.classAsignarFirma).click(function(event){
				
			});
			
			$("." + ref.modulo.classEmitirFirma).click(function(event){
				var $comp = $(event.currentTarget);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref._procesoEmitirFirma(idCorrespondencia);
			});
			
//			inicio ticket 9000003996
			
			$("." + ref.modulo.classRechzarFirmado).click(function(event){
				var $comp = $(event.currentTarget);
				var t = $(this);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compRechazarMotivoFirmado.val('');
				ref.modulo.compRechazarMotivoDescripcionFirmado.val('');
				ref.modulo.compRechazarCorrespondenciaFirmado.val(idCorrespondencia);
				
				ref.mostrarRechazosFirma();
				setTimeout(function() {
					ref.modulo.compModalRechazarFirmado.modal('show');
				}, 300);
			});
			
//			fin ticket 9000003996
			
			$("." + ref.modulo.classRechzarFirma).click(function(event){
				var $comp = $(event.currentTarget);
				var t = $(this);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compRechazarMotivo.val('');
				ref.modulo.compRechazarMotivoDescripcion.val('');
				ref.modulo.compRechazarCorrespondencia.val(idCorrespondencia);
				ref.mostrarRechazosTodos();
				setTimeout(function() {
					ref.modulo.compModalRechazar.modal('show');
				}, 300);
			});
			
			$("." + ref.modulo.classRechazarResponsableFirma).click(function(event){
				var $comp = $(event.currentTarget);
				var t = $(this);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compRechazarMotivoResponsable.val('');
				ref.modulo.compRechazarMotivoDescripcionResponsable.val('');
				ref.modulo.compRechazarCorrespondenciaResponsable.val(idCorrespondencia);
				ref.mostrarRechazosResponsable();
				setTimeout(function() {
					ref.modulo.compModalRechazarResponsable.modal('show');
				}, 300);
			});
			
			$("." + ref.modulo.classEnviarCorrespondencia).click(function(event){
				var $comp = $(event.currentTarget);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compIdCorrespondenciaEnviar.val(idCorrespondencia);
				ref.modulo.compModalEnviarDocumento.modal('show');
			});
			
			$("." + ref.modulo.classReasignarCorrespondencia).click(function(event){
				var $comp = $(event.currentTarget);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compReasignarCorrespondencia.val(idCorrespondencia);
				ref.modulo.compUsuarioReemplazante.val('');
				ref.modulo.compUsuarioReemplazante.change();
				setTimeout(function(){
					ref.modulo.compModalReasignarCorrespondencia.modal('show');
				}, 300);
			});
			
			$("." + ref.modulo.classDeclinarCorrespondencia).click(function(event){
				var $comp = $(event.currentTarget);
				var idCorrespondencia = $comp.data("idcorrespondencia");
				ref.modulo.compDeclinarCorrespondencia.val(idCorrespondencia);
				setTimeout(function(){
					ref.modulo.compModalDeclinarCorrespondencia.modal('show');
				}, 300);
			});
		},
		
		mostrarRechazosResponsable: function(){
			var ref = this;
			console.log("RECHAZO RESPONSABLE");
			//$("#rechzarMotivo option[data-responsable=false]").hide()
			/*$('#rechzarMotivo > option').each(function(){
		        var $option = $(this);
		        var data = $option.data('responsable');
		        if(data == "false" || data == false){
		        	$option.toggleOption( $option);
		        }
		    });*/
		},
		
		mostrarRechazosTodos: function(){
			var ref = this;
			/*$("#rechzarMotivo option[data-rechazofirmado=false]").show();
			$("#rechzarMotivo option[data-responsable=false]").show()*/
		},
		
//		inicio ticket 9000003996
		mostrarRechazosFirma: function(){
			
			var ref = this;
			//$("#rechzarMotivo option[data-rechazofirmado=false]").hide();
		},
		
//		fin ticket 9000003996

		
		_inicializarEventosCorrespondencia: function(){
			var ref = this;
			$("."+ ref.modulo.classCompCorrespondencia).click(function(event){
				var $columnaCorrespondencia = $(event.currentTarget);
				var seleccionado = $columnaCorrespondencia.data("idcorrespondencia");
				var flgRemplazo = "_" + $columnaCorrespondencia.data("flgremplazo") + $columnaCorrespondencia.data("userremplazo");
				if(!ref.correspondenciaSeleccionada){
					$("#CORRESPONDENCIA_" + seleccionado + flgRemplazo).addClass("correspondencia_seleccionda");
					$("#CORRESPONDENCIA_FOOTER_" + seleccionado + flgRemplazo).css("display", "block");
					ref.correspondenciaSeleccionada = seleccionado + flgRemplazo;
				} else{
					var isVisibleFooter = $("#CORRESPONDENCIA_FOOTER_" + seleccionado + flgRemplazo).css("display") == "block";
					$("." + ref.modulo.classCompCorrespondenciaFooter).css("display", "none");
					if(!isVisibleFooter){
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado + flgRemplazo).css('display', 'block');
					}else {
						$("#CORRESPONDENCIA_FOOTER_" + seleccionado + flgRemplazo).css('display', 'none');
					}
					$("#CORRESPONDENCIA_" +  ref.correspondenciaSeleccionada).removeClass('correspondencia_seleccionda');
	        		$("#CORRESPONDENCIA_" +  seleccionado + flgRemplazo).addClass('correspondencia_seleccionda');
					ref.correspondenciaSeleccionada = seleccionado + flgRemplazo;
				}
				console.log("ID SELECCIONADO: " + ref.correspondenciaSeleccionada)
				ref.modSistcorr.setBandejaFirmaSeleccionada(ref.correspondenciaSeleccionada);
			});
			
			$("." + ref.modulo.classFirmaGrupal).change(function(event){
				var corrSelecFirma = 0;
				$("." + ref.modulo.classFirmaGrupal).each(function(){
					var corrInd = $(this);
					if(corrInd.prop('checked') == true){
						corrSelecFirma++;
						console.log("ID: " + corrInd.attr('data-idcorrespondencia'));
					}
				});
				console.log("SELECCIONADOS: " + corrSelecFirma);
				if(corrSelecFirma > 1){
					ref.modulo.btnFirmaGrupal.show();
				}else{
					ref.modulo.btnFirmaGrupal.hide();
				}
			});
			
			ref.iniciarlizarEventosOpciones();
		},
		
		_procesoEmitirFirma: function(idCorrespodencia){
			var ref = this;
			ref.modFirmaDigital.generarZip(idCorrespodencia)
				.then(function(respuesta){
					var url_zip = respuesta;
					ref.modFirmaDigital.abrirFormularioFirma(idCorrespodencia);
				}).catch(function(error){
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		//INICIO TICKET 9000004807
		exportarExcelCorrespondenciasBS: function(){
			var ref = this;
			
			ref.modSistcorr.procesar(true);	
			ref.modulo.exportarExcelCorrespondenciasBS(ref.filtrosBusqueda)
			.then(function(respuesta){
				if (navigator.appVersion.toString().indexOf('.NET') > 0){
					window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_bandeja_entrada.xlsx');
				} else {
					var a = document.createElement('a');
                    a.href = URL.createObjectURL(respuesta);
                    a.download = 'reporte_bandeja_entrada.xlsx';
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
		//FIN TICKET 9000004807
		
		_rechazarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.rechazarFirma()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						// TICKET 9000003937
						var indice = ref.modulo.compRechazarMotivo.val();
						var idCorr = ref.modulo.compRechazarCorrespondencia.val();
						var redireccionAsignante = false;
						for(var i=0;i<ref.motivosRechazo.length;i++){
							var motivoRechazo = ref.motivosRechazo[i];
							if(motivoRechazo.id == indice){
								console.log(motivoRechazo);
								if(!motivoRechazo.cancelarDocumento && !motivoRechazo.aumentarFlujo){
									redireccionAsignante = true;
								}
							}
						}
						if(redireccionAsignante){
							setTimeout(function(){
								ref.modulo.compIdCorrespondenciaAsignar.val(idCorr);
								ref.modulo.compModalAsignarFirmante.modal('show');
							}, 500);
						}
						// FIN TICKET
						ref.obtenerBandeja();
					} else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					setTimeout(function(){
						ref.modulo.compModalRechazar.modal('hide');
					}, 200);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.compModalRechazar.modal('hide');
				});
		},
		
		// TICKET 9000003996 - POST
		
		_rechazarCorrespondenciaResponsable: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.rechazarFirmaResponsable()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						// TICKET 9000003937
						var indice = ref.modulo.compRechazarMotivoResponsable.val();
						var idCorr = ref.modulo.compRechazarCorrespondenciaResponsable.val();
						var redireccionAsignante = false;
						for(var i=0;i<ref.motivosRechazo.length;i++){
							var motivoRechazo = ref.motivosRechazo[i];
							if(motivoRechazo.id == indice){
								console.log(motivoRechazo);
								if(!motivoRechazo.cancelarDocumento && !motivoRechazo.aumentarFlujo){
									redireccionAsignante = true;
								}
							}
						}
						if(redireccionAsignante){
							setTimeout(function(){
								ref.modulo.compIdCorrespondenciaAsignar.val(idCorr);
								ref.modulo.compModalAsignarFirmante.modal('show');
							}, 500);
						}
						// FIN TICKET
						ref.obtenerBandeja();
					} else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					setTimeout(function(){
						ref.modulo.compModalRechazarResponsable.modal('hide');
					}, 200);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.compModalRechazar.modal('hide');
				});
		},
		
		_rechazarCorrespondenciaFirmado: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.rechazarFirmaFirmado()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						// TICKET 9000003937
						var indice = ref.modulo.compRechazarMotivoFirmado.val();
						var idCorr = ref.modulo.compRechazarCorrespondenciaFirmado.val();
						var redireccionAsignante = false;
						for(var i=0;i<ref.motivosRechazo.length;i++){
							var motivoRechazo = ref.motivosRechazo[i];
							if(motivoRechazo.id == indice){
								console.log(motivoRechazo);
								if(!motivoRechazo.cancelarDocumento && !motivoRechazo.aumentarFlujo){
									redireccionAsignante = true;
								}
							}
						}
						if(redireccionAsignante){
							setTimeout(function(){
								ref.modulo.compIdCorrespondenciaAsignar.val(idCorr);
								ref.modulo.compModalAsignarFirmante.modal('show');
							}, 500);
						}
						// FIN TICKET
						ref.obtenerBandeja();
					} else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					setTimeout(function(){
						ref.modulo.compModalRechazarFirmado.modal('hide');
					}, 200);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.compModalRechazar.modal('hide');
				});
		},
		
		// FIN TICKET
		
		_validar_RechazarCorrespondencia: function(){
			var ref = this;
			if(ref.modulo.compRechazarMotivo.val() == null || ref.modulo.compRechazarMotivo.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Seleccione motivo de rechazo', "Error");
				return false;
			}
			if(ref.modulo.compRechazarMotivoDescripcion.val() == null || ref.modulo.compRechazarMotivoDescripcion.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Ingrese descripción del motivo de rechazo', "Error");
				return false;
			}
			return true;
		},
		
		// TICKET 9000003996 - POST
		
		_validar_RechazarCorrespondenciaResponsable: function(){
			var ref = this;
			if(ref.modulo.compRechazarMotivoResponsable.val() == null || ref.modulo.compRechazarMotivoResponsable.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Seleccione motivo de rechazo', "Error");
				return false;
			}
			if(ref.modulo.compRechazarMotivoDescripcionResponsable.val() == null || ref.modulo.compRechazarMotivoDescripcionResponsable.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Ingrese descripción del motivo de rechazo', "Error");
				return false;
			}
			return true;
		},
		
		_validar_RechazarCorrespondenciaFirmado: function(){
			var ref = this;
			if(ref.modulo.compRechazarMotivoFirmado.val() == null || ref.modulo.compRechazarMotivoFirmado.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Seleccione motivo de rechazo', "Error");
				return false;
			}
			if(ref.modulo.compRechazarMotivoDescripcionFirmado.val() == null || ref.modulo.compRechazarMotivoDescripcionFirmado.val() == ''){
				ref.modSistcorr.notificar("ERROR", 'Ingrese descripción del motivo de rechazo', "Error");
				return false;
			}
			return true;
		},
		
		// FIN TICKET
		
		_enviarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.enviarCorrespondencia(ref.modulo.compIdCorrespondenciaEnviar.val())
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						var mensajes = respuesta.mensaje;
						mensajes = mensajes.split("|||");
						for(var i=0;i<mensajes.length;i++){
							var tipo = mensajes[i].split("<<>>");
							if(tipo.length == 1){
								ref.modSistcorr.notificar("OK", mensajes[i], "Success");
							}
							if(tipo.length == 2){
								if(tipo[0] == "true"){
									ref.modSistcorr.notificar("OK", tipo[1], "Success");
								}
								if(tipo[0] == "false"){
									ref.modSistcorr.notificar("ERROR", tipo[1], "Error");
								}
							}
						}
						ref.obtenerBandeja();
					} else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalEnviarDocumento.modal('hide');
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					ref.modulo.compModalEnviarDocumento.modal('hide');
				});
		},
		
		_mostrarFiltros: function(){
			var ref = this;
			ref.modulo.compFiltrosBusqueda.empty();
			var contenidoHTML = ref.modulo.htmlFiltros(ref.filtrosBusqueda || {});
			ref.modulo.compFiltrosBusqueda.html(contenidoHTML);
			ref.modSistcorr.eventoTooltip();
			var _bandeja = ref.modulo.bandeja.val();
			$('.icon_eliminar_filtro').click(function(){
				var $comp = $(this);
				var field = $comp.data("field");
				console.log("field:" + field);
				// TICKET 9000003994 - POST (Condicional)
				if(field=="usuario"){
					ref.modulo.compFiltroDependencia.val("0");
				}
				$('#icon_filtro_'+field).tooltip('hide');
				delete ref.filtrosBusqueda[field];
				ref.modSistcorr.setFiltrosCorrespondencia(ref.filtrosBusqueda, _bandeja);
				setTimeout(function() {
					ref.obtenerBandeja();
					$('#filtro_'+field).remove();
				}, 300);
				
			});
		},
		
		_abrirModalBusqueda: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.compFiltroDependencia.val(ref.filtrosBusqueda.usuario || '0');
			ref.modulo.compFiltroDependencia.change();
			ref.modulo.compFiltroCorrelativo.val(ref.filtrosBusqueda.correlativo || '');
			ref.modulo.compFiltroCorrelativo.change();
			ref.modulo.compFiltroAsunto.val(ref.filtrosBusqueda.asunto || '');
			ref.modulo.compFiltroAsunto.change();
			ref.modulo.compFiltroRechazado.val(ref.filtrosBusqueda.rechazados || false);
			ref.modulo.compFiltroMisPendientes.val(ref.filtrosBusqueda.misPendientes || false);
			ref.modulo.compFiltroDeclinado.prop('checked', ref.filtrosBusqueda.declinados || false);
			ref.modulo.compFiltroRechazado.prop('checked', ref.filtrosBusqueda.rechazados || false);
			ref.modulo.compFiltroMisPendientes.prop('checked', ref.filtrosBusqueda.misPendientes || false);
			ref.modulo.compFiltroFechaInicio.val(ref.filtrosBusqueda.fechaInicio || '');
			ref.modulo.compFiltroFechaInicio.change();
			ref.modulo.compFiltroFechaFin.val(ref.filtrosBusqueda.fechaFin || '');
			ref.modulo.compFiltroFechaFin.change();
			//inicio ticket 9000003866
			ref.modulo.compFiltroDependenciaDestinoOrg.val(ref.filtrosBusqueda.dependenciaOriginadora || '0');
			ref.modulo.compFiltroDependenciaDestinoOrg.change();
			ref.modulo.compFiltroTipoCorrespondencia.val(ref.filtrosBusqueda.tipoCorrespondencia || '0');
			ref.modulo.compFiltroTipoCorrespondencia.change();
			//fin ticket 9000003866
			
			setTimeout(function() {
				ref.modulo.compModalFiltrar.modal('show');
			}, 200);
			
			setTimeout(function() {
				ref.modSistcorr.procesar(false);
				ref.modulo.compFiltroCorrelativo.focus();
			}, 1000);
			
		},
		
		_filtrarCorrespondencias: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref._obtenerFiltros();
			ref._mostrarFiltros();
			ref.modulo.listarCorrespondencias(ref.filtrosBusqueda)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.bandeja = respuesta.datos;
						ref._actualizarBandeja();
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			ref.modulo.compModalFiltrar.modal('hide');
		},
		
		_obtenerFiltros: function(){
			var ref = this;
			var _filtro = {};
			_filtro.dependenciaOriginadora = ref.modulo.compFiltroDependenciaDestinoOrg.val();;//ticket 9000003866
			_filtro.tipoCorrespondencia = ref.modulo.compFiltroTipoCorrespondencia.val();//ticket 9000003866
			_filtro.correlativo = ref.modulo.compFiltroCorrelativo.val();
			_filtro.asunto = ref.modulo.compFiltroAsunto.val();
			_filtro.rechazados = ref.modulo.compFiltroRechazado.is(':checked'); 
			_filtro.misPendientes = ref.modulo.compFiltroMisPendientes.is(':checked');
			_filtro.declinados = ref.modulo.compFiltroDeclinado.is(':checked'); 
			_filtro.fechaInicio = ref.modulo.compFiltroFechaInicio.val();
			_filtro.fechaFin = ref.modulo.compFiltroFechaFin.val();
			_filtro.usuario = ref.modulo.compFiltroDependencia.val();
			ref.filtrosBusqueda = _filtro;
			ref.modSistcorr.setFiltrosCorrespondencia(_filtro, ref.modulo.bandeja.val());
		},
		
		_reasignarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.reasignarCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.obtenerBandeja();
						setTimeout(function() {
							ref.modulo.compModalReasignarCorrespondencia.modal('hide');
						}, 200);
					} else {
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					setTimeout(function() {
						ref.modulo.compModalReasignarCorrespondencia.modal('hide');
					}, 200);
				});
			
		},
		
		_declinarCorrespondencia: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.declinarCorrespondencia()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.obtenerBandeja();
					} else {
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					setTimeout(function() {
						ref.modulo.compModalDeclinarCorrespondencia.modal('hide');
					}, 200);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
					setTimeout(function() {
						ref.modulo.compModalDeclinarCorrespondencia.modal('hide');
					}, 200);
				});
			
		},
		
		validarNivelFirma: function(correlativos){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.validarNivelFirma(correlativos)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.generarZipGrupal(correlativos);
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		generarZipGrupal: function(correlativos){
			var ref = this;
			ref.modulo.generarZipGrupal(correlativos)
				.then(function(respuesta){
					console.log("Respuesta Zip Grupal");
					console.log(respuesta);
					if(respuesta.estado == true){
						ref.modSistcorr.procesar(false);
						var nombreZip = respuesta.datos[0];
						ref.modulo.abrirFormularioFirmaGrupal(nombreZip);
					}else{
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
};

setTimeout(function(){
	LISTA_DOCUMENTOS_VISTA.modSistcorr = modulo_sistcorr;
	LISTA_DOCUMENTOS_VISTA.modulo = modulo_lista_documentos;
	LISTA_DOCUMENTOS_VISTA.modFirmaDigital = modulo_firma_digital;
	LISTA_DOCUMENTOS_VISTA.dependenciasBPAC = LISTA_DEPENDENCIAS_BPAC;//TICKET 9000003866
	LISTA_DOCUMENTOS_VISTA.listas = {};
	LISTA_DOCUMENTOS_VISTA.listas.reemplazantes = new LISTA_DATA([]);
	LISTA_DOCUMENTOS_VISTA.filtro_correlativo = FILTRO_CORRELATIVO;
	LISTA_DOCUMENTOS_VISTA.declinados = DECLINADOS;
	LISTA_DOCUMENTOS_VISTA.rechazados = RECHAZADOS;
	LISTA_DOCUMENTOS_VISTA.misPendientes = MISPENDIENTES;
	LISTA_DOCUMENTOS_VISTA.motivosRechazo = MOTIVOS;
	LISTA_DOCUMENTOS_VISTA.inicializar();
}, 500);