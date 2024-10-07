package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.request.AuthorCreate;
import com.vladokk157.gmail.com.runtime.request.AuthorFilter;
import com.vladokk157.gmail.com.runtime.request.AuthorUpdate;
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
public class AuthorControllerTest {

  private Author testAuthor;
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
  public void testAuthorCreate() {
    AuthorCreate request = new AuthorCreate();

    ResponseEntity<Author> response =
        this.restTemplate.postForEntity("/Author/createAuthor", request, Author.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testAuthor = response.getBody();
    assertAuthor(request, testAuthor);
  }

  @Test
  @Order(2)
  public void testListAllAuthors() {
    AuthorFilter request = new AuthorFilter();
    ParameterizedTypeReference<PaginationResponse<Author>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Author>> response =
        this.restTemplate.exchange(
            "/Author/getAllAuthors", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Author> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Author> Authors = body.getList();
    Assertions.assertNotEquals(0, Authors.size());
    Assertions.assertTrue(Authors.stream().anyMatch(f -> f.getId().equals(testAuthor.getId())));
  }

  public void assertAuthor(AuthorCreate request, Author testAuthor) {
    Assertions.assertNotNull(testAuthor);
  }

  @Test
  @Order(3)
  public void testAuthorUpdate() {
    AuthorUpdate request = new AuthorUpdate().setId(testAuthor.getId());
    ResponseEntity<Author> response =
        this.restTemplate.exchange(
            "/Author/updateAuthor", HttpMethod.PUT, new HttpEntity<>(request), Author.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testAuthor = response.getBody();
    assertAuthor(request, testAuthor);
  }
}
