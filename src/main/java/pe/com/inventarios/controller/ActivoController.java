package pe.com.inventarios.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import pe.com.inventarios.exception.ActivoConflictException;
import pe.com.inventarios.exception.ActivoNotFoundException;
import pe.com.inventarios.model.Activo;
import pe.com.inventarios.model.ActivoForm;
import pe.com.inventarios.model.CurrentUser;
import pe.com.inventarios.model.Foto;
import pe.com.inventarios.util.MessageHelper;
import pe.com.inventarios.util.NullAwareBeanUtilsBean;

@Controller
@RequestMapping("/activo")
public class ActivoController extends BaseController{

	private JasperReportsPdfView reportsPdfView;
    private DataSource dbsoruce;

    private String URL_FRAGMENT_FORM = "activo/formularioAnexo";
    private final String TABLE_ITEMACTIVOS = " :: itemActivos";
    private String URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS="";
    private final String URL_ACTIVO_PARENT = "redirect:/activo";
    private final String URL_REDIRECT_SUCCESFUL = "redirect:/activo/successful";
    
    private static BeanUtilsBean notNull;
    
    @Autowired
    public ActivoController(JasperReportsPdfView reportsPdfView, DataSource dbsoruce) {
        this.reportsPdfView =reportsPdfView;
        this.dbsoruce = dbsoruce;
        notNull=new NullAwareBeanUtilsBean();
    }

    @RequestMapping(value={"","/"}, method=RequestMethod.GET)
	public ModelAndView inicio(HttpSession session){
    	session.removeAttribute("activo");
    	session.removeAttribute("activos");
    	session.removeAttribute("removeActivos");
		ModelAndView mav = new ModelAndView("activo/formularioBuscar");
		return mav;
	}
	
	@RequestMapping(value="/buscar", method=RequestMethod.POST)
	public ModelAndView buscar(@RequestParam(required=false) String codigo, @RequestParam(value="cboAnexo", required=false)String clase,
				@RequestParam(required=false) String responsable, @RequestParam(required=false) String folio,
				HttpSession session, RedirectAttributes ra){
        Activo unActivo=null;
        ModelAndView mav = new ModelAndView();
        List<Activo> activos = new ArrayList<>();
        if(StringUtils.hasText(codigo)){
        	
        	Long anexo = Long.valueOf(clase);
        	
            unActivo = activoService.buscarActivoXCodigo(Long.valueOf(codigo), anexo);
            if(unActivo==null){
            	unActivo = activoService.findByActivo(Long.valueOf(codigo));
            	if(unActivo == null){
            		unActivo = new Activo();
            		unActivo.setActivo_sedapal(Long.valueOf(codigo));
            		mav.addObject("process", "new");
//            		if(anexo.intValue() == 4)
//            			unActivo.setActivo(anexo);
            	}else{
            		MessageHelper.addErrorAttribute(ra, "activo.duplicate", unActivo.getClase());
            		mav.setViewName(URL_ACTIVO_PARENT);
            		return mav;
            	}            		
            }else{
            	if(anexo.intValue() == 4){
            		mav.addObject("process", "update");
            		activos = activoService.buscarActivosXActivoSuperior(unActivo.getActivo(), anexo);
            	}
            }
            
            if(anexo.intValue() == 4 || anexo.intValue() == 5)
        		mav.addObject("cecos", centroCostos());
       		mav.addObject("emplzamientos", emplazamientos());
            
            mav.setViewName("activo/formularioAnexo"+clase);
            mav.addObject("activo", unActivo);
            session.setAttribute("activo", unActivo);
            
        }else if(StringUtils.hasText(responsable)){
        	Map<String, Object> mapResponsable;
        	clase = "3";
        	mapResponsable = activoService.buscarActivoXResponsable(responsable, Long.valueOf(clase));
        		
            mav.setViewName("activo/formularioAnexo3");
            mav.addAllObjects(mapResponsable);
            mav.addObject("emplzamientos", emplazamientos());
            activos = (List<Activo>)mapResponsable.get("activos");
            session.setAttribute("activo", (Activo)new Activo((ActivoForm)mapResponsable.get("keyResponsable")));
        }else if(StringUtils.hasText(folio)){
        	return new ModelAndView("redirect:/activo/printPDFFolio/"+clase+'/'+folio);
        }
        
        session.setAttribute("activos", activos);
        session.setAttribute("removeActivos", new ArrayList<Activo>());
        mav.addObject("anexo", clase);
		return mav;
	}
	
	@RequestMapping(value="/guardar", method={RequestMethod.POST, RequestMethod.PUT})
	public String guardarActivo(@ModelAttribute Activo activo, RedirectAttributes ra, @RequestParam(value="anexo")String clase,
			@RequestParam(value="file") MultipartFile[] files, @RequestParam(value="imageIds") String imageIds,
			@AuthenticationPrincipal CurrentUser currentUser, HttpSession session) 
					throws IllegalAccessException, InvocationTargetException, FileNotFoundException, IOException{
		Activo activonew = null;
		
		Activo unActivo = (Activo)session.getAttribute("activo");
    	if(StringUtils.hasText(unActivo.getUsername())){
    		activo.setUsername_actualiza(currentUser.getUsername());
    		activo.setFecha_actualiza(new Date());
    	}else{
    		activo.setUsername(currentUser.getUsername());
    		activo.setFecha_registro(new Date());
    	}
		
		if(session.getAttribute("activo") != null)
			unActivo = (Activo)session.getAttribute("activo");
		
		notNull.copyProperties(unActivo, activo);
		Set<Foto> fotos = addImages(files, imageIds, unActivo);
		unActivo.setFotos(fotos);
		unActivo.setEstado_registro("2");
		unActivo.setUsername(currentUser.getUsername());
		activonew = activoService.guardar(unActivo);
		
		ra.addFlashAttribute("anexo",clase);
		ra.addFlashAttribute("activo",activonew);
		MessageHelper.addInfoAttribute(ra, "message.sussesful", activo.getNombre_denominacon());
		
		return URL_REDIRECT_SUCCESFUL;
	}

	@RequestMapping(value="/successful")
	public String successful(@ModelAttribute Activo activo, 
				Model model, HttpSession session){
		session.removeAttribute("activo");
    	session.removeAttribute("activos");
    	session.removeAttribute("removeActivos");
    	
		Activo unActivo=null;
		List<Activo> activos = new ArrayList<>();
		String clase = (String) model.asMap().get("anexo");
		Long anexo = Long.valueOf(clase);
		
		unActivo = activoService.buscarActivoXCodigo(activo.getActivo(), anexo);
		
		session.setAttribute("activo", unActivo);
		model.addAttribute("emplzamientos", emplazamientos());
		model.addAttribute("process", "update");
		if(anexo.intValue() == 4){
    		activos = activoService.buscarActivosXActivoSuperior(unActivo.getActivo(), anexo);
    	}else if(anexo.intValue() == 3){
    		Map<String, Object> mapResponsable;
        	mapResponsable = activoService.buscarActivoXResponsable(activo.getFichaUsuario().toString(), anexo);
        	model.addAllAttributes(mapResponsable);
            activos = (List<Activo>)mapResponsable.get("activos");
            session.setAttribute("activo", (Activo)new Activo((ActivoForm)mapResponsable.get("keyResponsable")));
		}
		
		if(anexo.intValue() == 4 || anexo.intValue() == 5)
			model.addAttribute("cecos", centroCostos());
		
		model.addAttribute("activo", unActivo);
		session.setAttribute("activos", activos);
		session.setAttribute("removeActivos", new ArrayList<Activo>());
		return URL_FRAGMENT_FORM+anexo;
	}
	
	@RequestMapping(value = "/printPDF/{anexo}/{idActivo}", method = RequestMethod.GET)
	public ModelAndView printPDF(ModelMap parameterMap, HttpServletRequest req, 
				@PathVariable String anexo, @PathVariable String idActivo) throws IOException {
		
		String path = "";
		String subReport = "";
		
		int tipoAnexo = Integer.parseInt(anexo);
		path = getFilePath("/static/image/logo-sedapal.png");
		
		parameterMap.put("datasource", dbsoruce);		
		parameterMap.put("pathLogo", path);
		
		if(tipoAnexo==3){
			parameterMap.put("idFichaUsuario", Long.valueOf(idActivo));
			subReport = getFilePath("/static/reportes/subreporte/anexo3_subreport_A4_Landscape.jasper");			
		}else if(tipoAnexo==4){
			subReport = getFilePath("/static/reportes/subreporte/anexo4_subreport_A4_Landscape.jasper");
			parameterMap.put("idNumSup", Long.valueOf(idActivo));
		}else{
			parameterMap.put("idActivo", Long.valueOf(idActivo));
		}		
		parameterMap.put("format", "pdf");
		parameterMap.put("pathSubReport", subReport);
		
		ModelAndView modelAndView = new ModelAndView("Anexo"+tipoAnexo+"_Landscape",parameterMap);
		return modelAndView;
	}
	
	@RequestMapping(value = "/printPDFFolio/{anexo}/{numFolio}", method = RequestMethod.GET)
	public ModelAndView printPDFFolio(HttpServletRequest req, 
				@PathVariable String anexo, @PathVariable String numFolio) throws Exception {
//		String path = "";
//		String subReport = "";
//		
		int tipoAnexo = Integer.parseInt(anexo);
//		path = getFilePath("/static/image/logo-sedapal.png");
//		
		Map<String, Object> map = configMapPrinter(tipoAnexo, numFolio);
		map.put("datasource", dbsoruce);
//		parameterMap.put("pathLogo", path);
//		
//		if(tipoAnexo==3){
//			parameterMap.put("numeroFolio", numFolio);
//			subReport = getFilePath("/static/reportes/subreporte/anexo3_subreport_folio.jasper");			
//		}else if(tipoAnexo==4){
//			subReport = getFilePath("/static/reportes/subreporte/anexo4_subreport_folio.jasper");
//			parameterMap.put("numeroFolio", numFolio);
//		}else{
//			parameterMap.put("numeroFolio", numFolio);
//		}
		
		
		map.put("format", "pdf");
		
		ModelAndView modelAndView = new ModelAndView("Anexo"+tipoAnexo+"_folio",map);
		return modelAndView;
	}
	
    @RequestMapping(value="/obtenerActivo", method=RequestMethod.POST)
	public String obtenerActivo(@RequestParam(defaultValue="0") String codigo, @RequestParam String anexo, Model model, 
				HttpSession session, @RequestParam(defaultValue="false") String flagAccept){
    	Activo unActivo=null;
//    	unActivo = activoService.encontrarActivoPorClase(Long.valueOf(codigo), Long.valueOf(anexo));
    	unActivo = activoService.findByActivo(Long.valueOf(codigo));
    	boolean accept = BooleanUtils.toBoolean(flagAccept);
    		
    	if(unActivo == null){
    		throw new ActivoNotFoundException("Activo no encontrado.");
    	}
    	if(unActivo.getEstado().equals("2") && !accept){
    		throw new ActivoConflictException("Activo "+codigo+" ya se encuentra conciliado.");
    	}
    	
    	Activo activoSession = null;
    	if(null != session.getAttribute("activo")){
    		activoSession = (Activo)session.getAttribute("activo");
    		//unActivo.setNumSup(activoSession.getNumSup());
    	}
    	
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    	if(!isActivoDuplicate(codigo, activos)){
    		
    		if(anexo.equalsIgnoreCase("3")){
	    		if(null != unActivo.getFichaUsuario()){
	    	    	if( unActivo.getFichaUsuario().intValue()>0 && !accept &&
	    	    			unActivo.getEstado_registro().equalsIgnoreCase("2") &&
	    	    			(activoSession.getFichaUsuario().intValue() != unActivo.getFichaUsuario().intValue())){
	    	    		throw new ActivoConflictException("Activo " +codigo+" pertenece a otra ficha de usuario (" + unActivo.getFichaUsuario().toString() +").");
	    	    	}
	        	}
    		}else if(anexo.equalsIgnoreCase("4")){
    			if(org.apache.commons.lang3.StringUtils.isNotBlank(unActivo.getNumSup()) && 
    					org.apache.commons.lang3.StringUtils.isNotBlank(activoSession.getNumSup()) && (
    					!activoSession.getNumSup().equalsIgnoreCase(unActivo.getNumSup()) && 
    					!accept &&
    	    			unActivo.getEstado_registro().equalsIgnoreCase("2"))){
    				throw new ActivoConflictException("Activo " +codigo+" pertenece a otro numero superior (" + unActivo.getNumSup() +").");
    			}
    			unActivo.setNumSup(activoSession.getNumSup());
    		}
    		
    		unActivo.setEstado_registro("2");
    		activos.add(unActivo);
    	}else
    		throw new ActivoConflictException("Activo ya se encuentra en la lista.");
    	
    	removeOfSessionActivesRemoves(unActivo, session);
    	model.addAttribute("anexo", anexo);
    	refreshTableForAnexo(anexo);
    	return URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS;
    }

	@RequestMapping(value={"/obtenerActivo/{idActivo}/edit/", "/obtenerActivo/{idActivo}"})
    public String modificarActivoVer(@PathVariable(value="idActivo") String codigo, 
    			Model model, HttpSession session){
    	String urlModalActivo = "fragment/showActivo :: modalActivo";
    	if(codigo.equals("0")){
    		model.addAttribute("activo", new Activo());
    		return urlModalActivo;
    	}
    	Activo unActivo = getActivoOfSession(codigo, session);
    	if(unActivo == null)
    		return null;
    	model.addAttribute("activo", unActivo);
    	model.addAttribute("process", "edit");
    	model.addAttribute("codigoEditable", codigo);
    	return urlModalActivo;
    }
    
    @RequestMapping(value="/obtenerActivo/{idActivo}/remove/{anexo}")
    public String removeActivo(@PathVariable(value="idActivo") String codigo, @PathVariable(value="anexo") String anexo, 
    			HttpSession session, Model model){
    	Activo unActivo = null;
    	List<Activo> codigoActivosRemove = null;
    	List<Activo> removeActivosById = null;
    	if(org.apache.commons.lang3.StringUtils.contains(codigo, ",")){
    		codigoActivosRemove = getActivosOfSession(codigo.split(","), session);
    	}else
    		unActivo = getActivoOfSession(codigo, session);
    	
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    	List<Activo> removeActivos = (List<Activo>)session.getAttribute("removeActivos");
    	
    	if(unActivo != null){
    		activos.remove(unActivo);
    		
    		unActivo.setEstado_registro("3");
        	unActivo.setFecha_actualiza(new Date());
        	unActivo.setFecha_registro(new Date());
        	unActivo.setNumeroImpresion(null);
        	unActivo.setNumero_folio(null);
        	if(anexo.equals("4")){
        		unActivo.setNumSup("");
        	}else{
        		unActivo.setFichaUsuario(null);
        	}
        	removeActivos.add(unActivo);
    	}else if(!codigoActivosRemove.isEmpty()){
    		removeActivosById =  getActivosOfSession(codigo.split(","), session, true, anexo);
    		removeActivos.addAll(removeActivosById);
    		activos.removeAll(codigoActivosRemove);
    	}
    	
    	refreshTableForAnexo(anexo);
    	model.addAttribute("anexo", anexo);
    	return URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS;
    }

    @RequestMapping(value="/save/{idActivo}/edit/{anexo}", method={RequestMethod.PUT, RequestMethod.POST})
    public String modificarActivoSave(@ModelAttribute Activo activo, HttpSession session, 
    		@PathVariable(value="idActivo") String idActivo, @PathVariable(value="anexo") String anexo, 
    		@AuthenticationPrincipal CurrentUser currentUser, @RequestParam(value="file") MultipartFile[] files, @RequestParam(value="imageIds") String imageIds)
    		throws IllegalAccessException, InvocationTargetException, FileNotFoundException, IOException{
    	
    	Activo unActivo = null;
    	
    	if(anexo.equalsIgnoreCase("4") && (activo.getActivo().intValue() == 0 || activo.getActivo().intValue() == 4))
			activo.setActivo(Long.valueOf(anexo));
    	if(!StringUtils.isEmpty(idActivo)){ //edit
    		activo.setUsername_actualiza(currentUser.getUsername());
    		unActivo = getActivoOfSession(idActivo, session);
    		Set<Foto> fotos = addImages(files, imageIds, unActivo);
    		if(fotos.size() > 0 ){
    			for(Foto fotoLocal : fotos){
    				if(fotoLocal.getId() == null)
    					unActivo.getFotos().add(fotoLocal);
    				else
    					for(Foto foto : unActivo.getFotos()){
    						if(foto.getId().intValue() == fotoLocal.getId().intValue()){
    							foto = fotoLocal;
    							break;
    						}
    					}
    			}
    			
    		}
    		unActivo.setClase(activo.getClase());
    		notNull.copyProperties(unActivo, activo);
    	}else{
    		activo.setUsername(currentUser.getUsername());
    		List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    		activos.add(activo);
    	}
    	
    	refreshTableForAnexo(anexo);
    	return URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS;
    }
    
    @RequestMapping(value="/save/new/{anexo}", method=RequestMethod.POST)
    public String nuevoActivoSave(@ModelAttribute Activo activo, HttpSession session, 
    		@RequestParam(value="file") MultipartFile[] files, @RequestParam(value="imageIds") String imageIds,
    		@PathVariable(value="anexo") String anexo)
    		throws IllegalAccessException, InvocationTargetException, FileNotFoundException, IOException{
    	
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
		activo.setEstado_registro("2");
		activos.add(activo);
		
//		if(anexo.equalsIgnoreCase("4"))
//			activo.setActivo(Long.valueOf(anexo));
		
		refreshTableForAnexo(anexo);
    	return URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS;
    }
    
    @RequestMapping(value="/save/ficha/activos", method={RequestMethod.POST, RequestMethod.PUT})
    public String guardarFichaPorActivos(@ModelAttribute ActivoForm activoForm, HttpSession session, 
    			@RequestParam(value="anexo") String anexo,
    			RedirectAttributes ra, @AuthenticationPrincipal CurrentUser currentUser) 
    					throws IllegalAccessException, InvocationTargetException{
    	Activo activonew = new Activo(activoForm);
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    	List<Activo> removeActivos = (List<Activo>)session.getAttribute("removeActivos");
    	
    	Activo activoSession = (Activo)session.getAttribute("activo");
    	if(StringUtils.hasText(activoSession.getUsername()))
    		activoSession.setUsername_actualiza(currentUser.getUsername());
    	else
    		activoSession.setUsername(currentUser.getUsername());
    	activoSession.setEstado_registro("2");
    	notNull.copyProperties(activoSession, activonew);
    	
    	saveActivoWhitAtivosFichas(activos, activoSession);
    	saveActivoWhitAtivosFichas(removeActivos);
    	
    	ra.addFlashAttribute("anexo", anexo);
		ra.addFlashAttribute("activo", activoSession);
    	MessageHelper.addInfoAttribute(ra, "message.sussesful", activonew.getNombre_denominacon());
    	
    	return URL_REDIRECT_SUCCESFUL;
    }
    
    
    @RequestMapping(value="/save/activo/activos", method={RequestMethod.POST, RequestMethod.PUT})
    public String guardarActivoYActivos(@ModelAttribute Activo activo, HttpSession session, 
    			@RequestParam(value="anexo") String anexo,
    			RedirectAttributes ra, @AuthenticationPrincipal CurrentUser currentUser) 
    					throws IllegalAccessException, InvocationTargetException{
    	    	
    	Activo activoSession = (Activo)session.getAttribute("activo");
    	if(StringUtils.hasText(activoSession.getUsername()))
    		activo.setUsername_actualiza(currentUser.getUsername());
    	else
    		activo.setUsername(currentUser.getUsername());
    	
    	notNull.copyProperties(activoSession, activo);
    	activoSession.setEstado_registro("2");
    	activoSession.setNumSup(activoSession.getActivo().toString());
    	activoService.guardar(activoSession);
    	
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    	List<Activo> removeActivos = (List<Activo>)session.getAttribute("removeActivos");
    	
    	saveActivoWhitAtivos(activos, activoSession, activoSession.getActivo().toString());
    	saveActivoWhitAtivosFichas(removeActivos);
    	
    	ra.addFlashAttribute("anexo", anexo);
		ra.addFlashAttribute("activo", activoSession);
    	MessageHelper.addInfoAttribute(ra, "message.sussesful", activo.getNombre_denominacon());
    	return URL_REDIRECT_SUCCESFUL;
    }
    
	public void saveActivoWhitAtivos(List<Activo> activos, Activo activonew, String numSuperior)
			throws IllegalAccessException, InvocationTargetException {
    	for(Activo activo: activos){
    		activo.setEstado_registro("2");
    		if(!StringUtils.hasLength(activo.getNumSup())){
    			activo.setNumSup(numSuperior);
    		}
    		if(activonew!=null){
    			activo.setNumeroImpresion(activonew.getNumeroImpresion());
    			activo.setNro_interno(activonew.getNro_interno());
    			activo.setEquip_tipo_estructura(activonew.getEquip_tipo_estructura());
    			activo.setDireccion(activonew.getDireccion());
    			activo.setDistrito(activonew.getDistrito());
    			activo.setEmplaz(activonew.getEmplaz());
    			activo.setDen_emplaz(activonew.getDen_emplaz());
    			activo.setNumSup(activonew.getNumSup());
    			activo.setEquip_ubigeo(activonew.getEquip_ubigeo());
    			activo.setEquipo_proyecto(activonew.getEquipo_proyecto());
    			activo.setGerencia(activonew.getGerencia());
    			activo.setCeco(activonew.getCeco());
    			activo.setCentro_costo_denomi(activonew.getCentro_costo_denomi());
    			activo.setEquip_nombre_operador(activonew.getEquip_nombre_operador());
    			activo.setEquip_telefono_operador(activonew.getEquip_telefono_operador());
    			activo.setFecha_inventario(activonew.getFecha_inventario());
    			activo.setUsername(activonew.getUsername());
    			activo.setUsername_actualiza(activonew.getUsername_actualiza());
//    			activonew.setClase(activo.getClase());
//    			notNull.copyProperties(activo, activonew);	
    		}
    		activoService.guardar(activo);
    	}
	}
	
	public void saveActivoWhitAtivosFichas(List<Activo> activos)
			throws IllegalAccessException, InvocationTargetException {
		saveActivoWhitAtivosFichas(activos, null);
	}
	
	public void saveActivoWhitAtivosFichas(List<Activo> activos, Activo activonew)
			throws IllegalAccessException, InvocationTargetException {
		Activo local = null;
		boolean flagRemove = true;
    	for(Activo activo: activos){
    		if(activonew!=null){
    			flagRemove = false;
    			activo.setNumeroImpresion(activonew.getNumeroImpresion());
    			activonew.setClase(activo.getClase());
    			notNull.copyProperties(activo, activonew);
    		}
    		local = activoService.guardar(activo);
    		if(!flagRemove && org.apache.commons.lang3.StringUtils.isEmpty(activonew.getNumeroImpresion()))
    			activonew.setNumeroImpresion(local.getNumeroImpresion());
    	}
	}
	
    private Activo getActivoOfSession(String codigo, HttpSession session) {
		Activo unActivo=null;    	
    	List<Activo> activos = (List<Activo>)session.getAttribute("activos");
    	if(codigo.length() < 4 )
    		return activos.get(Integer.parseInt(codigo)-1);
    	
    	Long nroActivo = Long.valueOf(codigo);
    	for(Activo activo: activos){
    		if(activo.getActivo().equals(nroActivo)){
    			unActivo = activo;
    			break;
    		}
    	}
		return unActivo;
	}
    
    private List<Activo> getActivosOfSession(String[] codigos, HttpSession session) {
    	return getActivosOfSession(codigos, session, false, null);
    }
    
    private List<Activo> getActivosOfSession(String[] codigos, HttpSession session, boolean isFlagRemove, String anexo) {
    	List<Activo> activos = new ArrayList<>();
    	Activo activo = null;
    	for(String codigo : codigos){
    		activo = getActivoOfSession(codigo, session);
    		if(activo != null){
    			if(isFlagRemove){
    				activo.setEstado_registro("3");
    				activo.setFecha_actualiza(new Date());
    				activo.setFecha_registro(new Date());
    				activo.setNumeroImpresion(null);
    				activo.setNumero_folio(null);
    		    	if(anexo.equals("4"))
    		    		activo.setNumSup("");
    		    	else
    		    		activo.setFichaUsuario(null);
        		} 
    			activos.add(activo);
    		}
    	}
    	return activos;
    }
    
    private boolean isActivoDuplicate(String codigo, List<Activo> activos) {    	
    	Long nroActivo = Long.valueOf(codigo);
    	boolean repeat = false;
    	for(Activo activo: activos){
    		if(activo.getActivo()!= null && activo.getActivo().equals(nroActivo)){
    			repeat = true;
    			break;
    		}
    	}
    	return repeat;
    }
    
	@RequestMapping(value = "/activoup", method = RequestMethod.GET)
	public ModelAndView updateactive (){
		return new ModelAndView("report/updateActive");
	}
	
	@RequestMapping(value = "/activoup/{anexo}/exists", method = RequestMethod.POST)
	public void validexist (@RequestParam(required=true) String codigo, 
			@RequestParam(required=true)String anexo){
		if(!StringUtils.hasText(codigo))
			return;
		if(!activoService.validateIfExists(Long.valueOf(codigo), anexo))
			throw new ActivoConflictException("No existe el activo "+codigo);
	}
	
	@RequestMapping(value = "/activoup/update/newactivo", method = RequestMethod.POST)
	public String saveNewActive (RedirectAttributes ra, @RequestParam(required=true) String codigo, 
			@RequestParam(value="cboAnexo", required=true)String anexo,
			@RequestParam(required=true) String newcodigo){
		
		Long activo = 0L, newActivo=0L;
		if(org.apache.commons.lang3.StringUtils.isNumeric(codigo))
			activo = Long.valueOf(codigo);
		if(org.apache.commons.lang3.StringUtils.isNumeric(newcodigo))
			newActivo = Long.valueOf(newcodigo);
		
		try {
			if(activoService.validateIfExists(newActivo, anexo)){
				MessageHelper.addWarningAttribute(ra, "activo.duplicate2", newActivo);
				return "redirect:/activo/activoup";
			}
			activoService.updateNewActive(activo, newActivo, anexo);
			MessageHelper.addInfoAttribute(ra, "update.sussesful", newActivo);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return "redirect:/activo/activoup";
	}

    
    private void removeOfSessionActivesRemoves(Activo unActivo, HttpSession session) {
    	List<Activo> removeActivos = (List<Activo>)session.getAttribute("removeActivos");
    	removeActivos.remove(unActivo);
	}

    private void refreshTableForAnexo(String anexo){
    	URL_FRAGMENT_REFRESH_TABLE_ANEXO_ITEMACTIVOS = URL_FRAGMENT_FORM + anexo + TABLE_ITEMACTIVOS;
    }
}
