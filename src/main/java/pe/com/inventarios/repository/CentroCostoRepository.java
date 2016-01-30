package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pe.com.inventarios.model.CentroCosto;

public interface CentroCostoRepository extends JpaRepository<CentroCosto, Long>{
	
//	public CentroCosto findByCentrocosto(Long ceco);
	
	public CentroCosto findByCodigoGestion(Long codigoGestion);
	
	@Query(value="select c.centrocosto as centrocosto, c.denominacion as denominacion, c.gerencia, c.codigoDocumentario from #{#entityName} c")
	public List<Object[]> getAllCentroCosto();

}
