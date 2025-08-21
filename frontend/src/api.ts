import axios from 'axios';


const apiBase = import.meta.env.VITE_API_URL || '';
export const api = axios.create({ baseURL: apiBase + '/api' });


api.interceptors.request.use((config) => {
    const t = localStorage.getItem('token');
    if (t) config.headers.Authorization = `Bearer ${t}`;
    return config;
});