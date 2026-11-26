public abstract class Person {
    private String name;
    private String password;
    //
    private String phoneNum;
    private String address;

    public Person(String name, String password, String phoneNum, String address) {
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public abstract String readType();

    public String readLine() {
        return readType() + "|" + name + "|" + password + "|" + phoneNum + "|" + address;
    }
}
