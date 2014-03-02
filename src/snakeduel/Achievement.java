/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakeduel;

import java.util.ArrayList;

/**
 *
 * @author zhanglianghui
 */
public class Achievement {

    Achievement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getCurrentAchievementLabel(){
        String currentAchievement = "";
        
        for (AchievementBoundary ab : achievementBoundaries) {
            if (ab.getBoundary() > count) {
                break;
            } else {
                currentAchievement = ab.getLabel();
            }
        }
        return currentAchievement;
    }
    
    public Achievement(String type, int count, ArrayList<AchievementBoundary> achievementBoundaries){
        this.type = type;
        this.count = count;
        this.achievementBoundaries = achievementBoundaries;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String type = "";
    private int count;
    private ArrayList<AchievementBoundary> achievementBoundaries;
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    /**
     * @param count the count to add
     */
    public void addToCount(int count) {
        this.count += count;
    }
    
    /**
     * @return the achievementBoundaries
     */
    public ArrayList<AchievementBoundary> getAchievementBoundaries() {
        return achievementBoundaries;
    }
    
    /**
     * @param achievementBoundaries the achievementBoundaries to set
     */
    public void setAchievementBoundaries(ArrayList<AchievementBoundary> achievementBoundaries) {
        this.achievementBoundaries = achievementBoundaries;
    }
  
//</editor-fold>   
}
