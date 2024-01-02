package pe.com.petroperu.model.emision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import pe.com.petroperu.model.EntidadBase;

@Entity
@Table(name = "correlativo")
public class Correlativo extends EntidadBase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	@Column(nullable = false, length = 25)
	private String codLugar;
	@JsonIgnore
	@Column(nullable = false, length = 10)
	private String siglaLugar;
	@JsonIgnore
	@Column(nullable = false, length = 25)
	private String codDependencia;
	@JsonIgnore
	@Column(nullable = false, length = 10)
	private String siglaDependencia;
	@JsonIgnore
	@Column(nullable = false)
	private Long correlativo;
	@JsonIgnore
	@Column(nullable = false)
	private int anio;
	@Column(nullable = true)
	private String codigo;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodLugar() {
		return this.codLugar;
	}

	public void setCodLugar(String codLugar) {
		this.codLugar = codLugar;
	}

	public String getSiglaLugar() {
		return this.siglaLugar;
	}

	public void setSiglaLugar(String siglaLugar) {
		this.siglaLugar = siglaLugar;
	}

	public String getCodDependencia() {
		return this.codDependencia;
	}

	public void setCodDependencia(String codDependencia) {
		this.codDependencia = codDependencia;
	}

	public String getSiglaDependencia() {
		return this.siglaDependencia;
	}

	public void setSiglaDependencia(String siglaDependencia) {
		this.siglaDependencia = siglaDependencia;
	}

	public Long getCorrelativo() {
		return this.correlativo;
	}

	public void setCorrelativo(Long correlativo) {
		this.correlativo = correlativo;
	}

	public int getAnio() {
		return this.anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
