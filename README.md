# Recruitment Starter

`starter` is a Spring Boot starter that provides reusable building blocks for:

- request-context based security helpers
- global exception advice registration
- Jackson `ObjectMapper` defaults
- Snowflake ID generation
- audit data integration (`AuditorAware` + JPA auditing)

## Installation (JitPack)

Current release tag: `1.0.1`

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Bravos-World:Recruitment-Starter:1.0.1")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Bravos-World</groupId>
        <artifactId>Recruitment-Starter</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

## Auto-configuration

This starter contributes the following auto-configurations through
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`:

- `com.bravos.recruitment.libs.starter.configuration.audit.AuditAwareAutoConfiguration`
- `com.bravos.recruitment.libs.starter.configuration.audit.AuditJpaAutoConfiguration`
- `com.bravos.recruitment.libs.starter.configuration.objectmapper.ObjectMapperAutoConfiguration`
- `com.bravos.recruitment.libs.starter.configuration.snowflake.SnowflakeAutoConfiguration`

## Enabling optional modules

Some modules are intentionally opt-in through annotations:

```java
@EnableSecurity
@EnableGlobalExceptionHandler
@SpringBootApplication
public class Application {
}
```

## Configuration properties

| Property | Default | Description |
| --- | --- | --- |
| `starter.object-mapper.enabled` | `true` | Enables `ObjectMapperAutoConfiguration`. |
| `starter.object-mapper.serialize-long-as-string` | `true` | Serializes `Long` and `BigInteger` as JSON strings. |
| `starter.audit.data.enabled` | `true` | Enables audit auto-configuration and JPA auditing integration when available. |
| `starter.audit.data.default-system-id` | `0` | Fallback auditor ID when no authenticated user is available. |
| `starter.snowflake.machine-id` | `1` | Worker/machine ID used by the Snowflake generator. |
| `starter.security.internal-secret` | _(required)_ | Internal service secret validated against `X-Internal-Secret`. |
| `starter.security.internal-path-prefix` | `/internal/` | Path prefix treated as internal-only traffic. |
| `starter.security.excluded-paths` | `[]` | URI prefixes bypassed by the security filter. |

## Security request headers

The security filter reads these headers from incoming requests:

- `X-Authenticated`: marks an external user request as authenticated when `true`
- `X-UserId`: authenticated user ID
- `X-Authorities`: comma-separated authorities
- `X-Internal-Secret`: shared secret for internal endpoint access

## Annotation usage

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Authenticated
    @HasPermission(resource = "user", action = Action.READ)
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable long id) {
        // ...
    }

    @Internal
    @PostMapping("/internal/reindex")
    public void reindex() {
        // ...
    }
}
```

For entities, you can use `@SnowflakeGeneratedId` with Hibernate:

```java
@Entity
public class UserEntity {

    @Id
    @SnowflakeGeneratedId
    private Long id;
}
```

