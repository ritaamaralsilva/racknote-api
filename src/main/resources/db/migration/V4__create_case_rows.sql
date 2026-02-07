CREATE TABLE case_rows (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  case_id UUID NOT NULL REFERENCES cases(id) ON DELETE CASCADE,
  row_index INTEGER NOT NULL,
  width_hp INTEGER NOT NULL,
  height_u INTEGER NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT ux_case_rows_case_row UNIQUE (case_id, row_index),
  CONSTRAINT ck_case_rows_row_index CHECK (row_index > 0),
  CONSTRAINT ck_case_rows_width_hp CHECK (width_hp > 0),
  CONSTRAINT ck_case_rows_height_u CHECK (height_u > 0)
);
