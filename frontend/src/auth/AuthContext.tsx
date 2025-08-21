import React, { createContext, useContext, useState } from 'react';
import { api } from '../api';


type AuthCtx = { token: string | null; login: (u: string, p: string) => Promise<void>; register: (u: string, p: string) => Promise<void>; logout: () => void };
const Ctx = createContext<AuthCtx>({ token: null, async login() { }, async register() { }, logout() { } });
export const useAuth = () => useContext(Ctx);


export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const login = async (username: string, password: string) => {
        const { data } = await api.post('/auth/login', { username, password });
        localStorage.setItem('token', data.token); setToken(data.token);
    };
    const register = async (username: string, password: string) => {
        const { data } = await api.post('/auth/register', { username, password });
        localStorage.setItem('token', data.token); setToken(data.token);
    };
    const logout = () => { localStorage.removeItem('token'); setToken(null); };
    return <Ctx.Provider value={{ token, login, register, logout }}>{children}</Ctx.Provider>;
}