package org.filipandrei.pandemic.model.entities;


public class PersonFactory {
    public static Person createPerson(Person.LifeStage stage, Person oldData){
        return switch (stage) {
            case Person.LifeStage.INFANT ->  new Infant(oldData);
            case CHILD -> new Child(oldData);
            case ADULT -> new Adult(oldData);
            case OLD -> new Old(oldData);
        };
    }
}
