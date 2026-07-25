from flask import Flask, render_template

app = Flask(__name__)

products = [
    {"name": "A", "price": 10, "image": "", "details": "x"},
     {"name": "B", "price": 10, "image": "", "details": "x"}
]

@app.route("/")
def index():
    return render_template("index.html", products=products)

@app.get("/about")
def about():
    return render_template("index.html", products=products)

@app.route(
    "/contact"
)
def contact():
    return render_template("add_product.html")

@app.route("/item/<int:i>", methods=["GET"])
def item(i):
    return render_template("product_details.html", product=products[0])

app.run(debug=True)