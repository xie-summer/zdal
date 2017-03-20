create database group_write;
create database group_read0;
create database group_read1;
create database group_read2;
create database group_read3;

CREATE TABLE group_write.test(id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE group_read0.test(id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE group_read1.test(id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE group_read2.test(id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE group_read3.test(id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;