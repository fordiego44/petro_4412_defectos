package pe.com.petroperu.filenet.model.administracion;

public class TipoCorrespondencia {
	
	private Integer id;
	private Integer codigoTipoCorr;
	private String  nombreTipoCorr;
	private String  mAplicaEnvInterna;
	private String  mAplicaEnvExterna;
	private String  mAplicaRecInterna;
	private String  mAplicaRecExterna;
	private String  mRequiereFecha;
	private String  mFinalizaAceptar;
	private String  mManualCorresp;
	private Integer secuencia;
	private String  mMultiple;
	private String reqCopia;
	private String reqDest;
	private String codigoAccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoTipoCorr() {
		return codigoTipoCorr;
	}
	public void setCodigoTipoCorr(Integer codigoTipoCorr) {
		this.codigoTipoCorr = codigoTipoCorr;
	}
	public String getNombreTipoCorr() {
		return nombreTipoCorr;
	}
	public void setNombreTipoCorr(String nombreTipoCorr) {
		this.nombreTipoCorr = nombreTipoCorr;
	}
	public String getmAplicaEnvInterna() {
		return mAplicaEnvInterna;
	}
	public void setmAplicaEnvInterna(String mAplicaEnvInterna) {
		this.mAplicaEnvInterna = mAplicaEnvInterna;
	}
	public String getmAplicaEnvExterna() {
		return mAplicaEnvExterna;
	}
	public void setmAplicaEnvExterna(String mAplicaEnvExterna) {
		this.mAplicaEnvExterna = mAplicaEnvExterna;
	}
	public String getmAplicaRecInterna() {
		return mAplicaRecInterna;
	}
	public void setmAplicaRecInterna(String mAplicaRecInterna) {
		this.mAplicaRecInterna = mAplicaRecInterna;
	}
	public String getmAplicaRecExterna() {
		return mAplicaRecExterna;
	}
	public void setmAplicaRecExterna(String mAplicaRecExterna) {
		this.mAplicaRecExterna = mAplicaRecExterna;
	}
	public String getmRequiereFecha() {
		return mRequiereFecha;
	}
	public void setmRequiereFecha(String mRequiereFecha) {
		this.mRequiereFecha = mRequiereFecha;
	}
	public String getmFinalizaAceptar() {
		return mFinalizaAceptar;
	}
	public void setmFinalizaAceptar(String mFinalizaAceptar) {
		this.mFinalizaAceptar = mFinalizaAceptar;
	}
	public String getmManualCorresp() {
		return mManualCorresp;
	}
	public void setmManualCorresp(String mManualCorresp) {
		this.mManualCorresp = mManualCorresp;
	}
	public Integer getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}
	public String getmMultiple() {
		return mMultiple;
	}
	public void setmMultiple(String mMultiple) {
		this.mMultiple = mMultiple;
	}
	public String getReqCopia() {
		return reqCopia;
	}
	public void setReqCopia(String reqCopia) {
		this.reqCopia = reqCopia;
	}
	public String getReqDest() {
		return reqDest;
	}
	public void setReqDest(String reqDest) {
		this.reqDest = reqDest;
	}
	public String getCodigoAccion() {
		return codigoAccion;
	}
	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}
	@Override
	public String toString() {
		return "TipoCorrespondencia [id=" + id + ", codigoTipoCorr=" + codigoTipoCorr + ", nombreTipoCorr="
				+ nombreTipoCorr + ", mAplicaEnvInterna=" + mAplicaEnvInterna + ", mAplicaEnvExterna="
				+ mAplicaEnvExterna + ", mAplicaRecInterna=" + mAplicaRecInterna + ", mAplicaRecExterna="
				+ mAplicaRecExterna + ", mRequiereFecha=" + mRequiereFecha + ", mFinalizaAceptar=" + mFinalizaAceptar
				+ ", mManualCorresp=" + mManualCorresp + ", secuencia=" + secuencia + ", mMultiple=" + mMultiple
				+ ", reqCopia=" + reqCopia + ", reqDest=" + reqDest + ", codigoAccion=" + codigoAccion + "]";
	}

}
