import React from 'react';
import { Button, Container, Typography, Box } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import { authApi } from '../services/api';

const LoginPage: React.FC = () => {
  const { login } = useAuth();

  const handleGoogleLogin = async () => {
    try {
      // This would typically involve OAuth flow with Google
      // For now, we'll simulate the process
      console.log('Google login initiated');
    } catch (error) {
      console.error('Google login failed:', error);
    }
  };

  const handleTelegramLogin = async () => {
    try {
      // This would typically get initData from Telegram Web Apps SDK
      // For now, we'll simulate the process
      const mockInitData = 'mock_telegram_init_data';
      const response = await authApi.loginTelegram(mockInitData);
      const { token, userId, displayName, avatarUrl } = response.data;
      
      login(token, {
        id: userId,
        displayName,
        avatarUrl,
      });
    } catch (error) {
      console.error('Telegram login failed:', error);
    }
  };

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
        
        <Typography variant="body1" color="text.secondary" textAlign="center" mb={3}>
          Sign in to manage your wishlists and share them with friends
        </Typography>

        <Box display="flex" flexDirection="column" gap={2} width="100%" maxWidth={300}>
          <Button
            variant="contained"
            size="large"
            onClick={handleGoogleLogin}
            fullWidth
          >
            Sign in with Google
          </Button>
          
          <Button
            variant="outlined"
            size="large"
            onClick={handleTelegramLogin}
            fullWidth
          >
            Sign in with Telegram
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default LoginPage;
