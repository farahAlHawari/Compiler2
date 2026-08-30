
x = 5
y = 3
s = "hello"
lst = [1, 2, 3]
d = {"key": "value"}
b = True
n = None


r1 = s + x
r2 = x + s
r3 = lst + x
r4 = s - x
r5 = s / x
r6 = s % x
r7 = s * lst
r8 = lst * s
r9 = s * 2.5
r10 = s // x
r11 = s ** x



r12 = x < s
r13 = s > x


r14 = x[0]
r15 = b[0]
r16 = n[0]
r17 = lst[s]


r18 = len(x)
r19 = len(n)


r20 = -s
r21 = +s


z = 10
z += s
w = "hello"
w -= x
m = [1, 2]
m *= s


r25 = sum(x)
r26 = sorted(x)
r27 = abs(s)
r28 = max(x)
r29 = round(s)


a30 = 10
a30 %= s
a31 = "hello"
a31 //= x
a32 = "hello"
a32 **= x
a33 = "hello"
a33 /= x


from flask import Flask, render_template

app = Flask(__name__)

@app.route('/test')
def test_route():
    return render_template("test_type_error.html", s=s, x=x)