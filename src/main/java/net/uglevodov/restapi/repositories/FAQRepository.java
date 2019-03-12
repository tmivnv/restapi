/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.FAQEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQEntry, Long> {
    Optional<List<FAQEntry>> findAllByCategory(Integer category);

    Optional<List<FAQEntry>> findAllByAnswerContainingIgnoreCaseOrQuestionContainingIgnoreCase(String string, String string2);
}
