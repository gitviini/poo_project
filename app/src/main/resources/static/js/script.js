document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('focus', (event) => {
            event.target.dataset.placeholder = event.target.placeholder || '';
            event.target.placeholder = '';
        });
        input.addEventListener('blur', (event) => {
            event.target.placeholder = event.target.dataset.placeholder || '';
        });
    });
});