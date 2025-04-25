document.addEventListener("DOMContentLoaded", function() {
    const menuItems = document.querySelectorAll(".menu-item");
    const currentPath = window.location.pathname;

    menuItems.forEach(link => {
        if(link.getAttribute('href') === currentPath){
            link.classList.add("menu__active", "fw-semibold", "text-light");
            
        }
    })
})