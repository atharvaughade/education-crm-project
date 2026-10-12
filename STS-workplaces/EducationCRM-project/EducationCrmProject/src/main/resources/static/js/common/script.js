// Wait for the HTML document to fully load before running scripts
document.addEventListener('DOMContentLoaded', function() {
    
    // 1. Console log to verify the file is connected correctly
    console.log("✅ Education CRM Public Script Loaded Successfully!");

    // 2. Navbar Shadow on Scroll
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 50) {
                navbar.classList.add('shadow-lg');
            } else {
                navbar.classList.remove('shadow-lg');
            }
        });
    }

    // 3. Password Match Validation (Only runs if these fields exist on the page)
    const passwordInput = document.querySelector('input[name="password"]');
    const confirmPasswordInput = document.getElementById('confirmPassword');

    // If we are on the registration page, these inputs will exist
    if (passwordInput && confirmPasswordInput) {
        
        // Find the form that contains these inputs
        const form = passwordInput.closest('form');
        
        form.addEventListener('submit', function(event) {
            // Check if the values match
            if (passwordInput.value !== confirmPasswordInput.value) {
                // Stop the form from submitting to the server
                event.preventDefault(); 
                
                // Show a warning to the user
                alert("Passwords do not match! Please check and try again.");
                
                // Optional: Highlight the confirm password box in red
                confirmPasswordInput.style.borderColor = "red";
            } else {
                // Reset border to normal if they match
                confirmPasswordInput.style.borderColor = "";
            }
        });
    }
});