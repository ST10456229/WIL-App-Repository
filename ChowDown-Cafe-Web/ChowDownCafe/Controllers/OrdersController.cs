using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers
{
    [Authorize]
    public class OrdersController : Controller
    {
        private readonly ChowDownDbContext _context;

        public OrdersController(ChowDownDbContext context)
        {
            _context = context;
        }

        // Display existing orders
        public async Task<IActionResult> Index()
        {
            var orders = await _context.Orders
                .AsNoTracking()
                .Include(o => o.OrderItems)
                .OrderByDescending(o => o.OrderDate)
                .ToListAsync();

            return View(orders);
        }

        // Open the Create Order form
        [HttpGet]
        public async Task<IActionResult> Create()
        {
            await LoadProductsAsync();
            return View(new CreateOrderViewModel());
        }

        // Create an order and deduct stock
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(
            CreateOrderViewModel model)
        {
            if (!ModelState.IsValid)
            {
                await LoadProductsAsync();
                return View(model);
            }

            await using var transaction =
                await _context.Database.BeginTransactionAsync();

            try
            {
                var product = await _context.Products
                    .FirstOrDefaultAsync(p =>
                        p.ProductId == model.ProductId &&
                        p.IsActive);

                if (product == null)
                {
                    await transaction.RollbackAsync();

                    ModelState.AddModelError(
                        nameof(model.ProductId),
                        "Please select an active product.");

                    await LoadProductsAsync();
                    return View(model);
                }

                if (!product.UnitPrice.HasValue ||
                    product.UnitPrice.Value < 0)
                {
                    await transaction.RollbackAsync();

                    ModelState.AddModelError(
                        nameof(model.ProductId),
                        "This product does not have a valid price.");

                    await LoadProductsAsync();
                    return View(model);
                }

                if (model.Quantity <= 0)
                {
                    await transaction.RollbackAsync();

                    ModelState.AddModelError(
                        nameof(model.Quantity),
                        "Quantity must be greater than zero.");

                    await LoadProductsAsync();
                    return View(model);
                }

                if (model.Quantity > product.QuantityInStock)
                {
                    var availableStock = product.QuantityInStock;
                    var unit = product.Unit;

                    await transaction.RollbackAsync();

                    ModelState.AddModelError(
                        nameof(model.Quantity),
                        $"Only {availableStock} {unit} are currently in stock.");

                    await LoadProductsAsync();
                    return View(model);
                }

                var order = new Order
                {
                    CustomerName = model.CustomerName.Trim(),
                    CustomerEmail = string.IsNullOrWhiteSpace(
                        model.CustomerEmail)
                        ? null : model.CustomerEmail.Trim(),
                    CustomerPhone = string.IsNullOrWhiteSpace(
                        model.CustomerPhone)
                        ? null : model.CustomerPhone.Trim(),
                    Notes = string.IsNullOrWhiteSpace(model.Notes)
                        ? null : model.Notes.Trim(),
                    OrderStatus = "Pending",
                    OrderDate = DateTime.Now
                };

                order.OrderItems.Add(new OrderItem
                {
                    ProductId = product.ProductId,
                    Quantity = model.Quantity,
                    UnitPrice = product.UnitPrice.Value
                });

                product.QuantityInStock -= model.Quantity;

                _context.Orders.Add(order);

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                TempData["SuccessMessage"] =
                    $"Order #{order.OrderId} was created successfully. " +
                    $"{model.Quantity} {product.Unit} of " +
                    $"{product.ProductName} deducted from inventory.";

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                await transaction.RollbackAsync();

                ModelState.AddModelError(
                    string.Empty,
                    "The order could not be saved. Please try again.");

                await LoadProductsAsync();
                return View(model);
            }
        }

        // Update order status and restore stock on cancellation
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> UpdateStatus(
            int orderId,
            string orderStatus)
        {
            var allowedStatuses = new[]
            {
                "Pending",
                "Preparing",
                "Ready",
                "Completed",
                "Cancelled"
            };

            if (string.IsNullOrWhiteSpace(orderStatus) ||
                !allowedStatuses.Contains(orderStatus))
            {
                TempData["ErrorMessage"] = "Invalid order status.";
                return RedirectToAction(nameof(Index));
            }

            await using var transaction =
                await _context.Database.BeginTransactionAsync();

            try
            {
                var order = await _context.Orders
                    .Include(o => o.OrderItems)
                    .FirstOrDefaultAsync(o => o.OrderId == orderId);

                if (order == null)
                {
                    await transaction.RollbackAsync();
                    TempData["ErrorMessage"] = "Order not found.";
                    return RedirectToAction(nameof(Index));
                }

                // Do not restore stock again if already cancelled.
                if (order.OrderStatus == "Cancelled")
                {
                    await transaction.RollbackAsync();

                    TempData["ErrorMessage"] =
                        "This order is already cancelled. " +
                        "Create a new order if the customer wants to order again.";

                    return RedirectToAction(nameof(Index));
                }

                // Restore each ordered product exactly once when cancelled.
                if (orderStatus == "Cancelled")
                {
                    foreach (var item in order.OrderItems)
                    {
                        var product = await _context.Products
                            .FirstOrDefaultAsync(
                                p => p.ProductId == item.ProductId);

                        if (product == null)
                        {
                            await transaction.RollbackAsync();

                            TempData["ErrorMessage"] =
                                "A product from this order could not be found. " +
                                "The cancellation was not saved.";

                            return RedirectToAction(nameof(Index));
                        }

                        product.QuantityInStock += item.Quantity;
                    }
                }

                order.OrderStatus = orderStatus;

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                TempData["SuccessMessage"] =
                    orderStatus == "Cancelled"
                        ? $"Order #{order.OrderId} cancelled. " +
                          "Its stock has been returned to inventory."
                        : $"Order #{order.OrderId} status updated to {orderStatus}.";

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                await transaction.RollbackAsync();

                TempData["ErrorMessage"] =
                    "The order status could not be updated. Please try again.";

                return RedirectToAction(nameof(Index));
            }
        }

        // Load active products for the Create Order form
        private async Task LoadProductsAsync()
        {
            var products = await _context.Products
                .AsNoTracking()
                .Where(p => p.IsActive && p.UnitPrice.HasValue)
                .OrderBy(p => p.ProductName)
                .ToListAsync();

            ViewBag.Products = new SelectList(
                products,
                nameof(Product.ProductId),
                nameof(Product.ProductName));
        }
    }
}