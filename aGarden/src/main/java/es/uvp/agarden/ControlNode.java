package es.uvp.agarden;

/**
 * Created by Alberto on 06/09/14.
 */
public class ControlNode {

    private Integer id;

    private String ip;

    private String model;

    private String serial;


    public ControlNode() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

}
