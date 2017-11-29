package com.pneuberger.xmageimageloader.data;

import javafx.util.BuilderFactory;

import java.util.Set;

public class MTGSet {

    private String name;
    private Set<MTGCard> setCards;

    public MTGSet(String name, Set<MTGCard> setCards) {
        this.name = name;
        this.setCards = setCards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MTGCard> getSetCards() {
        return setCards;
    }

    public void setSetCards(Set<MTGCard> setCards) {
        this.setCards = setCards;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("MTGSet{" + name + ":\n");
        setCards.forEach(card -> builder.append(card.getName()+",\n"));
        builder.append("}");

        return builder.toString();
    }
}
