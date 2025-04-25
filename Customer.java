public class Customer {
    int customerId;
    String name;
    String address;

    Customer(int customerId, String name, String address) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
    }

    void display() {
        System.out.println("Customer: " + name + ", Address: " + address);
    }
}
