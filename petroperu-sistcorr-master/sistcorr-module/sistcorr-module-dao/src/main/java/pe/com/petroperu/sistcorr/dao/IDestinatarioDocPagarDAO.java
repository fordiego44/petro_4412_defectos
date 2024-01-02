package pe.com.petroperu.sistcorr.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.petroperu.model.emision.DestinatarioDocPagar;

public interface IDestinatarioDocPagarDAO extends JpaRepository<DestinatarioDocPagar, Long> {

}
