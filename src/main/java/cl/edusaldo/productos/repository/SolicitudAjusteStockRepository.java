package cl.edusaldo.productos.repository;

import cl.edusaldo.productos.model.EstadoAutorizacion;
import cl.edusaldo.productos.model.SolicitudAjusteStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudAjusteStockRepository extends JpaRepository<SolicitudAjusteStock, Long> {
    List<SolicitudAjusteStock> findByEstado(EstadoAutorizacion estado);
}
