package claha.android.com.showtimeremote;

public class ShowtimeButton {

    private int id;
    private String name;
    private String onClickAction;
    private String onLongClickAction;


    public ShowtimeButton(int id, String name, String onClickAction, String onLongClickAction) {
        init(id, name, onClickAction, onLongClickAction);
    }

    public ShowtimeButton(int id, String name, String onClickAction) {
        init(id, name, onClickAction, "");
    }

    public ShowtimeButton(int id, String name) {
        init(id, name, "", "");
    }

    public ShowtimeButton(int id) {
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
        return id == ((ShowtimeButton)o).getId();
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
