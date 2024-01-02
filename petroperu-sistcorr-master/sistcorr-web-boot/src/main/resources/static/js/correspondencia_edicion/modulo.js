var MODULO_CORRESPONDENCIA_EDICION = (function(){
	var instance;
	function ModuloCorrespondenciaEdicion(SISCORR_APP){
		this.SISCORR_APP = SISCORR_APP;
		this.modSistcorr = null;
		this.SISCORR_AUTHOR = 'Kenyo';
		this.csrfToken = $("#csrfToken");
		this.csrfHeader = $("#csrfHeader");
		this.idCorrespondencia = $("#main_correspondencia");
		this.btnAbrirmenu = $("#btnAbrirMenu");
		this.compBtnGuardar = $("#btnGuardarCorrespondencia");
		this.compBtnCancelar = $("#btnCancelarCorrespondencia");
		this.compModalConfirmarRegistro = $("#modalConfirmarRegistro");
		this.btnConfirmarRegistro = $("#btnConfirmarRegistroSi");
		this.compModalConfirmarAsignarFirma = $("#modalConfirmarAsignarFirma");
		this.compModalConfirmarAsignarRutaAprobacion = $("#modalConfirmarInicioRutaAprobacion");
		this.btnConfirmarAsignarFirmaSi = $("#btnConfirmarAsignarFirmaSi");
		this.btnConfirmarAsignarFirmaNo = $("#btnConfirmarAsignarFirmaNo");
		this.compModalSeleccionarFirmante = $("#modalSeleccionarFirmante");
		this.compFirmante = $("#firmante");
		this.btnSeleccionarFirmanteAceptar = $("#btnSeleccionarFirmanteAceptar");
		this.btnSeleccionarFirmanteCancelar = $("#btnSeleccionarFirmanteCancelar");
		this.btnSeleccionarFirmanteRutaAceptar = $("#btnConfirmarInicioRutaAprobacionSi");
		this.btnSeleccionarFirmanteRutaCancelar = $("#btnConfirmarInicioRutaAprobacionNo");
		this.classDescargarArchivo = "btnDescargarArchivo";
		this.plantillaListaDestinatarioInterno = $("#template-destinatariosInternos");
		this.plantillaListaDestinatarioExterno = $("#template-destinatariosExterno");
		this.plantillaListaArchivosAdjuntos = $("#template-archivosAdjuntos");
		this.plantillaListaCopias = $("#template-copias");
		this.allowOnlyNumbersInput = $(".allow_only_numbers");
		this.codigoDocPagar = "81";//ticket 9000004765
		this.componentes = {/*TICKET 9000003934*/
				dataTable: $("#tablaSearchEntidadesExternasSunat"),	/*INICIO TICKET 9000003934*/
		},/*INICIO TICKET 9000003934*/
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
							compBtnFechaDocumento: $("#btnRmt_FechaDocumento"),
							compTipoCorrespondenciaOtros : $("#rmt_tipoCorrespondenciaOtros"),
							compAsunto : $("#rmt_asunto"),
							compTextoLugarTrabajo: $("#textoLugarTrabajo"),
							compTextoJefeDependencia: $("#textoJefeDependenciaRemitente"),
							compDependenciaOriginadora: $("#rmt_dependencia_originadora"),
							compJerarquia: $("input[name=corr_jerarquia]"),
							compRutaAprobacion: $("input[name=corr_rutaAprobacion]:checked"),
							// TICKET 9000004044
							compEsDocumentoRespuestaTexto: $("#rmt_esDocumentoRespuestaTexto"),
							compEsDocumentoRespuesta: $("#rmt_esDocumentoRespuesta"),
							compCorrelativoEntrada: $("#rmt_correlativoEntrada"),
							compTipoAccion: $("#rmt_tipoAccion"),
							compRespuesta: $("#rmt_respuesta"),
							compIdAsignacion: $("#rmt_idAsignacion")
							// FIN TICKET
						},
						datosCorrespondencia: {
							id: '',
							compHead: $("#btnDatosCorrespondencia"),
							compTipoEmision: $('input:radio[name=corr_tipoEmision]:checked'),
							compTipoEmisionD: $('input:radio[name=corr_tipoEmision]'),
							compDespachoFisico: $('input[name=corr_despachoFisico]'),
							compConfidencial: $('input[name=corr_confidencial]'),
							compUrgente: $('input[name=corr_urgente]'),
							compFirmaDigital: $('input:radio[name=corr_firmaDigital]:checked'),
							//compFirmaDigital: $('input[name=corr_firmaDigital]'),
							compPrimerFirmante: $('input[name=corr_primerFirmante]'),
							compObservacion: $("#corr_observacion"),
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
				},
				tabDestinatario: {
					compHeader: $("#Destinatario-tab"),
					compBody: $("#contenidoAccordionDestinatario"),
					compInterno: $("#destinatarioInternos"),
					compExterno: $("#destinatarioExternos"),
					compInternoMultiple: $("#destinatarioInternoMultiple"),
					compDestDocPagar: $("#destinatarioDocPagar"),//ticket 9000004765
					internos: {
						compLugarTrabajo: $("#destInterno_lugarTrabajo"),
						compDependencia: $("#destInterno_dependencia"),
						compDependencias2: $("#destInterno_dependencias2"),
						compDependencias3: $("#destInterno_dependencias3"),
						compBtnAgregarDest: $("#destInterno_btnAgregar"),
						compBtnCancelarAgregar: $("#btnCancelarAgregarDestInt"),
						compListaDestinatarios: $("#listaDestinatariosInternos"),
						compBtnOpenModal: $("#btnOpenModalDI"),
						compModal: $("#modalDestinatarioInterno"),
						compDestinatarioDocPagar: $("input:radio[name=destDocPagar]"),//TICKET 9000004765
						compBtnCloseModal: $("#btnCloseDestinatarioInterno")
					},
					externos: {
						compTipo: $("input:radio[name=desExterno_tipo]"),
						compPais: $("#destExterno_pais"),
						compDepartamento: $("#destExterno_departamento"),
						compProvincia: $("#destExterno_provincia"),
						compDistrito: $("#destExterno_distrito"),
						compDireccion: $("#destExterno_direccion"),
						compDependenciaNac: $("#destExterno_dependenciaNacional"),
						
						compBtnDependenciaNacRUCRazonSocialSunat: $("#btnSearchRZRUCEntidadExterna"),//TICKET 9000003934
						compRZDependenciaNacSunat: $("#searchRZRUCEntidadExterna"),//TICKET 9000003934
						compCodDependenciaNacRUCRazonSocialSunat: $("#codEntidadExternaPorRuc"),//TICKET 9000003934
						compIndicadorEntidadExternRucRZ: $("#entidadExternaRegistradaSunat"),//TICKET 9000003934
						
						compDependenciaInter: $("#destExterno_dependenciaInternacional"),
						compNombreDestinatario: $("#destExterno_nombreDestinatario"),
						
						compTipoDestinatario: $("input:radio[name=tipEntidadDestinatarioExterno]"),//TICKET 9000003934
						compDespFisicoExterno: $("#desFisicoExterno"),//TICKET 9000003934
						compDespCorreoElectronico: $("#desCorreoElectronicoExterno"),//TICKET 9000003934
						compDespCorreoDestino: $("#destExterno_correoDestinatario"),//TICKET 9000003934
						idDespFisicoExterno: "desFisicoExterno",//TICKET 9000003934
						idDespCorreoElectronico: "desCorreoElectronicoExterno",//TICKET 9000003934
						compDespCorreoElectronicoAndDespFisico: $("#desFisicoExterno,#desCorreoElectronicoExterno"),//TICKET 9000003934
						compBtnModalSearchRZRUCEntidadExterna: $("#btnModalSearchRZRUCEntidadExterna"),//TICKET 9000003934
						compSearchModalRZRUCEntidadExterna: $("#txtSearchModalRZRUCEntidadExterna"),//TICKET 9000003934
						compTipoBuscarEntidadExternaNacionalSunat: $("input:radio[name=searchEntidadExternaPorSunat]"),//TICKET 9000003934
						compSeleccionarEntidadExternaNacional: $("input:radio[name=seleccionarEntidadExternaNacional]"),//TICKET 9000003934
						
						compBtnAgregarDest: $("#destExterno_btnAgregar"),
						compBtnCancelarAgregar: $("#btnCancelarAgregarDestExt"),
						compListaDestinatarios: $("#listaDestinatariosExterno"),
						compBtnOpenModal: $("#btnOpenModalDE"),
						compModal: $("#modalDestinatarioExterno"),
						
						compModalSearchRucRazonSocialEntidadExterna: $("#modalSearchRucRazonSocialEntidadExterna"),
						
						compBtnCloseModal: $("#btnCloseDestinatarioExterno")
					},
					internosMultiple: {
						compCentro: $("#destInternoMultiple_centro"),
						compDependencia: $("#destInternoMultiple_dependencia"),
						compDependencias2: $("#desInternoMultiple_dependencias2"),
						compDependencias3: $("#desInternoMultiple_dependencias3"),
						compModal: $("#modalDestinatarioInternoMultiple"),
						compBtnOpenModal: $("#btnOpenModalDIM"),
						compBtnCloseModal: $("#btnCloseDestinatarioInternoMultiple")
					}
				},
				tabCopias: {
					compHeader: $("#Copias-tab"),
					compBody: $("#contenidoAccordionCopias"),
					compLugarTrabajo: $("#copia_lugarTrabajo"),
					compDependencia: $("#copia_dependencia"),
					compBtnAgregar: $("#copia_btnAgregar"),
					compBtnCancelarAgregar: $("#btnCancarlarAgregarCopia"),
					compListaCopias: $("#listaCopias"),
					compModal: $("#modalCopias"),
					compBtnCloseModal: $("#btnCloseCopias")
				},
				tabRutaAprobacion: {
					compHeader: $("#RutaAprobacion-tab"),
					compBody: $("#contenidoRutaAprobacion"),
					compBtnModalAgregarFirm: $("#btnOpenModalRutaAprobacion"),
					compTblFirmantes: $("#tblFirmantes"),
					compTipoFirmanteJefe: $("#firmanteTipo_jefe"),
					compTipoFirmantePersona: $("#firmanteTipo_persona"),
					compDependencia: $("#firmante_dependencia"),
					compPersona: $("#firmante_nombre"),
					compBtnAgregarFirm : $("#firmante_btnAgregar"),
					compBtnCloseModal: $("#btnCancelarAgregarFirmante")
				},
				tabFlujo: {
					
				}
		};
		this.classBtnEliminarAdjunto = "btnEliminarAdjunto";
		this.classBtnEliminarDestinatarioInterno = "btnEliminarDestinatarioInterno";
		this.classBtnEliminarDestinatarioExterno = "btnEliminarDestinatarioExterno";
		this.classBtnEliminarCopia = "btnEliminarCopia";
		this.compCerrarSession = $(".closeSession");
		this.nameRutaAprobacion = "corr_rutaAprobacion";
		this.nameFirmanteTipo = "firmante_tipo";
		this.URL_BUSCAR_DEPENDENCIAS_X_USUARIO = ['../app/emision/buscar/dependencia-usuario', '../../app/emision/buscar/dependencia-usuario'];
		this.URL_BUSCAR_LUGAR_X_DEPENDENCIA = ['../app/emision/buscar/lugar-dependencia', '../../app/emision/buscar/lugar-dependencia'];
		this.URL_BUSCAR_LUGARES = ['../app/emision/buscar/lugar', '../../app/emision/buscar/lugar'];
		this.URL_BUSCAR_DEPENDENCIAS = ['../app/emision/buscar/dependencia', '../../app/emision/buscar/dependencia'];
		// TICKET 9000003944
		this.URL_BUSCAR_DEPENDENCIAS_UM = ['../app/emision/buscar/dependenciaUM', '../../app/emision/buscar/dependenciaUM'];
		// FIN TICKET
		this.URL_BUSCAR_PAISES = ['../app/emision/buscar/pais', '../../app/emision/buscar/pais'];
		this.URL_BUSCAR_DEPARTAMENTOS = ['../app/emision/buscar/departamento', '../../app/emision/buscar/departamento'];
		this.URL_BUSCAR_PROVINCIAS = [ '../app/emision/buscar/provincia', '../../app/emision/buscar/provincia'];
		this.URL_BUSCAR_DISTRITOS = ['../app/emision/buscar/distrito', '../../app/emision/buscar/distrito'];
		this.URL_BUSCAR_DEPEN_EXTERNA = ['../app/emision/buscar/dependencia_externa', '../../app/emision/buscar/dependencia_externa'];
		this.URL_BUSCAR_TIPO_CORRESPONDENCIA = ['../app/emision/buscar/tipo_correspondencia', '../../app/emision/buscar/tipo_correspondencia'];
		this.URL_OBTENER_FIRMANTE = ['../app/emision/obtener/funcionario_jefe', '../../app/emision/obtener/funcionario_jefe'];
		this.URL_OBTENER_FIRMANTE_RUTA_APROBACION = ['../app/emision/obtener/funcionario_jefe/ruta_aprobacion', '../../app/emision/obtener/funcionario_jefe/ruta_aprobacion'];
		this.URL_REGISTRAR_CORRESPONDENCIA = '../app/emision/registrar/correspondencia';
		this.URL_MODIFICAR_CORRESPONDENCIA = '../../app/emision/modificar/correspondencia';
		this.URL_MODIFICAR_COMPLETO_CORRESPONDENCIA = '../../app/emision/modificar-completo/correspondencia';
		this.URL_OBTENER_CORRESPONDENCIA = '../../app/emision/buscar/correspondencia';
		this.URL_OBTENER_RUTA_APROBACION = '../../app/emision/obtener/ruta_aprobacion';
		this.URL_ASIGNAR_FIRMA = ['../app/asignacion-firma', '../../app/asignacion-firma'];
		this.URL_BANADEJA_PENDIENTE = ['../app/lista-documentos/pendientes', '../../app/lista-documentos/pendientes'];
		this.URL_BANADEJA_FIRMADOS = ['../app/lista-documentos/firmados', '../../app/lista-documentos/firmados'];
		this.URL_ASIGNAR_FIRMANTE_AUTOMATICO = ["../app/emision/asignar-firmante-automatico", "../../app/emision/asignar-firmante-automatico"];
		this.URL_ASIGNAR_FIRMANTE_RUTA_APROBACION = ["../app/emision/asignar-firmante-ruta-aprobacion", "../../app/emision/asignar-firmante-ruta-aprobacion"];
		this.URL_DESCARGAR_ARCHIVO_ADJUNTO = ['../app/emision/descargar-archivo', '../../app/emision/descargar-archivo'];
		this.URL_BUSCAR_DEPENDENCIAS_SUPERIORES = ['../app/emision/buscar/dependencias-superiores', '../../app/emision/buscar/dependencias-superiores'];
		this.URL_BUSCAR_DEPENDENCIAS_SUBORDINADAS = ['../app/emision/buscar/dependencias-subordinadas', '../../app/emision/buscar/dependencias-subordinadas'];
		this.URL_MODIFICAR_REMITENTE_CORRESPONDENCIA = '../../app/emision/modificar/remitenteCorrespondencia';
		this.URL_BUSCAR_DEPENDENCIAS_TODAS = ['../app/emision/buscar/dependencias-todas-nuevo','../../app/emision/buscar/dependencias-todas-nuevo'];
		this.URL_BUSCAR_PERSONAS_TODAS = ['../app/buscar/personas-todas','../../app/buscar/personas-todas'];
		this.URL_BUSCAR_DEPENDENCIAS_NIVEL = ['../app/listar-dependencias-nivel', '../../app/listar-dependencias-nivel'];
		this.URL_LISTAR_DEPENDENCIAS_NIVEL = '../app/listar-dependencias-nivel';
		this.URL_BUSCAR_ENTIDADES_EXTERNA_SUNAT = ''+((EDICION)?('../../'):('../'))+'app/correspondencias/consultar/EntidadesExternaNacionalSunat';
		this.URL_ACTUALIZAR_CORREO_DEST_EXTERNO = '../../app/correspondencias/actualizar/correoDestinatarioExterno';
		
		//INICIO TICKET 9000003934
		this.tipoCombByConfDespaTipoEnvio = ['EE_DF_EN','EE_CE_EN','EE_DF_CE_EN','EE_EN','PN_DF_EN','PN_CE_EN',
			  'PN_DF_CE_EN','PN_EN','EE_DF_EI','EE_CE_EI','EE_DF_CE_EI','EE_EI',
			  'PN_DF_EI','PN_CE_EI','PN_DF_CE_EI','PN_EI'];
		this.propertiesCombByConfDespaTipoEnvio = ['accion','show','required','maxLength','idField','typeElement',
			  'isGuardarValueAndText','fieldTextBD','fieldBD','valueDefault','numParent','md','isValidate','msgValidate','nameListBusByCode'];
		this.fieldByConfDespaTipoEnvio = ['entidadExternaRegistradaSunat','destExterno_departamento','destExterno_provincia','destExterno_distrito','destExterno_direccion',
									'destExterno_nombreDestinatario','btnSearchRZRUCEntidadExterna','destExterno_dependenciaNacional','destExterno_correoDestinatario','destExterno_pais',
									'destExterno_dependenciaInternacional'];
		//INDEX del nombre ID campo
		//0 = entidadExternaRegistradaSunat; 1 = destExterno_departamento; 2 = destExterno_provincia; 3 = destExterno_distrito; 4 = destExterno_direccion; 5 = destExterno_nombreDestinatario
		//6 = btnSearchRZRUCEntidadExterna; 7 = destExterno_dependenciaNacional; 8 = destExterno_correoDestinatario; 9 = destExterno_pais; 10 = destExterno_dependenciaInternacional
		
		//INDEX de las propiedades definidas
		//0 = accion; 1 = show; 2 = required; 3 = maxLength; 4 = idField; 5 = typeElement; 6 = isGuardarValueAndText; 7 = fieldTextBD; 8 = fieldBD; 9 = valueDefault; 10 = numParent
		//11 = md; 12 = isValidate; 13 = msgValidate; 14 = nameListBusByCode
		this.showHideFields = {};
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[0]] = {"show": [0,1,2,3,4,5,6,7], "1": {"md": "6","vl": 1}, "2": {"md": "6","vl": 1}, "3": {"md": "6","vl": 1}, "4": {"md": "6","vl": 1}, "5": {"md": "12","vl": 1}};//EE_DF_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[1]] = {"show": [0,5,6,7,8], "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//EE_CE_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[2]] = {"show": [0,1,2,3,4,5,6,7,8], "1": {"md": "6","vl": 1}, "2": {"md": "6","vl": 1}, "3": {"md": "6","vl": 1}, "4": {"md": "6","vl": 1}, "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//EE_DF_CE_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[3]] = {"show": [0,5,6,7], "5": {"md": "12","vl": 1}};//EE_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[4]] = {"show": [1,2,3,4,5], "1": {"md": "6","vl": 1}, "2": {"md": "6","vl": 1}, "3": {"md": "6","vl": 1}, "4": {"md": "6","vl": 1}, "5": {"md": "12","vl": 1}};//PN_DF_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[5]] = {"show": [5,8], "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//PN_CE_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[6]] = {"show": [1,2,3,4,5,8], "1": {"md": "6","vl": 1}, "2": {"md": "6","vl": 1}, "3": {"md": "6","vl": 1}, "4": {"md": "6","vl": 1}, "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//PN_DF_CE_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[7]] = {"show": [5], "5": {"md": "12","vl": 1}};//PN_EN
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[8]] = {"show": [4,5,9,10], "4": {"md": "12","vl": 1}, "5": {"md": "12","vl": 1}, "10": {"vl": 1}};//EE_DF_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[9]] = {"show": [5,8,10], "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}, "10": {"vl": 1}};//EE_CE_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[10]] = {"show": [4,5,8,9,10], "4": {"md": "12","vl": 1}, "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}, "10": {"vl": 1}};//EE_DF_CE_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[11]] = {"show": [5,10], "5": {"md": "12","vl": 1}, "10": {"vl": 1}};//EE_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[12]] = {"show": [4,5,9], "4": {"md": "12","vl": 1}, "5": {"md": "12","vl": 1}};//PN_DF_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[13]] = {"show": [5,8], "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//PN_CE_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[14]] = {"show": [4,5,8,9], "4": {"md": "12","vl": 1}, "5": {"md": "6","vl": 1}, "8": {"md": "6","vl": 1}};//PN_DF_CE_EI
		this.showHideFields[this.tipoCombByConfDespaTipoEnvio[15]] = {"show": [5], "5": {"md": "12","vl": 1}};//PN_EI
		
		this.propertiesFieldBD = {};
		this.propertiesFieldBD["0"] = {"3": "", "4": "entidadExternaRegistradaSunat", "5": "checkbox", "6": false, "7": "", "8": "esEntidadExternaConRuc", "9": null, "10": 2, "13": "", "14": ""};
		this.propertiesFieldBD["1"] = {"3": "255", "4": "destExterno_departamento", "5": "select", "6": true, "7": "departamento", "8": "codDepartamento", "9": null, "10": 1, "13": "Seleccione departamento", "14": "departamento"};
		this.propertiesFieldBD["2"] = {"3": "255", "4": "destExterno_provincia", "5": "select", "6": true, "7": "provincia", "8": "codProvincia", "9": null, "10": 1, "13": "Seleccione provincia", "14": "provincia"};
		this.propertiesFieldBD["3"] = {"3": "255", "4": "destExterno_distrito", "5": "select", "6": true, "7": "distrito", "8": "codDistrito", "9": null, "10": 1, "13": "Seleccione distrito", "14": "distrito"};
		this.propertiesFieldBD["4"] = {"3": "255", "4": "destExterno_direccion", "5": "text", "6": false, "7": "", "8": "direccion", "9": null, "10": 1, "13": "Ingrese la direcci&oacute;n", "14": ""};
		this.propertiesFieldBD["5"] = {"3": "255", "4": "destExterno_nombreDestinatario", "5": "text", "6": false, "7": "", "8": "nombreDestinatario", "9": null, "10": 1, "13": "Ingrese nombre destinatario", "14": ""};
		this.propertiesFieldBD["6"] = {"3": "", "4": "btnSearchRZRUCEntidadExterna", "5": "icon", "6": false, "7": "", "8": "", "9": null, "10": 2, "13": "", "14": ""};
		this.propertiesFieldBD["7"] = {"3": "255", "4": "destExterno_dependenciaNacional", "5": "select", "6": true, "7": "dependenciaNacional", "8": "codDependenciaNacional", "9": null, "10": 2, "13": "", "14": ""};
		this.propertiesFieldBD["8"] = {"3": "255", "4": "destExterno_correoDestinatario", "5": "text", "6": false, "7": "", "8": "correoDestinatario", "9": null, "10": 1, "13": "Ingrese correo destinatario", "14": ""};
		this.propertiesFieldBD["9"] = {"3": "255", "4": "destExterno_pais", "5": "select", "6": true, "7": "pais", "8": "codPais", "9": null, "10": 2, "13": "Seleccione pais", "14": "pais"};
		this.propertiesFieldBD["10"] = {"3": "255", "4": "destExterno_dependenciaInternacional", "5": "text", "6": false, "7": "pais", "8": "dependenciaInternacional", "9": null, "10": 2, "13": "Ingrese dependencia", "14": ""};
		
		this.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO = {};
		
		var nombTipo = "", accion = "";
		for(var it = 0; it < this.tipoCombByConfDespaTipoEnvio.length; it++){
			nombTipo = this.tipoCombByConfDespaTipoEnvio[it];
			this.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO[nombTipo] = [];
			for(var ip = 0; ip < this.fieldByConfDespaTipoEnvio.length; ip++){
				if (navigator.userAgent.match(/msie/i) || navigator.userAgent.match(/trident/i) ) {
					accion = ((this.showHideFields[nombTipo].show.indexOf(ip) != -1)?("show"):("hide"));
				}else{
					accion = ((this.showHideFields[nombTipo].show.includes(ip))?("show"):("hide"));
				}
				var jsonPropertiesField = {};
				for(var ipt = 0; ipt < this.propertiesCombByConfDespaTipoEnvio.length; ipt++){
					if(this.propertiesCombByConfDespaTipoEnvio[ipt] == "accion")
						jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = accion;
					else if(this.propertiesCombByConfDespaTipoEnvio[ipt] == "md"){
						if(this.showHideFields[nombTipo].hasOwnProperty(''+ip) && this.showHideFields[nombTipo][""+ip].hasOwnProperty('md'))
							jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = this.showHideFields[nombTipo][""+ip].md;
					}else if(this.propertiesCombByConfDespaTipoEnvio[ipt] == "isValidate"){
						if(this.showHideFields[nombTipo].hasOwnProperty(''+ip) && this.showHideFields[nombTipo][""+ip].hasOwnProperty('vl'))
							jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = this.showHideFields[nombTipo][""+ip].vl;
					}
					else if(this.propertiesCombByConfDespaTipoEnvio[ipt] == "required"){
						if (navigator.userAgent.match(/msie/i) || navigator.userAgent.match(/trident/i) ) {
							jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = (this.showHideFields[nombTipo].show.indexOf(ip) != -1);
						}else{
							jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = (this.showHideFields[nombTipo].show.includes(ip));
						}
					}
					else jsonPropertiesField[""+this.propertiesCombByConfDespaTipoEnvio[ipt]] = this.propertiesFieldBD[""+ip][""+ipt];
				}
				this.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO[nombTipo].push(jsonPropertiesField);
			}
		}
		//console.log("CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO: " + JSON.stringify(this.CONFIG_BY_TIPODESTINATARIO_CONFDESPACHO_TIPOENVIO));
		//FIN TICKET 9000003934
	}
	
	ModuloCorrespondenciaEdicion.prototype.abrirMenu = function(){
		var ref = this;
		ref.btnAbrirmenu.click();
	};
	
	/*INICIO TICKET 9000003934*/
	ModuloCorrespondenciaEdicion.prototype.irADetalle = function(id){
		var ref = this;
		var url = window.location.origin;
		var path = window.location.pathname.trim().split("/");
		for(var i = 1; i <= path.length ; i++){ 
			if(path[i] == 'app'){
				break;
			}else{
				url = url + "/" + path[i];
			}
		}
		url = url + "/app/ver-detalle/" + id;
		window.location.replace(url);
	};
	/*FIN TICKET 9000003934*/
	ModuloCorrespondenciaEdicion.prototype.obtenerFirmante = function(indiceURL, codDependencia){
		var ref = this;
		if(!codDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_FIRMANTE[indiceURL] + '/' + codDependencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.obtenerFirmanteRutaAprobacion = function(indiceURL, codDependencia){
		var ref = this;
		if(!codDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_FIRMANTE_RUTA_APROBACION[indiceURL] + '/' + codDependencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.listarDependenciasSuperiores = function(codigoDependencia, indiceURL){
		var ref = this;
		var ref = this;
		if(!codigoDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var datos = {codDependencia: codigoDependencia}
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_BUSCAR_DEPENDENCIAS_SUPERIORES[indiceURL],
			data	:	datos,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	
	ModuloCorrespondenciaEdicion.prototype.listarDependenciasSubordinadas = function(codDependencia){
		var ref = this;
		var ref = this;
		if(!codDependencia)
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_BUSCAR_DEPENDENCIAS_SUBORDINADAS[indiceURL] + '/' + codDependencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.modificarRemitenteCorrespondencia = function(idCorrespondencia, codRemitente){
		var ref = this;
		if(idCorrespondencia == 0 || codRemitente == "")
			return;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_MODIFICAR_REMITENTE_CORRESPONDENCIA + '/' + idCorrespondencia + "/" + codRemitente,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.buscarEntidadExternaNacionalSunat = function(txtRucRazonSocial, tipo){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		
		return $.ajax({
			type	:	'POST',
			url		: 	ref.URL_BUSCAR_ENTIDADES_EXTERNA_SUNAT+"?rucRazonSocial="+txtRucRazonSocial+"&tipo=" + tipo,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        
		        xhr.setRequestHeader(header, token);
		    }
		});
	},
	
	ModuloCorrespondenciaEdicion.prototype.registrarCorrespondencia = function(correspondencia, adjuntos, rutaAprobacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		var aprobadores = [];
		console.log("Correspondencia Edicion:");
		console.log(correspondencia);
		var fd = new FormData();
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
		console.log("Correspondencia:");
		console.log(correspondencia);
		console.log("Ruta Aprobacion:");
		console.log(rutaAprobacion);
		console.log("Principales:");
		console.log(principales);
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
	
	ModuloCorrespondenciaEdicion.prototype.modificarCorrespondencia = function(correspondencia, adjuntos, rutaAprobacion){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		var principales = [];
		var fd = new FormData();
		
		for(var i in adjuntos){
			var adj = adjuntos[i];
			if(adj.file){
				principales.push(adj.principal);
				fd.append('archivos', adj.file);	
			}else{
				correspondencia.adjuntos.push({"id": adj.id,"principal": adj.principal});
			}
		}
		fd.append('principales', new Blob([JSON.stringify(principales)], {
	        type: "application/json"
	    }));
		fd.append('rutaAprobacion', new Blob([JSON.stringify(rutaAprobacion)], {
			type: "application/json"
		}));
		
		fd.append('correspondencia', new Blob([JSON.stringify(correspondencia)], {
	        type: "application/json"
	    }));
		
		if(principales.length > 0){
			return $.ajax({
				type	:	'POST',
				url		: 	ref.URL_MODIFICAR_COMPLETO_CORRESPONDENCIA,
				cache	:	false,
				data	:	fd,
				processData: false,
		        contentType: false,
				beforeSend: function(xhr) {
			        xhr.setRequestHeader(header, token);
			    }
			});
		} else{
			correspondencia.aprobadores = rutaAprobacion;
			/*var fd = new FormData();
			fd.append('correspondencia', new Blob([JSON.stringify(correspondencia)]))*/
			return $.ajax({
				type	:	'PUT',
				url		: 	ref.URL_MODIFICAR_CORRESPONDENCIA,
				cache	:	false,
				data	:	JSON.stringify(correspondencia),
				beforeSend: function(xhr) {
			        xhr.setRequestHeader("Accept", "application/json");
			        xhr.setRequestHeader("Content-Type", "application/json");
			        xhr.setRequestHeader(header, token);
			    }
			});
		}
		
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
		Handlebars.registerHelper('esMostrarEditCorreoElectr', function(correspondenciaExterno){
			var estilo = "display: none;";
			
			if(correspondenciaExterno.esCorreoElectronicoDestExterno)
				estilo = "display: ;cursor: pointer;";
			
            return estilo;
        });
		Handlebars.registerHelper('eq', function () {
		    const args = Array.prototype.slice.call(arguments, 0, -1);
		    return args.every(function (expression) {
		        return args[0] === expression;
		    });
		});
		var plantillaScript = ref.plantillaListaDestinatarioExterno.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'destinatarios' : destinatarios};
		var contenidoHTML = plantilla(contexto);

		$(document).on('mousedown', '.iconEditarCorreoDestinatario', function(event) {
			event.preventDefault();
		}).on('click', '.iconEditarCorreoDestinatario', function(e) {
			var identAttr = ($(this).attr("id")).split("_")[1];
			$("#divCorreoDestinatarioEditar_" + identAttr).show();
			$("#divCorreoDestinatarioReadOnly_" + identAttr).hide();
			$("#correoDestinatarioEditar_" + identAttr).focus();
		});
		
		$(document).on('blur','.correoDestinatarioEditar',function(e){
			var thisKY = this;
			var identAttr = ($(thisKY).attr("id")).split("_")[1];
			$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
			$("#divCorreoDestinatarioEditar_" + identAttr).hide();
		});
		
		$(document).on('keyup','.correoDestinatarioEditar',function(e){
			var code = (e.keyCode ? e.keyCode : e.which);
			var thisKY = this;
			var identAttr = ($(thisKY).attr("id")).split("_")[1];
		    if (code === 13) {//ENTER
		    	var identificadorDX = identAttr;
		    	var correoDestinDX = $(thisKY).val();
		    	
		    	$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
				$("#divCorreoDestinatarioEditar_" + identAttr).hide();
				$(thisKY).val(correoDestinDX);
				$("#spanCorreoDestinatarioTexto_" + identAttr).text(correoDestinDX);
				
		    }else if(code === 27){//ESC
		    	$("#divCorreoDestinatarioReadOnly_" + identAttr).show();
				$("#divCorreoDestinatarioEditar_" + identAttr).hide();
		    }
		});
		
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaArchivosAdjuntos = function(adjuntos){
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
			if(adjunto.tipo == "PRINCIPAL"){
				return "display:block;";
			}else{
				return "display:none;";
			}
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
	
	ModuloCorrespondenciaEdicion.prototype.htmlListaCopias = function(copias){
		var ref = this;
		var plantillaScript = ref.plantillaListaCopias.html();
		var plantilla = Handlebars.compile(plantillaScript);
		var contexto = {'copias' : copias};
		var contenidoHTML = plantilla(contexto);
		return contenidoHTML || 'No hay contenido';
	};
	
	ModuloCorrespondenciaEdicion.prototype.obtenerCorrespondencia = function(){
		var ref = this;
		var idCorrespondencia = ref.idCorrespondencia.val();
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_CORRESPONDENCIA + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.obtenerRutaAprobacion = function(){
		var ref = this;
		var idCorrespondencia = ref.idCorrespondencia.val();
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_OBTENER_RUTA_APROBACION + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.abrirAsignarFirma = function(idCorrespondencia, edicion){
		var ref = this;
		if(!edicion){
			window.location.href = ref.URL_ASIGNAR_FIRMA[0] + "/" + idCorrespondencia;
		} else{
			window.location.href = ref.URL_ASIGNAR_FIRMA[1] + "/" + idCorrespondencia;
		}
	};
	
	ModuloCorrespondenciaEdicion.prototype.abrirBandejaPendiente = function(edicion){
		var ref = this;
		if(!edicion){
			window.location.href = ref.URL_BANADEJA_PENDIENTE[0];
		} else {
			window.location.href = ref.URL_BANADEJA_PENDIENTE[1];
		}
		
	};
	
	ModuloCorrespondenciaEdicion.prototype.abrirBandejaFirmado = function(edicion){
		var ref = this;
		if(!edicion){
			window.location.href = ref.URL_BANADEJA_FIRMADOS[0];
		} else {
			window.location.href = ref.URL_BANADEJA_FIRMADOS[1];
		}
		
	};
	
	ModuloCorrespondenciaEdicion.prototype.asignarFirmanteAutomatico = function(idCorrespondencia, edicion) {
		var ref = this;
		var url;
		if(!edicion){
			url = ref.URL_ASIGNAR_FIRMANTE_AUTOMATICO[0];
		} else {
			url = ref.URL_ASIGNAR_FIRMANTE_AUTOMATICO[1];
		}
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		: 	url + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.asignarFirmanteInicialRutaAprobacion = function(idCorrespondencia, edicion) {
		var ref = this;
		var url;
		if(!edicion){
			url = ref.URL_ASIGNAR_FIRMANTE_RUTA_APROBACION[0];
		} else {
			url = ref.URL_ASIGNAR_FIRMANTE_RUTA_APROBACION[1];
		}
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'POST',
			url		: 	url + '/' + idCorrespondencia,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	};
	
	ModuloCorrespondenciaEdicion.prototype.descargarArchivo = function(idArchivAdjunto, edicion){
		var ref = this;
		var url;
		if(!edicion){
			url = ref.URL_DESCARGAR_ARCHIVO_ADJUNTO[0];
		} else{
			url = ref.URL_DESCARGAR_ARCHIVO_ADJUNTO[1];
		}
		window.open(url + "/" + idArchivAdjunto, '_blank');
	};
	
	ModuloCorrespondenciaEdicion.prototype.obtenerLugar = function(indiceURL, codLugar){
		var ref = this;
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_BUSCAR_LUGAR_X_DEPENDENCIA[indiceURL] + '/' + codLugar,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
	}
	
	ModuloCorrespondenciaEdicion.prototype.listarDependenciasNivel = function(cgc, nivel, indiceURLS){
		var ref = this;
		if(cgc=="0" || cgc=="null"){
			cgc = "";
		}
		if(nivel==""){
			nivel = "0";
		}
		var token = ref.csrfToken.val(); 
		var header = ref.csrfHeader.val();
		return $.ajax({
			type	:	'GET',
			url		: 	ref.URL_BUSCAR_DEPENDENCIAS_NIVEL[indiceURLS] + '/' + cgc + "/" + nivel,
			cache	:	false,
			beforeSend: function(xhr) {
		        xhr.setRequestHeader("Accept", "application/json");
		        xhr.setRequestHeader("Content-Type", "application/json");
		        xhr.setRequestHeader(header, token);
		    }
		});
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