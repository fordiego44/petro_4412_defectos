var MODULO_CORRESPONDENCIA = (function(){
	var instance;
	function ModuloCorrespondencia(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		//this.URL_BASE_CORRESPONDENCIA = "./correspondencias"
		this.URL_DESCARGAR_DOCUMENTO = 'app/correspondencias/descargar/documento';
		this.tipoCorrespondencia = $("#tipoCorrespondencia");
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.classCompCorrespondencia = 'compCorrespondencia';
		this.classCompAsignacion = 'compAsignacion';
		this.classVerificarDocumentoPrincipalCorrespondencia = ".btnVerificarDocPrincipal_Correspondencia"
		this.classVerificarDocumentoPrincipalAsignacion = ".btnVerificarDocPrincipal_Asignacion";
		this.compTarea = $(".compTarea");
		this.classTareaFooter = 'footer_tarea';
		this.compTareaFooter = $(".footer_tarea");
		this.compCerrarSession = $(".closeSession");
		this.compMenuCantidad = $(".menucantidad");
		this.compTipoBandeja = $("#tipoBandeja");
		this.compUrl = $("#url");
		this.compNuevaCorrespondencia = $("#nuevaCorrespondencia");
		this.compPorAceptar = $("#porAceptar");
		this.compListaAsignaciones = $("#listaAsignaciones");
		this.compListaCorrespondencia = $("#listaCorrespondencias");
		this.compAbrirCompletarCorrespondencia = "btnCompletarCorrespondencia";
		this.compAbrirRechazarAsignacionCorresp = "btnRechazarAsignacionCorresp";//TICKET 9000004273
		this.compAbrirCerrarCorrespondencia = "btnCerrarCorrespondencia";
		this.compModalCerrarCorrespondencia = $("#modalCerrarCorrespondencia");
		this.compBtnCerrarCorrespondencia = $("#btnCerrarCorrespondencia");
		this.compComentarioCerrarCorrespondencia = $("#textCerrarCorrespondencia");
		// TICKET 9000004044
		this.compChkRequiereDocumentoCerrar = $("#requiereRespuestaCerrar");
		this.compTxtNumeroDocumentoCerrar = $("#textNumeroDocumentoCerrar");
		// FIN TICKET
		this.compModalCompletarCorrespondencia = $("#modalCompletarCorrespondencia");
		this.compModalRechazarAsignacionCorres = $("#modalRechazarAsignacionCorres");//TICKET 9000004273
		this.compComentarioCompletarCorrespondencia = $("#textCompletarCorrespondencia");
		this.compTxtMotivoRechazarAsignacionCorres = $("#textMotivoRechazarAsignacionCorres");//TICKET 9000004273
		this.compbtnCompletarCorrespondencia = $("#btnCompletarCorrespondencia");
		this.compbtnRechazarAsignacionCorres = $("#btnRechazarAsignacionCorres");//TICKET 9000004273
		// TICKET 900004044
		this.compChkRequiereDocumentoCompletar = $("#requiereRespuestaCompletar");
		this.compTxtNumeroDocumentoCompletar = $("#textNumeroDocumentoCompletar");
		// FIN TICKET
		this.compAbrirAceptarCorrespondencia = "btnAceptarCorrespondencia";
		this.compModalAceptarCorrespondencia = $("#modalAceptarCorrespondencia");
		this.compBtnAceptarCorrespondencia = $("#btnAceptarCorrespondencia");
		this.compAbrirRechazarCorrespondencia = "btnRechazarCorrespondencia";
		this.compModalRechazarCorrespondencia = $("#modalRechazarCorrespondencia");
		this.compBtnRechazarCorrespondencia = $("#btnRechazarCorrespondencia");
		this.compComentarioRechazarCorrespondencia = $("#textRechazarCorrespondencia");
		this.plantillaCorrespondencias = $("#template-correspondencias");
		this.plantillaAsignaciones = $("#template-asignaciones");
		this.plantillaFiltros = $("#template-filtros");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.dependenciaDefecto = "";
		
		this.compModalDescargarArchivo = $("#modalDescargarArchivo");
		this.compBtnDescargarArchivo = $("#btnDescargaArchivoSi");
		this.compFiltrosBusqueda = $("#filtrosBusqueda");
		this.compSinResultados = $("#sinResultados");
		this.compVerMas = $("#verMas");
		this.cantidadTotales = "totales";
		this.URL_BASE_CORRESPONDENCIA = "app/correspondencias";
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		this.compCorrelativo = $("#correlativoDef");
		// TICKET 9000004044
		this.URL_DOCUMENTO_RESPUESTA_CURSO_CORRESPONDENCIA = "app/validarExisteDocumentoRespuestaCorrespondencia";
		this.URL_DOCUMENTO_RESPUESTA_CURSO_ASIGNACION = "app/validarExisteDocumentoRespuestaAsignacion";
		this.URL_NUEVA_CORRESPONDENCIA = "app/registro";
		this.URL_VERIFICAR_ESTADO_NUMERO_DOCUMENTO = "app/verificarEstadoNumeroDocumento";
		this.URL_ENLAZAR_CORRESPONDENCIA_NUMERO_DOCUMENTO = "app/enlazarCorrespondenciaNumeroDocumento";
		// FIN TICKET
		
		// TICKET 9000004497
		this.compActivarSeleccionMultiple = "btnActivarSeleccionMultiple";
		this.btnAsignacionGrupal = $("#btnAsignacionGrupal");
		// FIN TICKET
	}
	
	
	ModuloCorrespondencia.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	
		
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondencia(SISCORR_APP);
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