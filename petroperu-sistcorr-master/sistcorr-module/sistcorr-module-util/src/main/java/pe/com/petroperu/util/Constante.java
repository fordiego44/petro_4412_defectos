package pe.com.petroperu.util;


public class Constante
{
  public static final int EMISION_INTERNA = 1;
  public static final int EMISION_EXTERNA = 2;
  public static final Long CORRESPONDENCIA_SIN_ASIGNAR = Long.valueOf(1L);
  public static final Long CORRESPONDENCIA_ASIGNADA = Long.valueOf(2L);
  public static final Long CORRESPONDENCIA_FIRMADA = Long.valueOf(3L);
  public static final Long CORRESPONDENCIA_COMPLETADA = Long.valueOf(4L);
  public static final Long CORRESPONDENCIA_ANULADA = Long.valueOf(5L);
  public static final Long CORRESPONDENCIA_POR_CORREGIR = Long.valueOf(6L);
  public static final Long CORRESPONDENCIA_ENVIADA = Long.valueOf(7L);
  public static final Long CORRESPONDENCIA_SIN_FIRMA_MANUAL = Long.valueOf(8L);
  public static final Long CORRESPONDENCIA_SIN_DOCUMENTOS = Long.valueOf(9L);
  public static final Long CORRESPONDENCIA_DECLINADA_PENDIENTE = Long.valueOf(10L);
  public static final Long CORRESPONDENCIA_DECLINADA_FIRMADA = Long.valueOf(11L);
  public static final Long CORRESPONDENCIA_APROBADA = Long.valueOf(12L); // adicion 9-3874
  public static final Long CORRESPONDENCIA_REINICIO = Long.valueOf(13L); // adicion 9-3908
  public static final String TIPO_CORRESPONDENCIA_DOC_PAGAR_ID = "81"; // TICKET 9000004765
  public static final int CORRESPONDENCIA_DOCUMENTO_RESPUESTA_ENLAZADO = 1; // TICKET 9000004044
  public static final int CORRESPONDENCIA_DOCUMENTO_RESPUESTA_DESLIGADO = 2; // TICKET 9000004044

  
  public static final String TIPO_CORRESPONDENCIA_OTROS = "OTROS";
  
  /**
   * MOTIVO DE RECHAZO
   */
  public static final Long DOCUMENTO_POR_CORREGIR = Long.valueOf(1L);
  /**
   * MOTIVO DE RECHAZO
   */
  public static final Long DOCUMENTO_POR_ANULAR = Long.valueOf(2L);
  /**
   * MOTIVO DE RECHAZO
   */
  public static final Long DOCUMENTO_MAL_ASIGNADO = Long.valueOf(3L);
  /**
   * MOTIVO DE RECHAZO
   */
  public static final Long DOCUMENTO_REQUIERE_VISTOS_PREVIOS = Long.valueOf(4L);
  
  public static final Long ROL_JEFE = Long.valueOf(1L);
  public static final Long ROL_EJECUTOR = Long.valueOf(2L);
  public static final Long ROL_GESTOR = Long.valueOf(3L);
  public static final Long ROL_ADMINISTRADOR = Long.valueOf(4L);
  public static final Long ROL_USUARIO = Long.valueOf(5L);
  public static final Long ROL_EXTERNO = Long.valueOf(6L);
  
  public static final String MENU_CORRESPONDENCIA = "Bandeja de entrada";
  
  public static final String MENU_FIRMA = "Bandeja de salida";
  
  // TICKET 9000003514
  public static final String MENU_CONSULTA = "Consultas";
  // FIN TICKET 9000003514
  
  public static final Integer FIRMA_PENDIENTES = Integer.valueOf(3); // TICKET 9000003944, AUMENTO DE ORDEN + 2
  public static final Integer FIRMA_FIRMADOS = Integer.valueOf(4); // TICKET 9000003944, AUMENTO DE ORDEN + 2
  public static final Integer FIRMA_ENVIADOS = Integer.valueOf(5); // TICKET 9000003944, AUMENTO DE ORDEN + 2
  
  // TICKET 9000003997
  public static final String DESTINO_RESPUESTA_ENVIADO = "ENTREGADA";
  public static final String DESTINO_RESPUESTA_ACEPTADO = "ACEPTADA";
  public static final String DESTINO_RESPUESTA_RECHAZADO = "RECHAZADA";
  // FIN TICKET
  
  	//TICKET 9000004510
  	public static final String INDICADOR_LOCAL_ARCHIVO_ADJUNTO = "N";
  	public static final String INDICADOR_REMOTO_ARCHIVO_ADJUNTO = "S";
  	// FIN TICKET
    
    // TICKET 7000003969
    public static final String SESSION_MENU = "menus";
    public static final String SESSION_MOTIVOS_RECHAZO = "motivosRechazo";
    public static final String SESSION_MOTIVOS_RECHAZO_RESPONSABLE = "motivosRechazoResponsable";
    public static final String SESSION_MOTIVOS_RECHAZO_FIRMADO = "motivosRechazoFirmado";
    public static final String SESSION_DEPENDENCIAS_GESTOR = "dependenciasGestor";
    public static final String SESSION_DEPENDENCIAS_BES = "dependenciasBandES";//ticket 9000003866
    public static final String SESSION_ROLES_USUARIO = "rolesUsuario";
    public static final String SESSION_ESTADOS_FILENET = "estadosFilenet";
    public static final String SESSION_TIPOS_CORR = "tiposCorrespondencia";
    public static final String SESSION_DEPENDENCIAS_FILENET = "dependenciasFilenet";
    public static final String SESSION_DEPENDENCIAS_REMITENTE_FILENET = "dependenciasRemitenteFilenet";
    public static final String SESSION_ESTADOS_ASIGNACION = "estadosAsignacion";
    public static final String SESSION_DEPENDENCIAS_ASIGNACION = "dependenciasAsignacion";
    public static final String SESSION_PERSONA_ASIGNADA = "personasAsignada";
    public static final String SESSION_ACCIONES = "acciones";
    public static final String SESSION_DEPENDENCIAS = "dependencias";
    public static final String SESSION_DEPENDENCIAS_TODAS = "dependenciasTodas";//ticket 9000004873
    public static final String SESSION_TIPOS_CORR_EMISION = "tiposCorrespondenciaEmision";
    public static final String SESSION_TIPOS_EMISION = "tiposEmision";
    // FIN TICKET
    
    // TICKET 9000004494
    public static final String[] COLUMNAS_CONSULTA_CORRESPONDENCIA = {"Correlativo", "Correlativo", "NroDocInterno", "Asunto", "FechaRadicado", "Origen", "Destino", "Estado", "TipoCorrespondencias"};
    public static final String[] COLUMNAS_CONSULTA_ASIGNACION = {"Radicado", "NroDocInterno", "Asunto", "FechaDocumento", "FechaRecepcion", "Remitente", "FechaAsig", "Asignado", "TextoAsignacion", "Solicitante", "Estado", "FechaPlazoRta", "DocumentoRespuesta"};
    public static final String[] COLUMNAS_CONSULTA_GENERAL = {"id_correspondencia", "correlativo", "asunto", "destinatario_dependencia", "dependencia", "estado", "fechaDocumento", "dependenciaOriginadora", "originador", "lugarTrabajo", "tipoCorrespondencia", "emision_nombre", "firmaDigital", "despachoFisico", "urgente", "confidencial", "destinatario_cgc", "copia_dependencia", "copia_cgc", "responsable", "fechaUltActualizacion"};
    public static final String[] COLUMNAS_CONSULTA_JEFE_GESTOR = {"id_correspondencia", "correlativo", "asunto", "destinatario_dependencia", "dependencia", "estado", "fechaDocumento", "dependenciaOriginadora", "originador", "lugarTrabajo", "tipoCorrespondencia", "emision_nombre", "firmaDigital", "despachoFisico", "urgente", "confidencial", "destinatario_cgc", "copia_dependencia", "copia_cgc", "responsable", "fechaUltActualizacion"};
    // FIN TICKET
    /* 9000004276 - INICIO */
	public static final String[] COLUMNAS_CONSULTA_COMPROBANTES = { "Correlativo", "FechaRecepcion", "Correlativo", "NroBatch", "RUC", "RazonSocial", "DescComprobante", "NroComprobante", "FechaComprobante", "Moneda", "Estado", "Dependencia" };
	public static final String[] COLUMNAS_CONSULTA_CONTRATACIONES = { "NroProceso", "NroProceso", "TipoProceso", "NroMemo", "Dependencia", "PersonaContacto", "CantidadBases", "ValorBase", "vbFechaHoraIni", "vbFechaHoraFin" };
    /* 9000004276 - FIN */
    // TICKET 9000004497
    public static final String SESSION_ASIGNACION_GRUPAL = "asignacionGrupal";
   // FIN TICKET
    // TICKET 9000004651
    public static final Long SOY_AUTOR = 1L;
    public static final Long VISTO_BUENO = 2L;
    public static final Long ULTIMO_FIRMANTE = 3L;
    // FIN TICKET
  
    /*INI Ticket 9000004412*/
    public static final String[] COLUMNAS_CONSULTA_DESPACHO = { "Correlativo", "FechaRecepcion", "Asunto", "NroDocInterno", "Destino", "Origen", "Estado", "TipoCorrespondencia", "DesdeEmision"};
    public static final String[] COLUMNAS_CONSULTA_VALIJAS_RECIBIDAS = { "CorrelativoGR", "FechaRadicado", "CGCRecibe", "CGCRemitente", "Courrier", "Guia", "Cantidad","Estado"};
    /*FIN Ticket 9000004412*/
    
    //INICIO TICKET 9000004275
    public static final String[] COLUMNAS_CONSULTA_DEPARTAMENTOS_GEOGRAFICOS = {"Id", "CodigoDepartamento", "Departamento"};
    public static final String[] COLUMNAS_CONSULTA_PAISES = {"Id", "CodigoPais", "Pais"};
    public static final String[] COLUMNAS_CONSULTA_JERARQUIA = {"Id", "CodigoJerarquia", "Jerarquia"};
    public static final String[] COLUMNAS_CONSULTA_COURIERS = {"Id", "CodigoCourier", "Courier"};
    public static final String[] COLUMNAS_CONSULTA_TIPO_COMPROBANTE = {"Id", "CodigoComprobante", "DescComprobante"};
    public static final String[] COLUMNAS_CONSULTA_MONEDA = {"Id", "CodigoMoneda", "Moneda"};
    public static final String[] COLUMNAS_CONSULTA_FORMA_ENVIO = {"Id", "CodigoFormaEnvio", "FormaEnvio"};
    public static final String[] COLUMNAS_CONSULTA_NUMERADOR = {"Id", "CodigoNumerador", "UltimoCorrelativo"};
    public static final String[] COLUMNAS_CONSULTA_ESTADO = {"Id", "CodigoEstado", "Estado","CodigoProceso"};
    public static final String[] COLUMNAS_CONSULTA_MOTIVOS = {"Id", "CodigoMotivo", "Motivo"};
    public static final String[] COLUMNAS_CONSULTA_TIPO_UM = {"Id", "Nombre"};
    public static final String[] COLUMNAS_CONSULTA_PROVINCIA = {"Id", "CodigoProvincia", "Provincia","Departamento"};
    public static final String[] COLUMNAS_CONSULTA_CIUDAD = {"Id", "CodigoCiudad", "Ciudad","Pais"};
    public static final String[] COLUMNAS_CONSULTA_CGC = {"Id", "CodigoCGC", "Nombre","TipoRotulo","MCodigoBarras","Impresora","TipoImpresora","LugarTrabajo","MComputarizado","CodigoERP"};
    public static final String[] COLUMNAS_CONSULTA_DISTRITO = {"Id", "CodigoDistrito", "Distrito","Departamento","Provincia", "CodigoPostal"};
    public static final String[] COLUMNAS_CONSULTA_GESTOR_DEPENDENCIA = {"Id", "Dependencia", "NombreApellido"};
    public static final String[] COLUMNAS_CONSULTA_USUARIO_CGC = {"Id", "Nombre", "NombreApellido"};
    public static final String[] COLUMNAS_CONSULTA_LUGARES_TRABAJO = {"Id", "CodigoLugar", "Nombre","Direccion","Departamento","Provincia","Distrito"};
    public static final String[] COLUMNAS_CONSULTA_COURIER_LUGAR_TRABAJO = {"Id", "NombreCGC", "Courier","Alcance"};
    public static final String[] COLUMNAS_CONSULTA_PROVINCIA_LUGAR_TRABAJO = {"Id", "Departamento", "Provincia","Nombre"};
    public static final String[] COLUMNAS_CONSULTA_DEPENDENCIA_EXTERNA = {"Id", "RUC", "Dependencia","Direccion","Departamento","Provincia","Distrito","Pais","Ciudad","Email"};
    public static final String[] COLUMNAS_CONSULTA_TIPO_ACCION = {"Id", "CodigoAccion", "Accion","MTextoReq","Mmutiple","MReqTextoRta","MEnviaMailRta","Procesos","Prioridad"};
    public static final String[] COLUMNAS_CONSULTA_TIPO_CORRESPONDENCIA = {"Id", "CodigoTipoCorr", "TipoCorrespondencia","MAplicaEnvInterna","MAplicaEnvExterna","MAplicaRecInterna","MAplicaRecExterna","MRequiereFecha","MFinalizaAceptar","MManualCorresp","Secuencia","MMultiple","ReqCopia"};
    public static final String[] COLUMNAS_CONSULTA_TRANSACCIONES_CGC = {"Id", "tipoTransaccion", "cgcOrigen","cgcDestino","Numerador"};
    public static final String[] COLUMNAS_CONSULTA_LOG = {"NombreTabla","Transaccion","Usuario","FechaHora","IdAfectado","Mensaje"};
    public static final String[] COLUMNAS_CONSULTA_CGC_LUGAR_TRABAJO = {"Id", "CGC", "LugarTrabajo"};
    public static final String[] COLUMNAS_CONSULTA_FUNCIONARIO ={"Registro","NombreApellido","Email","CodigoDependencia","Dependencia"};
    public static final String[] COLUMNAS_REEMPLAZOS_ROLES_ANTERIORES={"Radicado","NroDocInterno","Asunto","FechaRecepcion","Destino","Origen","Estado","TipoCorrespondencias","Rol"};
    public static final String[] COLUMNAS_TIPOS_REEMPLAZOS = {"UsuarioSaliente", "UsuarioEntrante", "ROL", "Dependencia", "FechaInicio", "FechaTermino", "Estado"};
    //FIN TICKET 9000004275
}
