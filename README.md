# NileDotCom - Java Shopping Cart System

## Overview
NileDotCom is a **Java GUI-based e-commerce simulation**, inspired by platforms like Amazon. The application allows users to **browse an inventory, add items to a cart, apply discounts, and calculate taxes** before checkout.

This project demonstrates **Java Swing GUI, file handling (CSV as database), and object-oriented programming** principles.

---

## Features
- **Search & View Items** – Users can search for products by ID.
- **Add to Cart** – Add multiple products with quantity selection.
- **Cart Management** – View, remove, or clear cart contents.
- **Automatic Tax & Discount Calculations** – Discounts applied for bulk purchases.
- **Transaction Logging** – Checkout transactions are saved to a CSV file (`transactions.csv`).

---

## Project Structure
```
NileDotCom/
│── src/
│   ├── com/niledotcom/
│   │   ├── Main.java          # GUI & logic for shopping cart
│   │   ├── Inventory.java     # Handles product inventory
│   ├── resources/  
│   │   ├── inventory.csv      # Product inventory data
│── bin/                       # Compiled Java classes (generated by Eclipse)
│── JRE System Library/         # Java runtime dependencies
│── README.md                  # Documentation (This file)
│── .gitignore                  # Git ignored files (like .class files)
```

---

## Installation & Usage
### 1️⃣ Clone the Repository
```
git clone https://github.com/RookieCreator/NileDotCom.git
cd NileDotCom
```

### 2️⃣ Open in Eclipse
1. Open **Eclipse** → **File** → **Open Projects from File System**.
2. Select the **NileDotCom** folder and click **Finish**.

### 3️⃣ Run the Application
- Open `Main.java` and **Run → Run As → Java Application**.

---

## CSV Database (Inventory)
The `inventory.csv` file stores item details:
```
ID,Name,InStock,StockQty,Price
101,Wireless Mouse,true,20,19.99
102,Mechanical Keyboard,true,15,89.99
103,USB-C Hub,true,10,34.50
```
- **Item ID** – Used for search and cart functionality.
- **Stock Status & Quantity** – Prevents adding out-of-stock items.
- **Price** – Used for subtotal & tax calculations.

---

## Future Improvements
- Implement a real database (MySQL or Firebase)
- Add user authentication for personalized shopping
- Convert GUI to a web-based version (Spring Boot & React)

---

## License
This project is licensed under the **MIT License** – feel free to use or modify it!

