# Spring Security, OAuth 2.0, and WSO2 Identity Server

This guide covers the core concepts of Spring Security, the OAuth 2.0 protocol, and how to integrate a Spring Boot application with WSO2 Identity Server as an Identity Provider (IdP).

---

## 🔒 1. Spring Security Overview

Spring Security is a powerful and highly customizable authentication and access-control framework for Java applications. It is the de-facto standard for securing Spring-based applications.

### Core Concepts

*   **Authentication (Who are you?):** The process of verifying the identity of a user, device, or system.
*   **Authorization / Access Control (What can you do?):** The process of verifying whether an authenticated user has permission to perform a specific action or access a specific resource.
*   **Principal:** The currently authenticated user.
*   **Granted Authority:** The permissions or roles assigned to the principal.
*   **SecurityFilterChain:** A chain of filters that Spring Security uses to intercept incoming HTTP requests and apply security logic (like verifying tokens, checking roles).

---

## 🔑 2. OAuth 2.0 Overview

OAuth 2.0 is an industry-standard protocol for **Authorization**. It enables a third-party application to obtain limited access to an HTTP service, either on behalf of a resource owner or by allowing the third-party application to obtain access on its own behalf.

**Note:** While OAuth 2.0 is primarily for authorization, **OpenID Connect (OIDC)** is an identity layer built *on top* of OAuth 2.0, providing **Authentication** (identity verification).

### OAuth 2.0 Roles

1.  **Resource Owner:** The user who owns the data and grants access to it.
2.  **Client:** The application requesting access to the user's data (e.g., your Spring Boot App).
3.  **Authorization Server:** The server that authenticates the Resource Owner and issues Access Tokens to the Client (e.g., **WSO2**, Keycloak, Okta).
4.  **Resource Server:** The API server hosting the protected data, which accepts and validates the Access Token.

### Common Grant Types

*   **Authorization Code:** The most secure and common flow for web apps. The client exchanges an authorization code for an access token.
*   **Client Credentials:** Used for machine-to-machine communication where there is no user involved.
*   **Implicit (Legacy):** Previously used for SPAs, but no longer recommended due to security concerns (use Authorization Code with PKCE instead).

---

## 🛡️ 3. WSO2 Identity Server Integration

WSO2 Identity Server (WSO2 IS) is an open-source Identity and Access Management (IAM) product. In this architecture, WSO2 acts as the **Authorization Server / Identity Provider (IdP)**.

### Architecture Flow

1.  User tries to access a protected resource in your Spring Boot application.
2.  Spring Security detects the user is unauthenticated and redirects them to WSO2 IS login page.
3.  User authenticates with WSO2 (username/password, MFA, etc.).
4.  WSO2 redirects back to your Spring Boot app with an Authorization Code.
5.  Spring Boot exchanges the code for an **Access Token** and an **ID Token (OIDC)**.
6.  The user is now logged in to your Spring application.

### Spring Boot Setup (OAuth2 Client)

To integrate, include the dependency in your `pom.xml` or `build.gradle`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

#### Application Properties (`application.yml`)

Configure WSO2 as the provider. You need to create a Service Provider in WSO2 to get the `client-id` and `client-secret`.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          wso2:
            client-id: your-wso2-client-id
            client-secret: your-wso2-client-secret
            client-authentication-method: client_secret_basic
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile, email
        provider:
          wso2:
            issuer-uri: https://<wso2-host>:<port>/oauth2/token
            authorization-uri: https://<wso2-host>:<port>/oauth2/authorize
            token-uri: https://<wso2-host>:<port>/oauth2/token
            user-info-uri: https://<wso2-host>:<port>/oauth2/userinfo
            jwk-set-uri: https://<wso2-host>:<port>/oauth2/jwks
```
*(Note: If WSO2 supports OIDC Discovery, you might only need the `issuer-uri` property).*

#### Security Configuration (Java)

Configure your `SecurityFilterChain` in Spring Boot 3.x to enforce OAuth2 login.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Require authentication for all endpoints
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/public/**").permitAll() // Allow public endpoints
                .anyRequest().authenticated() // Everything else requires auth
            )
            // Enable OAuth2 Login
            .oauth2Login(oauth2 -> oauth2
                 // You can customize the login page or success handler here
                .defaultSuccessUrl("/dashboard", true)
            )
            // Optional: configure logout
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
            );
            
        return http.build();
    }
}
```

### Passing Tokens to Downstream Microservices

If your Spring application is an API Gateway or needs to call other microservices (acting as a Resource Server), it should extract the token or act as a relay.

If you are building a **Resource Server** (an API that only accepts tokens, not a frontend web app), use the resource server dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

And configure it to validate JWTs signed by WSO2:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://<wso2-host>:<port>/oauth2/token
          # jwk-set-uri: https://<wso2-host>:<port>/oauth2/jwks
```

```java
@Bean
public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
}
```

---

## 🔐 4. Deep Dive: Authentication, Authorization, and JWTs

When combining Spring Security, OAuth2, and WSO2, it is crucial to understand exactly *how* Authentication and Authorization are separated and how JSON Web Tokens (JWTs) tie them together.

### The Authentication Flow (WSO2 & The ID Token)
1. **User Identity:** WSO2 manages the user database. It is the only component that ever checks the user's raw password or multifactor authentication (MFA).
2. **The ID Token:** Once WSO2 verifies the credentials, it generates an **ID Token** (via OpenID Connect). This is a JWT that contains claims like the user's `email`, `sub` (subject/username), and `name`.
3. **Purpose:** This ID Token is used by the frontend client (e.g., Angular/React) to know *who* logged in and to display their profile picture or welcome message. It is **not** usually sent to the backend API.

### The Authorization Flow (Spring Boot & The Access Token)
1. **The Access Token:** Along with the ID Token, WSO2 generates an **Access Token** (also a JWT). This token contains `scopes` (what the client app is allowed to do) and `roles` (what the user is allowed to do).
2. **The Request:** The frontend attaches this Access Token to the HTTP Request:
   ```http
   Authorization: Bearer <JWT_ACCESS_TOKEN>
   ```
3. **Resource Server Validation:** When the request hits Spring Boot:
   * **Authentication of the Token:** Spring Security extracts the JWT. It fetches WSO2's Public Key (from the `jwk-set-uri`). It uses this public key to mathematically verify the Token's **Signature**. If the signature is valid, Spring *trusts* that WSO2 issued it and that it hasn't been tampered with. Spring also checks if the token has expired (`exp` claim).
   * **Authorization of the Endpoint:** After the token is verified, Spring reads the `roles` or `scopes` payload inside the JWT. If your Spring Security setup requires specific roles (e.g., `@PreAuthorize("hasRole('ADMIN')")`), Spring checks if the string `"ADMIN"` exists inside the JWT's claims. If it does, the request is allowed.

> **Key takeaway:** Spring Boot never has to make a database call or contact WSO2 to authenticate the user for every API request. The cryptographic signature of the JWT ensures the token is secure and authentic, making this microservice architecture stateless and highly scalable.

---

## 🗣️ 5. Interview Preparation Q&A

Here are common interview questions and real-world analogies to explain these concepts effectively.

### Q: How does Spring Security work under the hood?
**Answer:** "At its core, Spring Security is just a chain of Servlet Filters called the **SecurityFilterChain**."
*   **Analogy:** Think of your API as an exclusive nightclub, and the `SecurityFilterChain` is the line of bouncers at the door.
    *   **Bouncer 1 (SecurityContextPersistenceFilter):** Checks if you already have a wristband from a previous visit.
    *   **Bouncer 2 (UsernamePasswordAuthenticationFilter):** Asks for your ID and checks if it matches the guest list.
    *   **Bouncer 3 (FilterSecurityInterceptor):** Checks if your specific wristband color allows you into the VIP area (Authorization).

### Q: What is the difference between Authentication and Authorization?
**Answer:**
*   **Authentication** is verifying *who* you are (e.g., entering a username and password, or presenting a valid JWT). 
*   **Authorization** is verifying *what you are allowed to do* (e.g., does your user profile have the `ROLE_ADMIN` to access this endpoint?).

### Q: What is the difference between OAuth 2.0 and OpenID Connect (OIDC)?
**Answer:**
"OAuth 2.0 is an **Authorization** protocol (delegating access without sharing passwords). It was never designed for identity. **OIDC** was built on top of it to add an **ID Token** so the client app knows *who* logged in."
*   **Analogy:** Providing a valet key to a parking attendant. The key (OAuth 2.0 Access Token) allows the attendant to park the car (**Authorization**), but it doesn't open the glove box or tell them your name. If the attendant also asks for your Driver's License to see your face, that is OpenID Connect (**Authentication**).

### Q: Why use an external Identity Provider like WSO2 instead of just writing token logic in Spring Boot?
**Answer:**
"In a microservices architecture, having every microservice manage passwords and token signing violates the Single Responsibility Principle. By using WSO2 as our IdP, we centralize user management. Our Spring Boot microservices become **Resource Servers**—they don't know the user's password; they simply verify WSO2's digital signature on the incoming JWT and map the claims to permissions to grant access."

---

## 🎯 6. Summary

1.  **Spring Security** provides the underlying filter infrastructure protecting your endpoints.
2.  **OAuth 2.0 / OIDC** are the protocols dictating how tokens are acquired and validated.
3.  **WSO2 IS** is the external service that actually verifies the user's password and issues the signed Web Tokens (JWTs).
4.  By configuring Spring as an `oauth2-client` or `oauth2-resource-server`, Spring delegates the heavy lifting of password management to WSO2 and trusts its cryptographic signatures.
