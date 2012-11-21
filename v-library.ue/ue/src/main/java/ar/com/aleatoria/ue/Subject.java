
package ar.com.aleatoria.ue;

import java.io.Serializable;


public class Subject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer careerId;

    private String name;

    public Subject() {
    }

    public Subject(Integer id, Integer careerId, String name) {
        super();
        this.id = id;
        this.careerId = careerId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCareerId() {
        return careerId;
    }

    public void setCareerId(Integer careerId) {
        this.careerId = careerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
