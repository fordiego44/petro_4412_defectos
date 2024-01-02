package pe.com.petroperu.model.emision;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "notificaciones_no_enviadas")
public class NotificacionNoEnviada extends EntidadBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 500)
	private String asunto;
	@Column(nullable = false, length = 500)
	private String destinatarios;
	@Column(nullable = false, length = 100)
	private String de;
	@Column(length = 500)
	private String copiaCc;
	
	@Column(nullable = false, columnDefinition = "text")
	private String cuerpo;
	@Column(nullable = true, length = 1000)
	private String nombreServidor;
	@Column(nullable = true, length = 1000)
	private String nombreArc;
	
	@Column(nullable = false, columnDefinition = "char(1)")
	private Character estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getNombreArc() {
		return nombreArc;
	}

	public void setNombreArc(String nombreArc) {
		this.nombreArc = nombreArc;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}
	
	public String getCopiaCc() {
		return copiaCc;
	}

	public void setCopiaCc(String copiaCc) {
		this.copiaCc = copiaCc;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getNombreServidor() {
		return nombreServidor;
	}

	public void setNombreServidor(String nombreServidor) {
		this.nombreServidor = nombreServidor;
	}

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}
	
}
