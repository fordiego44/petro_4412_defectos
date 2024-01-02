var modulo_reemplazos_total= MODULO_REEMPLAZOS_TOTAL.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REEMPLAZOS_TOTAL_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {combosSimples:{}, combosS2: {}, datePikers:{}},
		filtroJG: {},
		masfiltroJGs: false,
		dataTable: null,
		dataTableRA: null,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		datosConsulta: [],
		dependenciasJG: [],
		codigoConfirm: null,
		mensajeConfirm: null,
		codigoAxu: null,
		jefe: false,
		valorVar:null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.iniciarCombosAutoCompletados();
			setTimeout(function(){
				ref.inicialiarCombosFiltros();
				ref.setFormCombosFiltros();
			}, 1000);
		},
		
		inicialiarCombosFiltros :function(){
			var ref=this;
			
			ref.componentesJG.combosS2.desDependencia = ref.moduloJG.componentesJG.cmbDependencia.select2({
				ajax: {
					url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
					delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo: 'TOTAL',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
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
			
			ref.componentesJG.combosS2.rol =  ref.moduloJG.componentesJG.cmbRol.select2({
				ajax: {
				    url: ref.moduloJG.URL_BUSCAR_ROL_X_DEPENDENCIA,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependencia.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
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
			
			
			ref.componentesJG.combosS2.usuarioSaliente =  ref.moduloJG.componentesJG.cmbUsuarioSaliente.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependencia.val(),
				        		rol:ref.moduloJG.componentesJG.cmbRol.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
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
			
			ref.componentesJG.combosS2.usuarioEntrante =  ref.moduloJG.componentesJG.cmbUsuarioEntrante.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'TOTAL',
				        		rol:ref.moduloJG.componentesJG.cmbRol.val(),
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependencia.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
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
			
		},	
		
		setFormCombosFiltros: function(){
			var ref = this;
			if(ref.filtroJG != undefined && ref.filtroJG.dependencia != undefined){
				ref.moduloJG.componentesJG.cmbDependencia.append("<option value='" +ref.filtroJG.dependencia + "' selected='selected'>" +ref.filtroJG.dependenciaTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbDependencia.change();

				ref.moduloJG.componentesJG.cmbRol.append("<option value='" +ref.filtroJG.rol + "' selected='selected'>" +ref.filtroJG.rolTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbRol.change();

				ref.moduloJG.componentesJG.cmbUsuarioSaliente.append("<option value='" +ref.filtroJG.usuarioSaliente + "' selected='selected'>" +ref.filtroJG.usuarioSalienteTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbUsuarioSaliente.change();

				ref.moduloJG.componentesJG.cmbUsuarioEntrante.append("<option value='" +ref.filtroJG.usuarioEntrante + "' selected='selected'>" +ref.filtroJG.usuarioEntranteTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbUsuarioEntrante.change();

				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(ref.filtroJG.fechaDesde);
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(ref.filtroJG.fechaHasta);
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			}
		},
		
		eliminarReemplazoTotal: function(){
			var ref=this;
			var comp = $(this);
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoTotal.id_reemplazo
			};			
			ref.modSistcorr.procesar(true);			
			ref.moduloJG.eliminarReemplazoTotal(ref.parametros)
				.then(function(respuesta){
					console.log("Respuesta.estado:" + respuesta.estado)
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modSistcorr.procesar(false);
						
						ref.moduloJG.comElimTotal.myModal.modal('hide');
						setTimeout(function(){
							ref.inicializarTablaConsulta();
						}, 1000);
						
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
					
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comElimTotal.myModal.modal('hide');
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
			
			ref.componentesJG.txtFechaDocumentoDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentesJG.txtFechaDocumentoHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.focus();
			});	
			
			ref.moduloJG.componentesJG.btnFechaDesdeTotal.click(function(){
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.click();
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaHastTotal.click(function(){
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.click();
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.focus();
			});
			
			ref.componentesJG.txtFechaAdicionDesdeJG = ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.datepicker({
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
			
			ref.componentesJG.txtFechaAdicionHastaJG = ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.datepicker({
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
			
			ref.componentesJG.combosS2.desDependencia = ref.moduloJG.comRegTotal.cmbDependenciaReempTotal.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codLugar: 'TOTAL',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
            	ref.moduloJG.comRegTotal.cmbRolReempTotal.val("");
            	ref.moduloJG.comRegTotal.cmbRolReempTotal.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desRol =  ref.moduloJG.comRegTotal.cmbRolReempTotal.select2({
				ajax: {
				    url: ref.moduloJG.URL_BUSCAR_ROL_X_DEPENDENCIA,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.comRegTotal.cmbDependenciaReempTotal.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
            	ref.moduloJG.comRegTotal.cmbFuncionarioReempTotal.val("");
            	ref.moduloJG.comRegTotal.cmbFuncionarioReempTotal.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncReemplazado =  ref.moduloJG.comRegTotal.cmbFuncionarioReempTotal.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.comRegTotal.cmbDependenciaReempTotal.val(),
				        		rol:ref.moduloJG.comRegTotal.cmbRolReempTotal.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));            	
            	ref.moduloJG.comRegTotal.cmbFuncReempTotal.val("");
            	ref.moduloJG.comRegTotal.cmbFuncReempTotal.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncReemplazar =  ref.moduloJG.comRegTotal.cmbFuncReempTotal.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'TOTAL',
				        		rol:ref.moduloJG.comRegTotal.cmbRolReempTotal.val(),
				        		codDependencia: ref.moduloJG.comRegTotal.cmbDependenciaReempTotal.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	
				    	return {results: respuesta.datos};
				    }
				}
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
			
		},
		
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				ref.buscarReemplazosTotal();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltroJGs();
			});	
			
			ref.moduloJG.btnGuardarTotal.off('click').on('click', function(){
				ref.insertUpdateReemplazoTotal();
			});
			
			ref.moduloJG.btnEliminarReemplazoTotal.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
			});
			
			ref.moduloJG.compBtnRegistrarTotal.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				ref.abrirModalRegTotal();
			});
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comRegTotal.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnAceptarMensajeEliminarReemplazosAdicionAgregarTotal.off('click').on('click', function(){
				ref.validarSiUsuarioEsDeMejorCargo(true);
			});
			
			ref.moduloJG.btnAceptarConfirmacion.off('click').on('click', function(){
				ref.aceptarConfirmacionReemplazoTotal();
			});
			
			ref.moduloJG.btnAceptarMensajeRolMenorReemplazo.off('click').on('click', function(){
				ref.moduloJG.comPopUpMensajeRolMenorReemplazo.modal('hide');
				setTimeout(function() {				
					ref.modSistcorr.notificar("OK", "SE REGISTRO EL REEMPLAZO DE TIPO TOTAL CON ÉXITO", "Success");
					ref.inicializarTablaConsulta();
				}, 300);
			});
			
			ref.moduloJG.btnPreAceptarBorrarReemplazosTotal.off('click').on('click', function(){
				ref.moduloJG.comPopUpListaReemplazosTotal.modal('hide');
				setTimeout(function() {				
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosTotal.modal('show');
				}, 500);
			});
			
			ref.moduloJG.btnAceptarBorrarReemplazosTotal.off('click').on('click', function(){
				ref.eliminarRemplazosYactualizarValorVarConConfirmacion4();
			});
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			setTimeout(function() {				
				ref.obtenerfiltroJGs();
				if(Object.keys(ref.filtroJG).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					console.log("actualizar y buscar correspondencias");
					ref.searchReemplazosTotal();
				}
			}, 1500);
			
		},
		
		aceptarConfirmacionReemplazoTotal: function(){
			var ref = this;
			if(ref.codigoConfirm != null && ref.codigoConfirm != undefined && ref.codigoConfirm.trim() != ""){
				switch (ref.codigoConfirm) {
				case "1":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "1";
					ref.grabarReemplazoTotalConConfirmacion1(ref.parametros);
					break;
				case "2":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "2";
					ref.grabarReemplazoTotalConConfirmacion2(ref.parametros);
					break;
				case "3":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "3";
					ref.grabarReemplazoTotalConConfirmacion2(ref.parametros);
					break;
				case "4":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "4";
					ref.buscarReemplazosEnTotal(ref.parametros);
					break;
				case "5":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "5";
					ref.grabarReemplazoTotalConConfirmacion5(ref.parametros);
					break;
				default:
					break;
				}
			}else{
				
			}
		},
		
		grabarReemplazoTotalConConfirmacion1: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoTotalConConfirmacion1(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		grabarReemplazoTotalConConfirmacion5: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoTotalConConfirmacion1(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		grabarReemplazoTotalConConfirmacion2: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoTotalConConfirmacion2(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		grabarReemplazoTotalSinConfirmar: function(esMostrarMensaje){
			var ref = this;
			ref.moduloJG.grabarReemplazoTotalSinConfirmar(ref.parametros)
			.then(function(respuesta){
				
				if(respuesta.estado == true){
					if(esMostrarMensaje){
						ref.modSistcorr.procesar(false);
						ref.moduloJG.comRegTotal.myModal.modal('hide');
						setTimeout(function(){
							ref.moduloJG.comPopUpMensajeEliminarReemplazosAdicionAgregarTotal.modal('show');
						}, 300);
					}else{
						if(ref.parametros.accion == ""){
							ref.validarSiUsuarioEsDeMejorCargo(false);
						}else{
							ref.moduloJG.comRegTotal.myModal.modal('hide');
							ref.modSistcorr.procesar(false);
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.inicializarTablaConsulta();
						}
					}
				}else{
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		validarSiUsuarioEsDeMejorCargo: function(saltarPasoAceptarMensajeValUsuMejorCargo){
			var ref = this;
			if(saltarPasoAceptarMensajeValUsuMejorCargo)
				ref.modSistcorr.procesar(true);
			ref.moduloJG.validarSiUsuarioEsDeMejorCargo(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					if(saltarPasoAceptarMensajeValUsuMejorCargo){
						ref.moduloJG.comPopUpMensajeEliminarReemplazosAdicionAgregarTotal.myModal.modal('hide');
						if(!(respuesta.datos[0].descripcion == "")){
							ref.modSistcorr.procesar(false);
							$("#mensajeRolMenorReemplazo").text(respuesta.datos[0].descripcion);
							ref.moduloJG.comPopUpMensajeRolMenorReemplazo.modal('show');
						}else{
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.inicializarTablaConsulta();
						}
					}else{
						ref.moduloJG.comRegTotal.myModal.modal('hide');
						if(!(respuesta.datos[0].descripcion == "")){
							ref.modSistcorr.procesar(false);
							$("#mensajeRolMenorReemplazo").text(respuesta.datos[0].descripcion);
							ref.moduloJG.comPopUpMensajeRolMenorReemplazo.modal('show');
						}else{
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							ref.modSistcorr.procesar(false);
							ref.inicializarTablaConsulta();
						}
					}
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		buscarReemplazosEnTotal: function(){
			var ref = this;
			ref.moduloJG.buscarReemplazosEnTotal(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaReemplazoTotal(((respuesta.datos && respuesta.datos != null && respuesta.datos.length > 0)?(respuesta.datos):([])));
					setTimeout(function() {				
						ref.moduloJG.comPopUpListaReemplazosTotal.modal('show');
					}, 500);
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		eliminarRemplazosYactualizarValorVarConConfirmacion4: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.eliminarRemplazosYactualizarValorVarConConfirmacion4(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosTotal.modal('hide');
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		insertUpdateReemplazoTotal:function(){
			var ref=this;
			
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoTotal.id_reemplazo,
					'codDependencia' : ref.moduloJG.objReemplazoTotal.dependencia.val(),
					'rol' : ref.moduloJG.objReemplazoTotal.rol.val(),
					'usuarioSaliente' : ref.moduloJG.objReemplazoTotal.usuarioSaliente.val(),
					'usuarioEntrante' :ref.moduloJG.objReemplazoTotal.usuarioEntrante.val(),
					'fechaInicio' : ref.moduloJG.objReemplazoTotal.fechaReemplazoInicio.val(),
					'fechaTermino' : ref.moduloJG.objReemplazoTotal.fechaReemplazoFin.val(),
					'referencia' : ref.moduloJG.objReemplazoTotal.referencia.val(),
					'accion' : ref.moduloJG.objReemplazoTotal.accion,
					'tipoReemplazo': "TOTAL"
			}
			
			if(!($("#cmbDependenciaReempTotal").val() != undefined && $("#cmbDependenciaReempTotal").val() != null && $("#cmbDependenciaReempTotal").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Dependencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbRolReempTotal").val() != undefined && $("#cmbRolReempTotal").val() != null && $("#cmbRolReempTotal").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Rol es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbFuncionarioReempTotal").val() != undefined && $("#cmbFuncionarioReempTotal").val() != null && $("#cmbFuncionarioReempTotal").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario a ser reemplazado es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbFuncReempTotal").val() != undefined && $("#cmbFuncReempTotal").val() != null && $("#cmbFuncReempTotal").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario que realizará el reemplazo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaReemplazoTotalInicio").val() != undefined && $("#txtFechaReemplazoTotalInicio").val() != null && $("#txtFechaReemplazoTotalInicio").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Fecha Reemplazo - Inicio es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaReemplazoTotalHasta").val() != undefined && $("#txtFechaReemplazoTotalHasta").val() != null && $("#txtFechaReemplazoTotalHasta").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Fecha Reemplazo - Fin es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			/*if(!($("#txtReferencia").val() != undefined && $("#txtReferencia").val() != null && $("#txtReferencia").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Referencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}*/
			
			ref.modSistcorr.procesar(true);
			ref.moduloJG.validarReemplazo(ref.parametros)
				.then(function(respuesta){
					if(respuesta.estado == true){
						if (respuesta.datos!=null && respuesta.datos.length>0){
							var codigo=respuesta.datos[0].codigo;
							var mensaje=respuesta.datos[0].descripcion;
							var codigoaAxu=respuesta.datos[0].codigoAux;
							
							if(ref.moduloJG.objReemplazoTotal.accion == "M"){
								if (mensaje!=""){
									if(parseInt(codigo) > 0){
										ref.modSistcorr.procesar(false);
										ref.codigoConfirm = codigo;
										ref.mensajeConfirm = mensaje;
										ref.codigoAxu = codigoaAxu;
										$("#msjConfirmacion").html(ref.mensajeConfirm);
										ref.moduloJG.comRegTotal.myModal.modal('hide');
										
										setTimeout(function() {				
											ref.moduloJG.comPopUpConfirmacion.modal('show');
										}, 500);
									}else{
										ref.modSistcorr.notificar("ERROR", mensaje, "Error");
										ref.modSistcorr.procesar(false);
									}
								}else{
									ref.grabarReemplazoTotalSinConfirmar(false);
								}
							}else{
								if (mensaje!="" && codigo=="0"){
									ref.modSistcorr.notificar("ERROR", mensaje, "Error");
									ref.modSistcorr.procesar(false);
								}else{
									if(parseInt(codigoaAxu) > 0){
										ref.grabarReemplazoTotalSinConfirmar(false);
									}else{
										if (codigo!="0"){
											ref.modSistcorr.procesar(false);
											ref.codigoConfirm = codigo;
											ref.mensajeConfirm = mensaje;
											ref.codigoAxu = codigoaAxu;
											$("#msjConfirmacion").html(ref.mensajeConfirm);
											ref.moduloJG.comRegTotal.myModal.modal('hide');
											
											setTimeout(function() {				
												ref.moduloJG.comPopUpConfirmacion.modal('show');
											}, 500);
											
										}else{
											ref.grabarReemplazoTotalSinConfirmar(false);
										}
									}
								}
							}
						}
						
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistCorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);					
				});
		},
		resetarfiltroJGs: function(){
			var ref = this;
			ref.filtroJG = {};
			ref.moduloJG.componentesJG.cmbUsuarioSaliente.val("");
			ref.moduloJG.componentesJG.cmbUsuarioSaliente.change();
			ref.moduloJG.componentesJG.cmbUsuarioEntrante.val("");
			ref.moduloJG.componentesJG.cmbUsuarioEntrante.change();
			ref.moduloJG.componentesJG.cmbRol.val("");
			ref.moduloJG.componentesJG.cmbRol.change();
			ref.moduloJG.componentesJG.cmbDependencia.val("");
			ref.moduloJG.componentesJG.cmbDependencia.change();		
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			
		},
		
		abrirModalRegTotal : function(){
			var ref = this;
			ref.limpiarFormularioRempTotal();
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comRegTotal.myModal.modal('show');
			setTimeout(function() {
				ref.iniciarCombosAutoCompletados();
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 1500);
		},
		
		limpiarFormularioRempTotal : function(){
			var ref = this;
			
			ref.moduloJG.objReemplazoTotal.dependencia.val("")
			ref.moduloJG.objReemplazoTotal.dependencia.change();
			ref.moduloJG.objReemplazoTotal.fechaReemplazoInicio.val("");
			ref.moduloJG.objReemplazoTotal.fechaReemplazoInicio.change();
			ref.moduloJG.objReemplazoTotal.fechaReemplazoFin.val("");
			ref.moduloJG.objReemplazoTotal.fechaReemplazoFin.change();
			ref.moduloJG.objReemplazoTotal.referencia.val("");
			ref.moduloJG.objReemplazoTotal.referencia.change();
			ref.moduloJG.objReemplazoTotal.rol.val("")
			ref.moduloJG.objReemplazoTotal.rol.change();
			ref.moduloJG.objReemplazoTotal.usuarioSaliente.val("")
			ref.moduloJG.objReemplazoTotal.usuarioSaliente.change();
			ref.moduloJG.objReemplazoTotal.usuarioEntrante.val("")
			ref.moduloJG.objReemplazoTotal.usuarioEntrante.change();
			ref.moduloJG.objReemplazoTotal.accion="";
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			ref.inicializarTablaConsulta();
		},
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = JSON.parse(sessionStorage.getItem("FILTRO_REMPLAZO_TOTAL")) || {};
			ref.filtroJG = _filtroJG;			
		},
		
		abrirModalElimTotal : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comElimTotal.myModal.modal('show');
			setTimeout(function() {
				console.log('abre modal eliminar');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		inicializarcomponentesJG: function(){
			var ref = this;		
			
			ref.componentesJG.txtFechaDocumentoDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentesJG.txtFechaDocumentoHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.click();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.focus();
			});	
			
			ref.componentesJG.txtFechaReemplazoTotalInicio = ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.datepicker({
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
			
			ref.componentesJG.txtFechaReemplazoTotalInicio = ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaDesdeTotal.click(function(){
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.click();
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalInicio.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaHastTotal.click(function(){
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.click();
				ref.moduloJG.componentesJG.txtFechaReemplazoTotalHasta.focus();
			});		
			 
		},
		
		buscarReemplazosTotal: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.usuarioSaliente=ref.moduloJG.componentesJG.cmbUsuarioSaliente.val();
			ref.filtroJG.usuarioSalienteTexto=  $("#cmbUsuarioSaliente option:selected").text();
			ref.filtroJG.usuarioEntrante=ref.moduloJG.componentesJG.cmbUsuarioEntrante.val();
			ref.filtroJG.usuarioEntranteTexto= $("#cmbUsuarioEntrante option:selected").text();
			ref.filtroJG.rol=ref.moduloJG.componentesJG.cmbRol.val();
			ref.filtroJG.rolTexto=$("#cmbRol option:selected").text();
			ref.filtroJG.dependencia=ref.moduloJG.componentesJG.cmbDependencia.val();
			ref.filtroJG.dependenciaTexto=$("#cmbDependencia option:selected").text();
			ref.filtroJG.fechaDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val();
			ref.filtroJG.fechaHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val();				
			ref.searchReemplazosTotal();
			
			if(!(ref.filtroJG.usuarioEntrante != null && ref.filtroJG.usuarioEntrante != undefined)){
				ref.filtroJG.usuarioEntrante = "";
			}
			if(!(ref.filtroJG.usuarioSaliente != null && ref.filtroJG.usuarioSaliente != undefined)){
				ref.filtroJG.usuarioSaliente = "";
			}
			
			sessionStorage.setItem("FILTRO_REMPLAZO_TOTAL", JSON.stringify(ref.filtroJG));
		},
		
		searchReemplazosTotal: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.inicializarTablaConsulta();
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.exportarExcel(ref.filtroJG)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_ReemplazosTotal.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_ReemplazosTotal.xlsx';
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
				ref.moduloJG.componentesJG.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.moduloJG.URL_CONSULTA_REEMPLAZO_TOTAL,
			        	"type": "GET",
			        	"data": ref.filtroJG,
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
			        			datosConsulta = response.datos[0].listOfDataObjects;
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
			        	}
			        },
			        cache: true,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id_reemplazo', title: 'Elim.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarReemplazo' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id_reemplazo +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id_reemplazo', title: 'Mod.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-edit icon_editar btnModificarReemplazo'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id_reemplazo +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'usuarioSaliente', title: 'Usuario Saliente', defaultContent: ''},
			        	{data: 'usuarioEntrante', title: 'Usuario Entrante', defaultContent: ''},
			        	{data: 'rol', title: 'Rol', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dependencia', defaultContent: ''},
			        	{data: 'fechaInicio', title: 'Fecha Inicio', defaultContent: ''},
			        	{data: 'fechaTermino', title: 'Fecha Termino', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	ref.modSistcorr.procesar(false);
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		
			        	}, 1000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.moduloJG.btnModificarReemplazo = $(".btnModificarReemplazo");
					ref.moduloJG.btnEliminarReemplazo = $(".btnEliminarReemplazo");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'page.dt', function () {
					
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnModificarReemplazo = $(".btnModificarReemplazo");
						ref.moduloJG.btnEliminarReemplazo = $(".btnEliminarReemplazo");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'order.dt', function () {
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnModificarReemplazo = $(".btnModificarReemplazo");
						ref.moduloJG.btnEliminarReemplazo = $(".btnEliminarReemplazo");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.moduloJG.btnModificarReemplazo = $(".btnModificarReemplazo");
					ref.moduloJG.btnEliminarReemplazo = $(".btnEliminarReemplazo");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		inicializarTablaReemplazoTotal: function(data){
			var ref = this;
			console.log("Inicializando inicializarTablaReemplazoTotal")
			if(ref.dataTableRA){
				ref.dataTableRA.destroy();
				ref.moduloJG.componentesJG.dataTableRA.empty();
				ref.dataTableRA = null;
				ref.inicializarTablaReemplazoTotal(data);
			} else {
				ref.moduloJG.componentesJG.dataTableRA.show();
				ref.dataTableRA = ref.moduloJG.componentesJG.dataTableRA.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 3,
			        "data": data,
			        "columns": [
			        	
			        	{data: 'usuarioSaliente', title: 'Usuario Saliente', defaultContent: ''},
			        	{data: 'usuarioEntrante', title: 'Usuario Entrante', defaultContent: ''},
			        	{data: 'rol', title: 'Rol', defaultContent: ''},
			        	{data: 'dependencia', title: 'Dependencia', defaultContent: ''},
			        	{data: 'fechaInicio', title: 'Fecha Inicio', defaultContent: ''},
			        	{data: 'fechFin', title: 'Fecha Fin', defaultContent: ''},
			        	{data: 'estado', title: 'Estado', defaultContent: ''},
			        ],
			        "columnDefs": [{
			        	"targets": [0],
			        	"width": '15px',
			        	"orderable": false
			        }],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTableRA.responsive.rebuild();
			        		ref.dataTableRA.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTableRA.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					
				});
				
				ref.dataTableRA.on( 'page.dt', function () {
					ref.dataTableRA.responsive.rebuild();
	        		ref.dataTableRA.responsive.recalc();
				});
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.moduloJG.irADetalle(this.dataset.id);
				});
			}
		},
		
		eventoDocumentoPrincipal: function(){
			var ref = this;			
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click',function(){
				var comp=$(this);
				var idReemplazo= comp.attr("data-id");
				
				for (var i in datosConsulta){
					var reemp=datosConsulta[i];
					if (reemp.id_reemplazo==idReemplazo){
						ref.moduloJG.objReemplazoTotal.id_reemplazo=reemp.id_reemplazo;
						ref.moduloJG.objReemplazoTotal.dependencia.append("<option value='" +reemp.codDependencia + "' selected='selected'>" +reemp.dependencia+ "</option>")
						ref.moduloJG.objReemplazoTotal.dependencia.change();
						ref.moduloJG.objReemplazoTotal.fechaReemplazoInicio.val(reemp.fechaInicio);
						ref.moduloJG.objReemplazoTotal.fechaReemplazoInicio.change();
						ref.moduloJG.objReemplazoTotal.fechaReemplazoFin.val(reemp.fechaTermino);
						ref.moduloJG.objReemplazoTotal.fechaReemplazoFin.change();
						ref.moduloJG.objReemplazoTotal.referencia.val(reemp.referencia);
						ref.moduloJG.objReemplazoTotal.referencia.change();
						ref.moduloJG.objReemplazoTotal.rol.append("<option value='" +reemp.rol + "' selected='selected'>" +reemp.rol+ "</option>")
						ref.moduloJG.objReemplazoTotal.rol.change();
						ref.moduloJG.objReemplazoTotal.usuarioSaliente.append("<option value='" +reemp.codUsuarioSaliente + "' selected='selected'>" +reemp.usuarioSaliente+ "</option>")
						ref.moduloJG.objReemplazoTotal.usuarioSaliente.change();
						ref.moduloJG.objReemplazoTotal.usuarioEntrante.append("<option value='" +reemp.codUsuarioEntrante + "' selected='selected'>" +reemp.usuarioEntrante+ "</option>")
						ref.moduloJG.objReemplazoTotal.usuarioEntrante.change();
						ref.moduloJG.objReemplazoTotal.accion="M";
						break;
					}
				}
				setTimeout(function(){					
					ref.moduloJG.comRegTotal.myModal.modal('show');
				}, 300);
			
			}),
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				ref.moduloJG.objReemplazoTotal.id_reemplazo = idReemplazo;
				setTimeout(function(){
					ref.abrirModalElimTotal();
					console.log("eventoDocumentoPrincipal - abrirá modal Eliminar");
				}, 500);
			}),
			
			ref.moduloJG.btnEliminarReemplazoTotal.off('click').on('click', function(){
				ref.eliminarReemplazoTotal();
			})
			
		}
};

setTimeout(function() {
	REEMPLAZOS_TOTAL_VISTA.moduloJG = modulo_reemplazos_total;
	REEMPLAZOS_TOTAL_VISTA.modSistcorr = modulo_sistcorr;
	REEMPLAZOS_TOTAL_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	REEMPLAZOS_TOTAL_VISTA.jefe = ES_JEFE;
	REEMPLAZOS_TOTAL_VISTA.listas = {};
	REEMPLAZOS_TOTAL_VISTA.listas.dependencias = new LISTA_DATA([]);
	REEMPLAZOS_TOTAL_VISTA.listas.originadores = new LISTA_DATA([]);
	REEMPLAZOS_TOTAL_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	REEMPLAZOS_TOTAL_VISTA.inicializar();
}, 200);