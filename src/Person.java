public abstract class Person {
    protected String name;
    protected String password;
    protected String phoneNum;
    protected String address;

    public Person(String name, String password, String phoneNum, String address) {
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public abstract String readType();


    public String readLine() {
        return readType() + "|" + name + "|" + password + "|" + phoneNum + "|" + address;
    }
}
