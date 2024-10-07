package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Basic;
import com.vladokk157.gmail.com.runtime.request.BasicCreate;
import com.vladokk157.gmail.com.runtime.request.BasicFilter;
import com.vladokk157.gmail.com.runtime.request.BasicUpdate;
import com.vladokk157.gmail.com.runtime.request.LoginRequest;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
public class BasicControllerTest {

  private Basic testBasic;
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
  public void testBasicCreate() {
    BasicCreate request = new BasicCreate();

    request.setName("test-string");

    request.setSoftDelete(true);

    request.setDescription("test-string");

    request.setDateCreated(OffsetDateTime.now());

    request.setDateUpdated(OffsetDateTime.now());

    ResponseEntity<Basic> response =
        this.restTemplate.postForEntity("/Basic/createBasic", request, Basic.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBasic = response.getBody();
    assertBasic(request, testBasic);
  }

  @Test
  @Order(2)
  public void testListAllBasics() {
    BasicFilter request = new BasicFilter();
    ParameterizedTypeReference<PaginationResponse<Basic>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Basic>> response =
        this.restTemplate.exchange(
            "/Basic/getAllBasics", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Basic> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Basic> Basics = body.getList();
    Assertions.assertNotEquals(0, Basics.size());
    Assertions.assertTrue(Basics.stream().anyMatch(f -> f.getId().equals(testBasic.getId())));
  }

  public void assertBasic(BasicCreate request, Basic testBasic) {
    Assertions.assertNotNull(testBasic);

    if (request.getName() != null) {
      Assertions.assertEquals(request.getName(), testBasic.getName());
    }

    if (request.getSoftDelete() != null) {
      Assertions.assertEquals(request.getSoftDelete(), testBasic.isSoftDelete());
    }

    if (request.getDescription() != null) {
      Assertions.assertEquals(request.getDescription(), testBasic.getDescription());
    }

    if (request.getDateCreated() != null) {
      Assertions.assertEquals(
          request.getDateCreated().atZoneSameInstant(ZoneId.systemDefault()),
          testBasic.getDateCreated().atZoneSameInstant(ZoneId.systemDefault()));
    }

    if (request.getDateUpdated() != null) {
      Assertions.assertEquals(
          request.getDateUpdated().atZoneSameInstant(ZoneId.systemDefault()),
          testBasic.getDateUpdated().atZoneSameInstant(ZoneId.systemDefault()));
    }
  }

  @Test
  @Order(3)
  public void testBasicUpdate() {
    BasicUpdate request = new BasicUpdate().setId(testBasic.getId());
    ResponseEntity<Basic> response =
        this.restTemplate.exchange(
            "/Basic/updateBasic", HttpMethod.PUT, new HttpEntity<>(request), Basic.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBasic = response.getBody();
    assertBasic(request, testBasic);
  }
}
