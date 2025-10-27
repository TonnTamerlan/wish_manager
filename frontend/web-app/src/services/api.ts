import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authApi = {
  loginTelegram: (initData: string) => api.post('/auth/telegram', { initData }),
  loginGoogle: (code: string, verifier: string) => api.post('/auth/google', { code, verifier }),
  getMe: () => api.get('/auth/me'),
};

export const wishlistApi = {
  create: (data: any) => api.post('/wishlists', data),
  getAll: (params?: any) => api.get('/wishlists', { params }),
  getById: (id: string) => api.get(`/wishlists/${id}`),
  join: (id: string) => api.post(`/wishlists/${id}/join`),
  invite: (id: string, data: any) => api.post(`/wishlists/${id}/invite`, data),
  leave: (id: string) => api.post(`/wishlists/${id}/leave`),
};

export const wishApi = {
  create: (data: any) => api.post('/wishes', data),
  update: (id: string, data: any) => api.patch(`/wishes/${id}`, data),
  delete: (id: string) => api.delete(`/wishes/${id}`),
  book: (id: string, hideBookerName?: boolean) => api.post(`/wishes/${id}/book`, { hideBookerName }),
  unbook: (id: string) => api.post(`/wishes/${id}/unbook`),
  gift: (id: string) => api.post(`/wishes/${id}/gift`),
  ungift: (id: string) => api.post(`/wishes/${id}/ungift`),
};

export default api;
