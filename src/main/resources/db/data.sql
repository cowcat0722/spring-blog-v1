insert into user_tb(username, password, email, created_at) values('ssar', '1234', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '1234', 'cos@nate.com', now());

insert into board_tb(title, content, user_id,  created_at) values('제목1','내용1',1,now());
insert into board_tb(title, content, user_id,  created_at) values('제목2','내용2',1,now());
insert into board_tb(title, content, user_id,  created_at) values('제목3','내용3',1,now());
insert into board_tb(title, content, user_id,  created_at) values('제목4','내용4',2,now());

insert into reply_tb(board_id,user_id,comment,created_at) values (1,1,'안녕1-1',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (1,2,'안녕1-2',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (2,1,'안녕2-1',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (2,2,'안녕2-2',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (3,1,'안녕3-1',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (3,2,'안녕3-2',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (4,1,'안녕4-1',now());
insert into reply_tb(board_id,user_id,comment,created_at) values (4,2,'안녕4-2',now());