import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const WishlistPage: React.FC = () => {
  return (
    <Container maxWidth="lg">
      <Box mt={4}>
        <Typography variant="h4" component="h1" gutterBottom>
          My Wishlists
        </Typography>
        <Typography variant="body1" color="text.secondary">
          This page will display all your wishlists
        </Typography>
      </Box>
    </Container>
  );
};

export default WishlistPage;
