from flask import Flask, render_template

app = Flask(__name__)

x = 10
count = 0
name = "Sara"
total = 100

@app.route('/test')
def test_unbound():
    x += 1

    count -= 1

    z = 0
    z += 10

    global name
    name += " Ali"

    print(total)

    total *= 2

    return render_template("test.html", x=x, z=z, name=name)