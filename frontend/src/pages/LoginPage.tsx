import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';


export default function LoginPage() {
    const { login } = useAuth();
    const [u, setU] = useState('demo');
    const [p, setP] = useState('demo123');
    const [err, setErr] = useState('');
    const nav = useNavigate();
    const onSubmit = async (e: React.FormEvent) => { e.preventDefault(); setErr(''); try { await login(u, p); nav('/'); } catch (e: any) { setErr(e?.response?.data?.message || 'Login failed'); } }
    return (
        <form onSubmit={onSubmit} style={{ maxWidth: 360, margin: '48px auto', display: 'grid', gap: 12 }}>
            <h2>Login</h2>
            <input value={u} onChange={e => setU(e.target.value)} placeholder="Username" required />
            <input value={p} onChange={e => setP(e.target.value)} placeholder="Password" type="password" required />
            {err && <div style={{ color: 'crimson' }}>{err}</div>}
            <button>Login</button>
            <small>No account? <Link to="/register">Register</Link></small>
        </form>
    );
}