import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    uniqueCode: string;
}

const AdminDashboard: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [firstName, setFirstName] = useState<string>('');
    const [lastName, setLastName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [role, setRole] = useState<string>('teacher');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/admin/users', {
                headers: { Authorization: `Bearer ${localStorage.getItem('accessToken')}` },
            });
            setUsers(response.data);
        } catch (error: any) {
            console.error('Failed to fetch users', error);
        }
    };

    const handleAddUser = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        try {
            const response = await axios.post(
                'http://localhost:8081/api/admin/add-user',
                { firstName, lastName, email, role },
                { headers: { Authorization: `Bearer ${localStorage.getItem('accessToken')}` } }
            );
            setSuccess(`User added successfully! Unique Code: ${response.data.uniqueCode}`);
            fetchUsers(); // Refresh the user list
        } catch (error: any) {
            setError(error.response?.data || 'Failed to add user');
        }
    };

    return (
        <div>
            <h2>Super Admin Dashboard</h2>
            <form onSubmit={handleAddUser}>
                <div>
                    <label>First Name:</label>
                    <input
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Role:</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)} required>
                        <option value="teacher">Teacher</option>
                        <option value="student">Student</option>
                    </select>
                </div>
                <button type="submit">Add User</button>
            </form>

            {success && <p style={{ color: 'green' }}>{success}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <h3>List of Users</h3>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Unique Code</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{`${user.firstName} ${user.lastName}`}</td>
                            <td>{user.email}</td>
                            <td>{user.role}</td>
                            <td>{user.uniqueCode}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminDashboard;
