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
public class RealtyInCadaster implements IRecord<RealtyInCadaster>{
    private Realty realty;

    public RealtyInCadaster(Realty realty) {
        this.realty = realty;
    }

    public Realty getRealty() {
        return realty;
    }

    @Override
    public boolean equalsData(RealtyInCadaster realtyInCadaster) {
        return realty.getIdInCadaster() == realtyInCadaster.getRealty().getIdInCadaster() && realty.getCadsterName().equals(realtyInCadaster.getRealty().getCadsterName());
    }
}