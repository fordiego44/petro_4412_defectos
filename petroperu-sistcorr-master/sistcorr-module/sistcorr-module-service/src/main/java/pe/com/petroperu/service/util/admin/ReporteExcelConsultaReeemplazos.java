package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReporteExcelConsultaReeemplazos  implements IReport<ByteArrayInputStream> {
	
	private List<Object[]> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	private String titulo;
	
	public ReporteExcelConsultaReeemplazos(List<Object[]> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	public ReporteExcelConsultaReeemplazos(List<Object[]> data,  String userCreate, String titulo) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.titulo = titulo;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("CORRELATIVO", "Correlativo");
			this.columns.put("NRO_DOCUMENTO", "Nro. Documento");
			this.columns.put("ASUNTO", "Asunto");
			this.columns.put("FECHA_RECEPCION", "Fecha Recepción");
			this.columns.put("REMITENTE", "Remitente");
			this.columns.put("DESTINO", "Destino");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("TIPO_CORRESPONDENCIA", "Tipo Correspondencia");
			this.columns.put("ROL", "Rol");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (int i=0;i<data.size();i++){
				Map<String, Object> _item = new HashMap<>();
				_item.put("CORRELATIVO",data.get(i)[1]!=null?data.get(i)[1].toString():"");
				_item.put("NRO_DOCUMENTO",data.get(i)[4]!=null?data.get(i)[4].toString():"");
				_item.put("ASUNTO",data.get(i)[3]!=null?data.get(i)[3].toString():"");
				_item.put("FECHA_RECEPCION",data.get(i)[2]!=null?data.get(i)[2].toString():"");
				_item.put("REMITENTE",data.get(i)[5]!=null?data.get(i)[5].toString():"");
				_item.put("DESTINO",data.get(i)[6]!=null?data.get(i)[6].toString():"");
				_item.put("ESTADO",data.get(i)[7]!=null?data.get(i)[7].toString():"");
				_item.put("TIPO_CORRESPONDENCIA",data.get(i)[8]!=null?data.get(i)[8].toString():"");
				_item.put("ROL",data.get(i)[9]!=null?data.get(i)[9].toString():"");
				this.values.add(_item);
				
			}
			
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "Administración de  - " + this.titulo);
			excelData = excel.buildReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ByteArrayInputStream getResult() {
		return excelData;
	}

}
