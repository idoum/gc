package com.mesprojets.gc.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name="products")
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=100)
  private String sku;

  @Column(nullable=false, length=190)
  private String name;

  @Column(columnDefinition="TEXT")
  private String description;

  @Column(name="unit_price", nullable=false, precision=14, scale=2)
  private BigDecimal unitPrice;

  @Column(nullable=false, length=10)
  private String currency = "CFA";

  @Column(name="tax_rate", nullable=false, precision=5, scale=2)
  private BigDecimal taxRate = new BigDecimal("0.00");

  @Column(nullable=false)
  private boolean active = true;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="category_id")
  private Category category;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;

  @Column(name="updated_at", nullable=false)
  private LocalDateTime updatedAt;

  @PrePersist void pre() { this.createdAt = this.updatedAt = LocalDateTime.now(); }
  @PreUpdate void upd() { this.updatedAt = LocalDateTime.now(); }

  // getters/setters
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getSku(){return sku;} public void setSku(String s){this.sku=s;}
  public String getName(){return name;} public void setName(String n){this.name=n;}
  public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
  public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal p){this.unitPrice=p;}
  public String getCurrency(){return currency;} public void setCurrency(String c){this.currency=c;}
  public BigDecimal getTaxRate(){return taxRate;} public void setTaxRate(BigDecimal t){this.taxRate=t;}
  public boolean isActive(){return active;} public void setActive(boolean a){this.active=a;}
  public Category getCategory(){return category;} public void setCategory(Category c){this.category=c;}
  public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime t){this.createdAt=t;}
  public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime t){this.updatedAt=t;}
}