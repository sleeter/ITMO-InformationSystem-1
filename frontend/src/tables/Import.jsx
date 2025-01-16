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
    const downloadFile = async (id, filename) => {
        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/file?import=${id}&filename=${filename}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось загрузить файл');
            }

            // Преобразование ответа в Blob (двойтичные данные)
            const blob = await response.blob();

            // Создаем временную ссылку для скачивания файла
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename); // Устанавливаем имя файла

            // Добавляем ссылку на страницу и "кликаем" по ней для загрузки
            document.body.appendChild(link);
            link.click();

            // Удаляем временную ссылку и элемент
            link.parentNode.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        }
    };

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
                        <th>Filename</th>
                        <th>Status</th>
                        <th>User ID</th>
                        <th>Count</th>
                        <th>Download</th>
                    </tr>
                    </thead>
                    <tbody>
                    {imports.map((imp) => (
                        <tr key={imp.id}>
                            <td>{imp.id}</td>
                            <td>{imp.filename}</td>
                            <td>{imp.status}</td>
                            <td>{imp.user_id}</td>
                            <td>{imp.count_of_objects}</td>
                            {imp.count_of_objects !== 0 ? <td>
                                <button onClick={() => downloadFile(imp.id, imp.filename)}>Download</button>
                            </td> : ""}
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
