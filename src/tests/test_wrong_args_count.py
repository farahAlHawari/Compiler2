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
    # ✅ عدد صحيح
    greet("Ali", "Hello")

    # ❌ معامل واحد ناقص
    greet("Ali")

    # ❌ معامل واحد زائد
    greet("Ali", "Hello", "Extra")

    # ✅ عدد صحيح
    add(1, 2)

    # ❌ معامل واحد ناقص
    add(1)

    # ❌ معامل واحد زائد
    add(1, 2, 3)

    # ✅ بدون معاملات
    no_args()

    # ❌ معامل زائد على دالة بدون معاملات
    no_args("extra")

    # ✅ ثلاثة معاملات
    three_args(1, 2, 3)

    # ❌ معاملين فقط بدل ثلاثة
    three_args(1, 2)

    return render_template("test_wrong_args_count.html")