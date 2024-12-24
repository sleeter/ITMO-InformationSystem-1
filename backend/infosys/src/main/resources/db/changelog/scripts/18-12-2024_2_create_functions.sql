CREATE OR REPLACE FUNCTION delete_flats_by_transport(p_transport VARCHAR)
RETURNS VOID AS $$
BEGIN
DELETE FROM Flats WHERE transport = p_transport;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION count_flats_by_house_id_less_than(p_house_id BIGINT)
RETURNS BIGINT AS $$
DECLARE
flat_count BIGINT;
BEGIN
SELECT COUNT(*) INTO flat_count
FROM Flats
WHERE house_id < p_house_id;
RETURN flat_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_unique_transport_values()
RETURNS TABLE(transport VARCHAR) AS $$
BEGIN
RETURN QUERY
SELECT DISTINCT transport
FROM Flats;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_cheaper_flat(p_id1 BIGINT, p_id2 BIGINT)
RETURNS TABLE(flat_id BIGINT, price FLOAT) AS $$
BEGIN
RETURN QUERY
SELECT f.id, f.price
FROM Flats f
WHERE f.id = CASE
                 WHEN f.price < (SELECT price FROM Flats WHERE id = p_id2) THEN p_id1
                 ELSE p_id2
    END;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_flats_sorted_by_metro_time()
RETURNS TABLE(id BIGINT, name VARCHAR, time_to_metro_on_foot FLOAT) AS $$
BEGIN
RETURN QUERY
SELECT id, name, time_to_metro_on_foot
FROM Flats
ORDER BY time_to_metro_on_foot ASC;
END;
$$ LANGUAGE plpgsql;
