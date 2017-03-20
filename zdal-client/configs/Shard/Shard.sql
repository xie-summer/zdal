create database shard_0;
create database shard_1;
create database shard_2;
create database shard_3;
create database shard_4;

CREATE TABLE shard_0.user_00(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_0.user_01(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_1.user_02(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_1.user_03(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_2.user_04(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_2.user_05(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_3.user_06(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_3.user_07(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_4.user_08(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shard_4.user_09(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;


select * from shard_0.user_00;
select * from shard_0.user_01;
select * from shard_1.user_02;
select * from shard_1.user_03;
select * from shard_2.user_04;
select * from shard_2.user_05;
select * from shard_3.user_06;
select * from shard_3.user_07;
select * from shard_4.user_08;
select * from shard_4.user_09;
