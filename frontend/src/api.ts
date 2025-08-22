import axios from 'axios';


const apiBase = import.meta.env.VITE_API_URL || '';
export const api = axios.create({ baseURL: apiBase + '/api' });


api.interceptors.request.use((config) => {
    const t = localStorage.getItem('token');
    if (t) config.headers.Authorization = `Bearer ${t}`;
    return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response && err.response.status === 403) {
      alert("Unauthorized access.");
    } else {
      alert(err.message || "An unexpected error occurred");
    }
    return Promise.reject(err);
  }
);