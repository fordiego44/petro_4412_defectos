package pe.com.petroperu.cliente.model;




public class FiltroCorrespondencia
{
  private String fieldId;
  private String operator;
  private String value;
  
  public String getFieldId() { return this.fieldId; }

  
  public void setFieldId(String fieldId) { this.fieldId = fieldId; }

  
  public String getOperator() { return this.operator; }

  
  public void setOperator(String operator) { this.operator = operator; }

  
  public String getValue() { return this.value; }

  
  public void setValue(String value) { this.value = value; }
	
	@Override
	public String toString() {
		return "FiltroCorrespondencia [fieldId=" + fieldId + ", operator=" + operator + ", value=" + value + "]";
	}
}
