package com.mycompany.mtg.objdb.Classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "carta_id")
public class Criatura extends Carta {

    @Column(nullable = false)
    private int forca;

    @Column(nullable = false)
    private int resistencia;

    @Column(nullable = false, length = 60)
    private String tipusCriatura;

    @Column(nullable = false)
    private boolean vola;

    protected Criatura() {
    }

    public Criatura(
            String nom,
            String descripcio,
            Raresa raresa,
            String edicio,
            CostMana cost,
            int forca,
            int resistencia,
            String tipusCriatura,
            boolean vola
    ) {
        super(nom, descripcio, raresa, edicio, cost);
        this.forca = forca;
        this.resistencia = resistencia;
        this.tipusCriatura = tipusCriatura;
        this.vola = vola;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public String getTipusCriatura() {
        return tipusCriatura;
    }

    public void setTipusCriatura(String tipusCriatura) {
        this.tipusCriatura = tipusCriatura;
    }

    public boolean isVola() {
        return vola;
    }

    public void setVola(boolean vola) {
        this.vola = vola;
    }
}
