CREATE DATABASE IF NOT EXISTS appointment_db DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE appointment_db;


CREATE TABLE `appointments` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `user_id` bigint NOT NULL,
                                `service_name` varchar(100) NOT NULL,
                                `appointment_date` date NOT NULL,
                                `time_slot` varchar(50) NOT NULL,
                                `status` varchar(20) DEFAULT 'BOOKED',
                                `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `unique_appointment` (`user_id`,`appointment_date`,`time_slot`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `phone` varchar(20) NOT NULL,
                         `name` varchar(50) DEFAULT NULL,
                         `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
