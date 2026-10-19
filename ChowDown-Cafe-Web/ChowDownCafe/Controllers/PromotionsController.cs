using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChowDownCafe.Controllers
{
    [Authorize]
    public class PromotionsController : Controller
    {
        private readonly ChowDownDbContext _context;

        public PromotionsController(ChowDownDbContext context)
        {
            _context = context;
        }

        // Display all promotions
        public async Task<IActionResult> Index()
        {
            var promotions = await _context.Promotions
                .AsNoTracking()
                .OrderByDescending(p => p.CreatedAt)
                .ToListAsync();

            return View(promotions);
        }

        // Display the create form
        public IActionResult Create()
        {
            return View(new Promotion
            {
                StartDate = DateTime.Today,
                EndDate = DateTime.Today.AddDays(7),
                IsActive = true
            });
        }

        // Save a new promotion
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(Promotion promotion)
        {
            if (promotion.EndDate.Date < promotion.StartDate.Date)
            {
                ModelState.AddModelError(
                    nameof(promotion.EndDate),
                    "The end date cannot be before the start date.");
            }

            if (!ModelState.IsValid)
            {
                return View(promotion);
            }

            promotion.CreatedAt = DateTime.Now;

            _context.Promotions.Add(promotion);
            await _context.SaveChangesAsync();

            TempData["SuccessMessage"] = "Promotion created successfully!";

            return RedirectToAction(nameof(Index));
        }

        // Display the edit form
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var promotion = await _context.Promotions
                .FindAsync(id.Value);

            if (promotion == null)
            {
                return NotFound();
            }

            return View(promotion);
        }

        // Save promotion changes
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(
            int id,
            Promotion promotion)
        {
            if (id != promotion.PromotionId)
            {
                return NotFound();
            }

            if (promotion.EndDate.Date < promotion.StartDate.Date)
            {
                ModelState.AddModelError(
                    nameof(promotion.EndDate),
                    "The end date cannot be before the start date.");
            }

            if (!ModelState.IsValid)
            {
                return View(promotion);
            }

            var existingPromotion = await _context.Promotions
                .FindAsync(id);

            if (existingPromotion == null)
            {
                return NotFound();
            }

            existingPromotion.Title = promotion.Title;
            existingPromotion.Description = promotion.Description;
            existingPromotion.DiscountPercentage =
                promotion.DiscountPercentage;
            existingPromotion.StartDate = promotion.StartDate.Date;
            existingPromotion.EndDate = promotion.EndDate.Date;
            existingPromotion.IsActive = promotion.IsActive;

            await _context.SaveChangesAsync();

            TempData["SuccessMessage"] = "Promotion updated successfully!";

            return RedirectToAction(nameof(Index));
        }

        // Activate or deactivate a promotion
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> ToggleActive(int id)
        {
            var promotion = await _context.Promotions.FindAsync(id);

            if (promotion == null)
            {
                return NotFound();
            }

            promotion.IsActive = !promotion.IsActive;

            await _context.SaveChangesAsync();

            TempData["SuccessMessage"] = promotion.IsActive
                ? "Promotion activated successfully!"
                : "Promotion deactivated successfully!";

            return RedirectToAction(nameof(Index));
        }
    }
}