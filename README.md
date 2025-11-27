# 🏁 Leaderboard Ranking System – Spring Boot
A complete leaderboard ranking engine that processes a championship series across 24 events, applying multi-level tiebreaking rules and generating rankings, event-specific results, and rank-movement analytics.

This project reads an Excel leaderboard file (`leaderboard.xlsx`), calculates total points and spending, applies advanced tiebreakers, and exposes results via REST APIs.

---

# 📌 Features

### ✔ Load Data from Excel
Reads two tables from a single Excel sheet:

- **Points table (Top)**
- **Spending table (Bottom)**

Handles:
- Missing values
- DSQ (`D$Q`)
- Numeric + string + formula cells
- Decimal points for accuracy

### ✔ Multi-Level Tiebreakers (as per assignment rules)
Rank is determined using:
1. **Total Points (Descending)**
2. **Total Spending (Ascending)**
3. **Countback System (Highest Absolute Score)**
    - Compare highest event score
    - If tied → compare second highest
    - Continue…
4. **Alphabetical Order** (final fallback)

### ✔ REST Endpoints
| Endpoint | Description |
|---------|-------------|
| `/leaderboard` | Final ranking after all 24 events |
| `/leaderboard/event/{n}` | Ranking considering events 1…n |
| `/leaderboard/movement` | Rank progression across all events |

### ✔ Rank Movement Analytics
Shows how each player's rank changes from Event 1 → Event 24, including final movement such as `+3`, `-2`, or `0`.

---

# 📂 Project Structure

```
src/main/java/com.example.leaderboard
│
├── controller
│   └── LeaderboardController.java
│
├── model
│   ├── LeaderboardRow.java
│   ├── RankResult.java
│   └── MovementResult.java
│
├── service
│   ├── ExcelLoaderService.java
│   ├── RankingService.java
│   └── TieBreakerService.java
│
└── LeaderboardApplication.java
```

---

# 🧠 How Ranking Works (Logic Overview)

### **1. Read Excel**
Extract 24 event columns for:
- Points
- Spending

### **2. Compute Totals**
```
totalPoints = sum(eventPoints)
totalSpending = sum(eventSpending)
```

### **3. Apply Tiebreaker Logic**
```text
1. Higher total points wins
2. Lower total spending wins
3. Sort event scores high → low and compare
4. Alphabetical order if still tied
```

### **4. Generate Final Ranking**
Sort all players using the tiebreaker comparator.

---

# 🚀 API Usage

## 1️⃣ Get Final Leaderboard
```
GET /leaderboard
```
**Response Example:**
```json
[
  {
    "rank": 1,
    "playerName": "Team Overbudget",
    "totalPoints": 1080.0,
    "totalSpending": 132.00
  }
]
```

---

## 2️⃣ Get Leaderboard After Event N
```
GET /leaderboard/event/10
```
Responds with standings after event 10 only.

---

## 3️⃣ Get Rank Movement
```
GET /leaderboard/movement
```

**Response Example:**
```json
{
  "playerName": "Cocoa Pops",
  "ranks": [3, 1, 1, 1, … ],
  "finalMovement": "+2"
}
```

---

# 📦 Running the Project

### **1. Clone repository**
```sh
git clone https://github.com/FORREALTHO/leaderboard
cd leaderboard
```

### **2. Place Excel File**
Ensure your file is located at:
```
src/main/resources/leaderboard.xlsx
```

### **3. Run application**
```sh
mvn spring-boot:run
```

Server starts at:
```
http://localhost:8080
```

---

# 🖼️ Input File Format
Your Excel file must include:

### Top Table → Points
Rows 2–25  
Columns C–Z (24 events)

### Bottom Table → Spending
Rows 33–56  
Columns C–Z (24 events)

---

# 🧪 Testing (Optional but recommended)

Use Postman or browser:

- http://localhost:8080/leaderboard
- http://localhost:8080/leaderboard/event/1
- http://localhost:8080/leaderboard/movement

---

# 📘 Tech Stack

- Java 17
- Spring Boot
- Apache POI (Excel parsing)
- Lombok
- Maven

---

# 📝 Author
**Kartikeya Aryam**  
(Backend + Algorithm Implementation)
