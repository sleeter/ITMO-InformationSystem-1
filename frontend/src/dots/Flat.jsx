import React, { useRef, useEffect } from 'react';

const FlatMap = ({ flats }) => {
    const canvasRef = useRef(null);

    // Рисуем точки на canvas
    const drawPoints = (ctx, flats) => {
        flats.map(flat => {
            const { x, y } = flat.coordinates;

            // Преобразуем координаты для подходящего масштаба
            const canvasWidth = ctx.canvas.width;
            const canvasHeight = ctx.canvas.height;

            // Простой масштаб для отображения на холсте
            const scaledX = (x / 1000) * canvasWidth; // Масштаб X (например, для координат от 0 до 1000)
            const scaledY = (y / 1000) * canvasHeight; // Масштаб Y (например, для координат от 0 до 1000)

            // Рисуем точку
            ctx.beginPath();
            ctx.arc(scaledX, scaledY, 5, 0, 2 * Math.PI);
            ctx.fillStyle = 'blue';
            ctx.fill();
        });
    };

    // Когда компонент монтируется, рисуем точки
    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext('2d');

        // Очищаем холст
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        // Рисуем точки
        drawPoints(ctx, flats);
    }, [flats]);

    return (
        <div>
            <h2>Map of Flats</h2>
            <canvas ref={canvasRef} width="500" height="500" style={{ border: '1px solid black' }} />
        </div>
    );
};

export default FlatMap;
