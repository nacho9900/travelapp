CREATE TABLE IF NOT EXISTS picture
(id IDENTITY PRIMARY KEY,
 name  VARCHAR(255) NOT NULL,
 image BLOB
);

CREATE TABLE IF NOT EXISTS places
(id IDENTITY PRIMARY KEY,
 address   VARCHAR(500) NOT NULL,
 google_id VARCHAR(150) NOT NULL,
 latitude  DOUBLE PRECISION,
 longitude DOUBLE PRECISION,
 name      VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS trips
(id IDENTITY PRIMARY KEY,
 description VARCHAR(500) NOT NULL,
 end_date    DATE         NOT NULL,
 name        VARCHAR(100) NOT NULL,
 start_date  DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS activities
(id IDENTITY PRIMARY KEY,
 end_date   DATE        NOT NULL,
 name       VARCHAR(40) NOT NULL,
 start_date DATE        NOT NULL,
 place_id   BIGINT      NOT NULL,
 trip_id    BIGINT      NOT NULL,
 CONSTRAINT fkmta4l2nip09tcig9uc3frjgfu FOREIGN KEY (place_id) REFERENCES places,
 CONSTRAINT fkkx78ofjocchsrmnsf02creh65 FOREIGN KEY (trip_id) REFERENCES trips
);

CREATE TABLE IF NOT EXISTS trip_pictures
(id IDENTITY PRIMARY KEY,
 name    VARCHAR(255) NOT NULL,
 image   BLOB,
 trip_id BIGINT       NOT NULL,
 CONSTRAINT uk_cwebv4oi2n0mwmmf0962l8l6d UNIQUE (trip_id),
 CONSTRAINT fk3cxmkdbqqyu3kxw8rr8tqb2ix FOREIGN KEY (trip_id) REFERENCES trips
);

CREATE TABLE IF NOT EXISTS users
(id IDENTITY PRIMARY KEY,
 biography          VARCHAR(500),
 birthday           DATE         NOT NULL,
 email              VARCHAR(100) NOT NULL,
 firstname          VARCHAR(100) NOT NULL,
 lastname           VARCHAR(100) NOT NULL,
 nationality        VARCHAR(100) NOT NULL,
 password           VARCHAR(100) NOT NULL,
 verification_token VARCHAR(36)  NOT NULL,
 verified           BOOLEAN      NOT NULL,
 CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS password_recovery_token
(id IDENTITY PRIMARY KEY,
 expiresin TIMESTAMP   NOT NULL,
 token     VARCHAR(36) NOT NULL,
 used      BOOLEAN     NOT NULL,
 user_id   BIGINT      NOT NULL,
 CONSTRAINT uk_297d13rkyrp33fttiymw0gtcp UNIQUE (user_id),
 CONSTRAINT fkaibuvih1s7vsqeg3h79f8r5g2 FOREIGN KEY (user_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS trip_join_requests
(id IDENTITY PRIMARY KEY,
 created_on TIMESTAMP    NOT NULL,
 message    VARCHAR(255),
 status     VARCHAR(255) NOT NULL,
 trip_id    BIGINT       NOT NULL,
 user_id    BIGINT       NOT NULL,
 CONSTRAINT fki121t51penhygr7l9aondofke FOREIGN KEY (trip_id) REFERENCES trips,
 CONSTRAINT fk267mbd3d5l6p1bo1doim4bnqs FOREIGN KEY (user_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS trip_members
(id IDENTITY PRIMARY KEY,
 is_active BOOLEAN,
 role      VARCHAR(255) NOT NULL,
 trip_id   BIGINT,
 user_id   BIGINT       NOT NULL,
 CONSTRAINT fklrwxwvuaj3iddljvck3c3gtqr FOREIGN KEY (trip_id) REFERENCES trips,
 CONSTRAINT fk652lcpk0gdigjfm28bhgu24y2 FOREIGN KEY (user_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS trip_comments
(id IDENTITY PRIMARY KEY,
 comment    VARCHAR(160) NOT NULL,
 created_on TIMESTAMP    NOT NULL,
 member_id  BIGINT       NOT NULL,
 CONSTRAINT fkaa70y15ks42m67bawoqbi2dnr FOREIGN KEY (member_id) REFERENCES trip_members
);

CREATE TABLE IF NOT EXISTS trip_rate
(id IDENTITY PRIMARY KEY,
 created_on TIMESTAMP NOT NULL,
 rate       INTEGER   NOT NULL,
 member_id  BIGINT    NOT NULL,
 CONSTRAINT uk_3qaemwt3jof9lud66ccv7e4wi UNIQUE (member_id),
 CONSTRAINT fkqgbitgj6rcyjsmhrly40nc3s4 FOREIGN KEY (member_id) REFERENCES trip_members
);

CREATE TABLE IF NOT EXISTS user_pictures
(id IDENTITY PRIMARY KEY,
 name    VARCHAR(255),
 image   BLOB,
 user_id BIGINT NOT NULL,
 CONSTRAINT uk_6tfh6treuym2yjyt2suwqb0gd UNIQUE (user_id),
 CONSTRAINT fkh10qx447rnu5jptru984is8jc FOREIGN KEY (user_id) REFERENCES users
);

INSERT INTO users (id, biography, birthday, email, firstname, lastname, nationality, password, verification_token,
                   verified)
VALUES (1, '', DATE '1995-11-30', 'inegro@itba.edu.ar', 'Ignacio', 'Negro Caino', 'AR', 'v3r1s3cr3t3p4ss',
        UUID('b60cd648-74a3-11eb-9439-0242ac130002'), TRUE);



-- INSERT INTO places(id, google_id, name, latitude, longitude, address)
-- values(3, 'google id', 'Bahamas', 100, 100, 'Bahamas address');
--

-- INSERT INTO users (id, biography, birthday, email, firstname, lastname, nationality, password,
--                    verification_token, verified)
-- VALUES (1, '', DATE '1995-11-30', 'nachonegrocaino@gmail.com', 'Ignacio Nicolás', 'Negro Caino', 'AR',
--         'password', 'b60cd648-74a3-11eb-9439-0242ac130002', TRUE);
--
-- INSERT INTO trips(id, name, description, start_date, end_date, startplaceid, adminid)
-- values(2,'test name', 'test description', DATE '2019-08-23', DATE '2020-02-01', 3, 1);
--
-- INSERT INTO activities(id, name, category, place_id, start_date, end_date, trip_id)
-- values(4,'Scuba diving','Sports',3, DATE '2019-09-10', DATE '2019-09-11', 2);
--
-- INSERT INTO trips_users(trips_id, users_id)
-- values(2, 1);
--
-- INSERT INTO trip_pictures(id, trip_id, image)
-- values(1, 2, NULL);
--
-- INSERT INTO user_pictures(id, user_id, image)
-- values(1, 1, NULL);
--
-- INSERT INTO trip_comments(id, comment, created_on, trip_id, user_id)
-- VALUES (1, 'test comment', CURRENT_TIMESTAMP, 2, 1);


