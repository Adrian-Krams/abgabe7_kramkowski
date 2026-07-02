package zoo;

import zoo.animal.*;

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