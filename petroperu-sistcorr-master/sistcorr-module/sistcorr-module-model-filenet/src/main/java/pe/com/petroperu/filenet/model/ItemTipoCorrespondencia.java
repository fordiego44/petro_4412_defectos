package pe.com.petroperu.filenet.model;

public class ItemTipoCorrespondencia extends ItemFilenet {
	private boolean multipleDestinatario;
	private boolean intena;
	private boolean externa;
	private String destinatario; //adicion 9-3874
	private boolean copia; //adicion 9-3874

	public boolean isMultipleDestinatario() {
		return this.multipleDestinatario;
	}

	public void setMultipleDestinatario(boolean multipleDestinatario) {
		this.multipleDestinatario = multipleDestinatario;
	}

	public boolean isIntena() {
		return this.intena;
	}

	public void setIntena(boolean intena) {
		this.intena = intena;
	}

	public boolean isExterna() {
		return this.externa;
	}

	public void setExterna(boolean externa) {
		this.externa = externa;
	}

	//adicion 9-3874	
	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public boolean isCopia() {
		return copia;
	}

	public void setCopia(boolean copia) {
		this.copia = copia;
	}
	//fin adicion 9-3874
}
