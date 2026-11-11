package org.secretfriend.model.document;

import lombok.Getter;
import org.secretfriend.model.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Document(collection = "SESSION")
public class Session {

    @Id
    private String id;
    private List<Person> participants;

    public Session() {
        participants = new ArrayList<>();
    }

    public void addParticipant(Person person) {
        participants.add(person);
    }

    public boolean hasParticipant(String name) {
        return participants.stream().anyMatch(p -> p.getName().contentEquals(name));
    }

    public void removeParticipant(String name) {

        // Remove participant
        participants.removeIf(p -> p.getName().contentEquals(name));

        // Remove participant as secret friend
        participants.stream()
                .filter(p -> p.getSecretFriend().contentEquals(name))
                .forEach(p -> p.setSecretFriend(null));
    }

    public void sort() {

        Random random = new Random();

        List<Person> sortingList = new ArrayList<>(participants);
        participants.forEach(p -> {
            if (sortingList.isEmpty()) return;
            Person randomPerson = sortingList.get(random.nextInt(sortingList.size()));
            p.setSecretFriend(randomPerson.getName());
            sortingList.remove(randomPerson);
        });
    }
}
