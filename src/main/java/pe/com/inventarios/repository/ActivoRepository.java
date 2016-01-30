package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.Activo;

public interface ActivoRepository  extends CrudRepository<Activo, Long>{
	
	@Transactional(readOnly=true)
	@Query(value="select a from #{#entityName} a where a.fichaUsuario = :idficha and exists "
			+ "(select 'X' from Clase c where c.anexo = :anexo and a.clase = c.clase) order by activo")
	public List<Activo> getActivosByIdFicha(@Param("idficha") Long nro, @Param("anexo") int anexo);
	
	@Transactional(readOnly=true)
	@Query(value="select a from #{#entityName} a where a.activo = :id and exists "
			+ "(select 'X' from Clase c where c.anexo = :anexo and a.clase = c.clase)")
	public Activo findByActivoAndClase(@Param("id") Long id, @Param("anexo") int anexo);
	
	@Transactional(readOnly=true)
	@Query(value="select count(1) as cantidad from "
			+ "(select iv.emplaz, iv.DEN_EMPLAZ, iv.CECO, iv.EQUIPO_PROYECTO, iv.GERENCIA, iv.FICHA_USUARIO "
			+ " from inventario iv "
			+ " where iv.FICHA_USUARIO = :idficha and iv.ESTADO_REGISTRO = '2' and exists "
			+ " (select 'X' from clase c where c.anexo = :anexo and iv.clase = c.clase)"
			+ " group by iv.emplaz, iv.DEN_EMPLAZ, iv.CECO, iv.EQUIPO_PROYECTO, iv.GERENCIA, iv.FICHA_USUARIO "
			+ " ) t"
			, nativeQuery=true)	
	public int cantidadPrincipalDatosActivo(@Param("idficha") Long ficha, @Param("anexo") int anexo);
	
	@Transactional(readOnly=true)
	@Query(value="select i.* from inventario i inner join clase c on i.clase = c.clase and c.anexo = :anexo "
			+ " where i.ficha_usuario = :idficha and i.estado_registro = '2' LIMIT 1", nativeQuery=true)
	public Activo obtenerPrincipalDatosActivo(@Param("idficha") Long ficha, @Param("anexo") int anexo);
	
	@Transactional(readOnly=true)
	@Query(value="select i.* from inventario i inner join clase c on i.clase = c.clase and c.anexo = :anexo "
			+ " where i.num_sup = :idActivo and i.num_sup <> i.activo ", nativeQuery=true)
	public List<Activo> findByNumSup(@Param("idActivo") Long idActivo, @Param("anexo") int anexo);
	
	@Transactional(readOnly=true)
	public Activo findByActivo(Long activo);
	
}
