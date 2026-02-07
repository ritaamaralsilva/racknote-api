INSERT INTO cases (name)
VALUES ('Rita Silva - Main Case')
ON CONFLICT (name) DO NOTHING;

INSERT INTO case_rows (case_id, row_index, width_hp, height_u)
SELECT c.id, r.row_index, r.width_hp, r.height_u
FROM cases c
CROSS JOIN (VALUES
  (1, 104, 3),
  (2, 104, 1),
  (3, 104, 3)
) AS r(row_index, width_hp, height_u)
WHERE c.name = 'Rita Silva - Main Case'
ON CONFLICT DO NOTHING;
