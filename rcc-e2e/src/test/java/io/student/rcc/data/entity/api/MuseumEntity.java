package io.student.rcc.data.entity.api;

import io.student.rcc.model.api.MuseumJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
public class MuseumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column
    private String description;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "geo_id", referencedColumnName = "id")
    private GeoEntity geo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MuseumEntity that = (MuseumEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }

    public static MuseumEntity fromJson(MuseumJson json) {
        MuseumEntity entity = new MuseumEntity();
        entity.setTitle(json.title());
        entity.setDescription(json.description());
        entity.setPhoto(json.photo() != null ? Base64.getDecoder().decode(json.photo()) : null);
        entity.setGeo(GeoEntity.fromJson(json.geo()));
        return entity;
    }
}
