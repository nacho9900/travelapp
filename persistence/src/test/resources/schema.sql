CREATE TABLE IF NOT EXISTS users (
				id IDENTITY PRIMARY KEY,
				firstname varchar(100) NOT NULL,
				lastname varchar(100) NOT NULL,
				email varchar(100) UNIQUE NOT NULL,
				password varchar(100) NOT NULL,
				nationality varchar(100) NOT NULL,
				birthday DATE NOT NULL
);


insert into users values(1,'firstname','lastname','test@mail.com','password','Argentina','2016-01-02');
