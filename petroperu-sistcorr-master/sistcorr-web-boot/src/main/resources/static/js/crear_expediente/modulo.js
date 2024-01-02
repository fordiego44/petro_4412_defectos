/*9000004412*/
var MODULO_CREAR_EXPEDIENTE = (function(){
	var instance;
	function ModuloCrearExpediente(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compBtnGuardar = $("#btnCrearExpediente");
		this.compBtnCancelar = $("#btnCancelarExpediente"); 
		this.plantillaListaArchivosAdjuntos = $("#template-archivosAdjuntos");
		this.btnValidarSUNAT = $("#validarSUNAT");
		this.tabs = {
				tabDatos: {
					compHeader: $("#Datos-tab"),
					compBody: $("#contenidoAccordionDatos"),
					accordion: {
						datosRemitente:{ 
							compNroProceso:$("#rmt_proceso"),
							compTipoProceso: $("#rmt_tipo_proceso"),
							compMemo: $("#rmt_memo"),
							compDependencia: $("#rmt_rmt_dependenciaproceso"),
							compContacto: $("#rmt_contacto"),
							compAnexo: $("#rmt_anexo"),
							compBases: $("#rmt_bases"),
							compValorBase: $("#rmt_valor_base"),
							compObjetoProceso: $("#rmt_objeto_proceso"),  
							compBtnFechaDocumento: $("#btnRmt_FecInicioVB"),  
							compFechaDocumento : $("#rmt_fechaInicioVB"),
							compHoraInicioVB : $("#rmt_inicio"),
							compBtnFechaDesdeVB: $("#btnRmt_FecDesdeVB"),  
							compFechaDesdeVB : $("#rmt_fechaDesdeVB"),
							compHoraCierreVB : $("#rmt_cierre"),							
							compBtnFechaInicioCB: $("#btnRmt_FecInicioCB"),  
							compFechaInicioCB : $("#rmt_fechaInicioCB"),
							compHoraInicioCB : $("#rmt_inicioC"),	
							compBtnFechaDesdeCB: $("#btnRmt_FecDesdeCB"),  
							compFechaDesdeCB : $("#rmt_fechaDesdeCB"),
							compHoraCierreCB : $("#rmt_cierreC"),	
							compBtnFechaInicioRecepcion: $("#btnRmt_FechaInicioRecepcion"),  
							compFechaInicioRecepcion : $("#rmt_fechaInicioRecepcion"),
							compHoraInicioRecepcion : $("#rmt_inicioP"),	
							compBtnFechaDesdeRecepcion: $("#btnRmt_FechaDesdeRecepcion"),  
							compFechaDesdeRecepcion : $("#rmt_fechaDesdeRecepcion"),
							compHoraCierreRecepcion : $("#rmt_cierreP"),	
							compBtnFechaInicioImpugnaciones: $("#btnRmt_FechaInicioImpugnaciones"),  
							compFechaInicioImpugnaciones : $("#rmt_fechaInicioImpugnaciones"),
							compBtnFechaDesdeImpugnaciones: $("#btnRmt_FechaDesdeImpugnaciones"),  
							compFechaDesdeImpugnaciones : $("#rmt_fechaDesdeImpugnaciones")
						},
						datosArchivos: {
							id: '',
							btnOpenModalNuevoArchivo: $("#btnOpenModalNuevoArchivo"),
							compHead: $("#btnDatosArchivos"),
							compFormAdjuntar: $("#frmAdjuntarArchivo"),
							compFile: $("#arc_file"),
							compPrincipal: $("#arc_principal"),
							compListaArchivos: $("#listaArchivosAdjuntos"),
							compModal: $("#modalNueoArchivAdjunto")
							
						}
					}
				}  
		};
		
		this.URL_REGISTRAR_EXPEDIENTE = '../app/registrarExpediente';
	}
	
	ModuloCrearExpediente.prototype.registrarExpediente = function(expediente){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		//var fd = new FormData();
		console.log("RegistrarExpediente");
		console.log(expediente);
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTRAR_EXPEDIENTE,
			cache	:	false,
			data	:	JSON.stringify(expediente),
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
				 xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        
			        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCrearExpediente.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloCrearExpediente.prototype.htmlListaArchivosAdjuntos = function(adjuntos){
		var ref = this;
		Handlebars.registerHelper('icono_documento', function(adjunto){
			if(adjunto.contentType.indexOf("pdf") > -1){
				return "fa-file-pdf icon_pdf";
			} else if(adjunto.contentType.indexOf("word") > -1){
				return "fa-file-word icon_word";
			} else if(adjunto.contentType.indexOf("png") > -1){
				return "fa-file-image icon_image";
			}  else if(adjunto.contentType.indexOf("jpg") > -1){
				return "fa-file-image icon_image";
			} else 	if(adjunto.contentType.indexOf("spreadshee") > -1){
				return "fa-file-excel icon_excel";
			} else {
				return "fa-file icon_otro";
			}
		});
		
		Handlebars.registerHelper('ver_principal', function(adjunto){
			console.log(adjunto);
			
				return "display:none;";
		});
		
		Handlebars.registerHelper('descargar', function(adjunto){
			if(adjunto.file == null){
				return "btnDescargarArchivo";
			} else{
				return "";
			}
		});
		
		var plantillaScript = ref.plantillaListaArchivosAdjuntos.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'adjuntos' : adjuntos};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	  
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCrearExpediente(SISCORR_APP);
        return object;
    }
    return {
        getInstance: function (SISCORR_APP) {
            if (!instance) {
                instance = createInstance(SISCORR_APP);
                SISCORR_APP.fire('loaded', instance);
            }
            return instance;
        }
    };
	
})();