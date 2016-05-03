package pe.com.inventarios.controller;

import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
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
	
	@Value("${path.files.photos}")
	protected String pathFilesPhotos;

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
    
    @RequestMapping(value="/util/buscarCeco/{codigo}", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    CentroCosto ajaxGetCentroCosto (@PathVariable String codigo){
        return activoService.getCentroCostoByCC(Long.valueOf(codigo));
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
    public @ResponseBody byte[] showImage(@PathVariable int imageId, HttpSession session) throws IOException{
        byte[] image = null;
        Activo activo = (Activo)session.getAttribute("activo");
        if(activo==null)
            return null;
        Long imgeIdL = Long.valueOf(imageId);
        image = searchPhotoById(image, activo, imgeIdL);
        if(image != null)
        	return image;
        List<Activo> activos = (List<Activo>)session.getAttribute("activos");
        for(Activo subActivo : activos){
        	image = searchPhotoById(image, subActivo, imgeIdL);
        	if(image != null)
            	break;
        }
        return image;
    }

	private byte[] searchPhotoById(byte[] image, Activo activo, Long imgeIdL)
			throws FileNotFoundException, IOException {
		InputStream in;
		for(Foto foto : activo.getFotos()){
        	if(foto.getId().equals(imgeIdL)){
        		//image = foto.getFoto();
        		in = new FileInputStream(new File(foto.getPath()));
        		image = IOUtils.toByteArray(in);
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
		BufferedOutputStream stream = null;
		String filename = "";
		File fileDirectory = new File(pathFilesPhotos+File.separator+unActivo.getSeq2015());
		if(!fileDirectory.exists() && files.length>0)
			fileDirectory.mkdir();
		String filepath = "";
		for(MultipartFile oneFile : files){
			if (!oneFile.isEmpty()) {
				foto = new Foto();
				bytes = oneFile.getBytes();
				if(posImges.length>pos && posImges[pos] != null){
					foto.setId(Long.valueOf(posImges[pos]));
				}
				filename = oneFile.getOriginalFilename();
				filepath = Paths.get(fileDirectory.getAbsolutePath(), filename).toString();
				stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
				stream.write(bytes);
				stream.close();
				
				foto.setActivo(unActivo.getActivo());
				foto.setSecuence(unActivo.getSeq2015());
				foto.setPath(fileDirectory.getPath()+File.separator+filename);
//				foto.setFoto(bytes);
				fotos.add(foto);
			}
			pos++;
		}
		return fotos;
	}
    
    public Map<String, Object> configMapPrinter(int tipoAnexo, String numFolio) throws Exception{
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String imgPath = "";
		String subReport = "";
		imgPath = getFilePath("/static/image/logo-sedapal.png");
		parameterMap.put("pathLogo", imgPath);
		if(tipoAnexo==3){
			parameterMap.put("numeroFolio", numFolio);
			subReport = getFilePath("/static/reportes/subreporte/anexo3_subreport_folio.jrxml");
			parameterMap.put("pathSubReport3", loadReport(getResourceFile("/static/reportes/subreporte/anexo3_subreport_folio.jrxml")));
		}else if(tipoAnexo==4){
			subReport = getFilePath("/static/reportes/subreporte/anexo4_subreport_folio.jrxml");
			parameterMap.put("numeroFolio", numFolio);
			parameterMap.put("pathSubReport4", loadReport(getResourceFile("/static/reportes/subreporte/anexo4_subreport_folio.jrxml")));
		}else{
			parameterMap.put("numeroFolio", numFolio);
		}
		return parameterMap;
	}
    
    protected List<JasperPrint> getJasperPrints(int tipoAnexo, int folioStar, int folioEnd, DataSource dbsoruce) throws Exception{
    	String padName = null;
    	Map<String, Object> configMap = null;
    	List<JasperPrint> jasperPrints = new ArrayList<>();
    	JasperReport report = null;
		JasperPrint filledReport = null;
		for(; folioStar <= folioEnd; folioStar++){
			padName = org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(folioStar), 6, "0");
			configMap = configMapPrinter(tipoAnexo, padName);
			report = loadReport(getResourceFile("/static/reportes/Anexo"+tipoAnexo+"_folio.jrxml"));
			filledReport = doFillReport(report, configMap, dbsoruce);
			jasperPrints.add(filledReport);
		}
    	return jasperPrints;
    }
    
    protected void getJasperPrints(List<Activo> activosInput, DataSource dbsoruce, String planilla, Path fPath, boolean isPrint) throws Exception {
    	Path freport = null;
    	Map<String, Object> configMap = null;
    	JasperReport report = null;
		JasperPrint filledReport = null;
		int tipoAnexo = Integer.parseInt(planilla);
		String numFolio = "";
		for (Activo activo :activosInput){
			numFolio = activo.getNumero_folio();
			configMap = configMapPrinter(tipoAnexo, numFolio);
			report = loadReport(getResourceFile("/static/reportes/Anexo"+tipoAnexo+"_folio.jrxml"));
			filledReport = doFillReport(report, configMap, dbsoruce);
			
			if(isPrint){
				try{
					printReport(filledReport);
				}catch(JRException pException){
					System.err.println(pException);
					break;
				}
			}else{
				freport = Files.createFile(Paths.get(fPath.toString(), numFolio.concat(".pdf")));
				JasperExportManager.exportReportToPdfFile(filledReport, freport.toFile().getAbsolutePath());
			}
		}
//		return jasperPrints;
	}
    
    public Resource getResourceFile(String classpathRelativePath) throws IOException {
	    Resource rsrc = new ClassPathResource(classpathRelativePath);
	    return rsrc;
	}
    
    public String getFilePath(String classpathRelativePath) throws IOException {
	    Resource rsrc = getResourceFile(classpathRelativePath);
	    return rsrc.getFile().getAbsolutePath();
	}
    
    public void printReport(JasperPrint buildPrinter) throws JRException, PrinterException{
		long start = System.currentTimeMillis();
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);

		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(PrintServiceLookup.lookupDefaultPrintService().getName(), null));
		
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		
		exporter.setExporterInput(new SimpleExporterInput(buildPrinter));
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();

		System.err.println("Printing time : " + (System.currentTimeMillis() - start));
	}
    
    protected final JasperReport loadReport(Resource resource) {
		try {
			String filename = resource.getFilename();
			if (filename != null) {
				if (filename.endsWith(".jasper")) {
					InputStream is = resource.getInputStream();
					try {
						return (JasperReport) JRLoader.loadObject(is);
					}
					finally {
						is.close();
					}
				}
				else if (filename.endsWith(".jrxml")) {
					InputStream is = resource.getInputStream();
					try {
						JasperDesign design = JRXmlLoader.load(is);
						return JasperCompileManager.compileReport(design);
					}
					finally {
						is.close();
					}
				}
			}
			throw new IllegalArgumentException(
					"Report filename [" + filename + "] must end in either .jasper or .jrxml");
		}
		catch (IOException ex) {
			throw new ApplicationContextException(
					"Could not load JasperReports report from " + resource, ex);
		}
		catch (JRException ex) {
			throw new ApplicationContextException(
					"Could not parse JasperReports report from " + resource, ex);
		}
	}
    
    protected JasperPrint doFillReport(JasperReport report, Map<String, Object> model, DataSource ds) throws Exception {
		Connection con = ds.getConnection();
		try {
			return JasperFillManager.fillReport(report, model, con);
		}
		finally {
			try {
				con.close();
			}
			catch (Throwable ex) {
				System.out.println("Could not close JDBC Connection "+ ex.getMessage());
				try {
					con.close();
				}catch(Exception e){}
			}
		}
	}
}
