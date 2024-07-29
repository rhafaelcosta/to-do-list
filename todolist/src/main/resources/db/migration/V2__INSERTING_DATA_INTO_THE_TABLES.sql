-- Populating the TAG table
INSERT INTO TAG (name) VALUES ('Urgente');
INSERT INTO TAG (name) VALUES ('Importante');
INSERT INTO TAG (name) VALUES ('Opcional');
INSERT INTO TAG (name) VALUES ('Java');
INSERT INTO TAG (name) VALUES ('Spring Boot');

-- Populating the USERS table
INSERT INTO USERS (name, email, active) VALUES ('Anna', 'anna@email.com.br', TRUE);
INSERT INTO USERS (name, email, active) VALUES ('Jose', 'jose@email.com.br', TRUE);
INSERT INTO USERS (name, email, active) VALUES ('João', 'joao@email.com.br', FALSE);

-- Populating the TASKS table
INSERT INTO TASK (user_id, title, description, priority, status_type, severity_type, create_at) 
VALUES (1, 'Task 1', 'Description for Task 1', 1, 1, 1, CURRENT_TIMESTAMP);
INSERT INTO TASK (user_id, title, description, priority, status_type, severity_type, create_at) 
VALUES (1, 'Task 2', 'Description for Task 2', 2, 2, 2, CURRENT_TIMESTAMP);
INSERT INTO TASK (user_id, title, description, priority, status_type, severity_type, create_at) 
VALUES (2, 'Task 3', 'Description for Task 3', 1, 1, 1, CURRENT_TIMESTAMP);
INSERT INTO TASK (user_id, title, description, priority, status_type, severity_type, create_at) 
VALUES (3, 'Task 4', 'Description for Task 4', 3, 3, 3, CURRENT_TIMESTAMP);

-- Populating the TASK_TAG table
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (1, 1);  -- Urgente, Task 1
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (2, 1);  -- Importante, Task 1
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (3, 2);  -- Opcional, Task 2
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (4, 3);  -- Backlog, Task 3
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (5, 4);  -- Em Progresso, Task 4
INSERT INTO TASK_TAG (tag_id, task_id) VALUES (2, 4);  -- Importante, Task 4