INSERT INTO Users (username,password,enabled)
    values ('12345','{noop}12345',true);
INSERT INTO Authority (id,username,authority)
    values (2,'12345','PROF');