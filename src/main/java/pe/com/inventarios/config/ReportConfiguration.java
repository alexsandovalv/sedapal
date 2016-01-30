package pe.com.inventarios.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

@Configuration
public class ReportConfiguration {
	
	@Bean
	public JasperReportsPdfView reportsPdfView(){
		JasperReportsPdfView jasperReport = new JasperReportsPdfView();
		jasperReport.setUrl("classpath:static/reportes/Anexo2_Landscape.jrxml");
		jasperReport.setReportDataKey("datasource");
		return jasperReport;
	}
	
	@Bean
	public JasperReportsViewResolver reportsPdfViewResolver(){
		JasperReportsViewResolver jasperReport = new JasperReportsViewResolver();
		jasperReport.setPrefix("classpath:/static/reportes/");
		jasperReport.setSuffix(".jrxml");
		jasperReport.setReportDataKey("datasource");
		jasperReport.setViewNames("Anexo*", "reporte*");
		jasperReport.setViewClass(JasperReportsMultiFormatView.class);
		jasperReport.setOrder(0);
		Properties p = new Properties();
		p.setProperty("pathSubReport3", "classpath:/static/reportes/subreporte/anexo3_subreport_folio.jrxml");
		p.setProperty("pathSubReport4", "classpath:/static/reportes/subreporte/anexo4_subreport_folio.jrxml");
		jasperReport.setSubReportUrls(p);
		return jasperReport;
	}
	

}
