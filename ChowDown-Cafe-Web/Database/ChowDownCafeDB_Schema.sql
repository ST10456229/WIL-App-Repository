USE [ChowDownCafeDB]
GO
/****** Object:  Table [dbo].[Analytics]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Analytics](
	[AnalyticsId] [int] IDENTITY(1,1) NOT NULL,
	[PostId] [int] NOT NULL,
	[ViewsCount] [int] NOT NULL,
	[ClicksCount] [int] NOT NULL,
	[RecordedAt] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[AnalyticsId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[PostId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LowStockAlerts]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LowStockAlerts](
	[AlertId] [int] IDENTITY(1,1) NOT NULL,
	[ProductId] [int] NOT NULL,
	[CurrentQuantity] [decimal](10, 2) NOT NULL,
	[MinimumStockLevel] [decimal](10, 2) NOT NULL,
	[AlertDate] [datetime2](7) NOT NULL,
	[IsResolved] [bit] NOT NULL,
	[ResolvedBy] [int] NULL,
	[ResolvedAt] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[AlertId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MarketingPosts]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MarketingPosts](
	[PostId] [int] IDENTITY(1,1) NOT NULL,
	[Title] [nvarchar](150) NOT NULL,
	[Description] [nvarchar](max) NOT NULL,
	[ImageUrl] [nvarchar](500) NULL,
	[StartDate] [date] NULL,
	[EndDate] [date] NULL,
	[IsPublished] [bit] NOT NULL,
	[CreatedAt] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PostId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[OrderItems]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[OrderItems](
	[OrderItemId] [int] IDENTITY(1,1) NOT NULL,
	[OrderId] [int] NOT NULL,
	[ProductId] [int] NOT NULL,
	[Quantity] [int] NOT NULL,
	[UnitPrice] [decimal](10, 2) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[OrderItemId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Orders]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Orders](
	[OrderId] [int] IDENTITY(1,1) NOT NULL,
	[CustomerName] [nvarchar](150) NOT NULL,
	[CustomerEmail] [nvarchar](255) NULL,
	[CustomerPhone] [nvarchar](30) NULL,
	[OrderStatus] [nvarchar](20) NOT NULL,
	[OrderDate] [datetime2](7) NOT NULL,
	[Notes] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[OrderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Products]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Products](
	[ProductId] [int] IDENTITY(1,1) NOT NULL,
	[ProductName] [nvarchar](150) NOT NULL,
	[Category] [nvarchar](100) NULL,
	[Unit] [nvarchar](30) NOT NULL,
	[QuantityInStock] [decimal](10, 2) NOT NULL,
	[MinimumStockLevel] [decimal](10, 2) NOT NULL,
	[UnitPrice] [decimal](10, 2) NULL,
	[IsActive] [bit] NOT NULL,
	[CreatedAt] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ProductId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Promotions]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Promotions](
	[PromotionId] [int] IDENTITY(1,1) NOT NULL,
	[Title] [nvarchar](100) NOT NULL,
	[Description] [nvarchar](500) NOT NULL,
	[DiscountPercentage] [decimal](5, 2) NOT NULL,
	[StartDate] [date] NOT NULL,
	[EndDate] [date] NOT NULL,
	[IsActive] [bit] NOT NULL,
	[CreatedAt] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PromotionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Staff]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Staff](
	[StaffId] [int] IDENTITY(1,1) NOT NULL,
	[FirstName] [nvarchar](100) NOT NULL,
	[LastName] [nvarchar](100) NOT NULL,
	[Email] [nvarchar](255) NOT NULL,
	[PasswordHash] [nvarchar](255) NOT NULL,
	[Role] [nvarchar](30) NOT NULL,
	[IsActive] [bit] NOT NULL,
	[CreatedAt] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[StaffId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[StockTransactions]    Script Date: 2026/10/19 21:15:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[StockTransactions](
	[TransactionId] [int] IDENTITY(1,1) NOT NULL,
	[ProductId] [int] NOT NULL,
	[StaffId] [int] NOT NULL,
	[TransactionType] [nvarchar](20) NOT NULL,
	[Quantity] [decimal](10, 2) NOT NULL,
	[Notes] [nvarchar](500) NULL,
	[TransactionDate] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TransactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Analytics] ADD  DEFAULT ((0)) FOR [ViewsCount]
GO
ALTER TABLE [dbo].[Analytics] ADD  DEFAULT ((0)) FOR [ClicksCount]
GO
ALTER TABLE [dbo].[Analytics] ADD  DEFAULT (sysdatetime()) FOR [RecordedAt]
GO
ALTER TABLE [dbo].[LowStockAlerts] ADD  DEFAULT (sysdatetime()) FOR [AlertDate]
GO
ALTER TABLE [dbo].[LowStockAlerts] ADD  DEFAULT ((0)) FOR [IsResolved]
GO
ALTER TABLE [dbo].[MarketingPosts] ADD  DEFAULT ((0)) FOR [IsPublished]
GO
ALTER TABLE [dbo].[MarketingPosts] ADD  DEFAULT (sysdatetime()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Orders] ADD  DEFAULT ('Pending') FOR [OrderStatus]
GO
ALTER TABLE [dbo].[Orders] ADD  DEFAULT (sysdatetime()) FOR [OrderDate]
GO
ALTER TABLE [dbo].[Products] ADD  DEFAULT ('item') FOR [Unit]
GO
ALTER TABLE [dbo].[Products] ADD  DEFAULT ((0)) FOR [QuantityInStock]
GO
ALTER TABLE [dbo].[Products] ADD  DEFAULT ((5)) FOR [MinimumStockLevel]
GO
ALTER TABLE [dbo].[Products] ADD  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[Products] ADD  DEFAULT (sysdatetime()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Promotions] ADD  CONSTRAINT [DF_Promotions_IsActive]  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[Promotions] ADD  CONSTRAINT [DF_Promotions_CreatedAt]  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Staff] ADD  DEFAULT ('Staff') FOR [Role]
GO
ALTER TABLE [dbo].[Staff] ADD  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[Staff] ADD  DEFAULT (sysdatetime()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[StockTransactions] ADD  DEFAULT (sysdatetime()) FOR [TransactionDate]
GO
ALTER TABLE [dbo].[Analytics]  WITH CHECK ADD  CONSTRAINT [FK_Analytics_MarketingPosts] FOREIGN KEY([PostId])
REFERENCES [dbo].[MarketingPosts] ([PostId])
GO
ALTER TABLE [dbo].[Analytics] CHECK CONSTRAINT [FK_Analytics_MarketingPosts]
GO
ALTER TABLE [dbo].[LowStockAlerts]  WITH CHECK ADD  CONSTRAINT [FK_LowStockAlerts_Products] FOREIGN KEY([ProductId])
REFERENCES [dbo].[Products] ([ProductId])
GO
ALTER TABLE [dbo].[LowStockAlerts] CHECK CONSTRAINT [FK_LowStockAlerts_Products]
GO
ALTER TABLE [dbo].[LowStockAlerts]  WITH CHECK ADD  CONSTRAINT [FK_LowStockAlerts_Staff] FOREIGN KEY([ResolvedBy])
REFERENCES [dbo].[Staff] ([StaffId])
GO
ALTER TABLE [dbo].[LowStockAlerts] CHECK CONSTRAINT [FK_LowStockAlerts_Staff]
GO
ALTER TABLE [dbo].[OrderItems]  WITH CHECK ADD  CONSTRAINT [FK_OrderItems_Orders] FOREIGN KEY([OrderId])
REFERENCES [dbo].[Orders] ([OrderId])
GO
ALTER TABLE [dbo].[OrderItems] CHECK CONSTRAINT [FK_OrderItems_Orders]
GO
ALTER TABLE [dbo].[OrderItems]  WITH CHECK ADD  CONSTRAINT [FK_OrderItems_Products] FOREIGN KEY([ProductId])
REFERENCES [dbo].[Products] ([ProductId])
GO
ALTER TABLE [dbo].[OrderItems] CHECK CONSTRAINT [FK_OrderItems_Products]
GO
ALTER TABLE [dbo].[StockTransactions]  WITH CHECK ADD  CONSTRAINT [FK_StockTransactions_Products] FOREIGN KEY([ProductId])
REFERENCES [dbo].[Products] ([ProductId])
GO
ALTER TABLE [dbo].[StockTransactions] CHECK CONSTRAINT [FK_StockTransactions_Products]
GO
ALTER TABLE [dbo].[StockTransactions]  WITH CHECK ADD  CONSTRAINT [FK_StockTransactions_Staff] FOREIGN KEY([StaffId])
REFERENCES [dbo].[Staff] ([StaffId])
GO
ALTER TABLE [dbo].[StockTransactions] CHECK CONSTRAINT [FK_StockTransactions_Staff]
GO
ALTER TABLE [dbo].[Analytics]  WITH CHECK ADD  CONSTRAINT [CK_Analytics_Clicks] CHECK  (([ClicksCount]>=(0)))
GO
ALTER TABLE [dbo].[Analytics] CHECK CONSTRAINT [CK_Analytics_Clicks]
GO
ALTER TABLE [dbo].[Analytics]  WITH CHECK ADD  CONSTRAINT [CK_Analytics_Views] CHECK  (([ViewsCount]>=(0)))
GO
ALTER TABLE [dbo].[Analytics] CHECK CONSTRAINT [CK_Analytics_Views]
GO
ALTER TABLE [dbo].[OrderItems]  WITH CHECK ADD  CONSTRAINT [CK_OrderItems_Quantity] CHECK  (([Quantity]>(0)))
GO
ALTER TABLE [dbo].[OrderItems] CHECK CONSTRAINT [CK_OrderItems_Quantity]
GO
ALTER TABLE [dbo].[OrderItems]  WITH CHECK ADD  CONSTRAINT [CK_OrderItems_UnitPrice] CHECK  (([UnitPrice]>=(0)))
GO
ALTER TABLE [dbo].[OrderItems] CHECK CONSTRAINT [CK_OrderItems_UnitPrice]
GO
ALTER TABLE [dbo].[Orders]  WITH CHECK ADD  CONSTRAINT [CK_Orders_Status] CHECK  (([OrderStatus]='Cancelled' OR [OrderStatus]='Collected' OR [OrderStatus]='Ready' OR [OrderStatus]='Preparing' OR [OrderStatus]='Pending'))
GO
ALTER TABLE [dbo].[Orders] CHECK CONSTRAINT [CK_Orders_Status]
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD  CONSTRAINT [CK_Products_MinimumStock] CHECK  (([MinimumStockLevel]>=(0)))
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [CK_Products_MinimumStock]
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD  CONSTRAINT [CK_Products_Quantity] CHECK  (([QuantityInStock]>=(0)))
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [CK_Products_Quantity]
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD  CONSTRAINT [CK_Products_UnitPrice] CHECK  (([UnitPrice] IS NULL OR [UnitPrice]>=(0)))
GO
ALTER TABLE [dbo].[Products] CHECK CONSTRAINT [CK_Products_UnitPrice]
GO
ALTER TABLE [dbo].[Promotions]  WITH CHECK ADD  CONSTRAINT [CK_Promotions_DateRange] CHECK  (([EndDate]>=[StartDate]))
GO
ALTER TABLE [dbo].[Promotions] CHECK CONSTRAINT [CK_Promotions_DateRange]
GO
ALTER TABLE [dbo].[Promotions]  WITH CHECK ADD  CONSTRAINT [CK_Promotions_DiscountPercentage] CHECK  (([DiscountPercentage]>=(0) AND [DiscountPercentage]<=(100)))
GO
ALTER TABLE [dbo].[Promotions] CHECK CONSTRAINT [CK_Promotions_DiscountPercentage]
GO
ALTER TABLE [dbo].[StockTransactions]  WITH CHECK ADD  CONSTRAINT [CK_StockTransactions_Quantity] CHECK  (([Quantity]>(0)))
GO
ALTER TABLE [dbo].[StockTransactions] CHECK CONSTRAINT [CK_StockTransactions_Quantity]
GO
ALTER TABLE [dbo].[StockTransactions]  WITH CHECK ADD  CONSTRAINT [CK_StockTransactions_Type] CHECK  (([TransactionType]='Adjustment' OR [TransactionType]='Used' OR [TransactionType]='Received'))
GO
ALTER TABLE [dbo].[StockTransactions] CHECK CONSTRAINT [CK_StockTransactions_Type]
GO

