var modulo_copia_consulta = MODULO_COPIA_CORRESPONDENCIA.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var COPIA_CORRESPONDENCIA_VISTA = {
		bandeja: [],
		correspondenciaSeleccionada: null,
		modSistcorr: null,
		modulo: null,
		modFirmaDigital: null,
		errores: [],
		funcionarios: [],
		usuarios: [],
		filtrosBusqueda: null,
		filtro_correlativo: null,
		idEliminar: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			//ref.modulo.compFiltrosBusqueda.html("ABCDEFGHIJ")
			ref.inicializarComponentes();
			ref.iniciarEventos();
			ref.modSistcorr.eventoTooltip();
			//ref.mostrarCorrespondencias();
		},
		
		inicializarComponentes: function(){
			console.log("Vista>>inicializarComponentes");
			var ref = this;
			
			ref.modulo.corrCopia.select2({
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
			ref.modulo.corrCopia.val('');
			/*var acciones = ref.modSistcorr.getAcciones();
			ref.modulo.corrEstado.empty();
			ref.modulo.corrEstado.append("<option disabled selected></option>");
			for(var index in acciones){
				var accion = acciones[index];
				ref.modulo.corrEstado.append("<option value='"+accion.codigoAccion+"'>" + accion.accion + "</option>");
			}*/
			//ref.modulo.corrEstado.append("<option value='"+"0"+"'>" + "[SELECCIONE]" + "</option>");
			//ref.modulo.corrEstado.append("<option value='"+"-1"+"'>" + "VALOR PRUEBA" + "</option>");
			ref.modulo.btnAgregarContacto.click(function(event){
				event.preventDefault();
				console.log("Contacto Añadido: " + ref.modulo.corrCopia.val());
				console.log("Añadir contacto");
				if(ref.modulo.corrCopia.val().trim() == ""){
					ref.modSistcorr.notificar("ERROR", "Seleccione una persona", "Warning");
				}else{
					ref.agregarCopiaContacto();
				}
			});
			
			ref.modulo.btnEnviarCopia.click(function(event){
				event.preventDefault();
				ref.enviarCopiaCorreo();
			});
			
			ref.modulo.btnEnviarCopiaCab.click(function(event){
				event.preventDefault();
				ref.enviarCopiaCorreo();
			});
			
			ref.modulo.btnEliminarContacto.click(function(event){
				event.preventDefault();
				var ref2 = $(this);
				var val = ref2.attr('id');
				ref.eliminarCopiaCorreo(val);
				/*var ref2 = $(this);
				console.log("Eliminar Contacto");
				console.log(val);
				ref.eliminarCopiaContacto(val);*/
			});
			
			ref.modulo.btnRetroceder.click(function(){
				ref.modSistcorr.procesar(true);
				var urlBack = sessionStorage.getItem("urlBack");
				console.log("URL_BACK:" + urlBack);
				if(urlBack == null || urlBack == "null"){
					window.location.replace("../../../../app/" + ref.bandeja);
				}else{
					sessionStorage.removeItem("urlBack");
					window.location.replace(urlBack);
				}
				//window.location.replace("../../../../app/" + ref.bandeja);
				
				//ref.modSistcorr.retroceder();
			});
			
			ref.modulo.btnEnviarCopiaSi.click(function(e){
				e.preventDefault();
				ref.modSistcorr.procesar(true);
				ref.modulo.enviarCopiaCorrespondencia()
					.then(function(respuesta){
						console.log("Respuesta:");
						console.log(respuesta);
						ref.modSistcorr.procesar(false);
						
						if(respuesta.estado == true){
							ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
							$("#modalEnviarCopiaCorrespondencia").modal('hide');
							setTimeout(function(){
								this.btnRetroceder.click();
							}, 2500)
						} else{
							ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
							$("#modalEnviarCopiaCorrespondencia").modal('hide');
						}
					}).catch(function(error){
						console.log("ERROR:");
						console.log(error);
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
			});
			
			ref.modulo.btnEliminarCopiaSi.click(function(e){
				e.preventDefault();
				ref.eliminarCopiaContacto(ref.idEliminar);
				ref.idEliminar = null;
			});
			
			$(".removeContacto").each(function(){
				var t = $(this);
				console.log(t);
				var val = t.attr("id");
				$("#corrCopia option[value='" + val + "']").remove();
				$("#corrCopia").select2();
			})
		},
		
		iniciarEventos: function(){
			var ref = this;
		},
		
		abrirModalBusqueda: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			$(".panel-2").css('display', 'none');
			ref.modulo.compBusqueda.myModal.modal('show');
			setTimeout(function() {
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 1500);
						
		},
		
		agregarCopiaContacto: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.agregarContacto()
				.then(function(respuesta){
					console.log("Respuesta:");
					console.log(respuesta);
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.usuarios = respuesta.datos;
					ref.mostrarCopiaCorrespondencia();
					var idContacto = $("#corrCopia").val();
					$("#corrCopia option[value='" + idContacto + "']").remove();
					$("#corrCopia").select2();
					//location.reload();
					/*if(respuesta.estado == true){
						ref.asignaciones = respuesta.datos || [];
						if(ref.asignaciones.length > 0){
							
						}
					} else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
					
					ref.mostrarAsignacionesTemporales();*/
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		mostrarCopiaCorrespondencia: function(){
			var ref = this;
			var htmlCorrespondencias = ref.modulo.htmlCopiaCorrespondencias(ref.usuarios);
			console.log(htmlCorrespondencias);
			if(ref.usuarios.length == 0){
				ref.modulo.compSinResultados.show();
			} else {
				ref.modulo.compSinResultados.hide();
			}
			ref.modulo.vistaCopiaCorrespondencia.html("<input type='hidden' id='cantDestinatarios' value='" + ref.usuarios.length + "' />" + htmlCorrespondencias);
			ref.modulo.btnEliminarContacto = $(".removeContacto");
			
			ref.modulo.btnEliminarContacto.click(function(event){
				event.preventDefault();
				var ref2 = $(this);
				var val = ref2.attr('id');
				ref.eliminarCopiaCorreo(val);
				/*var ref2 = $(this);
				var val = ref2.attr('id');
				console.log("Eliminar Contacto");
				console.log(val);
				ref.eliminarCopiaContacto(val);*/
			});
			
			ref.modSistcorr.eventoTooltip();
		},
		
		eliminarCopiaContacto: function(usuario){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.eliminarContacto(usuario)
				.then(function(respuesta){
					console.log("Respuesta:");
					console.log(respuesta);
					ref.modSistcorr.procesar(false);
					var div = $("#div_" + usuario);
					console.log("Usuario: " + usuario);
					console.log(div);
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					var cant = $("#cantDestinatarios").val();
					cant = cant - 1;
					$("#cantDestinatarios").val(cant);
					
					var nombreUsuario = div.find(".nombre_usuario").html();
					console.log("Nombre Usuario eliminado:" + nombreUsuario);
					var o = new Option(nombreUsuario, usuario);
					/// jquerify the DOM object 'o' so we can use the html method
					$(o).html(nombreUsuario);
					$("#corrCopia").append(o);
					$("#corrCopia").select2();
					
					div.remove();
					//location.reload();
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error)
				});
			$("#modalEliminarCopiaCorrespondencia").modal('hide');
		},
		
		enviarCopiaCorreo: function(){
			var ref = this;
			//ref.modSistcorr.procesar(true);//9000004276
			/*if(ref.modulo.corrTexto.val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "Ingrese el texto que se enviará en el correo", "Warning");
				ref.modSistcorr.procesar(false);
			}else*/ if($("#cantDestinatarios").val() == "0"){
				ref.modSistcorr.notificar("ERROR", "Debe ingresar al menos un destinatario", "Warning");
				//ref.modSistcorr.procesar(false);//9000004276
			}else{
				$("#modalEnviarCopiaCorrespondencia").modal('show');
				//ref.modSistcorr.procesar(false);//9000004276
			}
		},
		
		eliminarCopiaCorreo: function(val){
			var ref = this;
			//ref.modSistcorr.procesar(true);//9000004276
			ref.idEliminar = val;
			$("#modalEliminarCopiaCorrespondencia").modal('show');
			//ref.modSistcorr.procesar(false);//9000004276
		},
		
};

setTimeout(function(){
	COPIA_CORRESPONDENCIA_VISTA.modSistcorr = modulo_sistcorr;
	COPIA_CORRESPONDENCIA_VISTA.modulo = modulo_copia_consulta;
	COPIA_CORRESPONDENCIA_VISTA.bandeja = BANDEJA;
	COPIA_CORRESPONDENCIA_VISTA.funcionarios = FUNCIONARIOS;
	COPIA_CORRESPONDENCIA_VISTA.inicializar();
}, 500);