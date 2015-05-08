package models;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import play.data.validation.ValidationError;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.i18n.Messages;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxmanski on 07.05.15.
 */
@Entity
public class User extends Model implements at.ac.tuwien.big.we15.lab2.api.User {

    public enum Gender{
        male, female
    }

    @Id
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    public String username;

    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    public String password;
    public String firstname;
    public String lastname;

    @Constraints.Pattern("\\d{1,2}\\.\\d{1,2}\\.(\\d{2,2}|\\d{4,4})")
    public String birthdate;
    public Gender gender;
    public String avatar;

    public User(){
        this(null, null, null, null, null, null, null);
    }

    public User(String username, String firstname, String lastname, String birthdate, Gender gender, String avatar, String password){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.avatar = avatar;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String getName() {
        return firstname + " " + lastname;
    }

    @Override
    public void setName(String s) {
        String[] names = s.split(" ");
        if(names.length >= 2){
            // deciding the first and last name(s)
            this.firstname = "";
            for(int i=0; i<(names.length-1); i++){
                this.firstname += names[i];
            }
            this.lastname = names[names.length-1];

        }else{
            this.firstname = names[0];
            this.lastname = "";
        }
    }

    @Override
    public Avatar getAvatar() {
        return Avatar.getAvatar(avatar);
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar.getId();
    }

    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if((other == null) || (!this.getClass().equals(other.getClass()))){
            return false;
        }

        User user = (User)other;
        if(((this.firstname == null) && (user.firstname != null)) ||
                ((this.lastname == null) && (user.lastname != null)) ||
                ((this.username == null) && (user.username != null)) ||
                ((this.gender == null) && (user.gender != null)) ||
                ((this.birthdate == null) && (user.birthdate != null)) ||
                ((this.password == null) && (user.password != null)) ||
                ((this.avatar == null) && (user.avatar != null))){

            return false;

        }
        if((this.avatar.equals(user.avatar)) && (this.username.equals(user.username)) &&
                (this.firstname.equals(user.firstname)) && (this.lastname.equals(user.lastname)) &&
                (this.birthdate.equals(user.birthdate)) && (this.gender.equals(user.gender)) &&
                (this.password.equals(user.password))){

            return true;
        }else{

            return false;
        }
    }

    public List<ValidationError> validate(){
        List<ValidationError> errors = new ArrayList<ValidationError>();

        try {
            new SimpleDateFormat("dd.MM.yyyy").parse(this.birthdate);
        }catch (ParseException e) {
            errors.add(new ValidationError("birthdate", Messages.get("error_birthday")));
        }

        if((this.username.length() < 4) || (this.username.length() > 8)){
            errors.add(new ValidationError("username", Messages.get("message_fieldRestrictionLengthUsername")));
        }

        if((this.password.length() < 4) || (this.password.length() > 8)){
            errors.add(new ValidationError("password", Messages.get("message_fieldRestrictionLengthPassword")));
        }

        return errors.isEmpty() ? null: errors;
    }

    public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    public static User authenticate(String username, String password){
        return find.where().eq("username", username).eq("password", password).findUnique();
    }

}
