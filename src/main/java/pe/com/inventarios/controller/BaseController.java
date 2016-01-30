package pe.com.inventarios.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import pe.com.inventarios.model.Activo;
import pe.com.inventarios.model.CentroCosto;
import pe.com.inventarios.model.Clase;
import pe.com.inventarios.model.Emplazamiento;
import pe.com.inventarios.model.Ficha;
import pe.com.inventarios.model.Foto;
import pe.com.inventarios.service.ActivoService;

/**
 * Created by sandovlu on 13/11/2015.
 */
public class BaseController {

    @Autowired
    protected ActivoService activoService;

    @RequestMapping(value="/util/buscarFicha/{codigo}", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Ficha ajaxUserFichero (@PathVariable String codigo){
        return activoService.getUserById(codigo);
    }
    
    @RequestMapping(value="/util/buscarFichas/{codigo}", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Ficha> ajaxUserFichas (@PathVariable String codigo){
        return activoService.getFichasById(codigo);
    }

    @RequestMapping(value="/util/buscarEmplaz/{codigo}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Emplazamiento ajaxEmplazamiento (@PathVariable String codigo){
        return activoService.getEmplazById(codigo);
    }
    
    @RequestMapping(value="/util/anexos/{anexo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Clase> listCampaigns(@PathVariable Integer anexo){
		return activoService.clasesForAnexo(anexo);
	}
    
//    @ModelAttribute("emplzamientos")
    public List<Emplazamiento> emplazamientos(){
    	return activoService.getAllEmplazmiento();
    }
    
//    @ModelAttribute("cecos")
    public List<CentroCosto> centroCostos(){
    	return activoService.getAllCentroCosto();
    }
    
    @RequestMapping(value="/getActivo", method=RequestMethod.POST)
    public Activo findOneActivo(Long activo){
    	return activoService.findByActivo(activo);
    }
    
    @RequestMapping(value = "/picture/{imageId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] showImage(@PathVariable int imageId, HttpSession session){
        byte[] image = null;
        Activo activo = (Activo)session.getAttribute("activo");
        if(activo==null)
            return null;
        
        for(Foto foto : activo.getFotos()){
        	if(foto.getId() == Long.valueOf(imageId)){
        		image = foto.getFoto();
        		break;
        	}
        }
        return image;
    }
    
    protected Set<Foto> addImages(MultipartFile[] files, String imageIds, Activo unActivo) throws IOException
			{
		String[] posImges = StringUtils.delimitedListToStringArray(imageIds, ",");
		Set<Foto> fotos = new HashSet<>();
		byte[] bytes = null;
		Foto foto = null;
		int pos = 0;
//		String name = "";
		for(MultipartFile oneFile : files){
			if (!oneFile.isEmpty()) {
				foto = new Foto();
				bytes = oneFile.getBytes();
//				name = oneFile.getName();
//					BufferedOutputStream stream =
//							new BufferedOutputStream(new FileOutputStream(new File(name)));
//					stream.write(bytes);
//					stream.close();
				if(posImges.length>pos && posImges[pos] != null){
					foto.setId(Long.valueOf(posImges[pos]));
				}
				foto.setActivo(unActivo.getActivo());
				foto.setFoto(bytes);
				fotos.add(foto);
			}
			pos++;
		}
		return fotos;
	}
    
    
    public String getFilePath(String classpathRelativePath) throws IOException {
	    Resource rsrc = new ClassPathResource(classpathRelativePath);
	    return rsrc.getFile().getAbsolutePath();
	}
}
