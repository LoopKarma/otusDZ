public class Main {
    public static void main(String[] args) {
        try {
            String className = args[0];
            TestRunner runner = new TestRunner();
            runner.doRun(className);
        } catch (Exception e) {
            System.err.println("Internal Error occurred.");
            e.printStackTrace(System.err);
        }
    }
}
