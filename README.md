# Ledger Application 

## OverView
The Accounting Ledger Application is a command‑line interface (CLI) Java application that allows users to track financial transactions such as deposits and payments.
Users can view transaction history, filter transactions, generate reports, and persist data using CSV files
---
## Features

- Add deposits (positive amounts)
- Record payments (stored as negative amounts)
- View a complete ledger of all transactions
- Filter transactions:
- Deposits only 
  - Payments only 
  - Generate reports:
- Add deposits (positive amounts)
- Record payments (stored as negative amounts)
- View a complete ledger of all transactions
- Filter transactions:
    - Deposits only
    - Payments only
- Generate reports:
    - Month to Date
    - Previous Month
    - Year to Date
    - Previous Year
    - Search by Vendor
    - Custom Search (date range, description, vendor, amount)
- Save and load transaction data using CSV files

---
## How to Run the Application
1. Open the repository URL on GitHub
2. Clone or download the repository
3. Open the project in IntelliJ
4. Run The 'Main' class 
5. Follow the on-scree menu prompts

---
## Project  Structure 
src/

└─ main/

├─ java/

│   └─ com.pluralsight/

│       ├─ Main.java

│       └─ Transaction.java

└─ resources/

└─ OldTransactions.csv

---

## CSV File Format
Transactions are stored using the following pipe-delimited format:
**Date|Time|Description|Vendor|Amount**
Example:
**2025-03-10|14:30:00|Tax Payment|IRS|-250.00**
---

## Limitations
- Code-Drive user interface. 
- CSV File path are fixed
- Limited input validation for non-numeric values

---
## Future Improvements
- Add Input validation and error handling
- GUI Interface
---
## Author
This project was created as part of a Java capstone assignment to demonstrate foundational software development skills, clean code practices, and file‑based persistence.
I approached the project with a single‑responsibility mindset, starting by outlining the overall structure and creating placeholder methods before implementing functionality. This approach helped me stay organized and complete the project on time.
Throughout development, I focused on maintaining consistent coding standards, including clear method and variable names, single‑purpose methods, clean formatting, and overall readability.
Building this application helped solidify my understanding of core Java concepts such as classes, methods, loops, conditional logic, and file input/output operations.

![Screenshot 2026-04-27 093657.png](Images/Screenshot%202026-04-27%20093657.png)







