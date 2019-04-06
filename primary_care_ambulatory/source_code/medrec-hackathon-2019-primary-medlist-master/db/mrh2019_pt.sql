-- Table: public.mrh2019_pt

-- DROP TABLE public.mrh2019_pt;

CREATE TABLE public.mrh2019_pt
(
  "PtId" integer NOT NULL,
  last_name text NOT NULL,
  first_name text NOT NULL,
  dob date,
  create_date date,
  CONSTRAINT "PtId_uniq" PRIMARY KEY ("PtId")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.mrh2019_pt
  OWNER TO postgres;
