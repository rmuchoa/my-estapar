CREATE TABLE IF NOT EXISTS garage_sector (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    base_price DECIMAL(5,2) NOT NULL,
    max_capacity INTEGER NOT NULL,
    duration_limit_minutes INTEGER NOT NULL,
    open_hour TIME NOT NULL,
    close_hour TIME NOT NULL
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

CREATE TABLE IF NOT EXISTS car_entry (
    id SERIAL PRIMARY KEY,
    license_plate TEXT NOT NULL,
    entry_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);