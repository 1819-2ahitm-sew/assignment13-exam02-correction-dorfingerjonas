package at.htl.bank.business;

import at.htl.bank.model.BankKonto;
import at.htl.bank.model.GiroKonto;
import at.htl.bank.model.SparKonto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Legen Sie eine statische Liste "konten" an, in der Sie die einzelnen Konten speichern
 *
 */
public class Main {

    // die Konstanten sind package-scoped wegen der Unit-Tests
    static final double GEBUEHR = 0.02;
    static final double ZINSSATZ = 3.0;
    static List<BankKonto> konten = new ArrayList<BankKonto>();

    static final String KONTENDATEI = "erstellung.csv";
    static final String BUCHUNGSDATEI = "buchungen.csv";
    static final String ERGEBNISDATEI = "ergebnis.csv";


    /*
     * Führen Sie die drei Methoden erstelleKonten, fuehreBuchungenDurch und
     * findKontoPerName aus
     *
     * @param args
     */
    public static void main(String[] args) {
        erstelleKonten(KONTENDATEI);
        fuehreBuchungenDurch(BUCHUNGSDATEI);
        schreibeKontostandInDatei(ERGEBNISDATEI);
    }

    /*
     * Lesen Sie aus der Datei (erstellung.csv) die Konten ein.
     * Je nach Kontentyp erstellen Sie ein Spar- oder Girokonto.
     * Gebühr und Zinsen sind als Konstanten angegeben.
     * <p>
     * Nach dem Anlegen der Konten wird auf der Konsole folgendes ausgegeben:
     * Erstellung der Konten beendet
     *
     * @param KONTENDATEI
     */
    private static void erstelleKonten(final String KONTENDATEI) {

        String inputRead;
        String[] parts;
        int x = 0;
        String kontoTyp;
        String name;
        double startKapital;

        try (Scanner scanner = new Scanner(new FileReader(KONTENDATEI))) {
            while (scanner.hasNextLine()) {
                inputRead = scanner.nextLine();

                if (x >= 1) {
                    parts = inputRead.split(";");
                    kontoTyp = parts[0];
                    name = parts[1];
                    startKapital = Double.parseDouble(parts[2]);

                    if (kontoTyp.equals("Sparkonto")) {
                        konten.add(new SparKonto(name, startKapital, ZINSSATZ));
                    } else {
                        konten.add(new GiroKonto(name, startKapital, GEBUEHR));
                    }
                }
                x++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Erstellung der Konten beendet!");
    }

    /*
     * Die einzelnen Buchungen werden aus der Datei eingelesen.
     * Es wird aus der Liste "konten" jeweils das Bankkonto für
     * kontoVon und kontoNach gesucht.
     * Anschließend wird der Betrag vom kontoVon abgebucht und
     * der Betrag auf das kontoNach eingezahlt
     * <p>
     * Nach dem Durchführen der Buchungen wird auf der Konsole folgendes ausgegeben:
     * Buchung der Beträge beendet
     * <p>
     * Tipp: Verwenden Sie hier die Methode 'findeKontoPerName(String name)'
     *
     * @param BUCHUNGSDATEI
     */
    private static void fuehreBuchungenDurch(String BUCHUNGSDATEI) {

        String inputRead;
        String[] parts;
        String kontoVon;
        String kontoNach;
        double betrag;
        int x = 0;

        try (Scanner scanner = new Scanner(new FileReader(BUCHUNGSDATEI))) {
            while (scanner.hasNextLine()) {
                inputRead = scanner.nextLine();

                if (x >= 1) {

                    parts = inputRead.split(";");
                    kontoVon = parts[0];
                    kontoNach = parts[1];
                    betrag = Double.parseDouble(parts[2]);

                    findeKontoPerName(kontoVon).abheben(betrag);
                    findeKontoPerName(kontoNach).einzahlen(betrag);
                }
                x++;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("\nBuchung der Beträge beendet");
    }

    /*
     * Es werden die Kontostände sämtlicher Konten in die ERGEBNISDATEI
     * geschrieben. Davor werden bei Sparkonten noch die Zinsen dem Konto
     * gutgeschrieben
     * <p>
     * Die Datei sieht so aus:
     * <p>
     * name;kontotyp;kontostand
     * Susi;SparKonto;875.5
     * Mimi;GiroKonto;949.96
     * Hans;GiroKonto;1199.96
     * <p>
     * Vergessen Sie nicht die Überschriftenzeile
     * <p>
     * Nach dem Schreiben der Datei wird auf der Konsole folgendes ausgegeben:
     * Ausgabe in Ergebnisdatei beendet
     *
     * @param ERGEBNISDATEI
     */
    private static void schreibeKontostandInDatei(String ERGEBNISDATEI) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ERGEBNISDATEI))) {
            writer.println("name;kontotyp;kontostand");
            for (BankKonto konto : konten) {

                if (konto instanceof SparKonto)
                    ((SparKonto) konto).zinsenAnrechnen(ZINSSATZ);

                writer.println(konto);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
     * Durchsuchen Sie die Liste "konten" nach dem ersten Konto mit dem als Parameter
     * übergebenen Namen
     *
     * @param name
     * @return Bankkonto mit dem gewünschten Namen oder NULL, falls der Namen
     * nicht gefunden wird
     */
    public static BankKonto findeKontoPerName(String name) {
        for (BankKonto konto : konten) {
            if (konto.getName().equals(name)) {
                return konto;
            }
        }
        return null;
    }
}
