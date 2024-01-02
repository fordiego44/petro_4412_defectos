var modulo_dependencia_unidad = MODULO_DEPENDENCIA_UNIDAD.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var DEPENDENCIA_UNIDAD_VISTA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		masFiltros: false,
		dataTable: null,
		listaIntegrantes: [],
		jefe: false,
		jefeActual: null,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.iniciarEventos();
			ref.inicializarComponentes();
		},
		dependencia: null,
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.btnBuscarIntegrante.click(function(event){
				ref.modulo.formularioIntegrante.registroIntegrante.val('');
				ref.modulo.formularioIntegrante.registroIntegrante.change();
				ref.modulo.modalAgregarIntegrante.modal('show');
			});
			
			ref.modulo.formulario.tipo.select2();
			ref.modulo.formulario.tipoUnidadMatricial.select2();
			ref.modulo.formulario.estado.select2();
			ref.modulo.formulario.jerarquia.select2();
			ref.modulo.formulario.jefe.select2();
			ref.modulo.formulario.lugarTrabajo.select2();
			ref.modulo.formulario.dependenciaSuperior.select2();
			ref.modulo.formularioIntegrante.registroIntegrante.select2();
			
			ref.modulo.formularioIntegrante.btnGuardarIntegrante.click(function(event){
				let codigoIntegrante = ref.modulo.formularioIntegrante.registroIntegrante.val();
				let nombreIntegrante = $("#integrante option:selected").text();
				let nuevoIntegrante = {'codigoIntegrante': codigoIntegrante, 'nombreIntegrante': nombreIntegrante};
				ref.modulo.modalAgregarIntegrante.modal('hide');
				ref.agregarIntegrante(nuevoIntegrante);
				/*ref.listaIntegrantes.push(nuevoIntegrante);
				ref.inicializarTabla(ref.listaIntegrantes);*/
			});
			
			ref.modSistcorr.accordionMenu.click(function(event){
				var t = $(this);
				var id = t.attr('data-id');
				console.log("Id ocultar:" + id);
				ocultarAcordeon(id);
			});
			
			//inicio ticket 9000004410
			ref.modulo.formulario.sinJefe.change(function(){
				var obj = $(this);
				//var variale = $("input[name=name_icb]:checked").val();
				if(obj.prop('checked')){
					ref.modulo.formulario.jefe.prop('disabled', true);
				}else{
					ref.modulo.formulario.jefe.removeAttr('disabled');
				}
				ref.modulo.formulario.jefe.val("0");
				ref.modulo.formulario.jefe.change();
			});
			//fin ticket 9000004410
			
			ref.modulo.btnGuardarDependencia.click(function(event){
				if(ref.validarFormulario()){
					console.log("Formulario validado.")
					ref.modSistcorr.procesar(true);
					
					//ref.modulo.verificarSiglas(ref.dependencia.idDependenciaUnidadMatricial, ref.dependencia.siglas)
					ref.modulo.verificarDatos(ref.dependencia.idDependenciaUnidadMatricial, ref.dependencia.codigo, ref.dependencia.siglas)
							.then(function(respuesta){
								if(respuesta.estado==true){
									ref.modulo.guardarDependencia(ref.dependencia)
											.then(function(respuesta){
												ref.modSistcorr.procesar(false);
												if(respuesta.estado == true){
													ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
													//ref.modulo.btnCancelarDependencia.click();
													ref.regresarLista();
												}else{
													ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
												}
											}).catch(function(error){
												ref.modSistcorr.procesar(false);
												ref.modSistcorr.showMessageErrorRequest(error);
											});
								}else{
									if(respuesta.mensaje == "SIG"){
										ref.modSistcorr.procesar(false);
										ref.modulo.modalConfirmarRegistro.modal('show');
									}else if(respuesta.mensaje == "COD"){
										ref.modSistcorr.procesar(false);
										ref.modSistcorr.notificar("ERROR", "El código ingresado ya existe. Ingrese un nuevo código.", "Error");
									}else {
										ref.modSistcorr.procesar(false);
										ref.modSistcorr.notificar("ERROR", "El código ingresado ya existe. Ingrese un nuevo código.", "Error");
									}
								}
							}).catch(function(error){
								ref.modSistcorr.procesar(false);
								ref.modSistcorr.showMessageErrorRequest(error);
							});
				}else{
					console.log("Errores en la validación del formulario.");
				}
			});
			
			ref.modulo.btnConfirmarRegistro.click(function(event){
				ref.modulo.guardarDependencia(ref.dependencia)
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.regresarLista();
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
			});
			
			ref.modulo.btnCancelarDependencia.click(function(event){
				if(ref.modulo.formulario.idDependenciaUnidadMatricial.val() == "0"){
					window.location.replace("../" + ref.modulo.URL_LISTAR_DEPENDENCIAS);
				}else{
					window.location.replace("../../../" + ref.modulo.URL_LISTAR_DEPENDENCIAS);
				}
			});
			
			ref.modulo.formulario.tipo.change(function(event){
				var t = this;
				var valor = $(t).val();
				if(valor == "D"){
					ref.modulo.formulario.tipoUnidadMatricial.val("");
					ref.modulo.formulario.tipoUnidadMatricial.change();
					ref.modulo.formulario.tipoUnidadMatricial.prop('disabled', true);
					ref.modulo.btnBuscarIntegrante.prop('disabled', true);
					ref.modulo.formulario.sinJefe.removeAttr('disabled');//ticket 9000004410
					ref.modulo.formulario.sinJefe.prop('checked', false);//ticket 9000004410
					ref.modulo.formulario.sinJefe.change();//ticket 9000004410
					ref.listaIntegrantes = [];
					ref.inicializarTabla(ref.listaIntegrantes);
					ref.modulo.btnBuscarIntegrante.removeClass('btn-petroperu-verde');
					ref.jefeActual = null;
				}else{
					ref.modulo.formulario.tipoUnidadMatricial.removeAttr('disabled');
					ref.modulo.formulario.sinJefe.prop('disabled', true);//ticket 9000004410
					ref.modulo.formulario.sinJefe.prop('checked', false);//ticket 9000004410
					ref.modulo.formulario.sinJefe.change();//ticket 9000004410
					ref.modulo.btnBuscarIntegrante.removeAttr('disabled', true);
					ref.modulo.btnBuscarIntegrante.addClass('btn-petroperu-verde');
					ref.jefeActual = ref.modulo.formulario.jefe.val();
				}
			});
			
			ref.modulo.formulario.jefe.change(function(event){
				var t = this;
				// JEFE NO SE INCLUYE EN LA LISTA DE INTEGRANTES
				var tipo = ref.modulo.formulario.tipo.val();
				if(tipo=="UM"){
					var valor = $(t).val();
					ref.jefeActual = valor;
					var codigo = ref.jefeActual;
					var nombre = $("#cbmJefe option:selected").text();
					for(var i=0;i<ref.listaIntegrantes.length;i++){
						var integr = ref.listaIntegrantes[i];
						console.log("Comparando:" + codigo + "-" + integr.codigoIntegrante);
						if(codigo == integr.codigoIntegrante){
							ref.listaIntegrantes.splice(i, 1);
						}
					}
					ref.inicializarTabla(ref.listaIntegrantes);
				}
				// FIN JEFE
				/*console.log("Cambio jefe");
				var tipo = ref.modulo.formulario.tipo.val();
				console.log("Tipo:" + tipo)
				if(tipo=="UM"){
					var valor = $(t).val();
					var nombre = $("#cbmJefe option:selected").text();
					let nuevoIntegrante = {'codigoIntegrante': valor, 'nombreIntegrante': nombre};
					
					var codigo = ref.jefeActual;
					for(var i=0;i<ref.listaIntegrantes.length;i++){
						var integr = ref.listaIntegrantes[i];
						if(codigo == integr.codigoIntegrante){
							ref.listaIntegrantes.splice(i, 1);
						}
					}
					ref.jefeActual = valor;
					
					ref.agregarIntegrante(nuevoIntegrante);*/
					/*ref.listaIntegrantes.push(nuevoIntegrante);
					ref.inicializarTabla(ref.listaIntegrantes);*/
				/*}*/
			});
		},
		
		regresarLista: function(){
			var ref = this;
			if(ref.dependencia.idDependenciaUnidadMatricial == "0"){
				window.location.replace("../" + ref.modulo.URL_LISTAR_DEPENDENCIAS);
			}else{
				window.location.replace("../../../" + ref.modulo.URL_LISTAR_DEPENDENCIAS);
			}
		},
		
		agregarIntegrante: function(nuevoIntegrante){
			var ref = this;
			var valido = true;
			for(var i=0;i<ref.listaIntegrantes.length;i++){
				console.log()
				if(nuevoIntegrante.codigoIntegrante == ref.listaIntegrantes[i].codigoIntegrante){
					valido = false;
				}
			}
			// JEFE NO SE INCLUYE EN LA LISTA DE INTEGRANTES
			if(ref.jefeActual == nuevoIntegrante.codigoIntegrante){
				valido = false;
			}
			// FIN JEFE
			if(valido){
				console.log("Inicializando Tabla con nuevo integrante");
				ref.listaIntegrantes.push(nuevoIntegrante);
			}

			ref.inicializarTabla(ref.listaIntegrantes);
		},
		
		validarFormulario: function(){
			var ref = this;
			let tipo = ref.modulo.formulario.tipo.val();
			if(tipo == "0"){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar un Tipo', "Error");
				return false;
			}
			var dep = {
				'tipo': tipo
			}
			let tipoUnidadMatricial = ref.modulo.formulario.tipoUnidadMatricial.val();
			if(tipo == "UM"){
				if(tipoUnidadMatricial == "0" || tipoUnidadMatricial == "null" || tipoUnidadMatricial == null){
					ref.modSistcorr.notificar("ERROR", 'Debe seleccionar un Tipo de Unidad Matricial', "Error");
					return false;
				}
				dep['tipoUnidadMatricial'] = tipoUnidadMatricial;
			}
			let codigo = ref.modulo.formulario.codigo.val();
			if(codigo.trim() == ""){
				ref.modSistcorr.notificar("ERROR", 'Debe ingresar un Código', "Error");
				return false;
			}
			if(isNaN(codigo)){
				ref.modSistcorr.notificar("ERROR", 'El código sólo debe contener dígitos', "Error");
				return false;
			}
			dep['codigo'] = codigo;
			let siglas = ref.modulo.formulario.siglas.val();
			if(siglas.trim() == ""){
				ref.modSistcorr.notificar("ERROR", 'Debe ingresar Siglas', "Error");
				return false;
			}
			dep['siglas'] = siglas;
			let nombre = ref.modulo.formulario.nombre.val();
			if(nombre.trim() == ""){
				ref.modSistcorr.notificar("ERROR", 'Debe ingresar un Nombre', "Error");
				return false;
			}
			dep['nombre'] = nombre;
			let estado = ref.modulo.formulario.estado.val();
			dep['estado'] = estado;
			let jerarquia = ref.modulo.formulario.jerarquia.val();
			if(jerarquia == "0"){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una Jerarquía', "Error");
				return false;
			}
			dep['jerarquia'] = jerarquia;
			let jefe = ref.modulo.formulario.jefe.val();
			let fSinJefe = ref.modulo.formulario.sinJefe.prop("checked");//TICKET 90000004410
			if(((tipo == "D" && !fSinJefe) || tipo == "UM") && jefe == "0"){//TICKET 90000004410 ADD VAL ((tipo == "D" && !fSinJefe) || tipo == "UM")
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar un Jefe', "Error");
				return false;
			}
			dep['jefe'] = jefe;
			let lugarTrabajo = ref.modulo.formulario.lugarTrabajo.val();
			if(lugarTrabajo == "0"){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar un Lugar de Trabajo', "Error");
				return false;
			}
			dep['lugarTrabajo'] = lugarTrabajo;
			let dependenciaSuperior = ref.modulo.formulario.dependenciaSuperior.val();
			if(dependenciaSuperior == "0" && tipo == "D"){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una Dependencia Superior', "Error");
				return false;
			}
			dep['dependenciaSuperior'] = dependenciaSuperior;
			let descripcionCargo = ref.modulo.formulario.descripcionCargo.val();
			if(descripcionCargo.trim() == ""){
				ref.modSistcorr.notificar("ERROR", 'Debe ingresar una descripción del cargo', "Error");
				return false;
			}
			dep['descripcionCargo'] = descripcionCargo;
			dep['integrantes'] = ref.listaIntegrantes;
			dep['idDependenciaUnidadMatricial'] = ref.modulo.formulario.idDependenciaUnidadMatricial.val();
			ref.dependencia = dep;
			return true;
		},
		
		inicializarComponentes: function(){
			var ref = this;
			console.log("ID:" + ref.modulo.formulario.idDependenciaUnidadMatricial.val());
			if(ref.modulo.formulario.idDependenciaUnidadMatricial.val() != "0"){
				ref.obtenerDependencia();
			}else{
				ref.validarInicioFormRegistro();
				ref.jefeActual = 00;
			}
		},
		
		validarInicioFormRegistro: function(){
			var ref = this;
			ref.modulo.formulario.estado.val("SI");
			ref.modulo.formulario.estado.change();
			ref.modulo.formulario.estado.prop('disabled', true);
		},
		
		validarInicioFormEdicion: function(){
			var ref = this;
			ref.modulo.formulario.tipo.prop('disabled', true);
		},
		
		obtenerDependencia: function(){
			var ref = this;
			console.log("Obteniendo dependencia");
			var codigo = ref.modulo.formulario.codigoDependenciaInicial.val();
			ref.modulo.obtenerDependencia(codigo)
					.then(function(respuesta){
						console.log("Dependencia obtenida:");
						console.log(respuesta);
						if(respuesta.estado == true){
							if(respuesta.datos != null && respuesta.datos.length > 0){
								ref.dependencia = respuesta.datos[0];
								ref.cargarDatosDependencia();
								ref.validarInicioFormEdicion();
								if(ref.dependencia.tipo != "D"){
									ref.obtenerIntegrantes();
								}
								ref.jefeActual = ref.dependencia.jefe;
							}
						}
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		cargarDatosDependencia: function(){
			var ref = this;
			console.log("Dependencia cargada:");
			console.log(ref.dependencia);
			ref.modulo.formulario.tipo.val(ref.dependencia.tipo);
			ref.modulo.formulario.tipo.change();
			
			ref.modulo.formulario.tipoUnidadMatricial.val(ref.dependencia.tipoUnidadMatricial);
			ref.modulo.formulario.tipoUnidadMatricial.change()
			ref.modulo.formulario.codigo.val(ref.dependencia.codigo);
			ref.modulo.formulario.codigo.change();
			ref.modulo.formulario.siglas.val(ref.dependencia.siglas);
			ref.modulo.formulario.siglas.change();
			ref.modulo.formulario.nombre.val(ref.dependencia.nombre);
			ref.modulo.formulario.nombre.change();
			ref.modulo.formulario.estado.val(ref.dependencia.estado);
			ref.modulo.formulario.estado.change();
			ref.modulo.formulario.jerarquia.val(ref.dependencia.jerarquia);
			ref.modulo.formulario.jerarquia.change();
			ref.modulo.formulario.jefe.val(ref.dependencia.jefe);
			ref.modulo.formulario.jefe.change();
			//INICIO ticket 9000004410
			if(ref.dependencia.tipo == "D"){
				ref.modulo.formulario.sinJefe.removeAttr('disabled');
				if(ref.dependencia.jefe == "SINJEFE"){
					ref.modulo.formulario.sinJefe.prop('checked', true);
					ref.modulo.formulario.sinJefe.change();
					ref.modulo.formulario.jefe.val("0");
					ref.modulo.formulario.jefe.change();
				}
			}else{
				ref.modulo.formulario.sinJefe.prop('disabled', true);
			}
			//FIN ticket 9000004410
			ref.modulo.formulario.lugarTrabajo.val(ref.dependencia.lugarTrabajo);
			ref.modulo.formulario.lugarTrabajo.change();
			ref.modulo.formulario.dependenciaSuperior.val(ref.dependencia.dependenciaSuperior);
			ref.modulo.formulario.dependenciaSuperior.change();
			ref.modulo.formulario.descripcionCargo.val(ref.dependencia.descripcionCargo);
			ref.modulo.formulario.descripcionCargo.change();
		},
		
		obtenerIntegrantes: function(){
			var ref = this;
			ref.modulo.obtenerIntegrantes(ref.dependencia.codigo)
					.then(function(respuesta){
						console.log("Integrantes:");
						console.log(respuesta);
						if(respuesta.estado == true){
							if(respuesta.datos != null && respuesta.datos.length > 0){
								for(var i=0;i<respuesta.datos.length;i++){
									let nuevoIntegrante = {'codigoIntegrante': respuesta.datos[i].codigoIntegrante, 'nombreIntegrante': respuesta.datos[i].nombreIntegrante};
									ref.agregarIntegrante(nuevoIntegrante);
								}
								/*ref.dependencia = respuesta.datos[0];
								ref.cargarDatosDependencia();
								ref.validarInicioFormEdicion();
								ref.obtenerIntegrantes();*/
							}
						}
					}).catch(function(error){
						ref.modSistcorr.procesar(false);
						ref.modSistcorr.showMessageErrorRequest(error);
					});
		},
		
		// BORRAR
		
		iniciarEventos2: function(){
			var ref = this;
			
			ref.modulo.componentes.btnFiltros.click();
			
			ref.modulo.componentes.btnExportExcel.click(function(){
				/*if(ref.filtro.considerarOriginadora == false && ref.filtro.codDependenciaRemitente == null){
					ref.modSistcorr.notificar("ERROR", 'Debe de seleccionar una dependencia remitente', "Error");
					ref.inicializarTabla([]);
					return;
				}*/
				ref.exportarExcel();
			});
			
			
			ref.modulo.componentes.btnBuscar.click(function(){
				ref.buscarCorrespondencias();
			});
			
			ref.modulo.componentes.btnMasFiltros.click(function(){
				ref.masFiltros = true;
				ref.modulo.componentes.btnMasFiltros.hide();
				ref.modulo.componentes.btnMenosFiltros.show();
				ref.modulo.componentes.filtrosSecundarios.show();
				ref.resetFiltrosSecundarios();
			});
			
			ref.modulo.componentes.btnMenosFiltros.click(function(){
				ref.masFiltros = false;
				ref.modulo.componentes.btnMenosFiltros.hide();
				ref.modulo.componentes.btnMasFiltros.show();
				ref.modulo.componentes.filtrosSecundarios.hide();
			});
			
			ref.modulo.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.modulo.componentes.btnResetear.click(function(){
				ref.resetarFiltros();
			});
			
			$("#cboxConsiderarDepenOriginadora").change(function(){
				var _val =  ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
				if(_val){
					ref.seleccionarDependenciaDefault();
					var idDep = $("#cmbDependenciaOriginadora").val();
					$("#cmbDependenciaOriginadora option[value='0']").remove();
					if(idDep=="0"){
						if(ref.dependenciasUsuario.length>0){
							var _dep = ref.dependenciasUsuario[0];
							ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);
							ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"' selected='selected'>" + _dep.text + "</option>");
							ref.modulo.componentes.cmbDependenciaOriginadora.change();
						}
					}
					if(!ref.jefe){
						ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
						ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									//console.log(datos);
									//console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
								}
							}
						}).on('select2:select', function(event){
							ref.seleccionarDependenciaDefault();
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
					}
				}else{
					if(ref.jefe){
						ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
						ref.modulo.componentes.cmbDependenciaOriginadora.val("0");
						ref.modulo.componentes.cmbDependenciaOriginadora.change();
					}else{
						console.log("NO SELECCIONADO, NO SOY JEFE")
						ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
						ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									console.log(datos);
									console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
								}
							}
						}).on('select2:select', function(event){
							ref.seleccionarDependenciaDefault();
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
						ref.modulo.componentes.cmbDependenciaOriginadora.val("0");
						ref.modulo.componentes.cmbDependenciaOriginadora.change();
						ref.modulo.componentes.cmbDependenciaOriginadora.attr('disabled', true);
					}
				}
			});
			
			ref.modulo.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.modulo.URL_TUTORIALES);
			});
			
			setTimeout(function() {
				ref.obtenerFiltros();
				if(Object.keys(ref.filtro).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					ref.update_form_filtros();
					ref.searchCorrespondencias();
				}
			}, 500);
			
		},
		
		resetFiltrosSecundarios: function(){
			var ref = this;
			ref.modulo.componentes.cmbNombreOriginador.val(null);
			ref.modulo.componentes.cmbNombreOriginador.change();
			ref.modulo.componentes.txtFechaDocumentoDesde.val(null);
			ref.modulo.componentes.txtFechaDocumentoDesde.change();
			ref.modulo.componentes.txtFechaDocumentoHasta.val(null);
			ref.modulo.componentes.txtFechaDocumentoHasta.change();
			
			ref.modulo.componentes.cbmTipoEmision.val(0);
			ref.modulo.componentes.cbmTipoEmision.change();
			
			ref.modulo.componentes.cbmTipoCorrespondencia.val(0);
			ref.modulo.componentes.cbmTipoCorrespondencia.change();
			ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val(null);
			ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
			//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val(null);
			//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
			ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val(null);
			ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.change();
			
			ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(null);
			ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			ref.modulo.componentes.cbmDependenciaCopia.val(null);
			ref.modulo.componentes.cbmDependenciaCopia.change();
			
			ref.modulo.componentes.cbmFlujoFirma.val(2);
			ref.modulo.componentes.cbmFlujoFirma.change();
			
			ref.modulo.componentes.cbmConfidencialidad.val(2);
			ref.modulo.componentes.cbmConfidencialidad.change();
			
			ref.modulo.componentes.cbmUrgente.val(2);
			ref.modulo.componentes.cbmUrgente.change();
			
			ref.modulo.componentes.cbmDespachoFisico.val(2);
			ref.modulo.componentes.cbmDespachoFisico.change();
			
			$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
			$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
			
		},
		
		resetarFiltros: function(){
			var ref = this;
			ref.filtro = {};
			ref.modulo.componentes.cmbDependenciaRemitente.val(null);
			ref.modulo.componentes.cmbDependenciaRemitente.change();
			ref.modulo.componentes.txtCorrelativo.val('');
			ref.modulo.componentes.txtCorrelativo.change();
			ref.modulo.componentes.txtAsunto.val('');
			ref.modulo.componentes.txtAsunto.change();
			ref.modulo.componentes.cbmEstado.val(0);
			ref.modulo.componentes.cbmEstado.change();
			ref.resetFiltrosSecundarios();
			ref.modulo.componentes.btnMenosFiltros.click();
			ref.actualizarValoresPorDefecto();
			
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			// TICKET 9000003974
			ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', false );
			ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='0'>Todas</option>");
			if(ref.dependenciasUsuario.length > 0){
				var _dep = ref.dependenciasUsuario[0];
				ref.listas.dependencias.agregarLista([{"id" : _dep.id, "text": _dep.text}]);
				//ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"' selected='selected'>" + _dep.text + "</option>"); ;
				//ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ _dep.id +"'>" + _dep.text + "</option>");
				ref.modulo.componentes.cmbDependenciaOriginadora.change();
				
				ref.seleccionarDependenciaDefault();
				
				ref.buscarCorrespondencias();
			} else {
				ref.inicializarTabla([]);
			}
		},
		
		seleccionarDependenciaDefault: function(){
			var ref = this;
			ref.listas.dependencias.agregarLista([{"id" : '0000', "text": 'Todas'}]);
			ref.modulo.componentes.cmbDependenciaRemitente.append("<option value='0000' selected='selected'>Todas</option>");
			ref.modulo.componentes.cmbDependenciaRemitente.change();
		},
		
		obtenerFiltros: function(){
			var ref = this;
			var _filtro = ref.modSistcorr.getFiltrosConsultaCorrespondencia();
			ref.filtro = _filtro;
			ref.masFiltros = ref.filtro.masFiltros || false;
			
			if(ref.masFiltros == true){
				ref.modulo.componentes.btnMasFiltros.click();
			} else {
				ref.modulo.componentes.btnMenosFiltros.click();
			}
		},
		
		inicializarComponentes2: function(){
			var ref = this;
			
			ref.componentes.cmbDependenciaOriginadora = ref.modulo.componentes.cmbDependenciaOriginadora.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
					data: function(params){
						var query = {
				        		q: params.term
				        };
				        return query;
					},
					processResults: function (respuesta) {
						var datos = [];
						var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
						if(!_val){
							datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
						}
						for(var arr in respuesta.datos){
							datos.push(respuesta.datos[arr]);
						}
						console.log(datos);
						console.log(respuesta.datos);
						ref.listas.dependencias.agregarLista(datos);
						return {results: datos};
					}
				}
			}).on('select2:select', function(event){
				ref.seleccionarDependenciaDefault();
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
			
			
			ref.componentes.cmbDependenciaRemitente = ref.modulo.componentes.cmbDependenciaRemitente.select2({
				ajax: {
					//url: ref.modulo.URL_LISTAR_DEPENDENCIAS_REMITENTES,
					url: ref.modulo.URL_LISTAR_DEPENDENCIAS_TODOS,
					data: function(params){
						var query = {
							codDependencia: ref.modulo.componentes.cmbDependenciaOriginadora.val(),
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						var _default = {id: '0000', text: 'Todas'};
						respuesta.datos.unshift(_default);
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			
			ref.componentes.txtFechaDocumentoDesde = ref.modulo.componentes.txtFechaDocumentoDesde.datepicker({
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
			
			ref.componentes.txtFechaDocumentoHasta = ref.modulo.componentes.txtFechaDocumentoHasta.datepicker({
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
			
			ref.componentes.cbmNombreOriginador = ref.modulo.componentes.cmbNombreOriginador.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_ORIGINADORES,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.originadores.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaDestinatariaInterno = ref.modulo.componentes.cbmDependenciaDestinatariaInterno.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaCopia = ref.modulo.componentes.cbmDependenciaCopia.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias.agregarLista(respuesta.datos);
						return {results: respuesta.datos};
					}
				}
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
			
			ref.componentes.cbmDependenciaDestinatariaExternaNacional = ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.select2({
				ajax: {
					url: ref.modulo.URL_LISTAR_TODAS_DEPENDENCIAS_EXT,
					data: function(params){
						var query = {
				        	q: params.term		
						};
						return query;
					},
					processResults: function (respuesta) {
						ref.listas.dependencias_ext.agregarLista(respuesta.datos);
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
			
			ref.modulo.componentes.btnFechaDesde.click(function(){
				ref.modulo.componentes.txtFechaDocumentoDesde.click();
				ref.modulo.componentes.txtFechaDocumentoDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaHasta.click(function(){
				ref.modulo.componentes.txtFechaDocumentoHasta.click();
				ref.modulo.componentes.txtFechaDocumentoHasta.focus();
			});
			
			
			
			ref.modulo.componentes.cbmTipoEmision.change(function(){
				var value = ref.modulo.componentes.cbmTipoEmision.val();
				if(value == 0){ // TODOS
					$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
					$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', true);
					$("#dependenciaDestinatariaInterna").hide();
					$("#dependenciaDestinatariaExternaNacional").hide();
					$("#dependenciaDestinatariaExternaInternaNacional").hide();
				} else if(value == 1){ //Interna
					$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', false);
					$("input:radio[name=rbtnTipoDestinatario][value='false']").prop('checked', false);
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', true);
					$("#dependenciaDestinatariaInterna").show();
					$("#dependenciaDestinatariaExternaNacional").hide();
					$("#dependenciaDestinatariaExternaInternaNacional").hide();
				} else { //Externa
					$('input:radio[name=rbtnTipoDestinatario]').prop('disabled', false);
					$("#dependenciaDestinatariaInterna").hide();
					var _valTipoDestinatario = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
					if(!_valTipoDestinatario){
						$("input:radio[name=rbtnTipoDestinatario][value='true']").prop('checked', true);
						$("input:radio[name=rbtnTipoDestinatario]").change();
					} else {
						$("input:radio[name=rbtnTipoDestinatario][value='"+_valTipoDestinatario+"']").prop('checked', true);
						$("input:radio[name=rbtnTipoDestinatario]").change();
					}
				}
			});
			
			 $('input:radio[name=rbtnTipoDestinatario]').change(function() {
				 var value = $("input:radio[name=rbtnTipoDestinatario]:checked").val();
				 value = value == 'true' ? true : false;
				 var valueTipoEmision = ref.modulo.componentes.cbmTipoEmision.val();
				 if(value == true && valueTipoEmision == 2){ // EXTERNO NACIONAL
					 $("#dependenciaDestinatariaInterna").hide();
					 $("#dependenciaDestinatariaExternaNacional").show();
					 $("#dependenciaDestinatariaExternaInternaNacional").hide();
				 } else if(value == false && valueTipoEmision == 2) { // EXTERNO INTERNACIONAL
					 $("#dependenciaDestinatariaInterna").hide();
					 $("#dependenciaDestinatariaExternaNacional").hide();
					 $("#dependenciaDestinatariaExternaInternaNacional").show();
				 } else {
					 
				 }
			 });
			 
			 if(!ref.jefe){
				 var _val =  ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
				 console.log("NO SOY JEFE");
				 console.log("VAL:" + _val);
				 if(!_val){
					 ref.modulo.componentes.cmbDependenciaOriginadora.attr('disabled', true); 
				 }else{
					 ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
					 ref.modulo.componentes.cmbDependenciaOriginadora.select2({
							ajax: {
								url: ref.modulo.URL_LISTAR_DEPENDENCIAS_ORIGINADORAS,
								data: function(params){
									var query = {
							        		q: params.term
							        };
							        return query;
								},
								processResults: function (respuesta) {
									var datos = [];
									var _val = ref.modulo.componentes.cboxConsiderarDepenOriginadora.is(':checked');
									if(!_val){
										datos = [{codigo:'0', descripcion:'Todas', id:'0', text:'Todas', selected: true}];
									}
									for(var arr in respuesta.datos){
										datos.push(respuesta.datos[arr]);
									}
									console.log(datos);
									console.log(respuesta.datos);
									ref.listas.dependencias.agregarLista(datos);
									return {results: datos};
								}
							}
						}).on('select2:select', function(event){
							ref.seleccionarDependenciaDefault();
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
				 }
			 }
		},
		
		update_form_filtros: function(){
			var ref = this;
			
			ref.modulo.componentes.cboxConsiderarDepenOriginadora.prop('checked', ref.filtro.considerarOriginadora );
			
			if(ref.filtro.codDependenciaOriginadora){
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDependenciaOriginadora, "text": ref.filtro.nombreDependenciaOriginadora}]);
				ref.modulo.componentes.cmbDependenciaOriginadora.append("<option value='"+ ref.filtro.codDependenciaOriginadora +"' selected='selected'>" + ref.filtro.nombreDependenciaOriginadora + "</option>");	
				ref.modulo.componentes.cmbDependenciaOriginadora.change();	
				if(ref.filtro.considerarOriginadora){
					ref.modulo.componentes.cmbDependenciaOriginadora.removeAttr('disabled');
				}
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
			
			
			/*if(ref.filtro.codDestinatario){
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
			}*/ //COMENTADO POR TICKET 9000003934
			
			
			if(ref.filtro.codDestinatario && ref.filtro.tipoEmision == 0){
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.val('');
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
				ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val('');
				ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.change();
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val('');
				ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
			} else if(ref.filtro.codDestinatario && ref.filtro.tipoEmision == 1) { // DEstinatario Interno
				ref.listas.dependencias.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
				ref.modulo.componentes.cbmDependenciaDestinatariaInterno.change();
			} else { // Destinatario Externo
				if(ref.filtro.destinatarioNacional == true){ //NAcional
					//ref.listas.dependencias_ext.agregarLista([{"id" : ref.filtro.codDestinatario, "text": ref.filtro.nombreDestinatario}]);
					//ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.append("<option value='"+ ref.filtro.codDestinatario +"' selected='selected'>" + ref.filtro.nombreDestinatario + "</option>");
					ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val(ref.filtro.nombreDestinatario);
					ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.change();
				} else { // Internacional
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.val(ref.filtro.nombreDestinatario);
					ref.modulo.componentes.txtDependenciasDestinatariaExternaInternacional.change();
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
			if(ref.filtro.codDependenciaOriginadora!="0"){
				ref.filtro.nombreDependenciaOriginadora = ref.listas.dependencias.buscarPorId(ref.filtro.codDependenciaOriginadora).text;
			}else{
				ref.filtro.nombreDependenciaOriginadora = "Todas";
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
			
			if(ref.filtro.codDependenciaOriginadora=="0"){
				ref.filtro.todos = true;
			}else{
				ref.filtro.todos = false;
			}
			
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
						/*ref.filtro.codDestinatario = ref.modulo.componentes.cbmDependenciaDestinatariaExternaNacional.val();
						if(ref.filtro.codDestinatario){
							ref.filtro.nombreDestinatario = ref.listas.dependencias_ext.buscarPorId(ref.filtro.codDestinatario).text;
						}*/
						ref.filtro.codDestinatario = "";
						ref.filtro.nombreDestinatario = ref.modulo.componentes.txtDependenciasDestinatariaExternaNacional.val();
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
			/*if(ref.filtro.considerarOriginadora == false && ref.filtro.codDependenciaRemitente == null){
				ref.modSistcorr.notificar("ERROR", 'Debe seleccionar una dependencia remitente', "Error");
				ref.inicializarTabla([]);
				return;
			}*/
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
			console.log("Inicializando Tabla")
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.dataTable.show();
				ref.dataTable = ref.modulo.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'nombreIntegrante', title: 'Nombre de Integrante', defaultContent: ''},
			        	{data: 'idIntegrante', title: '', defaultContent: '', render: function(data, type, full){
			        		if(full.codigoIntegrante == ref.jefeActual){
			        			return '';
			        		}else{
			        			return "<i class='fas fa-trash-alt icon_view_delete' data-toggle='tooltip' title='Clic para eliminar integrante' data-id='" + full.codigoIntegrante +"' style='cursor:pointer'></i>"
			        		}
			        	}},
			        ],
			        "columnDefs": [{
			        	"targets": [1],
			        	"width": '15px',
			        	"orderable": false
			        }],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.updateEventosTabla();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.dataTable.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
				});
				
				ref.dataTable.on( 'page.dt', function () {
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
			var allBtnsDetalle = document.querySelectorAll('.icon_view_delete');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.modulo.irAEliminar(this.dataset.id);
				});
			}
			
			$(".icon_view_delete").off('click').on('click', function(){
				var t = this;
				var codigo = $(t).attr('data-id')
				console.log("Codigo eliminado:" + codigo);
				console.log(ref.listaIntegrantes);
				for(var i=0;i<ref.listaIntegrantes.length;i++){
					var integr = ref.listaIntegrantes[i];
					if(codigo == integr.codigoIntegrante){
						ref.listaIntegrantes.splice(i, 1);
					}
				}
				ref.inicializarTabla(ref.listaIntegrantes);
			});
		}
};

setTimeout(function() {
	DEPENDENCIA_UNIDAD_VISTA.modulo = modulo_dependencia_unidad;
	DEPENDENCIA_UNIDAD_VISTA.modSistcorr = modulo_sistcorr;
	DEPENDENCIA_UNIDAD_VISTA.inicializar();
}, 200);