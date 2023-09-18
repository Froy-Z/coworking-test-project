-- Создание базы данных coworking_db
-- CREATE DATABASE coworking_db;
-- CREATE SCHEMA public;

--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0 (Debian 16.0-1.pgdg120+1)
-- Dumped by pg_dump version 16.0 (Ubuntu 16.0-1.pgdg22.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: coworking; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.coworking (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.coworking OWNER TO root;

--
-- Name: coworking_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.coworking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.coworking_id_seq OWNER TO root;

--
-- Name: coworking_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.coworking_id_seq OWNED BY public.coworking.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO root;

--
-- Name: reservation; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.reservation (
    id bigint NOT NULL,
    end_booking timestamp without time zone,
    start_booking timestamp without time zone,
    room_id bigint
);


ALTER TABLE public.reservation OWNER TO root;

--
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.reservation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reservation_id_seq OWNER TO root;

--
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.reservation_id_seq OWNED BY public.reservation.id;


--
-- Name: room; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.room (
    id bigint NOT NULL,
    work_spaces integer,
    coworking_id bigint
);


ALTER TABLE public.room OWNER TO root;

--
-- Name: room_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.room_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_id_seq OWNER TO root;

--
-- Name: room_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.room_id_seq OWNED BY public.room.id;


--
-- Name: coworking id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coworking ALTER COLUMN id SET DEFAULT nextval('public.coworking_id_seq'::regclass);


--
-- Name: reservation id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reservation ALTER COLUMN id SET DEFAULT nextval('public.reservation_id_seq'::regclass);


--
-- Name: room id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.room ALTER COLUMN id SET DEFAULT nextval('public.room_id_seq'::regclass);

INSERT INTO public.coworking (id, name) VALUES
(1,	'Мозговой штурм'),
(2,	'Необычный экипаж'),
(3,	'Нулевой километр'),
(4,	'Сообщество работяг'),
(5,	'Приближённые');

INSERT INTO public.reservation (id, end_booking, start_booking, room_id) VALUES
    (1,	'2023-09-16 11:30:00',	'2023-09-16 10:00:00',	1),
    (2,	'2023-09-16 15:30:00',	'2023-09-16 14:00:00',	1),
    (3,	'2023-09-16 17:30:00',	'2023-09-16 16:00:00',	2),
    (4,	'2023-09-17 10:30:00',	'2023-09-17 09:00:00',	3),
    (5,	'2023-09-17 14:30:00',	'2023-09-17 13:00:00',	3),
    (6,	'2023-09-18 12:30:00',	'2023-09-18 11:00:00',	4),
    (7,	'2023-09-19 15:30:00',	'2023-09-19 14:00:00',	5),
    (8,	'2023-09-19 17:30:00',	'2023-09-19 16:00:00',	6),
    (9,	'2023-09-20 11:30:00',	'2023-09-20 10:00:00',	6),
    (10, '2023-09-20 15:30:00',	'2023-09-20 14:00:00',	7),
    (11, '2023-09-21 10:30:00',	'2023-09-21 09:00:00',	8),
    (12, '2023-09-21 14:30:00',	'2023-09-21 13:00:00',	9),
    (13, '2023-09-16 11:30:00',	'2023-09-16 10:00:00',	10),
    (14, '2023-09-19 15:30:00',	'2023-09-19 14:00:00',	10),
    (15, '2023-09-19 17:30:00',	'2023-09-19 16:00:00',	10),
    (16, '2023-09-20 11:30:00',	'2023-09-20 10:00:00',	11),
    (17, '2023-09-20 15:30:00',	'2023-09-20 14:00:00',	11),
    (18, '2023-09-21 10:30:00',	'2023-09-21 09:00:00',	12),
    (19, '2023-09-21 14:30:00',	'2023-09-21 13:00:00',	12),
    (20, '2023-09-16 11:30:00',	'2023-09-16 10:00:00',	12),
    (21, '2023-09-16 15:30:00',	'2023-09-16 14:00:00',	12),
    (22, '2023-09-16 17:30:00',	'2023-09-16 16:00:00',	14),
    (23, '2023-09-17 10:30:00',	'2023-09-17 09:00:00',	14),
    (24, '2023-09-17 14:30:00',	'2023-09-17 13:00:00',	16);

INSERT INTO public.room (id, work_spaces, coworking_id) VALUES
(1,	2,	1),
(2,	5,	1),
(3,	14,	1),
(4,	18,	1),
(5,	18,	2),
(6,	18,	2),
(7,	18,	2),
(8,	18,	3),
(9,	18,	3),
(10, 12, 3),
(11, 14, 3),
(12, 16, 4),
(13, 18, 4),
(14, 15, 4),
(15, 2, 5),
(16, 2,	5);

--
-- Name: coworking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.coworking_id_seq', 5, true);


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.reservation_id_seq', 24, true);


--
-- Name: room_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.room_id_seq', 16, true);


--
-- Name: coworking coworking_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coworking
    ADD CONSTRAINT coworking_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: coworking uk_7lwc3jpy1njy5qkxmgnqh2hih; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.coworking
    ADD CONSTRAINT uk_7lwc3jpy1njy5qkxmgnqh2hih UNIQUE (name);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: room fk64hvngr6ucemfdsfjn5he0g6j; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT fk64hvngr6ucemfdsfjn5he0g6j FOREIGN KEY (coworking_id) REFERENCES public.coworking(id);


--
-- Name: reservation fkm8xumi0g23038cw32oiva2ymw; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fkm8xumi0g23038cw32oiva2ymw FOREIGN KEY (room_id) REFERENCES public.room(id);


--
-- PostgreSQL database dump complete
--