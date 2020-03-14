ALTER TABLE `order_t` ADD INDEX name_key (`name`);
ALTER TABLE `order_t` ADD UNIQUE INDEX order_id_unique (`order_id`);

ALTER TABLE `item_t` ADD INDEX name_key (`name`);

