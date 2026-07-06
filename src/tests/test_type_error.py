# ============================================================
# Test File: Type Error Cases (OperationTypeErrorChecker)
# Covers all testable type error scenarios
# ============================================================

# ===== تعريف متغيرات بأنواع واضحة =====
x = 5
y = 3
s = "hello"
lst = [1, 2, 3]
d = {"key": "value"}
b = True
n = None

# ===== الحالة 1: str + int =====
# بايثون: TypeError: can only concatenate str (not "int") to str
r1 = s + x

# ===== الحالة 2: int + str =====
# بايثون: TypeError: unsupported operand type(s) for +: 'int' and 'str'
r2 = x + s

# ===== الحالة 3: str - int =====
# بايثون: TypeError: unsupported operand type(s) for -: 'str' and 'int'
r3 = s - x

# ===== الحالة 4a: str / int =====
# بايثون: TypeError: unsupported operand type(s) for /: 'str' and 'int'
r4a = s / x

# ===== الحالة 4b: str % int =====
# بايثون: TypeError: unsupported operand type(s) for %: 'str' and 'int'
r4b = s % x

# ===== الحالة 5: str * list (تكرار بتوع مختلفين) =====
# بايثون: TypeError: can't multiply sequence by non-int of type 'list'
r5 = s * lst

# ===== الحالة 6: list + int =====
# بايثون: TypeError: can only concatenate list (not "int") to list
r6 = lst + x

# ===== الحالة 7: list * str =====
# بايثون: TypeError: can't multiply sequence by non-int of type 'str'
r7 = lst * s

# ===== الحالة 8: int < str =====
# بايثون: TypeError: '<' not supported between instances of 'int' and 'str'
r8 = x < s

# ===== الحالة 9: int > str =====
# بايثون: TypeError: '>' not supported between instances of 'int' and 'str'
r9 = x > s

# ===== الحالة 10: int <= str =====
# بايثون: TypeError: '<=' not supported between instances of 'int' and 'str'
r10 = x <= s

# ===== الحالة 11: int >= str =====
# بايثون: TypeError: '>=' not supported between instances of 'int' and 'str'
r11 = x >= s

# ===== الحالة 12: فهرسة على int (مش subscriptable) =====
# بايثون: TypeError: 'int' object is not subscriptable
r12 = x[0]

# ===== الحالة 13: فهرسة على bool (مش subscriptable) =====
# بايثون: TypeError: 'bool' object is not subscriptable
r13 = b[0]

# ===== الحالة 14: فهرسة على None =====
# بايثون: TypeError: 'NoneType' object is not subscriptable
r14 = n[0]

# ===== الحالة 15: فهرسة list بـ string (index غلط) =====
# بايثون: TypeError: list indices must be integers or slices, not 'str'
r15 = lst[s]

# ===== الحالة 16: فهرسة string بـ string =====
# بايثون: TypeError: string indices must be integers, not 'str'
r16 = s[s]

# ===== الحالة 17: len() على int =====
# بايثون: TypeError: object of type 'int' has no len()
r17 = len(x)

# ===== الحالة 18: len() على bool =====
# بايثون: TypeError: object of type 'bool' has no len()
r18 = len(b)

# ===== الحالة 19: len() على None =====
# بايثون: TypeError: object of type 'NoneType' has no len()
r19 = len(n)

# ===== الحالة 20: Augmented: int += str (لازم يتكشف بعد الاصلاح) =====
z = 10
z += s

# ===== الحالة 21: Augmented: str -= int (لازم يتكشف بعد الاصلاح) =====
w = "hello"
w -= x

# ===== الحالة 22: Augmented: list *= str (لازم يتكشف بعد الاصلاح) =====
m = [1, 2]
m *= s