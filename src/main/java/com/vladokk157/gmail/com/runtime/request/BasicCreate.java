package com.vladokk157.gmail.com.runtime.request;

import java.time.OffsetDateTime;

/** Object Used to Create Basic */
public class BasicCreate {

  private OffsetDateTime dateCreated;

  private OffsetDateTime dateUpdated;

  private String description;

  private String name;

  private Boolean softDelete;

  /**
   * @return dateCreated
   */
  public OffsetDateTime getDateCreated() {
    return this.dateCreated;
  }

  /**
   * @param dateCreated dateCreated to set
   * @return BasicCreate
   */
  public <T extends BasicCreate> T setDateCreated(OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
    return (T) this;
  }

  /**
   * @return dateUpdated
   */
  public OffsetDateTime getDateUpdated() {
    return this.dateUpdated;
  }

  /**
   * @param dateUpdated dateUpdated to set
   * @return BasicCreate
   */
  public <T extends BasicCreate> T setDateUpdated(OffsetDateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
    return (T) this;
  }

  /**
   * @return description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * @param description description to set
   * @return BasicCreate
   */
  public <T extends BasicCreate> T setDescription(String description) {
    this.description = description;
    return (T) this;
  }

  /**
   * @return name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param name name to set
   * @return BasicCreate
   */
  public <T extends BasicCreate> T setName(String name) {
    this.name = name;
    return (T) this;
  }

  /**
   * @return softDelete
   */
  public Boolean getSoftDelete() {
    return this.softDelete;
  }

  /**
   * @param softDelete softDelete to set
   * @return BasicCreate
   */
  public <T extends BasicCreate> T setSoftDelete(Boolean softDelete) {
    this.softDelete = softDelete;
    return (T) this;
  }
}
