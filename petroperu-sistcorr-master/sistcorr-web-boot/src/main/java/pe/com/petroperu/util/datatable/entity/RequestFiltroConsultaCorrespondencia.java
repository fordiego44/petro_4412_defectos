package pe.com.petroperu.util.datatable.entity;

import java.util.List;

import pe.com.petroperu.cliente.model.FiltroConsultaCorrespondencia;
import pe.com.petroperu.util.datatable.DataTableColumnSpecs;

public class RequestFiltroConsultaCorrespondencia extends FiltroConsultaCorrespondencia {
	
	/** The unique id. */
	private String uniqueId;
	
	/** The draw. */
	private String draw;
	
	/** The start. */
	private Integer start;
	
	/** The length. */
	private Integer length;
	
	/** The search. */
	private String search;
	
	/** The regex. */
	private boolean regex;

	/** The columns. */
	private List<DataTableColumnSpecs> columns;
	
	/** The order. */
	private DataTableColumnSpecs order;
	
	/** The is global search. */
	private boolean isGlobalSearch;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	public List<DataTableColumnSpecs> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumnSpecs> columns) {
		this.columns = columns;
	}

	public DataTableColumnSpecs getOrder() {
		return order;
	}

	public void setOrder(DataTableColumnSpecs order) {
		this.order = order;
	}

	public boolean isGlobalSearch() {
		return isGlobalSearch;
	}

	public void setGlobalSearch(boolean isGlobalSearch) {
		this.isGlobalSearch = isGlobalSearch;
	}
	
}
