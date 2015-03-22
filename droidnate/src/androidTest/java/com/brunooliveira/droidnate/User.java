package com.brunooliveira.droidnate;

import com.brunooliveira.droidnate.annotations.CPF;
import com.brunooliveira.droidnate.annotations.Email;
import com.brunooliveira.droidnate.annotations.Entity;
import com.brunooliveira.droidnate.annotations.Future;
import com.brunooliveira.droidnate.annotations.Length;
import com.brunooliveira.droidnate.annotations.Mask;
import com.brunooliveira.droidnate.annotations.NotEmpty;
import com.brunooliveira.droidnate.annotations.NotNull;
import com.brunooliveira.droidnate.annotations.Past;
import com.brunooliveira.droidnate.annotations.Pattern;
import com.brunooliveira.droidnate.annotations.PrimaryKey;
import com.brunooliveira.droidnate.annotations.Validator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 22/03/2015.
 */
@Entity
public class User {

    @PrimaryKey(autoIncrement = true)
    private Long id;
    @CPF
    private String cpf;
    @Past
    private Calendar dateOfBirth;
    @Email
    private String email;
    @Future
    private Date endOfContract;
    @NotNull
    private String job;
    @Length(min = 5, max = 100)
    private String name;
    @Validator(validatorClass = NotZeroValidator.class)
    private int numOfFriends;
    @NotEmpty
    private String password;
    @Pattern(regex = "\\d{2} \\d{4}-\\d{4}")
    private String phone;
    @Mask(mask = "##.###.###-#")
    private String rg;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setEndOfContract(Date endOfContract) {
        this.endOfContract = endOfContract;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNumOfFriends(int numOfFriends) {
        this.numOfFriends = numOfFriends;
    }

}