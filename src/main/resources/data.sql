INSERT INTO permissions (name, description, category) VALUES
('VIEW_STUDENT_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('VIEW_TUTOR_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('VIEW_STAFF_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('VIEW_ADMIN_DASHBOARD', 'Can view dashboard', 'DASHBOARD'),
('BULK_ALLOCATION', 'Can allocate bulk', 'ALLOCATE'),
('VIEW_BLOG_LIST', 'Can view blog list', 'BLOG'),
('VIEW_ASSIGNED_STUDENTS', 'Can view assigned students', 'ASSIGNED'),
('VIEW_ALL_USERS', 'Can view all users', 'ALL'),
('CREATE_BLOG', 'Can create blog', 'BLOG'),
('VIEW_ALL_SCHEDULE', 'Can view all schedule', 'SCHEDULE'),
('VIEW_STUDENT_EMAIL', 'Can view all student email', 'SCHEDULE'),
('SCHEDULE_MEETING', 'Can schedule meeting', 'SCHEDULE'),
('VIEW_NOTIFICATION', 'Can schedule meeting', 'NOTIFICATION'),
('UPDATE_NOTIFICATION', 'Can schedule meeting', 'NOTIFICATION'),
('GET_CHAT_CONTACTS', 'Can get chat contact', 'CHAT'),
('GET_CHAT_HISTORY', 'Can get chat history', 'CHAT'),
('LIKE_BLOG_POST', 'Can like blog', 'BLOG'),
('POST_COMMENT', 'Can post a comment on blogs', 'COMMENT'),
('VIEW_ALL_COMMENT', 'Can see a comment on blogs', 'COMMENT'),
('CREATE_STUDENT', 'Can create student account', 'STUDENT'),
('GET_UNREAD_COUNT', 'Can get unread message count', 'MESSAGE'),
('MARK_AS_READ', 'Mark unread message', 'MESSAGE'),
('SUBMIT_DOCUMENT', 'Submit document', 'DOCUMENT'),
('VIEW_INDIVIDUAL_DOCUMENT', 'View all document', 'DOCUMENT'),
('SUBMIT_ASSIGNMENT', 'Submit all assignment', 'ASSIGNMENT'),
('VIEW_ALL_ASSIGNMENT', 'View all assignment', 'ASSIGNMENT'),
('VIEW_ASSIGNMENT', 'View assignment', 'ASSIGNMENT');
('VIEW_ALL_COMMENT', 'Can see a comment on blogs', 'COMMENT'),
('CREATE_STUDENT', 'Can create student account', 'STUDENT');

INSERT INTO roles (name, description) VALUES
('STUDENT', 'Student role'),
('TUTOR', 'Tutor role'),
('STAFF', 'Staff/Admin role'),
('ADMIN', 'System Administrator role');

-- STAFF PERMISSIONS
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'STAFF'
  AND p.name IN (
    'VIEW_STAFF_DASHBOARD',
    'BULK_ALLOCATION',
    'CREATE_STUDENT'
  );

-- STUDENT PERMISSIONS
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'STUDENT'
  AND p.name IN (
    'VIEW_STUDENT_DASHBOARD',
    'VIEW_BLOG_LIST',
    'VIEW_NOTIFICATION',
    'UPDATE_NOTIFICATION',
    'CREATE_BLOG',
    'VIEW_ALL_USERS',
    'GET_CHAT_CONTACTS',
    'GET_CHAT_HISTORY',
    'LIKE_BLOG_POST',
    'POST_COMMENT',
    'VIEW_ALL_COMMENT',
    'GET_UNREAD_COUNT',
    'MARK_AS_READ',
    'SUBMIT_DOCUMENT',
    'VIEW_INDIVIDUAL_DOCUMENT',
    'VIEW_ALL_ASSIGNMENT',
    'VIEW_ASSIGNMENT'
   );

-- TUTOR PERMISSIONS
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'TUTOR'
  AND p.name IN (
    'VIEW_TUTOR_DASHBOARD',
    'VIEW_BLOG_LIST',
    'VIEW_ASSIGNED_STUDENTS',
    'VIEW_ALL_SCHEDULE',
    'VIEW_STUDENT_EMAIL',
    'SCHEDULE_MEETING',
    'CREATE_BLOG',
    'GET_CHAT_CONTACTS',
    'GET_CHAT_HISTORY',
    'LIKE_BLOG_POST',
    'POST_COMMENT',
    'VIEW_ALL_COMMENT',
    'GET_UNREAD_COUNT',
    'GET_CHAT_HISTORY',
    'MARK_AS_READ',
    'GET_UNREAD_COUNT',
    'GET_CHAT_CONTACTS',
    'VIEW_ALL_USERS',
    'SUBMIT_DOCUMENT',
    'VIEW_INDIVIDUAL_DOCUMENT',
    'SUBMIT_ASSIGNMENT',
    'VIEW_ALL_ASSIGNMENT',
    'VIEW_ASSIGNMENT'
  );

-- ADMIN PERMISSIONS
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN'
  AND p.name IN (
      'VIEW_ANALYTICS',
      'VIEW_ADMIN_DASHBOARD'
      );