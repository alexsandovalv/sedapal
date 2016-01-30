package pe.com.inventarios.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Configuration
public class WebConfigurer extends WebMvcConfigurerAdapter{
	
	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
		registry.addViewController("/activo/").setViewName("activo/formularioBuscar");
        //registry.addViewController("/hello").setViewName("hello");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

//    @Bean
//    public AjaxThymeleafViewResolver ajaxThymeleafViewResolver() {
//        AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
//        viewResolver.setViewClass(FlowAjaxThymeleafView.class);
//        viewResolver.setTemplateEngine(springTemplateEngine);
//        return viewResolver;
//    }


}
