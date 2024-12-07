package ca.gbc.comp3074groupt22.Employee;

public class Employee {


    private String name;
    private String email;

    private String phone;
    private String position;
    private String address;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;

    public Employee(String name, String email, String phone, String position, String address, int code) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.address = address;
        this.code = code;
    }

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}