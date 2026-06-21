from flask import Flask, render_template

app = Flask(__name__)

@app.route('/test')
def test_route():
    x = 5 / 0
    y = 0
    z = 10 / y
    count = 0
    price = 100
    return render_template("test_division.html", count=count, price=price)