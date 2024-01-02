/*9000004412*/
var modulo_registrar_ingreso = MODULO_REGISTRAR_INGRESO.getInstance(SISTCORR);
var modulo_sistcorr = MODULO_SISTCORR.getInstance(SISTCORR);

var REGISTRAR_INGRESO_VISTA = {
		modSistcorr: null,
		modulo: null, 
		tracking:{},
		dataTableRegistrarIngreso:null,
		procesando: false,
		inicializar: function(){
			var ref = this;
			ref.modSistcorr.cargarVista(); 
			ref.inicializarComponentes();
		},
		
		inicializarComponentes: function(){
			var ref = this; 
			
			ref.modulo.btnRegistarIngreso.click(function(event){
				console.log('Registar Ingreso');
				ref.registrarIngreso();
			});
		 
		},
		
		registrarIngreso: function(){
			console.log('registarIngreso');
			var ref=this;
			
			ref.tracking.correlativo=this.modulo.componentesCC.correlativo.val();
			ref.tracking.codFuncionario=this.modulo.componentesCC.cmpFuncionario.val();
			
			ref.modSistcorr.procesar(true);
			var _tracking = ref.tracking;
			console.log('se llena el objeto tracking');
			ref.modulo.registarTracking(_tracking)
			.then(function(respuesta){
				if(respuesta.estado == true){
					ref.modSistcorr.notificar("OK", respuesta.mensaje, "Success");
					console.log("Tracking Grabado.");
					console.log(respuesta);
					setTimeout(function() {
						ref.inicializarTablaRegistrarIngreso(ref.tracking.correlativo);
					}, 2000);
					ref.modSistcorr.procesar(false);
				} else {
					ref.modSistcorr.procesar(false);
					ref.modSistcorr.notificar("ERROR", respuesta.mensaje, "Error");
				}
				
			}).catch(function(error){
				ref.modSistcorr.procesar(false);
				ref.modSistcorr.showMessageErrorRequest(error);
			});
		},
		//METODOS
		
		inicializarTablaRegistrarIngreso: function(correlativo){
			var ref = this;
			if(ref.dataTableRegistrarIngreso){
				ref.dataTableRegistrarIngreso.destroy();
				ref.modulo.componentesCC.dataTableRegistrarIngreso.empty();
				ref.dataTableRegistrarIngreso = null;
				ref.inicializarTablaRegistrarIngreso();
			} else {
				ref.modulo.componentesCC.dataTableRegistrarIngreso.show();
				ref.dataTableRegistrarIngreso = ref.modulo.componentesCC.dataTableRegistrarIngreso.DataTable({
					"dom": DatatableAttachments.domsimple,
					"language": DatatableAttachments.language,
					"processing": true,
					"serverSide": true,
			        "responsive": true,
			        "ordering"	: true,
			        ajax: {
			        	"url": ref.modulo.URL_BUSQUEDA_TRACKING_INGRESO,
			        	"type": "GET",
			        	//"data":this.modulo.componentesCC.correlativo.val(),
			        	"data": {
			        		'correlativo' : this.modulo.componentesCC.correlativo.val(),
						
			        	},
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
			        	{data: 'item', title: 'Item', defaultContent: ''},
			        	{data: 'tipo', title: 'Tipo', defaultContent: ''},
			        	{data: 'correlativo', title: 'Correlativo', defaultContent: ''},
			        	{data: 'fechaHora', title: 'Fecha y Hora de Registro', defaultContent: ''}			        	
			        ],
			        "initComplete": function(settings, json){
			        	ref.modSistcorr.procesar(false);
			        	setTimeout(function() {
			        		ref.dataTableRegistrarIngreso.responsive.rebuild();
			        		ref.dataTableRegistrarIngreso.responsive.recalc();
			        	}, 1000);
			        },

				});
				
				ref.dataTableRegistrarIngreso.on('responsive-display', function ( e, datatable, row, showHide, update ) {
					
				});
				
				ref.modulo.componentesCC.dataTableRegistrarIngreso.on( 'page.dt', function () {
					
	        		setTimeout(function() {
						
					}, 1500);
				});
				
				ref.modulo.componentesCC.dataTableRegistrarIngreso.on( 'order.dt', function () {
					
	        		setTimeout(function() {
						
					}, 1500);
				} );
				
				setTimeout(function() {
					
				}, 1500);
			}
		},

};

setTimeout(function(){
	REGISTRAR_INGRESO_VISTA.modSistcorr = modulo_sistcorr;
	REGISTRAR_INGRESO_VISTA.modulo = modulo_registrar_ingreso;
	REGISTRAR_INGRESO_VISTA.modulo.modSistcorr = modulo_sistcorr; 
	
	REGISTRAR_INGRESO_VISTA.tiposCorrespondencia = TIPOS_CORRESPONDENCIA;
	REGISTRAR_INGRESO_VISTA.dependenciasUsuario = DEPENDENCIAS_USUARIO;
	REGISTRAR_INGRESO_VISTA.inicializar();
}, 500);
