package orm.entity;

import orm.annotation.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @OrderColumn(name="id")
    private List<PhoneDataSet> phones = new ArrayList<>();

    public User() {
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public User setAddress(AddressDataSet address) {
        this.address = address;
        return this;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public User setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
        return this;
    }

    public User addPhone(PhoneDataSet phone) {
        this.phones.add(phone);
        return this;
    }
}
