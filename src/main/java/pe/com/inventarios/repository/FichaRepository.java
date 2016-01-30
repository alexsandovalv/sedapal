package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.Ficha;

public interface FichaRepository extends CrudRepository<Ficha, Long>{

	
	@Transactional(readOnly=true)
	public Ficha findByIdficha(Long ficha);
	
	
	@Query(value="select f from Ficha f where f.idficha like CONCAT(:idfic, '%') ")
	public List<Ficha> findByIdfichalWithLike(@Param("idfic") Long ficha);
	
	
}
