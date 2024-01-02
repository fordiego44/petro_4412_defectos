package pe.com.petroperu.service.model;

public class ItemMenu {
	private String url;
	private String texto;
	private Integer cantidad;

	public ItemMenu() {
	}

	public ItemMenu(String url, String texto, Integer cantidad) {
		this.url = url;
		this.texto = texto;
		this.cantidad = cantidad;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int getCantidad() {
		return this.cantidad.intValue();
	}

	public void setCantidad(int cantidad) {
		this.cantidad = Integer.valueOf(cantidad);
	}
}
