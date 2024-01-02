package pe.com.petroperu.cliente.model;


public class RespuestaApi
{
  private Integer status;
  private String userMessage;
  private String userMessageError;
  
  public Integer getStatus() { return this.status; }

  
  public void setStatus(Integer status) { this.status = status; }

  
  public String getUserMessage() { return this.userMessage; }

  
  public void setUserMessage(String userMessage) { this.userMessage = userMessage; }


  public String getUserMessageError() {return userMessageError;}

  public void setUserMessageError(String userMessageError) {this.userMessageError = userMessageError;}

  
  public String toString() { return "RespuestaApi [status=" + this.status + ", userMessage=" + this.userMessage + "]"; }
}
