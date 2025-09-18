package com.mesprojets.gc.domain;

import jakarta.persistence.*;
import java.time.*;

@Entity @Table(name="customers")
public class Customer {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=100)
  private String code;

  @Column(nullable=false, length=190)
  private String name;

  @Column(length=190)
  private String email;

  @Column(length=60)
  private String phone;

  @Column(length=255)
  private String address;

  @Column(length=100)
  private String country;

  @Column(name="tax_id", length=100)
  private String taxId;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;

  @PrePersist void pre(){ this.createdAt = LocalDateTime.now(); }

  // getters/setters
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getCode(){return code;} public void setCode(String c){this.code=c;}
  public String getName(){return name;} public void setName(String n){this.name=n;}
  public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
  public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
  public String getAddress(){return address;} public void setAddress(String a){this.address=a;}
  public String getCountry(){return country;} public void setCountry(String c){this.country=c;}
  public String getTaxId(){return taxId;} public void setTaxId(String t){this.taxId=t;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime t){this.createdAt=t;}
}