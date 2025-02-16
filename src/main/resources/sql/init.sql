DROP TABLE IF EXISTS movies;

CREATE TABLE movies
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    date        VARCHAR(10),
    tagline     VARCHAR(255),
    description CLOB,
    duration    INT,
    rating      DECIMAL(3, 1)
);
