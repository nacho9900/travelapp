CREATE TABLE IF NOT EXISTS users (
				id IDENTITY PRIMARY KEY,
				firstname varchar(100) NOT NULL,
				lastname varchar(100) NOT NULL,
				email varchar(100) UNIQUE NOT NULL,
				password varchar(100) NOT NULL
);