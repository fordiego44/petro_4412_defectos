package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReporteExcelReemplazos  implements IReport<ByteArrayInputStream> {
	
	private List<Object[]> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	private String titulo;
	
	public ReporteExcelReemplazos(List<Object[]> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	public ReporteExcelReemplazos(List<Object[]> data,  String userCreate, String titulo) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.titulo = titulo;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("USUARIO_SALIENTE", "Usuario Saliente");
			this.columns.put("USUARIO_ENTRANTE", "Usuario Entrante");
			this.columns.put("ROL", "Rol");
			this.columns.put("DEPENDENCIA", "Dependencia");
			this.columns.put("FECHA_INICIO", "Fecha Inicio");
			this.columns.put("FECHA_TERMINO", "Fecha Fin");
			this.columns.put("ESTADO", "Estado");
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
				_item.put("USUARIO_SALIENTE",data.get(i)[3]!=null?data.get(i)[3].toString():"");
				_item.put("USUARIO_ENTRANTE",data.get(i)[5]!=null?data.get(i)[5].toString():"");
				_item.put("ROL",data.get(i)[6]!=null?data.get(i)[6].toString():"");
				_item.put("DEPENDENCIA",data.get(i)[8]!=null?data.get(i)[8].toString():"");
				_item.put("FECHA_INICIO",data.get(i)[9]!=null?data.get(i)[9].toString():"");
				_item.put("FECHA_TERMINO",data.get(i)[10]!=null?data.get(i)[10].toString():"");
				_item.put("ESTADO",data.get(i)[11]!=null?data.get(i)[11].toString():"");
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
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "Administración de Reemplazos - " + this.titulo);
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
