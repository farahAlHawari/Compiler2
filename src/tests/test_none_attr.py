

def test_none_simple_attr():
    user = None
    print(user.name)

def test_none_method_with_args():
    data = None
    result = data.get("key")
    return result

def test_none_method_no_args():
    text = None
    text.upper()

def test_none_append():
    items = None
    items.append(5)

def test_none_chain():
    obj = None
    x = obj.data

def test_none_pop():
    stack = None
    stack.pop()

