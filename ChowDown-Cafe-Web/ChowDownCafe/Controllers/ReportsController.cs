using System.Globalization;
using ChowDownCafe.Data;
using ChowDownCafe.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using QuestPDF.Fluent;
using QuestPDF.Helpers;
using QuestPDF.Infrastructure;

namespace ChowDownCafe.Controllers
{
    [Authorize]
    public class ReportsController : Controller
    {
        private readonly ChowDownDbContext _context;

        public ReportsController(ChowDownDbContext context)
        {
            _context = context;
        }

        public async Task<IActionResult> Sales(
            DateTime? startDate,
            DateTime? endDate)
        {
            var today = DateTime.Today;
            var start = startDate?.Date ?? today.AddDays(-6);
            var end = endDate?.Date ?? today;

            if (start > end)
            {
                ModelState.AddModelError(
                    string.Empty,
                    "The start date cannot be after the end date.");

                start = end;
            }

            var report = await BuildReport(start, end);

            return View(report);
        }

        public async Task<IActionResult> DownloadSalesPdf(
            DateTime? startDate,
            DateTime? endDate)
        {
            QuestPDF.Settings.License = LicenseType.Community;

            var today = DateTime.Today;
            var start = startDate?.Date ?? today.AddDays(-6);
            var end = endDate?.Date ?? today;

            if (start > end)
            {
                return BadRequest(
                    "The start date cannot be after the end date.");
            }

            var report = await BuildReport(start, end);

            var pdf = Document.Create(document =>
            {
                document.Page(page =>
                {
                    page.Size(PageSizes.A4);
                    page.Margin(35);
                    page.DefaultTextStyle(style => style.FontSize(10));

                    page.Header().Column(column =>
                    {
                        column.Item()
                            .Text("CHOWDOWN CAFE")
                            .FontSize(23)
                            .Bold();

                        column.Item()
                            .Text("SALES REPORT")
                            .FontSize(16)
                            .SemiBold();

                        column.Item()
                            .Text($"Reporting Period: {start:dd MMM yyyy} - {end:dd MMM yyyy}");

                        column.Item()
                            .PaddingTop(8)
                            .LineHorizontal(1);
                    });

                    page.Content().PaddingVertical(15).Column(column =>
                    {
                        column.Spacing(12);

                        column.Item()
                            .Text($"Completed Sales Revenue: {FormatMoney(report.TotalSales)}")
                            .FontSize(15)
                            .Bold();

                        column.Item()
                            .Text($"Total Orders: {report.TotalOrders}");

                        column.Item()
                            .Text($"Completed Orders: {report.CompletedOrders}");

                        column.Item()
                            .Text($"Pending Orders: {report.PendingOrders}");

                        column.Item()
                            .Text($"Cancelled Orders: {report.CancelledOrders}");

                        column.Item()
                            .Text($"Items Sold: {report.TotalItemsSold}");

                        column.Item()
                            .PaddingTop(10)
                            .Text("DAILY SALES SUMMARY")
                            .FontSize(13)
                            .Bold();

                        if (report.DailySales.Any())
                        {
                            column.Item().Table(table =>
                            {
                                table.ColumnsDefinition(columns =>
                                {
                                    columns.RelativeColumn(2);
                                    columns.RelativeColumn(1);
                                    columns.RelativeColumn(2);
                                });

                                table.Header(header =>
                                {
                                    header.Cell().Element(HeaderStyle)
                                        .Text("Date");

                                    header.Cell().Element(HeaderStyle)
                                        .Text("Orders");

                                    header.Cell().Element(HeaderStyle)
                                        .Text("Sales Total");
                                });

                                foreach (var day in report.DailySales)
                                {
                                    table.Cell().Element(CellStyle)
                                        .Text(day.Date.ToString("dd MMM yyyy"));

                                    table.Cell().Element(CellStyle)
                                        .Text(day.OrdersCount.ToString());

                                    table.Cell().Element(CellStyle)
                                        .Text(FormatMoney(day.SalesTotal));
                                }
                            });
                        }
                        else
                        {
                            column.Item()
                                .Text("No completed sales found for this period.");
                        }

                        column.Item()
                            .PaddingTop(15)
                            .Text("TOP 5 SELLING PRODUCTS")
                            .FontSize(13)
                            .Bold();

                        if (report.TopSellingProducts.Any())
                        {
                            column.Item().Table(table =>
                            {
                                table.ColumnsDefinition(columns =>
                                {
                                    columns.RelativeColumn(3);
                                    columns.RelativeColumn(1);
                                    columns.RelativeColumn(2);
                                });

                                table.Header(header =>
                                {
                                    header.Cell().Element(HeaderStyle)
                                        .Text("Product");

                                    header.Cell().Element(HeaderStyle)
                                        .Text("Quantity");

                                    header.Cell().Element(HeaderStyle)
                                        .Text("Sales Total");
                                });

                                foreach (var product in report.TopSellingProducts)
                                {
                                    table.Cell().Element(CellStyle)
                                        .Text(product.ProductName);

                                    table.Cell().Element(CellStyle)
                                        .Text(product.QuantitySold.ToString());

                                    table.Cell().Element(CellStyle)
                                        .Text(FormatMoney(product.SalesTotal));
                                }
                            });
                        }
                        else
                        {
                            column.Item()
                                .Text("No completed product sales found for this period.");
                        }
                    });

                    page.Footer().AlignCenter().Text(text =>
                    {
                        text.Span("ChowDown Cafe | Good Food. Great Moments. | Generated ");
                        text.Span(DateTime.Now.ToString("dd MMM yyyy HH:mm"));
                    });
                });
            }).GeneratePdf();

            var fileName =
                $"ChowDownCafe_Sales_Report_{start:yyyyMMdd}_{end:yyyyMMdd}.pdf";

            return File(pdf, "application/pdf", fileName);
        }

        private async Task<SalesReportViewModel> BuildReport(
            DateTime start,
            DateTime end)
        {
            var endExclusive = end.AddDays(1);

            var orders = await _context.Orders
                .AsNoTracking()
                .Include(order => order.OrderItems)
                    .ThenInclude(item => item.Product)
                .Where(order =>
                    order.OrderDate >= start &&
                    order.OrderDate < endExclusive)
                .ToListAsync();

            var completedOrders = orders
                .Where(order => order.OrderStatus == "Completed")
                .ToList();

            var report = new SalesReportViewModel
            {
                StartDate = start,
                EndDate = end,

                TotalOrders = orders.Count,

                PendingOrders = orders.Count(
                    order => order.OrderStatus == "Pending"),

                CompletedOrders = completedOrders.Count,

                CancelledOrders = orders.Count(
                    order => order.OrderStatus == "Cancelled"),

                TotalItemsSold = completedOrders
                    .SelectMany(order => order.OrderItems)
                    .Sum(item => item.Quantity),

                TotalSales = completedOrders
                    .SelectMany(order => order.OrderItems)
                    .Sum(item => item.Quantity * item.UnitPrice),

                DailySales = completedOrders
                    .GroupBy(order => order.OrderDate.Date)
                    .Select(group => new DailySalesItem
                    {
                        Date = group.Key,
                        OrdersCount = group.Count(),
                        SalesTotal = group
                            .SelectMany(order => order.OrderItems)
                            .Sum(item => item.Quantity * item.UnitPrice)
                    })
                    .OrderBy(day => day.Date)
                    .ToList()
            };

            report.TopSellingProducts = completedOrders
                .SelectMany(order => order.OrderItems)
                .GroupBy(item => new
                {
                    item.ProductId,
                    item.Product.ProductName
                })
                .Select(group => new TopSellingProductItem
                {
                    ProductName = group.Key.ProductName,
                    QuantitySold = group.Sum(item => item.Quantity),
                    SalesTotal = group.Sum(
                        item => item.Quantity * item.UnitPrice)
                })
                .OrderByDescending(product => product.QuantitySold)
                .Take(5)
                .ToList();

            return report;
        }

        private static string FormatMoney(decimal amount)
        {
            return amount.ToString(
                "C2",
                CultureInfo.GetCultureInfo("en-ZA"));
        }

        private static IContainer HeaderStyle(IContainer container)
        {
            return container
                .Background(Colors.Grey.Lighten3)
                .BorderBottom(1)
                .BorderColor(Colors.Grey.Lighten1)
                .Padding(5)
                .DefaultTextStyle(style => style.SemiBold());
        }

        private static IContainer CellStyle(IContainer container)
        {
            return container
                .BorderBottom(0.5f)
                .BorderColor(Colors.Grey.Lighten2)
                .PaddingVertical(5)
                .PaddingHorizontal(4);
        }
    }
}