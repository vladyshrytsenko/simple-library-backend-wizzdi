package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Lender;
import com.vladokk157.gmail.com.runtime.request.LenderCreate;
import com.vladokk157.gmail.com.runtime.request.LenderFilter;
import com.vladokk157.gmail.com.runtime.request.LenderUpdate;
import com.vladokk157.gmail.com.runtime.request.LoginRequest;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppInit.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class LenderControllerTest {

  private Lender testLender;
  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  private void init() {
    ResponseEntity<Object> authenticationResponse =
        this.restTemplate.postForEntity(
            "/login",
            new LoginRequest().setUsername("admin@flexicore.com").setPassword("admin"),
            Object.class);
    String authenticationKey =
        authenticationResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).stream()
            .findFirst()
            .orElse(null);
    restTemplate
        .getRestTemplate()
        .setInterceptors(
            Collections.singletonList(
                (request, body, execution) -> {
                  request.getHeaders().add("Authorization", "Bearer " + authenticationKey);
                  return execution.execute(request, body);
                }));
  }

  @Test
  @Order(1)
  public void testLenderCreate() {
    LenderCreate request = new LenderCreate();

    request.setBlocked(true);

    ResponseEntity<Lender> response =
        this.restTemplate.postForEntity("/Lender/createLender", request, Lender.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testLender = response.getBody();
    assertLender(request, testLender);
  }

  @Test
  @Order(2)
  public void testListAllLenders() {
    LenderFilter request = new LenderFilter();
    ParameterizedTypeReference<PaginationResponse<Lender>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Lender>> response =
        this.restTemplate.exchange(
            "/Lender/getAllLenders", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Lender> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Lender> Lenders = body.getList();
    Assertions.assertNotEquals(0, Lenders.size());
    Assertions.assertTrue(Lenders.stream().anyMatch(f -> f.getId().equals(testLender.getId())));
  }

  public void assertLender(LenderCreate request, Lender testLender) {
    Assertions.assertNotNull(testLender);

    if (request.getBlocked() != null) {
      Assertions.assertEquals(request.getBlocked(), testLender.isBlocked());
    }
  }

  @Test
  @Order(3)
  public void testLenderUpdate() {
    LenderUpdate request = new LenderUpdate().setId(testLender.getId());
    ResponseEntity<Lender> response =
        this.restTemplate.exchange(
            "/Lender/updateLender", HttpMethod.PUT, new HttpEntity<>(request), Lender.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testLender = response.getBody();
    assertLender(request, testLender);
  }
}
