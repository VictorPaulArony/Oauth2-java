# Spring Boot GitHub OAuth2 Login

A complete implementation of GitHub OAuth2 authentication with user registration and profile management.

## Features

-  GitHub OAuth2 Authentication
- Automatic user registration
- Rich GitHub profile data (repos, followers, following)
- User profile management
- Session management
- GitHub-themed UI (dark mode)
- H2 database for demo purposes
- Secure logout functionality

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- GitHub account

## GitHub OAuth2 Setup

### 1. Create GitHub OAuth App

1. Go to [GitHub Settings](https://github.com/settings/developers)
2. Click **OAuth Apps** in the left sidebar
3. Click **New OAuth App** button
4. Fill in the application details:
    - **Application name**: Spring Boot OAuth2 Demo (or your preferred name)
    - **Homepage URL**: `http://localhost:8080`
    - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
    - **Application description**: (optional)
5. Click **Register application**
6. Copy your **Client ID**
7. Click **Generate a new client secret** and copy the **Client Secret**

### 2. Configure Application

Update `src/main/resources/application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: YOUR_GITHUB_CLIENT_ID_HERE
            client-secret: YOUR_GITHUB_CLIENT_SECRET_HERE
```

**Important**: Keep your client secret secure! Never commit it to version control.


## Running the Application

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Using JAR

```bash
mvn clean package
java -jar target/github-oauth2-login-1.0.0.jar
```

The application will start on `http://localhost:8080`

## Available Endpoints

- **/login** - Login page with GitHub OAuth2 button
- **/profile** - Raw profile data page
- **/logout** - Logout endpoint
- **/h2-console** - H2 database console (JDBC URL: `jdbc:h2:mem:testdb`)

## How It Works

### OAuth2 Flow

1. User clicks "Continue with GitHub"
2. Redirected to GitHub's authorization page
3. User authorizes the application
4. GitHub redirects back with authorization code
5. Application exchanges code for access token
6. Application retrieves user info from GitHub API
7. `UserService` processes the user:
    - Creates new user if first login
    - Updates existing user information
    - Stores GitHub profile data (repos, followers, etc.)
8. User is redirected to dashboard

### GitHub Data Retrieved

The application retrieves and stores:
- Username (login)
- Display name
- Email address
- Avatar URL

### Database Schema

The `User` entity stores:
- `id` - Primary key
- `email` - User email
- `name` - User's display name
- `username` - GitHub username
- `profilePicture` - Avatar URL
- `providerId` - GitHub's user ID
- `createdAt` - Account creation timestamp
- `lastLoginAt` - Last login timestamp

## Security Features

- OAuth2 state parameter validation
- Secure session management
- Protected routes requiring authentication
- Token-based authentication
- Logout functionality with session invalidation

## Testing

1. Start the application
2. Navigate to `http://localhost:8080`
3. Click "Continue with GitHub" and authenticate
4. Authorize the application when prompted
5. View your GitHub profile data on the dashboard
6. Check H2 console at `http://localhost:8080/h2-console` to see stored users

### H2 Console Access

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

### Testing Query

```sql
SELECT * FROM users;
```

## Customization

### Requesting Additional Scopes

To access more GitHub data, update `application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            scope:
              - read:user
              - user:email
              - repo  # Access to repositories
              - read:org  # Access to organization data
```

### Add Multiple OAuth Providers

You can combine GitHub with Google, Facebook, etc.:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: YOUR_GITHUB_CLIENT_ID
            client-secret: YOUR_GITHUB_CLIENT_SECRET
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
```

### Change Database

Replace H2 dependency in `pom.xml` with PostgreSQL:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/oauth2db
    username: your_username
    password: your_password
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## Production Considerations

Before deploying to production:

1. Replace H2 with production database (PostgreSQL, MySQL)
2. Use environment variables for secrets:
   ```bash
   export GITHUB_CLIENT_ID=your_client_id
   export GITHUB_CLIENT_SECRET=your_client_secret
   ```
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             github:
               client-id: ${GITHUB_CLIENT_ID}
               client-secret: ${GITHUB_CLIENT_SECRET}
   ```
3. Enable HTTPS
4. Update callback URL in GitHub OAuth App settings
5. Configure proper CORS settings
6. Add rate limiting
7. Implement proper error handling
8. Add logging and monitoring
9. Remove H2 console access
10. Set up proper session management with Redis

## GitHub API Rate Limits

Be aware of GitHub API rate limits:
- **Authenticated requests**: 5,000 requests per hour
- **Unauthenticated requests**: 60 requests per hour

The OAuth2 token provides authenticated access, giving you higher rate limits.

## Troubleshooting

### Authorization Callback URL Mismatch

Ensure the callback URL in your GitHub OAuth App matches exactly:
```
http://localhost:8080/login/oauth2/code/github
```

### 401 Unauthorized Error

- Verify client ID and secret are correct
- Check that the OAuth App is not suspended
- Ensure you've copied the full client secret

### Email Not Available

If `email` is `null`:
- User's email might be private on GitHub
- Add `user:email` scope explicitly
- User needs to make at least one email public in GitHub settings

## Common Issues

### "The redirect_uri MUST match the registered callback URL"

This error means your callback URL doesn't match. Double-check:
1. The URL in your GitHub OAuth App settings
2. The URL should be: `http://localhost:8080/login/oauth2/code/github`
3. No trailing slashes
4. Correct protocol (http vs https)

### "Bad verification code"

This usually means:
- The authorization code expired (they expire quickly)
- Client ID or secret is incorrect
- Try the login flow again

## Additional Resources

- [GitHub OAuth Documentation](https://docs.github.com/en/developers/apps/building-oauth-apps)
- [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [GitHub REST API](https://docs.github.com/en/rest)

## License

MIT License - Feel free to use this code for your projects!

## Support

For issues related to:
- **GitHub OAuth**: Check [GitHub OAuth Documentation](https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps)
- **Spring Security**: Check [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- **This Project**: Open an issue on the repository