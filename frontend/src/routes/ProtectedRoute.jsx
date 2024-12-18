import React from 'react';
import {Navigate, Outlet, useNavigate} from 'react-router-dom';

const ProtectedRoute = () => {
    const token = localStorage.getItem('jwtToken');
    const navigate = useNavigate();

    const getUserName = () => {
        if (!token) return "";
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Расшифровываем токен
        return decodedToken?.sub || "";  // Возвращаем роль
    };

    const login = getUserName()

    // Если токен отсутствует, перенаправляем на страницу логина
    if (!token) {
        return <Navigate to="/auth/login" />;
    }

    // Если токен есть, отображаем дочерние компоненты (например, страницу /home)
    return <Outlet />;
};

export default ProtectedRoute;
