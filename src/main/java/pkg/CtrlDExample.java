//package pkg;
//
//import org.jline.reader.LineReader;
//import org.jline.reader.LineReaderBuilder;
//import org.jline.reader.UserInterruptException;
//import org.jline.console.Console;
//
//public class CtrlDExample {
//    public static void main(String[] args) {
//        Console console = new Console();
//        LineReader lineReader = LineReaderBuilder.builder()
//                .terminal(console.getTerminal())
//                .build();
//
//        System.out.println("Type your input (Ctrl+D to finish):");
//
//        String input = "";
//        try {
//            while (true) {
//                String line = lineReader.readLine();
//                input += line + "\n";
//            }
//        } catch (UserInterruptException e) {
//            System.out.println("Input terminated.");
//        }
//
//        System.out.println("You entered:");
//        System.out.println(input);
//    }
//}
