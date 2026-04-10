# AGENTS.md - Development Guidelines for This Repository

## Project Overview

This is a Java 17 microservices backend project using Spring Boot 3.2.6, Spring Cloud 2023.0.2, and MyBatis-Plus. The project is a Maven multi-module project.

## Project Structure

```
backend/
├── pom.xml                          # Parent POM
├── common/                          # Common modules
│   ├── common-core/
│   ├── common-datasource/
│   ├── common-feign/
│   ├── common-mq/
│   ├── common-redis/
│   └── common-security/
├── service/
│   └── user-service/               # User microservice
└── infrastructure/
    └── gateway/                    # API Gateway
```

## Build Commands

### Full Build
```bash
mvn clean install
```

### Build Single Module
```bash
mvn clean install -pl service/user-service -am
```

### Run Application
```bash
cd service/user-service
mvn spring-boot:run
```

### Package
```bash
mvn clean package
```

### Running Tests
Currently there are no test files in the project. When tests are added:

```bash
# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=UserServiceImplTest

# Run a single test method
mvn test -Dtest=UserServiceImplTest#testRegister
```

### Linting
This project does not have a configured linter. IDE auto-formatting (VS Code with Java extensions) is recommended.

## Code Style Guidelines

### Package Structure
- Base package: `com.example`
- Service packages follow pattern: `com.example.<module>.<layer>`
  - `controller`, `service`, `service.impl`, `entity`, `mapper`, `common`, `config`

### Naming Conventions
- **Classes**: PascalCase (e.g., `UserService`, `UserController`)
- **Methods**: camelCase (e.g., `findByUsername`, `register`)
- **Variables**: camelCase (e.g., `userMapper`, `passwordEncoder`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `serialVersionUID`)

### Imports
- Order imports by:
  1. Java/JDK packages
  2. Spring packages
  3. Third-party packages (MyBatis-Plus, Lombok, etc.)
  4. Internal packages
- Use wildcard imports sparingly (avoid `import com.example.user.*`)

### Lombok Usage
Lombok is used throughout the codebase:
- `@Data` - for entity/DTO classes (generates getters/setters/toString)
- `@RequiredArgsConstructor` - for dependency injection via constructor
- `@NoArgsConstructor`, `@AllArgsConstructor` - for Result wrapper classes
- Avoid `@Getter`/`@Setter` on individual fields; use `@Data` on class level

### Database Layer (MyBatis-Plus)
- Use `@TableName`, `@TableField`, `@TableId` annotations for mapping
- Use `IdType.AUTO` for auto-incrementing primary keys
- Use `@TableLogic` for soft deletes
- Use `LambdaQueryWrapper` for type-safe query building
- Entity fields should match database column names or use explicit `@TableField`

### REST API Patterns
- Use `@RestController` for REST endpoints
- Use `@RequestMapping` with versioning (e.g., `/v1/users`)
- Use `@Validated` on controller class for validation
- Use `@NotBlank`, `@NotNull` from `jakarta.validation.constraints` for input validation
- Wrap all responses in `Result<T>` wrapper class
- Use standard HTTP methods: `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`

### Error Handling
- Throw `RuntimeException` with descriptive messages in service layer
- Catch exceptions in controller layer and return appropriate `Result.error(code, message)`
- Use meaningful error messages: "Username already exists", "User not found", "Wrong password"
- Return appropriate HTTP status codes in Result: 200 (success), 400 (bad request), 401 (unauthorized), 404 (not found), 500 (server error)

### Security
- Use `BCryptPasswordEncoder` for password hashing
- Never return passwords in API responses (set to null before returning)
- Use `spring-security-test` for testing secured endpoints

### Type Usage
- Use primitive types where appropriate (`int` vs `Integer`)
- Use boxed types for nullable database fields (`Integer` for `deleted` column)
- Use `Long` for ID fields
- Use `LocalDateTime` for timestamp fields

### DTO/Entity Patterns
- Entities map to database tables (e.g., `User` -> `user` table)
- Use separate DTOs for request/response if needed
- Entity classes should implement `Serializable`

## Configuration

### Application Configuration
Configuration is in `service/user-service/src/main/resources/application.yml`:
- Database connection (MySQL)
- Nacos service discovery
- Server port

### Environment Variables
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR`

## VS Code Settings
The project has `.vscode/settings.json` with:
```json
{
    "java.compile.nullAnalysis.mode": "automatic"
}
```

## Adding New Services

1. Create module directory under `service/`
2. Add module to parent `pom.xml` `<modules>` section
3. Create `pom.xml` with Spring Boot starter dependencies
4. Create main application class with `@SpringBootApplication`
5. Follow the layered structure: controller -> service -> mapper -> entity
