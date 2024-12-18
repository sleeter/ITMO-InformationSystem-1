import React, { useState } from 'react';

const HouseForm = () => {
    const [name, setName] = useState("");
    const [number_of_lifts, setNumberOfLifts] = useState("");
    const [year, setYear] = useState("");
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
        if (number_of_lifts === null) {
            setError('Поле "number of lifts" обязательно');
            return;
        }
        if (year === null) {
            setError('Поле "year" обязательно');
            return;
        }
        setError('');
        const data = { name, number_of_lifts, year, user_id };

        try {
            const response = await fetch('http://localhost:8080/api/house', {
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
            <button onClick={openModal}>Create house</button>

            {/* Modal structure */}
            {isModalOpen && (
                <div style={modalStyles.overlay}>
                    <div style={modalStyles.container}>
                        <h3>House Form</h3>
                        <label>
                            Name:
                            <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                        </label>
                        <label>
                            Number of lifts:
                            <input type="number" value={number_of_lifts}
                                   onChange={(e) => setNumberOfLifts(e.target.value)}/>
                        </label>
                        <label>
                            Year:
                            <input type="number" value={year}
                                   onChange={(e) => setYear(e.target.value)}/>
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
        borderRadius: '5px',
        width: '300px',
        textAlign: 'center',
    },
};

export default HouseForm;
