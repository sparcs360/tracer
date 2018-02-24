package com.sparcs.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    CommandLineRunner init() {

	    return (args) -> {

	        ClassA a = new ClassA();
	        a.methodA();
        };
    }

    @Component
    public static class ClassA {

        public void methodA() {

            methodB();
        }

        private int methodB() {
            return 0;
        }
    }
}
