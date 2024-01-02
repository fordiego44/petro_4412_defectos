/*9000004412*/
var MODULO_ADJUNTAR_DOCUMENTO = (function(){
	var instance;
	function ModuloAdjuntarDocumento(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compBtnGuardar = $("#btnAdjuntarDocumento");
		this.compBtnCancelar = $("#btnCancelarCorrespondencia"); 
		this.plantillaListaArchivosAdjuntos = $("#template-archivosAdjuntos");
		this.btnValidarSUNAT = $("#validarSUNAT");
		this.tabs = {
				tabDatos: {
					compHeader: $("#Datos-tab"),
					compBody: $("#contenidoAccordionDatos"),
					accordion: {
						datosRemitente:{ 
							compBtnFechaDocumento: $("#btnRmt_FechaDocumento"),  
							compFechaDocumento : $("#rmt_fechaDocumento"),
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
	}
	
	ModuloAdjuntarDocumento.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloAdjuntarDocumento.prototype.htmlListaArchivosAdjuntos = function(adjuntos){
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
        var object = new ModuloAdjuntarDocumento(SISCORR_APP);
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