package com.example.mentalhealf;

import static org.junit.Assert.assertEquals;

import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingAllJUnit {

    @Test
    public void testDuplicateUsernameSimulation() {
        List<String> usernames = Arrays.asList("ali123", "testuser", "john");
        String newUsername = "ali123";

        boolean isTaken = usernames.contains(newUsername);
        assertEquals(true, isTaken);
    }
    @Test
    public void testEmptyEmailAndPassword() {
        String email = "";
        String password = "pass123";

        boolean valid = !email.isEmpty() && !password.isEmpty();
        assertEquals(false, valid);
    }

    @Test
    public void testMoodlogCreated() {
        Timestamp now = Timestamp.now();
        Moodlog log = new Moodlog("log123", 4, "Meditation", "Mindfulness", now);

        assertEquals("log123", log.getId());
        assertEquals(4, log.getFeeling());
        assertEquals("Meditation", log.getDescription());
        assertEquals("Mindfulness", log.getActivity());
        assertEquals(now, log.getTime());
    }

    @Test
    public void testEmptyDescriptionDefaults() {
        String description = "";
        if (description.isEmpty()) {
            description = "No description given.";
        }
        assertEquals("No description given.", description);
    }

    @Test
    public void testCalculateAverageMood() {
        List<Moodlog> logs = Arrays.asList(
                new Moodlog("1", 2, "", "", Timestamp.now()),
                new Moodlog("2", 5, "", "", Timestamp.now()),
                new Moodlog("3", 3, "", "", Timestamp.now())
        );
        float sum = 0;
        for (Moodlog m : logs) sum += m.getFeeling();
        float avg = sum / logs.size();

        assertEquals(3.33, avg, 0.01);
    }
    @Test
    public void testTopThreeActivitiesLikeTrends() {
        List<Moodlog> logs = Arrays.asList(
                new Moodlog("1", 3, "", "Walking", Timestamp.now()),
                new Moodlog("2", 4, "", "Yoga", Timestamp.now()),
                new Moodlog("3", 5, "", "Walking", Timestamp.now()),
                new Moodlog("4", 2, "", "Meditation", Timestamp.now()),
                new Moodlog("5", 3, "", "Walking", Timestamp.now()),
                new Moodlog("6", 4, "", "Yoga", Timestamp.now())
        );

        Map<String, Integer> activityAmount = new HashMap<>();
        for (Moodlog log : logs) {
            String activity = log.getActivity();
            if (activity != null && !activity.isEmpty()) {
                activityAmount.put(activity, activityAmount.getOrDefault(activity, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedActivities = new ArrayList<>(activityAmount.entrySet());
        sortedActivities.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        assertEquals("Walking", sortedActivities.get(0).getKey());
        assertEquals(3, (int) sortedActivities.get(0).getValue());

        assertEquals("Yoga", sortedActivities.get(1).getKey());
        assertEquals("Meditation", sortedActivities.get(2).getKey());
    }
}

