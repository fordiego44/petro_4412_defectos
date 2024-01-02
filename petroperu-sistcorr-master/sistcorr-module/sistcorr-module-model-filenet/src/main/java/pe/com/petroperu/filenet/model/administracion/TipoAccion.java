package pe.com.petroperu.filenet.model.administracion;

public class TipoAccion {
	
	private Integer id;
	private String  codigoAccion;
	private String  nombreAccion;
	private String  mTextoReq;
	private String  mMultipli;
	private String  mReqTextoRta;
	private String  mEnviaMailRta;
	private String  procesos;
	private Integer prioridad;
	
	private String  accion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoAccion() {
		return codigoAccion;
	}

	public void setCodigoAccion(String codigoAccion) {
		this.codigoAccion = codigoAccion;
	}

	public String getNombreAccion() {
		return nombreAccion;
	}

	public void setNombreAccion(String nombreAccion) {
		this.nombreAccion = nombreAccion;
	}

	public String getmTextoReq() {
		return mTextoReq;
	}

	public void setmTextoReq(String mTextoReq) {
		this.mTextoReq = mTextoReq;
	}

	public String getmMultipli() {
		return mMultipli;
	}

	public void setmMultipli(String mMultipli) {
		this.mMultipli = mMultipli;
	}

	public String getmReqTextoRta() {
		return mReqTextoRta;
	}

	public void setmReqTextoRta(String mReqTextoRta) {
		this.mReqTextoRta = mReqTextoRta;
	}

	public String getmEnviaMailRta() {
		return mEnviaMailRta;
	}

	public void setmEnviaMailRta(String mEnviaMailRta) {
		this.mEnviaMailRta = mEnviaMailRta;
	}

	public String getProcesos() {
		return procesos;
	}

	public void setProcesos(String procesos) {
		this.procesos = procesos;
	}

	public Integer getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	@Override
	public String toString() {
		return "TipoAccion [id=" + id + ", codigoAccion=" + codigoAccion + ", nombreAccion=" + nombreAccion
				+ ", mTextoReq=" + mTextoReq + ", mMultipli=" + mMultipli + ", mReqTextoRta=" + mReqTextoRta
				+ ", mEnviaMailRta=" + mEnviaMailRta + ", procesos=" + procesos + ", prioridad=" + prioridad
				+ ", accion=" + accion + "]";
	}


	
}
