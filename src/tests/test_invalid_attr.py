# ===== اختبار AttributeError: وصول لـ attribute غير موجود =====

def string_test():
    text = "hello world"
    # str ما عندها append (هذه list method)
    text.append("!")

def list_test():
    items = [1, 2, 3]
    # list ما عندها split (هذه str method)
    items.split(",")

def dict_test():
    user = {"name": "Ali", "age": 25}
    # dict ما عندها upper (هذه str method)
    user.upper()

def int_test():
    num = 42
    # int ما عندها keys (هذه dict method)
    num.keys()

# حالة 5: str لا تمتلك pop (هذه list/dict method)
def test_str_wrong_method2():
    msg = "test"
    msg.pop()

# حالة 6: list لا تمتلك upper (هذه str method)
def test_list_wrong_method2():
    arr = [1, 2, 3]
    arr.upper()

# حالة 7: int لا يمتلك append (هذه list method)
def test_int_wrong_method2():
    count = 10
    count.append(1)

# حالة 8: float لا يمتلك keys
def test_float_wrong_method():
    price = 9.9
    price.keys()