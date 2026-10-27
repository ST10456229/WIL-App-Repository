using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers.Api
{
    [ApiController]
    [Route("api/products")]
    [AllowAnonymous]
    public class ProductsApiController : ControllerBase
    {
        private readonly ChowDownDbContext _context;

        public ProductsApiController(ChowDownDbContext context)
        {
            _context = context;
        }

        // GET: api/products
        [HttpGet]
        public async Task<IActionResult> GetProducts()
        {
            var products = await _context.Products
                .AsNoTracking()
                .Where(p => p.IsActive)
                .Select(p => new
                {
                    productId = p.ProductId,
                    productName = p.ProductName,
                    category = p.Category,
                    unit = p.Unit,
                    quantityInStock = p.QuantityInStock,
                    unitPrice = p.UnitPrice ?? 0m,
                    isAvailable = p.IsActive && p.QuantityInStock > 0,
                    imageUrl = GetProductImageUrl(p.ProductName, p.Category)
                })
                .ToListAsync();

            return Ok(products);
        }

        // GET: api/products/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetProductById(int id)
        {
            var product = await _context.Products
                .AsNoTracking()
                .FirstOrDefaultAsync(p => p.ProductId == id && p.IsActive);

            if (product == null)
            {
                return NotFound(new { message = $"Product with ID {id} not found." });
            }

            return Ok(new
            {
                productId = product.ProductId,
                productName = product.ProductName,
                category = product.Category,
                unit = product.Unit,
                quantityInStock = product.QuantityInStock,
                unitPrice = product.UnitPrice ?? 0m,
                isAvailable = product.IsActive && product.QuantityInStock > 0,
                imageUrl = GetProductImageUrl(product.ProductName, product.Category)
            });
        }

        private static string GetProductImageUrl(string productName, string category)
        {
            var name = productName.ToLower();
            var cat = category.ToLower();

            if (name.Contains("burger") || cat.Contains("burger") || cat.Contains("fast food"))
                return "/images/products/BURGER.jpg";
            if (name.Contains("coffee") || name.Contains("cappuccino") || cat.Contains("beverage"))
                return "/images/products/COFFEE.jpg";
            if (name.Contains("sandwich") || name.Contains("toastie") || cat.Contains("sandwich"))
                return "/images/products/SANDWICH.jpg";
            if (name.Contains("shawarma"))
                return "/images/products/SHAWARMA.jpg";
            if (name.Contains("muffin") || cat.Contains("bakery"))
                return "/images/products/MUFFIN.jpg";

            return "/images/products/BURGER.jpg";
        }
    }
}