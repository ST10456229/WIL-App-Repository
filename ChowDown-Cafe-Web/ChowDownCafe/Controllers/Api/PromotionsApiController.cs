using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers.Api
{
    [ApiController]
    [Route("api/promotions")]
    [AllowAnonymous]
    public class PromotionsApiController : ControllerBase
    {
        private readonly ChowDownDbContext _context;

        public PromotionsApiController(ChowDownDbContext context)
        {
            _context = context;
        }

        // GET: api/promotions
        [HttpGet]
        public async Task<IActionResult> GetActivePromotions()
        {
            var today = DateTime.Today;
            var promotions = await _context.Promotions
                .AsNoTracking()
                .Where(p => p.IsActive && p.StartDate.Date <= today && p.EndDate.Date >= today)
                .OrderByDescending(p => p.CreatedAt)
                .Select(p => new
                {
                    promotionId = p.PromotionId,
                    title = p.Title,
                    description = p.Description,
                    discountPercentage = p.DiscountPercentage,
                    startDate = p.StartDate.ToString("yyyy-MM-dd"),
                    endDate = p.EndDate.ToString("yyyy-MM-dd"),
                    isActive = p.IsActive
                })
                .ToListAsync();

            return Ok(promotions);
        }
    }
}