public class Customer extends Person{
    public Customer(String name, String password, int phoneNum, String address) {
        super(name, password, phoneNum, address);
    }

    @Override
    public String readType() {
        return "Customer";
    }
}//
