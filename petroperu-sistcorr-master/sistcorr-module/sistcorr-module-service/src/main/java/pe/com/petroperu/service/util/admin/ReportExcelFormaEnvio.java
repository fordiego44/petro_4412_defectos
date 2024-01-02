package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.FormaEnvio;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelFormaEnvio  implements IReport<ByteArrayInputStream>{
	
	private List<FormaEnvio> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelFormaEnvio(List<FormaEnvio> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		// TODO Auto-generated method stub
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("ID", "id");
			this.columns.put("COD_FORMA_ENVIO", "Código Forma Envío");
			this.columns.put("DES_FROMA_ENVIO", "Descripcion");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (FormaEnvio dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_FORMA_ENVIO", dat.getCodigoFormaEnvio()==null?"":dat.getCodigoFormaEnvio());
				_item.put("DES_FROMA_ENVIO", dat.getDescripcion()==null?"":dat.getDescripcion());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA FORMAS DE ENVIO");
			excelData = excel.buildReport();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
	}

	@Override
	public ByteArrayInputStream getResult() {
		// TODO Auto-generated method stub
		return excelData;
	}
	
	

}
