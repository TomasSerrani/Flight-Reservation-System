package com.finalprogramacion.sistemaDeVuelos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.finalprogramacion.sistemaDeVuelos")
public class SistemaDeVuelosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeVuelosApplication.class, args);
	}

}
