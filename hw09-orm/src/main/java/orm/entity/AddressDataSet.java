package orm.entity;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "street")
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public AddressDataSet setId(int id) {
        this.id = id;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public AddressDataSet setStreet(String street) {
        this.street = street;
        return this;
    }
}
