// Elevate Luxe — Main JS

document.addEventListener('DOMContentLoaded', () => {

    // Auto-dismiss alerts after 4s
    document.querySelectorAll('.alert.alert-success, .alert.alert-info').forEach(el => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(el);
            if (bsAlert) bsAlert.close();
        }, 4000);
    });

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });

    // Navbar scroll effect
    const nav = document.querySelector('.luxury-nav');
    if (nav) {
        window.addEventListener('scroll', () => {
            nav.style.boxShadow = window.scrollY > 40
                ? '0 2px 20px rgba(26,24,20,0.10)' : '';
        });
    }

    // Add to cart button feedback
    document.querySelectorAll('form[action*="/cart/add"]').forEach(form => {
        form.addEventListener('submit', function() {
            const btn = this.querySelector('.btn-add-cart, .btn-luxury');
            if (btn) {
                const orig = btn.innerHTML;
                btn.innerHTML = '<i class="fas fa-check me-1"></i> Added!';
                btn.disabled = true;
                setTimeout(() => { btn.innerHTML = orig; btn.disabled = false; }, 1500);
            }
        });
    });

});
