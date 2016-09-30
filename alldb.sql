CREATE TABLE `virtuapay`.`user_info` ( `uid` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(60) NOT NULL , `email_id` VARCHAR(100) NOT NULL , `password` VARCHAR(60) NOT NULL , `mykey` VARCHAR(60) NOT NULL , `type` INT NOT NULL DEFAULT '0' , PRIMARY KEY (`uid`), UNIQUE `email` (`email_id`(100))) ENGINE = InnoDB;

CREATE TABLE `virtuapay`.`cash_holder` ( `uid` INT NOT NULL , `vcid` INT NOT NULL , PRIMARY KEY (`vcid`), INDEX `uid` (`uid`)) ENGINE = InnoDB;

ALTER TABLE `cash_holder` ADD CONSTRAINT `userrelation` FOREIGN KEY (`uid`) REFERENCES `virtuapay`.`user_info`(`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE `virtuapay`.`virtual_cash` ( `isvalid` INT NOT NULL DEFAULT '1' , `vcid` INT NOT NULL AUTO_INCREMENT , `amount` INT NOT NULL , `code` VARCHAR(400) NOT NULL , PRIMARY KEY (`vcid`)) ENGINE = InnoDB;


