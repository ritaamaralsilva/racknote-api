ALTER TABLE cases
  ADD COLUMN width_hp integer;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM cases c
    LEFT JOIN case_rows r ON r.case_id = c.id
    GROUP BY c.id
    HAVING COUNT(r.id) = 0
  ) THEN
    RAISE EXCEPTION 'Invariant violated: at least one case has 0 rows. Aborting migration.';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM (
      SELECT case_id
      FROM case_rows
      GROUP BY case_id
      HAVING MIN(width_hp) <> MAX(width_hp)
    ) bad
  ) THEN
    RAISE EXCEPTION 'Invariant violated: inconsistent row widths detected. Aborting migration.';
  END IF;
END $$;

UPDATE cases c
SET width_hp = sub.width_hp
FROM (
  SELECT case_id, MIN(width_hp) AS width_hp
  FROM case_rows
  GROUP BY case_id
) sub
WHERE c.id = sub.case_id;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM cases WHERE width_hp IS NULL) THEN
    RAISE EXCEPTION 'Backfill failed: at least one case did not receive width_hp. Aborting migration.';
  END IF;
END $$;

ALTER TABLE cases
  ALTER COLUMN width_hp SET NOT NULL;

ALTER TABLE case_rows
  DROP COLUMN width_hp;