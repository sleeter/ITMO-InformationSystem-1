import React, { useState } from 'react';

const FlatOperations = () => {
    // Состояния для ввода
    const [transport, setTransport] = useState('');
    const [houseId, setHouseId] = useState('');
    const [id1, setId1] = useState('');
    const [id2, setId2] = useState('');
    const [flatsCount, setFlatsCount] = useState(null);
    const [cheaperFlat, setCheaperFlat] = useState(null);
    const [flatsSorted, setFlatsSorted] = useState([]);
    const jwtToken = localStorage.getItem('jwtToken');

    // Обработчик для удаления квартир по транспорту
    const handleDeleteFlatsByTransport = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/flat/func/delete/${transport}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert(`Flats deleted for transport: ${transport}`);
            } else {
                console.error('Error deleting flats:', response.statusText);
            }
        } catch (error) {
            console.error('Error deleting flats:', error);
        }
    };

    // Обработчик для подсчета квартир по houseId
    const handleCountFlatsByHouseId = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/flat/func/count/${houseId}`,{
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch flats count');
            }
            const data = await response.json();
            setFlatsCount(data);
            console.log(data)
        } catch (error) {
            console.error('Error counting flats:', error);
        }
    };

    // Обработчик для получения более дешевой квартиры
    const handleGetCheaperFlat = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/api/flat/func/cheaper?id1=${id1}&id2=${id2}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${jwtToken}`,
                        'Content-Type': 'application/json'
                    }
                });
            if (!response.ok) {
                throw new Error('Failed to fetch cheaper flat');
            }
            const data = await response.json();
            console.log(data)
            setCheaperFlat(data);
        } catch (error) {
            console.error('Error getting cheaper flat:', error);
        }
    };

    // Обработчик для получения отсортированных квартир по времени до метро
    const handleGetFlatsSortedByMetroTime = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/flat/func/sorted', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch sorted flats');
            }
            const data = await response.json();
            console.log(data)
            setFlatsSorted(data);
        } catch (error) {
            console.error('Error sorting flats:', error);
        }
    };

    return (
        <div>
            <h2>Flat Operations</h2>

            {/* Удаление квартир по транспорту */}
            <div>
                <h3>Delete Flats by Transport</h3>
                <input
                    type="text"
                    value={transport}
                    onChange={(e) => setTransport(e.target.value)}
                    placeholder="Enter transport type"
                />
                <button onClick={handleDeleteFlatsByTransport}>Delete</button>
            </div>

            {/* Подсчет квартир по houseId */}
            <div>
                <h3>Count Flats by House ID</h3>
                <input
                    type="number"
                    value={houseId}
                    onChange={(e) => setHouseId(e.target.value)}
                    placeholder="Enter house ID"
                />
                <button onClick={handleCountFlatsByHouseId}>Count Flats</button>
                {flatsCount !== null && <p>Flats Count: {flatsCount}</p>}
            </div>

            {/* Получение более дешевой квартиры */}
            <div>
                <h3>Get Cheaper Flat</h3>
                <input
                    type="number"
                    value={id1}
                    onChange={(e) => setId1(e.target.value)}
                    placeholder="Enter ID1"
                />
                <input
                    type="number"
                    value={id2}
                    onChange={(e) => setId2(e.target.value)}
                    placeholder="Enter ID2"
                />
                <button onClick={handleGetCheaperFlat}>Get Cheaper Flat</button>
                {cheaperFlat && (
                    <div>
                        <p>Cheaper Flat: {cheaperFlat.id}</p>
                        <p>Price: {cheaperFlat.price}</p>
                    </div>
                )}
            </div>

            {/* Получение отсортированных квартир */}
            <div>
                <h3>Get Flats Sorted by Metro Time</h3>
                <button onClick={handleGetFlatsSortedByMetroTime}>Get Sorted Flats</button>
                {flatsSorted.length > 0 && (
                    <div>
                        <h4>Sorted Flats:</h4>
                        <ul>
                            {flatsSorted.map((flat) => (
                                <li key={flat.id}>
                                    {flat.id} - {flat.name} - {flat.time_to_metro_on_foot} minutes to metro
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FlatOperations;
