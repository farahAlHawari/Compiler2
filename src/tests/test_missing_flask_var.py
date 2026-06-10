from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def index():
    name = "Ali"
    age = 25
    return render_template("test_missing_flask_var.html", name=name, age=age)

@app.route('/empty')
def empty():
    return render_template("test_missing_flask_var.html")

@app.route('/none_val')
def none_val():
    x = None
    return render_template("test_missing_flask_var.html", x=x)

@app.route('/products')
def products_route():
    products = ["item1", "item2"]
    return render_template("test_missing_flask_var.html", products=products)