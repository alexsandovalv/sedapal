package pe.com.inventarios.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pe.com.inventarios.model.Activo;
import pe.com.inventarios.util.Commons;

@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{
	
	private DataSource dbsoruce;
	
	@Autowired
	public ReportController(DataSource dbsoruce) {
		this.dbsoruce = dbsoruce;
	}
	
	@RequestMapping(value={"","/"}, method=RequestMethod.GET)
	public ModelAndView inicio(){
		ModelAndView mav = new ModelAndView("report/mainReport");
		return mav;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView loadSearchFolios(){
		return new ModelAndView("report/printfolio");
	}
	
	@RequestMapping(value = "/search/folio/sendprint", method = RequestMethod.POST)
	public String searchFolios(@RequestParam(required=false) String folio1, @RequestParam(required=false) String folio2, 
			@RequestParam(value="cboAnexo", required=false)String planilla,
			@RequestParam(value="cboClase", required=false)String clase,
			@RequestParam(value="cboEntregable", required=false)String entregable, HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		int foliostart = 0;
		int folioend = 0;
		planilla = StringUtils.defaultIfBlank(planilla, "0");
		
		int tipoAnexo = Integer.parseInt(planilla);
		if(StringUtils.isNumeric(folio1))
			foliostart = Integer.parseInt(folio1);
		if(StringUtils.isNumeric(folio2))
			folioend = Integer.parseInt(folio2);
		
		List<Activo> activosDB = null;
		if(tipoAnexo>0 && StringUtils.isNotBlank(planilla) && StringUtils.isNotBlank(clase) && StringUtils.isNotBlank(entregable))
			activosDB = activoService.searchbyPlanillaClaseEntregable(tipoAnexo, clase, entregable);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(planilla) && StringUtils.isNotBlank(entregable) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaClaseEntregableRango(tipoAnexo, clase, entregable, folio1, folio2);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(clase))
			activosDB = activoService.searchbyPlanillaClase(tipoAnexo, clase);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(entregable))
			activosDB = activoService.searchbyPlanillaEntregable(tipoAnexo, entregable);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(entregable) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaEntregableRango(tipoAnexo, entregable, folio1, folio2);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(planilla) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaClaseRango(tipoAnexo, planilla, folio1, folio2);
		
		getJasperPrints(activosDB, dbsoruce, planilla, null, true);
//		if(isEntregable){
//			activosDB = activoService.searchbyPlanillaClaseEntregable(tipoAnexo, clase, entregable);
//			jasperPrints = getJasperPrints(activosDB, dbsoruce);
//		}else
//			jasperPrints = getJasperPrints(tipoAnexo, foliostart, folioend, dbsoruce);
		
//		for(JasperPrint print : jasperPrints){
//			try{
//				printReport(print);
//			}catch(JRException pException){
//				System.err.println(pException);
//				break;
//			}
//		}
		
		return "redirect:/report/search";
	}
	
	@RequestMapping(value = "/search/folio/download", method = RequestMethod.POST)
    public @ResponseBody void doDownload(@RequestParam(required=false) String folio1, @RequestParam(required=false) String folio2, 
    		@RequestParam(value="cboAnexo", required=false)String planilla,
			@RequestParam(value="cboClase", required=false)String clase, 
			@RequestParam(value="cboEntregable", required=false)String entregable, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		planilla = StringUtils.defaultIfBlank(planilla, "0");
		
		int foliostart = 0;
		int folioend = 0;
		int tipoAnexo = Integer.parseInt(planilla);
		
		if(StringUtils.isNumeric(folio1) )
			foliostart = Integer.parseInt(folio1);
		if(StringUtils.isNumeric(folio2) )
			folioend = Integer.parseInt(folio2);
		String currentDirectoryName = System.getProperty("user.dir");
		
		Path directoryZip = createFolderIfnotExist(Paths.get(currentDirectoryName), "tempzip");
		
		Path tempDir = Files.createTempDirectory("numfiles");
		
		String zipName = "";
		if(StringUtils.isNotBlank(clase) && (StringUtils.isBlank(folio1) || StringUtils.isBlank(folio2)))
			zipName=clase;
		
		Path playPathClass = null;
		if(StringUtils.isNotBlank(clase)){
			playPathClass = createFolderIfnotExist(tempDir, clase);
		}else
			playPathClass = tempDir;
		
		List<Activo> activosDB = null;
		if(tipoAnexo>0 && StringUtils.isNotBlank(clase) && StringUtils.isNotBlank(entregable))
			activosDB = activoService.searchbyPlanillaClaseEntregable(tipoAnexo, clase, entregable);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(entregable) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaClaseEntregableRango(tipoAnexo, clase, entregable, folio1, folio2);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(clase))
			activosDB = activoService.searchbyPlanillaClase(tipoAnexo, clase);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(entregable))
			activosDB = activoService.searchbyPlanillaEntregable(tipoAnexo, entregable);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(entregable) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaEntregableRango(tipoAnexo, entregable, folio1, folio2);
		else if(tipoAnexo>0 && StringUtils.isNotBlank(clase) && (foliostart>0 && folioend>0))
			activosDB = activoService.searchbyPlanillaClaseRango(tipoAnexo, clase, folio1, folio2);
		
		getJasperPrints(activosDB, dbsoruce, planilla, playPathClass, false);
		
//		if(isEntregable){
//			activosDB = activoService.searchbyEntregable(entregable);
//			jasperPrints = getJasperPrints(activosDB, dbsoruce);
//		}else
//			jasperPrints = getJasperPrints(tipoAnexo, foliostart, folioend, dbsoruce);
		
//		String padName = null;
		
		
//		for(JasperPrint print : jasperPrints){
//			padName = print.getName();
//			freport = Files.createFile(Paths.get(playPathClass.toString(), padName.concat(".pdf")));
//			JasperExportManager.exportReportToPdfFile(print, freport.toFile().getAbsolutePath());
//		}
		
		File downloadFile = new File(directoryZip.toFile(), zipName+".zip");
//		if(StringUtils.equals(planilla, "3") || StringUtils.equals(planilla, "4"))
			Commons.zipFileWithDirectory(tempDir.toFile().listFiles(), downloadFile);
//		else
//			Commons.zipFile(tempDir.toFile().listFiles(), downloadFile);
		
		FileUtils.deleteDirectory(tempDir.toFile());
		
    	ServletContext context = request.getServletContext();
        FileInputStream inputStream = new FileInputStream(downloadFile);
        
        String mimeType = context.getMimeType(downloadFile.getAbsolutePath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
        
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        response.setHeader("content-Disposition", "attachment; filename=" + downloadFile.getName());
        IOUtils.copyLarge(inputStream, response.getOutputStream());
        response.flushBuffer();
        IOUtils.closeQuietly(inputStream);	
        
        downloadFile.setWritable(true);
        downloadFile.delete();
    }

	private Path createFolderIfnotExist(Path playAreaPath, String subDirectory) throws IOException {
		Path playPathClass;
		if(!Files.exists(Paths.get(playAreaPath.toFile().getAbsolutePath(), subDirectory)))
			playPathClass = Files.createDirectory(Paths.get(playAreaPath.toFile().getAbsolutePath(), subDirectory));
		else{
			playPathClass = Paths.get(playAreaPath.toFile().getAbsolutePath(), subDirectory);
		}
		return playPathClass;
	}
	
	@RequestMapping(value = "/print/{reportCDR}", method = RequestMethod.GET)
	public ModelAndView printPDF(ModelMap parameterMap, HttpServletRequest req, 
				@PathVariable String reportCDR) throws IOException {
		
		String path = getFilePath("/static/image/logo_consorcio.png");
		
		parameterMap.put("format", "xls");
		parameterMap.put("datasource", dbsoruce);
		parameterMap.put("pathLogo", path);
		
		ModelAndView modelAndView = new ModelAndView("reporte"+reportCDR,parameterMap);
		return modelAndView;
	}
	
}
