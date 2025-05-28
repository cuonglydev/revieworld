const copyButton = document.getElementById('copyButton');
const copyInput = document.getElementById('copyInput');

copyButton.addEventListener('click', function () {
    copyInput.select();
    copyInput.setSelectionRange(0, 99999); 

    
    document.execCommand("copy");

    
    copyButton.disabled = true;
    const originalText = copyButton.textContent;
    copyButton.textContent = "copied!";

    
    setTimeout(() => {
        copyButton.disabled = false;
        copyButton.textContent = originalText;
    }, 2000);
});