/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Bugy
 */
public class Realty  implements IRecord<Realty>, ISerializable{
    private int id;
    private int idInCadaster;
    private String cadsterName;
    private String desc; 

    public int getId() {
        return id;
    }

    public int getIdInCadaster() {
        return idInCadaster;
    }

    public String getCadsterName() {
        return cadsterName;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInCadaster(int idInCadaster) {
        this.idInCadaster = idInCadaster;
    }

    public void setCadsterName(String name) {
        this.cadsterName = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }  

    @Override
    public boolean equalsData(Realty data) {
        return this.id == data.id;
    }
}
