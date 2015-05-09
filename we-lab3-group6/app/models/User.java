package models;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import play.i18n.Messages;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User Model Class, used for persistence, etc.
 */
@Entity
public class User extends Model implements at.ac.tuwien.big.we15.lab2.api.User {

    public enum Gender{
        male, female
    }

    @Id
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String username;

    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String password;

    @Constraints.Pattern("\\d{1,2}\\.\\d{1,2}\\.(\\d{2,2}|\\d{4,4})")
    private String birthdate;

    private String firstname;
    private String lastname;
    private Gender gender;
    private String avatarid;

    public User(){
        this("", "", "", null, null, null, "");
    }

    public User(String username, String firstname, String lastname, String birthdate, Gender gender, String avatarid, String password){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.avatarid = avatarid;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getAvatarid(){
        return this.avatarid;
    }

    public void setAvatarid(String avatarid){
        this.avatarid = avatarid;
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
        return Avatar.getAvatar(this.avatarid);
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatarid = avatar.getId();
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
                ((this.avatarid == null) && (user.avatarid != null))){

            return false;

        }
        if((this.avatarid.equals(user.avatarid)) && (this.username.equals(user.username)) &&
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
            if((this.birthdate != null) && (!this.birthdate.isEmpty())){
                new SimpleDateFormat("dd.MM.yyyy").parse(this.birthdate);
            }
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
        if((username == null) || (password == null)){
            return null;
        }
        return find.where().eq("username", username).eq("password", password).findUnique();
    }

    @Override
    public String toString(){
        return "User \"" + username + "\": Name: " + firstname + " " + lastname + ", PW: " + password + ", BDay: " + birthdate + ", Gender: " + gender + ", Avatar: " + avatarid;
    }

}
