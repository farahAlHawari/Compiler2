from flask import Flask, render_template

app = Flask(__name__)

# === Case 1: Variable used before declaration (declared later) ===

@app.route('/case1')
def case1():
    print(x)
    x = 5
    return render_template("index.html")

# === Case 2: Type annotation without value ===

@app.route('/case2')
def case2():
    x: int
    print(x)
    x = 10
    return render_template("index.html")

# === Case 3: Variable only assigned inside a conditional ===

@app.route('/case3')
def case3():
    if False:
        y = 5
    print(y)
    return render_template("index.html")

# === Negative: properly initialized before use (no error) ===

@app.route('/ok1')
def ok1():
    z = 10
    print(z)
    return render_template("index.html")

# === Negative: unconditional init exists before conditional ===

@app.route('/ok2')
def ok2():
    a = 0
    if True:
        a = 5
    print(a)
    return render_template("index.html")