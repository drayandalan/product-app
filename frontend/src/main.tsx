import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from './auth/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProductsPage from './pages/ProductsPage';

function Guard({ children }: { children: JSX.Element }) {
    const { token } = useAuth();
    return token ? children : <Navigate to="/login" />;
}

function Nav() {
    const { token, logout } = useAuth();
    return (
        <nav style={{ display: 'flex', gap: 12, padding: 12, borderBottom: '1px solid #ddd' }}>
            <Link to="/">Products</Link>
            <span style={{ flex: 1 }} />
            {token ? <button onClick={logout}>Logout</button> : <Link to="/login">Login</Link>}
        </nav>
    );
}

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AuthProvider>
            <BrowserRouter>
                <Nav />
                <Routes>
                    <Route path="/" element={<Guard><ProductsPage /></Guard>} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    </React.StrictMode>
);

export { };
