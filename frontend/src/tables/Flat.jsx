import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import FlatMap from "../dots/Flat.jsx";

const Furnish = { DESIGNER: 'DESIGNER', FINE: 'FINE', BAD: 'BAD', LITTLE: 'LITTLE' };
const Transport = { FEW: 'FEW', LITTLE: 'LITTLE', NORMAL: 'NORMAL', ENOUGH: 'ENOUGH' };
const View = { STREET: 'STREET', YARD: 'YARD', BAD: 'BAD', NORMAL: 'NORMAL' };
const Sort = {ID: "id", NAME: "name", AREA: "area", PRICE: "price", TIMETOMETROONFOOT: "time_to_metro_on_foot", NUMBEROFROOMS: "number_of_rooms"}
const Filter = {NAME: "name", AREA: "area", PRICE: "price", TIMETOMETROONFOOT: "time_to_metro_on_foot", NUMBEROFROOMS: "number_of_rooms"}
const SortOrder = {ASC: "asc", DESC: "desc"}
const FlatTable = () => {
    const [flat, setFlat] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [sortBy, setSortBy] = useState(Sort.ID);
    const [sortOrder, setSortOrder] = useState(SortOrder.ASC)
    const [filterName, setFilterName] = useState("filter");
    const [filterValue, setFilterValue] = useState(0);
    const [loading, setLoading] = useState(false);
    const [editingFlat, setEditingFlat] = useState(null); // Для хранения данных редактируемого автомобиля
    const [error, setError] = useState('');


    useEffect(() => {
        connectWebSocket(currentPage, sortBy, sortOrder, filterName, filterValue)
    }, [currentPage, sortBy, sortOrder, filterName, filterValue])
    const jwtToken = localStorage.getItem('jwtToken');
    function connectWebSocket(currentPage, sortBy, sortOrder, filterName, filterValue) {
        const socket = new SockJS("http://localhost:8080/ws")
        let stompClient = Stomp.over(socket)

        stompClient.connect({
            // Заголовки для подключения
            Authorization: `Bearer ${jwtToken}`,  // Передаем Bearer токен
        }, function () {
            stompClient.subscribe('/topic/app', data => {
                fetchFlats(currentPage, sortBy, sortOrder, filterName, filterValue)
            })
        })
    }



    // Функция для загрузки данных с бэкенда
    const fetchFlats = async (page, sortBy, sortOrder, filterName, filterValue) => {
        setLoading(true);
        const jwtToken = localStorage.getItem('jwtToken'); // Получаем JWT из localStorage

        try {
            const response = await fetch(`http://localhost:8080/api/flat?page=${page}&sort_by=${sortBy}&sort_order=${sortOrder}&${filterName}=${filterValue}`, {
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
            // console.log(data)
            setFlat(data.content); // flats возвращаются в свойстве content
            setTotalPages(data.totalPages); // totalPages возвращаются в свойстве totalPages
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        } finally {
            setLoading(false);
        }
    };

    // Загрузка данных при изменении страницы
    useEffect(() => {
        fetchFlats(currentPage, sortBy, sortOrder, filterName, filterValue);
    }, [currentPage, sortBy, sortOrder, filterName, filterValue]);

    // Функция для получения данных дома для редактирования
    const fetchFlatDetails = async (id) => {
        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/flat/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось загрузить информацию о доме');
            }

            const flat = await response.json();
            setEditingFlat(flat);
        } catch (error) {
            console.error('Ошибка при загрузке данных дома:', error);
        }
    };

    // Функция для отправки изменений на сервер
    const handleEditSubmit = async () => {
        if (editingFlat === null || editingFlat.name === null) {
            setError('Поле "name" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.x === null) {
            setError('Поле "x" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.y === null) {
            setError('Поле "y" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.area === null) {
            setError('Поле "area" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.price === null) {
            setError('Поле "price" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.balcony === null) {
            setError('Поле "balcony" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.time_to_mero_of_foot === null) {
            setError('Поле "time_to_mero_of_foot" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.furnish === null) {
            setError('Поле "furnish" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.view === null) {
            setError('Поле "view" обязательно');
            return;
        }
        if (editingFlat === null || editingFlat.furnish === null) {
            setError('Поле "transport" обязательно');
            return;
        }
        setError('');
        const data = {
            name: editingFlat.name,
            coordinates: {
                x: editingFlat.x,
                y: editingFlat.y,
            },
            area: editingFlat.area,
            price: editingFlat.price,
            balcony: editingFlat.balcony,
            time_to_metro_on_foot: editingFlat.time_to_metro_on_foot,
            number_of_rooms: editingFlat.number_of_rooms,
            furnish: editingFlat.furnish,
            view: editingFlat.view,
            transport: editingFlat.transport,
        };

        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/flat/${editingFlat.id}`, {
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

            setEditingFlat(null); // Закрыть форму редактирования
            fetchFlats(currentPage, sortBy, sortOrder, filterName, filterValue); // Обновить список
        } catch (error) {
            alert(error.message);
        }
    };

    // Функция для удаления flat
    const handleDelete = async (id) => {
        const jwtToken = localStorage.getItem('jwtToken');
        try {
            const response = await fetch(`http://localhost:8080/api/flat/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Ошибка при удалении автомобиля');
            }

            fetchFlats(currentPage, sortBy, sortOrder, filterName, filterValue); // Обновить список автомобилей после удаления
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
                <div>
                    <div>
                        <label>
                            Sort by:
                            <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                                <option value={Sort.ID}>ID</option>
                                <option value={Sort.NAME}>Name</option>
                                <option value={Sort.AREA}>Area</option>
                                <option value={Sort.PRICE}>Price</option>
                                <option value={Sort.TIMETOMETROONFOOT}>Time to Metro</option>
                                <option value={Sort.NUMBEROFROOMS}>Number of Rooms</option>
                            </select>
                        </label>
                        <label>
                            Sort order:
                            <select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
                                <option value={SortOrder.ASC}>Asc</option>
                                <option value={SortOrder.DESC}>Desc</option>
                            </select>
                        </label>
                        <label>
                            Filter by:
                            <select value={filterName} onChange={(e) => setFilterName(e.target.value)}>
                                <option value={Filter.NAME}>Name</option>
                                <option value={Filter.AREA}>Area</option>
                                <option value={Filter.PRICE}>Price</option>
                                <option value={Filter.TIMETOMETROONFOOT}>Time to Metro</option>
                                <option value={Filter.NUMBEROFROOMS}>Number of Rooms</option>
                            </select>
                        </label>
                        <label>
                            Filter value:
                            <input
                                type="text"
                                value={filterValue}
                                onChange={(e) => setFilterValue(e.target.value)}
                            />
                        </label>
                    </div>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>House ID</th>
                            <th>Name</th>
                            <th>X</th>
                            <th>Y</th>
                            <th>Area</th>
                            <th>Price</th>
                            <th>Balcony</th>
                            <th>Time to metro on foot</th>
                            <th>Number of rooms</th>
                            <th>Furnish</th>
                            <th>View</th>
                            <th>Transport</th>
                            <th>Edit</th>
                            <th>Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        {flat.map((flat) => (
                            <tr key={flat.id}>
                                <td>{flat.id}</td>
                                <td>{flat.house.id}</td>
                                <td>{flat.name}</td>
                                <td>{flat.coordinates.x}</td>
                                <td>{flat.coordinates.y}</td>
                                <td>{flat.area}</td>
                                <td>{flat.price}</td>
                                <td>{flat.balcony ? "Yes" : "No"}</td>
                                <td>{flat.time_to_metro_on_foot}</td>
                                <td>{flat.number_of_rooms}</td>
                                <td>{flat.furnish}</td>
                                <td>{flat.view}</td>
                                <td>{flat.transport}</td>
                                {flat.is_mine ? <td>
                                    <button onClick={() => fetchFlatDetails(flat.id)}>Edit</button>
                                </td> : ""}
                                {flat.is_mine ? <td>
                                    <button onClick={() => handleDelete(flat.id)}>Delete</button>
                                </td> : ""}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
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
            {console.log(flat)}
            <FlatMap flats={flat}/>

            {/* Модальное окно для редактирования */}
            {editingFlat && (
                <div style={modalStyles.overlay}>
                    <div style={modalStyles.container}>
                        <h3>Edit Flat</h3>
                        <form onSubmit={(e) => e.preventDefault()}>
                            <label>
                                Name:
                                <input
                                    type="text"
                                    value={editingFlat.name}
                                    onChange={(e) => setEditingFlat({...editingFlat, name: e.target.value})}
                                />
                            </label>
                            <label>
                                House ID:
                                <input
                                    type="number"
                                    value={editingFlat.house.id}
                                    onChange={(e) => setEditingFlat({...editingFlat, house_id: e.target.value})}
                                />
                            </label>
                            <label>
                                X:
                                <input
                                    type="number"
                                    value={editingFlat.coordinates.x}
                                    onChange={(e) => setEditingFlat({...editingFlat, x: e.target.value})}
                                />
                            </label>
                            <label>
                                Y:
                                <input
                                    type="number"
                                    value={editingFlat.coordinates.y}
                                    onChange={(e) => setEditingFlat({...editingFlat, y: e.target.value})}
                                />
                            </label>
                            <label>
                                Area:
                                <input
                                    type="number"
                                    value={editingFlat.area}
                                    onChange={(e) => setEditingFlat({...editingFlat, area: e.target.value})}
                                />
                            </label>
                            <label>
                                Price:
                                <input
                                    type="number"
                                    value={editingFlat.price}
                                    onChange={(e) => setEditingFlat({...editingFlat, price: e.target.value})}
                                />
                            </label>
                            <label>
                                Balcony:
                                <input
                                    type="radio"
                                    name="balcony"
                                    value="true"
                                    checked={editingFlat.balcony === true}
                                    onChange={() => setEditingFlat({...editingFlat, balcony: true})}
                                /> Yes
                                <input
                                    type="radio"
                                    name="balcony"
                                    value="false"
                                    checked={editingFlat.balcony === false}
                                    onChange={() => setEditingFlat({...editingFlat, balcony: false})}
                                /> No
                            </label>
                            <label>
                                Time to metro on foot:
                                <input
                                    type="number"
                                    value={editingFlat.time_to_metro_on_foot}
                                    onChange={(e) => setEditingFlat({
                                        ...editingFlat,
                                        time_to_metro_on_foot: e.target.value
                                    })}
                                />
                            </label>
                            <label>
                                Number of rooms:
                                <input
                                    type="number"
                                    value={editingFlat.number_of_rooms}
                                    onChange={(e) => setEditingFlat({...editingFlat, number_of_rooms: e.target.value})}
                                />
                            </label>
                            <label>
                                Furnish:
                                <select value={editingFlat.furnish}
                                        onChange={(e) => setEditingFlat({...editingHuman, furnish: e.target.value})}>
                                    <option value={Furnish.BAD}>BAD</option>
                                    <option value={Furnish.FINE}>FINE</option>
                                    <option value={Furnish.DESIGNER}>DESIGNER</option>
                                    <option value={Furnish.LITTLE}>LITTLE</option>
                                </select>
                            </label>
                            <label>
                                View:
                                <select value={editingFlat.view}
                                        onChange={(e) => setEditingFlat({...editingHuman, view: e.target.value})}>
                                    <option value={View.STREET}>STREET</option>
                                    <option value={View.YARD}>YARD</option>
                                    <option value={View.BAD}>BAD</option>
                                    <option value={View.NORMAL}>NORMAL</option>
                                </select>
                            </label>
                            <label>
                                Transport:
                                <select value={editingFlat.transport}
                                        onChange={(e) => setEditingFlat({...editingHuman, transport: e.target.value})}>
                                    <option value={Transport.FEW}>FEW</option>
                                    <option value={Transport.LITTLE}>LITTLE</option>
                                    <option value={Transport.NORMAL}>NORMAL</option>
                                    <option value={Transport.ENOUGH}>ENOUGH</option>
                                </select>
                            </label>
                            <br/>
                            {error && <p style={{color: 'red'}}>{error}</p>}
                            <button type="button" onClick={handleEditSubmit}>
                                Save changes
                            </button>
                            <button type="button" onClick={() => setEditingFlat(null)}>
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
        borderRadius: '10px',
        width: '500px',  // ширина контейнера
        maxHeight: '80vh', // максимальная высота
        overflowY: 'auto', // прокрутка, если форма слишком длинная
        textAlign: 'left',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)', // тень для красоты
        display: 'grid',
        gridTemplateColumns: '1fr 1fr', // два столбца
        gap: '15px', // расстояние между элементами
    },
    header: {
        gridColumn: 'span 2', // заголовок на всю ширину
        textAlign: 'center',
        color: '#333',
        marginBottom: '20px',
        fontSize: '18px',
        fontWeight: 'bold',
    },
    label: {
        display: 'block',
        marginBottom: '8px',
        fontWeight: 'bold',
        color: '#555',
    },
    input: {
        width: '100%',
        padding: '8px',
        marginBottom: '12px',
        border: '1px solid #ddd',
        borderRadius: '5px',
        fontSize: '14px',
        boxSizing: 'border-box',
    },
    select: {
        width: '100%',
        padding: '8px',
        marginBottom: '12px',
        border: '1px solid #ddd',
        borderRadius: '5px',
        fontSize: '14px',
        boxSizing: 'border-box',
    },
    radioLabel: {
        marginRight: '10px',
    },
    button: {
        backgroundColor: '#007bff',
        color: 'white',
        padding: '8px 15px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '16px',
        marginRight: '10px',
        transition: 'background-color 0.3s',
    },
    buttonHover: {
        backgroundColor: '#0056b3',
    },
    error: {
        color: '#ff0000',
        fontSize: '14px',
        marginTop: '-10px',
        marginBottom: '15px',
    },
    closeButton: {
        backgroundColor: '#dc3545',
        color: 'white',
    },
    closeButtonHover: {
        backgroundColor: '#c82333',
    },
};


export default FlatTable;
