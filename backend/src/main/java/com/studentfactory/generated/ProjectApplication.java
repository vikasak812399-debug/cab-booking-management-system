package com.studentfactory.generated;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {
  public static void main(String[] args) {
    configureDatabase();
    SpringApplication.run(ProjectApplication.class, args);
  }

  private static void configureDatabase() {
    String dbType = env("DB_TYPE", "postgres");
    if ("sqlite".equals(dbType)) {
      String sqlitePath = env("DB_SQLITE_PATH", "./data/cab-booking-management-system.sqlite");
      System.setProperty("spring.datasource.url", "jdbc:sqlite:" + sqlitePath);
      System.setProperty("spring.datasource.driver-class-name", "org.sqlite.JDBC");
      System.setProperty("spring.jpa.database-platform", "org.hibernate.community.dialect.SQLiteDialect");
      return;
    }

    String host = env("DB_HOST", "localhost");
    String port = env("DB_PORT", "mysql".equals(dbType) ? "3306" : "5432");
    String name = env("DB_NAME", "student_factory");
    String user = env("DB_USER", "mysql".equals(dbType) ? "student_factory" : "postgres");
    String password = env("DB_PASSWORD", "mysql".equals(dbType) ? "mysql" : "postgres");
    String url = "mysql".equals(dbType)
      ? "jdbc:mysql://" + host + ":" + port + "/" + name + "?allowPublicKeyRetrieval=true&useSSL=true&requireSSL=true"
      : "jdbc:postgresql://" + host + ":" + port + "/" + name;

    System.setProperty("spring.datasource.url", url);
    System.setProperty("spring.datasource.username", user);
    System.setProperty("spring.datasource.password", password);
    System.setProperty("spring.datasource.driver-class-name", "mysql".equals(dbType) ? "com.mysql.cj.jdbc.Driver" : "org.postgresql.Driver");
    System.setProperty("spring.jpa.database-platform", "mysql".equals(dbType) ? "org.hibernate.dialect.MySQLDialect" : "org.hibernate.dialect.PostgreSQLDialect");
  }

  private static String env(String key, String fallback) {
    String value = System.getenv(key);
    return value == null || value.isBlank() ? fallback : value;
  }
}
