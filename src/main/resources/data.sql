MERGE INTO MPA_RATINGS (rating_id, name)
    values (1, 'G');
MERGE INTO MPA_RATINGS (rating_id, name)
    values (2, 'PG');
MERGE INTO MPA_RATINGS (rating_id, name)
    values (3, 'PG-13');
MERGE INTO MPA_RATINGS (rating_id, name)
    values (4, 'R');
MERGE INTO MPA_RATINGS (rating_id, name)
    values (5, 'NC-17');

MERGE INTO GENRES (genre_id, name)
    values (1, 'Комедия');
MERGE INTO GENRES (genre_id, name)
    values (2, 'Драма');
MERGE INTO GENRES (genre_id, name)
    values (3, 'Мультфильм');
MERGE INTO GENRES (genre_id, name)
    values (4, 'Триллер');
MERGE INTO GENRES (genre_id, name)
    values (5, 'Документальный');
MERGE INTO GENRES (genre_id, name)
    values (6, 'Боевик');