package es.uvp.agarden;

/**
 * Created by Alberto on 06/09/14.
 */
public class ControlCode {

    private Integer id;
    private String name;
    private ControlNode controlNode;
    private Boolean state;

    public ControlCode() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ControlNode getControlNode() {
        return this.controlNode;
    }

    public void setControlNode(ControlNode controlNode) {
        this.controlNode = controlNode;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }


}
