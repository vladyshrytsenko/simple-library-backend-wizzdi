package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.request.BookInstanceCreate;
import com.vladokk157.gmail.com.runtime.request.BookInstanceFilter;
import com.vladokk157.gmail.com.runtime.request.BookInstanceUpdate;
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
public class BookInstanceControllerTest {

  private BookInstance testBookInstance;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Book book;

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
  public void testBookInstanceCreate() {
    BookInstanceCreate request = new BookInstanceCreate();

    request.setBookId(this.book.getId());

    request.setSerialNumber("test-string");

    request.setBlocked(true);

    ResponseEntity<BookInstance> response =
        this.restTemplate.postForEntity(
            "/BookInstance/createBookInstance", request, BookInstance.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBookInstance = response.getBody();
    assertBookInstance(request, testBookInstance);
  }

  @Test
  @Order(2)
  public void testListAllBookInstances() {
    BookInstanceFilter request = new BookInstanceFilter();
    ParameterizedTypeReference<PaginationResponse<BookInstance>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<BookInstance>> response =
        this.restTemplate.exchange(
            "/BookInstance/getAllBookInstances", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<BookInstance> body = response.getBody();
    Assertions.assertNotNull(body);
    List<BookInstance> BookInstances = body.getList();
    Assertions.assertNotEquals(0, BookInstances.size());
    Assertions.assertTrue(
        BookInstances.stream().anyMatch(f -> f.getId().equals(testBookInstance.getId())));
  }

  public void assertBookInstance(BookInstanceCreate request, BookInstance testBookInstance) {
    Assertions.assertNotNull(testBookInstance);

    if (request.getBookId() != null) {

      Assertions.assertNotNull(testBookInstance.getBook());
      Assertions.assertEquals(request.getBookId(), testBookInstance.getBook().getId());
    }

    if (request.getSerialNumber() != null) {
      Assertions.assertEquals(request.getSerialNumber(), testBookInstance.getSerialNumber());
    }

    if (request.getBlocked() != null) {
      Assertions.assertEquals(request.getBlocked(), testBookInstance.isBlocked());
    }
  }

  @Test
  @Order(3)
  public void testBookInstanceUpdate() {
    BookInstanceUpdate request = new BookInstanceUpdate().setId(testBookInstance.getId());
    ResponseEntity<BookInstance> response =
        this.restTemplate.exchange(
            "/BookInstance/updateBookInstance",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            BookInstance.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBookInstance = response.getBody();
    assertBookInstance(request, testBookInstance);
  }
}
