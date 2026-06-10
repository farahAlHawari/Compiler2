from flask import Flask, render_template

app = Flask(__name__)

# ❌ يعيد string بدل int
def get_age() -> int:
    return "twenty"

# ✅ يعيد int كما هو معلن
def get_count() -> int:
    return 10

# ❌ يعيد int بدل string
def get_name() -> str:
    return 25

# ✅ يعيد string كما هو معلن
def get_greeting() -> str:
    return "Hello"

# ❌ فرع صحيح وفرع خاطئ
def calc(x) -> int:
    if x > 0:
        return x
    else:
        return "error"

# ✅ بدون type hint - لا خطأ
def compute(val):
    return "anything"

# ❌ يعيد None بدل int
def get_number() -> int:
    return None

@app.route('/')
def index():
    age = get_age()
    return render_template("test_return_type_mismatch.html", age=age)