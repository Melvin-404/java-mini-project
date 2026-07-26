# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |

## Reporting a Vulnerability

If you discover a security vulnerability in this project, please report it by emailing muhammedadnanshakil456@gmail.com.

We will acknowledge receipt within 48 hours and provide an estimated timeline for a fix.

Please do **not** open public issues for security vulnerabilities.

## Security Notes

This is an academic/demo project. The following are **known security limitations** and are not considered active vulnerabilities for the purposes of this project:

- Hardcoded credentials in source code (`teacher/teacher123`, `student/student123`)
- No password hashing
- No HTTPS
- No CSRF protection
- PostgreSQL password hardcoded in `config.properties`
- No input sanitization beyond JDBC prepared statements

These are documented as architectural tradeoffs and should be addressed before any production use.
