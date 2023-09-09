package com.playground;

import java.net.http.HttpRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundApplication.class, args);
		
		System.out.println("Hello World!");
	}
}
