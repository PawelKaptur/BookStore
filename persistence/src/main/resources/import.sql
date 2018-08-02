insert into book (id, title, authors, status) values (1, 'First book', 'Jan Kowalski', 'FREE');
insert into book (id, title, authors, status) values (2, 'Second book', 'Zbigniew Nowak', 'FREE');
insert into book (id, title, authors, status) values (3, 'Third book', 'Janusz Jankowski', 'FREE');
insert into book (id, title, authors, status) values (4, 'Starter kit book', 'Kacper Ossoliński', 'FREE');
insert into book (id, title, authors, status) values (5, 'Z kamerą wśród programistów', 'Krystyna Czubówna', 'MISSING');

insert into user (id, user_name, password, role, enabled) values (1, 'admin', '$2a$10$JZkzesLdxenHQTzZ7g4CzuyOes7xj8Kxi3Ano9fu0rjXJLHTSpvGC', 'ROLE_ADMIN', true);
insert into user (id, user_name, password, role, enabled) values (2, 'user', '$2a$10$lFXbfGBp.SacATViXz5ui.pFpb31Bo5nTUYzQUcrE/4AXRQ2EKa7m', 'ROLE_USER', true);