# ChowDown Café: Digital Ecosystem

<div align="center">

  <img width="280" alt="ChowDown Cafe Logo" src="https://github.com/user-attachments/assets/f6048073-162c-4aee-8848-6cb1f312fdbe" />
</div>


![ChowDown Banner](https://img.shields.io/badge/ChowDown-Caf%C3%A9-red?style=for-the-badge&logo=buymeacoffee&logoColor=white)
![PHP](https://img.shields.io/badge/PHP-8.x-777BB4?style=for-the-badge&logo=php&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Backend-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

## 📖 Overview
ChowDown Café is an independent coffee shop located in the Durban CBD. This project is a complete **Digital Ecosystem** designed to modernize the café's operations, increase revenue, and eliminate manual paper-based processes. 

Built as a Work Integrated Learning (WIL) project for Rosebank College, this system bridges the gap between a Web-based Admin Portal for staff and a Customer-facing Mobile App for ordering and engagement.

## ✨ Key Features

### 🖥️ Web Admin Portal (For Owner & Staff)
*   **Real-Time Inventory Management:** Track stock levels, record daily usage, and eliminate manual paper sheets.
*   **Automated Low-Stock Alerts:** Receive instant dashboard notifications when ingredients fall below the threshold.
*   **Order Management:** View and manage incoming customer orders from the mobile app in real-time.
*   **Marketing Control:** Create and publish promotional posts that instantly trigger push notifications to all registered app users.
*   **Analytics & Reporting:** Generate R01 (Stock Usage) and R02 (Low Stock) reports, exportable to CSV.

### 📱 Customer Mobile App (For Customers)
*   **Full Ordering System:** Browse the digital menu, add items to a cart, and place orders directly from the app.
*   **Exclusive Promotions:** View a feed of daily specials and café updates.
*   **Push Notifications:** Receive instant alerts via Firebase Cloud Messaging (FCM) when new promotions drop.
*   **One-Tap Contact:** Directly call the café, send an email, or get Google Maps directions.

---

## 🏗️ System Architecture
The system utilizes a robust **Three-Tier Architecture**:
1.  **Presentation Layer:** Web Browser (Admin) & Android App (Customer).
2.  **Application Logic Layer:** PHP RESTful API handling business logic and Firebase Admin SDK for push notifications.
3.  **Data Layer:** 
    *   **MySQL (XAMPP):** Stores structured business data (Users, Products, Orders, Stock Transactions, Posts).
    *   **Firebase Firestore:** Stores real-time customer data (Profiles, FCM tokens, Notification logs).

---

## 🛠️ Tech Stack

**Frontend (Web):**
*   HTML5, CSS3, JavaScript
*   Chart.js (for data visualization)

**Frontend (Mobile):**
*   Kotlin
*   Android Studio (Min SDK 21)

**Backend:**
*   PHP 8.x
*   Firebase Cloud Messaging (FCM)

**Database:**
*   MySQL (via XAMPP)
*   Firebase Firestore (NoSQL)

---

## 🚀 Getting Started (Local Development)

### Prerequisites
*   [XAMPP](https://www.apachefriends.org/index.html) (or any Apache/MySQL/PHP stack)
*   [Android Studio](https://developer.android.com/studio)
*   [Git](https://git-scm.com/)
*   A [Firebase Console Account](https://console.firebase.google.com/)

### 1. Backend Setup (PHP & MySQL)
1. Clone the repository:
   ```bash
   git clone https://github.com/ST10456229/WIL-Repository.git
   
2. Move the backend folder to your XAMPP htdocs directory.

3. Start Apache and MySQL in the XAMPP Control Panel.

4. Open http://localhost/phpmyadmin in your browser.

5. Create a new database named chowdown_db.

6. Import the chowdown_db.sql file (located in the /database folder) into your new database.

7. Update the database credentials in backend/config/db_connect.php if necessary.

### 2. Frontend Setup (Android App)
Clone the Android repository:

```bash
git clone https://github.com/ST10458040/WIL-App-Repository.git
Open the project in Android Studio.

Connect the app to your local backend by updating the BASE_URL in RetrofitClient.kt (use your PC's IPv4 address instead of localhost).

Add your google-services.json file (from your Firebase Console) into the /app directory.

Sync Gradle and run the app on an emulator or physical device.
```

### 🔗 Links
Figma Prototype: [Insert Figma URL Here]

Web Backend Repo: https://github.com/ST10456229/WIL-Repository.git

Android App Repo: https://github.com/ST10458040/WIL-App-Repository.git

### 👥 The Team (Group Amajongo)
Wandile Shobede – Chairperson & Mobile Support (ST10457381)

Siphamandla Tshabalala – Secretary & Project Manager (ST10441479)

Sphumelele Khuzwayo – Organizer & Database Designer (ST10456229)

Mlondolozi Shezi – Treasurer & Backend Lead (ST10458040)

Institution: Rosebank College, Durban
Module: XISD6329 Work Integrated Learning
Year: 2026   
