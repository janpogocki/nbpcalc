package com.wimir.piotrmomot.nbpcalc;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Waluty{

    public static NodeList parsedXML; // lista z xmlem
    public static Document doc;

    public Waluty() throws Exception {
        String urlNBP = "http://www.nbp.pl/kursy/xml/LastA.xml";
        URL url = new URL(urlNBP);
        URLConnection connection = url.openConnection();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(String.valueOf(url));
    }

    public static String getDate(){
        return doc.getElementsByTagName("data_publikacji").item(0).getTextContent();
    }

    public static double getKursSredni(String kodWaluty) throws Exception{
        String parsedXML2 = "";
        parsedXML = doc.getElementsByTagName("kod_waluty");

        for (int i=0; i<parsedXML.getLength(); i++){
            if (parsedXML.item(i).getTextContent().equals(kodWaluty)){
                parsedXML2 = doc.getElementsByTagName("kurs_sredni").item(i).getTextContent();
                break;
            }
        }

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(parsedXML2);
        return number.doubleValue();
    }

    public static double convert2PLN(double kwota, String kodWaluty) throws Exception{
        if (kodWaluty.equals("PLN"))
            return kwota;
        else
            return kwota*getKursSredni(kodWaluty);
    }

    public static double convertFromPLN(double kwota, String kodWaluty) throws Exception{
        if (kodWaluty.equals("PLN"))
            return kwota;
        else
            return kwota/getKursSredni(kodWaluty);
    }

    public static double convertX2Y(double x, String kodWalutyX, String kodWalutyY) throws Exception{
        // X=PLN, Y=?
        if (kodWalutyX.equals("PLN") && !kodWalutyY.equals("PLN"))
            return (double) Math.round(convertFromPLN(x, kodWalutyY) * 10000) / 10000;
        // X=?, Y=PLN
        else if (!kodWalutyX.equals("PLN") && kodWalutyY.equals("PLN"))
            return (double) Math.round(convert2PLN(x, kodWalutyX) * 10000) / 10000;
        // X=?, Y=?
        else{
            // najpierw na zlotego
            double toPLN = convert2PLN(x, kodWalutyX);
            // zwracamy w obcej
            return (double) Math.round(convertFromPLN(toPLN, kodWalutyY) * 10000) / 10000;
        }
    }
}
