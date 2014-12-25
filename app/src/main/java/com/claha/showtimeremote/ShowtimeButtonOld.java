package com.claha.showtimeremote;

public class ShowtimeButtonOld {

    private int id;
    private String name;
    private String onClickAction;
    private String onLongClickAction;


    public ShowtimeButtonOld(int id, String name, String onClickAction, String onLongClickAction) {
        init(id, name, onClickAction, onLongClickAction);
    }

    public ShowtimeButtonOld(int id, String name, String onClickAction) {
        init(id, name, onClickAction, "");
    }

    public ShowtimeButtonOld(int id, String name) {
        init(id, name, "", "");
    }

    public ShowtimeButtonOld(int id) {
        init(id, "", "", "");
    }

    private void init(int id, String name, String onClickAction, String onLongClickAction) {
        this.id = id;
        this.name = name;
        this.onClickAction = onClickAction;
        this.onLongClickAction = onLongClickAction;
    }

    @Override
    public boolean equals(Object o) {
        return id == ((ShowtimeButtonOld)o).getId();
    }

    @Override
    public int hashCode() {
        int hash = id;
        hash += name.hashCode();
        hash += onClickAction.hashCode();
        hash += onLongClickAction.hashCode();
        return hash;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOnClickAction() {
        return onClickAction;
    }

    public String getOnLongClickAction() {
        return onLongClickAction;
    }
}
