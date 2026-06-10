from flask import Flask, render_template

app = Flask(__name__)

def greet(name, greeting):
    print(greeting + " " + name)

def add(a, b):
    return a + b

def get_age() -> int:
    return 25

def get_name() -> str:
    return "Ali"

def get_status() -> bool:
    return True

@app.route('/')
def index():
    name = "Ali"
    age = 25
    products = ["item1", "item2"]

    greet(name, "Hello")
    result = add(1, 2)
    user_age = get_age()
    user_name = get_name()
    status = get_status()

    print(user_age)
    print(len(products))

    return render_template("test_no_errors.html", name=name, age=age, products=products)

@app.route('/about')
def about():
    return render_template("test_no_errors.html", name="Sara", age=30, products=[])