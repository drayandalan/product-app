import React from 'react';


export default function ProductTable({ items, onEdit, onDelete }: { items: any[], onEdit: (p: any) => void, onDelete: (id: number) => void }) {
    return (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
                <tr>
                    <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd' }}>Name</th>
                    <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd' }}>Description</th>
                    <th style={{ textAlign: 'right', borderBottom: '1px solid #ddd' }}>Price</th>
                    <th style={{ borderBottom: '1px solid #ddd' }}>Actions</th>
                </tr>
            </thead>
            <tbody>
                {items.map(p => (
                    <tr key={p.id}>
                        <td>{p.name}</td>
                        <td>{p.description}</td>
                        <td style={{ textAlign: 'right' }}>{p.price}</td>
                        <td style={{ textAlign: 'center' }}>
                            <button onClick={() => onEdit(p)}>Edit</button>{' '}
                            <button onClick={() => onDelete(p.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}