DROP TABLE IF EXISTS public.product;

CREATE TABLE public.product
(
    id bigserial not null,
    name varchar(255),
    description varchar(255),
    price double precision,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.product OWNER TO devuser;