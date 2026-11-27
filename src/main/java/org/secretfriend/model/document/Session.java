package org.secretfriend.model.document;

import lombok.Getter;
import org.secretfriend.model.Participant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Getter
@Document(collection = "SESSION")
public class Session {

    @Id
    private String id;
    private List<Participant> participants;
    private boolean sorted;

    public Session() {
        participants = new ArrayList<>();
        sorted = false;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public boolean hasParticipant(String name) {
        return participants.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name));
    }

    public Optional<Participant> getParticipant(String name) {

        return participants.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void removeParticipant(Participant participant) {

        // Remove participant
        participants.remove(participant);

        // Remove participant as secret friend
        participants.stream()
                .filter(p -> p.getSecretFriend().equalsIgnoreCase(participant.getName()))
                .forEach(p -> p.setSecretFriend(null));
    }

    public void sort() {

        Random random = new Random();

        List<Participant> sortingList = new ArrayList<>(participants);
        participants.forEach(p -> {
            if (sortingList.isEmpty()) return;
            Participant randomParticipant = sortingList.get(random.nextInt(sortingList.size()));
            p.setSecretFriend(randomParticipant.getName());
            sortingList.remove(randomParticipant);
        });

        sorted = true;
    }
}
