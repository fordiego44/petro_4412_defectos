package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReporteExcelFuncionarios  implements IReport<ByteArrayInputStream> {
	
	private List<Object[]> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	private String titulo;
	
	public ReporteExcelFuncionarios(List<Object[]> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	public ReporteExcelFuncionarios(List<Object[]> data,  String userCreate, String titulo) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.titulo = titulo;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("REGISTRO", "Registro");
			this.columns.put("NOMBRES_APELLIDOS", "Nombres y Apellidos");
			this.columns.put("EMAIL", "Email");
			this.columns.put("COD_DEPENDENCIA", "Código Dependencia");
			this.columns.put("NOMBRE_DEPENDENCIA", "Nombre Dependencia");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (int i=1;i<data.size();i++){
				Map<String, Object> _item = new HashMap<>();
				_item.put("REGISTRO",data.get(i)[2]!=null?data.get(i)[2].toString():"");
				_item.put("NOMBRES_APELLIDOS",data.get(i)[3]!=null?data.get(i)[3].toString():"");
				_item.put("EMAIL",data.get(i)[8]!=null?data.get(i)[8].toString():"");
				_item.put("COD_DEPENDENCIA",data.get(i)[9]!=null?data.get(i)[9].toString():"");
				_item.put("NOMBRE_DEPENDENCIA",data.get(i)[10]!=null?data.get(i)[10].toString():"");
				this.values.add(_item);
				
			}
			/*for (Object[] corr : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("FECHA_HORA", String.valueOf(corr[0] + " " + corr[1]));
				_item.put("MODO_COMP", String.valueOf(corr[2]));
				_item.put("COMP_POR", String.valueOf(corr[3]));
				_item.put("DEST", String.valueOf(corr[4]));
				_item.put("COPIA", String.valueOf(corr[5]));
				_item.put("ASUNTO", String.valueOf(corr[6]));
				_item.put("CONT", String.valueOf(corr[7]));
				_item.put("ARCHIV", String.valueOf(corr[8]));
				this.values.add(_item);
			}*/
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "Administración" + (this.titulo == null?" de - FUNCIONARIOS":" de - "+this.titulo));
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
