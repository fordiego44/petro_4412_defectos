package pe.com.petroperu.cliente.model;

import java.util.List;


public class ListaFiltroConductor
{
  private List<FiltroConsultaConductor> searchCriteria;
  private List<String> listaWorkflowIds;
  private Integer total;
  
  public List<FiltroConsultaConductor> getSearchCriteria() { return this.searchCriteria; }


  
  public List<String> getListaWorkflowIds() {
	return listaWorkflowIds;
  }



  public void setListaWorkflowIds(List<String> listaWorkflowIds) {
	this.listaWorkflowIds = listaWorkflowIds;
}



public void setSearchCriteria(List<FiltroConsultaConductor> searchCriteria) { this.searchCriteria = searchCriteria; }


  
  public Integer getTotal() { return this.total; }


  
  public void setTotal(Integer total) { this.total = total; }

	@Override
	public String toString() {
		return "ListaFiltroConductor [searchCriteria=" + searchCriteria + ", total=" + total + "]";
	}
}
