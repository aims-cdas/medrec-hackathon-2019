-- Table: public.mrh2019_pt_sources

-- DROP TABLE public.mrh2019_pt_sources;

CREATE TABLE public.mrh2019_pt_sources
(
  source_id integer NOT NULL,
  "PtId" integer,
  ehr text NOT NULL,
  id text NOT NULL,
  base_url text NOT NULL,
  CONSTRAINT pt_source_id_pk PRIMARY KEY (source_id),
  CONSTRAINT pt_id_fk FOREIGN KEY ("PtId")
      REFERENCES public.mrh2019_pt ("PtId") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.mrh2019_pt_sources
  OWNER TO postgres;
