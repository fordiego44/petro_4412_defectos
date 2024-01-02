package pe.com.petroperu.cliente.model;

import java.util.List;

import pe.com.petroperu.model.Asignacion;

public class AsignacionGrupalRequest {
	
	private List<Asignacion> workflowIds;

	public List<Asignacion> getWorkflowIds() {
		return workflowIds;
	}

	public void setWorkflowIds(List<Asignacion> workflowIds) {
		this.workflowIds = workflowIds;
	}

}
