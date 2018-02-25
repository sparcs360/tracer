package com.sparcs.app;

import com.sparcs.tracer.capture.TraceSubject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    CommandLineRunner commandLineRunner() {

	    return Application::gogo;
    }

    @TraceSubject
    private static void gogo(String... args) {
        ClassA a = new ClassA("A");
        int result = a.methodA(37);
    }

    public static class ClassA {

        private String name;

        ClassA(String name) {

            this.name = name;
        }

        public String getName() {
            return name;
        }

        int methodA(int a) {

            return methodB(a) * 2;
        }

        private int methodB(int x) {
            return x * x;
        }
    }
}
