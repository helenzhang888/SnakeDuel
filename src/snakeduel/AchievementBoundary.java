/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakeduel;

/**
 *
 * @author zhanglianghui
 */
public class AchievementBoundary {
    
    public AchievementBoundary(int boundary, String label){
        this.boundary = boundary;
        this.label = label;
    }
    
    private int boundary;
    private String label = "";

    /**
     * @return the boundary
     */
    public int getBoundary() {
        return boundary;
    }

    /**
     * @param boundary the boundary to set
     */
    public void setBoundary(int boundary) {
        this.boundary = boundary;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
