package pe.com.petroperu.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.petroperu.filenet.dao.IFilenetDAO;
import pe.com.petroperu.filenet.model.ItemFilenet;
import pe.com.petroperu.model.emision.Correspondencia;
import pe.com.petroperu.model.emision.RutaAprobacion;
import pe.com.petroperu.service.IRutaAprobacionService;
import pe.com.petroperu.sistcorr.dao.IRutaAprobacionDAO;

@Service
public class RutaAprobacionServiceImpl implements IRutaAprobacionService {
	
	@Autowired
	private IRutaAprobacionDAO rutaAprobacionDAO;
	
	@Autowired
	private IFilenetDAO filenetDAO;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public List<RutaAprobacion> obtenerRutaAprobacion(Correspondencia correspondencia) {
		List<RutaAprobacion> aprobadores = rutaAprobacionDAO.findAllByCorrespondencia(correspondencia);
		for(int i=0;i<aprobadores.size();i++){
			RutaAprobacion ra = aprobadores.get(i);
			LOGGER.info("Ruta:" + ra.getId() + " - Firmante:" + ra.getFirmante() + " - Tipo:" + ra.getTipoFirmante());
			if(ra.getFirmante()==null){
				ra.setEstado("Pendiente");
				// TCKT 9000003997 - DEFECTO
				if(ra.getTipoFirmante().equals("1")){
					LOGGER.info("Actualizando jefe de dependencia");
					String codDependencia = ra.getCodDependencia();
					ItemFilenet firmante = filenetDAO.obtenerFirmante(codDependencia);
					ra.setUsuario(firmante.getCodigo());
					ra.setUsuarioNombre(firmante.getDescripcion());
					LOGGER.info("Actualizando " + ra.getCodDependencia() + ": " + "(" + ra.getUsuario() + ")" + ra.getUsuarioNombre()); 
					LOGGER.info("RutaAprobacionServiceImpl Linea 43");
					ra = rutaAprobacionDAO.save(ra);
				}
				// FIN TCKT
			}else{
				ra.setEstado(ra.getFirmante().getEstadoDescripcion());
			}
			if(ra.getTipoFirmante().equalsIgnoreCase("1")){
				ra.setNombreTipo("Jefe Dependencia");
			}else{
				ra.setNombreTipo("Participante");
			}
			aprobadores.set(i, ra);
		}
		return aprobadores;
	}

}
