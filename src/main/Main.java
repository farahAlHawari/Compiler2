//package main;
//
//import main.pythoncompiler.*;
//
//public class Main {
//
//    public static void main(String[] args) throws Exception {
//
//        PythonCompiler compiler = new PythonCompiler();
//        compiler.compile("src/tests/app1.py");
//    }
//}

package main;

import main.pythoncompiler.PythonCompiler;
import symbol_table.SymbolTable;

public class Main {

    public static void main(String[] args) throws Exception {

        SymbolTable symbolTable = new SymbolTable();

        PythonCompiler compiler = new PythonCompiler(symbolTable);
        compiler.compile("src/tests/app1.py");
    }
}
