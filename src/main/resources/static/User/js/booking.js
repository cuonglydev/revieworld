document.addEventListener('DOMContentLoaded', () => {
    const priceDisplays = document.querySelectorAll('[data-price-display]');
    const numberInput = document.getElementById('numberInput');
    const turnNumber = document.getElementById('turnNumber');
    const languageSelect = document.getElementById('languageSelect');
    const basePriceInput = document.getElementById('basePrice');
	
	const percentDiscount = document.getElementById('percentDiscount');
	const discountAmounts = document.querySelectorAll('[data-discount-amount]');
	const totalPayments = document.querySelectorAll('[data-total-payment]');

    function updatePrice() {
        const basePrice = parseFloat(basePriceInput.value) || 0;
        const quantity = parseInt(numberInput.value, 10) || 1;
        const totalPrice = basePrice * quantity;
		
        priceDisplays.forEach(display => {
            display.textContent = totalPrice.toLocaleString('en-US', {
                style: 'currency',
                currency: 'USD'
            });
        });
		
		const percent = parseFloat(percentDiscount.value) || 0;
		const discountAmount = (totalPrice / 100) * percent;
		discountAmounts.forEach(display => {
			display.textContent = discountAmount.toLocaleString('en-US', {
				style: 'currency',
				currency: 'USD'
			});
		})
		
		const totalPayment = totalPrice - discountAmount;
		totalPayments.forEach(display => {
			display.textContent = totalPayment.toLocaleString('en-US', {
				style: 'currency',
				currency: 'USD'
			})
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

    languageSelect.addEventListener('change', () => {
        const selectedOption = languageSelect.options[languageSelect.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        basePriceInput.value = price || '';
        updatePrice(); // cập nhật ngay khi chọn
    });

    // Gọi hàm lúc load ban đầu
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
