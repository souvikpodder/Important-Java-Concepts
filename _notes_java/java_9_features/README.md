# Java 9 Module System (JPMS)

The Java Platform Module System (JPMS), introduced in Java 9 (Project Jigsaw), is one of the most significant changes to the Java framework. It introduced a new level of abstraction above packages, known as **Modules**.

## What is a Module?
A **Module** is a self-describing collection of related code and data. It loosely groups together:
- A set of packages.
- Resources (like images or XML files).
- A **module descriptor** file (`module-info.java`) that specifies what the module requires to run and what it exposes to other modules.

Before Java 9, Java programs were built using packages, and physical distribution was done via JAR files. However, JAR files were just flat containers of packages without any inherent structure or encapsulation at the JAR level.

## Why were Modules Introduced? (The Problems Before Java 9)

1. **"JAR Hell" and Missing Dependencies**: In the classpath system, if a required JAR is missing, it wouldn't be discovered until runtime (resulting in a `NoClassDefFoundError` or `ClassNotFoundException`). 
2. **Lack of Strong Encapsulation**: A public class in Java before Java 9 was public to **everyone** on the classpath. You couldn't create a class that was public across your own packages but hidden from external consumers.
3. **Monolithic JDK**: The entire JDK was a massive monolithic artifact (`rt.jar` was over 60MB). Even if your small app only used `java.lang`, you had to carry the entire JVM library footprint.

## Core Concepts of JPMS

The module system solves these issues through the **Module Descriptor** (`module-info.java`), which lives at the root of your source directory.

### 1. `requires` (What do I need?)
Declares that this module depends on another module to compile and run. The runtime will guarantee that the required module is present.

```java
module com.mycompany.myapp {
    requires java.sql;      // Requires core Java SQL module
    requires java.desktop;  // Requires desktop UI features
}
```

### 2. `exports` (What do I share?)
By default, **nothing** in a module is visible to the outside world, even if the classes are declared `public`. You have to explicitly export packages.

```java
module com.mycompany.security {
    // Only classes inside com.mycompany.security.api can be seen by others
    exports com.mycompany.security.api; 
    
    // Classes in com.mycompany.security.internal remain completely hidden!
}
```

### 3. `requires transitive` (Implied Readability)
If Module A requires Module B, and Module B returns types from Module C, Module A also needs to read Module C. `requires transitive` ensures any module reading the current module automatically reads the transitive dependency.

```java
module com.mycompany.data {
    requires transitive java.sql; // Anyone requiring this module also gets access to java.sql
}
```

### 4. `opens` (Allowing Reflection)
Frameworks like Spring or Hibernate heavily rely on reflection to instantiate classes or inject dependencies. By default, strong encapsulation prevents deep reflection into a module. `opens` allows packages to be accessed via reflection at runtime.

```java
module com.mycompany.model {
    // Allows reflection at runtime, e.g., for JSON serialization or Hibernate
    opens com.mycompany.model.entities; 
}
```

### 5. `uses` and `provides` (Services)
The module system has built-in support for the Service Locator pattern, abstracting implementations from their interfaces.

```java
module com.mycompany.service.provider {
    // Declaring that we provide an implementation for an interface
    provides com.mycompany.api.PaymentService 
        with com.mycompany.impl.StripePaymentService;
}
```

```java
module com.mycompany.service.consumer {
    // Declaring that we will consume implementations of this interface
    uses com.mycompany.api.PaymentService;
}
```

## Benefits of the Module System

1. **Reliable Configuration**: Dependencies are explicitly declared and resolved at startup. If a required module is missing, the application fails immediately at startup (or compile-time), rather than randomly crashing later.
2. **Strong Encapsulation**: You can completely hide internal APIs. A package not explicitly `exported` is invisible to other modules, enforcing better architecture and preventing people from using your internal utility classes.
3. **Scalable / Custom JDK (jlink)**: Because the JDK itself is now modularized (e.g., `java.base`, `java.desktop`, `java.sql`), you can use the `jlink` tool to create a custom shrunk-down Java Runtime Environment (JRE) containing **only** the modules your application needs. This is perfect for Docker/Cloud deployments.
4. **Improved Performance**: Knowing the exact dependency graph allows the JVM to optimize class-loading more efficiently.

## Example Structure

Imagine an application split into two modules: `app.core` and `app.utils`.

**Directory Structure:**
```text
src/
├── app.utils/
│   ├── module-info.java
│   └── com/app/utils/
│       └── StringUtils.java
└── app.core/
    ├── module-info.java
    └── com/app/core/
        └── Main.java
```

**`app.utils/module-info.java`:**
```java
module app.utils {
    exports com.app.utils;
}
```

**`app.core/module-info.java`:**
```java
module app.core {
    requires app.utils;
}
```

In this setup, `app.core` can successfully import and use `StringUtils.java`, while any internal `com.app.utils.internal` packages would be completely secure and hidden from `app.core`.

## Important Built-in JDK Modules

When Java 9 modularized the JDK, it split the monolithic runtime into over 90 standard modules. You can view them all by running `java --list-modules` in your terminal. Here are a few of the most important core modules:

1. **`java.base`**: The foundational module of Java. **Every** other module implicitly requires `java.base` (you don't have to write `requires java.base`). It contains the core packages you use every day, like `java.lang`, `java.util`, `java.io`, `java.nio`, `java.net`, and `java.math`.
2. **`java.sql`**: Contains the JDBC API for database connectivity (`java.sql`, `javax.sql`).
3. **`java.desktop`**: Contains the APIs for building desktop interfaces, like AWT and Swing, as well as sound and imaging tools.
4. **`java.xml`**: Contains the core XML processing APIs (JAXP).
5. **`java.logging`**: Contains the standard `java.util.logging` API.
6. **`jdk.jlink`**: Contains the `jlink` tool itself, heavily utilized for creating custom runtime environments.

Because of this split, if your application only needs `java.base` and `java.sql` to function, you can build a severely stripped-down JRE that leaves behind everything in `java.desktop`!

## Deep Dive: `jlink` and Custom Runtime Images

One of the most consequential features enabled by the Module System is **`jlink`** (the Java Linker). 

### What is `jlink`?
Before Java 9, if you wanted to distribute your Java application so that the user didn't have to install Java themselves, you had to bundle the entire JRE (Java Runtime Environment) with your app. This JRE included every single library Java offered (desktop GUIs, sound libraries, enterprise tools), making it very large (often 100MB to 200MB+).

`jlink` is a command-line tool that allows you to link together a set of modules, along with their transitive dependencies, to create a **custom, stripped-down runtime image**.

### How it works
Because you declared exactly what your application needs in `module-info.java` (e.g., `requires java.base`, `requires java.sql`), `jlink` can trace the exact dependency graph. It looks at your modules, looks at the JDK modules, throws away everything you *don't* require, and generates a miniature JRE specifically tailored for your application.

### Why is this important?

1. **Massively Reduced Size**: If you write a lightweight microservice that only requires `java.base` and `java.logging`, `jlink` can generate a runtime that is as small as 15MB to 30MB. This is transformative for **Docker containers** and cloud deployments where smaller images mean faster deployments and less storage costs.
2. **Improved Security**: By stripping out unused modules (e.g., desktop tools, RMI, internal scripting engines), you drastically reduce the attack surface of your application. Hackers cannot exploit vulnerabilities in classes that physically do not exist in your runtime.
3. **Faster Startup**: A smaller runtime means less disk I/O, a smaller footprint for the JVM to interpret, and slightly faster startup times.

### Example Usage

Assuming you have a compiled module named `com.mycompany.app` sitting in a `mods/` directory, you can build a custom image like this:

```bash
jlink --module-path mods;%JAVA_HOME%/jmods \
      --add-modules com.mycompany.app \
      --output custom-jre
```

This creates a brand new directory folder named `custom-jre`. Inside it, you will find a custom `bin/java` executable that can run your app, and it will contain absolutely nothing extra.
