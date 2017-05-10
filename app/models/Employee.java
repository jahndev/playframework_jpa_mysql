package models;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

    public String name;
    public Integer type;
    public String telephone;
    public String address;
    public Date employmentdate;
}
