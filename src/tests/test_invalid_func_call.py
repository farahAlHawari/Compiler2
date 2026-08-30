from flask import Flask, render_template

app = Flask(__name__)

def greet(name):
    print("Hello " + name)

def add(a, b):
    return a + b

@app.route('/')
def index():

    greet("Ali")


    foo()


    x = 5
    x()

    print("test")
    len([1, 2, 3])

    calculate_total(100)

    return render_template("test_invalid_func_call.html")