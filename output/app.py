from flask import Flask, render_template

app = Flask(__name__)

products = [
    {"name": "A", "price": 10, "image": "", "details": "x"},
     {"name": "B", "price": 10, "image": "", "details": "x"}
]

@app.route("/")
def index():
    return render_template("index.html", products=products)

@app.route("/add", methods=["GET", "POST"])
def add():
    if request.method == "POST":
        products.append({
            "image": request.form["image"],
            "name": request.form["name"],
            "price": request.form["price"],
            "details": request.form["details"]
        })
    return render_template("add_product.html")

@app.route("/delete/<int:i>")
def delete_product(i):
    if 0 <= i < len(products):
        products.pop(i)
    return redirect(url_for("index"))

@app.route("/details/<int:i>")
def details(i):
    return render_template("product_details.html", product=products[i])

app.run(debug=True)