let addressCards = document.querySelectorAll(".floating");

for (let card of addressCards){
    card.style.animationDelay = (Math.random() * 3).toString()+"s";
}