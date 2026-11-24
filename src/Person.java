public abstract class Person {
    String name;
    String password;
    //
    int phoneNum;
    String address;

    public Person(String name, String password, int phoneNum, String address) {
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public abstract String readType();

    public String readLine() {
        return readType() + "|" + name + "|" + password + "|" + phoneNum + "|" + address;
    }
}
