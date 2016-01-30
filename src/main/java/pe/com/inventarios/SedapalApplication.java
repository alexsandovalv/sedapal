package pe.com.inventarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class SedapalApplication extends SpringBootServletInitializer{
	
	protected SpringApplicationBuilder config(SpringApplicationBuilder builder){
		return builder.sources(SedapalApplication.class);
	}

    public static void main(String[] args) {
        SpringApplication.run(SedapalApplication.class, args);
    }
    
}