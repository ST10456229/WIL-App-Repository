
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ChowDownCafe.Models
{
    [Table("Orders")]
    public class Order
    {
        [Key]
        public int OrderId { get; set; }

        [Required]
        [StringLength(150)]
        public string CustomerName { get; set; } = string.Empty;

        [StringLength(255)]
        public string? CustomerEmail { get; set; }

        [StringLength(30)]
        public string? CustomerPhone { get; set; }

        [Required]
        [StringLength(20)]
        public string OrderStatus { get; set; } = "Pending";

        public DateTime OrderDate { get; set; }

        [StringLength(500)]
        public string? Notes { get; set; }

        public ICollection<OrderItem> OrderItems { get; set; }
            = new List<OrderItem>();
    }
}
