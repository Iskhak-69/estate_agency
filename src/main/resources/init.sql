CREATE TABLE marketing_budgets (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   category_name VARCHAR(255),
                                   budget DECIMAL(10, 2)
);

CREATE TABLE marketing_expenses (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    amount DECIMAL(10, 2)
);


CREATE TABLE construction_equipment (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        equipment_name VARCHAR(100) NOT NULL,
                                        quantity INT NOT NULL
);


CREATE TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(50),
                           last_name VARCHAR(50),
                           position VARCHAR(100),
                           department VARCHAR(100)
);

CREATE TABLE tasks (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       task_name VARCHAR(255),
                       description TEXT,
                       due_date DATE
);


CREATE TABLE coverage_areas (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                region_name VARCHAR(255),
                                client_coverage INT, -- количество клиентов в регионе
                                total_population INT -- общая численность населения в регионе
);
CREATE TABLE real_estate (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             property_type VARCHAR(50),
                             transaction_type ENUM('sale', 'rent', 'lease-to-own'),
                             price DECIMAL(10, 2),
                             date_listed DATE
);

CREATE TABLE available_apartments_for_sale (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               address VARCHAR(255) NOT NULL,
                                               price DECIMAL(10, 2) NOT NULL,
                                               status VARCHAR(50) DEFAULT 'Available'
);


CREATE TABLE sold_apartments (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               address VARCHAR(255) NOT NULL,
                                               price DECIMAL(10, 2) NOT NULL,
                                               buyer_name VARCHAR(100) NOT NULL,
                                               sale_date DATE NOT NULL
);

CREATE TABLE apartments (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            address VARCHAR(255) NOT NULL,
                            owner VARCHAR(255),
                            status ENUM('Available', 'Sold', 'Rented') DEFAULT 'Available'
);

CREATE TABLE workers (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(100) NOT NULL,
                         password VARCHAR(100) NOT NULL,
                         salary DECIMAL(10, 2) NOT NULL
);


CREATE TABLE instructions (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              employee_id INT,
                              instruction TEXT,
                              date_assigned DATE,
                              FOREIGN KEY (employee_id) REFERENCES employees(id)
);


CREATE TABLE coverage_areas (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                area_name VARCHAR(255) NOT NULL,
                                region VARCHAR(255) NOT NULL
);


