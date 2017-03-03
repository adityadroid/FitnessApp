package canyons.fitness.models;

/**
 * Created by adi on 3/3/17.
 */
public class FriendObject {
    public String mobile;
    public String pushID;

    public FriendObject(){}
    public FriendObject(String mobile, String pushID){
        this.mobile = mobile;

        this.pushID= pushID;

    }


    public void setPushID(String pushID) {
        this.pushID = pushID;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPushID() {
        return pushID;
    }


    public String getMobile() {
        return mobile;
    }


}
