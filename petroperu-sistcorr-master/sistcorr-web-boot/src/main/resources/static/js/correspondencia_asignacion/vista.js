var modulo_correspondencia_asignacion = MODULO_CORRESPONDENCIA_ASIGNACION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CORRESPONDENCIA_ASIGNACION_VISTA = {
	modulo: null,
	modSistcorr: null,
	correspondencia: null,
	asignaciones : [],
	asignacionSeleccionada: null,
	errores: [],
	sizeScreen: 500,
	funcionarios: [],
	inicializar: function(){
		var ref = this;
		ref.obtenerTemporalAsignaciones();
		ref.modSistcorr.cargarVista();
		ref.iniciarEventos();
		ref.mostrarErrores();
	},
	
	iniciarEventos: function(){
		var ref = this;
		
		if(screen.width > ref.sizeScreen){
			ref.modulo.VISTA.opciones.opcAsignar.css('width', '80px');
			ref.modulo.VISTA.opciones.opcAsignarTodos.css('width', '80px');
		} else{
			ref.modulo.VISTA.opciones.opcAsignar.css('width', '50px');
			ref.modulo.VISTA.opciones.opcAsignarTodos.css('width', '50px');
		}
		
		ref.modulo.VISTA.btnRetroceder.click(function(){
			ref.modSistcorr.procesar(true);
			ref.modSistcorr.retroceder();
		});
		ref.actualizarCombosTipoAccion();
		ref.actualizarListaFuncionarios();
		ref.modulo.VISTA.form.compFechaTope.datepicker({
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
		
		ref.modulo.VISTA.form.compBtnFechaTope.click(function(){
			ref.modulo.VISTA.form.compFechaTope.focus();
		});
		
	
		
		ref.modulo.VISTA.form.compAccion.change(function(event){
			var $comp = $(event.currentTarget);
        	ref.modSistcorr.select_change($comp.attr('id'));
		}).focus(function(event){
			var $comp = $(event.currentTarget);
        	ref.modSistcorr.inputText_focus($comp.attr('id'));
		}).on('focusout',function(event){
			var $comp = $(event.currentTarget);
			ref.modSistcorr.inputText_focusOut($comp.attr('id'));
		});
		
		
		ref.modulo.VISTA.form.compDetalleReq.change(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.textArea_change($comp.attr('id'));
			}).focus(function(event){
				var $comp = $(event.currentTarget);
	        	ref.modSistcorr.textArea_focus($comp.attr('id'));
			}).on('focusout',function(event){
				var $comp = $(event.currentTarget);
				ref.modSistcorr.textArea_focusOut($comp.attr('id'));
			});
		
		setTimeout(function() {
			ref.modSistcorr.eventoTooltip();
			//ref.modSistcorr.eventosS2();	
			//ref.modSistcorr.eventoSelect();
		}, 500);
		
		/*ref.modulo.VISTA.form.compFechaTope.on('focus', function(){
			var label = $("label[for='" + $(this).attr('id') + "']");
			label.addClass('active');
			label.css('color', '#4285f4');
		});
		
		ref.modulo.VISTA.form.compFechaTope.on('change', function(){
			var label = $("label[for='" + $(this).attr('id') + "']");
			label.addClass('active');
			label.css('color', '');
			$(this).addClass('select_seleccionado');
		});
		
		ref.modulo.VISTA.form.compFechaTope.on('focusout', function(){
			var label = $("label[for='" + $(this).attr('id') + "']");
			if($(this).val() == ""){
				label.removeClass('active');
			}else{
				label.addClass('active');
			}
			label.css('color', '');
			$(this).addClass('select_seleccionado');
		});
		
		ref.modulo.VISTA.form.compDetalleReq.on('focusout', function(){
			var label = $("label[for='" + $(this).attr('id') + "']");
			label.addClass('active');
			label.css('color', '#757575');
		});*/
		
		ref.modulo.VISTA.opciones.btnAsignar.click(function(event){
			event.stopPropagation();
			ref.asignar();
		});
		
		ref.modulo.VISTA.opciones.opcAsignar.click(function(event){
			ref.modulo.VISTA.opciones.btnAsignar.click();
		});
		
		ref.modulo.VISTA.opciones.btnAsignarTodos.click(function(event){
			event.stopPropagation();
			ref.asignarTodos();
		});
		
		ref.modulo.VISTA.opciones.opcAsignarTodos.click(function(event){
			ref.modulo.VISTA.opciones.btnAsignarTodos.click();
		});
		
		
		ref.modulo.VISTA.opciones.btnEnviar.click(function(event){
			ref.enviarAsignaciones();
		});
		
		ref.modulo.VISTA.opciones.btnEnviarAsignaciones.click(function(event){
			ref.enviarAsignaciones();
		});
		
		ref.modulo.VISTA.opciones.btnEnviarAsigDatos.click(function(event){
			ref.enviarAsignaciones();
		});
		
		//inicio ticket 9000004807
		ref.modulo.VISTA.opciones.btnEnviarAD.click(function(event){
			ref.enviarAsignaciones();
		});
		//fin ticket 9000004807
		
		ref.modulo.compBtnBorrarAsignacion.click(function(event){
			ref.confirmarBorrarAsignacion();
		});
		
	},
	
	actualizarCombosTipoAccion: function(){
		var ref = this;
		var acciones = ref.modSistcorr.getAcciones();
		
		ref.modulo.VISTA.form.compAccion.empty();
		ref.modulo.VISTA.form.compAccion.append("<option disabled selected></option>");
		for(var index in acciones){
			var accion = acciones[index];
			ref.modulo.VISTA.form.compAccion.append("<option value='"+accion.codigoAccion+"'>" + accion.accion + "</option>");
		}		
	},
	
	actualizarListaFuncionarios: function(){
		var ref =  this;
		ref.modSistcorr.procesar(true);
		ref.modulo.recuperarFuncionarios()
		.then(function(respuesta){
			if(respuesta.estado == true){
				var funcionarios = respuesta.datos || [];
				for(var index in funcionarios){
					var func = funcionarios[index];
					func.id = func.nombreUsuario;
					ref.funcionarios.push(func);
				}
				ref.actualizarComboFuncionarios();
				ref.modSistcorr.procesar(false);
			}else{
				ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				ref.modSistcorr.procesar(false);
			}
		}).catch(function(error){
			ref.modSistcorr.procesar(false);
			ref.modSistcorr.showMessageErrorRequest(error);
		});
	},
	
	actualizarComboFuncionarios: function(){
		var ref = this;
		ref.modulo.VISTA.form.compAquienSeDirige.select2({
			data: ref.funcionarios
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
		ref.modulo.VISTA.form.compAquienSeDirige.val('');
		//ref.modulo.VISTA.form.compAquienSeDirige.change();
	},
	
	obtenerTemporalAsignaciones: function(){
		var ref = this;
		ref.modSistcorr.procesar(true);
		ref.modulo.recuperarTemporalAsignaciones()
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.asignaciones = respuesta.datos || [];
					if(ref.asignaciones.length > 0){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					}
				} else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				ref.modSistcorr.procesar(false);
				ref.mostrarAsignacionesTemporales();
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	},
	
	mostrarAsignacionesTemporales: function(){
		var ref = this;
		ref.modulo.VISTA.compAsignaciones.empty();
		var contenidoHTML = ref.modulo.htmlTemporalAsignaciones(ref.asignaciones);
		ref.modulo.VISTA.compAsignaciones.html(contenidoHTML);
		ref.eventosListaAsignaciones();
		if(ref.asignaciones.length == 0){
			ref.modulo.VISTA.compSinAsignaciones.show();
			ref.modulo.VISTA.compOpcionesListaAsignaciones.hide();
		} else{
			ref.modulo.VISTA.compSinAsignaciones.hide();
			ref.modulo.VISTA.compOpcionesListaAsignaciones.show();
		}
	},
	
	eventosListaAsignaciones: function(){
		var ref = this;
		ref.utilitario_textMas.inicializar();
		ref.modSistcorr.eventoTooltip();
		$('.' + ref.modulo.compEliminarAsignacion).click(function(event){
			ref.modSistcorr.procesar(true);
			var $element = $(event.currentTarget);
			var seleccionado = $element.data("asignacion");
			for(var index in ref.asignaciones){
				var asig = ref.asignaciones[index];
				if(asig.idAsignacion == seleccionado){
					ref.asignacionSeleccionada = asig;
					ref.modulo.compModalBorrarAsignacion.modal('show');
					ref.modSistcorr.procesar(false);
					break;
				}
			}
			
		});
	},
	
	confirmarBorrarAsignacion: function(){
		var ref = this;
		ref.modSistcorr.procesar(true);
		if(ref.asignacionSeleccionada){
			ref.modulo.eliminarAsignacion(ref.asignacionSeleccionada)
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						$('#' + ref.asignacionSeleccionada.idAsignacion).remove();
						ref.asignacionSeleccionada = null;
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					ref.modulo.compModalBorrarAsignacion.modal('hide');
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		} else{
			ref.modSistcorr.procesar(false);
		}
	},
	
	limpiarFormulario: function(){
		var ref = this;
		ref.modulo.VISTA.form.compAccion.val('');
		ref.modulo.VISTA.form.compAccion.change();
		
		//ref.actualizarListaFuncionarios();
		ref.modulo.VISTA.form.compAquienSeDirige.val('');
		ref.modulo.VISTA.form.compAquienSeDirige.change();
		ref.modulo.VISTA.form.compDetalleReq.val('');
		ref.modulo.VISTA.form.compDetalleReq.change()
		ref.modulo.VISTA.form.compFechaTope.val('');
		ref.modulo.VISTA.form.compFechaTope.change();
		setTimeout(function() {
			ref.modulo.VISTA.form.compFechaTope.removeClass('valid');
			ref.modulo.VISTA.form.compDetalleReq.removeClass('valid');
			ref.modulo.VISTA.form.compAccion.removeClass('valid');
			ref.modulo.VISTA.form.compAccion.removeClass('select_seleccionado ');
		}, 500);
	},
	
	asignar: function(){
		var ref = this;
		ref.modSistcorr.cerrarNofificaciones();
		var accion = ref.modulo.VISTA.form.compAccion.val() || '';
		var funcionarioResponsable = ref.modulo.VISTA.form.compAquienSeDirige.val() || '';
		var detalleReq = ref.modulo.VISTA.form.compDetalleReq.val() || '';
		var fechaTope = ref.modulo.VISTA.form.compFechaTope.val() || '';
		
		var requiereTexto = false;;
		var acciones = ref.modSistcorr.getAcciones()
		for(var index in acciones){
			if(acciones[index].codigoAccion == accion){
				if(acciones[index].requiereRespuesta == 'SI'){
					requiereTexto = true;
					break;
				}
			}
		}
		
		if(accion == ''){
			ref.modulo.VISTA.form.compAccion.focus();
			ref.modulo.VISTA.form.compAccion.select();
			ref.modSistcorr.notificar("ERROR", 'Seleccione una acción', "Warning");
			return;
		}
		if(funcionarioResponsable == ''){
			ref.modSistcorr.notificar("ERROR", 'Seleccione funcionario', "Warning");
			ref.modulo.VISTA.form.compAquienSeDirige.focus().select();
			return;
		}
		
		if(detalleReq == '' && requiereTexto == true){
			ref.modulo.VISTA.form.compDetalleReq.focus();
			ref.modSistcorr.notificar("ERROR", 'Ingrese detalle del requerimiento', "Warning");
			return;
		}
		
		 
		
		ref.modSistcorr.procesar(true);
		ref.modulo.agregarAsignacion(accion, funcionarioResponsable, detalleReq, fechaTope, ref.correspondencia.idPadre)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.asignaciones = respuesta.datos || [];
					ref.modSistcorr.procesar(false);
					ref.mostrarAsignacionesTemporales();
					ref.limpiarFormulario();
				} else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	},
	
	//INI TICKET 8000004807
	asignacionTemporalDesdeEnviar: function(){
		var ref = this;
		var accion = ref.modulo.VISTA.form.compAccion.val() || '';
		var funcionarioResponsable = ref.modulo.VISTA.form.compAquienSeDirige.val() || '';
		var detalleReq = ref.modulo.VISTA.form.compDetalleReq.val() || '';
		var fechaTope = ref.modulo.VISTA.form.compFechaTope.val() || '';
		
		ref.modSistcorr.procesar(true);
		ref.modulo.agregarAsignacion(accion, funcionarioResponsable, detalleReq, fechaTope, ref.correspondencia.idPadre)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.asignaciones = respuesta.datos || [];
					ref.enviarAsignacionesSinValidarDatosAsignacion();
				} else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	},
	
	validarDatosAsignacionDesdeEnviar: function(){
		var ref = this;
		ref.modSistcorr.cerrarNofificaciones();
		
		var accion = ref.modulo.VISTA.form.compAccion.val() || '';
		var funcionarioResponsable = ref.modulo.VISTA.form.compAquienSeDirige.val() || '';
		var detalleReq = ref.modulo.VISTA.form.compDetalleReq.val() || '';
		var fechaTope = ref.modulo.VISTA.form.compFechaTope.val() || '';
		
		var requiereTexto = false;;
		var acciones = ref.modSistcorr.getAcciones()
		for(var index in acciones){
			if(acciones[index].codigoAccion == accion){
				if(acciones[index].requiereRespuesta == 'SI'){
					requiereTexto = true;
					break;
				}
			}
		}
		
		if(accion == '')
			return false;
		if(funcionarioResponsable == '')
			return false;
		
		if(detalleReq == '' && requiereTexto == true)
			return false;
		
		return true;
	},
	//FIN TICKET 8000004807
	
	asignarTodos: function(){
		var ref = this;
		ref.modSistcorr.cerrarNofificaciones();
		var accion = ref.modulo.VISTA.form.compAccion.val() || '';
		var funcionarioResponsable = ref.modulo.VISTA.form.compAquienSeDirige.val() || '';
		var detalleReq = ref.modulo.VISTA.form.compDetalleReq.val() || '';
		var fechaTope = ref.modulo.VISTA.form.compFechaTope.val() || '';
		
		if(accion == ''){
			ref.modulo.VISTA.form.compAccion.focus();
			ref.modulo.VISTA.form.compAccion.select();
			ref.modSistcorr.notificar("ERROR", 'Seleccione una acción', "Warning");
			return;
		}
		
		var requiereTexto = false;;
		var acciones = ref.modSistcorr.getAcciones()
		for(var index in acciones){
			if(acciones[index].codigoAccion == accion){
				if(acciones[index].requiereRespuesta == 'SI'){
					requiereTexto = true;
					break;
				}
			}
		}
		
		
		if(detalleReq == ''  && requiereTexto == true){
			ref.modulo.VISTA.form.compDetalleReq.focus();
			ref.modSistcorr.notificar("ERROR", 'Ingrese detalle del requerimiento', "Warning");
			return;
		}
		
		ref.modSistcorr.procesar(true);
		ref.modulo.agregarAsignacionMasivo(accion, ref.correspondencia.codigoDependencia, detalleReq, fechaTope, ref.correspondencia.idPadre)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.asignaciones = respuesta.datos || [];
					ref.modSistcorr.procesar(false);
					ref.mostrarAsignacionesTemporales();
					ref.limpiarFormulario();
				} else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	},
	
	
	enviarAsignaciones: function(){
		var ref = this;
		//INI TICKET 8000004807
		if(ref.validarDatosAsignacionDesdeEnviar() && ref.asignaciones.length == 0){
			ref.asignacionTemporalDesdeEnviar();
		}else{//FIN TICKET 8000004807
			ref.modSistcorr.cerrarNofificaciones();
			ref.modSistcorr.procesar(true);
			ref.modulo.enviarAsignaciones()
				.then(function(respuesta){
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						setTimeout(function(){
							ref.modSistcorr.procesar(false);
							ref.regresaraBandeja();
						}, 2500);
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		}
	},
	
	//INI TICKET 8000004807
	enviarAsignacionesSinValidarDatosAsignacion: function(){
		var ref = this;
		ref.modSistcorr.procesar(true);
		ref.modulo.enviarAsignaciones()
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					setTimeout(function(){
						ref.modSistcorr.procesar(false);
						ref.regresaraBandeja();
					}, 2500);
				} else{
					ref.mostrarAsignacionesTemporales();
					ref.limpiarFormulario();
					
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
	},
	//FIN TICKET 8000004807
	
	regresaraBandeja: function(){
		var ref = this;
		window.location.replace("../../../app/" + ref.modulo.tipoBandeja.val());
	},
	
	mostrarErrores: function(){
		var ref = this;
		for(var i in ref.errores){
			var err = ref.errores[i];
			if(err){
				ref.modSistcorr.notificar("ERROR", ref.errores[i], "Error");	
			}
		}
	},
};

setTimeout(function() {
	CORRESPONDENCIA_ASIGNACION_VISTA.modulo = modulo_correspondencia_asignacion;
	CORRESPONDENCIA_ASIGNACION_VISTA.modSistcorr = modulo_sistcorr;
	CORRESPONDENCIA_ASIGNACION_VISTA.correspondencia = CORRESPONDENCIA;
	CORRESPONDENCIA_ASIGNACION_VISTA.errores = ERRORES || [];
	CORRESPONDENCIA_ASIGNACION_VISTA.utilitario_textMas = utilitario_textLarge;
	CORRESPONDENCIA_ASIGNACION_VISTA.inicializar();
}, 200);