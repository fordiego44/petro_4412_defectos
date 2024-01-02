package pe.com.petroperu.scheduling.tasks;

import java.util.ArrayList;
import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.List;

import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.NotificacionNoEnviada;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.notificacion.Mail;
import pe.com.petroperu.notificacion.NotificacionService;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.INotificacionNoEnviadaDAO;
import pe.com.petroperu.sistcorr.dao.IParametroDAO;
import pe.com.petroperu.util.Constante;

@Component
@PropertySource({ "classpath:application.properties" })
public class ScheduledTasksNotificacionNoEnviada {

	@Autowired
	private INotificacionNoEnviadaDAO notificacionNoEnviadaDAO;
	
	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;
	
	@Autowired
	private IParametroDAO parametroDAO;
	
	@Autowired
	private ISistcorrCliente sistcorrCliente;
	
	@Value("${sistcorr.directorio}")
	private String urlBase;
	
	@Autowired
	private NotificacionService notificacionService;
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasksNotificacionNoEnviada.class);

	//private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRateString = "${sistcorr.frecuencia.schedulink.task.notificacion}")
	public void enviarNotificacionNoEnviadas() {
		log.info("[INICIO] ScheduledTasksNotificacionNoEnviada->enviarNotificacionNoEnviadas");
		List<NotificacionNoEnviada> notificacionesBDNE = notificacionNoEnviadaDAO.findByEstado('N');
		if(notificacionesBDNE != null && notificacionesBDNE.size() > 0) {
			// TICKET 9000004510
			List<ArchivoAdjunto> archivos = new ArrayList<ArchivoAdjunto>();
			// FIN TICKET
			log.info("iniciando envio de notificaciones por ScheduledTasks");
			for(NotificacionNoEnviada not : notificacionesBDNE) {
				// TICKET 9000004510
				if(not.getNombreServidor() != null){
					String[] params = not.getNombreServidor().split("/");
					String nombreServ = params[params.length - 1];
					log.info("Nombre:" + nombreServ);
					ArchivoAdjunto archivo = archivoAdjuntoDAO.findOneByNombreServidor(nombreServ);
					log.info("Archivo Encontrado:" + archivo);
					if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(archivo.getIndicadorRemoto())){
						archivos.add(archivo);
						String urlCarpetaArchivos = "adjuntos";
						String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + archivo.getNombreServidor();
						List<Parametro> usuario = parametroDAO.findByGrupo("USER_FILENET");
						List<Parametro> password = parametroDAO.findByGrupo("PASS_FILENET");
						Respuesta<String> respuestaToken = this.sistcorrCliente.recuperarToken(usuario.get(0).getDenominacion(), password.get(0).getDenominacion());
						ResponseEntity<byte[]> archivoDescargado =  sistcorrCliente.descargarDocumentoServidor(respuestaToken.datos.get(0), archivo.getNombreServidor());
						try{
							org.apache.commons.io.FileUtils.writeByteArrayToFile(new java.io.File(nuevaUrl), archivoDescargado.getBody());
						}catch(Exception e){
							log.error("[ERROR]", e);
						}
					}
				}
				// FIN TICKET
				Mail mail = new Mail();
				mail.setTo(not.getDestinatarios());
				if(not.getCopiaCc() != null && !not.getCopiaCc().trim().equalsIgnoreCase(""))
					mail.setCopies(not.getCopiaCc().split("\\,"));
				mail.setHtmlBody(not.getCuerpo());
				mail.setFrom(not.getDe());
				mail.setSubject(not.getAsunto());
				mail.setArchivosDirStr(not.getNombreServidor());
				mail.setNombreArcStr(not.getNombreArc());
				boolean enviado = notificacionService.enviarNotificacionScheduledTasks(mail);
				if(enviado) {
					not.setFechaModifica(new Date());
					not.setUsuarioModifica("usrauto");
					not.setEstado(((enviado)?('S'):('N')));
					
					notificacionNoEnviadaDAO.save(not);
				}
			}
			// TICKET 9000004510
			for(ArchivoAdjunto arch : archivos){
				String urlCarpetaArchivos = "adjuntos";
				String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + arch.getNombreServidor();
				java.io.File temp = new java.io.File(nuevaUrl);
				temp.delete();
			}
			// FIN TICKET
			log.info("finalizo el envio de notificaciones por ScheduledTasks");
		}
		log.info("[FIN] ScheduledTasksNotificacionNoEnviada->enviarNotificacionNoEnviadas");
	}
}
