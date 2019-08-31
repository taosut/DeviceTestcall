package com.emailgw.master.CallingSim;
import javax.persistence.*;


import java.io.Serializable;

@Entity
@Table(name = "simcard")
public class SimCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "callingNumber")
    private String callingNumber;

    @Column(name = "imei")
    private String imei;

    public SimCard(String imei, String callingNumber){
        this.imei = imei;
        this.callingNumber = callingNumber;
    }
}
