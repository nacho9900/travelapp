CREATE TABLE IF NOT EXISTS places (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL,
                latitude float NOT NULL,
                longitude float NOT NULL,
                country varchar(100) NOT NULL,
                area1 varchar(100) NOT NULL,
                area2 varchar(100) NOT NULL,
                locality varchar(100) NOT NULL,
                street varchar(100) NOT NULL,
                street_num integer NOT NULL
);


CREATE TABLE IF NOT EXISTS users (
				id SERIAL PRIMARY KEY,
				firstname varchar(100) NOT NULL,
				lastname varchar(100) NOT NULL,
				email varchar(100) UNIQUE NOT NULL,
				password varchar(100) NOT NULL,
				nationality varchar(100) NOT NULL,
				birthday DATE NOT NULL
);


CREATE TABLE IF NOT EXISTS trips (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL,
                description varchar(500),
                start_date DATE,
                end_date DATE,
                startplace_id INTEGER NOT NULL,
                FOREIGN KEY (startplace_id) REFERENCES places ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS activity_categories (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS activities (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL,
                category_id integer NOT NULL,
                FOREIGN KEY (category_id) REFERENCES activity_categories ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS activity_places (
                id SERIAL PRIMARY KEY,
                activity_id INTEGER NOT NULL,
                place_id INTEGER NOT NULL,
                FOREIGN KEY (activity_id) REFERENCES activities ON DELETE CASCADE,
                FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS trip_places (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                place_id INTEGER NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS trip_activities (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                place_id INTEGER NOT NULL,
                activity_id INTEGER NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE,
                FOREIGN KEY (activity_id) REFERENCES activities ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS user_roles (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL,
                level INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS trip_users (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                user_role_id INTEGER NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
                FOREIGN KEY (user_role_id) REFERENCES user_roles ON DELETE CASCADE
);
/*
CREATE INDEX IF NOT EXISTS email_idx ON users (email);


*/