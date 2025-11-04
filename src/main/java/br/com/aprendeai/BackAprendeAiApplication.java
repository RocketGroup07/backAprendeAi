package br.com.aprendeai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BackAprendeAiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
        System.setProperty("EMAIL", dotenv.get("EMAIL"));
        System.setProperty("EMAIL_SENHA", dotenv.get("EMAIL_SENHA"));
        
        
		SpringApplication.run(BackAprendeAiApplication.class, args);
	}

}
