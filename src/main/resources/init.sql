CREATE TABLE marketing_budgets (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   category_name VARCHAR(255),
                                   budget DECIMAL(10, 2)
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
