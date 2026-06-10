from flask import Flask, render_template

app = Flask(__name__)

# === Return Type Mismatch ===

def get_age() -> int:
    return "twenty"

def get_price() -> float:
    return "free"

def get_info() -> str:
    return 42

# === Valid functions ===

def greet(name, greeting):
    print(greeting + " " + name)

def add(a, b):
    return a + b

def calculate(x, y, z):
    return x + y + z

# === No type hint - should NOT trigger return type error ===

def compute(val):
    return "result"

@app.route('/')
def index():
    name = "Ali"
    age = 25
    products = ["item1", "item2", "item3"]

    # ✅ render_template مع متغيرات صحيحة
    return render_template("test_all_errors.html", name=name, age=age, products=products)

@app.route('/partial')
def partial():
    name = "Sara"
    # ❌ نمرر name فقط - المتغيرات الأخرى مفقودة
    return render_template("test_all_errors.html", name=name)

@app.route('/empty')
def empty_route():
    # ❌ لا نمرر أي متغير
    return render_template("test_all_errors.html")

@app.route('/test_calls')
def test_calls():
    # ✅ استدعاء صحيح
    greet("Ali", "Hello")

    # ❌ Wrong Args - ناقص
    greet("Ali")

    # ❌ Wrong Args - زائد
    greet("Ali", "Hello", "Extra")

    # ✅ استدعاء صحيح
    add(1, 2)

    # ❌ Wrong Args - ناقص
    add(1)

    # ❌ Invalid Func Call - دالة غير موجودة
    unknown_function()

    # ❌ Invalid Func Call - متغير كدالة
    x = 5
    x()

    # ❌ Wrong Args
    calculate(1)

    # ✅ دالة مدمجة - لا خطأ
    print("test")
    len([1, 2])

    # ❌ Return Type Mismatch - نستدعي الدوال الخطأ
    result = get_age()
    price = get_price()
    info = get_info()

    return render_template("test_all_errors.html", name="test", age=0, products=[])

@app.route('/more')
def more():
    # ✅ دالة بدون type hint
    compute("test")

    # ❌ Wrong Args على دالة بدون معاملات
    result = compute()

    return render_template("test_all_errors.html", name="test", age=0)