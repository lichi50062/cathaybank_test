# cathaybank
國泰世華 JAVA engineer 線上作業

首頁 http://localhost:8888/

SQL語法如下

  CREATE TABLE CURRENCY_MAPPER

  (

  id INTEGER auto_increment,

  CURRENCY_CODE varchar2 UNIQUE,

  CURRENCY_NAME varchar2 UNIQUE

  );

comment on table CURRENCY_MAPPER is '幣別對照表';

comment on column CURRENCY_MAPPER.id is '主鍵';

comment on column CURRENCY_MAPPER.CURRENCY_CODE is '幣別代碼';

comment on column CURRENCY_MAPPER.CURRENCY_NAME is '幣別中文';

INSERT INTO CURRENCY_MAPPER (CURRENCY_CODE, CURRENCY_NAME) VALUES ('USD', '美元');

INSERT INTO CURRENCY_MAPPER (CURRENCY_CODE, CURRENCY_NAME) VALUES ('GBP', '英鎊');

INSERT INTO CURRENCY_MAPPER (CURRENCY_CODE, CURRENCY_NAME) VALUES ('EUR', '歐元');

