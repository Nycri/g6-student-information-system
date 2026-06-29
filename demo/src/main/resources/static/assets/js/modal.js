/**
 * modal.js
 * Handles global confirmation dialogs for Form Submissions and Logouts.
 */

$(document).ready(function() {
    
    // ==========================================
    // 1. Generic Form Submission Interceptor
    // ==========================================
    let pendingForm = null;

    // Listen for any form that has the class 'require-confirmation'
    $('.require-confirmation').on('submit', function(e) {
        // Did it pass HTML5 validation?
        if (this.checkValidity()) {
            e.preventDefault();           // Pause the actual form submission
            pendingForm = this;           // Store the form in memory
            $('#confirmSubmitModal').modal('show'); // Pop up the warning
        }
    });

    // If they click "Yes, Save Record" inside the modal
    $('#btnExecuteSubmit').on('click', function() {
        if (pendingForm) {
            // Change button state to show loading
            let originalText = $(this).html();
            $(this).html('<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Saving...');
            $(this).addClass('disabled');

            // Optional: Simulate a brief network delay so the user sees the spinner, then submit
            setTimeout(function() {
                pendingForm.submit(); // Execute the actual POST request to Spring Boot
            }, 500); 
        }
    });

    // ==========================================
    // 2. Logout Trigger 
    // ==========================================
    // Attach this to any link or button with the class 'btn-logout'
    $('.btn-logout').on('click', function(e) {
        e.preventDefault(); 
        $('#logoutModal').modal('show');
    });

});