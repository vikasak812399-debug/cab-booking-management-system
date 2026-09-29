package com.studentfactory.generated.repository;

import com.studentfactory.generated.model.DomainItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainItemRepository extends JpaRepository<DomainItem, String> {
  List<DomainItem> findByEntityName(String entityName);
}