package wanted.preonboarding.domain.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AutoIdAndCreatedAtAndUpdatedAtEntity extends AutoIdAndCreatedAtEntity {

    @UpdateTimestamp
    private Timestamp updatedAt;
}
