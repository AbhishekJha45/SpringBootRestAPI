## Authentication Service

#This repository contains a simple authentication service that supports user registration, login, JWT token-based authorization, token revocation, and token refreshing.

## Getting Started

To get started with the authentication service, clone the repository and set up your environment.

```bash
git clone https://github.com/AbhishekJha45/SpringBootRestAPI.git
cd authentication-service
# Ensure you have Java and Maven installed, then build the project:
```bash
mvn clean install
mvn spring-boot:run
# Create a new user account with an email and password.
```bash
## Signup
 curl -X POST http://localhost:8080/auth/signup \
-H "Content-Type: application/json" \
-d '{
    "email": "testuser@example.com", 
    "password": "password"
}'

##Signin
# Authenticate user credentials and receive a JWT token for further requests.
curl -X POST http://localhost:8080/auth/signin \
-H "Content-Type: application/json" \
-d '{
    "email": "testuser@example.com", 
    "password": "password"
}'
## Token Revocation
# Revoke an existing token to invalidate the user's session.

```bash
curl -X POST http://localhost:8080/auth/revoke \
-H "Authorization: Bearer <your_jwt_token>"
## Token Refreshing
# Obtain a new JWT token using the refresh token before the current token expires.
curl -X POST http://localhost:8080/auth/refresh \
-H "Authorization: Bearer <refresh_token>"

### Functionality Overview
- Sign Up: Register a new user by providing an email and password.
- Sign In: Validate user credentials and return a JWT token for authorized access.
- Token Authorization: Ensure requests are authenticated by validating the provided JWT token.
- Token Revocation: Securely invalidate a token, preventing further access.
- Token Refresh: Refresh the JWT token to maintain an active session without re-authentication.







