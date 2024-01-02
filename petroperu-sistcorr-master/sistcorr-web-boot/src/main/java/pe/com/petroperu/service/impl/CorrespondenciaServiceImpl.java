package pe.com.petroperu.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sound.midi.Track;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.ad.util.RolAD;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.AgregarAsignacionMasivaParametro;
import pe.com.petroperu.cliente.model.AgregarAsignacionParametro;
import pe.com.petroperu.cliente.model.AsignacionGrupal;
import pe.com.petroperu.cliente.model.AsignacionGrupalRequest;
import pe.com.petroperu.cliente.model.AsignarDependenciaParametro;
import pe.com.petroperu.cliente.model.Bandeja;
import pe.com.petroperu.cliente.model.CerrarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.ContadorBandeja;
import pe.com.petroperu.cliente.model.FiltroConsultaAsignacion;
import pe.com.petroperu.cliente.model.FiltroConsultaAuditoria;
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroConductor;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RechazarAsignacionCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RechazarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RegistrarObservacion;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.emision.AsignarDocumento;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExternaRespuesta;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInternaRespuesta;
import pe.com.petroperu.cliente.model.emision.Folder;
import pe.com.petroperu.cliente.model.emision.PropiedadesDocumento;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.cliente.util.Constante;
import pe.com.petroperu.filenet.dao.IBandejaEntradaCorrespondenciaDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.Accion;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.Conductor;
import pe.com.petroperu.model.ConductorPaginado;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.Estado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Menu;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.TrackingFisico;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.CorrespondenciaEstado;
import pe.com.petroperu.model.emision.DatosFirmante;
import pe.com.petroperu.model.emision.DestinatarioDocPagar;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.MotivoRechazo;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;
import pe.com.petroperu.notificacion.NotificacionService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.IFilenetService;
import pe.com.petroperu.service.IMenuService;
import pe.com.petroperu.service.IRolService;
import pe.com.petroperu.service.model.ItemMenu;
import pe.com.petroperu.service.util.IReport;
import pe.com.petroperu.service.util.ReportExcel;
import pe.com.petroperu.service.util.ReportExcelCABandejaEntrada;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrelativoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;
import pe.com.petroperu.sistcorr.dao.IDatosFirmanteDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioDocPagarDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioRespuestaDAO;
import pe.com.petroperu.sistcorr.dao.IMotivoRechazoDAO;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;
import pe.com.petroperu.sistcorr.dao.SistcorrDAO;



@Service
@PropertySource({"classpath:application.properties"})
public class CorrespondenciaServiceImpl  implements ICorrespondenciaService {
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  
  @Autowired
  private ISistcorrCliente sistcorrCliente;
  
  @Autowired
  private IMenuService menuService;
  
  @Autowired
  private IFilenetService filenetService;
  
  @Autowired
  private SistcorrDAO sistcorrDAO;
  
  @Autowired
  private MessageSource messageSource;
  
  @Value("${sistcorr.directorio}")
  private String DIRECCTORIO_BASE;
  
  private String DIRECTORIO_TEMPORAL = "temporal";
  
  @Autowired
  private IRolService rolService;
  
  @Autowired
  private ICorrelativoDAO correlativoDAO;
  
  @Autowired
  private ICorrespondenciaDAO correspondenciaDAO;
  
  @Autowired
  private IDestinatarioRespuestaDAO destinatarioRespuestaDAO;
  
  @Autowired
  private IRutaAprobacionDAO rutaAprobacionDAO;
  
  @Autowired
  private NotificacionService notificacionService;
  
  @Autowired
  private IMotivoRechazoDAO motivoRechazoDAO;
  
  @Autowired
  @Qualifier("iBandejaEntradaCorrespondenciaDAOImpl")
  private IBandejaEntradaCorrespondenciaDAO bandejaEntradaCorrespondenciaDAO;
  
  // TICKET 9000004044
  @Autowired
  private ICorrespondenciaService correspondenciaService;
  
  // TICKET 9000004510
  @Autowired
  private IArchivoAdjuntoDAO archivoAdjuntoDAO;
  
  @Autowired
  private IDestinatarioDocPagarDAO destDocPagar;//ticket 9000004765

  
  public List<Menu> obtenerMenuSistcorr(UsuarioPetroperu usuario, boolean estado, Locale locale) {
    List<Menu> listaMenu = this.menuService.listarPorUsuario(usuario.getUsername());
    ContadorBandeja contadorPendientes;
	ContadorBandeja contadorEnAtencion;
	ContadorBandeja contadorCompletadas;
	ContadorBandeja contadorDelGestor;
	ContadorBandeja contadorDelReceptor; // Ticket 9*4413
    Respuesta<ContadorBandeja> respuesta = new Respuesta<>();
    
    respuesta = this.sistcorrCliente.contadorBandeja(usuario.getToken(), Constante.BANDEJA_PENDIENTES);
    if (!respuesta.estado)
    	contadorPendientes = new ContadorBandeja(Integer.valueOf(0)); 
    contadorPendientes = respuesta.datos.isEmpty() ? new ContadorBandeja(Integer.valueOf(0)) : respuesta.datos.get(0);
    respuesta = new Respuesta<>();
    respuesta = this.sistcorrCliente.contadorBandeja(usuario.getToken(), Constante.BANDEJA_ATENCION);
    if (!respuesta.estado)
    	contadorEnAtencion = new ContadorBandeja(Integer.valueOf(0)); 
    contadorEnAtencion = respuesta.datos.isEmpty() ? new ContadorBandeja(Integer.valueOf(0)) : respuesta.datos.get(0);
    respuesta = new Respuesta<>();
    respuesta = this.sistcorrCliente.contadorBandeja(usuario.getToken(), Constante.BANDEJA_COMPLETADAS);
    if (!respuesta.estado)
    	contadorCompletadas = new ContadorBandeja(Integer.valueOf(0)); 
    contadorCompletadas = respuesta.datos.isEmpty() ? new ContadorBandeja(Integer.valueOf(0)) : respuesta.datos.get(0);
    respuesta = new Respuesta<>();
    respuesta = this.sistcorrCliente.contadorBandeja(usuario.getToken(), Constante.BANDEJA_DELGESTOR);
    if (!respuesta.estado)
    	contadorDelGestor = new ContadorBandeja(Integer.valueOf(0)); 
    contadorDelGestor = respuesta.datos.isEmpty() ? new ContadorBandeja(Integer.valueOf(0)) : respuesta.datos.get(0);
    
    /*INI Ticket 9*4413*/
    if (!respuesta.estado)
    	contadorDelReceptor = new ContadorBandeja(Integer.valueOf(0)); 
    contadorDelReceptor = respuesta.datos.isEmpty() ? new ContadorBandeja(Integer.valueOf(0)) : respuesta.datos.get(0);
    respuesta = new Respuesta<>();
    respuesta = this.sistcorrCliente.contadorBandeja(usuario.getToken(), Constante.BANDEJA_DELRECEPTOR);
    /*FIN Ticket 9*4413*/
    
    for (Menu menu : listaMenu) {
      if (menu.getIdMenuSuperior() == null && menu.getNombre().equals("Bandeja de entrada")) {
        for (Menu subMenu : menu.getSubMenu()) {
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.pendientes.etiqueta", null, locale)))
            subMenu.setCantidad(contadorPendientes.getTotal()); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.atencion.etiqueta", null, locale)))
            subMenu.setCantidad(contadorEnAtencion.getTotal()); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.completados.etiqueta", null, locale)))
            subMenu.setCantidad(contadorCompletadas.getTotal()); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.delgestor.etiqueta", null, locale)))
        	  subMenu.setCantidad(contadorDelGestor.getTotal());
          /*INI Ticket 9*4413*/
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.delreceptor.etiqueta", null, locale)))
              subMenu.setCantidad(contadorDelReceptor.getTotal()); 
          /*FIN Ticket 9*4413*/
        }  continue;
      }  if (menu.getIdMenuSuperior() == null && menu.getNombre().equals("Bandeja de salida")) {
        for (Menu subMenu : menu.getSubMenu()) {
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_PENDIENTES)) {
            subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("pendiente", usuario.getUsername()).intValue()));
          }
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_FIRMADOS)) {
            subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("firmado", usuario.getUsername()).intValue()));
          }
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_ENVIADOS)) {
            subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("enviado", usuario.getUsername()).intValue()));
          }
        } 
      }
    } 
    return listaMenu;
  }

  
  public List<Menu> obtenerMenuSistcorr(UsuarioPetroperu usuario, Locale locale) {
    List<Menu> listaMenu = this.menuService.listarPorUsuario(usuario.getUsername());

    Respuesta<ContadorBandeja> respuesta = new Respuesta<>();

    
    for (Menu menu : listaMenu) {
      if (menu.getIdMenuSuperior() == null && menu.getNombre().equals("Bandeja de entrada")) {
        for (Menu subMenu : menu.getSubMenu()) {
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.pendientes.etiqueta", null, locale)))
            subMenu.setCantidad(Integer.valueOf(0)); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.atencion.etiqueta", null, locale)))
            subMenu.setCantidad(Integer.valueOf(0)); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.completados.etiqueta", null, locale)))
            subMenu.setCantidad(Integer.valueOf(0)); 
          if (subMenu.getNombre().equals(this.messageSource.getMessage("sistcorr.correspondencia.delgestor.etiqueta", null, locale)))
              subMenu.setCantidad(Integer.valueOf(0)); 
        }  continue;
      }  if (menu.getIdMenuSuperior() == null && menu.getNombre().equals("Bandeja de salida")) {
        for (Menu subMenu : menu.getSubMenu()) {
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_PENDIENTES)) {
            //subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("pendiente", usuario.getUsername()).intValue()));
        	  subMenu.setCantidad(0);
          }
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_FIRMADOS)) {
            //subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("firmado", usuario.getUsername()).intValue()));
        	  subMenu.setCantidad(0);
          }
          if (subMenu.getOrden().equals(pe.com.petroperu.util.Constante.FIRMA_ENVIADOS)) {
            subMenu.setCantidad(Integer.valueOf(this.menuService.obtenerContadorBandeja("enviado", usuario.getUsername()).intValue()));
          }
        } 
      }
    } 
    return listaMenu;
  }

  
  public Respuesta<Bandeja> obtenerColeccion(String token, String bandeja, Locale locale) {
    Respuesta<Bandeja> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.recuperarCorrespondencias(token, bandeja);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recupearBanedaExitoso", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public ResponseEntity<byte[]> descargarDocumentoPrincipal(String token, String idDocumento, Locale locale) {
    ResponseEntity<byte[]> respuesta = this.sistcorrCliente.descargarDocumento(token, idDocumento);
    return respuesta;
  }
  
  // TICKET 9000003514
  public Respuesta<RespuestaApi> eliminarDocumento(String token, String idDocumento, String proceso, String codigoTraza, Locale locale){
	  Respuesta<RespuestaApi> respuesta = this.sistcorrCliente.eliminarDocumento(token, idDocumento, proceso, codigoTraza);
	  return respuesta;
  }
  // FIN TICKET 9000003514

  
  public Respuesta<InformacionDocumento> obtenerDocumentoPrincipal(String token, String correlativo, Locale locale) {
    Respuesta<InformacionDocumento> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.recuperarDocumento(token, correlativo);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarDocumentoExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<Correspondencia> recuperarCorrespondencia(String token, String correlativo, Locale locale) {
    Respuesta<Correspondencia> respuesta = new Respuesta<>();
    try {
    	LOGGER.info("[INICIO] recuperarCorrespondencia " + correlativo);
      //respuesta = this.sistcorrCliente.recuperarCorrespondencia(token, correlativo);
      //TICKET 9000004165 CLAULATE
      Object[] _respuesta = bandejaEntradaCorrespondenciaDAO.obtenerInformacionCorrespondencia(correlativo);
      Correspondencia corresp = new Correspondencia();
      corresp.setCorrelativo((String)_respuesta[0]);
      corresp.setDependenciaDestino((String)_respuesta[1]);
      corresp.setDependenciaRemitente((String)_respuesta[2]);
      corresp.setAsunto((String)_respuesta[3]);
      corresp.setFechaCreacion((String)_respuesta[4]);
      corresp.setCodigoDependenciaDestino(((Integer)_respuesta[5]).toString());
      
      corresp.setCodigoCGC((String)_respuesta[6]);
      corresp.setTipo((String)_respuesta[7]);
      corresp.setUsuarioResponsable((String)_respuesta[8]);
      corresp.setNombreApellidoResponsable((String)_respuesta[9]);
      corresp.setUsuarioRadicador((String)_respuesta[10]);
      corresp.setUsuarioGestor((String)_respuesta[11]);
      
      corresp.setNombreApellidoGestor((String)_respuesta[12]);
      corresp.setNumeroDocumento((String)_respuesta[13]);
      corresp.setEstado((String)_respuesta[14]);
      corresp.setEsConfidencial((String)_respuesta[15]);
      corresp.setProceso((String)_respuesta[16]);
      corresp.setTipoTransaccion((String)_respuesta[17]);
      
      corresp.setCorrelativoReferencia((String)_respuesta[18]);
      corresp.setNumeroFolios(((Integer)_respuesta[19]).toString());
      corresp.setLugarTrabajoDestino((String)_respuesta[20]);
      corresp.setLugarTrabajoRemitente((String)_respuesta[21]);
      corresp.setNombreApellidoPersonaDestino((String)_respuesta[22]);
      corresp.setNombreApellidoPersonaRemitente((String)_respuesta[23]);
      
      corresp.setGuia((String)_respuesta[24]);
      corresp.setGuiaRemision((String)_respuesta[25]);
      corresp.setEsRegistroDesdeDependencia((String)_respuesta[26]);
      corresp.setFechaDocumento((String)_respuesta[27]);
      corresp.setUltimaObservacion((String)_respuesta[28]);
      corresp.setUrgente((String)_respuesta[29]);
      
      corresp.setCgc((String)_respuesta[30]);
      
      corresp.setDocumentoRespuesta((String) _respuesta[31]);
      
      respuesta.estado = true;
      respuesta.mensaje = "200";
      respuesta.datos.add(corresp);
      
      //LOGGER.info("[valor correspondencia correlativo:] " + String.valueOf(_respuesta[0]));
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recupearCorrespondenciaExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<InformacionAsignacion> recuperarInformacionAsignacion(String token, Integer idAsignacion, Locale locale) {
    Respuesta<InformacionAsignacion> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.recuperarAsignacion(token, idAsignacion);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarAsignacionExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<InformacionDocumento> obtenerDocumentosAdjuntos(String token, String correlativo, Locale locale) {
    Respuesta<InformacionDocumento> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.recuperarDocumentos(token, correlativo, locale);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarDocumentosAsjuntosExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  //TICKET 9000004165
  private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
  }
  
  public Respuesta<Asignacion> obtenerHistorialAsignaciones(String token, String correlativo, Locale locale) {
    Respuesta<Asignacion> respuesta = new Respuesta<>();
    try {
      //respuesta = this.sistcorrCliente.recupearHistorial(token, correlativo);//TICKET 9000004165
      UsuarioPetroperu usuario = obtenerUsuario();
      List<Object[]> listObject = this.bandejaEntradaCorrespondenciaDAO.consultarListaHistorialAsignaciones(usuario.getUsername(), correlativo);
      if(listObject != null) {
    	  List<Asignacion> listAsig = new ArrayList<>();
    	  for(Object[] row : listObject) {
    		  Asignacion asig = new Asignacion();
    		  asig.setFechaAsignacion(row[0] != null?row[0].toString():"");
    		  asig.setAccion(row[1] != null?row[1].toString():"");
    		  asig.setNombreApellidoAsignador(row[2] != null?row[2].toString():"");
    		  asig.setNombreApellidoAsignado(row[3] != null?row[3].toString():"");
    		  asig.setFechaLimite(row[4] != null?row[4].toString():"");
    		  asig.setAtendida(row[5] != null?row[5].toString():"");
    		  asig.setFechaRespuesta(row[6] != null?row[6].toString():"");
    		  asig.setDetalleSolicitud(row[7] != null?row[7].toString():"");
    		  asig.setRespuesta(row[8] != null?row[8].toString():"");
    		  asig.setNumeroDocumentoEnlace(row[9] != null?row[9].toString():"");
    		  listAsig.add(asig);
    	  }
    	  
    	  respuesta.estado = true;
          respuesta.mensaje = "200";
          respuesta.datos.addAll(listAsig);
      }
      if(respuesta.estado) {
			respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarHistorialAsignacionesExito", null, locale);
			Collections.sort(respuesta.datos, new Comparator<Asignacion>() {

				@Override
				public int compare(Asignacion o1, Asignacion o2) {
					if(o1.getFechaAsignacionSTR().after(o2.getFechaAsignacionSTR()))
						return -1;
					return 1;
				}
			});
		}
    }
    catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }
  
  public Respuesta<Traza> obtenerListaTrazas(String token, String correlativo, Locale locale) {
	    Respuesta<Traza> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.recuperarTraza(token, correlativo);//TICKET 9000004165
	      List<Object[]> listObjs = this.bandejaEntradaCorrespondenciaDAO.recuperarListaTraza("", correlativo, "");
	      if(listObjs != null) {
				List<Traza> listSP = new ArrayList<>();
				for (Object[] obja : listObjs) {
					Traza obj = new Traza();
					obj.setFecha(obja[0] != null?obja[0].toString():"");
					obj.setUsuario(obja[1] != null?obja[1].toString():"");
					obj.setNomApeUsuario(obja[2] != null?obja[2].toString():"");
					obj.setDetalle(obja[3] != null?obja[3].toString():"");
					obj.setTraza(obja[4] != null?obja[4].toString():"");
					listSP.add(obj);
				}
				
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listSP);
			}
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarListaTrazasExito", null, locale);
				/*Collections.sort(respuesta.datos, new Comparator<Traza>() {

					@Override
					public int compare(Traza o1, Traza o2) {
						if(o1.getFechaSTR().equals(o2.getFechaSTR()))
							return -1;
						return 1;
					}
				});*/
			}
	    }
	    catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}

	public Respuesta<Observaciones> obtenerListaObservaciones(String token, String correlativo, Locale locale) {
	    Respuesta<Observaciones> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.recuperarObservaciones(token, correlativo);//TICKET 9000004165
	      List<Object[]> listObjs = this.bandejaEntradaCorrespondenciaDAO.recuperarListaObservaciones("TD_CR", correlativo, "");
	      if(listObjs != null) {
				List<Observaciones> listSP = new ArrayList<>();
				for (Object[] obja : listObjs) {
					Observaciones obj = new Observaciones();
					obj.setFecha(obja[0] != null?obja[0].toString():"");
					obj.setNomApeUsuario(obja[1] != null?obja[1].toString():"");
					obj.setObservacion(obja[2] != null?obja[2].toString():"");
					listSP.add(obj);
				}
				
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listSP);
			}
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarListaObservacionesExito", null, locale);
				/*Collections.sort(respuesta.datos, new Comparator<Observaciones>() {

					@Override
					public int compare(Observaciones o1, Observaciones o2) {
						if(o1.getFechaSTR().equals(o2.getFechaSTR()))
							return -1;
						return 1;
					}
				});*/
			}
	    }
	    catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<TrackingFisico> obtenerTrackingFisico(String token, String correlativo, Locale locale) {
		Respuesta<TrackingFisico> respuesta = new Respuesta<>();
		try {
			//respuesta = this.sistcorrCliente.recuperarTrackingFisico(token, correlativo);
			List<Object[]> listTracFisi = this.bandejaEntradaCorrespondenciaDAO.consultaTrackingFisico(correlativo);// TICKET 9000004165
			
			if(listTracFisi != null) {
				List<TrackingFisico> listTfis = new ArrayList<>();
				for (Object[] tfisico : listTracFisi) {
					TrackingFisico obTfis = new TrackingFisico();
					obTfis.setFecha(tfisico[0].toString());
					obTfis.setNomApeUsuario(tfisico[1].toString());
					obTfis.setIngreso(tfisico[2].toString());
					obTfis.setEgreso(tfisico[3].toString());
					
					listTfis.add(obTfis);
				}
				
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listTfis);
			}
			
			if (respuesta.estado) {
				respuesta.mensaje = messageSource.getMessage("sistcorr.recuperarListaTrackingFisicoExito", null,
						locale);
				/*
				 * Collections.sort(respuesta.datos, new Comparator<TrackingFisico>() {
				 * 
				 * @Override public int compare(TrackingFisico t1, TrackingFisico t2) {
				 * if(t1.getFechaSTR().equals(t2.getFechaSTR())) return -1; return 1; } });
				 */
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	public Respuesta<CorrespondenciaConsulta> consultarCorrespondencia(String token, FiltroConsultaCorrespondencia filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
	    Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.consultarCorrespondencia(token, filtro);//TICKET 9000004165
	      List<Object[]> listObjs = this.bandejaEntradaCorrespondenciaDAO.consultaCorresRecibidaXProce(obtenerUsuario().getUsername(), filtro.getCorrelativo(), filtro.getFechaRegistroDesde(), filtro.getFechaRegistroHasta(), 
	    			(filtro.getCodigoTipoCorrespondencia() != null?Integer.parseInt(filtro.getCodigoTipoCorrespondencia()):0), (filtro.getCodigoEstado() != null?Integer.parseInt(filtro.getCodigoEstado()):0), filtro.getFechaDocumentoInterno(), 
	    			(filtro.getCodigoDependenciaDestino() != null?Integer.parseInt(filtro.getCodigoDependenciaDestino()):0), (filtro.getCodigoDependenciaRemitente() != null?Integer.parseInt(filtro.getCodigoDependenciaRemitente()):0), filtro.getNombreDependenciaExterna(), filtro.getAsunto(), filtro.getNumeroDocumentoInterno(), filtro.getGuiaRemision(), null, 
	    			filtro.getProcedencia(), itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
	      LOGGER.info("Correspondencias obtenidas:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  listObjs.remove(0);
	    	  List<CorrespondenciaConsulta> consulListCorres = new ArrayList<>();
	    	  LOGGER.info("Correspondencias obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  CorrespondenciaConsulta obj = new CorrespondenciaConsulta();
	    		  obj.setCorrelativo(obja[1] != null?obja[1].toString():"");
	    		  obj.setFechaRadicado(obja[2] != null?obja[2].toString():"");
	    		  obj.setAsunto(obja[3] != null?obja[3].toString():"");
	    		  obj.setNumeroDocumentoInterno(obja[4] != null?obja[4].toString():"");
	    		  obj.setDestino(obja[5] != null?obja[5].toString():"");
	    		  obj.setOrigen(obja[6] != null?obja[6].toString():"");
	    		  obj.setEstado(obja[7] != null?obja[7].toString():"");
	    		  obj.setTipoCorrespondencia(obja[8] != null?obja[8].toString():"");
	    		  obj.setTipoIcono(obja[10] != null?obja[10].toString():"");
	    		  obj.setEsConfidencial(obja[11] != null?obja[11].toString():"");
	    		  obj.setEsAsignado(obja[13] != null?obja[13].toString():"");
	    		  consulListCorres.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(consulListCorres);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
				/*Collections.sort(respuesta.datos, new Comparator<CorrespondenciaConsulta>() {

					@Override
					public int compare(CorrespondenciaConsulta c1, CorrespondenciaConsulta c2) {
						if(c1.getFechaRadicadoProc().equals(c2.getFechaRadicadoProc()))
							return -1;
						return 1;
					}
				});*/
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	

	
	public Respuesta<AsignacionConsulta> consultarAsignaciones(String token, FiltroConsultaAsignacion filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
		this.LOGGER.info("[INICIO] consultarAsignaciones");
	    Respuesta<AsignacionConsulta> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.consultarAsignaciones(token, filtro);
	      List<Object[]> listObjects = this.bandejaEntradaCorrespondenciaDAO.consultaAsignacionesv2(obtenerUsuario().getUsername(), filtro.getCorrelativo(), filtro.getNumeroDocumentoInterno(), 
	    			(filtro.getDependenciaAsignante() != null?Integer.parseInt(filtro.getDependenciaAsignante()):0), filtro.getPersonaAsignada(), 
	    			filtro.getTipoAccion(), (filtro.getCodigoEstado() != null?Integer.parseInt(filtro.getCodigoEstado()):0), 
	    			filtro.getFechaAsignacionDesde(), filtro.getFechaAsignacionHasta(), filtro.getFechaVencimientoDesde(), filtro.getFechaVencimientoHasta(), "", 
	    			itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
	      LOGGER.info("Correspondencias obtenidas:" + listObjects.size());
	      if(listObjects != null) {
	    	  Object[] total = listObjects.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  listObjects.remove(0);
	    	  LOGGER.info("Correspondencias obtenidas:" + listObjects.size());
	    	  List<AsignacionConsulta> listObjRp = new ArrayList<>();
	    	  for(Object[] obja : listObjects) {
	    		  AsignacionConsulta obj = new AsignacionConsulta();
	    		  obj.setIdAsignacion(obja[1] != null?(Integer)obja[1]:0);
	    		  obj.setCorrelativo(obja[2] != null?obja[2].toString():"");
	    		  obj.setNumeroDocumentoInterno(obja[3] != null?obja[3].toString():"");
	    		  obj.setAccion(obja[4] != null?obja[4].toString():"");
	    		  obj.setAsunto(obja[15] != null?obja[15].toString():"");
	    		  obj.setFechaAsignacion(obja[7] != null?obja[7].toString():"");
	    		  obj.setAsignado(obja[8] != null?obja[8].toString():"");
	    		  obj.setSolicitante(obja[9] != null?obja[9].toString():"");
	    		  obj.setEstado(obja[10] != null?obja[10].toString():"");
	    		  obj.setFechaPlazoRespuesta(obja[11] != null?obja[11].toString():"");
	    		  obj.setAvance(obja[12] != null?obja[12].toString():"");
	    		  obj.setTextoAsig(obja[13] != null?obja[13].toString():"");
	    		  obj.setCodigoAccion(obja[5] != null?obja[5].toString():"");
	    		  obj.setTipoIcono(obja[6] != null?obja[6].toString():"");
	    		  obj.setEsConfidencial(obja[17] != null?obja[17].toString():"");
	    		  obj.setEsAsignado(obja[18] != null?obja[18].toString():"");
	    		  obj.setFechaRecepcion(obja[19] != null?obja[19].toString():"");
	    		  obj.setFechaDocumento(obja[20] != null?obja[20].toString():"");
	    		  obj.setRemitente(obja[21] != null?obja[21].toString():"");
	    		  obj.setDocumentoRespuesta(obja[22] != null?obja[22].toString():"");
	    		  listObjRp.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(listObjRp);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_asignaciones.Exito", null, locale);
				/*Collections.sort(respuesta.datos, new Comparator<AsignacionConsulta>() {

					/*@Override
					public int compare(AsignacionConsulta a1, AsignacionConsulta a2) {
						//if(a1.getFechaAsignacion().equals(a2.getFechaAsignacion()))
						//	return -1;
						//return 1;
						a1.getFechaAsignacion().compareToIgnoreCase(a2.getFechaAsignacion());
					}
				});*/
			}
	    }
	    catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	      LOGGER.error("[ERROR]", e);
	    } 
	    return respuesta;
	}
	
	public Respuesta<CopiaCorrespondencia> obtenerCopiaCorrespondencia(String token, String correlativo, Locale locale) {
	    Respuesta<CopiaCorrespondencia> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.recuperarCopiaCorrespondencia(token, correlativo);//TICKET 9000004165
	      List<Object[]> listObjects = this.bandejaEntradaCorrespondenciaDAO.consultarListaFuncionariosCopiados(obtenerUsuario().getUsername(), correlativo);
	      if(listObjects != null) {
	    	  List<CopiaCorrespondencia> listObjRp = new ArrayList<>();
	    	  for(Object[] obja : listObjects) {
	    		  CopiaCorrespondencia obj = new CopiaCorrespondencia();
	    		  obj.setUsuarioCopia(obja[0] != null?obja[0].toString():"");
	    		  obj.setEmail(obja[1] != null?obja[1].toString():"");
	    		  obj.setNombreApellidoUsuario(obja[2] != null?obja[2].toString():"");
	    		  listObjRp.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(listObjRp);
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarListaCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<CopiaCorrespondencia>() {

					@Override
					public int compare(CopiaCorrespondencia o1, CopiaCorrespondencia o2) {
						if(o1.getUsuarioCopia().equals(o2.getUsuarioCopia()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	LOGGER.info("[ERROR] " + e.getMessage());
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<RespuestaApi> eliminarCopiaCorrespondencia(String token, String usuario, String correlativo, Locale locale) {
	    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.eliminarCopiaCorrespondencia(token, usuario, correlativo);//TICKET 9000004165
	      boolean respSP = this.bandejaEntradaCorrespondenciaDAO.eliminarFuncionarioListaCopias(obtenerUsuario().getUsername(), usuario, correlativo);
	      if(respSP) {
	    	  List<Object[]> listObjects = this.bandejaEntradaCorrespondenciaDAO.consultarListaFuncionariosCopiados(obtenerUsuario().getUsername(), correlativo);
		      if(listObjects != null) {
		    	  RespuestaApi resapi = new RespuestaApi();
		    	  List<CopiaCorrespondencia> listObjRp = new ArrayList<>();
		    	  for(Object[] obja : listObjects) {
		    		  CopiaCorrespondencia obj = new CopiaCorrespondencia();
		    		  obj.setUsuarioCopia(obja[0] != null?obja[0].toString():"");
		    		  obj.setEmail(obja[1] != null?obja[1].toString():"");
		    		  obj.setNombreApellidoUsuario(obja[2] != null?obja[2].toString():"");
		    		  listObjRp.add(obj);
		    	  }
		    	  respuesta.estado = true;
		    	  respuesta.mensaje = "200";
		    	  resapi.setStatus(200);
		    	  resapi.setUserMessage("");
		    	  respuesta.datos.add(resapi);
		      }
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.eliminarListaCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<RespuestaApi>() {

					@Override
					public int compare(RespuestaApi o1, RespuestaApi o2) {
						if(o1.getUserMessage().equals(o2.getUserMessage()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	LOGGER.info("[ERROR] " + e.getMessage());
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<CopiaCorrespondencia> agregarCopiaCorrespondencia(String token, String usuario, String correlativo, Locale locale) {
	    Respuesta<CopiaCorrespondencia> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.agregarCopiaCorrespondencia(token, usuario, correlativo);//TICKET 9000004165
	      boolean respSP = this.bandejaEntradaCorrespondenciaDAO.agregarFuncionarioListaCopias(obtenerUsuario().getUsername(), usuario, correlativo);
	      if(respSP) {
	    	  List<Object[]> listObjects = this.bandejaEntradaCorrespondenciaDAO.consultarListaFuncionariosCopiados(obtenerUsuario().getUsername(), correlativo);
		      if(listObjects != null) {
		    	  List<CopiaCorrespondencia> listObjRp = new ArrayList<>();
		    	  for(Object[] obja : listObjects) {
		    		  CopiaCorrespondencia obj = new CopiaCorrespondencia();
		    		  obj.setUsuarioCopia(obja[0] != null?obja[0].toString():"");
		    		  obj.setEmail(obja[1] != null?obja[1].toString():"");
		    		  obj.setNombreApellidoUsuario(obja[2] != null?obja[2].toString():"");
		    		  listObjRp.add(obj);
		    	  }
		    	  respuesta.estado = true;
		    	  respuesta.mensaje = "200";
		    	  respuesta.datos.addAll(listObjRp);
		      }
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.agregarListaCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<CopiaCorrespondencia>() {

					@Override
					public int compare(CopiaCorrespondencia o1, CopiaCorrespondencia o2) {
						if(o1.getUsuarioCopia().equals(o2.getUsuarioCopia()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	LOGGER.info("[ERROR] " + e.getMessage());
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<Funcionario> obtenerFuncionarios(String token, String codigoDependencia, String codigoLugar, String activo, Locale locale) {
	    Respuesta<Funcionario> respuesta = new Respuesta<>();
	    try {
	      //respuesta = this.sistcorrCliente.obtenerFuncionarios(token, codigoDependencia, codigoLugar, activo);//TICKET 9000004165
	      List<Object[]> listaObjst = this.bandejaEntradaCorrespondenciaDAO.consultarListaFuncionarios(Integer.parseInt(codigoDependencia), codigoLugar, "", activo);
	      if(listaObjst != null) {
				List<Funcionario> listObjstre = new ArrayList<>();
				for (Object[] obja : listaObjst) {
					Funcionario obj = new Funcionario();
					obj.setNombreApellidoUsuario(obja[0] != null?obja[0].toString():"");
					obj.setUsuario(obja[1] != null?obja[1].toString():"");
					obj.setCodDependencia(obja[2] != null?obja[2].toString():"");
					obj.setNombreDependencia(obja[3] != null?obja[3].toString():"");
					listObjstre.add(obj);
				}
				
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll(listObjstre);
			}
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarListaCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<Funcionario>() {

					@Override
					public int compare(Funcionario f1, Funcionario f2) {
						//if(f1.getNombreUsuario().equals(f2.getNombreUsuario()))
						if(f1.getNombreApellidoUsuario().equals(f2.getNombreApellidoUsuario()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	LOGGER.info("[ERROR] " + e.getMessage());
	    	LOGGER.info("Respuesta Estado: " + respuesta.estado);
	    	LOGGER.info("Respuesta Mensaje: " + respuesta.mensaje);
	    	LOGGER.info("Respuesta Datos: " + respuesta.datos.size());
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<Funcionario> obtenerFuncionariosRuta(String token, String codigoDependencia, String codigoLugar, String activo, String texto, Locale locale) {
	    Respuesta<Funcionario> respuesta = new Respuesta<>();
	    try {
	      respuesta = this.sistcorrCliente.obtenerFuncionarios(token, codigoDependencia, codigoLugar, activo);
	      List<Funcionario> funcionarios = respuesta.datos.stream()
					.filter(fir -> fir.getNombreApellidoUsuario().toLowerCase().contains(texto.toLowerCase()))
					.collect(Collectors.toList());
	      respuesta.datos = funcionarios;
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.recuperarListaCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<Funcionario>() {

					@Override
					public int compare(Funcionario f1, Funcionario f2) {
						//if(f1.getNombreUsuario().equals(f2.getNombreUsuario()))
						if(f1.getNombreApellidoUsuario().equals(f2.getNombreApellidoUsuario()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	LOGGER.info("[ERROR] " + e.getMessage());
	    	LOGGER.info("Respuesta Estado: " + respuesta.estado);
	    	LOGGER.info("Respuesta Mensaje: " + respuesta.mensaje);
	    	LOGGER.info("Respuesta Datos: " + respuesta.datos.size());
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public Respuesta<RespuestaApi> enviarCopiaCorrespondencia(String token, String texto, String correlativo, Locale locale) {
	    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	    try {
	      respuesta = this.sistcorrCliente.enviarCopiaCorrespondencia(token, texto, correlativo);
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.enviarCopiaCorrespondenciaExito", null, locale);
				Collections.sort(respuesta.datos, new Comparator<RespuestaApi>() {

					@Override
					public int compare(RespuestaApi o1, RespuestaApi o2) {
						if(o1.getUserMessage().equals(o2.getUserMessage()))
							return -1;
						return 1;
					}
				});
			}
	    }
	    catch (Exception e) {
	    	LOGGER.info("[ERROR] " + e.getMessage());
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	
	public List<Estado> listarEstados(String tipo){
		return sistcorrDAO.listarEstados(tipo);
	}
	
	// TICKET 9000003514
	public List<Estado> listarEstadosFile(String tipo){
		return filenetService.listarEstados(tipo);
	}
	
	public List<Estado> listarEstadosAsignacion(){
		return filenetService.listarEstadosAsignacion();
	}
	
	public List<Estado> listarTiposCorrespondencia(){
		return filenetService.listarTiposCorrespondencia();
	}
	// FIN TICKET 9000003514

  
  public Respuesta<Accion> obtenerListaAcciones(String token, Locale locale) {
    Respuesta<Accion> respuesta = new Respuesta<>();
    try {
      //respuesta = this.sistcorrCliente.recuperarAcciones(token);
    	List<Object[]> listTracFisi = this.bandejaEntradaCorrespondenciaDAO.recuperarListaAcciones("TD_CR");// TICKET 9000004165
		
		if(listTracFisi != null) {
			List<Accion> listAccions = new ArrayList<>();
			for (Object[] accion : listTracFisi) {
				Accion obTAccion = new Accion();
				obTAccion.setCodigoAccion(accion[0] != null?accion[0].toString():"");
				obTAccion.setAccion(accion[1] != null?accion[1].toString():"");
				obTAccion.setRequiereRespuesta(accion[2] != null?accion[2].toString():"");
				
				listAccions.add(obTAccion);
			}
			
			respuesta.estado = true;
			respuesta.mensaje = "200";
			respuesta.datos.addAll(listAccions);
		}else {
			respuesta.estado = false;
		}
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }



  
  	// TICKET 9000004494
  	/*public Respuesta<Bandeja> buscarCorrespondencia(String token, String bandeja, ListaFiltroCorrespondencia filtro, Locale locale) {
    	Respuesta<Bandeja> respuesta = new Respuesta<>();
    	try {
      		respuesta = this.sistcorrCliente.filtraCorrespondencias(token, bandeja, filtro);
      		if (respuesta.estado)
        		respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscarCorrespondenciaExito", null, locale); 
    	} catch (Exception e) {
      		respuesta.estado = false;
      		respuesta.mensaje = e.getMessage();
    	} 
    	return respuesta;
  	}*/
  	public Respuesta<CorrespondenciaTareaPaginado> buscarCorrespondencia(String token, String bandeja, ListaFiltroCorrespondencia filtro, String tamanio, Locale locale) {
		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta<>();
		try {
			respuesta = this.sistcorrCliente.filtraCorrespondenciasPrimeraPagina(token, bandeja, filtro, tamanio);
			if(respuesta.datos.get(0).getDetalleCorrespondencias() == null){
				respuesta.datos.get(0).setDetalleCorrespondencias(new ArrayList<CorrespondenciaSimple>());
			}
			if (respuesta.estado) {
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscarCorrespondenciaExito", null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
  	// FIN TICKET
  	
  	// TICKET 9000004494
 	public Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasSiguientePagina(String token, FiltroSiguientePagina filtros, Locale locale){
 		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta<>();
 		try{
 			System.out.println(filtros.toString());
 			respuesta = this.sistcorrCliente.filtraCorrespondenciasSiguientePagina(token, filtros);
 		}catch(Exception e){
 			respuesta.estado = false;
 			respuesta.mensaje = e.getMessage();
 		}
 		return respuesta;
 	}
 	// FIN TICKET

  
  public Respuesta<Funcionario> recuperarFuncionarios(String token, String wobNum, Locale locale) {
    Respuesta<Funcionario> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.recuperarFuncionarios(token, wobNum);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarFuncionariosExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<Asignacion> agregarAsignacion(String token, AgregarAsignacionParametro asignacion, Locale locale) {
    Respuesta<Asignacion> respuesta = new Respuesta<>();
    try {
      if (asignacion.getFechaLimite() != null && 
        !asignacion.getFechaLimite().equals("")) {
        Date _now = new Date();
        Date fechaActual = new Date(_now.getYear(), _now.getMonth(), _now.getDate());
        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaTope = fomat.parse(asignacion.getFechaLimite());
        this.LOGGER.info("Fecha actual: " + fechaActual + " Fecha Tope: " + fechaTope);
        this.LOGGER.info("Fecha actual: " + fechaActual.getTime() + " Fecha Tope: " + fechaTope.getTime());
        if (fechaActual.getTime() > fechaTope.getTime()) {
          throw new Exception(this.messageSource.getMessage("sistcorr.errorFechaTope", null, locale));
        }
      } 
      UsuarioPetroperu usuario = obtenerUsuario();
      //respuesta = this.sistcorrCliente.agregarAsignacion(token, asignacion);//TICKET 9000004165
      String msj = this.bandejaEntradaCorrespondenciaDAO.agregarAsignacionTemporal(asignacion.getCodigoAccion(), asignacion.getCorrelativo(), asignacion.getUsuarioAsignado(), asignacion.getDetalleSolicitud(), asignacion.getFechaLimite(), asignacion.getIdPadre(), usuario.getUsername());
      if(msj != null && msj.equalsIgnoreCase("")) {
    	  
          List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionesTemporales(asignacion.getCorrelativo(), usuario.getUsername());
          if(listAsigTemp != null) {
        	  respuesta.estado = true;
              respuesta.mensaje = "200";
				List<Asignacion> listObjs = new ArrayList<>();
				for (Object[] obja : listAsigTemp) {
					Asignacion obj = new Asignacion();
					obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
					obj.setAccion(obja[1] != null?obja[1].toString():"");
					obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
					obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
					obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
					
					listObjs.add(obj);
				}
				respuesta.datos.addAll(listObjs);
			}
      }else {
    	  respuesta.estado = false;
		  respuesta.mensaje = (msj != null?msj:"");
      }
      
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.agregarAsignacionExito", null, locale); 
    } catch (Exception e) {
      this.LOGGER.info("[ERROR]" + e.getMessage());
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }


  
  public Respuesta<Asignacion> agregarAsignacionMasivo(String token, AgregarAsignacionMasivaParametro asignacion, Locale locale) {
    Respuesta<Asignacion> respuesta = new Respuesta<>();
    try {
      if (asignacion.getFechaLimite() != null && 
        !asignacion.getFechaLimite().equals("")) {
        Date _now = new Date();
        Date fechaActual = new Date(_now.getYear(), _now.getMonth(), _now.getDate());
        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaTope = fomat.parse(asignacion.getFechaLimite());
        this.LOGGER.info("Fecha actual: " + fechaActual + " Fecha Tope: " + fechaTope);
        this.LOGGER.info("Fecha actual: " + fechaActual.getTime() + " Fecha Tope: " + fechaTope.getTime());
        if (fechaActual.getTime() > fechaTope.getTime()) {
          throw new Exception(this.messageSource.getMessage("sistcorr.errorFechaTope", null, locale));
        }
      }
      
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getCodigoAccion(): " + asignacion.getCodigoAccion());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getCorrelativo(): " + asignacion.getCorrelativo());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getDetalleSolicitud(): " + asignacion.getDetalleSolicitud());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getFechaLimite(): " + asignacion.getFechaLimite());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getIdPadre(): " + asignacion.getIdPadre());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getCodigoAgetCodigoDependenciaccion(): " + asignacion.getCodigoDependencia());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getUsername(): " + obtenerUsuario().getUsername());
      //respuesta = this.sistcorrCliente.agregarAsignacionMasivo(token, asignacion);//TICKET 9000004165
      String msj = this.bandejaEntradaCorrespondenciaDAO.agregarAsignacionTemporalMasivo(asignacion.getCodigoAccion(), asignacion.getCorrelativo(), asignacion.getDetalleSolicitud(), asignacion.getFechaLimite(), asignacion.getIdPadre(), asignacion.getCodigoDependencia(), obtenerUsuario().getUsername());
      this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - msj: " + msj);
      if(msj != null && msj.equalsIgnoreCase("")) {
    	  this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - asignacion.getCorrelativo(): " + asignacion.getCorrelativo());
    	  this.LOGGER.info("CorrespondenciaServiceImpl -agregarAsignacionMasivo - obtenerUsuario().getUsername(): " + obtenerUsuario().getUsername());
          List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionesTemporales(asignacion.getCorrelativo(), obtenerUsuario().getUsername());
          if(listAsigTemp != null) {
        	  respuesta.estado = true;
              respuesta.mensaje = "200";
				List<Asignacion> listObjs = new ArrayList<>();
				for (Object[] obja : listAsigTemp) {
					Asignacion obj = new Asignacion();
					obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
					obj.setAccion(obja[1] != null?obja[1].toString():"");
					obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
					obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
					obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
					
					listObjs.add(obj);
				}
				respuesta.datos.addAll(listObjs);
			}
      }else {
    	  respuesta.estado = false;
		  respuesta.mensaje = (msj != null?msj:"");
      }
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.agregarAsignacionMasivaExito", null, locale); 
    } catch (Exception e) {
      this.LOGGER.info("[ERROR]" + e.getMessage());
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }



  
  public Respuesta<RespuestaApi> enviarAsignaciones(String token, String correlativo, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.enviarAsignacion(token, correlativo);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.enviarAsignacionExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }




  
  public Respuesta<Asignacion> recuperarTemporalAsignaciones(String token, String correlativo, Locale locale) {
    Respuesta<Asignacion> respuesta = new Respuesta<>();
    try {
      //respuesta = this.sistcorrCliente.recuperarTemporalAsignaciones(token, correlativo); TICKET 900004165
    	List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionesTemporales(correlativo, obtenerUsuario().getUsername());
        if(listAsigTemp != null) {
      	  respuesta.estado = true;
            respuesta.mensaje = "200";
				List<Asignacion> listObjs = new ArrayList<>();
				for (Object[] obja : listAsigTemp) {
					Asignacion obj = new Asignacion();
					obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
					obj.setAccion(obja[1] != null?obja[1].toString():"");
					obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
					obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
					obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
					
					listObjs.add(obj);
				}
				respuesta.datos.addAll(listObjs);
			}
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recupearAsignacionesTemporalesExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<RespuestaApi> borrarAsignacion(String token, Integer idAsignacion, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
      //respuesta = this.sistcorrCliente.borrarAsigancion(token, idAsignacion);TICKET 9000004165
    	boolean respSP = this.bandejaEntradaCorrespondenciaDAO.eliminarAsignacionesTemporales(idAsignacion, obtenerUsuario().getUsername());
    	if(respSP) {
    		RespuestaApi resapi = new RespuestaApi();
    		respuesta.estado = respSP;
            respuesta.mensaje = "200";
            resapi.setStatus(200);
	    	resapi.setUserMessage("");
	    	respuesta.datos.add(resapi);
    		/*List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionesTemporales(asignacion.getCorrelativo(), usuario.getUsername());
            if(listAsigTemp != null) {
          	  respuesta.estado = true;
                respuesta.mensaje = "200";
  				List<Asignacion> listObjs = new ArrayList<>();
  				for (Object[] obja : listAsigTemp) {
  					Asignacion obj = new Asignacion();
  					obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
  					obj.setAccion(obja[1] != null?obja[1].toString():"");
  					obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
  					obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
  					obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
  					
  					listObjs.add(obj);
  				}
  				respuesta.datos.addAll(listObjs);
  			}*/
    	}
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.borrarAsignacionExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<RespuestaApi> cerrarCorrespondencia(String token, String correlativo, String observacion, String usuarioResponsable, String documentoRespuesta, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
      CerrarCorrespondenciaParametro parametro = new CerrarCorrespondenciaParametro();
      parametro.setObservacion(observacion);
      parametro.setUsuarioResponsable(usuarioResponsable);
      parametro.setDocumentoRespuesta(documentoRespuesta);
      respuesta = this.sistcorrCliente.cerrarCorrespondencia(token, correlativo, parametro);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<RespuestaApi> aceptarCorrespondencia(String token, String correlativo, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
      respuesta = this.sistcorrCliente.aceptarCorrespondencia(token, correlativo);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.aceptarCorrespondenciaExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }
  
  // TICKET 9000003997
  public Respuesta<RespuestaApi> procesarAceptarCorrespondencia(String token, String usuarioModifica, String nombreUsuario, String nroDocumento, String correlativo, Locale locale) {
	    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    	respuesta.estado = true;
	    respuesta.mensaje = "";
	    try {
	    	Correlativo corr = correlativoDAO.findOneByCodigo(nroDocumento);
	    	LOGGER.info("Correlativo buscado:" + nroDocumento + "-" + corr);
	    	if(corr!=null){
	    		DestinatarioRespuesta nuevoDest = new DestinatarioRespuesta();
		    	pe.com.petroperu.model.emision.Correspondencia corres = correspondenciaDAO.findOneByCorrelativo(corr);
		    	LOGGER.info("Correspondencia encontrada:" + corres);
		    	if(corres!=null){
			    	if(corres.getNroEnvio()>0){
			    		boolean encontrado = false;
			    		LOGGER.info("Correlativo enviada:" + corres.getNroEnvio());
			    		List<DestinatarioRespuesta> respuestas = destinatarioRespuestaDAO.findAllByCorrespondencia(corres);
				    	for(int i=0;i<respuestas.size();i++){
			    			DestinatarioRespuesta dest = respuestas.get(i);
			    			LOGGER.info("Comparacion:" + correlativo + "-" + dest.getCorrelativo());
				    		if(correlativo.equals(dest.getCorrelativo())){
				    			dest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ACEPTADO);
				    			dest.setUsuarioModifica(usuarioModifica);
				    			dest.setFechaModifica(new Date());
				    			dest.setNombreUsuario(nombreUsuario);
				    			destinatarioRespuestaDAO.save(dest);
				    		}
							encontrado = true;
				    	}
				    	if(!encontrado){
				    		List<ItemFilenet> destinatariosSist = filenetService.obtenerStatusCorrespondencia(nroDocumento);
				    		for(int i=0;i<destinatariosSist.size();i++){
				    			ItemFilenet dest = destinatariosSist.get(i);
				    			if(correlativo.equals(dest.getCodigo())){
				    				nuevoDest.setCorrelativo(correlativo);
				    				nuevoDest.setCorrespondencia(corres);
				    				// TICKET 9000004044
				    				//nuevoDest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_RECHAZADO);
				    				nuevoDest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ACEPTADO);
				    				// FIN TICKET
				    				nuevoDest.setDescripcionRechazo("");
				    				nuevoDest.setFechaCrea(new Date());
				    				nuevoDest.setUsuarioCrea(usuarioModifica);
				    				nuevoDest.setFechaModifica(new Date());
				    				nuevoDest.setUsuarioModifica(usuarioModifica);
				    				nuevoDest.setNombreUsuario(nombreUsuario);
				    				nuevoDest.setNroEnvio(1);
				    				nuevoDest.setCodDependencia(dest.getCodSup());
				    				nuevoDest = destinatarioRespuestaDAO.save(nuevoDest);
				    			}
				    		}
				    	}
			    	}else{
			    		LOGGER.info("Correlativo no enviada");
			    		List<ItemFilenet> destinatariosSist = filenetService.obtenerStatusCorrespondencia(nroDocumento);
			    		for(int i=0;i<destinatariosSist.size();i++){
			    			ItemFilenet dest = destinatariosSist.get(i);
			    			if(correlativo.equals(dest.getCodigo())){
			    				nuevoDest.setCorrelativo(correlativo);
			    				nuevoDest.setCorrespondencia(corres);
			    				nuevoDest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ACEPTADO);
			    				nuevoDest.setFechaCrea(new Date());
			    				nuevoDest.setUsuarioCrea(usuarioModifica);
			    				nuevoDest.setFechaModifica(new Date());
			    				nuevoDest.setUsuarioModifica(usuarioModifica);
			    				nuevoDest.setNombreUsuario(nombreUsuario);
			    				nuevoDest.setNroEnvio(1);
			    				nuevoDest.setCodDependencia(dest.getCodSup());
			    				destinatarioRespuestaDAO.save(nuevoDest);
			    			}
			    		}
			    		corres.setNroEnvio(1);
			    		correspondenciaDAO.save(corres);
			    	}
			    	// TICKET 9000004044
			    	// VERIFICAR ESTADO OTROS DESTINATARIOS
		    		LOGGER.info("Verificación de estados de todos los destinatarios");
		    		List<ItemFilenet> destinatariosSistcorr = filenetService.obtenerStatusCorrespondencia(nroDocumento);
		    		boolean todosAceptados = true;
		    		boolean algunPendiente = false;
		    		LOGGER.info("Cantidad de destinatarios:" + destinatariosSistcorr.size());
		    		for(int i=0;i<destinatariosSistcorr.size();i++){
		    			ItemFilenet item = destinatariosSistcorr.get(i);
		    			List<DestinatarioRespuesta> des = destinatarioRespuestaDAO.findAllByCorrelativo(item.getCodigo());
		    			LOGGER.info("Verificacion en tabla " + (i+1) + ":" + item.getCodigo());
		    			if(des != null || des.size() > 0){
		    				DestinatarioRespuesta dr = des.get(0);
		    				LOGGER.info("Verificacion por numero de envio:" + dr.getNroEnvio() + "-" + corres.getNroEnvio());
		    				if(dr.getNroEnvio() == corres.getNroEnvio()){
		    					LOGGER.info("Verificando estados " + item.getCodigoAux());
		    					if(!item.getCodigoAux().equals(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ACEPTADO)){
				    				todosAceptados = false;
				    			}
				    			if(item.getCodigoAux().equals(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ENVIADO)){
				    				algunPendiente = true;
				    			}
		    				}
		    			}
		    		}
		    		if(todosAceptados){
		    			if(corres.isEsDocumentoRespuesta() && corres.getEstadoDocumentoRespuesta() == pe.com.petroperu.util.Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_ENLAZADO){
		    				UsuarioPetroperu usuario = obtenerUsuario();
		    	    		if(corres.getIdAsignacion() != null && corres.getIdAsignacion().compareTo(0L) > 0){
		    					CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(corres.getRespuesta(), corres.getUsuarioCrea(), corres.getCorrelativo().getCodigo());
		    					respuesta = correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(corres.getIdAsignacion().toString()), ccp, locale);
		    					Object[] parametros = { corres.getCorrelativoEntrada() };
		    					LOGGER.info("Respuesta despues del service invocando al servicio: " + respuesta.estado + "-" + respuesta.mensaje);
		    					if(respuesta.estado){
		    						respuesta.mensaje = MessageFormat.format(
			    							this.messageSource.getMessage("sistcorr.completarCorrespondenciaInternoExito", null, locale),
			    							parametros); 
		    					}
		    				}else{
		    					respuesta = correspondenciaService.cerrarCorrespondencia(usuario.getToken(), corres.getCorrelativoEntrada(), corres.getRespuesta(), corres.getCodRemitente(), corres.getCorrelativo().getCodigo(), locale);
		    					Object[] parametros = { corres.getCorrelativoEntrada() };
		    					if(respuesta.estado){
		    						respuesta.mensaje = MessageFormat.format(
			    							this.messageSource.getMessage("sistcorr.cerrarCorrespondenciaInternoExito", null, locale),
			    							parametros);
		    					}
		    				}
		    	    	}
		    		}else{
		    			if(!algunPendiente){
		    				corres.setEstadoDocumentoRespuesta(pe.com.petroperu.util.Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_DESLIGADO);
		    				correspondenciaDAO.save(corres);
		    			}
		    		}
			    	// FIN TICKET
		    	}else{
			    	respuesta.estado = false;
			    	//respuesta.mensaje = "La correspondencia no existe";
		    	}
	    	}else{

		    	respuesta.estado = false;
		    	//respuesta.mensaje = "El correlativo no existe";
	    	}
	    	
	      //respuesta = this.sistcorrCliente.aceptarCorrespondencia(token, correlativo);
	      //if (respuesta.estado)
	        //respuesta.mensaje = this.messageSource.getMessage("sistcorr.aceptarCorrespondenciaExito", null, locale); 
	    } catch (Exception e) {
	    	LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      //respuesta.mensaje = e.getMessage();
	    } 
	    LOGGER.info("Respuesta dentro service: " + respuesta.mensaje);
	    return respuesta;
	  }
  
  	public Respuesta<RespuestaApi> procesarRechazarCorrespondencia(String token, String usuarioModifica, String usuarioNombreCompleto, String nroDocumento, String correlativo, String observacion, Locale locale) {
	    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	    try {
	    	Correlativo corr = correlativoDAO.findOneByCodigo(nroDocumento);
	    	LOGGER.info("Correlativo buscado:" + nroDocumento + "-" + corr);
	    	if(corr!=null){
		    	pe.com.petroperu.model.emision.Correspondencia corres = correspondenciaDAO.findOneByCorrelativo(corr);
		    	LOGGER.info("Correspondencia encontrada:" + corres);
		    	String nombreDependencia = "";
		    	if(corres!=null){
		    		DestinatarioRespuesta nuevoDest = new DestinatarioRespuesta();
			    	if(corres.getNroEnvio()>0){
			    		LOGGER.info("Correlativo enviada:" + corres.getNroEnvio());
			    		boolean encontrado = false;
			    		List<DestinatarioRespuesta> respuestas = destinatarioRespuestaDAO.findAllByCorrespondencia(corres);
				    	for(int i=0;i<respuestas.size();i++){
			    			DestinatarioRespuesta dest = respuestas.get(i);
			    			LOGGER.info("Comparacion:" + correlativo + "-" + dest.getCorrelativo());
				    		if(correlativo.equals(dest.getCorrelativo())){
				    			dest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_RECHAZADO);
				    			dest.setDescripcionRechazo(observacion);
				    			dest.setUsuarioModifica(usuarioModifica);
				    			dest.setNombreUsuario(usuarioNombreCompleto);
				    			dest.setFechaModifica(new Date());
				    			destinatarioRespuestaDAO.save(dest);
				    			List<ItemFilenet> dependencias = this.filenetService.listarDependenciasSuperiores(dest.getCodDependencia(), "");
								for(int j=0;j<dependencias.size();j++){
									if(dest.getCodDependencia().equals(dependencias.get(j).getCodigo())){
										nombreDependencia = dependencias.get(j).getDescripcion();
									}
								}
								encontrado = true;
				    		}
				    	}
				    	if(!encontrado){
				    		List<ItemFilenet> destinatariosSist = filenetService.obtenerStatusCorrespondencia(nroDocumento);
				    		for(int i=0;i<destinatariosSist.size();i++){
				    			ItemFilenet dest = destinatariosSist.get(i);
				    			if(correlativo.equals(dest.getCodigo())){
				    				nuevoDest.setCorrelativo(correlativo);
				    				nuevoDest.setCorrespondencia(corres);
				    				nuevoDest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_RECHAZADO);
				    				nuevoDest.setDescripcionRechazo(observacion);
				    				nuevoDest.setFechaCrea(new Date());
				    				nuevoDest.setUsuarioCrea(usuarioModifica);
				    				nuevoDest.setFechaModifica(new Date());
				    				nuevoDest.setUsuarioModifica(usuarioModifica);
				    				nuevoDest.setNombreUsuario(usuarioNombreCompleto);
				    				nuevoDest.setNroEnvio(1);
				    				nuevoDest.setCodDependencia(dest.getCodSup());
				    				nuevoDest = destinatarioRespuestaDAO.save(nuevoDest);
				    				nombreDependencia = dest.getDescripcion();
				    			}
				    		}
				    	}
			    	}else{
			    		LOGGER.info("Correlativo no enviada");
			    		List<ItemFilenet> destinatariosSist = filenetService.obtenerStatusCorrespondencia(nroDocumento);
			    		for(int i=0;i<destinatariosSist.size();i++){
			    			ItemFilenet dest = destinatariosSist.get(i);
			    			if(correlativo.equals(dest.getCodigo())){
			    				nuevoDest.setCorrelativo(correlativo);
			    				nuevoDest.setCorrespondencia(corres);
			    				nuevoDest.setEstado(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_RECHAZADO);
			    				nuevoDest.setDescripcionRechazo(observacion);
			    				nuevoDest.setFechaCrea(new Date());
			    				nuevoDest.setUsuarioCrea(usuarioModifica);
			    				nuevoDest.setFechaModifica(new Date());
			    				nuevoDest.setUsuarioModifica(usuarioModifica);
			    				nuevoDest.setNombreUsuario(usuarioNombreCompleto);
			    				nuevoDest.setNroEnvio(1);
			    				nuevoDest.setCodDependencia(dest.getCodSup());
			    				nuevoDest = destinatarioRespuestaDAO.save(nuevoDest);
			    				nombreDependencia = dest.getDescripcion();
			    			}
			    		}
			    		corres.setNroEnvio(1);
			    		correspondenciaDAO.save(corres);
			    	}
			    	//if(nuevoDest.getId().compareTo(0L)>0){
			    		// ENVIAR NOTIFICACION
			    		LOGGER.info("Envío de notificación");
			    		notificacionService.notificarCorrespondenciaRechazoDestinatario(corres, nroDocumento, nombreDependencia, usuarioNombreCompleto, observacion, locale);
			    		// VERIFICAR ESTADO OTROS DESTINATARIOS
			    		LOGGER.info("Verificación de estados de todos los destinatarios: " + nroDocumento);
			    		List<ItemFilenet> destinatariosSistcorr = filenetService.obtenerStatusCorrespondencia(nroDocumento);
			    		
			    		boolean todosRechazados = true;
			    		boolean algunPendiente = false;
			    		LOGGER.info("Dest. encontrados: " + destinatariosSistcorr.size());
			    		for(int i=0;i<destinatariosSistcorr.size();i++){
			    			ItemFilenet item = destinatariosSistcorr.get(i);
			    			LOGGER.info("Dest " + (i+1) + ": " + item.getCodigoAux());
			    			if(!item.getCodigoAux().equals(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_RECHAZADO)){
			    				todosRechazados = false;
			    			}
			    			if(item.getCodigoAux().equals(pe.com.petroperu.util.Constante.DESTINO_RESPUESTA_ENVIADO)){
			    				algunPendiente = true;
			    			}
			    		}
			    		if(todosRechazados){
			    			LOGGER.info("Todos los destinatarios se han rechazado");
			    			// RECHAZO DE CORRESPONDENCIA
			    			corres.setEstado(new CorrespondenciaEstado(pe.com.petroperu.util.Constante.CORRESPONDENCIA_REINICIO));
			    			corres.setNroFlujo(corres.getNroFlujo() + 1);
							List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(corres);
							
							LOGGER.info("Correspondencia:" + corres.getId());
							LOGGER.info("Ruta Aprobacion:" + aprobadores.size());
							for(int i=0;i<aprobadores.size();i++){
								RutaAprobacion ra = aprobadores.get(i);
								ra.setFirmante(null);
								ra.setUsuarioModifica(usuarioModifica);
								ra.setFechaModifica(new Date());
								LOGGER.info("Aprobador:" + ra.getId());
								ra.setUsuario(ra.getUsuario().toLowerCase());
								LOGGER.info("CorrespondenciaServiceImpl Linea 1563");
								rutaAprobacionDAO.save(ra);
							}
							
							MotivoRechazo motivoRechazo = motivoRechazoDAO.findOne(pe.com.petroperu.util.Constante.DOCUMENTO_POR_CORREGIR);
							corres.setMotivoRechazo(motivoRechazo);
							corres.setUsuarioModifica(usuarioModifica);
							corres.setFechaModifica(new Date());
							correspondenciaDAO.save(corres);
							
			    		}else{
			    			LOGGER.info("Algun pendiente:" + algunPendiente);
			    			if(!algunPendiente){
			    				corres.setEstadoDocumentoRespuesta(pe.com.petroperu.util.Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_DESLIGADO);
			    				correspondenciaDAO.save(corres);
			    			}
			    		}
			    	//}
		    	}
	    	}
	    	respuesta.estado = true;
	    	respuesta.mensaje = "Se realizó el proceso backend correctamente";
	    	
	      //respuesta = this.sistcorrCliente.aceptarCorrespondencia(token, correlativo);
	      //if (respuesta.estado)
	        //respuesta.mensaje = this.messageSource.getMessage("sistcorr.aceptarCorrespondenciaExito", null, locale); 
	    } catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	  }
  // FIN TICKET

  
  public Respuesta<RespuestaApi> rechazarCorrespondencia(String token, String correlativo, String observacion, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
      RechazarCorrespondenciaParametro parametro = new RechazarCorrespondenciaParametro();
      parametro.setObservacion(observacion);
      respuesta = this.sistcorrCliente.rechazarCorrespondencia(token, correlativo, parametro);
      if (respuesta.estado)
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.rechazarCorrespondenciaExito", null, locale); 
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }


  
  public Respuesta<RespuestaApi> completarCorrespondencia(String token, Integer idAsignacion, CompletarCorrespondenciaParametro correspondencia, Locale locale) {
    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
    try {
    	if(correspondencia.getDocumentoRespuesta() == null || "".equalsIgnoreCase(correspondencia.getDocumentoRespuesta())){
    		UsuarioPetroperu usuario = obtenerUsuario();
    		correspondencia.setUsuarioResponsable(usuario.getUsername());
    	}
      respuesta = this.sistcorrCliente.completarAsignacion(token, idAsignacion, correspondencia);
      LOGGER.info("Respuesta de la funcion service que invoca al servicio rest:" + respuesta.estado + "-" + respuesta.mensaje);
      if (respuesta.estado){
        respuesta.mensaje = this.messageSource.getMessage("sistcorr.completarCorrespondenciaExito", null, locale);
        // TICKET 9000004044
        Respuesta<RespuestaApi> respuestaAutomatico = new Respuesta<>();
        respuestaAutomatico = this.sistcorrCliente.completarAsignacionAutomatico(token, idAsignacion);
        if(!respuestaAutomatico.estado){
        	LOGGER.error("[ERROR] ", respuesta.mensaje);
        }
        // FIN TICKET
      }
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<Object> registrarCorrespondencia(String token, pe.com.petroperu.model.emision.Correspondencia  correspondencia, CorrespondenciaInterna correspondenciaI, String usuario, Locale locale) {
    Respuesta<Object> respuesta = new Respuesta<>();
    try {
      String correlativo = this.filenetService.generarCorrelativoTemporal();
      if ("".equals(correlativo)) {
        throw new Exception(this.messageSource.getMessage("sistcorr.enviar_correspondencia.error", null, locale));
      }
      String[] generados = this.filenetService.obtenerFechaCGCUsuario(usuario);
      Respuesta<RespuestaCargaAdjunto> respuestaAdjuntos = cargarArchivosAdjuntos(token, usuario, correspondencia, correlativo, generados, locale);
      if (!respuestaAdjuntos.estado) {
        throw new Exception(respuestaAdjuntos.mensaje);
      }
      Respuesta<CorrespondenciaInternaRespuesta> respuestaCI = this.sistcorrCliente.registrarCorrespondenciaEmitidaInterna(token, correlativo, correspondenciaI);
      if (!respuestaCI.estado) {
        throw new Exception(respuestaCI.mensaje);
      }
      respuesta.estado = true;
      respuesta.mensaje = this.messageSource.getMessage("sistcorr.enviar_correspondencia.exito", null, locale);
      List<RespuestaCargaAdjunto> adjuntos = respuestaAdjuntos.datos;
      respuesta.datos.add(adjuntos);
      respuesta.datos.add(respuestaCI.datos.get(0));
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<Object> registrarCorrespondencia(String token, pe.com.petroperu.model.emision.Correspondencia  correspondencia, CorrespondenciaExterna correspondenciaE, String usuario, Locale locale) {
    Respuesta<Object> respuesta = new Respuesta<>();
    try {
      String correlativo = this.filenetService.generarCorrelativoTemporal();
      if ("".equals(correlativo)) {
        throw new Exception(this.messageSource.getMessage("sistcorr.enviar_correspondencia.error", null, locale));
      }
      String[] generados = this.filenetService.obtenerFechaCGCUsuario(usuario);
      Respuesta<RespuestaCargaAdjunto> respuestaAdjuntos = cargarArchivosAdjuntos(token, usuario, correspondencia, correlativo, generados, locale);
      if (!respuestaAdjuntos.estado) {
        throw new Exception(respuestaAdjuntos.mensaje);
      }
      Respuesta<CorrespondenciaExternaRespuesta> respuestaCE = this.sistcorrCliente.registrarCorrespondenciaEmitidaExterna(token, correlativo, correspondenciaE);
      if (!respuestaCE.estado) {
        throw new Exception(respuestaCE.mensaje);
      }
      respuesta.estado = true;
      respuesta.mensaje = this.messageSource.getMessage("sistcorr.enviar_correspondencia.exito", null, locale);
      List<RespuestaCargaAdjunto> adjuntos = respuestaAdjuntos.datos;
      respuesta.datos.add(adjuntos);
      respuesta.datos.add(respuestaCE.datos.get(0));
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }

  
  public Respuesta<RespuestaCargaAdjunto> cargarArchivosAdjuntos(String token, String usuario, pe.com.petroperu.model.emision.Correspondencia correspondencia, String correlativoTemporal, String[] cgcFecha, Locale locale) {
    Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta<>();
    try {
      PropiedadesDocumento codTransaccion = new PropiedadesDocumento("CodigodeTransaccion", "");
      PropiedadesDocumento fecha = new PropiedadesDocumento("Fecha", cgcFecha[2]);
      PropiedadesDocumento correlativo = new PropiedadesDocumento("Correlativo", correlativoTemporal);
      PropiedadesDocumento centrodeGestion = new PropiedadesDocumento("CentrodeGestion", cgcFecha[1]);
      PropiedadesDocumento numDoc = new PropiedadesDocumento("NumerodeDocumento", "SI");
      Folder folder = new Folder("$FOLDER_CR$", correlativoTemporal);
      List<ArchivoAdjunto> archivos = correspondencia.getAdjuntos();
      Collections.sort(archivos, new Comparator<ArchivoAdjunto>() {
			@Override
			public int compare(ArchivoAdjunto a1, ArchivoAdjunto a2) {
				if(a1.isPrincipal())
					return -1;
				if(a2.isPrincipal())
					return 1;
				return 0;
			}
		});








      
      for (ArchivoAdjunto archivo : archivos) {
        //Path temp = Files.copy(Paths.get(archivo.getUbicacion(), new String[0]), Paths.get(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre(), new String[0]), new java.nio.file.CopyOption[0]);
        //File file = new File(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre());
    	  
	  	if(pe.com.petroperu.util.Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(archivo.getIndicadorRemoto())){
	  		UsuarioPetroperu usuario_ = obtenerUsuario();
			String urlCarpetaArchivos = "adjuntos";
			String nuevaUrl = this.DIRECCTORIO_BASE + urlCarpetaArchivos + "/" + archivo.getNombreServidor();
			ResponseEntity<byte[]> archivo_ = sistcorrCliente.descargarDocumentoServidor(usuario_.getToken(), archivo.getNombreServidor());
			FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo_.getBody());
		}

      	File file = new File(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre());
      	if(!file.exists()) {
      		Path temp = Files.copy(Paths.get(archivo.getUbicacion(), new String[0]), Paths.get(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre(), new String[0]), new java.nio.file.CopyOption[0]);
      	}
          
        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", archivo.getNombre());
        AsignarDocumento documento = new AsignarDocumento();
        documento.propiedades.add(documentTitle);
        documento.propiedades.add(codTransaccion);
        documento.propiedades.add(fecha);
        documento.propiedades.add(correlativo);
        documento.propiedades.add(centrodeGestion);
        documento.propiedades.add(numDoc);
        documento.nombre = "Correspondencia";
        this.LOGGER.info(archivo.getNombre() + " " + archivo.isPrincipal());
        Respuesta<RespuestaCargaAdjunto> res = this.sistcorrCliente.cargarAdjunto(token, documento, folder, file, archivo.getNombre(), archivo.getNombreServidor());
        file.delete();
        if (!res.estado) {
          throw new Exception(res.mensaje);
        }
        // TICKET 9000004510
        archivo.setIndicadorRemoto(pe.com.petroperu.util.Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
		File temp = new File(archivo.getUbicacion());
		temp.delete();
		archivoAdjuntoDAO.save(archivo);
        // FIN TICKET
        respuesta.datos.add(res.datos.get(0));
      } 
      respuesta.estado = true;
      respuesta.mensaje = "OK";
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] cargarArchivosAdjuntos ", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    return respuesta;
  }
  
  	// TICKET 9000003514
  
  	public Respuesta<RespuestaCargaAdjunto> cargarArchivoAdjunto(String token, String usuario, pe.com.petroperu.model.Correspondencia correspondencia, String correlativoTemporal, Locale locale, ArchivoAdjunto archivo) {
  		Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta<>();
  		try {
  			String[] cgcFecha = this.filenetService.obtenerFechaCGCUsuario(usuario);
  			
  			PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", archivo.getNombre());
  			PropiedadesDocumento codTransaccion = new PropiedadesDocumento("CodigodeTransaccion", correspondencia.getTipoTransaccion());
  			PropiedadesDocumento fecha = new PropiedadesDocumento("Fecha", cgcFecha[2]);
  			PropiedadesDocumento correlativo = new PropiedadesDocumento("Correlativo", correlativoTemporal);
  			PropiedadesDocumento centrodeGestion = new PropiedadesDocumento("CentrodeGestion", cgcFecha[1]);
  			PropiedadesDocumento numDoc = new PropiedadesDocumento("NumerodeDocumento", "CRCONSULTA");
  			
  			
  			Folder folder = new Folder("$FOLDER_TMP_CR$", correlativoTemporal);
  			//List<ArchivoAdjunto> archivos = correspondencia.getAdjuntos();
      
  			//for (ArchivoAdjunto archivo : archivos) {
  				Path temp = Files.copy(Paths.get(archivo.getUbicacion(), new String[0]), Paths.get(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre(), new String[0]), new java.nio.file.CopyOption[0]);
  				File file = new File(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre());
  				
  				AsignarDocumento documento = new AsignarDocumento();
  				documento.propiedades.add(documentTitle);
  				documento.propiedades.add(codTransaccion);
  				documento.propiedades.add(fecha);
  				documento.propiedades.add(correlativo);
  				documento.propiedades.add(centrodeGestion);
  				documento.propiedades.add(numDoc);
  				documento.nombre = "Correspondencia";
  				this.LOGGER.info(archivo.getNombre() + " " + archivo.isPrincipal());
  				this.LOGGER.info("documentTitle:" + documentTitle.valor);
  				this.LOGGER.info("codTransaccion:" + codTransaccion.valor);
  				this.LOGGER.info("fecha:" + fecha.valor);
  				this.LOGGER.info("correlativo:" + correlativo.valor);
  				this.LOGGER.info("centrodeGestion:" + centrodeGestion.valor);
  				this.LOGGER.info("numDoc:" + numDoc.valor);
  				Respuesta<RespuestaCargaAdjunto> res = this.sistcorrCliente.cargarAdjunto(token, documento, folder, file, archivo.getNombre(), archivo.getNombreServidor());
  				file.delete();
  				if (!res.estado) {
  					throw new Exception(res.mensaje);
  				}
  				respuesta.datos.add(res.datos.get(0));
  			//} 
  			respuesta.estado = true;
  			respuesta.mensaje = messageSource.getMessage("sistcorr.cargarAdjuntoExito", null, locale);;
  		} catch (Exception e) {
  			this.LOGGER.error("[ERROR] cargarArchivosAdjuntos ", e);
  			respuesta.estado = false;
  			respuesta.mensaje = e.getMessage();
  		} 
  		return respuesta;
  	}
  	// FIN TICKET 9000003514
  	
  	// TICKET 9000004065
  	public Respuesta<RespuestaApi> registrarObservacion(String token, String correlativo, String observacion, Locale locale){
  		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
  	    try {
  	      RegistrarObservacion parametro = new RegistrarObservacion();
  	      parametro.setCorrelativo(correlativo);
  	      parametro.setObservacion(observacion);
  	      parametro.setProceso("TD_CR");
  	      obtenerUsuario();
  	      //respuesta = this.sistcorrCliente.registrarObservacion(token, parametro);//TICKET 9000004165
  	      boolean respuestaSP = this.bandejaEntradaCorrespondenciaDAO.agregarObservacion(parametro.getProceso(), obtenerUsuario().getUsername(), correlativo, observacion);
  	      if(respuestaSP) {
  	    	respuesta.estado = respuestaSP;
			respuesta.mensaje = "200";
			RespuestaApi resapi = new RespuestaApi();
			resapi.setStatus(200);
	    	resapi.setUserMessage("");
	    	respuesta.datos.add(resapi);
  	      }
  	      if (respuesta.estado)
  	        respuesta.mensaje = this.messageSource.getMessage("sistcorr.registrarObservacionExito", null, locale); 
  	    } catch (Exception e) {
  	      respuesta.estado = false;
  	      respuesta.mensaje = e.getMessage();
  	    } 
  	    return respuesta;
  	}
  	// FIN TICKET
  
  private String generarMensajeFromApi(String statusCode, Locale locale) {
    String mensaje = "";
    switch (statusCode) {
	case "500":
		mensaje = messageSource.getMessage("sistcorr.api.error", null, locale);
		break;
	default:
		mensaje = messageSource.getMessage("sistcorr.noListado", null, locale);
		break;
	}
	return mensaje;
  }






  
  private boolean tieneRol(UsuarioPetroperu usuario, RolAD rol) {
    this.LOGGER.info("[ROL] " + rol.ROL_SEGURIDAD);
    boolean resultado = false;
    List<Rol> roles = this.rolService.listarRolPorUsuario(usuario.getUsername());
    for (Rol _rol : roles) {
      this.LOGGER.info("[  ROL] " + _rol.getRolSpring());
      if (_rol.getRolSpring().equals(rol.ROL_SEGURIDAD)) {
        resultado = true;
        break;
      } 
    } 
    return resultado;
  }


	// INICIO TICKET 9000004273
	@Override
	public Respuesta<RespuestaApi> rechazarAsignacionCorrespondencia(String token, Integer idAsignacion,
			RechazarAsignacionCorrespondenciaParametro correspondencia, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try {
			//UsuarioPetroperu usuario = obtenerUsuario();
			respuesta = this.sistcorrCliente.rechazarAsignacionCorrespondencia(token, idAsignacion, correspondencia);
			LOGGER.info("Respuesta de la funcion service que invoca al servicio rest:" + respuesta.estado + "-"
					+ respuesta.mensaje);
			if (respuesta.estado){
		        respuesta.mensaje = this.messageSource.getMessage("sistcorr.rechazarAsignacionCorrespondenciaExito", null, locale);
		    }
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	//INICIO TICKET 9000004273
	
	// TICKET 9000004510
	public Object[] descargarDocumentoServidor(String token, String idDocumento, Locale locale) {
		Object[] rpta = new Object[1];
		ArchivoAdjunto aa = archivoAdjuntoDAO.findOne(Long.valueOf(idDocumento));
	    ResponseEntity<byte[]> respuesta = this.sistcorrCliente.descargarDocumentoServidor(token, aa.getNombreServidor());
	    if(respuesta == null){
	    	return null;
	    }
	    rpta[0] = respuesta.getBody();
	    return rpta;
	}
	// FIN TICKET
	
	public Respuesta<RespuestaCargaAdjunto> cargarDocumentoServidor(String token, String usuario, pe.com.petroperu.model.emision.Correspondencia correspondencia, String correlativoTemporal, String[] cgcFecha, Locale locale) {
		Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta<>();
		/*try {
			PropiedadesDocumento codTransaccion = new PropiedadesDocumento("CodigodeTransaccion", "");
			PropiedadesDocumento fecha = new PropiedadesDocumento("Fecha", cgcFecha[2]);
			PropiedadesDocumento correlativo = new PropiedadesDocumento("Correlativo", correlativoTemporal);
			PropiedadesDocumento centrodeGestion = new PropiedadesDocumento("CentrodeGestion", cgcFecha[1]);
			PropiedadesDocumento numDoc = new PropiedadesDocumento("NumerodeDocumento", "SI");
			Folder folder = new Folder("$FOLDER_CR$", correlativoTemporal);
			List<ArchivoAdjunto> archivos = correspondencia.getAdjuntos();
			Collections.sort(archivos, new Comparator<ArchivoAdjunto>() {
				@Override
				public int compare(ArchivoAdjunto a1, ArchivoAdjunto a2) {
					if(a1.isPrincipal())
						return -1;
					if(a2.isPrincipal())
						return 1;
					return 0;
				}
			});
			
			for (ArchivoAdjunto archivo : archivos) {
				//Path temp = Files.copy(Paths.get(archivo.getUbicacion(), new String[0]), Paths.get(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre(), new String[0]), new java.nio.file.CopyOption[0]);
				//File file = new File(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre());

				File file = new File(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre());
				if(!file.exists()) {
					Path temp = Files.copy(Paths.get(archivo.getUbicacion(), new String[0]), Paths.get(this.DIRECCTORIO_BASE + this.DIRECTORIO_TEMPORAL + "/" + archivo.getNombre(), new String[0]), new java.nio.file.CopyOption[0]);
				}
	          
				PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", archivo.getNombre());
				AsignarDocumento documento = new AsignarDocumento();
				documento.propiedades.add(documentTitle);
				documento.propiedades.add(codTransaccion);
				documento.propiedades.add(fecha);
				documento.propiedades.add(correlativo);
				documento.propiedades.add(centrodeGestion);
				documento.propiedades.add(numDoc);
				documento.nombre = "Correspondencia";
				this.LOGGER.info(archivo.getNombre() + " " + archivo.isPrincipal());
				Respuesta<RespuestaCargaAdjunto> res = this.sistcorrCliente.cargarAdjunto(token, documento, folder, file, archivo.getNombre());
				file.delete();
				if (!res.estado) {
					throw new Exception(res.mensaje);
				}
				respuesta.datos.add(res.datos.get(0));
			} */
			respuesta.estado = true;
			respuesta.mensaje = "OK";/*
	    } catch (Exception e) {
	    	this.LOGGER.error("[ERROR] cargarArchivosAdjuntos ", e);
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    } */
	    return respuesta;
	  }
	// FIN TICKET
	//  TICKET 9000004497
	public Respuesta<Asignacion> agregarAsignacionGrupal(String token, AgregarAsignacionParametro asignacion, Locale locale, AsignacionGrupal asignacionGrupal) {
	    Respuesta<Asignacion> respuesta = new Respuesta<>();
	    try {
	      if (asignacion.getFechaLimite() != null && 
	        !asignacion.getFechaLimite().equals("")) {
	        Date _now = new Date();
	        Date fechaActual = new Date(_now.getYear(), _now.getMonth(), _now.getDate());
	        SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
	        Date fechaTope = fomat.parse(asignacion.getFechaLimite());
	        this.LOGGER.info("Fecha actual: " + fechaActual + " Fecha Tope: " + fechaTope);
	        this.LOGGER.info("Fecha actual: " + fechaActual.getTime() + " Fecha Tope: " + fechaTope.getTime());
	        if (fechaActual.getTime() > fechaTope.getTime()) {
	          throw new Exception(this.messageSource.getMessage("sistcorr.errorFechaTope", null, locale));
	        }
	      } 
	      
			List<Asignacion> listAsignacionGrupal = new ArrayList<>();
			listAsignacionGrupal = asignacionGrupal.getListAsignacionGrupal();
			String grupoCorrelativo="";
			String grupoIdPadre="";
			for (Asignacion nAsinacion : listAsignacionGrupal) {
				grupoCorrelativo = grupoCorrelativo + nAsinacion.getCorrelativo() + ",";
				grupoIdPadre = grupoIdPadre + nAsinacion.getIdPadre() + ",";
			}
			grupoCorrelativo = grupoCorrelativo.substring(0, grupoCorrelativo.length()-1);
			grupoIdPadre = grupoIdPadre.substring(0, grupoIdPadre.length()-1);
			this.LOGGER.info("[  grupoCorrespondecia: ] " + grupoCorrelativo);
			this.LOGGER.info("[  grupoIdPadre: ] " + grupoIdPadre);
	      
	      UsuarioPetroperu usuario = obtenerUsuario();
	      String msj = this.bandejaEntradaCorrespondenciaDAO.agregarAsignacionGrupalTemporal(asignacion.getCodigoAccion(), asignacion.getUsuarioAsignado(), asignacion.getDetalleSolicitud(), asignacion.getFechaLimite(), usuario.getUsername(), grupoCorrelativo, grupoIdPadre);
	      //if(msj == null || msj.equalsIgnoreCase("")) {
	     
	          List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionGrupalTemporales(usuario.getUsername(), grupoCorrelativo);
	          if(listAsigTemp != null) {
	        	  if(msj == null || msj.equalsIgnoreCase("")) {
		        	  respuesta.estado = true;
		              respuesta.mensaje = "200";
	        	  }else{
	        		 respuesta.estado = false;
	        		 respuesta.mensaje = (msj != null?msj:"");
	        	  }
					List<Asignacion> listObjs = new ArrayList<>();
					for (Object[] obja : listAsigTemp) {
						Asignacion obj = new Asignacion();
						obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
						obj.setAccion(obja[1] != null?obja[1].toString():"");
						obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
						obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
						obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
						obj.setCorrelativo(obja[5] != null?obja[5].toString():"");
						listObjs.add(obj);
					}
					respuesta.datos.addAll(listObjs);
				}else{
					respuesta.estado = false;
					respuesta.mensaje = "No pudo recuperar las asignaciones temporales";
				}
	      /*}else {
	    	  respuesta.estado = false;
			  respuesta.mensaje = (msj != null?msj:"");
	      }*/
	      
	      if (respuesta.estado)
	        respuesta.mensaje = this.messageSource.getMessage("sistcorr.agregarAsignacionExito", null, locale); 
	    } catch (Exception e) {
	      this.LOGGER.info("[ERROR]" + e.getMessage());
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	  }
	
	  public Respuesta<Asignacion> recuperarTemporalAsignacionGrupal(String token, String grupoCorrelativo, Locale locale) {
		    Respuesta<Asignacion> respuesta = new Respuesta<>();
		    try {
		    	List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO.obtenerAsignacionGrupalTemporales(obtenerUsuario().getUsername(), grupoCorrelativo);
		        if(listAsigTemp != null) {
		      	  respuesta.estado = true;
		            respuesta.mensaje = "200";
						List<Asignacion> listObjs = new ArrayList<>();
						for (Object[] obja : listAsigTemp) {
							Asignacion obj = new Asignacion();
							obj.setIdAsignacion(obja[0] != null?(Integer)obja[0]:0);
							obj.setAccion(obja[1] != null?obja[1].toString():"");
							obj.setNombreApellidoAsignado(obja[2] != null?obja[2].toString():"");
							obj.setDetalleSolicitud(obja[3] != null?obja[3].toString():"");
							obj.setFechaLimite(obja[4] != null?obja[4].toString():"");
							obj.setCorrelativo(obja[5] != null?obja[5].toString():"");
							listObjs.add(obj);
						}
						respuesta.datos.addAll(listObjs);
					}
		      if (respuesta.estado)
		        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recupearAsignacionesTemporalesExito", null, locale); 
		    } catch (Exception e) {
		      respuesta.estado = false;
		      respuesta.mensaje = e.getMessage();
		    } 
		    return respuesta;
		  }
	  
  	public Respuesta<RespuestaApi> validarAsignacionGrupal(String token, AsignacionGrupal asignacioGrupal, Locale locale){
  		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
  	    try {
			List<Asignacion> listAsignacionGrupal = new ArrayList<>();
			listAsignacionGrupal = asignacioGrupal.getListAsignacionGrupal();
			String grupoCorrelativo="";
			String grupoIdPadre="";
			for (Asignacion nAsinacion : listAsignacionGrupal) {
				grupoCorrelativo = grupoCorrelativo + nAsinacion.getCorrelativo() + ",";
				grupoIdPadre = grupoIdPadre + nAsinacion.getIdPadre() + ",";
			}
			grupoCorrelativo = grupoCorrelativo.substring(0, grupoCorrelativo.length()-1);
			grupoIdPadre = grupoIdPadre.substring(0, grupoIdPadre.length()-1);
  	      obtenerUsuario();
  	      String msj = this.bandejaEntradaCorrespondenciaDAO.validarAsignacionGrupal(obtenerUsuario().getUsername(), grupoCorrelativo, grupoIdPadre);
  	      if(msj == null || (msj != null && msj.equalsIgnoreCase(""))) {
  	    	respuesta.estado = true;
			respuesta.mensaje = "";
  	      }else{
  	    	  respuesta.estado = false;
			  respuesta.mensaje = (msj != null?msj:"");
  	      }
  	      if (respuesta.estado)
  	        respuesta.mensaje = this.messageSource.getMessage("sistcorr.registrarObservacionExito", null, locale); 
  	
  	    } catch (Exception e) {
  	      respuesta.estado = false;
  	      respuesta.mensaje = e.getMessage();
  	    } 
  	    return respuesta;
  	}
  	
    public Respuesta<RespuestaApi> enviarAsignacionGrupal(String token,AsignacionGrupal asignacionGrupal, Locale locale) {
        Respuesta<RespuestaApi> respuesta = new Respuesta<>();
     	AsignacionGrupalRequest request = new AsignacionGrupalRequest();
        try {
        	         	
        	List<Asignacion> listAsignacionGrupal = new ArrayList<>();
			listAsignacionGrupal = asignacionGrupal.getListAsignacionGrupal();
			String grupoCorrelativo="";
			for (Asignacion nAsinacion : listAsignacionGrupal) {
				grupoCorrelativo = grupoCorrelativo + nAsinacion.getCorrelativo() + ",";
			}
			grupoCorrelativo = grupoCorrelativo.substring(0, grupoCorrelativo.length()-1);
			this.LOGGER.info("[ grupoCorrespondecia: ] " + grupoCorrelativo);
			
			UsuarioPetroperu usuario = obtenerUsuario();
			List<Object[]> listAsigTemp = this.bandejaEntradaCorrespondenciaDAO
					.obtenerAsignacionGrupalTemporales(usuario.getUsername(), grupoCorrelativo);
			if (listAsigTemp != null ) {
				if (listAsigTemp.size() > 0){
					List<Asignacion> listObjs = new ArrayList<>();
	
					for (Asignacion nAsinacion : listAsignacionGrupal) {
						Asignacion obj = new Asignacion();
						for (Object[] obja : listAsigTemp) {
							if (nAsinacion.getCorrelativo().equals(obja[5].toString())) {
								obj.setCorrelativo(obja[5] != null ? obja[5].toString() : "");
								obj.setWorkflowId(nAsinacion.getWorkflowId());
								listObjs.add(obj);
								break;
							}
						}
					}
					request.setWorkflowIds(listObjs);
			          respuesta = this.sistcorrCliente.enviarAsignacionGrupal(token, request);
			          if (respuesta.estado)
			            respuesta.mensaje = this.messageSource.getMessage("sistcorr.enviarAsignacionExito", null, locale); 
				} else {
					respuesta.estado = false;
			        respuesta.mensaje = "DEBE INGRESAR AL MENOS UNA ASIGNACIÓN!";
				}
			}
        } catch (Exception e) {
          respuesta.estado = false;
          respuesta.mensaje = e.getMessage();
        } 
        return respuesta;
      }

	// FIN TICKET
    
    /*INI Ticket 9*4275*/
	@Override
	public Respuesta<ReemplazoConsultaDTO> notificacionReemplazo(String token, String sCodReemplazo, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ReemplazoConsultaDTO> respuesta = new Respuesta<>();
        try {
          respuesta = this.sistcorrCliente.notificacionReemplazo(token, sCodReemplazo);
          if (respuesta.estado)
            respuesta.mensaje = this.messageSource.getMessage("sistcorr.recuperarFuncionariosExito", null, locale); 
        } catch (Exception e) {
          respuesta.estado = false;
          respuesta.mensaje = e.getMessage();
        } 
        return respuesta;
	}


	@Override
	public Respuesta<ConductorPaginado> buscarConductor(String token, String bandeja, ListaFiltroConductor filtro,
			String tamanio, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ConductorPaginado> respuesta = new Respuesta<>();
		try {
			respuesta = this.sistcorrCliente.filtraConductorPrimeraPagina(token, bandeja, filtro, tamanio);
			if(respuesta.datos.get(0).getDetalleConductor() == null){
				respuesta.datos.get(0).setDetalleConductor(new ArrayList<Conductor>());
			}
			if (respuesta.estado) {
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscarCorrespondenciaExito", null, locale);
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}


	@Override
	public Respuesta<ConductorPaginado> filtraConductorSiguientePagina(String token, FiltroSiguientePagina filtros,
			Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<ConductorPaginado> respuesta = new Respuesta<>();
 		try{
 			System.out.println(filtros.toString());
 			respuesta = this.sistcorrCliente.filtraConductorSiguientePagina(token, filtros);
 		}catch(Exception e){
 			respuesta.estado = false;
 			respuesta.mensaje = e.getMessage();
 		}
 		return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> reintentarConductor(String token, String workflowId, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
        try {
          respuesta = this.sistcorrCliente.reintentarConductor(token, workflowId);
          if (respuesta.estado)
            respuesta.mensaje = this.messageSource.getMessage("sistcorr.reintentarConductorExito", null, locale); 
        } catch (Exception e) {
          respuesta.estado = false;
          respuesta.mensaje = e.getMessage();
        } 
        return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> saltarPasoConductor(String token, String workflowId, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
        try {
          respuesta = this.sistcorrCliente.saltarPasoConductor(token, workflowId);
          if (respuesta.estado)
            respuesta.mensaje = this.messageSource.getMessage("sistcorr.avanzarPasoConductorExito", null, locale); 
        } catch (Exception e) {
          respuesta.estado = false;
          respuesta.mensaje = e.getMessage();
        } 
        return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> terminarPasoConductor(String token, String workflowId, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
        try {
          respuesta = this.sistcorrCliente.terminarPasoConductor(token, workflowId);
          if (respuesta.estado)
            respuesta.mensaje = this.messageSource.getMessage("sistcorr.terminarPasoConductorExito", null, locale); 
        } catch (Exception e) {
          respuesta.estado = false;
          respuesta.mensaje = e.getMessage();
        } 
        return respuesta;
	}

    //inicio ticket 9000004765
	@Override
	public Respuesta<DestinatarioDocPagar> recuperarDestinatariosDocPorPagar() {
		// TODO Auto-generated method stub
		Respuesta<DestinatarioDocPagar> respuesta = new Respuesta<>();
  	    try {
  	    	respuesta.datos.addAll(destDocPagar.findAll());
  	    	respuesta.estado = true;
			respuesta.mensaje = "";
  	
  	    } catch (Exception e) {
  	      respuesta.estado = false;
  	      respuesta.mensaje = e.getMessage();
  	    } 
  	    return respuesta;
	}
	//fin ticket 9000004765
	/*FIN Ticket 9*4275*/
	
	/*INI Ticket 9*4807*/
	@Override
	public Respuesta<ByteArrayInputStream> exportarExcelCorrespondenciasAsignacionesBandejaEntrada(
			ListaFiltroCorrespondencia filtro, String usuarioCreador, String usuario, String bandeja, Locale locale) {
		// TODO Auto-generated method stub
		LOGGER.info("[INICIO] exportarExcelCorrespondenciasAsignacionesBandejaEntrada " + filtro.toString());
		
		Respuesta<Bandeja> respuestaData = new Respuesta<>();
		Respuesta<ByteArrayInputStream> respuesta = new Respuesta<>();
		try {
			UsuarioPetroperu usuarioSess = obtenerUsuario();
			
			respuestaData = this.sistcorrCliente.filtraCorrespondencias(usuarioSess.getToken(), bandeja, filtro);
			if(respuestaData.datos.get(0).getCorrespondencias() == null){
				respuestaData.datos.get(0).setCorrespondencias(new ArrayList<CorrespondenciaSimple>());
			}
			if (respuestaData.estado) {
				respuestaData.mensaje = this.messageSource.getMessage("sistcorr.buscarCorrespondenciaExito", null, locale);
			}
			
			LOGGER.info("Resultados del excel:" + respuesta.datos.size());
			IReport<ByteArrayInputStream> report;
			if(respuestaData.estado) {
				report = new ReportExcelCABandejaEntrada(respuestaData.datos, usuarioCreador, bandeja);
			} else {
				report = new ReportExcelCABandejaEntrada(new ArrayList<>(), usuarioCreador, bandeja);
			}
			report.prepareRequest();
			report.process();
			respuesta.estado = true;
			respuesta.datos.add(report.getResult());
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		return respuesta;
	}
	
	/*FIN Ticket 9*4807*/
	
	// TICKET 9000004961
	public Respuesta<CorrespondenciaConsulta> consultarCorrespondenciaAuditoria(String token, FiltroConsultaAuditoria filtro, Integer itemsPorPagina, Integer numeroPagina, String columnaOrden, String orden, String exportarExcel, Locale locale) {
	    Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta<>();
	    try {

	      List<Object[]> listObjs = this.bandejaEntradaCorrespondenciaDAO.consultaCorrespondenciaAuditoria(obtenerUsuario().getUsername(), filtro.getCorrelativo(), filtro.getFechaRegistroDesde(), filtro.getFechaRegistroHasta(), 
	    			(filtro.getCodigoTipoCorrespondencia() != null?Integer.parseInt(filtro.getCodigoTipoCorrespondencia()):0), (filtro.getCodigoEstado() != null?Integer.parseInt(filtro.getCodigoEstado()):0), filtro.getFechaDocumentoInterno(), 
	    			(filtro.getCodigoDependenciaDestino() != null?Integer.parseInt(filtro.getCodigoDependenciaDestino()):0), (filtro.getCodigoDependenciaRemitente() != null?Integer.parseInt(filtro.getCodigoDependenciaRemitente()):0), filtro.getNombreDependenciaExterna(), filtro.getAsunto(), filtro.getNumeroDocumentoInterno(), filtro.getGuiaRemision(), null, 
	    			filtro.getProcedencia(), itemsPorPagina, numeroPagina, columnaOrden, orden, exportarExcel);
	      LOGGER.info("Correspondencias obtenidas:" + listObjs.size());
	      if(listObjs != null) {
	    	  Object[] total = listObjs.get(0);
	    	  System.out.println("Total Service:" + Integer.valueOf(String.valueOf(total[0])));
	    	  listObjs.remove(0);
	    	  List<CorrespondenciaConsulta> consulListCorres = new ArrayList<>();
	    	  LOGGER.info("Correspondencias obtenidas:" + listObjs.size());
	    	  for(Object[] obja : listObjs) {
	    		  CorrespondenciaConsulta obj = new CorrespondenciaConsulta();
	    		  obj.setCorrelativo(obja[1] != null?obja[1].toString():"");
	    		  obj.setFechaRadicado(obja[2] != null?obja[2].toString():"");
	    		  obj.setAsunto(obja[3] != null?obja[3].toString():"");
	    		  obj.setNumeroDocumentoInterno(obja[4] != null?obja[4].toString():"");
	    		  obj.setDestino(obja[5] != null?obja[5].toString():"");
	    		  obj.setOrigen(obja[6] != null?obja[6].toString():"");
	    		  obj.setEstado(obja[7] != null?obja[7].toString():"");
	    		  obj.setTipoCorrespondencia(obja[8] != null?obja[8].toString():"");
	    		  obj.setTipoIcono(obja[10] != null?obja[10].toString():"");
	    		  obj.setEsConfidencial(obja[11] != null?obja[11].toString():"");
	    		  obj.setEsAsignado(obja[13] != null?obja[13].toString():"");
	    		  consulListCorres.add(obj);
	    	  }
	    	  respuesta.estado = true;
	    	  respuesta.mensaje = "200";
	    	  respuesta.datos.addAll(consulListCorres);
	    	  respuesta.total = Integer.valueOf(String.valueOf(total[0]));
	      }
	      if(respuesta.estado) {
				respuesta.mensaje =  messageSource.getMessage("sistcorr.consultar_correspondencia.Exito", null, locale);
			}
	    }
	    catch (Exception e) {
		      LOGGER.error("[ERROR]", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	}
	// FIN TICKET
	
	/*INI Ticket 9*4413*/
	public Respuesta<Correspondencia> recuperarCorrespondenciaMPV(String token, String correlativo, Locale locale) {
		  Respuesta<Correspondencia> respuesta = new Respuesta<>();
		  try{
			  LOGGER.info("[INICIO] recuperarCorrespondenciaMPV " + correlativo);
			  List<Correspondencia> listObject = bandejaEntradaCorrespondenciaDAO.obtenerInformacionCorrespondenciaMPV(correlativo);
		      
		      respuesta.estado = true;
		      respuesta.mensaje = "200";
		      respuesta.datos.addAll(listObject);
			 
		      if (respuesta.estado)
		        respuesta.mensaje = this.messageSource.getMessage("sistcorr.recupearCorrespondenciaExitoMPV", null, locale); 
		      
		  }catch (Exception e) {
		      respuesta.estado = false;
		      respuesta.mensaje = e.getMessage();
		    } 
		    return respuesta;
	  }
	
	public Respuesta<RespuestaApi> rechazarCorrespondenciaMPV(String token, String correlativo, String observacion, Locale locale) {
	    Respuesta<RespuestaApi> respuesta = new Respuesta<>();
	    try {
	      RechazarCorrespondenciaParametro parametro = new RechazarCorrespondenciaParametro();
	      parametro.setObservacion(observacion);
	      respuesta = this.sistcorrCliente.rechazarCorrespondenciaMPV(token, correlativo, parametro);
	      if (respuesta.estado)
	        respuesta.mensaje = this.messageSource.getMessage("sistcorr.rechazarCorrespondenciaExito", null, locale); 
	    } catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    return respuesta;
	  }
	
	public Respuesta<RespuestaApi> asignarDependenciaMPV(String token,String correlativo, String cgc, 
									String dependencia, String accion, Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			AsignarDependenciaParametro parametro = new AsignarDependenciaParametro();
			parametro.setCgc(cgc);
			parametro.setDependencia(dependencia);
			parametro.setAccion(accion);
			respuesta=this.sistcorrCliente.asignarDependenciaMPV(token,correlativo,parametro);
			if (respuesta.estado)
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.asignarDependenciaExitoMPV", null, locale); 
		}catch(Exception e){
			respuesta.estado=false;
			respuesta.mensaje=e.getMessage();
		}
		return respuesta;
	}
	
	/*FIN Ticket 9*4413*/
	
	/*INI Ticket 9000004412*/
	public Respuesta<RespuestaApi> crearExpediente(String token,String nroProceso,Locale locale) {
		Respuesta<RespuestaApi> respuesta = new Respuesta<>();
		try{
			respuesta=this.sistcorrCliente.crearExpediente(token,nroProceso);
			if (respuesta.estado)
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.asignarDependenciaExitoMPV", null, locale); 
		 }catch(Exception e){
			 respuesta.estado=false;
			 respuesta.mensaje=e.getMessage();	
		 }
		return respuesta;
	}
	
	/*FIN Ticket 9000004412*/
}
