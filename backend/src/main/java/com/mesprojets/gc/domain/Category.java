package com.mesprojets.gc.domain;

import jakarta.persistence.*;
import java.time.*;

@Entity @Table(name="categories")
public class Category {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=100)
  private String code;

  @Column(nullable=false, length=190)
  private String name;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;

  @PrePersist void pre() { this.createdAt = LocalDateTime.now(); }

  // getters/setters
  public Long getId() { return id; } public void setId(Long id){this.id=id;}
  public String getCode(){return code;} public void setCode(String code){this.code=code;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime t){this.createdAt=t;}
}