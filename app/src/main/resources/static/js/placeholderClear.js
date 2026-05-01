// este script limpa o placeholder dos campos de input quando eles recebem foco, e restaura o placeholder
//quando eles perdem o foco. Isso é útil para evitar que o texto do placeholder interfira na digitação do usuário.

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