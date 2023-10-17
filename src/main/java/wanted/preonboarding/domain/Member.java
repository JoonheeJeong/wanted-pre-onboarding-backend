package wanted.preonboarding.domain;

import jakarta.persistence.*;
import wanted.preonboarding.domain.base.AutoIdEntity;

@Entity
public class Member extends AutoIdEntity {

    @Column(insertable = false, updatable = false, nullable = false, length = 40)
    private String name;
    @Column(insertable = false, updatable = false, nullable = false, unique = true, length = 40)
    private String email;
}
