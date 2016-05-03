package pe.com.inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.com.inventarios.model.Activo;

public interface ActivoRepository  extends CrudRepository<Activo, Long> {
	
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
//	@Query(value="select i.* from inventario i inner join clase c on i.clase = c.clase and c.anexo = :anexo "
//			+ " where i.num_sup = :idActivo and i.num_sup <> i.activo ", nativeQuery=true)
	@Query(value="select i.* from inventario i "
			+ " where i.num_sup = :idActivo and i.num_sup <> i.activo ", nativeQuery=true)
//	public List<Activo> findByNumSup(@Param("idActivo") Long idActivo, @Param("anexo") int anexo);
	public List<Activo> findByNumSup(@Param("idActivo") Long idActivo);
	
	@Transactional(readOnly=true)
	public Activo findByActivo(Long activo);
	
	@Transactional(readOnly=true)
	public Activo findBySeq2015(String seq); 
	
	@Transactional(readOnly=true)
	@Query(value="SELECT CASE WHEN COUNT(1) > 0 THEN true ELSE false END FROM inventario iv WHERE iv.activo = :activo "
			+ " and exists (select 'X' from clase c where iv.clase = c.clase and c.anexo = :anexo)", nativeQuery=true)
	public int existByActivo(@Param("activo")Long activo, @Param("anexo")String anexo);
	
	@Transactional
	@Modifying
	@Query("update #{#entityName} iv set iv.activo = ?2 where iv.activo = ?1")
	public int setNewActive(Long activo, Long newActivo);
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Modifying
	@Query("update #{#entityName} iv set iv.numSup = ?2 where numSup = ?1")
	public int setNumSupActive(String activo, String newActivo);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.clase = :clase and iv.ESTADO_REGISTRO = '2' and iv.entregable = :entregable and iv.numero_folio between :starFolio and :endFolio "
			+ " order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaClaseEntregableRangFolio(@Param("planilla")int planilla, @Param("clase")String clase, @Param("entregable")String entregable, 
			@Param("starFolio")String starFolio, @Param("endFolio")String endFolio);
	
	@Transactional
	@Query(value=" SELECT iv.* FROM inventario iv FORCE INDEX (activo)  inner join clase c on c.clase = iv.clase "
			+ " where c.anexo = :planilla and iv.clase = :clase and iv.ESTADO_REGISTRO = '2' and iv.entregable = :entregable "
			+ " group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaClaseEntregable(@Param("planilla")int planilla, @Param("clase")String clase, @Param("entregable")String entregable);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.ESTADO_REGISTRO = '2' and iv.entregable = :entregable group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaEntregable(@Param("planilla")int planilla,  @Param("entregable")String entregable);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.clase = :clase and iv.ESTADO_REGISTRO = '2' group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaClase(@Param("planilla")int planilla,  @Param("clase")String clase);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.clase = :clase and iv.ESTADO_REGISTRO = '2' and iv.numero_folio between :starFolio and :endFolio "
			+ " group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaClaseRango(@Param("planilla")int planilla,  @Param("clase")String clase, @Param("starFolio")String starFolio, @Param("endFolio")String endFolio);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.ESTADO_REGISTRO = '2' and iv.entregable = :entregable and iv.numero_folio between :starFolio and :endFolio "
			+ " group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaEntregableRango(@Param("planilla")int planilla,  @Param("entregable")String entregable, @Param("starFolio")String starFolio, @Param("endFolio")String endFolio);
	
	@Transactional
	@Query(value="SELECT iv.*  FROM inventario iv FORCE INDEX (activo)  inner join clase c on iv.clase = c.clase and c.anexo = :planilla "
			+ " where iv.ESTADO_REGISTRO = '2' and iv.numero_folio between :starFolio and :endFolio group by iv.NUMERO_FOLIO, iv.clase order by iv.NUMERO_FOLIO, iv.clase ", nativeQuery=true)
	public List<Activo> findAllPlanillaRango(@Param("planilla")int planilla, @Param("starFolio")String starFolio, @Param("endFolio")String endFolio);
}
