/**
 * sidebar.js
 * Handles responsive sidebar toggling and basic client-side session protection.
 */
(function () {
    // 1. Session check & Role verification
    const userStr = localStorage.getItem('user');
    const path = window.location.pathname;

    if (!userStr && !path.includes('/auth/') && path !== '/') {
        window.location.href = '/auth/index';
        return;
    }

    if (userStr) {
        const user = JSON.parse(userStr);
        if (path.includes('/admin/') && user.role !== 'ADMIN') {
            window.location.href = '/auth/index';
            return;
        }
        if (path.includes('/faculty/') && user.role !== 'FACULTY') {
            window.location.href = '/auth/index';
            return;
        }
        if (path.includes('/student/') && user.role !== 'STUDENT') {
            window.location.href = '/auth/index';
            return;
        }

        // Dynamically inject user name into top navbar dropdown
        document.addEventListener('DOMContentLoaded', function() {
            const userNameEl = document.querySelector('.top-navbar .dropdown .fw-medium');
            if (userNameEl) {
                userNameEl.textContent = user.fullName;
            }
            const userImgEl = document.querySelector('.top-navbar .dropdown img');
            if (userImgEl) {
                if (user.role === 'STUDENT' || user.role === 'FACULTY') {
                    userImgEl.src = `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 128 128"><rect width="100%" height="100%" fill="%23e9ecef"/><path d="M64 14a26 26 0 1 0 26 26 26 26 0 0 0-26-26zm0 56c-20.07 0-38.3 11-47.16 28.5a4 4 0 0 0 3.54 5.5h87.24a4 4 0 0 0 3.54-5.5C102.3 81 84.07 70 64 70z" fill="%236c757d"/></svg>`;
                } else {
                    userImgEl.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(user.fullName)}&background=1E3A8A&color=fff`;
                }
            }
            
            // Map Logout button
            const logoutBtns = document.querySelectorAll('a[href*="/auth/index.html"], a[href*="index.html"], a[href*="/logout"], .btn-logout, a.text-danger');
            logoutBtns.forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    localStorage.removeItem('user');
                    window.location.href = '/auth/index';
                });
            });
        });
    }

    function toggleSidebar(force) {
        const sidebar = document.querySelector('.sidebar');
        if (!sidebar) return;

        if (window.innerWidth <= 992) {
            const shouldShow = force === undefined ? !sidebar.classList.contains('show') : force;
            sidebar.classList.toggle('show', shouldShow);
            sidebar.classList.remove('collapsed');
            document.body.classList.toggle('sidebar-open', shouldShow);
        } else {
            const shouldCollapse = force === undefined ? !sidebar.classList.contains('collapsed') : force;
            sidebar.classList.toggle('collapsed', shouldCollapse);
            sidebar.classList.remove('show');
            document.body.classList.remove('sidebar-open');
        }
    }

    function initSidebar() {
        // Auto highlight active sidebar link based on current path
        const currentPath = window.location.pathname.toLowerCase();
        const sidebarLinks = document.querySelectorAll('.sidebar-link');
        
        sidebarLinks.forEach(link => {
            const href = link.getAttribute('href');
            if (href) {
                const cleanHref = href.replace('.html', '').toLowerCase();
                let isMatch = false;
                
                if (currentPath.includes('/' + cleanHref)) {
                    isMatch = true;
                } else if (cleanHref === 'students' && (currentPath.includes('/students') || currentPath.includes('/student-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'subjects' && (currentPath.includes('/subjects') || currentPath.includes('/subject-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'sections' && (currentPath.includes('/sections') || currentPath.includes('/section-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'faculty' && (currentPath.includes('/faculty') || currentPath.includes('/faculty-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'curriculum' && (currentPath.includes('/curriculum') || currentPath.includes('/curriculum-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'schedule' && (currentPath.includes('/schedule') || currentPath.includes('/schedule-form'))) {
                    isMatch = true;
                } else if (cleanHref === 'dashboard' && (currentPath.endsWith('/admin') || currentPath.endsWith('/admin/'))) {
                    isMatch = true;
                }
                
                if (isMatch) {
                    sidebarLinks.forEach(l => l.classList.remove('active'));
                    link.classList.add('active');
                }
            }
        });

        const toggleButton = document.getElementById('sidebarToggle');
        if (toggleButton) {
            toggleButton.addEventListener('click', function (event) {
                event.stopPropagation();
                toggleSidebar();
            });
        }

        document.addEventListener('click', function (event) {
            if (window.innerWidth <= 992) {
                const sidebar = document.querySelector('.sidebar');
                const toggleButton = document.getElementById('sidebarToggle');
                const clickedInsideSidebar = sidebar && sidebar.contains(event.target);
                const clickedToggle = toggleButton && toggleButton.contains(event.target);

                if (!clickedInsideSidebar && !clickedToggle) {
                    sidebar.classList.remove('show');
                    document.body.classList.remove('sidebar-open');
                }
            }
        });

        window.addEventListener('resize', function () {
            const sidebar = document.querySelector('.sidebar');
            if (!sidebar) return;

            if (window.innerWidth <= 992) {
                sidebar.classList.remove('collapsed');
                document.body.classList.remove('sidebar-open');
            } else {
                sidebar.classList.remove('show');
                document.body.classList.remove('sidebar-open');
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initSidebar);
    } else {
        initSidebar();
    }

    window.toggleSidebar = toggleSidebar;
})();