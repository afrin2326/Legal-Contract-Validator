import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 60000,
});

export const validateContract = (formData) =>
  api.post('/contracts/validate', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });

export const healthCheck = () => api.get('/contracts/health');

export default api;