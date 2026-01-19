package br.edu.utfpr.td.tsi.trabalho1.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.edu.utfpr.td.tsi.trabalho1")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}