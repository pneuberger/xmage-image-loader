package com.pneuberger.xmageimageloader.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("app")
public class MTGSetsConfiguration {

    private List<String> sets;

    public List<String> getSets() {
        return sets;
    }

    public String getSet(){
        if(sets.size()>0){
            return sets.remove(0);
        }
        return "";
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }
}
