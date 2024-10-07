package com.vladokk157.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class Basic {

  @Id private String id;

  private String name;

  private String description;

  private OffsetDateTime dateCreated;

  private OffsetDateTime dateUpdated;

  private boolean softDelete;

  /**
   * @return id
   */
  @Id
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return Basic
   */
  public <T extends Basic> T setId(String id) {
    this.id = id;
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
   * @return Basic
   */
  public <T extends Basic> T setName(String name) {
    this.name = name;
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
   * @return Basic
   */
  public <T extends Basic> T setDescription(String description) {
    this.description = description;
    return (T) this;
  }

  /**
   * @return dateCreated
   */
  public OffsetDateTime getDateCreated() {
    return this.dateCreated;
  }

  /**
   * @param dateCreated dateCreated to set
   * @return Basic
   */
  public <T extends Basic> T setDateCreated(OffsetDateTime dateCreated) {
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
   * @return Basic
   */
  public <T extends Basic> T setDateUpdated(OffsetDateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
    return (T) this;
  }

  /**
   * @return softDelete
   */
  public boolean isSoftDelete() {
    return this.softDelete;
  }

  /**
   * @param softDelete softDelete to set
   * @return Basic
   */
  public <T extends Basic> T setSoftDelete(boolean softDelete) {
    this.softDelete = softDelete;
    return (T) this;
  }
}
