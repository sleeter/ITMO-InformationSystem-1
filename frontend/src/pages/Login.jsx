import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const getUserName = (token) => {
        if (!token) return null;
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Расшифровываем токен
        return decodedToken?.sub || "";  // Возвращаем роль
    };

    // Проверяем наличие токена при загрузке компонента
    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            setLogin(getUserName(token))
            // Если токен существует, перенаправляем на главную страницу
            navigate('/home', { state: { login } });
        }
    }, [navigate]);

    const handleLogin = async () => {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ login, password }),
        });

        if (response.ok) {
            const data = await response.json();
            // Assuming the JWT token is in data.token
            const { token } = data;
            // Save the token to localStorage (or sessionStorage)
            localStorage.setItem('jwtToken', token);
            // console.log("token " + token)
            // Navigate to home page
            navigate('/home', { state: { login } });
        } else {
            alert('Login failed');
        }
    };

    return (
        <div>
            <h2>Login Page</h2>
            <input
                type="text"
                placeholder="Login"
                value={login}
                onChange={(e) => setLogin(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Login</button>
            <p>
                Don&#39;t have an account? <a href="/auth/register">Register</a>
            </p>
        </div>
    );
}

export default Login;
