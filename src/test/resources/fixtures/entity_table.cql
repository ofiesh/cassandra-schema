CREATE TABLE keyspace_test.table_name (
    p_k int,
    p_k1 text,
    c_k text,
    c_k1 int,
    normal int,
    dec_imal decimal,
    PRIMARY KEY ((p_k, p_k1), c_k, c_k1)
)