CREATE TABLE `order_t` (
  `id` integer AUTO_INCREMENT COMMENT 'auto inc id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'order name' ,
  `order_id` integer NOT NULL DEFAULT 0 COMMENT 'order id' ,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `item_t` (
  `id` integer   AUTO_INCREMENT COMMENT 'auto inc id',
  `item_id` integer NOT NULL DEFAULT  0 COMMENT 'item id',
  `order_id` integer NOT NULL DEFAULT 0 COMMENT 'order autoinc id' ,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'item name' ,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for jar_info
-- ----------------------------
CREATE TABLE `jar_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `type` varchar(32) NOT NULL,
  `version` varchar(32) NOT NULL,
  `uploadTime` bigint(20) NOT NULL,
  `sparkVersion` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='Manager管理的所有JAR包';

-- ----------------------------
-- Table structure for employees
-- ----------------------------
CREATE TABLE `employees` (
  `emp_no` int(11) NOT NULL,
  `birth_date` date NOT NULL,
  `first_name` varchar(14) NOT NULL,
  `last_name` varchar(16) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `hire_date` date NOT NULL,
  PRIMARY KEY (`emp_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for titles
-- ----------------------------
CREATE TABLE `titles` (
  `emp_no` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `from_date` date NOT NULL,
  `to_date` date DEFAULT NULL,
  PRIMARY KEY (`emp_no`,`title`,`from_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for salaries
-- ----------------------------
CREATE TABLE `salaries` (
  `emp_no` int(11) NOT NULL,
  `salary` int(11) NOT NULL,
  `from_date` date NOT NULL,
  `to_date` date NOT NULL,
  PRIMARY KEY (`emp_no`,`from_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


