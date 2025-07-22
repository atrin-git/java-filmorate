MERGE INTO GENRES (ID, NAME)
VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик')
;

MERGE INTO RATINGS (ID, NAME)
VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17')
;

MERGE INTO EVENTS (ID, CODE)
VALUES
    (1, 'LIKE'),
    (2, 'REVIEW'),
    (3, 'FRIEND')
;


MERGE INTO OPERATIONS (ID, CODE)
VALUES
    (1, 'REMOVE'),
    (2, 'ADD'),
    (3, 'UPDATE')
;