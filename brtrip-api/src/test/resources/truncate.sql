SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE user RESTART IDENTITY;
TRUNCATE TABLE trip RESTART IDENTITY;
TRUNCATE TABLE trip_path RESTART IDENTITY;
TRUNCATE TABLE path RESTART IDENTITY;
TRUNCATE TABLE path_place RESTART IDENTITY;
TRUNCATE TABLE place RESTART IDENTITY;
TRUNCATE TABLE place_category RESTART IDENTITY;
TRUNCATE TABLE category RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;