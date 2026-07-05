package zoo;

import zoo.animal.Animal;
import zoo.animal.Bird;
import zoo.animal.Fish;
import zoo.animal.Mammal;
import zoo.animal.Reptile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Zoo {

    private static final Logger LOG = Logger.getLogger(Zoo.class.getName());

    private final List<Enclosure<? extends Animal>> enclosures = new ArrayList<>();

    public boolean addEnclosure(Enclosure<? extends Animal> enclosure) {
        LOG.info(() -> "addEnclosure aufgerufen mit Gehege: " + enclosure.getName());

        boolean nameAlreadyExists = enclosures.stream()
                .anyMatch(e -> e.getName().equals(enclosure.getName()));

        if (nameAlreadyExists) {
            LOG.warning(() -> "Gehege mit dem Namen " + enclosure.getName() + " existiert bereits.");
            return false;
        }

        boolean added = enclosures.add(enclosure);

        LOG.fine(() -> "Gehege hinzugefuegt. Anzahl Gehege: " + enclosures.size()
                + ", Anzahl Tiere: " + getAllAnimals().size());

        return added;
    }

    public List<Enclosure<? extends Animal>> getEnclosures() {
        LOG.info("getEnclosures aufgerufen");

        List<Enclosure<? extends Animal>> result = List.copyOf(enclosures);

        LOG.fine(() -> "getEnclosures liefert " + result.size() + " Gehege.");

        return result;
    }

    public Enclosure<? extends Animal> findEnclosureByName(String name) {
        LOG.info(() -> "findEnclosureByName aufgerufen mit Name: " + name);

        Enclosure<? extends Animal> result = enclosures.stream()
                .filter(enclosure -> enclosure.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (result == null) {
            LOG.warning(() -> "Kein Gehege mit dem Namen " + name + " gefunden.");
        } else {
            LOG.fine(() -> "Gehege gefunden: " + result.getName());
        }

        return result;
    }

    public Optional<Animal> findAnimalByName(String animalName) {
        Objects.requireNonNull(animalName);

        LOG.info(() -> "findAnimalByName aufgerufen mit Name: " + animalName);

        Optional<Animal> result = enclosures.stream()
                .flatMap(enclosure -> enclosure.findAnimalByName(animalName)
                        .stream()
                        .map(Animal.class::cast))
                .findFirst();

        if (result.isEmpty()) {
            LOG.warning(() -> "Kein Tier mit dem Namen " + animalName + " im Zoo gefunden.");
        } else {
            LOG.fine(() -> "Tier gefunden: " + result.get().name());
        }

        return result;
    }

    public List<Animal> getAllAnimals() {
        LOG.info("getAllAnimals aufgerufen");

        List<Animal> result = enclosures.stream()
                .flatMap(enclosure -> enclosure.getInhabitants().stream())
                .map(Animal.class::cast)
                .toList();

        LOG.fine(() -> "getAllAnimals liefert " + result.size() + " Tiere.");

        return result;
    }

    public List<Mammal> getAllMammals() {
        LOG.info("getAllMammals aufgerufen");

        List<Mammal> result = getAllAnimals().stream()
                .filter(Mammal.class::isInstance)
                .map(Mammal.class::cast)
                .toList();

        LOG.fine(() -> "getAllMammals liefert " + result.size() + " Saeugetiere.");

        return result;
    }

    public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
        LOG.info("getAnimalsByPredicate aufgerufen");

        List<Animal> result = getAllAnimals().stream()
                .filter(predicate)
                .toList();

        LOG.fine(() -> "getAnimalsByPredicate liefert " + result.size() + " Tiere.");

        return result;
    }

    public Map<Class<? extends Animal>, Long> countAnimalsByType() {
        LOG.info("countAnimalsByType aufgerufen");

        Map<Class<? extends Animal>, Long> result = getAllAnimals().stream()
                .collect(Collectors.groupingBy(
                        animal -> animal.getClass().asSubclass(Animal.class),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        LOG.fine(() -> "countAnimalsByType Ergebnis: " + result);

        return result;
    }

    public List<Enclosure<? extends Animal>> getOvercrowdedEnclosures(int maxAnimals) {
        LOG.info(() -> "getOvercrowdedEnclosures aufgerufen mit maxAnimals: " + maxAnimals);

        if (maxAnimals < 0) {
            LOG.severe("maxAnimals ist negativ. Das ist eine schwerwiegende Inkonsistenz.");
        }

        List<Enclosure<? extends Animal>> result = enclosures.stream()
                .filter(enclosure -> enclosure.size() > maxAnimals)
                .toList();

        LOG.fine(() -> "getOvercrowdedEnclosures liefert " + result.size() + " Gehege.");

        return result;
    }

    public String summary() {
        LOG.info("summary aufgerufen");

        int enclosureCount = enclosures.size();
        int animalCount = getAllAnimals().size();

        String groupedAnimals = getAllAnimals().stream()
                .collect(Collectors.groupingBy(
                        this::getMainAnimalGroup,
                        LinkedHashMap::new,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> entry.getValue() + " " + entry.getKey())
                .collect(Collectors.joining(", "));

        String result = "Zoo mit " + enclosureCount + " Gehegen und "
                + animalCount + " Tieren: " + groupedAnimals;

        LOG.fine(() -> "summary erstellt: " + result);

        return result;
    }

    public <T extends Animal> boolean addAnimalToEnclosure(Enclosure<T> enclosure, T animal) {
        LOG.info(() -> "addAnimalToEnclosure aufgerufen mit Tier: " + animal.name()
                + " und Gehege: " + enclosure.getName());

        if (!enclosures.contains(enclosure)) {
            LOG.warning(() -> "Gehege " + enclosure.getName() + " ist nicht im Zoo vorhanden.");
            return false;
        }

        boolean added = enclosure.add(animal);

        LOG.fine(() -> "Tier aufgenommen: " + added
                + ". Anzahl Tiere im Zoo: " + getAllAnimals().size());

        return added;
    }

    public <T extends Animal> boolean removeAnimalFromEnclosure(Enclosure<T> enclosure, T animal) {
        LOG.info(() -> "removeAnimalFromEnclosure aufgerufen mit Tier: " + animal.name()
                + " und Gehege: " + enclosure.getName());

        if (!enclosures.contains(enclosure)) {
            LOG.warning(() -> "Gehege " + enclosure.getName() + " ist nicht im Zoo vorhanden.");
            return false;
        }

        boolean removed = enclosure.remove(animal);

        if (!removed) {
            LOG.warning(() -> "Tier " + animal.name() + " wurde im Gehege "
                    + enclosure.getName() + " nicht gefunden.");
        }

        LOG.fine(() -> "Tier abgegeben: " + removed
                + ". Anzahl Tiere im Zoo: " + getAllAnimals().size());

        return removed;
    }

    public <T extends Animal> boolean moveAnimal(Enclosure<T> from, Enclosure<T> to, T animal) {
        LOG.info(() -> "moveAnimal aufgerufen mit Tier: " + animal.name()
                + " von " + from.getName() + " nach " + to.getName());

        if (!enclosures.contains(from) || !enclosures.contains(to)) {
            LOG.warning("Mindestens eines der Gehege ist nicht im Zoo vorhanden.");
            return false;
        }

        boolean removed = from.remove(animal);

        if (!removed) {
            LOG.warning(() -> "Tier " + animal.name() + " wurde im Ausgangsgehege nicht gefunden.");
            return false;
        }

        boolean added = to.add(animal);

        if (!added) {
            LOG.severe(() -> "Tier " + animal.name()
                    + " konnte nicht ins Zielgehege eingefuegt werden. Aktion wird rueckgaengig gemacht.");
            from.add(animal);
            return false;
        }

        LOG.fine(() -> "Tier erfolgreich umgesetzt. Anzahl Tiere im Zoo: " + getAllAnimals().size());

        return true;
    }

    private String getMainAnimalGroup(Animal animal) {
        if (animal instanceof Mammal) {
            return "Mammals";
        }

        if (animal instanceof Bird) {
            return "Birds";
        }

        if (animal instanceof Fish) {
            return "Fish";
        }

        if (animal instanceof Reptile) {
            return "Reptiles";
        }

        return "Other Animals";
    }

    static Logger getLogger() {
        return LOG;
    }
}