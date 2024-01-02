var MODULO_SISTCORR = (function(){
	var instance;
	
	function ModuloSistcorr(SISCORR_APP) {
		 this.SISCORR_APP = SISCORR_APP;
		 this.SISCORR_AUTHOR = 'Kenyo';
		 this.STR_NOTIFY = toastr;
		 this.compomenteProceso = $("#modalProcesando");
		 this.mensajeErrorPeticion = "Ocurrió un error en el proceso";
		 this.mensajeSessionExpirada = "Su sesión ha caducado, por favor vuelva a ingresar";
		 this.divPreload = $("#contenidoPreload");
		 this.divCuerpoModulo = $("#cuerpoModulo");
		 this.logNavegacion = "SISTCORR_LOG";
		 this.acciones ="SISTCORR_ACCIONES";
		 this.dependenciasDestino ="SISTCORR_DEPENDENCIAS_DESTINO";
		 this.filtros = "SISTCORR_FILTROS";
		 this.logoutForm = "logoutForm";
		 this.classAutocomplete = "sistcorrAutocomple";
		 this.classAutocompleteRemote = "sistcorrAutocompleRemoto";
		 this.classDatePicker = "gj-textbox-md";
		 this.classTextArea = "sistcorrTextArea";
		 this.classSelect = "sistcorrSelect";
		 this.classInputText = "sistcorrInput";
		 this.colorBlue = "#4285f4";
		 this.colorGrey = "#757575";
		 this.filtrosCorrespondencia = "SISTCORR_FILTROS_CORRESPONDENCIA";
		 this.bandeja_firma_seleccionada = "SISTCORR_FIRMA_SELECCIONADA";
		 this.filtrosConsultaCorrespondencia = "SISTCORR_CILTROS_CONSULTA_CORRESPODENCIA";
		 // INICIO TICKET 9000004408
		 this.filtrosJGConsultaCorrespondencia = "SISTCORR_FILTROS_JG_CONSULTA_CORRESPODENCIA";
		 // FIN TICKET 9000004408
		 // 9000004276 - INICIO
		 this.filtrosConsultaComprobantes = "SISTCORR_FILTROS_CONSULTA_COMPROBANTES";
		 this.filtrosConsultaContrataciones = "SISTCORR_FILTROS_CONSULTA_CONTRATACIONES";
		 // 9000004276 - FIN
		this.accordionMenu = $(".accordion");
		// TICKET 9000004270
		this.bandeja_entrada_firma_seleccionada = "SISTCORR_CORRESPONDENCIA_SELECCIONADA";
		// FIN TICKET
		/*INI Ticket 9000004412*/
		this.filtrosConsultaEstDigContrataciones = "SISTCORR_FILTROS_CONSULTA_EST_DIG_CONTRATACIONES";
		this.filtrosConsultaDespacho = "SISTCORR_FILTROS_CONSULTA_DESPACHO";
		this.filtrosConsultaValijasRecibidas = "SISTCORR_FILTROS_CONSULTA_VALIJAS_RECIBIDAS";
		/*FIN Ticket 9000004412*/
	}
	
	ModuloSistcorr.prototype.cargarVista = function(){
		var ref = this;
		setTimeout(function(){
			// TICKET 9000003791
			var tab = sessionStorage.getItem("tab");
			if(tab !=  null && tab != undefined && tab != ""){
				$("#" + tab).click();
				// TICKET 9000004272
				$(".petro-tabs-activo").each(function(){
					var t = $(this);
					t.removeClass('petro-tabs-activo');
				});
				$("#" + tab).addClass("petro-tabs-activo");
				// FIN TICKET
				sessionStorage.removeItem("tab");
			}
			
			//$(".list-group[data-cantidad=2]").parent().parent().click();
			
			// FIN TICKET 9000003791
			
            ref.divPreload.css('display', 'none');
            ref.divCuerpoModulo.css('display','block');
        }, 700);
		
		var url_actual = window.location.href;
		url_actual = url_actual.replace('#!', '');
		if(!sessionStorage.getItem(ref.logNavegacion)){
			var nav = [];
			if(url_actual.indexOf('403') <= -1){
				nav.push(url_actual);
				sessionStorage.setItem(ref.logNavegacion, JSON.stringify(nav));
			}
		}else{
			var nav = JSON.parse(sessionStorage.getItem(ref.logNavegacion));
			var _url_actual = url_actual + '#!';
			if(nav[nav.length -1] != url_actual){
				nav.push(url_actual);
				sessionStorage.setItem(ref.logNavegacion, JSON.stringify(nav));
			}
		}
		
		// TICKET 9000004272
		sessionStorage.removeItem("procedenciaFirma");
		// FIN TICKET
		
	};
	
	ModuloSistcorr.prototype.eventoTooltip = function(){
		if(navigator.userAgent.match(/Android/i)
				 || navigator.userAgent.match(/webOS/i)
				 || navigator.userAgent.match(/iPhone/i)
				 || navigator.userAgent.match(/iPad/i)
				 || navigator.userAgent.match(/iPod/i)
				 || navigator.userAgent.match(/BlackBerry/i)
				 || navigator.userAgent.match(/Windows Phone/i)){
			
		}else {
			$('[data-toggle="tooltip"]').tooltip({
				animation	:	true,
				delay		:	{"show": 100}
			});
			$('[data-toggle="tooltip"]').on('show.bs.tooltip', function(){
				setTimeout(function() {
					$('[data-toggle="tooltip"]').tooltip('hide');
				}, 1000);
			});
			
			$('[data-toggle="tooltip"]').click(function(){
				setTimeout(function(){
					$('[data-toggle="tooltip"]').tooltip('hide');
				}, 50)
			});
		}
		
	};
	
	ModuloSistcorr.prototype.retroceder = function(){
		var ref = this;
		var url_actual = window.location.href;
		url_actual = url_actual.replace('#!', '');
		var nav = JSON.parse(sessionStorage.getItem(ref.logNavegacion));
		if(nav.length == 1){
			console.log("Nav. length:" + nav)
			if(nav[0].indexOf("ver-detalle") >= 0){
				window.location.replace('../lista-documentos/pendientes');
				return;
			}
			if(nav[0].indexOf("/tutoriales") >= 0){
				window.location.replace('lista-documentos/pendientes');
				return;
			}
			if(nav[0].indexOf("asignacion-firma") >= 0){
				window.location.replace('../lista-documentos/firmados');
				return;
			}
			if(nav[0].indexOf("historial-compartido") >= 0){
				window.location.replace('../consulta-bandeja-salida');
				return;
			}
			if(nav[0].indexOf("/compartir/") >= 0){
				window.location.replace('../../../consulta-bandeja-salida');
				return;
			}
			window.location.replace('./login');
			return;
		}
		var indice = 0;
		for(var i = nav.length -1; i >= 0; i--){
			if(nav[i] == url_actual){
				indice = i -1;
				var paginaAnterior = nav[indice];
				if(paginaAnterior.indexOf('undefined') > -1){
					window.location.replace('./login');
				} else {
					nav.splice(i, 1);
					sessionStorage.setItem(ref.logNavegacion, JSON.stringify(nav));
					window.location.replace(paginaAnterior);
				}
				
				break;
			}
		}
		
	};
	
	ModuloSistcorr.prototype.showMessageErrorRequest =  function(error){
		var ref = this;
		if (error.status == 403){
			ref.notificar("ERROR", ref.mensajeSessionExpirada, "Error");
			setTimeout(function(){
				location.reload();
			}, 2000);
		} else {
			console.log("showMessageErrorRequest:")
			console.log(error);
			ref.notificar("ERROR", ref.mensajeErrorPeticion, "Error");
		}
			
	};
	
	ModuloSistcorr.prototype.showMessageRequest = function(respuesta){
		var ref =  this;
		console.log(respuesta);
		if(respuesta.estado == true){
			ref.notificar("OK", respuesta.mensaje, "Success");
		} else {
			//$('#detalleError').hide();
		
			var msg = ref.mensajeErrorPeticion + "<br/><span class='btnVerDetalleError'><b>Ver mensaje<b></span><br/><span id='detalleError' style='display:none'>" + respuesta.mensaje + "</span>";
			ref.notificar("ERROR",msg, 'Error' );
			setTimeout(function() {
				$('.btnVerDetalleError').click(function(event){
					
					console.log($('.btnVerDetalleError').text());
					//alert(msg);
					$('#detalleError').show();
					event.stopPropagation();
				});
			}, 200);
		}
	};
	
	
	
	
	ModuloSistcorr.prototype.getCantidadLog = function(){
		var ref = this;
		var nav = JSON.parse(sessionStorage.getItem(ref.logNavegacion));
		return (nav)?(nav.length):(0);
	};
	
	ModuloSistcorr.prototype.setDependenciasDestino = function(_dependencias){
		var ref = this;
		sessionStorage.setItem(ref.dependenciasDestino, JSON.stringify(_dependencias || []));
	};
	
	ModuloSistcorr.prototype.getDependenciasDestino = function(){
		var ref = this;
		return JSON.parse(sessionStorage.getItem(ref.dependenciasDestino));
	};
	
	//INICIO TICKET 9000003866
	ModuloSistcorr.prototype.setDependenciasDestinoBPAC = function(_dependencias){
		var ref = this;
		sessionStorage.setItem(ref.dependenciasDestino+"_BPAC", JSON.stringify(_dependencias || []));
	};
	
	ModuloSistcorr.prototype.getDependenciasDestinoBPAC = function(){
		var ref = this;
		return JSON.parse(sessionStorage.getItem(ref.dependenciasDestino+"_BPAC"));
	};
	//FIN TICKET 9000003866
	
	ModuloSistcorr.prototype.setAcciones = function(_acciones){
		var ref = this;
		sessionStorage.setItem(ref.acciones, JSON.stringify(_acciones || []));
	};
	
	ModuloSistcorr.prototype.getAcciones = function(_acciones){
		var ref = this;
		return JSON.parse(sessionStorage.getItem(ref.acciones));
	};
	
	ModuloSistcorr.prototype.generarHtml = function(){
		var ref = this;
		
	};
	
	ModuloSistcorr.prototype.setFiltros = function(_filtros, bandeja){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtros)) || {};
		filtros[bandeja] = _filtros || [];
		sessionStorage.setItem(ref.filtros, JSON.stringify(filtros));
	};	
	
	ModuloSistcorr.prototype.getFiltros = function(bandeja){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtros)) || {};
		return filtros[bandeja] || [];
	};
	
	ModuloSistcorr.prototype.setFiltrosCorrespondencia = function(_filtros, bandeja){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosCorrespondencia)) || {};
		filtros[bandeja] = _filtros || {};
		sessionStorage.setItem(ref.filtrosCorrespondencia, JSON.stringify(filtros));
	};
	
	ModuloSistcorr.prototype.getFiltrosCorrespondencia = function(bandeja){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosCorrespondencia)) || {};
		return filtros[bandeja] || {};
	};
	
	ModuloSistcorr.prototype.setFiltrosConsultaCorrespondencia = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaCorrespondencia, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getFiltrosConsultaCorrespondencia = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaCorrespondencia)) || {};
		return filtros || {};
	};	
	
	ModuloSistcorr.prototype.setfiltrosJGConsultaCorrespondencia = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosJGConsultaCorrespondencia, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getFiltrosJGConsultaCorrespondencia = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosJGConsultaCorrespondencia)) || {};
		return filtros || {};
	};
	// 9000004276 - INICIO
	ModuloSistcorr.prototype.setfiltrosConsultaComprobantes = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaComprobantes, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getfiltrosConsultaComprobantes = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaComprobantes)) || {};
		return filtros || {};
	};

	ModuloSistcorr.prototype.setfiltrosConsultaContrataciones = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaContrataciones, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getfiltrosConsultaContrataciones = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaContrataciones)) || {};
		return filtros || {};
	};	
	// 9000004276 - FIN
	ModuloSistcorr.prototype.setBandejaFirmaSeleccionada =  function(id){
		var ref = this;
		sessionStorage.setItem(ref.bandeja_firma_seleccionada, id);
	};
	
	ModuloSistcorr.prototype.getBandejaFirmaSeleccionada =  function(){
		var ref = this;
		return sessionStorage.getItem(ref.bandeja_firma_seleccionada) || "";
	};
	
	// TICKET 9000004270
	ModuloSistcorr.prototype.setBandejaEntradaFirmaSeleccionada =  function(id){
		var ref = this;
		sessionStorage.setItem(ref.bandeja_entrada_firma_seleccionada, id);
	};
	
	ModuloSistcorr.prototype.getBandejaEntradaFirmaSeleccionada =  function(){
		var ref = this;
		return sessionStorage.getItem(ref.bandeja_entrada_firma_seleccionada) || "";
	};
	// FIN TICKET
	
	ModuloSistcorr.prototype.cerrarSession = function(){
		var ref = this;
		sessionStorage.removeItem(ref.logNavegacion);
		sessionStorage.removeItem(ref.acciones);
		sessionStorage.removeItem(ref.filtros);
		sessionStorage.removeItem(ref.filtrosCorrespondencia);
		sessionStorage.removeItem(ref.bandeja_firma_seleccionada);
		// TICKET 9000004270
		sessionStorage.removeItem(ref.bandeja_entrada_firma_seleccionada);
		// FIN TICKET
		sessionStorage.removeItem(ref.filtrosConsultaCorrespondencia);
		sessionStorage.removeItem('FILTROS_CONSULTA_ASIGNACION');
		sessionStorage.removeItem('FILTROS_CONSULTA_CORRESPONDENCIA');
		sessionStorage.removeItem('SISTCORR_FILTROS_JG_CONSULTA_CORRESPODENCIA');
		// 9000004276 - INICIO
		sessionStorage.removeItem(ref.filtrosConsultaComprobantes);
		sessionStorage.removeItem(ref.filtrosConsultaContrataciones);
		// 9000004276 - FIN
		
		//inicio ticket 9000004275
		sessionStorage.removeItem('FILTROS_ADM_DISTRITO');
		sessionStorage.removeItem('FILTROS_ADM_PROVINCIA');
		sessionStorage.removeItem('FILTROS_ADM_DEPARTAMENTO');
		
		sessionStorage.removeItem('OBJ_CONDUCTOR');
		sessionStorage.removeItem('FILTROS_ADM_CGC');
		sessionStorage.removeItem('FILTROS_ADM_CGC_LUGAR_TRABAJO');
		sessionStorage.removeItem('FILTROS_ADM_CIUDAD');
		sessionStorage.removeItem('FILTROS_ADM_COURIER_LOCAL');
		sessionStorage.removeItem('FILTROS_ADM_CONSULTA_COURIERS');
		sessionStorage.removeItem('FILTROS_ADM_DEPARTAMENTO');
		sessionStorage.removeItem('FILTROS_ADM_DEPENDENCIA_EXTERNA');
		sessionStorage.removeItem('FILTROS_ESTADOS');
		sessionStorage.removeItem('FILTROS_FORMA_ENVIO');
		sessionStorage.removeItem('FILTROS_ADM_GESTOR_DEPENDENCIA');
		sessionStorage.removeItem('FILTROS_ADM_CONSULTA_JERARQUIAS');
		sessionStorage.removeItem('FILTROS_ADM_LUGAR_TRABAJO');
		sessionStorage.removeItem('FILTROS_MONEDAS');
		sessionStorage.removeItem('FILTROS_CONSULTA_MOTIVO');
		sessionStorage.removeItem('FILTROS_CONSULTA_NUMERADOR');
		sessionStorage.removeItem('FILTROS_ADM_CONSULTA_PAISES');
		
		sessionStorage.removeItem('FILTROS_ADM_PROV_LUGAR_TRABAJO');
		sessionStorage.removeItem('FILTROS_ADM_TIPO_ACCION');
		sessionStorage.removeItem('FILTROS_ADM_TIPO_CORRESPONDENCIA');
		sessionStorage.removeItem('FILTROS_ADM_TIPO_COMPROBANTE');
		sessionStorage.removeItem('FILTROS_CONSULTA_TIPO_UM');
		sessionStorage.removeItem('FILTROS_ADM_TRSANSACION_CGC');
		sessionStorage.removeItem('FILTROS_ADM_USUARIO_CGC');
		sessionStorage.removeItem('FILTROS_ADM_CONDUCTOR');
		sessionStorage.removeItem('ColOrd_Cond');
		sessionStorage.removeItem('NroPag_Cond');
		sessionStorage.removeItem('origPagCond');
		sessionStorage.removeItem('FILTRO_REMPLAZO_ADICION');
		sessionStorage.removeItem('FILTRO_REMPLAZO_TOTAL');
		sessionStorage.removeItem('FILTRO_REMPLAZO_GG');
		sessionStorage.removeItem('FILTRO_ADM_LOG');
		sessionStorage.removeItem('FILTRO_CONSULTA_REMPLAZO');
		sessionStorage.removeItem('FILTRO_CONSULTA_ROLES_ANTERIORES');
		//inicio ticket 9000004275
		//ticket 9000004808
		sessionStorage.removeItem('_itemSelec_ConsultaGeneralSalida');
		sessionStorage.removeItem('_itemSelec_ConsultaJefeGestorSalida');
		//Fin 9000004808
		//ticket 9000004962
		sessionStorage.removeItem('_itemSelec_ConsultaAuditoriaGeneralSalida');
		//Fin 9000004962
		sessionStorage.removeItem("_menu");
		sessionStorage.removeItem('FILTROS_CONSULTA_CORRESPONDENCIA_AUDITORIA');
		document.getElementById("logoutForm").submit();
	};
	
	ModuloSistcorr.prototype.clearSession = function(){
		var ref = this;
		sessionStorage.removeItem(ref.logNavegacion);
		sessionStorage.removeItem(ref.acciones);
		sessionStorage.removeItem(ref.filtros);
		sessionStorage.removeItem(ref.filtrosCorrespondencia);
		sessionStorage.removeItem(ref.filtrosJGConsultaCorrespondencia);
		// 9000004276 - INICIO
		sessionStorage.removeItem(ref.filtrosConsultaComprobantes);
		sessionStorage.removeItem(ref.filtrosConsultaContrataciones);
		// 9000004276 - FIN
		sessionStorage.removeItem("_menu");
		// TICKET 9000004494
		sessionStorage.removeItem("NroPag_CC");
		sessionStorage.removeItem("ColOrd_CC");
		sessionStorage.removeItem("NroPag_CA");
		sessionStorage.removeItem("ColOrd_CA");
		sessionStorage.removeItem("NroPag_JG");
		sessionStorage.removeItem("ColOrd_JG");
		sessionStorage.removeItem("NroPag_CG");
		sessionStorage.removeItem("ColOrd_CG");
		// FIN TICKET
		// TICKET 9000004961
		sessionStorage.removeItem("ColOrd_CCA");
		sessionStorage.removeItem("NroPag_CCA");
		// FIN TICKET
		//ticket 9000004962
		sessionStorage.removeItem('ColOrd_CAS');
		sessionStorage.removeItem('NroPag_CAS');
		
		//Fin 9000004962
	};
	
	
	ModuloSistcorr.prototype.datePicker_change = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		$comp.addClass('form-control');
		label.css('color', ref.colorGrey);
    	if(ref.validarComponte(idComponente) == true && ref.validarFecha(idComponente)){
    		setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
			}, 20);
    	} else{
    		setTimeout(function() {
    			$comp.removeClass('valid');
        		label.addClass('active');
        		$comp.addClass('invalid');
			}, 20);
    		ref.mostrarMensajeErrorComponente(idComponente);
    	}
	};
	
	ModuloSistcorr.prototype.datePicker_open = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		$comp.addClass('form-control');
    	var label = $("label[for='" + $comp.attr('id') + "']");
    	label.addClass('active');
    	label.css('color', ref.colorBlue);
	};
	
	ModuloSistcorr.prototype.datePicker_close = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
    	$comp.addClass('form-control');
    	var label = $("label[for='" + $comp.attr('id') + "']");
    	label.css('color', ref.colorGrey);
    	if(ref.validarComponte(idComponente) == true && ref.validarFecha(idComponente)){
    		setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
			}, 200);
    	} else{
    		setTimeout(function() {
    			$comp.removeClass('valid');
        		label.addClass('active');
        		$comp.addClass('invalid');
			}, 200);
    		ref.mostrarMensajeErrorComponente(idComponente);
    	}
    	
	};
	
	ModuloSistcorr.prototype.textArea_focus = function(idComponente){
		var ref = this;
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorBlue);
		label.addClass('active');
	};
	
	ModuloSistcorr.prototype.textArea_focusOut = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var requerido = $comp.attr('required') 	|| false; 
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorGrey);
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');	
    		}, 200)
		} else {
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
    			$comp.addClass('invalid');
			}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.textArea_change = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var requerido = $comp.attr('required') 	|| false; 
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorGrey);
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');	
    		}, 200)
		} else {
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
    			$comp.addClass('invalid');
			}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.inputText_change = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorGrey);
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function(){
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
			}, 200);
		} else {
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
			}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.inputText_focusOut = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorGrey);
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function(){
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
			}, 200);
		} else {
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
			}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.inputText_focus = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorBlue);
		label.addClass('active');
	};
	
	ModuloSistcorr.prototype.select2_change = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		var opcionSeleccionada = $("#select2-"+$comp.attr('id') + "-container");
		console.log(idComponente,opcionSeleccionada.html().length);
		label.css('color', ref.colorGrey);
		if(opcionSeleccionada.html().length > 0){
			opcionSeleccionada.addClass("select2-seleccionado-sistcorr");
		} else{
			opcionSeleccionada.removeClass("select2-seleccionado-sistcorr");
		}
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
    		}, 200);
		} else{
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
    		}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.select2_open = function(idComponente){
		var ref = this;
		if(!idComponente){
			return;
		}
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorBlue);
		label.addClass('active');
	};
	
	ModuloSistcorr.prototype.select2_close = function(idComponente){
		var ref = this;
		if(!idComponente){
			return;
		}
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		var opcionSeleccionada = $("#select2-"+$comp.attr('id') + "-container");
		console.log(idComponente,opcionSeleccionada.html().length);
		label.css('color', ref.colorGrey);
		if(opcionSeleccionada.html().length > 0){
			opcionSeleccionada.addClass("select2-seleccionado-sistcorr");
		} else{
			opcionSeleccionada.removeClass("select2-seleccionado-sistcorr");
		}
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
    			$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
    		}, 200);
		} else{
			setTimeout(function() {
    			$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
    		}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.select_change = function(idComponente){
		var ref = this;
		if(!idComponente){
			return;
		}
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorGrey);
		if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
				$comp.removeClass('invalid');
	    		$comp.addClass('valid');
	    		label.addClass('active');
	    		$comp.addClass('select_seleccionado');
			}, 200);
		} else{
			setTimeout(function(){
				$comp.removeClass('valid');
	    		label.addClass('active');
	    		$comp.removeClass('invalid');
			}, 200);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.select_focus = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var label = $("label[for='" + $comp.attr('id') + "']");
		label.css('color', ref.colorBlue);
		label.addClass('active');	
	};
	
	ModuloSistcorr.prototype.select_focusOut = function(idComponente){
		var ref = this;
		var $comp = $(event.currentTarget);
    	//$comp.addClass('form-control');
    	var label = $("label[for='" + $comp.attr('id') + "']");
    	label.css('color', ref.colorGrey);
    	if(ref.validarComponte(idComponente) == true){
			setTimeout(function() {
				$comp.removeClass('invalid');
	    		$comp.addClass('valid');
	    		label.addClass('active');
	    		$comp.addClass('select_seleccionado');
			}, 200);
		} else{
			setTimeout(function(){
				$comp.removeClass('valid');
	    		label.addClass('active');
	    		$comp.removeClass('invalid');
			}, 5000);
			ref.mostrarMensajeErrorComponente(idComponente);
		}
	};
	
	ModuloSistcorr.prototype.eventoTextArea = function(){
		var ref = this;
		/*$("." + ref.classTextArea).on('focusout', function(){
			var label = $("label[for='" + $(this).attr('id') + "']");
			label.addClass('active');
			label.css('color', '#757575');
		});*/
	}
	
	
	ModuloSistcorr.prototype.eventoDatePicker = function(){
		var ref = this;
		
	}
	
	ModuloSistcorr.prototype.eventosS2Remote = function(){
		var ref = this;
		/*$("." + ref.classAutocompleteRemote).on('select2:open', function(){
			var $comp = $(event.currentTarget);
			var label = $("label[for='" + $comp.attr('id') + "']");
			label.addClass('active');
        	label.css('color', ref.colorBlue);
		});
		
		$("." + ref.classAutocompleteRemote).on('select2:select', function(){
			var $comp = $(event.currentTarget);
			var label = $("label[for='" + $comp.attr('id') + "']");
			label.css('color', ref.colorGrey);
			console.log('seleccionado select', $comp.val());
        	if($comp.val() == null || $comp.val() == ''){
        		$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
        	} else{
        		$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
        	}
		});
		
		$("." + ref.classAutocompleteRemote).on('select2:close', function(){
			var $comp = $(event.currentTarget);
        	var label = $("label[for='" + $comp.attr('id') + "']");
        	label.css('color', ref.colorGrey);
        	console.log('seleccionado close', $comp.val());
        	if($comp.val() == null || $comp.val() == ''){
        		$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
        	} else{
        		$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
        	}
		});*/
		

	};
	
	ModuloSistcorr.prototype.eventosS2 = function(){
		var ref = this;
		/*var tipoAccionS2 = $("." + ref.classAutocomplete).sm_select();
		$("." + ref.classAutocomplete).on('select2:open', function(e){
			var label = $("label[for='" + $(this).attr('id') + "']");
			label.addClass('active');
			label.css('color', '#4285f4');
		});
		
		$("." + ref.classAutocomplete).on('select2:select', function(e){
			var label = $("label[for='" + $(this).attr('id')+ "']");
			label.addClass('active');
			label.css('color', '');
			label.css('font-size', '0.8rem');
		});
		
		$("." + ref.classAutocomplete).on('select2:close', function(e){
			console.log('out');
			var label = $("label[for='" + $(this).attr('id')+ "']");
			if(window.navigator.userAgent.indexOf('.NET4') > -1 ){
				label.addClass('active');
			} else{
				label.removeClass('active');
			}
			
			label.css('color', '#757575');
		});*/
	};
	
	
	
	ModuloSistcorr.prototype.eventoSelect = function(){
		var ref = this;
		/*$("." + ref.classSelect).on('focus', function(event){
			var $comp = $(event.currentTarget);
			var label = $("label[for='" + $comp.attr('id') + "']");
			label.addClass('active');
			label.css('color', ref.colorBlue);
		});
		
		$("." + ref.classSelect).change(function(event){
			var $comp = $(event.currentTarget);
			var label = $("label[for='" + $comp.attr('id') + "']");
			label.css('color', ref.colorGrey);
        	if($comp.val() == null || $comp.val() == ''){
        		$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
        	} else{
        		$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
        		$comp.addClass('select_seleccionado');
        	}	
		});
		
		$("." + ref.classSelect).on('focusout', function(event){
			var $comp = $(event.currentTarget);
        	$comp.addClass('form-control');
        	var label = $("label[for='" + $comp.attr('id') + "']");
        	label.css('color', ref.colorGrey);
        	if($comp.val() == null || $comp.val() == ''){
        		$comp.removeClass('valid');
        		label.removeClass('active');
        		$comp.addClass('invalid');
        	} else{
        		$comp.removeClass('invalid');
        		$comp.addClass('valid');
        		label.addClass('active');
        		$comp.addClass('select_seleccionado');
        	}
			
		});*/
	};
	
	ModuloSistcorr.prototype.eventoInputText = function(){
		var ref = this;
		
	};
	
	ModuloSistcorr.prototype.validarComponte = function(idComponente){
		var ref = this;
		var $comp = $("#" + idComponente);
		var requerido = $comp.attr('required') == 'required' ? true : false ; 
		var esVacio = $comp.val() == null ? true : ($comp.val() == '' ? true : false);
		//var esVacio = $comp.val() == null ? true :  false;
		if(requerido == true && esVacio == false)
			return true;
		if(requerido == true && esVacio == true)
			return false;
		if(requerido == false && esVacio == true)
			return true;
		if(requerido == false && esVacio == false)
			return true;
	};
	
	ModuloSistcorr.prototype.validarFecha = function(idComponente) {
		var ref = this;
		var $comp = $("#" + idComponente);
		if($comp.val() == ''){
			return true;
		}
		if( !$comp.val().match(/([0-2][0-9]|(3)[0-1])(\/)(((0)[0-9])|((1)[0-2]))(\/)\d{4}$/i) ){
			ref.notificar('Error', 'Formato de fecha incorrecta.', 'Warning');
			return false;
		}
		return true;
	}
	
	
	ModuloSistcorr.prototype.mostrarMensajeErrorComponente = function(idComponente){
		var ref = this;
		/*var $comp = $("#" + idComponente);
		var mensaje = $comp.data("msgerror");
		ref.notificar("Error", mensaje, "Warning");*/
	};
	
	ModuloSistcorr.prototype.procesar = function(isProcesando){
		var ref = this;
		if(isProcesando == true){
			//Ticket 7000004687
			ref.compomenteProceso.modal('show');
			//this.compomenteProceso.modal('show');
		}else{
			
			//Ticket 7000004687
			setTimeout(function() {
				ref.compomenteProceso.modal('hide');
			}, 1000);
			/*
			setTimeout(function() {
				ref.compomenteProceso.modal('hide');
			}, 500);
			*/
		}
	};
	
	ModuloSistcorr.prototype.notificar = function(titulo, mensaje, tipoMensaje){
		if(titulo == ""){
			titulo = "Resultado";
		}
		this.STR_NOTIFY.options = {
				  "closeButton": true,
				  "debug": false,
				  "newestOnTop": false,
				  "progressBar": true,
				  "positionClass": "toast-bottom-right",
				  "preventDuplicates": false,
				  "onclick": null,
				  "showDuration": "400",
				  "preventDuplicates": true,
				  "hideDuration": "1000",
				  "timeOut": "5000",
				  "extendedTimeOut": "1000",
				  "showEasing": "swing",
				  "hideEasing": "linear",
				  "showMethod": "fadeIn",
				  "hideMethod": "fadeOut"
				};
		switch (tipoMensaje) {
			case "Success":
				this.STR_NOTIFY["success"](mensaje, titulo);
				break;
			case "Info":
				this.STR_NOTIFY["info"](mensaje, titulo);	
				break;
			case "Warning":
				this.STR_NOTIFY.options["timeOut"] = "5000";
				//this.STR_NOTIFY["warning"](mensaje, titulo);
				this.STR_NOTIFY["error"](mensaje, titulo);
				break;
			case "Error":
				this.STR_NOTIFY.options["timeOut"] = "5000";
				this.STR_NOTIFY["error"](mensaje, titulo);
				break;
			default:
				break;
		}

	};
	
	ModuloSistcorr.prototype.cerrarNofificaciones = function(){
		this.STR_NOTIFY.clear();
	};
	
	ModuloSistcorr.prototype.clonarObjecto = function(objectoTo, objectoFrom){
		 if(!objectoFrom)
             return null;
		 for(var _i in objectoFrom){
			 objectoTo[_i] = objectoFrom[_i];
		 }
		 return objectoTo;
	};
	
	/*INI Ticket 9000004412*/
	ModuloSistcorr.prototype.setfiltrosConsultaEstDigContrataciones = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaEstDigContrataciones, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getfiltrosConsultaEstDigContrataciones = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaEstDigContrataciones)) || {};
		return filtros || {};
	};	
	
	ModuloSistcorr.prototype.setfiltrosConsultaDespacho = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaDespacho, JSON.stringify(_filtros));
	};
	
	ModuloSistcorr.prototype.getfiltrosConsultaDespacho = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaDespacho)) || {};
		return filtros || {};
	};	
	
	
	
	ModuloSistcorr.prototype.getfiltrosConsultaValijasRecibidas = function(){
		var ref = this;
		var filtros = JSON.parse(sessionStorage.getItem(ref.filtrosConsultaValijasRecibidas)) || {};
		return filtros || {};
	};	
	
	ModuloSistcorr.prototype.setfiltrosConsultaValijasRecibidas = function(_filtros){
		var ref = this;
		sessionStorage.setItem(ref.filtrosConsultaValijasRecibidas, JSON.stringify(_filtros));
	};
	/*FIN Ticket 9000004412*/
	
	
	function createInstance(SISCORR_APP) {
        var object = new ModuloSistcorr(SISCORR_APP);
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