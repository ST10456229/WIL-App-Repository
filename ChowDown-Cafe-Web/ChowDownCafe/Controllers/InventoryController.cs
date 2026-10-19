using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers
{
    [Authorize]
    public class InventoryController : Controller
    {
        private readonly ChowDownDbContext _context;

        public InventoryController(ChowDownDbContext context)
        {
            _context = context;
        }

        // Display all products and their stock levels
        public async Task<IActionResult> Index()
        {
            var products = await _context.Products
                .AsNoTracking()
                .OrderBy(p => p.ProductName)
                .ToListAsync();

            return View(products);
        }

        // Display the stock update form
        [HttpGet]
        public async Task<IActionResult> UpdateStock(int id)
        {
            var product = await _context.Products
                .AsNoTracking()
                .FirstOrDefaultAsync(p => p.ProductId == id);

            if (product == null)
            {
                return NotFound();
            }

            return View(product);
        }

        // Save the new stock quantity
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> UpdateStock(
            int id,
            decimal quantityInStock)
        {
            if (quantityInStock < 0)
            {
                TempData["ErrorMessage"] =
                    "Stock quantity cannot be negative.";

                return RedirectToAction(nameof(UpdateStock),
                    new { id });
            }

            var product = await _context.Products
                .FirstOrDefaultAsync(p => p.ProductId == id);

            if (product == null)
            {
                return NotFound();
            }

            product.QuantityInStock = quantityInStock;

            await _context.SaveChangesAsync();

            TempData["SuccessMessage"] =
                $"Stock updated for {product.ProductName}.";

            return RedirectToAction(nameof(Index));
        }
    }
}