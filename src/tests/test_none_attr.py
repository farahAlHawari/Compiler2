
# ===== اختبار AttributeError على NoneType =====

# حالة 1: وصول لخاصية بسيطة
def test_none_simple_attr():
    user = None
    print(user.name)

# حالة 2: استدعاء طريقة بمعاملات
def test_none_method_with_args():
    data = None
    result = data.get("key")
    return result

# حالة 3: استدعاء طريقة بدون معاملات
def test_none_method_no_args():
    text = None
    text.upper()

# حالة 4: append على None
def test_none_append():
    items = None
    items.append(5)

# حالة 5: وصول لخاصية متداخلة (chain)
def test_none_chain():
    obj = None
    x = obj.data

# حالة 6: استدعاء pop على None
def test_none_pop():
    stack = None
    stack.pop()

