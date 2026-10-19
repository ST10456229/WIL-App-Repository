
namespace ChowDownCafe.Models
{
    public class SalesReportViewModel
    {
        public DateTime StartDate { get; set; }

        public DateTime EndDate { get; set; }

        public decimal TotalSales { get; set; }

        public int TotalOrders { get; set; }

        public int PendingOrders { get; set; }

        public int CompletedOrders { get; set; }

        public int CancelledOrders { get; set; }

        public int TotalItemsSold { get; set; }

        public List<DailySalesItem> DailySales { get; set; }
            = new List<DailySalesItem>();

        public List<TopSellingProductItem> TopSellingProducts { get; set; }
            = new List<TopSellingProductItem>();
    }

    public class DailySalesItem
    {
        public DateTime Date { get; set; }

        public decimal SalesTotal { get; set; }

        public int OrdersCount { get; set; }
    }

    public class TopSellingProductItem
    {
        public string ProductName { get; set; } = string.Empty;

        public int QuantitySold { get; set; }

        public decimal SalesTotal { get; set; }
    }
}
