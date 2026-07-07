# Type Error Test — كل الحالات

# تعريف متغيرات
x = 5
y = 3
s = "hello"
lst = [1, 2, 3]
d = {"key": "value"}
b = True
n = None

# ===== 1. العمليات الثنائية =====

# حالة 1: str + int
r1 = s + x
# حالة 2: int + str
r2 = x + s
# حالة 3: list + int
r3 = lst + x
# حالة 4: str - int
r4 = s - x
# حالة 5: str / int
r5 = s / x
# حالة 6: str % int (formatting message)
r6 = s % x
# حالة 7: str * list (sequence * non-int)
r7 = s * lst
# حالة 8: list * str (sequence * non-int)
r8 = lst * s
# حالة 9: str * 2.5 (sequence * non-int)
r9 = s * 2.5
# حالة 10: str // int
r10 = s // x
# حالة 11: str ** int
r11 = s ** x

# ===== 2. المقارنات =====

# حالة 12: int < str
r12 = x < s
# حالة 13: str > int
r13 = s > x

# ===== 3. الفهرسة =====

# حالة 14: int[0] — not subscriptable
r14 = x[0]
# حالة 15: bool[0] — not subscriptable
r15 = b[0]
# حالة 16: None[0] — not subscriptable
r16 = n[0]
# حالة 17: list[str] — wrong index type
r17 = lst[s]

# ===== 4. len() =====

# حالة 18: len(int)
r18 = len(x)
# حالة 19: len(None)
r19 = len(n)

# ===== 5. Unary =====

# حالة 20: -"hello"
r20 = -s
# حالة 21: +"hello"
r21 = +s

# ===== 6. Augmented Assignment =====

# حالة 22: int += str
z = 10
z += s
# حالة 23: str -= int
w = "hello"
w -= x
# حالة 24: list *= str
m = [1, 2]
m *= s

# ===== 7. Built-in Functions =====

# حالة 25: sum(int) — not iterable
r25 = sum(x)
# حالة 26: sorted(int) — not iterable
r26 = sorted(x)
# حالة 27: abs(str) — bad operand
r27 = abs(s)
# حالة 28: max(int) — not iterable
r28 = max(x)
# حالة 29: round(str) — no __round__
r29 = round(s)

# ===== 8. Augmented Assignment (إضافية) =====

# حالة 30: int %= str
a30 = 10
a30 %= s
# حالة 31: str //= int
a31 = "hello"
a31 //= x
# حالة 32: str **= int
a32 = "hello"
a32 **= x
# حالة 33: str /= int
a33 = "hello"
a33 /= x

# ===== 9. Jinja Arithmetic =====

from flask import Flask, render_template

app = Flask(__name__)

@app.route('/test')
def test_route():
    return render_template("test_type_error.html", s=s, x=x)