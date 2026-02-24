INSERT INTO permissions (name, description, category) VALUES
('VIEW_STUDENT_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('VIEW_TUTOR_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('VIEW_STAFF_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('BULK_ALLOCATION', 'Can allocate bulk', 'ALLOCATE');

INSERT INTO roles (name, description) VALUES
('STUDENT', 'Student role'),
('TUTOR', 'Tutor role'),
('STAFF', 'Staff/Admin role');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'STAFF'
   AND p.name IN (
    'VIEW_STAFF_DASHBOARD',
    'BULK_ALLOCATION'
  );

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'STUDENT'
  AND p.name IN (
    'VIEW_STUDENT_DASHBOARD'
    );

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'TUTOR'
  AND p.name IN (
    'VIEW_TUTOR_DASHBOARD'
    );