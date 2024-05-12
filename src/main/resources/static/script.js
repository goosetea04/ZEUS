$(document).ready(function() {
    // Attach click event listener to the remove button/link
    $(".remove-from-cart-btn").click(function(event) {
        event.preventDefault(); // Prevent the default action of the link/button

        var cartItemId = $(this).data("cart-item-id"); // Get the cart item ID

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
                // Optionally, display an error message to the user
            }
        });
    });
});