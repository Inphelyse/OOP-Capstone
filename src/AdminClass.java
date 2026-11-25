public class AdminClass extends Person {
    public AdminClass(String name, String password, String phoneNum, String address) {
        super(name, password, phoneNum, address);
    }

    @Override
    public String readType() {
        return "";
    }


}
