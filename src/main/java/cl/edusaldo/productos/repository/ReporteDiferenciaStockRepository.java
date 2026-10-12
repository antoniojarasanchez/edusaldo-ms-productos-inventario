package cl.edusaldo.productos.repository;

import cl.edusaldo.productos.model.EstadoReporte;
import cl.edusaldo.productos.model.ReporteDiferenciaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteDiferenciaStockRepository extends JpaRepository<ReporteDiferenciaStock, Long> {
    List<ReporteDiferenciaStock> findByEstado(EstadoReporte estado);
}
