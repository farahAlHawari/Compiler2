
# ===== اختبار TypeError: عملية حسابية على None =====

# حالة 1: None في الطرف الأيسر (+)
def test_none_left_add():
    x = None
    result = x + 5
    return result

# حالة 2: None في الطرف الأيمن (*) — الكائن الحرفي هو الأيسر
def test_none_right_multiply():
    x = None
    result = 10 * x
    return result

# حالة 3: None - متغير (كلا الطرفين متغيرات)
def test_none_subtract_var():
    x = None
    y = 3
    result = x - y
    return result

# حالة 4: None / حرفي
def test_none_divide():
    x = None
    result = x / 2
    return result

# حالة 5: None ** حرفي (القوة)
def test_none_power():
    x = None
    result = x ** 2
    return result

# حالة 6: None % حرفي (باقي القسمة)
def test_none_modulo():
    x = None
    result = x % 3
    return result

# حالة 7: حرفي + None (None على اليمين)
def test_literal_plus_none():
    x = None
    result = 100 + x
    return result

# حالة 8: متغيرين كلاهما None
def test_both_none():
    a = None
    b = None
    result = a + b
    return result

# حالة 9: None في عملية طرح معكوسة (حرفي - None)
def test_none_right_subtract():
    x = None
    result = 50 - x
    return result
