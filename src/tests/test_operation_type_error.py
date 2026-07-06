# ===== اختبار Error Type: عملية بين نوعين غير متوافقين =====

# حالة 1: "Sara" + 4  -> String + Int
def test_string_plus_int():
    name = "Sara"
    result = name + 4
    return result

# حالة 2: "Sara" < 4  -> String < Int
def test_string_lt_int():
    name = "Sara"
    result = name < 4
    return result

# حالة 3: arr + 3  -> List + Int
def test_list_plus_int():
    arr = [1, 2]
    result = arr + 3
    return result

# حالة 4: len(10)  -> len() على int
def test_len_on_int():
    result = len(10)
    return result

# حالة 5: 5 / "hello"  -> Int / String
def test_int_div_string():
    result = 5 / "hello"
    return result

# حالة 6: "a" * "b"  -> String * String
def test_string_mult_string():
    result = "a" * "b"
    return result

# حالة 7: x[0] حيث x=5  -> فهرسة على int
def test_index_on_int():
    x = 5
    result = x[0]
    return result

# حالة 8: فهرسة list بمفتاح string بدل int
def test_list_index_with_string():
    items = [1, 2, 3]
    result = items["key"]
    return result

# حالة 9: None < 5  -> مقارنة مع None (مش مغطاة بأي checker تاني)
def test_none_comparison():
    x = None
    result = x < 5
    return result

# حالة 10: None[0]  -> فهرسة على None
def test_index_on_none():
    x = None
    result = x[0]
    return result

# حالة 11: len(None)
def test_len_on_none():
    x = None
    result = len(x)
    return result

# حالة 12: "ab" ** 2  -> ** على string (بعكس * ما في استثناء تكرار)
def test_string_power_int():
    result = "ab" ** 2
    return result


# ===== حالات صحيحة (Sanity Checks) — ما لازم تنكشف كأخطاء =====

# صحيحة: تكرار نص بعامل الضرب
def test_string_repeat_valid():
    result = "ab" * 3
    return result

# صحيحة: bool هو subclass من int ببايثون
def test_bool_plus_int_valid():
    flag = True
    result = flag + 5
    return result

# صحيحة: dict بتقبل أي نوع مفتاح
def test_dict_index_valid():
    settings = {"a": 1}
    result = settings["a"]
    return result