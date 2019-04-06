CREATE TABLE public.mrh2019_med_list
(
   med_id integer NOT NULL DEFAULT nextval('med_list_seq'),
  "PtId" integer NOT NULL,
  med_name text,
  med_brand text,
  med_dose text,
  med_route text,
  med_freq text,
  med_indication text,
  med_comments text,
  CONSTRAINT "med_id_uniq" PRIMARY KEY ("med_id"),
    CONSTRAINT pt_id_fk FOREIGN KEY ("PtId")
      REFERENCES public.mrh2019_pt ("PtId") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.mrh2019_med_list
  OWNER TO postgres;
