package com.mycompany.mtg.objdb.Classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "carta_id")
public class Encanteri extends Carta {

    @Column(nullable = false, length = 40)
    private String tipus;

    @Column(nullable = false)
    private boolean esInstantani;

    protected Encanteri() {
    }

    public Encanteri(
            String nom,
            String descripcio,
            Raresa raresa,
            String edicio,
            CostMana cost,
            String tipus,
            boolean esInstantani
    ) {
        super(nom, descripcio, raresa, edicio, cost);
        this.tipus = tipus;
        this.esInstantani = esInstantani;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public boolean isEsInstantani() {
        return esInstantani;
    }

    public void setEsInstantani(boolean esInstantani) {
        this.esInstantani = esInstantani;
    }
}
