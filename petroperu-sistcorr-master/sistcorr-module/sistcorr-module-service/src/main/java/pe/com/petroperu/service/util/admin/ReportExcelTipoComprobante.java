package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.TipoComprobante;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelTipoComprobante implements IReport<ByteArrayInputStream>{
	
	private List<TipoComprobante> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelTipoComprobante(List<TipoComprobante> data,  String userCreate) {
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
			this.columns.put("COD_COMPROBANTE", "Código Comprobante");
			this.columns.put("NOM_COMPROBANTE", "Nombre Comprobante");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (TipoComprobante dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_COMPROBANTE", dat.getCodigoComprobante()==null?"":dat.getCodigoComprobante());
				_item.put("NOM_COMPROBANTE", dat.getNombreComprobante()==null?"":dat.getNombreComprobante());
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA TIPOS DE COMPROBANTES");
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
