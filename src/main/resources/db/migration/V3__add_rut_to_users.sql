-- Agregar columna RUT a la tabla users
ALTER TABLE users
    ADD COLUMN rut VARCHAR(20);

-- Opcional: dejar algún valor temporal para no tener nulls
UPDATE users
SET rut = CONCAT('TEMP-', id)
WHERE rut IS NULL;
