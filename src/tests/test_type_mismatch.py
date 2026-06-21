from flask import Flask, render_template

app = Flask(__name__)


a: int = 5
name: str = "Sara"
price: float = 3
active: bool = True
items: list = [1, 2, 3]


c: int = "hello"


x: str = 42


y: list = "not a list"


z: int = None


w: float = "free"




@app.route('/bridge')
def bridge():

    name = "Sara"

    age = 22

    items = 5

    valid_list = [1, 2, 3]


    age_str = 30

    return render_template(
        "test_type_mismatch.html",
        name=name,
        age=age,
        items=items,
        valid_list=valid_list,
        age_str=age_str
    )


if __name__ == '__main__':
    app.run()