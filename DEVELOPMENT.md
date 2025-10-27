# Development Guide

This guide provides detailed instructions for setting up and developing the Wish Manager application.

## 🛠️ Development Environment Setup

### Prerequisites

Before starting development, ensure you have the following installed:

- **Java 17+** - [Download OpenJDK](https://adoptium.net/)
- **Node.js 18+** - [Download Node.js](https://nodejs.org/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 15+** - [Download PostgreSQL](https://www.postgresql.org/download/)
- **Docker & Docker Compose** - [Download Docker](https://www.docker.com/get-started)

### IDE Setup

Recommended IDEs:
- **IntelliJ IDEA** (for backend development)
- **Visual Studio Code** (for frontend development)
- **Eclipse** (alternative for backend)

### Backend Development

#### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE wish_manager;
CREATE USER wish_manager WITH PASSWORD 'wish_manager_password';
GRANT ALL PRIVILEGES ON DATABASE wish_manager TO wish_manager;
```

#### 2. Environment Configuration

Create `backend/src/main/resources/application-dev.yml`:

```yaml
spring:
  profiles:
    active: dev
  
  datasource:
    url: jdbc:postgresql://localhost:5432/wish_manager
    username: wish_manager
    password: wish_manager_password
  
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: validate
  
logging:
  level:
    com.wishmanager: DEBUG
    org.springframework.security: DEBUG
```

#### 3. Run the Application

```bash
cd backend
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080`

### Frontend Development

#### Web App Setup

```bash
cd frontend/web-app
npm install
npm start
```

The web app will be available at `http://localhost:3000`

#### Mini App Setup

```bash
cd frontend/mini-app
npm install
npm start
```

The mini app will be available at `http://localhost:3001`

## 🔧 Configuration

### Telegram Bot Development

1. **Create Bot**: Message [@BotFather](https://t.me/botfather) to create a new bot
2. **Get Token**: Save the bot token for configuration
3. **Set Commands**: Configure bot commands for better UX
4. **Webhook**: Set webhook URL for production

### Google OAuth Development

1. **Google Cloud Console**: Go to [Google Cloud Console](https://console.cloud.google.com/)
2. **Create Project**: Create a new project or select existing
3. **Enable APIs**: Enable Google+ API and Google OAuth2 API
4. **Create Credentials**: Create OAuth 2.0 client ID
5. **Configure URIs**: Add authorized redirect URIs

### Environment Variables

Create `.env` file in the root directory:

```bash
# Telegram Bot
TELEGRAM_BOT_TOKEN=your_bot_token_here
TELEGRAM_BOT_USERNAME=your_bot_username

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# JWT
JWT_SECRET=your_jwt_secret_minimum_32_characters

# Database
POSTGRES_DB=wish_manager
POSTGRES_USER=wish_manager
POSTGRES_PASSWORD=wish_manager_password
```

## 🧪 Testing

### Backend Testing

Run unit tests:
```bash
cd backend
./mvnw test
```

Run integration tests:
```bash
./mvnw test -Dtest=*IntegrationTest
```

### Frontend Testing

Run tests for web app:
```bash
cd frontend/web-app
npm test
```

Run tests for mini app:
```bash
cd frontend/mini-app
npm test
```

### End-to-End Testing

Use Docker Compose for full integration testing:

```bash
docker-compose up -d
# Run your E2E tests against the running services
docker-compose down
```

## 🐛 Debugging

### Backend Debugging

1. **IDE Debugging**: Run the Spring Boot application in debug mode
2. **Logging**: Use appropriate log levels for debugging
3. **Database**: Use database tools to inspect data
4. **API Testing**: Use Postman or curl for API testing

### Frontend Debugging

1. **Browser DevTools**: Use browser developer tools
2. **React DevTools**: Install React Developer Tools extension
3. **Network Tab**: Monitor API calls and responses
4. **Console**: Check for JavaScript errors

### Common Issues

#### Database Connection Issues
- Check PostgreSQL service is running
- Verify connection parameters
- Check firewall settings

#### CORS Issues
- Verify CORS configuration in backend
- Check allowed origins in frontend
- Ensure proper headers

#### Authentication Issues
- Verify JWT secret configuration
- Check token expiration
- Validate OAuth credentials

## 📦 Building and Deployment

### Backend Build

```bash
cd backend
./mvnw clean package
```

### Frontend Build

Web App:
```bash
cd frontend/web-app
npm run build
```

Mini App:
```bash
cd frontend/mini-app
npm run build
```

### Docker Build

Build all services:
```bash
docker-compose build
```

Build specific service:
```bash
docker-compose build backend
```

### Production Deployment

1. **Environment**: Set production environment variables
2. **SSL**: Configure SSL certificates
3. **Domain**: Set up domain and DNS
4. **Database**: Use production database
5. **Monitoring**: Set up logging and monitoring

## 🔄 Development Workflow

### Git Workflow

1. **Create Branch**: `git checkout -b feature/your-feature-name`
2. **Make Changes**: Implement your feature
3. **Commit**: `git commit -m "Add feature description"`
4. **Push**: `git push origin feature/your-feature-name`
5. **Pull Request**: Create PR for code review
6. **Merge**: Merge after approval

### Code Review Process

1. **Self Review**: Review your own code before submitting
2. **Peer Review**: Get feedback from team members
3. **Testing**: Ensure all tests pass
4. **Documentation**: Update documentation if needed

### Release Process

1. **Version Bump**: Update version numbers
2. **Changelog**: Update changelog with new features
3. **Testing**: Run full test suite
4. **Deployment**: Deploy to staging/production
5. **Monitoring**: Monitor for issues

## 📚 Additional Resources

### Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://reactjs.org/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)

### Tools

- [Postman](https://www.postman.com/) - API testing
- [pgAdmin](https://www.pgadmin.org/) - PostgreSQL administration
- [Docker Desktop](https://www.docker.com/products/docker-desktop) - Container management

### Learning Resources

- [Spring Boot Guides](https://spring.io/guides)
- [React Tutorial](https://reactjs.org/tutorial/tutorial.html)
- [PostgreSQL Tutorial](https://www.postgresql.org/docs/current/tutorial.html)

## 🤝 Contributing

### Code Standards

- **Java**: Follow Google Java Style Guide
- **TypeScript**: Use ESLint and Prettier
- **SQL**: Use descriptive names and proper formatting
- **Git**: Use conventional commit messages

### Pull Request Guidelines

1. **Description**: Provide clear description of changes
2. **Testing**: Include tests for new functionality
3. **Documentation**: Update documentation if needed
4. **Screenshots**: Include screenshots for UI changes

### Issue Reporting

When reporting issues, include:
- **Environment**: OS, Java version, Node version
- **Steps**: Steps to reproduce the issue
- **Expected**: Expected behavior
- **Actual**: Actual behavior
- **Logs**: Relevant error logs

## 🆘 Getting Help

- **Documentation**: Check this guide and README
- **Issues**: Search existing issues or create new ones
- **Discussions**: Use GitHub discussions for questions
- **Community**: Join our community channels
