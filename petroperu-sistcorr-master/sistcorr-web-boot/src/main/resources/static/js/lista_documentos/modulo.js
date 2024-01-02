var MODULO_LISTA_DOCUMENTOS = (function(){
	var instance;
	function ModuloListaDocumentos(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.bandeja = $("#bandeja");
		this.usuario = $("#solicitante");
		this.url = $("#url");
		this.compListaDocumentos = $("#listaDocumentos");
		this.classCompCorrespondencia = "compCorrespondencia";
		this.classCompCorrespondenciaFooter = "footer_correspondencia";
		this.plantillaCorrespondencias = $("#template-correspondencias");
		this.plantillaFiltros = $("#template-filtros");
		this.compRechazarMotivo = $("#rechzarMotivo");
		this.compRechazarMotivoDescripcion = $("#rechzarMotivoDescripcion");
		this.compRechazarCorrespondencia = $("#rechazarCorrespondencia");
		this.compModalRechazar = $("#modalRechazar");
		this.btnRechzarSi = $("#btnConfirmaRechazoSi");
		this.compModalFiltrar = $("#modalFiltrarBandeja");
		this.compFiltroCorrelativo = $("#filtroCorrelativo");
		this.compFiltroDependenciaDestinoOrg = $("#textDependenciaDestinoOrg");//ticket 9000003866
		this.compFiltroTipoCorrespondencia = $("#textTipoCorrespondencia");//ticket 9000003866
		this.compFiltroAsunto = $("#filtroAsunto");
		this.compFiltroRechazado = $('input[name=filtroRechazado]');
		this.compFiltroMisPendientes = $('input[name=filtroMisPendientes]');
		this.compFiltroDeclinado = $('input[name=filtroDeclinado]');
		this.compFiltroFechaInicio = $("#filtroFechaInicio");
		this.comBtnFiltroFechaInicio = $("#btnFechaInicio");
		this.compFiltroFechaFin = $("#filtroFechaFin");
		this.compBtnFiltroFechaFin = $("#btnFechaFin");
		this.btnFiltrar = $("#btnFiltrarBandeja");
		this.compFiltrosBusqueda = $("#filtrosBusqueda");
		this.btnAbrirBusqueda = $("#btnAbrirBusqueda");
		this.btnNuevoDocumento = $("#btnNuevoDocumento");
		this.btnEmitirFirmaGrupal = $("#btnEmitirFirmaGrupal");
		this.compSinResultados = $("#sinResultados");
		this.compCerrarSession = $(".closeSession");
		this.motivo_rechazo = {
				ERROR_ASIGNACION	: "ERROR DE ASIGNACIÓN",
				ERROR_ELABORACION	: "ERROR DE ELABORACIÓN"
		};
		this.compModalReasignarCorrespondencia = $("#modalReasignarCorrespondencia");
		this.compReasignarCorrespondencia = $("#idCorrespondenciaReasignar");
		this.btnReasignarCorrespondencia = $("#btnReasignarCorrespondencia");
		this.compUsuarioReemplazante = $("#usuarioReemplazante");
		this.compDeclinarCorrespondencia = $("#idCorrespondenciaDeclinar");
		this.btnDeclinarCorrespondencia = $("#btnDeclinarCorrespondencia");
		this.compModalDeclinarCorrespondencia = $("#modalDeclinarCorrespondencia");
		this.classAsignarFirma = "btnAsignarFirma";
		this.classEmitirFirma = "btnEmitirFirma";
		this.classRechzarFirmado = "btnRechazarFirmado";
		this.classRechzarFirma = "btnRechazar";
		this.classRechazarResponsableFirma = "btnRechazarResponsable";
		this.classEnviarCorrespondencia = "btnEnviarDocumento";
		this.classReasignarCorrespondencia = "btnReasignar";
		this.classDeclinarCorrespondencia = "btnDeclinar";
		this.compModalEnviarDocumento = $("#modalEnviarCorrespondencia");
		this.compIdCorrespondenciaEnviar = $("#idCorrespondenciaEnviar");
		this.btnEnviarCorrespondencia = $("#btnEnviarCorrespondencia");
		this.btnExportarExcelCorrespondenciaBS = $("#btnExportarExcelCorrespondenciaBS");//ticket 9000004807
		this.URL_LISTAR_CORRESPONDENCIAS = "../../app/emision/listar-correspondencias";
		this.URL_EXPORTAR_CORRESPONDENCIAS = "../../app/emision/exportar-correspondencias";
		this.URL_RECHAZAR_FIRMA = '../../app/emision/rechazar-solicitud-firma';
		this.URL_NUEVO_DOCUMENTO = '../../app/registro';
		this.URL_ENVIAR_CORRESPONDENCIA = '../../app/emision/enviar-correspondencia';
		this.URL_REASIGNAR_CORRESPONDENCIA = '../../app/emision/reasignar-correspondencia';
		this.URL_DECLINAR_CORRESPONDENCIA = '../../app/emision/declinar-correspondencia';
		this.URL_LISTAR_REEMPLAZANTES = '../../app/emision/buscar/funcionarios-reemplazantes';
		this.btnAbrirTutoriales = $("#btnAbrirTutoriales");
		this.URL_TUTORIALES = "app/tutoriales";
		
		this.compModalAsignarFirmante = $("#modalAsignarSiguienteFirmante");
		this.compIdCorrespondenciaAsignar = $("#idCorrespondenciaAsignar");
		this.btnAsignarSiguienteFirmante = $("#btnAsignarSiguienteFirmante");
		// TICKET 9000003994
		this.compFiltroDependencia = $("#filtroDependencia");
		// FIN TICKET
		// TICKET 9000003996
		this.compRechazarMotivoResponsable = $("#rechzarMotivoResponsable");
		this.compRechazarMotivoDescripcionResponsable = $("#rechzarMotivoDescripcionResponsable");
		this.compRechazarCorrespondenciaResponsable = $("#rechazarCorrespondenciaResponsable");
		this.compModalRechazarResponsable = $("#modalRechazarResponsable");
		this.btnRechzarResponsableSi = $("#btnConfirmaRechazoResponsableSi");
		
		this.compRechazarMotivoFirmado = $("#rechzarMotivoFirmado");
		this.compRechazarMotivoDescripcionFirmado = $("#rechzarMotivoDescripcionFirmado");
		this.compRechazarCorrespondenciaFirmado = $("#rechazarCorrespondenciaFirmado");
		this.compModalRechazarFirmado = $("#modalRechazarFirmado");
		this.btnRechzarFirmadoSi = $("#btnConfirmaRechazoFirmadoSi");
		// FIN TICKET
		this.classFirmaGrupal = "chk_firma_grupal";
		this.btnFirmaGrupal = $("#btnEmitirFirmaGrupal");
		this.URL_VALIDAR_NIVEL_FIRMA = "../../app/emision/validar/nivel-firma";
		this.URL_GENERAR_ZIP_GRUPAL = "../../app/firma-digital/zipear-grupal";
	}
	
	ModuloListaDocumentos.prototype.listarCorrespondencias = function(filtro){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.bandeja.val();
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_LISTAR_CORRESPONDENCIAS + "/" + bandeja,
			cache	:	false,
			data	:	JSON.stringify(filtro),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	//INI TICKET 9000004807
	ModuloListaDocumentos.prototype.exportarExcelCorrespondenciasBS = function(filtros){
		var ref = this;
		
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var bandeja = ref.bandeja.val();
		
		return $.ajax({
			type		:	'POST',
			url			: 	ref.URL_EXPORTAR_CORRESPONDENCIAS + '/' + bandeja,
			cache		:	false,
			data		:	JSON.stringify(filtros),
			xhrFields	:	{
                responseType: 'blob'
            },
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	//FIN TICKET 9000004807
	
	ModuloListaDocumentos.prototype.htmlCorrespondencias = function(correspondencias){
		var ref = this;
		var bandeja = ref.bandeja.val();
		var usuario = ref.usuario.val();
		
		Handlebars.registerHelper('url_detalle_correspondencia', function(correspondencia){
            return "../../app/ver-detalle/" + correspondencia.id;
        });
		
		Handlebars.registerHelper('url_asignar_firma', function(correspondencia){
            return "../../app/asignacion-firma/" + correspondencia.id;
        });
		
		Handlebars.registerHelper('url_modificar_correspondencia', function(correspondencia){
            return "../../app/edicion/" + correspondencia.id;
        });
		
		
		Handlebars.registerHelper('ver_detalle', function(correspondencia){
			var estilo = "display: none;";
			switch (bandeja) {
				case "pendiente":
					estilo = "display: block;";
					break;
				case "firmado":
					estilo = "display: block;";
					break;
				case "enviado":
					estilo = "display: block;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('ver_modificar', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			
			switch (bandeja) {
				case "pendiente":
					if((dependencia == "0" && (correspondencia.usuario == usuario && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_PENDIENTE)) || 
							(correspondencia.usuario.toUpperCase() == dependencia.toUpperCase() && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_PENDIENTE)){
						estilo = "display: block;";
					} else{
						estilo = "display: none;";
					}
					// Adicion 9-3874
					if(correspondencia.firmaDigital 
					 && (correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR
					 && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_FIRMA_MANUAL
					 && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_DOCUMENTOS
					 // TICKET 9000003874
					 && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_REINICIO)){
						estilo = "display: none;";
					} else {
						estilo = "display: block;";
					}
					// fin adicion 9-3874
					break;
				case "firmado":
					if((dependencia == "0" && (correspondencia.usuario == usuario && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA )) || (correspondencia.usuario.toUpperCase() == dependencia.toUpperCase() && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA)){
						estilo = "display: block;";
					} else{
						estilo = "display: none;";
					}
					// Adicion 9-3874
					if(correspondencia.firmaDigital 
					 && (correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR
					 && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_FIRMA_MANUAL
					 && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_DOCUMENTOS)){
						estilo = "display: none;";
					} else {
						estilo = "display: block;";
					}
					// fin adicion 9-3874
					// TICKET 9000003943
					if((dependencia == "0" && (correspondencia.rutaAprobacion == true && correspondencia.usuario == usuario && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA)) || 
							(correspondencia.rutaAprobacion == true && correspondencia.usuario.toUpperCase() == dependencia.toUpperCase() && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA)){
						estilo = "display: block;";
					}
					// FIN TICKET
					break;					
					
				case "enviado":
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		
		
		Handlebars.registerHelper('asignar_firma', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					// TICKET 9000003908
					if((dependencia == "0" && (correspondencia.usuario == usuario && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_REINICIO) && correspondencia.rutaAprobacion == false && correspondencia.firmaDigital == true)) || 
							(correspondencia.usuario.toUpperCase() == dependencia.toUpperCase() && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_SIN_ASIGNAR || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_REINICIO) && correspondencia.rutaAprobacion == false && correspondencia.firmaDigital == true)){
						estilo = "display: block;";
					} else{
						estilo = "display: none;";
					}
					break;
				case "firmado":
					//if(usuario == correspondencia.usuario && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR)){
					if((dependencia == "0" && (usuario == correspondencia.usuario && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR) && correspondencia.rutaAprobacion == false)) || 
							(dependencia.toUpperCase() == correspondencia.usuario.toUpperCase() && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR) && correspondencia.rutaAprobacion == false)){
						estilo = "display: block;";
					//} else if(usuario == correspondencia.firmante && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA){ 
					} else if((dependencia == "0" && (usuario == correspondencia.firmante && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && correspondencia.rutaAprobacion == false)) || 
							(dependencia.toUpperCase() == correspondencia.firmante.toUpperCase() && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && correspondencia.rutaAprobacion == false)){
						estilo = "display: block;";
					//} else if(usuario == correspondencia.firmantePrevio &&  correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR){
					} else if((dependencia == "0" && (usuario == correspondencia.firmantePrevio &&  correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR && correspondencia.rutaAprobacion == false))|| 
							(dependencia.toUpperCase() == correspondencia.firmantePrevio.toUpperCase() &&  correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR && correspondencia.rutaAprobacion == false)){
						estilo = "display: block;";
					}else {
						estilo = "display: none;";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('enviar_documento', function(correspondencia){
			var estilo = "display: none;";
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					if(correspondencia.firmaDigital == false && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_COMPLETADA){ // si es firma manual y completada
						estilo = "display: block;";
					} else{
						estilo = "display: none;";	
					}
					break;
				case "firmado":
					if((usuario == correspondencia.usuario || usuario == correspondencia.firmante) &&  correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA){ // ultimo firmante o responsable y completada
						estilo = "display: block;";
					}else{
						estilo = "display: none;";
					}
					break;
				case "enviado":
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('emitir_firma', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					estilo = "display: none;";
					break;
				case "firmado":
					if(dependencia == "0" && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA && usuario.toUpperCase() == correspondencia.firmante.toUpperCase()){ // asignado y es el firmante
						estilo = "display: block;";
					} else {
						estilo = "display: none;";
					}
					break;
				case "enviado":
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		<!-- inicio ticket 9000003996 -->
		
		Handlebars.registerHelper('rechazar_firmado', function(correspondencia){
			var dependencia = ref.compFiltroDependencia.val();
			var estilo = "display: none;";
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					//console.log("rechazar_firmado pendiente");
					estilo = "display: none;";
					break;
				case "firmado":
					//console.log("rechazar_firmado firmado");
					if((dependencia == "0" && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && usuario == correspondencia.firmante || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA) || (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_FIRMADA && dependencia.toUpperCase() == correspondencia.firmante.toUpperCase() || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_APROBADA))){ // firmado y es el firmante
						console.log("firmdo si");
						estilo = "display: block";
					} else{
						console.log("rechazar_firmado firmado no");
						estilo = "display: none";
					}
					break;
				case "enviado":
					//console.log("rechazar_firmado enviado");
					estilo = "display: none;";
					break;
				default:
					//console.log("rechazar_firmado default");
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		<!-- fin ticket 9000003996 -->
		
		Handlebars.registerHelper('rechazar', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					estilo = "display: none;";
					break;
				case "firmado":
					if((dependencia == "0" && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA && usuario == correspondencia.firmante)) || (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ASIGNADA && dependencia.toUpperCase() == correspondencia.firmante.toUpperCase())){ // asignado y es el firmante
						estilo = "display: block";
					} else{
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('rechazar_responsable', function(correspondencia){
			var estilo = "display: none;";
			usuario = ref.usuario.val();
			var dependencia = ref.compFiltroDependencia.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					estilo = "display: none;";
					break;
				case "firmado":
					if((dependencia == "0" && (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante != usuario)) || (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toUpperCase() != dependencia.toUpperCase() && dependencia != "0")){
					//if((dependencia == "0" && (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante != ref.usuario.val())) || (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toUpperCase() != dependencia.toUpperCase() && dependencia != "0")){
						console.log("Correspondencia responsable:");
						console.log(correspondencia);
						estilo = "display: block";
					} else{
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('reasignar', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					if(correspondencia.usuario == usuario  && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_PENDIENTE && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA ){ // si es el responsable
						estilo = "display: block";
					} else {
						estilo = "display: none";
					}
					break;
				case "firmado":
					//if(correspondencia.usuario == usuario && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA){ // si es el responsable
					if(dependencia == "0" && correspondencia.usuario == usuario && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA){ // si es el responsable
						estilo = "display: block";
					} else {
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('declinar', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					if(correspondencia.usuario == usuario  && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_PENDIENTE && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA){ // si es el responsable
						estilo = "display: block";
					} else {
						estilo = "display: none";
					}
					break;
				case "firmado":
					//if(correspondencia.usuario == usuario  && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA){ // si es el responsable
					if(dependencia == "0" && correspondencia.usuario == usuario  && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_DECLINADA_FIRMADA && correspondencia.estado.id != CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA){ // si es el responsable
						estilo = "display: block";
					} else {
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('motivo_rechazo', function(correspondencia){
			var estilo = "display: none;";
			usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}
			switch (bandeja) {
				case "pendiente":
					if(correspondencia.usuario == usuario && correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_REINICIO){
						estilo = "display: block";
					}else{
						estilo = "display: none";
					}
					break;
				case "firmado":
					if(correspondencia.usuario == usuario  && (correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_ANULADA || correspondencia.estado.id == CONSTANTES_SISTCORR.CORRESPONDENCIA_POR_CORREGIR)){ // si es el responsable
						estilo = "display: block";
					} else {
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		
		Handlebars.registerHelper('ruta_aprobacion', function(correspondencia){
			var estilo = "display: none;";
			switch (bandeja) {
				case "pendiente":
					if(correspondencia.rutaAprobacion == true){
						estilo = "display: block";
					}else{
						estilo = "display: none";
					}
					break;
				case "firmado":
					if(correspondencia.rutaAprobacion == true){
						estilo = "display: block";
					}else{
						estilo = "display: none";
					}
					break;
				case "enviado":
					estilo = "display: none;";
					break;
				default:
					estilo = "display: none;";
					break;
			}
			return estilo;
		});
		

		
		Handlebars.registerHelper('espera', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			/*usuario = ref.usuario.val();
			if (correspondencia.flgRemplazo == '1'){
		        usuario = correspondencia.userRemplazo;
			}*/
			//if(correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toLowerCase() != ref.usuario.val().toLowerCase()){
			if((dependencia == "0" && (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toLowerCase() != usuario.toLowerCase())) || (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toLowerCase() != dependencia.toLowerCase() && dependencia != "0")){
			//if((dependencia == "0" && (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toLowerCase() != ref.usuario.val().toLowerCase())) || (correspondencia.estadoDescripcion == "Por firmar" && correspondencia.firmante.toLowerCase() != dependencia.toLowerCase() && dependencia != "0")){	
				estilo = "display: ;";
			}
			return estilo;
		});
		
		Handlebars.registerHelper('remplazo', function(correspondencia){
			var estilo = "display: none;";
			var dependencia = ref.compFiltroDependencia.val();
			
			switch (bandeja) {
			case "pendiente":
				estilo = "display: none;";
				break;
			case "firmado":
				if(correspondencia.flgRemplazo =='1'){
					console.log("Correspondencia responsable:");
					console.log(correspondencia);
					estilo = "display: block; color: red;font-size: 0.8rem;";
				} else{
					estilo = "display: none";
				}
				
				break;
			case "enviado":
				estilo = "display: none;";
				break;
			default:
				estilo = "display: none;";
				break;
		}
		return estilo;
		
		});
		
		
		var plantillaScript = ref.plantillaCorrespondencias.html();
		var plantilla =  Handlebars.compile(plantillaScript);
		var contexto = {'correspondencias' : correspondencias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloListaDocumentos.prototype.abrirAsignarFirma = function(idCorrespondencia){
		var ref = this;
	
	};
	
	ModuloListaDocumentos.prototype.rechazarFirma = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var firmante = {
				"motivoRechazo"	: {
					"id" : ref.compRechazarMotivo.val(),
				}, 
				"descripcionMotivoRechazo" : ref.compRechazarMotivoDescripcion.val()
		};
		var correspondencia = ref.compRechazarCorrespondencia.val();
		return $.ajax({
			type	:	'PUT',
			url		:	ref.URL_RECHAZAR_FIRMA + "/" + correspondencia,
			cache	:	false,
			data	:	JSON.stringify(firmante),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	// TICKET 9000003996
	
	ModuloListaDocumentos.prototype.rechazarFirmaResponsable = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var firmante = {
				"motivoRechazo"	: {
					"id" : ref.compRechazarMotivoResponsable.val(),
				}, 
				"descripcionMotivoRechazo" : ref.compRechazarMotivoDescripcionResponsable.val()
		};
		var correspondencia = ref.compRechazarCorrespondenciaResponsable.val();
		return $.ajax({
			type	:	'PUT',
			url		:	ref.URL_RECHAZAR_FIRMA + "/" + correspondencia,
			cache	:	false,
			data	:	JSON.stringify(firmante),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.rechazarFirmaFirmado = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var firmante = {
				"motivoRechazo"	: {
					"id" : ref.compRechazarMotivoFirmado.val(),
				}, 
				"descripcionMotivoRechazo" : ref.compRechazarMotivoDescripcionFirmado.val()
		};
		var correspondencia = ref.compRechazarCorrespondenciaFirmado.val();
		return $.ajax({
			type	:	'PUT',
			url		:	ref.URL_RECHAZAR_FIRMA + "/" + correspondencia,
			cache	:	false,
			data	:	JSON.stringify(firmante),
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	// FIN TICKET
	
	ModuloListaDocumentos.prototype.htmlFiltros = function(filtros){
		var ref = this;
		var _filtros = [];
		if(filtros.usuario && filtros.usuario != "0" && filtros.usuario != ""){
			_filtros.push({'fieldId' : 'usuario', 'value' : filtros.usuario, 'text' : 'Usuario: ' + filtros.usuario});
		}
		if(filtros.correlativo){
			_filtros.push({'fieldId' : 'correlativo', 'value' : filtros.correlativo, 'text' : 'Correlativo: ' + filtros.correlativo});
		}
		if(filtros.asunto){
			_filtros.push({'fieldId' : 'asunto', 'value' : filtros.asunto, 'text' : 'Asunto: ' + filtros.asunto});
		}
		if(filtros.rechazados){
			var text = filtros.rechazados == true ? 'SI' : 'NO';
			_filtros.push({'fieldId' : 'rechazados', 'value' : filtros.rechazados, 'text' : 'Rechazados: ' + text});
		}
		if(filtros.misPendientes){
			var text = filtros.misPendientes == true ? 'SI' : 'NO';
			_filtros.push({'fieldId' : 'misPendientes', 'value' : filtros.misPendientes, 'text' : 'Mis Pendientes: ' + text});
		}
		if(filtros.declinados){
			var text = filtros.declinados == true ? 'SI' : 'NO';
			_filtros.push({'fieldId' : 'declinados', 'value' : filtros.declinados, 'text' : 'Declinados: ' + text});
		}
		if(filtros.fechaInicio){
			_filtros.push({'fieldId' : 'fechaInicio', 'value' : filtros.fechaInicio, 'text' : 'Desde : ' + filtros.fechaInicio});
		}
		if(filtros.fechaFin){
			_filtros.push({'fieldId' : 'fechaFin', 'value' : filtros.fechaFin, 'text' : 'Hasta: ' + filtros.fechaFin});
		}
		//inicio ticket 9000003866
		if(filtros.dependenciaOriginadora && filtros.dependenciaOriginadora != "0"){
			var txtDepOrig = "";
			if($("#textDependenciaDestinoOrg").length > 0){
				var objDependencia = $("#textDependenciaDestinoOrg option[value='" + filtros.dependenciaOriginadora + "']");
				var textDependencia = objDependencia.text();
				txtDepOrig = 'Dependencia Originadora: ' + textDependencia;
			}
			_filtros.push({'fieldId' : 'dependenciaOriginadora', 'value' : filtros.dependenciaOriginadora, 'text' : txtDepOrig});
		}
		if(filtros.tipoCorrespondencia && filtros.tipoCorrespondencia != "0"){
			var txtTipCorres = "";
			if($("#textTipoCorrespondencia").length > 0){
				var objTC = $("#textTipoCorrespondencia option[value='" + filtros.tipoCorrespondencia + "']");
				var txtTipCorres = objTC.text();
				txtTipCorres = 'Tipo Correspondencia: ' + txtTipCorres;
			}
			_filtros.push({'fieldId' : 'tipoCorrespondencia', 'value' : filtros.tipoCorrespondencia, 'text' : '' + txtTipCorres});
		}
		//fin ticket 9000003866
		var plantillaScript = ref.plantillaFiltros.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'filtros' : _filtros};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';	
	};
	
	ModuloListaDocumentos.prototype.abrirNuevoDocumento = function(){
		var ref = this;
		window.location.href = ref.URL_NUEVO_DOCUMENTO;
	};
	
	ModuloListaDocumentos.prototype.enviarCorrespondencia = function(idCorrespondenica){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'PUT',
			url		: 	ref.URL_ENVIAR_CORRESPONDENCIA + '/' + idCorrespondenica,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.reasignarCorrespondencia = function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondenica = ref.compReasignarCorrespondencia.val();
		var nuevoResponsable = ref.compUsuarioReemplazante.val();
		return $.ajax({
			type: "PUT",
			url:	ref.URL_REASIGNAR_CORRESPONDENCIA + '/' + idCorrespondenica + '/' + nuevoResponsable,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.declinarCorrespondencia =  function(){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var idCorrespondenica = ref.compDeclinarCorrespondencia.val();
		return $.ajax({
			type: "PUT",
			url:	ref.URL_DECLINAR_CORRESPONDENCIA + '/' + idCorrespondenica,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.validarNivelFirma = function(correlativos){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'GET',
			url			:	ref.URL_VALIDAR_NIVEL_FIRMA + "/" + correlativos,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.generarZipGrupal = function(correlativos){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type		:	'GET',
			url			:	ref.URL_GENERAR_ZIP_GRUPAL + "/" + correlativos,
			cache		:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloListaDocumentos.prototype.abrirFormularioFirmaGrupal = function(correspondencias){
		var ref = this;
		window.location.href = "../../app/firma-digital-grupal/" + correspondencias;
	};
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloListaDocumentos(SISCORR_APP);
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