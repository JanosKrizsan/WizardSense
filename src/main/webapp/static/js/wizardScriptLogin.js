let loginButton = document.querySelector(".login-button");

loginButton.addEventListener("click", animateLogin);

function animateLogin() {
    let loginForm = this.closest("form");
    loginForm.closest("div").style.backgroundImage = "url(\"/static/img/login_circle.gif\")";
    window.setTimeout(() => loginForm.submit(), 1500);
}
