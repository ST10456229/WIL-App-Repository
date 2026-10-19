
using Microsoft.EntityFrameworkCore;
using ChowDownCafe.Models;

namespace ChowDownCafe.Data
{
    public class ChowDownDbContext : DbContext
    {
        public ChowDownDbContext(
            DbContextOptions<ChowDownDbContext> options)
            : base(options)
        {
        }

        public DbSet<Product> Products { get; set; }

        public DbSet<Order> Orders { get; set; }

        public DbSet<OrderItem> OrderItems { get; set; }

        public DbSet<Promotion> Promotions { get; set; }
    }
}
