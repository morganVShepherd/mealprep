CREATE SCHEMA meals;

SET SEARCH_PATH  = meals;

CREATE TABLE food_type (
                                 ID INT NOT NULL,
                                 NAME VARCHAR(255) NULL,
                                 PRIMARY KEY (ID)
);

CREATE SEQUENCE food_type_seq
    AS SMALLINT
    INCREMENT 1
START 2;

INSERT INTO FOOD_TYPE VALUES (1, 'nothing');

CREATE TABLE recipe (
                              ID INT NOT NULL,
                              KCAL INT NOT NULL,
                              SERVING_SIZE INT NOT NULL,
                              RATING INT NOT NULL,
                              NOTES VARCHAR NOT NULL,
                              NAME VARCHAR NOT NULL,
                              MEAL_TYPE VARCHAR NOT NULL,
                              IN_ROTATION VARCHAR NOT NULL,
                              PRIMARY KEY (ID)
);

INSERT INTO recipe VALUES (1,0,0,0,'Not selected','Other Plan','DINNER','CONSTANT');
INSERT INTO recipe VALUES (2,0,0,0,'Not selected','Other Plan','BREAKFAST','CONSTANT');

CREATE SEQUENCE recipe_seq
    AS SMALLINT
    INCREMENT 1
START 3;

CREATE TABLE step (
                           ID INT NOT NULL,
                           STEP_NO INT NOT NULL,
                           DETAILS VARCHAR(1500) NULL,
                           RECIPE_ID INT REFERENCES recipe(ID),
                           PRIMARY KEY (ID)
);

INSERT INTO step VALUES (1,1,'No Steps',1);

CREATE SEQUENCE step_seq
    AS SMALLINT
    INCREMENT 1
START 2;


CREATE TABLE ingredient (
                                 ID INT NOT NULL,
                                 QUANTITY INT NOT NULL,
                                 METRIC VARCHAR NOT NULL,
                                 INGREDIENT_TYPE VARCHAR NOT NULL,
                                 FOOD_TYPE_ID INT REFERENCES food_type(ID),
                                 RECIPE_ID INT REFERENCES recipe(ID),
                                 PRIMARY KEY (ID)
);

INSERT INTO ingredient VALUES (1,0,'BLANK','OTHER',1,1);

CREATE SEQUENCE ingredient_seq
    AS SMALLINT
    INCREMENT 1
START 2;



CREATE TABLE meal_prep (
                              ID INT NOT NULL,
                              MON_DINNER_ID INT NOT NULL,
                              TUES_DINNER_ID INT NOT NULL,
                              WED_DINNER_ID INT NOT NULL,
                              THURS_DINNER_ID INT NOT NULL,
                              FRI_DINNER_ID INT NOT NULL,
                              SAT_DINNER_ID INT NOT NULL,
                              SUN_DINNER_ID INT NOT NULL,
                              SAT_BREAK_ID INT NOT NULL,
                              SUN_BREAK_ID INT NOT NULL,
                              PRIMARY KEY (ID)
);

CREATE SEQUENCE meal_prep_seq
    AS SMALLINT
    INCREMENT 1
START 1;