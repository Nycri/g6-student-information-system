/**
 * auth.js
 * Handles logic for the authentication module (Role Detection, Password Toggle, and Vanilla JS submission).
 */

document.addEventListener('DOMContentLoaded', function() {
    
    // ==========================================
    // 1. Role Detection & Dynamic Title Injection
    // ==========================================
    
    // Function to grab URL parameters
    function getUrlParameter(sParam) {
        var sPageURL = window.location.search.substring(1),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
            }
        }
        return false;
    }

    // Determine role and set title
    const role = getUrlParameter('role');
    const loginTitle = document.getElementById('loginTitle');

    if (loginTitle) {
        if (role === 'admin') {
            loginTitle.textContent = 'Administrator Login';
        } else if (role === 'faculty') {
            loginTitle.textContent = 'Faculty Login';
        } else if (role === 'student') {
            loginTitle.textContent = 'Student Login';
        } else {
            loginTitle.textContent = 'System Login';
        }
    }

    // ==========================================
    // 2. Password Visibility Toggle
    // ==========================================
    
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (togglePassword && passwordInput && toggleIcon) {
        togglePassword.addEventListener('click', function() {
            // Check current input type
            if (passwordInput.getAttribute('type') === 'password') {
                // Switch to text
                passwordInput.setAttribute('type', 'text');
                toggleIcon.classList.remove('bi-eye-slash');
                toggleIcon.classList.add('bi-eye');
            } else {
                // Switch to password
                passwordInput.setAttribute('type', 'password');
                toggleIcon.classList.remove('bi-eye');
                toggleIcon.classList.add('bi-eye-slash');
            }
        });
    }

    // ==========================================
    // 3. Form Submission (Vanilla JS AJAX fetch)
    // ==========================================
    
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault(); 
            
            const usernameInput = document.getElementById('username');
            const passwordValInput = document.getElementById('password');

            if (!usernameInput || !passwordValInput) return;

            const username = usernameInput.value;
            const password = passwordValInput.value;

            fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: username, password: password })
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return response.json().then(err => { 
                        throw new Error(err.message || 'Login failed'); 
                    }).catch(() => {
                        throw new Error('Invalid username or password.');
                    });
                }
            })
            .then(data => {
                if (data.status === 'success') {
                    // Cache session in localStorage
                    localStorage.setItem('user', JSON.stringify(data.user));
                    
                    // Route to correct dashboard
                    let targetDashboard = '';
                    const userRole = data.user.role.toLowerCase();
                    if (userRole === 'admin') {
                        targetDashboard = '/admin/dashboard';
                    } else if (userRole === 'faculty') {
                        targetDashboard = '/faculty/dashboard';
                    } else if (userRole === 'student') {
                        targetDashboard = '/student/dashboard';
                    } else {
                        targetDashboard = '/';
                    }
                    
                    window.location.href = targetDashboard;
                } else {
                    alert('Login failed: ' + data.message);
                }
            })
            .catch(error => {
                alert(error.message || 'Invalid username or password.');
            });
        });
    }
});