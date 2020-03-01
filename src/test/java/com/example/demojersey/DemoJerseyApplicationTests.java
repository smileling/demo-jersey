package com.example.demojersey;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoJerseyApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void hello() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity("/hello/hello1", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isEqualTo("Hello from Spring");
	}

	@Test
	public void reverse() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity("/reverse?data=regit", String.class);
		assertThat(((ResponseEntity) entity).getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isEqualTo("tiger");
	}

	@Test
	public void validation() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity("/reverse", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

}
