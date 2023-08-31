insert into tags (name)
values ('negative'),
       ('neutral'),
       ('positive'),
       ('simple'),
       ('complicated');

insert into tag_news (tag_id, news_id)
VALUES (3, 1),
       (4, 1),
       (3, 2),
       (5, 2),
       (2, 3),
       (5, 3),
       (1, 4),
       (5, 4),
       (3, 5),
       (4, 5);
