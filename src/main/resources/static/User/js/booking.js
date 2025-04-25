document.addEventListener('DOMContentLoaded', () => {
    const basePrice = 0.5;
    const priceDisplays = document.querySelectorAll('[data-price-display]');
    const numberInput = document.getElementById('numberInput');
    const turnNumber = document.getElementById('turnNumber');

    function updatePrice() {
        const quantity = parseInt(numberInput.value, 10) || 1;
        const totalPrice = basePrice * quantity;
        priceDisplays.forEach(display => {
            display.textContent = totalPrice.toLocaleString('en-US', {
                style: 'currency',
                currency: 'USD'
            });
        })
    }

    function updateTurnNumberMax() {
        const max = parseInt(numberInput.value, 10) || 1;
        turnNumber.max = max;
        if (parseInt(turnNumber.value, 10) > max) {
            turnNumber.value = max;
        }
    }

    numberInput.addEventListener('input', () => {
        updatePrice();
        updateTurnNumberMax();
    });

    turnNumber.addEventListener('input', () => {
        const max = parseInt(turnNumber.max, 10);
        const value = parseInt(turnNumber.value, 10);
        if (value > max) {
            turnNumber.value = max;
        }
    });

    // Gọi hàm lúc load lần đầu
    updatePrice();
    updateTurnNumberMax();
});


const form = document.querySelector('form');
const confirmBtn = document.getElementById('confirmBtn');

function checkFormValidity() {
    if (form.checkValidity()) {
        confirmBtn.disabled = false;
    } else {
        confirmBtn.disabled = true;
    }
}

// Gán listener cho mọi input, select, textarea trong form
const inputs = form.querySelectorAll('input, select, textarea');

inputs.forEach(input => {
    input.addEventListener('input', checkFormValidity);
    input.addEventListener('change', checkFormValidity);
});

// Kiểm tra khi trang load lại (trường hợp form có dữ liệu từ trước)
window.addEventListener('load', checkFormValidity);
