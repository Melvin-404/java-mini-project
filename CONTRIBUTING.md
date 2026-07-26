# Contributing to Smart Attendance Management System

We welcome contributions! Follow these guidelines to get started.

## Getting Started

1. Fork the repository.
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/java-mini-project.git
   ```
3. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Setup

- **Java 17** (JDK 17+)
- **Maven** (or use bundled `mvnw.cmd`)
- **Tomcat 9** (or compatible servlet container)
- The project uses H2 by default — no database setup required for development.

## Code Standards

- Follow existing code style and naming conventions
- Use prepared statements for all database queries
- Keep Servlets thin — delegate business logic to Service/DAO layers
- Write Javadoc for public methods
- Test manually via the web UI before submitting

## Commit Messages

Use conventional commits:
```
feat: add attendance export to CSV
fix: correct date parsing in RecordsServlet
docs: update ARCHITECTURE.md
```

## Pull Request Process

1. Ensure your branch is rebased on the latest `main`.
2. Update or add documentation if your change introduces new behavior.
3. Verify the application builds and runs:
   ```bash
   ./mvnw clean package
   ```
4. Submit a PR with a clear description of what it does and why.

## Code of Conduct

Be respectful and constructive. Harassment or toxic behavior will not be tolerated.

## Questions?

Open a discussion or email muhammedadnanshakil456@gmail.com.
