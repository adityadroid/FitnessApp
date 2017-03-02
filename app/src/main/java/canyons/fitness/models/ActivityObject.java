package canyons.fitness.models;

/**
 * Created by adi on 3/3/17.
 */
public class ActivityObject {
    public String distance, steps, calories;
    public String date;
    public ActivityObject(String distance, String steps,String calories,String date){
        this.distance= distance;
        this.steps= steps;
        this.calories= calories;
        this.date= date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalories() {
        return calories;
    }

    public String getDistance() {
        return distance;
    }

    public String getSteps() {
        return steps;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
