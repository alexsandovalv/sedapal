package pe.com.inventarios.repository;

import org.springframework.data.repository.CrudRepository;

import pe.com.inventarios.model.Emplazamiento;

/**
 * Created by sandovlu on 06/11/2015.
 */
public interface EmplazamientoRepository extends CrudRepository<Emplazamiento, Long>{
	
	Emplazamiento findByCodigo(Long codigo);
	
}
