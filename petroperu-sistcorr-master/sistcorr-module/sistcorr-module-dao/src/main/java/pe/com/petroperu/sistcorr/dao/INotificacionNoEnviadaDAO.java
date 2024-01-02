package pe.com.petroperu.sistcorr.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.petroperu.model.emision.NotificacionNoEnviada;

@Repository
public interface INotificacionNoEnviadaDAO extends JpaRepository<NotificacionNoEnviada, Long> {

	List<NotificacionNoEnviada> findByEstado(Character estado);
}
