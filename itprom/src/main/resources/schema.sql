DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS professions;

CREATE TABLE departments (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(100) NOT NULL UNIQUE ,
    commentary TEXT ,
    parent_department BIGINT ,
    FOREIGN KEY (parent_department) REFERENCES departments(id) ON DELETE SET NULL
);

CREATE TABLE professions (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL UNIQUE ,
    commentary TEXT
);

CREATE TABLE employees (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    first_name VARCHAR(50) NOT NULL ,
    last_name VARCHAR(50) NOT NULL ,
    patronymic VARCHAR(50) ,
    commentary TEXT ,
    profession_id BIGINT ,
    department_id BIGINT ,
    FOREIGN KEY (profession_id) REFERENCES professions(id) ON DELETE SET NULL ,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);