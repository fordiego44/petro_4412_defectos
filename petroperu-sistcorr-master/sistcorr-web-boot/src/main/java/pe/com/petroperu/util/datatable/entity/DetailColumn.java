package pe.com.petroperu.util.datatable.entity;

import java.util.List;

public class DetailColumn {
	
	private String data;
	private String name;
	private String searchable;
	private String orderable;
	private DetailSearch search;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSearchable() {
		return searchable;
	}
	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}
	public String getOrderable() {
		return orderable;
	}
	public void setOrderable(String orderable) {
		this.orderable = orderable;
	}
	public DetailSearch getSearch() {
		return search;
	}
	public void setSearch(DetailSearch search) {
		this.search = search;
	}
	
	

}
