import React, { useState, useEffect, useRef } from 'react';

export default function ProductForm({
    initial,
    onSubmit,
}: {
    initial?: any;
    onSubmit: (v: any) => void;
}) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const nameRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (initial) {
            setName(initial.name || '');
            setDescription(initial.description || '');
            setPrice(String(initial.price ?? ''));
        } else {
            setName('');
            setDescription('');
            setPrice('');
        }
    }, [initial]);

    const resetForm = () => {
        setName('');
        setDescription('');
        setPrice('');
        requestAnimationFrame(() => nameRef.current?.focus());
    };

    const submit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit({ name, description, price: Number(price) });
        resetForm();
    };

    return (
        <form onSubmit={submit} style={{ display: 'grid', gap: 8 }}>
            <input
                ref={nameRef}
                value={name}
                onChange={e => setName(e.target.value)}
                placeholder="Name"
                required
                maxLength={100}
            />
            <textarea
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder="Description"
                maxLength={500}
            />
            <input
                value={price}
                onChange={e => setPrice(e.target.value)}
                placeholder="Price"
                type="number"
                step="0.01"
                min="0.01"
                required
            />
            <button>Save</button>
        </form>
    );
}
