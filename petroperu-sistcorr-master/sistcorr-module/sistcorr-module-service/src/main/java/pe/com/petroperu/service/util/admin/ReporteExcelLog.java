package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReporteExcelLog  implements IReport<ByteArrayInputStream> {
	
	private List<Object[]> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	private String titulo;
	
	public ReporteExcelLog(List<Object[]> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	public ReporteExcelLog(List<Object[]> data,  String userCreate, String titulo) {
		super();
		this.data = data;
		this.userCreate = userCreate;
		this.titulo = titulo;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("TABLA_AFECTADA", "Tabla Afectada");
			this.columns.put("ACCION_REALIZADA", "Acción Realizada");
			this.columns.put("USUARIO_ACCION", "Usuario que Ejecuta la Acción");
			this.columns.put("FECHA", "Fecha");
			this.columns.put("IDARTEFACTO", "idArtefacto");
			this.columns.put("DETALLE", "Detalle");
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
				_item.put("TABLA_AFECTADA",data.get(i)[2]!=null?data.get(i)[2].toString():"");
				_item.put("ACCION_REALIZADA",data.get(i)[3]!=null?data.get(i)[3].toString():"");
				_item.put("USUARIO_ACCION",data.get(i)[4]!=null?data.get(i)[4].toString():"");
				_item.put("FECHA",data.get(i)[5]!=null?data.get(i)[5].toString():"");
				_item.put("IDARTEFACTO",data.get(i)[6]!=null?data.get(i)[6].toString():"");
				_item.put("DETALLE",data.get(i)[7]!=null?data.get(i)[7].toString():"");
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
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "Administración de Log " + ((this.titulo == null)?(""):("- " + this.titulo)));
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
