import java.util.ArrayList;

public class CustomerClass extends Person{

    public ArrayList<Item> cart;

    public CustomerClass(String name, String password, String phoneNum, String address) {
        super(name, password, phoneNum, address);
        this.cart = new ArrayList<>();
    }


    @Override
    public String readType() {
        return "Customer";
    }


}//
