$(document).ready(function() {
    // Attach click event listener to the remove button/link
    $(".styled-button").click(function(event) {
        event.preventDefault(); // Prevent the default action of the link/button

        var form = $(this).closest('form');
        var cartItemId = form.find('input[name="cartItemId"]').val(); // Get the cart item ID

        // Send an AJAX request to remove the item from the cart
        $.ajax({
            type: "POST",
            url: "/remove-from-cart",
            data: {
                cartItemId: cartItemId
            },
            success: function(response) {
                // If successful, refresh the page
                location.reload();
            },
            error: function(xhr, status, error) {
                // Handle error
                console.error(error);
            }
        });
    });
});


function addToCart(button) {
    var row = button.closest('div'); // Get the parent div of the button
    var form = row.querySelector('form'); // Get the form element within the row

    if (!form) {
        console.error('Form element not found.');
        return; // Exit the function if form element is not found
    }

    var quantity = parseInt(row.querySelector('input[name="quantity"]').value);
    var maxQuantity = parseInt(row.querySelector('input[name="maxQuantity"]').value);
    console.log("Quantity:", quantity); // Print quantity to console
    console.log("Max Quantity:", maxQuantity); // Print maxQuantity to console
    if (isNaN(quantity) || quantity <= 0) {
        alert("Please enter a valid quantity greater than zero.");
        return; // Exit the function if quantity is invalid
    }

    if (quantity > maxQuantity) {
        alert("Quantity exceeds available stock.");
        return; // Exit the function if quantity exceeds maxQuantity
    }

    // If validation passes, submit the form
    form.submit();
}