CREATE TABLE main.book
(
    isbn       varchar(13)  NOT NULL,
    title      varchar(100) NOT NULL,
    author     varchar(100) NOT NULL,
    publisher  varchar(100) NOT NULL,
    price      integer      NOT NULL,
    created_at timestamp    NOT NULL,
    updated_at timestamp    NOT NULL,
    PRIMARY KEY (isbn)
)