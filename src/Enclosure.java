package zoo;

import zoo.animal.Animal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Enclosure<T extends Animal> {

    private final String name;
    private final Set<T> inhabitants = new LinkedHashSet<>();

    public Enclosure(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public boolean add(T animal) {
        return inhabitants.add(Objects.requireNonNull(animal));
    }

    public boolean remove(T animal) {
        return inhabitants.remove(animal);
    }

    public List<T> getInhabitants() {
        return List.copyOf(inhabitants);
    }

    public int size() {
        return inhabitants.size();
    }

    public Optional<T> findAnimalByName(String animalName) {
        Objects.requireNonNull(animalName);

        return inhabitants.stream()
                .filter(animal -> animal.name().equals(animalName))
                .findFirst();
    }

    @Override
    public String toString() {
        return name + " mit " + inhabitants.size() + " Tieren";
    }
}