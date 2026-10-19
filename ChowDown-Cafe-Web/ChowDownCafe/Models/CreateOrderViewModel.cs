
using System.ComponentModel.DataAnnotations;

namespace ChowDownCafe.Models
{
    public class CreateOrderViewModel
    {
        [Required]
        [StringLength(150)]
        [Display(Name = "Customer Name")]
        public string CustomerName { get; set; } = string.Empty;

        [EmailAddress]
        [StringLength(255)]
        [Display(Name = "Customer Email")]
        public string? CustomerEmail { get; set; }

        [Phone]
        [StringLength(30)]
        [Display(Name = "Customer Phone")]
        public string? CustomerPhone { get; set; }

        [StringLength(500)]
        public string? Notes { get; set; }

        [Range(1, int.MaxValue)]
        [Display(Name = "Product")]
        public int ProductId { get; set; }

        [Range(1, 10000)]
        public int Quantity { get; set; } = 1;
    }
}
