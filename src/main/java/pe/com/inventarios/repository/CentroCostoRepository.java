package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.CentroCosto;

public interface CentroCostoRepository extends JpaRepository<CentroCosto, Long>{
	
	@Transactional(readOnly=true)
	@Query(value="select c from #{#entityName} c where c.centrocosto = :ceco")
	public CentroCosto findByCentrocosto(@Param("ceco") Long ceco);
	
	public CentroCosto findByCodigoGestion(Long codigoGestion);
	
	@Query(value="select c.centrocosto as centrocosto, c.denominacion as denominacion, c.gerencia, c.codigoDocumentario from #{#entityName} c")
	public List<Object[]> getAllCentroCosto();

}
