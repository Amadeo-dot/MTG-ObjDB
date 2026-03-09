package com.mycompany.mtg.objdb.Classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "carta_id")
public class Terra extends Carta {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ColorMana produccio;

    @Column(nullable = false)
    private boolean esBasica;

    protected Terra() {
    }

    public Terra(
            String nom,
            String descripcio,
            Raresa raresa,
            String edicio,
            CostMana cost,
            ColorMana produccio,
            boolean esBasica
    ) {
        super(nom, descripcio, raresa, edicio, cost);
        this.produccio = produccio;
        this.esBasica = esBasica;
    }

    public ColorMana getProduccio() {
        return produccio;
    }

    public void setProduccio(ColorMana produccio) {
        this.produccio = produccio;
    }

    public boolean isEsBasica() {
        return esBasica;
    }

    public void setEsBasica(boolean esBasica) {
        this.esBasica = esBasica;
    }
}
