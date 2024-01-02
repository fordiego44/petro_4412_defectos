package pe.com.petroperu.cliente;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pe.com.petroperu.Respuesta;
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
import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.cliente.model.InformacionDocumento;
import pe.com.petroperu.cliente.model.ListaFiltroConductor;
import pe.com.petroperu.cliente.model.ListaFiltroCorrespondencia;
import pe.com.petroperu.cliente.model.RechazarAsignacionCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RechazarCorrespondenciaParametro;
import pe.com.petroperu.cliente.model.RegistrarObservacion;
import pe.com.petroperu.cliente.model.RespuestaApi;
import pe.com.petroperu.cliente.model.RespuestaError;
import pe.com.petroperu.cliente.model.emision.AsignarDocumento;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaExternaRespuesta;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInterna;
import pe.com.petroperu.cliente.model.emision.CorrespondenciaInternaRespuesta;
import pe.com.petroperu.cliente.model.emision.Folder;
import pe.com.petroperu.cliente.model.emision.RespuestaCargaAdjunto;
import pe.com.petroperu.model.Accion;
import pe.com.petroperu.model.Asignacion;
import pe.com.petroperu.model.AsignacionConsulta;
import pe.com.petroperu.model.ConductorPaginado;
import pe.com.petroperu.model.CopiaCorrespondencia;
import pe.com.petroperu.model.Correspondencia;
import pe.com.petroperu.model.CorrespondenciaConsulta;
import pe.com.petroperu.model.CorrespondenciaSimple;
import pe.com.petroperu.model.CorrespondenciaTareaPaginado;
import pe.com.petroperu.model.FiltroSiguientePagina;
import pe.com.petroperu.model.Funcionario;
import pe.com.petroperu.model.InformacionAsignacion;
import pe.com.petroperu.model.Observaciones;
import pe.com.petroperu.model.TrackingFisico;
import pe.com.petroperu.model.Traza;
import pe.com.petroperu.model.emision.dto.ReemplazoConsultaDTO;







@Service
@PropertySource({"classpath:application.properties"})
public class SistcorrClienteImpl
  implements ISistcorrCliente
{
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());
  
  private final String OK = "200";

  
  @Value("${sistcorr.api.url}")
  private String apiUrlbase;
  
  @Value("${sistcorr.api.version}")
  private String apiVersion;
  
  @Value("${sistcorr.api.login}")
  private String apiUrlLogin;
  
  @Value("${sistcorr.api.documento_descarga}")
  private String apiUrlDocumentoDescarga;
  
  @Value("${sistcorr.api.documento_informacion}")
  private String apiUrlDocumentoInfo;
  
  @Value("${sistcorr.api.contador_bandeja}")
  private String apiUrlContadorBandeja;
  
  @Value("${sistcorr.api.recuperar_correspondencias}")
  private String apiUrlRecuperarCorrespondencias;
  
  @Value("${sistcorr.api.recuperar_correspondencia}")
  private String apiUrlRecuperarCorrespondencia;
  
  @Value("${sistcorr.api.recuperar_adjuntos}")
  private String apiUrlRecuperarAdjuntos;
  
  @Value("${sistcorr.api.historial_asignaciones}")
  private String apiUrlHistoricoAsignaciones;
  
  @Value("${sistcorr.api.lista_acciones}")
  private String apiUrlListarAcciones;
  
  @Value("${sistcorr.api.lista_funcionarios}")
  private String apiUrlListaFuncionarios;
  
  @Value("${sistcorr.api.agregar_asignacion}")
  private String apiUrlAgregarAsignacion;
  
  @Value("${sistcorr.api.borrar_asignacion}")
  private String apiUrlBorrarAsignaion;
  
  @Value("${sistcorr.api.enviar_asignacion}")
  private String apiUrlEnviarAsignacion;
  
  @Value("${sistcorr.api.completar_asignacion}")
  private String apiUrlCompletarAsignacion;
  
  @Value("${sistcorr.api.rechazar.asignacion.correspondencia}")
  private String apiUrlRechazarAsignacionCorresp;
  
  @Value("${sistcorr.api.cerrar_correspondencia}")
  private String apiUrlCerrarCorrespondencia;
  
  @Value("${sistcorr.api.recuperar_asignaciones_temporales}")
  private String apiUrlTemporalAsignaciones;
  
  @Value("${sistcorr.api.agregar_asignacion_masivo}")
  private String apiUrlAgregarAsignacionMasiva;
  
  @Value("${sistcorr.api.recuperar_asignacion}")
  private String apiUrlRecupearAsignacion;
  
  @Value("${sistcorr.api.registrar_correspondencia_interna}")
  private String apiUrlRegistraCorrespondenciaInterna;
  
  @Value("${sistcorr.api.registrar_correspondencia_externa}")
  private String apiUrlRegistrarCorrespondenciaExterna;
  
  @Value("${sistcorr.api.carga_adjunto}")
  private String apiUrlCargaDocumento;
  
  @Value("${sistcorr.api.usuario}")
  private String apiUsuarioNombre;
  
  @Value("${sistcorr.api.clave}")
  private String apiUsuarioClave;
  
  // TICKET 9000003514
  @Value("${sistcorr.api.recuperar_lista_traza}")
  private String apiUrlRecuperarListaTraza;
  
  @Value("${sistcorr.api.recuperar_lista_observaciones}")
  private String apiUrlRecuperarListaObservaciones;
  
  @Value("${sistcorr.api.recuperar_lista_tracking_fisico}")
  private String apiUrlRecuperarListaTrackingFisico;
  
  @Value("${sistcorr.api.recuperar_consulta_correspondencia}")
  private String apiUrlRecuperarConsultaCorrespondencia;
  
  @Value("${sistcorr.api.recuperar_consulta_asignaciones}")
  private String apiUrlRecuperarConsultaAsignaciones;
  
  @Value("${sistcorr.api.copia_correspondencia}")
  private String apiUrlCopiaCorrespondencia;
  
  @Value("${sistcorr.api.agregar_copia_correspondencia}")
  private String apiUrlAgregarCopiaCorrespondencia;
  
  @Value("${sistcorr.api.eliminar_copia_correspondencia}")
  private String apiUrlEliminarCopiaCorrespondencia;
  
  @Value("${sistcorr.api.enviar_copia_correspondencia}")
  private String apiUrlEnviarCopiaCorrespondencia;
  
  @Value("${sistcorr.api.obtener_funcionarios}")
  private String apiUrlObtenerFuncionarios;
  
  @Value("${sistcorr.api.eliminar_adjunto}")
  private String apiUrlEliminarAdjunto;
  
  	//TICKET 9000003514
  	@Value("${sistcorr.api.aceptar_correspondencia}")
  	private String apiUrlAceptarCorrespondencia;
  	
  	@Value("${sistcorr.api.rechazar_correspondencia}")
  	private String apiUrlRechazarCorrespondencia;
  
	@Autowired
	private MessageSource messageSource;
	
	//TICKET 9000004065
	@Value("${sistcorr.api.registrar_observacion}")
  	private String apiUrlRegistrarObservacion;

	//TICKET 9000004044
	@Value("${sistcorr.api.completar_asignacion_automatico}")
  	private String apiUrlCompletarAsignacionAutomatico;
	
	// TICKET 9000004510
	@Value("${sistcorr.api.agregar.adjunto}")
	private String apiUrlAgregarDocumentoServidor;

	@Value("${sistcorr.api.eliminar.adjunto}")
	private String apiUrlEliminarDocumentoServidor;

	@Value("${sistcorr.api.descargar.adjunto}")
	private String apiUrlDescargarDocumentoServidor;
	
	// TICKET 9000004494
	@Value("${sistcorr.api.correspondencias.primera_pagina}")
	private String apiUrlCorrespondenciasPrimeraPagina;
	
	// TICKET 9000004494
	@Value("${sistcorr.api.correspondencias.siguiente_pagina}")
	private String apiUrlCorrespondenciasSiguientePagina;

	// TICKET 9000004497
	@Value("${sistcorr.api.enviar_asignacion_grupal}")
	private String apiUrlEnviarAsignacionGrupal;
  
	// TICKET 9000004276
	@Value("${sistcorr.api.recuperar_adjuntos_general}")
	private String apiUrlRecuperarAdjuntosGeneral;
	
	/*INI Ticket 9*4275*/
	@Value("${sistcorr.api.notificar_reemplazos}")
	private String apiUrlNotificarReemplazos;
	
	@Value("${sistcorr.api.conductor.primera_pagina}")
	private String apiUrlConductorPrimeraPagina;
	
	@Value("${sistcorr.api.conductor.siguiente_pagina}")
	private String apiUrlConductorSiguientePagina;
	
	@Value("${sistcorr.api.conductor.reintentar_conductor}")
	private String apiUrlReintentarConductor;
	
	@Value("${sistcorr.api.conductor.avanzar_paso_conductor}")
	private String apiUrlAvanzarPasoConductor;
	
	@Value("${sistcorr.api.conductor.terminar_paso_conductor}")
	private String apiUrlTerminarPasoConductor;
	
	/*FIN Ticket 9*4275*/
	
	/*INI Ticket 9*4413*/
  	@Value("${sistcorr.api.rechazar_correspondenciaMPV}")
  	private String apiUrlRechazarCorrespondenciaMPV;
  	
  	@Value("${sistcorr.api.asignar_dependenciaMPV}")
  	private String apiUrlAsignarDependenciaaMPV;
  	/*FIN Ticket 9*4413*/
  
  	/*INI Ticket 9000004412*/
  	@Value("${sistcorr.api.crear_expediente}")
  	private String apiUrlCrearExpediente;
  	/*FIN Ticket 9000004412*/
  	
  public Respuesta<String> recuperarToken(String nombreUsuario, String claveUsuario) {
    Respuesta<String> respuesta = new Respuesta();
    try {
      URL url = new URL(this.apiUrlLogin);
      this.LOGGER.info("URL " + this.apiUrlLogin);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      String data = "{'userName' : '" + nombreUsuario + "', 'password': '" + claveUsuario + "'}";
      this.LOGGER.info(data);
      OutputStream os = conn.getOutputStream();
      os.write(data.getBytes());
      os.flush();
      if (conn.getResponseCode() != 200) {
        this.LOGGER.info("[INFO] " + conn.getResponseMessage());
        throw new Exception(conn.getResponseCode() + "");
      } 
      this.LOGGER.info("[TOKEN] " + conn.getHeaderField("Authorization"));
      String TOKEN = conn.getHeaderField("Authorization");
      respuesta.mensaje = "200";
      respuesta.estado = true;
      respuesta.datos.add(TOKEN);
      conn.disconnect();
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] " + e.getMessage());
    } 
    return respuesta;
  }

  
  public ResponseEntity<byte[]> descargarDocumento(String token, String documentoId) {
    try {
      documentoId = URLEncoder.encode(documentoId, "UTF-8");
      Object[] parametros = { documentoId };
      String _URL = MessageFormat.format(this.apiUrlDocumentoDescarga, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      URL url = new URL(_URL);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_OCTET_STREAM }));
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<byte[]> response = restTemplate.exchange(url.toURI(), HttpMethod.GET, entity, byte[].class);
      if (response.getStatusCode() == HttpStatus.OK) {
        return response;
      }
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] descargarDocumento", e);
    } 
    this.LOGGER.info("[FIN] descargarDocumento");
    return null;
  }
  
	public Respuesta<RespuestaApi> eliminarDocumento(String token, String documentoId, String proceso, String codigoTraza) {
		Respuesta<RespuestaApi> respuesta = new Respuesta();
		try {
			documentoId = URLEncoder.encode(documentoId, "UTF-8");
			Object[] parametros = { documentoId, proceso, codigoTraza };
			String _URL = MessageFormat.format(this.apiUrlEliminarAdjunto, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			URL url = new URL(_URL);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			ResponseEntity<byte[]> response = restTemplate.exchange(url.toURI(), HttpMethod.DELETE, entity, byte[].class);
			this.LOGGER.info("Status:" + response.getStatusCodeValue());
			if (response.getStatusCode() != HttpStatus.OK)
		        throw new Exception(response.getStatusCode().value() + ""); 
		      if (response.getStatusCode() == HttpStatus.OK) {
		        respuesta.estado = true;
		        respuesta.mensaje = "200";
		        respuesta.mensaje = "Archivo eliminado correctamente";
		      } 
		      return respuesta;
		} catch(HttpServerErrorException ex){
			//this.LOGGER.error("[ERROR] eliminarDocumento", ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
		      this.LOGGER.info("Error: " + error.toString());
		      this.LOGGER.info("Mensaje de Error: " + error.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	this.LOGGER.error("[ERROR] eliminarDocumento", e);
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] eliminarDocumento");
	    return respuesta;
	}

  
  public Respuesta<InformacionDocumento> recuperarDocumento(String token, String correlativo) {
    this.LOGGER.info("[INICIO] recuperarDocumento");
    Respuesta<InformacionDocumento> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlDocumentoInfo, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<InformacionDocumento> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, InformacionDocumento.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] recuperarDocumento", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] recuperarDocumento", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] recuperarDocumento");
    return respuesta;
  }

  
  public Respuesta<ContadorBandeja> contadorBandeja(String token, String bandeja) {
    this.LOGGER.info("[INICIO] contadorBandeja");
    Respuesta<ContadorBandeja> respuesta = new Respuesta();
    try {
      Object[] parametros = { bandeja };
      String _URL = MessageFormat.format(this.apiUrlContadorBandeja, parametros);
      this.LOGGER.info("[URL] " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      this.LOGGER.info("[INICIO PETICION REST] contadorBandeja " + bandeja);
      ResponseEntity<ContadorBandeja> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, ContadorBandeja.class, new Object[0]);
      this.LOGGER.info("[FIN PETICION REST] contadorBandeja " + bandeja + ":" + response.getStatusCode());
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] contadorBandeja HttpServerErrorException", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] contadorBandeja", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] contadorBandeja");
    return respuesta;
  }

  
  public Respuesta<Bandeja> recuperarCorrespondencias(String token, String bandeja) {
    this.LOGGER.info("[INICIO] recuperarCorrespondencias");
    Respuesta<Bandeja> respuesta = new Respuesta();
    try {
      Object[] parametros = { bandeja };
      String _URL = MessageFormat.format(this.apiUrlRecuperarCorrespondencias, parametros);
      this.LOGGER.info("[URL] " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      this.LOGGER.info("[INICIO PETICION REST] recuperarCorrespondencias " + bandeja);
      ResponseEntity<List<CorrespondenciaSimple>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<CorrespondenciaSimple>>() {  }, new Object[0]);
      this.LOGGER.info("[FIN PETICION REST] recuperarCorrespondencias " + bandeja + ":" + response.getStatusCode());
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        Bandeja _bandeja = new Bandeja();
        _bandeja.setCorrespondencias((List)response.getBody());
        respuesta.datos.add(_bandeja);
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarCorrespondencias HttpServerErrorException", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recuperarCorrespondencias", e);
    } 
    this.LOGGER.info("[FIN] recuperarCorrespondencias");
    return respuesta;
  }

  
  public Respuesta<Correspondencia> recuperarCorrespondencia(String token, String correlativo) {
    this.LOGGER.info("[INICIO] recuperarCorrespondencia");
    Respuesta<Correspondencia> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlRecuperarCorrespondencia, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<Correspondencia> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, Correspondencia.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarCorrespondencia ", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recuperarCorrespondencia ", e);
    } 
    this.LOGGER.info("[FIN] recuperarCorrespondencia");
    return respuesta;
  }

  
  public Respuesta<InformacionDocumento> recuperarDocumentos(String token, String correlativo, Locale locale) {
    this.LOGGER.info("[INICIO] recuperarDocumentos " + correlativo);
    Respuesta<InformacionDocumento> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlRecuperarAdjuntos, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<InformacionDocumento>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<InformacionDocumento>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarDocumentos", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recuperarDocumentos", e);
    } 
    this.LOGGER.info("[FIN] recuperarDocumentos");
    return respuesta;
  }

  
  public Respuesta<Asignacion> recupearHistorial(String token, String correlativo) {
    this.LOGGER.info("[INICIO] recupearHistorial");
    Respuesta<Asignacion> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlHistoricoAsignaciones, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<Asignacion>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Asignacion>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] recupearHistorial", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recupearHistorial", e);
    } 
    this.LOGGER.info("[FIN] recupearHistorial");
    return respuesta;
  }

  
  public Respuesta<Accion> recuperarAcciones(String token) {
    this.LOGGER.info("[INICIO] recuperarAcciones");
    Respuesta<Accion> respuesta = new Respuesta();
    try {
      Object[] parametros = { "TD_CR" };
      String _URL = MessageFormat.format(this.apiUrlListarAcciones, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<Accion>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Accion>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarAcciones", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recuperarAcciones", e);
    } 
    this.LOGGER.info("[FIN] recuperarAcciones");
    return respuesta;
  }


  
  public Respuesta<Funcionario> recuperarFuncionarios(String token, String wobNum) {
    this.LOGGER.info("[INICIO] recuperarFuncionarios");
    Respuesta<Funcionario> respuesta = new Respuesta();
    try {
      wobNum = URLEncoder.encode(wobNum, "UTF-8");
      Object[] parametros = { wobNum };
      String _URL = MessageFormat.format(this.apiUrlListaFuncionarios, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<Funcionario>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Funcionario>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarFuncionarios", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] recuperarFuncionarios", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] recuperarFuncionarios");
    return respuesta;
  }



  
  public Respuesta<Asignacion> agregarAsignacion(String token, AgregarAsignacionParametro parametro) {
    this.LOGGER.info("[INICIO] agregarAsignacion");
    Respuesta<Asignacion> respuesta = new Respuesta();
    try {
      Object[] parametros = new Object[0];
      String _URL = MessageFormat.format(this.apiUrlAgregarAsignacion, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<AgregarAsignacionParametro> entity = new HttpEntity(parametro, (MultiValueMap)headers);
      ResponseEntity<List<Asignacion>> response = restTemplate.exchange(_URL, HttpMethod.PUT, entity, new ParameterizedTypeReference<List<Asignacion>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] agregarAsignacion", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] agregarAsignacion", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[INICIO] agregarAsignacion");
    return respuesta;
  }


  
  public Respuesta<RespuestaApi> borrarAsigancion(String token, Integer idAsignacion) {
    this.LOGGER.info("[INICIO] borrarAsigancion");
    Respuesta<RespuestaApi> respuesta = new Respuesta();
    try {
      Object[] parametros = { String.valueOf(idAsignacion) };
      String _URL = MessageFormat.format(this.apiUrlBorrarAsignaion, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.DELETE, entity, RespuestaApi.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.info("[ERROR] borrarAsigancion", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.info("[ERROR] borrarAsigancion", e);
    } 
    this.LOGGER.info("[FIN] borrarAsigancion");
    return respuesta;
  }

  
  public Respuesta<RespuestaApi> enviarAsignacion(String token, String correlativo) {
    this.LOGGER.info("[INICIO] enviarAsignacion");
    Respuesta<RespuestaApi> respuesta = new Respuesta();
    try {
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlEnviarAsignacion, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
      this.LOGGER.info(response.getStatusCodeValue() + "");
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
    	  //tikcet - 9000004931
    	  String errorControlado = response.getBody().getUserMessageError() == null ? "" : response.getBody().getUserMessageError();
  	  	  if (errorControlado.isEmpty()) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.add(response.getBody());
	  	  }else{
	  	  	    respuesta.estado = false;
	  	        respuesta.mensaje = response.getBody().getUserMessageError();
	  	        respuesta.datos.add(response.getBody());
	  	   }

      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] enviarAsignacion", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] enviarAsignacion", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] enviarAsignacion");
    return respuesta;
  }



  
  public Respuesta<RespuestaApi> completarAsignacion(String token, Integer idAsignacion, CompletarCorrespondenciaParametro correspondencia) {
    this.LOGGER.info("[INICIO] completarAsignacion " + idAsignacion.toString() + " parametros " + correspondencia.getDocumentoRespuesta() + " - " + correspondencia.getRespuesta() + " - " + correspondencia.getUsuarioResponsable());
    Respuesta<RespuestaApi> respuesta = new Respuesta();
    try {
      Object[] parametros = { idAsignacion.toString() };
      String _URL = MessageFormat.format(this.apiUrlCompletarAsignacion, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<CompletarCorrespondenciaParametro> entity = new HttpEntity(correspondencia, (MultiValueMap)headers);
      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
      if (response.getStatusCode() == HttpStatus.OK) {
	  	  //tikcet - 9000004931
    	  String errorControlado = response.getBody().getUserMessageError() == null ? "" : response.getBody().getUserMessageError();
  	  	  if (errorControlado.isEmpty()) {
		        respuesta.estado = true;
		        respuesta.mensaje = "200";
		        respuesta.datos.add(response.getBody());
	  	  	  }else{
	  	  	    respuesta.estado = false;
	  	        respuesta.mensaje = response.getBody().getUserMessageError();
	  	        respuesta.datos.add(response.getBody());
	  	  }
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] completarAsignacio", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] completarAsignacio", e);
    } 
    this.LOGGER.info("[FIN] completarAsignacion");
    this.LOGGER.info("respuesta:" + respuesta.mensaje);
    return respuesta;
  }

  
  public Respuesta<Bandeja> filtraCorrespondencias(String token, String bandeja, ListaFiltroCorrespondencia filtros) {
    this.LOGGER.info("[INICIO] filtraCorrespondencias");
    Respuesta<Bandeja> respuesta = new Respuesta();
    try {
      Object[] parametros = { bandeja };
      String _URL = MessageFormat.format(this.apiUrlRecuperarCorrespondencias, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<ListaFiltroCorrespondencia> entity = new HttpEntity(filtros, (MultiValueMap)headers);
      this.LOGGER.info("[INICIO PETICION REST] filtraCorrespondencias " + bandeja);
      ResponseEntity<List<CorrespondenciaSimple>> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<List<CorrespondenciaSimple>>() {  }, new Object[0]);
      this.LOGGER.info("[FIN PETICION REST] filtraCorrespondencias " + bandeja + ":" + response.getStatusCode());
      if (response.getStatusCode() != HttpStatus.OK) {
        throw new Exception(response.getStatusCode().value() + "");
      }
      
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        Bandeja _bandeja = new Bandeja();
        _bandeja.setCorrespondencias((List)response.getBody());
        respuesta.datos.add(_bandeja);
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] filtraCorrespondencias HttpServerErrorException", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] filtraCorrespondencias", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] filtraCorrespondencias");
    return respuesta;
  }


  
  public Respuesta<RespuestaApi> cerrarCorrespondencia(String token, String correlativo, CerrarCorrespondenciaParametro parametros) {
    this.LOGGER.info("[INICIO] cerrarCorrespondencia " + correlativo + " - " + parametros.getDocumentoRespuesta() + " - " + parametros.getObservacion() + " - " + parametros.getUsuarioResponsable());
    Respuesta<RespuestaApi> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] _parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlCerrarCorrespondencia, _parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<CerrarCorrespondenciaParametro> entity = new HttpEntity(parametros, (MultiValueMap)headers);
      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
	  	  //tikcet - 9000004931
    	  String errorControlado = response.getBody().getUserMessageError() == null ? "" : response.getBody().getUserMessageError();
  	  	  if (errorControlado.isEmpty()) {
		        respuesta.estado = true;
		        respuesta.mensaje = "200";
		        respuesta.datos.add(response.getBody());
	  	  	  }else{
	  	  	    respuesta.estado = false;
	  	        respuesta.mensaje = response.getBody().getUserMessageError();
	  	        respuesta.datos.add(response.getBody());
	  	  }
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] cerrarCorrespondencia", (Throwable)ex);
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] cerrarCorrespondencia", e);
    } 
    this.LOGGER.info("[FIN] cerrarCorrespondencia");
    return respuesta;
  }


  
  public Respuesta<Asignacion> recuperarTemporalAsignaciones(String token, String correlativo) {
    this.LOGGER.info("[INICIO] recuperarTemporalAsignaciones");
    Respuesta<Asignacion> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlTemporalAsignaciones, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<Asignacion>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Asignacion>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarTemporalAsignaciones", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] recuperarTemporalAsignaciones", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] recuperarTemporalAsignaciones");
    return respuesta;
  }


  
  public Respuesta<Asignacion> agregarAsignacionMasivo(String token, AgregarAsignacionMasivaParametro parametro) {
    this.LOGGER.info("[INICIO] agregarAsignacionMasivo");
    Respuesta<Asignacion> respuesta = new Respuesta();
    try {
      Object[] parametros = new Object[0];
      String _URL = MessageFormat.format(this.apiUrlAgregarAsignacionMasiva, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<AgregarAsignacionMasivaParametro> entity = new HttpEntity(parametro, (MultiValueMap)headers);
      ResponseEntity<List<Asignacion>> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<List<Asignacion>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] agregarAsignacionMasivo", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] agregarAsignacionMasivo", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] agregarAsignacionMasivo");
    return respuesta;
  }


  
  public Respuesta<InformacionAsignacion> recuperarAsignacion(String token, Integer idAsignacion) {
    this.LOGGER.info("[INICIO] recuperarAsignacion");
    Respuesta<InformacionAsignacion> respuesta = new Respuesta();
    try {
      Object[] parametros = { String.valueOf(idAsignacion) };
      String _URL = MessageFormat.format(this.apiUrlRecupearAsignacion, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<InformacionAsignacion> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, InformacionAsignacion.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
        this.LOGGER.info("Fecha Documento:" + response.getBody().getCorrespondencia().getFechaDocumento());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] recuperarAsignacion", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] recuperarAsignacion", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] recuperarAsignacion");
    return respuesta;
  }

  
  public Respuesta<CorrespondenciaInternaRespuesta> registrarCorrespondenciaEmitidaInterna(String token, String correlativo, CorrespondenciaInterna correspondencia) {
    this.LOGGER.info("[INICIO] registrarCorrespondencisEmitidaInterna");
    Respuesta<CorrespondenciaInternaRespuesta> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] _parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlRegistraCorrespondenciaInterna, _parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      this.LOGGER.info("Correspondencia interna: " + correspondencia.toString());
      HttpEntity<CorrespondenciaInterna> entity = new HttpEntity(correspondencia, (MultiValueMap)headers);
      ResponseEntity<CorrespondenciaInternaRespuesta> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, CorrespondenciaInternaRespuesta.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
        this.LOGGER.info("Respuesta: " + response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] registrarCorrespondencisEmitidaInterna", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] registrarCorrespondencisEmitidaInterna", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] registrarCorrespondencisEmitidaInterna");
    return respuesta;
  }

  
  public Respuesta<CorrespondenciaExternaRespuesta> registrarCorrespondenciaEmitidaExterna(String token, String correlativo, CorrespondenciaExterna correspondencia) {
    this.LOGGER.info("[INICIO] CorrespondenciaExternaRespuesta");
    Respuesta<CorrespondenciaExternaRespuesta> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] _parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlRegistrarCorrespondenciaExterna, _parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      this.LOGGER.info("Correspondencia externa: " + correspondencia.toString());
      HttpEntity<CorrespondenciaExterna> entity = new HttpEntity(correspondencia, (MultiValueMap)headers);
      ResponseEntity<CorrespondenciaExternaRespuesta> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, CorrespondenciaExternaRespuesta.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.add(response.getBody());
        this.LOGGER.info("Respuesta: " + response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] CorrespondenciaExternaRespuesta", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] CorrespondenciaExternaRespuesta", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] CorrespondenciaExternaRespuesta");
    return respuesta;
  }


  
  public Respuesta<RespuestaCargaAdjunto> cargarAdjunto(String token, AsignarDocumento parametro, Folder folder, File file, String fileName, String titulo) {
    this.LOGGER.info("[INICIO] cargarAdjunto");
    Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta();

    
    try {
    	// TICKET 9000004510
      //String _URL = this.apiUrlCargaDocumento;
        Object[] parametros = { titulo };
        String _URL = MessageFormat.format(this.apiUrlCargaDocumento, parametros);
      	// FIN TICKET
        _URL = _URL + "&objectStoreName=Correspondencia";
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("TOKEN " + token);
      LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
      linkedMultiValueMap.add("file", new FileSystemResource(file));
      linkedMultiValueMap.add("documentClass ", parametro);
      linkedMultiValueMap.add("folder", folder);
      this.LOGGER.info("file: " + file.getAbsolutePath());
      this.LOGGER.info("documentClass: " + parametro.toString());
      this.LOGGER.info("folder: " + folder.toString());
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(linkedMultiValueMap, (MultiValueMap)headers);
      ResponseEntity<RespuestaCargaAdjunto> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaCargaAdjunto.class, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.mensaje = "Archivo guardado correctamente";
        respuesta.datos.add(response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
      this.LOGGER.error("[ERROR] cargarAdjunto", (Throwable)ex);
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] cargarAdjunto", e);
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
    } 
    this.LOGGER.info("[FIN] cargarAdjunto");
    return respuesta;
  }



  
  public String getApiUsuarioNombre() { return this.apiUsuarioNombre; }



  
  public String getApiUsuarioClave() { return this.apiUsuarioClave; }


  // TICKET 9000003514
  
  public Respuesta<Traza> recuperarTraza(String token, String correlativo) {
    this.LOGGER.info("[INICIO] recuperarTraza");
    Respuesta<Traza> respuesta = new Respuesta();
    try {
      correlativo = URLEncoder.encode(correlativo, "UTF-8");
      Object[] parametros = { correlativo };
      String _URL = MessageFormat.format(this.apiUrlRecuperarListaTraza, parametros);
      this.LOGGER.info("URL " + _URL);
      this.LOGGER.info("[TOKEN] " + token);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", token);
      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
      ResponseEntity<List<Traza>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Traza>>() {  }, new Object[0]);
      if (response.getStatusCode() != HttpStatus.OK)
        throw new Exception(response.getStatusCode().value() + ""); 
      if (response.getStatusCode() == HttpStatus.OK) {
        respuesta.estado = true;
        respuesta.mensaje = "200";
        respuesta.datos.addAll((Collection)response.getBody());
      } 
    } catch (HttpServerErrorException ex) {
      this.LOGGER.error("[ERROR] recuperarTraza", (Throwable)ex);
      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
      respuesta.estado = false;
      respuesta.mensaje = error.getMessage();
    } catch (Exception e) {
      respuesta.estado = false;
      respuesta.mensaje = e.getMessage();
      this.LOGGER.error("[ERROR] recuperarTraza", e);
    } 
    this.LOGGER.info("[FIN] recuperarTraza");
    return respuesta;
  }
  
	public Respuesta<Observaciones> recuperarObservaciones(String token, String correlativo) {
		this.LOGGER.info("[INICIO] recuperarObservaciones");
		Respuesta<Observaciones> respuesta = new Respuesta();
		try {
			correlativo = URLEncoder.encode(correlativo, "UTF-8");
			Object[] parametros = { correlativo };
			String _URL = MessageFormat.format(this.apiUrlRecuperarListaObservaciones, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("[TOKEN] " + token);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			ResponseEntity<List<Observaciones>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Observaciones>>() {  }, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + "");
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			}
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] recuperarObservaciones", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] recuperarObservaciones", e);
	    } 
	    this.LOGGER.info("[FIN] recuperarObservaciones");
	    return respuesta;
	}
	
	public Respuesta<TrackingFisico> recuperarTrackingFisico(String token, String correlativo) {
		this.LOGGER.info("[INICIO] recuperarTrackingFisico");
		Respuesta<TrackingFisico> respuesta = new Respuesta();
		try {
			correlativo = URLEncoder.encode(correlativo, "UTF-8");
			Object[] parametros = { correlativo };
			String _URL = MessageFormat.format(this.apiUrlRecuperarListaTrackingFisico, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("[TOKEN] " + token);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			ResponseEntity<List<TrackingFisico>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<TrackingFisico>>() {  }, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + "");
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			}
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] recuperarTrackingFisico", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] recuperarTrackingFisico", e);
	    } 
	    this.LOGGER.info("[FIN] recuperarTrackingFisico");
	    return respuesta;
	}
	
	public Respuesta<CorrespondenciaConsulta> consultarCorrespondencia(String token, FiltroConsultaCorrespondencia filtro) {
		this.LOGGER.info("[INICIO] consultarCorrespondencia");
		Respuesta<CorrespondenciaConsulta> respuesta = new Respuesta();
		try {
			String _URL = this.apiUrlRecuperarConsultaCorrespondencia;
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("[TOKEN] " + token);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			Map<String, String> valores = new HashMap();
		    valores.put("correlativo", filtro.getCorrelativo()); // ""
		    valores.put("codigoEstado", filtro.getCodigoEstado()); // 0
		    valores.put("fechaRegistroDesde", filtro.getFechaRegistroDesde()); // ""
		    valores.put("fechaRegistroHasta", filtro.getFechaRegistroHasta()); // ""
		    valores.put("numeroDocumentoInterno", filtro.getNumeroDocumentoInterno()); // "%"
		    valores.put("fechaDocumentoInterno", filtro.getFechaDocumentoInterno()); // ""
		    valores.put("codigoDependenciaRemitente", filtro.getCodigoDependenciaRemitente()); // 0
		    valores.put("codigoDependenciaDestino", filtro.getCodigoDependenciaDestino()); // 0
		    valores.put("codigoTipoCorrespondencia", filtro.getCodigoTipoCorrespondencia()); // 0
		    valores.put("nombreDependenciaExterna", filtro.getNombreDependenciaExterna()); // "%"
		    valores.put("guiaRemision", filtro.getGuiaRemision()); // ""
		    valores.put("asunto", filtro.getAsunto()); // "%"
		    valores.put("procedencia", filtro.getProcedencia()); // NC
			HttpEntity<String> entity = new HttpEntity(valores, (MultiValueMap)headers);
			ResponseEntity<List<CorrespondenciaConsulta>> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<List<CorrespondenciaConsulta>>() {  }, new Object[0]);
			//ResponseEntity<List<CorrespondenciaConsulta>> response2 = restTemplate.postForEntity(_URL, data, ResponseEntity.class);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + "");
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			}
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] consultarCorrespondencia conectividad", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			//respuesta.mensaje = error.getMessage();
			respuesta.mensaje = "Error de conexión, volver a intentar luego";
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] consultarCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] consultarCorrespondencia: " + respuesta.datos.size());
	    return respuesta;
	}
	
	public Respuesta<AsignacionConsulta> consultarAsignaciones(String token, FiltroConsultaAsignacion filtro) {
		this.LOGGER.info("[INICIO] consultarAsignaciones");
		Respuesta<AsignacionConsulta> respuesta = new Respuesta();
		try {
			String _URL = this.apiUrlRecuperarConsultaAsignaciones;
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("[TOKEN] " + token);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			
			Map<String, String> valores = new HashMap();
			valores.put("correlativo", filtro.getCorrelativo()); // ""
		    valores.put("numeroDocumentoInterno", filtro.getNumeroDocumentoInterno()); // "%"
		    valores.put("codigoDependenciaAsignante", filtro.getDependenciaAsignante()); // 0
		    valores.put("usuarioAsignado", filtro.getPersonaAsignada()); // ""
		    valores.put("codigoAccion", filtro.getTipoAccion()); // ""
		    valores.put("codigoEstado", filtro.getCodigoEstado()); // 0
		    valores.put("fechaDesde", filtro.getFechaAsignacionDesde()); // ""
		    valores.put("fechaHasta", filtro.getFechaAsignacionHasta()); // ""
		    valores.put("vencimientoDesde", filtro.getFechaVencimientoDesde()); // ""
		    valores.put("vencimientoHasta", filtro.getFechaVencimientoHasta()); // ""
			
			HttpEntity<String> entity = new HttpEntity(valores, (MultiValueMap)headers);
			ResponseEntity<List<AsignacionConsulta>> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<List<AsignacionConsulta>>() {  }, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + "");
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			}
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] consultarAsignaciones conectividad", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			//respuesta.mensaje = error.getMessage();
			respuesta.mensaje = "Error de conexión, volver a intentar luego";
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	//respuesta.mensaje = e.getMessage();
	    	respuesta.mensaje = "Error de conexión, volver a intentar luego";
	    	this.LOGGER.error("[ERROR] consultarAsignaciones", e);
	    } 
	    this.LOGGER.info("[FIN] consultarAsignaciones: " + respuesta.datos.size());
	    return respuesta;
	}
	
	public Respuesta<CopiaCorrespondencia> recuperarCopiaCorrespondencia(String token, String correlativo) {
	    this.LOGGER.info("[INICIO] recuperarCopiaCorrespondencia");
	    Respuesta<CopiaCorrespondencia> respuesta = new Respuesta();
	    try {
	      correlativo = URLEncoder.encode(correlativo, "UTF-8");
	      Object[] parametros = { correlativo };
	      String _URL = MessageFormat.format(this.apiUrlCopiaCorrespondencia, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("[TOKEN] " + token);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<List<CopiaCorrespondencia>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<CopiaCorrespondencia>>() {  }, new Object[0]);
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.addAll((Collection)response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] recuperarCopiaCorrespondencia", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	      this.LOGGER.error("[ERROR] recuperarCopiaCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] recuperarCopiaCorrespondencia");
	    return respuesta;
	}
	
	public Respuesta<RespuestaApi> eliminarCopiaCorrespondencia(String token, String usuario, String correlativo) {
	    this.LOGGER.info("[INICIO] eliminarCopiaCorrespondencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	      correlativo = URLEncoder.encode(correlativo, "UTF-8");
	      Object[] parametros = { correlativo, usuario };
	      String _URL = MessageFormat.format(this.apiUrlEliminarCopiaCorrespondencia, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("[TOKEN] " + token);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
			
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.DELETE, entity, RespuestaApi.class, new Object[0]);
	      //ResponseEntity<List<RespuestaApi>> response = restTemplate.exchange(_URL, HttpMethod.DELETE, entity, new ParameterizedTypeReference<List<RespuestaApi>>() {  }, new Object[0]);
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "Destinatario eliminado correctamente";
	        respuesta.datos.add(response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] eliminarCopiaCorrespondencia", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	      this.LOGGER.error("[ERROR] eliminarCopiaCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] eliminarCopiaCorrespondencia");
	    return respuesta;
	}
	
	public Respuesta<CopiaCorrespondencia> agregarCopiaCorrespondencia(String token, String usuario, String correlativo) {
	    this.LOGGER.info("[INICIO] agregarCopiaCorrespondencia");
	    Respuesta<CopiaCorrespondencia> respuesta = new Respuesta();
	    try {
	    	correlativo = URLEncoder.encode(correlativo, "UTF-8");
	    	Object[] parametros = { correlativo, usuario };
	    	String _URL = MessageFormat.format(this.apiUrlAgregarCopiaCorrespondencia, parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("[TOKEN] " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			ResponseEntity<List<CopiaCorrespondencia>> response = restTemplate.exchange(_URL, HttpMethod.PUT, entity, new ParameterizedTypeReference<List<CopiaCorrespondencia>>() {  }, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + ""); 
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			} 
	    } catch (HttpServerErrorException ex) {
	    	this.LOGGER.error("[ERROR] agregarCopiaCorrespondencia", (Throwable)ex);
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] agregarCopiaCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] agregarCopiaCorrespondencia");
	    return respuesta;
	}
	
	public Respuesta<Funcionario> obtenerFuncionarios(String token, String codigoDependencia, String codigoLugar, String activo) {
	    this.LOGGER.info("[INICIO] obtenerFuncionarios");
	    Respuesta<Funcionario> respuesta = new Respuesta();
	    try {
	    	String _URL = this.apiUrlObtenerFuncionarios;
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("[TOKEN] " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	      
	    	Map<String, String> valores = new HashMap();
	    	valores.put("codigoDependencia", codigoDependencia);
	    	valores.put("codigoLugar", codigoLugar);
	    	valores.put("activo", activo);
			
			HttpEntity<String> entity = new HttpEntity(valores, (MultiValueMap)headers);
			ResponseEntity<List<Funcionario>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Funcionario>>() {  }, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + ""); 
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection)response.getBody());
			} 
	    } catch (HttpServerErrorException ex) {
	    	this.LOGGER.error("[ERROR] obtenerFuncionarios", (Throwable)ex);
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] obtenerFuncionarios", e);
	    } 
	    this.LOGGER.info("[FIN] obtenerFuncionarios");
	    return respuesta;
	}
	
	public Respuesta<RespuestaApi> enviarCopiaCorrespondencia(String token, String texto, String correlativo) {
	    this.LOGGER.info("[INICIO] enviarCopiaCorrespondencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	      correlativo = URLEncoder.encode(correlativo, "UTF-8");
	      Object[] parametros = { correlativo};
	      String _URL = MessageFormat.format(this.apiUrlEnviarCopiaCorrespondencia, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("[TOKEN] " + token);
	      this.LOGGER.info("[Texto:] " + texto);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
			
			Map<String, String> valores = new HashMap();
			valores.put("observacion", texto);
			
			HttpEntity<String> entity = new HttpEntity(valores, (MultiValueMap)headers);
	      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	      //ResponseEntity<List<RespuestaApi>> response = restTemplate.exchange(_URL, HttpMethod.DELETE, entity, new ParameterizedTypeReference<List<RespuestaApi>>() {  }, new Object[0]);
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.add(response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] enviarCopiaCorrespondencia", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	      this.LOGGER.error("[ERROR] enviarCopiaCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] enviarCopiaCorrespondencia");
	    return respuesta;
	}
  
  // FIN TICKET
	
	// TICKET 9000003862
	
	public Respuesta<RespuestaApi> aceptarCorrespondencia(String token, String correlativo) {
	    this.LOGGER.info("[INICIO] aceptarCorrespondencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	correlativo = URLEncoder.encode(correlativo, "UTF-8");
	    	Object[] parametros = { correlativo };
	    	String _URL = MessageFormat.format(this.apiUrlAceptarCorrespondencia, parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("[TOKEN] " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	this.LOGGER.error("[ERROR] aceptarCorrespondencia", (Throwable)ex);
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] aceptarCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] aceptarCorrespondencia");
	    return respuesta;
	}
	  
	public Respuesta<RespuestaApi> rechazarCorrespondencia(String token, String correlativo, RechazarCorrespondenciaParametro parametros) {
	    this.LOGGER.info("[INICIO] rechazarCorrespondencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	correlativo = URLEncoder.encode(correlativo, "UTF-8");
	    	Object[] _parametros = { correlativo };
	    	String _URL = MessageFormat.format(this.apiUrlRechazarCorrespondencia, _parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<RechazarCorrespondenciaParametro> entity = new HttpEntity(parametros, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] rechazarCorrespondencia", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] rechazarCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] rechazarCorrespondencia");
	    return respuesta;
	}
	// FIN TICKET
	
	// TICKET 9000004065
	public Respuesta<RespuestaApi> registrarObservacion(String token, RegistrarObservacion parametros) {
	    this.LOGGER.info("[INICIO] registrarObservacion");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	String _URL = this.apiUrlRegistrarObservacion;
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<RechazarCorrespondenciaParametro> entity = new HttpEntity(parametros, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] registrarObservacion", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] registrarObservacion", e);
	    } 
	    this.LOGGER.info("[FIN] registrarObservacion");
	    return respuesta;
	}
	// FIN TICKET
	
	// TICKET 9000004044
	public Respuesta<RespuestaApi> completarAsignacionAutomatico(String token, Integer idAsignacion) {
	    this.LOGGER.info("[INICIO] completarAsignacionAutomatico " + idAsignacion);
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	Object[] parametros = { idAsignacion.toString() };
	    	String _URL = MessageFormat.format(this.apiUrlCompletarAsignacionAutomatico, parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<RechazarCorrespondenciaParametro> entity = new HttpEntity((MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	  	  	  	  //tikcet - 9000004931
		    	  String errorControlado = response.getBody().getUserMessageError() == null ? "" : response.getBody().getUserMessageError();
		  	  	  if (errorControlado.isEmpty()) {
		    		respuesta.estado = true;
		    		respuesta.mensaje = "200";
		    		respuesta.datos.add(response.getBody());
			  	  }else{
			  	    respuesta.estado = false;
		  	  	    respuesta.mensaje = response.getBody().getUserMessageError();
		  	  	    respuesta.datos.add(response.getBody());
			  	  }
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] completarAsignacionAutomatico", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] completarAsignacionAutomatico", e);
	    } 
	    this.LOGGER.info("[FIN] completarAsignacionAutomatico");
	    return respuesta;
	}
	// FIN TICKET

  
  private RespuestaError obtenerErrorHttp(String responseError) {
    this.LOGGER.info("Response error: " + responseError);
    Gson g = new Gson();
    RespuestaError error = null;
    try {
      error = (RespuestaError)g.fromJson(responseError, RespuestaError.class);
      this.LOGGER.info("Mensaje: " + error.toString());
    } catch (Exception e) {
      this.LOGGER.error("[ERROR] obtenerErrorHttp", e);
    } 
    return error;
  }

  	//INICIO TICKET 9000004273
	@Override
	public Respuesta<RespuestaApi> rechazarAsignacionCorrespondencia(String token, Integer idAsignacion,
			RechazarAsignacionCorrespondenciaParametro correspondencia) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] rechazarAsignacionCorrespondencia " + idAsignacion.toString() + " parametros "
				+ correspondencia.getObservacion());
		Respuesta<RespuestaApi> respuesta = new Respuesta();
		try {
			Object[] parametros = { idAsignacion.toString() };
			String _URL = MessageFormat.format(this.apiUrlRechazarAsignacionCorresp, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<RechazarAsignacionCorrespondenciaParametro> entity = new HttpEntity(correspondencia,
					(MultiValueMap) headers);
			ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity,
					RespuestaApi.class, new Object[0]);
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.add(response.getBody());
			}
			
		} catch (HttpServerErrorException ex) {
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
			this.LOGGER.error("[ERROR] rechazarAsignacionCorrespondencia", (Throwable) ex);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] rechazarAsignacionCorrespondencia", e);
		}
		this.LOGGER.info("[FIN] rechazarAsignacionCorrespondencia");
		this.LOGGER.info("respuesta:" + respuesta.mensaje);
		return respuesta;
	}
	//FIN TICKET 9000004273
	
	// TICKET 9000004510
	public ResponseEntity<byte[]> descargarDocumentoServidor(String token, String documentoId) {
		this.LOGGER.info("[INICIO] descargarDocumentoServidor");
	    try {
	      documentoId = URLEncoder.encode(documentoId, "UTF-8");
	      Object[] parametros = { documentoId, "Correspondencia" };
	      String _URL = MessageFormat.format(this.apiUrlDescargarDocumentoServidor, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("TOKEN " + token);
	      URL url = new URL(_URL);
	      RestTemplate restTemplate = new RestTemplate();
	      restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
	      headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_OCTET_STREAM }));
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<byte[]> response = restTemplate.exchange(url.toURI(), HttpMethod.GET, entity, byte[].class);
	      if (response.getStatusCode() == HttpStatus.OK) {
	        return response;
	      }
	    } catch (Exception e) {
	      this.LOGGER.error("[ERROR] descargarDocumento", e);
	    } 
	    this.LOGGER.info("[FIN] descargarDocumentoServidor");
	    return null;
	}
	
	public Respuesta<RespuestaCargaAdjunto> cargarArchivoServidor(String token, AsignarDocumento parametro, File file, String fileName) {
	    this.LOGGER.info("[INICIO] cargarArchivoServidor");
	    Respuesta<RespuestaCargaAdjunto> respuesta = new Respuesta();
	    
	    try {
	    	Object[] parametros = { "Correspondencia" };
			String _URL = MessageFormat.format(this.apiUrlAgregarDocumentoServidor, parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
	    	linkedMultiValueMap.add("file", new FileSystemResource(file));
	    	linkedMultiValueMap.add("documentClass ", parametro);
	    	this.LOGGER.info("file: " + file.getAbsolutePath());
	    	this.LOGGER.info("documentClass: " + parametro.toString());
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    	HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(linkedMultiValueMap, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaCargaAdjunto> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaCargaAdjunto.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	        	respuesta.mensaje = "200";
	        	respuesta.mensaje = "Archivo guardado correctamente";
	        	respuesta.datos.add(response.getBody());
	      	} 
	    } catch (HttpServerErrorException ex) {
	      	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      	respuesta.estado = false;
	      	respuesta.mensaje = error.getMessage();
	      	this.LOGGER.error("[ERROR] cargarAdjunto", (Throwable)ex);
	    } catch (Exception e) {
	      	this.LOGGER.error("[ERROR] cargarAdjunto", e);
	      	respuesta.estado = false;
	      	respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] cargarArchivoServidor");
	    return respuesta;
	}
	
	public Respuesta<RespuestaApi> eliminarDocumentoServidor(String token, String documentoId) {
	    this.LOGGER.info("[INICIO] eliminarDocumentoServidor");
		Respuesta<RespuestaApi> respuesta = new Respuesta();
		try {
			documentoId = URLEncoder.encode(documentoId, "UTF-8");
			Object[] parametros = { documentoId, "Correspondencia" };
			String _URL = MessageFormat.format(this.apiUrlEliminarDocumentoServidor, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			URL url = new URL(_URL);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			ResponseEntity<byte[]> response = restTemplate.exchange(url.toURI(), HttpMethod.DELETE, entity, byte[].class);
			this.LOGGER.info("Status:" + response.getStatusCodeValue());
			if (response.getStatusCode() != HttpStatus.OK)
		        throw new Exception(response.getStatusCode().value() + ""); 
		      if (response.getStatusCode() == HttpStatus.OK) {
		        respuesta.estado = true;
		        respuesta.mensaje = "200";
		        respuesta.mensaje = "Archivo eliminado correctamente";
		      } 
		      return respuesta;
		} catch(HttpServerErrorException ex){
			//this.LOGGER.error("[ERROR] eliminarDocumento", ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
		      this.LOGGER.info("Error: " + error.toString());
		      this.LOGGER.info("Mensaje de Error: " + error.getMessage());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	    	this.LOGGER.error("[ERROR] eliminarDocumentoServidor", e);
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] eliminarDocumentoServidor");
	    return respuesta;
	}
	// FIN TICKET
	
	// TICKET 9000004494
	public Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasPrimeraPagina(String token, String bandeja, ListaFiltroCorrespondencia filtros, String tamanio) {
		this.LOGGER.info("[INICIO] filtraCorrespondenciasPrimeraPagina");
		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta();
		try {
			Object[] parametros = { bandeja, tamanio };
			String _URL = MessageFormat.format(this.apiUrlCorrespondenciasPrimeraPagina, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			this.LOGGER.info("FILTRO:" + filtros.toString());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<ListaFiltroCorrespondencia> entity = new HttpEntity(filtros, (MultiValueMap)headers);
			this.LOGGER.info("[INICIO PETICION REST] filtraCorrespondenciasPrimeraPagina " + bandeja + " - " + tamanio);
			ResponseEntity<CorrespondenciaTareaPaginado> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, CorrespondenciaTareaPaginado.class, new Object[0]);
			this.LOGGER.info("[FIN PETICION REST] filtraCorrespondenciasPrimeraPagina " + bandeja + ":" + response.getStatusCode());
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new Exception(response.getStatusCode().value() + "");
			}
      
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.add(response.getBody());
			} 
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasPrimeraPagina HttpServerErrorException", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasPrimeraPagina", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		} 
		this.LOGGER.info("[FIN] filtraCorrespondenciasPrimeraPagina");
		return respuesta;
	}
		
	public Respuesta<CorrespondenciaTareaPaginado> filtraCorrespondenciasSiguientePagina(String token, FiltroSiguientePagina filtros) {
		this.LOGGER.info("[INICIO] filtraCorrespondenciasSiguientePagina");
		Respuesta<CorrespondenciaTareaPaginado> respuesta = new Respuesta();
		try {
			String _URL = this.apiUrlCorrespondenciasSiguientePagina;
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			this.LOGGER.info("Filtro:" + filtros.toString());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<ListaFiltroCorrespondencia> entity = new HttpEntity(filtros, (MultiValueMap)headers);
			this.LOGGER.info("[INICIO PETICION REST] filtraCorrespondenciasSiguientePagina");
			ResponseEntity<CorrespondenciaTareaPaginado> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, CorrespondenciaTareaPaginado.class, new Object[0]);
			this.LOGGER.info("[FIN PETICION REST] filtraCorrespondenciasSiguientePagina" + ":" + response.getStatusCode());
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new Exception(response.getStatusCode().value() + "");
			}
      
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.add(response.getBody());
				this.LOGGER.info("Correspondencias recibidas:" + respuesta.datos.get(0).getDetalleCorrespondencias().size());
			} 
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasSiguientePagina HttpServerErrorException", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasSiguientePagina", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		} 
		this.LOGGER.info("[FIN] filtraCorrespondenciasSiguientePagina");
		return respuesta;
	}
	// FIN TICKET
	
	// TICKET 9000004497
	public Respuesta<RespuestaApi> enviarAsignacionGrupal(String token, AsignacionGrupalRequest request) {
	    this.LOGGER.info("[INICIO] enviarAsignacionGrupal");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();    
	    try {
	    	String _URL = this.apiUrlEnviarAsignacionGrupal;
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	this.LOGGER.info("FILTRO:" + request.toString());
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	        HttpEntity<AsignacionGrupalRequest> entity = new HttpEntity(request, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	  	  	  //tikcet - 9000004931
	    	  String errorControlado = response.getBody().getUserMessageError() == null ? "" : response.getBody().getUserMessageError();
	  	  	  if (errorControlado.isEmpty()) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	  	  	  }else{
	  	  	    respuesta.estado = false;
  	  	        respuesta.mensaje = response.getBody().getUserMessageError();
  	  	        respuesta.datos.add(response.getBody());
	  	  	  }
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] enviarAsignacionGrupal", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] enviarAsignacionGrupal", e);
	    } 
	    this.LOGGER.info("[FIN] enviarAsignacionGrupal");
	    return respuesta;
	  }
	// FIN TICKET

	/* 9000004276 - INICIO */
	@Override
	public Respuesta<InformacionDocumento> recuperarDocumentosGeneral(String token, String clase, String correlativo, Locale locale) {
		this.LOGGER.info("[INICIO] recuperarDocumentos " + correlativo);
		Respuesta<InformacionDocumento> respuesta = new Respuesta();
		try {
			clase = URLEncoder.encode(clase, "UTF-8");
			correlativo = URLEncoder.encode(correlativo, "UTF-8");
			
			Object[] parametros = { clase, correlativo };
			String _URL = MessageFormat.format(this.apiUrlRecuperarAdjuntosGeneral, parametros);
			
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("[TOKEN] " + token);
			
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
			
			ResponseEntity<List<InformacionDocumento>> response = restTemplate.exchange(_URL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<InformacionDocumento>>() {
			}, new Object[0]);
			if (response.getStatusCode() != HttpStatus.OK)
				throw new Exception(response.getStatusCode().value() + "");
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.addAll((Collection) response.getBody());
			}
		} catch (HttpServerErrorException ex) {
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
			this.LOGGER.error("[ERROR] recuperarDocumentos", (Throwable) ex);
		} catch (Exception e) {
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
			this.LOGGER.error("[ERROR] recuperarDocumentos", e);
		}
		this.LOGGER.info("[FIN] recuperarDocumentos");
		return respuesta;
	}
	/* 9000004276 - FIN */


	/*INI Ticket 9*4275*/
	@Override
	public Respuesta<ReemplazoConsultaDTO> notificacionReemplazo(String token, String sCodReemplazo) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] notificarReemplazo");
	    Respuesta<ReemplazoConsultaDTO> respuesta = new Respuesta();
	    try {
	        Object[] parametros = { sCodReemplazo };
	        String _URL = MessageFormat.format(this.apiUrlNotificarReemplazos, parametros);
	        this.LOGGER.info("[URL] " + _URL);
	        this.LOGGER.info("[TOKEN] " + token);
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", token);
	        HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);  
	        ResponseEntity<ReemplazoConsultaDTO> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, ReemplazoConsultaDTO.class, new Object[0]);
	        if (response.getStatusCode() != HttpStatus.OK)
	          throw new Exception(response.getStatusCode().value() + ""); 
	        if (response.getStatusCode() == HttpStatus.OK) {
	          respuesta.estado = true;
	          respuesta.mensaje = "200";
	          respuesta.datos.add((ReemplazoConsultaDTO) response.getBody());
	        } 
	      } catch (HttpServerErrorException ex) {
	        RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	        respuesta.estado = false;
	        respuesta.mensaje = error.getMessage();
	        this.LOGGER.error("[ERROR] notificarReemplazo HttpServerErrorException", (Throwable)ex);
	      } catch (Exception e) {
	        respuesta.estado = false;
	        respuesta.mensaje = e.getMessage();
	        this.LOGGER.error("[ERROR] notificarReemplazo", e);
	      } 
	      this.LOGGER.info("[FIN] notificarReemplazo");
	      return respuesta;
	}


	@Override
	public Respuesta<ConductorPaginado> filtraConductorPrimeraPagina(String token, String bandeja,
			ListaFiltroConductor filtros, String tamanio) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] filtraConductorPrimeraPagina");
		Respuesta<ConductorPaginado> respuesta = new Respuesta();
		try {
			Object[] parametros = { bandeja, tamanio };
			String _URL = MessageFormat.format(this.apiUrlConductorPrimeraPagina, parametros);
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			this.LOGGER.info("FILTRO:" + filtros.toString());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<ListaFiltroConductor> entity = new HttpEntity(filtros, (MultiValueMap)headers);
			this.LOGGER.info("[INICIO PETICION REST] filtraCorrespondenciasPrimeraPagina " + bandeja + " - " + tamanio);
			ResponseEntity<ConductorPaginado> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, ConductorPaginado.class, new Object[0]);
			this.LOGGER.info("[FIN PETICION REST] filtraCorrespondenciasPrimeraPagina " + bandeja + ":" + response.getStatusCode());
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new Exception(response.getStatusCode().value() + "");
			}
      
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.add(response.getBody());
			} 
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasPrimeraPagina HttpServerErrorException", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] filtraCorrespondenciasPrimeraPagina", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		} 
		this.LOGGER.info("[FIN] filtraCorrespondenciasPrimeraPagina");
		return respuesta;
	}


	@Override
	public Respuesta<ConductorPaginado> filtraConductorSiguientePagina(String token, FiltroSiguientePagina filtros) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] filtraConductorSiguientePagina");
		Respuesta<ConductorPaginado> respuesta = new Respuesta();
		try {
			String _URL = this.apiUrlConductorSiguientePagina;
			this.LOGGER.info("URL " + _URL);
			this.LOGGER.info("TOKEN " + token);
			this.LOGGER.info("Filtro:" + filtros.toString());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			HttpEntity<FiltroSiguientePagina> entity = new HttpEntity(filtros, (MultiValueMap)headers);
			this.LOGGER.info("[INICIO PETICION REST] filtraConductorSiguientePagina");
			ResponseEntity<ConductorPaginado> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, ConductorPaginado.class, new Object[0]);
			this.LOGGER.info("[FIN PETICION REST] filtraConductorSiguientePagina" + ":" + response.getStatusCode());
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new Exception(response.getStatusCode().value() + "");
			}
      
			if (response.getStatusCode() == HttpStatus.OK) {
				respuesta.estado = true;
				respuesta.mensaje = "200";
				respuesta.datos.add(response.getBody());
				this.LOGGER.info("Conductores recibidas:" + respuesta.datos.get(0).getDetalleConductor().size());
			} 
		} catch (HttpServerErrorException ex) {
			this.LOGGER.error("[ERROR] filtraConductorSiguientePagina HttpServerErrorException", (Throwable)ex);
			RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
			respuesta.estado = false;
			respuesta.mensaje = error.getMessage();
		} catch (Exception e) {
			this.LOGGER.error("[ERROR] filtraConductorSiguientePagina", e);
			respuesta.estado = false;
			respuesta.mensaje = e.getMessage();
		} 
		this.LOGGER.info("[FIN] filtraConductorSiguientePagina");
		return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> reintentarConductor(String token, String workflowId) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] reintentarConductor");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	      Object[] parametros = { workflowId };
	      String _URL = MessageFormat.format(this.apiUrlReintentarConductor, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("TOKEN " + token);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	      this.LOGGER.info(response.getStatusCodeValue() + "");
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.add(response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] reintentarConductor", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      this.LOGGER.error("[ERROR] reintentarConductor", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] enviarAsignacion");
	    return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> saltarPasoConductor(String token, String workflowId) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] saltarPasoConductor");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	      Object[] parametros = { workflowId };
	      String _URL = MessageFormat.format(this.apiUrlAvanzarPasoConductor, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("TOKEN " + token);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	      this.LOGGER.info(response.getStatusCodeValue() + "");
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.add(response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] saltarPasoConductor", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      this.LOGGER.error("[ERROR] saltarPasoConductor", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] saltarPasoConductor");
	    return respuesta;
	}


	@Override
	public Respuesta<RespuestaApi> terminarPasoConductor(String token, String workflowId) {
		// TODO Auto-generated method stub
		this.LOGGER.info("[INICIO] saltarPasoConductor");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	      Object[] parametros = { workflowId };
	      String _URL = MessageFormat.format(this.apiUrlTerminarPasoConductor, parametros);
	      this.LOGGER.info("URL " + _URL);
	      this.LOGGER.info("TOKEN " + token);
	      RestTemplate restTemplate = new RestTemplate();
	      HttpHeaders headers = new HttpHeaders();
	      headers.add("Authorization", token);
	      HttpEntity<String> entity = new HttpEntity((MultiValueMap)headers);
	      ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	      this.LOGGER.info(response.getStatusCodeValue() + "");
	      if (response.getStatusCode() != HttpStatus.OK)
	        throw new Exception(response.getStatusCode().value() + ""); 
	      if (response.getStatusCode() == HttpStatus.OK) {
	        respuesta.estado = true;
	        respuesta.mensaje = "200";
	        respuesta.datos.add(response.getBody());
	      } 
	    } catch (HttpServerErrorException ex) {
	      this.LOGGER.error("[ERROR] saltarPasoConductor", (Throwable)ex);
	      RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	      respuesta.estado = false;
	      respuesta.mensaje = error.getMessage();
	    } catch (Exception e) {
	      this.LOGGER.error("[ERROR] saltarPasoConductor", e);
	      respuesta.estado = false;
	      respuesta.mensaje = e.getMessage();
	    } 
	    this.LOGGER.info("[FIN] saltarPasoConductor");
	    return respuesta;
	}
	
	/*FIN Ticket 9*4275*/
	
	/*INI Ticket 9*4413*/
	public Respuesta<RespuestaApi> rechazarCorrespondenciaMPV(String token, String correlativo, RechazarCorrespondenciaParametro parametros) {
	    this.LOGGER.info("[INICIO] rechazarCorrespondencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	correlativo = URLEncoder.encode(correlativo, "UTF-8");
	    	Object[] _parametros = { correlativo };
	        String _URL = MessageFormat.format(this.apiUrlRechazarCorrespondenciaMPV, _parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<RechazarCorrespondenciaParametro> entity = new HttpEntity(parametros, (MultiValueMap)headers);//new HttpEntity(parametros, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] rechazarCorrespondencia", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] rechazarCorrespondencia", e);
	    } 
	    this.LOGGER.info("[FIN] rechazarCorrespondencia");
	    return respuesta;
	}
	
	
	public Respuesta<RespuestaApi> asignarDependenciaMPV(String token, String correlativo, AsignarDependenciaParametro parametros) {
	    this.LOGGER.info("[INICIO] asignarDependencia");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	correlativo = URLEncoder.encode(correlativo, "UTF-8");
	    	String codigoCGC = URLEncoder.encode(parametros.getCgc(), "UTF-8");
	    	String codigoDependencia = URLEncoder.encode(parametros.getDependencia(), "UTF-8");
	    	String accion = URLEncoder.encode(parametros.getAccion(), "UTF-8");
	    	this.LOGGER.info("correlativo " + correlativo);
	    	this.LOGGER.info("codigoCGC " + codigoCGC);
	    	this.LOGGER.info("codigoDependencia " + codigoDependencia);
	    	this.LOGGER.info("accion " + accion);
	    	Object[] _parametros = { correlativo,codigoCGC,codigoDependencia,accion };
	        String _URL = MessageFormat.format(this.apiUrlAsignarDependenciaaMPV, _parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<AsignarDependenciaParametro> entity = new HttpEntity(parametros, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] asignarDependencia", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] asignarDependencia", e);
	    } 
	    this.LOGGER.info("[FIN] rechazarCorrespondencia");
	    return respuesta;
	}
	/*FIN Ticket 9*4413*/
	
	/*INI Ticket 9000004412*/
	public Respuesta<RespuestaApi> crearExpediente(String token, String nroProceso) {
	    this.LOGGER.info("[INICIO] crearExpediente");
	    Respuesta<RespuestaApi> respuesta = new Respuesta();
	    try {
	    	nroProceso = URLEncoder.encode(nroProceso, "UTF-8");
	    	
	    	Object[] _parametros = { nroProceso};
	        String _URL = MessageFormat.format(this.apiUrlCrearExpediente, _parametros);
	    	this.LOGGER.info("URL " + _URL);
	    	this.LOGGER.info("TOKEN " + token);
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", token);
	    	HttpEntity<AsignarDependenciaParametro> entity = new HttpEntity(_parametros, (MultiValueMap)headers);
	    	ResponseEntity<RespuestaApi> response = restTemplate.exchange(_URL, HttpMethod.POST, entity, RespuestaApi.class, new Object[0]);
	    	if (response.getStatusCode() != HttpStatus.OK)
	    		throw new Exception(response.getStatusCode().value() + ""); 
	    	if (response.getStatusCode() == HttpStatus.OK) {
	    		respuesta.estado = true;
	    		respuesta.mensaje = "200";
	    		respuesta.datos.add(response.getBody());
	    	} 
	    } catch (HttpServerErrorException ex) {
	    	RespuestaError error = obtenerErrorHttp(ex.getResponseBodyAsString());
	    	respuesta.estado = false;
	    	respuesta.mensaje = error.getMessage();
	    	this.LOGGER.error("[ERROR] crearExpediente", (Throwable)ex);
	    } catch (Exception e) {
	    	respuesta.estado = false;
	    	respuesta.mensaje = e.getMessage();
	    	this.LOGGER.error("[ERROR] crearExpediente", e);
	    } 
	    this.LOGGER.info("[FIN] crearExpediente");
	    return respuesta;
	}
	
	/*FIN Ticket 9000004412*/
}
