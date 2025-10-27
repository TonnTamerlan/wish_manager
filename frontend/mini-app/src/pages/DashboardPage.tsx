import React from 'react';
import { Container, Typography, Box, Button, AppBar, Toolbar } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const DashboardPage: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Wish Manager
          </Typography>
          <Button color="inherit" onClick={logout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>
      
      <Container maxWidth="lg" sx={{ mt: 4 }}>
        <Box mb={4}>
          <Typography variant="h4" component="h1" gutterBottom>
            Welcome, {user?.displayName}!
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Manage your wishlists and share them with friends
          </Typography>
        </Box>

        <Box display="flex" gap={2} flexWrap="wrap">
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/wishlists/create')}
          >
            Create New Wishlist
          </Button>
          
          <Button
            variant="outlined"
            size="large"
            onClick={() => navigate('/wishlists')}
          >
            View All Wishlists
          </Button>
        </Box>
      </Container>
    </>
  );
};

export default DashboardPage;
