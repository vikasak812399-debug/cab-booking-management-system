package com.studentfactory.generated.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "domain_items")
public class DomainItem {
  @Id
  public String id;

  @Column(name = "entity_name")
  public String entityName;

  @Column(columnDefinition = "TEXT")
  public String payload = "{}";

  public String status = "active";

  @PrePersist
  public void prePersist() {
    if (id == null) id = UUID.randomUUID().toString();
  }
}