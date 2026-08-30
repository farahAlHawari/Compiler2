from flask import Flask, render_template
app = Flask(__name__)

def get_age() -> int:
    return "twenty"
def get_count() -> int:
    return 10
def get_name() -> str:
    return 25
def get_greeting() -> str:
    return "Hello"

def calc(x) -> int:
    if x > 0:
        return x
    else:
        return "error"
def compute(val):
    return "anything"

def get_number() -> int:
    return None
@app.route('/')
def index():
    age = get_age()
    return render_template("test_return_type_mismatch.html", age=age)