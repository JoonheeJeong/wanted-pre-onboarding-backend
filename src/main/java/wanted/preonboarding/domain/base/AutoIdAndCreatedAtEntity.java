package wanted.preonboarding.domain.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AutoIdAndCreatedAtEntity extends AutoIdEntity {

    @CreationTimestamp
    private Timestamp createdAt;
}
