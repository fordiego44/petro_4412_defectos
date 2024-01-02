var modulo_reemplazos_adicion= MODULO_REEMPLAZOS_ADICION.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REEMPLAZOS_ADICION_VISTA = {
		moduloJG: null,
		modSistcorr: null,
		componentesJG: {combosSimples:{}, combosS2: {}, datePikers:{}},
		filtroJG: {},
		masfiltroJGs: false,
		dataTable: null,
		dataTableConsulta: null,
		dependenciasUsuario: [],
		datosConsulta: [],
		dependenciasJG: [],
		jefe: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista();
			ref.inicializarcomponentesJG();
			ref.iniciarEventos();
			setTimeout(function(){
				ref.setFormCombosFiltros();
			}, 2000)
		},
		
		iniciarEventos: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.btnFiltrosJG.click();
			
			ref.moduloJG.componentesJG.btnExportExcel.click(function(){
				ref.exportarExcel();
			});
			
			ref.moduloJG.componentesJG.btnBuscar.click(function(){
				ref.buscarFuncionarios();
			});
			
			ref.moduloJG.compCerrarSession.click(function(){
				ref.modSistcorr.cerrarSession();
			});
			
			ref.moduloJG.componentesJG.btnResetear.click(function(){
				ref.resetarfiltroJGs();
			});			
			
			
			ref.moduloJG.btnAbrirTutoriales.click(function(event){
				event.preventDefault();
				var t = $(this);
				window.location.replace("../" + ref.moduloJG.URL_TUTORIALES);
			});
			
			ref.moduloJG.compBtnRegistrarAdicion.click(function(e){
				e.preventDefault();
				var $comp = $(this);
				ref.abrirModalRegAdicion();
			});
			
			
			ref.moduloJG.btnModificarFuncionario.off('click').on('click', function(){				
				var comp = $(this);
				var idFuncionario = comp.attr("data-id");
				setTimeout(function(){
					ref.modulo.abrirModalRegAdicion.modal('show');
				}, 300);
			});
						
			ref.moduloJG.btnElimFuncionario.off('click').on('click', function(){
				var comp = $(this);
				var idReempAdicion = comp.attr("data-id");
				setTimeout(function(){
					ref.moduloJG.comElimFuncionario.modal('show');
				}, 300);
			});
			
			ref.moduloJG.btnElimFuncionario.off('click').on('click', function(){
				var comp = $(this);
				var idFuncionario = comp.attr("data-id");
				var registro=comp.attr("data-registro")
			});
			
			setTimeout(function() {				
				ref.obtenerfiltroJGs();
				if(Object.keys(ref.filtroJG).length == 0){
					ref.actualizarValoresPorDefecto();
				} else {
					console.log("actualizar y buscar correspondencias")
					ref.searchFuncionarios();
				}
			}, 1500);
			
		},
		
		setFormCombosFiltros: function(){
			var ref = this;
			
			if(ref.filtroJG != undefined && ref.filtroJG.dependencia != undefined){
				
				ref.moduloJG.componentesJG.cmbDependenciaFuncionario.val(ref.filtroJG.dependencia);
				ref.moduloJG.componentesJG.cmbDependenciaFuncionario.change();
				
				ref.moduloJG.componentesJG.txtNombres.val(ref.filtroJG.nombres);
				ref.moduloJG.componentesJG.txtNombres.change();
				ref.moduloJG.componentesJG.txtApellidos.val(ref.filtroJG.apellidos);
				ref.moduloJG.componentesJG.txtApellidos.change();
				ref.moduloJG.componentesJG.txtRegistroFiltro.val(ref.filtroJG.registro);
				ref.moduloJG.componentesJG.txtRegistroFiltro.change();
			
				label = $("label[for='" + ref.moduloJG.componentesJG.txtNombres.attr('id') + "']");
				label.addClass('active');
				label = $("label[for='" + ref.moduloJG.componentesJG.txtApellidos.attr('id') + "']");
				label.addClass('active');
				label = $("label[for='" + ref.moduloJG.componentesJG.txtRegistroFiltro.attr('id') + "']");
				label.addClass('active');
			}
		},
		
		eliminarFuncionario: function(){
			var ref=this;
			var comp = $(this);
			ref.parametros={
					'idFuncionario' : ref.moduloJG.objetoFuncionarios.id,
					'registro' : ref.moduloJG.objetoFuncionarios.registro
			};			
			ref.modSistcorr.procesar(true);			
			ref.moduloJG.eliminarFuncionario(ref.parametros)
				.then(function(respuesta){
					console.log("Respuesta.estado:" + respuesta.estado)
					if(respuesta.estado == true){
						ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
						ref.modSistcorr.procesar(false);
						ref.moduloJG.comElimFuncionario.myModal.modal('hide');
						ref.inicializarTablaConsulta();
					}else{
						ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
						ref.modSistcorr.procesar(false);
					}
					
				}).catch(function(error){
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comElimFuncionario.myModal.modal('hide');
					ref.modSistcorr.showMessageErrorRequest(error);
				});
		},
		
		resetarfiltroJGs: function(){
			var ref = this;
			
			ref.moduloJG.componentesJG.txtRegistroFiltro.val("");
			ref.moduloJG.componentesJG.txtRegistroFiltro.change();
			ref.moduloJG.componentesJG.txtNombres.val("");
			ref.moduloJG.componentesJG.txtNombres.change();
			ref.moduloJG.componentesJG.txtApellidos.val("");
			ref.moduloJG.componentesJG.txtApellidos.change();
			ref.moduloJG.componentesJG.cmbDependenciaFuncionario.val("");
			ref.moduloJG.componentesJG.cmbDependenciaFuncionario.change();
			//ref.actualizarValoresPorDefecto();
			
		},
		
		abrirModalRegAdicion : function(){
			var ref = this;
			ref.limpiarFormFuncionario();
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comRegAdicion.myModal.modal('show');
			setTimeout(function() {
				ref.iniciarCombosAutoCompletados();
				console.log('focus');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		limpiarFormFuncionario: function(){
			ref = this;
			ref.moduloJG.objetoFuncionarios.id="";
			ref.moduloJG.objetoFuncionarios.registro.val("");
			ref.moduloJG.objetoFuncionarios.registro.change();
			ref.moduloJG.objetoFuncionarios.primerNombre.val("");
			ref.moduloJG.objetoFuncionarios.primerNombre.change();
			ref.moduloJG.objetoFuncionarios.segundoNombre.val("");
			ref.moduloJG.objetoFuncionarios.segundoNombre.change();
			ref.moduloJG.objetoFuncionarios.apellidoPaterno.val("");
			ref.moduloJG.objetoFuncionarios.apellidoPaterno.change();
			ref.moduloJG.objetoFuncionarios.apellidoMaterno.val("");
			ref.moduloJG.objetoFuncionarios.apellidoMaterno.change();
			ref.moduloJG.objetoFuncionarios.dependencia.val("");
			ref.moduloJG.objetoFuncionarios.dependencia.change();
			ref.moduloJG.objetoFuncionarios.email.val("");
			ref.moduloJG.objetoFuncionarios.email.change();
			ref.moduloJG.objetoFuncionarios.ficha.val("");
			ref.moduloJG.objetoFuncionarios.ficha.change();
			ref.moduloJG.objetoFuncionarios.operacion.val("");
			ref.moduloJG.objetoFuncionarios.operacion.change();
			ref.moduloJG.objetoFuncionarios.tipoFuncionario.val("");
			ref.moduloJG.objetoFuncionarios.tipoFuncionario.change();
			ref.moduloJG.objetoFuncionarios.notificaciones.val("");
			ref.moduloJG.objetoFuncionarios.notificaciones.change();
			ref.moduloJG.objetoFuncionarios.participa.val("");
			ref.moduloJG.objetoFuncionarios.participa.change();
			ref.moduloJG.objetoFuncionarios.activo.val("");
			ref.moduloJG.objetoFuncionarios.activo.change();
			ref.moduloJG.objetoFuncionarios.supervisor.val("");
			ref.moduloJG.objetoFuncionarios.supervisor.change();
			ref.moduloJG.objetoFuncionarios.accion="";
		},
		
		abrirModalElimAdicion : function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.comElimFuncionario.myModal.modal('show');
			setTimeout(function() {
				console.log('abre modal eliminar');
				ref.modSistcorr.procesar(false);
			}, 500);
		},
		
		iniciarCombosAutoCompletados: function(){
			var ref = this;
		},
		
		actualizarValoresPorDefecto: function(){
			var ref = this;
			ref.inicializarTablaConsulta();
		},
		
		
		obtenerfiltroJGs: function(){
			var ref = this;
			var _filtroJG = JSON.parse(sessionStorage.getItem("FILTRO_ADM_FUNCIONARIOS")) || {};;
			ref.filtroJG = _filtroJG;			
		},
		
		
		inicializarcomponentesJG: function(){
			var ref = this;		
			//debugger;
			
			ref.componentesJG.txtFechaAdicionDesdeJG = ref.moduloJG.componentesJG.txtFechaAdicionDesdeJG.datepicker({
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
			
			ref.componentesJG.txtFechaAdicionHastaJG = ref.moduloJG.componentesJG.txtFechaAdicionHastaJG.datepicker({
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
			
			
			ref.componentesJG.combosS2.desDependencia = $("#cmbDepend").select2({ 
				
			})
			.change(function(event){
				var $comp = $(event.currentTarget);
				
			}).on('select2:open', function(event){
				
			}).on('select2:closing', function (event) {
				
			});
			
			ref.componentesJG.combosS2.desDependenciaFiltro = $("#cmbDependencia").select2({ 
				
			})
			.change(function(event){
				var $comp = $(event.currentTarget);
				
			}).on('select2:open', function(event){
				
			}).on('select2:closing', function (event) {
				
			});
			 
		},
		
		
		buscarFuncionarios: function(){
			var ref = this;
			ref.filtroJG = {};			
			
			ref.filtroJG.dependencia=ref.moduloJG.componentesJG.cmbDependenciaFuncionario.val();
			ref.filtroJG.registro=ref.moduloJG.componentesJG.txtRegistroFiltro.val();
			ref.filtroJG.nombres=ref.moduloJG.componentesJG.txtNombres.val();
			ref.filtroJG.apellidos=ref.moduloJG.componentesJG.txtApellidos.val();			
			ref.searchFuncionarios();
			
			sessionStorage.setItem("FILTRO_ADM_FUNCIONARIOS", JSON.stringify(ref.filtroJG));
		},
		
		searchFuncionarios: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);	
			ref.inicializarTablaConsulta();
		},
		
		exportarExcel: function(){
			var ref = this;
			ref.modSistcorr.procesar(true);
			ref.moduloJG.exportarExcel(ref.filtroJG)
				.then(function(respuesta){
					if (navigator.appVersion.toString().indexOf('.NET') > 0){
						window.navigator.msSaveOrOpenBlob(respuesta, 'reporte_AdministracionFuncionarios.xlsx');
					} else {
						var a = document.createElement('a');
	                    a.href = URL.createObjectURL(respuesta);
	                    a.download = 'reporte_AdministracionFuncionarios.xlsx';
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
		
		inicializarTablaConsulta: function(){
			var ref = this;
			if(ref.dataTableConsulta){
				ref.dataTableConsulta.destroy();
				ref.moduloJG.componentesJG.dataTableConsulta.empty();
				ref.dataTableConsulta = null;
				ref.inicializarTablaConsulta();
			} else {
				ref.moduloJG.componentesJG.dataTableConsulta.show();
				ref.dataTableConsulta = ref.moduloJG.componentesJG.dataTableConsulta.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.moduloJG.URL_CONSULTA_ADMIN_FUNCIONARIOS,
			        	"type": "GET",
			        	"data": ref.filtroJG,
			        	"dataFilter": function(result){
			        		if(result != null && result != "null"){
			        			var response = JSON.parse(result);
			        			datosConsulta = response.datos[0].listOfDataObjects;
			        			var dtFilter = {
			        				"draw": Number(response.datos[0].draw),
			        				"recordsFiltered": Number(response.datos[0].recordsFiltered),
			        				"recordsTotal": Number(response.datos[0].recordsTotal),
			        				"data": response.datos[0].listOfDataObjects || []
			        			}
			        		}else{
			        			var dtFilter = {
			        					"draw": 0,
				        				"recordsFiltered": 0,
				        				"recordsTotal": 0,
				        				"data": []
			        			};
			        		}
			        		return JSON.stringify( dtFilter );
			        	},
			        	'error': function(result){
			        		console.log("Error Consulta Correspondencia");
			        		console.log(result);
			        		ref.modSistcorr.procesar(false);
			        		//location.reload();
			        	}
			        },
			        cache: true,
			        //"pageLength": 10,
			        "columns": [
			        	{data: 'id', title: 'Elim.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-trash-alt icon_eliminar btnEliminarFuncionario' data-toggle='tooltip' title='Clic para eliminar el registro' data-id='" + full.id +"' data-registro='" + full.registro +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'id', title: 'Mod.', defaultContent: '', render: function(data, type, full){
			        		return "<i class='far fas fa-edit icon_editar btnModificarFuncionario'  data-toggle='tooltip' title='Clic para modificar el registro' data-id='" + full.id +"' style='cursor:pointer'></i>"
			        	}},
			        	{data: 'registro', title: 'Registro', defaultContent: ''},
			        	{data: 'nombreApellidos', title: 'Nombres y Apellidos', defaultContent: ''},
			        	{data: 'email', title: 'Email', defaultContent: ''},
			        	{data: 'codigoDependencia', title: 'Código Dependencia', defaultContent: ''},
			        	{data: 'nombreDependencia', title: 'Nombre Dependencia', defaultContent: ''}
			        ],
			        "initComplete": function(settings, json){
			        	setTimeout(function() {
			        		var order = sessionStorage.getItem("ColOrd_JG");
			        		console.log("order:" + order);
			        		if(order!=null && order != "undefined"){
				        		var critOrder = order.split(",");
				        		console.log("ORDER SESSION:" + parseInt(critOrder[0]) + ",'" + critOrder[1] + "'");
				        		var colOrd = critOrder[1];
				        		ref.dataTableConsulta.order([parseInt(critOrder[0]), colOrd]).draw();
			        		}
			        	}, 1000);
			        	setTimeout(function() {
			        		ref.dataTableConsulta.responsive.rebuild();
			        		ref.dataTableConsulta.responsive.recalc();
			        		ref.modSistcorr.procesar(false);
			        		var nroPag = sessionStorage.getItem("NroPag_JG");
			        		console.log("NroPag:" + nroPag);
			        		if(nroPag==null){
								nroPag = 0;
							}
			        		var origPag = sessionStorage.getItem("origPag");
			        		console.log("OrigPag:" + origPag);
			        		if(origPag == "verDetalleBS"){
			        			ref.dataTableConsulta.page(parseInt(nroPag)).draw('page');
			        			sessionStorage.removeItem("origPagJG");
			        		}
			        	}, 2000);
			        },

				});
				
				ref.dataTableConsulta.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					ref.updateEventosTabla();
					ref.moduloJG.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.moduloJG.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'page.dt', function () {
					var pagActual = ref.dataTableConsulta.page.info();
					sessionStorage.setItem("NroPag_JG", pagActual.page);
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.moduloJG.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				});
				
				ref.moduloJG.componentesJG.dataTableConsulta.on( 'order.dt', function () {
					var tableOrder = $("#tablaConsultaCorrespondenciasJG").dataTable();
					var api = tableOrder.api();
					var order = tableOrder.api().order();
					console.log("Order by:" + order);
					if(order != "0,asc"){
						sessionStorage.setItem("ColOrd_JG", order);
					}
	        		setTimeout(function() {
						ref.updateEventosTabla();
						ref.moduloJG.btnModificarFuncionario = $(".btnModificarFuncionario");
						ref.moduloJG.btnEliminarFuncionario = $(".btnEliminarFuncionario");
						ref.eventoDocumentoPrincipal();
					}, 1500);
				} );
				
				setTimeout(function() {
					ref.updateEventosTabla();
					ref.moduloJG.btnModificarFuncionario = $(".btnModificarFuncionario");
					ref.moduloJG.btnEliminarFuncionario = $(".btnEliminarFuncionario");
					ref.eventoDocumentoPrincipal();
				}, 1500);
			}
		},
		
		updateEventosTabla: function(){
			var ref = this;
			ref.modSistcorr.eventoTooltip();
			var allBtnsDetalle = document.querySelectorAll('.icon_view_detail');
			for(var i = 0; i < allBtnsDetalle.length; i++){
				allBtnsDetalle[i].addEventListener('click', function(){
					ref.moduloJG.irADetalle(this.dataset.id);
				});
			}
		},
		
		guardarFuncionario : function(){
			var ref=this;
			ref.modSistcorr.procesar(true);
			
			ref.parametros = {
				'id' : ref.moduloJG.objetoFuncionarios.id,
				'registro' : ref.moduloJG.objetoFuncionarios.registro.val(),
				'primerNombre' : ref.moduloJG.objetoFuncionarios.primerNombre.val(),
				'segundoNombre' : ref.moduloJG.objetoFuncionarios.segundoNombre.val(),
				'apellidoPaterno' : ref.moduloJG.objetoFuncionarios.apellidoPaterno.val(),
				'apellidoMaterno':ref.moduloJG.objetoFuncionarios.apellidoMaterno.val(),
				'dependencia' :ref.moduloJG.objetoFuncionarios.dependencia.val(),
				'email' :ref.moduloJG.objetoFuncionarios.email.val(),
				'ficha' :ref.moduloJG.objetoFuncionarios.ficha.val(),
				'operacion' :ref.moduloJG.objetoFuncionarios.operacion.val(),
				'tipoFuncionario' :ref.moduloJG.objetoFuncionarios.tipoFuncionario.val(),
				'notificaciones':ref.moduloJG.objetoFuncionarios.notificaciones.val(),
				'participaProceso' : ref.moduloJG.objetoFuncionarios.participa.val(),
				'activo':ref.moduloJG.objetoFuncionarios.activo.val(),
				'supervisor':ref.moduloJG.objetoFuncionarios.supervisor.val(),
				'accion':ref.moduloJG.objetoFuncionarios.accion
			};
			
			if($("#txtRegistro").val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Registro es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if($("#txtPrimerNombre").val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Primer Nombre es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if($("#txtApellidoPaterno").val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Apellido Paterno es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbDepend").val() != undefined && $("#cmbDepend").val() != null && $("#cmbDepend").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Dependencia es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if($("#txtEmail").val().trim() == ""){
				ref.modSistcorr.notificar("ERROR", "El campo Email es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbNotificaciones").val() != undefined && $("#cmbNotificaciones").val() != null && $("#cmbNotificaciones").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Recibe notificaciones es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbParticipa").val() != undefined && $("#cmbParticipa").val() != null && $("#cmbParticipa").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Participa en proceso es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			if(!($("#cmbActivo").val() != undefined && $("#cmbActivo").val() != null && $("#cmbActivo").val().trim() != "")){
				ref.modSistcorr.notificar("ERROR", "El campo Activo es obligatorio", "Warning");
				ref.modSistcorr.procesar(false);
				return;
			}
			
			ref.moduloJG.guardarFuncionarios(ref.parametros)
			.then(function(respuesta){
				console.log("Respuesta.estado:" + respuesta.estado)
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					ref.modSistcorr.procesar(false);
					ref.moduloJG.comRegAdicion.myModal.modal('hide');
					ref.inicializarTablaConsulta();
				}else{
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
					ref.modSistcorr.procesar(false);
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.moduloJG.comRegAdicion.myModal.modal('hide');
				ref.modSistcorr.showMessageErrorRequest(error);
			});
			
		},
		
		eventoDocumentoPrincipal: function(){
			var ref = this;			
			
			ref.moduloJG.btnModificarFuncionario.off('click').on('click',function(){
				var comp=$(this);
				var idFuncionario= comp.attr("data-id");
				for (var i in datosConsulta){
					var func=datosConsulta[i];
					if (func.id == idFuncionario){
						ref.moduloJG.objetoFuncionarios.id=func.id;
						ref.moduloJG.objetoFuncionarios.registro.val(func.registro);
						ref.moduloJG.objetoFuncionarios.registro.change();
						ref.moduloJG.objetoFuncionarios.primerNombre.val(func.nombre1);
						ref.moduloJG.objetoFuncionarios.primerNombre.change();
						ref.moduloJG.objetoFuncionarios.segundoNombre.val(func.nombre2);
						ref.moduloJG.objetoFuncionarios.segundoNombre.change();
						ref.moduloJG.objetoFuncionarios.apellidoPaterno.val(func.apellido1);
						ref.moduloJG.objetoFuncionarios.apellidoPaterno.change();
						ref.moduloJG.objetoFuncionarios.apellidoMaterno.val(func.apellido2);
						ref.moduloJG.objetoFuncionarios.apellidoMaterno.change();
						ref.moduloJG.objetoFuncionarios.dependencia.val(func.codigoDependencia);
						ref.moduloJG.objetoFuncionarios.dependencia.change();
						ref.moduloJG.objetoFuncionarios.email.val(func.email);
						ref.moduloJG.objetoFuncionarios.email.change();
						ref.moduloJG.objetoFuncionarios.ficha.val(func.ficha);
						ref.moduloJG.objetoFuncionarios.ficha.change();
						ref.moduloJG.objetoFuncionarios.operacion.val(func.operaciones);
						ref.moduloJG.objetoFuncionarios.operacion.change();
						ref.moduloJG.objetoFuncionarios.tipoFuncionario.val(func.tipoFuncionario);
						ref.moduloJG.objetoFuncionarios.tipoFuncionario.change();
						ref.moduloJG.objetoFuncionarios.notificaciones.val(func.notificaciones);
						ref.moduloJG.objetoFuncionarios.notificaciones.change();
						ref.moduloJG.objetoFuncionarios.participa.val(func.proceso);
						ref.moduloJG.objetoFuncionarios.participa.change();
						ref.moduloJG.objetoFuncionarios.activo.val(func.activo);
						ref.moduloJG.objetoFuncionarios.activo.change();
						ref.moduloJG.objetoFuncionarios.supervisor.val(func.supervisor);
						ref.moduloJG.objetoFuncionarios.supervisor.change();
						ref.moduloJG.objetoFuncionarios.accion="M";
						break;
					}
				}
				setTimeout(function(){
					//ref.modulo.compModalAddUpdate.modal('show');
					ref.moduloJG.comModificarAdicion.myModal.modal('show');
				}, 300);
			}),
			
			
			ref.moduloJG.btnEliminarFuncionario.off('click').on('click', function(){
				var comp = $(this);
				var idFuncionario = comp.attr("data-id");
				var registro=comp.attr("data-registro")
				ref.moduloJG.objetoFuncionarios.id = idFuncionario;
				ref.moduloJG.objetoFuncionarios.registro = registro;
				setTimeout(function(){
					ref.abrirModalElimAdicion();
					console.log("eventoDocumentoPrincipal - abrirá modal Eliminar");
				}, 500);
			}),
			
			ref.moduloJG.btnElimFuncionario.off('click').on('click', function(){
				ref.eliminarFuncionario();
			}),
			
			ref.moduloJG.btnGuardarFuncionario.off('click').on('click', function(){
				ref.guardarFuncionario();
			});
		}
};

setTimeout(function() {
	REEMPLAZOS_ADICION_VISTA.moduloJG = modulo_reemplazos_adicion;
	REEMPLAZOS_ADICION_VISTA.modSistcorr = modulo_sistcorr;
	REEMPLAZOS_ADICION_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	REEMPLAZOS_ADICION_VISTA.jefe = ES_JEFE;
	REEMPLAZOS_ADICION_VISTA.listas = {};
	REEMPLAZOS_ADICION_VISTA.listas.dependencias = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.listas.originadores = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.listas.dependencias_ext = new LISTA_DATA([]);
	REEMPLAZOS_ADICION_VISTA.inicializar();
}, 200);