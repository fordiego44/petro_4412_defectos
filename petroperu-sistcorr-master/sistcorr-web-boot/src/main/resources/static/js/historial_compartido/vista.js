var modulo_historial_compartido = MODULO_HISTORIAL_COMPARTIDO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var HISTORIAL_COMPARTIDO_VISTA = {
		modulo: null,
		modSistcorr: null,
		componentes: {},
		filtro: {},
		dataTable: null,
		sizeScreen: 550,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarComponentes();
			ref.iniciarEventos();
			setTimeout(function(){
				ref.modulo.componentes.btnBuscar.click();
			}, 500);
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.modulo.componentes.btnFiltros.click();
			
			ref.modulo.componentes.btnRetroceder.click(function(){
				sessionStorage.setItem("tab", "Archivos-tab");
				ref.modSistcorr.retroceder();
			});
			
			ref.modulo.componentes.btnLimpiar.click(function(){
				ref.limpiarFiltros();
			});
			
			ref.modulo.componentes.btnBuscar.click(function(){
				ref.buscarHistorial();
			});
			
			ref.modulo.componentes.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			console.log("Inicia: " + screen.width + "||" + ref.sizeScreen)
			if(screen.width < ref.sizeScreen){
				console.log("ABC")
				console.log("Title: " + $("#mainTitle").html() );
				var correlativo = $("#mainTitle").html().substring(30);
				$("#mainTitle").text("Hist. Notif. - " + correlativo);
			}
			
			$('[data-toggle="tooltip"]').tooltip('update')
			
		},
		
		limpiarFiltros: function(){
			var ref = this;
			
			ref.modSistcorr.procesar(true);

			ref.modulo.componentes.txtFechaNotificacionDesde.val(null);
			ref.modulo.componentes.txtFechaNotificacionDesde.change();
			ref.modulo.componentes.txtFechaNotificacionHasta.val(null);
			ref.modulo.componentes.txtFechaNotificacionHasta.change();
			ref.modulo.componentes.txtValorBuscar.val('');
			ref.modulo.componentes.txtValorBuscar.change();
			
			ref.inicializarTabla([]);
			
		},
		
		inicializarComponentes: function(){
			var ref = this;
			
			ref.componentes.txtFechaNotificacionDesde = ref.modulo.componentes.txtFechaNotificacionDesde.datepicker({
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
			
			ref.componentes.txtFechaNotificacionHasta = ref.modulo.componentes.txtFechaNotificacionHasta.datepicker({
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
			
			ref.modulo.componentes.btnFechaDesde.click(function(){
				ref.modulo.componentes.txtFechaNotificacionDesde.click();
				ref.modulo.componentes.txtFechaNotificacionDesde.focus();
			});
			
			ref.modulo.componentes.btnFechaHasta.click(function(){
				ref.modulo.componentes.txtFechaNotificacionHasta.click();
				ref.modulo.componentes.txtFechaNotificacionHasta.focus();
			});
			
		},
		
		
		buscarHistorial: function(){
			var ref = this;
			ref.filtro = {};
			
			ref.filtro.fechaDesde = ref.modulo.componentes.txtFechaNotificacionDesde.val();
			ref.filtro.fechaHasta = ref.modulo.componentes.txtFechaNotificacionHasta.val();
			ref.filtro.valorBuscar = ref.modulo.componentes.txtValorBuscar.val();
			
			var valido = true;
			var fecI = ref.filtro.fechaDesde.trim();
			var fecF = ref.filtro.fechaHasta.trim();
			console.log("Desde " + fecI + " hasta " + fecF);
			
			if(fecI != "" && fecF != ""){
				var anioI = parseInt(fecI.substring(6,10));
				var anioF = parseInt(fecF.substring(6,10));
				console.log("Desde " + anioI + " hasta " + anioF);
				if(anioF < anioI){
					valido = false;
				}else if(anioF == anioI){
					var mesI = parseInt(fecI.substring(3,5));
					var mesF = parseInt(fecF.substring(3,5));
					console.log("Desde " + mesI + " hasta " + mesF);
					if(mesF < mesI){
						valido = false;
					}else if(mesF == mesI){
						var diaI = parseInt(fecI.substring(0,2));
						var diaF = parseInt(fecF.substring(0,2));
						console.log("Desde " + diaI + " hasta " + diaF);
						if(diaF < diaI){
							valido = false;
						}
					}
				}
			}
			
			if(valido){
				ref.searchHistorial();
			}else{
				ref.modSistcorr.notificar("ERROR", "Fecha Hasta no puede ser menor a Fecha Desde", "Error");
			}
		},
		
		searchHistorial: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.consultar(ref.filtro, ref.modulo.componentes.hdnIdCorrespondencia.val())
				.then(function(respuesta){
					ref.modSistcorr.procesar(false);
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.inicializarTabla(respuesta.datos || []);
					} else {
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.inicializarTabla([]);
					}
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.modulo.exportarExcel(ref.filtro, ref.modulo.componentes.hdnIdCorrespondencia.val())
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_correspondencia.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_historial.xlsx';
	                    document.body.appendChild(a);
	                    a.click();
	                    document.body.removeChild(a);
					}
					ref.modSistcorr.procesar(false);
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		inicializarTabla: function(data){
			var ref = this;
			console.log("ref.dataTable:" + ref.dataTable);
			console.log(data);
			if(ref.dataTable){
				ref.dataTable.destroy();
				ref.modulo.componentes.dataTable.empty();
				ref.dataTable = null;
				ref.inicializarTabla(data);
			} else {
				ref.modulo.componentes.dataTable.show();
				ref.dataTable = ref.modulo.componentes.dataTable.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
			        "responsive": true,
			        "pageLength": 10,
			        "data": data,
			        "columns": [
			        	{data: 'fecha', title: 'Fecha y Hora', defaultContent: '', render: function(data, type, full){
			        		var arr = full.fecha.split("/")
			        		return '<span style="display:none;">'+arr[2]+'/'+arr[1]+'/'+arr[0]+' ' + full.hora + '</span>' + full.fecha + ' ' + full.hora;
			        	}},
			        	{data: 'modo', title: 'Modo Compartido', defaultContent: '', render: function(data, type, full){
			        		return full.modoCompartido;
			        	}},
			        	{data: 'compartidoPor', title: 'Compartido Por', defaultContent: '', render: function(data, type, full){
			        		return full.compartidoPor;
			        	}},
			        	{data: 'destinatarios', title: 'Destinatarios', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 500) {
			        			max = 150;
			        		} else {
			        			max = 25;
			        		}
			        		var text = full.destinatarios.length > max ? (full.destinatarios.substring(0, max)+ "<span title='" + full.destinatarios +"'>...</span>") : full.destinatarios;
			        		return text;
			        		//return full.destinatarios;
			        	}},
			        	{data: 'copia', title: 'Copia', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 500) {
			        			max = 150;
			        		} else {
			        			max = 25;
			        		}
			        		var text = full.copias.length > max ? (full.copias.substring(0, max)+ "<span title='" + full.copias +"'>...</span>") : full.copias;
			        		return text;
			        		//return full.copias;
			        	}},
			        	{data: 'asunto', title: 'Asunto', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 500) {
			        			max = 150;
			        		} else {
			        			max = 25;
			        		}
			        		var text = full.asunto.length > max ? (full.asunto.substring(0, max)+ "<span title='" + full.asunto +"'>...</span>") : full.asunto;
			        		return text;
			        		//return full.asunto;
			        	}},
			        	{data: 'contenido', title: 'Contenido', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 500) {
			        			max = 150;
			        		} else {
			        			max = 25;
			        		}
			        		var text = full.contenido.length > max ? (full.contenido.substring(0, max)+ "<span title='" + full.contenido +"'>...</span>") : full.contenido;
			        		return text;
			        		//return full.contenido;
			        	}},
			        	{data: 'archivos', title: 'Archivos', defaultContent: '', render: function(data, type, full){
			        		var max = 0;
			        		if(screen.width > 500) {
			        			max = 150;
			        		} else {
			        			max = 25;
			        		}
			        		var text = full.archivos.length > max ? (full.archivos.substring(0, max)+ "<span title='" + full.archivos +"'>...</span>") : full.archivos;
			        		return text;
			        		//return full.archivos;
			        	}},
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		ref.dataTable.responsive.rebuild();
			        		ref.dataTable.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        	}, 1000);
			        },

				});
				
				ref.modulo.componentes.dataTable.on( 'page.dt', function () {
					ref.dataTable.responsive.rebuild();
	        		ref.dataTable.responsive.recalc();
				});
			}
		}
};

setTimeout(function() {
	HISTORIAL_COMPARTIDO_VISTA.modulo = modulo_historial_compartido;
	HISTORIAL_COMPARTIDO_VISTA.modSistcorr = modulo_sistcorr;
	HISTORIAL_COMPARTIDO_VISTA.inicializar();
}, 200);