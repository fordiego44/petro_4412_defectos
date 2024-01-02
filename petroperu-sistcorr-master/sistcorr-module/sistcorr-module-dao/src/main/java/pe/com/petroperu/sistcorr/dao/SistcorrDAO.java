package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import pe.com.petroperu.model.Estado;

public interface SistcorrDAO {
	
	/**
	 * 
	 * @param bandeja
	 * @param usuario
	 * @return
	 */
	Long contadorBandeja(String bandeja, String usuario);
	
	List<Estado> listarEstados(String tipo);

}
