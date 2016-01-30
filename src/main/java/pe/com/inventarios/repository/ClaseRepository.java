package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.Clase;

public interface ClaseRepository extends JpaRepository<Clase, Long>{
	
	@Transactional(readOnly=true)
	List<Clase> findByAnexo(Integer anexo);

}
