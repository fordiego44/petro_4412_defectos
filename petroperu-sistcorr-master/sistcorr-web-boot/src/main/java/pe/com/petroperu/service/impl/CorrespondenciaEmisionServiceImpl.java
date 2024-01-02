package pe.com.petroperu.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.text.pdf.PdfCopy;

import pe.com.petroperu.PDFTool;
import pe.com.petroperu.Respuesta;
import pe.com.petroperu.Utilitario;
import pe.com.petroperu.ad.FConexionDirectorio;
import pe.com.petroperu.ad.model.Usuario;
import pe.com.petroperu.ad.util.Enviroment;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.cliente.model.CompletarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.emision.AsignarDocumento;
import pe.com.petroperu.cliente.model.emision.ConCopia;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExternaRespuesta;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInternaRespuesta;
import pe.com.petroperu.cliente.model.emision.Destinatario;
import pe.com.petroperu.cliente.model.emision.DestinoRespuesta;
import pe.com.petroperu.cliente.model.emision.Folder;
import pe.com.petroperu.cliente.model.emision.PropiedadesDocumento;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.UsuarioRemplazo;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.ArchivoCompartido;
import pe.com.petroperu.model.emision.Correlativo;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.CorrespondenciaDestDocPagar;
import pe.com.petroperu.model.emision.CorrespondenciaEstado;
import pe.com.petroperu.model.emision.DestinatarioCopia;
import pe.com.petroperu.model.emision.DestinatarioDocPagar;
import pe.com.petroperu.model.emision.DestinatarioExterno;
import pe.com.petroperu.model.emision.DestinatarioInterno;
import pe.com.petroperu.model.emision.DestinatarioRespuesta;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.MotivoRechazo;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.model.emision.dto.CorrespondenciaDTO;
import pe.com.petroperu.model.emision.dto.DatosFirmanteDTO;
import pe.com.petroperu.notificacion.NotificacionService;
import pe.com.petroperu.service.ICorrespondenciaEmisionService;
import pe.com.petroperu.service.ICorrespondenciaService;
import pe.com.petroperu.service.dto.Flujo;
import pe.com.petroperu.service.impl.CorrespondenciaEmisionServiceImpl;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.IArchivoCompartidoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrelativoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaDestDocPagarDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaEstadoDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioCopiaDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioExternoDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioInternoDAO;
import pe.com.petroperu.sistcorr.dao.IDestinatarioRespuestaDAO;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;
import pe.com.petroperu.sistcorr.dao.IMotivoRechazoDAO;
import pe.com.petroperu.sistcorr.dao.IParametroDAO;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;
import pe.com.petroperu.util.Constante;

@Service
@PropertySource({ "classpath:application.properties" })
public class CorrespondenciaEmisionServiceImpl implements ICorrespondenciaEmisionService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${sistcorr.directorio}")
	private String apiUrlbase;

	@Value("${pe.com.petroperu.sistcorr.produccion}")
	private boolean produccion;
	
	@Value("${sistcorr.envio.mensaje}")
	private String envioMensaje;

	@Autowired
	private ICorrespondenciaDAO correspondenciaDAO;

	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;

	@Autowired
	private IDestinatarioCopiaDAO destinatarioDAO;

	@Autowired
	private IDestinatarioExternoDAO destinatarioExternoDAO;

	@Autowired
	private IDestinatarioInternoDAO destinatarioInternoDAO;

	@Autowired
	private IDestinatarioCopiaDAO destinatarioCopiaDAO;

	@Autowired
	private ICorrelativoDAO correlativoDAO;

	@Autowired
	private IFilenetDAO filenetDAO;

	@Autowired
	private IFirmarteDAO firmanteDAO;

	@Autowired
	private IMotivoRechazoDAO motivoRechazoDAO;

	@Autowired
	private ICorrespondenciaEstadoDAO estadoDAO;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NotificacionService notificacionService;

	@Autowired
	private ICorrespondenciaService correspondenciaService;
	
	// TICKET 9000003791
	@Value("${sistcorr.api.url_compartir}")
	private String urlCompartir;
	
	@Autowired
	private IArchivoCompartidoDAO archivoCompartidoDAO;
	
	// TICKET 9000003943
	@Autowired
	private IRutaAprobacionDAO rutaAprobacionDAO;
	// FIN TICKET
	
	// TICKET 9000003997
	@Autowired
	private IDestinatarioRespuestaDAO destinatarioRespuestaDAO;
	// FIN TICKET
	
	// TICKET 9000004510
	@Autowired
	private ISistcorrCliente sistcorrCliente;
	// FIN TICKET
	
	@Autowired
	private ICorrespondenciaDestDocPagarDAO corresponDestDocPagar;
	
	private final String DIRECTORIO_ADJUNTOS = "adjuntos";

	@Transactional
	public Respuesta<Correspondencia> registrar(Correspondencia correspondencia, String usuarioCrea, String nombreCompletoUsuarioCrea, List<RutaAprobacion> aprobadores, Locale locale) {
		this.LOGGER.info("[INICIO] registrar correspondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			correspondencia.setId(Long.valueOf(0L));
			correspondencia.setUsuarioCrea(usuarioCrea);
			correspondencia.setFechaCrea(new Date());
			correspondencia.setOriginador(nombreCompletoUsuarioCrea);

			if (correspondencia.getFechaDocumento().after(new Date())) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.correspondencia.registro.fecha.error", null, locale));
			}

			for (ArchivoAdjunto archivo : correspondencia.getAdjuntos()) {
				archivo.setId(Long.valueOf(0L));
				archivo.setUsuarioCrea(usuarioCrea);
				archivo.setFechaCrea(new Date());
				archivo.setCorrespondencia(correspondencia);
			}

			for (DestinatarioInterno interno : correspondencia.getDetalleInterno()) {
				interno.setId(Long.valueOf(0L));
				interno.setUsuarioCrea(usuarioCrea);
				interno.setFechaCrea(new Date());
				interno.setCorrespondencia(correspondencia);
			}
			for (DestinatarioExterno externo : correspondencia.getDetalleExterno()) {
				externo.setId(Long.valueOf(0L));
				externo.setUsuarioCrea(usuarioCrea);
				externo.setFechaCrea(new Date());
				externo.setCorrespondencia(correspondencia);
			}

			for (DestinatarioCopia copia : correspondencia.getDetalleCopia()) {
				copia.setId(Long.valueOf(0L));
				copia.setUsuarioCrea(usuarioCrea);
				copia.setFechaCrea(new Date());
				copia.setCorrespondencia(correspondencia);
			}
			//inicio ticket 9000004765
			for (CorrespondenciaDestDocPagar docPagar : correspondencia.getDetalleCorrespDestDocPagar()) {
				docPagar.setId(Long.valueOf(0L));
				docPagar.setUsuarioCrea(usuarioCrea);
				docPagar.setFechaCrea(new Date());
				
				docPagar.setCorrespondencia(correspondencia);
			}
			//fin ticket 9000004765
			
			Date fechaActual = new Date();
			correspondencia.getFechaDocumento().setHours(fechaActual.getHours());
			correspondencia.getFechaDocumento().setMinutes(fechaActual.getMinutes());
			correspondencia.getFechaDocumento().setSeconds(fechaActual.getSeconds());
			Correlativo correlativo = generarCorrelativo(correspondencia.getCodLugarTrabajo(),
					correspondencia.getCodDependencia(), Integer.valueOf((new Date()).getYear() + 1900),
					correspondencia.getLugarTrabajo(), correspondencia.getDependencia(), usuarioCrea, locale);
			// TICKET 7000003379
			if("".equalsIgnoreCase(correlativo.getSiglaDependencia())){
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrarCorrespondenciaSiglaDependencia.error", null, locale));
			}
			//FIN TICKET

			if (correspondencia.isFirmaDigital()) {
				boolean hayDocPrincipal = false;
				for (ArchivoAdjunto a : correspondencia.getAdjuntos()) {
					if (a.isPrincipal()) {
						hayDocPrincipal = true;
						break;
					}
				}
				if (hayDocPrincipal) {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_ASIGNAR));
				} else {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_DOCUMENTOS));
				}
			} else {
				boolean tienePDF = false;
				for (ArchivoAdjunto adj : correspondencia.getAdjuntos()) {
					if (adj.getNombre().toLowerCase().contains("pdf")) {
						tienePDF = true;
						break;
					}
				}
				if (tienePDF) {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_COMPLETADA));
				} else {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_FIRMA_MANUAL));
				}
			}

			correlativo = (Correlativo) this.correlativoDAO.save(correlativo);
			if (correlativo == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_correspondencia.error", null, locale));
			}
			correspondencia.setResponsable(usuarioCrea);
			correspondencia.setCorrelativo(correlativo);
			LOGGER.info("JERARQUIA: " + correspondencia.isJerarquia());
			correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
			if (correspondencia == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_correspondencia.error", null, locale));
			}
			if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_SIN_ASIGNAR)) {
				UsuarioPetroperu _firmante = obtenerUsuario();
				// TICKET 9000003908 - Agregar campo Nro Flujo
				// TICKET 9000003943
				Respuesta<Firmante> res = new Respuesta<>();
				if(correspondencia.isRutaAprobacion()){
					int prim = 0;
					List<RutaAprobacion> aprobs = aprobadores.stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
					RutaAprobacion aprobInicial = aprobs.get(0);
					if(aprobInicial.getUsuario().equalsIgnoreCase(correspondencia.getUsuarioCrea())){
						boolean encont = false;
						int i = 0;
						while(i<aprobs.size() && !encont){
							if(aprobs.get(i).getFirmante()!=null){
								prim = i;
								encont = true;
							}
							i++;
						}
						RutaAprobacion ra = aprobadores.get(prim);
						String codDep = "";
						String nomDep = "";
						if("2".equalsIgnoreCase(ra.getTipoFirmante())){
							if("".equalsIgnoreCase(ra.getCodDependencia())){
								List<ItemFilenet> lista = this.filenetDAO.obtenerDependenciaPorUsuario(ra.getUsuario());
								if(lista.size()>0){
									codDep = lista.get(0).getCodigo();
									nomDep = lista.get(0).getDescripcion();
								}
							}
						}
						if("1".equalsIgnoreCase(ra.getTipoFirmante())){
							ItemFilenet listaUsuario = this.filenetDAO.obtenerFirmante(ra.getCodDependencia());
							codDep = ra.getCodDependencia();
							nomDep = ra.getDependenciaNombre();
							if(listaUsuario!=null){
								ra.setUsuario(listaUsuario.getCodigo());
								ra.setUsuarioNombre(listaUsuario.getDescripcion());
							}
						}
						ItemFilenet lugar = filenetDAO.obtenerLugarPorDependencia(codDep);
						res = agregarFirmante(correspondencia.getId(), usuarioCrea, ra.getUsuario(), 
								ra.getUsuarioNombre(), codDep, nomDep, lugar.getCodigo(), 
								lugar.getDescripcion(), usuarioCrea, correspondencia.getNroFlujo(), locale);
					}
				}else{
					if(correspondencia.isPrimerFirmante())
						res = agregarFirmante(correspondencia.getId(), usuarioCrea, usuarioCrea, _firmante

							.getNombreCompleto(), correspondencia.getCodDependencia(), correspondencia.getDependencia(),
							correspondencia.getCodLugarTrabajo(), correspondencia.getLugarTrabajo(), usuarioCrea, correspondencia.getNroFlujo(), locale);
				}
				

				if (!res.estado) {
					respuesta.estado = res.estado;
					respuesta.mensaje = res.mensaje;
				} else {
					correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(correspondencia.getId());
				}
			}
			respuesta.datos.add(correspondencia);
			Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
			String mensaje = MessageFormat.format(
					this.messageSource.getMessage("sistcorr.registrar_correspondencia.exito", null, locale),
					parametros);
			respuesta.mensaje = mensaje;
			respuesta.estado = true;
			// TICKET 9000003791
			List<ArchivoAdjunto> archivos = correspondencia.getAdjuntos();
			for(ArchivoAdjunto a : archivos){
				LOGGER.info("Archivo " + a.getNombre() + " es principal: " + a.isPrincipal());
				if(a.isPrincipal()){
					String clave = Utilitario.generateKey();
					if(correspondencia.getTipoEmision().getId()==2){
						String destino = apiUrlbase + DIRECTORIO_ADJUNTOS + "/_" + a.getNombreServidor();
						PDFTool pdf = new PDFTool(a.getUbicacion(), destino, urlCompartir, clave, correspondencia.getCorrelativo().getCodigo(), correspondencia.getTipoCorrespondencia());
						pdf.writePDF();
						File fileOrigen = new File(destino);
						File fileDestino = new File(a.getUbicacion());
						FileCopyUtils.copy(fileOrigen, fileDestino);
						fileOrigen.delete();
					}
					ArchivoCompartido ac = new ArchivoCompartido();
					ac.setArchivo(a);
					ac.setClave(clave);
					ac.setFechaCrea(new Date());
					ac.setUsuarioCrea(correspondencia.getUsuarioCrea());
					ac.setCompartido(false);
					archivoCompartidoDAO.save(ac);
				}
			}
			// FIN TICKET 9000003791
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] registrar correspondencia", e);
		}
		this.LOGGER.info("[FIN] registrar correspondencia");
		return respuesta;
	}

	public Respuesta<Correspondencia> modificar(Correspondencia correspondencia, String usuarioModifica, List<RutaAprobacion> aprobadores,
			Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		this.LOGGER.info("[INICIO] modificar correspondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondenciaAnterior = (Correspondencia) this.correspondenciaDAO
					.findOne(correspondencia.getId());
			if (correspondenciaAnterior == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.buscar_correspondencia.error", null, locale));
			}
			// TICKET 9000003994 - POST
			/*if (!correspondencia.getResponsable().equals(usuarioModifica)) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}*/
			boolean permiso = false;
			if(correspondencia.getResponsable().equals(usuarioModifica)){
				permiso = true;
			}
			if(permiso==false){
				List<ItemFilenet> dependencias = filenetDAO.obtenerDependenciasGestor(usuarioModifica);
				if(dependencias!=null){
					for(int i=0;i<dependencias.size();i++){
						ItemFilenet item = dependencias.get(i);
						if(correspondencia.getResponsable().toUpperCase().equals(item.getCodigoAux().toUpperCase())){
							permiso = true;
						}
					}
				}
			}
			if(permiso==false){
				// TICKET 9000004409	
				boolean existeReemplazo = false;
				for (UsuarioRemplazo user : usuarioRemplazo) {
					LOGGER.info("UsuarioRemplazo: " + user.getUsername());
					if (correspondencia.getResponsable().equals(user.getUsername().toLowerCase())) {
						existeReemplazo = true;
						break;
					}
				}
				if (permiso == false && !existeReemplazo) {
					throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
				}
				// FIN TICKET 9000004409
			}
			// FIN TICKET
			if (correspondencia.getFechaDocumento().after(new Date())) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.correspondencia.registro.fecha.error", null, locale));
			}
			if (!correspondenciaAnterior.getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA)) {
				correspondencia.setFechaModifica(new Date());
				correspondencia.setUsuarioModifica(usuarioModifica);
				correspondencia.setResponsable(correspondenciaAnterior.getResponsable());
				correspondencia.setFechaCrea(correspondenciaAnterior.getFechaCrea());
				correspondencia.setUsuarioCrea(correspondenciaAnterior.getUsuarioCrea());
				correspondencia.setCodLugarTrabajo(correspondenciaAnterior.getCodLugarTrabajo());
				correspondencia.setLugarTrabajo(correspondenciaAnterior.getLugarTrabajo());
				correspondencia.setCodDependencia(correspondenciaAnterior.getCodDependencia());
				correspondencia.setDependencia(correspondenciaAnterior.getDependencia());

				for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
					if (((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).getId().equals(Long.valueOf(0L))) {
						((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).setFechaCrea(new Date());
						((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).setUsuarioCrea(usuarioModifica);
					} else {
						
						ArchivoAdjunto arcAdjuntoBD = this.archivoAdjuntoDAO
						.findOne(((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).getId());
						arcAdjuntoBD.setPrincipal(((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).isPrincipal());
						
						correspondencia.getAdjuntos().set(i, arcAdjuntoBD);
					}
					((ArchivoAdjunto) correspondencia.getAdjuntos().get(i)).setCorrespondencia(correspondencia);
				}

				for (int i = 0; i < correspondencia.getDetalleInterno().size(); i++) {
					if (((DestinatarioInterno) correspondencia.getDetalleInterno().get(i)).getId()
							.equals(Long.valueOf(0L))) {
						((DestinatarioInterno) correspondencia.getDetalleInterno().get(i)).setFechaCrea(new Date());
						((DestinatarioInterno) correspondencia.getDetalleInterno().get(i))
								.setUsuarioCrea(usuarioModifica);
					} else {
						correspondencia.getDetalleInterno().set(i, this.destinatarioInternoDAO
								.findOne(((DestinatarioInterno) correspondencia.getDetalleInterno().get(i)).getId()));
					}
					((DestinatarioInterno) correspondencia.getDetalleInterno().get(i))
							.setCorrespondencia(correspondencia);
				}

				for (int i = 0; i < correspondencia.getDetalleExterno().size(); i++) {
					if (((DestinatarioExterno) correspondencia.getDetalleExterno().get(i)).getId()
							.equals(Long.valueOf(0L))) {
						((DestinatarioExterno) correspondencia.getDetalleExterno().get(i)).setFechaCrea(new Date());
						((DestinatarioExterno) correspondencia.getDetalleExterno().get(i))
								.setUsuarioCrea(usuarioModifica);
					} else {
						/*correspondencia.getDetalleExterno().set(i, this.destinatarioExternoDAO
								.findOne(((DestinatarioExterno) correspondencia.getDetalleExterno().get(i)).getId()));*/
						DestinatarioExterno deBD = this.destinatarioExternoDAO
								.findOne(((DestinatarioExterno) correspondencia.getDetalleExterno().get(i)).getId());
								deBD.setCorreoDestinatario(correspondencia.getDetalleExterno().get(i).getCorreoDestinatario());
								
						correspondencia.getDetalleExterno().set(i, deBD);
					}
					((DestinatarioExterno) correspondencia.getDetalleExterno().get(i))
							.setCorrespondencia(correspondencia);
				}

				for (int i = 0; i < correspondencia.getDetalleCopia().size(); i++) {
					if (((DestinatarioCopia) correspondencia.getDetalleCopia().get(i)).getId()
							.equals(Long.valueOf(0L))) {
						((DestinatarioCopia) correspondencia.getDetalleCopia().get(i)).setFechaCrea(new Date());
						((DestinatarioCopia) correspondencia.getDetalleCopia().get(i)).setUsuarioCrea(usuarioModifica);
					} else {
						correspondencia.getDetalleCopia().set(i, this.destinatarioCopiaDAO
								.findOne(((DestinatarioCopia) correspondencia.getDetalleCopia().get(i)).getId()));
					}
					((DestinatarioCopia) correspondencia.getDetalleCopia().get(i)).setCorrespondencia(correspondencia);
				}
				
				//inicio ticket 9000004765
				if(correspondencia.getDetalleCorrespDestDocPagar() != null && correspondencia.getDetalleCorrespDestDocPagar().size() > 0) {
					List<CorrespondenciaDestDocPagar> corrDPBD = this.corresponDestDocPagar.obtenerCorrespDestDocPagar(correspondencia.getId());
					
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setId(corrDPBD.get(0).getId());
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setCorrespondencia(correspondencia);
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setFechaCrea(corrDPBD.get(0).getFechaCrea());
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setUsuarioCrea(corrDPBD.get(0).getUsuarioCrea());
					
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setFechaModifica(new Date());
					correspondencia.getDetalleCorrespDestDocPagar().get(0).setUsuarioModifica(usuarioModifica);
				}
				//fin ticket 9000004765

				if (correspondencia.isFirmaDigital()) {
					boolean hayDocPrincipal = false;
					for (ArchivoAdjunto a : correspondencia.getAdjuntos()) {
						if (a.isPrincipal()) {
							hayDocPrincipal = true;
							break;
						}
					}
					List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
					int nroFirmantes = 0;
					if(firmantes!=null){
						for(int i=0;i<firmantes.size();i++){
							if(firmantes.get(i).getNroFlujo()==correspondencia.getNroFlujo()){
								nroFirmantes = nroFirmantes + 1;
							}
						}
					}
					firmantes = (firmantes == null) ? new ArrayList<>() : firmantes;
					//if (hayDocPrincipal == true && firmantes.size() == 0) {
					if (hayDocPrincipal == true && nroFirmantes == 0) {
						correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_ASIGNAR));
					//} else if (!hayDocPrincipal && firmantes.size() == 0) {
					} else if (!hayDocPrincipal && nroFirmantes == 0) {
						correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_DOCUMENTOS));
					}

				} else {

					boolean tienePDF = false;
					for (ArchivoAdjunto adj : correspondencia.getAdjuntos()) {
						if (adj.getNombre().toLowerCase().contains("pdf")) {
							tienePDF = true;
							break;
						}
					}
					if (tienePDF == true) {
						correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_COMPLETADA));
					} else {
						correspondencia
								.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_SIN_FIRMA_MANUAL));
					}
				}
				Date fechaActual = new Date();
				correspondencia.getFechaDocumento().setHours(fechaActual.getHours());
				correspondencia.getFechaDocumento().setMinutes(fechaActual.getMinutes());
				correspondencia.getFechaDocumento().setSeconds(fechaActual.getSeconds());
				correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
				if (correspondencia == null) {
					throw new Exception(
							this.messageSource.getMessage("sistcorr.modificar_correspondencia.error", null, locale));
				}
				// TICKET 9000003908
				//if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_SIN_ASIGNAR)
				// TICKET 9000003943
				Correspondencia corr = correspondenciaDAO.findOne(correspondencia.getId());
			    List<RutaAprobacion> aprobs = rutaAprobacionDAO.findAllByCorrespondencia(corr);
			    for(RutaAprobacion ra : aprobs){
			    	boolean encontrado = false;
			    	for(int j=0;j<aprobadores.size();j++){
			    		LOGGER.info("Comparacion ids aprobador:" + aprobadores.get(j).getId() + "||" + ra.getId());
			    		if(aprobadores.get(j).getId().equals(ra.getId())){
			    			encontrado = true;
			    			LOGGER.info("if encontrado:" + encontrado);
			    			break;
			    		}
			    	}
			    	LOGGER.info("for encontrado:" + encontrado);

			    	if(encontrado == false){
			    		rutaAprobacionDAO.delete(ra);
			    	}
			    }
			    for(int i=0;i<aprobadores.size();i++){
			    	LOGGER.info("Aprobador (" + i + "):" + aprobadores.get(i).getId() + "||" + aprobadores.get(i).getDependenciaNombre() + aprobadores.get(i).getUsuarioNombre());
			    	RutaAprobacion ra = aprobadores.get(i); 
			    	ra.setUsuario(ra.getUsuario().toLowerCase());
			  		
			    	/*INI Ticket 7000004827*/
			    	//if(ra.getId()>1000000L){
			  		if(ra.getId()>10000000L){
			  		/*FIN Ticket 7000004827*/
			  			ra.setId(null);
			  	  		ra.setUsuarioCrea(usuarioModifica);
			  			ra.setFechaCrea(new Date());
			  			ra.setCorrespondencia(correspondencia);
			  		}else{
			  	  		ra.setUsuarioModifica(usuarioModifica);
			  			ra.setFechaModifica(new Date());
			  		}
			  		
					LOGGER.info("Ruta Aprobacion:" + ra.toString());
			  		if(i==0 && correspondencia.getEstado().getId() == Constante.CORRESPONDENCIA_ASIGNADA){
			  			List<Firmante> respuestaFirmante = firmanteDAO.obtenerFirmantes(correspondencia.getId());
			  			if(respuestaFirmante.size()==1){
			  				ra.setFirmante(respuestaFirmante.get(0));
			  			}
			  		}
			  		LOGGER.info("Ruta Aprobacion 2:" + ra.toString());
			  		LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 617");
			  		RutaAprobacion ra2 = rutaAprobacionDAO.save(ra);
			  		LOGGER.info("ID generado:" + ra2.getId());
			    }
				// FIN TICKET
				LOGGER.info("Verificación paso manual a digital:" + correspondencia.getEstado().getId());
				if ((correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA) || correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_SIN_ASIGNAR) || 
						correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_REINICIO) || correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_POR_CORREGIR))) {
					UsuarioPetroperu _firmante = obtenerUsuario();
					// TICKET 9000003908 - Agregar campo Nro Flujo
					// TICKET 9000003943
					Respuesta<Firmante> res = new Respuesta<>();
					if(correspondencia.isRutaAprobacion()){
						int indice = 0;
						boolean encontrado = false;
						List<RutaAprobacion> _aprobs = aprobadores.stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
						boolean rutaFirmada = false;
						for(int i=0;i<_aprobs.size();i++){
							if(_aprobs.get(i).getFirmante()!=null){
								rutaFirmada = true;
							}
						}
						if(_aprobs.size()>0 && usuarioModifica.equalsIgnoreCase(_aprobs.get(0).getUsuario()) && !rutaFirmada){
							for(int i=0;i<_aprobs.size();i++){
								RutaAprobacion _ra = _aprobs.get(i);
								if(!encontrado){
									if(_ra.getFirmante()==null){
										indice = i;
										encontrado = true;
									}
								}
							}
							LOGGER.info("Es ruta aprobacion:" + aprobadores.size() + "||" + aprobadores.get(indice).getTipoFirmante());
							RutaAprobacion ra = aprobadores.get(indice);
							ItemFilenet lugar = new ItemFilenet();
							String codDependencia = "";
							String nombreDependencia = "";
							ra.setUsuario(ra.getUsuario().toLowerCase());
							if("1".equalsIgnoreCase(ra.getTipoFirmante())){
								ItemFilenet lista = this.filenetDAO.obtenerFirmante(ra.getCodDependencia());
								codDependencia = ra.getCodDependencia();
								nombreDependencia = ra.getDependenciaNombre();
								if(lista != null){
									ra.setUsuario(lista.getCodigo().toLowerCase());
									ra.setUsuarioNombre(lista.getDescripcion());
								}
								lugar = filenetDAO.obtenerLugarPorDependencia(ra.getCodDependencia());
							}
							if("2".equalsIgnoreCase(ra.getTipoFirmante())){
								List<ItemFilenet> lista = this.filenetDAO.obtenerDependenciaPorUsuario(ra.getUsuario());
								if(lista.size()>0){
									codDependencia = lista.get(0).getCodigo();
									nombreDependencia = lista.get(0).getDescripcion();
									//ra.setCodDependencia(lista.get(0).getCodigo());
									//ra.setDependenciaNombre(lista.get(0).getDescripcion());
									lugar = filenetDAO.obtenerLugarPorDependencia(lista.get(0).getCodigo());
								}
							}
							res = agregarFirmante(correspondencia.getId(), usuarioModifica, ra.getUsuario(), 
									ra.getUsuarioNombre(), codDependencia, nombreDependencia, lugar.getCodigo(), 
									lugar.getDescripcion(), usuarioModifica, correspondencia.getNroFlujo(), locale);
							ra.setFirmante(res.datos.get(0));
							LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 679");
							rutaAprobacionDAO.save(ra);
						}
					}else{
						if(correspondencia.isPrimerFirmante()){
							res = agregarFirmante(correspondencia.getId(), usuarioModifica, usuarioModifica, _firmante
									.getNombreCompleto(), correspondencia.getCodDependencia(), correspondencia.getDependencia(),
									correspondencia.getCodLugarTrabajo(), correspondencia.getLugarTrabajo(), usuarioModifica, correspondencia.getNroFlujo(), locale);
						}
					}

					if (!res.estado) {
						respuesta.estado = res.estado;
						respuesta.mensaje = res.mensaje;
					} else {
						correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(correspondencia.getId());
					}
				}
				respuesta.datos.add(correspondencia);
				String mensaje = this.messageSource.getMessage("sistcorr.modificar_correspondencia.exito", null,
						locale);
				respuesta.mensaje = mensaje;
				respuesta.estado = true;
				// TICKET 9000003791
				List<ArchivoAdjunto> archivos = correspondencia.getAdjuntos();
				for(ArchivoAdjunto a : archivos){
					LOGGER.info("Archivo " + a.getNombre() + " es principal: " + a.isPrincipal());
					if(a.isPrincipal()){
						ArchivoCompartido ac = new ArchivoCompartido();
						ac = archivoCompartidoDAO.findOneByArchivo(a);
						if(ac==null){
							String clave = Utilitario.generateKey();
							if(correspondencia.getTipoEmision().getId()==2){
								String destino = apiUrlbase + DIRECTORIO_ADJUNTOS + "/_" + a.getNombreServidor();
								PDFTool pdf = new PDFTool(a.getUbicacion(), destino, urlCompartir, clave, correspondencia.getCorrelativo().getCodigo(), correspondencia.getTipoCorrespondencia());
								pdf.writePDF();
								File fileOrigen = new File(destino);
								File fileDestino = new File(a.getUbicacion());
								FileCopyUtils.copy(fileOrigen, fileDestino);
								fileOrigen.delete();
							}
							ac = new ArchivoCompartido();
							ac.setArchivo(a);
							ac.setClave(clave);
							ac.setFechaCrea(new Date());
							ac.setUsuarioCrea(correspondencia.getUsuarioCrea());
							ac.setCompartido(false);
							archivoCompartidoDAO.save(ac);
						}
					}
				}
				// FIN TICKET 9000003791
			} else {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.modificar_correspondencia.error", null, locale));
			}
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] modificar correspondencia", e);
			respuesta.mensaje = e.getMessage();
			respuesta.estado = false;
		}
		this.LOGGER.info("[FIN] modificar correspondencia");
		return respuesta;
	}

	public Respuesta<ArchivoAdjunto> registrar(ArchivoAdjunto archivoAdjunto, String usuarioCrea, Locale locale) {
		this.LOGGER.info("[INICIO] registrar adjunto");
		Respuesta<ArchivoAdjunto> respuesta = new Respuesta<>();
		try {
			archivoAdjunto.setUsuarioCrea(usuarioCrea);
			archivoAdjunto.setFechaCrea(new Date());
			archivoAdjunto = (ArchivoAdjunto) this.archivoAdjuntoDAO.save(archivoAdjunto);
			if (archivoAdjunto == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.error", null, locale));
			}
			respuesta.datos.add(archivoAdjunto);
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.registrar_archivo_adjunto.exito", null, locale);
			respuesta.estado = true;
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[FIN] registrar adjunto");
		}
		this.LOGGER.info("[FIN] registrar adjunto");
		return respuesta;
	}

	public Respuesta<Correspondencia> buscarCorrespondencia(Long idCorrespondencia, Locale locale) {
		this.LOGGER.info("[INICIO] buscarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			if (correspondencia == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.buscar_correspondencia.error", null, locale));
			}
			for(ArchivoAdjunto aa : correspondencia.getAdjuntos()){
				ArchivoCompartido ac = archivoCompartidoDAO.findOneByArchivo(aa);
				if(ac!=null){
					aa.setKey(ac.getClave());
				}
			}
			respuesta.estado = true;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.buscar_correspondencia.exito", null, locale);
			respuesta.datos.add(correspondencia);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] buscarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] buscarCorrespondencia");
		return respuesta;
	}

	public Correlativo generarCorrelativo(String codLugar, String codDependencia, Integer anio, String lugar,
			String dependencia, String usuario, Locale locale) {
		this.LOGGER.info("[INICIO] generarCorrelativo");
		Correlativo correlativo = new Correlativo();
		Long numCorrelativo = Long.valueOf(0L);
		numCorrelativo = this.correlativoDAO.obtenerUltimoCorrelativo(codLugar, codDependencia, anio);
		String[] CGC = this.filenetDAO.obtenerCGC(codLugar, codDependencia);
		Long long_1 = numCorrelativo, long_2 = numCorrelativo = Long.valueOf(numCorrelativo.longValue() + 1L);
		// TICKET 7000003379
		//String siglaDependencia = CGC[1].equals("") ? dependencia.substring(0, 3) : CGC[1];
		//String siglaLugar = CGC[0].equals("") ? lugar.substring(0, 3) : CGC[0];
		String siglaDependencia = CGC[1];
		String siglaLugar = CGC[0];
		// FIN TICKET
		String _numCorrelativo = "0000";
		if (_numCorrelativo.length() >= numCorrelativo.toString().length()) {
			_numCorrelativo = _numCorrelativo.substring(0,
					_numCorrelativo.length() - numCorrelativo.toString().length()) + numCorrelativo.toString();
		}
		String codigo = siglaDependencia + "-" + _numCorrelativo + "-" + anio;

		correlativo.setAnio(anio.intValue());
		correlativo.setCodDependencia(codDependencia);
		correlativo.setCodLugar(codLugar);
		correlativo.setCodigo(codigo);
		correlativo.setFechaCrea(new Date());
		correlativo.setSiglaDependencia(siglaDependencia);
		correlativo.setSiglaLugar(siglaLugar);
		correlativo.setCorrelativo(Long.valueOf(numCorrelativo.longValue()));
		correlativo.setUsuarioCrea(usuario);
		this.LOGGER.info("[FIN] generarCorrelativo");
		return correlativo;
	}

	public Respuesta<CorrespondenciaDTO> correspondenciasPendientes(String bandeja, String usuario, String correlativo,
			String asunto, boolean rechazados, boolean declinados, String fechaInicio, String fechaFin, String dorig, String tcorre, Locale locale) {//ticket 9000003866 add dorig, tcorre
		this.LOGGER.info("[INICIO] correspondenciasPendientes");
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();
		try {
			if (!"".equals(fechaInicio) && !"".equals(fechaFin)) {
				Date desde = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaInicio);
				Date fin = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaFin);
				if (desde.after(fin))
					throw new Exception(this.messageSource.getMessage("sistcorr.bandeja.error", null, locale));
			}
			List<Object[]> _respuesta = this.correspondenciaDAO.bandejaPendientes(bandeja, usuario, correlativo, asunto,
					rechazados ? 1 : 0, declinados ? 1 : 0, fechaInicio, fechaFin, ((dorig == null)?("0"):(dorig)), ((tcorre == null)?("0"):(tcorre)));//ticket 9000003866 add dorig, tcorre
			respuesta.estado = true;
			for (Object[] corr : _respuesta) {
				CorrespondenciaDTO dto = new CorrespondenciaDTO();
				dto.setId(Long.valueOf(Long.parseLong(String.valueOf(corr[0]))));
				dto.setUsuario(String.valueOf(corr[1]));
				dto.setCodigoTipoCorrespondencia(String.valueOf(corr[2]));
				dto.setTipoCorrespondencia(String.valueOf(corr[3]));
				dto.setAsunto(String.valueOf(corr[4]));
				dto.setIdCorrelativo(Long.valueOf(String.valueOf(corr[5])));
				dto.setCorrelativo(String.valueOf(corr[6]));
				dto.setFechaDocumento(String.valueOf(corr[7]));
				dto.setFirmaDigital(Boolean.parseBoolean(String.valueOf(corr[8])));
				dto.setSolicitante(String.valueOf(corr[9]));
				dto.setFirmante(String.valueOf(corr[10]));
				dto.setEstado(new CorrespondenciaEstado(Long.valueOf(Long.parseLong(String.valueOf(corr[11]))),
						String.valueOf(corr[12]), String.valueOf(corr[13]), String.valueOf(corr[14])));
				dto.setCorrelativoFilenet(String.valueOf(corr[15]));
				dto.setMotivoRechazo((corr[16] == null) ? "" : String.valueOf(corr[16]));
				//LOGGER.info("Motivo Rechazo(" + corr[6] + "):" + corr[16]);
				dto.setFirmantePrevio((corr[17] == null) ? "" : String.valueOf(corr[17]));
				dto.setRutaAprobacion(Boolean.parseBoolean(String.valueOf(corr[18])));
				// TICKET 9000004510
				//LOGGER.info("Fecha modificacion " + corr[6] + ": " + corr[19]);
				// FIN TICKET
				dto.setFechaModificacion(String.valueOf(corr[19])); // TICKET 9000003993
				respuesta.datos.add(dto);
			}
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.exito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.error", null, locale);
			this.LOGGER.error("[ERROR] correspondenciasPendientes", e);
		}
		this.LOGGER.info("[FIN] correspondenciasPendientes");
		return respuesta;
	}

	public Respuesta<CorrespondenciaDTO> correspondencias(String bandeja, String usuario, String correlativo,
			String asunto, boolean rechazados, boolean declinados, String fechaInicio, String fechaFin, Locale locale) {
		this.LOGGER.info("[INICIO] correspondencias");
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();
		try {
			if (!"".equals(fechaInicio) && !"".equals(fechaFin)) {
				Date desde = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaInicio);
				Date fin = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaFin);
				if (desde.after(fin))
					throw new Exception(this.messageSource.getMessage("sistcorr.bandeja.error", null, locale));
			}
			List<Object[]> _respuesta = this.correspondenciaDAO.bandeja(bandeja, usuario, correlativo, asunto,
					rechazados ? 1 : 0, declinados ? 1 : 0, fechaInicio, fechaFin);
			respuesta.estado = true;
			for (Object[] corr : _respuesta) {
				CorrespondenciaDTO dto = new CorrespondenciaDTO();
				dto.setId(Long.valueOf(Long.parseLong(String.valueOf(corr[0]))));
				dto.setUsuario(String.valueOf(corr[1]));
				dto.setCodigoTipoCorrespondencia(String.valueOf(corr[2]));
				dto.setTipoCorrespondencia(String.valueOf(corr[3]));
				dto.setAsunto(String.valueOf(corr[4]));
				dto.setIdCorrelativo(Long.valueOf(String.valueOf(corr[5])));
				dto.setCorrelativo(String.valueOf(corr[6]));
				dto.setFechaDocumento(String.valueOf(corr[7]));
				dto.setFirmaDigital(Boolean.parseBoolean(String.valueOf(corr[8])));
				dto.setSolicitante(String.valueOf(corr[9]));
				dto.setFirmante(String.valueOf(corr[10]));
				dto.setEstado(new CorrespondenciaEstado(Long.valueOf(Long.parseLong(String.valueOf(corr[11]))),
						String.valueOf(corr[12]), String.valueOf(corr[13]), String.valueOf(corr[14])));
				dto.setCorrelativoFilenet(String.valueOf(corr[15]));
				dto.setMotivoRechazo((corr[16] == null) ? "" : String.valueOf(corr[16]));
				dto.setFirmantePrevio((corr[17] == null) ? "" : String.valueOf(corr[17]));
				respuesta.datos.add(dto);
			}
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.exito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.error", null, locale);
			this.LOGGER.error("[ERROR] correspondencias", e);
		}
		this.LOGGER.info("[FIN] correspondencias");
		return respuesta;
	}
	
	public Respuesta<CorrespondenciaDTO> correspondencias(String bandeja, String usuario, String correlativo, String asunto, 
			boolean rechazados, boolean misPendientes, boolean declinados, String fechaInicio, String fechaFin, List<UsuarioRemplazo> usuarioRemplazo, Locale locale) {
		this.LOGGER.info("[INICIO] correspondencias");
		Respuesta<CorrespondenciaDTO> respuesta = new Respuesta<>();
		// TICKET 9000004409
		String userRemplazo = "";
		if (usuarioRemplazo.size() > 0){
			for (UsuarioRemplazo user : usuarioRemplazo) {
				LOGGER.info("Usuario: "  + user.getUsername());
				userRemplazo = userRemplazo + user.getUsername() + ",";
			}
			userRemplazo = userRemplazo.substring(0, userRemplazo.length()-1);
		}
		// fin TICKET 9000004409
		// TICKET 9000003994
		this.LOGGER.info("Parametros:" + bandeja + "|" + usuario + "|" + correlativo + "|" + asunto + "|" + rechazados + "|" + misPendientes + "|" + declinados + "|" + fechaInicio + "|" + fechaFin);
		// FIN TICKET
		try {
			if (!"".equals(fechaInicio) && !"".equals(fechaFin)) {
				Date desde = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaInicio);
				Date fin = (new SimpleDateFormat("dd/MM/yyyy")).parse(fechaFin);
				if (desde.after(fin))
					throw new Exception(this.messageSource.getMessage("sistcorr.bandeja.error", null, locale));
			}
			List<Object[]> _respuesta = this.correspondenciaDAO.bandeja(bandeja, usuario, correlativo, asunto,
					rechazados ? 1 : 0, misPendientes ? 1 : 0, declinados ? 1 : 0, fechaInicio, fechaFin, userRemplazo );
			respuesta.estado = true;
			for (Object[] corr : _respuesta) {
				CorrespondenciaDTO dto = new CorrespondenciaDTO();
				dto.setId(Long.valueOf(Long.parseLong(String.valueOf(corr[0]))));
				dto.setUsuario(String.valueOf(corr[1]));
				dto.setCodigoTipoCorrespondencia(String.valueOf(corr[2]));
				dto.setTipoCorrespondencia(String.valueOf(corr[3]));
				dto.setAsunto(String.valueOf(corr[4]));
				dto.setIdCorrelativo(Long.valueOf(String.valueOf(corr[5])));
				dto.setCorrelativo(String.valueOf(corr[6]));
				dto.setFechaDocumento(String.valueOf(corr[7]));
				dto.setFirmaDigital(Boolean.parseBoolean(String.valueOf(corr[8])));
				dto.setSolicitante(String.valueOf(corr[9]));
				dto.setFirmante(String.valueOf(corr[10]));
				dto.setEstado(new CorrespondenciaEstado(Long.valueOf(Long.parseLong(String.valueOf(corr[11]))),
						String.valueOf(corr[12]), String.valueOf(corr[13]), String.valueOf(corr[14])));
				dto.setCorrelativoFilenet(String.valueOf(corr[15]));
				dto.setMotivoRechazo((corr[16] == null) ? "" : String.valueOf(corr[16]));
				dto.setFirmantePrevio((corr[17] == null) ? "" : String.valueOf(corr[17]));
				dto.setRutaAprobacion(corr[20] == null ? false : Boolean.parseBoolean(String.valueOf(corr[20])));
				dto.setFechaModificacion((corr[21] == null) ? "" : String.valueOf(corr[21])); // TICKET 9000003993
				dto.setFlgRemplazo((corr[22] == null) ? "" : String.valueOf(corr[22])); // TICKET 9000004409
				dto.setUserRemplazo((corr[23] == null) ? "" : String.valueOf(corr[23])); // TICKET 9000004409
				
				if (usuarioRemplazo.size() > 0){
					for (UsuarioRemplazo user : usuarioRemplazo) {
						if (dto.getUserRemplazo().toLowerCase().equals(user.getUsername().toLowerCase())){
						dto.setNombreRemplazo(user.getNombres());
						break;
						}
					}
				}
				respuesta.datos.add(dto);
			}
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.exito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.bandeja.error", null, locale);
			this.LOGGER.error("[ERROR] correspondencias", e);
		}
		this.LOGGER.info("[FIN] correspondencias");
		return respuesta;
	}

	public Respuesta<ArchivoAdjunto> buscarArchivoAdjunto(Long idArchivoAdjunto, Locale locale) {
		this.LOGGER.info("[INICIO] buscarArchivoAdjunto");
		Respuesta<ArchivoAdjunto> respuesta = new Respuesta<>();
		try {
			ArchivoAdjunto archivoAdjunto = (ArchivoAdjunto) this.archivoAdjuntoDAO.findOne(idArchivoAdjunto);
			if (archivoAdjunto == null) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.archivo_adjunto.descargar.error", null, locale));
			}
			respuesta.estado = true;
			respuesta.datos.add(archivoAdjunto);
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.archivo_adjunto.descargar.exito", null, locale);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] buscarArchivoAdjunto", e);
		}
		this.LOGGER.info("[FIN] buscarArchivoAdjunto");
		return respuesta;
	}

	@Transactional
	public Respuesta<Firmante> agregarFirmanteVistosPrevios(Long idCorrespondencia, String usuarioCrea, String codFirmante,
			String nombreFirmante, String codDependenciaFirmante, String dependenciaFirmante,
			String codLugarTrabajoFirmante, String lugarTrabajoFirmante, String solicitante, Integer nroFlujo, Locale locale) {
		this.LOGGER.info("[INICIO] agregarFirmanteVistosPrevios");
		Respuesta<Firmante> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			// TICKET 9000003908 - Aumentar estado permitido para realizar asignacion
			if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_SIN_ASIGNAR)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_POR_CORREGIR)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_REINICIO)) {

				if(!validarSiPuedeAsignarFirma(correspondencia, usuarioCrea))
					throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));

				CorrespondenciaEstado estadoAsignado = (CorrespondenciaEstado) this.estadoDAO
						.findOne(Constante.CORRESPONDENCIA_ASIGNADA);
				if (estadoAsignado == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
				}
				Firmante firmante = new Firmante();
				firmante.setCorrespondencia(correspondencia);
				firmante.setCodFirmante(codFirmante);
				firmante.setNombreFirmante(nombreFirmante);
				firmante.setCodDependenciaFirmante(codDependenciaFirmante);
				firmante.setDependenciaFirmante(dependenciaFirmante);
				firmante.setPuestoFirmante("Jefe de " + dependenciaFirmante);
				firmante.setCodLugarTrabajoFirmante(codLugarTrabajoFirmante);
				firmante.setLugarTrabajoFirmante(lugarTrabajoFirmante);
				firmante.setFecha(new Date());
				firmante.setEstado(estadoAsignado);
				firmante.setSolicitante(usuarioCrea);
				firmante.setUsuarioCrea(usuarioCrea);
				firmante.setFechaCrea(new Date());
				// TICKET 9000003908
				firmante.setNroFlujo(nroFlujo);

				if (this.notificacionService.notificarAsignacionParaFirmar(correspondencia, firmante, locale)) {
					// TICKET 9000003943
					boolean insertarRuta = false;
					boolean insertarRuta2 = false;
					RutaAprobacion raInsert = new RutaAprobacion();
					RutaAprobacion raInsertNuevo = new RutaAprobacion();
					RutaAprobacion raInsertDup = new RutaAprobacion();
					int ordenDup = 0;
					if(correspondencia.isRutaAprobacion()){
						List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(idCorrespondencia);
						Firmante lastFirmante = new Firmante();
						Long idLastFirmante = 0L;
						for(int i=0;i<firmantes.size();i++){
							Firmante faux = firmantes.get(i);
							if(faux.getId()>idLastFirmante){
								lastFirmante = faux;
								idLastFirmante = faux.getId();
							}
						}
						if(correspondencia.getEstado().getId() == Constante.CORRESPONDENCIA_POR_CORREGIR){
							List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondencia).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
							if(lastFirmante.getMotivoRechazo() != null && lastFirmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_MAL_ASIGNADO){
								// CAMBIANDO ESTADO EN CASO ULTIMO APROBADOR SEA EL QUE RECHAZO
								RutaAprobacion ultimoAprobador = aprobadores.get(aprobadores.size()-1);
								if(ultimoAprobador.getFirmante() != null){
									ultimoAprobador.setFirmante(null);
									LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1078");
									rutaAprobacionDAO.save(ultimoAprobador);
								}
								aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondencia).stream().sorted(Comparator.comparingLong(RutaAprobacion::getOrden)).collect(Collectors.toList());
								// FIN CAMBIO
								insertarRuta = true;
								RutaAprobacion ra;
								int orden = 1000;
								for(int i=0;i<aprobadores.size();i++){
									ra = aprobadores.get(i);
									ra.setUsuario(ra.getUsuario().toLowerCase());
									if(ra.getFirmante()==null){
										if(ra.getOrden()<orden){
											orden = ra.getOrden();
										}
										ra.setOrden(ra.getOrden()+1);
										LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1094");
										rutaAprobacionDAO.save(ra);
									}
								}
								raInsert.setCorrespondencia(correspondencia);
								raInsert.setUsuario(codFirmante.toLowerCase());
								raInsert.setUsuarioNombre(nombreFirmante);
								//raInsert.setCodDependencia(codDependenciaFirmante);
								raInsert.setCodDependencia("");
								//raInsert.setDependenciaNombre(dependenciaFirmante);
								raInsert.setDependenciaNombre("");
								raInsert.setOrden(orden);
								raInsert.setTipoFirmante("2");
								raInsert.setUsuarioCrea(usuarioCrea);
								raInsert.setFechaCrea(new Date());
							}
							if(lastFirmante.getMotivoRechazo() != null && lastFirmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_REQUIERE_VISTOS_PREVIOS){
								insertarRuta2 = true;
								RutaAprobacion ra2;
								int ordenMin = 1000;
								for(int i=0;i<aprobadores.size();i++){
									ra2 = aprobadores.get(i);
									ra2.setUsuario(ra2.getUsuario().toLowerCase());
									LOGGER.info("Aprobador pre (" + ra2.getId() + "):" + ra2.getOrden());
									if(ra2.getFirmante()==null){
										if(ra2.getOrden()<ordenMin){
											ordenMin = ra2.getOrden();
											raInsertNuevo.setCorrespondencia(correspondencia);
											raInsertNuevo.setOrden(ra2.getOrden()-1);
											//raInsertNuevo.setCodDependencia(firmante.getCodDependenciaFirmante());
											raInsertNuevo.setCodDependencia("");
											//raInsertNuevo.setDependenciaNombre(firmante.getDependenciaFirmante());
											raInsertNuevo.setDependenciaNombre("");
											raInsertNuevo.setUsuario(firmante.getCodFirmante().toLowerCase());
											raInsertNuevo.setUsuarioNombre(firmante.getNombreFirmante());
											raInsertNuevo.setEstado("PENDIENTE");
											raInsertNuevo.setTipoFirmante("2");
											raInsertNuevo.setNombreTipo("Participante");
											raInsertNuevo.setUsuarioCrea(usuarioCrea);
											raInsertNuevo.setFechaCrea(new Date());
										}
										ra2.setOrden(ra2.getOrden()+1);
										LOGGER.info("Actualizando orden de registro de ruta id (" + ra2.getId() + "): " + ra2.getOrden());
										LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1137");
										ra2 = rutaAprobacionDAO.save(ra2);
									}else{
										if(ra2.getOrden()>ordenDup){
											ordenDup = ra2.getOrden();
											raInsertDup.setCorrespondencia(correspondencia);
											raInsertDup.setCodDependencia(ra2.getCodDependencia());
											raInsertDup.setDependenciaNombre(ra2.getDependenciaNombre());
											raInsertDup.setUsuario(ra2.getUsuario().toLowerCase());
											raInsertDup.setUsuarioNombre(ra2.getUsuarioNombre());
											raInsertDup.setOrden(ra2.getOrden()+1);
											raInsertDup.setEstado(ra2.getEstado());
											raInsertDup.setTipoFirmante(ra2.getTipoFirmante());
											raInsertDup.setNombreTipo(ra2.getNombreTipo());
											raInsertDup.setUsuarioCrea(ra2.getUsuarioCrea());
											raInsertDup.setFechaCrea(ra2.getFechaCrea());
											raInsertDup.setId(ra2.getId());
											raInsertDup.setFechaModifica(ra2.getFechaModifica());
											raInsertDup.setUsuarioModifica(ra2.getUsuarioModifica());
											//raInsertDup = ra2;
											//raInsertDup.setOrden(ra2.getOrden() + 1);
										}
									}
									LOGGER.info("Aprobador post (" + ra2.getId() + "):" + ra2.getOrden());
								}
								if(ordenMin==1000){
									raInsertNuevo.setCorrespondencia(correspondencia);
									raInsertNuevo.setOrden(aprobadores.get(aprobadores.size()-1).getOrden());
									raInsertNuevo.setCodDependencia("");
									raInsertNuevo.setDependenciaNombre("");
									raInsertNuevo.setUsuario(firmante.getCodFirmante().toLowerCase());
									raInsertNuevo.setUsuarioNombre(firmante.getNombreFirmante());
									raInsertNuevo.setEstado("PENDIENTE");
									raInsertNuevo.setTipoFirmante("2");
									raInsertNuevo.setNombreTipo("Participante");
									raInsertNuevo.setUsuarioCrea(usuarioCrea);
									raInsertNuevo.setFechaCrea(new Date());
								}
							}
						}
						
						
					}
					// FIN TICKET
					correspondencia.setEstado(estadoAsignado);
					correspondencia.setUsuarioModifica(usuarioCrea);
					correspondencia.setFechaModifica(new Date());
					correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
					if (correspondencia == null) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
					}
					firmante = (Firmante) this.firmanteDAO.save(firmante);
					if (firmante == null) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
					}
					// TICKET 9000003943
					if(insertarRuta){
						raInsert.setFirmante(firmante);
						raInsert.setUsuario(raInsert.getUsuario().toLowerCase());
						LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1198");
						rutaAprobacionDAO.save(raInsert);
					}
					if(insertarRuta2){
						raInsertNuevo.setFirmante(firmante);
						raInsertNuevo.setUsuario(raInsertNuevo.getUsuario().toLowerCase());
						LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1204");
						rutaAprobacionDAO.save(raInsertNuevo);
						raInsertDup.setFirmante(null);
						raInsertDup.setOrden(ordenDup + 1);
						raInsertDup.setUsuario(raInsertDup.getUsuario().toLowerCase());
						LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1209");
						rutaAprobacionDAO.save(raInsertDup);
					}
					// FIN TICKET
					respuesta.estado = true;
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.asignar_firma.exito", null, locale);
					respuesta.datos.add(firmante);
				} else {
					throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
				}
			} else {
				throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] agregarFirmante", e);
		}
		this.LOGGER.info("[FIN] agregarFirmante");
		return respuesta;
	}
	
	

	@Transactional
	public Respuesta<Firmante> agregarFirmante(Long idCorrespondencia, String usuarioCrea, String codFirmante,
			String nombreFirmante, String codDependenciaFirmante, String dependenciaFirmante,
			String codLugarTrabajoFirmante, String lugarTrabajoFirmante, String solicitante, Integer nroFlujo, Locale locale) {
		this.LOGGER.info("[INICIO] agregarFirmante");
		Respuesta<Firmante> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			// TICKET 9000003908 - Aumentar estado permitido para realizar asignacion
			LOGGER.info("Estado actual de correspondencia:" + correspondencia.getEstado().getId());
			if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_SIN_ASIGNAR)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_POR_CORREGIR)
					|| correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_REINICIO)) {

				if(!validarSiPuedeAsignarFirma(correspondencia, usuarioCrea))
					throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));

				CorrespondenciaEstado estadoAsignado = (CorrespondenciaEstado) this.estadoDAO
						.findOne(Constante.CORRESPONDENCIA_ASIGNADA);
				if (estadoAsignado == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
				}
				Firmante firmante = new Firmante();
				firmante.setCorrespondencia(correspondencia);
				firmante.setCodFirmante(codFirmante);
				firmante.setNombreFirmante(nombreFirmante);
				firmante.setCodDependenciaFirmante(codDependenciaFirmante);
				firmante.setDependenciaFirmante(dependenciaFirmante);
				firmante.setPuestoFirmante("Jefe de " + dependenciaFirmante);
				firmante.setCodLugarTrabajoFirmante(codLugarTrabajoFirmante);
				firmante.setLugarTrabajoFirmante(lugarTrabajoFirmante);
				firmante.setFecha(new Date());
				firmante.setEstado(estadoAsignado);
				firmante.setSolicitante(usuarioCrea);
				firmante.setUsuarioCrea(usuarioCrea);
				firmante.setFechaCrea(new Date());
				// TICKET 9000003908
				firmante.setNroFlujo(nroFlujo);

				if (this.notificacionService.notificarAsignacionParaFirmar(correspondencia, firmante, locale)) {
					// TICKET 9000003943
					/*boolean insertarRuta = false;
					boolean insertarRuta2 = false;
					RutaAprobacion raInsert = new RutaAprobacion();
					RutaAprobacion raInsertNuevo = new RutaAprobacion();
					RutaAprobacion raInsertDup = new RutaAprobacion();
					if(correspondencia.isRutaAprobacion()){
						List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(idCorrespondencia);
						Firmante lastFirmante = new Firmante();
						Long idLastFirmante = 0L;
						for(int i=0;i<firmantes.size();i++){
							Firmante faux = firmantes.get(i);
							if(faux.getId()>idLastFirmante){
								lastFirmante = faux;
								idLastFirmante = faux.getId();
							}
						}
						if(correspondencia.getEstado().getId() == Constante.CORRESPONDENCIA_POR_CORREGIR){
							List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondencia);
							if(lastFirmante.getMotivoRechazo() != null && lastFirmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_MAL_ASIGNADO){
								insertarRuta = true;
								RutaAprobacion ra;
								int orden = 1000;
								for(int i=0;i<aprobadores.size();i++){
									ra = aprobadores.get(i);
									if(ra.getFirmante()==null){
										if(ra.getOrden()<orden){
											orden = ra.getOrden();
										}
										ra.setOrden(ra.getOrden()+1);
										rutaAprobacionDAO.save(ra);
									}
								}
								raInsert.setCorrespondencia(correspondencia);
								raInsert.setUsuario(codFirmante.toLowerCase());
								raInsert.setUsuarioNombre(nombreFirmante);
								raInsert.setCodDependencia(codDependenciaFirmante);
								raInsert.setDependenciaNombre(dependenciaFirmante);
								raInsert.setOrden(orden);
								raInsert.setUsuarioCrea(usuarioCrea);
								raInsert.setFechaCrea(new Date());
							}
							if(lastFirmante.getMotivoRechazo() != null && lastFirmante.getMotivoRechazo().getId() == Constante.DOCUMENTO_REQUIERE_VISTOS_PREVIOS){
								insertarRuta2 = true;
								RutaAprobacion ra2;
								int ordenDup = 0;
								int ordenMin = 1000;
								for(int i=0;i<aprobadores.size();i++){
									ra2 = aprobadores.get(i);
									if(ra2.getFirmante()==null){
										if(ra2.getOrden()<ordenMin){
											ordenMin = ra2.getOrden();
											raInsertNuevo.setCorrespondencia(correspondencia);
											raInsertNuevo.setOrden(ra2.getOrden());
											raInsertNuevo.setCodDependencia(firmante.getCodDependenciaFirmante());
											raInsertNuevo.setDependenciaNombre(firmante.getDependenciaFirmante());
											raInsertNuevo.setUsuario(firmante.getCodFirmante().toLowerCase());
											raInsertNuevo.setUsuarioNombre(firmante.getNombreFirmante());
											raInsertNuevo.setEstado("PENDIENTE");
											raInsertNuevo.setTipoFirmante("2");
											raInsertNuevo.setNombreTipo("Participante");
											raInsertNuevo.setUsuarioCrea(usuarioCrea);
											raInsertNuevo.setFechaCrea(new Date());
										}
										ra2.setOrden(ra2.getOrden()+1);
										rutaAprobacionDAO.save(ra2);
									}else{
										if(ra2.getOrden()>ordenDup){
											ordenDup = ra2.getOrden();
											raInsertDup.setCorrespondencia(correspondencia);
											raInsertDup.setCodDependencia(ra2.getCodDependencia());
											raInsertDup.setDependenciaNombre(ra2.getDependenciaNombre());
											raInsertDup.setUsuario(ra2.getUsuario().toLowerCase());
											raInsertDup.setUsuarioNombre(ra2.getUsuarioNombre());
											raInsertDup.setOrden(ra2.getOrden()+1);
											raInsertDup.setEstado(ra2.getEstado());
											raInsertDup.setTipoFirmante(ra2.getTipoFirmante());
											raInsertDup.setNombreTipo(ra2.getNombreTipo());
											raInsertDup.setUsuarioCrea(usuarioCrea);
											raInsertDup.setFechaCrea(new Date());
											raInsertDup = ra2;
											raInsertDup.setOrden(ra2.getOrden() + 1);
										}
									}
								}
							}
						}
						
						
					}*/
					// FIN TICKET
					correspondencia.setEstado(estadoAsignado);
					correspondencia.setUsuarioModifica(usuarioCrea);
					correspondencia.setFechaModifica(new Date());
					correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
					if (correspondencia == null) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
					}
					firmante = (Firmante) this.firmanteDAO.save(firmante);
					if (firmante == null) {
						throw new Exception(
								this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
					}
					// TICKET 9000003943
					//if(insertarRuta){
						//raInsert.setFirmante(firmante);
						//rutaAprobacionDAO.save(raInsert);
					//}
					//if(insertarRuta2){
						//raInsertNuevo.setFirmante(firmante);
						//rutaAprobacionDAO.save(raInsertNuevo);
						//raInsertDup.setFirmante(null);
						//rutaAprobacionDAO.save(raInsertDup);
					//}
					// FIN TICKET
					respuesta.estado = true;
					respuesta.mensaje = this.messageSource.getMessage("sistcorr.asignar_firma.exito", null, locale);
					respuesta.datos.add(firmante);
				} else {
					throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
				}
			} else {
				throw new Exception(this.messageSource.getMessage("sistcorr.asignar_firma.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] agregarFirmante", e);
		}
		this.LOGGER.info("[FIN] agregarFirmante");
		return respuesta;
	}

	public Respuesta<Firmante> obtenerFirmantes(Long idCorrespondencia, Locale locale) {
		this.LOGGER.info("[INICIO] obtenerFirmantes");
		Respuesta<Firmante> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.datos.addAll(this.firmanteDAO.obtenerFirmantes(idCorrespondencia));
		this.LOGGER.info("[FIN] obtenerFirmantes");
		return respuesta;
	}
	
	// TICKET 9000003997
	public Respuesta<DestinatarioRespuesta> obtenerDestinatarioRespuesta(Long idCorrespondencia, int nroEnvio, Locale locale) {
		this.LOGGER.info("[INICIO] obtenerDestinatarioRespuesta");
		Respuesta<DestinatarioRespuesta> respuesta = new Respuesta<>();
		respuesta.estado = true;
		respuesta.datos.addAll(this.destinatarioRespuestaDAO.obtenerDestinatarioRespuesta(idCorrespondencia, nroEnvio));
		List<DestinatarioRespuesta> respuestas = respuesta.datos;
		if(respuestas!=null){
			for(int i=0;i<respuestas.size();i++){
				DestinatarioRespuesta obj = respuestas.get(i);
				LOGGER.info("Estado recepción:" + obj.getEstado());
				if(obj.getEstado().equals(Constante.DESTINO_RESPUESTA_ENVIADO)){
					obj.setColor("AMARILLO");
				}else if(obj.getEstado().equals(Constante.DESTINO_RESPUESTA_ACEPTADO)){
					obj.setColor("VERDE");
				}else if(obj.getEstado().equals(Constante.DESTINO_RESPUESTA_RECHAZADO)){
					obj.setColor("ROJO");
				}
				String nombreDependencia = "";
				List<ItemFilenet> dependencias = this.filenetDAO.listarDependenciasSuperiores(obj.getCodDependencia());
				for(int j=0;j<dependencias.size();j++){
					if(obj.getCodDependencia().equals(dependencias.get(j).getCodigo())){
						nombreDependencia = dependencias.get(j).getDescripcion();
					}
				}
				obj.setNombreDependencia(nombreDependencia);
				respuestas.set(i, obj);
			}
		}
		this.LOGGER.info("[FIN] obtenerDestinatarioRespuesta");
		return respuesta;
	}
	// FIN TICKET

	@Transactional
	public Respuesta<Firmante> rechazarFirma(Long idCorrespondencia, String usuarioModifica, Long motivoRechazo, String descripcionRechazo, String token,
			Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		Respuesta<Firmante> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
//			if (!correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA)) {
//				throw new Exception(this.messageSource.getMessage("sistcorr.rechazar_firma.error", null, locale));
//			}

			MotivoRechazo rechazo = (MotivoRechazo) this.motivoRechazoDAO.findOne(motivoRechazo);
			if (rechazo == null) {
				throw new Exception(this.messageSource.getMessage("sistcorr.rechazar_firma.error", null, locale));
			}

			List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
			Firmante firmaRechazada = firmantes.get(firmantes.size() - 1);
			//if (!firmaRechazada.getCodFirmante().equals(usuarioModifica)) { // TICKET 9000003908 - Rechazo de un responsable
			LOGGER.info("Usuario Modifica:" + usuarioModifica);
			LOGGER.info("Responsable de la correspondencia:" + correspondencia.getResponsable());
			if(correspondencia.getResponsable().equals(usuarioModifica)){
				LOGGER.info("true");
			}else{
				LOGGER.info("false");
			}
			// TICKET 9000003994 - POST
			/*if (!firmaRechazada.getCodFirmante().equals(usuarioModifica) && !correspondencia.getResponsable().equals(usuarioModifica)) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}*/
			boolean permiso = false;
			if(firmaRechazada.getCodFirmante().equals(usuarioModifica) || correspondencia.getResponsable().equals(usuarioModifica)){
				permiso = true;
			}
			if(permiso==false){
				List<ItemFilenet> dependencias = filenetDAO.obtenerDependenciasGestor(usuarioModifica);
				if(dependencias!=null){
					for(int i=0;i<dependencias.size();i++){
						ItemFilenet item = dependencias.get(i);
						if(firmaRechazada.getCodFirmante().toUpperCase().equals(item.getCodigoAux().toUpperCase()) || 
								correspondencia.getResponsable().toUpperCase().equals(item.getCodigoAux().toUpperCase())){
							permiso = true;
						}
					}
				}
			}
			if(permiso==false){
				
			   // TICKET 9000004409	
			   boolean existeReemplazo = false;
			for (UsuarioRemplazo user : usuarioRemplazo) {
							LOGGER.info("UsuarioRemplazo: "  + user.getUsername());
		                    if (correspondencia.getResponsable().equals(user.getUsername().toLowerCase())){
		                    	existeReemplazo = true;
		                    	break;
		                    }
						}
							if (permiso==false && !existeReemplazo) {
								throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
				}

				// FIN TICKET 9000004409
				
				//throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
				
			}
			// FIN TICKET
			// TICKET 9000003908
			firmaRechazada.setDescripcionMotivoRechazo(descripcionRechazo);
			firmaRechazada.setUsuarioModifica(usuarioModifica);
			// FIN TICKET
			if (this.notificacionService.notificarRechazoSolicutdFirma(correspondencia, firmaRechazada, motivoRechazo,
					locale)) {

				if (rechazo.isCancelarDocumento()) {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_ANULADA));
					// TICKET 9000004510
					List<ArchivoAdjunto> listaArchivos = correspondencia.getAdjuntos();
					for(ArchivoAdjunto aa : listaArchivos){
						if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
							/*UsuarioPetroperu usuario = obtenerUsuario();
							if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
								String urlCarpetaArchivos = "adjuntos";
								String nuevaUrl = apiUrlbase + urlCarpetaArchivos + "/" + aa.getNombreServidor();
								ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(usuario.getToken(), aa.getNombreServidor());
								FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
							}*/
							AsignarDocumento document = new AsignarDocumento();
							document.nombre = "Adjuntos";
					        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", aa.getNombreServidor());
					        document.propiedades.add(documentTitle);
					        Respuesta<RespuestaCargaAdjunto> rptaAA = sistcorrCliente.cargarArchivoServidor(token, document, new File(aa.getUbicacion()), aa.getNombre());
					        if(rptaAA.estado){
								aa.setIndicadorRemoto(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
								File temp = new File(aa.getUbicacion());
								temp.delete();
							}else{
								//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error_carga_archivos", null, locale));
								LOGGER.info("[ERROR] El archivo " + aa.getNombre() + "(" + aa.getNombreServidor() + ") no pudo subir correctamente al servidor - Rechazar correspondencia, motivo anular");
							}
						}
					}
					// FIN TICKET
				} else {
					// TICKET 9000003908 - Aumentando condicional if, el nuevo estado en caso cumplirla y aumentando el nro. flujo
					if(rechazo.isAumentarFlujo()){
						correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_REINICIO));
						correspondencia.setNroFlujo(correspondencia.getNroFlujo() + 1);
						// TICKET 9000003943
						List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondencia);
						LOGGER.info("Correspondencia:" + correspondencia.getId());
						LOGGER.info("Ruta Aprobacion:" + aprobadores.size());
						for(int i=0;i<aprobadores.size();i++){
							RutaAprobacion ra = aprobadores.get(i);
							// TICKET 9000003943 - Validar que ocurre con los caso que hubo un rechazo por mal asignado o documentos previos.
							ra.setFirmante(null);
							ra.setUsuarioModifica(usuarioModifica);
							ra.setFechaModifica(new Date());
							LOGGER.info("Aprobador:" + ra.getId());
							ra.setUsuario(ra.getUsuario().toLowerCase());
							LOGGER.info("CorrespondenciaEmisionServiceImpl Linea 1570");
							rutaAprobacionDAO.save(ra);
						}
						// FIN TICKET
					}else{
						correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_POR_CORREGIR));
					}
					// FIN TICKET
				}
				
				//INICIO TICKET 9000004714
				if(motivoRechazo == Constante.DOCUMENTO_POR_CORREGIR)
					correspondencia.setMotivoRechazo(rechazo);
				//FIN TICKET 9000004714
				correspondencia.setUsuarioModifica(usuarioModifica);
				correspondencia.setFechaModifica(new Date());
				correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
				if (correspondencia == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.rechazar_firma.error", null, locale));
				}

				if (rechazo.isCancelarDocumento()) {
					firmaRechazada.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_ANULADA));
				} else {
					firmaRechazada.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_POR_CORREGIR));
				}

				firmaRechazada.setUsuarioModifica(usuarioModifica);
				firmaRechazada.setFechaModifica(new Date());
				firmaRechazada.setFecha(new Date());
				firmaRechazada.setMotivoRechazo(rechazo);
				firmaRechazada.setDescripcionMotivoRechazo(descripcionRechazo);
				firmaRechazada.setCorrespondencia(correspondencia);
				firmaRechazada = (Firmante) this.firmanteDAO.save(firmaRechazada);
				if (firmaRechazada == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.rechazar_firma.error", null, locale));
				}
				respuesta.estado = true;
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.rechazar_firma.exito", null, locale);
				respuesta.datos.add(firmaRechazada);
			} else {
				throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] rechazarFirma", e);
		}
		this.LOGGER.info("[FIN] rechazarFirma");
		return respuesta;
	}

	@Transactional
	public Respuesta<Firmante> emitirFirma(Long idCorrespondencia, String usuarioModifica, Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		this.LOGGER.info("[INICIO] emitirFirma");
		Respuesta<Firmante> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			if (!correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ASIGNADA)) {
				throw new Exception(this.messageSource.getMessage("sistcorr.emitir_firma.error", null, locale));
			}
			List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(idCorrespondencia);
			Firmante firmante = firmantes.get(firmantes.size() - 1);
			LOGGER.info("Compara " + firmante.getCodFirmante() + " con " + usuarioModifica);

			UsuarioPetroperu usuario = obtenerUsuario();
			// TICKET 9000004409
			if (!firmante.getCodFirmante().toLowerCase().equals(usuarioModifica.toLowerCase())) {
				boolean existeReemplazo = false;
				for (UsuarioRemplazo user : usuarioRemplazo) {
					LOGGER.info("UsuarioRemplazo: "  + user.getUsername());
                    if (firmante.getCodFirmante().toLowerCase().equals(user.getUsername().toLowerCase())){
                    	firmante.setCodFirmante(usuario.getUsername());
                    	firmante.setNombreFirmante(usuario.getApellidos().toUpperCase() + " " + usuario.getNombres().toUpperCase() );
                    	existeReemplazo = true;
                    	firmante.setCodFirmanteReemplazado(user.getUsername());
                      	firmante.setNomFirmanteReemplazado(user.getNombres());
                      	break;
                    }
				}
				
				if (!firmante.getCodFirmante().toLowerCase().equals(usuarioModifica.toLowerCase()) && !existeReemplazo) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}
			}
			
			 /*if (!firmante.getCodFirmante().toLowerCase().equals(usuarioModifica.toLowerCase())) {
					throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
				}*/
			//TICKET 9000004409  Fin
			
			//if (this.notificacionService.notificarEmisionFirmaDigital(correspondencia, firmante, locale)) {

			// TICKET 9000003874
			boolean enviado = false;
			//if(correspondencia.getCodRemitente().equals(firmante.getCodFirmante())){
			/*ItemFilenet listaUsuario = this.filenetDAO.obtenerFirmante(correspondencia.getCodDependencia());
			if(listaUsuario!=null){
				correspondencia.setCodRemitente(listaUsuario.getCodigo());
				correspondencia.setRemitente(listaUsuario.getDescripcion());
			}*/
			if(correspondencia.getCodRemitente().equals(firmante.getCodFirmante()) || correspondencia.getCodRemitente().equals(firmante.getCodFirmanteReemplazado())){
			//if(correspondencia.getCodRemitente().equals(firmante.getCodFirmante())){
				enviado = this.notificacionService.notificarEmisionAprobacionFirmaDigital(correspondencia, firmante, locale);
				//inicio ticket 9000004765
				if(correspondencia.getCodTipoCorrespondencia() != null && correspondencia.getCodTipoCorrespondencia().trim().equalsIgnoreCase(Constante.TIPO_CORRESPONDENCIA_DOC_PAGAR_ID)) {
					enviado = this.notificacionService.notificarEmisionAprobacionFirmaDigitalTipoCorresponDocPagar(correspondencia, firmante, locale);
				}
				//fin ticket 9000004765
			}else{
				enviado = this.notificacionService.notificarEmisionFirmaDigital(correspondencia, firmante, locale);
			}
			//if (this.notificacionService.notificarEmisionFirmaDigital(correspondencia, firmante, locale)) {
			//if (enviado) { // SE COMENTA POR TICKET 7000003621
			// FIN TICKET
			if (correspondencia.getCodRemitente().equals(firmante.getCodFirmante()) || correspondencia.getCodRemitente().equals(firmante.getCodFirmanteReemplazado())) {	
			//if (correspondencia.getCodRemitente().equals(firmante.getCodFirmante())) {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_APROBADA)); // eliminacion 9-3874
					correspondencia.setUsuarioAprueba(usuarioModifica);
					correspondencia.setFechaAprueba(new Date());
					//correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_COMPLETADA));
				} else {
					correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_FIRMADA));
				}

				correspondencia.setUsuarioModifica(usuarioModifica);
				correspondencia.setFechaModifica(new Date());
				correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
				if (correspondencia == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.emitir_firma.error", null, locale));
				}else{
					if(correspondencia.getEstado().getId()==Constante.CORRESPONDENCIA_APROBADA){
						for(ArchivoAdjunto aa : correspondencia.getAdjuntos()){
							if(aa.isPrincipal()){
								ArchivoCompartido ac = archivoCompartidoDAO.findOneByArchivo(aa);
								if(ac!=null){
									ac.setCompartido(true);
									archivoCompartidoDAO.save(ac);
								}
							}
						}

						// TICKET 9000004510
						int destInt = correspondencia.getDetalleInterno() == null ? 0 : correspondencia.getDetalleInterno().size();
						int destExt = correspondencia.getDetalleExterno() == null ? 0 : correspondencia.getDetalleExterno().size();
						int totalDest = destInt + destExt;
						if(totalDest == 0){
							List<ArchivoAdjunto> listaArchivos = correspondencia.getAdjuntos();
							for(ArchivoAdjunto aa : listaArchivos){
								if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
									//UsuarioPetroperu usuario = obtenerUsuario(); //9000004409 se comento
									/*if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
										String urlCarpetaArchivos = "adjuntos";
										String nuevaUrl = apiUrlbase + urlCarpetaArchivos + "/" + aa.getNombreServidor();
										ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(usuario.getToken(), aa.getNombreServidor());
										FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
									}*/
									AsignarDocumento document = new AsignarDocumento();
									document.nombre = "Adjuntos";
							        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", aa.getNombreServidor());
							        document.propiedades.add(documentTitle);
							        Respuesta<RespuestaCargaAdjunto> rptaAA = sistcorrCliente.cargarArchivoServidor(usuario.getToken(), document, new File(aa.getUbicacion()), aa.getNombre());
							        if(rptaAA.estado){
										aa.setIndicadorRemoto(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
										File temp = new File(aa.getUbicacion());
										temp.delete();
									}else{
										//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error_carga_archivos", null, locale));
										LOGGER.info("[ERROR] El archivo " + aa.getNombre() + "(" + aa.getNombreServidor() + ") no pudo subir correctamente al servidor - Correspondencia Digital Aprobada.");
									}
								}
							}
							
						}
						// FIN TICKET
					}
				}
				if (correspondencia.getCodRemitente().equals(firmante.getCodFirmante()) || correspondencia.getCodRemitente().equals(firmante.getCodFirmanteReemplazado())) {
				//if (correspondencia.getCodRemitente().equals(firmante.getCodFirmante())) {
					firmante.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_COMPLETADA));
				} else {
					firmante.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_FIRMADA));
				}

				firmante.setUsuarioModifica(usuarioModifica);
				firmante.setFechaModifica(new Date());
				firmante.setFecha(new Date());
				firmante.setCorrespondencia(correspondencia);
				firmante = (Firmante) this.firmanteDAO.save(firmante);
				if (firmante == null) {
					throw new Exception(this.messageSource.getMessage("sistcorr.emitir_firma.error", null, locale));
				}
				respuesta.estado = true;
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.emitir_firma.exito", null, locale);
				respuesta.datos.add(firmante);
			// TICKET 7000003621
			//} else {
			//	throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			//}
			if(!enviado) {
				this.LOGGER.error("[ERROR] emitirFirmaNotificacion", this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			}
			// FIN TICKET
			// TICKET 9000004044
			/*int cantidad = correspondencia.getDetalleInterno().size() + correspondencia.getDetalleExterno().size();
			if(correspondencia.getEstado().getId() == Constante.CORRESPONDENCIA_APROBADA && cantidad == 0 && correspondencia.isEsDocumentoRespuesta() && correspondencia.getEstadoDocumentoRespuesta() == Constante.CORRESPONDENCIA_DOCUMENTO_RESPUESTA_ENLAZADO){
				UsuarioPetroperu usuario = obtenerUsuario();
				if(correspondencia.getIdAsignacion() != null && correspondencia.getIdAsignacion().compareTo(0L) > 0){
					CompletarCorrespondenciaParametro ccp = new CompletarCorrespondenciaParametro(correspondencia.getRespuesta(), correspondencia.getCodRemitente(), correspondencia.getCorrelativo().getCodigo());
					correspondenciaService.completarCorrespondencia(usuario.getToken(), Integer.valueOf(correspondencia.getIdAsignacion().toString()), ccp, locale);
				}else{
					correspondenciaService.cerrarCorrespondencia(usuario.getToken(), correspondencia.getCorrelativoEntrada(), correspondencia.getRespuesta(), correspondencia.getCodRemitente(), correspondencia.getCorrelativo().getCodigo(), locale);
				}
			}*/
			// FIN TICKET
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] emitirFirma", e);
		}
		this.LOGGER.info("[FIN] emitirFirma");
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Respuesta<Correspondencia> enviarCorrespondencia(Long idCorrespondencia, String usuarioModifica,
			String token, Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		this.LOGGER.info("[INICIO] enviarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
				
			
			// TICKET 9000004409
			if (!correspondencia.getResponsable().equals(usuarioModifica)
					&& !correspondencia.getCodRemitente().equals(usuarioModifica)) {
				
					boolean existeReemplazo = false;
					for (UsuarioRemplazo user : usuarioRemplazo) {
						LOGGER.info("UsuarioRemplazo: "  + user.getUsername());
	                    if (correspondencia.getResponsable().equals(user.getUsername().toLowerCase())){
	                    	existeReemplazo = true;
	                    	break;
	                    }
					}
						if (correspondencia.getResponsable().equals(usuarioModifica)
								&& !correspondencia.getCodRemitente().equals(usuarioModifica) && !existeReemplazo) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}
			}

			// FIN TICKET 9000004409
			
			/*if (!correspondencia.getResponsable().equals(usuarioModifica)
					&& !correspondencia.getCodRemitente().equals(usuarioModifica)) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}*/
			/* Eliminacion 9-3874
			if (correspondencia.getDetalleExterno().size() + correspondencia.getDetalleInterno().size() == 0) {
				throw new Exception(this.messageSource.getMessage("sistcorr.enviar_correspondencia.error_destinatarios",
						null, locale));
			}
			fin eleiminacion 9-3874*/
			
			// TICKET 9000003874
			//if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_COMPLETADA)) {
			//if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_APROBADA)) {
			if ((correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_COMPLETADA) && !correspondencia.isFirmaDigital()) || (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_APROBADA) && correspondencia.isFirmaDigital())) {
				if (correspondencia.getTipoEmision().getId().intValue() == 1) {
					CorrespondenciaInterna cInterna = new CorrespondenciaInterna();
					cInterna.tipoCE = "CEI";
					cInterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
					cInterna.confidencial = (correspondencia.isConfidencial() == true) ? "SI" : "NO";
					cInterna.urgente = (correspondencia.isUrgente() == true) ? "SI" : "NO";
					cInterna.fechaDocumento = dateFormat.format(correspondencia.getFechaDocumento());
					cInterna.numDocInterno = correspondencia.getCorrelativo().getCodigo();
					cInterna.idTipoCorrespondencia = Long
							.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));
					if (correspondencia.getTipoCorrespondencia().equals("OTROS")) {
						cInterna.setTipoCorrespondenciaOtro(correspondencia.getTipoCorrespondenciaOtros());
					} else {
						cInterna.tipoCorrespondenciaOtro = "";
					}
					cInterna.asunto = correspondencia.getAsunto();
					cInterna.observacion = correspondencia.getObservaciones();
					String despachoFisico = (correspondencia.isDespachoFisico() == true) ? "SI" : "NO";
					Boolean fDestInt = false; //adicion 9-3874
					for (DestinatarioInterno dest : correspondencia.getDetalleInterno()) {
						Destinatario destinatario = new Destinatario(
								Long.valueOf(Long.parseLong(dest.getCodDependencia())), despachoFisico);
						cInterna.destinatarios.add(destinatario);
						fDestInt = true ;
					}
					for (DestinatarioCopia copia : correspondencia.getDetalleCopia()) {
						ConCopia conCopia = new ConCopia(Long.valueOf(Long.parseLong(copia.getCodDependencia())),
								despachoFisico);
						cInterna.conCopia.add(conCopia);
					}

					/*Respuesta<Object> enviado = this.correspondenciaService.registrarCorrespondencia(token,
							correspondencia, cInterna, usuarioModifica, locale);
					if (!enviado.estado) {
						throw new Exception(enviado.mensaje);
					}
					List<RespuestaCargaAdjunto> adjuntos = (List<RespuestaCargaAdjunto>) enviado.datos.get(0);
					CorrespondenciaInternaRespuesta resCI = (CorrespondenciaInternaRespuesta) enviado.datos.get(1);
					for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
						((ArchivoAdjunto) correspondencia.getAdjuntos().get(i))
								.setFileNetID(((RespuestaCargaAdjunto) adjuntos.get(i)).getId());
					}
					for (int i = 0; i < correspondencia.getDetalleInterno().size(); i++) {
						((DestinatarioInterno) correspondencia.getDetalleInterno().get(i)).setFileNetCorrelativo(
								((DestinoRespuesta) resCI.getDestinos().get(i)).getCorrelativo());
					}
					correspondencia.setFileNetCorrelativo(resCI.getCorrelativoCE());
					if (!this.notificacionService.notificarEnvioCorrespondencia(correspondencia, locale)) {
						throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
					}*/
					//edicion 9-3874
					if (fDestInt) {
						if("S".equals(envioMensaje)){
							Respuesta<Object> enviado = this.correspondenciaService.registrarCorrespondencia(token,
									correspondencia, cInterna, usuarioModifica, locale);
							if (!enviado.estado) {
								throw new Exception(enviado.mensaje);
							}
							List<RespuestaCargaAdjunto> adjuntos = (List<RespuestaCargaAdjunto>) enviado.datos.get(0);
							CorrespondenciaInternaRespuesta resCI = (CorrespondenciaInternaRespuesta) enviado.datos.get(1);	
							
							for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
								((ArchivoAdjunto) correspondencia.getAdjuntos().get(i))
										.setFileNetID(((RespuestaCargaAdjunto) adjuntos.get(i)).getId());
							}
							for (int i = 0; i < correspondencia.getDetalleInterno().size(); i++) {
								((DestinatarioInterno) correspondencia.getDetalleInterno().get(i)).setFileNetCorrelativo(
										((DestinoRespuesta) resCI.getDestinos().get(i)).getCorrelativo());
							}
							correspondencia.setFileNetCorrelativo(resCI.getCorrelativoCE());
							// TICKET 9000003997
							UsuarioPetroperu usuario = obtenerUsuario();
							List<DestinoRespuesta> destinosRespuesta = resCI.getDestinos();
							Integer nroEnvio = correspondencia.getNroEnvio();
							if(nroEnvio.equals(null)){
								nroEnvio = 0;
							}
							nroEnvio = nroEnvio + 1;
							correspondencia.setNroEnvio(nroEnvio);
							for(int i=0;i<destinosRespuesta.size();i++){
								DestinoRespuesta destinoRespuesta = destinosRespuesta.get(i);
								DestinatarioRespuesta dest = new DestinatarioRespuesta();
								dest.setCodDependencia(destinoRespuesta.getIdDependencia().toString());
								dest.setCorrelativo(destinoRespuesta.getCorrelativo());
								dest.setCorrespondencia(correspondencia);
								dest.setNroEnvio(nroEnvio);
								dest.setEstado(Constante.DESTINO_RESPUESTA_ENVIADO);
								dest.setFechaCrea(new Date());
								dest.setUsuarioCrea(usuario.getUsername());
								destinatarioRespuestaDAO.save(dest);
							}
							// FIN TICKET
							if (!this.notificacionService.notificarEnvioCorrespondencia(correspondencia, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}						
						}
					// TICKET 9000004510
					//}
					}else{
						List<ArchivoAdjunto> listaArchivos = correspondencia.getAdjuntos();
						LOGGER.info("Archivos encontrados:" + listaArchivos.size());
						for(ArchivoAdjunto aa : listaArchivos){
							if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
								LOGGER.info("Archivo:" + aa.getNombre() + "-" + aa.getNombreServidor());
								/*if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
									String urlCarpetaArchivos = "adjuntos";
									String nuevaUrl = apiUrlbase + urlCarpetaArchivos + "/" + aa.getNombreServidor();
									ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(token, aa.getNombreServidor());
									FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
								}*/
								AsignarDocumento document = new AsignarDocumento();
								document.nombre = "Adjuntos";
						        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", aa.getNombreServidor());
						        document.propiedades.add(documentTitle);
						        Respuesta<RespuestaCargaAdjunto> rptaAA = sistcorrCliente.cargarArchivoServidor(token, document, new File(aa.getUbicacion()), aa.getNombre());
						        LOGGER.info("Estado:" + rptaAA.estado);
								if(rptaAA.estado){
									aa.setIndicadorRemoto(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
									File temp = new File(aa.getUbicacion());
									temp.delete();
									//aa = archivoAdjuntoDAO.save(aa);
									LOGGER.info("Actualización de indicador remoto y eliminación de archivo");
								}else{
									//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error_carga_archivos", null, locale));
									LOGGER.info("[ERROR] El archivo " + aa.getNombre() + "(" + aa.getNombreServidor() + ") no pudo subir correctamente al servidor - Correspondencia Manual Interna - SD.");
								}
							}
						}
					}
					// FIN TICKET
					//fin edicion 9-3874
				} else if (correspondencia.getTipoEmision().getId().intValue() == 2) {
					//CorrespondenciaExterna cExterna = new CorrespondenciaExterna();//comentado por ticket 9000003934
					//cExterna.tipoCE = "CEE"; //comentado por ticket 9000003934
					//adicion 9-3874
					/*Boolean fDestExt = false; 
					for (DestinatarioExterno dest : correspondencia.getDetalleExterno()) {
							fDestExt = true ;
					}*/ //comentado por ticket 9000003934
					//fin adicion 9-3874					
					
					//if ( fDestExt && ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).isNacional()) {  // edicion 9-3874 //comentado por ticket 9000003934
						/*LOGGER.info("Dependencia FileNet: " + correspondencia.getDetalleExterno().get(0).getDependenciaNacional());
						cExterna.entidadExterna = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDependenciaNacional();
						cExterna.idProvincia = Long.valueOf(Long.parseLong(
								((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodProvincia()));
						cExterna.idDistrito = Long.valueOf(Long.parseLong(
								((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodDistrito()));
						cExterna.idDepartamento = Long.valueOf(
								Long.parseLong(((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
										.getCodDepartamento()));*/ //comentado por ticket 9000003934
					//} else {// adicion 9-3874
						/*cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getNombreDestinatario();
							cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDireccion();
							cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
							cExterna.idTipoCorrespondencia = Long
								.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));*///comentado por ticket 9000003934
							// fin adicion 9-3874
					//} else if(fDestExt) { // edicion 9-3874 //comentado por ticket 9000003934

						/*cExterna.entidadExterna = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDependenciaInternacional();
						cExterna.ciudad = "";
						cExterna.idPais = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getCodPais();*/ //comentado por ticket 9000003934
						// } --> * 	// eliminacion 9-3874
						/*cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getNombreDestinatario();
						cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDireccion();
						cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
						cExterna.idTipoCorrespondencia = Long
							.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));*/
						/*cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
							.getNombreDestinatario();
						cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
							.getDireccion();
						cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
						cExterna.idTipoCorrespondencia = Long
							.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));*///comentado por ticket 9000003934
					//} //*
					/*if (correspondencia.getTipoCorrespondencia().equals("OTROS")) {
						cExterna.tipoCorrespondenciaOtro = correspondencia.getTipoCorrespondenciaOtros();
					} else {
						cExterna.tipoCorrespondenciaOtro = "";
					}
					cExterna.fechaDocumento = dateFormat.format(correspondencia.getFechaDocumento());
					cExterna.numDocInterno = correspondencia.getCorrelativo().getCodigo();
					// TICKET 900003874
					//cExterna.retornoCargo = "NO";
					cExterna.retornoCargo = "SI";
					cExterna.confidencial = (correspondencia.isConfidencial() == true) ? "SI" : "NO";
					cExterna.urgente = (correspondencia.isUrgente() == true) ? "SI" : "NO";
					cExterna.asunto = correspondencia.getAsunto();
					String despachoFisico = (correspondencia.isDespachoFisico() == true) ? "SI" : "NO";
					cExterna.observacion = correspondencia.getObservaciones();
					for (DestinatarioCopia copia : correspondencia.getDetalleCopia()) {
						ConCopia cCopia = new ConCopia(Long.valueOf(Long.parseLong(copia.getCodDependencia())),
								despachoFisico);
						cExterna.conCopia.add(cCopia);
					}*/ //comentado por ticket 9000003934

					//edicion 9-3874
					//if (fDestExt){
						//if("S".equals(envioMensaje)){//comentado por ticket 9000003934
							/*Respuesta<Object> enviado = this.correspondenciaService.registrarCorrespondencia(token,
									correspondencia, cExterna, usuarioModifica, locale);
							if (!enviado.estado) {
								throw new Exception(enviado.mensaje);
							}
							List<RespuestaCargaAdjunto> adjuntos = (List<RespuestaCargaAdjunto>) enviado.datos.get(0);
							CorrespondenciaExternaRespuesta resCE = (CorrespondenciaExternaRespuesta) enviado.datos.get(1);
							for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
								((ArchivoAdjunto) correspondencia.getAdjuntos().get(i))
										.setFileNetID(((RespuestaCargaAdjunto) adjuntos.get(i)).getId());
							}
		
							correspondencia.setFileNetCorrelativo(resCE.getCorrelativoCE());
							// TICKET 9000003791
							UsuarioPetroperu usuario = obtenerUsuario();*/ //comentado por ticket 9000003934
							// FIN TICKET 9000003791
							//if (!this.notificacionService.notificarCorrespondenciaExterna(correspondencia, locale)) { // SE COMENTA POR TICKET 9000003791
							// COMENTADO POR TICKET 9000003874
							/*if (!this.notificacionService.notificarCorrespondenciaExterna(correspondencia, usuario, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}*/
							// TICKET 9000003997
							/*List<DestinoRespuesta> destinosRespuesta = resCE.getDestinos();
							Integer nroEnvio = correspondencia.getNroEnvio();
							if(nroEnvio.equals(null)){
								nroEnvio = 0;
							}
							nroEnvio = nroEnvio + 1;
							correspondencia.setNroEnvio(nroEnvio);
							for(int i=0;i<destinosRespuesta.size();i++){
								DestinoRespuesta destinoRespuesta = destinosRespuesta.get(i);
								DestinatarioRespuesta dest = new DestinatarioRespuesta();
								dest.setCodDependencia(destinoRespuesta.getIdDependencia().toString());
								dest.setCorrelativo(destinoRespuesta.getCorrelativo());
								dest.setCorrespondencia(correspondencia);
								dest.setNroEnvio(nroEnvio);
								dest.setEstado(Constante.DESTINO_RESPUESTA_ENVIADO);
								dest.setFechaCrea(new Date());
								dest.setUsuarioCrea(usuario.getUsername());
								destinatarioRespuestaDAO.save(dest);
							}
							// FIN TICKET
							// SE COMENTA POR TICKET 9*3940
							//if (!this.notificacionService.notificarEnvioCorrespondencia(correspondencia, locale)) {
							if (!this.notificacionService.notificarEnvioCorrespondenciaExterno(correspondencia, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}*/ //comentado por ticket 9000003934
						//}//comentado por ticket 9000003934
					//} //comentado por ticket 9000003934
					
					
					
					//adicion 9-3874
					Boolean fDestExt = false; 
					for (DestinatarioExterno dest : correspondencia.getDetalleExterno()) {
							fDestExt = true ;
					}
					//fin adicion 9-3874
					
					/* INICIO TICKET 9000003934 */
					if(fDestExt) {
						List<CorrespondenciaExterna> listaCorrespExternasEnviar = new ArrayList<CorrespondenciaExterna>();
						String nombreEntidadDestinatarioExterno = null, direccion = null, idPais = null, nombreDestinatario = null, despachoFisico = null;
						Long idProvincia = 0L, idDistrito = 0L, idDepartamento = 0L;
						despachoFisico = (correspondencia.isDespachoFisico() == true) ? "SI" : "NO";
						
						for (DestinatarioExterno destExterno : correspondencia.getDetalleExterno()) {
							
							nombreEntidadDestinatarioExterno = null; direccion = null; idPais = null; nombreDestinatario = null;
							idProvincia = 0L; idDistrito = 0L; idDepartamento = 0L;
							CorrespondenciaExterna cExterna = new CorrespondenciaExterna();
							
							cExterna.tipoCE = "CEE";
							if(destExterno.isNacional() && (destExterno.getTipoDestinatario() != null && destExterno.getTipoDestinatario().toString().equalsIgnoreCase("X"))) 
								nombreEntidadDestinatarioExterno = destExterno.getDependenciaNacional();
							else if((destExterno.isNacional() || !destExterno.isNacional()) && (destExterno.getTipoDestinatario() != null && destExterno.getTipoDestinatario().toString().equalsIgnoreCase("P")))
								nombreEntidadDestinatarioExterno = "Persona Natural";//nombreEntidadDestinatarioExterno = destExterno.getNombreDestinatario();
							else if(!destExterno.isNacional() && (destExterno.getTipoDestinatario() != null && destExterno.getTipoDestinatario().toString().equalsIgnoreCase("P")))
								nombreEntidadDestinatarioExterno = destExterno.getDependenciaInternacional();
							else if(destExterno.isNacional())
								nombreEntidadDestinatarioExterno = destExterno.getDependenciaNacional();
							else nombreEntidadDestinatarioExterno = destExterno.getDependenciaInternacional();
							
							if(destExterno.getCodDepartamento() != null && !destExterno.getCodDepartamento().trim().equalsIgnoreCase(""))
								idDepartamento = Long.valueOf(Long.parseLong(destExterno.getCodDepartamento()));
							if(destExterno.getCodProvincia() != null && !destExterno.getCodProvincia().trim().equalsIgnoreCase(""))
								idProvincia = Long.valueOf(Long.parseLong(destExterno.getCodProvincia()));
							if(destExterno.getCodDistrito() != null && !destExterno.getCodDistrito().trim().equalsIgnoreCase(""))
								idDistrito = Long.valueOf(Long.parseLong(destExterno.getCodDistrito()));
							
							if(destExterno.getDireccion() != null && !destExterno.getDireccion().trim().equalsIgnoreCase(""))
								direccion = destExterno.getDireccion().trim();
							
							if(destExterno.getCodPais() != null && !destExterno.getCodPais().trim().equalsIgnoreCase(""))
								idPais = destExterno.getCodPais().trim();
							
							if(destExterno.getNombreDestinatario() != null && !destExterno.getNombreDestinatario().trim().equalsIgnoreCase(""))
								nombreDestinatario = destExterno.getNombreDestinatario().trim();
							
							cExterna.ciudad = "";
							cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
							cExterna.idTipoCorrespondencia = Long.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));
							cExterna.entidadExterna = nombreEntidadDestinatarioExterno;
							cExterna.idDepartamento = idDepartamento;
							cExterna.idProvincia = idProvincia;
							cExterna.idDistrito = idDistrito;
							cExterna.direccion = direccion;
							cExterna.personaDest = nombreDestinatario;
							cExterna.idPais = idPais;
							
							if (correspondencia.getTipoCorrespondencia().equals("OTROS")) cExterna.tipoCorrespondenciaOtro = correspondencia.getTipoCorrespondenciaOtros();
							else cExterna.tipoCorrespondenciaOtro = "";
							
							cExterna.fechaDocumento = dateFormat.format(correspondencia.getFechaDocumento());
							cExterna.numDocInterno = correspondencia.getCorrelativo().getCodigo();
							cExterna.retornoCargo = "SI";
							cExterna.confidencial = (correspondencia.isConfidencial() == true) ? "SI" : "NO";
							cExterna.urgente = (correspondencia.isUrgente() == true) ? "SI" : "NO";
							cExterna.asunto = correspondencia.getAsunto();
							
							cExterna.observacion = correspondencia.getObservaciones();
							
							for (DestinatarioCopia copia : correspondencia.getDetalleCopia()) {
								ConCopia cCopia = new ConCopia(Long.valueOf(Long.parseLong(copia.getCodDependencia())),
										despachoFisico);
								cExterna.conCopia.add(cCopia);
							}
							
							if(destExterno.isEsDespachoFisicoDestExterno() != null && destExterno.isEsCorreoElectronicoDestExterno() != null 
									&& destExterno.isEsDespachoFisicoDestExterno() && destExterno.isEsCorreoElectronicoDestExterno()) {
								cExterna.despachoFisico = "SI";
								CorrespondenciaExterna cExternaDespaFisicoParaEnviar = new CorrespondenciaExterna();
								BeanUtils.copyProperties(cExterna, cExternaDespaFisicoParaEnviar);
								Destinatario destinatarioDespaFisicoParaEnviar = new Destinatario(destExterno.getId(), "SI");
								destinatarioDespaFisicoParaEnviar.esEnviarPorCorreoElectronico = "N";
								destinatarioDespaFisicoParaEnviar.correoDestinatario = destExterno.getCorreoDestinatario();
								
								cExternaDespaFisicoParaEnviar.destinatarios = null;
								cExternaDespaFisicoParaEnviar.destinatarios = new ArrayList<Destinatario>(Arrays.asList(destinatarioDespaFisicoParaEnviar));
								listaCorrespExternasEnviar.add(cExternaDespaFisicoParaEnviar);
								
								cExterna.despachoFisico = "NO";
								CorrespondenciaExterna cExternaParaEnviarEmail = new CorrespondenciaExterna();
								BeanUtils.copyProperties(cExterna, cExternaParaEnviarEmail);
								Destinatario destinatarioParaEnviarEmail = new Destinatario(destExterno.getId(), "NO");
								destinatarioParaEnviarEmail.esEnviarPorCorreoElectronico = "S";
								destinatarioParaEnviarEmail.correoDestinatario = destExterno.getCorreoDestinatario();
								
								cExternaParaEnviarEmail.destinatarios = null;
								cExternaParaEnviarEmail.destinatarios = new ArrayList<Destinatario>(Arrays.asList(destinatarioParaEnviarEmail));
								listaCorrespExternasEnviar.add(cExternaParaEnviarEmail);
							}else {
								
								Destinatario destinatarioDespaFisicoParaEnviar = new Destinatario();
								destinatarioDespaFisicoParaEnviar.idDependencia = destExterno.getId();
								destinatarioDespaFisicoParaEnviar.esEnviarPorCorreoElectronico = ((destExterno.isEsCorreoElectronicoDestExterno() != null && destExterno.isEsCorreoElectronicoDestExterno())?("S"):("N"));
								destinatarioDespaFisicoParaEnviar.correoDestinatario = destExterno.getCorreoDestinatario();
								
								if(destExterno.isEsDespachoFisicoDestExterno() != null && destExterno.isEsCorreoElectronicoDestExterno() != null 
										&& destExterno.isEsDespachoFisicoDestExterno() && !destExterno.isEsCorreoElectronicoDestExterno()) {
									cExterna.despachoFisico = "SI";
									destinatarioDespaFisicoParaEnviar.despachoFisico = "SI";
								}
								else if(destExterno.isEsDespachoFisicoDestExterno() != null && destExterno.isEsCorreoElectronicoDestExterno() != null 
										&& !destExterno.isEsDespachoFisicoDestExterno() && destExterno.isEsCorreoElectronicoDestExterno()) {
									cExterna.despachoFisico = "NO";
									destinatarioDespaFisicoParaEnviar.despachoFisico = "NO";
								}else {
									cExterna.despachoFisico = "";
									destinatarioDespaFisicoParaEnviar.despachoFisico = "";
								}
								
								cExterna.destinatarios = new ArrayList<Destinatario>(Arrays.asList(destinatarioDespaFisicoParaEnviar));
								
								listaCorrespExternasEnviar.add(cExterna);
							}
						}
						
						if("S".equals(envioMensaje) && listaCorrespExternasEnviar.size() > 0){
							
							List<DestinatarioExterno> listDestExterOnlyCheckCorreoElectronico = correspondencia.getDetalleExterno().stream().filter(dx -> { return ((dx.isEsDespachoFisicoDestExterno() != null && dx.isEsCorreoElectronicoDestExterno() != null) && !dx.isEsDespachoFisicoDestExterno() && dx.isEsCorreoElectronicoDestExterno());}).collect(Collectors.toList());
							UsuarioPetroperu usuario = obtenerUsuario();
							for(CorrespondenciaExterna cExternaEnviar : listaCorrespExternasEnviar) {
								 
								/*this.LOGGER.info("Asunto: " + cExternaEnviar.getAsunto());
								this.LOGGER.info("Ciudad: " + cExternaEnviar.getCiudad());
								this.LOGGER.info("Confidencial: " + cExternaEnviar.getConfidencial());
								this.LOGGER.info("Direccion: " + cExternaEnviar.getDireccion());
								this.LOGGER.info("EntidadExterna: " + cExternaEnviar.getEntidadExterna());
								this.LOGGER.info("FechaDocumento: " + cExternaEnviar.getFechaDocumento());
								this.LOGGER.info("IdPais: " + cExternaEnviar.getIdPais());
								this.LOGGER.info("NumDocInterno: " + cExternaEnviar.getNumDocInterno());
								this.LOGGER.info("Observacion: " + cExternaEnviar.getObservacion());
								this.LOGGER.info("PersonaDest: " + cExternaEnviar.getPersonaDest());
								this.LOGGER.info("RetornoCargo: " + cExternaEnviar.getRetornoCargo());
								this.LOGGER.info("TipoCE: " + cExternaEnviar.getTipoCE());
								this.LOGGER.info("TipoCorrespondenciaOtro: " + cExternaEnviar.getTipoCorrespondenciaOtro());
								this.LOGGER.info("Urgente: " + cExternaEnviar.getUrgente());
								this.LOGGER.info("IdDepartamento: " + cExternaEnviar.getIdDepartamento());
								this.LOGGER.info("IdDependenciaRemitente: " + cExternaEnviar.getIdDependenciaRemitente());
								this.LOGGER.info("IdDistrito: " + cExternaEnviar.getIdDistrito());
								this.LOGGER.info("IdProvincia: " + cExternaEnviar.getIdProvincia());
								this.LOGGER.info("IdTipoCorrespondencia: " + cExternaEnviar.getIdTipoCorrespondencia());
								this.LOGGER.info("DespachoFisico: " + cExternaEnviar.getDespachoFisico());
								
								this.LOGGER.info("Destinatario -> correoDestinatario: " + cExternaEnviar.getDestinatarios().get(0).correoDestinatario);
								this.LOGGER.info("Destinatario -> despachoFisico: " + cExternaEnviar.getDestinatarios().get(0).despachoFisico);
								this.LOGGER.info("Destinatario -> esEnviarPorCorreoElectronico: " + cExternaEnviar.getDestinatarios().get(0).esEnviarPorCorreoElectronico);
								this.LOGGER.info("Destinatario -> idDependencia: " + cExternaEnviar.getDestinatarios().get(0).idDependencia);
								
								this.LOGGER.info("*****************************************************************************************");*/
								Respuesta<Object> enviado = this.correspondenciaService.registrarCorrespondencia(token,
										correspondencia, cExternaEnviar, usuarioModifica, locale);
								if (!enviado.estado)
									throw new Exception(enviado.mensaje);
								List<RespuestaCargaAdjunto> adjuntos = (List<RespuestaCargaAdjunto>) enviado.datos.get(0);
								CorrespondenciaExternaRespuesta resCE = (CorrespondenciaExternaRespuesta) enviado.datos.get(1);
								for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
									((ArchivoAdjunto) correspondencia.getAdjuntos().get(i))
											.setFileNetID(((RespuestaCargaAdjunto) adjuntos.get(i)).getId());
								}
								DestinatarioExterno destinatarioExternoBD = destinatarioExternoDAO.findOne(cExternaEnviar.getDestinatarios().get(0).idDependencia);
								if(destinatarioExternoBD.getFileNetCorrelativo() != null && !destinatarioExternoBD.getFileNetCorrelativo().trim().equalsIgnoreCase("")) {
									destinatarioExternoBD.setFileNetCorrelativo(destinatarioExternoBD.getFileNetCorrelativo() + " / " + resCE.getCorrelativoCE());
								}else {
									destinatarioExternoBD.setFileNetCorrelativo(resCE.getCorrelativoCE());
								}
								
								// TICKET 9000003997
								List<DestinoRespuesta> destinosRespuesta = resCE.getDestinos();
								Integer nroEnvio = correspondencia.getNroEnvio();
								if(nroEnvio.equals(null)){
									nroEnvio = 0;
								}
								nroEnvio = nroEnvio + 1;
								correspondencia.setNroEnvio(nroEnvio);
								for(int i=0;i<destinosRespuesta.size();i++){
									DestinoRespuesta destinoRespuesta = destinosRespuesta.get(i);
									DestinatarioRespuesta dest = new DestinatarioRespuesta();
									dest.setCodDependencia(destinoRespuesta.getIdDependencia().toString());
									dest.setCorrelativo(destinoRespuesta.getCorrelativo());
									dest.setCorrespondencia(correspondencia);
									dest.setNroEnvio(nroEnvio);
									dest.setEstado(Constante.DESTINO_RESPUESTA_ENVIADO);
									dest.setFechaCrea(new Date());
									dest.setUsuarioCrea(usuario.getUsername());
									destinatarioRespuestaDAO.save(dest);
								}
								// FIN TICKET
								
								destinatarioExternoBD.setUsuarioModifica(usuarioModifica);
								destinatarioExternoBD.setFechaModifica(new Date());
								
								if(correspondencia.isFirmaDigital() && cExternaEnviar.getDestinatarios().get(0).esEnviarPorCorreoElectronico.equalsIgnoreCase("S")) {
									if (!this.notificacionService.notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico(correspondencia, cExternaEnviar.personaDest, cExternaEnviar.getDestinatarios().get(0).correoDestinatario, usuarioModifica, locale)) {
										//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
									}
									// TICKET 70000004052
									List<ArchivoAdjunto> archivosPorBorrar = correspondencia.getAdjuntos();
									for(ArchivoAdjunto aab : archivosPorBorrar){
										if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aab.getIndicadorRemoto())){
											String ubicacion = aab.getUbicacion();
											File archivoFisico = new File(ubicacion);
											if(archivoFisico.exists()){
												archivoFisico.delete();
											}
										}
									}
									// FIN TICKET
								}
								
								destinatarioExternoDAO.save(destinatarioExternoBD);
								//correspondencia.setFileNetCorrelativo(resCE.getCorrelativoCE());
							}
							
							if (!(listDestExterOnlyCheckCorreoElectronico.size() == correspondencia.getDetalleExterno().size()) && !this.notificacionService.notificarEnvioCorrespondenciaExterno(correspondencia, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}
						}
					// TICKET 9000004510
					//}
					}else{
						List<ArchivoAdjunto> listaArchivos = correspondencia.getAdjuntos();
						LOGGER.info("Archivos encontrados:" + listaArchivos.size());
						for(ArchivoAdjunto aa : listaArchivos){
							if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
								LOGGER.info("Archivo:" + aa.getNombre() + "-" + aa.getNombreServidor());
								/*if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
									String urlCarpetaArchivos = "adjuntos";
									String nuevaUrl = apiUrlbase + urlCarpetaArchivos + "/" + aa.getNombreServidor();
									ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(token, aa.getNombreServidor());
									FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
								}*/
								AsignarDocumento document = new AsignarDocumento();
								document.nombre = "Adjuntos";
						        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", aa.getNombreServidor());
						        document.propiedades.add(documentTitle);
						        Respuesta<RespuestaCargaAdjunto> rptaAA = sistcorrCliente.cargarArchivoServidor(token, document, new File(aa.getUbicacion()), aa.getNombre());
						        LOGGER.info("Estado:" + rptaAA.estado);
								if(rptaAA.estado){
									aa.setIndicadorRemoto(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
									File temp = new File(aa.getUbicacion());
									temp.delete();
									//aa = archivoAdjuntoDAO.save(aa);
									LOGGER.info("Actualización de indicador remoto y eliminación de archivo");
								}else{
									//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error_carga_archivos", null, locale));
									LOGGER.info("[ERROR] El archivo " + aa.getNombre() + "(" + aa.getNombreServidor() + ") no pudo subir correctamente al servidor - Correspondencia Manual Externa - SD.");
								}
							}
						}
					}
					// FIN TICKET
					/* FIN TICKET 9000003934 */
					
					/* SE COMENTO POR EL TICKET 9000003934 */
					/*if ( fDestExt && ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).isNacional()) {  // edicion 9-3874
						LOGGER.info("Dependencia FileNet: " + correspondencia.getDetalleExterno().get(0).getDependenciaNacional());
						cExterna.entidadExterna = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDependenciaNacional();
						cExterna.idProvincia = Long.valueOf(Long.parseLong(
								((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodProvincia()));
						cExterna.idDistrito = Long.valueOf(Long.parseLong(
								((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodDistrito()));
						cExterna.idDepartamento = Long.valueOf(
								Long.parseLong(((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
										.getCodDepartamento()));
					//} else {// adicion 9-3874
						cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getNombreDestinatario();
							cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDireccion();
							cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
							cExterna.idTipoCorrespondencia = Long
								.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));	
							// fin adicion 9-3874
					} else */ /*if(fDestExt) {*/ // edicion 9-3874

						/*cExterna.entidadExterna = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDependenciaInternacional();
						cExterna.ciudad = "";
						cExterna.idPais = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getCodPais();*/
						// } --> * 	// eliminacion 9-3874
						/*cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getNombreDestinatario();
						cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
								.getDireccion();
						cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
						cExterna.idTipoCorrespondencia = Long
							.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));*/
						/*cExterna.personaDest = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
							.getNombreDestinatario();
						cExterna.direccion = ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0))
							.getDireccion();
						cExterna.idDependenciaRemitente = Long.valueOf(Long.parseLong(correspondencia.getCodDependencia()));
						cExterna.idTipoCorrespondencia = Long
							.valueOf(Long.parseLong(correspondencia.getCodTipoCorrespondencia()));*/
					//} //*
					/*cExterna.destinatarios= null;
					if (correspondencia.getTipoCorrespondencia().equals("OTROS")) {
						cExterna.tipoCorrespondenciaOtro = correspondencia.getTipoCorrespondenciaOtros();
					} else {
						cExterna.tipoCorrespondenciaOtro = "";
					}
					cExterna.fechaDocumento = dateFormat.format(correspondencia.getFechaDocumento());
					cExterna.numDocInterno = correspondencia.getCorrelativo().getCodigo();
					// TICKET 900003874
					//cExterna.retornoCargo = "NO";
					cExterna.retornoCargo = "SI";
					cExterna.confidencial = (correspondencia.isConfidencial() == true) ? "SI" : "NO";
					cExterna.urgente = (correspondencia.isUrgente() == true) ? "SI" : "NO";
					cExterna.asunto = correspondencia.getAsunto();
					String despachoFisico = (correspondencia.isDespachoFisico() == true) ? "SI" : "NO";
					cExterna.observacion = correspondencia.getObservaciones();
					for (DestinatarioCopia copia : correspondencia.getDetalleCopia()) {
						ConCopia cCopia = new ConCopia(Long.valueOf(Long.parseLong(copia.getCodDependencia())),
								despachoFisico);
						cExterna.conCopia.add(cCopia);
					}*/

					//edicion 9-3874
					//if (fDestExt){
						//if("S".equals(envioMensaje)){
							/*Respuesta<Object> enviado = this.correspondenciaService.registrarCorrespondencia(token,
									correspondencia, cExterna, usuarioModifica, locale);
							if (!enviado.estado) {
								throw new Exception(enviado.mensaje);
							}
							List<RespuestaCargaAdjunto> adjuntos = (List<RespuestaCargaAdjunto>) enviado.datos.get(0);
							CorrespondenciaExternaRespuesta resCE = (CorrespondenciaExternaRespuesta) enviado.datos.get(1);
							for (int i = 0; i < correspondencia.getAdjuntos().size(); i++) {
								((ArchivoAdjunto) correspondencia.getAdjuntos().get(i))
										.setFileNetID(((RespuestaCargaAdjunto) adjuntos.get(i)).getId());
							}*/
		
							//correspondencia.setFileNetCorrelativo(resCE.getCorrelativoCE());
							// TICKET 9000003791
							//UsuarioPetroperu usuario = obtenerUsuario();
							// FIN TICKET 9000003791
							//if (!this.notificacionService.notificarCorrespondenciaExterna(correspondencia, locale)) { // SE COMENTA POR TICKET 9000003791
							// COMENTADO POR TICKET 9000003874
							/*if (!this.notificacionService.notificarCorrespondenciaExterna(correspondencia, usuario, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}*/
							// SE COMENTA POR TICKET 9*3940
							//if (!this.notificacionService.notificarEnvioCorrespondencia(correspondencia, locale)) {
							/*if (!this.notificacionService.notificarEnvioCorrespondenciaExterno(correspondencia, locale)) {
								throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
							}*/
						//}
					//}
				} else {
					throw new Exception(
							this.messageSource.getMessage("sistcorr.enviar_correspondencia.error", null, locale));
				}
				correspondencia.setUsuarioModifica(usuarioModifica);
				correspondencia.setFechaModifica(new Date());
				correspondencia.setEstado(new CorrespondenciaEstado(Constante.CORRESPONDENCIA_ENVIADA));
				correspondencia.setUsuarioEnvio(usuarioModifica);
				correspondencia.setFechaEnvio(new Date());
				// TICKET 9000004496
				correspondencia.setFlgEnvio("F");
				correspondencia.setFlgEnvioFin(new Date());
				// FIN TICKET 9000004496
				correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
				respuesta.estado = true;
				respuesta.mensaje = this.messageSource.getMessage("sistcorr.enviar_correspondencia.exito", null,
						locale);
				respuesta.datos.add(correspondencia);
			} else {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.enviar_correspondencia.error", null, locale));
			}
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.info("[ERROR] enviarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] enviarCorrespondencia");
		return respuesta;
	}

	public Respuesta<Correspondencia> reasignarCorrespondencia(Long idCorrespondencia, String nuevoResponsable,
			String usuarioModifica, Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		this.LOGGER.info("[INICIO] reasignarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA)) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.reasignar_correspondencia.error", null, locale));
			}
			
			// TICKET 9000004409
			if (!usuarioModifica.equals(correspondencia.getResponsable())) {
				
					boolean existeReemplazo = false;
					for (UsuarioRemplazo user : usuarioRemplazo) {
						LOGGER.info("UsuarioRemplazo: "  + user.getUsername());
	                    if (correspondencia.getResponsable().equals(user.getUsername().toLowerCase())){
	                    	existeReemplazo = true;
	                    	break;
	                    }
					}
						if (!usuarioModifica.equals(correspondencia.getResponsable()) && !existeReemplazo) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}
			}

			// FIN TICKET 9000004409
			if (nuevoResponsable.equals(correspondencia.getResponsable())) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.reasignar_correspondencia.usuario_otro", null, locale));
			}
			Usuario infoNuevoResponsable = null;
			if (this.produccion) {
				infoNuevoResponsable = FConexionDirectorio.datosUsuario(nuevoResponsable, Enviroment.PROD);
				this.LOGGER.info(infoNuevoResponsable.toString());
			} else {
				infoNuevoResponsable = FConexionDirectorio.datosUsuario(nuevoResponsable, Enviroment.QA);
				this.LOGGER.info(infoNuevoResponsable.toString());
			}
			if (infoNuevoResponsable.getUid() == null) {
				throw new Exception(this.messageSource.getMessage("sistcorr.reasignar_correspondencia.usuario_error",
						null, locale));
			}
			if (!this.notificacionService.notificarCorrespondenciaReasignada(correspondencia, nuevoResponsable, locale))
				throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			correspondencia.setResponsable(infoNuevoResponsable.getUid());
			correspondencia.setUsuarioModifica(usuarioModifica);
			correspondencia.setFechaModifica(new Date());
			correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
			respuesta.estado = true;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.reasignar_correspondencia.exito", null, locale);
			respuesta.datos.add(correspondencia);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.info("[ERROR] reasignarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] reasignarCorrespondencia");
		return respuesta;
	}

	public Respuesta<Correspondencia> declinarCorrespondencia(Long idCorrespondencia, String usuarioModifica, String token,
			Locale locale, List<UsuarioRemplazo> usuarioRemplazo) {
		this.LOGGER.info("[INICIO] declinarCorrespondencia");
		Respuesta<Correspondencia> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondencia = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			if (correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA)) {
				throw new Exception(
						this.messageSource.getMessage("sistcorr.declinar_correspondencia.error", null, locale));
			}
			
			
			// TICKET 9000004409
			if (!usuarioModifica.equals(correspondencia.getResponsable())) {
				
					boolean existeReemplazo = false;
					for (UsuarioRemplazo user : usuarioRemplazo) {
						LOGGER.info("UsuarioRemplazo: "  + user.getUsername());
	                    if (correspondencia.getResponsable().equals(user.getUsername().toLowerCase())){
	                    	existeReemplazo = true;
	                    	break;
	                    }
					}
						if (!usuarioModifica.equals(correspondencia.getResponsable()) && !existeReemplazo) {
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			}
			}

			// FIN TICKET 9000004409
			//if (!usuarioModifica.equals(correspondencia.getResponsable())) {
			//	throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			//}
			
			
			CorrespondenciaEstado declinadoPendiente = (CorrespondenciaEstado) this.estadoDAO
					.findOne(Constante.CORRESPONDENCIA_DECLINADA_PENDIENTE);
			CorrespondenciaEstado declinadoFirmada = (CorrespondenciaEstado) this.estadoDAO
					.findOne(Constante.CORRESPONDENCIA_DECLINADA_FIRMADA);
			if (!correspondencia.isFirmaDigital()) {
				correspondencia.setEstado(declinadoPendiente);
				if (!this.notificacionService.notificarCorrespondenciaDeclinadaPendiente(correspondencia, locale))
					throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			} else if (correspondencia.getEstado().getBandeja().equals(declinadoPendiente.getBandeja())) {
				correspondencia.setEstado(declinadoPendiente);
				if (!this.notificacionService.notificarCorrespondenciaDeclinadaPendiente(correspondencia, locale))
					throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			} else if (correspondencia.getEstado().getBandeja().equals(declinadoFirmada.getBandeja())) {
				correspondencia.setEstado(declinadoFirmada);
				if (!this.notificacionService.notificarCorrespondenciaDeclinadaFirmada(correspondencia, locale))
					throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error", null, locale));
			}
			// TICKET 9000004510
			List<ArchivoAdjunto> listaArchivos = correspondencia.getAdjuntos();
			LOGGER.info("Archivos encontrados:" + listaArchivos.size());
			for(ArchivoAdjunto aa : listaArchivos){
				if(Constante.INDICADOR_LOCAL_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
					LOGGER.info("Archivo:" + aa.getNombre() + "-" + aa.getNombreServidor());
					/*if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(aa.getIndicadorRemoto())){
						String urlCarpetaArchivos = "adjuntos";
						String nuevaUrl = apiUrlbase + urlCarpetaArchivos + "/" + aa.getNombreServidor();
						ResponseEntity<byte[]> archivo = sistcorrCliente.descargarDocumentoServidor(token, aa.getNombreServidor());
						FileUtils.writeByteArrayToFile(new File(nuevaUrl), archivo.getBody());
					}*/
					AsignarDocumento document = new AsignarDocumento();
					document.nombre = "Adjuntos";
			        PropiedadesDocumento documentTitle = new PropiedadesDocumento("DocumentTitle", aa.getNombreServidor());
			        document.propiedades.add(documentTitle);
			        Respuesta<RespuestaCargaAdjunto> rptaAA = sistcorrCliente.cargarArchivoServidor(token, document, new File(aa.getUbicacion()), aa.getNombre());
			        LOGGER.info("Estado:" + rptaAA.estado);
					if(rptaAA.estado){
						aa.setIndicadorRemoto(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO);
						File temp = new File(aa.getUbicacion());
						temp.delete();
						//aa = archivoAdjuntoDAO.save(aa);
						LOGGER.info("Actualización de indicador remoto y eliminación de archivo");
					}else{
						//throw new Exception(this.messageSource.getMessage("sistcorr.notificar.error_carga_archivos", null, locale));
						LOGGER.info("[ERROR] El archivo " + aa.getNombre() + "(" + aa.getNombreServidor() + ") no pudo subir correctamente al servidor - Declinar correspondencia.");
					}
				}
			}
			// FIN TICKET
			correspondencia.setUsuarioModifica(usuarioModifica);
			correspondencia.setFechaModifica(new Date());
			correspondencia = (Correspondencia) this.correspondenciaDAO.save(correspondencia);
			respuesta.estado = true;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.declinar_correspondencia.exito", null, locale);
			respuesta.datos.add(correspondencia);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.info("[ERROR] declinarCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] declinarCorrespondencia");
		return respuesta;
	}
	
	@Override
	public boolean validarSiPuedeAsignarFirma(Correspondencia correspondencia, String usuario) {
		LOGGER.info("[VALIDANDO Responsable]" + usuario);
		if(correspondencia.getResponsable().equals(usuario)) // USUARIO RESPONSABLE
			return true;
		// TICKET 9000003994 - POST
		List<ItemFilenet> dependencias = filenetDAO.obtenerDependenciasGestor(usuario);
		for(ItemFilenet dep : dependencias){
			if(correspondencia.getResponsable().toUpperCase().equals(dep.getCodigoAux().toUpperCase())){
				return true;
			}
		}
		List<Firmante> firmantesPre = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
		List<Firmante> _firmantesPre = firmantesPre.stream().sorted(Comparator.comparingLong(Firmante::getId).reversed()).collect(Collectors.toList());
		for(ItemFilenet dep : dependencias){
			LOGGER.info("Ultima firmante:" + _firmantesPre.get(0).getCodFirmante());
			LOGGER.info("Dependencia:" + dep.getCodigoAux());
			if(_firmantesPre.get(0).getCodFirmante().toUpperCase().equals(dep.getCodigoAux().toUpperCase())) // ULTIMO FIRMANTE
				return true;
		}
		// FIN TICKET
		
		if(correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA)) {
			LOGGER.info("[VALIDANDO Ultimo Firmante]");
			List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
			List<Firmante> _firmantes = firmantes.stream().sorted(Comparator.comparingLong(Firmante::getId).reversed()).collect(Collectors.toList()); 			
			LOGGER.info("[ULTIMO NUEVO SOLICITANTE - ULTIMO QUE FIRMO] " + _firmantes.get(0).getCodFirmante());
			if(_firmantes.get(0).getCodFirmante().toLowerCase().equals(usuario.toLowerCase())) // ULTIMO FIRMANTE
				return true;
			// TICKET 9000003994 - POST
			List<ItemFilenet> dependencias_ = filenetDAO.obtenerDependenciasGestor(usuario);
			for(ItemFilenet dep : dependencias_){
				if(_firmantes.get(0).getCodFirmante().toUpperCase().equals(dep.getCodigoAux().toUpperCase())){
					return true;
				}
			}
			// FIN TICKET
		}
		if(correspondencia.getEstado().getId().equals(Constante.CORRESPONDENCIA_POR_CORREGIR)) {
			LOGGER.info("[VALIDANDO Penultimo Firmante]");
			List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
			List<Firmante> _firmantes = firmantes.stream().sorted(Comparator.comparingLong(Firmante::getId).reversed()).collect(Collectors.toList());
			Firmante ultimoFirmante = _firmantes.stream()
					.filter(f -> f.getEstado().getId().equals(Constante.CORRESPONDENCIA_FIRMADA) && usuario.equals(f.getCodFirmante()))
					.findAny()
					.orElse(null);
			//LOGGER.info("[ULTIMO NUEVO SOLICITANTE - ULTIMO QUE FIRMO] " + ultimoFirmante.getCodFirmante());
			if(ultimoFirmante != null && ultimoFirmante.getCodFirmante().equals(usuario)){ // PENULTIMO FIRMANTE
				LOGGER.info("[ULTIMO NUEVO SOLICITANTE - ULTIMO QUE FIRMO] " + ultimoFirmante.getCodFirmante());
				return true;
			}
			if(_firmantes.size()>0 && _firmantes.get(0).getCodFirmante().equals(usuario)){
				LOGGER.info("[RECHAZO DEL ULTIMO SOLICITANTE] " + _firmantes.get(0).getCodFirmante());
				return true;
			}
		}
		LOGGER.info("[NO TIENE ACCESO]");
		return false;
	}

	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}

	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondencias(int considerarOriginadora, String codDependenciaOriginadora,
			String codeDependenciaRemitente, String correlativo, String asunto, String estado, int masFiltros,
			String codNombreOriginador, String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision,
			int firmaDigital, int confidencial, int urgente, int despachoFisico, String codDependenciaDestinataria,
			String nombreDependenciaDestinataria , String codDependenciaCopia, String usuario, int esJefe, int todos, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondencias");
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			String correspondenciasOriginadoras = "";
			List<CorrespondenciaConsultaDTO> correspondencias = new ArrayList<>();
			if(considerarOriginadora==1){
				if(!codDependenciaOriginadora.equals("")) {
					List<ItemFilenet> dependenciasSubordinadas = filenetDAO.listarDependenciasSubordinadas(codDependenciaOriginadora);
					for (ItemFilenet depend : dependenciasSubordinadas) {
						correspondenciasOriginadoras += "'"+depend.getId() + "',";
					}
					
				} 
				correspondenciasOriginadoras = correspondenciasOriginadoras.length() > 0 ? correspondenciasOriginadoras.substring(0, correspondenciasOriginadoras.length() -1) : "";
			}else{
				correspondenciasOriginadoras = codDependenciaOriginadora;
			}
			LOGGER.info("Dependencias originadoras: " + correspondenciasOriginadoras);
			LOGGER.info("PARAMETROS: " + considerarOriginadora + "||" + correspondenciasOriginadoras + "||" + codeDependenciaRemitente + "||" + correlativo + "||" + asunto + "||" + estado + "||" + masFiltros + "||" + codNombreOriginador + "||" + fechaDesde + "||" + fechaHasta + "||" + tipoCorrespondencia + "||" + tipoEmision + "||" + firmaDigital + "||" + confidencial + "||" + urgente + "||" + despachoFisico + "||" + codDependenciaDestinataria + "||" + nombreDependenciaDestinataria + "||" + codDependenciaCopia + "||" + usuario + "||" + esJefe + "||" + todos);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe);
			List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act_orig(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe, todos);
			LOGGER.info("CANTIDAD DE RESULTADOS:" + _correspondencias.size());
			for (Object[] _correspondencia : _correspondencias) {
				//
				/*for (Object objects : _correspondencia) {
					System.out.println(String.valueOf(objects));
				}*/
				CorrespondenciaConsultaDTO corr = new CorrespondenciaConsultaDTO(
						_correspondencia[0] == null ? 0L : Long.valueOf(_correspondencia[0].toString()), 
						_correspondencia[1] == null ? "" : String.valueOf(_correspondencia[1]), 
						_correspondencia[2] == null ? "" : String.valueOf(_correspondencia[2]), 
						_correspondencia[3] == null ? "" : String.valueOf(_correspondencia[3]), 
						_correspondencia[4] == null ? "" : String.valueOf(_correspondencia[4]), 
						_correspondencia[5] == null ? "" : String.valueOf(_correspondencia[5]),  
						_correspondencia[6] == null ? "" : String.valueOf(_correspondencia[6]), 
						_correspondencia[7] == null ? "" : String.valueOf(_correspondencia[7]),  
						_correspondencia[8] == null ? "" : String.valueOf(_correspondencia[8]), 
						_correspondencia[9] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[9].toString().substring(0, 19)),
						_correspondencia[10] == null ? "" : String.valueOf(_correspondencia[10]),  
						_correspondencia[11] == null ? 0L : Long.valueOf(_correspondencia[11].toString()), 
						_correspondencia[12] == null ? "" : String.valueOf(_correspondencia[12]),
						_correspondencia[13] == null ? "" : String.valueOf(_correspondencia[13]), 
						_correspondencia[14] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[14])), 
						_correspondencia[15] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[15])),  
						_correspondencia[16] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[16])), 
						_correspondencia[17] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[17])), 
						_correspondencia[18] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[18])), 
						_correspondencia[19] == null ? "" : String.valueOf(_correspondencia[19]),  
						_correspondencia[20] == null ? "" : String.valueOf(_correspondencia[20]),  
						_correspondencia[21] == null ? "" : String.valueOf(_correspondencia[21]), 
						_correspondencia[22] == null ? "" : String.valueOf(_correspondencia[22]), 
						_correspondencia[23] == null ? "" : String.valueOf(_correspondencia[23]), 
						_correspondencia[24] == null ? "" : String.valueOf(_correspondencia[24]),  
						_correspondencia[25] == null ? "" : String.valueOf(_correspondencia[25]), 
						_correspondencia[26] == null ? 0L : Long.valueOf(_correspondencia[26].toString()),
						_correspondencia[29] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[29])),
						_correspondencia[27] == null ? "" : String.valueOf(_correspondencia[27]),
						_correspondencia[28] == null? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[28].toString().substring(0, 19)));
				//LOGGER.info("[CORR] " + corr.toString());
				correspondencias.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			respuesta.datos.addAll(correspondencias);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondencias", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarCorrespondencias");
		return respuesta;
	}

	// inicio ticket 9000003996
	public List<Flujo> nombreNumeroFlujos(Long idCorrespondecia) {

		List<Flujo> flujos = new ArrayList<>();
		Flujo flujo;

		List<String> listaNumerosFlujo = correspondenciaDAO.findNumeroFujos(idCorrespondecia);

		if (listaNumerosFlujo != null && listaNumerosFlujo.size() > 0) {
			
			int contador = 0;
			

			while (contador < listaNumerosFlujo.size()) {
				flujo = new Flujo();
				
				flujo.setIndice(contador + 1);
				flujo.setIdCorrespondencia(idCorrespondecia);

				if ((contador + 1) == listaNumerosFlujo.size()) {
					flujo.setNombre("Flujo Actual");
				} else {
					flujo.setNombre("Flujo "+(contador+1));
				}
				
				flujos.add(flujo);

				contador++;
			}

		}

		return flujos;
	}
	

	
	public List<pe.com.petroperu.model.Correspondencia> listaFlujo(Long correspondecia, Long nroFlujo) {
		
		return correspondenciaDAO.listaFlujo(correspondecia, nroFlujo);
	}
	
	// fin ticket 9000003996

	@Override
	@Transactional
	public Respuesta<DestinatarioExterno> actualizarCorreoDestinatarioExterno(Long idCorrespondencia,
			String usuarioModifica, Long idDestinatarioExterno, String correo, Locale locale) {
		// TODO Auto-generated method stub
		Respuesta<DestinatarioExterno> respuesta = new Respuesta<>();
		try {
			Correspondencia correspondenciaBD = (Correspondencia) this.correspondenciaDAO.findOne(idCorrespondencia);
			if(correspondenciaBD == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.correspondencia.error", null, locale));
			
			if (correspondenciaBD.getEstado() != null && correspondenciaBD.getEstado().getId().equals(Constante.CORRESPONDENCIA_ENVIADA))
				throw new Exception(this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.estado.error", null, locale));
			
			if (!(correspondenciaBD.getResponsable() != null && correspondenciaBD.getResponsable().equalsIgnoreCase(usuarioModifica.trim())))
				throw new Exception(this.messageSource.getMessage("sistcorr.permisos.error", null, locale));
			
			List<DestinatarioExterno> perteneceDestiExternoCorrespondencia = correspondenciaBD.getDetalleExterno().stream().filter(dx -> { return dx.getId().compareTo(idDestinatarioExterno) == 0;}).collect(Collectors.toList());
			
			if (!(perteneceDestiExternoCorrespondencia != null && perteneceDestiExternoCorrespondencia.size() > 0))
				throw new Exception(this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.destinatario.correspondencia.error", null, locale));
			
			DestinatarioExterno dxBD = perteneceDestiExternoCorrespondencia.get(0);
			
			if (!(dxBD.isEsCorreoElectronicoDestExterno() != null && dxBD.isEsCorreoElectronicoDestExterno()))
				throw new Exception(this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.destinatario.error", null, locale));
			
			dxBD.setUsuarioModifica(usuarioModifica);
			dxBD.setFechaModifica(new Date());
			dxBD.setCorreoDestinatario(correo);
			
			dxBD = (DestinatarioExterno) this.destinatarioExternoDAO.save(dxBD);
			
			if (dxBD == null)
				throw new Exception(this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.error", null, locale));
			
			respuesta.estado = true;
			respuesta.mensaje = this.messageSource.getMessage("sistcorr.actualizar_correo_destinatario.exito", null, locale);
			respuesta.datos.add(dxBD);
			
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] actualizarCorreoDestinatarioExterno", e);
		}
		
		this.LOGGER.info("[FIN] actualizarCorreoDestinatarioExterno");
		
		return respuesta;
	}
	
	// TICKET 9000004044
	public List<Correspondencia> listarDocumentoRespuestaCorrespondencia(String correlativo){
		LOGGER.info("[INICIO] listarDocumentoRespuestaCorrespondencia " + correlativo);
		try{
			List<Correspondencia> correspondencias = correspondenciaDAO.listarDocumentoRespuestaCorrespondencia(correlativo); 
			return correspondencias;
		}catch(Exception e){
			LOGGER.error("[ERROR] listarDocumentoRespuestaCorrespondencia " + e);
			return null;
		}
	}
	
	public List<Correspondencia> listarDocumentoRespuestaAsignacion(Long idAsignacion){
		return correspondenciaDAO.findByIdAsignacion(idAsignacion);
	}
	
	public List<Correspondencia> buscarCorrespondenciaPorNumeroDocumento(String numeroDocumento){
		LOGGER.info("[INICIO] buscarCorrespondenciaPorNumeroDocumento " + numeroDocumento);
		List<Correspondencia> correspondencias = new ArrayList<>();
		try{
			Correlativo correl = correlativoDAO.findOneByCodigo(numeroDocumento);
			if(correl != null){
				Correspondencia corres = correspondenciaDAO.findOneByCorrelativo(correl);
				if(corres != null){
					correspondencias.add(corres);
				}
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] buscarCorrespondenciaPorNumeroDocumento");
			correspondencias = new ArrayList<>();
		}
		LOGGER.info("[FIN] buscarCorrespondenciaPorNumeroDocumento");
		return correspondencias;
	}
	// FIN TICKET
	//Inicio Ticket 4408
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestor(
			String codeDependenciaRemitente, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasJefeGestor");
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {		
			List<CorrespondenciaConsultaDTO> correspondencias = new ArrayList<>();
			List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act_orig_gestor( codeDependenciaRemitente, correlativo, asunto, estado, fechaDesde, fechaHasta);
			LOGGER.info("CANTIDAD DE RESULTADOS:" + _correspondencias.size());
			for (Object[] _correspondencia : _correspondencias) {
				
				CorrespondenciaConsultaDTO corr = new CorrespondenciaConsultaDTO(
						_correspondencia[0] == null ? 0L : Long.valueOf(_correspondencia[0].toString()), 
						_correspondencia[1] == null ? "" : String.valueOf(_correspondencia[1]), 
						_correspondencia[2] == null ? "" : String.valueOf(_correspondencia[2]), 
						_correspondencia[3] == null ? "" : String.valueOf(_correspondencia[3]), 
						_correspondencia[4] == null ? "" : String.valueOf(_correspondencia[4]), 
						_correspondencia[5] == null ? "" : String.valueOf(_correspondencia[5]),  
						_correspondencia[6] == null ? "" : String.valueOf(_correspondencia[6]), 
						_correspondencia[7] == null ? "" : String.valueOf(_correspondencia[7]),  
						_correspondencia[8] == null ? "" : String.valueOf(_correspondencia[8]), 
						_correspondencia[9] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[9].toString().substring(0, 19)),
						_correspondencia[10] == null ? "" : String.valueOf(_correspondencia[10]),  
						_correspondencia[11] == null ? 0L : Long.valueOf(_correspondencia[11].toString()), 
						_correspondencia[12] == null ? "" : String.valueOf(_correspondencia[12]),
						_correspondencia[13] == null ? "" : String.valueOf(_correspondencia[13]), 
						_correspondencia[14] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[14])), 
						_correspondencia[15] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[15])),  
						_correspondencia[16] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[16])), 
						_correspondencia[17] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[17])), 
						_correspondencia[18] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[18])), 
						_correspondencia[19] == null ? "" : String.valueOf(_correspondencia[19]),  
						_correspondencia[20] == null ? "" : String.valueOf(_correspondencia[20]),  
						_correspondencia[21] == null ? "" : String.valueOf(_correspondencia[21]), 
						_correspondencia[22] == null ? "" : String.valueOf(_correspondencia[22]), 
						_correspondencia[23] == null ? "" : String.valueOf(_correspondencia[23]), 
						_correspondencia[24] == null ? "" : String.valueOf(_correspondencia[24]),  
						_correspondencia[25] == null ? "" : String.valueOf(_correspondencia[25]), 
						_correspondencia[26] == null ? 0L : Long.valueOf(_correspondencia[26].toString()),
						_correspondencia[29] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[29])),
						_correspondencia[27] == null ? "" : String.valueOf(_correspondencia[27]),
						_correspondencia[28] == null? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[28].toString().substring(0, 19)));
				//LOGGER.info("[CORR] " + corr.toString());
				correspondencias.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			respuesta.datos.addAll(correspondencias);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasJefeGestor", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarCorrespondenciasJefeGestor");
		return respuesta;
	}
	//Fin Ticket 4408
	
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasJefeGestorPaginado(
			String codeDependenciaRemitente, String correlativo,  String asunto, String estado,
			String fechaDesde, String fechaHasta, String token, int length, int start, String columna, String orden, String excel, String fechaModificaDesde, String fechaModificaHasta, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasJefeGestorPaginado");
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {		
			List<CorrespondenciaConsultaDTO> correspondencias = new ArrayList<>();
			LOGGER.info(codeDependenciaRemitente + "||" + correlativo + "||" + asunto + "||" + estado + "||" + fechaDesde + "||" + fechaHasta + "||" + length + "||" + start + "||" + columna + "||" + orden + "||" + excel + "||" + fechaModificaDesde + "||" + fechaModificaHasta);
			List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act_orig_gestor_paginado( codeDependenciaRemitente, correlativo, asunto, estado, fechaDesde, fechaHasta, start, length, columna, orden, excel,fechaModificaDesde, fechaModificaHasta);
			LOGGER.info("CANTIDAD DE RESULTADOS:" + _correspondencias.size());
			for (Object[] _correspondencia : _correspondencias) {
				
				CorrespondenciaConsultaDTO corr = new CorrespondenciaConsultaDTO(
						_correspondencia[2] == null ? 0L : Long.valueOf(_correspondencia[2].toString()), 
						_correspondencia[3] == null ? "" : String.valueOf(_correspondencia[3]), 
						_correspondencia[4] == null ? "" : String.valueOf(_correspondencia[4]), 
						_correspondencia[5] == null ? "" : String.valueOf(_correspondencia[5]), 
						_correspondencia[6] == null ? "" : String.valueOf(_correspondencia[6]), 
						_correspondencia[7] == null ? "" : String.valueOf(_correspondencia[7]),  
						_correspondencia[8] == null ? "" : String.valueOf(_correspondencia[8]), 
						_correspondencia[9] == null ? "" : String.valueOf(_correspondencia[9]),  
						_correspondencia[10] == null ? "" : String.valueOf(_correspondencia[10]), 
						_correspondencia[11] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[11].toString().substring(0, 19)),
						_correspondencia[12] == null ? "" : String.valueOf(_correspondencia[12]),  
						_correspondencia[13] == null ? 0L : Long.valueOf(_correspondencia[13].toString()), 
						_correspondencia[14] == null ? "" : String.valueOf(_correspondencia[14]),
						_correspondencia[15] == null ? "" : String.valueOf(_correspondencia[15]), 
						_correspondencia[16] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[16])), 
						_correspondencia[17] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[17])),  
						_correspondencia[18] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[18])), 
						_correspondencia[19] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[19])), 
						_correspondencia[20] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[20])), 
						_correspondencia[21] == null ? "" : String.valueOf(_correspondencia[21]),  
						_correspondencia[22] == null ? "" : String.valueOf(_correspondencia[22]),  
						_correspondencia[23] == null ? "" : String.valueOf(_correspondencia[23]), 
						_correspondencia[24] == null ? "" : String.valueOf(_correspondencia[24]), 
						_correspondencia[25] == null ? "" : String.valueOf(_correspondencia[25]), 
						_correspondencia[26] == null ? "" : String.valueOf(_correspondencia[26]),  
						_correspondencia[27] == null ? "" : String.valueOf(_correspondencia[27]), 
						_correspondencia[28] == null ? 0L : Long.valueOf(_correspondencia[28].toString()),
						_correspondencia[31] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[31])),
						_correspondencia[29] == null ? "" : String.valueOf(_correspondencia[29]),
						_correspondencia[30] == null? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[30].toString().substring(0, 19)));
				Integer total = _correspondencia[1] == null ? 0 : Integer.valueOf(_correspondencia[1].toString());
				corr.setTotal(total);
				//LOGGER.info("[CORR] " + corr.toString());
				correspondencias.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			respuesta.datos.addAll(correspondencias);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasJefeGestorPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarCorrespondenciasJefeGestorPaginado");
		return respuesta;
	}
	
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarCorrespondenciasPaginado(int considerarOriginadora, String codDependenciaOriginadora,
			String codeDependenciaRemitente, String correlativo, String asunto, String estado, int masFiltros,
			String codNombreOriginador, String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision,
			int firmaDigital, int confidencial, int urgente, int despachoFisico, String codDependenciaDestinataria,
			String nombreDependenciaDestinataria , String codDependenciaCopia, String usuario, int esJefe, int todos, 
			int start, int length, String columna, String orden, String excel, String fechaModificaDesde, String fechaModificaHasta, Locale locale) {
		LOGGER.info("[INICIO] consultarCorrespondenciasPaginado");
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			String correspondenciasOriginadoras = "";
			List<CorrespondenciaConsultaDTO> correspondencias = new ArrayList<>();
			if(considerarOriginadora==1){
				if(!codDependenciaOriginadora.equals("")) {
					List<ItemFilenet> dependenciasSubordinadas = filenetDAO.listarDependenciasSubordinadas(codDependenciaOriginadora);
					for (ItemFilenet depend : dependenciasSubordinadas) {
						correspondenciasOriginadoras += "'"+depend.getId() + "',";
					}
					
				} 
				correspondenciasOriginadoras = correspondenciasOriginadoras.length() > 0 ? correspondenciasOriginadoras.substring(0, correspondenciasOriginadoras.length() -1) : "";
			}else{
				correspondenciasOriginadoras = codDependenciaOriginadora;
			}
			LOGGER.info("Dependencias originadoras: " + correspondenciasOriginadoras);
				
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe);
			//List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act_orig(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe, todos);
			LOGGER.info("PARAMETROS: " + considerarOriginadora + "||" + correspondenciasOriginadoras + "||" + codeDependenciaRemitente + "||" + correlativo + "||" + asunto + "||" + estado + "||" + masFiltros + "||" + codNombreOriginador + "||" + fechaDesde + "||" + fechaHasta + "||" + tipoCorrespondencia + "||" + tipoEmision + "||" + firmaDigital + "||" + confidencial + "||" + urgente + "||" + despachoFisico + "||" + codDependenciaDestinataria + "||" + nombreDependenciaDestinataria + "||" + codDependenciaCopia + "||" + usuario + "||" + esJefe + "||" + todos + "||" + start + "||" + length + "||" + columna + "||" + orden + "||" + excel + "||" + fechaModificaDesde + "||" + fechaModificaHasta );
			List<Object[]> _correspondencias = correspondenciaDAO.consultar_cantidad_jefe_act_orig_paginado(considerarOriginadora, correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, usuario, esJefe, todos, start, length, columna, orden, excel,fechaModificaDesde,fechaModificaHasta);
			LOGGER.info("CANTIDAD DE RESULTADOS:" + _correspondencias.size());
			for (Object[] _correspondencia : _correspondencias) {
				//
				/*for (Object objects : _correspondencia) {
					System.out.println(String.valueOf(objects));
				}*/
				CorrespondenciaConsultaDTO corr = new CorrespondenciaConsultaDTO(
						_correspondencia[2] == null ? 0L : Long.valueOf(_correspondencia[2].toString()), 
						_correspondencia[3] == null ? "" : String.valueOf(_correspondencia[3]), 
						_correspondencia[4] == null ? "" : String.valueOf(_correspondencia[4]), 
						_correspondencia[5] == null ? "" : String.valueOf(_correspondencia[5]), 
						_correspondencia[6] == null ? "" : String.valueOf(_correspondencia[6]), 
						_correspondencia[7] == null ? "" : String.valueOf(_correspondencia[7]),  
						_correspondencia[8] == null ? "" : String.valueOf(_correspondencia[8]), 
						_correspondencia[9] == null ? "" : String.valueOf(_correspondencia[9]),  
						_correspondencia[10] == null ? "" : String.valueOf(_correspondencia[10]), 
						_correspondencia[11] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[11].toString().substring(0, 19)),
						_correspondencia[12] == null ? "" : String.valueOf(_correspondencia[12]),  
						_correspondencia[13] == null ? 0L : Long.valueOf(_correspondencia[13].toString()), 
						_correspondencia[14] == null ? "" : String.valueOf(_correspondencia[14]),
						_correspondencia[15] == null ? "" : String.valueOf(_correspondencia[15]), 
						_correspondencia[16] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[16])), 
						_correspondencia[17] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[17])),  
						_correspondencia[18] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[18])), 
						_correspondencia[19] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[19])), 
						_correspondencia[20] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[20])), 
						_correspondencia[21] == null ? "" : String.valueOf(_correspondencia[21]),  
						_correspondencia[22] == null ? "" : String.valueOf(_correspondencia[22]),  
						_correspondencia[23] == null ? "" : String.valueOf(_correspondencia[23]), 
						_correspondencia[24] == null ? "" : String.valueOf(_correspondencia[24]), 
						_correspondencia[25] == null ? "" : String.valueOf(_correspondencia[25]), 
						_correspondencia[26] == null ? "" : String.valueOf(_correspondencia[26]),  
						_correspondencia[27] == null ? "" : String.valueOf(_correspondencia[27]), 
						_correspondencia[28] == null ? 0L : Long.valueOf(_correspondencia[28].toString()),
						_correspondencia[31] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[31])),
						_correspondencia[29] == null ? "" : String.valueOf(_correspondencia[29]),
						_correspondencia[30] == null? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[30].toString().substring(0, 19)));
				//LOGGER.info("[CORR] " + corr.toString());
				Integer total = _correspondencia[1] == null ? 0 : Integer.valueOf(_correspondencia[1].toString());
				corr.setTotal(total);
				correspondencias.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			respuesta.datos.addAll(correspondencias);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarCorrespondenciasPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarCorrespondenciasPaginado");
		return respuesta;
	}

	//ticket inicio 9000004714
	@Override
	public Respuesta<DatosFirmanteDTO> obtenerDatosFirmanteYFirmantePrevio(Long idCorrespondencia, Locale locale, String usuario) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] obtenerDatosFirmante");
		Respuesta<DatosFirmanteDTO> respuesta = new Respuesta<>();
		try {
			List<Object[]> _respuesta = this.correspondenciaDAO.obtenerFirmanteAndFirmantePrevioCorrespondencia(idCorrespondencia, usuario);
			
			for (Object[] corr : _respuesta) {
				DatosFirmanteDTO dto = new DatosFirmanteDTO();
				dto.setId(Long.valueOf(Long.parseLong(String.valueOf(corr[0]))));
				dto.setIdCorrespondencia(Long.parseLong(String.valueOf(corr[1])));
				dto.setCodFirmante(String.valueOf(corr[2]));
				dto.setSolicitante(String.valueOf(corr[3]));
				dto.setMotivoRechazo(((corr[4] != null)?(String.valueOf(corr[4])):(null)));
				dto.setCodFirmantePrevio(((corr[5] != null)?(String.valueOf(corr[5])):(null)));
				dto.setMensaje(((corr[6] != null)?(String.valueOf(corr[6])):(null)));
				respuesta.datos.add(dto);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] obtenerDatosFirmante", e);
		}
		this.LOGGER.info("[FIN] obtenerDatosFirmante");
		return respuesta;
	}
	//ticket fin 9000004714
	
	// TICKET 9000004651
	@Override
	public Long validarNivelFirma(String correlativos, Locale locale){
		LOGGER.info("[INICIO] validarNivelFirma " + correlativos);
		Long tipoFirma = 0L;
		try{
			String[] corrs = correlativos.split(",");
			for(int i=0;i<corrs.length;i++){
				Correspondencia correspondencia = correspondenciaDAO.findOne(Long.valueOf(corrs[i]));
				Long tFirma = this.obtenerTipoFirma(correspondencia);
				if(tipoFirma.compareTo(0L) == 0){
					LOGGER.info("PRIMERA ENTRADA:" + tFirma);
					if(tFirma.compareTo(0L) == 0){
						break;
					}else{
						tipoFirma = tFirma;
					}
				}else{
					LOGGER.info("NO ES PRIMERA ENTRADA:" + tipoFirma + "-" + tFirma);
					if(tipoFirma.compareTo(tFirma) != 0){
						tipoFirma = 0L;
						break;
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error("[ERROR] validarNivelFirma", e);
			tipoFirma = 0L;
		}
		LOGGER.info("[FIN] validarNivelFirma");
		return tipoFirma;
	}
	
	public Long obtenerTipoFirma(Correspondencia correspondencia){
		LOGGER.info("[INICIO] obtenerTipoFirma " + correspondencia.getCodRemitente());
		Long tipoFirma = 0L;
		try{
			UsuarioPetroperu usuario = obtenerUsuario();
			if(correspondencia.getCodRemitente().toUpperCase().equalsIgnoreCase(usuario.getUsername().toUpperCase())){
				tipoFirma = Constante.ULTIMO_FIRMANTE;
				LOGGER.info("ULTIMO FIRMANTE");
			}else{
				List<Firmante> firmantes = firmanteDAO.obtenerFirmantes(correspondencia.getId());
				Boolean primeraFirma = true;
				for(int i=0;i<firmantes.size();i++){
					Firmante f = firmantes.get(i);
					if(f.getEstado().getId()==Constante.CORRESPONDENCIA_FIRMADA && f.getNroFlujo()==correspondencia.getNroFlujo()){
						primeraFirma = false;
					}
				}
				if(primeraFirma && correspondencia.getUsuarioCrea().equals(usuario.getUsername())){
					tipoFirma = Constante.SOY_AUTOR;
					LOGGER.info("AUTOR");
				}else{
					tipoFirma = Constante.VISTO_BUENO;
					LOGGER.info("VISTO BUENO");
				}
			}
		}catch(Exception e){
			LOGGER.error("[ERROR] obtenerTipoFirma", e);
			tipoFirma = 0L;
		}
		LOGGER.info("[FIN] obtenerTipoFirma ");
		return tipoFirma;
	}
	// FIN TICKET
	
	
	// TICKET 9000004962
	@Override
	public Respuesta<CorrespondenciaConsultaDTO> consultarAuditoriaCorrespondenciasPaginado(String codDependenciaOriginadora,
			String codeDependenciaRemitente, String correlativo, String asunto, String estado, int masFiltros,
			String codNombreOriginador, String fechaDesde, String fechaHasta, int tipoCorrespondencia, int tipoEmision,
			int firmaDigital, int confidencial, int urgente, int despachoFisico, String codDependenciaDestinataria,
			String nombreDependenciaDestinataria , String codDependenciaCopia, int todos, 
			int start, int length, String columna, String orden, String excel, String fechaModificaDesde, String fechaModificaHasta, Locale locale) {
		LOGGER.info("[INICIO] consultarAuditoriaCorrespondenciasPaginado");
		Respuesta<CorrespondenciaConsultaDTO> respuesta = new Respuesta<>();
		try {
			String correspondenciasOriginadoras = "";
			List<CorrespondenciaConsultaDTO> correspondencias = new ArrayList<>();

			correspondenciasOriginadoras = codDependenciaOriginadora;
	
			LOGGER.info("Dependencias originadoras: " + correspondenciasOriginadoras);
			
			LOGGER.info("PARAMETROS: " + correspondenciasOriginadoras + "||" + codeDependenciaRemitente + "||" + correlativo + "||" + asunto + "||" + estado + "||" + masFiltros + "||" + codNombreOriginador + "||" + fechaDesde + "||" + fechaHasta + "||" + tipoCorrespondencia + "||" + tipoEmision + "||" + firmaDigital + "||" + confidencial + "||" + urgente + "||" + despachoFisico + "||" + codDependenciaDestinataria + "||" + nombreDependenciaDestinataria + "||" + codDependenciaCopia + "||"  + todos + "||" + start + "||" + length + "||" + columna + "||" + orden + "||" + excel + "||" + fechaModificaDesde + "||" + fechaModificaHasta );
			List<Object[]> _correspondencias = correspondenciaDAO.consultar_auditoria_correspondencia_paginado(correspondenciasOriginadoras, codeDependenciaRemitente, correlativo, asunto, estado, masFiltros, codNombreOriginador, fechaDesde, fechaHasta, tipoCorrespondencia, tipoEmision, firmaDigital, confidencial, urgente, despachoFisico, codDependenciaDestinataria, nombreDependenciaDestinataria, codDependenciaCopia, todos, start, length, columna, orden, excel,fechaModificaDesde,fechaModificaHasta);
			LOGGER.info("CANTIDAD DE RESULTADOS:" + _correspondencias.size());
			for (Object[] _correspondencia : _correspondencias) {
				//
				/*for (Object objects : _correspondencia) {
					System.out.println(String.valueOf(objects));
				}*/
				CorrespondenciaConsultaDTO corr = new CorrespondenciaConsultaDTO(
						_correspondencia[2] == null ? 0L : Long.valueOf(_correspondencia[2].toString()), 
						_correspondencia[3] == null ? "" : String.valueOf(_correspondencia[3]), 
						_correspondencia[4] == null ? "" : String.valueOf(_correspondencia[4]), 
						_correspondencia[5] == null ? "" : String.valueOf(_correspondencia[5]), 
						_correspondencia[6] == null ? "" : String.valueOf(_correspondencia[6]), 
						_correspondencia[7] == null ? "" : String.valueOf(_correspondencia[7]),  
						_correspondencia[8] == null ? "" : String.valueOf(_correspondencia[8]), 
						_correspondencia[9] == null ? "" : String.valueOf(_correspondencia[9]),  
						_correspondencia[10] == null ? "" : String.valueOf(_correspondencia[10]), 
						_correspondencia[11] == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[11].toString().substring(0, 19)),
						_correspondencia[12] == null ? "" : String.valueOf(_correspondencia[12]),  
						_correspondencia[13] == null ? 0L : Long.valueOf(_correspondencia[13].toString()), 
						_correspondencia[14] == null ? "" : String.valueOf(_correspondencia[14]),
						_correspondencia[15] == null ? "" : String.valueOf(_correspondencia[15]), 
						_correspondencia[16] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[16])), 
						_correspondencia[17] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[17])),  
						_correspondencia[18] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[18])), 
						_correspondencia[19] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[19])), 
						_correspondencia[20] == null ? false : Boolean.valueOf(String.valueOf(_correspondencia[20])), 
						_correspondencia[21] == null ? "" : String.valueOf(_correspondencia[21]),  
						_correspondencia[22] == null ? "" : String.valueOf(_correspondencia[22]),  
						_correspondencia[23] == null ? "" : String.valueOf(_correspondencia[23]), 
						_correspondencia[24] == null ? "" : String.valueOf(_correspondencia[24]), 
						_correspondencia[25] == null ? "" : String.valueOf(_correspondencia[25]), 
						_correspondencia[26] == null ? "" : String.valueOf(_correspondencia[26]),  
						_correspondencia[27] == null ? "" : String.valueOf(_correspondencia[27]), 
						_correspondencia[28] == null ? 0L : Long.valueOf(_correspondencia[28].toString()),
						_correspondencia[31] == null ? 0 : Integer.valueOf(String.valueOf(_correspondencia[31])),
						_correspondencia[29] == null ? "" : String.valueOf(_correspondencia[29]),
						_correspondencia[30] == null? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(_correspondencia[30].toString().substring(0, 19)));
				//LOGGER.info("[CORR] " + corr.toString());
				Integer total = _correspondencia[1] == null ? 0 : Integer.valueOf(_correspondencia[1].toString());
				corr.setTotal(total);
				correspondencias.add(corr);
			}
			respuesta.estado = true;
			respuesta.mensaje = messageSource.getMessage("sistcorr.consulta.exito", null, locale);
			respuesta.datos.addAll(correspondencias);
		} catch (Exception e) {
			LOGGER.error("[ERROR] consultarAuditoriaCorrespondenciasPaginado", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		}
		LOGGER.info("[FIN] consultarAuditoriaCorrespondenciasPaginado");
		return respuesta;
	}
	// FIN TICKET
}
