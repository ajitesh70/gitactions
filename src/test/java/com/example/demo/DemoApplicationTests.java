package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void homeReturnsWelcomeMessage() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string("Welcome to Docker Demo"));
	}

	@Test
	void helloReturnsGreeting() throws Exception {
		mockMvc.perform(get("/hello"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello from Spring Boot!"));
	}

	@Test
	void healthReturnsHealthyStatus() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(content().string("Application is Healthy"));
	}

	@Test
	void versionReturnsVersionString() throws Exception {
		mockMvc.perform(get("/version"))
				.andExpect(status().isOk())
				.andExpect(content().string("Version 1.0"));
	}

	@Test
	void timeReturnsIsoLocalDateTime() throws Exception {
		mockMvc.perform(get("/time"))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")));
	}

	@Test
	void actuatorLivenessIsUp() throws Exception {
		mockMvc.perform(get("/actuator/health/liveness"))
				.andExpect(status().isOk());
	}

	@Test
	void actuatorReadinessIsUp() throws Exception {
		mockMvc.perform(get("/actuator/health/readiness"))
				.andExpect(status().isOk());
	}

}
