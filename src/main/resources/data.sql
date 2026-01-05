-- =========================
-- USERS
-- =========================
INSERT INTO users (first_name, last_name, email, password, role)
VALUES
(
  'osen',
  'osen',
  'osen@osen.com',
  '$2a$12$GpKmjCGpWEm8TrOuUFvrDOEXWVyYLvHBJbCYu6k7xzOeOZxyNeL56',
  'ROLE_USER'
),
(
  'admin',
  'admin',
  'admin@admin.com',
  '$2a$12$ZtiZcpx.HubSRA7UCNSIzeLOmD.jJBGjxrVW2SOjKErdkDicyoRz6',
  'ROLE_ADMIN'
)
ON CONFLICT (email) DO NOTHING;


-- =========================
-- CATEGORIES
-- =========================
INSERT INTO category (name)
VALUES
  ('Audífonos'),
  ('Celulares'),
  ('Teclados')
ON CONFLICT (name) DO NOTHING;


-- =========================
-- PRODUCTS - AUDÍFONOS
-- =========================
INSERT INTO products (
  name,
  description,
  price,
  stock,
  image_url,
  created_at,
  is_active,
  category_id
)
VALUES
(
  'Audífonos Sony WH-1000XM5',
  'Audífonos inalámbricos con cancelación de ruido de Sony',
  1499.00,
  12,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Audífonos')
),
(
  'Audífonos Bose QuietComfort 45',
  'Audífonos Bose con excelente cancelación de ruido y comodidad',
  1399.00,
  8,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Audífonos')
),
(
  'Audífonos Inalámbricos JBL Tune 230NC',
  'Audífonos JBL inalámbricos con sonido potente y ligero',
  399.00,
  20,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Audífonos')
);


-- =========================
-- PRODUCTS - CELULARES
-- =========================
INSERT INTO products (
  name,
  description,
  price,
  stock,
  image_url,
  created_at,
  is_active,
  category_id
)
VALUES
(
  'Samsung Galaxy S23',
  'Smartphone Samsung con cámara avanzada y pantalla AMOLED',
  3299.00,
  10,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Celulares')
),
(
  'iPhone 14',
  'iPhone 14 con chip A15 y cámara dual avanzada',
  3899.00,
  7,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Celulares')
),
(
  'Xiaomi Redmi Note 12',
  'Smartphone Xiaomi económico con buena batería y pantalla AMOLED',
  1099.00,
  18,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Celulares')
);


-- =========================
-- PRODUCTS - TECLADOS
-- =========================
INSERT INTO products (
  name,
  description,
  price,
  stock,
  image_url,
  created_at,
  is_active,
  category_id
)
VALUES
(
  'Teclado Mecánico Logitech G Pro X',
  'Teclado mecánico para gamers con switches personalizables',
  699.00,
  15,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Teclados')
),
(
  'Teclado Mecánico Redragon Kumara K552',
  'Teclado mecánico económico con retroiluminación LED',
  249.00,
  30,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Teclados')
),
(
  'Teclado Inalámbrico Keychron K6',
  'Teclado compacto e inalámbrico con compatibilidad Mac/Windows',
  459.00,
  12,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  (SELECT id FROM category WHERE name = 'Teclados')
);
