package pe.com.inventarios.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping(value = "/print/{reportCDR}", method = RequestMethod.GET)
	public ModelAndView printPDF(ModelMap parameterMap, HttpServletRequest req, 
				@PathVariable String reportCDR) throws IOException {
		
		String path = getFilePath("/static/image/logo_consorcio.png");
		
		parameterMap.put("format", "pdf");
		parameterMap.put("datasource", dbsoruce);
		parameterMap.put("pathLogo", path);
		
		ModelAndView modelAndView = new ModelAndView("reporte"+reportCDR,parameterMap);
		return modelAndView;
	}

}
