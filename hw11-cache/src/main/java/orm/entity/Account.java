package orm.entity;

import orm.annotation.Id;

import java.math.BigDecimal;

public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

    public Account() {
    }

    public Account(long no, String type, BigDecimal rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public Account setNo(long no) {
        this.no = no;
        return this;
    }

    public String getType() {
        return type;
    }

    public Account setType(String type) {
        this.type = type;
        return this;
    }

    public BigDecimal getRest() {
        return rest;
    }

    public Account setRest(BigDecimal rest) {
        this.rest = rest;
        return this;
    }
}
