

def test_none_left_add():
    x = None
    result = x + 5
    return result

def test_none_right_multiply():
    x = None
    result = 10 * x
    return result

def test_none_subtract_var():
    x = None
    y = 3
    result = x - y
    return result

def test_none_divide():
    x = None
    result = x / 2
    return result

def test_none_power():
    x = None
    result = x ** 2
    return result

def test_none_modulo():
    x = None
    result = x % 3
    return result

def test_literal_plus_none():
    x = None
    result = 100 + x
    return result

def test_both_none():
    a = None
    b = None
    result = a + b
    return result

def test_none_right_subtract():
    x = None
    result = 50 - x
    return result
