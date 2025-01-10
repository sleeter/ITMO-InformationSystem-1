import React, { useEffect, useState } from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const ImportTable = () => {
    const [imports, setImports] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // Функция для загрузки данных с бэкенда
    const fetchImports = async (page) => {
        setLoading(true);
        const jwtToken = localStorage.getItem('jwtToken'); // Получаем JWT из localStorage

        try {
            const response = await fetch(`http://localhost:8080/infosys/lab2?page=${page}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Ошибка при загрузке данных');
            }

            const data = await response.json();
            setImports(data.content); // houses возвращаются в свойстве content
            setTotalPages(data.totalPages); // totalPages возвращаются в свойстве totalPages
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        } finally {
            setLoading(false);
        }
    };

    // Загрузка данных при изменении страницы
    useEffect(() => {
        fetchImports(currentPage);
    }, [currentPage]);

    useEffect(() => {
        connectWebSocket(currentPage)
    }, [currentPage])
    const jwtToken = localStorage.getItem('jwtToken');
    function connectWebSocket(currentPage) {
        const socket = new SockJS("http://localhost:8080/ws")
        let stompClient = Stomp.over(socket)

        stompClient.connect({
            // Заголовки для подключения
            Authorization: `Bearer ${jwtToken}`,  // Передаем Bearer токен
        }, function (frame) {
            stompClient.subscribe('/topic/app', data => {
                fetchImports(currentPage)
            })
        })
    }

    // Обработчики для переключения страниц
    const handleNext = () => {
        if (currentPage < totalPages - 1) {
            setCurrentPage(currentPage + 1);
        }
    };

    const handlePrev = () => {
        if (currentPage > 0) {
            setCurrentPage(currentPage - 1);
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
                        <th>Status</th>
                        <th>User ID</th>
                        <th>Count</th>
                    </tr>
                    </thead>
                    <tbody>
                    {imports.map((imp) => (
                        <tr key={imp.id}>
                            <td>{imp.id}</td>
                            <td>{imp.status ? "accepted" : "rejected"}</td>
                            <td>{imp.user_id}</td>
                            <td>{imp.count_of_objects}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            <div>
                <button onClick={handlePrev} disabled={currentPage === 0}>
                    Backward
                </button>
                <span>
          Page {currentPage + 1} of {totalPages}
        </span>
                <button onClick={handleNext} disabled={currentPage === totalPages - 1}>
                    Forward
                </button>
            </div>
        </div>
    );
};

const modalStyles = {
    overlay: {
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: 1000,
    },
    container: {
        backgroundColor: 'white',
        padding: '20px',
        borderRadius: '5px',
        width: '300px',
        textAlign: 'center',
    },
};
export default ImportTable;
