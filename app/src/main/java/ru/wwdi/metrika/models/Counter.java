package ru.wwdi.metrika.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ru.wwdi.metrika.helpers.CounterColorsHelper;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
 * Created by dmitrykorotchenkov on 15/04/14.
 */

@Table(name = "Counter")
public class Counter extends Model implements Serializable {

    @Column(name = "counter_id")
    @Expose
    @SerializedName("id")
    private Long counterId;

    @Column(name = "site")
    @Expose
    @SerializedName("site")
    private String site;

    @Column(name = "codeStatus")
    @Expose
    @SerializedName("code_status")
    private String codeStatus;

    @Column(name = "permission")
    @Expose
    @SerializedName("permission")
    private String permission;

    @Column(name = "name")
    @Expose
    @SerializedName("name")
    private String name;

    @Column(name = "type")
    @Expose
    @SerializedName("type")
    private String type;

    @Column(name = "ownerLogin")
    @Expose
    @SerializedName("owner_login")
    private String ownerLogin;

    @Column(name = "visible")
    private Boolean visible;

    @Column(name = "color")
    private Integer color;

    @Column(name = "gradient_color")
    private Integer gradientColor;

    @Column(name = "response")
    private CounterListResponse countersList;

    public Counter(){
        super();

        if(color==null){
            int[] colors = CounterColorsHelper.getRandomColors();
            color = colors[0];
            gradientColor = colors[1];
        }
        if(visible==null) visible = true;
    }

    public void merge(Counter counter){
//        site = counter.getSite();
//        codeStatus = counter.getCodeStatus();
//        permission = counter.getPermission();
//        name = counter.getName();
//        type = counter.getType();
//        ownerLogin = counter.getOwnerLogin();
        color = counter.getColor();
        gradientColor = counter.getGradientColor();
        visible = counter.isVisible();

        save();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Integer getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(Integer gradientColor) {
        this.gradientColor = gradientColor;
    }

    public void setCountersList(CounterListResponse countersList) {
        this.countersList = countersList;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getColor() {
        return color;
    }

    public Long getCounterId() {
        return counterId;
    }

    public String getSite() {
        return site;
    }

    public String getCodeStatus() {
        return codeStatus;
    }

    public String getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public CounterListResponse getCountersList() {
        return countersList;
    }

}