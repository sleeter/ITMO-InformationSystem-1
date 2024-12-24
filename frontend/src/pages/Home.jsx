import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import HouseTable from "../tables/House.jsx";
import FlatTable from "../tables/Flat.jsx";
import HouseForm from "../inputs/House.jsx";
import FlatForm from "../inputs/Flat.jsx";
import AdminForm from "../inputs/Admin.jsx";
import FlatOperations from "../operations/Flat.jsx";

function Home() {
    const location = useLocation();
    const navigate = useNavigate();
    var username = location.state?.username || 'Guest';
    const token = localStorage.getItem('jwtToken');

    // Проверяем роль из JWT токена
    const getRoleFromToken = () => {
        if (!token) return null;
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Расшифровываем токен
        return decodedToken?.role || null;  // Возвращаем роль
    };
    const getUserName = () => {
        if (!token) return null;
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Расшифровываем токен
        return decodedToken?.sub || null;  // Возвращаем роль
    };

    const name = getUserName()
    if (name) {
        username = name
    }

    const role = getRoleFromToken();

    // Логика выхода
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        navigate('/auth/login');
    };

    // Логика становления админом
    const handleBecomeAdmin = async () => {
        try {
            await fetch('http://localhost:8080/api/admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            });
        } catch (error) {
            alert('Ошибка при отправке запроса.');
        }
    };

    // Логика перехода на панель админа
    const handleGoToAdminPanel = () => {
        navigate('/home/admin');  // Переход на панель админа в том же окне
    };

    return (
        <div>
            {/* Хедер */}
            <header>
                <div>
                    <span>Welcome, {username}!</span>
                </div>
                <div className="header-buttons">
                    {role === "ROLE_ADMIN" ? <AdminForm/> : null}
                    {role === "ROLE_ADMIN" ? <button onClick={handleGoToAdminPanel}>Go to admin panel</button> : null}
                    <button onClick={handleLogout} className="header-buttons-logout">Logout</button>

                </div>
            </header>

            <h2>Houses</h2>
            <HouseForm/>
            <HouseTable/>


            <h2>Flats</h2>
            <FlatForm/>
            <FlatTable/>

            <FlatOperations/>

        </div>

    );
}

export default Home;
