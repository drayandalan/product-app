import React, { useEffect, useRef, useState } from 'react';
import { api } from '../api';
import { useAuth } from '../auth/AuthContext';
import ProductForm from '../components/ProductForm';
import ProductTable from '../components/ProductTable';

export default function ProductsPage() {
    const { token } = useAuth();

    const [items, setItems] = useState<any[]>([]);
    const [editing, setEditing] = useState<any | null>(null);
    const [name, setName] = useState('');
    const [min, setMin] = useState('');
    const [max, setMax] = useState('');

    const load = async () => {
        const { data } = await api.get('/products', { params: name ? { name } : {} });
        setItems(data.content || data);
    };

    const fetchedFor = useRef<string | null>(null);
    useEffect(() => {
        if (!token) return;
        if (fetchedFor.current === token) return;
        fetchedFor.current = token;
        load();
    }, [token]);

    const save = async (v: any) => {
        if (editing) { await api.put(`/products/${editing.id}`, v); setEditing(null); }
        else { await api.post('/products', v); }
        await load();
    };

    const del = async (id: number) => { await api.delete(`/products/${id}`); await load(); };

    const filterPrice = async () => {
        const { data } = await api.get('/products/search', { params: { min, max } });
        setItems(data.content || data);
    };

    return (
        <div style={{ maxWidth: 900, margin: '24px auto', display: 'grid', gap: 16 }}>
            <h2>Products</h2>
            <div style={{ display: 'flex', gap: 8 }}>
                <input placeholder="Filter by name" value={name} onChange={e => setName(e.target.value)} />
                <button onClick={load}>Search</button>
                <span style={{ flex: 1 }} />
                <input placeholder="Min" value={min} onChange={e => setMin(e.target.value)} type="number" step="0.01" min="0" />
                <input placeholder="Max" value={max} onChange={e => setMax(e.target.value)} type="number" step="0.01" min="0" />
                <button onClick={filterPrice}>By Price</button>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
                <div>
                    <ProductTable items={items} onEdit={setEditing} onDelete={del} />
                </div>
                <div>
                    <h3>{editing ? 'Edit' : 'Add'} product</h3>
                    <ProductForm initial={editing || undefined} onSubmit={save} />
                </div>
            </div>
        </div>
    );
}
