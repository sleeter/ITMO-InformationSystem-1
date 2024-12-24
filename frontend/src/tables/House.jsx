import React, { useEffect, useState } from 'react';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

const HouseTable = () => {
    const [house, setHouse] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);
    const [editingHouse, setEditingHouse] = useState(null); // Для хранения данных редактируемого автомобиля
    const [error, setError] = useState('');
    // Функция для загрузки данных с бэкенда
    const fetchHouses = async (page) => {
        setLoading(true);
        const jwtToken = localStorage.getItem('jwtToken'); // Получаем JWT из localStorage

        try {
            const response = await fetch(`http://localhost:8080/api/house?page=${page}`, {
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
            setHouse(data.content); // houses возвращаются в свойстве content
            setTotalPages(data.totalPages); // totalPages возвращаются в свойстве totalPages
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        } finally {
            setLoading(false);
        }
    };

    // Загрузка данных при изменении страницы
    useEffect(() => {
        fetchHouses(currentPage);
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
                fetchHouses(currentPage)
            })
        })
    }

    // Функция для получения данных дома для редактирования
    const fetchHouseDetails = async (id) => {
        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/house/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось загрузить информацию о доме');
            }

            const house = await response.json();
            setEditingHouse(house);
        } catch (error) {
            console.error('Ошибка при загрузке данных дома:', error);
        }
    };

    // Функция для отправки изменений на сервер
    const handleEditSubmit = async () => {
        if (editingHouse === null || editingHouse.name === null) {
            setError('Поле "name" обязательно');
            return;
        }
        if (editingHouse === null || editingHouse.number_of_lifts === null) {
            setError('Поле "number_of_lifts" обязательно');
            return;
        }
        if (editingHouse === null || editingHouse.year === null) {
            setError('Поле "year" обязательно');
            return;
        }
        setError('');
        const data = {
            name: editingHouse.name,
            number_of_lifts: editingHouse.number_of_lifts,
            year: editingHouse.year
        };

        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/house/${editingHouse.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwtToken}`,
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Ошибка при отправке данных на сервер');
            }

            setEditingHouse(null); // Закрыть форму редактирования
            fetchHouses(currentPage); // Обновить список автомобилей
        } catch (error) {
            alert(error.message);
        }
    };

    // Функция для удаления house
    const handleDelete = async (id) => {
        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/house/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Ошибка при удалении автомобиля');
            }

            fetchHouses(currentPage); // Обновить список автомобилей после удаления
        } catch (error) {
            alert(error.message);
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
                        <th>Name</th>
                        <th>Number of lifts</th>
                        <th>Year</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    </thead>
                    <tbody>
                    {house.map((house) => (
                        <tr key={house.id}>
                            <td>{house.id}</td>
                            <td>{house.name}</td>
                            <td>{house.number_of_lifts}</td>
                            <td>{house.year}</td>
                            {house.is_mine ? <td>
                                <button onClick={() => fetchHouseDetails(house.id)}>Edit</button>
                            </td>: ""}
                            {house.is_mine ? <td>
                                <button onClick={() => handleDelete(house.id)}>Delete</button>
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

            {/* Модальное окно для редактирования */}
            {editingHouse && (
                <div style={modalStyles.overlay}>
                    <div style={modalStyles.container}>
                        <h3>Edit House</h3>
                        <form onSubmit={(e) => e.preventDefault()}>
                            <label>
                                Name:
                                <input
                                    type="text"
                                    value={editingHouse.name}
                                    onChange={(e) => setEditingHouse({...editingHouse, name: e.target.value})}
                                />
                            </label>
                            <label>
                                Number of lifts:
                                <input
                                    type="number"
                                    value={editingHouse.number_of_lifts}
                                    onChange={(e) => setEditingHouse({
                                        ...editingHouse,
                                        number_of_lifts: e.target.value
                                    })}
                                />
                            </label>
                            <label>
                                Year:
                                <input
                                    type="number"
                                    value={editingHouse.year}
                                    onChange={(e) => setEditingHouse({
                                        ...editingHouse,
                                        year: e.target.value
                                    })}
                                />
                            </label>
                            <br/>
                            {error && <p style={{color: 'red'}}>{error}</p>}
                            <button type="button" onClick={handleEditSubmit}>
                                Save changes
                            </button>
                            <button type="button" onClick={() => setEditingHouse(null)}>
                                Cancel
                            </button>
                        </form>
                    </div>
                </div>
            )}
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
export default HouseTable;
