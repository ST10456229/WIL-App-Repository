
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ChowDownCafe.Models
{
    [Table("Products")]
    public class Product
    {
        [Key]
        public int ProductId { get; set; }

        [Required]
        [StringLength(100)]
        public string ProductName { get; set; } = string.Empty;

        [StringLength(50)]
        public string Category { get; set; } = string.Empty;

        [StringLength(30)]
        public string Unit { get; set; } = "item";

        public decimal QuantityInStock { get; set; }

        public decimal MinimumStockLevel { get; set; }

        public decimal? UnitPrice { get; set; }

        public bool IsActive { get; set; }

        public DateTime CreatedAt { get; set; }
    }
}
