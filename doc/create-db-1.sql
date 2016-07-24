-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema hailey
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hailey
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hailey` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `hailey` ;

-- -----------------------------------------------------
-- Table `hailey`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`product` (
  `id` INT NOT NULL COMMENT 'Hailey system produce number',
  `product_no` VARCHAR(25) NOT NULL COMMENT 'Manufacture product number',
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(300) NULL,
  `rrp` DECIMAL(13,4) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `product_no_UNIQUE` (`product_no` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`customer` (
  `id` INT NOT NULL,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(25) NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `bill_address_id` INT NULL,
  `ship_address_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`purchase` (
  `id` INT NOT NULL COMMENT 'To avoid keyword order',
  `cutomer_id` INT NULL,
  `staff_id` INT NULL,
  `date` DATETIME NULL,
  `status` ENUM('backorder','processing','completed') NULL,
  `total` DECIMAL(13,4) NULL,
  `notes` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_customer1_idx` (`cutomer_id` ASC),
  CONSTRAINT `fk_order_customer1`
    FOREIGN KEY (`cutomer_id`)
    REFERENCES `hailey`.`customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`order_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`order_details` (
  `id` INT NOT NULL,
  `product_id` INT NULL,
  `purchase_id` INT NULL,
  `item_serial_no` VARCHAR(25) NULL,
  `quantity` FLOAT NULL,
  `quantity_unit` ENUM('each','kg','m') NULL,
  `price_per_unit` DECIMAL(13,4) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_details_product1_idx` (`product_id` ASC),
  INDEX `fk_order_details_purchase1_idx` (`purchase_id` ASC),
  CONSTRAINT `fk_order_details_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `hailey`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_details_purchase1`
    FOREIGN KEY (`purchase_id`)
    REFERENCES `hailey`.`purchase` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`address` (
  `id` INT NOT NULL,
  `street` VARCHAR(100) NULL,
  `suburb` VARCHAR(45) NULL,
  `state` VARCHAR(45) NULL,
  `postcode` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_address_customer1`
    FOREIGN KEY (`id`)
    REFERENCES `hailey`.`customer` (`bill_address_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_customer2`
    FOREIGN KEY (`id`)
    REFERENCES `hailey`.`customer` (`ship_address_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`media` (
  `id` INT NOT NULL,
  `product_id` INT NULL,
  `item_serial` VARCHAR(25) NULL,
  `type` ENUM('video','image') NULL,
  `url` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_media_product1_idx` (`product_id` ASC),
  CONSTRAINT `fk_media_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `hailey`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hailey`.`sale`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`sale` (
  `id` INT NOT NULL,
  `product_id` INT NULL,
  `item_serial_no` VARCHAR(25) NULL,
  `price` DECIMAL(13,4) NOT NULL,
  `start_date` DATETIME NULL,
  `end_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sale_product1_idx` (`product_id` ASC),
  CONSTRAINT `fk_sale_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `hailey`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `hailey` ;

-- -----------------------------------------------------
-- Placeholder table for view `hailey`.`view1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hailey`.`view1` (`id` INT);

-- -----------------------------------------------------
-- View `hailey`.`view1`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hailey`.`view1`;
USE `hailey`;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
