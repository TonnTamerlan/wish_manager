import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const CreateWishlistPage: React.FC = () => {
  return (
    <Container maxWidth="lg">
      <Box mt={4}>
        <Typography variant="h4" component="h1" gutterBottom>
          Create New Wishlist
        </Typography>
        <Typography variant="body1" color="text.secondary">
          This page will contain the form to create a new wishlist
        </Typography>
      </Box>
    </Container>
  );
};

export default CreateWishlistPage;
