
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ChowDownCafe.Models
{
    [Table("OrderItems")]
    public class OrderItem
    {
        [Key]
        public int OrderItemId { get; set; }

        public int OrderId { get; set; }

        public int ProductId { get; set; }

        [Range(1, int.MaxValue)]
        public int Quantity { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        [Range(typeof(decimal), "0", "99999999.99")]
        public decimal UnitPrice { get; set; }

        [ForeignKey(nameof(OrderId))]
        public Order Order { get; set; } = null!;

        [ForeignKey(nameof(ProductId))]
        public Product Product { get; set; } = null!;
    }
}
