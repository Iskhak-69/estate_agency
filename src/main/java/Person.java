public class Person {
    protected String username;
    protected String password;
    protected String accountType;


    public Person(String username, String password, String accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }
    public boolean authenticate(String username, String password, String accountType) {
        return this.username.equals(username) && this.password.equals(password) && this.accountType.equals(accountType);
    }

    void displayOptions() {

    }
}