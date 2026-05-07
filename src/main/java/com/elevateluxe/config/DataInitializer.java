package com.elevateluxe.config;

import com.elevateluxe.entity.*;
import com.elevateluxe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() > 0) return;

        // Roles
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
        Role userRole = roleRepository.save(new Role("ROLE_USER"));

        // Admin user
        User admin = new User();
        admin.setEmail("admin@elevateluxe.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Alexandra");
        admin.setLastName("Beaumont");
        admin.setPhone("+40 721 000 001");
        admin.setAddress("Calea Victoriei 12");
        admin.setCity("Bucharest");
        admin.setCountry("Romania");
        admin.getRoles().add(adminRole);
        admin.getRoles().add(userRole);
        userRepository.save(admin);

        // Regular users
        User user1 = new User();
        user1.setEmail("sophie@example.com");
        user1.setPassword(passwordEncoder.encode("user123"));
        user1.setFirstName("Sophie");
        user1.setLastName("Laurent");
        user1.setPhone("+40 722 111 222");
        user1.setAddress("Strada Florilor 5");
        user1.setCity("Cluj-Napoca");
        user1.setCountry("Romania");
        user1.getRoles().add(userRole);
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("marcus@example.com");
        user2.setPassword(passwordEncoder.encode("user123"));
        user2.setFirstName("Marcus");
        user2.setLastName("Chen");
        user2.setPhone("+40 733 444 555");
        user2.setAddress("Bulevardul Eroilor 22");
        user2.setCity("Timișoara");
        user2.setCountry("Romania");
        user2.getRoles().add(userRole);
        userRepository.save(user2);

        User user3 = new User();
        user3.setEmail("isabella@example.com");
        user3.setPassword(passwordEncoder.encode("user123"));
        user3.setFirstName("Isabella");
        user3.setLastName("Rossi");
        user3.setPhone("+40 744 666 777");
        user3.setAddress("Piața Unirii 8");
        user3.setCity("Iași");
        user3.setCountry("Romania");
        user3.getRoles().add(userRole);
        userRepository.save(user3);

        // Categories
        Category aromatherapy = new Category();
        aromatherapy.setName("Aromatherapy & Fragrance");
        aromatherapy.setDescription("Artisan perfumes, essential oil diffusers, and hand-poured soy candles");
        aromatherapy.setIconClass("fas fa-spa");
        categoryRepository.save(aromatherapy);

        Category bathBody = new Category();
        bathBody.setName("Bath & Body");
        bathBody.setDescription("Triple-milled botanical soaps, organic bath salts, and hydrating body serums");
        bathBody.setIconClass("fas fa-bath");
        categoryRepository.save(bathBody);

        Category homeBedding = new Category();
        homeBedding.setName("Home & Bedding");
        homeBedding.setDescription("Egyptian cotton bed sheets, silk pillowcases, and plush Turkish cotton towels");
        homeBedding.setIconClass("fas fa-home");
        categoryRepository.save(homeBedding);

        Category morningRituals = new Category();
        morningRituals.setName("Morning Rituals");
        morningRituals.setDescription("Premium matcha sets, artisanal coffee blends, and high-end ceramic mugs");
        morningRituals.setIconClass("fas fa-coffee");
        categoryRepository.save(morningRituals);

        // Products - Aromatherapy
        createProduct("Maison Noir Eau de Parfum", "A sophisticated blend of black orchid, vetiver, and aged sandalwood. Bottled in hand-blown glass with 24k gold accents. Long-lasting 12+ hour wear.", new BigDecimal("185.00"), 25, "Maison Noir", aromatherapy, true, "/images/products/perfume1.jpg");
        createProduct("Rose Oud Artisan Perfume", "An intoxicating fusion of Bulgarian rose absolute and rare Cambodian oud. Presented in a hand-painted ceramic flacon.", new BigDecimal("220.00"), 18, "Orient Luxe", aromatherapy, true, "/images/products/perfume2.jpg");
        createProduct("Nebula Essential Oil Diffuser", "Ultrasonic cold-mist diffuser in brushed brass and walnut. Covers 500 sq ft, whisper-quiet operation, 8-hour continuous mist.", new BigDecimal("145.00"), 30, "Aura Home", aromatherapy, false, "/images/products/diffuser1.jpg");
        createProduct("Solstice Soy Candle Collection", "Set of 3 hand-poured soy candles: Bergamot & Thyme, Cedarwood & Vanilla, Jasmine & White Musk. 60-hour burn time each.", new BigDecimal("89.00"), 40, "Lumière", aromatherapy, true, "/images/products/candles1.jpg");
        createProduct("Ambre Nocturne Perfume Oil", "A pure perfume oil in amber and musk. Apply to pulse points for an intimate, skin-warming fragrance that evolves throughout the day.", new BigDecimal("95.00"), 22, "Essence Rare", aromatherapy, false, "/images/products/perfume3.jpg");

        // Products - Bath & Body
        createProduct("Botanical Triple-Milled Soap Set", "Set of 6 handcrafted soaps: Lavender & Honey, Rose & Shea, Charcoal & Tea Tree, Oatmeal & Vanilla, Citrus & Mint, Argan & Almond.", new BigDecimal("65.00"), 50, "Botanica Pure", bathBody, true, "/images/products/soap1.jpg");
        createProduct("Dead Sea Organic Bath Salts", "Premium Dead Sea mineral salts infused with organic rose petals, jasmine essential oil, and vitamin E. 1kg luxury jar.", new BigDecimal("55.00"), 35, "Terra Salina", bathBody, false, "/images/products/bathsalts1.jpg");
        createProduct("24K Gold Body Serum", "Ultra-rich body serum with 24k gold micro-particles, hyaluronic acid, and jojoba oil. Leaves skin luminous and deeply hydrated.", new BigDecimal("125.00"), 20, "Aurum Beauty", bathBody, true, "/images/products/serum1.jpg");
        createProduct("Velvet Rose Body Oil", "Lightweight yet deeply nourishing body oil with Bulgarian rose, sea buckthorn, and vitamin C ester. Absorbs instantly without grease.", new BigDecimal("78.00"), 28, "Botanica Pure", bathBody, false, "/images/products/bodyoil1.jpg");
        createProduct("Exfoliating Coffee Body Scrub", "Organic Colombian coffee grounds with coconut oil, brown sugar, and vanilla. Buffs away dead skin for a soft, glowing finish.", new BigDecimal("42.00"), 45, "Raw Origins", bathBody, false, "/images/products/scrub1.jpg");

        // Products - Home & Bedding
        createProduct("400TC Egyptian Cotton Sheet Set", "Hotel-quality 400 thread count Egyptian long-staple cotton. Set includes flat sheet, fitted sheet, and 2 pillowcases. Available in ivory, white, sage.", new BigDecimal("185.00"), 20, "Linara Home", homeBedding, true, "/images/products/sheets1.jpg");
        createProduct("Mulberry Silk Pillowcase Set", "Grade 6A 22-momme mulberry silk pillowcases. Reduces hair breakage and sleep lines. Hidden zipper closure. Standard/Queen size, set of 2.", new BigDecimal("120.00"), 30, "Serene Sleep", homeBedding, true, "/images/products/pillowcase1.jpg");
        createProduct("Turkish Cotton Bath Towel Set", "Spa-grade Turkish cotton with double-loop construction. Ultra-absorbent and quick-drying. Set of 6: 2 bath, 2 hand, 2 face.", new BigDecimal("155.00"), 25, "Aegean Linen", homeBedding, false, "/images/products/towels1.jpg");
        createProduct("Linen Duvet Cover Set", "100% French linen duvet cover with matching pillowcases. Pre-washed for softness. Temperate year-round. King size.", new BigDecimal("245.00"), 15, "Linara Home", homeBedding, false, "/images/products/duvet1.jpg");
        createProduct("Marble & Brass Soap Dispenser Set", "Set of 3 hand-carved marble dispensers with brass pump. Perfect for bathroom vanity styling. 350ml capacity each.", new BigDecimal("135.00"), 18, "Marmo Studio", homeBedding, false, "/images/products/dispenser1.jpg");

        // Products - Morning Rituals
        createProduct("Ceremonial Grade Matcha Set", "Authentic Japanese ceremonial matcha with bamboo whisk (chasen), ceramic chawan, and bamboo scoop. 30g premium matcha included.", new BigDecimal("95.00"), 28, "Zen Garden", morningRituals, true, "/images/products/matcha1.jpg");
        createProduct("Single-Origin Cold Brew Kit", "Hand-roasted single-origin Ethiopian Yirgacheffe beans with cold brew mason jar system. Produces 1L smooth, low-acid cold brew.", new BigDecimal("72.00"), 32, "Altitude Roasters", morningRituals, true, "/images/products/coffee1.jpg");
        createProduct("Handmade Ceramic Mug Set", "Set of 4 wheel-thrown ceramic mugs in matte glazes. Each piece unique. Dishwasher safe, microwave safe. 350ml capacity.", new BigDecimal("88.00"), 22, "Clay & Co", morningRituals, false, "/images/products/mugs1.jpg");
        createProduct("Pour-Over Coffee Chemex Kit", "Borosilicate glass Chemex with natural wood collar and 100 pre-folded filters. Brews 6 cups of crystal-clear, rich coffee.", new BigDecimal("115.00"), 20, "Altitude Roasters", morningRituals, false, "/images/products/chemex1.jpg");
        createProduct("Artisanal Tea Collection Box", "Curated selection of 12 premium loose-leaf teas from Darjeeling, Ceylon, and Yunnan. Elegant wooden presentation box with glass canisters.", new BigDecimal("68.00"), 35, "Leaf & Root", morningRituals, true, "/images/products/tea1.jpg");

        // Sample orders for user1
        createSampleOrder(user1, List.of(
            new long[]{1, 1}, new long[]{6, 2}
        ), "EL-SAMPLE-0001", "Strada Florilor 5", "Cluj-Napoca", "Romania", "400001", Order.OrderStatus.DELIVERED);

        createSampleOrder(user1, List.of(
            new long[]{11, 1}
        ), "EL-SAMPLE-0002", "Strada Florilor 5", "Cluj-Napoca", "Romania", "400001", Order.OrderStatus.SHIPPED);

        // Sample orders for user2
        createSampleOrder(user2, List.of(
            new long[]{16, 1}, new long[]{18, 1}
        ), "EL-SAMPLE-0003", "Bulevardul Eroilor 22", "Timișoara", "Romania", "300001", Order.OrderStatus.CONFIRMED);
    }

    private void createProduct(String name, String description, BigDecimal price, int stock,
                                String brand, Category category, boolean featured, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStockQuantity(stock);
        p.setBrand(brand);
        p.setCategory(category);
        p.setFeatured(featured);
        p.setActive(true);
        p.setImageUrl(imageUrl);
        productRepository.save(p);
    }

    private void createSampleOrder(User user, List<long[]> productQtyPairs, String orderNum,
                                   String address, String city, String country, String postal,
                                   Order.OrderStatus status) {
        Order order = new Order();
        order.setOrderNumber(orderNum);
        order.setUser(user);
        order.setShippingAddress(address);
        order.setShippingCity(city);
        order.setShippingCountry(country);
        order.setShippingPostalCode(postal);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now().minusDays((long)(Math.random() * 30)));

        BigDecimal total = BigDecimal.ZERO;
        List<Product> products = productRepository.findAll();

        for (long[] pq : productQtyPairs) {
            int idx = (int)pq[0] - 1;
            if (idx < products.size()) {
                Product product = products.get(idx);
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity((int)pq[1]);
                item.setUnitPrice(product.getPrice());
                order.getItems().add(item);
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(pq[1])));
            }
        }
        order.setTotalAmount(total);
        orderRepository.save(order);
    }
}
