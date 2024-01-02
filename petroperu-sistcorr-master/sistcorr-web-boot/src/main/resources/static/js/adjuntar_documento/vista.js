/*9000004412*/
var modulo_adjuntar_documento = MODULO_ADJUNTAR_DOCUMENTO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var ADJUNTAR_DOCUMENTO_VISTA = {
		modSistcorr: null,
		modulo: null,
		tiposCorrespondencia: [],
		correspondenciaSelecionada: {},  
		procesando: false,
		componentes: {combosSimples:{}, combosS2: {}, datePikers:{}},
		archivosSeleccionados: [],
		archivosAdjuntos: [],
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
		},
		
		
		inicializarComponentes: function(){
			var ref = this; 
			
			ref.modulo.compBtnGuardar.click(function(){
				console.log('compBtnGuardar');
				ref.procesoCargarAdjunto();
			});
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
	            	//var label = $("label[for='" + $comp.attr('id') + "']");
	            	//label.addClass('active');
	            },
	        });
			
			ref.modulo.tabs.tabDatos.accordion.datosRemitente.compFechaDocumento.change();
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.btnOpenModalNuevoArchivo.click(function(){ 
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.click(); 
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.change(function(e){
				ref.archivosSeleccionados = e.target.files;
				// TICKET 900004270
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.submit();
				// FIN TICKET
			});
			
			ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.submit(function(e){ 
				
				e.preventDefault();
				if(ref.archivosSeleccionados.length > 0){
					var esPrincipal = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compPrincipal.prop("checked");
					var maxSizeMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size');
					var maxSizeSinFirmaDigitalMB = ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFile.data('max-size-sin-firma-digital');
					var maxSize = maxSizeMB * 1048576;
					var maxSizeSinFirmaDigital = maxSizeSinFirmaDigitalMB * 1048576; 
					
					// TICKET 9000004270
					var d = new Date();
					var identificadorIterativo = d.getTime();
					// FIN TICKET
					for(var indice = 0; indice < ref.archivosSeleccionados.length; indice++){
						var tamanioArchivo = ref.archivosSeleccionados[indice].size;
						var tamanioMaximo = 0;
						var tamanioMaximoMB = 0;
						
						
							tamanioMaximo = maxSizeSinFirmaDigital;
							tamanioMaximoMB = maxSizeSinFirmaDigitalMB;
					
						
						console.log("Tamaño del archivo " + ref.archivosSeleccionados[indice].name + ": " + tamanioArchivo + " (" + tamanioMaximo + ")");
						
						if(tamanioArchivo > tamanioMaximo){
							ref.modSistcorr.notificar("ERROR", 'El tamaño permitido es de ' + tamanioMaximoMB + " MB, el archivo " + ref.archivosSeleccionados[indice].name + " excede dicho tamaño.", "Error");
							//return false;
						} else{
							var adjunto  = {};
							adjunto.identificador = identificadorIterativo;
							var nombreArcDP = ref.archivosSeleccionados[indice].name;
							adjunto.file = ref.archivosSeleccionados[indice];
							
							adjunto.nombre = ref.archivosSeleccionados[indice].name;
							adjunto.contentType = ref.archivosSeleccionados[indice].type;
							adjunto.tamanio = tamanioArchivo/1048576;
							adjunto.indicadorRemoto = 'N';
							ref.archivosAdjuntos.push(adjunto);
							identificadorIterativo++;
							
						}
					}
				}
				ref._actualizarListaArchivosAdjuntos();
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compFormAdjuntar.trigger("reset");
				ref.modulo.tabs.tabDatos.accordion.datosArchivos.compModal.modal('hide');				
			});
			
		},	
		
		procesoRegistrarValijas: function(){
			console.log('Proceso Registar Valijas')
		},
		
		_actualizarListaArchivosAdjuntos: function(){
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
		},
		
		
};

setTimeout(function(){
	ADJUNTAR_DOCUMENTO_VISTA.modSistcorr = modulo_sistcorr;
	ADJUNTAR_DOCUMENTO_VISTA.modulo = modulo_adjuntar_documento;
	ADJUNTAR_DOCUMENTO_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	ADJUNTAR_DOCUMENTO_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	ADJUNTAR_DOCUMENTO_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	ADJUNTAR_DOCUMENTO_VISTA.inicializar();
}, 500);
