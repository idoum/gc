package com.mesprojets.gc.web;

import com.mesprojets.gc.domain.Customer;
import com.mesprojets.gc.domain.Product;
import com.mesprojets.gc.repo.CustomerRepo;
import com.mesprojets.gc.repo.ProductRepo;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CatalogController {

  private final ProductRepo products;
  private final CustomerRepo customers;

  public CatalogController(ProductRepo products, CustomerRepo customers) {
    this.products = products;
    this.customers = customers;
  }

  /* ----------------------- PRODUITS ----------------------- */

  @GetMapping("/{lang:fr|en}/products")
  public String listProducts(@PathVariable String lang,
                             @RequestParam(defaultValue = "") String q,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {

    // bornes simples
    if (page < 0) page = 0;
    if (size <= 0 || size > 100) size = 10;

    Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
    Page<Product> pageData = (q == null || q.isBlank())
        ? products.findAll(pageable)
        : products.findByNameContainingIgnoreCase(q, pageable);

    // IMPORTANT : fournir les noms attendus par les templates + lang
    model.addAttribute("lang", lang);
    model.addAttribute("q", q);
    model.addAttribute("products", pageData);
    return "products/list";
  }

  @PostMapping("/{lang:fr|en}/products/{id}/delete")
  public ResponseEntity<?> deleteProduct(@PathVariable String lang,
                                         @PathVariable Long id,
                                         @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
    products.deleteById(id);

    // Si HTMX : 204 No Content -> la ligne est retirée côté client (hx-swap='outerHTML:remove')
    if (hxRequest != null) {
      return ResponseEntity.noContent().build();
    }
    // Sinon, on redirige vers la liste
    return ResponseEntity.status(303) // 303 See Other
        .header("Location", "/" + lang + "/products")
        .build();
  }

  /* ----------------------- CLIENTS ----------------------- */

  @GetMapping("/{lang:fr|en}/customers")
  public String listCustomers(@PathVariable String lang,
                              @RequestParam(defaultValue = "") String q,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

    if (page < 0) page = 0;
    if (size <= 0 || size > 100) size = 10;

    Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
    Page<Customer> pageData = (q == null || q.isBlank())
        ? customers.findAll(pageable)
        : customers.findByNameContainingIgnoreCase(q, pageable);

    model.addAttribute("lang", lang);
    model.addAttribute("q", q);
    model.addAttribute("customers", pageData);
    return "customers/list";
  }

  @PostMapping("/{lang:fr|en}/customers/{id}/delete")
  public ResponseEntity<?> deleteCustomer(@PathVariable String lang,
                                          @PathVariable Long id,
                                          @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
    customers.deleteById(id);

    if (hxRequest != null) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(303)
        .header("Location", "/" + lang + "/customers")
        .build();
  }
}
