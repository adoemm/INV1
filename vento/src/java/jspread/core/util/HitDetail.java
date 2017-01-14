/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jspread.core.util;

/**
 *
 * @author marco
 */
public final class HitDetail {

    private String base = "";
    private String carcater = "";
    private float porcentaje = 0;
    private String tipoDato = "";
    private int hits = 0;
    private int exactlyHits = 0;
    private int tipoHits = 0;
    private int diferentHits = 0;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getExactlyHits() {
        return exactlyHits;
    }

    public void setExactlyHits(int exactlyHits) {
        this.exactlyHits = exactlyHits;
    }

    public int getTipoHits() {
        return tipoHits;
    }

    public void setTipoHits(int tipoHits) {
        this.tipoHits = tipoHits;
    }

    public int getDiferentHits() {
        return diferentHits;
    }

    public void setDiferentHits(int diferentHits) {
        this.diferentHits = diferentHits;
    }

    public String getCarcater() {
        return carcater;
    }

    public void setCarcater(String carcater) {
        this.carcater = carcater;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDatoAdd) {
        if (!tipoDato.contains(tipoDatoAdd)) {
            tipoDato = tipoDato + tipoDatoAdd;
        }
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void addHit() {
        hits++;
    }

    public void addExactlyHit() {
        exactlyHits++;
    }

    public void addTipoHit() {
        tipoHits++;
    }

    public void addDiferentHit() {
        diferentHits++;
    }

    public int getPercentCalc(int muestra) {
        return hits * 100 / muestra;
    }

}
