package pe.com.petroperu.util;

public class CaptchaResponse {
	
	private String capatcha;
	private String id;
	
	public CaptchaResponse(String capatcha, String id) {
		this.capatcha = capatcha;
		this.id = id;
	}
	
	public String getCapatcha() {
		return capatcha;
	}
	public void setCapatcha(String capatcha) {
		this.capatcha = capatcha;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
