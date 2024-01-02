package pe.com.petroperu.service.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pe.com.petroperu.Utilitario;
import pe.com.petroperu.model.DependenciaUnidadMatricial;
import pe.com.petroperu.model.emision.dto.CorrespondenciaConsultaDTO;

public class ReportExcelDependencias  implements IReport<ByteArrayInputStream> {
	
	private List<DependenciaUnidadMatricial> data;
	private Map<String, String> columns;
	private List<Map<String, Object>> values;
	private ByteArrayInputStream excelData;
	private String userCreate;	
	
	public ReportExcelDependencias(List<DependenciaUnidadMatricial> data,  String userCreate) {
		super();
		this.data = data;
		this.userCreate = userCreate;
	}
	
	

	@Override
	public void prepareRequest() {
		try {
			this.columns = new LinkedHashMap<>();
			this.columns.put("TIPO", "Tipo");
			this.columns.put("TIPOUM", "Tipo Unidad Matricial");
			this.columns.put("CODIGO", "Código");
			this.columns.put("SIGLA", "Siglas");
			this.columns.put("NOMBRE", "Nombre");
			this.columns.put("ESTADO", "Estado");
			this.columns.put("JERARQUIA", "Jerarquía");
			this.columns.put("JEFE", "Jefe");
			this.columns.put("LUGARTRABAJO", "Lugar de Trabajo");
			this.columns.put("DEPENDENCIASUPERIOR", "Dependencia Superior");
			this.columns.put("DESCRIPCIONCARGO", "Descripción de cargo");
			this.columns.put("INTEGRANTES", "Integrantes");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		try {
			this.values = new ArrayList<>();
			for (DependenciaUnidadMatricial dep : data) {
				Map<String, Object> _item = new HashMap<>();
				_item.put("TIPO", dep.getTipo());
				_item.put("TIPOUM", dep.getTipoUnidadMatricial());
				_item.put("CODIGO", dep.getCodigo());
				_item.put("SIGLA", dep.getSiglas());
				_item.put("NOMBRE", dep.getNombre());
				_item.put("ESTADO", dep.getEstado());
				_item.put("JERARQUIA", dep.getJerarquia());
				_item.put("JEFE", dep.getJefe());
				_item.put("LUGARTRABAJO", dep.getLugarTrabajo());
				_item.put("DEPENDENCIASUPERIOR", dep.getDependenciaSuperior());
				_item.put("DESCRIPCIONCARGO", dep.getDescripcionCargo());
				String integrantes = "";
				if(dep.getIntegrantes() != null && dep.getIntegrantes().size() > 0){
					for(int i=0;i<dep.getIntegrantes().size();i++){
						if(i > 0){
							integrantes = integrantes + ", ";
						}
						integrantes = integrantes + dep.getIntegrantes().get(i).getNombreIntegrante();
					}
				}
				_item.put("INTEGRANTES", integrantes);
				this.values.add(_item);
			}
			ExcelReporteDependencia excel = new ExcelReporteDependencia(columns, values, userCreate);
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
