package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pe.com.petroperu.model.emision.TipoEmision;

public interface ITipoEmisionDAO extends CrudRepository<TipoEmision, Integer>{

	List<TipoEmision> findAll();
}
