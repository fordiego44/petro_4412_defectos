package pe.com.petroperu.service;

import java.util.List;

import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.RutaAprobacion;

public interface IRutaAprobacionService {
	
	List<RutaAprobacion> obtenerRutaAprobacion(Correspondencia correspondencia);

}
