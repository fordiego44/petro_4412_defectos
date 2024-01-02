package pe.com.petroperu.util.datatable;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class DataTableResults<T> {
		
		
		private String draw;
		
	
		private String recordsFiltered;
		
	
		private String recordsTotal;

	
		@SerializedName("data")
		List<T> listOfDataObjects;
		
		
		public String getJson() {
			return new Gson().toJson(this);
		}

		
		public String getDraw() {
			return draw;
		}

		
		public void setDraw(String draw) {
			this.draw = draw;
		}

	
		public String getRecordsFiltered() {
			return recordsFiltered;
		}

		
		public void setRecordsFiltered(String recordsFiltered) {
			this.recordsFiltered = recordsFiltered;
		}

		
		public String getRecordsTotal() {
			return recordsTotal;
		}

		
		public void setRecordsTotal(String recordsTotal) {
			this.recordsTotal = recordsTotal;
		}

		
		public List<T> getListOfDataObjects() {
			return listOfDataObjects;
		}

		
		public void setListOfDataObjects(List<T> listOfDataObjects) {
			this.listOfDataObjects = listOfDataObjects;
		}

}