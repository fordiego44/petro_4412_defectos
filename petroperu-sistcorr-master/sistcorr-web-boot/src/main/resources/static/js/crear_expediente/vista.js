/*9000004412*/
var modulo_crear_expediente = MODULO_CREAR_EXPEDIENTE.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CREAR_EXPEDIENTE_VISTA = {
		modSistcorr: null,
		modulo: null,
		tiposCorrespondencia: [],
		expediente: {},
		correspondenciaSelecionada: {},  
		procesando: false,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		archivosSeleccionados: [],
		archivosAdjuntos: [],
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.iniciarEventos(); //4412 defecto 10   
			ref.inicializarComponentes();
		},
		//4412 defecto 10 
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compContacto.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoProceso.select2();
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreVB.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioVB.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreCB.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioCB.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioRecepcion.select2();
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreRecepcion.select2();
			
			
		},
		//4412 defecto 10  fin
		 
		
		inicializarComponentes: function(){
			var ref = this; 
			
			ref.modulo.btnValidarSUNAT.click(function(){
				 console.log("???");
			});	
			//4412 defecto 10 
			ref.modulo.compBtnCancelar.click(function(){
				 	 
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compNroProceso.val('');
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compMemo.val('');
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compAnexo.val('');
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBases.val('');
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compValorBase.val('');
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compObjetoProceso.val('');  
				 	
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoProceso.val('0').trigger('change');;
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val('0000').trigger('change');;
				 	ref.modulo.tabs.tabDatos.accordion.datosRemitente.compContacto.val('0').trigger('change');;
				 	  
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioVB.val('8:00 AM').trigger('change');;
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreVB.val('6:00 PM').trigger('change');;
				     
				     
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioCB.val('8:00 AM').trigger('change');;
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreCB.val('6:00 PM').trigger('change');;
				     
				    
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioRecepcion.val('8:00 AM').trigger('change');;
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreRecepcion.val('6:00 PM').trigger('change');;


				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.val('');
				    ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.val('');
			});	
			//4412 defecto 10  fin
			/*SECCION FECHAS*/
			
			/*Venta Bases*/
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDocumento.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.change();
			
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDesdeVB.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.change();
			
			/*CONSULTA DE BASES*/
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaInicioCB.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.change();
			
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDesdeCB.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.change();
			
			/*Recepcion*/
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaInicioRecepcion.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.change();
			
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDesdeRecepcion.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.change();
			
			/*Impugnaciones*/
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaInicioImpugnaciones.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.change();
			
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compBtnFechaDesdeImpugnaciones.click(function(){
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.click();
				ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.focus();
			});
			
			ref.componentes.datePikers.fechaDocumento = ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.datepicker({
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
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.change();
			
			/**/
			
			ref.modulo.compBtnGuardar.click(function(event){
				console.log('crear expediente');
				ref.crearExpediente();
			});
			
		},	
		
		crearExpediente : function(){
			console.log('crearExpediente')
			var ref = this;
			ref.expediente.nroProceso=this.modulo.tabs.tabDatos.accordion.datosRemitente.compNroProceso.val();
			ref.expediente.tipoProceso=this.modulo.tabs.tabDatos.accordion.datosRemitente.compTipoProceso.val();
			ref.expediente.nroMemo=this.modulo.tabs.tabDatos.accordion.datosRemitente.compMemo.val();
			ref.expediente.codigoDependencia=this.modulo.tabs.tabDatos.accordion.datosRemitente.compDependencia.val();
			ref.expediente.registroContacto=this.modulo.tabs.tabDatos.accordion.datosRemitente.compContacto.val();
			ref.expediente.anexo=this.modulo.tabs.tabDatos.accordion.datosRemitente.compAnexo.val();
			ref.expediente.cantidadBases=this.modulo.tabs.tabDatos.accordion.datosRemitente.compBases.val();
			ref.expediente.valorPliego=this.modulo.tabs.tabDatos.accordion.datosRemitente.compValorBase.val();
			ref.expediente.objetoProceso=this.modulo.tabs.tabDatos.accordion.datosRemitente.compObjetoProceso.val();
			ref.expediente.fechaInicioVentaBase=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.val();
			ref.expediente.fechaCierreVentaBase=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeVB.val();
			ref.expediente.fechaInicioConsulta=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioCB.val();
			ref.expediente.fechaFinConsulta=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeCB.val();
			ref.expediente.fechaInicioRecep=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioRecepcion.val();
			ref.expediente.fechaFinRecep=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeRecepcion.val();
			ref.expediente.fechaInicioImpugnacion=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaInicioImpugnaciones.val();
			ref.expediente.fechaFinalImpugnacion=this.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDesdeImpugnaciones.val();
			
			ref.expediente.horaInicioVentaBase=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioVB.val();
			ref.expediente.horaCierreVentaBase=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreVB.val();
			ref.expediente.horaInicioConsulta=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioCB.val();
			ref.expediente.horaFinConsulta=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreCB.val();
			ref.expediente.horaInicioRecep=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraInicioRecepcion.val();
			ref.expediente.horaFinRecep=this.modulo.tabs.tabDatos.accordion.datosRemitente.compHoraCierreRecepcion.val();
			
			ref.modSistcorr.procesar(true);
			var _expediente = ref.expediente;
			console.log('se llena el objeto expediente');
			ref.modulo.registrarExpediente(_expediente)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("Expediente Grabado.");
					console.log(respuesta);
					setTimeout(function() {
						//ref.modulo.abrirEdicion(respuesta.datos[0]);
					}, 2000);
					ref.modSistcorr.procesar(false);
				} else {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
			
			
		},
		
		/*_actualizarListaArchivosAdjuntos: function(){
			var ref = this;
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.empty();
			var html = ref.modulo.htmlListaArchivosAdjuntos(ref.archivosAdjuntos);
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compListaArchivos.html(html);
			
			// TICKET 9000004270
			for(var ind = 0; ind < ref.archivosAdjuntos.length; ind++){
				if(ref.archivosAdjuntos[ind].tipo == "PRINCIPAL"){
					$("#corr_archivo_" + ref.archivosAdjuntos[ind].identificador).attr('checked', true);
				}else{
					$("#corr_archivo_" + ref.archivosAdjuntos[ind].identificador).removeAttr('checked');
				}
			}
			// FIN TICKET
			
			//Eventos
			$("." + ref.modulo.classBtnEliminarAdjunto).click(function(event){
				var $comp = $(event.currentTarget);
				var idArchivoAdjunto = $comp.data("identificador");
				console.log("Eliminar id:" + idArchivoAdjunto);
				ref._eliminarArchivoAdjunto(idArchivoAdjunto);
			});
			
			// TICKET 9000004270
			var _val = $("input:radio[name=corr_firmaDigital]:checked").val();
			_val = (_val == 'true');
			console.log("Valor:" + _val)
			if(!_val){
				$(".archivo-principal").attr('checked', false);
				$(".archivo-principal").attr('disabled', true);
			}
			
			$(".archivo-principal").change(function(event){
				var $comp = $(event.currentTarget);
				var name_id = $comp.attr('id');
				var id = name_id.substring(13);
				console.log();
				for(var i=0;i<ref.archivosAdjuntos.length;i++){
					if(ref.archivosAdjuntos[i].identificador == id){
						if($comp.prop('checked')){
							ref.archivosAdjuntos[i].principal = true;
							ref.archivosAdjuntos[i].tipo = 'PRINCIPAL';
						}else{
							ref.archivosAdjuntos[i].principal = false;
							ref.archivosAdjuntos[i].tipo = 'SECUNDARIO';
						}
					}
				}
			});
			// FIN TICKET
			
			ref.modSistcorr.eventoTooltip();
		},*/
		
};

setTimeout(function(){
	CREAR_EXPEDIENTE_VISTA.modSistcorr = modulo_sistcorr;
	CREAR_EXPEDIENTE_VISTA.modulo = modulo_crear_expediente;
	CREAR_EXPEDIENTE_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	CREAR_EXPEDIENTE_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	CREAR_EXPEDIENTE_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	CREAR_EXPEDIENTE_VISTA.inicializar();
}, 500);
