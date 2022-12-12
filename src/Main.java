

public class Main {
    public static void main(String[] args) {
        Dataset file = new Dataset();
        file.addData("/resources/armando.txt");
        file.processData();
        System.out.println(file.generate("g", 4));
    }
}
