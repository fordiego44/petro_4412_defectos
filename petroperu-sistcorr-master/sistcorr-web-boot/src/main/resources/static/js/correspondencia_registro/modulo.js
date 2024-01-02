var MODULO_CORRESPONDENCIA_EDICION = (function(){
	var instance;
	function ModuloCorrespondenciaEdicion(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compBtnGuardar = $("#btnGuardarCorrespondencia");
		this.compModalConfirmarRegistro = $("#modalConfirmarRegistro");
		this.btnConfirmarRegistro = $("#btnConfirmarRegistroSi");
		this.plantillaListaDestinatarioInterno = $("#template-destinatariosInternos");
		this.plantillaListaDestinatarioExterno = $("#template-destinatariosExterno");
		this.plantillaListaArchivosAdjuntos = $("#template-archivosAdjuntos");
		this.plantillaListaCopias = $("#template-copias");
		this.tabs = {
				tabDatos: {
					compHeader: $("#Datos-tab"),
					compBody: $("#contenidoAccordionDatos"),
					accordion: {
						datosRemitente:{
							id: '',
							compHead: $("#btnDatosRemitente"),
							compLugarTrabajo: $("#rmt_lugarTrabajo"),
							compDependencia: $("#rmt_dependencia"),
							compNombre : $("#rmt_nombre"),
							compTipoCorrespondencia: $("#rmt_tipoCorrespondencia"),
							compTipoCorrespondenciaSelected: $("#rmt_tipoCorrespondencia option:selected"),
							compFechaDocumento : $("#rmt_fechaDocumento"),
							compAsunto : $("#rmt_asunto"),
							compJerarquia: $('input[name=corr_jerarquia]'),
						},
						datosCorrespondencia: {
							id: '',
							compHead: $("#btnDatosCorrespondencia"),
							compTipoEmision: $('input:radio[name=corr_tipoEmision]:checked'),
							compDespachoFisico: $('input[name=corr_despachoFisico]'),
							compConfidencial: $('input[name=corr_confidencial]'),
							compUrgente: $('input[name=corr_urgente]'),
							compFirmaDigital: $('input[name=corr_firmaDigital]'),
							compObservacion: $("#corr_observacion"),
						},
						datosArchivos: {
							id: '',
							compHead: $("#btnDatosArchivos"),
							compFormAdjuntar: $("#frmAdjuntarArchivo"),
							compFile: $("#arc_file"),
							compPrincipal: $("#arc_principal"),
							compListaArchivos: $("#listaArchivosAdjuntos")
							
						}
					}
				},
				tabDestinatario: {
					compHeader: $("#Destinatario-tab"),
					compBody: $("#contenidoAccordionDestinatario"),
					compInterno: $("#destinatarioInternos"),
					compExterno: $("#destinatarioExternos"),
					internos: {
						compLugarTrabajo: $("#destInterno_lugarTrabajo"),
						compDependencia: $("#destInterno_dependencia"),
						compBtnAgregarDest: $("#destInterno_btnAgregar"),
						compListaDestinatarios: $("#listaDestinatariosInternos")
					},
					externos: {
						compTipo: $("input:radio[name=desExterno_tipo]"),
						compPais: $("#destExterno_pais"),
						compDepartamento: $("#destExterno_departamento"),
						compProvincia: $("#destExterno_provincia"),
						compDistrito: $("#destExterno_distrito"),
						compDireccion: $("#destExterno_direccion"),
						compDependenciaNac: $("#destExterno_dependenciaNacional"),
						compDependenciaInter: $("#destExterno_dependenciaInternacional"),
						compNombreDestinatario: $("#destExterno_nombreDestinatario"),
						compBtnAgregarDest: $("#destExterno_btnAgregar"),
						compListaDestinatarios: $("#listaDestinatariosExterno")
					}
				},
				tabCopias: {
					compHeader: $("#Copias-tab"),
					compBody: $("#contenidoAccordionCopias"),
					compLugarTrabajo: $("#copia_lugarTrabajo"),
					compDependencia: $("#copia_dependencia"),
					compBtnAgregar: $("#copia_btnAgregar"),
					compListaCopias: $("#listaCopias")
				}
		};
		this.URL_BUSCAR_LUGARES = '../app/emision/buscar/lugar';
		this.URL_BUSCAR_DEPENDENCIAS = '../app/emision/buscar/dependencia';
		this.URL_BUSCAR_PAISES = '../app/emision/buscar/pais';
		this.URL_BUSCAR_DEPARTAMENTOS = '../app/emision/buscar/departamento';
		this.URL_BUSCAR_PROVINCIAS = '../app/emision/buscar/provincia';
		this.URL_BUSCAR_DISTRITOS = '../app/emision/buscar/distrito';
		this.URL_BUSCAR_DEPEN_EXTERNA = '../app/emision/buscar/dependencia_externa';
		this.URL_BUSCAR_TIPO_CORRESPONDENCIA = '../app/emision/buscar/tipo_correspondencia';
		this.URL_OBTENER_FIRMANTE = '../app/emision/obtener/funcionario_jefe';
		this.URL_REGISTRAR_CORRESPONDENCIA = '../app/emision/registrar/correspondencia';
		this.URL_REGISTRAR_ARCHIVO_ADJUNTO = '../app/emision/registrar/archivo-adjunto';
	}
	
	ModuloCorrespondenciaEdicion.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	ModuloCorrespondenciaEdicion.prototype.obtenerFirmante = function(codDependencia){
		var ref = this;
		if(!codDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_FIRMANTE + '/' + codDependencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.registrarCorrespondencia = function(correspondencia, adjuntos){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		var fd = new FormData();
		console.log("Correspondencia Registro:");
		console.log(correspondencia);
		fd.append('correspondencia', new Blob([JSON.stringify(correspondencia)], {
	        type: "application/json"
	    }));
		for(var i in adjuntos){
			var adj = adjuntos[i];
			principales.push(adj.principal);
			fd.append('archivos', adj.file);
		}
		fd.append('rutaAprobacion', new Blob([JSON.stringify(rutaAprobacion)], {
			type: "application/json"
		}));
		fd.append('principales', new Blob([JSON.stringify(principales)], {
	        type: "application/json"
	    }));
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_REGISTRAR_CORRESPONDENCIA,
			cache	:	false,
			data	:	fd,
			processData: false,
	        contentType: false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaDestinatariosInternos = function(destinatarios){
		var ref = this;
		var plantillaScript = ref.plantillaListaDestinatarioInterno.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'destinatarios' : destinatarios};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaDestinatariosExternos = function(destinatarios){
		var ref = this;
		var plantillaScript = ref.plantillaListaDestinatarioExterno.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'destinatarios' : destinatarios};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaArchivosAdjuntos = function(adjuntos){
		var ref = this;
		var plantillaScript = ref.plantillaListaArchivosAdjuntos.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'adjuntos' : adjuntos};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaCopias = function(copias){
		var ref = this;
		var plantillaScript = ref.plantillaListaCopias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'copias' : copias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.abrirEdicion = function(correspondencia){
		var ref = this;
		window.location.href = '../app/edicion/' + correspondencia.idCorrespondencia;
	};
	
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloCorrespondenciaEdicion(SISCORR_APP);
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