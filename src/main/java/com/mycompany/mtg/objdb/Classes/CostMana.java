/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mtg.objdb.Classes;

/**
 *
 * @author alumne
 */
public class CostMana {
    
    int blanc;
    int blau;
    int negre;
    int vermell;
    int verd;
    int incolor;
    
    public CostMana() {}

    public CostMana(int blanc, int blau, int negre, int vermell, int verd, int incolor) {
        this.blanc = blanc;
        this.blau = blau;
        this.negre = negre;
        this.vermell = vermell;
        this.verd = verd;
        this.incolor = incolor;
    }

    public int getBlanc() {
        return blanc;
    }

    public void setBlanc(int blanc) {
        this.blanc = blanc;
    }

    public int getBlau() {
        return blau;
    }

    public void setBlau(int blau) {
        this.blau = blau;
    }

    public int getNegre() {
        return negre;
    }

    public void setNegre(int negre) {
        this.negre = negre;
    }

    public int getVermell() {
        return vermell;
    }

    public void setVermell(int vermell) {
        this.vermell = vermell;
    }

    public int getVerd() {
        return verd;
    }

    public void setVerd(int verd) {
        this.verd = verd;
    }

    public int getIncolor() {
        return incolor;
    }

    public void setIncolor(int incolor) {
        this.incolor = incolor;
    }
}
