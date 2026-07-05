package zoo;

import zoo.animal.*;
import zoo.command.AddAnimalCommand;
import zoo.command.CommandManager;
import zoo.command.RemoveAnimalCommand;

import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Main {

    public static void main(String[] args) {
        configureLogging(Level.FINE);

        Zoo zoo = new Zoo();

        Aquarium<Fish> aquarium = new Aquarium<>("Grosses Aquarium");
        aquarium.add(new Trout("Forelle Franz"));
        aquarium.add(new Shark("Hai Harald"));

        Terrarium<Reptile> terrarium = new Terrarium<>("Wuesten-Terrarium");
        terrarium.add(new Snake("Schlange Susi"));
        terrarium.add(new Turtle("Schildkroete Theo"));

        MammalHouse<Mammal> mammalHouse = new MammalHouse<>("Saeugetierhaus");
        mammalHouse.add(new Dog("Bello"));
        mammalHouse.add(new Elephant("Dumbo"));
        mammalHouse.add(new Gorilla("Gorilla Gerd"));

        CatHouse catHouse = new CatHouse("Loewenhaus");
        catHouse.add(new Lion("Simba"));
        catHouse.add(new Lion("Mufasa"));

        zoo.addEnclosure(aquarium);
        zoo.addEnclosure(terrarium);
        zoo.addEnclosure(mammalHouse);
        zoo.addEnclosure(catHouse);

        Lion nala = new Lion("Nala");

        zoo.addAnimalToEnclosure(catHouse, nala);
        zoo.removeAnimalFromEnclosure(catHouse, nala);

        Lion scar = new Lion("Scar");
        catHouse.add(scar);

        CatHouse zweitesLoewenhaus = new CatHouse("Zweites Loewenhaus");
        zoo.addEnclosure(zweitesLoewenhaus);

        zoo.moveAnimal(catHouse, zweitesLoewenhaus, scar);

        System.out.println(zoo.summary());

        System.out.println("Alle Tiere:");
        zoo.getAllAnimals().forEach(animal -> System.out.println(animal.name()));

        System.out.println("Alle Saeugetiere:");
        zoo.getAllMammals().forEach(animal -> System.out.println(animal.name()));

        System.out.println("Ueberfuellte Gehege mit mehr als 2 Tieren:");
        zoo.getOvercrowdedEnclosures(2).forEach(System.out::println);

        System.out.println("Zaehlung nach konkretem Tiertyp:");
        System.out.println(zoo.countAnimalsByType());

        zoo.findEnclosureByName("Nicht vorhandenes Gehege");

        // Das hier waere ein Compilerfehler, weil CatHouse nur Lion erlaubt:
        // catHouse.add(new Tiger("Shir Khan"));


        // =====================================================
        // Aufgabe 1: Optional<T> testen
        // =====================================================

        System.out.println();
        System.out.println("----- Aufgabe 1: Optional testen -----");

        Optional<Lion> simbaImGehege = catHouse.findAnimalByName("Simba");

        if (simbaImGehege.isPresent()) {
            System.out.println("In CatHouse gefunden: " + simbaImGehege.get().name());
        } else {
            System.out.println("Simba wurde im CatHouse nicht gefunden.");
        }

        Optional<Animal> haiImZoo = zoo.findAnimalByName("Hai Harald");

        if (haiImZoo.isPresent()) {
            System.out.println("Im Zoo gefunden: " + haiImZoo.get().name());
        } else {
            System.out.println("Hai Harald wurde im Zoo nicht gefunden.");
        }

        Optional<Animal> nichtGefunden = zoo.findAnimalByName("NichtDa");

        if (nichtGefunden.isEmpty()) {
            System.out.println("NichtDa wurde korrekt nicht gefunden.");
        }


        // =====================================================
        // Aufgabe 2 und 3: Command, Undo, Redo und Result testen
        // =====================================================

        System.out.println();
        System.out.println("----- Aufgabe 2 und 3: Command testen -----");

        Lion leo = new Lion("Leo");

        AddAnimalCommand<Lion> addLeo = new AddAnimalCommand<>(leo);
        RemoveAnimalCommand<Lion> removeLeo = new RemoveAnimalCommand<>(leo);

        CommandManager<Enclosure<Mammal>> mammalManager = new CommandManager<>();

        System.out.println("Saeugetierhaus vorher: " + mammalHouse.size());

        mammalManager.executeCommand(addLeo, mammalHouse);
        System.out.println("Nach Add Leo: " + mammalHouse.size());

        mammalManager.undo(mammalHouse);
        System.out.println("Nach Undo Add Leo: " + mammalHouse.size());

        mammalManager.redo(mammalHouse);
        System.out.println("Nach Redo Add Leo: " + mammalHouse.size());

        mammalManager.executeCommand(removeLeo, mammalHouse);
        System.out.println("Nach Remove Leo: " + mammalHouse.size());

        mammalManager.undo(mammalHouse);
        System.out.println("Nach Undo Remove Leo: " + mammalHouse.size());

        mammalManager.redo(mammalHouse);
        System.out.println("Nach Redo Remove Leo: " + mammalHouse.size());


        // =====================================================
        // Compiler-Sicherheits-Test
        // =====================================================

        // Das hier soll NICHT kompilieren, weil Shark kein Mammal ist:
        // AddAnimalCommand<Shark> addNemo = new AddAnimalCommand<>(new Shark("Nemo"));
        // mammalManager.executeCommand(addNemo, mammalHouse);
    }

    private static void configureLogging(Level level) {
        Zoo.getLogger().setUseParentHandlers(false);
        Zoo.getLogger().setLevel(level);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        handler.setFormatter(new SimpleFormatter());

        Zoo.getLogger().addHandler(handler);
    }
}