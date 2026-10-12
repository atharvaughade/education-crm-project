/* ==========================================================
   SMART PROGRAMMING CRM
   SIDEBAR JAVASCRIPT
========================================================== */

document.addEventListener("DOMContentLoaded", function() {

    /* ======================================================
       SELECT ELEMENTS
    ====================================================== */

    const sidebar = document.querySelector(".sidebar");
    const mainContent = document.querySelector(".main-content");
    const sidebarToggle = document.getElementById("sidebarToggle");

    const submenus = document.querySelectorAll(".submenu");

    /* ======================================================
       SIDEBAR COLLAPSE
    ====================================================== */

    if (sidebarToggle) {

        sidebarToggle.addEventListener("click", function() {

            sidebar.classList.toggle("collapsed");

            mainContent.classList.toggle("expanded");

            // Close every submenu while sidebar is collapsed

            if (sidebar.classList.contains("collapsed")) {

                submenus.forEach(function(submenu) {

                    submenu.classList.remove("active");

                });

            }

        });

    }

    /* ======================================================
       SUBMENU TOGGLE
    ====================================================== */

    submenus.forEach(function(submenu) {

        const button = submenu.querySelector(".submenu-btn");

        button.addEventListener("click", function() {

            // Don't open submenu if sidebar is collapsed

            if (sidebar.classList.contains("collapsed")) {

                return;

            }

            // Close all other submenus

            submenus.forEach(function(item) {

                if (item !== submenu) {

                    item.classList.remove("active");

                }

            });

            // Toggle current submenu

            submenu.classList.toggle("active");

        });

    });

    /* ======================================================
       AUTO HIGHLIGHT ACTIVE LINK (based on current URL)
       👇 NEW BLOCK - PASTE THIS HERE 👇
    ====================================================== */

    const currentPath = window.location.pathname;

    document.querySelectorAll(".sidebar-menu a").forEach(function(link) {
        const href = link.getAttribute("href");
        if (href && currentPath.endsWith(href)) {
            link.classList.add("active");
        }
    });

    /* ======================================================
       AUTO-OPEN PARENT SUBMENU (if active link is inside one)
    ====================================================== */
    const activeLink = document.querySelector(".sidebar-menu a.active");

    if (activeLink) {
        const parentSubmenu = activeLink.closest(".submenu");
        if (parentSubmenu) {
            parentSubmenu.classList.add("active");
        }
    }

});  // <-- closing of document.addEventListener