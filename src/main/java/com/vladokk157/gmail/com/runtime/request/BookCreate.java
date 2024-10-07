package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.validation.Create;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Create Book */
@IdValid.List({
  @IdValid(
      field = "bookInstanceId",
      fieldType = BookInstance.class,
      targetField = "bookInstance",
      groups = {Update.class, Create.class})
})
public class BookCreate extends BasicCreate {

  @JsonIgnore private BookInstance bookInstance;

  private String bookInstanceId;

  /**
   * @return bookInstance
   */
  @JsonIgnore
  public BookInstance getBookInstance() {
    return this.bookInstance;
  }

  /**
   * @param bookInstance bookInstance to set
   * @return BookCreate
   */
  public <T extends BookCreate> T setBookInstance(BookInstance bookInstance) {
    this.bookInstance = bookInstance;
    return (T) this;
  }

  /**
   * @return bookInstanceId
   */
  public String getBookInstanceId() {
    return this.bookInstanceId;
  }

  /**
   * @param bookInstanceId bookInstanceId to set
   * @return BookCreate
   */
  public <T extends BookCreate> T setBookInstanceId(String bookInstanceId) {
    this.bookInstanceId = bookInstanceId;
    return (T) this;
  }
}
