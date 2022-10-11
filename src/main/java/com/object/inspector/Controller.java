/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.object.inspector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

/**
 *
 * @author Alex
 *
 *
 *///implements EventHandler<Event>
public class Controller {

    private final ObservableList<String> input = FXCollections.observableArrayList();

    private String aktuellerText = "";

    public ObservableList<String> getInput() {
        return input;
    }

    public void action(ActionEvent t) {

        Class<?> classObject = sucheClass(aktuellerText);
        if (classObject == null) {
            return;
        }

        input.clear();
        input.add("Name:            " + aktuellerText);

        // suche Typ-Kategorie:
        String typKategorie;
        if (classObject.isInterface()) {
            typKategorie = "Typ:                Interface";
        } else if (classObject.isPrimitive()) { // kann sein, dass das nicht funktioniert, weil forname keine Primitiven Typen nimmt.. irgendwie umgehen..
            typKategorie = "Typ:                Primitive";
        } else if (classObject.isArray()) {
            typKategorie = "Typ:                Array";
        } else {
            typKategorie = "Typ:                Klasse";
        }
        input.add(typKategorie);

        // suche Interfaces und collecte in String
        String interfaces = "Interfaces:      "
                + Arrays.stream(classObject.getInterfaces())//
                        .map(Class::getName) //
                        .collect(Collectors.joining(";  "));
        input.add(interfaces);

        // get Modifier
        String modifier = "Modifier:        " + Modifier.toString(classObject.getModifiers());
        input.add(modifier); // Interfaces haben den Modifier abstract UND interface. Abstrakte Klassen haben NUR abstract.

        // get Fields
        Field[] felder = classObject.getDeclaredFields(); // getDeclaredFields klappt nicht... warum???
        String feld = "Felder:  ";
        for (Field x : felder) {
            feld += "\n                     (" + Modifier.toString(x.getModifiers()) + " - " + x.getType() + ") ";
            feld += x.getName() + "  =  ";
            

        // AccessibleObject.setAccessible(felder, true); // geht nicht, wegen irgendwelchen moduls
            if (x.getModifiers() == 25) { try {
                // daher nur public..
                feld += x.get(this);
                } catch (IllegalAccessException | IllegalArgumentException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                feld += "?";
            }
        }
        input.add(feld);

        // Konstruktoren
        List<String> liste = Arrays.stream(classObject.getConstructors()).map((x) -> {
            return x.toString();
        }).collect(Collectors.toList());
        input.add("Konstruktoren:   ");
        liste.forEach(e -> input.add("                      " + e));

        // Methoden
        Method[] methoden = classObject.getDeclaredMethods(); // getDeclaredFields klappt nicht... warum???
        String methode = "Methoden:  ";
        for (Method x : methoden) {
            methode += "\n                  " +x.toString();
            
        }
        input.add(methode);

    }

    public void keyRelease(KeyEvent t) {
        TextField textfeld = null;
        if (t.getSource() instanceof TextField) {
            textfeld = (TextField) t.getSource();
            aktuellerText = textfeld.getText(); // nehme aktuelen Text aus Textfeld
        }
        textfeld.setStyle("-fx-background-color: rgb(144,238,144);");

        try { // prüfe ob Klasse gefunden wird
            Class.forName(aktuellerText);
        } catch (ClassNotFoundException e) {
            textfeld.setStyle("-fx-background-color: rgb(240, 128, 128);");
        }

        // sonderfall: primitive Typen
        switch (aktuellerText) {
            case "boolean":
            case "int":
            case "float":
            case "double":
            case "byte":
            case "char":
            case "short":
            case "long":
                textfeld.setStyle("-fx-background-color: rgb(144,238,144);");
        }
    }

    private Class sucheClass(String aktuellerText) {
        Class klasse = null;
        try {
            klasse = Class.forName(aktuellerText);
        } catch (ClassNotFoundException e) {
            switch (aktuellerText) {
                case "boolean":
                    klasse = boolean.class;
                    break;
                case "int":
                    klasse = int.class;
                    break;
                case "float":
                    klasse = float.class;
                    break;
                case "double":
                    klasse = double.class;
                    break;
                case "byte":
                    klasse = byte.class;
                    break;
                case "char":
                    klasse = char.class;
                    break;
                case "short":
                    klasse = short.class;
                    break;
                case "long":
                    klasse = long.class;
                    break;
                default:
                    System.out.println("Ungültiger Klasssenname. Bitte verwenden Sie den vollqualifizierten Namen.");
            }
        }
        return klasse;

    }
}
