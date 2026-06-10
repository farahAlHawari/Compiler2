from flask import Flask, render_template

app = Flask(__name__)

def greet(name):
    print("Hello " + name)

def add(a, b):
    return a + b

@app.route('/')
def index():
    # ✅ دالة موجودة
    greet("Ali")

    # ❌ دالة غير موجودة
    foo()

    # ❌ متغير يُستخدم كدالة
    x = 5
    x()

    # ✅ دالة مدمجة في Python
    print("test")
    len([1, 2, 3])

    # ❌ دالة غير موجودة أخرى
    calculate_total(100)

    return render_template("test_invalid_func_call.html")