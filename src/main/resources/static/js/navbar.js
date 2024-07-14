// navbar.js
document.addEventListener('DOMContentLoaded', function() {
    const profileIcon = document.querySelector('.profile-icon');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    profileIcon.addEventListener('click', function(event) {
        event.stopPropagation();
        dropdownMenu.classList.add('show');

        const rect = dropdownMenu.getBoundingClientRect();
        if (rect.right > window.innerWidth) {
            dropdownMenu.classList.add('left');
        } else {
            dropdownMenu.classList.remove('left');
        }

        setTimeout(function() {
            dropdownMenu.classList.remove('show');
        }, 3000);
    });

    document.addEventListener('click', function() {
        dropdownMenu.classList.remove('show');
    });
});
