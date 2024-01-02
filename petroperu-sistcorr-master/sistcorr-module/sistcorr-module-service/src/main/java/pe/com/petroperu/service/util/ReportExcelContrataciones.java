//9000004276
package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.emision.dto.ContratacionConsultaDTO;

public class ReportExcelContrataciones implements IReport<ByteArrayInputStream> {

	private List<ContratacionConsultaDTO> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;

	public ReportExcelContrataciones(List<ContratacionConsultaDTO> data, String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("NRO_PROC", "Nro Proceso");
			this.columns.put("TIPO_PROC", "Tipo de Proceso");
			this.columns.put("NRO_MEMO", "Número de Memo");
			this.columns.put("DEP", "Dependencia");
			this.columns.put("PER_CONT", "Persona Contacto");
			this.columns.put("CANT_BASES", "Cantidad de Base");
			this.columns.put("VALOR_BASE", "Valor Base");
			this.columns.put("VB_FH_INI", "Venta Base: Fecha y hora Inicio");
			this.columns.put("VB_FH_FIN", "Venta Base: Fecha y hora Fin");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (ContratacionConsultaDTO cont : data) {
				Map<String, Object> _item = new HashMap<>();
				
				_item.put("NRO_PROC", cont.getNroProceso());
				_item.put("TIPO_PROC", cont.getTipoProceso());
				_item.put("NRO_MEMO", cont.getNroMemo());
				_item.put("DEP",  cont.getDependencia());
				_item.put("PER_CONT", cont.getPersonaContacto());
				_item.put("CANT_BASES", cont.getCantBases());
				_item.put("VALOR_BASE", cont.getValorBase());	
				_item.put("VB_FH_INI", cont.getVbFechaHoraInicio() == null? "" : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).format(cont.getVbFechaHoraInicio()));
				_item.put("VB_FH_FIN", cont.getVbFechaHoraFin() == null? "" : new SimpleDateFormat(Utilitario.FORMATO_FECHA_API_1).format(cont.getVbFechaHoraFin()));
				
				this.values.add(_item);
			}
			ExcelReporteContratacion excel = new ExcelReporteContratacion(columns, values, userCreate);
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
