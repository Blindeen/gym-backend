package project.gym.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;
import java.time.DayOfWeek;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Short durationMin;

    @ManyToMany
    private Set<Member> members;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Member trainer;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Activity activity) {
            return id == activity.getId();
        }

        return false;
    }
}
