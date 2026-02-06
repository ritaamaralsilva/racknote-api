CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE modules (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL,
  brand TEXT NOT NULL,
  panel_width_hp INTEGER NOT NULL CHECK (panel_width_hp > 0),
  image_path TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ux_modules_brand_name ON modules (brand, name);
