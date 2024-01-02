package pe.com.petroperu.cliente.model;


public class ErrorResponse
{
  private Long timestamp;
  private Integer status;
  private String error;
  private String exception;
  private String message;
  private String path;
  
  public ErrorResponse() {}
  
  public ErrorResponse(Long timestamp, Integer status, String error, String exception, String path, String message) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.exception = exception;
    this.path = path;
    this.message = message;
  }

  
  public Long getTimestamp() { return this.timestamp; }

  
  public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

  
  public Integer getStatus() { return this.status; }

  
  public void setStatus(Integer status) { this.status = status; }

  
  public String getError() { return this.error; }

  
  public void setError(String error) { this.error = error; }

  
  public String getException() { return this.exception; }

  
  public void setException(String exception) { this.exception = exception; }

  
  public String getPath() { return this.path; }

  
  public void setPath(String path) { this.path = path; }

  
  public String getMessage() { return this.message; }

  
  public void setMessage(String message) { this.message = message; }



  
  public String toString() { return "ErrorResponse [timestamp=" + this.timestamp + ", status=" + this.status + ", error=" + this.error + ", exception=" + this.exception + ", message=" + this.message + ", path=" + this.path + "]"; }
}
