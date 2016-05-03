package pe.com.inventarios.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pe.com.inventarios.model.Activo;
import pe.com.inventarios.model.ActivoForm;
import pe.com.inventarios.model.CentroCosto;
import pe.com.inventarios.model.Clase;
import pe.com.inventarios.model.Emplazamiento;
import pe.com.inventarios.model.Ficha;
import pe.com.inventarios.model.SeqActivoNuevo;
import pe.com.inventarios.model.SeqRegistro;
import pe.com.inventarios.repository.ActivoRepository;
import pe.com.inventarios.repository.CentroCostoRepository;
import pe.com.inventarios.repository.ClaseRepository;
import pe.com.inventarios.repository.EmplazamientoRepository;
import pe.com.inventarios.repository.FichaRepository;
import pe.com.inventarios.repository.FotoRepository;
import pe.com.inventarios.repository.SeqActivoNuevoRepository;
import pe.com.inventarios.repository.SeqRegistroRepository;
@Service
public class ActivoService{
	
	private ActivoRepository activoRepository;
	private CentroCostoRepository centroCostoRepository;
    private EmplazamientoRepository emplazamientoRepository;
    private FichaRepository fichaRepository;
    private FotoRepository fotoRepository;
    private ClaseRepository claseRepository;
    
    private SeqActivoNuevoRepository seqActivoNuevoRepository;
    private SeqRegistroRepository seqRegistroRepository;
    
    @Autowired
	public ActivoService(CentroCostoRepository centroCostoRepository, ActivoRepository activoRepository,
                         EmplazamientoRepository emplazamientoRepository, FichaRepository fichaRepository,
                         FotoRepository fotoRepository, ClaseRepository claseRepository, 
                         SeqActivoNuevoRepository seqActivoNuevoRepository, SeqRegistroRepository seqRegistroRepository) {
		this.centroCostoRepository = centroCostoRepository;
		this.activoRepository = activoRepository;
        this.emplazamientoRepository = emplazamientoRepository;
        this.fichaRepository = fichaRepository;
        this.fotoRepository = fotoRepository;
        this.claseRepository = claseRepository;
        this.seqActivoNuevoRepository = seqActivoNuevoRepository;
        this.seqRegistroRepository = seqRegistroRepository;
	}

	public Activo buscarActivoXCodigo(Long codigo, Long clase){
		
		Activo activo = null; 
		if(clase.intValue()==4)
			clase = 5L;
		activo = encontrarActivoPorClase(codigo, clase);
		if(activo==null)
			return null;
//		CentroCosto ceco = centroCostoRepository.findByCentroCosto(Long.valueOf(activo.getCecor()));
//		activo.setCentroCosto(ceco);
		
        activo.setEmplazamiento((Emplazamiento) emplazamientoRepository.findByCodigo(Long.valueOf(activo.getEmplaz())));
        if(activo.getFichaUsuario() == null)
        	return activo;
        
        Long nroPers = Long.valueOf(activo.getFichaUsuario());
		activo.setFicha(fichaRepository.findByIdficha(nroPers));
		activo.setFotos(fotoRepository.findByActivo(activo.getActivo()));
		return activo;
	}

	public Activo encontrarActivoPorClase(Long codigo, Long clase) {
		Activo activo;
		activo = activoRepository.findByActivoAndClase(codigo, clase.intValue());
		return activo;
	}
	
	public List<Activo> buscarActivosXActivoSuperior(Long codigo, Long anexo){
		List<Activo> activos = activoRepository.findByNumSup(codigo);
		findFotosByActivos(activos);
		return activos;
	}

    public Map<String, Object> buscarActivoXResponsable(String responsable, Long clase){
    	
    	Map<String, Object> mapResponsable = new HashMap<String, Object>();
    	ActivoForm form = obtenerDatosPrincipalesActivo(responsable, clase);
    	mapResponsable.put("keyResponsable", form);
    	mapResponsable.put("process", form.getProcess());
    	    	
    	List<Activo> activos = getActivosByIdFicha(clase, form.getFicha());
    	findFotosByActivos(activos);
    	mapResponsable.put("activos", activos);
        return mapResponsable;
    }
    
    public void findFotosByActivos(List<Activo> activos){
    	for(Activo activo : activos){
    		activo.setFotos(fotoRepository.findByActivo(activo.getActivo()));
    	}
    }

	public ActivoForm obtenerDatosPrincipalesActivo(String responsable, Long clase) {
		ActivoForm form;
		Activo activo = null;
		Ficha ficha = null;
		ficha = fichaRepository.findByIdficha(Long.valueOf(responsable));
		if(ficha == null)
			return new ActivoForm("new");
		
		int cantidad = activoRepository.cantidadPrincipalDatosActivo(Long.valueOf(responsable), clase.intValue());
		if(cantidad == 1)
			activo = activoRepository.obtenerPrincipalDatosActivo(Long.valueOf(responsable), clase.intValue());
		
    	if(activo ==null){
    		form = new ActivoForm(ficha);
    	}else
    		form = new ActivoForm(activo, ficha.getNombreCompleto());
		return form;
	}

	public List<Activo> getActivosByIdFicha(Long clase, Long ficha) {
		List<Activo> activos = activoRepository.getActivosByIdFicha(ficha, clase.intValue());
		return activos;
	}
    
	public List<Emplazamiento> getAllEmplazmiento(){
		return (List<Emplazamiento>) emplazamientoRepository.findAll();
	}
	
	public CentroCosto getCentroCostoByCC(Long idCC){
		return centroCostoRepository.findByCentrocosto(idCC);
	}
	
	public List<CentroCosto> getAllCentroCosto(){
		List<Object[]> mapCeco = (List<Object[]>) centroCostoRepository.getAllCentroCosto();
		
		List<CentroCosto> cecos = new ArrayList<>();
		CentroCosto ceco = null;
		
		for(Object[] obj : mapCeco){
			ceco = new CentroCosto((Long)obj[0], (String)obj[1]);
			ceco.setGerencia((String)obj[2]);
			ceco.setCodigoDocumentario((String)obj[3]);
			cecos.add(ceco);
		}		
		return cecos;
	}
    
    public Ficha getUserById(String userId){
    	return fichaRepository.findByIdficha(Long.valueOf(userId));
    }
    
    public List<Ficha> getFichasById(String userId){
    	return fichaRepository.findByIdfichalWithLike(Long.valueOf(userId));
    }
    
    public Emplazamiento getEmplazById(String emplazId){
    	return emplazamientoRepository.findOne(Long.valueOf(emplazId));
    }
    
    public List<Clase> clasesForAnexo(Integer anexo){
    	return claseRepository.findByAnexo(anexo);
    }
    
    public Activo findByActivo(Long activo){
    	return activoRepository.findByActivo(activo);
    }

    @Transactional
	public Activo guardar(Activo activo){
    	
    	if(activo.getActivo() == null || (activo.getActivo() == 0L || activo.getActivo() == 4L)){
    		SeqActivoNuevo seqActivoNew = new SeqActivoNuevo(activo.getClase(), activo.getUsername());
    		if(!StringUtils.isEmpty(activo.getActivo_sedapal()))
    			seqActivoNew.setActivo_referencia(activo.getActivo_sedapal());
    		seqActivoNuevoRepository.save(seqActivoNew);
    		if( activo.getActivo() == null || activo.getActivo() == 0L){
    			activo.setId(null);
    			if(null == activo.getFichaUsuario() || !StringUtils.hasLength(activo.getFichaUsuario().toString()))
    			if(!StringUtils.hasLength(activo.getNumSup()) || !!activo.getNumSup().equalsIgnoreCase("0") || activo.getActivo() == 4L)
    				activo.setNumSup(seqActivoNew.getId().toString());
    		}
    		activo.setActivo(seqActivoNew.getId());
    	}
    	if(!activo.getEstado_registro().equals("3") && StringUtils.isEmpty(activo.getNumeroImpresion())){
    		SeqRegistro seqRegistro = new SeqRegistro(activo.getClase());
    		seqRegistroRepository.save(seqRegistro);
    		String impresion = org.apache.commons.lang3.StringUtils.leftPad(seqRegistro.getId().toString(),6,"0");
    		activo.setNumeroImpresion(impresion);
    	}
		if(activo.getFotos() != null && !activo.getFotos().isEmpty()){
			fotoRepository.save(activo.getFotos());
		}		
		activo.setFecha_actualiza(new Date());
		activoRepository.save(activo);
		return activo;
	}
    
    public boolean validateIfExists(Long activo, String planilla){
    	return BooleanUtils.toBoolean(activoRepository.existByActivo(activo, planilla));
    }
    
    public void updateNewActive(Long activo, Long newActivo, String planilla) throws SQLException{ 
    	
    	int up = activoRepository.setNewActive(activo, newActivo);
    	if(planilla.equalsIgnoreCase("4"))
    		activoRepository.setNumSupActive(activo.toString(), newActivo.toString());
    }
    
    public List<Activo> searchbyPlanillaClaseEntregableRango(int planilla, String clase, String entregable, String startFolio, String endFolio){
    	return activoRepository.findAllPlanillaClaseEntregableRangFolio(planilla, clase, entregable, startFolio, endFolio);
    }
    
    public List<Activo> searchbyPlanillaClaseEntregable(int planilla, String clase, String entregable){
    	return activoRepository.findAllPlanillaClaseEntregable(planilla, clase, entregable);
    }
    
    public List<Activo> searchbyPlanillaEntregable(int planilla, String entregable){
    	return activoRepository.findAllPlanillaEntregable(planilla, entregable);
    }
    
    public List<Activo> searchbyPlanillaClase(int planilla, String clase){
    	return activoRepository.findAllPlanillaClase(planilla, clase);
    }
    
    public List<Activo> searchbyPlanillaEntregableRango(int planilla, String entregable, String startFolio, String endFolio){
    	return activoRepository.findAllPlanillaEntregableRango(planilla, entregable, startFolio, endFolio);
    }
    
    public List<Activo> searchbyPlanillaClaseRango(int planilla, String clase, String startFolio, String endFolio){
    	return activoRepository.findAllPlanillaClaseRango(planilla, clase, startFolio, endFolio);
    }
}
