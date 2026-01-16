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
  'usuario',
  'usuario',
  'usuario@usuario.com',
  '$2a$12$xux6JGAD9VpQjrU9oxb4BOtNaLFCBzWzEWK2xzBpdoG8OE84Omxpa',
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



INSERT INTO category (name)
VALUES
  ('Audífonos'),
  ('Celulares'),
  ('Teclados')
ON CONFLICT (name) DO NOTHING;

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
  'usuario',
  'usuario',
  'usuario@usuario.com',
  '$2a$12$xux6JGAD9VpQjrU9oxb4BOtNaLFCBzWzEWK2xzBpdoG8OE84Omxpa',
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

WITH nuevos_productos (name, description, price, stock, category_name) AS (
  VALUES
    -- 🎧 AUDÍFONOS (9)
    ('Audífonos Sony WH-1000XM5', 'Cancelación de ruido líder en la industria', 1499.00, 12, 'Audífonos'),
    ('Audífonos Bose QuietComfort 45', 'Comodidad y sonido nítido', 1399.00, 8, 'Audífonos'),
    ('Audífonos JBL Tune 230NC', 'Bajos potentes y larga batería', 399.00, 20, 'Audífonos'),
    ('Sennheiser Momentum 4', 'Sonido premium para audiófilos', 1599.00, 5, 'Audífonos'),
    ('AirPods Max', 'Diseño elegante e integración Apple', 2299.00, 10, 'Audífonos'),
    ('Beats Studio Pro', 'Estilo icónico y sonido envolvente', 1199.00, 15, 'Audífonos'),
    ('Logitech G733 Lightspeed', 'Audífonos gamer con luces RGB', 799.00, 25, 'Audífonos'),
    ('Razer BlackShark V2 Pro', 'Ideales para e-sports competitivos', 899.00, 14, 'Audífonos'),
    ('Audio-Technica ATH-M50xBT2', 'Monitoreo de estudio inalámbrico', 950.00, 7, 'Audífonos'),

    -- 📱 CELULARES (9)
    ('Samsung Galaxy S23 Ultra', 'Pantalla AMOLED y S-Pen incluido', 4299.00, 6, 'Celulares'),
    ('iPhone 14 Pro', 'Chip A16 Bionic y Dynamic Island', 4899.00, 5, 'Celulares'),
    ('Xiaomi Redmi Note 12 Pro', 'Carga ultra rápida y buena cámara', 1299.00, 15, 'Celulares'),
    ('Google Pixel 7 Pro', 'La mejor experiencia de Android puro', 3500.00, 4, 'Celulares'),
    ('Motorola Edge 40', 'Diseño delgado y resistente al agua', 1899.00, 12, 'Celulares'),
    ('Poco F5 Pro', 'Máximo rendimiento para juegos', 2100.00, 20, 'Celulares'),
    ('Nothing Phone (2)', 'Interfaz Glyph y diseño transparente', 2800.00, 8, 'Celulares'),
    ('ZTE Axon 50 Ultra', 'Potencia y elegancia en un solo equipo', 1600.00, 10, 'Celulares'),
    ('OnePlus 11', 'Cámara Hasselblad y carga de 100W', 3100.00, 9, 'Celulares'),

    -- ⌨️ TECLADOS (9)
    ('Logitech G Pro X', 'Switches intercambiables para pro-gamers', 699.00, 15, 'Teclados'),
    ('Redragon Kumara K552', 'Mecánico compacto y muy resistente', 249.00, 30, 'Teclados'),
    ('Keychron K6', 'Inalámbrico perfecto para productividad', 459.00, 12, 'Teclados'),
    ('Razer Huntsman Mini', 'Formato 60% con switches ópticos', 550.00, 18, 'Teclados'),
    ('Corsair K100 RGB', 'El teclado más avanzado de Corsair', 1200.00, 5, 'Teclados'),
    ('SteelSeries Apex Pro', 'Sensibilidad de teclas ajustable', 950.00, 7, 'Teclados'),
    ('HyperX Alloy Origins', 'Cuerpo de aluminio y luces vibrantes', 420.00, 22, 'Teclados'),
    ('Anne Pro 2', 'Favorito de los entusiastas de teclados', 380.00, 10, 'Teclados'),
    ('Ducky One 3', 'Calidad de construcción excepcional', 650.00, 6, 'Teclados')
)
INSERT INTO products (name, description, price, stock, image_url, created_at, is_active, category_id)
SELECT
  np.name,
  np.description,
  np.price,
  np.stock,
  'https://eatkxdfnzqpqmtljjxye.supabase.co/storage/v1/object/public/previews/ia_image_cellphone.png',
  NOW(),
  TRUE,
  c.id
FROM nuevos_productos np
JOIN category c ON c.name = np.category_name
ON CONFLICT (name) DO NOTHING;