# ===== اختبار NameError: متغير غير معرّف نهائياً =====

# حالة 1: متغير غير معرّف أبداً
def test_basic_undefined():
    print(x)
    return "done"

# حالة 2: استخدام متغير قبل تعريفه (forward reference)
def test_forward_reference():
    y = 10
    result = z + y
    z = 5
    return result

# حالة 3: متغير غير معرّف داخل تعبير معقد (دالة + سلسلة)
def test_undefined_in_expression():
    name = "Ali"
    return name + " " + str(age)

# حالة 4: متغير غير معرّف كمعامل لدالة
def test_undefined_as_argument():
    result = len(missing_list)
    return result

# حالة 5: متغير غير معرّف في شرط if
def test_undefined_in_condition():
    if flag:
        return "yes"
    return "no"

# حالة 6: متغير غير معرّف على يسار عملية حسابية
def test_undefined_in_arithmetic():
    total = unknown_var + 100
    return total


