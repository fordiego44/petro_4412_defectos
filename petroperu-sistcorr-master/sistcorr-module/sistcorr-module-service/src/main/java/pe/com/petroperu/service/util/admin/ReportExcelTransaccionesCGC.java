package pe.com.petroperu.service.util.admin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.filenet.model.administracion.TransaccionesCgc;
import pe.com.petroperu.service.util.ExcelReporteConsulta;
import pe.com.petroperu.service.util.IReport;

public class ReportExcelTransaccionesCGC implements IReport<ByteArrayInputStream>{
	
	private List<TransaccionesCgc> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;
	
	public ReportExcelTransaccionesCGC(List<TransaccionesCgc> data,  String userCreate) {
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
			this.columns.put("TIPO_TRANSA", "Tipo Transacción");
			this.columns.put("CGC_ORIGEN", "CGC Origen");
			this.columns.put("CGC_DESTINO", "CGC Destino");
			this.columns.put("NUMERADOR", "Numerador");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		try {
			this.values = new ArrayList<>();
			for (TransaccionesCgc dat : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("ID", dat.getId()==null?"":dat.getId());
				_item.put("TIPO_TRANSA", dat.getTipoTransaccion()==null?"":dat.getTipoTransaccion());
				_item.put("CGC_ORIGEN", dat.getCgcOrigen()==null?"":dat.getCgcOrigen());
				_item.put("CGC_DESTINO", dat.getCgcDestino()==null?"":dat.getCgcDestino());
				_item.put("NUMERADOR", dat.getCodigoNumerador()==null?"":dat.getCodigoNumerador());

				this.values.add(_item);
			}
			ExcelReporteConsulta excel = new ExcelReporteConsulta(columns, values, userCreate, "REPORTE CONSULTA TRANSACCIONES POR CGC");
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
