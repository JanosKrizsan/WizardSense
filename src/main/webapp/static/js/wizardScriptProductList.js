let addButtons = document.querySelectorAll('.add-to-cart');
let cartButton = document.querySelector('.cart-anchor');

for (let button of addButtons) {
    button.addEventListener('click', callFly)
}

function callFly(){
    flyToElement(this.closest('.card').querySelector('.product-image'), cartButton);
    let addButton = this;
    window.setTimeout(() => clickity(addButton), 1000);
}

function clickity(buttonToCall) {
    buttonToCall.setAttribute("type", "submit");
    buttonToCall.removeEventListener("click", callFly);
    buttonToCall.click();
}

function flyToElement(flyer, flyingTo) {
    let divider = 3;
    let flyerClone = $(flyer).clone();
    $(flyerClone).css({position: 'absolute', top: $(flyer).offset().top + "px", left: $(flyer).offset().left + "px", opacity: 1, 'z-index': 1000});
    $('body').append($(flyerClone));
    let gotoX = $(flyingTo).offset().left + ($(flyingTo).width() / 2) - ($(flyer).width()/divider)/2;
    let gotoY = $(flyingTo).offset().top + ($(flyingTo).height() / 2) - ($(flyer).height()/divider)/2;

    $(flyerClone).animate({
            opacity: 0.4,
            left: gotoX,
            top: gotoY,
            width: $(flyer).width()/divider,
            height: $(flyer).height()/divider
        }, 700,
        function () {
            $(flyingTo).fadeOut('fast', function () {
                $(flyingTo).fadeIn('fast', function () {
                    $(flyerClone).fadeOut('fast', function () {
                        $(flyerClone).remove();
                    });
                });
            });
        });
}