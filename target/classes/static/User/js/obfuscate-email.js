document.addEventListener("DOMContentLoaded", function () {
	const emailSpans = document.querySelectorAll(".obfuscate-email");
	
	emailSpans.forEach(span => {
		const email = span.textContent.trim();
		const [localPart, domain] = email.split("@");
		
		if(localPart.length >= 3){
			const firstChar = localPart.charAt(0);
			const lastChar = localPart.charAt(localPart.length - 1);
			const maskedLength = localPart.length - 2;
			const masked =firstChar + '*'.repeat(maskedLength) + lastChar;
			span.textContent = masked + "@" + domain;
		}else{
			span.textContent = email;
		}
	})
})