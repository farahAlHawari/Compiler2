
def test_string_plus_int():
    name = "Sara"
    result = name + 4
    return result

def test_string_lt_int():
    name = "Sara"
    result = name < 4
    return result

def test_list_plus_int():
    arr = [1, 2]
    result = arr + 3
    return result

def test_len_on_int():
    result = len(10)
    return result

def test_int_div_string():
    result = 5 / "hello"
    return result

def test_string_mult_string():
    result = "a" * "b"
    return result

def test_index_on_int():
    x = 5
    result = x[0]
    return result

def test_list_index_with_string():
    items = [1, 2, 3]
    result = items["key"]
    return result

def test_none_comparison():
    x = None
    result = x < 5
    return result

def test_index_on_none():
    x = None
    result = x[0]
    return result

def test_len_on_none():
    x = None
    result = len(x)
    return result

def test_string_power_int():
    result = "ab" ** 2
    return result



def test_string_repeat_valid():
    result = "ab" * 3
    return result

def test_bool_plus_int_valid():
    flag = True
    result = flag + 5
    return result

def test_dict_index_valid():
    settings = {"a": 1}
    result = settings["a"]
    return result