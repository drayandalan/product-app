import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';


export default function RegisterPage() {
    const { register } = useAuth();
    const [u, setU] = useState('');
    const [p, setP] = useState('');
    const [err, setErr] = useState('');
    const nav = useNavigate();
    const onSubmit = async (e: React.FormEvent) => { e.preventDefault(); setErr(''); try { await register(u, p); nav('/'); } catch (e: any) { setErr(e?.response?.data?.message || 'Register failed'); } }
    return (
        <form onSubmit={onSubmit} style={{ maxWidth: 360, margin: '48px auto', display: 'grid', gap: 12 }}>
            <h2>Register</h2>
            <input value={u} onChange={e => setU(e.target.value)} placeholder="Username" required />
            <input value={p} onChange={e => setP(e.target.value)} placeholder="Password" type="password" required />
            {err && <div style={{ color: 'crimson' }}>{err}</div>}
            <button>Register</button>
        </form>
    );
}