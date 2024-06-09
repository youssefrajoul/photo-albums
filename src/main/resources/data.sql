INSERT INTO Users (username, password, enabled, PUBLIC_KEY, SALT)
    values ('test','$2a$10$bBkBRRDtDSyemY769mAb5.HI0JlKb.TeFUr0BDVMl9ywAwO8ZtXGq', true, 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6NIAIXTDBZlXKlQLGl4CLV6nhY1KM6En74YbNLFJsYAN+c6V8m+LvD5PFkFPbmmaLFAvLMFpYI2bgwWftQmNK4Q1FzmTmJ5f16ejzeGwEmwsLnVSftKNUsZgkx6lmfsVV7EwnoUbWR+xU2zDzCTE3M31fB/rx0a7OQEHK8xNCRTuMxAqJxCbmlhGrv9g//Vgo3slkaY+TUjhWM++UHlouIUJ/iG4JmIPqriHW90rnn1OVckKQl08tWlGzeDjqwMLDe9XiGI+VXLZSohuisTqUtqKRHk8eq4mEWpM2/g1u2y6OYv3bWficfHJm8YT0NSO8tg3+BwT54GIwJcPoKciAQIDAQAB','e7k5THKm2Gx+lT+JoKsteQ==');
INSERT INTO Authority (username,authority)
    values ('test','admin');
INSERT INTO Album (name, owner)
    values ('holiday','test');