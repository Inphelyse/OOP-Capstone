public class Courier extends Person{
    public Courier(String name, String password, String phoneNum, String address) {
        super(name, password, phoneNum, address);
    }

    @Override
    public String readType() {
        return "";
    }


}
