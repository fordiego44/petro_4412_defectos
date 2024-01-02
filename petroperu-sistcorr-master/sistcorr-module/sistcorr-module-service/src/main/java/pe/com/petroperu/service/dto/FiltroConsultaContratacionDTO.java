//9000004276
package pe.com.petroperu.service.dto;

public class FiltroConsultaContratacionDTO {

	private String nroProceso;
	private String tipoProceso;
	private String nroMemo;
	private Integer codDependencia;

	@Override
	public String toString() {
		return "FiltroConsultaContratacionDTO [nroProceso=" + nroProceso + ", tipoProceso=" + tipoProceso + ", nroMemo=" + nroMemo
				+ ", codDependencia=" + codDependencia + "]";
	}

	public String getNroProceso() {
		nroProceso = nroProceso == null ? "" : nroProceso;
		return nroProceso;
	}

	public void setNroProceso(String nroProceso) {
		this.nroProceso = nroProceso;
	}

	public String getTipoProceso() {
		tipoProceso = tipoProceso == null ? "" : tipoProceso;
		return tipoProceso;
	}

	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	public String getNroMemo() {
		nroMemo = nroMemo == null ? "" : nroMemo;
		return nroMemo;
	}

	public void setNroMemo(String nroMemo) {
		this.nroMemo = nroMemo;
	}

	public Integer getCodDependencia() {
		codDependencia = codDependencia == null ? 0 : codDependencia;
		return codDependencia;
	}

	public void setCodDependencia(Integer codDependencia) {
		this.codDependencia = codDependencia;
	}

}
