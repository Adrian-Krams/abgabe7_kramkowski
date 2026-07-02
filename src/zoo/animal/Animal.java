package zoo.animal;

public sealed interface Animal permits Mammal, Fish, Reptile, Bird, Jellyfish, Starfish {
    String name();
}