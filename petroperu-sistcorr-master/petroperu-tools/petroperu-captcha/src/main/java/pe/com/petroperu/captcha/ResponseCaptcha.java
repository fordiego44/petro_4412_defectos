package pe.com.petroperu.captcha;

import java.util.Date;
import java.util.List;

public class ResponseCaptcha {

	private boolean success;
	private Date challenge_ts;
	private String hostname;
	private Float score;
	private List<String> errorCodes;
	
	public List<String> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Date getChallenge_ts() {
		return challenge_ts;
	}
	public void setChallenge_ts(Date challenge_ts) {
		this.challenge_ts = challenge_ts;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
}
