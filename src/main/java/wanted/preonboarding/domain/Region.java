package wanted.preonboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Region {

    @Column(insertable = false, updatable = false, nullable = false, length = 40)
    private String country;
    @Column(insertable = false, updatable = false, nullable = false, length = 40)
    private String city;
}
