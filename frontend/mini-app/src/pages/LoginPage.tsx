import React, { useEffect } from 'react';
import { Container, Typography, Box } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import { authApi } from '../services/api';

const LoginPage: React.FC = () => {
  const { login } = useAuth();

  useEffect(() => {
    // Initialize Telegram Web Apps SDK
    const initTelegramAuth = async () => {
      try {
        // Get initData from Telegram Web Apps SDK
        const initData = (window as any).Telegram?.WebApp?.initData;
        
        if (initData) {
          const response = await authApi.loginTelegram(initData);
          const { token, userId, displayName, avatarUrl } = response.data;
          
          login(token, {
            id: userId,
            displayName,
            avatarUrl,
          });
        }
      } catch (error) {
        console.error('Telegram authentication failed:', error);
      }
    };

    initTelegramAuth();
  }, [login]);

  return (
    <Container maxWidth="sm">
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="100vh"
        gap={3}
      >
        <Typography variant="h4" component="h1" gutterBottom>
          Welcome to Wish Manager
        </Typography>
        
        <Typography variant="body1" color="text.secondary" textAlign="center">
          Loading your account...
        </Typography>
      </Box>
    </Container>
  );
};

export default LoginPage;
