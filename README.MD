# 📇 Contacts Management System

A command-line application written in **Java** for managing personal and organizational contacts. The project follows Object-Oriented Programming principles, features a modular service-oriented architecture, and includes automated testing and static code analysis.

---

## 🏗 Project Structure

The project is organized into logical packages to improve maintainability and readability.

### `contacts`
Contains the application entry point (`Main.java`), responsible for coordinating user interaction and application workflow.

### `contacts.Entity`
Contains the domain models.

- **`Contact`** – Abstract base class for all contact types.
- **`Person`** – Represents an individual contact with:
    - Name
    - Surname
    - Birthdate
    - Gender
    - Phone number
- **`Organization`** – Represents an organization with:
    - Organization name
    - Address
    - Phone number

### `contacts.Services`
Contains the business logic.

- **`PersonService`** – Creates and validates person records.
- **`OrganizationService`** – Creates and validates organization records.
- **`ListService`** – Displays records and allows viewing detailed information.
- **`SearchService`** – Performs case-insensitive searches across contact fields.
- **`EditService`** – Supports editing and deleting existing records.
- **`SerializationUtils`** – Handles saving and loading contacts using Java serialization.

### `contacts.Enums`
Contains application enums such as `Action`.

### `contacts.Exceptions`
Contains custom exceptions (e.g. `BadIndexException`) for safer error handling.

---

# 🚀 Getting Started

## Requirements

- Java **17** or later (recommended: **Java 23**)
- Apache Maven

---

## Build

Compile the project and create the executable JAR:

```bash
mvn clean package
```

---

## Run

### Temporary Session (without saving)

```bash
java -cp target/Contacts-1.0-SNAPSHOT.jar contacts.Main
```

### Persistent Mode (save/load contacts)

```bash
java -cp target/Contacts-1.0-SNAPSHOT.jar contacts.Main open contacts.db
```

---

# ✨ Features

The application provides an interactive console menu with the following commands:

| Command | Description |
|---------|-------------|
| `add` | Create a new person or organization. |
| `list` | Display all contacts and inspect individual records. |
| `search` | Search contacts across all available fields. |
| `count` | Show the total number of stored contacts. |
| `exit` | Exit the application safely. |

After selecting a record, you can:

- Edit its fields
- Delete it
- Return to the main menu

---

# 🧪 Testing

The project includes automated unit tests written with **JUnit 5**.

Run all tests:

```bash
mvn test
```

---

# 📊 Code Quality

## Checkstyle

Verify coding style compliance:

```bash
mvn checkstyle:check
```

---

## JaCoCo Coverage Report

Generate the code coverage report:

```bash
mvn jacoco:report
```

Open the generated report in your browser:

```text
target/site/jacoco/index.html
```

---

# 🛠 Technologies

- Java 23
- Maven
- JUnit 5
- JaCoCo
- Checkstyle
- Java Serialization
- Object-Oriented Programming (OOP)