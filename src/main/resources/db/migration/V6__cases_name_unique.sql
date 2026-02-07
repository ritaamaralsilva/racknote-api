ALTER TABLE cases
ADD CONSTRAINT ux_cases_name UNIQUE (name);
