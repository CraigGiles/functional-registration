CREATE TABLE IF NOT EXISTS  `sessions` (
  `id` varchar(40) NOT NULL,
  `ip_address` varchar(16) DEFAULT '0' NOT NULL,
  `user_agent` varchar(50) NOT NULL,
  `user_data` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);
