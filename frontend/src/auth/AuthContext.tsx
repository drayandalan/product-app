import React, { createContext, useContext, useMemo, useState } from 'react';
import { api } from '../api';

type AuthCtx = {
    token: string | null;
    login: (u: string, p: string) => Promise<void>;
    register: (u: string, p: string) => Promise<void>;
    logout: () => void;
};

const Ctx = createContext<AuthCtx | undefined>(undefined);

export function useAuth(): AuthCtx {
    const ctx = useContext(Ctx);
    if (!ctx) throw new Error('useAuth must be used within <AuthProvider>');
    return ctx;
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

    const login = async (username: string, password: string) => {
        const { data } = await api.post('/auth/login', { username, password });
        localStorage.setItem('token', data.token);
        setToken(data.token);
    };

    const register = async (username: string, password: string) => {
        const { data } = await api.post('/auth/register', { username, password });
        localStorage.setItem('token', data.token);
        setToken(data.token);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
    };

    const value = useMemo<AuthCtx>(() => ({ token, login, register, logout }), [token]);

    return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}
