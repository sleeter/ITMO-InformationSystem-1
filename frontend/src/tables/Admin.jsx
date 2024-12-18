import React, { useEffect, useState } from 'react';

const AdminRequestTable = () => {
    const [adminReq, setAdminReq] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    // Функция для загрузки данных с бэкенда
    const fetchAdminReq = async () => {
        setLoading(true);
        const jwtToken = localStorage.getItem('jwtToken'); // Получаем JWT из localStorage

        try {
            const response = await fetch(`http://localhost:8080/api/admin/request`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Ошибка при загрузке данных');
            }
            const data = await response.json()
            setAdminReq(data);
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        } finally {
            setLoading(false);
        }
    };

    // Загрузка данных при изменении страницы
    useEffect(() => {
        fetchAdminReq();
    }, []);


    // Функция для отправки изменений на сервер
    const handleApproveSubmit = async (id) => {
        setError('');

        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/admin/approve/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwtToken}`,
                },
            });

            if (!response.ok) {
                throw new Error('Ошибка при отправке данных на сервер');
            }

            fetchAdminReq(); // Обновить список автомобилей
        } catch (error) {
            alert(error.message);
        }
    };


    return (
        <div>
            {loading ? (
                <p>Загрузка...</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name of user</th>
                        <th>Name of admin</th>
                        <th>Approve</th>
                    </tr>
                    </thead>
                    <tbody>
                    {adminReq.map((adminReq) => (
                        <tr key={adminReq.id}>
                            <td>{adminReq.id}</td>
                            <td>{adminReq.user_login}</td>
                            <td>{adminReq.admin_login}</td>
                            <td>
                                <button onClick={() => handleApproveSubmit(adminReq.id)}>Approve</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

        </div>
    );
};


export default AdminRequestTable;
