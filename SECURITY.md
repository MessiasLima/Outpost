# Security Policy

This policy applies to the Outpost platform itself **and every application built on top of Outpost**. Whether the vulnerability is in the shared framework, a platform bridge, or an app derived from the platform, please follow the responsible disclosure process below.

## Supported Versions

We provide security patches only for the latest stable release of Outpost and apps built on the latest stable release of Outpost. To ensure you have the most recent security fixes, we recommend always staying up to date with the latest version.

| Version | Supported |
|---------|-----------|
| Latest stable release of Outpost | ✅ |
| Older releases of Outpost | ❌ |

## Reporting a Vulnerability

If you discover a security vulnerability in Outpost or in any app built on top of Outpost, please do not open a public issue. Reporting publicly creates a "zero-day" risk for all users.

### Preferred Method: GitHub Private Reporting

Please use the [GitHub Private Vulnerability Reporting feature](https://github.com/MessiasLima/Outpost/security/advisories/new). This is the most secure and efficient way for us to track and resolve the issue.

### Alternative Method: Email

If you are unable to use GitHub's reporting tool, you can contact us directly at: `engineering@appoutlet.dev`

### What to Include

To help us triage and patch the issue quickly, please include:

- A descriptive title and a summary of the vulnerability.
- The specific version of Outpost (or the app built on Outpost) where the issue was found.
- Steps to reproduce (or a proof-of-concept script).
- Potential impact (e.g., unauthorized data access, CI/CD secret leakage).

## Our Commitment

We will acknowledge receipt of your report within 48 hours. We will keep you updated on our progress as we work toward a resolution and will credit you for the discovery once the vulnerability is patched.

Thank you for helping us keep Outpost and all apps built on it secure!
