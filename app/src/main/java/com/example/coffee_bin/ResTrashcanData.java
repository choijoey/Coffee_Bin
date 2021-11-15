package com.example.coffee_bin;

import com.google.gson.annotations.SerializedName;

public class ResTrashcanData {

    @SerializedName("name")
    String name;
    @SerializedName("plastic_amount")
    String plastic_amount;
    @SerializedName("paper_amount")
    String paper_amount;

    public ResTrashcanData(String name, String plastic_amount, String paper_amount) {
        this.name = name;
        this.plastic_amount = plastic_amount;
        this.paper_amount = paper_amount;
    }

    public String getName() {
        return name;
    }

    public String getPlastic_amount() {
        return plastic_amount;
    }

    public String getPaper_amount() {
        return paper_amount;
    }


}
