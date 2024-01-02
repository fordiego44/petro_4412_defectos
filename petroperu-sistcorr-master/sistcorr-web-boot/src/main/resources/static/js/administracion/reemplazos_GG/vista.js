var modulo_reemplazo_apoyo= MODULO_REEMPLAZO_APOYO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REEMPLAZO_APOYO_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {combosSimples:{}, combosS2: {}, datePikers:{}},
		filtroJG: {},
		masfiltroJGs: false,
		dataTable: null,
		dataTableRA: null,
		dataTableConsulta: null,
		dependenciasJG: [],
		jefe: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();			
			ref.inicializarcomponentesJG();
			setTimeout(function(){
				ref.inicialiarCombosFiltros();
				ref.setFormCombosFiltros();
			}, 2000);
		},
		
		inicialiarCombosFiltros :function(){
			var ref=this;
			
			ref.componentesJG.combosS2.desDependencia = ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.select2({
				ajax: {
					url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
					delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo: 'APOYO',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_change($comp.attr('id'));
            	ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.val("");
            	ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			
			ref.componentesJG.combosS2.jefeDependencia =  ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.val(),
				        		rol:'JEFE',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
	        	ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.val("");
            	ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncApoyo =  ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'APOYO',
				        		rol:'JEFE',
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
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
			
			
			/*VENTANA REGISTRO*/
			ref.componentesJG.combosS2.desDependReemplazoApoyo = ref.moduloJG.componentesJG.cmbDependReemplazoApoyo.select2({
				ajax: {
					url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
					delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo: 'APOYO',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
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
			
			
			ref.componentesJG.combosS2.jefeDependenciaRegistro =  ref.moduloJG.componentesJG.cmbJefeDependencia.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependReemplazoApoyo.val(),
				        		rol:'JEFE',
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
				    	return {results: respuesta.datos};
				    }
				}
			}).change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_change($comp.attr('id'));
	        	ref.moduloJG.componentesJG.cmbFuncApoyo.val("");
            	ref.moduloJG.componentesJG.cmbFuncApoyo.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncApoyoRegistro =  ref.moduloJG.componentesJG.cmbFuncApoyo.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'APOYO',
				        		rol:'JEFE',
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependReemplazoApoyo.val(),
				        		q: params.term
				        }
				        return query;
				    },
				    processResults: function (respuesta) {
				    	ref.listas.dependencias.agregarLista(respuesta.datos);
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
				ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.append("<option value='" +ref.filtroJG.dependencia + "' selected='selected'>" +ref.filtroJG.dependenciaTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.change();


				ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.append("<option value='" +ref.filtroJG.usuarioSaliente + "' selected='selected'>" +ref.filtroJG.jefeDependenciaTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.change();

				ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.append("<option value='" +ref.filtroJG.usuarioEntrante + "' selected='selected'>" +ref.filtroJG.funcionarioApoyoTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.change();

				ref.moduloJG.componentesJG.txtFecInicioGGFiltro.val(ref.filtroJG.fechaDesde);
				ref.moduloJG.componentesJG.txtFecInicioGGFiltro.change();
				ref.moduloJG.componentesJG.txtFecFinGGFiltro.val(ref.filtroJG.fechaHasta);
				ref.moduloJG.componentesJG.txtFecFinGGFiltro.change();
			}
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				ref.buscarApoyoGG();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltroGG();
			});			
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReempAdicion = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comElimApoyo.myModal.modal('show');
				}, 100);
			});
			
			ref.moduloJG.btnEliminarReemplazoApoyo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
			});
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				setTimeout(function(){
					//ref.iniciarCombosAutoCompletados();
					ref.moduloJG.comRegApoyo.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			ref.moduloJG.componentesJG.btnRegistrarApoyo.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				ref.abrirModalRegApoyo();
			});
			
			ref.moduloJG.componentesJG.btnGuardarApoyo.off('click').on('click', function(){
				ref.insertUpdateReemplazoApoyo();
			});
			
			ref.moduloJG.btnAceptarConfirmacion.off('click').on('click', function(){
				ref.aceptarConfirmacionReemplazoApoyo();
			});
			
			ref.moduloJG.btnPreAceptarBorrarReemplazosApoyo.off('click').on('click', function(){
				ref.moduloJG.comPopUpListaReemplazosApoyo.modal('hide');
				setTimeout(function() {				
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosApoyo.modal('show');
				}, 500);
			});
			
			ref.moduloJG.btnAceptarBorrarReemplazosApoyo.off('click').on('click', function(){
				ref.eliminarRemplazosYactualizarValorVarConConfirmacion4();
			});
			
			setTimeout(function() {				
				ref.obtenerfiltroJGs();
				if(Object.keys(ref.filtroJG).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					console.log("actualizar y buscar correspondencias");
					ref.searchApoyoGG();
				}
			}, 1500);
			
		},
		
		aceptarConfirmacionReemplazoApoyo: function(){
			var ref = this;
			if(ref.codigoConfirm != null && ref.codigoConfirm != undefined && ref.codigoConfirm.trim() != ""){
				switch (ref.codigoConfirm) {
				case "1":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "1";
					ref.grabarReemplazoApoyoConConfirmacion1(ref.parametros);
					break;
				case "2":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "2";
					ref.grabarReemplazoApoyoConConfirmacion2(ref.parametros);
					break;
				case "3":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "3";
					ref.grabarReemplazoApoyoConConfirmacion2(ref.parametros);
					break;
				case "4":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "4";
					ref.buscarReemplazosEnApoyo(ref.parametros);
					break;
				case "5":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "5";
					ref.grabarReemplazoApoyoConConfirmacion5(ref.parametros);
					break;
				default:
					break;
				}
			}else{
				
			}
		},
		
		grabarReemplazoApoyoConConfirmacion1: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoApoyoConConfirmacion1(ref.parametros)
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
		
		grabarReemplazoApoyoConConfirmacion5: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoApoyoConConfirmacion1(ref.parametros)
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
		
		grabarReemplazoApoyoConConfirmacion2: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoApoyoConConfirmacion2(ref.parametros)
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
		
		grabarReemplazoApoyoSinConfirmar: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoApoyoSinConfirmar(ref.parametros)
			.then(function(respuesta){
				
				if(respuesta.estado == true){
					ref.moduloJG.comRegApoyo.myModal.modal('hide');
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		buscarReemplazosEnApoyo: function(){
			var ref = this;
			ref.moduloJG.buscarReemplazosEnApoyo(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaReemplazoApoyo(((respuesta.datos && respuesta.datos != null && respuesta.datos.length > 0)?(respuesta.datos):([])));
					setTimeout(function() {				
						ref.moduloJG.comPopUpListaReemplazosApoyo.modal('show');
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
					//ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosApoyo.modal('hide');
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
		
		insertUpdateReemplazoApoyo: function(){
			var ref = this;
			
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoApoyo.id_reemplazo,
					'codDependencia' : ref.moduloJG.objReemplazoApoyo.dependencia.val(),
					'rol' : 'JEFE',
					'usuarioSaliente' : ref.moduloJG.objReemplazoApoyo.usuarioSaliente.val(),
					'usuarioEntrante' :ref.moduloJG.objReemplazoApoyo.usuarioEntrante.val(),
					'fechaInicio' : ref.moduloJG.objReemplazoApoyo.fechaReemplazoInicio.val(),
					'fechaTermino' : ref.moduloJG.objReemplazoApoyo.fechaReemplazoFin.val(),
					'referencia' : ref.moduloJG.objReemplazoApoyo.referencia.val(),
					'accion' : ref.moduloJG.objReemplazoApoyo.accion,
					'tipoReemplazo': "APOYO"
			}
			
			if(!($("#cmbDependReemplazoApoyo").val() != undefined && $("#cmbDependReemplazoApoyo").val() != null && $("#cmbDependReemplazoApoyo").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Dependencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbJefeDependencia").val() != undefined && $("#cmbJefeDependencia").val() != null && $("#cmbJefeDependencia").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Jefe Dependencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbFuncApoyo").val() != undefined && $("#cmbFuncApoyo").val() != null && $("#cmbFuncApoyo").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario que realizará el apoyo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaApoyoDesde").val() != undefined && $("#txtFechaApoyoDesde").val() != null && $("#txtFechaApoyoDesde").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Fecha Reemplazo - Inicio es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaApoyoHasta").val() != undefined && $("#txtFechaApoyoHasta").val() != null && $("#txtFechaApoyoHasta").val().trim() != "")){
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
							
							if(ref.moduloJG.objReemplazoApoyo.accion == "M"){
								if (mensaje!=""){
									if(parseInt(codigo) > 0){
										ref.modSistcorr.procesar(false);
										ref.codigoConfirm = codigo;
										ref.mensajeConfirm = mensaje;
										ref.codigoAxu = codigoaAxu;
										$("#msjConfirmacion").html(ref.mensajeConfirm);
										ref.moduloJG.comRegApoyo.myModal.modal('hide');
										
										setTimeout(function() {				
											ref.moduloJG.comPopUpConfirmacion.modal('show');
										}, 500);
									}else{
										ref.modSistcorr.notificar("ERROR", mensaje, "Error");
										ref.modSistcorr.procesar(false);
									}
								}else{
									ref.grabarReemplazoApoyoSinConfirmar();
								}
							}else{
								if (mensaje!=""){
									ref.modSistcorr.notificar("ERROR", mensaje, "Error");
									ref.modSistcorr.procesar(false);
								}else{
									ref.grabarReemplazoApoyoSinConfirmar();
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
		
		resetarfiltroGG: function(){
			var ref = this;
			ref.filtroJG = {};
			ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.val("");
			ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.change();
			ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.val("");
			ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.change();
			ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.val("");
			ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.change();
			ref.moduloJG.componentesJG.txtFecInicioGGFiltro.val("");
			ref.moduloJG.componentesJG.txtFecInicioGGFiltro.change();
			ref.moduloJG.componentesJG.txtFecFinGGFiltro.val("");
			ref.moduloJG.componentesJG.txtFecFinGGFiltro.change();
			ref.actualizarValoresPorDefecto();
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.inicializarTablaConsulta();			
		},
		
		abrirModalRegApoyo : function(){
			var ref = this;
			ref.limpiarFormularioRempGG();
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comRegApoyo.myModal.modal('show');
			setTimeout(function() {
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 1500);
		},
		
		limpiarFormularioRempGG : function(){
			var ref = this;
			
			ref.moduloJG.objReemplazoApoyo.dependencia.val("")
			ref.moduloJG.objReemplazoApoyo.dependencia.change();
			ref.moduloJG.objReemplazoApoyo.usuarioSaliente.val("");
			ref.moduloJG.objReemplazoApoyo.usuarioSaliente.change();
			ref.moduloJG.objReemplazoApoyo.usuarioEntrante.val("");
			ref.moduloJG.objReemplazoApoyo.usuarioEntrante.change();
			ref.moduloJG.objReemplazoApoyo.referencia.val("");
			ref.moduloJG.objReemplazoApoyo.referencia.change();
			
			ref.moduloJG.objReemplazoApoyo.fechaReemplazoInicio.val("")
			ref.moduloJG.objReemplazoApoyo.fechaReemplazoInicio.change();
			ref.moduloJG.objReemplazoApoyo.fechaReemplazoFin.val("")
			ref.moduloJG.objReemplazoApoyo.fechaReemplazoFin.change();
			
			ref.moduloJG.objReemplazoApoyo.accion="";
		},
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = JSON.parse(sessionStorage.getItem("FILTRO_REMPLAZO_GG")) || {};
			ref.filtroJG = _filtroJG;			
		},
		
		abrirModalElimApoyo : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comElimApoyo.myModal.modal('show');
			setTimeout(function() {
				console.log('abre modal eliminar');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		inicializarcomponentesJG: function(){
			var ref = this;		
			
			ref.componentesJG.txtFecInicioGGFiltro = ref.moduloJG.componentesJG.txtFecInicioGGFiltro.datepicker({
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
			
			ref.componentesJG.txtFecFinGGFiltro = ref.moduloJG.componentesJG.txtFecFinGGFiltro.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaInicioFiltro.click(function(){
				ref.moduloJG.componentesJG.txtFecInicioGGFiltro.click();
				ref.moduloJG.componentesJG.txtFecInicioGGFiltro.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaFinFiltro.click(function(){
				ref.moduloJG.componentesJG.txtFecFinGGFiltro.click();
				ref.moduloJG.componentesJG.txtFecFinGGFiltro.focus();
			});		
			
			/*FORMULARIO REGISTRO*/
			ref.componentesJG.txtFecInicioGGFiltro = ref.moduloJG.componentesJG.txtFechaApoyoDesde.datepicker({
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
			
			ref.componentesJG.txtFecFinGGFiltro = ref.moduloJG.componentesJG.txtFechaApoyoHasta.datepicker({
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
			
			
			ref.moduloJG.componentesJG.btnFechaApoyoDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaApoyoDesde.click();
				ref.moduloJG.componentesJG.txtFechaApoyoDesde.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaApoyoHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaApoyoHasta.click();
				ref.moduloJG.componentesJG.txtFechaApoyoHasta.focus();
			});		
			 
		},
		
		buscarApoyoGG: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.dependencia = ref.moduloJG.componentesJG.cmbDependenciaFiltroGG.val();
			ref.filtroJG.dependenciaTexto=  $("#cmbDependenciaFiltroGG option:selected").text();
			ref.filtroJG.usuarioSaliente = ref.moduloJG.componentesJG.cmbJefeDependenciaFiltroGG.val();
			ref.filtroJG.jefeDependenciaTexto=  $("#cmbJefeDependenciaFiltroGG option:selected").text();
			ref.filtroJG.usuarioEntrante = ref.moduloJG.componentesJG.cmbFuncionarioApoyoFiltroGG.val();
			ref.filtroJG.funcionarioApoyoTexto=  $("#cmbFuncionarioApoyoFiltroGG option:selected").text();
			ref.filtroJG.fechaDesde = ref.moduloJG.componentesJG.txtFecInicioGGFiltro.val();
			ref.filtroJG.fechaHasta = ref.moduloJG.componentesJG.txtFecFinGGFiltro.val();
			ref.filtroJG.rol = "JEFE";
			if(!(ref.filtroJG.dependencia != null && ref.filtroJG.dependencia != undefined)){
				ref.filtroJG.dependencia = "";
			}
			if(!(ref.filtroJG.usuarioSaliente != null && ref.filtroJG.usuarioSaliente != undefined)){
				ref.filtroJG.usuarioSaliente = "";
			}
			if(!(ref.filtroJG.usuarioEntrante != null && ref.filtroJG.usuarioEntrante != undefined)){
				ref.filtroJG.usuarioEntrante = "";
			}
			ref.searchApoyoGG();
			
			sessionStorage.setItem("FILTRO_REMPLAZO_GG", JSON.stringify(ref.filtroJG));
		},
		
		searchApoyoGG: function(){
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
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_reemplazoApoyo.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_reemplazoApoyo.xlsx';
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
			        	"url": ref.moduloJG.URL_CONSULTA_REEMPLAZO_APOYO,
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
		
		eventoDocumentoPrincipal: function(){
			
			var ref = this;			
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click',function(){
				var comp=$(this);
				var idReemplazo= comp.attr("data-id");
				
				for (var i in datosConsulta){
					var reemp=datosConsulta[i];
					if (reemp.id_reemplazo==idReemplazo){
						ref.moduloJG.objReemplazoApoyo.id_reemplazo=reemp.id_reemplazo;
						ref.moduloJG.objReemplazoApoyo.dependencia.append("<option value='" +reemp.codDependencia + "' selected='selected'>" +reemp.dependencia+ "</option>")
						ref.moduloJG.objReemplazoApoyo.dependencia.change();
						ref.moduloJG.objReemplazoApoyo.fechaReemplazoInicio.val(reemp.fechaInicio);
						ref.moduloJG.objReemplazoApoyo.fechaReemplazoInicio.change();
						ref.moduloJG.objReemplazoApoyo.fechaReemplazoFin.val(reemp.fechaTermino);
						ref.moduloJG.objReemplazoApoyo.fechaReemplazoFin.change();
						ref.moduloJG.objReemplazoApoyo.referencia.val(reemp.referencia);
						ref.moduloJG.objReemplazoApoyo.referencia.change();
						ref.moduloJG.objReemplazoApoyo.usuarioSaliente.append("<option value='" +reemp.codUsuarioSaliente + "' selected='selected'>" +reemp.usuarioSaliente+ "</option>")
						ref.moduloJG.objReemplazoApoyo.usuarioSaliente.change();
						ref.moduloJG.objReemplazoApoyo.usuarioEntrante.append("<option value='" +reemp.codUsuarioEntrante + "' selected='selected'>" +reemp.usuarioEntrante+ "</option>")
						ref.moduloJG.objReemplazoApoyo.usuarioEntrante.change();
						ref.moduloJG.objReemplazoApoyo.accion="M";
						break;
					}
				}
				setTimeout(function(){					
					ref.moduloJG.comRegApoyo.myModal.modal('show');
				}, 300);
			}),
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				ref.moduloJG.objReemplazoApoyo.id_reemplazo = idReemplazo;
				setTimeout(function(){
					ref.abrirModalElimApoyo();
					console.log("eventoDocumentoPrincipal - abrirá modal Eliminar");
				}, 500);
			}),
			
			ref.moduloJG.btnEliminarReemplazoApoyo.off('click').on('click', function(){
				ref.eliminarReemplazoApoyo();
			})
			
		},
		
		eliminarReemplazoApoyo: function(){
			var ref=this;
			var comp = $(this);
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoApoyo.id_reemplazo
			};			
			ref.modSistcorr.procesar(true);			
			ref.moduloJG.eliminarReemplazoApoyo(ref.parametros)
				.then(function(respuesta){
					console.log("Respuesta.estado:" + respuesta.estado)
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modSistcorr.procesar(false);
						
						ref.moduloJG.comElimApoyo.myModal.modal('hide');
						setTimeout(function(){					
							ref.inicializarTablaConsulta();
						}, 1000);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
					
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comElimApoyo.myModal.modal('hide');
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		inicializarTablaReemplazoApoyo: function(data){
			var ref = this;
			console.log("Inicializando inicializarTablaReemplazoApoyo")
			if(ref.dataTableRA){
				ref.dataTableRA.destroy();
				ref.moduloJG.componentesJG.dataTableRA.empty();
				ref.dataTableRA = null;
				ref.inicializarTablaReemplazoApoyo(data);
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
			        	{data: 'fechaFin', title: 'Fecha Fin', defaultContent: ''},
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
		}
};

setTimeout(function() {
	REEMPLAZO_APOYO_VISTA.moduloJG = modulo_reemplazo_apoyo;
	REEMPLAZO_APOYO_VISTA.modSistcorr = modulo_sistcorr;
	REEMPLAZO_APOYO_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	REEMPLAZO_APOYO_VISTA.jefe = ES_JEFE;
	REEMPLAZO_APOYO_VISTA.listas = {};
	REEMPLAZO_APOYO_VISTA.listas.dependencias = new LISTA_DATA([]);
	REEMPLAZO_APOYO_VISTA.listas.originadores = new LISTA_DATA([]);
	REEMPLAZO_APOYO_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	REEMPLAZO_APOYO_VISTA.inicializar();
}, 200);