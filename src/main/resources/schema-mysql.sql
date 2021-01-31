DROP TABLE IF EXISTS Exercise;
DROP TABLE IF EXISTS Equipment;

CREATE TABLE Equipment (
	`id` int NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE Exercise (
	`id` int NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) UNIQUE NOT NULL,
    `description` VARCHAR(255),
    `type` VARCHAR(30),
    `muscle_group` VARCHAR(50),
    `equipment_id` int,
    PRIMARY KEY (id),
    CONSTRAINT FK__Exercise__Equipment FOREIGN KEY (equipment_id) REFERENCES Equipment(id)
);


