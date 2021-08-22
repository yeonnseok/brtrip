package com.brtrip.path.domain

import com.brtrip.common.BaseEntity
import com.brtrip.path.domain.PathPlace
import javax.persistence.*

@Entity
class Path(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "like_count")
    var likeCount: Long = 0
): BaseEntity() {
    @OneToMany(mappedBy = "path", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var pathPlaces: MutableList<PathPlace> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            field.sortBy { it.sequence }
        }
}
