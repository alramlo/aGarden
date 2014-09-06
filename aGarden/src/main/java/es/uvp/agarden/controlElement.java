package es.uvp.agarden;

/**
 * Created by Alberto on 06/09/14.
 */
public class controlElement {

    private Integer id;

    private String model;

    private String name;

    private String serial;

    private ControlNode controlNodeBean;

    private StatesControlElement statesControlElement;

    private Type typeBean;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public ControlNode getControlNodeBean() {
        return controlNodeBean;
    }

    public void setControlNodeBean(ControlNode controlNodeBean) {
        this.controlNodeBean = controlNodeBean;
    }

    public StatesControlElement getStatesControlElement() {
        return statesControlElement;
    }

    public void setStatesControlElement(StatesControlElement statesControlElement) {
        this.statesControlElement = statesControlElement;
    }

    public Type getTypeBean() {
        return typeBean;
    }

    public void setTypeBean(Type typeBean) {
        this.typeBean = typeBean;
    }
}
