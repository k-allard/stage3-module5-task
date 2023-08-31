INSERT INTO authors (name, create_date, last_update_date)
VALUES ('Michelle Dessler', current_timestamp, current_timestamp),
       ('Mark Twain', current_timestamp, current_timestamp),
       ('Kim O''Brian', current_timestamp, current_timestamp),
       ('Chloe Bauer', current_timestamp, current_timestamp),
       ('Anna Palmer', current_timestamp, current_timestamp);

insert into news (content, create_date, last_update_date, title, author_id)
values ('A Nigerian boy solves a 30-year math equation, is recognized by a Japanese university.', current_timestamp,
        current_timestamp, 'GENERAL PROVISIONS', 1),
       ('Monsanto will close three of its facilities. We are making a difference!', current_timestamp,
        current_timestamp, 'PROBATE CODE', 1),
       ('Rosa Parks has been honored with a reserved front seat on Texas buses.', current_timestamp, current_timestamp,
        'TRUSTS', 2),
       ('This little boy giving his sister bone marrow, even though he believed it would kill him.', current_timestamp,
        current_timestamp, 'DOMESTIC RELATIONS', 2),
       ('A city letting people pay parking fines with canned goods for the hungry.', current_timestamp,
        current_timestamp, 'COMPENSATION', 5);
