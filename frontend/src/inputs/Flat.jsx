import React, { useState } from 'react';

const Furnish = { DESIGNER: 'DESIGNER', FINE: 'FINE', BAD: 'BAD', LITTLE: 'LITTLE' };
const Transport = { FEW: 'FEW', LITTLE: 'LITTLE', NORMAL: 'NORMAL', ENOUGH: 'ENOUGH' };
const View = { STREET: 'STREET', YARD: 'YARD', BAD: 'BAD', NORMAL: 'NORMAL' };


const FlatForm = () => {
    const [name, setName] = useState("");
    const [x, setX] = useState("");
    const [y, setY] = useState("");
    const [house_id, setHouseId] = useState("");
    const [area, setArea] = useState("");
    const [price, setPrice] = useState("");
    const [balcony, setBalcony] = useState(false);
    const [time_to_metro_on_foot, setTimeToMetroOnFoot] = useState("");
    const [number_of_rooms, setNumberOfRooms] = useState("");
    const [furnish, setFurnish] = useState(Furnish.BAD);
    const [view, setView] = useState(View.STREET);
    const [transport, setTransport] = useState(Transport.FEW);
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false); // Track modal visibility
    const token = localStorage.getItem('jwtToken');
    const getUserName = () => {
        if (!token) return null;
        const decodedToken = JSON.parse(atob(token.split('.')[1])); // Расшифровываем токен
        return decodedToken?.id || null;  // Возвращаем роль
    };

    const user_id = getUserName()

    const handleSubmit = async () => {
        if (name === null) {
            setError('Поле "name" обязательно');
            return;
        }
        if (x === null) {
            setError('Поле "x" обязательно');
            return;
        }
        if (y === null) {
            setError('Поле "y" обязательно');
            return;
        }
        if (house_id === null) {
            setError('Поле "house id" обязательно');
            return;
        }
        if (area === null) {
            setError('Поле "area" обязательно');
            return;
        }
        if (price === null) {
            setError('Поле "price" обязательно');
            return;
        }
        if (balcony === null) {
            setError('Поле "balcony" обязательно');
            return;
        }
        if (time_to_metro_on_foot === null) {
            setError('Поле "time to metro on foot" обязательно');
            return;
        }
        if (number_of_rooms === null) {
            setError('Поле "number of rooms" обязательно');
            return;
        }
        if (furnish === null) {
            setError('Поле "furnish" обязательно');
            return;
        }
        if (view === null) {
            setError('Поле "view" обязательно');
            return;
        }
        if (transport === null) {
            setError('Поле "transport" обязательно');
            return;
        }
        setError('');
        const data = { name, coordinate: {x, y}, area, price, balcony, time_to_metro_on_foot, number_of_rooms, furnish, view, transport, house_id, user_id };

        try {
            const response = await fetch('http://localhost:8080/api/flat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`, // Добавляем токен в заголовки
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Ошибка при отправке данных на сервер');
            }

            setIsModalOpen(false); // Close the modal on success
        } catch (error) {
            alert(error.message);
        }
    };

    // Function to open the modal
    const openModal = () => {
        setIsModalOpen(true);
    };

    // Function to close the modal
    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div>
            <button onClick={openModal}>Create flat</button>

            {/* Modal structure */}
            {isModalOpen && (
                <div style={modalStyles.overlay}>
                    <div style={modalStyles.container}>
                        <h3>Flat Form</h3>
                        <label>
                            Name:
                            <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                        </label>
                        <label>
                            House ID:
                            <input type="number" value={house_id}
                                   onChange={(e) => setHouseId(e.target.value)}/>
                        </label>
                        <label>
                            X:
                            <input type="number" value={x}
                                   onChange={(e) => setX(e.target.value)}/>
                        </label>
                        <label>
                            Y:
                            <input type="number" value={y}
                                   onChange={(e) => setY(e.target.value)}/>
                        </label>
                        <label>
                            Area:
                            <input type="number" value={area}
                                   onChange={(e) => setArea(e.target.value)}/>
                        </label>
                        <label>
                            Price:
                            <input type="number" value={price}
                                   onChange={(e) => setPrice(e.target.value)}/>
                        </label>
                        <label>
                            Balcony:
                            <input type="radio" name="balcony" value="true" onChange={() => setBalcony(true)}/> Yes
                            <input type="radio" name="balcony" value="false" onChange={() => setBalcony(false)}/> No
                        </label>
                        <label>
                            Time to metro on foot:
                            <input type="number" value={time_to_metro_on_foot}
                                   onChange={(e) => setTimeToMetroOnFoot(e.target.value)}/>
                        </label>
                        <label>
                            Number of rooms:
                            <input type="number" value={number_of_rooms}
                                   onChange={(e) => setNumberOfRooms(e.target.value)}/>
                        </label>
                        <label>
                            Furnish:
                            <select value={furnish} onChange={(e) => setFurnish(e.target.value)}>
                                <option value={Furnish.BAD}>BAD</option>
                                <option value={Furnish.FINE}>FINE</option>
                                <option value={Furnish.DESIGNER}>DESIGNER</option>
                                <option value={Furnish.LITTLE}>LITTLE</option>
                            </select>
                        </label>
                        <label>
                            View:
                            <select value={view} onChange={(e) => setView(e.target.value)}>
                                <option value={View.STREET}>STREET</option>
                                <option value={View.YARD}>YARD</option>
                                <option value={View.BAD}>BAD</option>
                                <option value={View.NORMAL}>NORMAL</option>
                            </select>
                        </label>
                        <label>
                            Transport:
                            <select value={transport} onChange={(e) => setTransport(e.target.value)}>
                                <option value={Transport.FEW}>FEW</option>
                                <option value={Transport.LITTLE}>LITTLE</option>
                                <option value={Transport.NORMAL}>NORMAL</option>
                                <option value={Transport.ENOUGH}>ENOUGH</option>
                            </select>
                        </label>
                        <br/>
                        {error && <p style={{color: 'red'}}>{error}</p>}
                        <button type="button" onClick={handleSubmit}>
                            Submit
                        </button>
                        <button type="button" onClick={closeModal}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

// Styles for modal
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


export default FlatForm;
