# Use Node.js 18 as base image
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY frontend/web-app/package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY frontend/web-app/src ./src
COPY frontend/web-app/public ./public

# Build the application
RUN npm run build

# Use nginx to serve the built app
FROM nginx:alpine

# Copy built app to nginx
COPY --from=0 /app/build /usr/share/nginx/html

# Copy nginx configuration
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
