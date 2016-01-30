package pe.com.inventarios.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.Foto;

public interface FotoRepository extends JpaRepository<Foto, Long>{
	
	List<Foto> save(Iterable fotos);
	
	@Transactional(readOnly=true)
	Set<Foto> findByActivo(Long activo);

}
