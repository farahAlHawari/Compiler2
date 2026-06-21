from flask import Flask, render_template

app = Flask(__name__)

@app.route('/test')
def test_route():
    c: int = "hello"
    x: str = 42
    y: list = "wrong"
    z: int = None
    w: float = "free"
    age = 25
    name = "Sara"
    items = 5
    return render_template("test_type_mismatch.html", age=age, name=name, items=items)