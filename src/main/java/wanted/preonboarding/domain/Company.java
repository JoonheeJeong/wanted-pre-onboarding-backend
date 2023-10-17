package wanted.preonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import wanted.preonboarding.domain.base.AutoIdEntity;

@Entity
public class Company extends AutoIdEntity {

    @Column(insertable = false, updatable = false, nullable = false, length = 40)
    private String name;
    @Embedded
    private Region region;
}
