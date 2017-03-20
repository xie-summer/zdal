create database shardfailover_master_0;
create database shardfailover_master_1;
create database shardfailover_master_2;
create database shardfailover_master_3;
create database shardfailover_master_4;
create database shardfailover_failover;


CREATE TABLE shardfailover_master_0.user_00(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_0.user_01(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_1.user_02(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_1.user_03(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_2.user_04(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_2.user_05(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_3.user_06(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_3.user_07(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_4.user_08(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_master_4.user_09(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;

CREATE TABLE shardfailover_failover.user_00(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_01(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_02(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_03(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_04(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_05(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_06(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_07(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_08(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;
CREATE TABLE shardfailover_failover.user_09(user_id varchar(20) DEFAULT NULL,name varchar(20) DEFAULT NULL,address varchar(255) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gbk;


select * from shardfailover_master_0.user_00;
select * from shardfailover_master_0.user_01;
select * from shardfailover_master_1.user_02;
select * from shardfailover_master_1.user_03;
select * from shardfailover_master_2.user_04;
select * from shardfailover_master_2.user_05;
select * from shardfailover_master_3.user_06;
select * from shardfailover_master_3.user_07;
select * from shardfailover_master_4.user_08;
select * from shardfailover_master_4.user_09;
