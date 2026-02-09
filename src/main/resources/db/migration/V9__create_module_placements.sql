CREATE TABLE module_placements (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  case_row_id UUID NOT NULL REFERENCES case_rows(id) ON DELETE CASCADE,
  module_id UUID NOT NULL REFERENCES modules(id) ON DELETE RESTRICT,

  -- 1-based HP position from the left
  x_hp INTEGER NOT NULL CHECK (x_hp > 0),

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ix_module_placements_case_row
  ON module_placements (case_row_id);

CREATE UNIQUE INDEX ux_module_placements_row_x_module
  ON module_placements (case_row_id, x_hp, module_id);
