package pe.com.inventarios.config;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPassword_Demo {

	public static void main(String[] args) {
		int i=1;
		while(i<11){
			int randomU = RandomUtils.nextInt(1000, 9999);
			String password = "user"+i+"_"+randomU;
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
			
			System.out.println("Password("+password+", "+hashedPassword+")");	
			i++;
			
			
		}
		
	}

}
