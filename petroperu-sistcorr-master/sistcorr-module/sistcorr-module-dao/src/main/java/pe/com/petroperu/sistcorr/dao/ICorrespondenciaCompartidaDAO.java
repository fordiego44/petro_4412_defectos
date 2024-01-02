package pe.com.petroperu.sistcorr.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.petroperu.model.emision.CorrespondenciaCompartida;

public interface ICorrespondenciaCompartidaDAO extends JpaRepository<CorrespondenciaCompartida, Long> {

}
