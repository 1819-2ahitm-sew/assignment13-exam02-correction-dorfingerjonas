package at.htl.bank.model;

public class BankKonto {

  private String name;
  private double kontoStand;

  public BankKonto(String name, double kontoStand) {
    this.name = name;
    this.kontoStand = kontoStand;
  }

  public BankKonto(String name) {
    this.name = name;
    this.kontoStand = 0;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getKontoStand() {
    return kontoStand;
  }

  public void einzahlen(double betrag) {
    this.kontoStand += betrag;
  }

  public void abheben(double betrag) {
    this.kontoStand -= betrag;
  }
}
