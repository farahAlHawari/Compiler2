from flask import Flask, render_template

app = Flask(__name__)

def greet(name, greeting):
    print(greeting + " " + name)

def add(a, b):
    return a + b

def no_args():
    print("no args")

def three_args(x, y, z):
    return x + y + z

@app.route('/')
def index():
    greet("Ali", "Hello")

    greet("Ali")

    greet("Ali", "Hello", "Extra")

    add(1, 2)

    add(1)

    add(1, 2, 3)

    no_args()

    no_args("extra")

    three_args(1, 2, 3)

    three_args(1, 2)

    return render_template("test_wrong_args_count.html")