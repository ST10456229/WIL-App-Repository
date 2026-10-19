using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers
{
    [Authorize]
    public class MenuController : Controller
    {
        private readonly ChowDownDbContext _context;

        public MenuController(ChowDownDbContext context)
        {
            _context = context;
        }

        // Display active products
        public async Task<IActionResult> Index()
        {
            var products = await _context.Products
                .AsNoTracking()
                .Where(p => p.IsActive)
                .ToListAsync();

            return View(products);
        }

        // Open the Add Product form
        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }

        // Save a new product to SQL Server
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(Product product)
        {
            if (!ModelState.IsValid)
            {
                return View(product);
            }

            product.CreatedAt = DateTime.UtcNow;

            _context.Products.Add(product);
            await _context.SaveChangesAsync();

            return RedirectToAction(nameof(Index));
        }
    }
}