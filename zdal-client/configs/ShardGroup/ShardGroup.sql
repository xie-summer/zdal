create database shardgroup_0;
create database shardgroup_1;
create database shardgroup_2;
create database shardgroup_3;
create database shardgroup_4;
create database shardgroup_5;
create database shardgroup_6;
create database shardgroup_7;
create database shardgroup_8;
create database shardgroup_9;

//Ö÷¿â
CREATE TABLE shardgroup_0.user_00(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_0.user_01(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_2.user_02(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_2.user_03(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_4.user_04(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_4.user_05(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_6.user_06(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_6.user_07(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_8.user_08(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_8.user_09(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
//±¸¿â
CREATE TABLE shardgroup_1.user_00(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_1.user_01(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_3.user_02(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_3.user_03(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_5.user_04(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_5.user_05(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_7.user_06(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_7.user_07(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_9.user_08(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardgroup_9.user_09(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;

select * from shardgroup_0.user_00;
select * from shardgroup_0.user_01;
select * from shardgroup_2.user_02;
select * from shardgroup_2.user_03;
select * from shardgroup_4.user_04;
select * from shardgroup_4.user_05;
select * from shardgroup_6.user_06;
select * from shardgroup_6.user_07;
select * from shardgroup_8.user_08;
select * from shardgroup_8.user_09;
