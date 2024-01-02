package pe.com.petroperu.cliente.model;

import java.util.List;


public class ListaFiltroCorrespondencia
{
  private List<FiltroCorrespondencia> searchCriteria;
  private Integer total;
  
  public List<FiltroCorrespondencia> getSearchCriteria() { return this.searchCriteria; }


  
  public void setSearchCriteria(List<FiltroCorrespondencia> searchCriteria) { this.searchCriteria = searchCriteria; }


  
  public Integer getTotal() { return this.total; }


  
  public void setTotal(Integer total) { this.total = total; }

	@Override
	public String toString() {
		return "ListaFiltroCorrespondencia [searchCriteria=" + searchCriteria + ", total=" + total + "]";
	}
}
