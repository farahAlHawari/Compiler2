
def test_basic_undefined():
    print(x)
    return "done"

def test_forward_reference():
    y = 10
    result = z + y
    z = 5
    return result

def test_undefined_in_expression():
    name = "Ali"
    return name + " " + str(age)

def test_undefined_as_argument():
    result = len(missing_list)
    return result

def test_undefined_in_condition():
    if flag:
        return "yes"
    return "no"

def test_undefined_in_arithmetic():
    total = unknown_var + 100
    return total


