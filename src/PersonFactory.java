public class PersonFactory {
    public static Person createPerson(String type, String username, String password, String phoneNum, String address) {
        switch (type){
            case "Seller":
                System.out.println("niga");
                return new Seller(username,password,phoneNum,address);
            case "Customer":
                System.out.println("nigasaurus");
                return new CustomerClass(username,password,phoneNum,address);
            case "Courier":
                System.out.println("nigo");
                return new Courier(username,password,phoneNum,address);
            default:
                System.out.println("error");
                return null;
        }
    }
}
