document.querySelectorAll('.toggle-password').forEach(button => {
    button.addEventListener('click', function () {
        const input = this.parentElement.querySelector('input');

        if (input.type == 'password') {
            input.type = 'text';
            this.innerHTML = '<i class="fa-solid fa-eye"></i>';
        } else {
            input.type = 'password';
            this.innerHTML = '<i class="fa-solid fa-eye-slash"></i>'
        }
    });
});
