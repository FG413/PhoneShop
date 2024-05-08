DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS purchases;

CREATE TABLE phones
(
    phone_id       INT PRIMARY KEY,
    phone_name     VARCHAR(50) NOT NULL,
    phone_price    INT         NOT NULL,
    phone_quantity INT         NOT NULL

);


CREATE TABLE purchases
(
    id          INT PRIMARY KEY,
    client_name VARCHAR(50) NOT NULL,
    phone_id    INT         NOT NULL,
    date        TIMESTAMP   NOT NULL

);

