/**
 * app.js
 * Global JavaScript helpers for the SIS Frontend.
 */
$(document).ready(function() {
    
    // ==========================================
    // Global Table Search Filter
    // ==========================================
    // Attach this to any input with the class 'table-search'
    // It will filter the table specified in its 'data-target' attribute
    
    $('.table-search').on('keyup', function() {
        var value = $(this).val().toLowerCase();
        var targetTable = $(this).attr('data-target');
        
        $(targetTable + " tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

});