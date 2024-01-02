package pe.com.petroperu.notificacion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import pe.com.petroperu.ArchivoCorrespondenciaEnviar;
import pe.com.petroperu.HeaderFooterPageEvent;
import pe.com.petroperu.PDFTool;
import pe.com.petroperu.Respuesta;
import pe.com.petroperu.cliente.ISistcorrCliente;
import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.model.Rol;
import pe.com.petroperu.model.UsuarioPetroperu;
import pe.com.petroperu.model.UsuarioRol;
import pe.com.petroperu.model.emision.ArchivoAdjunto;
import pe.com.petroperu.model.emision.ArchivoCompartido;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.CorrespondenciaCompartida;
import pe.com.petroperu.model.emision.DestinatarioExterno;
import pe.com.petroperu.model.emision.DestinatarioInterno;
import pe.com.petroperu.model.emision.Firmante;
import pe.com.petroperu.model.emision.HistorialArchivo;
import pe.com.petroperu.model.emision.MotivoRechazo;
import pe.com.petroperu.model.emision.NotificacionNoEnviada;
import pe.com.petroperu.model.emision.Parametro;
import pe.com.petroperu.sistcorr.dao.IArchivoAdjuntoDAO;
import pe.com.petroperu.sistcorr.dao.IArchivoCompartidoDAO;
import pe.com.petroperu.sistcorr.dao.ICorrespondenciaCompartidaDAO;
import pe.com.petroperu.sistcorr.dao.IFirmarteDAO;
import pe.com.petroperu.sistcorr.dao.IHistorialArchivoDAO;
import pe.com.petroperu.sistcorr.dao.IMotivoRechazoDAO;
import pe.com.petroperu.sistcorr.dao.INotificacionNoEnviadaDAO;
import pe.com.petroperu.sistcorr.dao.IParametroDAO;
import pe.com.petroperu.sistcorr.dao.IRolDAO;
import pe.com.petroperu.sistcorr.dao.IUsuarioDAO;
import pe.com.petroperu.sistcorr.dao.IUsuarioRolDAO;
import pe.com.petroperu.util.Constante;

@Component
@PropertySource({ "classpath:application.properties" })
public class NotificacionService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private IFilenetDAO filenetDAO;

	@Autowired
	private ICorrespondenciaCompartidaDAO correspondenciaCompartidaDAO;
	
	@Autowired
	private IMotivoRechazoDAO motivoRechazoDAO;

	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Autowired
	private IRolDAO rolDAO;

	@Autowired
	private IUsuarioRolDAO usuarioRolDAO;

	@Autowired
	private IFirmarteDAO firmanteDAO;

	@Autowired
	private MessageSource messageSource;

	@Value("${pe.com.petroperu.sistcorr.produccion}")
	private boolean produccion;

	@Value("${sistcorr.email.test}")
	private String emailTo;

	@Value("${sistcorr.url_base}")
	private String url_base;

	@Value("${sistcorr.notificar}")
	private boolean notificar;
	
	@Value("${sistcorr.directorio}")
	private String urlBase;
	
	@Value("${sistcorr.api.url_compartir}")
	private String apiUrlCompartirCorrespondencia;
	
	@Autowired
	private IParametroDAO parametroDAO;
	
	@Autowired
	private INotificacionNoEnviadaDAO notificacionNoEnviadaDAO;
	
	@Autowired
	private ISistcorrCliente sistcorrCliente;

	private final DateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	// 9000003791
	private final String DIRECTORIO_COMPARTIDOS = "compartidos";
	@Value("${sistcorr.directorio}")
	private String DIRECCTORIO_BASE;
	@Value("${sistcorr.api.url_compartir}")
	private String URL_COMPARTIR;
	@Autowired
	private IArchivoCompartidoDAO archivoCompartidoDAO;
	@Autowired
	private IArchivoAdjuntoDAO archivoAdjuntoDAO;
	@Autowired
	private IHistorialArchivoDAO historialArchivoDAO;

	private String obtenerEmail(String usuario) {
		return this.filenetDAO.obtenerEmailFuncionario(usuario);
	}
	
	//TICKET 9000004411
	private UsuarioPetroperu obtenerUsuario() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
			return (UsuarioPetroperu) auth.getPrincipal();
		}
		return null;
	}

	private boolean enviarNotificacion(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacion");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template", (IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			mail.setFrom("sistcorr@petroperu.com.pe");//TICKET 9000004411
			this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacion", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacion " + respuesta);
		return respuesta;
	}
	
	//TICKET 9000004411
	public boolean enviarNotificacionScheduledTasks(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionScheduledTasks");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			
			String html = mail.getHtmlBody();
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			if(mail.getArchivosDirStr() != null && !mail.getArchivosDirStr().equalsIgnoreCase("")) {
				String[] archivosAdj = mail.getArchivosDirStr().split("\\;");
				String[] nomArchivosAdj = mail.getNombreArcStr().split("\\;");
				int idx = 0;
				for(String arc : archivosAdj) {
					File f = new File(arc);
					if(f.exists())
						helper.addAttachment(nomArchivosAdj[idx], f);
					idx++;
				}
			}
			
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getFrom());
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacionScheduledTasks", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionScheduledTasks " + respuesta);
		return respuesta;
	}
	
	//TICKET 9000004411
	private void guardarNotificacionNoEnviada(Mail mail) {
		
		NotificacionNoEnviada entity = new NotificacionNoEnviada();
		UsuarioPetroperu usuario = obtenerUsuario();
		entity.setAsunto(mail.getSubject());
		if (mail.getCopies() != null && (mail.getCopies()).length > 0)
			entity.setCopiaCc(StringUtils.arrayToCommaDelimitedString(mail.getCopies()));
		if(mail.getArchivosDirStr() != null && !mail.getArchivosDirStr().equalsIgnoreCase(""))
			entity.setNombreServidor(mail.getArchivosDirStr());
		if(mail.getNombreArcStr() != null && !mail.getNombreArcStr().equalsIgnoreCase(""))
			entity.setNombreArc(mail.getNombreArcStr());
		entity.setCuerpo(mail.getHtmlBody());
		entity.setDestinatarios(mail.getTo());
		entity.setDe(mail.getFrom());
		entity.setEstado('N');
		entity.setFechaCrea(new Date());
		entity.setUsuarioCrea(usuario.getUsername());
		
		notificacionNoEnviadaDAO.save(entity);
	}
	
	private boolean enviarNotificacionEnvioCorrespondencia(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionEnvioCorrespondencia");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-envio", (IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			mail.setFrom("sistcorr@petroperu.com.pe");//TICKET 9000004411
			this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacionEnvioCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionEnvioCorrespondencia " + respuesta);
		return respuesta;
	}
	
	private boolean enviarNotificacionEnvioCorrespondenciaExterno(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionEnvioCorrespondenciaExterno");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-envio-externo", (IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			mail.setFrom("sistcorr@petroperu.com.pe");//TICKET 9000004411
			this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacionEnvioCorrespondenciaExterno", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionEnvioCorrespondenciaExterno " + respuesta);
		return respuesta;
	}
	
	// TICKET 9000003934
	/*@SuppressWarnings("unchecked")
	private boolean enviarNotificacionDestinatarioExternoPorCorreoDestinatario(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionDestinatarioExternoPorCorreoDestinatario");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			String archivosDirStr = "", nomArchivosStr = "";//TICKET 9000004411
			List<ArchivoCorrespondenciaEnviar> listaArchivos = (List<ArchivoCorrespondenciaEnviar>) mail.getModel()
					.get("archivos");
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-envio-destinatario-externo-correo-destino",
					(IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getFrom()); // edicion 9-3874
			for (ArchivoCorrespondenciaEnviar ar : listaArchivos) {

				if (ar.isEsEnviarComoAdjunto()) {
					String urlBase = DIRECCTORIO_BASE;
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + ar.getNombreDocServidor();
					
					if(archivosDirStr.equalsIgnoreCase("")) archivosDirStr = archivosDirStr  + nuevaUrl;//TICKET 9000004411
					else archivosDirStr = archivosDirStr + ";"  + nuevaUrl;//TICKET 9000004411
					
					if(nomArchivosStr.equalsIgnoreCase("")) nomArchivosStr = nomArchivosStr  + ar.getNombre();//TICKET 9000004411
					else nomArchivosStr = nomArchivosStr + ";"  + ar.getNombre();//TICKET 9000004411
					
					LOGGER.info("Nueva Url:" + nuevaUrl);
					File f = new File(nuevaUrl);

					helper.addAttachment(ar.getNombre(), f);
				}
			}
			if(!archivosDirStr.equalsIgnoreCase(""))//TICKET 9000004411
				mail.setArchivosDirStr(archivosDirStr);//TICKET 9000004411
			if(!nomArchivosStr.equalsIgnoreCase(""))//TICKET 9000004411
				mail.setNombreArcStr(nomArchivosStr);//TICKET 9000004411
			
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			if(e.getMessage() != null && e.getMessage().trim().indexOf("Exception reading response") == -1)//TICKET 9000004411
				this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacionDestinatarioExternoPorCorreoDestinatario", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionDestinatarioExternoPorCorreoDestinatario " + respuesta);
		return respuesta;
	}*/
	
	@SuppressWarnings("unchecked")
	private boolean enviarNotificacionDestinatarioExternoPorCorreoDestinatario(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionDestinatarioExternoPorCorreoDestinatario");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			Context context = new Context();
			List<ArchivoCorrespondenciaEnviar> listaArchivos = (List<ArchivoCorrespondenciaEnviar>) mail.getModel()
					.get("archivos");
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-envio-destinatario-externo-correo-destino",
					(IContext) context);
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo())); 
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				for(String cc : mail.getCopies()){
					if(!cc.trim().equalsIgnoreCase("")){
						message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); 
					}
				}
			}
			
			message.setSubject(mail.getSubject());
			message.setFrom(new InternetAddress(mail.getFrom()));
			
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setContent(html, "text/html; charset=utf-8");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for(ArchivoCorrespondenciaEnviar ar : listaArchivos){
				
				if (ar.isEsEnviarComoAdjunto()) {
					String urlBase = DIRECCTORIO_BASE;
					String urlCarpetaArchivos = "adjuntos";
					
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + ar.getNombreDocServidor();
					
					File fileAdj = new File(nuevaUrl);
					
					MimeBodyPart attachmentPart = new MimeBodyPart();
					
				    attachmentPart.attachFile(fileAdj);
				    attachmentPart.setFileName(ar.getNombre());
					multipart.addBodyPart(attachmentPart);
				}
			}
			message.setContent(multipart);
			if(this.emailSender==null){
				LOGGER.info("EmailSender null");
			}
			
			this.emailSender.send(message);
			
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacionDestinatarioExternoPorCorreoDestinatario", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionDestinatarioExternoPorCorreoDestinatario " + respuesta);
		return respuesta;
	}
	
	//inicio ticket 9000004765
	@SuppressWarnings("unchecked")
	private boolean enviarNotificacionDestCorreoDocPagar(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionDestCorreoDocPagar");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			Context context = new Context();
			List<ArchivoCorrespondenciaEnviar> listaArchivos = (List<ArchivoCorrespondenciaEnviar>) mail.getModel()
					.get("archivos");
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-envio-dest-doc-pagar",
					(IContext) context);
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo())); 
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				for(String cc : mail.getCopies()){
					if(!cc.trim().equalsIgnoreCase("")){
						message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); 
					}
				}
			}
			
			message.setSubject(mail.getSubject());
			message.setFrom(new InternetAddress(mail.getFrom()));
			
			BodyPart messageBodyPart = new MimeBodyPart(); 
			mail.setHtmlBody(html);
			messageBodyPart.setContent(html, "text/html; charset=utf-8");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for(ArchivoCorrespondenciaEnviar ar : listaArchivos){
				
				if (ar.isEsEnviarComoAdjunto()) {
					String urlBase = DIRECCTORIO_BASE;
					String urlCarpetaArchivos = "adjuntos";
					
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + ar.getNombreDocServidor();
					
					File fileAdj = new File(nuevaUrl);
					
					MimeBodyPart attachmentPart = new MimeBodyPart();
					
				    attachmentPart.attachFile(fileAdj);
				    attachmentPart.setFileName(ar.getNombre());
					multipart.addBodyPart(attachmentPart);
				}
			}
			message.setContent(multipart);
			if(this.emailSender==null){
				LOGGER.info("EmailSender null");
			}
			
			this.emailSender.send(message);
			
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.guardarNotificacionNoEnviada(mail);
			this.LOGGER.error("[ERROR] enviarNotificacionDestCorreoDocPagar", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionDestCorreoDocPagar " + respuesta);
		return respuesta;
	}
	//fin ticket 9000004765
		
	private boolean enviarNotificacionAprobacion(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionAprobacion");//TICKET 9000004411 enviarNotificacion to enviarNotificacionAprobacion
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-aprobacion", (IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			mail.setFrom("sistcorr@petroperu.com.pe");//TICKET 9000004411
			this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacion", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionAprobacion " + respuesta);//TICKET 9000004411 enviarNotificacion to enviarNotificacionAprobacion
		return respuesta;
	}
	
	private boolean enviarNotificacionExterna(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionExterna");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-externo", (IContext) context);
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			//helper.setFrom("administrador@petroperu.com.pe");
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacion", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacion " + respuesta);
		return respuesta;
	}

	private String[] obtenerUsuarioExterno(Correspondencia correspondencia) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] obtenerUsuarioExterno");
		String[] rucEmail = this.filenetDAO.obtenerRUCEmailDependenciaExterna(
				((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodDependenciaNacional());
		LOGGER.info("obtenerUsuarioExterno:" + rucEmail[0] + "||" + rucEmail[1]);
		UsuarioPetroperu usuario = this.usuarioDAO.findOneByUsername(rucEmail[0]);
		/*if (usuario == null) {
			long startTime = Instant.now().toEpochMilli();

			usuario = new UsuarioPetroperu(startTime, rucEmail[0], startTime + "", true, rucEmail[1],
					((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).getCodDependenciaNacional(),
					"EXTERNO");

			usuario.setUsuarioCrea("MOD_NOTIFICACION");
			usuario.setFechaCrea(new Date());
			usuario = (UsuarioPetroperu) this.usuarioDAO.save(usuario);
			Rol rol = (Rol) this.rolDAO.findOne(Constante.ROL_EXTERNO);
			UsuarioRol ur = new UsuarioRol();
			ur.setFechaCrea(new Date());
			ur.setUsuarioCrea("MOD_NOTIFICACION");
			ur.setUsuario(usuario);
			ur.setRol(rol);
			this.usuarioRolDAO.save(ur);
		}*/
		if(usuario==null){
			//return new String[] { "", "", "" };
			return new String[]{rucEmail[0], rucEmail[1]};
		}
		// return new String[] { usuario.getUsername(), usuario.getPassword(),
		// usuario.getEmail() };
		// TICKET 9000003992
		this.LOGGER.info("[FIN] obtenerUsuarioExterno");
		return new String[] { rucEmail[0], rucEmail[1] };
	}

	public boolean notificarAsignacionParaFirmar(Correspondencia correspondencia, Firmante firmante, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarAsignacionParaFirmar");
		String[] ccopias = { obtenerEmail(firmante.getSolicitante()), obtenerEmail(correspondencia.getResponsable())  };
		Mail mail = new Mail();
		mail.setFrom("aaaaa@gmail.com");
		mail.setTo(obtenerEmail(firmante.getCodFirmante()));
		// TICKET 9000003874
		//mail.setCopies(ccopias);
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.asignar_firma.asunto", null, locale), parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.asignar_firma.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(firmante.getFecha()));
		mail.getModel().put("solicitante", firmante.getSolicitante());
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/firmados?correspondencia="
				+ correspondencia.getCorrelativo().getCodigo());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarAsignacionParaFirmar");
		return enviarNotificacion(mail);
	}

	public boolean notificarEnvioCorrespondencia(Correspondencia correspondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarEnvioCorrespondencia");
		Mail mail = new Mail();
		String dependencia_destino = "";
		int c = 0;
		for (DestinatarioInterno di : correspondencia.getDetalleInterno()) {
			dependencia_destino = di.getDependencia() + " / " + dependencia_destino;
		}
		mail.setFrom("xxx@gmail.com");
		mail.setTo(this.filenetDAO.obtenerEmailFuncionario(correspondencia.getResponsable()));
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", "");
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		//mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/enviados?correspondencia="
		//		+ correspondencia.getCorrelativo().getCodigo());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarEnvioCorrespondencia");
		return enviarNotificacionEnvioCorrespondencia(mail);
	}
	
	public boolean notificarEnvioCorrespondenciaExterno(Correspondencia correspondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarEnvioCorrespondenciaExterno");
		Mail mail = new Mail();
		String dependencia_destino = "";
		int c = 0;
		for (DestinatarioInterno di : correspondencia.getDetalleInterno()) {
			dependencia_destino = di.getDependencia() + " / " + dependencia_destino;
		}
		mail.setFrom("xxx@gmail.com");
		mail.setTo(this.filenetDAO.obtenerEmailFuncionario(correspondencia.getResponsable()));
		LOGGER.info("Enviado a:" + this.filenetDAO.obtenerEmailFuncionario(correspondencia.getResponsable()));
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia_externo.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia_externo.mensaje", null, locale));
		mail.getModel().put("mensaje_nota1",
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia_externo_nota1.mensaje", null, locale));
		mail.getModel().put("mensaje_nota2",
				this.messageSource.getMessage("sistcorr.notificar.envio_correspondencia_externo_nota2.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("dependenciasDestExtDet", getDependenciasDestinatarioExterno(correspondencia));//TICKET 9000003934
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", "");
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		// mail.getModel().put("url_documento", this.url_base +
		// "/app/lista-documentos/enviados?correspondencia="
		//		+ correspondencia.getCorrelativo().getCodigo());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarEnvioCorrespondenciaExterno");
		return enviarNotificacionEnvioCorrespondenciaExterno(mail);
	}
	

	//TICKET 9000003934
	public boolean notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico(Correspondencia correspondencia, String nombreDestinatario, String correoDestinatario, String usuarioModifica, Locale locale) {
		Mail mail = new Mail();
		boolean esEnviado = false;
		List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
		String emailUltimoFirmante = "", nombreUltimoFirmante = "";
		
		if(firmantes != null && firmantes.size() > 0) {
			emailUltimoFirmante = firmantes.get(firmantes.size() - 1).getCodFirmante() + "@petroperu.com.pe";//obtenerEmail(firmantes.get(firmantes.size() - 1).getCodFirmante());
			nombreUltimoFirmante = firmantes.get(firmantes.size() - 1).getNombreFirmante();
		}
		List<ArchivoCorrespondenciaEnviar> archivos = null;
		List<ArchivoCorrespondenciaEnviar> archivosEnviarDireccion = null;
		String[] ccopias = new String[1];
		/*this.LOGGER.info("notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico correspondencia -> getId: " + correspondencia.getId());
		this.LOGGER.info("notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico Destinatario -> emailUltimoFirmante: " + emailUltimoFirmante);
		this.LOGGER.info("notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico Destinatario -> nombreDestinatario: " + nombreDestinatario);
		this.LOGGER.info("notificarEnvioCorrespondenciaDestinatarioExternoPorCorreoElectronico Destinatario -> correoDestinatario: " + correoDestinatario);*/
		if(emailUltimoFirmante != null && !emailUltimoFirmante.equalsIgnoreCase("") && 
				(nombreDestinatario != null && !nombreDestinatario.equalsIgnoreCase("") &&
				(correoDestinatario != null && !correoDestinatario.equalsIgnoreCase("")))) {
			
			mail.setFrom(emailUltimoFirmante);
			ccopias[0] = obtenerEmail(correspondencia.getResponsable());
			mail.setCopies(ccopias);
			mail.setTo(correoDestinatario);
			mail.setSubject("" + correspondencia.getAsunto() + " - [" + correspondencia.getCorrelativo().getCodigo() + "]");
			archivos = this.buildArchivosEnviarDestinatarioExternoPorCorreoDestino(correspondencia);
			archivosEnviarDireccion = archivos.stream().filter(arc -> { return arc.isExisteAchivoEnviarComoDireccion();}).collect(Collectors.toList());
			
			mail.getModel().put("archivos", archivos);
			mail.getModel().put("nombreUltimoFirmante", nombreUltimoFirmante);
			mail.getModel().put("nombreDestinatario", nombreDestinatario);
			mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
			mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
			mail.getModel().put("asunto", correspondencia.getAsunto());
			mail.getModel().put("existeAchivoEnviarComoDireccion", (archivosEnviarDireccion != null && archivosEnviarDireccion.size() > 0));
			mail.getModel().put("linkAppCorrespondencia", apiUrlCompartirCorrespondencia);
			mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
			
			esEnviado = enviarNotificacionDestinatarioExternoPorCorreoDestinatario(mail);
		}
		
		if(esEnviado) {
			guardarHistorialCompartidoDestinatarioExternoPorCorreoElectronico(correspondencia, archivos, correoDestinatario, (archivosEnviarDireccion != null && archivosEnviarDireccion.size() > 0), usuarioModifica, nombreDestinatario, ccopias);
		}
		
		return esEnviado;
	}
	
	private void guardarHistorialCompartidoDestinatarioExternoPorCorreoElectronico(Correspondencia correspondencia, List<ArchivoCorrespondenciaEnviar> archivos, String correoDestinatario, boolean existArchivoEnviarComoDireccion, String usuarioModifica, String nombreDestinatario, String ...ccopias) {
		
		CorrespondenciaCompartida corresComp = new CorrespondenciaCompartida();
		String strCc = "";
		if(ccopias.length > 0)
			for (String copia : ccopias) {
				if(strCc.equalsIgnoreCase(""))
					strCc+=copia;
				else
					strCc+=";"+copia;
			}
		
		corresComp.setAsunto(correspondencia.getAsunto());//Por este del correo le hacemos TICKET 7000004032
		corresComp.setContenido("Estimado(a) " + nombreDestinatario + "\n\n" + 
								"Por este mensaje de correo le hacemos llegar la siguiente correspondencia:\n\n" + 
								"Asunto: " + correspondencia.getAsunto() + "\n" +
								"Nro. Documento: " + correspondencia.getCorrelativo().getCodigo());
		corresComp.setDestinatarios(correoDestinatario);
		corresComp.setCopias(strCc);
		corresComp.setModoCompartido((existArchivoEnviarComoDireccion)?("Dirección"):("Adjunto"));
		corresComp.setCorrespondencia(correspondencia);
		corresComp.setUsuarioCrea(usuarioModifica);
		corresComp.setFechaCrea(new Date());
		
		corresComp = correspondenciaCompartidaDAO.save(corresComp);
		
		if(corresComp != null && corresComp.getId() > 0)
		for(ArchivoAdjunto adj : correspondencia.getAdjuntos()){
			if(adj.isPrincipal()) {
				LOGGER.info("Id Arc Adj:" + adj.getId());
				ArchivoAdjunto arcAdj = archivoAdjuntoDAO.findOne(adj.getId());
				ArchivoCompartido arc = archivoCompartidoDAO.findOneByArchivo(arcAdj);
				
				if(!arc.isCompartido()){
					arc.setCompartido(true);
					archivoCompartidoDAO.save(arc);
				}
				HistorialArchivo hist = new HistorialArchivo();
				hist.setCorrespondenciaCompartida(corresComp);
				hist.setArchivoCompartido(arc);
				hist.setUsuarioCrea(usuarioModifica);
				hist.setFechaCrea(new Date());
				historialArchivoDAO.save(hist);
			}
		}
	}
	
	//inicio ticket 9000004765
	private void guardarHistorialCompartidoDestDocPagar(Correspondencia correspondencia, List<ArchivoCorrespondenciaEnviar> archivos, String correoDestinatario, String nombreUltimoFirmante, String ...ccopias) {
		
		CorrespondenciaCompartida corresComp = new CorrespondenciaCompartida();
		String strCc = "";
		if(ccopias.length > 0)
			for (String copia : ccopias) {
				if(strCc.equalsIgnoreCase(""))
					strCc+=copia;
				else
					strCc+=";"+copia;
			}
		
		corresComp.setAsunto(correspondencia.getAsunto() + " - [" + correspondencia.getCorrelativo().getCodigo()+"]");
		corresComp.setContenido("Estimado(s) " + "\n\n" + 
								"Mediante el presente correo le hacemos llegar la siguiente correspondencia:\n\n" +
								"Asunto: " + correspondencia.getAsunto() + "\n" +
								"Nro. Documento: " + correspondencia.getCorrelativo().getCodigo()+"\n\n"+
								"A continuación, se detallan los documentos que se han compartido:\n\n" +
								"Link de Acceso:\n"+
								"URL: "+apiUrlCompartirCorrespondencia+"\n\n"+
								"Nombre: " + archivos.get(0).getNombre()+"  "+"Clave: "+archivos.get(0).getClaveArcCompartido()+"  "+"Correlativo: "+archivos.get(0).getNumeroDocumento()+"\n\n"+
								"Por favor confirmar la recepción del correo."+"\n\n"+
								"Atentamente."+"\n"+
								nombreUltimoFirmante);
		corresComp.setDestinatarios(correoDestinatario);
		corresComp.setCopias(strCc);
		corresComp.setModoCompartido("Adjunto");
		corresComp.setCorrespondencia(correspondencia);
		corresComp.setUsuarioCrea(this.obtenerUsuario().getUsername());
		corresComp.setFechaCrea(new Date());
		
		corresComp = correspondenciaCompartidaDAO.save(corresComp);
		
		if(corresComp != null && corresComp.getId() > 0)
		for(ArchivoAdjunto adj : correspondencia.getAdjuntos()){
			if(adj.isPrincipal()) {
				LOGGER.info("Id Arc Adj:" + adj.getId());
				ArchivoAdjunto arcAdj = archivoAdjuntoDAO.findOne(adj.getId());
				ArchivoCompartido arc = archivoCompartidoDAO.findOneByArchivo(arcAdj);
				
				if(!arc.isCompartido()){
					arc.setCompartido(true);
					archivoCompartidoDAO.save(arc);
				}
				HistorialArchivo hist = new HistorialArchivo();
				hist.setCorrespondenciaCompartida(corresComp);
				hist.setArchivoCompartido(arc);
				hist.setUsuarioCrea(this.obtenerUsuario().getUsername());
				hist.setFechaCrea(new Date());
				historialArchivoDAO.save(hist);
			}
		}
	}
	//fin ticket 9000004765
	
	private List<ArchivoCorrespondenciaEnviar> buildArchivosEnviarDestinatarioExternoPorCorreoDestino(Correspondencia correspondencia){
		
		List<ArchivoCorrespondenciaEnviar> listArchivos = new ArrayList<ArchivoCorrespondenciaEnviar>();
		List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
		Long maxFileDefault = new Long(1);
		
		if(listParametro != null && listParametro.size() > 0) 
			maxFileDefault = listParametro.get(0).getValor();
		
		for(ArchivoAdjunto arcAdj : correspondencia.getAdjuntos()) {
			ArchivoCorrespondenciaEnviar archivo = new ArchivoCorrespondenciaEnviar();
			archivo.setNombreDocServidor(arcAdj.getNombreServidor());
			archivo.setExisteAchivoEnviarComoDireccion(false);
			if((arcAdj.isPrincipal() && (Double.compare(arcAdj.getTamanio(), maxFileDefault) < 0 || Double.compare(arcAdj.getTamanio(), maxFileDefault) == 0))) {//si el archivo es firma digital y es <= 1MB
				
				archivo.setEsEnviarComoAdjunto(true);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setUbicacionArchivo(arcAdj.getUbicacion());
				// TICKET 7000004052
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(arcAdj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + arcAdj.getNombreServidor();
					List<Parametro> usuario = parametroDAO.findByGrupo("USER_FILENET");
					List<Parametro> password = parametroDAO.findByGrupo("PASS_FILENET");
					Respuesta<String> respuestaToken = this.sistcorrCliente.recuperarToken(usuario.get(0).getDenominacion(), password.get(0).getDenominacion());
					ResponseEntity<byte[]> archivoDescargado =  sistcorrCliente.descargarDocumentoServidor(respuestaToken.datos.get(0), arcAdj.getNombreServidor());
					try{
						org.apache.commons.io.FileUtils.writeByteArrayToFile(new java.io.File(nuevaUrl), archivoDescargado.getBody());
					}catch(Exception e){
						LOGGER.error("[ERROR]", e);
					}
				}
				// FIN TICKET
			}else if(arcAdj.isPrincipal()) {//si el archivo es firma digital y es > 1MB
				
				archivo.setExisteAchivoEnviarComoDireccion(true);
				archivo.setEsEnviarComoAdjunto(false);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setClaveArcCompartido((arcAdj.getArchivoCompartido() != null && arcAdj.getArchivoCompartido().size() > 0)?(arcAdj.getArchivoCompartido().get(0).getClave()):(""));
				archivo.setNumeroDocumento(correspondencia.getCorrelativo().getCodigo());
			}else { 
				archivo.setEsEnviarComoAdjunto(true);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setUbicacionArchivo(arcAdj.getUbicacion());
				// TICKET 7000004067
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(arcAdj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + arcAdj.getNombreServidor();
					List<Parametro> usuario = parametroDAO.findByGrupo("USER_FILENET");
					List<Parametro> password = parametroDAO.findByGrupo("PASS_FILENET");
					Respuesta<String> respuestaToken = this.sistcorrCliente.recuperarToken(usuario.get(0).getDenominacion(), password.get(0).getDenominacion());
					ResponseEntity<byte[]> archivoDescargado =  sistcorrCliente.descargarDocumentoServidor(respuestaToken.datos.get(0), arcAdj.getNombreServidor());
					try{
						org.apache.commons.io.FileUtils.writeByteArrayToFile(new java.io.File(nuevaUrl), archivoDescargado.getBody());
					}catch(Exception e){
						LOGGER.error("[ERROR]", e);
					}
				}
				// FIN TICKET
			}/*else if((!arcAdj.isPrincipal() && (Double.compare(arcAdj.getTamanio(), 1.0) < 0 || Double.compare(arcAdj.getTamanio(), 1.0) == 0))) {//si el archivo no es firma digital y es <= 1MB
				
				archivo.setEsEnviarComoAdjunto(true);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setUbicacionArchivo(arcAdj.getUbicacion());
			}else {//si el archivo no es firma digital y es > 1MB
				
				archivo.setExisteAchivoEnviarComoDireccion(true);
				archivo.setEsEnviarComoAdjunto(false);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setClaveArcCompartido((arcAdj.getArchivoCompartido() != null && arcAdj.getArchivoCompartido().size() > 0)?(arcAdj.getArchivoCompartido().get(0).getClave()):(""));
				archivo.setNumeroDocumento(arcAdj.getFileNetID());
			}*/
			
			listArchivos.add(archivo);
		}
		
		return listArchivos;
	}
	
	
	private List<ArchivoCorrespondenciaEnviar> buildArchivosEnviarDestinatarioDocPagar(Correspondencia correspondencia){
		
		List<ArchivoCorrespondenciaEnviar> listArchivos = new ArrayList<ArchivoCorrespondenciaEnviar>();
		List<Parametro> listParametro = parametroDAO.findByGrupoAndDenominacion("CARGAR_ARCHIVO", "SIN_FIRMA_CORRES_EXTER_MAX_ARCHIVO");
		Long maxFileDefault = new Long(1);
		
		if(listParametro != null && listParametro.size() > 0) 
			maxFileDefault = listParametro.get(0).getValor();
		
		for(ArchivoAdjunto arcAdj : correspondencia.getAdjuntos()) {
			ArchivoCorrespondenciaEnviar archivo = new ArchivoCorrespondenciaEnviar();
			archivo.setNombreDocServidor(arcAdj.getNombreServidor());
			archivo.setExisteAchivoEnviarComoDireccion(false);
			if((arcAdj.isPrincipal() && (Double.compare(arcAdj.getTamanio(), maxFileDefault) < 0 || Double.compare(arcAdj.getTamanio(), maxFileDefault) == 0))) {
				
				archivo.setEsEnviarComoAdjunto(true);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setUbicacionArchivo(arcAdj.getUbicacion());
				archivo.setClaveArcCompartido((arcAdj.getArchivoCompartido() != null && arcAdj.getArchivoCompartido().size() > 0)?(arcAdj.getArchivoCompartido().get(0).getClave()):(""));
				archivo.setNumeroDocumento(correspondencia.getCorrelativo().getCodigo());
				
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(arcAdj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + arcAdj.getNombreServidor();
					List<Parametro> usuario = parametroDAO.findByGrupo("USER_FILENET");
					List<Parametro> password = parametroDAO.findByGrupo("PASS_FILENET");
					Respuesta<String> respuestaToken = this.sistcorrCliente.recuperarToken(usuario.get(0).getDenominacion(), password.get(0).getDenominacion());
					ResponseEntity<byte[]> archivoDescargado =  sistcorrCliente.descargarDocumentoServidor(respuestaToken.datos.get(0), arcAdj.getNombreServidor());
					try{
						org.apache.commons.io.FileUtils.writeByteArrayToFile(new java.io.File(nuevaUrl), archivoDescargado.getBody());
					}catch(Exception e){
						LOGGER.error("[ERROR]", e);
					}
				}
			}else if(arcAdj.isPrincipal()) {
				
				archivo.setExisteAchivoEnviarComoDireccion(true);
				archivo.setEsEnviarComoAdjunto(false);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setClaveArcCompartido((arcAdj.getArchivoCompartido() != null && arcAdj.getArchivoCompartido().size() > 0)?(arcAdj.getArchivoCompartido().get(0).getClave()):(""));
				archivo.setNumeroDocumento(correspondencia.getCorrelativo().getCodigo());
			}else { 
				archivo.setEsEnviarComoAdjunto(true);
				archivo.setNombre(arcAdj.getNombre());
				archivo.setUbicacionArchivo(arcAdj.getUbicacion());
				archivo.setClaveArcCompartido((arcAdj.getArchivoCompartido() != null && arcAdj.getArchivoCompartido().size() > 0)?(arcAdj.getArchivoCompartido().get(0).getClave()):(""));
				archivo.setNumeroDocumento(correspondencia.getCorrelativo().getCodigo());
				// TICKET 7000004067
				if(Constante.INDICADOR_REMOTO_ARCHIVO_ADJUNTO.equalsIgnoreCase(arcAdj.getIndicadorRemoto())){
					String urlCarpetaArchivos = "adjuntos";
					String nuevaUrl = urlBase + urlCarpetaArchivos + "/" + arcAdj.getNombreServidor();
					List<Parametro> usuario = parametroDAO.findByGrupo("USER_FILENET");
					List<Parametro> password = parametroDAO.findByGrupo("PASS_FILENET");
					Respuesta<String> respuestaToken = this.sistcorrCliente.recuperarToken(usuario.get(0).getDenominacion(), password.get(0).getDenominacion());
					ResponseEntity<byte[]> archivoDescargado =  sistcorrCliente.descargarDocumentoServidor(respuestaToken.datos.get(0), arcAdj.getNombreServidor());
					try{
						org.apache.commons.io.FileUtils.writeByteArrayToFile(new java.io.File(nuevaUrl), archivoDescargado.getBody());
					}catch(Exception e){
						LOGGER.error("[ERROR]", e);
					}
				}
			}
			
			listArchivos.add(archivo);
		}
		
		return listArchivos;
	}

	public boolean notificarEmisionFirmaDigital(Correspondencia correspondencia, Firmante firmante, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarEmisionFirmaDigital");
		// String[] ccopias = { obtenerEmail(firmante.getCodFirmante()) };
		String[] ccopias = { obtenerEmail(correspondencia.getResponsable()) };
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(firmante.getSolicitante()));
		mail.setCopies(ccopias);
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.emision_firma.asunto", null, locale), parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.emision_firma.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(firmante.getFecha()));
		mail.getModel().put("solicitante", firmante.getSolicitante());
		mail.getModel().put("firmante", firmante.getCodFirmante());
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", firmante.getUsuarioModifica());
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("url_documento", this.url_base + "/app/documentos-firmados/" + correspondencia.getId());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarEmisionFirmaDigital");
		return enviarNotificacion(mail);
	}
	
	public boolean notificarEmisionAprobacionFirmaDigital(Correspondencia correspondencia, Firmante firmante,
			Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarEmisionAprobacionFirmaDigital");
		// String[] ccopias = { obtenerEmail(firmante.getCodFirmante()) };
		String[] ccopias = { obtenerEmail(correspondencia.getResponsable()) };
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(firmante.getSolicitante()));
		mail.setCopies(ccopias);
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.emision_aprobacion_firma.asunto", null, locale), parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.emision_aprobacion_firma.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(firmante.getFecha()));
		mail.getModel().put("solicitante", firmante.getSolicitante());
		mail.getModel().put("firmante", firmante.getCodFirmante());
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("url_documento", this.url_base + "/app/documentos-firmados/" + correspondencia.getId());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarEmisionAprobacionFirmaDigital");
		return enviarNotificacionAprobacion(mail);
	}
	
	//inicio ticket 9000004765
	public boolean notificarEmisionAprobacionFirmaDigitalTipoCorresponDocPagar(Correspondencia correspondencia, Firmante firmante,
			Locale locale) {
		
		this.LOGGER.info("[INICIO] notificarEmisionAprobacionFirmaDigitalTipoCorresponDocPagar");
		
		String[] ccopias = { obtenerEmail(correspondencia.getResponsable()) };
		Mail mail = new Mail();
		String correoDestDocPagar = "";
		if(correspondencia.getDetalleCorrespDestDocPagar() != null && 
				correspondencia.getDetalleCorrespDestDocPagar().size() > 0) {
			correoDestDocPagar = correspondencia.getDetalleCorrespDestDocPagar().get(0).getDestinarioDocPagar().getCorreo();
		}
		mail.setFrom(obtenerEmail(firmante.getCodFirmante()));
		mail.setTo(correoDestDocPagar);
		mail.setCopies(ccopias);
		
		mail.setSubject("" + correspondencia.getAsunto() + " - [" + correspondencia.getCorrelativo().getCodigo() + "]");
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		
		List<ArchivoCorrespondenciaEnviar> archivos = null;
		archivos = this.buildArchivosEnviarDestinatarioDocPagar(correspondencia);
		
		mail.getModel().put("archivos", archivos);
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(firmante.getFecha()));
		mail.getModel().put("solicitante", firmante.getSolicitante());
		mail.getModel().put("firmante", firmante.getCodFirmante());
		mail.getModel().put("linkAppCorrespondencia", apiUrlCompartirCorrespondencia);
		mail.getModel().put("url_documento", this.url_base + "/app/documentos-firmados/" + correspondencia.getId());
		mail.getModel().put("nombreUltimoFirmante", firmante.getNombreFirmante());
		
		this.LOGGER.info("[FIN] notificarEmisionAprobacionFirmaDigitalTipoCorresponDocPagar");
		boolean esEnviado = enviarNotificacionDestCorreoDocPagar(mail);
		
		if(esEnviado) guardarHistorialCompartidoDestDocPagar(correspondencia, archivos, correoDestDocPagar, firmante.getNombreFirmante(), ccopias);
		
		return esEnviado;
	}
	//fin ticket 9000004765

	public boolean notificarRechazoSolicutdFirma(Correspondencia correspondencia, Firmante firmante, Long motivoRechazo,
			Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarRechazoSolicutdFirma");
		MotivoRechazo motivo = (MotivoRechazo) this.motivoRechazoDAO.findOne(motivoRechazo);
		//String[] ccopias = {obtenerEmail(firmante.getCodFirmante()), obtenerEmail(firmante.getSolicitante())};
		String[] ccopias = {obtenerEmail(firmante.getSolicitante())};
		//String[] ccopias = { obtenerEmail(firmante.getCodFirmante()) };
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(correspondencia.getResponsable()));
		if(!motivo.isAumentarFlujo()){
			mail.setCopies(ccopias);
		}
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat
				.format(this.messageSource.getMessage("sistcorr.notificar.rechazo.asunto", null, locale), parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.rechazo.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(firmante.getFecha()));
		mail.getModel().put("solicitante", firmante.getSolicitante());
		mail.getModel().put("firmante", firmante.getCodFirmante());
		mail.getModel().put("motivo_rechazo", motivo.getDescripcion());
		mail.getModel().put("descripcion_motivo_rechazo", firmante.getDescripcionMotivoRechazo());
		mail.getModel().put("usuarioModifica", firmante.getUsuarioModifica());
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		if(motivo.isAumentarFlujo()){
			mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/pendientes?correspondencia="
					+ correspondencia.getCorrelativo().getCodigo());
		}else{
			mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/firmados?correspondencia="
					+ correspondencia.getCorrelativo().getCodigo() + "&misPendientes=false");
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarRechazoSolicutdFirma");
		return enviarNotificacion(mail);
	}

	public boolean notificarCorrespondenciaExterna(Correspondencia correspondencia, UsuarioPetroperu usuario,
			Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarCorrespondenciaExterna");
		String[] ccopias = { obtenerEmail(correspondencia.getResponsable()) };
		if (correspondencia.getTipoEmision().getId().intValue() == 2
				&& ((DestinatarioExterno) correspondencia.getDetalleExterno().get(0)).isNacional()) {
			Mail mail = new Mail();
			String[] infoUsuarioExterno = obtenerUsuarioExterno(correspondencia);
			mail.setFrom("xxx@gmail.com");
			
			LOGGER.info("TO:" + infoUsuarioExterno[1]);
			mail.setTo(infoUsuarioExterno[1]);
			//mail.setTo("winston.vargas@ibm.com");
			mail.setCopies(ccopias);
			Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
			mail.setSubject(MessageFormat.format(
					this.messageSource.getMessage("sistcorr.notificar.correspondecia_externa.asunto", null, locale),
					parametros));
			mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
			mail.getModel().put("mensaje",
					this.messageSource.getMessage("sistcorr.notificar.correspondecia_externa.mensaje", null, locale));
			mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
			mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
			mail.getModel().put("copias", "");
			mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
			mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
			mail.getModel().put("asunto", correspondencia.getAsunto());
			mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
			mail.getModel().put("solicitante", "");
			mail.getModel().put("firmante", "");
			mail.getModel().put("motivo_rechazo", "");
			mail.getModel().put("descripcion_motivo_rechazo", "");
			mail.getModel().put("esExterno", Boolean.valueOf(true));
			//mail.getModel().put("usuario_Externo", infoUsuarioExterno[0]);
			//mail.getModel().put("password_Externo", infoUsuarioExterno[1]);
			//mail.getModel().put("url_documento", this.url_base + "/app/documentos-firmados/" + correspondencia.getId());
			
			//MailHistorialDireccion mailH = new MailHistorialDireccion();
			//String[] to = new String[]{infoUsuarioExterno[2]};
			//String[] to = new String[]{infoUsuarioExterno[1], "winston.vargas@ibm.com"};
			//mailH.setFrom("administrador@petroperu.com");
			//mailH.setTo(to);
			//mailH.setCopias(ccopias);
			List<ArchivoCompartido> lista = new ArrayList<>();
			for(ArchivoAdjunto arcAdj : correspondencia.getAdjuntos()){
				if(arcAdj.isPrincipal()){
					ArchivoCompartido arcCom = archivoCompartidoDAO.findOneByArchivo(arcAdj);
					LOGGER.info("Archivo encontrado: " + arcCom);
					/*if(arcCom==null){
						ArchivoCompartido ac = new ArchivoCompartido();
						ac.setClave(generateKey());
						ac.setUsuarioCrea(usuario.getUsername());
						ac.setFechaCrea(new Date());
						ac.setArchivo(arcAdj);
						LOGGER.info("Archivo Adjunto: " + arcAdj.getNombre());
						arcCom = (ArchivoCompartido) archivoCompartidoDAO.save(ac);
						String pathTo = urlBase + DIRECTORIO_COMPARTIDOS + "/" + arcAdj.getNombreServidor();
						PDFTool pdf = new PDFTool(arcAdj.getUbicacion(), pathTo, apiUrlCompartirCorrespondencia, arcCom.getClave(), arcAdj.getCorrespondencia().getCorrelativo().getCodigo());
						pdf.writePDF();
					}*/
					if(!arcCom.isCompartido()){
						arcCom.setCompartido(true);
						archivoCompartidoDAO.save(arcCom);
					}
					lista.add(arcCom);
				}
			}
			mail.getModel().put("url", apiUrlCompartirCorrespondencia);
			mail.getModel().put("archivos", lista);
			
			return enviarNotificacionExterna(mail);
			// TICKET 9000003791
			
			//return enviarNotificacionHistorialDireccion(mail);
			// FIN TICKET 9000003791
		}
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(ccopias[0]);

		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.correspondecia_externa.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.correspondecia_externa.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", "");
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("usuario_Externo", "");
		mail.getModel().put("password_Externo", "");
		mail.getModel().put("url_documento", this.url_base + "/app/documentos-firmados/" + correspondencia.getId());
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarCorrespondenciaExterna");
		return enviarNotificacion(mail);
	}

	public boolean notificarCorrespondenciaReasignada(Correspondencia correspondencia, String nuevoResponsable,
			Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarCorrespondenciaReasignada");
		Mail mail = new Mail();
		String[] ccopias = { obtenerEmail(correspondencia.getResponsable()) };
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(nuevoResponsable));
		mail.setCopies(ccopias);
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.reasignar_correspondencia.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.reasignar_correspondencia.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));

		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", "");
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "Cambio de responsable");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		if (correspondencia.isFirmaDigital()) {
			mail.getModel().put("url_documento", this.url_base + "/app/" + correspondencia.getEstado().getUrl()
					+ "?correspondencia=" + correspondencia.getCorrelativo().getCodigo());
		} else {
			mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/pendientes?correspondencia="
					+ correspondencia.getCorrelativo().getCodigo());
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarCorrespondenciaReasignada");
		return enviarNotificacion(mail);
	}

	public boolean notificarCorrespondenciaDeclinadaPendiente(Correspondencia correspondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarCorrespondenciaDeclinadaPendiente");
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(correspondencia.getResponsable()));
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.declinar_correspondencia.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.declinar_correspondencia.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", correspondencia.getResponsable());
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/pendientes?correspondencia="
				+ correspondencia.getCorrelativo().getCodigo() + "&declinados=true");
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarCorrespondenciaDeclinadaPendiente");
		return enviarNotificacion(mail);
	}

	public boolean notificarCorrespondenciaDeclinadaFirmada(Correspondencia correspondencia, Locale locale) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] notificarCorrespondenciaDeclinadaFirmada");
		List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
		int j = 0;
		for (int i = 0; i < firmantes.size(); i++) {
			Firmante f = (Firmante) firmantes.get(i);
			Long idEstado = f.getEstado().getId();
			if(idEstado==Constante.CORRESPONDENCIA_ASIGNADA || idEstado==Constante.CORRESPONDENCIA_FIRMADA || idEstado==Constante.CORRESPONDENCIA_COMPLETADA){
				// TICKET 9000003908 - Solo ultimo flujo, se añade condicional
				if(correspondencia.getNroFlujo()==f.getNroFlujo()){
					j++;
				}
			}
		}
		String[] copias = new String[j];
		j = 0;
		for (int i = 0; i < firmantes.size(); i++) {
			Firmante f = (Firmante) firmantes.get(i);
			Long idEstado = f.getEstado().getId();
			if(idEstado==Constante.CORRESPONDENCIA_ASIGNADA || idEstado==Constante.CORRESPONDENCIA_FIRMADA || idEstado==Constante.CORRESPONDENCIA_COMPLETADA){
				// TICKET 9000003908 - Solo ultimo flujo, se añade condicional
				if(correspondencia.getNroFlujo()==f.getNroFlujo()){
					copias[j] = obtenerEmail(((Firmante) firmantes.get(i)).getCodFirmante());
					j++;
				}
			}
		}
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(correspondencia.getResponsable()));
		mail.setCopies(copias);
		Object[] parametros = { correspondencia.getCorrelativo().getCodigo() };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.declinar_correspondencia.asunto", null, locale),
				parametros));
		mail.getModel().put("interno", Boolean.valueOf((1 == correspondencia.getTipoEmision().getId().intValue())));
		mail.getModel().put("mensaje",
				this.messageSource.getMessage("sistcorr.notificar.declinar_correspondencia.mensaje", null, locale));
		mail.getModel().put("dependencia_remitente", correspondencia.getDependencia());
		mail.getModel().put("dependencia_destino", obtenerDependencias(correspondencia));
		mail.getModel().put("copias", "");
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("nro_correspondencia", correspondencia.getCorrelativo().getCodigo());
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("fecha", this.FORMATO_FECHA.format(new Date()));
		mail.getModel().put("solicitante", correspondencia.getResponsable());
		mail.getModel().put("firmante", "");
		mail.getModel().put("motivo_rechazo", "");
		mail.getModel().put("descripcion_motivo_rechazo", "");
		mail.getModel().put("usuarioModifica", "");
		mail.getModel().put("esExterno", Boolean.valueOf(false));
		mail.getModel().put("url_documento", this.url_base + "/app/lista-documentos/firmados?correspondencia="
				+ correspondencia.getCorrelativo().getCodigo() + "&declinados=true");
		// TICKET 9000003992
		this.LOGGER.info("[FIN] notificarCorrespondenciaDeclinadaFirmada");
		return enviarNotificacion(mail);
	}
	
	// TICKET 9000003997


	public boolean notificarCorrespondenciaRechazoDestinatario(Correspondencia correspondencia, String nroDocumento, String nombreDependencia, 
			String usuario, String observacion, Locale locale) {
		List<Firmante> firmantes = this.firmanteDAO.obtenerFirmantes(correspondencia.getId());
		int j = 0;
		for (int i = 0; i < firmantes.size(); i++) {
			Firmante f = (Firmante) firmantes.get(i);
			Long idEstado = f.getEstado().getId();
			if(idEstado==Constante.CORRESPONDENCIA_ASIGNADA || idEstado==Constante.CORRESPONDENCIA_FIRMADA || idEstado==Constante.CORRESPONDENCIA_COMPLETADA){
				// TICKET 9000003908 - Solo ultimo flujo, se añade condicional
				if(correspondencia.getNroFlujo()==f.getNroFlujo()){
					j++;
				}
			}
		}
		String[] copias = new String[j];
		j = 0;
		for (int i = 0; i < firmantes.size(); i++) {
			Firmante f = (Firmante) firmantes.get(i);
			Long idEstado = f.getEstado().getId();
			if(idEstado==Constante.CORRESPONDENCIA_ASIGNADA || idEstado==Constante.CORRESPONDENCIA_FIRMADA || idEstado==Constante.CORRESPONDENCIA_COMPLETADA){
				// TICKET 9000003908 - Solo ultimo flujo, se añade condicional
				if(correspondencia.getNroFlujo()==f.getNroFlujo()){
					copias[j] = obtenerEmail(((Firmante) firmantes.get(i)).getCodFirmante());
					j++;
				}
			}
		}
		Mail mail = new Mail();
		mail.setFrom("xxx@gmail.com");
		mail.setTo(obtenerEmail(correspondencia.getResponsable()));
		mail.setCopies(copias);
		Object[] parametros = { nroDocumento };
		mail.setSubject(MessageFormat.format(
				this.messageSource.getMessage("sistcorr.notificar.rechazo_destinatario_correspondencia.asunto", null, locale),
				parametros));
		
		mail.getModel().put("nroDocumento", nroDocumento);
		mail.getModel().put("asunto", correspondencia.getAsunto());
		mail.getModel().put("dependencia", nombreDependencia);
		mail.getModel().put("tipo_correspondencia", correspondencia.getTipoCorrespondencia());
		mail.getModel().put("usuario", usuario);
		mail.getModel().put("observacion", observacion);

		return enviarNotificacionRechazoDestinatario(mail);
	}
	// FIN TICKET

	private String obtenerDependencias(Correspondencia correspondencia) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] obtenerDependencias");
		if (1 == correspondencia.getTipoEmision().getId().intValue()) {
			return getDependenciasInterna(correspondencia);
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] getDependenciasInterna");
		return getDependenciasExternas(correspondencia);
	}
	
	private class DependenciasDestExternoDetalle{
		
		private String txtConfigDespacho;
		private String dependencia;
		private String direccion;
		
		public String getTxtConfigDespacho() {
			return txtConfigDespacho;
		}
		public void setTxtConfigDespacho(String txtConfigDespacho) {
			this.txtConfigDespacho = txtConfigDespacho;
		}
		public String getDependencia() {
			return dependencia;
		}
		public void setDependencia(String dependencia) {
			this.dependencia = dependencia;
		}
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		
		
	}
	
	private List<DependenciasDestExternoDetalle> getDependenciasDestinatarioExterno(Correspondencia correspondencia) {
		String txtConfigDespacho = "Ninguno", direccion ="";
		List<DependenciasDestExternoDetalle> listDestExternDetalle = new ArrayList<DependenciasDestExternoDetalle>();
		
		for (DestinatarioExterno dx : correspondencia.getDetalleExterno()) {
			txtConfigDespacho = dx.getDependencia() + " (Ninguno)";
			direccion ="";
			DependenciasDestExternoDetalle destExternDetalle = new DependenciasDestExternoDetalle();
			
			if(dx.isEsDespachoFisicoDestExterno() != null && dx.isEsCorreoElectronicoDestExterno() != null && 
					dx.isEsDespachoFisicoDestExterno() && dx.isEsCorreoElectronicoDestExterno()) {
				txtConfigDespacho = dx.getDependencia() + " (Ambos)";
				direccion = "Dirección Física y Dirección de Correo: " + ((dx.getCodDepartamento() != null && !dx.getCodDepartamento().equalsIgnoreCase(""))?(dx.getDepartamento() + "-" + dx.getProvincia() + "-" + dx.getDistrito() + ", " + dx.getDireccion() + "; " + dx.getCorreoDestinatario()):(""));
			}
			else if(dx.isEsDespachoFisicoDestExterno() != null && dx.isEsCorreoElectronicoDestExterno() != null &&
					dx.isEsDespachoFisicoDestExterno() && !dx.isEsCorreoElectronicoDestExterno()) {
				txtConfigDespacho = dx.getDependencia() + " (Despacho Físico)";
				direccion = "Dirección Física: " + ((dx.getCodDepartamento() != null && !dx.getCodDepartamento().equalsIgnoreCase(""))?(dx.getDepartamento() + "-" + dx.getProvincia() + "-" + dx.getDistrito() + ", " + dx.getDireccion()):(((dx.getPais() != null && !dx.getPais().equalsIgnoreCase(""))?(dx.getPais() + ", " + dx.getDireccion()):(dx.getDireccion()))));
			}
			else if(dx.isEsDespachoFisicoDestExterno() != null && dx.isEsCorreoElectronicoDestExterno() != null && 
					!dx.isEsDespachoFisicoDestExterno() && dx.isEsCorreoElectronicoDestExterno()) {
				txtConfigDespacho = dx.getDependencia() + " (Por Correo)";
				direccion = "Dirección de Correo: " + ((dx.getCorreoDestinatario() != null && !dx.getCorreoDestinatario().equalsIgnoreCase(""))?(dx.getCorreoDestinatario()):(""));
			}
			
			destExternDetalle.setTxtConfigDespacho(txtConfigDespacho);
			destExternDetalle.setDependencia(dx.getDependencia());
			destExternDetalle.setDireccion(direccion);
			
			listDestExternDetalle.add(destExternDetalle);
			
		}
		
		return listDestExternDetalle;
	}
	
	private String getDependenciasInterna(Correspondencia correspondencia) {
		// TICKET 9000003992
		this.LOGGER.info("[INICIO] getDependenciasInterna");
		String destinatarios = "";
		for (DestinatarioInterno di : correspondencia.getDetalleInterno()) {
			destinatarios = di.getDependencia() + " / " + destinatarios;
		}
		if (destinatarios.length() > 0) {
			return destinatarios.substring(0, destinatarios.length() - 2);
		}
		// TICKET 9000003992
		this.LOGGER.info("[FIN] getDependenciasInterna");
		return destinatarios;
	}

	private String getDependenciasExternas(Correspondencia correspondencia) {
		String destinatarios = "";
		for (DestinatarioExterno dx : correspondencia.getDetalleExterno()) {
			destinatarios = dx.getDependencia() + " / " + destinatarios;
		}
		if (destinatarios.length() > 0) {
			return destinatarios.substring(0, destinatarios.length() - 2);
		}
		return destinatarios;
	}
	
	// TICKET 9000003791
	
	public boolean notificarHistorialCorrespondencia(List<HistorialArchivo> historial, UsuarioPetroperu usuario, Locale locale) {
		MailHistorialDireccion mail = new MailHistorialDireccion();
		//mail.setFrom("administrador@petroperu.com");
		mail.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
		String destinatarios = historial.get(0).getCorrespondenciaCompartida().getDestinatarios();
		String[] dest = destinatarios.split(";");
		mail.setTo(dest);
		String copias = historial.get(0).getCorrespondenciaCompartida().getCopias();
		String[] cop = copias.split(";");
		mail.setCopias(cop);
		String subject = historial.get(0).getCorrespondenciaCompartida().getAsunto();
		mail.setSubject(subject);
		String contenido = historial.get(0).getCorrespondenciaCompartida().getContenido();
		mail.getModel().put("contenido", contenido.replaceAll("\\n", "<br>"));
		List<ArchivoCompartido> archivos = new ArrayList<ArchivoCompartido>();
		for(HistorialArchivo h : historial){
			ArchivoCompartido ac = h.getArchivoCompartido();
			archivos.add(ac);
		}
		mail.getModel().put("archivos", archivos);
		if(historial.get(0).getCorrespondenciaCompartida().getModoCompartido().equalsIgnoreCase("Dirección")){
			return enviarNotificacionHistorialDireccion(mail, usuario);
		}else if(historial.get(0).getCorrespondenciaCompartida().getModoCompartido().equalsIgnoreCase("Adjunto")){
			return enviarNotificacionHistorialAdjunto(mail, usuario);
		}
		return false;
	}
	
	private boolean enviarNotificacionHistorialDireccion(MailHistorialDireccion mail, UsuarioPetroperu usuario) {
		this.LOGGER.info("[INICIO] enviarNotificacionHistorialDireccion");
		boolean respuesta = false;
		try {
			mail.getModel().put("url", URL_COMPARTIR);
			//mail.setUrl(URL_COMPARTIR);
			LOGGER.info("USUARIO NOMBRE COMPLETO:" + usuario.getNombreCompleto());
			LOGGER.info("USUARIO NOMBRES Y APELLIDOS:" + usuario.getNombres() + " " + usuario.getApellidos());
			mail.getModel().put("responsable", usuario.getNombreCompleto());
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-historial-direccion-template", (IContext) context);
			for(String to : mail.getTo()){
				helper.addTo(to);
			}
			if (mail.getCopias() != null && (mail.getCopias()).length > 0) {
				for(String cc : mail.getCopias()){
					if(!cc.trim().equalsIgnoreCase("")){
						helper.addCc(cc);
					}
				}
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			//LOGGER.info("USER MAIL:" + usuario.getEmail());
			//helper.setFrom("administrador@petroperu.com.pe");
			helper.setFrom(usuario.getEmail());
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacionHistorialDireccion", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionHistorialDireccion " + respuesta);
		return respuesta;
	}
	
	/*private boolean enviarNotificacionHistorialAdjunto(MailHistorialDireccion mail, UsuarioPetroperu usuario) {
		this.LOGGER.info("[INICIO] enviarNotificacionHistorialAdjunto");
		boolean respuesta = false;
		try {
			mail.setUrl(URL_COMPARTIR);
			LOGGER.info("USUARIO NOMBRE COMPLETO:" + usuario.getNombreCompleto());
			LOGGER.info("USUARIO NOMBRES Y APELLIDOS:" + usuario.getNombres() + " " + usuario.getApellidos());
			mail.getModel().put("responsable", usuario.getNombreCompleto());
			List<ArchivoCompartido> lista = (List<ArchivoCompartido>)mail.getModel().get("archivos");
			mail.getModel().replace("archivos", new ArrayList<>());
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-historial-direccion-template", (IContext) context);
			for(String to : mail.getTo()){
				helper.addTo(to);
			}
			if (mail.getCopias() != null && (mail.getCopias()).length > 0) {
				for(String cc : mail.getCopias()){
					if(!cc.trim().equalsIgnoreCase("")){
						helper.addCc(cc);
					}
				}
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			//helper.setFrom("administrador@petroperu.com.pe");
			helper.setFrom(usuario.getEmail());
			
			for(ArchivoCompartido ar : lista){
				//String nuevaUrl = generarCopiaArchivo(ar, mail);
				//File f = new File(ar.getArchivo().getUbicacion());
				String urlBase = DIRECCTORIO_BASE;
				String urlCarpetaCompartidos = "adjuntos";
				String nuevaUrl = urlBase + urlCarpetaCompartidos + "/" + ar.getArchivo().getNombreServidor();
				LOGGER.info("Nueva Url:" + nuevaUrl);
				File f = new File(nuevaUrl);
				
				helper.addAttachment(MimeUtility.encodeWord(ar.getArchivo().getNombre()), f);
			}
			// DESCOMENTAR CUANDO ESTE TERMINADO EL FOOTER DE LOS ARCHIVOS
			if(this.emailSender==null){
				LOGGER.info("EmailSender null");
			}
			if(message==null){
				LOGGER.info("message null");
			}
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacionHistorialAdjunto", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionHistorialAdjunto " + respuesta);
		return respuesta;
	}*/
	
	//inicio ticket 7000004284
	@SuppressWarnings("unchecked")
	private boolean enviarNotificacionHistorialAdjunto(MailHistorialDireccion mail, UsuarioPetroperu usuario) {
		this.LOGGER.info("[INICIO] enviarNotificacionHistorialAdjunto");
		boolean respuesta = false;
		try {
			mail.setUrl(URL_COMPARTIR);
			LOGGER.info("USUARIO NOMBRE COMPLETO:" + usuario.getNombreCompleto());
			LOGGER.info("USUARIO NOMBRES Y APELLIDOS:" + usuario.getNombres() + " " + usuario.getApellidos());
			mail.getModel().put("responsable", usuario.getNombreCompleto());
			List<ArchivoCompartido> lista = (List<ArchivoCompartido>)mail.getModel().get("archivos");
			mail.getModel().replace("archivos", new ArrayList<>());
			MimeMessage message = this.emailSender.createMimeMessage();
			
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-historial-direccion-template", (IContext) context);
			for(String to : mail.getTo()){
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); 
			}
			
			if (mail.getCopias() != null && (mail.getCopias()).length > 0) {
				for(String cc : mail.getCopias()){
					if(!cc.trim().equalsIgnoreCase("")){
						message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
					}
				}
			}
			
			message.setSubject(mail.getSubject());
			message.setFrom(new InternetAddress(usuario.getEmail()));
			
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setContent(html, "text/html; charset=utf-8");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			for(ArchivoCompartido ar : lista){
				
				String urlBase = DIRECCTORIO_BASE;
				String urlCarpetaCompartidos = "adjuntos";
				String nuevaUrl = urlBase + urlCarpetaCompartidos + "/" + ar.getArchivo().getNombreServidor();
				LOGGER.info("Nueva Url:" + nuevaUrl);
				File fileAdj = new File(nuevaUrl);
				
				MimeBodyPart attachmentPart = new MimeBodyPart();
				
			    attachmentPart.attachFile(fileAdj);
			    attachmentPart.setFileName(ar.getArchivo().getNombre());
				multipart.addBodyPart(attachmentPart);
			}
			message.setContent(multipart);
			if(this.emailSender==null){
				LOGGER.info("EmailSender null");
			}
			
			this.emailSender.send(message);
			
			respuesta = true;
		} catch (Exception e) {
			respuesta = false;
			this.LOGGER.error("[ERROR] enviarNotificacionHistorialAdjunto", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionHistorialAdjunto " + respuesta);
		return respuesta;
	}
	//fin fin ticket 7000004284
	
	// TICKET 9000003997
	private boolean enviarNotificacionRechazoDestinatario(Mail mail) {
		this.LOGGER.info("[INICIO] enviarNotificacionRechazoDestinatario");
		if (!this.notificar) {
			return true;
		}
		boolean respuesta = false;
		try {
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mail.getModel());
			String html = this.templateEngine.process("email/email-template-rechazo-destinatario", (IContext) context);
			mail.setHtmlBody(html);//TICKET 9000004411
			helper.setTo(mail.getTo());
			if (mail.getCopies() != null && (mail.getCopies()).length > 0) {
				helper.setCc(mail.getCopies());
			}
			helper.setText(html, true);
			helper.setSubject(mail.getSubject());
			helper.setFrom("sistcorr@petroperu.com.pe"); // edicion 9-3874
			this.emailSender.send(message);
			respuesta = true;
		} catch (Exception e) {
			respuesta = true;//TICKET 9000004411 false to true
			mail.setFrom("sistcorr@petroperu.com.pe");//TICKET 9000004411
			this.guardarNotificacionNoEnviada(mail);//TICKET 9000004411
			this.LOGGER.error("[ERROR] enviarNotificacionRechazoDestinatario", e);
		}
		this.LOGGER.info("[FIN] enviarNotificacionRechazoDestinatario " + respuesta);
		return respuesta;
	}
	// FIN TICKET
	
	public String generarCopiaArchivo(ArchivoCompartido ar, MailHistorialDireccion mail){
		String ruta = "";
		try{
			/*Document document = new Document();
			LOGGER.info("Archivo: " + ar.getArchivo().getUbicacion());
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ar.getArchivo().getUbicacion()));
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			writer.setPageEvent(event);
			PdfWriter.getInstance(document, new FileOutputStream("/var/www/html/sistcorr/temporal/" + ar.getArchivo().getNombre()));*/
			File source = new File(ar.getArchivo().getUbicacion());
			String[] datos = ar.getArchivo().getUbicacion().split("/");
			String nombre = datos[datos.length-1];
			String rutaDestino = DIRECCTORIO_BASE + DIRECTORIO_COMPARTIDOS + "/" + nombre;
			String rutaOrigen = rutaDestino;
			File dest = new File(rutaDestino);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//Path path = dest.toPath();
			//Files.copy(path, baos);
			//FileSystemUtils.copyRecursively(source, dest);
			FileInputStream fis = new FileInputStream(ar.getArchivo().getUbicacion());
			FileOutputStream fos = new FileOutputStream(rutaDestino);
			byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
            
			Document doc = new Document();
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
			doc.open();
			//doc.add(new Chunk());
			PdfPTable table = new PdfPTable(1);
			PdfPCell cell = new PdfPCell(new Paragraph("Esta es una copia auténtica imprimible de un documento firmado electrónicamente"));
			table.addCell(cell);
			doc.add(table);
			doc.close();
			/*PdfReader reader = new PdfReader(ar.getArchivo().getUbicacion());
			LOGGER.info("Nro. de paginas: " + reader.getNumberOfPages());
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			for(int i=1;i<reader.getNumberOfPages();i++){
				PdfImportedPage page = writer.getImportedPage(reader, i);
				
			}
			writer.setPageEvent(event);
			doc.add(new Chunk());
			doc.close();*/
			/*
			PdfCopy copy = new PdfCopy(doc, new FileOutputStream(rutaDestino));
			PdfCopy.PageStamp stamp;
			doc.open();
			PdfReader reader = new PdfReader(ar.getArchivo().getUbicacion());
			int n = reader.getNumberOfPages();
			for(int i=1;i<=n;i++){
				PdfImportedPage page = copy.getImportedPage(reader, i);
				HeaderFooterPageEvent event = new HeaderFooterPageEvent();
				copy.setPageEvent(event);
				//stamp = copy.createPageStamp(page);
				//ColumnText.showTextAligned( stamp.getUnderContent(), Element.ALIGN_RIGHT, new Phrase(String.format("XXXXX Pág. %d de %d", i, n), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)), 460, 15, 0);
				//stamp.alterContents();
				//copy.addPage(page);
				Rectangle rect = new Rectangle(85,650,800,833);
                PdfFormField pushbutton = PdfFormField.createPushButton(writer);
                pushbutton.setWidget(rect, PdfAnnotation.HIGHLIGHT_PUSH);
                PdfContentByte cb = writer.getDirectContent();
                PdfAppearance app = cb.createAppearance(380,201);
                app.rectangle(62,100,50,-1);
                app.fill();
                pushbutton.setAppearance(PdfAnnotation.APPEARANCE_NORMAL,app);
                writer.addAnnotation(pushbutton);
                PdfImportedPage page = writer.getImportedPage(reader, i);
                Image instance = Image.getInstance(page);
                doc.add(instance);
			}
			//reader.close();
			//fis.close();
			doc.close();
			//copy.close();
			 */
		}catch(Exception e){
			e.printStackTrace();
		}
		return ruta;
	}
	
	private String generateKey(){
		String key = "";
		SecureRandom random = new SecureRandom();
		String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
		String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";
	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	    StringBuilder sb = new StringBuilder(10);
	    for(int i=0;i<10;i++){
	    	int indice = random.nextInt(DATA_FOR_RANDOM_STRING.length());
	    	sb.append(DATA_FOR_RANDOM_STRING.charAt(indice));
	    }
	    key = sb.toString();
		return key;
	}
}
