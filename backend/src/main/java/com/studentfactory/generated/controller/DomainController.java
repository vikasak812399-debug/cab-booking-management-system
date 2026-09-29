package com.studentfactory.generated.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentfactory.generated.model.AuditLog;
import com.studentfactory.generated.model.DomainItem;
import com.studentfactory.generated.repository.AuditLogRepository;
import com.studentfactory.generated.repository.DomainItemRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/domain")
public class DomainController {
  private final ObjectMapper mapper;
  private final DomainItemRepository items;
  private final AuditLogRepository auditLogs;

  public DomainController(ObjectMapper mapper, DomainItemRepository items, AuditLogRepository auditLogs) {
    this.mapper = mapper;
    this.items = items;
    this.auditLogs = auditLogs;
  }

  @GetMapping("/{entityName}")
  public Object list(
    @PathVariable String entityName,
    @RequestParam(required = false) Integer page,
    @RequestParam(defaultValue = "10") Integer limit
  ) {
    List<Map<String, Object>> all = items.findByEntityName(entityName).stream().map(this::toMap).toList();
    if (page == null) return all;

    int safeLimit = Math.min(Math.max(limit, 1), 100);
    int from = Math.min(Math.max(0, page - 1) * safeLimit, all.size());
    int to = Math.min(from + safeLimit, all.size());
    int totalPages = Math.max(1, (int) Math.ceil(all.size() / (double) safeLimit));

    return Map.of(
      "data", all.subList(from, to),
      "meta", Map.of(
        "page", page,
        "limit", safeLimit,
        "total", all.size(),
        "totalPages", totalPages
      )
    );
  }

  @PostMapping("/{entityName}")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Object> create(
    @PathVariable String entityName,
    @RequestBody Map<String, Object> body
  ) throws JsonProcessingException {
    DomainItem item = new DomainItem();
    item.entityName = entityName;
    item.payload = mapper.writeValueAsString(body);
    DomainItem saved = items.save(item);

    AuditLog log = new AuditLog();
    log.actor = "admin@example.com";
    log.action = "create";
    log.entity = entityName;
    log.entityId = saved.id;
    log.payload = item.payload;
    auditLogs.save(log);

    return toMap(saved);
  }

  private Map<String, Object> toMap(DomainItem item) {
    Map<String, Object> result = new LinkedHashMap<>();
    try {
      Map<String, Object> data = mapper.readValue(item.payload, new TypeReference<Map<String, Object>>() {});
      result.putAll(data);
    } catch (Exception ignored) {
      // if the saved text is broken, we still return id and status
    }
    result.put("id", item.id);
    result.putIfAbsent("status", item.status);
    return result;
  }
}