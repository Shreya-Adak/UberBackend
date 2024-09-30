package com.codingshuttle.project.uber.UberAppBackend;

import com.codingshuttle.project.uber.UberAppBackend.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppBackendApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail("leteti3266@ndiety.com",
				"This testing mail",
				"Body");

	}
@Test
void emailSenderMultipleEmail(){
	String emails[]={
			"cisiga5080@ndiety.com",
			"vafote4185@kwalah.com",
			"yekada7354@hapied.com",
			"leteti3266@ndiety.com"

	};
	emailSenderService.sendEmail(emails,"This testing mail","Body");
}
}
