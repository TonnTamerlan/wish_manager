# Wish Manager

A comprehensive wishlist management application with Telegram Mini App and Web App support, built with Spring Boot backend and React frontend.

## 🚀 Features

- **Multi-Platform Support**: Web App and Telegram Mini App
- **Authentication**: Telegram OAuth and Google OAuth 2.0
- **Wishlist Management**: Create, share, and manage wishlists
- **Wish Items**: Add wishes with descriptions and links
- **Booking System**: Book/unbook wishes with privacy options
- **Real-time Notifications**: Telegram bot notifications
- **Docker Support**: Complete containerized deployment

## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0 with Java 17+
- **Database**: PostgreSQL with Flyway migrations
- **Security**: JWT authentication with Spring Security
- **API**: RESTful API with comprehensive endpoints
- **Bot Integration**: Telegram Bot API integration

### Frontend
- **Web App**: React with TypeScript, Material-UI
- **Mini App**: React with Telegram Web Apps SDK
- **State Management**: React Query for server state
- **Routing**: React Router for navigation

### Infrastructure
- **Containerization**: Docker and Docker Compose
- **Reverse Proxy**: Nginx with SSL support
- **Database**: PostgreSQL with proper indexing

## 📁 Project Structure

```
wish_manager/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/wishmanager/
│   │   ├── controller/         # REST controllers
│   │   ├── service/           # Business logic
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data repositories
│   │   ├── dto/               # Data transfer objects
│   │   └── config/            # Configuration classes
│   ├── src/main/resources/
│   │   ├── db/migration/      # Flyway migrations
│   │   └── application.yml    # Application configuration
│   └── pom.xml                # Maven dependencies
├── frontend/
│   ├── web-app/               # React web application
│   │   ├── src/
│   │   │   ├── components/    # Reusable components
│   │   │   ├── pages/         # Page components
│   │   │   ├── contexts/      # React contexts
│   │   │   └── services/      # API services
│   │   └── package.json
│   └── mini-app/              # Telegram mini app
│       ├── src/
│       │   ├── components/    # Reusable components
│       │   ├── pages/         # Page components
│       │   ├── contexts/      # React contexts
│       │   └── services/      # API services
│       └── package.json
├── docker/                    # Docker configurations
│   ├── backend/Dockerfile
│   ├── frontend/web-app/Dockerfile
│   ├── frontend/mini-app/Dockerfile
│   └── nginx/nginx.conf
├── docker-compose.yml         # Multi-container setup
├── schema.sql                 # Database schema reference
├── system-design.md          # System design documentation
└── README.md                  # This file
```

## 🛠️ Prerequisites

- **Java 17+**
- **Node.js 18+**
- **Docker & Docker Compose**
- **PostgreSQL 15+** (if running locally)
- **Maven 3.6+**

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd wish_manager
```

### 2. Environment Setup

Copy the environment template and configure:

```bash
cp env.example .env
```

Edit `.env` with your configuration:

```bash
# Telegram Bot Configuration
TELEGRAM_BOT_TOKEN=your_telegram_bot_token_here
TELEGRAM_BOT_USERNAME=your_bot_username_here

# Google OAuth Configuration
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_here_minimum_32_characters
```

### 3. Docker Deployment (Recommended)

Start all services with Docker Compose:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Backend API on port 8080
- Web App on port 3000
- Mini App on port 3001
- Nginx reverse proxy on port 80

### 4. Local Development

#### Backend Development

```bash
cd backend
./mvnw spring-boot:run
```

#### Frontend Development

Web App:
```bash
cd frontend/web-app
npm install
npm start
```

Mini App:
```bash
cd frontend/mini-app
npm install
npm start
```

## 🔧 Configuration

### Telegram Bot Setup

1. Create a bot with [@BotFather](https://t.me/botfather)
2. Get your bot token
3. Set webhook URL: `https://your-domain.com/bot/webhook`
4. Configure Mini App URL in bot settings

### Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create OAuth 2.0 credentials
3. Add authorized redirect URIs:
   - `http://localhost:3000/auth/callback` (development)
   - `https://your-domain.com/auth/callback` (production)

### Database Configuration

The application uses Flyway for database migrations. Migrations are automatically applied on startup.

## 📚 API Documentation

### Authentication Endpoints

- `POST /auth/telegram` - Authenticate with Telegram initData
- `POST /auth/google` - Authenticate with Google OAuth code
- `GET /auth/me` - Get current user profile

### Wishlist Endpoints

- `POST /wishlists` - Create new wishlist
- `GET /wishlists` - Get user's wishlists
- `GET /wishlists/{id}` - Get wishlist details
- `POST /wishlists/{id}/join` - Join public wishlist
- `POST /wishlists/{id}/invite` - Invite user to wishlist
- `POST /wishlists/{id}/leave` - Leave wishlist

### Wish Endpoints

- `POST /wishes` - Create new wish
- `PATCH /wishes/{id}` - Update wish
- `DELETE /wishes/{id}` - Delete wish
- `POST /wishes/{id}/book` - Book wish item
- `POST /wishes/{id}/unbook` - Unbook wish item
- `POST /wishes/{id}/gift` - Mark wish as gifted
- `POST /wishes/{id}/ungift` - Unmark wish as gifted

### Bot Webhook

- `POST /bot/webhook` - Telegram bot webhook endpoint

## 🗄️ Database Schema

### Users Table
- `id` (UUID, Primary Key)
- `telegram_id` (VARCHAR, Unique)
- `google_sub` (VARCHAR, Unique)
- `display_name` (VARCHAR, Not Null)
- `avatar_url` (TEXT)
- `created_at` (TIMESTAMP)

### Wishlists Table
- `id` (UUID, Primary Key)
- `owner_id` (UUID, Foreign Key)
- `title` (VARCHAR, Not Null)
- `description` (TEXT)
- `is_public` (BOOLEAN, Default: false)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Memberships Table
- `id` (UUID, Primary Key)
- `user_id` (UUID, Foreign Key)
- `wishlist_id` (UUID, Foreign Key)
- `role` (VARCHAR, Enum: OWNER|EDITOR|VIEWER)
- `created_at` (TIMESTAMP)

### Wishes Table
- `id` (UUID, Primary Key)
- `wishlist_id` (UUID, Foreign Key)
- `name` (VARCHAR, Not Null)
- `description` (TEXT)
- `links` (JSONB, Array of URLs)
- `status` (VARCHAR, Enum: FREE|BOOKED|GIFTED)
- `booked_by` (UUID, Foreign Key, Nullable)
- `hide_booker_name` (BOOLEAN, Default: false)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 🔒 Security Features

- **JWT Authentication**: Stateless token-based authentication
- **CORS Configuration**: Proper cross-origin resource sharing
- **Rate Limiting**: API rate limiting with Nginx
- **Input Validation**: Comprehensive input validation
- **SQL Injection Protection**: JPA/Hibernate protection
- **XSS Protection**: Security headers and input sanitization

## 🚀 Deployment

### Production Deployment

1. **Environment Variables**: Configure all required environment variables
2. **SSL Certificates**: Set up SSL certificates for HTTPS
3. **Domain Configuration**: Configure your domain and DNS
4. **Database**: Set up production PostgreSQL instance
5. **Docker Compose**: Deploy with production configuration

### Scaling Considerations

- **Horizontal Scaling**: Stateless API design supports horizontal scaling
- **Database Optimization**: Proper indexing for performance
- **Caching**: Consider Redis for session caching
- **CDN**: Use CDN for static assets
- **Load Balancing**: Multiple backend instances behind load balancer

## 🧪 Testing

### Backend Testing

```bash
cd backend
./mvnw test
```

### Frontend Testing

```bash
cd frontend/web-app
npm test

cd frontend/mini-app
npm test
```

## 📝 Development Guidelines

### Code Style

- **Backend**: Follow Spring Boot conventions and Java best practices
- **Frontend**: Use TypeScript, follow React best practices
- **Database**: Use descriptive names and proper indexing
- **API**: RESTful design with proper HTTP status codes

### Git Workflow

1. Create feature branches from `main`
2. Use descriptive commit messages
3. Create pull requests for code review
4. Merge to `main` after approval

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the system design documentation
- Review the API documentation

## 🔄 Roadmap

- [ ] Real-time notifications with WebSocket
- [ ] Advanced search and filtering
- [ ] Wish categories and tags
- [ ] Export/import functionality
- [ ] Mobile app (React Native)
- [ ] Analytics and reporting
- [ ] Multi-language support
