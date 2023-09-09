package com.playground;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@GetMapping("/testasdasd")
	public String test() {
		System.out.println("CJ checkpoint 1");
		return "Hi MADAFAKA";
	}
}
