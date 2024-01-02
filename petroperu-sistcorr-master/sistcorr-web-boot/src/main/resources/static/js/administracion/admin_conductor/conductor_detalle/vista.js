var modulo_conductor_edicion = MODULO_CONDUCTOR_EDICION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var CONDUCTOR_EDICION_VISTA = {
		modSistcorr: null,
		modulo: null,
		esEdicion: false,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		indiceURLS: 0,
		sizeScreen: 500,
		procesando: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.indiceURLS = ref.esEdicion == false ? 0 : 1;
			ref.inicializarComponentes();
			var conductor = JSON.parse(sessionStorage.getItem('OBJ_CONDUCTOR'));
			
			if (conductor!=null){
				ref.modulo.componentesConductor.workflowId.val(conductor['id']);
				ref.modulo.componentesConductor.txtProceso.val(conductor['proceso']);
				ref.modulo.componentesConductor.txtVersion.val(conductor['version']);
				ref.modulo.componentesConductor.txtReferenciaPrincipal.val(conductor['referenciaPrincipal']);
				ref.modulo.componentesConductor.txtReferenciaAlternativa.val(conductor['referenciaAlternativa']);
				ref.modulo.componentesConductor.txtMensajeError.val(conductor['ultimoMensaje'].replace(/(\r\n|\n|\r)/gm, ""));
				
				label = $("label[for='" + ref.modulo.componentesConductor.txtProceso.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.modulo.componentesConductor.txtVersion.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.modulo.componentesConductor.txtReferenciaPrincipal.attr('id') + "']");
				label.addClass('active');
				
				label = $("label[for='" + ref.modulo.componentesConductor.txtReferenciaAlternativa.attr('id') + "']");
				label.addClass('active');
			}
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.modSistcorr.eventosS2Remote();	
			ref.modSistcorr.eventoDatePicker();
			ref.modSistcorr.eventoTextArea();
			ref.modSistcorr.eventoSelect();
			ref.modSistcorr.eventoInputText();
			setTimeout(function() {
				ref.inicializarEventosComponentes();
				console.log("CHECK FIRMA");
				if(ref.correspondencia && ref.correspondencia.id == 0){
					$("#corr_flujo_firma_digital").click();
					$("#corr_primerFirmante").click();
					$("#RutaAprobacion-tab").closest('li').hide();
					$("#Flujo-tab").closest('li').hide();
				}
			}, 500);
			
			ref.modulo.btnReintentar.off('click').on('click', function(){
				ref.reintentarConductor();
			});
			
			ref.modulo.btnSaltarPaso.off('click').on('click', function(){
				ref.saltarPasoConductor();
			});
			
			ref.modulo.btnTerminar.off('click').on('click', function(){
				ref.terminarPasoConductor();
			});
		},
		
		reintentarConductor : function(){
			var ref=this;
			ref.modSistcorr.procesar(true);
			
			ref.parametros = {
					'id' :ref.modulo.componentesConductor.workflowId.val()
			}
			
			ref.modulo.reintentarConductor(ref.parametros.id)
			.then(function(respuesta){
				console.log("Respuesta.estado:" + respuesta.estado)
				if(respuesta.estado == true){
					sessionStorage.removeItem('OBJ_CONDUCTOR');
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					setTimeout(function() {
	        			console.log("redirect...");
	        			window.location.replace("../../app/conductor");
					}, 2000);
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		
		saltarPasoConductor : function(){
			var ref=this;
			ref.modSistcorr.procesar(true);
			
			ref.parametros = {
					'id' :ref.modulo.componentesConductor.workflowId.val()
			}
			
			ref.modulo.saltarPasoConductor(ref.parametros.id)
			.then(function(respuesta){
				console.log("Respuesta.estado:" + respuesta.estado)
				if(respuesta.estado == true){
					sessionStorage.removeItem('OBJ_CONDUCTOR');
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					setTimeout(function() {
	        			console.log("redirect...");
	        			window.location.replace("../../app/conductor");
					}, 2000);
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		terminarPasoConductor : function(){
			var ref=this;
			ref.modSistcorr.procesar(true);
			
			ref.parametros = {
					'id' :ref.modulo.componentesConductor.workflowId.val()
			}
			
			ref.modulo.terminarPasoConductor(ref.parametros.id)
			.then(function(respuesta){
				console.log("Respuesta.estado:" + respuesta.estado)
				if(respuesta.estado == true){
					sessionStorage.removeItem('OBJ_CONDUCTOR');
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					setTimeout(function() {
	        			console.log("redirect...");
	        			window.location.replace("../../app/conductor");
					}, 2000);
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		
		inicializarEventosComponentes: function(){
			var ref = this;
			
			ref.modulo.tabs.tabDatos.compHeader.addClass('petro-tabs-activo');
			
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				if (!e.target.dataset.tab) {
			        return;
			    }
				 var $tab = $(e.currentTarget);
				 $('a[data-toggle="tab"]').removeClass('petro-tabs-activo');
				 $tab.addClass('petro-tabs-activo');
				 if (e.target.dataset.tab == "datos") {
				      $("#contenidoAccordionDatos").show();
				      $("#contenidoHistorialDestinatario").hide();
				      $("#contenidoCopias").hide();
				 }
				 
			});
			
			ref.modulo.compRetroceder.click(function(){
				sessionStorage.setItem("origPagCond", "verDetalleCond");
				ref.modSistcorr.procesar(true);
				ref.modSistcorr.retroceder();
			});
		},				
};

setTimeout(function(){
	CONDUCTOR_EDICION_VISTA.modSistcorr = modulo_sistcorr;
	CONDUCTOR_EDICION_VISTA.modulo = modulo_conductor_edicion;
	CONDUCTOR_EDICION_VISTA.modulo.modSistcorr = modulo_sistcorr;
	CONDUCTOR_EDICION_VISTA.esEdicion = EDICION || false;
	CONDUCTOR_EDICION_VISTA.listas = {};
	CONDUCTOR_EDICION_VISTA.listas.lugarTrabajo = new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.dependencia =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.persona =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.dependenciaExterna =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.pais =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.departamento =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.provincia =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.listas.distrito =  new LISTA_DATA([]);
	CONDUCTOR_EDICION_VISTA.inicializar();
}, 500);
