INSERT INTO role (name) VALUES ('USER'), ('ORGANIZER'), ('ADMIN'), ('STAFF');

INSERT INTO permission (name) VALUES
    ('event:write'), ('event:read'),
    ('booking:write'), ('booking:read'),
    ('user:manage');