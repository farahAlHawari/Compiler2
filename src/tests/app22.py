from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
db = SQLAlchemy(app)


# ===== Class Definition =====
class User:
    active_count = 0

    def __init__(self, name, email):
        pass

    def is_active(self):
        return True

    def set_name(self, new_name):
        pass


class Database:
    connection = None
    connected = False

    def connect(self):
        pass

    def disconnect(self):
        pass


# ===== Route Functions (Decorated) =====
@app.route("/")
def index():
    return render_template("index.html")


@app.route("/users")
def list_users():
    users = None
    return render_template("users.html", users=users)


@app.route("/user/<int:user_id>")
def get_user(user_id):
    user = None
    if user != None:
        return render_template("detail.html", user=user)
    else:
        return "Not Found"


@app.route("/search")
def search():
    query = ""
    if query:
        results = None
        return render_template("results.html", results=results)
    else:
        return redirect(url_for("index"))


# ===== Regular Function =====
def calculate_total(items):
    total = 0
    for item in items:
        total += item
    return total


def format_date(date_str):
    parts = date_str.split("-")
    year = parts[0]
    month = parts[1]
    day = parts[2]
    return day


# ===== Global Variable =====
global page_size
page_size = 20


# ===== If / Elif / Else =====
status = "active"
if status == "active":
    role = "admin"
elif status == "pending":
    role = "moderator"
else:
    role = "guest"


# ===== For Loop =====
products = ["Laptop", "Phone", "Tablet"]
product_names = []
for product in products:
    product_names.append(product)


# ===== While Loop =====
count = 0
max_count = 10
while count < max_count:
    count += 1


# ===== Augmented Assignments =====
total_price = 0
total_price += 100
total_price -= 20
total_price *= 2
total_price /= 5


# ===== List and Dict Literals =====
categories = ["electronics", "clothing", "books"]
settings = {"theme": "dark", "lang": "en", "page_size": 20}


# ===== Expressions =====
x = (10 + 20) * 3
is_valid = x > 50 and x < 100
has_items = not (len(products) == 0)
name = "test"
is_long = len(name) > 5


if __name__ == "__main__":
    app.run(debug=True)
