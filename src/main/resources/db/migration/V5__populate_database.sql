-- 1. Insert grocery categories
INSERT INTO categories (name)
VALUES ('Fruits'),
       ('Vegetables'),
       ('Dairy'),
       ('Bakery'),
       ('Beverages'),
       ('Pantry Staples'),
       ('Snacks'),
       ('Frozen Foods'),
       ('Personal Care'),
       ('Household Essentials');

-- 2. Insert 10 real-world grocery products
INSERT INTO products (name, price, `description`, category_id)
VALUES
-- Fruits (category: Fruits)
('Fresh Apples (1 kg)', 180.00, 'Fresh, crisp apples for snacking and desserts.',
 (SELECT id FROM categories WHERE name = 'Fruits' LIMIT 1)),
('Bananas (1 dozen)', 60.00, 'Sweet, ripe bananas rich in potassium.',
 (SELECT id FROM categories WHERE name = 'Fruits' LIMIT 1)),
('Alphonso Mangoes (1 kg)', 250.00, 'Sweet and aromatic mangoes, ideal for eating fresh.',
 (SELECT id FROM categories WHERE name = 'Fruits' LIMIT 1)),
('Fresh Oranges (1 kg)', 100.00, 'Juicy oranges with a refreshing citrus flavor.',
 (SELECT id FROM categories WHERE name = 'Fruits' LIMIT 1)),
('Green Grapes (500 g)', 80.00, 'Seedless green grapes for snacking and fruit salads.',
 (SELECT id FROM categories WHERE name = 'Fruits' LIMIT 1)),

-- Vegetables
('Fresh Tomatoes (1 kg)', 40.00, 'Ripe tomatoes for curries, sauces, and salads.',
 (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1)),
('Potatoes (1 kg)', 35.00, 'Versatile potatoes for frying, roasting, and cooking.',
 (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1)),
('Onions (1 kg)', 45.00, 'Fresh onions for everyday cooking.',
 (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1)),
('Carrots (500 g)', 35.00, 'Crunchy carrots for salads, soups, and cooking.',
 (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1)),
('Fresh Spinach (250 g)', 25.00, 'Leafy green spinach for curries and healthy meals.',
 (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1)),

-- Dairy
('Amul Taaza Milk (1 L)', 68.00, 'Pasteurized toned milk for drinking and cooking.',
 (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1)),
('Amul Butter (100 g)', 60.00, 'Creamy butter for spreading and cooking.',
 (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1)),
('Amul Cheese Slices (200 g)', 140.00, 'Cheese slices for sandwiches and burgers.',
 (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1)),
('Plain Curd (400 g)', 40.00, 'Fresh plain curd for meals and snacks.',
 (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1)),
('Paneer (200 g)', 90.00, 'Soft cottage cheese for curries and stir-fries.',
 (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1)),

-- Bakery
('Whole Wheat Bread (400 g)', 45.00, 'Whole wheat bread for sandwiches and toast.',
 (SELECT id FROM categories WHERE name = 'Bakery' LIMIT 1)),
('Burger Buns (4 pieces)', 40.00, 'Soft burger buns for homemade burgers.',
 (SELECT id FROM categories WHERE name = 'Bakery' LIMIT 1)),
('Chocolate Muffins (2 pieces)', 70.00, 'Soft chocolate muffins for a sweet snack.',
 (SELECT id FROM categories WHERE name = 'Bakery' LIMIT 1)),
('Rusk Toast (200 g)', 55.00, 'Crispy toasted rusks for tea-time snacks.',
 (SELECT id FROM categories WHERE name = 'Bakery' LIMIT 1)),
('Plain Croissants (2 pieces)', 100.00, 'Flaky baked pastries for breakfast.',
 (SELECT id FROM categories WHERE name = 'Bakery' LIMIT 1)),

-- Beverages
('Tata Tea Premium (250 g)', 150.00, 'Black tea blend for traditional Indian tea.',
 (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1)),
('Nescafe Classic Coffee (100 g)', 320.00, 'Instant coffee for a rich coffee beverage.',
 (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1)),
('Real Orange Juice (1 L)', 110.00, 'Ready-to-drink orange fruit beverage.',
 (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1)),
('Coca-Cola (750 ml)', 40.00, 'Carbonated cola soft drink.',
 (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1)),
('Packaged Drinking Water (1 L)', 20.00, 'Packaged drinking water for hydration.',
 (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1)),

-- Pantry Staples
('Tata Salt (1 kg)', 28.00, 'Iodized salt for daily cooking.',
 (SELECT id FROM categories WHERE name = 'Pantry Staples' LIMIT 1)),
('India Gate Basmati Rice (1 kg)', 140.00, 'Long-grain basmati rice for biryani and pulao.',
 (SELECT id FROM categories WHERE name = 'Pantry Staples' LIMIT 1)),
('Aashirvaad Atta (5 kg)', 260.00, 'Whole wheat flour for making chapatis and rotis.',
 (SELECT id FROM categories WHERE name = 'Pantry Staples' LIMIT 1)),
('Toor Dal (1 kg)', 160.00, 'Split pigeon peas for everyday dal preparations.',
 (SELECT id FROM categories WHERE name = 'Pantry Staples' LIMIT 1)),
('Fortune Sunflower Oil (1 L)', 150.00, 'Refined sunflower cooking oil for everyday use.',
 (SELECT id FROM categories WHERE name = 'Pantry Staples' LIMIT 1)),

-- Snacks
('Lays Classic Salted Chips (52 g)', 20.00, 'Crispy salted potato chips for snacking.',
 (SELECT id FROM categories WHERE name = 'Snacks' LIMIT 1)),
('Kurkure Masala Munch (90 g)', 30.00, 'Crunchy, spicy corn-based snack.',
 (SELECT id FROM categories WHERE name = 'Snacks' LIMIT 1)),
('Parle-G Biscuits (250 g)', 30.00, 'Classic glucose biscuits for tea-time.',
 (SELECT id FROM categories WHERE name = 'Snacks' LIMIT 1)),
('Oreo Original Cookies (120 g)', 40.00, 'Chocolate sandwich cookies with vanilla-flavored filling.',
 (SELECT id FROM categories WHERE name = 'Snacks' LIMIT 1)),
('Haldirams Bhujia (200 g)', 60.00, 'Crispy spiced gram-flour noodle snack.',
 (SELECT id FROM categories WHERE name = 'Snacks' LIMIT 1)),

-- Frozen Foods
('McCain French Fries (420 g)', 120.00, 'Frozen potato fries for oven or air-fryer preparation.',
 (SELECT id FROM categories WHERE name = 'Frozen Foods' LIMIT 1)),
('Frozen Green Peas (500 g)', 90.00, 'Frozen green peas for curries and rice dishes.',
 (SELECT id FROM categories WHERE name = 'Frozen Foods' LIMIT 1)),
('Frozen Sweet Corn (500 g)', 100.00, 'Sweet corn kernels for soups and snacks.',
 (SELECT id FROM categories WHERE name = 'Frozen Foods' LIMIT 1)),
('Frozen Veggie Nuggets (400 g)', 160.00, 'Frozen vegetable nuggets for quick snacks.',
 (SELECT id FROM categories WHERE name = 'Frozen Foods' LIMIT 1)),
('Frozen Parathas (400 g)', 110.00, 'Frozen flatbreads for quick meals.',
 (SELECT id FROM categories WHERE name = 'Frozen Foods' LIMIT 1)),

-- Personal Care
('Dove Beauty Bathing Bar (100 g)', 65.00, 'Moisturizing bathing bar for daily cleansing.',
 (SELECT id FROM categories WHERE name = 'Personal Care' LIMIT 1)),
('Colgate Strong Teeth Toothpaste (150 g)', 95.00, 'Fluoride toothpaste for everyday oral hygiene.',
 (SELECT id FROM categories WHERE name = 'Personal Care' LIMIT 1)),
('Dettol Original Handwash (200 ml)', 85.00, 'Liquid handwash for everyday hand hygiene.',
 (SELECT id FROM categories WHERE name = 'Personal Care' LIMIT 1)),
('Head & Shoulders Shampoo (180 ml)', 180.00, 'Anti-dandruff shampoo for regular hair washing.',
 (SELECT id FROM categories WHERE name = 'Personal Care' LIMIT 1)),
('Whisper Sanitary Pads (pack)', 110.00, 'Disposable sanitary pads for menstrual hygiene.',
 (SELECT id FROM categories WHERE name = 'Personal Care' LIMIT 1)),

-- Household Essentials
('Vim Dishwash Liquid (250 ml)', 55.00, 'Dishwashing liquid for cleaning utensils.',
 (SELECT id FROM categories WHERE name = 'Household Essentials' LIMIT 1)),
('Surf Excel Detergent Powder (1 kg)', 150.00, 'Laundry detergent powder for everyday washing.',
 (SELECT id FROM categories WHERE name = 'Household Essentials' LIMIT 1)),
('Harpic Toilet Cleaner (500 ml)', 105.00, 'Liquid toilet cleaner for bathroom hygiene.',
 (SELECT id FROM categories WHERE name = 'Household Essentials' LIMIT 1)),
('Scotch-Brite Scrub Pad (pack of 3)', 40.00, 'Kitchen scrub pads for cleaning cookware and utensils.',
 (SELECT id FROM categories WHERE name = 'Household Essentials' LIMIT 1)),
('Garbage Bags (medium pack)', 75.00, 'Household garbage bags for everyday waste disposal.',
 (SELECT id FROM categories WHERE name = 'Household Essentials' LIMIT 1));
