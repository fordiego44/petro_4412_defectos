package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.CgcCorrespondencia;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelCGC implements IReport<ByteArrayInputStream>{
	
	private List<CgcCorrespondencia> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelCGC(List<CgcCorrespondencia> data,  String userCreate) {
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
			this.columns.put("COD_CGC", "Código CGC");
			this.columns.put("NOM_CGC", "Nombre CGC");
			this.columns.put("TIPO_ROTULO", "Tipo Rotulo");
			
			this.columns.put("COD_BARRA", "Código Barras");
			this.columns.put("IMPRESORA", "Impresora");
			this.columns.put("TIPO_IMPRESORA", "Tipo Impresora");
			
			this.columns.put("NOM_LUGAR", "Lugar de Trabajo");
			this.columns.put("M_COMPUTARIZADO", "Marca CGC Compu");
			this.columns.put("COD_ERP", "Código ERP");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (CgcCorrespondencia dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("COD_CGC", dat.getCodigoCGC()==null?"":dat.getCodigoCGC());
				_item.put("NOM_CGC", dat.getNombreCGC()==null?"":dat.getNombreCGC());
				_item.put("TIPO_ROTULO", dat.getTipoRotulo()==null?"":dat.getTipoRotulo());
				_item.put("COD_BARRA", dat.getmCodigoBarras()==null?"":dat.getmCodigoBarras());
				_item.put("IMPRESORA", dat.getImpresora()==null?"":dat.getImpresora());
				_item.put("TIPO_IMPRESORA", dat.getTipoImpresora()==null?"":dat.getTipoImpresora());
				_item.put("NOM_LUGAR", dat.getNombreLugar()==null?"":dat.getNombreLugar());
				_item.put("M_COMPUTARIZADO", dat.getmComputarizado()==null?"":dat.getmComputarizado());
				_item.put("COD_ERP", dat.getCodigoERP()==null?"":dat.getCodigoERP());
				
				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA CGC");
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
