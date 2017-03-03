package canyons.fitness.models;

/**
 * Created by adi on 3/3/17.
 */
public class UserDetailsObject {
    public String mobile;
    public String age;
    public String sex;
    public String pushID;
    public String name;
    public String height;
    public String weight;
    public String BMI;

    public UserDetailsObject(){}
    public UserDetailsObject(String mobile, String age, String sex, String pushID,String name,  String height, String weight, String BMI){
        this.mobile = mobile;
        this.age = age;
        this.sex = sex;
        this.pushID= pushID;
        this.name= name;
        this.height = height;
        this.weight = weight;
        this.BMI = BMI;

    }

    public String getBMI() {
        return BMI;
    }


    public String getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }


    public void setHeight(String height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPushID() {
        return pushID;
    }

    public String getAge() {
        return age;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSex() {
        return sex;
    }
}
