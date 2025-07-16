CREATE TABLE IF NOT EXISTS garage_sector (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    base_price DECIMAL(5,2) NOT NULL,
    max_capacity INTEGER NOT NULL,
    duration_limit_minutes INTEGER NOT NULL,
    open_hour TIME NOT NULL,
    close_hour TIME NOT NULL,
    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS garage_spot (
    id SERIAL PRIMARY KEY,
    sector_id INTEGER NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    occupied BOOLEAN NOT NULL,
    CONSTRAINT fk_garage_sector
        FOREIGN KEY (sector_id)
        REFERENCES garage_sector(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS garage_logging (
    id SERIAL PRIMARY KEY,
    license_plate TEXT NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP NULL,
    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS garage_parking (
    id SERIAL PRIMARY KEY,
    spot_id INTEGER NOT NULL,
    license_plate TEXT NOT NULL,
    price_level_rate INTEGER NOT NULL,
    parking_time TIMESTAMP NOT NULL,
    unparking_time TIMESTAMP NULL,
    CONSTRAINT fk_garage_spot
        FOREIGN KEY (spot_id)
        REFERENCES garage_spot(id)
        ON DELETE CASCADE
);