// Theme Toggle Script
(function () {
    // Get saved theme or default to dark
    const savedTheme = localStorage.getItem('theme') || 'dark';
    const savedSidebar = localStorage.getItem('sidebarCollapsed') || 'false';

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

    // Toggle sidebar function
    window.toggleSidebar = function () {
        const dashboard = document.querySelector('.dashboard');
        const isCollapsed = dashboard.classList.toggle('sidebar-collapsed');
        localStorage.setItem('sidebarCollapsed', isCollapsed);
        updateSidebarIcon(isCollapsed);
    };

    // Update sidebar toggle icon
    window.updateSidebarIcon = function (isCollapsed) {
        const btn = document.getElementById('sidebarToggle');
        if (btn) {
            btn.textContent = isCollapsed ? '☰' : '✕';
            btn.title = isCollapsed ? 'Expand Sidebar' : 'Collapse Sidebar';
        }
    };

    // Initialize on DOM ready
    document.addEventListener('DOMContentLoaded', function () {
        updateThemeIcon(savedTheme);

        // Apply saved sidebar state
        if (savedSidebar === 'true') {
            const dashboard = document.querySelector('.dashboard');
            if (dashboard) {
                dashboard.classList.add('sidebar-collapsed');
            }
        }
        updateSidebarIcon(savedSidebar === 'true');
    });
})();
