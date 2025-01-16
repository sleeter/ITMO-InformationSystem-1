-- Таблица для пользователей
CREATE TABLE Users (
                      id BIGSERIAL PRIMARY KEY,
                      login VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL,
                      role VARCHAR(50) NOT NULL
);

-- Таблица для Coordinates
CREATE TABLE Coordinates (
                             id BIGSERIAL PRIMARY KEY,
                             x BIGINT NOT NULL CHECK (x <= 598),
                             y FLOAT NOT NULL CHECK (y > -254)
);

-- Таблица для House
CREATE TABLE Houses (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        number_of_lifts BIGINT NOT NULL CHECK (number_of_lifts > 0),
                        year INT NOT NULL CHECK (Houses.year > 0 AND Houses.year <= 901),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        user_create_id BIGINT REFERENCES Users(id),
                        user_update_id BIGINT REFERENCES Users(id)
);

-- Таблица для Flat
CREATE TABLE Flats (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL CHECK (name <> ''),
                      coordinates_id BIGINT NOT NULL REFERENCES Coordinates(id),
                      area DOUBLE PRECISION NOT NULL CHECK (area > 0 AND area <= 724),
                      price FLOAT NOT NULL CHECK (price > 0),
                      balcony BOOLEAN NOT NULL,
                      time_to_metro_on_foot FLOAT NOT NULL CHECK (time_to_metro_on_foot > 0),
                      number_of_rooms BIGINT CHECK (number_of_rooms > 0),
                      furnish VARCHAR(50),
                      view VARCHAR(50) NOT NULL,
                      transport VARCHAR(50) NOT NULL,
                      house_id BIGINT REFERENCES Houses(id),
                      creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      user_create_id BIGINT REFERENCES Users(id),
                      user_update_id BIGINT REFERENCES Users(id)
);

CREATE TABLE Requests (
                          id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES Users(id),
    admin_id BIGINT NOT NULL REFERENCES Users(id),
    approved BOOLEAN NOT NULL
);

CREATE TABLE imports (
    id bigserial primary key,
    status varchar(50) not null,
    user_create_id bigint references Users(id),
    created_at timestamp default current_timestamp,
    filename varchar(50) not null,
    count int not null
);
