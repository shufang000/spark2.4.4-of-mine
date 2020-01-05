public class TestVMAndParameters {
    public static void main(String[] args) {
        if (args.length != 2 ) {
            System.out.println("长度不为2重新输入");
        }
        System.out.println(args[0]+" "+args[1]);
    }
}
