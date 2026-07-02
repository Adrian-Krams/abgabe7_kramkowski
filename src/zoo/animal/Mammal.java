package zoo.animal;

public sealed interface Mammal extends Animal permits Primate, Rodent, Cat, Dog, Elephant {
}