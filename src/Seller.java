public class Seller extends Person {
    public Seller(String name, String password, String phoneNum, String address) {
        super(name, password, phoneNum, address);
    }

    @Override
    public String readType() {
        return "Seller";
    }
}//
