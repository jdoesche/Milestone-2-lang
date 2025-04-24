package skeptical;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;

import skeptical.ast.Program;
import skeptical.parser.SkepticalLexer;
import skeptical.parser.SkepticalParser;

public class Reader {

    public Program read() throws IOException {
        String programText = readNextProgram();
        return parse(programText);
    }

    public Program parse(String programText) {
        SkepticalLexer lexer = new SkepticalLexer(new ANTLRInputStream(programText));
        SkepticalParser parser = new SkepticalParser(new CommonTokenStream(lexer));
        return parser.program().ast;
    }

    public static String readFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    private String readNextProgram() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("$ ");
        String input = br.readLine();
        return runFile(input);
    }

    protected String getProgramDirectory() {
        return "build" + File.separator + "skeptical" + File.separator + "examples" + File.separator;
    }

    private String runFile(String input) throws IOException {
        if (input.startsWith("run ")) {
            input = readFile(getProgramDirectory() + input.substring(4));
        }
        return input;
    }
}
