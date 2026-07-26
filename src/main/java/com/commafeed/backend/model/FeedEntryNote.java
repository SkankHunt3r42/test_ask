package com.commafeed.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FEEDENTRYNOTES")
@SuppressWarnings("serial")
@Getter
@Setter
public class FeedEntryNote extends AbstractModel {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "entry_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedEntry entry;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "rating")
    private Integer rating;
}
