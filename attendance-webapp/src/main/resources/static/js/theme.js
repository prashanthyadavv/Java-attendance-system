// Theme Toggle Script
(function () {
    // Get saved theme or default to dark
    const savedTheme = localStorage.getItem('theme') || 'dark';

    // Apply theme immediately to prevent flash
    document.documentElement.setAttribute('data-theme', savedTheme);

    // Toggle theme function
    window.toggleTheme = function () {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';

        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);

        // Update button icon
        updateThemeIcon(newTheme);
    };

    // Update theme button icon
    window.updateThemeIcon = function (theme) {
        const btn = document.getElementById('themeToggle');
        if (btn) {
            btn.textContent = theme === 'dark' ? '☀️' : '🌙';
            btn.title = theme === 'dark' ? 'Switch to Light Mode' : 'Switch to Dark Mode';
        }
    };

    // Initialize on DOM ready
    document.addEventListener('DOMContentLoaded', function () {
        updateThemeIcon(savedTheme);
    });
})();
