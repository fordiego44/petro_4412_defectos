var modulo_reemplazos_adicion= MODULO_REEMPLAZOS_ADICION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REEMPLAZOS_ADICION_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {combosSimples:{}, combosS2: {}, datePikers:{}},
		filtroJG: {},
		dataTable: null,
		dataTableRA: null,
		dataTableConsulta: null,
		datosConsulta: [],
		codigoConfirm: null,
		mensajeConfirm: null,
		jefe: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.inicializarcomponentesJG();
			ref.iniciarCombosAutoCompletados();
			setTimeout(function(){
				ref.inicialiarCombosFiltros();
				ref.setFormCombosFiltros();
				ref.inicializarTablaConsulta();
			}, 2000);
		},
		
		inicialiarCombosFiltros :function(){
			var ref=this;
			
			ref.componentesJG.combosS2.desDependencia = ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.select2({
				ajax: {
					url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
					delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo: 'ADICION',
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
			
			ref.componentesJG.combosS2.rol =  ref.moduloJG.componentesJG.cmbRolFiltroAdicion.select2({
				ajax: {
				    url: ref.moduloJG.URL_BUSCAR_ROL_X_DEPENDENCIA,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.val(),
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
			
			
			ref.componentesJG.combosS2.usuarioSaliente =  ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.val(),
				        		rol:ref.moduloJG.componentesJG.cmbRolFiltroAdicion.val(),
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
			
			ref.componentesJG.combosS2.usuarioEntrante =  ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'ADICION',
				        		rol:ref.moduloJG.componentesJG.cmbRolFiltroAdicion.val(),
				        		codDependencia: ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.val(),
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
				ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.append("<option value='" +ref.filtroJG.dependencia + "' selected='selected'>" +ref.filtroJG.dependenciaTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.change();


				ref.moduloJG.componentesJG.cmbRolFiltroAdicion.append("<option value='" +ref.filtroJG.rol + "' selected='selected'>" +ref.filtroJG.rolTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbRolFiltroAdicion.change();

				ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.append("<option value='" +ref.filtroJG.usuarioSaliente + "' selected='selected'>" +ref.filtroJG.usuarioSalienteTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.change();

				ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.append("<option value='" +ref.filtroJG.usuarioEntrante + "' selected='selected'>" +ref.filtroJG.usuarioEntranteTexto+ "</option>");
				ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.change();


				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val(ref.filtroJG.fechaDesde);
				ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val(ref.filtroJG.fechaHasta);
				ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
			}
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				ref.buscarReemplazoAdicion();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltroJGs();
			});			
			
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			ref.moduloJG.compBtnRegistrarAdicion.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				ref.abrirModalRegAdicion();
			});
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				setTimeout(function(){
					//ref.iniciarCombosAutoCompletados();
					ref.moduloJG.comRegAdicion.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReempAdicion = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comElimAdicion.myModal.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnEliminarReemplazoAdicion.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
			});
			
			ref.moduloJG.btnRegistrarAdicion.off('click').on('click', function(){
				ref.insertUpdateReemplazoAdicion();
			});
			
			ref.moduloJG.btnAceptarConfirmacion.off('click').on('click', function(){
				ref.aceptarConfirmacionReemplazoAdicion();
			});
			
			ref.moduloJG.btnAceptarMensajeRolMenorReemplazo.off('click').on('click', function(){
				ref.moduloJG.comPopUpMensajeRolMenorReemplazo.modal('hide');
				setTimeout(function() {				
					ref.modSistcorr.notificar("OK", "SE REGISTRO EL REEMPLAZO DE TIPO ADICIÓN CON ÉXITO", "Success");
					ref.inicializarTablaConsulta();
				}, 300);
			});
			
			ref.moduloJG.btnPreAceptarBorrarReemplazosAdicion.off('click').on('click', function(){
				ref.moduloJG.comPopUpListaReemplazosEnAdicion.modal('hide');
				setTimeout(function() {				
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosEnAdicion.modal('show');
				}, 500);
			});
			
			ref.moduloJG.btnAceptarBorrarReemplazosAdicion.off('click').on('click', function(){
				ref.eliminarRemplazosYactualizarValorVarConConfirmacion4();
			});
			
			setTimeout(function() {				
				ref.obtenerfiltroJGs();				
			}, 1500);
			
		},
		
		eliminarRemplazosYactualizarValorVarConConfirmacion4: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.eliminarRemplazosYactualizarValorVarConConfirmacion4(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					//ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comPopUpConfirmacionBorrarReemplazosEnAdicion.modal('hide');
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
		
		aceptarConfirmacionReemplazoAdicion: function(){
			var ref = this;
			if(ref.codigoConfirm != null && ref.codigoConfirm != undefined && ref.codigoConfirm.trim() != ""){
				switch (ref.codigoConfirm) {
				case "1":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "1";
					ref.grabarReemplazoAdicionConConfirmacion1(ref.parametros);
					break;
				case "2":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "2";
					ref.grabarReemplazoAdicionConConfirmacion2(ref.parametros);
					break;
				case "3":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "3";
					ref.grabarReemplazoAdicionConConfirmacion2(ref.parametros);
					break;
				case "4":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "4";
					ref.buscarReemplazosEnAdicion(ref.parametros);
					break;
				case "5":
					ref.moduloJG.comPopUpConfirmacion.modal('hide');
					ref.modSistcorr.procesar(true);
					ref.parametros.codConfirm = "5";
					ref.grabarReemplazoAdicionConConfirmacion5(ref.parametros);
					break;
				default:
					break;
				}
			}else{
				
			}
		},
		
		grabarReemplazoAdicionConConfirmacion1: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoAdicionConConfirmacion1(ref.parametros)
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
		
		grabarReemplazoAdicionConConfirmacion5: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoAdicionConConfirmacion1(ref.parametros)
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
		
		grabarReemplazoAdicionConConfirmacion2: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoAdicionConConfirmacion2(ref.parametros)
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
		
		buscarReemplazosEnAdicion: function(){
			var ref = this;
			ref.moduloJG.buscarReemplazosEnEdicion(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.procesar(false);
					ref.inicializarTablaReemplazoEnAdicion((  (respuesta.datos && respuesta.datos != null && respuesta.datos.length > 0)?(respuesta.datos):([])   ));
					setTimeout(function() {				
						ref.moduloJG.comPopUpListaReemplazosEnAdicion.modal('show');
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
		
		grabarReemplazoAdicionSinConfirmar: function(){
			var ref = this;
			ref.moduloJG.grabarReemplazoAdicionSinConfirmar(ref.parametros)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.moduloJG.comRegAdicion.myModal.modal('hide');
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
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		insertUpdateReemplazoAdicion: function(){
			var ref = this;
			
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoAdicion.id_reemplazo,
					'codDependencia' : ref.moduloJG.objReemplazoAdicion.dependencia.val(),
					'rol' : ref.moduloJG.objReemplazoAdicion.rol.val(),
					'usuarioSaliente' : ref.moduloJG.objReemplazoAdicion.usuarioSaliente.val(),
					'usuarioEntrante' :ref.moduloJG.objReemplazoAdicion.usuarioEntrante.val(),
					'fechaInicio' : ref.moduloJG.objReemplazoAdicion.fechaReemplazoInicio.val(),
					'fechaTermino' : ref.moduloJG.objReemplazoAdicion.fechaReemplazoFin.val(),
					'referencia' : ref.moduloJG.objReemplazoAdicion.referencia.val(),
					'accion' : ref.moduloJG.objReemplazoAdicion.accion,
					'tipoReemplazo': "ADICION"
			}
			
			if(!($("#cmbDependReemplazoAdicion").val() != undefined && $("#cmbDependReemplazoAdicion").val() != null && $("#cmbDependReemplazoAdicion").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Dependencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbRolReemplazoAdicion").val() != undefined && $("#cmbRolReemplazoAdicion").val() != null && $("#cmbRolReemplazoAdicion").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Rol es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbFuncionarioReemplazado").val() != undefined && $("#cmbFuncionarioReemplazado").val() != null && $("#cmbFuncionarioReemplazado").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario a ser reemplazado es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbFuncReemplazar").val() != undefined && $("#cmbFuncReemplazar").val() != null && $("#cmbFuncReemplazar").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Funcionario que realizará el reemplazo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaAdicionDesdeJG").val() != undefined && $("#txtFechaAdicionDesdeJG").val() != null && $("#txtFechaAdicionDesdeJG").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Fecha Reemplazo - Inicio es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#txtFechaAdicionHastaJG").val() != undefined && $("#txtFechaAdicionHastaJG").val() != null && $("#txtFechaAdicionHastaJG").val().trim() != "")){
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
							if(ref.moduloJG.objReemplazoAdicion.accion == ""){
								if (mensaje!="" && codigo=="0"){
									ref.modSistcorr.notificar("ERROR", mensaje, "Error");
									ref.modSistcorr.procesar(false);
								}else{
									if (codigo!="0"){
										ref.modSistcorr.procesar(false);
										ref.codigoConfirm = codigo;
										ref.mensajeConfirm = mensaje;
										$("#msjConfirmacion").html(ref.mensajeConfirm);
										ref.moduloJG.comRegAdicion.myModal.modal('hide');
										
										setTimeout(function() {				
											ref.moduloJG.comPopUpConfirmacion.modal('show');
										}, 500);
										
									}else{
										ref.grabarReemplazoAdicionSinConfirmar();
									}
								}
							}else{
								if (mensaje!=""){
									if (parseInt(codigo) > 0){
										ref.modSistcorr.procesar(false);
										ref.codigoConfirm = codigo;
										ref.mensajeConfirm = mensaje;
										$("#msjConfirmacion").html(ref.mensajeConfirm);
										ref.moduloJG.comRegAdicion.myModal.modal('hide');
										
										setTimeout(function() {				
											ref.moduloJG.comPopUpConfirmacion.modal('show');
										}, 500);
									}else{
										ref.modSistcorr.notificar("ERROR", mensaje, "Error");
										ref.modSistcorr.procesar(false);
									}
								}else{
									ref.grabarReemplazoAdicionSinConfirmar();
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
		
		eliminarReemplazoAdicion: function(){
			var ref=this;
			var comp = $(this);
			ref.parametros={
					'id_reemplazo' : ref.moduloJG.objReemplazoAdicion.id_reemplazo
			};			
			ref.modSistcorr.procesar(true);			
			ref.moduloJG.eliminarReemplazoAdicion(ref.parametros)
				.then(function(respuesta){
					console.log("Respuesta.estado:" + respuesta.estado)
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modSistcorr.procesar(false);
						ref.moduloJG.comElimAdicion.myModal.modal('hide');
						setTimeout(function(){
							ref.inicializarTablaConsulta();
						}, 1000);
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comElimAdicion.myModal.modal('hide');
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		/*EVENTO BOTON LIMPIAR*/
		resetarfiltroJGs: function(){
			var ref = this;
			ref.filtroJG = {};
			ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.val("");
			ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.change();
			ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.val("");
			ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.change();
			ref.moduloJG.componentesJG.cmbRolFiltroAdicion.val("");
			ref.moduloJG.componentesJG.cmbRolFiltroAdicion.change();
			ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.val("");
			ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoDesde.change();
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val("");
			ref.moduloJG.componentesJG.txtFechaDocumentoHasta.change();
		},
		
		abrirModalRegAdicion : function(){
			var ref = this;
			ref.limpiarFormularioRempAdicion();
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comRegAdicion.myModal.modal('show');
			setTimeout(function() {
				//ref.iniciarCombosAutoCompletados();
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		limpiarFormularioRempAdicion : function(){
			var ref = this;
			
			ref.moduloJG.objReemplazoAdicion.dependencia.val("")
			ref.moduloJG.objReemplazoAdicion.dependencia.change();
			ref.moduloJG.objReemplazoAdicion.fechaReemplazoInicio.val("");
			ref.moduloJG.objReemplazoAdicion.fechaReemplazoInicio.change();
			ref.moduloJG.objReemplazoAdicion.fechaReemplazoFin.val("");
			ref.moduloJG.objReemplazoAdicion.fechaReemplazoFin.change();
			ref.moduloJG.objReemplazoAdicion.referencia.val("");
			ref.moduloJG.objReemplazoAdicion.referencia.change();
			ref.moduloJG.objReemplazoAdicion.rol.val("")
			ref.moduloJG.objReemplazoAdicion.rol.change();
			ref.moduloJG.objReemplazoAdicion.usuarioSaliente.val("")
			ref.moduloJG.objReemplazoAdicion.usuarioSaliente.change();
			ref.moduloJG.objReemplazoAdicion.usuarioEntrante.val("")
			ref.moduloJG.objReemplazoAdicion.usuarioEntrante.change();
			ref.moduloJG.objReemplazoAdicion.accion="";
		},
		
		abrirModalElimAdicion : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			
			ref.moduloJG.comElimAdicion.myModal.modal('show');
			setTimeout(function() {
				console.log('abre modal eliminar');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
			
			ref.componentesJG.combosS2.desDependencia = ref.moduloJG.comRegAdicion.cmbDependReemplazoAdicion.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_CMBDEPENDENCIAS_GG,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo: 'ADICION',
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
            	ref.moduloJG.comRegAdicion.cmbRolReemplazoAdicion.val("");
            	ref.moduloJG.comRegAdicion.cmbRolReemplazoAdicion.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desRol =  ref.moduloJG.comRegAdicion.cmbRolReemplazoAdicion.select2({
				ajax: {
				    url: ref.moduloJG.URL_BUSCAR_ROL_X_DEPENDENCIA,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.comRegAdicion.cmbDependReemplazoAdicion.val(),
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
            	ref.moduloJG.comRegAdicion.cmbFuncionarioReemplazado.val("");
            	ref.moduloJG.comRegAdicion.cmbFuncionarioReemplazado.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncReemplazado =  ref.moduloJG.comRegAdicion.cmbFuncionarioReemplazado.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_JEFE_X_DEPENDENCIA,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		codDependencia: ref.moduloJG.comRegAdicion.cmbDependReemplazoAdicion.val(),
				        		rol:ref.moduloJG.comRegAdicion.cmbRolReemplazoAdicion.val(),
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
            	ref.moduloJG.comRegAdicion.cmbFuncReemplazar.val("");
            	ref.moduloJG.comRegAdicion.cmbFuncReemplazar.change();
			}).on('select2:open', function(event){
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_open($comp.attr('id'));
			}).on('select2:closing', function (event) {
				var $comp = $(event.currentTarget);
            	ref.modSistcorr.select2_close($comp.attr('id'));
			});	
			
			ref.componentesJG.combosS2.desFuncReemplazar =  ref.moduloJG.comRegAdicion.cmbFuncReemplazar.select2({
				ajax: {
				    url: ref.moduloJG.URL_FILTRO_FUNCIONARIO_APOYO,
				    delay: 500,
				    data: function (params) {
				        var query = {
				        		tipoReemplazo:'ADICION',
				        		rol:ref.moduloJG.comRegAdicion.cmbRolReemplazoAdicion.val(),
				        		codDependencia: ref.moduloJG.comRegAdicion.cmbDependReemplazoAdicion.val(),
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
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = JSON.parse(sessionStorage.getItem("FILTRO_REMPLAZO_ADICION")) || {};
			ref.filtroJG = _filtroJG;			
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
			
			ref.moduloJG.componentesJG.btnFechaAdicionDesde.click(function(){
				ref.moduloJG.componentesJG.txtFechaAdicionDesdeJG.click();
				ref.moduloJG.componentesJG.txtFechaAdicionDesdeJG.focus();
			});
			
			ref.moduloJG.componentesJG.btnFechaAdicionHasta.click(function(){
				ref.moduloJG.componentesJG.txtFechaAdicionHastaJG.click();
				ref.moduloJG.componentesJG.txtFechaAdicionHastaJG.focus();
			});	
			
			ref.componentesJG.txtFechaAdicionDesdeJG = ref.moduloJG.componentesJG.txtFechaAdicionDesdeJG.datepicker({
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
			
			ref.componentesJG.txtFechaAdicionHastaJG = ref.moduloJG.componentesJG.txtFechaAdicionHastaJG.datepicker({
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
			
		},
		
		buscarReemplazoAdicion: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.usuarioSaliente=ref.moduloJG.componentesJG.cmbUsuarioSalienteFiltroAdicion.val();
			ref.filtroJG.usuarioSalienteTexto=  $("#cmbUsuarioSalienteFiltroAdicion option:selected").text();
			ref.filtroJG.usuarioEntrante=ref.moduloJG.componentesJG.cmbUsuarioEntranteFiltroAdicion.val();
			ref.filtroJG.usuarioEntranteTexto= $("#cmbUsuarioEntranteFiltroAdicion option:selected").text();
			ref.filtroJG.rol=ref.moduloJG.componentesJG.cmbRolFiltroAdicion.val();
			ref.filtroJG.rolTexto=$("#cmbRolFiltroAdicion option:selected").text();
			ref.filtroJG.dependencia=ref.moduloJG.componentesJG.cmbDependenciaFiltroAdicion.val();
			ref.filtroJG.dependenciaTexto=$("#cmbDependenciaFiltroAdicion option:selected").text();
			ref.filtroJG.fechaDesde = ref.moduloJG.componentesJG.txtFechaDocumentoDesde.val();
			ref.filtroJG.fechaHasta = ref.moduloJG.componentesJG.txtFechaDocumentoHasta.val();	
			if(!(ref.filtroJG.usuarioEntrante != null && ref.filtroJG.usuarioEntrante != undefined)){
				ref.filtroJG.usuarioEntrante = "";
			}
			if(!(ref.filtroJG.usuarioSaliente != null && ref.filtroJG.usuarioSaliente != undefined)){
				ref.filtroJG.usuarioSaliente = "";
			}
			ref.searchReemplazoAdicion();
			
			sessionStorage.setItem("FILTRO_REMPLAZO_ADICION", JSON.stringify(ref.filtroJG));
		},
		
		searchReemplazoAdicion: function(){
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
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_reemplazoAdicion.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_reemplazoAdicion.xlsx';
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
			        	"url": ref.moduloJG.URL_CONSULTA_REEMPLAZO_ADICION,
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
			        		//location.reload();
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
		
		inicializarTablaReemplazoEnAdicion: function(data){
			var ref = this;
			console.log("Inicializando TablaReemplazoEnAdicion")
			if(ref.dataTableRA){
				ref.dataTableRA.destroy();
				ref.moduloJG.componentesJG.dataTableRA.empty();
				ref.dataTableRA = null;
				ref.inicializarTablaReemplazoEnAdicion(data);
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
		},
		
		eventoDocumentoPrincipal: function(){
			var ref = this;			
			
			ref.moduloJG.btnModificarReemplazo.off('click').on('click',function(){
				var comp=$(this);
				var idReemplazo= comp.attr("data-id");
				
				for (var i in datosConsulta){
					var reemp=datosConsulta[i];
					if (reemp.id_reemplazo==idReemplazo){
						ref.moduloJG.objReemplazoAdicion.id_reemplazo=reemp.id_reemplazo;
						ref.moduloJG.objReemplazoAdicion.dependencia.append("<option value='" +reemp.codDependencia + "' selected='selected'>" +reemp.dependencia+ "</option>")
						ref.moduloJG.objReemplazoAdicion.dependencia.change();
						ref.moduloJG.objReemplazoAdicion.fechaReemplazoInicio.val(reemp.fechaInicio);
						ref.moduloJG.objReemplazoAdicion.fechaReemplazoInicio.change();
						ref.moduloJG.objReemplazoAdicion.fechaReemplazoFin.val(reemp.fechaTermino);
						ref.moduloJG.objReemplazoAdicion.fechaReemplazoFin.change();
						ref.moduloJG.objReemplazoAdicion.referencia.val(reemp.referencia);
						ref.moduloJG.objReemplazoAdicion.referencia.change();
						ref.moduloJG.objReemplazoAdicion.rol.append("<option value='" +reemp.rol + "' selected='selected'>" +reemp.rol+ "</option>")
						ref.moduloJG.objReemplazoAdicion.rol.change();
						ref.moduloJG.objReemplazoAdicion.usuarioSaliente.append("<option value='" +reemp.codUsuarioSaliente + "' selected='selected'>" +reemp.usuarioSaliente+ "</option>")
						ref.moduloJG.objReemplazoAdicion.usuarioSaliente.change();
						ref.moduloJG.objReemplazoAdicion.usuarioEntrante.append("<option value='" +reemp.codUsuarioEntrante + "' selected='selected'>" +reemp.usuarioEntrante+ "</option>")
						ref.moduloJG.objReemplazoAdicion.usuarioEntrante.change();
						ref.moduloJG.objReemplazoAdicion.accion="M";
						break;
					}
				}
				setTimeout(function(){					
					ref.moduloJG.comRegAdicion.myModal.modal('show');
				}, 300);
			}),
			
			
			ref.moduloJG.btnEliminarReemplazo.off('click').on('click', function(){
				var comp = $(this);
				var idReemplazo = comp.attr("data-id");
				ref.moduloJG.objReemplazoAdicion.id_reemplazo = idReemplazo;
				setTimeout(function(){
					ref.abrirModalElimAdicion();
					console.log("eventoDocumentoPrincipal - abrirá modal Eliminar");
				}, 500);
			}),
			
			ref.moduloJG.btnEliminarReemplazoAdicion.off('click').on('click', function(){
				ref.eliminarReemplazoAdicion();
			})
		}
};

setTimeout(function() {
	REEMPLAZOS_ADICION_VISTA.moduloJG = modulo_reemplazos_adicion;
	REEMPLAZOS_ADICION_VISTA.modSistcorr = modulo_sistcorr;
	REEMPLAZOS_ADICION_VISTA.jefe = ES_JEFE;
	REEMPLAZOS_ADICION_VISTA.listas = {};
	REEMPLAZOS_ADICION_VISTA.listas.dependencias = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.listas.originadores = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.inicializar();
}, 200);