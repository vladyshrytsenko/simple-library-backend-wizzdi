package com.vladokk157.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import java.time.OffsetDateTime;

@Entity
public class Person extends Basic {

  private String email;

  private String address;

  private String phoneNumber;

  private OffsetDateTime birthDate;

  private String socialSecurityNumber;

  /**
   * @return email
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * @param email email to set
   * @return Person
   */
  public <T extends Person> T setEmail(String email) {
    this.email = email;
    return (T) this;
  }

  /**
   * @return address
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * @param address address to set
   * @return Person
   */
  public <T extends Person> T setAddress(String address) {
    this.address = address;
    return (T) this;
  }

  /**
   * @return phoneNumber
   */
  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  /**
   * @param phoneNumber phoneNumber to set
   * @return Person
   */
  public <T extends Person> T setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return (T) this;
  }

  /**
   * @return birthDate
   */
  public OffsetDateTime getBirthDate() {
    return this.birthDate;
  }

  /**
   * @param birthDate birthDate to set
   * @return Person
   */
  public <T extends Person> T setBirthDate(OffsetDateTime birthDate) {
    this.birthDate = birthDate;
    return (T) this;
  }

  /**
   * @return socialSecurityNumber
   */
  public String getSocialSecurityNumber() {
    return this.socialSecurityNumber;
  }

  /**
   * @param socialSecurityNumber socialSecurityNumber to set
   * @return Person
   */
  public <T extends Person> T setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
    return (T) this;
  }
}
