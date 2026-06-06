# ===== IMPORTS =====
from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy


# ===== APP INITIALIZATION =====
app = Flask(__name__)
db = SQLAlchemy(app)


# ===== CLASS DEFINITIONS =====
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


# ===== ROUTE FUNCTIONS (Decorated) =====
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


@app.route("/delete/<int:item_id>")
def delete_item(item_id):
    if item_id > 0:
        return redirect(url_for("index"))
    else:
        return "Invalid ID"


# ===== REGULAR FUNCTIONS =====
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


def process_data(data, limit, offset):
    results = []
    for item in data:
        if item != None:
            results.append(item)
    return results


# ===== TRY / EXCEPT =====
try:
    result = 10
except error:
    result = 0
finally:
    result = result


# ===== WITH STATEMENT =====
with open("data.txt") as file:
    content = file


# ===== GLOBAL VARIABLE =====
global page_size
page_size = 20


# ===== IF / ELIF / ELSE =====
status = "active"
if status == "active":
    role = "admin"
elif status == "pending":
    role = "moderator"
elif status == "suspended":
    role = "restricted"
else:
    role = "guest"


# ===== FOR LOOP =====
products = ["Laptop", "Phone", "Tablet"]
product_names = []
for product in products:
    product_names.append(product)


# ===== WHILE LOOP =====
count = 0
max_count = 10
while count < max_count:
    count += 1


# ===== AUGMENTED ASSIGNMENTS =====
total_price = 0
total_price += 100
total_price -= 20
total_price *= 2
total_price /= 5


# ===== LIST AND DICT LITERALS =====
categories = ["electronics", "clothing", "books"]
settings = {"theme": "dark", "lang": "en", "page_size": 20}


# ===== EXPRESSIONS =====
x = (10 + 20) * 3
is_valid = x > 50 and x < 100
has_items = not (len(products) == 0)
is_long = len(products) > 5
is_equal = settings == None
is_ready = True and False
not_ready = not True


# ===== PASS / BREAK / CONTINUE =====
pass
break
continue


# ===== BOOLEAN AND NONE =====
flag = True
is_done = False
empty = None


# ===== INDEX ACCESS =====
name = products[0]
key = settings["theme"]


# ===== MAIN =====
if __name__ == "__main__":
    app.run(debug=True)