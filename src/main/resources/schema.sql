DROP TABLE IF EXISTS phones;

CREATE TABLE phones (
                               id INT PRIMARY KEY,
                               phone_name VARCHAR(50) NOT NULL,
                               phone_price INT NOT NULL

);
CREATE TABLE goodsinstock (
                        id INT PRIMARY KEY,
                        phone_id INT NOT NULL,
                        phone_quantity INT NOT NULL,
                        foreign key (phone_id) references phones(id)

);

CREATE TABLE purchase (
                              id INT PRIMARY KEY,
                              client_name VARCHAR(50) NOT NULL,
                              phone_id INT NOT NULL,
                              date TIMESTAMP NOT NULL,
                              foreign key (phone_id) references phones(id)

);

