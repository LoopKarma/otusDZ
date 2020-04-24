package orm.entity;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public PhoneDataSet(String number, User user) {
        this.number = number;
        this.user = user;
    }

    public PhoneDataSet() {
    }

    public String getNumber() {
        return number;
    }

    public PhoneDataSet setNumber(String number) {
        this.number = number;
        return this;
    }
}
